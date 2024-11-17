/*    */ package com.izforge.izpack.panels.xstprocess.specprocessors;
/*    */ 
/*    */ import com.izforge.izpack.api.adaptator.IXMLElement;
/*    */ import com.izforge.izpack.panels.xstprocess.Processable;
/*    */ import com.izforge.izpack.panels.xstprocess.processables.Shutdown;
/*    */ import com.izforge.izpack.panels.xstprocess.processables.Shutdown.ShutdownOperation;
/*    */ 
/*    */ public class ShutdownSpecProcessor extends SpecProcessor
/*    */ {
/*    */   public boolean isApplicable(IXMLElement element)
/*    */   {
/* 12 */     return (element.getName().equals("shutdown")) || (element.getName().equals("reboot"));
/*    */   }
/*    */   
/*    */   public Processable processElement(IXMLElement element)
/*    */   {
/* 17 */     String operation = element.getAttribute("operation");
/*    */     
/* 19 */     Shutdown.ShutdownOperation opEnum = null;
/* 20 */     if ((operation != null) && (operation.length() != 0))
/*    */     {
/*    */       try
/*    */       {
/* 24 */         opEnum = Shutdown.ShutdownOperation.valueOf(operation.toUpperCase());
/*    */       } catch (Exception e) {
/* 26 */         System.err.println("invalid operation  attribute for <shutdown>: " + operation);
/* 27 */         return null;
/*    */       }
/*    */     }
/*    */     
/*    */ 
/* 32 */     if (opEnum == null) {
/* 33 */       opEnum = Shutdown.ShutdownOperation.valueOf(element.getName().toUpperCase());
/*    */     }
/*    */     
/* 36 */     return new Shutdown(opEnum);
/*    */   }
/*    */ }


/* Location:              D:\stage-installer-01-30-2023\postghost\installer\JCG-Xstore17-InstallerCopy.jar!\com\izforge\izpack\panels\xstprocess\specprocessors\ShutdownSpecProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */