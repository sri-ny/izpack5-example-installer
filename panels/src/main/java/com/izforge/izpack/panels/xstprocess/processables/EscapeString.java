/*    */ package com.izforge.izpack.panels.xstprocess.processables;
/*    */ 
/*    */ import com.izforge.izpack.api.data.Variables;
/*    */ 
/*    */ public class EscapeString extends com.izforge.izpack.panels.xstprocess.Processable
/*    */ {
/*  7 */   String characterToEscape = "";
/*  8 */   String escapeWith = "";
/*  9 */   String escapeString = "";
/* 10 */   String setVarible = "";
/*    */   
/*    */   public EscapeString(String characterToEscape, String escapeWith, String escapeString, String setVariable)
/*    */   {
/* 14 */     this.characterToEscape = characterToEscape;
/* 15 */     this.escapeWith = escapeWith;
/* 16 */     this.escapeString = escapeString;
/* 17 */     this.setVarible = setVariable;
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean run()
/*    */   {
/* 23 */     this.characterToEscape = this.variables.replace(this.characterToEscape);
/* 24 */     this.escapeWith = this.variables.replace(this.escapeWith);
/* 25 */     this.escapeString = this.variables.replace(this.escapeString);
/* 26 */     this.setVarible = this.variables.replace(this.setVarible);
/*    */     
/*    */ 
/* 29 */     this.handler.logOutput("Character escaping: " + this.characterToEscape, false);
/* 30 */     this.handler.logOutput("Escaping with: " + this.escapeWith, false);
/* 31 */     this.handler.logOutput("String escaping: " + this.escapeString, false);
/* 32 */     this.handler.logOutput("Setting final result in variable: " + this.setVarible, false);
/*    */     
/*    */ 
/* 35 */     String finalString = "";
/* 36 */     finalString = this.escapeString.replaceAll(this.characterToEscape, this.escapeWith + this.characterToEscape);
/*    */     
/* 38 */     this.handler.logOutput("Final result: " + finalString, false);
/*    */     
/* 40 */     this.variables.set(this.setVarible, finalString);
/*    */     
/* 42 */     return true;
/*    */   }
/*    */   
/*    */   public String getProcessableName()
/*    */   {
/* 47 */     return "Escape String";
/*    */   }
/*    */ }


/* Location:              D:\stage-installer-01-30-2023\postghost\installer\JCG-Xstore17-InstallerCopy.jar!\com\izforge\izpack\panels\xstprocess\processables\EscapeString.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */