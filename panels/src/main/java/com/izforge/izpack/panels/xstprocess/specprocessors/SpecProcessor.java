 package com.izforge.izpack.panels.xstprocess.specprocessors;
 
 import com.izforge.izpack.api.adaptator.IXMLElement;
 
 public abstract class SpecProcessor
 {
   public abstract boolean isApplicable(IXMLElement paramIXMLElement);
   
   protected abstract com.izforge.izpack.panels.xstprocess.Processable processElement(IXMLElement paramIXMLElement);
   
   public com.izforge.izpack.panels.xstprocess.Processable createProcessable(IXMLElement element) {
     String pause = element.getAttribute("pauseoncomplete");
     
     com.izforge.izpack.panels.xstprocess.Processable processable = processElement(element);
     
     if ((pause != null) && (pause.equalsIgnoreCase("true"))) {
       processable.setPauseExecution(true);
     }
     
     return processable;
   }
 }

