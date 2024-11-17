 package com.izforge.izpack.panels.xstprocess;
 
 class ButtonConfig
 {
   private final String conditionid;
   
   private final boolean unlockPrev;
   
   private final boolean unlockNext;
   
   public ButtonConfig(String conditionid, boolean unlockPrev, boolean unlockNext)
   {
     this.conditionid = conditionid;
     this.unlockPrev = unlockPrev;
     this.unlockNext = unlockNext;
   }
 
   public boolean isUnlockPrev()
   {
     return this.unlockPrev;
   }
 
   public boolean isUnlockNext()
   {
     return this.unlockNext;
   }
 
   public String getConditionid()
   {
     return this.conditionid;
   }
 }

