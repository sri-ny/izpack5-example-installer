package com.izforge.izpack.panels.xstprocess.processables;

import com.izforge.izpack.panels.xstprocess.AbstractUIProcessHandler;

public class SetVariable extends com.izforge.izpack.panels.xstprocess.Processable
{
  public static enum PadType {
    MAXSTRINGSIZE, 
    MAXSTRINGSIZETRIM, 
    PADNUMTIMES;
    
    private PadType() {} }
  
  public static enum PadSide { LEFT, 
    RIGHT;
    
    private PadSide() {} }
  
  public static enum CaseTransform { UPPER, 
    LOWER, 
    NONE;
    
    private CaseTransform() {} }
  
  public static enum MathOperation { ADD, 
    SUBTRACT, 
    MULTIPLY, 
    DIVIDE;
    
    private MathOperation() {} }
  
  boolean padVariable = false;
  PadType padType = null;
  PadSide padSide = null;
  int padSize = 0;
  char padChar = '0';
  

  CaseTransform caseTransform = CaseTransform.NONE;
  
  MathOperation mathOperation;
  
  String mathValue;
  String variableName;
  String variableValue;
  
  public SetVariable(String variableName, String variableValue)
  {
    this.variableName = variableName;
    this.variableValue = variableValue;
  }
  
  public SetVariable(String variableName, String variableValue, MathOperation mathOperation, String mathValue) {
    this.variableName = variableName;
    this.variableValue = variableValue;
    this.mathOperation = mathOperation;
    this.mathValue = mathValue;
  }
  
  public SetVariable(String variableName, String variableValue, PadType padType, PadSide padSide, int padSize) {
    this.variableName = variableName;
    this.variableValue = variableValue;
    this.padType = padType;
    this.padSide = padSide;
    this.padSize = padSize;
    this.padVariable = true;
  }
  
  public SetVariable(String variableName, String variableValue, PadType padType, PadSide padSide, int padSize, char padChar) {
    this.variableName = variableName;
    this.variableValue = variableValue;
    this.padType = padType;
    this.padSide = padSide;
    this.padSize = padSize;
    this.padChar = padChar;
    this.padVariable = true;
  }
  
  public void setCaseTransform(CaseTransform caseTransform)
  {
    if (caseTransform == null) {
      caseTransform = CaseTransform.NONE;
    }
    
    this.caseTransform = caseTransform;
  }
  

  public boolean run()
  {
    this.variableValue = this.variables.replace(this.variableValue);
    
    if (this.padVariable) {
      String before = this.variableValue;
      this.handler.logOutput("Padding value enabled with character: " + this.padChar, false);
      
      if (this.padType == PadType.MAXSTRINGSIZE) {
        padMaxSize();
      } else if (this.padType == PadType.MAXSTRINGSIZETRIM) {
        padMaxSizeTrim();
      } else if (this.padType == PadType.PADNUMTIMES) {
        padNumTimes();
      }
      this.handler.logOutput("Value padded from '" + before + "' to '" + this.variableValue + "'", false);
    }
    

    if ((this.caseTransform != null) && (this.caseTransform.equals(CaseTransform.UPPER))) {
      String before = this.variableValue;
      this.variableValue = this.variableValue.toUpperCase();
      this.handler.logOutput("String transformed from '" + before + "' to '" + this.variableValue + "'", false);
    } else if ((this.caseTransform != null) && (this.caseTransform.equals(CaseTransform.LOWER))) {
      String before = this.variableValue;
      this.variableValue = this.variableValue.toLowerCase();
      this.handler.logOutput("String transformed from '" + before + "' to '" + this.variableValue + "'", false);
    }
    

    if (this.mathOperation != null) {
      this.mathValue = this.variables.replace(this.mathValue);
      int mathValueInt;
      int variableValueInt;
      try {
        mathValueInt = Integer.parseInt(this.mathValue);
        variableValueInt = Integer.parseInt(this.variableValue);
      } catch (Exception ex) {
        this.handler.logOutput("One of these values failed to be converted to Integer: Value: " + this.variableValue + " - mathValue: " + this.mathValue, true);
        return false;
      }
      

      if (this.mathOperation == MathOperation.ADD) {
        this.variableValue = Integer.toString(variableValueInt + mathValueInt);
      }
      
      if (this.mathOperation == MathOperation.SUBTRACT) {
        this.variableValue = Integer.toString(variableValueInt - mathValueInt);
      }
      
      if (this.mathOperation == MathOperation.MULTIPLY) {
        this.variableValue = Integer.toString(variableValueInt * mathValueInt);
      }
      try
      {
        if (this.mathOperation == MathOperation.DIVIDE) {
          this.variableValue = Integer.toString(variableValueInt / mathValueInt);
        }
      } catch (Exception ex) {
        this.handler.logOutput("Cannot divide by ZERO!", true);
        return false;
      }
    }
    

    this.handler.logOutput("Setting '" + this.variableName + "' to '" + this.variableValue + "'", false);
    this.variables.set(this.variableName, this.variableValue);
    return true;
  }
  
  protected void padNumTimes() {
    for (int i = 0; i < this.padSize; i++) {
      padOne();
    }
  }
  
  protected void padMaxSizeTrim() {
    if (this.variableValue.length() < this.padSize) {
      padMaxSize();
    } else if ((this.variableValue.length() > this.padSize) && (this.padSide == PadSide.LEFT)) {
      int startPos = this.variableValue.length() - this.padSize;
      this.variableValue = this.variableValue.substring(startPos, this.variableValue.length());
    } else if ((this.variableValue.length() > this.padSize) && (this.padSide == PadSide.RIGHT)) {
      this.variableValue = this.variableValue.substring(0, this.padSize);
    }
  }
  
  protected void padMaxSize()
  {
    if (this.variableValue.length() < this.padSize) {
      int sizeDiff = this.padSize - this.variableValue.length();
      for (int i = 0; i < sizeDiff; i++) {
        padOne();
      }
    }
  }
  
  protected void padOne() {
    if (this.padSide == PadSide.LEFT)
      leftPad();
    if (this.padSide == PadSide.RIGHT)
      rightPad();
  }
  
  protected void leftPad() {
    this.variableValue = (this.padChar + this.variableValue);
  }
  
  protected void rightPad() {
    this.variableValue += this.padChar;
  }
  
  public String getProcessableName()
  {
    return "Set Variable";
  }
}

