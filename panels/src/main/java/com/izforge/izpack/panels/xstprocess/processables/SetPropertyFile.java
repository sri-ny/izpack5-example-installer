/*     */ package com.izforge.izpack.panels.xstprocess.processables;
/*     */ 
/*     */ import com.izforge.izpack.api.data.Variables;
/*     */ import com.izforge.izpack.panels.xstprocess.AbstractUIProcessHandler;
/*     */ import com.izforge.izpack.panels.xstprocess.Processable;
/*     */ import com.izforge.izpack.panels.xstprocess.util.XstPropertyFileNormalizer;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import org.apache.commons.configuration2.PropertiesConfiguration;
/*     */ 
/*     */ public class SetPropertyFile
/*     */   extends Processable
/*     */ {
/*     */   private String fileName;
/*     */   private HashMap<String, String> keyValues;
/*  22 */   private List<String> xstEncryptValues = null;
/*     */   
/*     */   public SetPropertyFile(String fileName, HashMap<String, String> keyValues, List<String> xstEncryptValues) {
/*  25 */     this.fileName = fileName;
/*  26 */     this.keyValues = keyValues;
/*  27 */     this.xstEncryptValues = xstEncryptValues;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean run()
/*     */   {
/*  33 */     this.fileName = this.variables.replace(this.fileName);
/*     */     
/*  35 */     this.handler.logOutput("Setting property file: " + this.fileName, false);
/*  36 */     File propertyFileFile = new File(this.fileName);
/*     */     
/*     */ 
/*  39 */     if (!propertyFileFile.exists()) {
/*     */       try {
/*  41 */         this.handler.logOutput("Property file does not exist, creating: " + this.fileName, false);
/*  42 */         propertyFileFile.createNewFile();
/*     */       } catch (IOException exception) {
/*  44 */         exception.printStackTrace();
/*  45 */         this.handler.logOutput("Error Creating Property File!: " + this.fileName, true);
/*  46 */         this.handler.logOutput(exception.getMessage(), true);
/*  47 */         return false;
/*     */       }
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
/*     */     try
/*     */     {
/*  63 */       XstPropertyFileNormalizer.normalizeFile(this.fileName);
/*     */     } catch (IOException e1) {
/*  65 */       this.handler.logOutput("Error normalizing property file!", true);
/*  66 */       this.handler.logOutput(e1.getMessage(), true);
/*  67 */       return false;
/*     */     }
/*     */     
/*     */ 
/*  71 */     PropertiesConfiguration config = new PropertiesConfiguration();
/*     */     try
/*     */     {
/*  74 */       config.read(new FileReader(this.fileName));
/*     */     } catch (Exception e) {
/*  76 */       this.handler.logOutput("Error reading in property file!", true);
/*  77 */       this.handler.logOutput(e.getStackTrace().toString(), true);
/*  78 */       e.printStackTrace();
/*  79 */       return false;
/*     */     }
/*     */     
/*     */ 
/*  83 */     for (String key : this.keyValues.keySet())
/*     */     {
/*     */ 
/*     */ 
/*  87 */       if (this.keyValues.get(key) == null) {
/*  88 */         config.clearProperty(key);
/*     */       }
/*     */       else
/*     */       {
/*     */         try
/*     */         {
/*  94 */           String replacedVal = this.variables.replace((String)this.keyValues.get(key));
/*     */           
/*  96 */           this.keyValues.put(key, replacedVal);
/*     */         }
/*     */         catch (Exception e) {}
/*     */         
/*     */ 
/*     */ 
/* 102 */         if (this.xstEncryptValues.contains(key)) {
/* 103 */           String value = (String)this.keyValues.get(key);
/*     */           
/* 105 */           if ((value != null) && (!value.equals(""))) {
/* 106 */             this.handler.logOutput("Xstore encrypting value for property: " + key, false);
/* 107 */             XstEncryptor encryptor = new XstEncryptor(value);
/* 108 */             value = encryptor.getEncString();
/*     */           }
/*     */           
/* 111 */           this.handler.logOutput("Setting property: " + key, false);
/* 112 */           config.setProperty(key, value);
/*     */         } else {
/* 114 */           this.handler.logOutput("Setting property: " + key, false);
/* 115 */           config.setProperty(key, this.keyValues.get(key));
/*     */         }
/*     */         
/* 118 */         this.handler.logOutput("\n", false);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 125 */       config.write(new FileWriter(this.fileName));
/*     */     } catch (Exception e) {
/* 127 */       e.printStackTrace();
/* 128 */       return false;
/*     */     }
/*     */     
/* 131 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class XstEncryptor
/*     */   {
/* 141 */     String xstoreInstallDir = "c:/xstore";
/* 142 */     String xstJavaDir = "c:/jre";
/* 143 */     String xstEnvJavaDir = "c:/environment/runtime/win32/jre";
/*     */     String rawString;
/*     */     String encString;
/*     */     
/*     */     public XstEncryptor(String rawString)
/*     */     {
/* 149 */       this.rawString = rawString;
/*     */       
/*     */ 
/* 152 */       File javaNewCommand = new File(this.xstJavaDir + "/bin/java.exe");
/* 153 */       File javaOldCommand = new File(this.xstEnvJavaDir + "/bin/java.exe");
/*     */       
/* 155 */       File javaCommand = null;
/* 156 */       if (javaOldCommand.exists()) {
/* 157 */         javaCommand = javaOldCommand;
/*     */       } else {
/* 159 */         javaCommand = javaNewCommand;
/*     */       }
/*     */       
/*     */ 
/* 163 */       File libDir = new File(this.xstoreInstallDir + "/lib");
/* 164 */       File libExtDir = new File(this.xstoreInstallDir + "/lib/ext");
/*     */       
/*     */ 
/* 167 */       StringBuilder cpStringBuild = new StringBuilder();
/*     */       
/*     */ 
/* 170 */       for (File file : libDir.listFiles()) {
/* 171 */         cpStringBuild.append(file.getAbsolutePath());
/* 172 */         cpStringBuild.append(';');
/*     */       }
/*     */       
/*     */ 
/* 176 */       for (File file : libExtDir.listFiles()) {
/* 177 */         cpStringBuild.append(file.getAbsolutePath());
/* 178 */         cpStringBuild.append(';');
/*     */       }
/*     */       
/*     */ 
/* 182 */       ProcessBuilder pb = new ProcessBuilder(new String[] { javaCommand.getAbsolutePath(), "-cp", cpStringBuild.toString(), "dtv.util.config.EncryptedStringConfig", rawString });
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       try
/*     */       {
/* 192 */         pb.redirectErrorStream(false);
/*     */         
/*     */ 
/* 195 */         pb.directory(new File(this.xstoreInstallDir));
/*     */         
/*     */ 
/* 198 */         Process p = pb.start();
/*     */         
/*     */ 
/*     */ 
/* 202 */         BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
/*     */         
/*     */ 
/* 205 */         String s = null;
/* 206 */         while ((s = stdInput.readLine()) != null) {
/* 207 */           this.encString += s;
/*     */         }
/*     */       }
/*     */       catch (IOException e) {
/* 211 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */     
/*     */     public String getEncString() {
/* 216 */       if ((this.encString == null) || (this.encString.equalsIgnoreCase(""))) {
/* 217 */         return "";
/*     */       }
/* 219 */       String encStringStripped = this.encString.substring(this.encString.indexOf("Pj4"));
/* 220 */       return encStringStripped;
/*     */     }
/*     */     
/*     */     public String getRawString() {
/* 224 */       return this.rawString;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getProcessableName()
/*     */   {
/* 236 */     return "Set Property File";
/*     */   }
/*     */ }


/* Location:              D:\stage-installer-01-30-2023\postghost\installer\JCG-Xstore17-InstallerCopy.jar!\com\izforge\izpack\panels\xstprocess\processables\SetPropertyFile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */