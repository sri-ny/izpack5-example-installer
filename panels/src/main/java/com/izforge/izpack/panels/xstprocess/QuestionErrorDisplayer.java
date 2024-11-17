/*    */ package com.izforge.izpack.panels.xstprocess;
/*    */ 
/*    */ 
/*    */ public class QuestionErrorDisplayer
/*    */   implements Runnable
/*    */ {
/*    */   private AbstractUIProcessHandler uiHandler;
/*  8 */   private boolean toBeContinued = true;
/*    */   
/*    */   QuestionErrorDisplayer(AbstractUIProcessHandler uiHandler)
/*    */   {
/* 12 */     this.uiHandler = uiHandler;
/*    */   }
/*    */   
/*    */ 
/*    */   public void run()
/*    */   {
/* 18 */     if (this.uiHandler.askQuestion("Process execution failed", "Continue anyway?", 37, 47) == 49)
/*    */     {
/*    */ 
/*    */ 
/* 22 */       mustContinue(false);
/*    */     }
/*    */   }
/*    */   
/*    */   public synchronized boolean shouldContinue()
/*    */   {
/* 28 */     return this.toBeContinued;
/*    */   }
/*    */   
/*    */   public synchronized void mustContinue(boolean toBeContinued)
/*    */   {
/* 33 */     this.toBeContinued = toBeContinued;
/*    */   }
/*    */ }


/* Location:              D:\stage-installer-01-30-2023\postghost\installer\JCG-Xstore17-InstallerCopy.jar!\com\izforge\izpack\panels\xstprocess\QuestionErrorDisplayer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */