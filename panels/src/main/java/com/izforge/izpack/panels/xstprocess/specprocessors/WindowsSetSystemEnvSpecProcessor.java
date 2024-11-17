/*    */ package com.izforge.izpack.panels.xstprocess.specprocessors;
/*    */ 
/*    */ import com.izforge.izpack.api.adaptator.IXMLElement;
/*    */ import com.izforge.izpack.panels.xstprocess.Processable;
/*    */ import java.io.PrintStream;
/*    */ 
/*    */ public class WindowsSetSystemEnvSpecProcessor extends SpecProcessor
/*    */ {
/*    */   public boolean isApplicable(IXMLElement element)
/*    */   {
/* 11 */     return element.getName().equals("setsystemvar");
/*    */   }
/*    */   
/*    */   public Processable processElement(IXMLElement element)
/*    */   {
/* 16 */     String variable = element.getAttribute("variable");
/* 17 */     String value = element.getAttribute("value");
/*    */     
/* 19 */     if ((variable == null) || (variable.length() == 0))
/*    */     {
/* 21 */       System.err.println("No variable set for <setsystemvar>: " + variable);
/* 22 */       return null;
/*    */     }
/*    */     
/* 25 */     if ((value == null) || (value.length() == 0))
/*    */     {
/* 27 */       System.err.println("No value set for <setsystemvar>: " + value);
/* 28 */       return null;
/*    */     }
/*    */     
/* 31 */     return new com.izforge.izpack.panels.xstprocess.processables.WindowsSetSystemEnv(variable, value);
/*    */   }
/*    */ }


/* Location:              D:\stage-installer-01-30-2023\postghost\installer\JCG-Xstore17-InstallerCopy.jar!\com\izforge\izpack\panels\xstprocess\specprocessors\WindowsSetSystemEnvSpecProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */