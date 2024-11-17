package com.izforge.izpack.panels.xstprocess.specprocessors;

import com.izforge.izpack.api.adaptator.IXMLElement;
import com.izforge.izpack.panels.xstprocess.Processable;
import com.izforge.izpack.panels.xstprocess.processables.ExecutableClass;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class ExecutableClassSpecProcessor extends SpecProcessor
{
  public boolean isApplicable(IXMLElement element)
  {
    return element.getName().equals("executeclass");
  }
  
  public Processable processElement(IXMLElement element)
  {
    String className = element.getAttribute("name");
    if ((className == null) || (className.length() == 0))
    {
      System.err.println("missing \"name\" attribute for <executeclass>");
      return null;
    }
    

    List<String> args = new ArrayList();
    for (IXMLElement argElement : element.getChildrenNamed("arg"))
    {
      String argValue = argElement.getContent();
      args.add(argValue);
    }
    
    return new ExecutableClass(className, args);
  }
}
