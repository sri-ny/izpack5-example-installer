/*    */ package com.izforge.izpack.panels.xstprocess.specprocessors;
/*    */ 
/*    */ import com.izforge.izpack.api.adaptator.IXMLElement;
/*    */ import com.izforge.izpack.panels.xstprocess.Processable;
/*    */ import com.izforge.izpack.panels.xstprocess.processables.ExecutableFile;
/*    */ import java.io.PrintStream;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ public class ExecutableFileSpecProcessor extends SpecProcessor
/*    */ {
/*    */   public boolean isApplicable(IXMLElement element)
/*    */   {
/* 14 */     return element.getName().equals("executefile");
/*    */   }
/*    */   
/*    */   public Processable processElement(IXMLElement element)
/*    */   {
/* 19 */     String fileName = element.getAttribute("name");
/* 20 */     String reloadPath = element.getAttribute("reloadpath");
/*    */     
/* 22 */     if ((fileName == null) || (fileName.length() == 0))
/*    */     {
/* 24 */       System.err.println("missing \"name\" attribute for <executefile>");
/* 25 */       return null;
/*    */     }
/*    */     
/*    */ 
/*    */ 
/* 30 */     String workingDir = element.getAttribute("workingDir");
/*    */     
/*    */ 
/* 33 */     List<String> args = new ArrayList();
/* 34 */     for (IXMLElement arg : element.getChildrenNamed("arg"))
/*    */     {
/* 36 */       String argVal = arg.getContent();
/* 37 */       args.add(argVal);
/*    */     }
/*    */     
/*    */ 
/* 41 */     List<String> envvars = new ArrayList();
/* 42 */     for (IXMLElement envElement : element.getChildrenNamed("env"))
/*    */     {
/* 44 */       String envVal = envElement.getContent();
/* 45 */       envvars.add(envVal);
/*    */     }
/*    */     
/* 48 */     if ((reloadPath != null) && (reloadPath.equalsIgnoreCase("true"))) {
/* 49 */       return new ExecutableFile(fileName, args, envvars, workingDir, true);
/*    */     }
/*    */     
/* 52 */     return new ExecutableFile(fileName, args, envvars, workingDir);
/*    */   }
/*    */ }


/* Location:              D:\stage-installer-01-30-2023\postghost\installer\JCG-Xstore17-InstallerCopy.jar!\com\izforge\izpack\panels\xstprocess\specprocessors\ExecutableFileSpecProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */