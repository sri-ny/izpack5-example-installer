/*     */ package com.izforge.izpack.panels.xstprocess;
/*     */ 
/*     */ import com.izforge.izpack.api.adaptator.IXMLElement;
/*     */ import com.izforge.izpack.api.adaptator.IXMLParser;
/*     */ import com.izforge.izpack.api.adaptator.impl.XMLParser;
/*     */ import com.izforge.izpack.api.data.InstallData;
/*     */ import com.izforge.izpack.api.resource.Resources;
/*     */ import com.izforge.izpack.api.rules.Condition;
/*     */ import com.izforge.izpack.api.rules.RulesEngine;
/*     */ import com.izforge.izpack.panels.xstprocess.processables.ProcessableFactory;
/*     */ import com.izforge.izpack.panels.xstprocess.specprocessors.ConditionChecker;
/*     */ import com.izforge.izpack.panels.xstprocess.specprocessors.ConditionCheckerImpl;
/*     */ import com.izforge.izpack.util.IoHelper;
/*     */ import com.izforge.izpack.util.PlatformModelMatcher;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ProcessPanelWorker
/*     */   implements Runnable
/*     */ {
/*     */   public static final String SPEC_RESOURCE_NAME = "ProcessPanel.Spec.xml";
/*     */   private AbstractUIProcessHandler handler;
/*  55 */   private ArrayList<ProcessingJob> jobs = new ArrayList();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  60 */   private ArrayList<ProcessingJob> catchJobs = new ArrayList();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  65 */   private ArrayList<ProcessingJob> finalJobs = new ArrayList();
/*     */   
/*  67 */   private boolean result = true;
/*     */   
/*  69 */   private PrintWriter logfile = null;
/*     */   
/*  71 */   private String logfiledir = null;
/*     */   
/*     */   private final InstallData idata;
/*     */   
/*  75 */   private final Map<Boolean, List<ButtonConfig>> buttonConfigs = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final RulesEngine rules;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final Resources resources;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  91 */   private static final Logger logger = Logger.getLogger(ProcessPanelWorker.class.getName());
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final ConditionChecker conditionChecker;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final ProcessableFactory processableFactory;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ProcessPanelWorker(InstallData installData, RulesEngine rules, Resources resources, PlatformModelMatcher matcher)
/*     */   {
/* 108 */     this.idata = installData;
/* 109 */     this.rules = rules;
/* 110 */     this.resources = resources;
/*     */     
/*     */ 
/*     */ 
/* 114 */     this.conditionChecker = new ConditionCheckerImpl(installData, rules, matcher);
/*     */     
/*     */ 
/* 117 */     this.processableFactory = new ProcessableFactory();
/*     */   }
/*     */   
/*     */   public void setHandler(AbstractUIProcessHandler handler)
/*     */   {
/* 122 */     this.handler = handler;
/*     */   }
/*     */   
/*     */   private boolean readSpec()
/*     */     throws IOException
/*     */   {
/*     */     InputStream input;
/*     */     try
/*     */     {
/* 131 */       input = this.resources.getInputStream("ProcessPanel.Spec.xml");
/*     */     } catch (Exception e) {
/* 133 */       logger.log(Level.SEVERE, "Failed to read ProcessPanel.Spec.xml", e);
/* 134 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 138 */     IXMLParser parser = new XMLParser();
/*     */     IXMLElement spec;
/*     */     try {
/* 141 */       spec = parser.parse(input);
/*     */     } catch (Exception e) {
/* 143 */       logger.log(Level.SEVERE, "Failed to parse ProcessPanel.Spec.xml", e);
/* 144 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 148 */     if (!spec.hasChildren()) {
/* 149 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 153 */     IXMLElement logFileDirElement = spec.getFirstChildNamed("logfiledir");
/* 154 */     if (logFileDirElement != null)
/*     */     {
/* 156 */       this.logfiledir = logFileDirElement.getContent();
/*     */     }
/*     */     
/*     */ 
/* 160 */     for (IXMLElement jobElement : spec.getChildrenNamed("job"))
/*     */     {
/*     */ 
/*     */ 
/* 164 */       String jobName = jobElement.getAttribute("name", "");
/* 165 */       String jobLog = jobElement.getAttribute("joblog", "");
/* 166 */       String conditionId = jobElement.getAttribute("condition", "");
/*     */       
/*     */ 
/* 169 */       if (!this.conditionChecker.checkOS(jobElement)) {
/* 170 */         logger.info("OS condition not met for job: " + jobName);
/*     */ 
/*     */ 
/*     */ 
/*     */       }
/* 175 */       else if (!this.conditionChecker.checkPacks(jobElement)) {
/* 176 */         logger.info("Packs condition not met for job: " + jobName);
/*     */       }
/*     */       else
/*     */       {
/* 180 */         logger.info("OS and Packs Conditions were met for job: " + jobName);
/*     */         
/*     */ 
/* 183 */         ArrayList<Processable> processables = new ArrayList();
/*     */         
/*     */ 
/* 186 */         for (IXMLElement actionElement : jobElement.getChildren()) {
/* 187 */           Processable processable = this.processableFactory.getProcessableForElement(actionElement);
/*     */           
/*     */ 
/* 190 */           if (processable == null) {
/* 191 */             logger.severe("Processable factrory did not return a processable for action: " + actionElement.getName() + " for job: " + jobName);
/*     */           }
/*     */           else
/*     */           {
/* 195 */             logger.info("Adding processable " + actionElement.getName() + " for job: " + jobName);
/*     */             
/*     */ 
/* 198 */             processables.add(processable);
/*     */           }
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 204 */         Boolean isCatch = Boolean.valueOf((jobElement.hasAttribute("catch")) && (Boolean.parseBoolean(jobElement.getAttribute("catch"))));
/* 205 */         Boolean isFinal = Boolean.valueOf((jobElement.hasAttribute("final")) && (Boolean.parseBoolean(jobElement.getAttribute("final"))));
/*     */         
/* 207 */         if (processables.isEmpty()) {
/* 208 */           logger.info("Nothing to do for job '" + jobName + "'");
/*     */         }
/*     */         else
/*     */         {
/* 212 */           ArrayList<String> conditions = new ArrayList();
/* 213 */           conditions.add(conditionId);
/*     */           
/* 215 */           ProcessingJob processingJob = new ProcessingJob(jobName, processables, conditions, jobLog);
/*     */           
/* 217 */           if (isCatch.booleanValue()) {
/* 218 */             this.catchJobs.add(processingJob);
/* 219 */           } else if (isFinal.booleanValue()) {
/* 220 */             this.finalJobs.add(processingJob);
/*     */           } else {
/* 222 */             logger.info("Adding job " + jobName + " to job list!");
/* 223 */             this.jobs.add(processingJob);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 231 */     this.buttonConfigs.put(Boolean.FALSE, new ArrayList());
/* 232 */     this.buttonConfigs.put(Boolean.TRUE, new ArrayList());
/*     */     
/* 234 */     for (IXMLElement onFailElement : spec.getChildrenNamed("onFail"))
/*     */     {
/* 236 */       String conditionid = onFailElement.hasAttribute("conditionid") ? onFailElement.getAttribute("conditionid") : onFailElement.hasAttribute("condition") ? onFailElement.getAttribute("condition") : null;
/*     */       
/*     */ 
/* 239 */       boolean unlockPrev = onFailElement.hasAttribute("previous") ? Boolean.parseBoolean(onFailElement.getAttribute("previous")) : false;
/*     */       
/* 241 */       boolean unlockNext = onFailElement.hasAttribute("next") ? Boolean.parseBoolean(onFailElement.getAttribute("next")) : false;
/*     */       
/* 243 */       ((List)this.buttonConfigs.get(Boolean.FALSE)).add(new ButtonConfig(conditionid, unlockPrev, unlockNext));
/*     */     }
/* 245 */     for (IXMLElement onSuccessElement : spec.getChildrenNamed("onSuccess"))
/*     */     {
/* 247 */       String conditionid = onSuccessElement.hasAttribute("conditionid") ? onSuccessElement.getAttribute("conditionid") : onSuccessElement.hasAttribute("condition") ? onSuccessElement.getAttribute("condition") : null;
/*     */       
/*     */ 
/* 250 */       boolean unlockPrev = onSuccessElement.hasAttribute("previous") ? Boolean.parseBoolean(onSuccessElement.getAttribute("previous")) : false;
/*     */       
/* 252 */       ((List)this.buttonConfigs.get(Boolean.TRUE)).add(new ButtonConfig(conditionid, unlockPrev, true));
/*     */     }
/*     */     
/* 255 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void run()
/*     */   {
/* 267 */     this.jobs.clear();
/*     */     
/*     */     try
/*     */     {
/* 271 */       if (!readSpec()) {
/* 272 */         logger.severe("Error parsing XML specification for processing.");
/* 273 */         return;
/*     */       }
/*     */     } catch (IOException ioe) {
/* 276 */       logger.severe("Error reading spec file! See resultant stack trace!");
/* 277 */       logger.severe(ioe.toString());
/* 278 */       return;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 283 */     if (this.logfiledir != null)
/*     */     {
/* 285 */       this.logfiledir = IoHelper.translatePath(this.logfiledir, this.idata.getVariables());
/*     */       
/* 287 */       String appVersion = this.idata.getVariable("APP_VER");
/*     */       
/* 289 */       if (appVersion != null)
/*     */       {
/* 291 */         appVersion = "V" + appVersion;
/*     */       }
/*     */       else
/*     */       {
/* 295 */         appVersion = "undef";
/*     */       }
/*     */       
/* 298 */       String identifier = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
/*     */       
/* 300 */       identifier = appVersion.replace(' ', '_') + "_" + identifier;
/*     */       
/*     */       try
/*     */       {
/* 304 */         File tempLogFile = File.createTempFile("Install_" + identifier + "_", ".log", new File(this.logfiledir));
/*     */         
/* 306 */         this.logfile = new PrintWriter(new FileOutputStream(tempLogFile), true);
/*     */       }
/*     */       catch (IOException e)
/*     */       {
/* 310 */         logger.log(Level.WARNING, e.getMessage(), e);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 317 */     this.handler.startProcessing(this.jobs.size());
/*     */     
/*     */ 
/*     */ 
/* 321 */     for (ProcessingJob processingJob : this.jobs)
/*     */     {
/*     */ 
/* 324 */       if (!this.conditionChecker.conditionsMet(processingJob.getConditions())) {
/* 325 */         logger.info("Dynamic Conditions not met for job, skipping: " + processingJob.name);
/* 326 */         this.handler.skipProcess(processingJob.name, "Dynamic conditions for Job were not met at this time");
/*     */       }
/*     */       else
/*     */       {
/* 330 */         logger.info("Dynamic Conditions were met for job: " + processingJob.name);
/*     */         
/*     */ 
/* 333 */         this.result = runJob(processingJob);
/*     */         
/* 335 */         if (!this.result)
/*     */         {
/* 337 */           for (ProcessingJob catchJob : this.catchJobs) {
/* 338 */             runJob(catchJob);
/*     */           }
/*     */           
/* 341 */           int response = this.handler.askQuestion("Job Failed", "Continue?", 37);
/* 342 */           if (response == 49) {
/*     */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 355 */     for (ProcessingJob finalJob : this.finalJobs)
/*     */     {
/* 357 */       runJob(finalJob);
/*     */     }
/*     */     
/* 360 */     boolean unlockNext = true;
/* 361 */     boolean unlockPrev = false;
/*     */     
/*     */ 
/* 364 */     for (ButtonConfig buttonConfig : (List)this.buttonConfigs.get(Boolean.valueOf(this.result)))
/*     */     {
/* 366 */       String conditionid = buttonConfig.getConditionid();
/* 367 */       if ((conditionid != null) && (conditionid.length() > 0))
/*     */       {
/* 369 */         logger.info("Condition for job: " + conditionid);
/* 370 */         Condition cond = this.rules.getCondition(conditionid);
/* 371 */         if ((cond != null) && (!cond.isTrue()))
/*     */         {
/* 373 */           logger.info("Condition " + conditionid + " is not fulfilled");
/*     */           
/* 375 */           continue;
/*     */         }
/*     */       }
/*     */       
/* 379 */       unlockNext = buttonConfig.isUnlockNext();
/* 380 */       unlockPrev = buttonConfig.isUnlockPrev();
/*     */     }
/*     */     
/*     */ 
/* 384 */     this.handler.finishProcessing(unlockPrev, unlockNext);
/* 385 */     if (this.logfile != null)
/*     */     {
/* 387 */       this.logfile.close();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean runJob(ProcessingJob job)
/*     */   {
/* 400 */     this.handler.startProcess(job.name);
/*     */     
/* 402 */     Boolean val = Boolean.valueOf(job.run(this.handler, this.idata.getVariables()));
/*     */     
/* 404 */     this.handler.finishProcess();
/*     */     
/* 406 */     return val.booleanValue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void startThread()
/*     */   {
/* 414 */     Thread processingThread = new Thread(this, "processing thread");
/*     */     
/* 416 */     processingThread.start();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getResult()
/*     */   {
/* 426 */     return this.result;
/*     */   }
/*     */ }


/* Location:              D:\stage-installer-01-30-2023\postghost\installer\JCG-Xstore17-InstallerCopy.jar!\com\izforge\izpack\panels\xstprocess\ProcessPanelWorker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */