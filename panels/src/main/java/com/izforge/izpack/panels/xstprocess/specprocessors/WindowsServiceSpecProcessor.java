/*    */ package com.izforge.izpack.panels.xstprocess.specprocessors;
/*    */ 
/*    */ import com.izforge.izpack.api.adaptator.IXMLElement;
/*    */ import com.izforge.izpack.panels.xstprocess.processables.WindowsService.ServiceOperation;
/*    */ import java.io.PrintStream;
/*    */ 
/*    */ public class WindowsServiceSpecProcessor extends SpecProcessor
/*    */ {
/*    */   public boolean isApplicable(IXMLElement element)
/*    */   {
/* 11 */     return element.getName().equals("windowsservice");
/*    */   }
/*    */   
/*    */   public com.izforge.izpack.panels.xstprocess.Processable processElement(IXMLElement element)
/*    */   {
/* 16 */     String serviceName = element.getAttribute("servicename");
/* 17 */     String serviceOperation = element.getAttribute("operation");
/*    */     
/* 19 */     if ((serviceName == null) || (serviceName.length() == 0))
/*    */     {
/* 21 */       System.err.println("missing \"serviceName\" attribute for <windowsservice>");
/* 22 */       return null;
/*    */     }
/*    */     
/* 25 */     if ((serviceOperation == null) || (serviceOperation.length() == 0))
/*    */     {
/* 27 */       System.err.println("missing \"serviceOperation\" attribute for <windowsservice>");
/* 28 */       return null;
/*    */     }
/*    */     
/* 31 */     WindowsService.ServiceOperation serviceOpEnum = null;
/*    */     try {
/* 33 */       serviceOpEnum = WindowsService.ServiceOperation.valueOf(serviceOperation.toUpperCase());
/*    */     } catch (Exception e) {
/* 35 */       System.err.println("invalid service operation \"serviceOperation\" attribute for <windowsservice>: " + serviceOperation);
/* 36 */       return null;
/*    */     }
/*    */     
/* 39 */     return new com.izforge.izpack.panels.xstprocess.processables.WindowsService(serviceName, serviceOpEnum);
/*    */   }
/*    */ }


/* Location:              D:\stage-installer-01-30-2023\postghost\installer\JCG-Xstore17-InstallerCopy.jar!\com\izforge\izpack\panels\xstprocess\specprocessors\WindowsServiceSpecProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */