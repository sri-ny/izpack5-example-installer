/*     */ package com.izforge.izpack.panels.xstprocess.processables;
/*     */ 
/*     */ import com.izforge.izpack.panels.xstprocess.AbstractUIProcessHandler;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import java.util.zip.ZipOutputStream;
/*     */ 
/*     */ public class CompressFiles extends com.izforge.izpack.panels.xstprocess.Processable
/*     */ {
/*     */   String zipFile;
/*     */   List<String> fileList;
/*     */   
/*     */   public static enum COMPRESSION_TYPE
/*     */   {
/*  17 */     ZIP;
/*     */     
/*     */     private COMPRESSION_TYPE() {}
/*     */   }
/*     */   
/*     */   public CompressFiles(String zipFile, List<String> fileList)
/*     */   {
/*  24 */     this.zipFile = zipFile;
/*  25 */     this.fileList = fileList;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean run()
/*     */   {
/*  31 */     return zip();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean zip()
/*     */   {
/*  41 */     this.zipFile = this.variables.replace(this.zipFile);
/*     */     try
/*     */     {
/*  44 */       java.io.FileOutputStream fos = new java.io.FileOutputStream(this.zipFile);
/*  45 */       ZipOutputStream zos = new ZipOutputStream(fos);
/*     */       
/*  47 */       this.handler.logOutput("*** Zipping to Zip : " + this.zipFile + " ***", false);
/*     */       
/*  49 */       for (String file : this.fileList)
/*     */       {
/*  51 */         theFile = new File(this.variables.replace(file));
/*     */         
/*     */ 
/*  54 */         if (!theFile.isAbsolute()) {
/*  55 */           this.handler.logOutput("Path must be absolute, skipping: " + file, true);
/*     */ 
/*     */ 
/*     */ 
/*     */         }
/*  60 */         else if (!theFile.exists()) {
/*  61 */           this.handler.logOutput("Can not locate file to add to zip, skipping: " + file, true);
/*     */ 
/*     */ 
/*     */ 
/*     */         }
/*  66 */         else if (theFile.isFile())
/*     */         {
/*  68 */           this.handler.logOutput("Adding file: " + theFile + " to root of ZIP", false);
/*  69 */           addFileToZip(theFile, theFile.getName(), zos);
/*     */         }
/*     */         else {
/*  72 */           this.handler.logOutput("Adding files in: " + theFile.getAbsolutePath(), false);
/*     */           
/*  74 */           List<File> fileInDir = generateFileList(theFile);
/*     */           
/*  76 */           for (File dirFile : fileInDir)
/*     */           {
/*  78 */             String parentPath = theFile.getParent();
/*  79 */             parentPath = parentPath.replaceAll("\\\\+$", "");
/*  80 */             parentPath = parentPath.replaceAll("\\/+$", "");
/*     */             
/*     */ 
/*  83 */             int pathLength = parentPath.length();
/*  84 */             String fileRelativeToSource = dirFile.getAbsolutePath().substring(pathLength);
/*  85 */             fileRelativeToSource = fileRelativeToSource.replaceAll("^\\\\+", "");
/*  86 */             fileRelativeToSource = fileRelativeToSource.replaceAll("^\\/", "");
/*     */             
/*  88 */             this.handler.logOutput("-->Adding: " + fileRelativeToSource, false);
/*  89 */             addFileToZip(dirFile, fileRelativeToSource, zos);
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */       File theFile;
/*     */       
/*  96 */       zos.closeEntry();
/*     */       
/*  98 */       zos.close();
/*     */       
/* 100 */       this.handler.logOutput("*** Zip complete ***", false);
/*     */     } catch (IOException ex) {
/* 102 */       this.handler.logOutput("Error zipping!", true);
/* 103 */       this.handler.logOutput(ex.getMessage(), true);
/* 104 */       ex.printStackTrace();
/*     */       
/* 106 */       return false;
/*     */     }
/*     */     
/* 109 */     return true;
/*     */   }
/*     */   
/*     */   protected void addFileToZip(File sourceFile, String relativePath, ZipOutputStream zos) throws IOException
/*     */   {
/* 114 */     java.util.zip.ZipEntry ze = new java.util.zip.ZipEntry(relativePath);
/* 115 */     zos.putNextEntry(ze);
/*     */     
/* 117 */     byte[] buffer = new byte['Ѐ'];
/*     */     
/*     */ 
/*     */ 
/* 121 */     FileInputStream in = new FileInputStream(sourceFile);
/*     */     
/*     */     int len;
/* 124 */     while ((len = in.read(buffer)) > 0) {
/* 125 */       zos.write(buffer, 0, len);
/*     */     }
/*     */     
/* 128 */     in.close();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<File> generateFileList(File node)
/*     */   {
/* 138 */     List<File> list = new java.util.ArrayList();
/*     */     
/*     */ 
/* 141 */     if (node.isFile()) {
/* 142 */       list.add(node);
/*     */     }
/*     */     
/* 145 */     if (node.isDirectory()) {
/* 146 */       String[] subNote = node.list();
/* 147 */       for (String filename : subNote) {
/* 148 */         List<File> returnList = generateFileList(new File(node, filename));
/* 149 */         list.addAll(returnList);
/*     */       }
/*     */     }
/*     */     
/* 153 */     return list;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getProcessableName()
/*     */   {
/* 162 */     return "Compress Files";
/*     */   }
/*     */ }


/* Location:              D:\stage-installer-01-30-2023\postghost\installer\JCG-Xstore17-InstallerCopy.jar!\com\izforge\izpack\panels\xstprocess\processables\CompressFiles.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */