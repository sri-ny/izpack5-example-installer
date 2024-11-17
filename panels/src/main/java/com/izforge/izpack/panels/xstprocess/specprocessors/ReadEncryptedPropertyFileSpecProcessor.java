/*    */ package com.izforge.izpack.panels.xstprocess.specprocessors;
/*    */ 
/*    */ import com.izforge.izpack.api.adaptator.IXMLElement;
/*    */ import com.izforge.izpack.panels.xstprocess.Processable;
/*    */ import com.izforge.izpack.panels.xstprocess.processables.EncryptedReadPropertyFile;
/*    */ import java.util.HashMap;
/*    */ 
/*    */ public class ReadEncryptedPropertyFileSpecProcessor
/*    */   extends SpecProcessor
/*    */ {
/*    */   public boolean isApplicable(IXMLElement element)
/*    */   {
/* 13 */     return element.getName().equals("encryptedpropertyfileread");
/*    */   }
/*    */   
/*    */   public Processable processElement(IXMLElement element)
/*    */   {
/* 18 */     String propertyFile = element.getAttribute("propertyfile");
/*    */     
/* 20 */     if ((propertyFile == null) || (propertyFile.equals(""))) {
/* 21 */       return null;
/*    */     }
/*    */     
/*    */ 
/* 25 */     HashMap<String, String> keyVariables = new HashMap();
/* 26 */     for (IXMLElement propertyElement : element.getChildrenNamed("property"))
/*    */     {
/*    */ 
/* 29 */       String key = propertyElement.getAttribute("key");
/* 30 */       String variable = propertyElement.getAttribute("variable");
/*    */       
/*    */ 
/* 33 */       if ((key != null) && (!key.equals("")) && 
/*    */       
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 39 */         (variable != null))
/*    */       {
/*    */ 
/*    */ 
/*    */ 
/* 44 */         keyVariables.put(key, variable);
/*    */       }
/*    */     }
/*    */     
/* 48 */     String password = element.getAttribute("filepassword");
/* 49 */     if ((password == null) || (password.equals(""))) {
/* 50 */       return null;
/*    */     }
/*    */     
/* 53 */     return new EncryptedReadPropertyFile(propertyFile, keyVariables, password);
/*    */   }
/*    */ }


/* Location:              D:\stage-installer-01-30-2023\postghost\installer\JCG-Xstore17-InstallerCopy.jar!\com\izforge\izpack\panels\xstprocess\specprocessors\ReadEncryptedPropertyFileSpecProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */