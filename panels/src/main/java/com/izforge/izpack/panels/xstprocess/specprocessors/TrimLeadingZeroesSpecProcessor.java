/*    */ package com.izforge.izpack.panels.xstprocess.specprocessors;
/*    */ 
/*    */ import com.izforge.izpack.api.adaptator.IXMLElement;
/*    */ import com.izforge.izpack.panels.xstprocess.Processable;
/*    */ import com.izforge.izpack.panels.xstprocess.processables.TrimLeadingZeroes;
/*    */ 
/*    */ public class TrimLeadingZeroesSpecProcessor
/*    */   extends SpecProcessor
/*    */ {
/*    */   public boolean isApplicable(IXMLElement element)
/*    */   {
/* 12 */     return element.getName().equalsIgnoreCase("TrimLeadingZeroes");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public Processable processElement(IXMLElement element)
/*    */   {
/* 19 */     String variableName = element.getAttribute("variable");
/* 20 */     if ((variableName == null) || (variableName.equals(""))) {
/* 21 */       return null;
/*    */     }
/*    */     
/* 24 */     String variableValue = element.getAttribute("value");
/* 25 */     if (variableValue == null) {
/* 26 */       return null;
/*    */     }
/*    */     
/* 29 */     return new TrimLeadingZeroes(variableName, variableValue);
/*    */   }
/*    */ }


/* Location:              D:\stage-installer-01-30-2023\postghost\installer\JCG-Xstore17-InstallerCopy.jar!\com\izforge\izpack\panels\xstprocess\specprocessors\TrimLeadingZeroesSpecProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */