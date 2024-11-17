/*     */ package com.izforge.izpack.panels.xstprocess.processables;
/*     */ 
/*     */ import com.izforge.izpack.api.data.Variables;
/*     */ import com.izforge.izpack.panels.xstprocess.AbstractUIProcessHandler;
/*     */ import com.izforge.izpack.panels.xstprocess.Processable;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.logging.Logger;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ 
/*     */ public class ExecutableJar
/*     */   extends Processable
/*     */ {
/*  17 */   String jrePath = System.getProperty("java.home");
/*  18 */   String jarPath = null;
/*  19 */   String workingPath = null;
/*  20 */   String[] args = null;
/*     */   
/*     */ 
/*     */ 
/*  24 */   boolean jarRelativeToWorkingDir = false;
/*     */   
/*  26 */   Logger logger = Logger.getLogger(ExecutableJar.class.getName());
/*     */   
/*     */   public ExecutableJar(String javaHome, String jarPath, String workingPath, String... args)
/*     */   {
/*  30 */     this.jarPath = jarPath;
/*  31 */     this.workingPath = workingPath;
/*  32 */     this.args = args;
/*     */     
/*  34 */     if (javaHome != null) {
/*  35 */       this.jrePath = javaHome;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected void setWorkingPath()
/*     */   {
/*  42 */     File file = new File(this.jarPath);
/*  43 */     this.workingPath = file.getParentFile().getName();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean run()
/*     */   {
/*  50 */     this.jrePath = this.variables.replace(this.jrePath);
/*  51 */     String localJrePath = this.jrePath + "/bin/java.exe";
/*  52 */     File jre = new File(localJrePath);
/*     */     
/*     */ 
/*  55 */     if (!jre.exists()) {
/*  56 */       this.logger.severe("Could not locate JAVA: " + jre.getAbsolutePath());
/*  57 */       this.handler.logOutput("Could not locate JAVA: " + jre.getAbsolutePath(), true);
/*  58 */       return false;
/*     */     }
/*     */     
/*     */ 
/*  62 */     this.jarPath = this.variables.replace(this.jarPath);
/*  63 */     File jar = new File(this.jarPath);
/*     */     
/*     */ 
/*  66 */     if ((!jar.isAbsolute()) && (this.jarRelativeToWorkingDir)) {
/*  67 */       String jarExecutePath = this.workingPath + '/' + this.jarPath;
/*     */       
/*  69 */       this.logger.fine("JAR path built relative to working path set: " + jarExecutePath);
/*  70 */       jar = new File(jarExecutePath);
/*     */ 
/*     */ 
/*     */     }
/*  74 */     else if ((jar.isAbsolute()) && (this.jarRelativeToWorkingDir)) {
/*  75 */       this.logger.warning("Option set that jar is relative to working path, yet an absolute path was supplied.\nYou can't do this! - JAR PATH SUPPLIED: " + this.jarPath);
/*     */       
/*  77 */       this.logger.warning("Using this path for JAR, ignoring the relative to working path option: " + this.jarPath);
/*     */     }
/*     */     
/*     */ 
/*  81 */     if (!jar.exists()) {
/*  82 */       this.logger.severe("Could not locate JAR: " + jar.getAbsolutePath());
/*  83 */       this.handler.logOutput("Could not locate JAR: " + jar.getAbsolutePath(), true);
/*  84 */       return false;
/*     */     }
/*     */     
/*  87 */     this.workingPath = this.variables.replace(this.workingPath);
/*  88 */     File workingDir = new File(this.workingPath);
/*  89 */     if (!workingDir.exists()) {
/*  90 */       this.logger.severe("Could not locate working directory: " + workingDir.getAbsolutePath());
/*  91 */       this.handler.logOutput("Could not locate working directory: " + workingDir.getAbsolutePath(), true);
/*  92 */       return false;
/*     */     }
/*     */     
/*     */ 
/*  96 */     ArrayList<String> launchArgs = new ArrayList();
/*  97 */     launchArgs.add(jre.getAbsolutePath());
/*  98 */     launchArgs.add("-jar");
/*  99 */     launchArgs.add(jar.getAbsolutePath());
/*     */     
/*     */ 
/* 102 */     if (this.args != null) {
/* 103 */       launchArgs.addAll(Arrays.asList(this.args));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 108 */     Process proc = null;
/*     */     try {
/* 110 */       ProcessBuilder pb = new ProcessBuilder(launchArgs);
/* 111 */       pb.directory(new File(this.workingPath));
/*     */       
/* 113 */       proc = pb.start();
/*     */     } catch (IOException e) {
/* 115 */       this.logger.severe("Error launching JAR!");
/* 116 */       this.logger.severe(e.toString());
/*     */     }
/*     */     
/* 119 */     final InputStream out = proc.getInputStream();
/* 120 */     final InputStream err = proc.getErrorStream();
/* 121 */     final AbstractUIProcessHandler uiHandler = this.handler;
/*     */     
/*     */ 
/* 124 */     Runnable logRunnable = new Runnable()
/*     */     {
/*     */ 
/*     */       public void run()
/*     */       {
/*     */         try
/*     */         {
/* 131 */           while ((!Thread.currentThread().isInterrupted()) && (out.available() != 0) && (err.available() != 0))
/*     */           {
/*     */ 
/* 134 */             if (out.available() > 0) {
/* 135 */               uiHandler.logOutput(IOUtils.toString(out), false);
/*     */             }
/*     */             
/* 138 */             if (err.available() > 0) {
/* 139 */               uiHandler.logOutput(IOUtils.toString(err), true);
/*     */             }
/*     */           }
/*     */         }
/*     */         catch (IOException e) {
/* 144 */           ExecutableJar.this.logger.warning("Error reading from process stream err or out.");
/* 145 */           ExecutableJar.this.logger.warning(e.toString());
/*     */         }
/*     */         
/*     */       }
/*     */       
/* 150 */     };
/* 151 */     Thread loggingThread = new Thread(logRunnable);
/* 152 */     loggingThread.start();
/*     */     
/*     */     try
/*     */     {
/* 156 */       proc.waitFor();
/*     */     } catch (InterruptedException e) {
/* 158 */       this.logger.warning("JAR process was interuppted?");
/* 159 */       this.logger.warning(e.toString());
/*     */     }
/*     */     
/*     */ 
/* 163 */     loggingThread.interrupt();
/*     */     try {
/* 165 */       loggingThread.join(15000L);
/*     */     } catch (InterruptedException e) {
/* 167 */       this.logger.warning(e.toString());
/*     */     }
/*     */     
/* 170 */     return true;
/*     */   }
/*     */   
/*     */   public String getJrePath() {
/* 174 */     return this.jrePath;
/*     */   }
/*     */   
/*     */   public void setJrePath(String jrePath) {
/* 178 */     this.jrePath = jrePath;
/*     */   }
/*     */   
/*     */   public boolean isJarRelativeToWorkingDir() {
/* 182 */     return this.jarRelativeToWorkingDir;
/*     */   }
/*     */   
/*     */   public void setJarRelativeToWorkingDir(boolean jarRelativeToWorkingDir) {
/* 186 */     this.jarRelativeToWorkingDir = jarRelativeToWorkingDir;
/*     */   }
/*     */   
/*     */   public String getProcessableName()
/*     */   {
/* 191 */     return "Executable JAR";
/*     */   }
/*     */ }


/* Location:              D:\stage-installer-01-30-2023\postghost\installer\JCG-Xstore17-InstallerCopy.jar!\com\izforge\izpack\panels\xstprocess\processables\ExecutableJar.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */