package com.izforge.izpack.panels.xstprocess.specprocessors;

import com.izforge.izpack.api.adaptator.IXMLElement;
import com.izforge.izpack.panels.xstprocess.Processable;
import com.izforge.izpack.panels.xstprocess.processables.WriteRegistryValue;
import com.izforge.izpack.panels.xstprocess.processables.WriteRegistryValue.RegValueType;
import java.util.logging.Logger;

public class WriteRegistryValueSpecProcessor
  extends SpecProcessor
{
  Logger logger = Logger.getLogger(FileOperationSpecProcessor.class.getName());
  
  public boolean isApplicable(IXMLElement element)
  {
    return element.getName().equalsIgnoreCase("setregistryvalue");
  }
  
  public Processable processElement(IXMLElement element)
  {
    String path = element.getAttribute("regpath");
    String key = element.getAttribute("regkey");
    String valtype = element.getAttribute("regvaltype");
    String value = element.getAttribute("regvalue");
    
    if ((path == null) || (path.equals(""))) {
      this.logger.severe("Skipping setting registry value, missing regpath.");
      return null;
    }
    
    if ((key == null) || (key.equals(""))) {
      this.logger.severe("Skipping setting registry value, missing regkey.");
      return null;
    }
    
    if (value == null) {
      this.logger.severe("Skipping setting registry value, missing regvalue.");
      return null;
    }
    try
    {
      WriteRegistryValue.RegValueType.valueOf(WriteRegistryValue.RegValueType.class, valtype);
    } catch (IllegalArgumentException ex) {
      this.logger.severe("Skipping setting registry value, invalid regvaltype.");
      return null;
    } catch (NullPointerException ex) {
      this.logger.severe("Skipping setting registry value, no regvaltype.");
      return null;
    }
    
    return new WriteRegistryValue(path, key, value, WriteRegistryValue.RegValueType.valueOf(valtype));
  }
}
