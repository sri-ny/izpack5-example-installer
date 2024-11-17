/*    */ package com.izforge.izpack.panels.xstprocess;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class ButtonConfig
/*    */ {
/*    */   private final String conditionid;
/*    */   
/*    */ 
/*    */ 
/*    */   private final boolean unlockPrev;
/*    */   
/*    */ 
/*    */   private final boolean unlockNext;
/*    */   
/*    */ 
/*    */ 
/*    */   public ButtonConfig(String conditionid, boolean unlockPrev, boolean unlockNext)
/*    */   {
/* 21 */     this.conditionid = conditionid;
/* 22 */     this.unlockPrev = unlockPrev;
/* 23 */     this.unlockNext = unlockNext;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean isUnlockPrev()
/*    */   {
/* 31 */     return this.unlockPrev;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean isUnlockNext()
/*    */   {
/* 39 */     return this.unlockNext;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getConditionid()
/*    */   {
/* 48 */     return this.conditionid;
/*    */   }
/*    */ }


/* Location:              D:\stage-installer-01-30-2023\postghost\installer\JCG-Xstore17-InstallerCopy.jar!\com\izforge\izpack\panels\xstprocess\ButtonConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */