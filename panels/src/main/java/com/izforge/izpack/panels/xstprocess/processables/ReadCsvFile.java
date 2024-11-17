/*     */ package com.izforge.izpack.panels.xstprocess.processables;
/*     */ 
/*     */ import com.izforge.izpack.api.data.Variables;
/*     */ import com.izforge.izpack.panels.xstprocess.AbstractUIProcessHandler;
/*     */ import com.izforge.izpack.panels.xstprocess.Processable;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ public class ReadCsvFile extends Processable
/*     */ {
/*  18 */   protected String csvDelimiter = "|";
/*     */   
/*     */   protected String csvFile;
/*     */   
/*     */   protected HashMap<Integer, String> columnsToMatch;
/*     */   protected HashMap<Integer, String> columnsToRead;
/*  24 */   Logger logger = Logger.getLogger(ReadCsvFile.class.getName());
/*     */   
/*     */   public ReadCsvFile(String csvFile, HashMap<Integer, String> columnsToMatch, HashMap<Integer, String> columnsToRead) {
/*  27 */     this.csvFile = csvFile;
/*  28 */     this.columnsToMatch = columnsToMatch;
/*  29 */     this.columnsToRead = columnsToRead;
/*     */   }
/*     */   
/*     */   public ReadCsvFile(String csvFile, HashMap<Integer, String> columnsToMatch, HashMap<Integer, String> columnsToRead, String csvDelimiter) {
/*  33 */     this.csvFile = csvFile;
/*  34 */     this.columnsToMatch = columnsToMatch;
/*  35 */     this.columnsToRead = columnsToRead;
/*  36 */     this.csvDelimiter = csvDelimiter;
/*     */   }
/*     */   
/*     */   public boolean run()
/*     */   {
/*  41 */     this.csvFile = this.variables.replace(this.csvFile);
/*  42 */     this.handler.logOutput("Starting CSV Read of file: " + this.csvFile, false);
/*     */     
/*  44 */     File csvFileFile = new File(this.csvFile);
/*  45 */     if (!csvFileFile.exists()) {
/*  46 */       this.logger.severe("CSV File does not exist! - " + csvFileFile.getAbsolutePath());
/*  47 */       this.handler.logOutput("CSV File does not exist! - " + csvFileFile.getAbsolutePath(), true);
/*  48 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  53 */     for (Integer key : this.columnsToMatch.keySet()) {
/*  54 */       String oldValue = (String)this.columnsToMatch.get(key);
/*  55 */       this.columnsToMatch.put(key, this.variables.replace(oldValue));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  60 */     BufferedReader csvReader = null;
/*     */     try {
/*  62 */       csvReader = new BufferedReader(new java.io.FileReader(csvFileFile));
/*     */     } catch (FileNotFoundException e) {
/*  64 */       this.logger.severe("Error reading CSV File! - " + csvFileFile.getAbsolutePath());
/*  65 */       this.logger.severe(e.getMessage());
/*  66 */       this.handler.logOutput("Error reading CSV File!", true);
/*  67 */       e.printStackTrace();
/*  68 */       return false;
/*     */     }
/*     */     
/*     */ 
/*  72 */     ArrayList<CsvFileLine> lines = new ArrayList();
/*  73 */     String theLine = "";
/*     */     try {
/*  75 */       while ((theLine = csvReader.readLine()) != null) {
/*  76 */         lines.add(new CsvFileLine(theLine, this.csvDelimiter));
/*     */       }
/*     */     } catch (IOException e) {
/*  79 */       this.logger.severe("Failure reading csv, skipping....");
/*  80 */       this.logger.severe(e.getMessage());
/*  81 */       this.handler.logOutput("Failure during CSV read!", true);
/*  82 */       return false;
/*     */     }
/*     */     
/*  85 */     this.handler.logOutput("\nRead in " + lines.size() + " lines from csv file\n", false);
/*     */     
/*     */ 
/*  88 */     this.handler.logOutput("Looking for the following in file:", false);
/*  89 */     for (Integer key : this.columnsToMatch.keySet()) {
/*  90 */       this.handler.logOutput("Column number: " + key + " to match value: " + (String)this.columnsToMatch.get(key), false);
/*     */     }
/*     */     
/*     */ 
/*  94 */     CsvFileLine matchedLine = findFirstMatch(lines, this.columnsToMatch);
/*  95 */     if (matchedLine == null) {
/*  96 */       this.handler.logOutput("\nNO MATCH FOUND!", true);
/*  97 */       this.logger.severe("Could not find a matching line....");
/*     */       try
/*     */       {
/* 100 */         csvReader.close();
/*     */       } catch (IOException e) {
/* 102 */         e.printStackTrace();
/*     */       }
/*     */       
/* 105 */       return false;
/*     */     }
/*     */     
/* 108 */     this.handler.logOutput("\nMatched line found:\n" + matchedLine.line, false);
/*     */     
/*     */ 
/* 111 */     this.handler.logOutput("\nSetting IZPACK variables:\n", false);
/* 112 */     for (Integer columnNum : this.columnsToRead.keySet()) {
/* 113 */       String value = matchedLine.getValue(columnNum.intValue());
/* 114 */       String varName = (String)this.columnsToRead.get(columnNum);
/*     */       
/* 116 */       this.logger.info("Setting variable " + varName + " to: " + value);
/* 117 */       this.handler.logOutput(varName + " = " + value, false);
/* 118 */       this.variables.set(varName, value);
/* 119 */       this.handler.logOutput("Izpack Variable:   " + varName + " = " + value, false);
/*     */     }
/*     */     try
/*     */     {
/* 123 */       csvReader.close();
/*     */     } catch (IOException e) {
/* 125 */       e.printStackTrace();
/*     */     }
/*     */     
/* 128 */     return true;
/*     */   }
/*     */   
/*     */   protected CsvFileLine findFirstMatch(List<CsvFileLine> lines, HashMap<Integer, String> toMatch)
/*     */   {
/* 133 */     for (CsvFileLine line : lines) {
/* 134 */       if (line.columnsContain(toMatch)) {
/* 135 */         return line;
/*     */       }
/*     */     }
/* 138 */     return null;
/*     */   }
/*     */   
/*     */   protected class CsvFileLine
/*     */   {
/* 143 */     List<String> values = new ArrayList();
/*     */     String line;
/*     */     String delimiter;
/*     */     
/*     */     public CsvFileLine(String line, String delimiter)
/*     */     {
/* 149 */       this.line = line;
/* 150 */       this.delimiter = delimiter;
/*     */       String[] splitline;
/*     */       String[] splitline;
/* 153 */       if (delimiter.equals("|")) {
/* 154 */         splitline = line.split("\\|", -1);
/*     */       } else {
/* 156 */         splitline = line.split(delimiter, -1);
/*     */       }
/*     */       
/*     */ 
/* 160 */       this.values.addAll(Arrays.asList(splitline));
/*     */     }
/*     */     
/*     */     public int getValueCount() {
/* 164 */       return this.values.size();
/*     */     }
/*     */     
/*     */     public boolean columnContains(int column, String value) {
/* 168 */       return ((String)this.values.get(column)).equals(value);
/*     */     }
/*     */     
/*     */     public boolean columnsContain(HashMap<Integer, String> values) {
/* 172 */       for (Integer key : values.keySet())
/*     */       {
/* 174 */         if (!columnContains(key.intValue(), (String)values.get(key))) {
/* 175 */           return false;
/*     */         }
/*     */       }
/* 178 */       return true;
/*     */     }
/*     */     
/*     */     public String getValue(int column) {
/* 182 */       return (String)this.values.get(column);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getProcessableName()
/*     */   {
/* 191 */     return "Read CSV File";
/*     */   }
/*     */ }


/* Location:              D:\stage-installer-01-30-2023\postghost\installer\JCG-Xstore17-InstallerCopy.jar!\com\izforge\izpack\panels\xstprocess\processables\ReadCsvFile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */