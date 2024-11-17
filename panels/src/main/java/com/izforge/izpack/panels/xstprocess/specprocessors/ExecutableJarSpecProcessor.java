package com.izforge.izpack.panels.xstprocess.specprocessors;

import com.izforge.izpack.api.adaptator.IXMLElement;
import com.izforge.izpack.panels.xstprocess.Processable;
import com.izforge.izpack.panels.xstprocess.processables.ExecutableJar;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class ExecutableJarSpecProcessor extends SpecProcessor
{
  public boolean isApplicable(IXMLElement element)
  {
    return element.getName().equals("executejar");
  }
  
  public Processable processElement(IXMLElement element)
  {
    String jarPath = element.getAttribute("jar");
    String javaHome = element.getAttribute("javahome");
    
    if ((jarPath == null) || (jarPath.length() == 0))
    {
      System.err.println("missing \"jar\" attribute for <executejar>");
      return null;
    }
    


    String workingDir = element.getAttribute("workingdir");
    if ((workingDir == null) || (workingDir.length() == 0))
    {
      System.err.println("missing \"workingdir\" attribute for <executejar>");
      return null;
    }
    


    List<String> args = new ArrayList();
    for (IXMLElement arg : element.getChildrenNamed("arg"))
    {
      String argVal = arg.getContent();
      args.add(argVal);
    }
    

    return new ExecutableJar(javaHome, jarPath, workingDir, new String[0]);
  }
}

