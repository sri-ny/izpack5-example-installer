package com.izforge.izpack.panels.xstprocess.specprocessors;

import com.izforge.izpack.api.adaptator.IXMLElement;
import com.izforge.izpack.panels.xstprocess.Processable;
import com.izforge.izpack.panels.xstprocess.processables.SetPropertyFile;
import java.util.ArrayList;
import java.util.HashMap;

public class SetPropertyFileSpecProcessor
  extends SpecProcessor
{
  public boolean isApplicable(IXMLElement element)
  {
    return element.getName().equals("propertyfileset");
  }
  
  public Processable processElement(IXMLElement element)
  {
    String propertyFile = element.getAttribute("propertyfile");
    
    if ((propertyFile == null) || (propertyFile.equals(""))) {
      return null;
    }
    
    HashMap<String, String> keyValues = new HashMap();
    ArrayList<String> xstEncryptValues = new ArrayList();
    for (IXMLElement propertyElement : element.getChildrenNamed("property"))
    {

      String key = propertyElement.getAttribute("key");
      String value = propertyElement.getAttribute("value");
      
      if ((key != null) && (!key.equals("")))
      {
        String deleteAttribute = propertyElement.getAttribute("delete");
        if ((deleteAttribute != null) && (deleteAttribute.equalsIgnoreCase("true"))) {
          keyValues.put(key, null);
        }
        else if (value != null)
        {

          keyValues.put(key, value);
          
          String xstEncrypt = propertyElement.getAttribute("xstencrypt", "FALSE");
          if (xstEncrypt.equalsIgnoreCase("TRUE"))
            xstEncryptValues.add(key);
        }
      }
    }
    return new SetPropertyFile(propertyFile, keyValues, xstEncryptValues);
  }
}
