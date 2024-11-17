/*     */ package com.izforge.izpack.panels.xstprocess.processables;
/*     */ 
/*     */ import com.izforge.izpack.api.data.Variables;
/*     */ import com.izforge.izpack.panels.xstprocess.AbstractUIProcessHandler;
/*     */ import com.izforge.izpack.panels.xstprocess.Processable;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import org.apache.commons.io.FileUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FileOperation
/*     */   extends Processable
/*     */ {
/*     */   FileOp fileOperation;
/*     */   String srcPath;
/*     */   String dstPath;
/*     */   
/*     */   public static enum FileOp
/*     */   {
/*  23 */     COPY, 
/*  24 */     MOVE, 
/*  25 */     DELETE, 
/*  26 */     MKDIR;
/*     */     
/*     */     private FileOp() {}
/*     */   }
/*     */   
/*  31 */   boolean failOnError = false;
/*     */   
/*     */   public FileOperation(FileOp fileOperation, String srcPath, String dstPath, boolean failOnError) {
/*  34 */     this.fileOperation = fileOperation;
/*  35 */     this.srcPath = srcPath;
/*  36 */     this.dstPath = dstPath;
/*  37 */     this.failOnError = failOnError;
/*     */   }
/*     */   
/*     */   public boolean run()
/*     */   {
/*  42 */     this.handler.logOutput("Fail on error is set to: " + this.failOnError, false);
/*     */     
/*  44 */     File srcPath = new File(this.variables.replace(this.srcPath));
/*  45 */     File dstPath = null;
/*     */     
/*  47 */     if (this.dstPath != null) {
/*  48 */       dstPath = new File(this.variables.replace(this.dstPath));
/*     */     }
/*     */     
/*     */ 
/*  52 */     if ((this.fileOperation == FileOp.MOVE) || (this.fileOperation == FileOp.COPY))
/*     */     {
/*     */       try
/*     */       {
/*  56 */         if (srcPath.isDirectory()) {
/*  57 */           this.handler.logOutput("Source determined to be directory: " + srcPath.getAbsolutePath(), false);
/*  58 */           FileUtils.copyDirectoryToDirectory(srcPath, dstPath);
/*     */         } else {
/*  60 */           this.handler.logOutput("Source determined to be file: " + srcPath.getAbsolutePath(), false);
/*     */           
/*  62 */           String fileAbsPath = dstPath.getAbsolutePath();
/*     */           
/*     */ 
/*  65 */           if ((fileAbsPath.endsWith("/")) || (fileAbsPath.endsWith("\\")))
/*     */           {
/*  67 */             this.handler.logOutput("Copying file: " + srcPath.getAbsolutePath() + "to directory: " + dstPath.getAbsolutePath(), false);
/*     */             
/*     */ 
/*  70 */             FileUtils.copyFileToDirectory(srcPath, dstPath);
/*     */           }
/*     */           else
/*     */           {
/*  74 */             this.handler.logOutput("Copying file: " + srcPath.getAbsolutePath() + "to file: " + dstPath.getAbsolutePath(), false);
/*     */             
/*     */ 
/*  77 */             FileUtils.copyFile(srcPath, dstPath);
/*     */           }
/*     */         }
/*     */         
/*     */ 
/*  82 */         if (this.fileOperation == FileOp.MOVE) {
/*  83 */           this.handler.logOutput("Deleting source: " + srcPath, false);
/*  84 */           FileUtils.forceDelete(srcPath);
/*     */         }
/*     */       }
/*     */       catch (IOException e)
/*     */       {
/*  89 */         this.handler.logOutput("File operation error!", true);
/*  90 */         this.handler.logOutput(e.getMessage(), true);
/*     */         
/*  92 */         if (this.failOnError) {
/*  93 */           this.handler.logOutput("Fail on error set!", true);
/*  94 */           e.printStackTrace();
/*  95 */           return false;
/*     */ 
/*     */         }
/*     */         
/*     */ 
/*     */       }
/*     */       
/*     */     }
/* 103 */     else if (this.fileOperation == FileOp.DELETE) {
/*     */       try
/*     */       {
/* 106 */         FileUtils.forceDelete(srcPath);
/*     */         
/* 108 */         this.handler.logOutput("Deleted: " + srcPath.getAbsolutePath(), false);
/*     */       }
/*     */       catch (IOException e)
/*     */       {
/* 112 */         this.handler.logOutput("Error deleteing: " + srcPath.getAbsolutePath(), true);
/* 113 */         this.handler.logOutput(e.getMessage(), true);
/*     */         
/* 115 */         if (this.failOnError) {
/* 116 */           this.handler.logOutput("Fail on error set!", true);
/* 117 */           e.printStackTrace();
/* 118 */           return false;
/*     */ 
/*     */         }
/*     */         
/*     */       }
/*     */       
/*     */     }
/* 125 */     else if (this.fileOperation == FileOp.MKDIR) {
/*     */       try {
/* 127 */         Files.createDirectories(srcPath.toPath(), new FileAttribute[0]);
/* 128 */         this.handler.logOutput("Created: " + srcPath.getAbsolutePath(), false);
/*     */       }
/*     */       catch (IOException e) {
/* 131 */         this.handler.logOutput("Error creating directory: " + srcPath.getAbsolutePath(), true);
/*     */         
/* 133 */         if (this.failOnError) {
/* 134 */           this.handler.logOutput(e.getMessage(), true);
/* 135 */           e.printStackTrace();
/* 136 */           return false;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 141 */     return true;
/*     */   }
/*     */   
/*     */   public String getProcessableName()
/*     */   {
/* 146 */     return "File operation: " + this.fileOperation.toString();
/*     */   }
/*     */ }


/* Location:              D:\stage-installer-01-30-2023\postghost\installer\JCG-Xstore17-InstallerCopy.jar!\com\izforge\izpack\panels\xstprocess\processables\FileOperation.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */