package com.izforge.izpack.panels.xstprocess.processables;

import com.izforge.izpack.api.data.Variables;
import com.izforge.izpack.panels.xstprocess.AbstractUIProcessHandler;
import com.izforge.izpack.panels.xstprocess.Processable;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

public class ReadCsvFile extends Processable
{
  protected String csvDelimiter = "|";
  
  protected String csvFile;
  
  protected HashMap<Integer, String> columnsToMatch;
  protected HashMap<Integer, String> columnsToRead;
  Logger logger = Logger.getLogger(ReadCsvFile.class.getName());
  
  public ReadCsvFile(String csvFile, HashMap<Integer, String> columnsToMatch, HashMap<Integer, String> columnsToRead) {
    this.csvFile = csvFile;
    this.columnsToMatch = columnsToMatch;
    this.columnsToRead = columnsToRead;
  }
  
  public ReadCsvFile(String csvFile, HashMap<Integer, String> columnsToMatch, HashMap<Integer, String> columnsToRead, String csvDelimiter) {
    this.csvFile = csvFile;
    this.columnsToMatch = columnsToMatch;
    this.columnsToRead = columnsToRead;
    this.csvDelimiter = csvDelimiter;
  }
  
  public boolean run()
  {
    this.csvFile = this.variables.replace(this.csvFile);
    this.handler.logOutput("Starting CSV Read of file: " + this.csvFile, false);
    
    File csvFileFile = new File(this.csvFile);
    if (!csvFileFile.exists()) {
      this.logger.severe("CSV File does not exist! - " + csvFileFile.getAbsolutePath());
      this.handler.logOutput("CSV File does not exist! - " + csvFileFile.getAbsolutePath(), true);
      return false;
    }
    


    for (Integer key : this.columnsToMatch.keySet()) {
      String oldValue = (String)this.columnsToMatch.get(key);
      this.columnsToMatch.put(key, this.variables.replace(oldValue));
    }
    


    BufferedReader csvReader = null;
    try {
      csvReader = new BufferedReader(new java.io.FileReader(csvFileFile));
    } catch (FileNotFoundException e) {
      this.logger.severe("Error reading CSV File! - " + csvFileFile.getAbsolutePath());
      this.logger.severe(e.getMessage());
      this.handler.logOutput("Error reading CSV File!", true);
      e.printStackTrace();
      return false;
    }
    

    ArrayList<CsvFileLine> lines = new ArrayList();
    String theLine = "";
    try {
      while ((theLine = csvReader.readLine()) != null) {
        lines.add(new CsvFileLine(theLine, this.csvDelimiter));
      }
    } catch (IOException e) {
      this.logger.severe("Failure reading csv, skipping....");
      this.logger.severe(e.getMessage());
      this.handler.logOutput("Failure during CSV read!", true);
      return false;
    }
    
    this.handler.logOutput("\nRead in " + lines.size() + " lines from csv file\n", false);
    

    this.handler.logOutput("Looking for the following in file:", false);
    for (Integer key : this.columnsToMatch.keySet()) {
      this.handler.logOutput("Column number: " + key + " to match value: " + (String)this.columnsToMatch.get(key), false);
    }
    

    CsvFileLine matchedLine = findFirstMatch(lines, this.columnsToMatch);
    if (matchedLine == null) {
      this.handler.logOutput("\nNO MATCH FOUND!", true);
      this.logger.severe("Could not find a matching line....");
      try
      {
        csvReader.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
      
      return false;
    }
    
    this.handler.logOutput("\nMatched line found:\n" + matchedLine.line, false);
    

    this.handler.logOutput("\nSetting IZPACK variables:\n", false);
    for (Integer columnNum : this.columnsToRead.keySet()) {
      String value = matchedLine.getValue(columnNum.intValue());
      String varName = (String)this.columnsToRead.get(columnNum);
      
      this.logger.info("Setting variable " + varName + " to: " + value);
      this.handler.logOutput(varName + " = " + value, false);
      this.variables.set(varName, value);
      this.handler.logOutput("Izpack Variable:   " + varName + " = " + value, false);
    }
    try
    {
      csvReader.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    
    return true;
  }
  
  protected CsvFileLine findFirstMatch(List<CsvFileLine> lines, HashMap<Integer, String> toMatch)
  {
    for (CsvFileLine line : lines) {
      if (line.columnsContain(toMatch)) {
        return line;
      }
    }
    return null;
  }
  
  protected class CsvFileLine
  {
    List<String> values = new ArrayList();
    String line;
    String delimiter;
    
    public CsvFileLine(String line, String delimiter)
    {
      this.line = line;
      this.delimiter = delimiter;
      String[] splitline;
      String[] splitline;
      if (delimiter.equals("|")) {
        splitline = line.split("\\|", -1);
      } else {
        splitline = line.split(delimiter, -1);
      }
      

      this.values.addAll(Arrays.asList(splitline));
    }
    
    public int getValueCount() {
      return this.values.size();
    }
    
    public boolean columnContains(int column, String value) {
      return ((String)this.values.get(column)).equals(value);
    }
    
    public boolean columnsContain(HashMap<Integer, String> values) {
      for (Integer key : values.keySet())
      {
        if (!columnContains(key.intValue(), (String)values.get(key))) {
          return false;
        }
      }
      return true;
    }
    
    public String getValue(int column) {
      return (String)this.values.get(column);
    }
  }
  



  public String getProcessableName()
  {
    return "Read CSV File";
  }
}

