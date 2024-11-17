package com.izforge.izpack.panels.xstprocess.processables;

import com.izforge.izpack.panels.xstprocess.AbstractUIProcessHandler;

public class WriteRegistryValue extends com.izforge.izpack.panels.xstprocess.Processable { String regPath;
  String regKey;
  String regValue;
  RegValueType regValueType;
  
  public static enum RegValueType { REG_BINARY, 
    REG_DWORD, 
    REG_EXPAND_SZ, 
    REG_MULTI_SZ, 
    REG_QWORD, 
    REG_SZ;
    

    private RegValueType() {}
  }
  

  public WriteRegistryValue(String regPath, String regKey, String regValue, RegValueType regValueType)
  {
    this.regPath = regPath;
    this.regKey = regKey;
    this.regValue = regValue;
    this.regValueType = regValueType;
  }
  
  public boolean run()
  {
    this.regPath = this.variables.replace(this.regPath);
    this.regValue = this.variables.replace(this.regValue);
    

    this.handler.logOutput("\nSetting: " + this.regPath + " " + this.regKey + " to value: " + this.regValue, false);
    

    ProcessBuilder systemPathPb = new ProcessBuilder(new String[] { "reg.exe", "add", this.regPath, "/v", this.regKey, "/t", this.regValueType.toString(), "/d", this.regValue, "/f" });
    










    systemPathPb.redirectErrorStream(false);
    Process setReg;
    try
    {
      setReg = systemPathPb.start();
    } catch (java.io.IOException e) {
      this.handler.logOutput("Error setting registry value.", true);
      e.printStackTrace();
      return false;
    }
    try
    {
      setReg.waitFor();
    }
    catch (InterruptedException e) {
      e.printStackTrace();
    }
    
    int returnVal = setReg.exitValue();
    if (returnVal != 0) {
      this.handler.logOutput("Command failed, returned value: " + returnVal, true);
      return false;
    }
    
    this.handler.logOutput("Success setting value.\n", false);
    return true;
  }
  

  public String getProcessableName()
  {
    return "Write Registry Value";
  }
}

