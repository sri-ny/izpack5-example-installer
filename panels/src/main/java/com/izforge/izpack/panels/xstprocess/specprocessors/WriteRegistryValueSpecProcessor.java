/*    */ package com.izforge.izpack.panels.xstprocess.specprocessors;
/*    */ 
/*    */ import com.izforge.izpack.api.adaptator.IXMLElement;
/*    */ import com.izforge.izpack.panels.xstprocess.Processable;
/*    */ import com.izforge.izpack.panels.xstprocess.processables.WriteRegistryValue;
/*    */ import com.izforge.izpack.panels.xstprocess.processables.WriteRegistryValue.RegValueType;
/*    */ import java.util.logging.Logger;
/*    */ 
/*    */ public class WriteRegistryValueSpecProcessor
/*    */   extends SpecProcessor
/*    */ {
/* 12 */   Logger logger = Logger.getLogger(FileOperationSpecProcessor.class.getName());
/*    */   
/*    */   public boolean isApplicable(IXMLElement element)
/*    */   {
/* 16 */     return element.getName().equalsIgnoreCase("setregistryvalue");
/*    */   }
/*    */   
/*    */   public Processable processElement(IXMLElement element)
/*    */   {
/* 21 */     String path = element.getAttribute("regpath");
/* 22 */     String key = element.getAttribute("regkey");
/* 23 */     String valtype = element.getAttribute("regvaltype");
/* 24 */     String value = element.getAttribute("regvalue");
/*    */     
/* 26 */     if ((path == null) || (path.equals(""))) {
/* 27 */       this.logger.severe("Skipping setting registry value, missing regpath.");
/* 28 */       return null;
/*    */     }
/*    */     
/* 31 */     if ((key == null) || (key.equals(""))) {
/* 32 */       this.logger.severe("Skipping setting registry value, missing regkey.");
/* 33 */       return null;
/*    */     }
/*    */     
/* 36 */     if (value == null) {
/* 37 */       this.logger.severe("Skipping setting registry value, missing regvalue.");
/* 38 */       return null;
/*    */     }
/*    */     try
/*    */     {
/* 42 */       WriteRegistryValue.RegValueType.valueOf(WriteRegistryValue.RegValueType.class, valtype);
/*    */     } catch (IllegalArgumentException ex) {
/* 44 */       this.logger.severe("Skipping setting registry value, invalid regvaltype.");
/* 45 */       return null;
/*    */     } catch (NullPointerException ex) {
/* 47 */       this.logger.severe("Skipping setting registry value, no regvaltype.");
/* 48 */       return null;
/*    */     }
/*    */     
/* 51 */     return new WriteRegistryValue(path, key, value, WriteRegistryValue.RegValueType.valueOf(valtype));
/*    */   }
/*    */ }


/* Location:              D:\stage-installer-01-30-2023\postghost\installer\JCG-Xstore17-InstallerCopy.jar!\com\izforge\izpack\panels\xstprocess\specprocessors\WriteRegistryValueSpecProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */