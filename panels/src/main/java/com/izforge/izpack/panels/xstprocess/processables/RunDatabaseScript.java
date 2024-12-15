package com.izforge.izpack.panels.xstprocess.processables;

import com.izforge.izpack.api.data.Variables;
import com.izforge.izpack.panels.xstprocess.AbstractUIProcessHandler;
import com.izforge.izpack.panels.xstprocess.Processable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RunDatabaseScript
  extends Processable
{
  String jdbcDriver;
  String jdbcConnection;
  List<SqlLine> sqlScript;
  Properties connectionProperties;
  Logger logger = Logger.getLogger(RunDatabaseScript.class.getName());
  
  public RunDatabaseScript(String jdbcDriver, String jdbcConnection, Properties connectionProperties, List<SqlLine> sqlScript) {
    this.jdbcDriver = jdbcDriver;
    this.jdbcConnection = jdbcConnection;
    this.connectionProperties = connectionProperties;
    this.sqlScript = sqlScript;
  }
  



  public boolean run()
  {
    try
    {
      Class.forName(this.jdbcDriver);
    } catch (ClassNotFoundException e) {
      this.logger.severe("Error registering driver: " + this.jdbcDriver);
      this.logger.log(Level.SEVERE, e.getMessage(), e);
      this.handler.logOutput("Error registering DB driver! - " + this.jdbcDriver, true);
      return false;
    }
    


    this.jdbcConnection = this.variables.replace(this.jdbcConnection);
    

    for (Object keyObj : this.connectionProperties.keySet()) {
      String key = (String)keyObj;
      String property = this.connectionProperties.getProperty(key);
      property = this.variables.replace(property);
      this.connectionProperties.setProperty(key, property);
    }
    Connection dbConnection;
    try {
      this.handler.logOutput("Opening Sql connection to: " + this.jdbcConnection, false);
      dbConnection = DriverManager.getConnection(this.jdbcConnection, this.connectionProperties);
    } catch (SQLException e) {
      this.logger.severe("Error getting connection to DB!");
      this.logger.log(Level.SEVERE, e.getMessage(), e);
      this.handler.logOutput("Error getting connection to DB - " + this.jdbcConnection, true);
      this.handler.logOutput(e.getMessage(), true);
      return false;
    }
    

    this.handler.logOutput("\nStarting execution of sql lines:", false);
    

    for (SqlLine line : this.sqlScript) {
      Statement sqlStatement;
      try {
        sqlStatement = dbConnection.createStatement();
      } catch (SQLException e) {
        this.logger.severe("Error creating sql statement in DB");
        this.logger.log(Level.SEVERE, e.getMessage(), e);
        this.handler.logOutput("Error creating sql statement in DB", true);
        this.handler.logOutput(e.getMessage(), true);
        return false;
      }
      

      String sqlLine = this.variables.replace(line.getSqlStatement());
      


      ResultSet results = null;
      try {
        this.handler.logOutput("Executing: " + sqlLine, false);
        
        if (line.wantsResults()) {
          results = sqlStatement.executeQuery(sqlLine);
        } else {
          sqlStatement.execute(sqlLine);
        }
      }
      catch (SQLException e) {
        this.logger.severe("Error running sql statement:\n" + sqlLine);
        this.logger.log(Level.SEVERE, e.getMessage(), e);
        this.handler.logOutput("Error running sql statement:\n" + sqlLine, true);
        this.handler.logOutput(e.getMessage(), true);
        return false;
      }
      

      if (line.wantsResults())
      {
        String resultVar = line.getResultVar();
        int rowCount = 0;
        

        HashMap<String, String> loadedResults = new HashMap();
        

        try
        {
          int columnCount = results.getMetaData().getColumnCount();
          

          List<String> columnNames = new ArrayList();
          for (int i = 1; i <= columnCount; i++) {
            String columnName = results.getMetaData().getColumnLabel(i);
            columnNames.add(columnName);
          }
          

          while (results.next()) {
            for (int i = 1; i <= columnCount; i++) {
              String columnValue = results.getString(i);
              String uniqueVarName = resultVar + rowCount + "_" + (String)columnNames.get(i - 1);
              loadedResults.put(uniqueVarName, columnValue);
              rowCount++;
            }
          }
          

          String rowCountVar = resultVar + "_" + rowCount;
          this.variables.set(rowCountVar, "" + rowCount);
          this.handler.logOutput("Row count of " + rowCount + " set in " + rowCountVar, false);
          

          for (String key : loadedResults.keySet()) {
            String hashValue = (String)loadedResults.get(key);
            this.variables.set(key, hashValue);
            this.handler.logOutput(key + " = " + hashValue, false);
          }
        }
        catch (SQLException e) {
          this.handler.logOutput("Error loading results....", true);
          e.printStackTrace();
          return false;
        }
      }
    }
    


    return true;
  }
  




  public String getProcessableName()
  {
    return "Run Database Script";
  }
  
  public static class SqlLine
  {
    String sqlStatement;
    String resultVar;
    
    public SqlLine(String sqlStatement, String resultVar)
    {
      this.sqlStatement = sqlStatement;
      

      if ((resultVar == null) || (resultVar.isEmpty())) {
        resultVar = null;
      } else {
        this.resultVar = resultVar;
      }
    }
    
    public String getSqlStatement()
    {
      return this.sqlStatement;
    }
    
    public String getResultVar() {
      return this.resultVar;
    }
    
    public boolean wantsResults() {
      return this.resultVar != null;
    }
  }
}

