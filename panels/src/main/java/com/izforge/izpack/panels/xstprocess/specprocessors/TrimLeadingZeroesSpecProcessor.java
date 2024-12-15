 package com.izforge.izpack.panels.xstprocess.specprocessors;
 
 import com.izforge.izpack.api.adaptator.IXMLElement;
 import com.izforge.izpack.panels.xstprocess.Processable;
 import com.izforge.izpack.panels.xstprocess.processables.TrimLeadingZeroes;
 
 public class TrimLeadingZeroesSpecProcessor
   extends SpecProcessor
 {
   public boolean isApplicable(IXMLElement element)
   {
     return element.getName().equalsIgnoreCase("TrimLeadingZeroes");
   }
   
   public Processable processElement(IXMLElement element)
   {
     String variableName = element.getAttribute("variable");
     if ((variableName == null) || (variableName.equals(""))) {
       return null;
     }
     
     String variableValue = element.getAttribute("value");
     if (variableValue == null) {
       return null;
     }
     
     return new TrimLeadingZeroes(variableName, variableValue);
   }
 }
