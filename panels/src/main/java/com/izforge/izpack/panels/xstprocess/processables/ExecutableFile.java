/*     */ package com.izforge.izpack.panels.xstprocess.processables;
/*     */ 
/*     */ import com.izforge.izpack.api.data.Variables;
/*     */ import com.izforge.izpack.panels.xstprocess.AbstractUIProcessHandler;
/*     */ import com.izforge.izpack.panels.xstprocess.Processable;
/*     */ import com.izforge.izpack.util.IoHelper;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class ExecutableFile
/*     */   extends Processable
/*     */ {
/*  20 */   private PrintWriter logfile = null;
/*     */   
/*     */ 
/*     */   private String filename;
/*     */   
/*     */ 
/*     */   private String workingDir;
/*     */   
/*     */ 
/*     */   private List<String> arguments;
/*     */   
/*     */   private List<String> envvariables;
/*     */   
/*  33 */   protected boolean reloadPath = false;
/*     */   
/*     */ 
/*     */ 
/*     */   public ExecutableFile(String fn, List<String> args, List<String> envvars, String workingDir)
/*     */   {
/*  39 */     this.filename = fn;
/*  40 */     this.arguments = args;
/*  41 */     this.envvariables = envvars;
/*  42 */     this.workingDir = workingDir;
/*     */   }
/*     */   
/*     */   public ExecutableFile(String fn, List<String> args, List<String> envvars, String workingDir, boolean reloadPath)
/*     */   {
/*  47 */     this.filename = fn;
/*  48 */     this.arguments = args;
/*  49 */     this.envvariables = envvars;
/*  50 */     this.workingDir = workingDir;
/*  51 */     this.reloadPath = reloadPath;
/*     */   }
/*     */   
/*     */   public boolean run()
/*     */   {
/*  56 */     List<String> params = new ArrayList(this.arguments.size() + 1);
/*     */     
/*     */     try
/*     */     {
/*  60 */       params.add(this.variables.replace(this.filename));
/*     */     } catch (Exception e) {
/*  62 */       params.add(this.filename);
/*     */     }
/*     */     
/*     */ 
/*  66 */     for (String argument : this.arguments) {
/*     */       try
/*     */       {
/*  69 */         params.add(this.variables.replace(argument));
/*     */       } catch (Exception e) {
/*  71 */         params.add(argument);
/*     */       }
/*     */     }
/*     */     
/*  75 */     ProcessBuilder processBuilder = new ProcessBuilder(params);
/*     */     
/*     */ 
/*  78 */     if ((this.workingDir != null) && (!this.workingDir.equals("")))
/*     */     {
/*  80 */       this.workingDir = IoHelper.translatePath(this.workingDir, this.variables);
/*  81 */       processBuilder.directory(new File(this.workingDir));
/*     */     }
/*     */     
/*     */ 
/*  85 */     Map<String, String> environment = processBuilder.environment();
/*  86 */     for (String envvar : this.envvariables)
/*     */     {
/*  88 */       String ev = this.variables.replace(envvar);
/*  89 */       int i = ev.indexOf("=");
/*  90 */       if (i > 0) {
/*  91 */         environment.put(ev.substring(0, i), ev.substring(i + 1));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*  96 */     if (this.reloadPath) {
/*  97 */       String path = reloadPath();
/*  98 */       environment.put("PATH", path);
/*     */     }
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 104 */       String logCommand = "Starting Executable: ";
/* 105 */       for (String arg : processBuilder.command()) {
/* 106 */         logCommand = logCommand + arg + " ";
/*     */       }
/* 108 */       this.handler.logOutput(logCommand, false);
/*     */       
/*     */ 
/* 111 */       Process process = processBuilder.start();
/*     */       
/* 113 */       OutputMonitor stdoutMon = new OutputMonitor(this.handler, process.getInputStream(), false);
/*     */       
/* 115 */       OutputMonitor stderrMon = new OutputMonitor(this.handler, process.getErrorStream(), true);
/*     */       
/* 117 */       Thread stdoutThread = new Thread(stdoutMon);
/* 118 */       Thread stderrThread = new Thread(stderrMon);
/* 119 */       stdoutThread.setDaemon(true);
/* 120 */       stderrThread.setDaemon(true);
/* 121 */       stdoutThread.start();
/* 122 */       stderrThread.start();
/*     */       
/*     */       try
/*     */       {
/* 126 */         process.waitFor();
/*     */         
/* 128 */         stopMonitor(stdoutMon, stdoutThread);
/* 129 */         stopMonitor(stderrMon, stderrThread);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       }
/*     */       catch (InterruptedException ie)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 148 */         process.destroy();
/* 149 */         this.handler.emitError("process interrupted", ie.toString());
/* 150 */         return false;
/*     */       }
/*     */       
/*     */     }
/*     */     catch (IOException ioe)
/*     */     {
/* 156 */       this.handler.emitError("I/O error", ioe.toString());
/* 157 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 162 */     return true;
/*     */   }
/*     */   
/*     */   private String reloadPath()
/*     */   {
/* 167 */     String path = "";
/*     */     
/*     */ 
/* 170 */     ArrayList<String> regLocs = new ArrayList();
/* 171 */     regLocs.add("HKLM\\SYSTEM\\CurrentControlSet\\Control\\Session Manager\\Environment");
/* 172 */     regLocs.add("HKCU\\Environment");
/*     */     
/*     */ 
/* 175 */     for (String regLoc : regLocs)
/*     */     {
/* 177 */       ProcessBuilder systemPathPb = new ProcessBuilder(new String[] { "reg.exe", "query", regLoc, "/v", "Path" });
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 184 */       systemPathPb.redirectErrorStream(false);
/*     */       
/*     */       try
/*     */       {
/* 188 */         Process systemPathProcess = systemPathPb.start();
/* 189 */         BufferedReader stdInput = new BufferedReader(new InputStreamReader(systemPathProcess.getInputStream()));
/*     */         
/*     */ 
/* 192 */         Thread.sleep(500L);
/* 193 */         String s = null;
/* 194 */         String tempPath = "";
/* 195 */         while ((s = stdInput.readLine()) != null) {
/* 196 */           tempPath = tempPath + s;
/*     */         }
/*     */         
/*     */ 
/* 200 */         String[] parsed = tempPath.split("REG_EXPAND_SZ");
/* 201 */         path = path + parsed[(parsed.length - 1)].trim();
/* 202 */         path = path + ";";
/*     */       }
/*     */       catch (Exception e) {}
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 209 */     this.handler.logOutput("Path reloaded, New path: " + path, false);
/* 210 */     return path;
/*     */   }
/*     */   
/*     */ 
/*     */   private void stopMonitor(OutputMonitor monitor, Thread thread)
/*     */   {
/* 216 */     monitor.doStop();
/* 217 */     long softTimeout = 500L;
/*     */     try
/*     */     {
/* 220 */       thread.join(softTimeout);
/*     */     }
/*     */     catch (InterruptedException e) {}
/*     */     
/*     */ 
/*     */ 
/* 226 */     if (!thread.isAlive())
/*     */     {
/* 228 */       return;
/*     */     }
/*     */     
/* 231 */     thread.interrupt();
/* 232 */     long hardTimeout = 500L;
/*     */     try
/*     */     {
/* 235 */       thread.join(hardTimeout);
/*     */     }
/*     */     catch (InterruptedException e) {}
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public class OutputMonitor
/*     */     implements Runnable
/*     */   {
/* 245 */     private boolean stderr = false;
/*     */     
/*     */     private AbstractUIProcessHandler handler;
/*     */     
/*     */     private BufferedReader reader;
/*     */     
/* 251 */     private Boolean stop = Boolean.valueOf(false);
/*     */     
/*     */     public OutputMonitor(AbstractUIProcessHandler handler, InputStream is, boolean stderr)
/*     */     {
/* 255 */       this.stderr = stderr;
/* 256 */       this.reader = new BufferedReader(new InputStreamReader(is));
/* 257 */       this.handler = handler;
/*     */     }
/*     */     
/*     */ 
/*     */     public void run()
/*     */     {
/*     */       try
/*     */       {
/*     */         String line;
/* 266 */         while ((line = this.reader.readLine()) != null)
/*     */         {
/* 268 */           this.handler.logOutput(line, this.stderr);
/*     */           
/*     */ 
/*     */ 
/* 272 */           if (ExecutableFile.this.logfile != null)
/*     */           {
/* 274 */             ExecutableFile.this.logfile.println(line);
/*     */           }
/*     */           
/* 277 */           synchronized (this.stop)
/*     */           {
/* 279 */             if (this.stop.booleanValue())
/*     */             {
/* 281 */               return;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       catch (IOException ioe)
/*     */       {
/* 288 */         this.handler.logOutput(ioe.toString(), true);
/*     */         
/*     */ 
/*     */ 
/* 292 */         if (ExecutableFile.this.logfile != null)
/*     */         {
/* 294 */           ExecutableFile.this.logfile.println(ioe.toString());
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void doStop()
/*     */     {
/* 303 */       synchronized (this.stop)
/*     */       {
/* 305 */         this.stop = Boolean.valueOf(true);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public String getProcessableName()
/*     */   {
/* 313 */     return "Executable File";
/*     */   }
/*     */ }


/* Location:              D:\stage-installer-01-30-2023\postghost\installer\JCG-Xstore17-InstallerCopy.jar!\com\izforge\izpack\panels\xstprocess\processables\ExecutableFile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */