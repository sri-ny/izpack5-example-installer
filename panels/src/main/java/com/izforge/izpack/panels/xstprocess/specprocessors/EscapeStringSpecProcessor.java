package com.izforge.izpack.panels.xstprocess.specprocessors;

import com.izforge.izpack.api.adaptator.IXMLElement;
import com.izforge.izpack.panels.xstprocess.Processable;
import com.izforge.izpack.panels.xstprocess.processables.EscapeString;

public class EscapeStringSpecProcessor extends SpecProcessor
{
  public boolean isApplicable(IXMLElement element)
  {
    return element.getName().equalsIgnoreCase("escapestring");
  }
  

  protected Processable processElement(IXMLElement element)
  {
    String characterToEscape = element.getAttribute("charactertoescape");
    String escapeWith = element.getAttribute("escapewith");
    String escapeString = element.getContent();
    String setVariable = element.getAttribute("setvariable");
    



    return new EscapeString(characterToEscape, escapeWith, escapeString, setVariable);
  }
}

