package com.izforge.izpack.panels.xstprocess.specprocessors;

import com.izforge.izpack.api.adaptator.IXMLElement;
import com.izforge.izpack.panels.xstprocess.Processable;
import com.izforge.izpack.panels.xstprocess.processables.Shutdown;
import com.izforge.izpack.panels.xstprocess.processables.Shutdown.ShutdownOperation;

public class ShutdownSpecProcessor extends SpecProcessor
{
  public boolean isApplicable(IXMLElement element)
  {
    return (element.getName().equals("shutdown")) || (element.getName().equals("reboot"));
  }
  
  public Processable processElement(IXMLElement element)
  {
    String operation = element.getAttribute("operation");
    
    Shutdown.ShutdownOperation opEnum = null;
    if ((operation != null) && (operation.length() != 0))
    {
      try
      {
        opEnum = Shutdown.ShutdownOperation.valueOf(operation.toUpperCase());
      } catch (Exception e) {
        System.err.println("invalid operation  attribute for <shutdown>: " + operation);
        return null;
      }
    }
    

    if (opEnum == null) {
      opEnum = Shutdown.ShutdownOperation.valueOf(element.getName().toUpperCase());
    }
    
    return new Shutdown(opEnum);
  }
}

