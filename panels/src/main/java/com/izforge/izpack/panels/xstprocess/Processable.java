/*    */ package com.izforge.izpack.panels.xstprocess;
/*    */ 
/*    */ import com.izforge.izpack.api.data.Variables;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class Processable
/*    */ {
/* 14 */   protected boolean pauseExecution = false;
/*    */   
/*    */ 
/*    */   protected AbstractUIProcessHandler handler;
/*    */   
/*    */ 
/*    */   protected Variables variables;
/*    */   
/*    */ 
/*    */   public final boolean run(AbstractUIProcessHandler handler, Variables variables)
/*    */   {
/* 25 */     this.handler = handler;
/* 26 */     this.variables = variables;
/*    */     
/*    */ 
/* 29 */     boolean returnValue = run();
/*    */     
/* 31 */     if (returnValue) {
/* 32 */       handler.logOutput("Processable completed successfully!", false);
/*    */     } else {
/* 34 */       handler.logOutput("Processable completed with failure!", true);
/*    */     }
/*    */     
/* 37 */     if (this.pauseExecution) {
/* 38 */       handler.emitNotification("Execution paused because element was set on this processable.");
/*    */     }
/*    */     
/* 41 */     return returnValue;
/*    */   }
/*    */   
/*    */   public abstract boolean run();
/*    */   
/*    */   public abstract String getProcessableName();
/*    */   
/*    */   public boolean isPauseExecution()
/*    */   {
/* 50 */     return this.pauseExecution;
/*    */   }
/*    */   
/*    */   public void setPauseExecution(boolean pauseExecution) {
/* 54 */     this.pauseExecution = pauseExecution;
/*    */   }
/*    */ }


/* Location:              D:\stage-installer-01-30-2023\postghost\installer\JCG-Xstore17-InstallerCopy.jar!\com\izforge\izpack\panels\xstprocess\Processable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */