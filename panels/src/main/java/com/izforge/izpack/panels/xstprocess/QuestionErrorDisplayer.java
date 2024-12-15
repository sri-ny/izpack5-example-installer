 package com.izforge.izpack.panels.xstprocess;
 
 
 public class QuestionErrorDisplayer
   implements Runnable
 {
   private AbstractUIProcessHandler uiHandler;
   private boolean toBeContinued = true;
   
   QuestionErrorDisplayer(AbstractUIProcessHandler uiHandler)
   {
     this.uiHandler = uiHandler;
   }
   
 
   public void run()
   {
     if (this.uiHandler.askQuestion("Process execution failed", "Continue anyway?", 37, 47) == 49)
     {
 
 
       mustContinue(false);
     }
   }
   
   public synchronized boolean shouldContinue()
   {
     return this.toBeContinued;
   }
   
   public synchronized void mustContinue(boolean toBeContinued)
   {
     this.toBeContinued = toBeContinued;
   }
 }
