/*    */ package com.izforge.izpack.panels.xstprocess.specprocessors;
/*    */ 
/*    */ import com.izforge.izpack.api.adaptator.IXMLElement;
/*    */ import com.izforge.izpack.panels.xstprocess.Processable;
/*    */ import com.izforge.izpack.panels.xstprocess.processables.RunDatabaseScript;
/*    */ import com.izforge.izpack.panels.xstprocess.processables.RunDatabaseScript.SqlLine;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Properties;
/*    */ import java.util.logging.Logger;
/*    */ 
/*    */ public class RunDatabaseScriptSpecProcessor
/*    */   extends SpecProcessor
/*    */ {
/* 15 */   Logger logger = Logger.getLogger(RunDatabaseScriptSpecProcessor.class.getName());
/*    */   
/*    */   public boolean isApplicable(IXMLElement element)
/*    */   {
/* 19 */     return element.getName().equals("rundbscript");
/*    */   }
/*    */   
/*    */   public Processable processElement(IXMLElement element)
/*    */   {
/* 24 */     String database = element.getAttribute("dbtype");
/* 25 */     String databaseUrl = element.getAttribute("connectionurl");
/* 26 */     String dbUserName = element.getAttribute("username");
/* 27 */     String dbPassword = element.getAttribute("password");
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 32 */     if (database == null) {
/* 33 */       this.logger.severe("No database type specified, can not create database run script");
/* 34 */       return null; }
/* 35 */     String dbDriver; if (database.equalsIgnoreCase("MSSQL")) {
/* 36 */       dbDriver = "net.sourceforge.jtds.jdbc.Driver"; } else { String dbDriver;
/* 37 */       if (database.equalsIgnoreCase("ORACLE")) {
/* 38 */         dbDriver = "oracle.jdbc.driver.OracleDriver";
/*    */       } else {
/* 40 */         this.logger.severe("Invalid database type sepcified:" + database);
/* 41 */         return null;
/*    */       }
/*    */     }
/*    */     String dbDriver;
/* 45 */     Properties dbProperties = new Properties();
/*    */     
/*    */ 
/* 48 */     if ((dbUserName != null) || (dbPassword != null)) {
/* 49 */       if ((dbUserName == null) || (dbPassword == null)) {
/* 50 */         this.logger.severe("Error creating RunDatabaseScript, both username and password must be supplied, when one or the other is.");
/*    */         
/* 52 */         return null;
/*    */       }
/* 54 */       dbProperties.setProperty("user", dbUserName);
/* 55 */       dbProperties.setProperty("password", dbPassword);
/*    */     }
/*    */     
/*    */ 
/* 59 */     List<RunDatabaseScript.SqlLine> sqlStatements = new ArrayList();
/* 60 */     for (IXMLElement sqlStatement : element.getChildrenNamed("sqlstatement")) {
/* 61 */       String resultVar = sqlStatement.getAttribute("resultvar");
/*    */       
/* 63 */       RunDatabaseScript.SqlLine sqlLine = new RunDatabaseScript.SqlLine(sqlStatement.getContent(), resultVar);
/* 64 */       sqlStatements.add(sqlLine);
/*    */     }
/*    */     
/* 67 */     return new RunDatabaseScript(dbDriver, databaseUrl, dbProperties, sqlStatements);
/*    */   }
/*    */ }


/* Location:              D:\stage-installer-01-30-2023\postghost\installer\JCG-Xstore17-InstallerCopy.jar!\com\izforge\izpack\panels\xstprocess\specprocessors\RunDatabaseScriptSpecProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */