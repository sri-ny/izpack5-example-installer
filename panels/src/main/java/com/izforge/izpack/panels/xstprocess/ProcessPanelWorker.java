 package com.izforge.izpack.panels.xstprocess;
 
 import com.izforge.izpack.api.adaptator.IXMLElement;
 import com.izforge.izpack.api.adaptator.IXMLParser;
 import com.izforge.izpack.api.adaptator.impl.XMLParser;
 import com.izforge.izpack.api.data.InstallData;
 import com.izforge.izpack.api.resource.Resources;
 import com.izforge.izpack.api.rules.Condition;
 import com.izforge.izpack.api.rules.RulesEngine;
 import com.izforge.izpack.panels.xstprocess.processables.ProcessableFactory;
 import com.izforge.izpack.panels.xstprocess.specprocessors.ConditionChecker;
 import com.izforge.izpack.panels.xstprocess.specprocessors.ConditionCheckerImpl;
 import com.izforge.izpack.util.IoHelper;
 import com.izforge.izpack.util.PlatformModelMatcher;
 import java.io.File;
 import java.io.FileOutputStream;
 import java.io.IOException;
 import java.io.InputStream;
 import java.io.PrintWriter;
 import java.text.SimpleDateFormat;
 import java.util.ArrayList;
 import java.util.Date;
 import java.util.HashMap;
 import java.util.List;
 import java.util.Map;
 import java.util.logging.Level;
 import java.util.logging.Logger;
 
 public class ProcessPanelWorker
   implements Runnable
 {
   public static final String SPEC_RESOURCE_NAME = "ProcessPanel.Spec.xml";
   private AbstractUIProcessHandler handler;
   private ArrayList<ProcessingJob> jobs = new ArrayList();
    
   private ArrayList<ProcessingJob> catchJobs = new ArrayList();
   
   private ArrayList<ProcessingJob> finalJobs = new ArrayList();
   
   private boolean result = true;
   
   private PrintWriter logfile = null;
   
   private String logfiledir = null;
   
   private final InstallData idata;
   
   private final Map<Boolean, List<ButtonConfig>> buttonConfigs = new HashMap();
    
   private final RulesEngine rules;
   
   private final Resources resources;
 
   private static final Logger logger = Logger.getLogger(ProcessPanelWorker.class.getName());
   
   private final ConditionChecker conditionChecker;
 
   private final ProcessableFactory processableFactory;
 
   public ProcessPanelWorker(InstallData installData, RulesEngine rules, Resources resources, PlatformModelMatcher matcher)
   {
     this.idata = installData;
     this.rules = rules;
     this.resources = resources; 
     this.conditionChecker = new ConditionCheckerImpl(installData, rules, matcher);      
     this.processableFactory = new ProcessableFactory();
   }
   
   public void setHandler(AbstractUIProcessHandler handler)
   {
     this.handler = handler;
   }
   
   private boolean readSpec()
     throws IOException
   {
     InputStream input;
     try
     {
       input = this.resources.getInputStream("ProcessPanel.Spec.xml");
     } catch (Exception e) {
       logger.log(Level.SEVERE, "Failed to read ProcessPanel.Spec.xml", e);
       return false;
     }
     
 
     IXMLParser parser = new XMLParser();
     IXMLElement spec;
     try {
       spec = parser.parse(input);
     } catch (Exception e) {
       logger.log(Level.SEVERE, "Failed to parse ProcessPanel.Spec.xml", e);
       return false;
     }
     
 
     if (!spec.hasChildren()) {
       return false;
     }
     
 
     IXMLElement logFileDirElement = spec.getFirstChildNamed("logfiledir");
     if (logFileDirElement != null)
     {
       this.logfiledir = logFileDirElement.getContent();
     }
     
 
     for (IXMLElement jobElement : spec.getChildrenNamed("job"))
     {
 
 
       String jobName = jobElement.getAttribute("name", "");
       String jobLog = jobElement.getAttribute("joblog", "");
       String conditionId = jobElement.getAttribute("condition", "");
       
 
       if (!this.conditionChecker.checkOS(jobElement)) {
         logger.info("OS condition not met for job: " + jobName);

       }
       else if (!this.conditionChecker.checkPacks(jobElement)) {
         logger.info("Packs condition not met for job: " + jobName);
       }
       else
       {
         logger.info("OS and Packs Conditions were met for job: " + jobName);
         
 
         ArrayList<Processable> processables = new ArrayList();
         
 
         for (IXMLElement actionElement : jobElement.getChildren()) {
           Processable processable = this.processableFactory.getProcessableForElement(actionElement);
           
 
           if (processable == null) {
             logger.severe("Processable factrory did not return a processable for action: " + actionElement.getName() + " for job: " + jobName);
           }
           else
           {
             logger.info("Adding processable " + actionElement.getName() + " for job: " + jobName);
             processables.add(processable);
           }
         }
         
         Boolean isCatch = Boolean.valueOf((jobElement.hasAttribute("catch")) && (Boolean.parseBoolean(jobElement.getAttribute("catch"))));
         Boolean isFinal = Boolean.valueOf((jobElement.hasAttribute("final")) && (Boolean.parseBoolean(jobElement.getAttribute("final"))));
         
         if (processables.isEmpty()) {
           logger.info("Nothing to do for job '" + jobName + "'");
         }
         else
         {
           ArrayList<String> conditions = new ArrayList();
           conditions.add(conditionId);
           
           ProcessingJob processingJob = new ProcessingJob(jobName, processables, conditions, jobLog);
           
           if (isCatch.booleanValue()) {
             this.catchJobs.add(processingJob);
           } else if (isFinal.booleanValue()) {
             this.finalJobs.add(processingJob);
           } else {
             logger.info("Adding job " + jobName + " to job list!");
             this.jobs.add(processingJob);
           }
         }
       }
     }
 
     this.buttonConfigs.put(Boolean.FALSE, new ArrayList());
     this.buttonConfigs.put(Boolean.TRUE, new ArrayList());
     
     for (IXMLElement onFailElement : spec.getChildrenNamed("onFail"))
     {
       String conditionid = onFailElement.hasAttribute("conditionid") ? onFailElement.getAttribute("conditionid") : onFailElement.hasAttribute("condition") ? onFailElement.getAttribute("condition") : null;
       
 
       boolean unlockPrev = onFailElement.hasAttribute("previous") ? Boolean.parseBoolean(onFailElement.getAttribute("previous")) : false;
       
       boolean unlockNext = onFailElement.hasAttribute("next") ? Boolean.parseBoolean(onFailElement.getAttribute("next")) : false;
       
       ((List)this.buttonConfigs.get(Boolean.FALSE)).add(new ButtonConfig(conditionid, unlockPrev, unlockNext));
     }
     for (IXMLElement onSuccessElement : spec.getChildrenNamed("onSuccess"))
     {
       String conditionid = onSuccessElement.hasAttribute("conditionid") ? onSuccessElement.getAttribute("conditionid") : onSuccessElement.hasAttribute("condition") ? onSuccessElement.getAttribute("condition") : null;
       
 
       boolean unlockPrev = onSuccessElement.hasAttribute("previous") ? Boolean.parseBoolean(onSuccessElement.getAttribute("previous")) : false;
       
       ((List)this.buttonConfigs.get(Boolean.TRUE)).add(new ButtonConfig(conditionid, unlockPrev, true));
     }
     
     return true;
   }
 
   public void run()
   {
     this.jobs.clear();
     
     try
     {
       if (!readSpec()) {
         logger.severe("Error parsing XML specification for processing.");
         return;
       }
     } catch (IOException ioe) {
       logger.severe("Error reading spec file! See resultant stack trace!");
       logger.severe(ioe.toString());
       return;
     }
     
 
 
     if (this.logfiledir != null)
     {
       this.logfiledir = IoHelper.translatePath(this.logfiledir, this.idata.getVariables());
       
       String appVersion = this.idata.getVariable("APP_VER");
       
       if (appVersion != null)
       {
         appVersion = "V" + appVersion;
       }
       else
       {
         appVersion = "undef";
       }
       
       String identifier = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
       
       identifier = appVersion.replace(' ', '_') + "_" + identifier;
       
       try
       {
         File tempLogFile = File.createTempFile("Install_" + identifier + "_", ".log", new File(this.logfiledir));
         
         this.logfile = new PrintWriter(new FileOutputStream(tempLogFile), true);
       }
       catch (IOException e)
       {
         logger.log(Level.WARNING, e.getMessage(), e);
       }
     }
      
     this.handler.startProcessing(this.jobs.size());
      
     for (ProcessingJob processingJob : this.jobs)
     {
 
       if (!this.conditionChecker.conditionsMet(processingJob.getConditions())) {
         logger.info("Dynamic Conditions not met for job, skipping: " + processingJob.name);
         this.handler.skipProcess(processingJob.name, "Dynamic conditions for Job were not met at this time");
       }
       else
       {
         logger.info("Dynamic Conditions were met for job: " + processingJob.name);
         
 
         this.result = runJob(processingJob);
         
         if (!this.result)
         {
           for (ProcessingJob catchJob : this.catchJobs) {
             runJob(catchJob);
           }
           
           int response = this.handler.askQuestion("Job Failed", "Continue?", 37);
           if (response == 49) {
             break;
           }
         }
       }
     }
     
     for (ProcessingJob finalJob : this.finalJobs)
     {
       runJob(finalJob);
     }
     
     boolean unlockNext = true;
     boolean unlockPrev = false;
      
     for (ButtonConfig buttonConfig : (List)this.buttonConfigs.get(Boolean.valueOf(this.result)))
     {
       String conditionid = buttonConfig.getConditionid();
       if ((conditionid != null) && (conditionid.length() > 0))
       {
         logger.info("Condition for job: " + conditionid);
         Condition cond = this.rules.getCondition(conditionid);
         if ((cond != null) && (!cond.isTrue()))
         {
           logger.info("Condition " + conditionid + " is not fulfilled");
           
           continue;
         }
       }
       
       unlockNext = buttonConfig.isUnlockNext();
       unlockPrev = buttonConfig.isUnlockPrev();
     }
     
     this.handler.finishProcessing(unlockPrev, unlockNext);
     if (this.logfile != null)
     {
       this.logfile.close();
     }
   }

   private boolean runJob(ProcessingJob job)
   {
     this.handler.startProcess(job.name);
     
     Boolean val = Boolean.valueOf(job.run(this.handler, this.idata.getVariables()));
     
     this.handler.finishProcess();
     
     return val.booleanValue();
   }
 
   public void startThread()
   {
     Thread processingThread = new Thread(this, "processing thread");
     
     processingThread.start();
   }

  public boolean getResult()
   {
     return this.result;
   }
 }

