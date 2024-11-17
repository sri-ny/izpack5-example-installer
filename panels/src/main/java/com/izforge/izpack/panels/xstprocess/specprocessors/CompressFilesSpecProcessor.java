/*    */ package com.izforge.izpack.panels.xstprocess.specprocessors;
/*    */ 
/*    */ import com.izforge.izpack.api.adaptator.IXMLElement;
/*    */ import com.izforge.izpack.panels.xstprocess.Processable;
/*    */ import com.izforge.izpack.panels.xstprocess.processables.CompressFiles;
/*    */ import java.io.PrintStream;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ public class CompressFilesSpecProcessor extends SpecProcessor
/*    */ {
/*    */   public boolean isApplicable(IXMLElement element)
/*    */   {
/* 14 */     return element.getName().equalsIgnoreCase("zip");
/*    */   }
/*    */   
/*    */ 
/*    */   public Processable processElement(IXMLElement element)
/*    */   {
/* 20 */     String zipFile = element.getAttribute("zipfile");
/*    */     
/* 22 */     if ((zipFile.isEmpty()) || (zipFile == null)) {
/* 23 */       System.err.println("Zip file not specified");
/* 24 */       return null;
/*    */     }
/*    */     
/*    */ 
/* 28 */     List<IXMLElement> zipEntriesXml = element.getChildrenNamed("zipentry");
/* 29 */     if (zipEntriesXml.size() < 1) {
/* 30 */       System.err.println("No zip entries, skipping");
/* 31 */       return null;
/*    */     }
/*    */     
/* 34 */     List<String> zipEntries = new ArrayList();
/* 35 */     for (IXMLElement zipElement : zipEntriesXml) {
/* 36 */       String entry = zipElement.getContent();
/* 37 */       zipEntries.add(entry);
/*    */     }
/*    */     
/*    */ 
/*    */ 
/* 42 */     return new CompressFiles(zipFile, zipEntries);
/*    */   }
/*    */ }


/* Location:              D:\stage-installer-01-30-2023\postghost\installer\JCG-Xstore17-InstallerCopy.jar!\com\izforge\izpack\panels\xstprocess\specprocessors\CompressFilesSpecProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */