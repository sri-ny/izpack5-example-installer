 package com.izforge.izpack.panels.xstprocess;
 
 import com.izforge.izpack.api.adaptator.IXMLElement;
 import com.izforge.izpack.api.config.Options;
 import com.izforge.izpack.api.data.InstallData;
 import com.izforge.izpack.api.exception.InstallerException;
 import com.izforge.izpack.api.resource.Resources;
 import com.izforge.izpack.api.rules.RulesEngine;
 import com.izforge.izpack.installer.automation.PanelAutomation;
 import com.izforge.izpack.installer.automation.PanelAutomationHelper;
 import com.izforge.izpack.util.PlatformModelMatcher;
 import java.io.PrintStream;
 
 public class ProcessPanelAutomation
   extends PanelAutomationHelper
   implements PanelAutomation, AbstractUIProcessHandler
 {
   private int noOfJobs = 0;
   
   private int currentJob = 0;
    
   private ProcessPanelWorker processPanelWorker;
    
   public ProcessPanelAutomation(InstallData installData, RulesEngine rules, Resources resources, PlatformModelMatcher matcher)
   {
     this.processPanelWorker = new ProcessPanelWorker(installData, rules, resources, matcher);
     this.processPanelWorker.setHandler(this);
   }
    
   public void createInstallationRecord(InstallData idata, IXMLElement panelRoot) {}
 
   public void runAutomated(InstallData idata, IXMLElement panelRoot)
     throws InstallerException
   {
     this.processPanelWorker.run();
     if (!this.processPanelWorker.getResult())
     {
       throw new InstallerException("The work done by the ProcessPanel (line " + panelRoot.getLineNr() + ") failed");
     }
   }
   
 
   public void processOptions(InstallData installData, Options options) {}
   
   public void logOutput(String message, boolean stderr)
   {
     if (stderr)
     {
       System.err.println(message);
     }
     else
     {
       System.out.println(message);
     }
   }
    
   public void startProcessing(int noOfJobs)
   {
     System.out.println("[ Starting processing ]");
     this.noOfJobs = noOfJobs;
   }
    
   public void finishProcessing(boolean unlockPrev, boolean unlockNext)
   {
     System.out.println("[ Processing finished ]");
   }
    
   public void startProcess(String name)
   {
     this.currentJob += 1;
     System.out.println("Starting process " + name + " (" + this.currentJob + "/" + this.noOfJobs + ")");
   }
    
   public void finishProcess() {}
    
   public void skipProcess(String name, String message)
   {
     this.currentJob += 1;
   }
 }
