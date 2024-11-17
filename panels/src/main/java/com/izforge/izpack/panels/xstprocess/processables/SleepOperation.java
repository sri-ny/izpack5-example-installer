/*    */ package com.izforge.izpack.panels.xstprocess.processables;
/*    */ 
/*    */ import com.izforge.izpack.panels.xstprocess.Processable;
/*    */ 
/*    */ public class SleepOperation extends Processable
/*    */ {
/*    */   int sleepMs;
/*    */   
/*    */   public SleepOperation(int sleepMs) {
/* 10 */     this.sleepMs = sleepMs;
/*    */   }
/*    */   
/*    */   public boolean run()
/*    */   {
/*    */     try {
/* 16 */       Thread.sleep(this.sleepMs);
/*    */     }
/*    */     catch (Exception ex) {}
/*    */     
/*    */ 
/* 21 */     return true;
/*    */   }
/*    */   
/*    */   public String getProcessableName()
/*    */   {
/* 26 */     return "Sleep operation: " + this.sleepMs + "ms";
/*    */   }
/*    */ }


/* Location:              D:\stage-installer-01-30-2023\postghost\installer\JCG-Xstore17-InstallerCopy.jar!\com\izforge\izpack\panels\xstprocess\processables\SleepOperation.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */