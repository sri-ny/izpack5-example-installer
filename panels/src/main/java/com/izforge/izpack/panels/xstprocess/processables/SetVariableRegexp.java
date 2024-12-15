package com.izforge.izpack.panels.xstprocess.processables;

import com.izforge.izpack.api.data.Variables;
import com.izforge.izpack.panels.xstprocess.AbstractUIProcessHandler;
import com.izforge.izpack.panels.xstprocess.Processable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SetVariableRegexp extends Processable
{
  String regularExpression;
  String inputString;
  String setString;
  String setVariable;
  boolean failOnNoMatch = false;
  
  public SetVariableRegexp(String regularExpression, String inputString, String setString, String setVariable, boolean failOnNoMatch) {
    this.regularExpression = regularExpression;
    this.inputString = inputString;
    this.setString = setString;
    this.setVariable = setVariable;
    this.failOnNoMatch = failOnNoMatch;
  }
  



  public boolean run()
  {
    this.inputString = this.variables.replace(this.inputString);
    this.setString = this.variables.replace(this.setString);
    this.setVariable = this.variables.replace(this.setVariable);
    
    this.handler.logOutput("String to match against: " + this.inputString, false);
    this.handler.logOutput("String to set variable to after regexp: " + this.setString, false);
    this.handler.logOutput("Variable to set result to: " + this.setVariable, false);
    
    Pattern regExpPattern = Pattern.compile(this.regularExpression);
    Matcher matcher = regExpPattern.matcher(this.inputString);
    

    boolean matchFound = matcher.matches();
    

    if ((!matchFound) && (this.failOnNoMatch)) {
      this.variables.set(this.setVariable, "");
      this.handler.logOutput("No match found at all, fail on no match set....failing....", true);
      return false; }
    if ((!matchFound) && (!this.failOnNoMatch)) {
      this.variables.set(this.setVariable, this.setString);
      this.handler.logOutput("No match found at all, fail on no match set to false.\nSetting variable to set string: " + this.setString, true);
      return true;
    }
    
    this.handler.logOutput("\nMatch found!\n", false);
    

    int groupCount = matcher.groupCount();
    this.handler.logOutput("\nMatching groups found in pattern: " + groupCount + "\n", false);
    

    for (int i = 1; i <= groupCount; i++) {
      String groupMatch = matcher.group(i);
      
      if (groupMatch == null) {
        this.handler.logOutput("No match found for match group " + i + ", not replacing. ", true);
      }
      else
      {
        this.handler.logOutput("Replacing all \\" + i + " with " + groupMatch, false);
        this.setString = this.setString.replaceAll("\\\\" + i, groupMatch);
      }
    }
    

    this.setString = this.setString.replaceAll("\\\\\\d", "");
    

    this.variables.set(this.setVariable, this.setString);
    this.handler.logOutput("\nAssigning Final string to variable '" + this.setVariable + "' : " + this.setString, false);
    
    return true;
  }
  
  public String getProcessableName()
  {
    return "Set variable regular expression.";
  }
}

