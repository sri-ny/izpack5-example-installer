package com.izforge.izpack.panels.xstprocess.specprocessors;

import com.izforge.izpack.api.adaptator.IXMLElement;
import com.izforge.izpack.panels.xstprocess.Processable;
import java.io.PrintStream;

public class WindowsSetSystemEnvSpecProcessor extends SpecProcessor
{
  public boolean isApplicable(IXMLElement element)
  {
    return element.getName().equals("setsystemvar");
  }
  
  public Processable processElement(IXMLElement element)
  {
    String variable = element.getAttribute("variable");
    String value = element.getAttribute("value");
    
    if ((variable == null) || (variable.length() == 0))
    {
      System.err.println("No variable set for <setsystemvar>: " + variable);
      return null;
    }
    
    if ((value == null) || (value.length() == 0))
    {
      System.err.println("No value set for <setsystemvar>: " + value);
      return null;
    }
    
    return new com.izforge.izpack.panels.xstprocess.processables.WindowsSetSystemEnv(variable, value);
  }
}

