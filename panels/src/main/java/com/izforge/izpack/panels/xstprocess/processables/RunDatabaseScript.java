/*     */ package com.izforge.izpack.panels.xstprocess.processables;
/*     */ 
/*     */ import com.izforge.izpack.api.data.Variables;
/*     */ import com.izforge.izpack.panels.xstprocess.AbstractUIProcessHandler;
/*     */ import com.izforge.izpack.panels.xstprocess.Processable;
/*     */ import java.sql.Connection;
/*     */ import java.sql.DriverManager;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.ResultSetMetaData;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Statement;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Properties;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ public class RunDatabaseScript
/*     */   extends Processable
/*     */ {
/*     */   String jdbcDriver;
/*     */   String jdbcConnection;
/*     */   List<SqlLine> sqlScript;
/*     */   Properties connectionProperties;
/*  26 */   Logger logger = Logger.getLogger(RunDatabaseScript.class.getName());
/*     */   
/*     */   public RunDatabaseScript(String jdbcDriver, String jdbcConnection, Properties connectionProperties, List<SqlLine> sqlScript) {
/*  29 */     this.jdbcDriver = jdbcDriver;
/*  30 */     this.jdbcConnection = jdbcConnection;
/*  31 */     this.connectionProperties = connectionProperties;
/*  32 */     this.sqlScript = sqlScript;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean run()
/*     */   {
/*     */     try
/*     */     {
/*  42 */       Class.forName(this.jdbcDriver);
/*     */     } catch (ClassNotFoundException e) {
/*  44 */       this.logger.severe("Error registering driver: " + this.jdbcDriver);
/*  45 */       this.logger.log(Level.SEVERE, e.getMessage(), e);
/*  46 */       this.handler.logOutput("Error registering DB driver! - " + this.jdbcDriver, true);
/*  47 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  52 */     this.jdbcConnection = this.variables.replace(this.jdbcConnection);
/*     */     
/*     */ 
/*  55 */     for (Object keyObj : this.connectionProperties.keySet()) {
/*  56 */       String key = (String)keyObj;
/*  57 */       String property = this.connectionProperties.getProperty(key);
/*  58 */       property = this.variables.replace(property);
/*  59 */       this.connectionProperties.setProperty(key, property);
/*     */     }
/*     */     Connection dbConnection;
/*     */     try {
/*  63 */       this.handler.logOutput("Opening Sql connection to: " + this.jdbcConnection, false);
/*  64 */       dbConnection = DriverManager.getConnection(this.jdbcConnection, this.connectionProperties);
/*     */     } catch (SQLException e) {
/*  66 */       this.logger.severe("Error getting connection to DB!");
/*  67 */       this.logger.log(Level.SEVERE, e.getMessage(), e);
/*  68 */       this.handler.logOutput("Error getting connection to DB - " + this.jdbcConnection, true);
/*  69 */       this.handler.logOutput(e.getMessage(), true);
/*  70 */       return false;
/*     */     }
/*     */     
/*     */ 
/*  74 */     this.handler.logOutput("\nStarting execution of sql lines:", false);
/*     */     
/*     */ 
/*  77 */     for (SqlLine line : this.sqlScript) {
/*     */       Statement sqlStatement;
/*     */       try {
/*  80 */         sqlStatement = dbConnection.createStatement();
/*     */       } catch (SQLException e) {
/*  82 */         this.logger.severe("Error creating sql statement in DB");
/*  83 */         this.logger.log(Level.SEVERE, e.getMessage(), e);
/*  84 */         this.handler.logOutput("Error creating sql statement in DB", true);
/*  85 */         this.handler.logOutput(e.getMessage(), true);
/*  86 */         return false;
/*     */       }
/*     */       
/*     */ 
/*  90 */       String sqlLine = this.variables.replace(line.getSqlStatement());
/*     */       
/*     */ 
/*     */ 
/*  94 */       ResultSet results = null;
/*     */       try {
/*  96 */         this.handler.logOutput("Executing: " + sqlLine, false);
/*     */         
/*  98 */         if (line.wantsResults()) {
/*  99 */           results = sqlStatement.executeQuery(sqlLine);
/*     */         } else {
/* 101 */           sqlStatement.execute(sqlLine);
/*     */         }
/*     */       }
/*     */       catch (SQLException e) {
/* 105 */         this.logger.severe("Error running sql statement:\n" + sqlLine);
/* 106 */         this.logger.log(Level.SEVERE, e.getMessage(), e);
/* 107 */         this.handler.logOutput("Error running sql statement:\n" + sqlLine, true);
/* 108 */         this.handler.logOutput(e.getMessage(), true);
/* 109 */         return false;
/*     */       }
/*     */       
/*     */ 
/* 113 */       if (line.wantsResults())
/*     */       {
/* 115 */         String resultVar = line.getResultVar();
/* 116 */         int rowCount = 0;
/*     */         
/*     */ 
/* 119 */         HashMap<String, String> loadedResults = new HashMap();
/*     */         
/*     */ 
/*     */         try
/*     */         {
/* 124 */           int columnCount = results.getMetaData().getColumnCount();
/*     */           
/*     */ 
/* 127 */           List<String> columnNames = new ArrayList();
/* 128 */           for (int i = 1; i <= columnCount; i++) {
/* 129 */             String columnName = results.getMetaData().getColumnLabel(i);
/* 130 */             columnNames.add(columnName);
/*     */           }
/*     */           
/*     */ 
/* 134 */           while (results.next()) {
/* 135 */             for (int i = 1; i <= columnCount; i++) {
/* 136 */               String columnValue = results.getString(i);
/* 137 */               String uniqueVarName = resultVar + rowCount + "_" + (String)columnNames.get(i - 1);
/* 138 */               loadedResults.put(uniqueVarName, columnValue);
/* 139 */               rowCount++;
/*     */             }
/*     */           }
/*     */           
/*     */ 
/* 144 */           String rowCountVar = resultVar + "_" + rowCount;
/* 145 */           this.variables.set(rowCountVar, "" + rowCount);
/* 146 */           this.handler.logOutput("Row count of " + rowCount + " set in " + rowCountVar, false);
/*     */           
/*     */ 
/* 149 */           for (String key : loadedResults.keySet()) {
/* 150 */             String hashValue = (String)loadedResults.get(key);
/* 151 */             this.variables.set(key, hashValue);
/* 152 */             this.handler.logOutput(key + " = " + hashValue, false);
/*     */           }
/*     */         }
/*     */         catch (SQLException e) {
/* 156 */           this.handler.logOutput("Error loading results....", true);
/* 157 */           e.printStackTrace();
/* 158 */           return false;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 165 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getProcessableName()
/*     */   {
/* 174 */     return "Run Database Script";
/*     */   }
/*     */   
/*     */   public static class SqlLine
/*     */   {
/*     */     String sqlStatement;
/*     */     String resultVar;
/*     */     
/*     */     public SqlLine(String sqlStatement, String resultVar)
/*     */     {
/* 184 */       this.sqlStatement = sqlStatement;
/*     */       
/*     */ 
/* 187 */       if ((resultVar == null) || (resultVar.isEmpty())) {
/* 188 */         resultVar = null;
/*     */       } else {
/* 190 */         this.resultVar = resultVar;
/*     */       }
/*     */     }
/*     */     
/*     */     public String getSqlStatement()
/*     */     {
/* 196 */       return this.sqlStatement;
/*     */     }
/*     */     
/*     */     public String getResultVar() {
/* 200 */       return this.resultVar;
/*     */     }
/*     */     
/*     */     public boolean wantsResults() {
/* 204 */       return this.resultVar != null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\stage-installer-01-30-2023\postghost\installer\JCG-Xstore17-InstallerCopy.jar!\com\izforge\izpack\panels\xstprocess\processables\RunDatabaseScript.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */