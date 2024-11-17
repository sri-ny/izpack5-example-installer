/*    */ package com.izforge.izpack.panels.xstprocess.specprocessors;
/*    */ 
/*    */ import com.izforge.izpack.api.adaptator.IXMLElement;
/*    */ import com.izforge.izpack.panels.xstprocess.Processable;
/*    */ import com.izforge.izpack.panels.xstprocess.processables.ExecutableClass;
/*    */ import java.io.PrintStream;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ public class ExecutableClassSpecProcessor extends SpecProcessor
/*    */ {
/*    */   public boolean isApplicable(IXMLElement element)
/*    */   {
/* 14 */     return element.getName().equals("executeclass");
/*    */   }
/*    */   
/*    */   public Processable processElement(IXMLElement element)
/*    */   {
/* 19 */     String className = element.getAttribute("name");
/* 20 */     if ((className == null) || (className.length() == 0))
/*    */     {
/* 22 */       System.err.println("missing \"name\" attribute for <executeclass>");
/* 23 */       return null;
/*    */     }
/*    */     
/*    */ 
/* 27 */     List<String> args = new ArrayList();
/* 28 */     for (IXMLElement argElement : element.getChildrenNamed("arg"))
/*    */     {
/* 30 */       String argValue = argElement.getContent();
/* 31 */       args.add(argValue);
/*    */     }
/*    */     
/* 34 */     return new ExecutableClass(className, args);
/*    */   }
/*    */ }


/* Location:              D:\stage-installer-01-30-2023\postghost\installer\JCG-Xstore17-InstallerCopy.jar!\com\izforge\izpack\panels\xstprocess\specprocessors\ExecutableClassSpecProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */