/*    */ package com.izforge.izpack.panels.xstprocess.specprocessors;
/*    */ 
/*    */ import com.izforge.izpack.api.adaptator.IXMLElement;
/*    */ import com.izforge.izpack.panels.xstprocess.Processable;
/*    */ import com.izforge.izpack.panels.xstprocess.processables.SleepOperation;
/*    */ import java.util.logging.Logger;
/*    */ 
/*    */ public class SleepSpecProcessor
/*    */   extends SpecProcessor
/*    */ {
/* 11 */   Logger logger = Logger.getLogger(SleepSpecProcessor.class.getName());
/*    */   
/*    */   public boolean isApplicable(IXMLElement element)
/*    */   {
/* 15 */     return element.getName().equals("sleep");
/*    */   }
/*    */   
/*    */   public Processable processElement(IXMLElement element)
/*    */   {
/* 20 */     String sleepMs = element.getAttribute("timems");
/* 21 */     int sleepMsInt = 1000;
/*    */     
/* 23 */     if ((sleepMs == null) || (sleepMs.equals(""))) {
/* 24 */       this.logger.warning("No Sleep time set, defaulting to 1000ms");
/*    */     } else {
/*    */       try {
/* 27 */         sleepMsInt = Integer.parseInt(sleepMs);
/*    */       } catch (Exception e) {
/* 29 */         this.logger.severe("Can't pasrse the time set to sleep to an integer, setting to 1000ms: " + sleepMs);
/*    */       }
/*    */     }
/*    */     
/*    */ 
/* 34 */     return new SleepOperation(sleepMsInt);
/*    */   }
/*    */ }


/* Location:              D:\stage-installer-01-30-2023\postghost\installer\JCG-Xstore17-InstallerCopy.jar!\com\izforge\izpack\panels\xstprocess\specprocessors\SleepSpecProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */