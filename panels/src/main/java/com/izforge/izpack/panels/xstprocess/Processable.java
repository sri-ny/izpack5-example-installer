 package com.izforge.izpack.panels.xstprocess;
 
 import com.izforge.izpack.api.data.Variables;
 
 public abstract class Processable
 {
   protected boolean pauseExecution = false;
   
 
   protected AbstractUIProcessHandler handler;
   
 
   protected Variables variables;
   
 
   public final boolean run(AbstractUIProcessHandler handler, Variables variables)
   {
     this.handler = handler;
     this.variables = variables;
     
 
     boolean returnValue = run();
     
     if (returnValue) {
       handler.logOutput("Processable completed successfully!", false);
     } else {
       handler.logOutput("Processable completed with failure!", true);
     }
     
     if (this.pauseExecution) {
       handler.emitNotification("Execution paused because element was set on this processable.");
     }
     
     return returnValue;
   }
   
   public abstract boolean run();
   
   public abstract String getProcessableName();
   
   public boolean isPauseExecution()
   {
     return this.pauseExecution;
   }
   
   public void setPauseExecution(boolean pauseExecution) {
     this.pauseExecution = pauseExecution;
   }
 }


