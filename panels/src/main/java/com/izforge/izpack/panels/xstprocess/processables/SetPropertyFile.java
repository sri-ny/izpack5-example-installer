package com.izforge.izpack.panels.xstprocess.processables;

import com.izforge.izpack.api.data.Variables;
import com.izforge.izpack.panels.xstprocess.AbstractUIProcessHandler;
import com.izforge.izpack.panels.xstprocess.Processable;
import com.izforge.izpack.panels.xstprocess.util.XstPropertyFileNormalizer;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.configuration2.PropertiesConfiguration;

public class SetPropertyFile
  extends Processable
{
  private String fileName;
  private HashMap<String, String> keyValues;
  private List<String> xstEncryptValues = null;
  
  public SetPropertyFile(String fileName, HashMap<String, String> keyValues, List<String> xstEncryptValues) {
    this.fileName = fileName;
    this.keyValues = keyValues;
    this.xstEncryptValues = xstEncryptValues;
  }
  

  public boolean run()
  {
    this.fileName = this.variables.replace(this.fileName);
    
    this.handler.logOutput("Setting property file: " + this.fileName, false);
    File propertyFileFile = new File(this.fileName);
    

    if (!propertyFileFile.exists()) {
      try {
        this.handler.logOutput("Property file does not exist, creating: " + this.fileName, false);
        propertyFileFile.createNewFile();
      } catch (IOException exception) {
        exception.printStackTrace();
        this.handler.logOutput("Error Creating Property File!: " + this.fileName, true);
        this.handler.logOutput(exception.getMessage(), true);
        return false;
      }
    }

    try
    {
      XstPropertyFileNormalizer.normalizeFile(this.fileName);
    } catch (IOException e1) {
      this.handler.logOutput("Error normalizing property file!", true);
      this.handler.logOutput(e1.getMessage(), true);
      return false;
    }
    

    PropertiesConfiguration config = new PropertiesConfiguration();
    try
    {
      config.read(new FileReader(this.fileName));
    } catch (Exception e) {
      this.handler.logOutput("Error reading in property file!", true);
      this.handler.logOutput(e.getStackTrace().toString(), true);
      e.printStackTrace();
      return false;
    }
    

    for (String key : this.keyValues.keySet())
    {


      if (this.keyValues.get(key) == null) {
        config.clearProperty(key);
      }
      else
      {
        try
        {
          String replacedVal = this.variables.replace((String)this.keyValues.get(key));
          
          this.keyValues.put(key, replacedVal);
        }
        catch (Exception e) {}
        


        if (this.xstEncryptValues.contains(key)) {
          String value = (String)this.keyValues.get(key);
          
          if ((value != null) && (!value.equals(""))) {
            this.handler.logOutput("Xstore encrypting value for property: " + key, false);
            XstEncryptor encryptor = new XstEncryptor(value);
            value = encryptor.getEncString();
          }
          
          this.handler.logOutput("Setting property: " + key, false);
          config.setProperty(key, value);
        } else {
          this.handler.logOutput("Setting property: " + key, false);
          config.setProperty(key, this.keyValues.get(key));
        }
        
        this.handler.logOutput("\n", false);
      }
    }
    

    try
    {
      config.write(new FileWriter(this.fileName));
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    
    return true;
  }

  public static class XstEncryptor
  {
    String xstoreInstallDir = "c:/xstore";
    String xstJavaDir = "c:/jre";
    String xstEnvJavaDir = "c:/environment/runtime/win32/jre";
    String rawString;
    String encString;
    
    public XstEncryptor(String rawString)
    {
      this.rawString = rawString;
      

      File javaNewCommand = new File(this.xstJavaDir + "/bin/java.exe");
      File javaOldCommand = new File(this.xstEnvJavaDir + "/bin/java.exe");
      
      File javaCommand = null;
      if (javaOldCommand.exists()) {
        javaCommand = javaOldCommand;
      } else {
        javaCommand = javaNewCommand;
      }
      

      File libDir = new File(this.xstoreInstallDir + "/lib");
      File libExtDir = new File(this.xstoreInstallDir + "/lib/ext");
      

      StringBuilder cpStringBuild = new StringBuilder();
      

      for (File file : libDir.listFiles()) {
        cpStringBuild.append(file.getAbsolutePath());
        cpStringBuild.append(';');
      }
      

      for (File file : libExtDir.listFiles()) {
        cpStringBuild.append(file.getAbsolutePath());
        cpStringBuild.append(';');
      }
      

      ProcessBuilder pb = new ProcessBuilder(new String[] { javaCommand.getAbsolutePath(), "-cp", cpStringBuild.toString(), "dtv.util.config.EncryptedStringConfig", rawString });
      
      try
      {
        pb.redirectErrorStream(false);

        pb.directory(new File(this.xstoreInstallDir));

        Process p = pb.start();

        BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
        

        String s = null;
        while ((s = stdInput.readLine()) != null) {
          this.encString += s;
        }
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    }
    
    public String getEncString() {
      if ((this.encString == null) || (this.encString.equalsIgnoreCase(""))) {
        return "";
      }
      String encStringStripped = this.encString.substring(this.encString.indexOf("Pj4"));
      return encStringStripped;
    }
    
    public String getRawString() {
      return this.rawString;
    }
  }
  
  public String getProcessableName()
  {
    return "Set Property File";
  }
}

