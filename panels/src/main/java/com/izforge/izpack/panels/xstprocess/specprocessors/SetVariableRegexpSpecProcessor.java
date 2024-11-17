package com.izforge.izpack.panels.xstprocess.specprocessors;

import com.izforge.izpack.api.adaptator.IXMLElement;
import com.izforge.izpack.panels.xstprocess.Processable;
import com.izforge.izpack.panels.xstprocess.processables.SetVariableRegexp;

public class SetVariableRegexpSpecProcessor extends SpecProcessor
{
  public boolean isApplicable(IXMLElement element)
  {
    return element.getName().equalsIgnoreCase("setvariableregexp");
  }
  


  public Processable processElement(IXMLElement element)
  {
    String expression = element.getAttribute("expression");
    if (expression == null) {
      return null;
    }
    

    String inputString = element.getAttribute("inputstring");
    if (inputString == null) {
      return null;
    }
    


    String setString = element.getAttribute("setstring");
    if (setString == null) {
      return null;
    }
    

    String variable = element.getAttribute("variable");
    if ((variable == null) || (variable.isEmpty())) {
      return null;
    }
    

    String failNoMatchString = element.getAttribute("failnomatch");
    boolean failNoMatch = false;
    
    if (failNoMatchString == null) {
      failNoMatch = false;
    } else {
      Boolean failNoMatchConvert = new Boolean(failNoMatchString);
      failNoMatch = failNoMatchConvert.booleanValue();
    }
    

    return new SetVariableRegexp(expression, inputString, setString, variable, failNoMatch);
  }
}
