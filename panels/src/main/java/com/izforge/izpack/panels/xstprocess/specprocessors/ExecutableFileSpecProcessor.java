package com.izforge.izpack.panels.xstprocess.specprocessors;

import com.izforge.izpack.api.adaptator.IXMLElement;
import com.izforge.izpack.panels.xstprocess.Processable;
import com.izforge.izpack.panels.xstprocess.processables.ExecutableFile;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class ExecutableFileSpecProcessor extends SpecProcessor
{
  public boolean isApplicable(IXMLElement element)
  {
    return element.getName().equals("executefile");
  }
  
  public Processable processElement(IXMLElement element)
  {
    String fileName = element.getAttribute("name");
    String reloadPath = element.getAttribute("reloadpath");
    
    if ((fileName == null) || (fileName.length() == 0))
    {
      System.err.println("missing \"name\" attribute for <executefile>");
      return null;
    }
    


    String workingDir = element.getAttribute("workingDir");
    

    List<String> args = new ArrayList();
    for (IXMLElement arg : element.getChildrenNamed("arg"))
    {
      String argVal = arg.getContent();
      args.add(argVal);
    }
    

    List<String> envvars = new ArrayList();
    for (IXMLElement envElement : element.getChildrenNamed("env"))
    {
      String envVal = envElement.getContent();
      envvars.add(envVal);
    }
    
    if ((reloadPath != null) && (reloadPath.equalsIgnoreCase("true"))) {
      return new ExecutableFile(fileName, args, envvars, workingDir, true);
    }
    
    return new ExecutableFile(fileName, args, envvars, workingDir);
  }
}

