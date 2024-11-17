/*    */ package com.izforge.izpack.panels.xstprocess.specprocessors;
/*    */ 
/*    */ import com.izforge.izpack.api.adaptator.IXMLElement;
/*    */ import com.izforge.izpack.panels.xstprocess.Processable;
/*    */ import com.izforge.izpack.panels.xstprocess.processables.WriteFile;
/*    */ import java.util.logging.Logger;
/*    */ 
/*    */ public class WriteFileSpecProcessor
/*    */   extends SpecProcessor
/*    */ {
/* 11 */   Logger logger = Logger.getLogger(WriteFileSpecProcessor.class.getName());
/*    */   
/*    */   public boolean isApplicable(IXMLElement element)
/*    */   {
/* 15 */     return element.getName().equalsIgnoreCase("writefile");
/*    */   }
/*    */   
/*    */   public Processable processElement(IXMLElement element)
/*    */   {
/* 20 */     String file = element.getAttribute("file");
/* 21 */     String contents = element.getContent();
/* 22 */     String createParentDir = element.getAttribute("createparentdir");
/* 23 */     String append = element.getAttribute("append");
/* 24 */     String failOnError = element.getAttribute("failonerror");
/* 25 */     String isDirectory = element.getAttribute("isdirectory");
/*    */     
/* 27 */     boolean createParentDirBool = false;
/* 28 */     boolean appendBool = false;
/* 29 */     boolean failOnErrorBool = false;
/* 30 */     boolean isDirectoryBool = false;
/*    */     
/*    */ 
/* 33 */     if ((file == null) || (file.equals(""))) {
/* 34 */       this.logger.severe("Skipping writing file...A file attribute is required!");
/* 35 */       return null;
/*    */     }
/*    */     
/*    */ 
/* 39 */     if (contents == null) {
/* 40 */       contents = "";
/*    */     }
/*    */     
/* 43 */     if ((createParentDir != null) && (createParentDir.equalsIgnoreCase("true"))) {
/* 44 */       createParentDirBool = true;
/*    */     }
/* 46 */     if ((append != null) && (append.equalsIgnoreCase("true"))) {
/* 47 */       appendBool = true;
/*    */     }
/* 49 */     if ((failOnError != null) && (failOnError.equalsIgnoreCase("true"))) {
/* 50 */       failOnErrorBool = true;
/*    */     }
/* 52 */     if ((isDirectory != null) && (isDirectory.equalsIgnoreCase("true"))) {
/* 53 */       isDirectoryBool = true;
/*    */     }
/*    */     
/* 56 */     if (isDirectoryBool) {
/* 57 */       return new WriteFile(file, createParentDirBool, failOnErrorBool, isDirectoryBool);
/*    */     }
/* 59 */     return new WriteFile(file, contents, appendBool, createParentDirBool, failOnErrorBool);
/*    */   }
/*    */ }


/* Location:              D:\stage-installer-01-30-2023\postghost\installer\JCG-Xstore17-InstallerCopy.jar!\com\izforge\izpack\panels\xstprocess\specprocessors\WriteFileSpecProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */