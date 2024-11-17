/*    */ package com.izforge.izpack.panels.xstprocess.processables;
/*    */ 
/*    */ import com.izforge.izpack.panels.xstprocess.AbstractUIProcessHandler;
/*    */ 
/*    */ public class WriteRegistryValue extends com.izforge.izpack.panels.xstprocess.Processable { String regPath;
/*    */   String regKey;
/*    */   String regValue;
/*    */   RegValueType regValueType;
/*    */   
/* 10 */   public static enum RegValueType { REG_BINARY, 
/* 11 */     REG_DWORD, 
/* 12 */     REG_EXPAND_SZ, 
/* 13 */     REG_MULTI_SZ, 
/* 14 */     REG_QWORD, 
/* 15 */     REG_SZ;
/*    */     
/*    */ 
/*    */     private RegValueType() {}
/*    */   }
/*    */   
/*    */ 
/*    */   public WriteRegistryValue(String regPath, String regKey, String regValue, RegValueType regValueType)
/*    */   {
/* 24 */     this.regPath = regPath;
/* 25 */     this.regKey = regKey;
/* 26 */     this.regValue = regValue;
/* 27 */     this.regValueType = regValueType;
/*    */   }
/*    */   
/*    */   public boolean run()
/*    */   {
/* 32 */     this.regPath = this.variables.replace(this.regPath);
/* 33 */     this.regValue = this.variables.replace(this.regValue);
/*    */     
/*    */ 
/* 36 */     this.handler.logOutput("\nSetting: " + this.regPath + " " + this.regKey + " to value: " + this.regValue, false);
/*    */     
/*    */ 
/* 39 */     ProcessBuilder systemPathPb = new ProcessBuilder(new String[] { "reg.exe", "add", this.regPath, "/v", this.regKey, "/t", this.regValueType.toString(), "/d", this.regValue, "/f" });
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 51 */     systemPathPb.redirectErrorStream(false);
/*    */     Process setReg;
/*    */     try
/*    */     {
/* 55 */       setReg = systemPathPb.start();
/*    */     } catch (java.io.IOException e) {
/* 57 */       this.handler.logOutput("Error setting registry value.", true);
/* 58 */       e.printStackTrace();
/* 59 */       return false;
/*    */     }
/*    */     try
/*    */     {
/* 63 */       setReg.waitFor();
/*    */     }
/*    */     catch (InterruptedException e) {
/* 66 */       e.printStackTrace();
/*    */     }
/*    */     
/* 69 */     int returnVal = setReg.exitValue();
/* 70 */     if (returnVal != 0) {
/* 71 */       this.handler.logOutput("Command failed, returned value: " + returnVal, true);
/* 72 */       return false;
/*    */     }
/*    */     
/* 75 */     this.handler.logOutput("Success setting value.\n", false);
/* 76 */     return true;
/*    */   }
/*    */   
/*    */ 
/*    */   public String getProcessableName()
/*    */   {
/* 82 */     return "Write Registry Value";
/*    */   }
/*    */ }


/* Location:              D:\stage-installer-01-30-2023\postghost\installer\JCG-Xstore17-InstallerCopy.jar!\com\izforge\izpack\panels\xstprocess\processables\WriteRegistryValue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */