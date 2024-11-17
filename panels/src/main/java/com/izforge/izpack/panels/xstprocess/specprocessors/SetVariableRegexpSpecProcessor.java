/*    */ package com.izforge.izpack.panels.xstprocess.specprocessors;
/*    */ 
/*    */ import com.izforge.izpack.api.adaptator.IXMLElement;
/*    */ import com.izforge.izpack.panels.xstprocess.Processable;
/*    */ import com.izforge.izpack.panels.xstprocess.processables.SetVariableRegexp;
/*    */ 
/*    */ public class SetVariableRegexpSpecProcessor extends SpecProcessor
/*    */ {
/*    */   public boolean isApplicable(IXMLElement element)
/*    */   {
/* 11 */     return element.getName().equalsIgnoreCase("setvariableregexp");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public Processable processElement(IXMLElement element)
/*    */   {
/* 18 */     String expression = element.getAttribute("expression");
/* 19 */     if (expression == null) {
/* 20 */       return null;
/*    */     }
/*    */     
/*    */ 
/* 24 */     String inputString = element.getAttribute("inputstring");
/* 25 */     if (inputString == null) {
/* 26 */       return null;
/*    */     }
/*    */     
/*    */ 
/*    */ 
/* 31 */     String setString = element.getAttribute("setstring");
/* 32 */     if (setString == null) {
/* 33 */       return null;
/*    */     }
/*    */     
/*    */ 
/* 37 */     String variable = element.getAttribute("variable");
/* 38 */     if ((variable == null) || (variable.isEmpty())) {
/* 39 */       return null;
/*    */     }
/*    */     
/*    */ 
/* 43 */     String failNoMatchString = element.getAttribute("failnomatch");
/* 44 */     boolean failNoMatch = false;
/*    */     
/* 46 */     if (failNoMatchString == null) {
/* 47 */       failNoMatch = false;
/*    */     } else {
/* 49 */       Boolean failNoMatchConvert = new Boolean(failNoMatchString);
/* 50 */       failNoMatch = failNoMatchConvert.booleanValue();
/*    */     }
/*    */     
/*    */ 
/* 54 */     return new SetVariableRegexp(expression, inputString, setString, variable, failNoMatch);
/*    */   }
/*    */ }


/* Location:              D:\stage-installer-01-30-2023\postghost\installer\JCG-Xstore17-InstallerCopy.jar!\com\izforge\izpack\panels\xstprocess\specprocessors\SetVariableRegexpSpecProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */