 package com.izforge.izpack.panels.xstprocess;
 
 import com.izforge.izpack.api.data.InstallData;
 import com.izforge.izpack.api.handler.Prompt;
 import com.izforge.izpack.api.handler.Prompt.Type;
 import com.izforge.izpack.api.resource.Resources;
 import com.izforge.izpack.api.rules.RulesEngine;
 import com.izforge.izpack.core.handler.PromptUIHandler;
 import com.izforge.izpack.installer.console.AbstractConsolePanel;
 import com.izforge.izpack.installer.console.ConsolePanel;
 import com.izforge.izpack.installer.panel.PanelView;
 import com.izforge.izpack.util.Console;
 import com.izforge.izpack.util.PlatformModelMatcher;
 import java.io.PrintWriter;
 import java.util.Properties;
 
 public class XstProcessConsolePanel
   extends AbstractConsolePanel
   implements ConsolePanel, AbstractUIProcessHandler
 {
   private RulesEngine rules;
   private Resources resources;
   private Prompt prompt;
   private PromptUIHandler handler;
   private final PlatformModelMatcher matcher;
   private int noOfJobs = 0;
   
   private int currentJob = 0;
    
   public XstProcessConsolePanel(RulesEngine rules, Resources resources, Prompt prompt, PlatformModelMatcher matcher, PanelView<ConsolePanel> panel)
   {
     super(panel);
     this.rules = rules;
     this.resources = resources;
     this.prompt = prompt;
     this.handler = new PromptUIHandler(prompt);
     this.matcher = matcher;
   }
   
   public void emitNotification(String message)
   {
     this.handler.emitNotification(message);
   }
   
   public boolean emitWarning(String title, String message)
   {
     return this.handler.emitWarning(title, message);
   }
   
   public void emitError(String title, String message)
   {
     this.handler.emitError(title, message);
   }
   
   public void emitErrorAndBlockNext(String title, String message)
   {
     emitError(title, message);
   }
   
   public int askQuestion(String title, String question, int choices)
   {
     return this.handler.askQuestion(title, question, choices);
   }
   
   public int askQuestion(String title, String question, int choices, int default_choice)
   {
     return this.handler.askQuestion(title, question, choices, default_choice);
   }
    
   public int askWarningQuestion(String title, String question, int choices, int default_choice)
   {
     return 0;
   }
   
   public void logOutput(String message, boolean stderr)
   {
     if (stderr)
     {
       this.prompt.message(Prompt.Type.ERROR, message);
     }
     else
     {
       this.prompt.message(Prompt.Type.INFORMATION, message);
     }
   }
   
   public void startProcessing(int no_of_processes)
   {
     logOutput("[ Starting processing ]", false);
     this.noOfJobs = no_of_processes;
   }
   
   public void startProcess(String name)
   {
     this.currentJob += 1;
     logOutput("Starting process " + name + " (" + Integer.toString(this.currentJob) + "/" + Integer.toString(this.noOfJobs) + ")", false);
   }
   
   public void finishProcess() {}
   
   public void finishProcessing(boolean unlockPrev, boolean unlockNext) {}
    
   public boolean generateOptions(InstallData installData, PrintWriter printWriter)
   {
     return false;
   }
   
   public boolean run(InstallData installData, Properties p)
   {
     return run(installData);
   }
   
   public boolean run(InstallData installData, Console console)
   {
     printHeadLine(installData, console);
     
     return run(installData);
   }
   
   private boolean run(InstallData installData)
   {
     ProcessPanelWorker worker = new ProcessPanelWorker(installData, this.rules, this.resources, this.matcher);
     worker.setHandler(this);
     worker.run();
     return worker.getResult();
   }
   
   public void skipProcess(String name, String message)
   {
     this.currentJob += 1;
     logOutput("Skipping process " + name + " (" + Integer.toString(this.currentJob) + "/" + Integer.toString(this.noOfJobs) + ")", false);
     
     logOutput("->Reason: " + message, false);
   }
 }
