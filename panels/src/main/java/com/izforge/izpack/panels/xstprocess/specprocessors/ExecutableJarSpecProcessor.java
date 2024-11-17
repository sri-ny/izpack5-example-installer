/*    */ package com.izforge.izpack.panels.xstprocess.specprocessors;
/*    */ 
/*    */ import com.izforge.izpack.api.adaptator.IXMLElement;
/*    */ import com.izforge.izpack.panels.xstprocess.Processable;
/*    */ import com.izforge.izpack.panels.xstprocess.processables.ExecutableJar;
/*    */ import java.io.PrintStream;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ public class ExecutableJarSpecProcessor extends SpecProcessor
/*    */ {
/*    */   public boolean isApplicable(IXMLElement element)
/*    */   {
/* 14 */     return element.getName().equals("executejar");
/*    */   }
/*    */   
/*    */   public Processable processElement(IXMLElement element)
/*    */   {
/* 19 */     String jarPath = element.getAttribute("jar");
/* 20 */     String javaHome = element.getAttribute("javahome");
/*    */     
/* 22 */     if ((jarPath == null) || (jarPath.length() == 0))
/*    */     {
/* 24 */       System.err.println("missing \"jar\" attribute for <executejar>");
/* 25 */       return null;
/*    */     }
/*    */     
/*    */ 
/*    */ 
/* 30 */     String workingDir = element.getAttribute("workingdir");
/* 31 */     if ((workingDir == null) || (workingDir.length() == 0))
/*    */     {
/* 33 */       System.err.println("missing \"workingdir\" attribute for <executejar>");
/* 34 */       return null;
/*    */     }
/*    */     
/*    */ 
/*    */ 
/* 39 */     List<String> args = new ArrayList();
/* 40 */     for (IXMLElement arg : element.getChildrenNamed("arg"))
/*    */     {
/* 42 */       String argVal = arg.getContent();
/* 43 */       args.add(argVal);
/*    */     }
/*    */     
/*    */ 
/* 47 */     return new ExecutableJar(javaHome, jarPath, workingDir, new String[0]);
/*    */   }
/*    */ }


/* Location:              D:\stage-installer-01-30-2023\postghost\installer\JCG-Xstore17-InstallerCopy.jar!\com\izforge\izpack\panels\xstprocess\specprocessors\ExecutableJarSpecProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */