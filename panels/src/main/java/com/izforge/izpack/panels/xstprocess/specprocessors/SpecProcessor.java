/*    */ package com.izforge.izpack.panels.xstprocess.specprocessors;
/*    */ 
/*    */ import com.izforge.izpack.api.adaptator.IXMLElement;
/*    */ 
/*    */ public abstract class SpecProcessor
/*    */ {
/*    */   public abstract boolean isApplicable(IXMLElement paramIXMLElement);
/*    */   
/*    */   protected abstract com.izforge.izpack.panels.xstprocess.Processable processElement(IXMLElement paramIXMLElement);
/*    */   
/*    */   public com.izforge.izpack.panels.xstprocess.Processable createProcessable(IXMLElement element) {
/* 12 */     String pause = element.getAttribute("pauseoncomplete");
/*    */     
/* 14 */     com.izforge.izpack.panels.xstprocess.Processable processable = processElement(element);
/*    */     
/* 16 */     if ((pause != null) && (pause.equalsIgnoreCase("true"))) {
/* 17 */       processable.setPauseExecution(true);
/*    */     }
/*    */     
/* 20 */     return processable;
/*    */   }
/*    */ }


/* Location:              D:\stage-installer-01-30-2023\postghost\installer\JCG-Xstore17-InstallerCopy.jar!\com\izforge\izpack\panels\xstprocess\specprocessors\SpecProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */