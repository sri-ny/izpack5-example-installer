package com.izforge.izpack.panels.xstprocess.processables;

import com.izforge.izpack.api.data.Variables;

public class EscapeString extends com.izforge.izpack.panels.xstprocess.Processable
{
  String characterToEscape = "";
  String escapeWith = "";
  String escapeString = "";
  String setVarible = "";
  
  public EscapeString(String characterToEscape, String escapeWith, String escapeString, String setVariable)
  {
    this.characterToEscape = characterToEscape;
    this.escapeWith = escapeWith;
    this.escapeString = escapeString;
    this.setVarible = setVariable;
  }
  

  public boolean run()
  {
    this.characterToEscape = this.variables.replace(this.characterToEscape);
    this.escapeWith = this.variables.replace(this.escapeWith);
    this.escapeString = this.variables.replace(this.escapeString);
    this.setVarible = this.variables.replace(this.setVarible);
    

    this.handler.logOutput("Character escaping: " + this.characterToEscape, false);
    this.handler.logOutput("Escaping with: " + this.escapeWith, false);
    this.handler.logOutput("String escaping: " + this.escapeString, false);
    this.handler.logOutput("Setting final result in variable: " + this.setVarible, false);
    

    String finalString = "";
    finalString = this.escapeString.replaceAll(this.characterToEscape, this.escapeWith + this.characterToEscape);
    
    this.handler.logOutput("Final result: " + finalString, false);
    
    this.variables.set(this.setVarible, finalString);
    
    return true;
  }
  
  public String getProcessableName()
  {
    return "Escape String";
  }
}

