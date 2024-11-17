/*    */ package com.izforge.izpack.panels.xstprocess.specprocessors;
/*    */ 
/*    */ import com.izforge.izpack.api.adaptator.IXMLElement;
/*    */ import com.izforge.izpack.panels.xstprocess.Processable;
/*    */ import com.izforge.izpack.panels.xstprocess.processables.FileOperation;
/*    */ import com.izforge.izpack.panels.xstprocess.processables.FileOperation.FileOp;
/*    */ import java.util.logging.Logger;
/*    */ 
/*    */ public class FileOperationSpecProcessor
/*    */   extends SpecProcessor
/*    */ {
/* 12 */   Logger logger = Logger.getLogger(FileOperationSpecProcessor.class.getName());
/*    */   
/*    */   public boolean isApplicable(IXMLElement element)
/*    */   {
/* 16 */     return (element.getName().equals("fileoperation")) || (element.getName().equals("fileop"));
/*    */   }
/*    */   
/*    */ 
/*    */   public Processable processElement(IXMLElement element)
/*    */   {
/* 22 */     String fileOperation = element.getAttribute("operation");
/* 23 */     String srcPath = element.getAttribute("srcpath");
/* 24 */     String dstPath = null;
/*    */     
/*    */ 
/* 27 */     if ((srcPath == null) || (srcPath.equals(""))) {
/* 28 */       this.logger.severe("Must be supplied a 'srcpath', instead got: " + srcPath);
/* 29 */       return null;
/*    */     }
/*    */     
/*    */ 
/* 33 */     if ((FileOperation.FileOp.COPY.toString().equalsIgnoreCase(fileOperation)) || (FileOperation.FileOp.MOVE.toString().equalsIgnoreCase(fileOperation)))
/*    */     {
/*    */ 
/* 36 */       dstPath = element.getAttribute("dstpath");
/* 37 */       if ((dstPath == null) || (dstPath.equals(""))) {
/* 38 */         this.logger.severe("Must be supplied a 'dstpath' when COPY or MOVE operation is requested, instead got: " + srcPath);
/*    */         
/* 40 */         return null;
/*    */       }
/*    */     }
/* 43 */     else if ((!FileOperation.FileOp.DELETE.toString().equalsIgnoreCase(fileOperation)) && (!FileOperation.FileOp.MKDIR.toString().equalsIgnoreCase(fileOperation)))
/*    */     {
/*    */ 
/* 46 */       this.logger.severe("Invalid operation specified, must be MOVE, COPY, MKDIR or DELETE, instead got: " + fileOperation.toString());
/* 47 */       return null;
/*    */     }
/*    */     
/*    */ 
/* 51 */     boolean failOnError = false;
/*    */     try {
/* 53 */       String failOnErrorString = element.getAttribute("failonerror");
/*    */       
/* 55 */       if (failOnErrorString != null) {
/* 56 */         failOnError = Boolean.parseBoolean(failOnErrorString);
/*    */       }
/*    */     }
/*    */     catch (Exception e) {}
/*    */     
/* 61 */     return new FileOperation(FileOperation.FileOp.valueOf(fileOperation.toUpperCase()), srcPath, dstPath, failOnError);
/*    */   }
/*    */ }


/* Location:              D:\stage-installer-01-30-2023\postghost\installer\JCG-Xstore17-InstallerCopy.jar!\com\izforge\izpack\panels\xstprocess\specprocessors\FileOperationSpecProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */