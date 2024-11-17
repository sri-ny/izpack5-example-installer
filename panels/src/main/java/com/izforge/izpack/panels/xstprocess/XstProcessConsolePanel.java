/*     */ package com.izforge.izpack.panels.xstprocess;
/*     */ 
/*     */ import com.izforge.izpack.api.data.InstallData;
/*     */ import com.izforge.izpack.api.handler.Prompt;
/*     */ import com.izforge.izpack.api.handler.Prompt.Type;
/*     */ import com.izforge.izpack.api.resource.Resources;
/*     */ import com.izforge.izpack.api.rules.RulesEngine;
/*     */ import com.izforge.izpack.core.handler.PromptUIHandler;
/*     */ import com.izforge.izpack.installer.console.AbstractConsolePanel;
/*     */ import com.izforge.izpack.installer.console.ConsolePanel;
/*     */ import com.izforge.izpack.installer.panel.PanelView;
/*     */ import com.izforge.izpack.util.Console;
/*     */ import com.izforge.izpack.util.PlatformModelMatcher;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Properties;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XstProcessConsolePanel
/*     */   extends AbstractConsolePanel
/*     */   implements ConsolePanel, AbstractUIProcessHandler
/*     */ {
/*     */   private RulesEngine rules;
/*     */   private Resources resources;
/*     */   private Prompt prompt;
/*     */   private PromptUIHandler handler;
/*     */   private final PlatformModelMatcher matcher;
/*  33 */   private int noOfJobs = 0;
/*     */   
/*  35 */   private int currentJob = 0;
/*     */   
/*     */ 
/*     */   public XstProcessConsolePanel(RulesEngine rules, Resources resources, Prompt prompt, PlatformModelMatcher matcher, PanelView<ConsolePanel> panel)
/*     */   {
/*  40 */     super(panel);
/*  41 */     this.rules = rules;
/*  42 */     this.resources = resources;
/*  43 */     this.prompt = prompt;
/*  44 */     this.handler = new PromptUIHandler(prompt);
/*  45 */     this.matcher = matcher;
/*     */   }
/*     */   
/*     */   public void emitNotification(String message)
/*     */   {
/*  50 */     this.handler.emitNotification(message);
/*     */   }
/*     */   
/*     */   public boolean emitWarning(String title, String message)
/*     */   {
/*  55 */     return this.handler.emitWarning(title, message);
/*     */   }
/*     */   
/*     */   public void emitError(String title, String message)
/*     */   {
/*  60 */     this.handler.emitError(title, message);
/*     */   }
/*     */   
/*     */   public void emitErrorAndBlockNext(String title, String message)
/*     */   {
/*  65 */     emitError(title, message);
/*     */   }
/*     */   
/*     */   public int askQuestion(String title, String question, int choices)
/*     */   {
/*  70 */     return this.handler.askQuestion(title, question, choices);
/*     */   }
/*     */   
/*     */   public int askQuestion(String title, String question, int choices, int default_choice)
/*     */   {
/*  75 */     return this.handler.askQuestion(title, question, choices, default_choice);
/*     */   }
/*     */   
/*     */ 
/*     */   public int askWarningQuestion(String title, String question, int choices, int default_choice)
/*     */   {
/*  81 */     return 0;
/*     */   }
/*     */   
/*     */   public void logOutput(String message, boolean stderr)
/*     */   {
/*  86 */     if (stderr)
/*     */     {
/*  88 */       this.prompt.message(Prompt.Type.ERROR, message);
/*     */     }
/*     */     else
/*     */     {
/*  92 */       this.prompt.message(Prompt.Type.INFORMATION, message);
/*     */     }
/*     */   }
/*     */   
/*     */   public void startProcessing(int no_of_processes)
/*     */   {
/*  98 */     logOutput("[ Starting processing ]", false);
/*  99 */     this.noOfJobs = no_of_processes;
/*     */   }
/*     */   
/*     */   public void startProcess(String name)
/*     */   {
/* 104 */     this.currentJob += 1;
/* 105 */     logOutput("Starting process " + name + " (" + Integer.toString(this.currentJob) + "/" + Integer.toString(this.noOfJobs) + ")", false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void finishProcess() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void finishProcessing(boolean unlockPrev, boolean unlockNext) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean generateOptions(InstallData installData, PrintWriter printWriter)
/*     */   {
/* 124 */     return false;
/*     */   }
/*     */   
/*     */   public boolean run(InstallData installData, Properties p)
/*     */   {
/* 129 */     return run(installData);
/*     */   }
/*     */   
/*     */   public boolean run(InstallData installData, Console console)
/*     */   {
/* 134 */     printHeadLine(installData, console);
/*     */     
/* 136 */     return run(installData);
/*     */   }
/*     */   
/*     */   private boolean run(InstallData installData)
/*     */   {
/* 141 */     ProcessPanelWorker worker = new ProcessPanelWorker(installData, this.rules, this.resources, this.matcher);
/* 142 */     worker.setHandler(this);
/* 143 */     worker.run();
/* 144 */     return worker.getResult();
/*     */   }
/*     */   
/*     */   public void skipProcess(String name, String message)
/*     */   {
/* 149 */     this.currentJob += 1;
/* 150 */     logOutput("Skipping process " + name + " (" + Integer.toString(this.currentJob) + "/" + Integer.toString(this.noOfJobs) + ")", false);
/*     */     
/* 152 */     logOutput("->Reason: " + message, false);
/*     */   }
/*     */ }


/* Location:              D:\stage-installer-01-30-2023\postghost\installer\JCG-Xstore17-InstallerCopy.jar!\com\izforge\izpack\panels\xstprocess\XstProcessConsolePanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */