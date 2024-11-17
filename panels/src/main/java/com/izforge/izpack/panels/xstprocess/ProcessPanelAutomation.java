/*     */ package com.izforge.izpack.panels.xstprocess;
/*     */ 
/*     */ import com.izforge.izpack.api.adaptator.IXMLElement;
/*     */ import com.izforge.izpack.api.config.Options;
/*     */ import com.izforge.izpack.api.data.InstallData;
/*     */ import com.izforge.izpack.api.exception.InstallerException;
/*     */ import com.izforge.izpack.api.resource.Resources;
/*     */ import com.izforge.izpack.api.rules.RulesEngine;
/*     */ import com.izforge.izpack.installer.automation.PanelAutomation;
/*     */ import com.izforge.izpack.installer.automation.PanelAutomationHelper;
/*     */ import com.izforge.izpack.util.PlatformModelMatcher;
/*     */ import java.io.PrintStream;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ProcessPanelAutomation
/*     */   extends PanelAutomationHelper
/*     */   implements PanelAutomation, AbstractUIProcessHandler
/*     */ {
/*  43 */   private int noOfJobs = 0;
/*     */   
/*  45 */   private int currentJob = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private ProcessPanelWorker processPanelWorker;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ProcessPanelAutomation(InstallData installData, RulesEngine rules, Resources resources, PlatformModelMatcher matcher)
/*     */   {
/*  59 */     this.processPanelWorker = new ProcessPanelWorker(installData, rules, resources, matcher);
/*  60 */     this.processPanelWorker.setHandler(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void createInstallationRecord(InstallData idata, IXMLElement panelRoot) {}
/*     */   
/*     */ 
/*     */ 
/*     */   public void runAutomated(InstallData idata, IXMLElement panelRoot)
/*     */     throws InstallerException
/*     */   {
/*  72 */     this.processPanelWorker.run();
/*  73 */     if (!this.processPanelWorker.getResult())
/*     */     {
/*  75 */       throw new InstallerException("The work done by the ProcessPanel (line " + panelRoot.getLineNr() + ") failed");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void processOptions(InstallData installData, Options options) {}
/*     */   
/*     */ 
/*     */   public void logOutput(String message, boolean stderr)
/*     */   {
/*  85 */     if (stderr)
/*     */     {
/*  87 */       System.err.println(message);
/*     */     }
/*     */     else
/*     */     {
/*  91 */       System.out.println(message);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void startProcessing(int noOfJobs)
/*     */   {
/* 102 */     System.out.println("[ Starting processing ]");
/* 103 */     this.noOfJobs = noOfJobs;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void finishProcessing(boolean unlockPrev, boolean unlockNext)
/*     */   {
/* 112 */     System.out.println("[ Processing finished ]");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void startProcess(String name)
/*     */   {
/* 120 */     this.currentJob += 1;
/* 121 */     System.out.println("Starting process " + name + " (" + this.currentJob + "/" + this.noOfJobs + ")");
/*     */   }
/*     */   
/*     */ 
/*     */   public void finishProcess() {}
/*     */   
/*     */ 
/*     */   public void skipProcess(String name, String message)
/*     */   {
/* 130 */     this.currentJob += 1;
/*     */   }
/*     */ }


/* Location:              D:\stage-installer-01-30-2023\postghost\installer\JCG-Xstore17-InstallerCopy.jar!\com\izforge\izpack\panels\xstprocess\ProcessPanelAutomation.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */