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
/*     */ 
/*     */ public class WindowsService
/*     */   extends Processable
/*     */ {
/*  15 */   private PrintWriter logfile = null;
/*     */   
/*     */ 
/*     */   String serviceName;
/*     */   
/*     */   String serviceOperation;
/*     */   
/*     */ 
/*     */   public static enum ServiceOperation
/*     */   {
/*  25 */     START, 
/*  26 */     STOP, 
/*  27 */     RESTART;
/*     */     
/*     */     private ServiceOperation() {}
/*     */   }
/*     */   
/*  32 */   public WindowsService(String serviceName, ServiceOperation serviceOperation) { this.serviceName = serviceName;
/*  33 */     this.serviceOperation = serviceOperation.toString().toLowerCase();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean run()
/*     */   {
/*  40 */     this.serviceName = this.variables.replace(this.serviceName);
/*     */     
/*  42 */     ProcessBuilder processBuilder = new ProcessBuilder(new String[] { "sc", this.serviceOperation.toString().toLowerCase(), this.serviceName });
/*     */     
/*     */ 
/*     */ 
/*     */     try
/*     */     {
/*  48 */       String logCommand = "Running " + this.serviceOperation + " on service:" + this.serviceName;
/*  49 */       this.handler.logOutput(logCommand, false);
/*     */       
/*     */ 
/*  52 */       Process process = processBuilder.start();
/*     */       
/*  54 */       OutputMonitor stdoutMon = new OutputMonitor(this.handler, process.getInputStream(), false);
/*     */       
/*  56 */       OutputMonitor stderrMon = new OutputMonitor(this.handler, process.getErrorStream(), true);
/*     */       
/*  58 */       Thread stdoutThread = new Thread(stdoutMon);
/*  59 */       Thread stderrThread = new Thread(stderrMon);
/*  60 */       stdoutThread.setDaemon(true);
/*  61 */       stderrThread.setDaemon(true);
/*  62 */       stdoutThread.start();
/*  63 */       stderrThread.start();
/*     */       
/*     */       try
/*     */       {
/*  67 */         process.waitFor();
/*     */         
/*  69 */         stopMonitor(stdoutMon, stdoutThread);
/*  70 */         stopMonitor(stderrMon, stderrThread);
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
/*  89 */         process.destroy();
/*  90 */         this.handler.emitError("process interrupted", ie.toString());
/*  91 */         return false;
/*     */       }
/*     */       
/*     */     }
/*     */     catch (IOException ioe)
/*     */     {
/*  97 */       this.handler.emitError("I/O error", ioe.toString());
/*  98 */       return false;
/*     */     }
/*     */     
/* 101 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void stopMonitor(OutputMonitor monitor, Thread thread)
/*     */   {
/* 108 */     monitor.doStop();
/* 109 */     long softTimeout = 500L;
/*     */     try
/*     */     {
/* 112 */       thread.join(softTimeout);
/*     */     }
/*     */     catch (InterruptedException e) {}
/*     */     
/*     */ 
/*     */ 
/* 118 */     if (!thread.isAlive())
/*     */     {
/* 120 */       return;
/*     */     }
/*     */     
/* 123 */     thread.interrupt();
/* 124 */     long hardTimeout = 500L;
/*     */     try
/*     */     {
/* 127 */       thread.join(hardTimeout);
/*     */     }
/*     */     catch (InterruptedException e) {}
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public class OutputMonitor
/*     */     implements Runnable
/*     */   {
/* 137 */     private boolean stderr = false;
/*     */     
/*     */     private AbstractUIProcessHandler handler;
/*     */     
/*     */     private BufferedReader reader;
/*     */     
/* 143 */     private Boolean stop = Boolean.valueOf(false);
/*     */     
/*     */     public OutputMonitor(AbstractUIProcessHandler handler, InputStream is, boolean stderr)
/*     */     {
/* 147 */       this.stderr = stderr;
/* 148 */       this.reader = new BufferedReader(new InputStreamReader(is));
/* 149 */       this.handler = handler;
/*     */     }
/*     */     
/*     */ 
/*     */     public void run()
/*     */     {
/*     */       try
/*     */       {
/*     */         String line;
/* 158 */         while ((line = this.reader.readLine()) != null)
/*     */         {
/* 160 */           this.handler.logOutput(line, this.stderr);
/*     */           
/*     */ 
/*     */ 
/* 164 */           if (WindowsService.this.logfile != null)
/*     */           {
/* 166 */             WindowsService.this.logfile.println(line);
/*     */           }
/*     */           
/* 169 */           synchronized (this.stop)
/*     */           {
/* 171 */             if (this.stop.booleanValue())
/*     */             {
/* 173 */               return;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       catch (IOException ioe)
/*     */       {
/* 180 */         this.handler.logOutput(ioe.toString(), true);
/*     */         
/*     */ 
/*     */ 
/* 184 */         if (WindowsService.this.logfile != null)
/*     */         {
/* 186 */           WindowsService.this.logfile.println(ioe.toString());
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void doStop()
/*     */     {
/* 195 */       synchronized (this.stop)
/*     */       {
/* 197 */         this.stop = Boolean.valueOf(true);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public String getProcessableName()
/*     */   {
/* 205 */     return "Windows Service Operation:";
/*     */   }
/*     */ }


/* Location:              D:\stage-installer-01-30-2023\postghost\installer\JCG-Xstore17-InstallerCopy.jar!\com\izforge\izpack\panels\xstprocess\processables\WindowsService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */