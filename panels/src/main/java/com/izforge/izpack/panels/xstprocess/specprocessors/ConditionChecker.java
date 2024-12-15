package com.izforge.izpack.panels.xstprocess.specprocessors;

import com.izforge.izpack.api.adaptator.IXMLElement;
import java.util.List;

public abstract interface ConditionChecker
{
  public abstract boolean conditionsMet(IXMLElement paramIXMLElement);
  
  public abstract boolean conditionsMet(String paramString);
  
  public abstract boolean conditionsMet(List<String> paramList);
  
  public abstract boolean checkOS(IXMLElement paramIXMLElement);
  
  public abstract boolean checkPacks(IXMLElement paramIXMLElement);
}

