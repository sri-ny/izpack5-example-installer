package com.izforge.izpack.panels.xstprocess.specprocessors;

import com.izforge.izpack.api.adaptator.IXMLElement;
import com.izforge.izpack.panels.xstprocess.Processable;
import com.izforge.izpack.panels.xstprocess.processables.RunDatabaseScript;
import com.izforge.izpack.panels.xstprocess.processables.RunDatabaseScript.SqlLine;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

public class RunDatabaseScriptSpecProcessor
  extends SpecProcessor
{
  Logger logger = Logger.getLogger(RunDatabaseScriptSpecProcessor.class.getName());
  
  public boolean isApplicable(IXMLElement element)
  {
    return element.getName().equals("rundbscript");
  }
  
  public Processable processElement(IXMLElement element)
  {
    String database = element.getAttribute("dbtype");
    String databaseUrl = element.getAttribute("connectionurl");
    String dbUserName = element.getAttribute("username");
    String dbPassword = element.getAttribute("password");
    
    if (database == null) {
      this.logger.severe("No database type specified, can not create database run script");
      return null; }
    String dbDriver; if (database.equalsIgnoreCase("MSSQL")) {
      dbDriver = "net.sourceforge.jtds.jdbc.Driver"; } else { String dbDriver;
      if (database.equalsIgnoreCase("ORACLE")) {
        dbDriver = "oracle.jdbc.driver.OracleDriver";
      } else {
        this.logger.severe("Invalid database type sepcified:" + database);
        return null;
      }
    }
    String dbDriver;
    Properties dbProperties = new Properties();
    

    if ((dbUserName != null) || (dbPassword != null)) {
      if ((dbUserName == null) || (dbPassword == null)) {
        this.logger.severe("Error creating RunDatabaseScript, both username and password must be supplied, when one or the other is.");
        
        return null;
      }
      dbProperties.setProperty("user", dbUserName);
      dbProperties.setProperty("password", dbPassword);
    }
    

    List<RunDatabaseScript.SqlLine> sqlStatements = new ArrayList();
    for (IXMLElement sqlStatement : element.getChildrenNamed("sqlstatement")) {
      String resultVar = sqlStatement.getAttribute("resultvar");
      
      RunDatabaseScript.SqlLine sqlLine = new RunDatabaseScript.SqlLine(sqlStatement.getContent(), resultVar);
      sqlStatements.add(sqlLine);
    }
    
    return new RunDatabaseScript(dbDriver, databaseUrl, dbProperties, sqlStatements);
  }
}

