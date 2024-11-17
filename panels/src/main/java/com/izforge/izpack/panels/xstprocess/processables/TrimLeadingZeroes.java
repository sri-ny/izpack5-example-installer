/*    */ package com.izforge.izpack.panels.xstprocess.processables;
/*    */ 
/*    */ import com.izforge.izpack.api.data.Variables;
/*    */ 
/*    */ public class TrimLeadingZeroes extends com.izforge.izpack.panels.xstprocess.Processable
/*    */ {
/*    */   String variableName;
/*    */   String variableValue;
/*  9 */   String strPattern = "^0+(?!$)";
/*    */   
/*    */   public TrimLeadingZeroes(String variableName, String variableValue) {
/* 12 */     this.variableName = variableName;
/* 13 */     this.variableValue = variableValue;
/*    */   }
/*    */   
/*    */   public boolean run()
/*    */   {
/* 18 */     this.variableValue = this.variables.replace(this.variableValue);
/*    */     
/*    */ 
/* 21 */     String newVariableValue = this.variableValue.replaceAll(this.strPattern, "");
/*    */     
/*    */ 
/* 24 */     this.variables.set(this.variableName, newVariableValue);
/*    */     
/* 26 */     this.handler.logOutput("Trimming zeros from " + this.variableValue + " , Result: " + newVariableValue, false);
/* 27 */     this.handler.logOutput("Setting " + newVariableValue + " in izpack variable " + this.variableName, false);
/*    */     
/* 29 */     return true;
/*    */   }
/*    */   
/*    */   public String getProcessableName()
/*    */   {
/* 34 */     return "TrimLeadingZeroes";
/*    */   }
/*    */ }


/* Location:              D:\stage-installer-01-30-2023\postghost\installer\JCG-Xstore17-InstallerCopy.jar!\com\izforge\izpack\panels\xstprocess\processables\TrimLeadingZeroes.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */