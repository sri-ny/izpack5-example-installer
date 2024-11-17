package com.izforge.izpack.panels.xstprocess.processables;

import com.izforge.izpack.api.data.Variables;

public class TrimLeadingZeroes extends com.izforge.izpack.panels.xstprocess.Processable
{
  String variableName;
  String variableValue;
  String strPattern = "^0+(?!$)";
  
  public TrimLeadingZeroes(String variableName, String variableValue) {
    this.variableName = variableName;
    this.variableValue = variableValue;
  }
  
  public boolean run()
  {
    this.variableValue = this.variables.replace(this.variableValue);
    

    String newVariableValue = this.variableValue.replaceAll(this.strPattern, "");
    

    this.variables.set(this.variableName, newVariableValue);
    
    this.handler.logOutput("Trimming zeros from " + this.variableValue + " , Result: " + newVariableValue, false);
    this.handler.logOutput("Setting " + newVariableValue + " in izpack variable " + this.variableName, false);
    
    return true;
  }
  
  public String getProcessableName()
  {
    return "TrimLeadingZeroes";
  }
}

