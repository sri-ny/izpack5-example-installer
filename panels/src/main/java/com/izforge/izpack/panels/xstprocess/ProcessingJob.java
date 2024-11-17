/*     */ package com.izforge.izpack.panels.xstprocess;
/*     */ 
/*     */ import com.izforge.izpack.api.data.Variables;
/*     */ import java.io.File;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ProcessingJob
/*     */ {
/*     */   protected String name;
/*     */   private List<Processable> processables;
/*  18 */   SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss z");
/*     */   
/*  20 */   String jobLog = "";
/*     */   
/*     */ 
/*  23 */   List<String> conditionIds = new ArrayList();
/*     */   
/*     */   public ProcessingJob(String name, List<Processable> processables, List<String> conditionIds, String jobLog)
/*     */   {
/*  27 */     this.name = name;
/*  28 */     this.processables = processables;
/*  29 */     this.jobLog = jobLog;
/*  30 */     this.conditionIds = conditionIds;
/*     */   }
/*     */   
/*     */   public List<String> getConditions() {
/*  34 */     return this.conditionIds;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean run(AbstractUIProcessHandler oldHandler, Variables variables)
/*     */   {
/*  42 */     this.jobLog = variables.replace(this.jobLog);
/*     */     AbstractUIProcessHandler handler;
/*  44 */     AbstractUIProcessHandler handler; if ((this.jobLog == null) || (this.jobLog.isEmpty())) {
/*  45 */       handler = oldHandler;
/*     */     } else {
/*  47 */       handler = new JobLogHandler(oldHandler, variables.replace(this.jobLog));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  52 */     for (Processable processable : this.processables)
/*     */     {
/*     */ 
/*     */ 
/*  56 */       handler.logOutput("\n<--------Start " + processable.getProcessableName() + " @ " + this.dateFormat.format(new Date()) + "---------->", false);
/*     */       
/*  58 */       boolean returnStatus = processable.run(handler, variables);
/*     */       
/*     */ 
/*  61 */       handler.logOutput("<--------End " + processable.getProcessableName() + " @ " + this.dateFormat.format(new Date()) + "---------->", false);
/*     */       
/*  63 */       if (!returnStatus) {
/*  64 */         handler.logOutput("******** Job Failure: " + this.name + " *********\n", false);
/*  65 */         return false;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  71 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   class JobLogHandler
/*     */     implements AbstractUIProcessHandler
/*     */   {
/*  79 */     AbstractUIProcessHandler oldHandler = null;
/*  80 */     PrintWriter jobLogWriter = null;
/*     */     
/*     */     public JobLogHandler(AbstractUIProcessHandler oldHandler, String jobLog) {
/*  83 */       this.oldHandler = oldHandler;
/*     */       
/*  85 */       if (!jobLog.equals("")) {
/*     */         try {
/*  87 */           jobLogFile = new File(jobLog);
/*  88 */           jobLogFile.getParentFile().mkdirs();
/*  89 */           this.jobLogWriter = new PrintWriter(jobLogFile);
/*     */         } catch (Exception e) { File jobLogFile;
/*  91 */           logOutput("Error creating job specific log, will continue....", true);
/*  92 */           this.jobLogWriter = null;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     public void writeToLog(String message) {
/*  98 */       if (this.jobLogWriter != null) {
/*  99 */         this.jobLogWriter.println(message);
/*     */       }
/*     */     }
/*     */     
/*     */     public void emitNotification(String message)
/*     */     {
/* 105 */       writeToLog("NOTIFICATION EMITTED: " + message);
/* 106 */       this.oldHandler.emitNotification(message);
/*     */     }
/*     */     
/*     */     public boolean emitWarning(String title, String message)
/*     */     {
/* 111 */       writeToLog("WARNING EMITTED: " + message);
/* 112 */       return this.oldHandler.emitWarning(title, message);
/*     */     }
/*     */     
/*     */     public void emitError(String title, String message)
/*     */     {
/* 117 */       writeToLog("ERROR EMITTED: " + message);
/* 118 */       this.oldHandler.emitError(title, message);
/*     */     }
/*     */     
/*     */ 
/*     */     public void emitErrorAndBlockNext(String title, String message)
/*     */     {
/* 124 */       writeToLog("ERROR EMITTED: " + message);
/* 125 */       this.oldHandler.emitErrorAndBlockNext(title, message);
/*     */     }
/*     */     
/*     */     public int askQuestion(String title, String question, int choices)
/*     */     {
/* 130 */       return this.oldHandler.askQuestion(title, question, choices);
/*     */     }
/*     */     
/*     */     public int askQuestion(String title, String question, int choices, int default_choice)
/*     */     {
/* 135 */       return this.oldHandler.askQuestion(title, question, choices, default_choice);
/*     */     }
/*     */     
/*     */     public int askWarningQuestion(String title, String question, int choices, int default_choice)
/*     */     {
/* 140 */       return this.oldHandler.askWarningQuestion(title, question, choices, default_choice);
/*     */     }
/*     */     
/*     */     public void logOutput(String message, boolean stderr)
/*     */     {
/*     */       try {
/* 146 */         if (this.jobLogWriter != null) {
/* 147 */           if (stderr) {
/* 148 */             this.jobLogWriter.println("ERR: " + message);
/*     */           } else
/* 150 */             this.jobLogWriter.println("LOG: " + message);
/*     */         }
/*     */       } catch (Exception e) {
/* 153 */         System.err.println("Error writing to job log file: " + ProcessingJob.this.jobLog);
/*     */       }
/*     */       
/* 156 */       this.oldHandler.logOutput(message, stderr);
/*     */     }
/*     */     
/*     */     public void startProcessing(int no_of_processes)
/*     */     {
/* 161 */       writeToLog("*** JOB STARTED ***");
/* 162 */       this.oldHandler.startProcessing(no_of_processes);
/*     */     }
/*     */     
/*     */     public void startProcess(String name)
/*     */     {
/* 167 */       writeToLog("*** STARTING PROCESS: " + name + "***");
/* 168 */       this.oldHandler.startProcess(name);
/*     */     }
/*     */     
/*     */ 
/*     */     public void finishProcess()
/*     */     {
/* 174 */       writeToLog("*** PROCESS ENDED ***");
/* 175 */       this.oldHandler.finishProcess();
/*     */     }
/*     */     
/*     */     public void finishProcessing(boolean unlockPrev, boolean unlockNext)
/*     */     {
/* 180 */       writeToLog("*** JOB ENDED ***");
/* 181 */       this.oldHandler.finishProcessing(unlockPrev, unlockNext);
/*     */       try
/*     */       {
/* 184 */         this.jobLogWriter.flush();
/* 185 */         this.jobLogWriter.close();
/*     */       }
/*     */       catch (Exception e) {}
/*     */     }
/*     */     
/*     */ 
/*     */     public void skipProcess(String name, String message)
/*     */     {
/* 193 */       writeToLog("Skipping: " + name + " - because: " + message);
/* 194 */       this.oldHandler.skipProcess(name, message);
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\stage-installer-01-30-2023\postghost\installer\JCG-Xstore17-InstallerCopy.jar!\com\izforge\izpack\panels\xstprocess\ProcessingJob.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */