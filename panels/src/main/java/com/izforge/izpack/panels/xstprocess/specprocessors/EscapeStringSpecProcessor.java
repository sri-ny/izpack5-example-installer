/*    */ package com.izforge.izpack.panels.xstprocess.specprocessors;
/*    */ 
/*    */ import com.izforge.izpack.api.adaptator.IXMLElement;
/*    */ import com.izforge.izpack.panels.xstprocess.Processable;
/*    */ import com.izforge.izpack.panels.xstprocess.processables.EscapeString;
/*    */ 
/*    */ public class EscapeStringSpecProcessor extends SpecProcessor
/*    */ {
/*    */   public boolean isApplicable(IXMLElement element)
/*    */   {
/* 11 */     return element.getName().equalsIgnoreCase("escapestring");
/*    */   }
/*    */   
/*    */ 
/*    */   protected Processable processElement(IXMLElement element)
/*    */   {
/* 17 */     String characterToEscape = element.getAttribute("charactertoescape");
/* 18 */     String escapeWith = element.getAttribute("escapewith");
/* 19 */     String escapeString = element.getContent();
/* 20 */     String setVariable = element.getAttribute("setvariable");
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 25 */     return new EscapeString(characterToEscape, escapeWith, escapeString, setVariable);
/*    */   }
/*    */ }


/* Location:              D:\stage-installer-01-30-2023\postghost\installer\JCG-Xstore17-InstallerCopy.jar!\com\izforge\izpack\panels\xstprocess\specprocessors\EscapeStringSpecProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */