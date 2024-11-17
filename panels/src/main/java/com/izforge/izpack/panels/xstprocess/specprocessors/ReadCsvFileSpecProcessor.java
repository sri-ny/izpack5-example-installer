package com.izforge.izpack.panels.xstprocess.specprocessors;

import com.izforge.izpack.api.adaptator.IXMLElement;
import com.izforge.izpack.panels.xstprocess.Processable;
import com.izforge.izpack.panels.xstprocess.processables.ReadCsvFile;
import java.util.HashMap;

public class ReadCsvFileSpecProcessor
  extends SpecProcessor
{
  public boolean isApplicable(IXMLElement element)
  {
    return element.getName().equals("csvfileread");
  }
  
  public Processable processElement(IXMLElement element)
  {
    String csvFile = element.getAttribute("csvfile");
    
    if ((csvFile == null) || (csvFile.equals(""))) {
      return null;
    }
    
    HashMap<Integer, String> matchColumns = new HashMap();
    for (IXMLElement matchElement : element.getChildrenNamed("matchcolumn"))
    {

      String columnNum = matchElement.getAttribute("columnnum");
      String matchValue = matchElement.getAttribute("matchvalue");
      
      if ((columnNum == null) || (columnNum.equals(""))) {
        return null;
      }
      
      int columnNumInt;
      try
      {
        columnNumInt = Integer.parseInt(columnNum);
      }
      catch (NumberFormatException e) {
        return null;
      }
      

      if (matchValue == null) {
        return null;
      }
      
      matchColumns.put(Integer.valueOf(columnNumInt), matchValue);
    }
    
    HashMap<Integer, String> readColumns = new HashMap();
    for (IXMLElement matchElement : element.getChildrenNamed("readcolumn"))
    {

      String columnNum = matchElement.getAttribute("columnnum");
      String variableName = matchElement.getAttribute("variablename");
      

      if ((columnNum == null) || (columnNum.equals(""))) {
        return null;
      }
      
      int columnNumInt;
      try
      {
        columnNumInt = Integer.parseInt(columnNum);
      }
      catch (NumberFormatException e) {
        return null;
      }
      
      if ((variableName != null) && (!variableName.isEmpty()))
      {

        readColumns.put(Integer.valueOf(columnNumInt), variableName);
      }
    }
    
    String delimiter = element.getAttribute("delimiter", "|");
    

    return new ReadCsvFile(csvFile, matchColumns, readColumns, delimiter);
  }
}
