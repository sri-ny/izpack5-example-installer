/*    */ package com.izforge.izpack.panels.xstprocess.specprocessors;
/*    */ 
/*    */ import com.izforge.izpack.api.adaptator.IXMLElement;
/*    */ import com.izforge.izpack.panels.xstprocess.Processable;
/*    */ import com.izforge.izpack.panels.xstprocess.processables.ReadCsvFile;
/*    */ import java.util.HashMap;
/*    */ 
/*    */ public class ReadCsvFileSpecProcessor
/*    */   extends SpecProcessor
/*    */ {
/*    */   public boolean isApplicable(IXMLElement element)
/*    */   {
/* 13 */     return element.getName().equals("csvfileread");
/*    */   }
/*    */   
/*    */   public Processable processElement(IXMLElement element)
/*    */   {
/* 18 */     String csvFile = element.getAttribute("csvfile");
/*    */     
/* 20 */     if ((csvFile == null) || (csvFile.equals(""))) {
/* 21 */       return null;
/*    */     }
/*    */     
/*    */ 
/*    */ 
/* 26 */     HashMap<Integer, String> matchColumns = new HashMap();
/* 27 */     for (IXMLElement matchElement : element.getChildrenNamed("matchcolumn"))
/*    */     {
/*    */ 
/* 30 */       String columnNum = matchElement.getAttribute("columnnum");
/* 31 */       String matchValue = matchElement.getAttribute("matchvalue");
/*    */       
/*    */ 
/*    */ 
/* 35 */       if ((columnNum == null) || (columnNum.equals(""))) {
/* 36 */         return null;
/*    */       }
/*    */       
/*    */       int columnNumInt;
/*    */       try
/*    */       {
/* 42 */         columnNumInt = Integer.parseInt(columnNum);
/*    */       }
/*    */       catch (NumberFormatException e) {
/* 45 */         return null;
/*    */       }
/*    */       
/*    */ 
/* 49 */       if (matchValue == null) {
/* 50 */         return null;
/*    */       }
/*    */       
/* 53 */       matchColumns.put(Integer.valueOf(columnNumInt), matchValue);
/*    */     }
/*    */     
/*    */ 
/* 57 */     HashMap<Integer, String> readColumns = new HashMap();
/* 58 */     for (IXMLElement matchElement : element.getChildrenNamed("readcolumn"))
/*    */     {
/*    */ 
/* 61 */       String columnNum = matchElement.getAttribute("columnnum");
/* 62 */       String variableName = matchElement.getAttribute("variablename");
/*    */       
/*    */ 
/* 65 */       if ((columnNum == null) || (columnNum.equals(""))) {
/* 66 */         return null;
/*    */       }
/*    */       
/*    */       int columnNumInt;
/*    */       try
/*    */       {
/* 72 */         columnNumInt = Integer.parseInt(columnNum);
/*    */       }
/*    */       catch (NumberFormatException e) {
/* 75 */         return null;
/*    */       }
/*    */       
/*    */ 
/* 79 */       if ((variableName != null) && (!variableName.isEmpty()))
/*    */       {
/*    */ 
/*    */ 
/* 83 */         readColumns.put(Integer.valueOf(columnNumInt), variableName);
/*    */       }
/*    */     }
/*    */     
/* 87 */     String delimiter = element.getAttribute("delimiter", "|");
/*    */     
/*    */ 
/* 90 */     return new ReadCsvFile(csvFile, matchColumns, readColumns, delimiter);
/*    */   }
/*    */ }


/* Location:              D:\stage-installer-01-30-2023\postghost\installer\JCG-Xstore17-InstallerCopy.jar!\com\izforge\izpack\panels\xstprocess\specprocessors\ReadCsvFileSpecProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */