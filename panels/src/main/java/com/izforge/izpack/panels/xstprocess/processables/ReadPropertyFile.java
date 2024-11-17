/*     */ package com.izforge.izpack.panels.xstprocess.processables;
/*     */ 
/*     */ import com.izforge.izpack.api.data.Variables;
/*     */ import com.izforge.izpack.panels.xstprocess.Processable;
/*     */ import com.izforge.izpack.panels.xstprocess.util.XstPropertyFileNormalizer;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
/*     */ import java.util.logging.Logger;
/*     */ import org.apache.commons.configuration2.PropertiesConfiguration;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ReadPropertyFile
/*     */   extends Processable
/*     */ {
/*     */   protected String fileName;
/*     */   protected HashMap<String, String> keyVariables;
/*  20 */   Logger logger = Logger.getLogger(ReadPropertyFile.class.getName());
/*     */   
/*     */   public ReadPropertyFile(String fileName, HashMap<String, String> keyVariables) {
/*  23 */     this.fileName = fileName;
/*  24 */     this.keyVariables = keyVariables;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean run()
/*     */   {
/*  30 */     this.logger.finer("Attempting to read property file values from: " + this.fileName);
/*     */     
/*     */     try
/*     */     {
/*  34 */       this.fileName = this.variables.replace(this.fileName);
/*     */     } catch (Exception ex) {
/*  36 */       ex.printStackTrace();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  41 */     File propertyFileFile = new File(this.fileName);
/*  42 */     if (!propertyFileFile.exists()) {
/*  43 */       this.logger.severe("File set to read properties from does not exist: " + this.fileName);
/*  44 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     try
/*     */     {
/*  60 */       XstPropertyFileNormalizer.normalizeFile(this.fileName);
/*     */     } catch (IOException e1) {
/*  62 */       e1.printStackTrace();
/*  63 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  68 */     PropertiesConfiguration config = new PropertiesConfiguration();
/*     */     try
/*     */     {
/*  71 */       config.read(new FileReader(this.fileName));
/*     */     } catch (Exception e) {
/*  73 */       e.printStackTrace();
/*  74 */       return false;
/*     */     }
/*     */     
/*     */ 
/*  78 */     setInstallerVariables(config, this.variables);
/*     */     
/*  80 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void setInstallerVariables(PropertiesConfiguration config, Variables variables)
/*     */   {
/*  88 */     for (String key : this.keyVariables.keySet())
/*     */     {
/*     */ 
/*  91 */       String propFileValue = (String)config.getProperty(key);
/*  92 */       String variableToSet = (String)this.keyVariables.get(key);
/*     */       
/*     */ 
/*  95 */       if (propFileValue == null) {
/*  96 */         this.logger.warning("Could not find key " + key + " in " + this.fileName + ". Setting " + variableToSet + " to blank String: \"\" ");
/*  97 */         variables.set(variableToSet, "");
/*     */       } else {
/*  99 */         this.logger.finer("Found key " + key + " in " + this.fileName + ". Setting " + variableToSet + " to: \"" + propFileValue + "\"");
/* 100 */         variables.set(variableToSet, propFileValue);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getProcessableName()
/*     */   {
/* 109 */     return "Read Proprty File";
/*     */   }
/*     */ }


/* Location:              D:\stage-installer-01-30-2023\postghost\installer\JCG-Xstore17-InstallerCopy.jar!\com\izforge\izpack\panels\xstprocess\processables\ReadPropertyFile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */