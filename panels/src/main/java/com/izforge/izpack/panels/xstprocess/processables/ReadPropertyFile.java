package com.izforge.izpack.panels.xstprocess.processables;

import com.izforge.izpack.api.data.Variables;
import com.izforge.izpack.panels.xstprocess.Processable;
import com.izforge.izpack.panels.xstprocess.util.XstPropertyFileNormalizer;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;
import org.apache.commons.configuration2.PropertiesConfiguration;



public class ReadPropertyFile
  extends Processable
{
  protected String fileName;
  protected HashMap<String, String> keyVariables;
  Logger logger = Logger.getLogger(ReadPropertyFile.class.getName());
  
  public ReadPropertyFile(String fileName, HashMap<String, String> keyVariables) {
    this.fileName = fileName;
    this.keyVariables = keyVariables;
  }
  

  public boolean run()
  {
    this.logger.finer("Attempting to read property file values from: " + this.fileName);
    
    try
    {
      this.fileName = this.variables.replace(this.fileName);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    


    File propertyFileFile = new File(this.fileName);
    if (!propertyFileFile.exists()) {
      this.logger.severe("File set to read properties from does not exist: " + this.fileName);
      return false;
    }
    











    try
    {
      XstPropertyFileNormalizer.normalizeFile(this.fileName);
    } catch (IOException e1) {
      e1.printStackTrace();
      return false;
    }
    


    PropertiesConfiguration config = new PropertiesConfiguration();
    try
    {
      config.read(new FileReader(this.fileName));
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    

    setInstallerVariables(config, this.variables);
    
    return true;
  }
  



  protected void setInstallerVariables(PropertiesConfiguration config, Variables variables)
  {
    for (String key : this.keyVariables.keySet())
    {

      String propFileValue = (String)config.getProperty(key);
      String variableToSet = (String)this.keyVariables.get(key);
      

      if (propFileValue == null) {
        this.logger.warning("Could not find key " + key + " in " + this.fileName + ". Setting " + variableToSet + " to blank String: \"\" ");
        variables.set(variableToSet, "");
      } else {
        this.logger.finer("Found key " + key + " in " + this.fileName + ". Setting " + variableToSet + " to: \"" + propFileValue + "\"");
        variables.set(variableToSet, propFileValue);
      }
    }
  }
  


  public String getProcessableName()
  {
    return "Read Proprty File";
  }
}

