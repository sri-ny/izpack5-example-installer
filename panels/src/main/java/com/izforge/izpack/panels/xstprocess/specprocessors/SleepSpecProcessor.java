 package com.izforge.izpack.panels.xstprocess.specprocessors;
 
 import com.izforge.izpack.api.adaptator.IXMLElement;
 import com.izforge.izpack.panels.xstprocess.Processable;
 import com.izforge.izpack.panels.xstprocess.processables.SleepOperation;
 import java.util.logging.Logger;
 
 public class SleepSpecProcessor
   extends SpecProcessor
 {
   Logger logger = Logger.getLogger(SleepSpecProcessor.class.getName());
   
   public boolean isApplicable(IXMLElement element)
   {
     return element.getName().equals("sleep");
   }
   
   public Processable processElement(IXMLElement element)
   {
     String sleepMs = element.getAttribute("timems");
     int sleepMsInt = 1000;
     
     if ((sleepMs == null) || (sleepMs.equals(""))) {
       this.logger.warning("No Sleep time set, defaulting to 1000ms");
     } else {
       try {
         sleepMsInt = Integer.parseInt(sleepMs);
       } catch (Exception e) {
         this.logger.severe("Can't pasrse the time set to sleep to an integer, setting to 1000ms: " + sleepMs);
       }
     }
     
 
     return new SleepOperation(sleepMsInt);
   }
 }

