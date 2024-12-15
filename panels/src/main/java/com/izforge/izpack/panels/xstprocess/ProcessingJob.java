package com.izforge.izpack.panels.xstprocess;

import com.izforge.izpack.api.data.Variables;
import java.io.File;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProcessingJob {
    protected String name;
    private List<Processable> processables;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss z");

    String jobLog = "";

    List<String> conditionIds = new ArrayList<>();

    public ProcessingJob(String name, List<Processable> processables, List<String> conditionIds, String jobLog) {
        this.name = name;
        this.processables = processables;
        this.jobLog = jobLog;
        this.conditionIds = conditionIds;
    }

    public List<String> getConditions() {
        return this.conditionIds;
    }

    public boolean run(AbstractUIProcessHandler oldHandler, Variables variables) {
        this.jobLog = variables.replace(this.jobLog);
        AbstractUIProcessHandler handler;
        if ((this.jobLog == null) || (this.jobLog.isEmpty())) {
            handler = oldHandler;
        } else {
            handler = new JobLogHandler(oldHandler, variables.replace(this.jobLog));
        }

        for (Processable processable : this.processables) {

            handler.logOutput("\n<--------Start " + processable.getProcessableName() + " @ " + this.dateFormat.format(new Date()) + "---------->", false);

            boolean returnStatus = processable.run(handler, variables);

            handler.logOutput("<--------End " + processable.getProcessableName() + " @ " + this.dateFormat.format(new Date()) + "---------->", false);

            if (!returnStatus) {
                handler.logOutput("******** Job Failure: " + this.name + " *********\n", false);
                return false;
            }
        }

        return true;
    }

    class JobLogHandler implements AbstractUIProcessHandler {
        AbstractUIProcessHandler oldHandler = null;
        PrintWriter jobLogWriter = null;

        public JobLogHandler(AbstractUIProcessHandler oldHandler, String jobLog) {
            this.oldHandler = oldHandler;

            if (!jobLog.equals("")) {
                File jobLogFile;
                try {
                    jobLogFile = new File(jobLog);
                    jobLogFile.getParentFile().mkdirs();
                    this.jobLogWriter = new PrintWriter(jobLogFile);
                } catch (Exception e) {
                    logOutput("Error creating job specific log, will continue....", true);
                    this.jobLogWriter = null;
                }
            }
        }

        public void writeToLog(String message) {
            if (this.jobLogWriter != null) {
                this.jobLogWriter.println(message);
            }
        }

        public void emitNotification(String message) {
            writeToLog("NOTIFICATION EMITTED: " + message);
            this.oldHandler.emitNotification(message);
        }

        public boolean emitWarning(String title, String message) {
            writeToLog("WARNING EMITTED: " + message);
            return this.oldHandler.emitWarning(title, message);
        }

        public void emitError(String title, String message) {
            writeToLog("ERROR EMITTED: " + message);
            this.oldHandler.emitError(title, message);
        }

        public void emitErrorAndBlockNext(String title, String message) {
            writeToLog("ERROR EMITTED: " + message);
            this.oldHandler.emitErrorAndBlockNext(title, message);
        }

        public int askQuestion(String title, String question, int choices) {
            return this.oldHandler.askQuestion(title, question, choices);
        }

        public int askQuestion(String title, String question, int choices, int default_choice) {
            return this.oldHandler.askQuestion(title, question, choices, default_choice);
        }

        public int askWarningQuestion(String title, String question, int choices, int default_choice) {
            return this.oldHandler.askWarningQuestion(title, question, choices, default_choice);
        }

        public void logOutput(String message, boolean stderr) {
            try {
                if (this.jobLogWriter != null) {
                    if (stderr) {
                        this.jobLogWriter.println("ERR: " + message);
                    } else
                        this.jobLogWriter.println("LOG: " + message);
                }
            } catch (Exception e) {
                System.err.println("Error writing to job log file: " + ProcessingJob.this.jobLog);
            }

            this.oldHandler.logOutput(message, stderr);
        }

        public void startProcessing(int no_of_processes) {
            writeToLog("*** JOB STARTED ***");
            this.oldHandler.startProcessing(no_of_processes);
        }

        public void startProcess(String name) {
            writeToLog("*** STARTING PROCESS: " + name + "***");
            this.oldHandler.startProcess(name);
        }

        public void finishProcess() {
            writeToLog("*** PROCESS ENDED ***");
            this.oldHandler.finishProcess();
        }

        public void finishProcessing(boolean unlockPrev, boolean unlockNext) {
            writeToLog("*** JOB ENDED ***");
            this.oldHandler.finishProcessing(unlockPrev, unlockNext);
            try {
                this.jobLogWriter.flush();
                this.jobLogWriter.close();
            } catch (Exception e) {
                // Handle exception
            }
        }

        public void skipProcess(String name, String message) {
            writeToLog("Skipping: " + name + " - because: " + message);
            this.oldHandler.skipProcess(name, message);
        }
    }
}
