/*    */ package com.izforge.izpack.panels.xstprocess.processables;
/*    */ 
/*    */ import com.izforge.izpack.api.data.Variables;
/*    */ import com.izforge.izpack.panels.xstprocess.AbstractUIProcessHandler;
/*    */ import java.io.File;
/*    */ import java.io.FileWriter;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class WriteFile extends com.izforge.izpack.panels.xstprocess.Processable
/*    */ {
/*    */   String file;
/*    */   String contents;
/* 13 */   boolean append = false;
/* 14 */   boolean createParentDir = false;
/* 15 */   boolean failOnError = false;
/* 16 */   boolean isDirectory = false;
/*    */   
/*    */   public WriteFile(String file, String contents, boolean append, boolean createParentDir, boolean failOnError)
/*    */   {
/* 20 */     this.file = file;
/* 21 */     this.contents = contents;
/* 22 */     this.append = append;
/* 23 */     this.createParentDir = createParentDir;
/* 24 */     this.failOnError = failOnError;
/*    */   }
/*    */   
/*    */   public WriteFile(String file, boolean createParentDir, boolean failOnError, boolean isDirectory) {
/* 28 */     this.file = file;
/* 29 */     this.createParentDir = createParentDir;
/* 30 */     this.failOnError = failOnError;
/* 31 */     this.isDirectory = isDirectory;
/*    */   }
/*    */   
/*    */   public boolean run() {
/* 35 */     this.handler.logOutput("Writing to file: " + this.file + " - Append: " + this.append, false);
/*    */     
/* 37 */     this.file = this.variables.replace(this.file);
/* 38 */     File theFile = new File(this.file);
/* 39 */     this.contents = this.variables.replace(this.contents);
/*    */     
/*    */ 
/*    */ 
/* 43 */     if (this.createParentDir) {
/* 44 */       theFile.getParentFile().mkdirs();
/*    */     }
/*    */     
/* 47 */     if (this.isDirectory) {
/*    */       try {
/* 49 */         theFile.mkdir();
/* 50 */         this.handler.logOutput("Directory written successfully!", false);
/* 51 */         return true;
/*    */       } catch (Exception e) {
/* 53 */         if (this.failOnError) {
/* 54 */           this.handler.logOutput("Writing to file failed! - " + e.getMessage(), true);
/* 55 */           e.printStackTrace();
/* 56 */           return false;
/*    */         }
/* 58 */         return true;
/*    */       }
/*    */     }
/*    */     
/*    */     try
/*    */     {
/* 64 */       FileWriter fw = new FileWriter(theFile, this.append);
/* 65 */       fw.write(this.contents);
/* 66 */       fw.close();
/*    */     } catch (IOException e) {
/* 68 */       if (this.failOnError) {
/* 69 */         this.handler.logOutput("Writing to file failed! - " + e.getMessage(), true);
/* 70 */         e.printStackTrace();
/* 71 */         return false;
/*    */       }
/*    */     }
/*    */     
/* 75 */     this.handler.logOutput("File written successfully!", false);
/* 76 */     return true;
/*    */   }
/*    */   
/*    */ 
/*    */   public String getProcessableName()
/*    */   {
/* 82 */     return "Write File: " + this.file;
/*    */   }
/*    */ }


/* Location:              D:\stage-installer-01-30-2023\postghost\installer\JCG-Xstore17-InstallerCopy.jar!\com\izforge\izpack\panels\xstprocess\processables\WriteFile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */