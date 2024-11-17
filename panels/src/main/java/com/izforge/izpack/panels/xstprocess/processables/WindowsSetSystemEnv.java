/*     */ package com.izforge.izpack.panels.xstprocess.processables;
/*     */ 
/*     */ import com.izforge.izpack.api.data.Variables;
/*     */ import com.izforge.izpack.panels.xstprocess.AbstractUIProcessHandler;
/*     */ import com.izforge.izpack.panels.xstprocess.Processable;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ public class WindowsSetSystemEnv
/*     */   extends Processable
/*     */ {
/*  17 */   private PrintWriter logfile = null;
/*     */   
/*     */ 
/*     */   String variable;
/*     */   
/*     */ 
/*     */   String value;
/*     */   
/*     */ 
/*     */ 
/*     */   public WindowsSetSystemEnv(String variable, String value)
/*     */   {
/*  29 */     this.variable = variable;
/*  30 */     this.value = value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean run()
/*     */   {
/*  37 */     this.variable = this.variables.replace(this.variable);
/*  38 */     this.value = this.variables.replace(this.value);
/*     */     
/*  40 */     List<String> command = new ArrayList();
/*  41 */     command.add("setx.exe");
/*  42 */     command.add("/M");
/*  43 */     command.add(this.variable);
/*  44 */     command.add(this.value);
/*     */     
/*  46 */     ProcessBuilder processBuilder = new ProcessBuilder(command);
/*     */     
/*     */     try
/*     */     {
/*  50 */       String logCommand = "Setting Windows System Environmental Variable " + this.variable + "=" + this.value;
/*  51 */       this.handler.logOutput(logCommand, false);
/*     */       
/*     */ 
/*  54 */       Process process = processBuilder.start();
/*     */       
/*  56 */       OutputMonitor stdoutMon = new OutputMonitor(this.handler, process.getInputStream(), false);
/*     */       
/*  58 */       OutputMonitor stderrMon = new OutputMonitor(this.handler, process.getErrorStream(), true);
/*     */       
/*  60 */       Thread stdoutThread = new Thread(stdoutMon);
/*  61 */       Thread stderrThread = new Thread(stderrMon);
/*  62 */       stdoutThread.setDaemon(true);
/*  63 */       stderrThread.setDaemon(true);
/*  64 */       stdoutThread.start();
/*  65 */       stderrThread.start();
/*     */       
/*     */       try
/*     */       {
/*  69 */         process.waitFor();
/*     */         
/*  71 */         stopMonitor(stdoutMon, stdoutThread);
/*  72 */         stopMonitor(stderrMon, stderrThread);
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
/*  91 */         process.destroy();
/*  92 */         this.handler.emitError("process interrupted", ie.toString());
/*  93 */         return false;
/*     */       }
/*     */       
/*     */     }
/*     */     catch (IOException ioe)
/*     */     {
/*  99 */       this.handler.emitError("I/O error", ioe.toString());
/* 100 */       return false;
/*     */     }
/*     */     
/* 103 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void stopMonitor(OutputMonitor monitor, Thread thread)
/*     */   {
/* 110 */     monitor.doStop();
/* 111 */     long softTimeout = 500L;
/*     */     try
/*     */     {
/* 114 */       thread.join(softTimeout);
/*     */     }
/*     */     catch (InterruptedException e) {}
/*     */     
/*     */ 
/*     */ 
/* 120 */     if (!thread.isAlive())
/*     */     {
/* 122 */       return;
/*     */     }
/*     */     
/* 125 */     thread.interrupt();
/* 126 */     long hardTimeout = 500L;
/*     */     try
/*     */     {
/* 129 */       thread.join(hardTimeout);
/*     */     }
/*     */     catch (InterruptedException e) {}
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public class OutputMonitor
/*     */     implements Runnable
/*     */   {
/* 139 */     private boolean stderr = false;
/*     */     
/*     */     private AbstractUIProcessHandler handler;
/*     */     
/*     */     private BufferedReader reader;
/*     */     
/* 145 */     private Boolean stop = Boolean.valueOf(false);
/*     */     
/*     */     public OutputMonitor(AbstractUIProcessHandler handler, InputStream is, boolean stderr)
/*     */     {
/* 149 */       this.stderr = stderr;
/* 150 */       this.reader = new BufferedReader(new InputStreamReader(is));
/* 151 */       this.handler = handler;
/*     */     }
/*     */     
/*     */ 
/*     */     public void run()
/*     */     {
/*     */       try
/*     */       {
/*     */         String line;
/* 160 */         while ((line = this.reader.readLine()) != null)
/*     */         {
/* 162 */           this.handler.logOutput(line, this.stderr);
/*     */           
/*     */ 
/*     */ 
/* 166 */           if (WindowsSetSystemEnv.this.logfile != null)
/*     */           {
/* 168 */             WindowsSetSystemEnv.this.logfile.println(line);
/*     */           }
/*     */           
/* 171 */           synchronized (this.stop)
/*     */           {
/* 173 */             if (this.stop.booleanValue())
/*     */             {
/* 175 */               return;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       catch (IOException ioe)
/*     */       {
/* 182 */         this.handler.logOutput(ioe.toString(), true);
/*     */         
/*     */ 
/*     */ 
/* 186 */         if (WindowsSetSystemEnv.this.logfile != null)
/*     */         {
/* 188 */           WindowsSetSystemEnv.this.logfile.println(ioe.toString());
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void doStop()
/*     */     {
/* 197 */       synchronized (this.stop)
/*     */       {
/* 199 */         this.stop = Boolean.valueOf(true);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public String getProcessableName()
/*     */   {
/* 207 */     return "Shutdown operation:";
/*     */   }
/*     */ }


/* Location:              D:\stage-installer-01-30-2023\postghost\installer\JCG-Xstore17-InstallerCopy.jar!\com\izforge\izpack\panels\xstprocess\processables\WindowsSetSystemEnv.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */