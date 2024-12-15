package com.izforge.izpack.panels.xstprocess.specprocessors;

import com.izforge.izpack.api.adaptator.IXMLElement;
import com.izforge.izpack.panels.xstprocess.Processable;
import com.izforge.izpack.panels.xstprocess.processables.SetVariable;
import com.izforge.izpack.panels.xstprocess.processables.SetVariable.CaseTransform;
import com.izforge.izpack.panels.xstprocess.processables.SetVariable.PadSide;
import com.izforge.izpack.panels.xstprocess.processables.SetVariable.PadType;

public class SetVariableSpecProcessor extends SpecProcessor
{
  public boolean isApplicable(IXMLElement element)
  {
    return element.getName().equals("setvariable");
  }
  
  public Processable processElement(IXMLElement element)
  {
    String variableName = element.getAttribute("variable");
    if ((variableName == null) || (variableName.equals(""))) {
      return null;
    }
    
    String variableValue = element.getAttribute("value");
    if (variableValue == null) {
      return null;
    }
    
    SetVariable setVariableProcessable = null;
    

    String padType = element.getAttribute("paddingtype");
    String mathOperation = element.getAttribute("mathoperation");
    String mathValue = element.getAttribute("mathvalue");
    if (padType != null)
    {

      boolean padTypeValid = (padType.equalsIgnoreCase(SetVariable.PadType.MAXSTRINGSIZE.toString())) || (padType.equalsIgnoreCase(SetVariable.PadType.MAXSTRINGSIZETRIM.toString())) || (padType.equalsIgnoreCase(SetVariable.PadType.PADNUMTIMES.toString()));
      
      if (!padTypeValid) {
        return null;
      }
      
      String padSide = element.getAttribute("paddingside");
      boolean padSideValid = (padSide.equalsIgnoreCase(SetVariable.PadSide.LEFT.toString())) || (padSide.equalsIgnoreCase(SetVariable.PadSide.RIGHT.toString()));
      
      if (!padSideValid) {
        return null;
      }
      

      String padSize = element.getAttribute("paddingsize");
      Integer padSizeInt;
      try {
        padSizeInt = Integer.valueOf(Integer.parseInt(padSize));
      } catch (NumberFormatException ex) {
        return null;
      }
     

      String padChar = element.getAttribute("paddingchar");
      if (padChar != null)
      {
        if (padChar.length() != 1) {
          return null;
        }
        
        setVariableProcessable = new SetVariable(variableName, variableValue, SetVariable.PadType.valueOf(padType), SetVariable.PadSide.valueOf(padSide), padSizeInt.intValue(), padChar.toCharArray()[0]);
      }
      
      setVariableProcessable = new SetVariable(variableName, variableValue, SetVariable.PadType.valueOf(padType), SetVariable.PadSide.valueOf(padSide), padSizeInt.intValue());

    }
    else
    {

      if (mathOperation != null)
      {
        setVariableProcessable = new SetVariable(variableName, variableValue, com.izforge.izpack.panels.xstprocess.processables.SetVariable.MathOperation.valueOf(mathOperation), mathValue);
        return setVariableProcessable;
      }
      
      setVariableProcessable = new SetVariable(variableName, variableValue);
    }
    
    String caseTransform = element.getAttribute("casetransform");
    
    if (caseTransform == null) {
      setVariableProcessable.setCaseTransform(SetVariable.CaseTransform.NONE);
    }
    else if (caseTransform.equalsIgnoreCase(SetVariable.CaseTransform.UPPER.toString())) {
      setVariableProcessable.setCaseTransform(SetVariable.CaseTransform.UPPER);
    }
    else if (caseTransform.equalsIgnoreCase(SetVariable.CaseTransform.LOWER.toString())) {
      setVariableProcessable.setCaseTransform(SetVariable.CaseTransform.LOWER);
    }
    else {
      setVariableProcessable.setCaseTransform(SetVariable.CaseTransform.NONE);
    }
    return setVariableProcessable;
  }
}

