/*    */ package com.izforge.izpack.panels.xstprocess.specprocessors;
/*    */ 
/*    */ import com.izforge.izpack.api.adaptator.IXMLElement;
/*    */ import com.izforge.izpack.panels.xstprocess.Processable;
/*    */ import com.izforge.izpack.panels.xstprocess.processables.SetPropertyFile;
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SetPropertyFileSpecProcessor
/*    */   extends SpecProcessor
/*    */ {
/*    */   public boolean isApplicable(IXMLElement element)
/*    */   {
/* 16 */     return element.getName().equals("propertyfileset");
/*    */   }
/*    */   
/*    */   public Processable processElement(IXMLElement element)
/*    */   {
/* 21 */     String propertyFile = element.getAttribute("propertyfile");
/*    */     
/* 23 */     if ((propertyFile == null) || (propertyFile.equals(""))) {
/* 24 */       return null;
/*    */     }
/*    */     
/*    */ 
/* 28 */     HashMap<String, String> keyValues = new HashMap();
/* 29 */     ArrayList<String> xstEncryptValues = new ArrayList();
/* 30 */     for (IXMLElement propertyElement : element.getChildrenNamed("property"))
/*    */     {
/*    */ 
/* 33 */       String key = propertyElement.getAttribute("key");
/* 34 */       String value = propertyElement.getAttribute("value");
/*    */       
/*    */ 
/*    */ 
/*    */ 
/* 39 */       if ((key != null) && (!key.equals("")))
/*    */       {
/*    */ 
/*    */ 
/*    */ 
/* 44 */         String deleteAttribute = propertyElement.getAttribute("delete");
/* 45 */         if ((deleteAttribute != null) && (deleteAttribute.equalsIgnoreCase("true"))) {
/* 46 */           keyValues.put(key, null);
/*    */ 
/*    */ 
/*    */ 
/*    */         }
/* 51 */         else if (value != null)
/*    */         {
/*    */ 
/*    */ 
/* 55 */           keyValues.put(key, value);
/*    */           
/*    */ 
/*    */ 
/* 59 */           String xstEncrypt = propertyElement.getAttribute("xstencrypt", "FALSE");
/* 60 */           if (xstEncrypt.equalsIgnoreCase("TRUE"))
/* 61 */             xstEncryptValues.add(key);
/*    */         }
/*    */       }
/*    */     }
/* 65 */     return new SetPropertyFile(propertyFile, keyValues, xstEncryptValues);
/*    */   }
/*    */ }


/* Location:              D:\stage-installer-01-30-2023\postghost\installer\JCG-Xstore17-InstallerCopy.jar!\com\izforge\izpack\panels\xstprocess\specprocessors\SetPropertyFileSpecProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */