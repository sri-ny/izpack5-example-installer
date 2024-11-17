package com.izforge.izpack.panels.xstprocess.specprocessors;

import com.izforge.izpack.api.adaptator.IXMLElement;
import com.izforge.izpack.panels.xstprocess.Processable;
import com.izforge.izpack.panels.xstprocess.processables.Echo;

public class EchoSpecProcessor extends SpecProcessor
{
  public boolean isApplicable(IXMLElement element)
  {
    return element.getName().equalsIgnoreCase("echo");
  }
  
  protected Processable processElement(IXMLElement element)
  {
    String message = element.getContent();
    return new Echo(message);
  }
}

