package com.izforge.izpack.panels.xstprocess.specprocessors;

import com.izforge.izpack.api.adaptator.IXMLElement;
import com.izforge.izpack.panels.xstprocess.Processable;
import com.izforge.izpack.panels.xstprocess.processables.EncryptedReadPropertyFile;
import java.util.HashMap;

public class ReadEncryptedPropertyFileSpecProcessor
  extends SpecProcessor
{
  public boolean isApplicable(IXMLElement element)
  {
    return element.getName().equals("encryptedpropertyfileread");
  }
  
  public Processable processElement(IXMLElement element)
  {
    String propertyFile = element.getAttribute("propertyfile");
    
    if ((propertyFile == null) || (propertyFile.equals(""))) {
      return null;
    }
    
    HashMap<String, String> keyVariables = new HashMap();
    for (IXMLElement propertyElement : element.getChildrenNamed("property"))
    {

      String key = propertyElement.getAttribute("key");
      String variable = propertyElement.getAttribute("variable");
      
      if ((key != null) && (!key.equals("")) && 
      
        (variable != null))
      {

        keyVariables.put(key, variable);
      }
    }
    
    String password = element.getAttribute("filepassword");
    if ((password == null) || (password.equals(""))) {
      return null;
    }
    
    return new EncryptedReadPropertyFile(propertyFile, keyVariables, password);
  }
}

