/*     */ package com.izforge.izpack.panels.xstprocess.processables;
/*     */ 
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
/*     */ 
/*     */ public class Shutdown
/*     */   extends Processable
/*     */ {
/*  17 */   private PrintWriter logfile = null;
/*     */   
/*     */ 
/*     */   ShutdownOperation operation;
/*     */   
/*     */ 
/*     */ 
/*     */   public static enum ShutdownOperation
/*     */   {
/*  26 */     SHUTDOWN, 
/*  27 */     REBOOT, 
/*  28 */     RESTART;
/*     */     
/*     */     private ShutdownOperation() {}
/*     */   }
/*     */   
/*  33 */   public Shutdown(ShutdownOperation operation) { this.operation = operation; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean run()
/*     */   {
/*  40 */     List<String> command = new ArrayList();
/*  41 */     command.add("shutdown.exe");
/*  42 */     command.add("/f");
/*  43 */     command.add("/t");
/*  44 */     command.add("0");
/*     */     
/*  46 */     if ((this.operation.equals(ShutdownOperation.REBOOT)) || (this.operation.equals(ShutdownOperation.RESTART)))
/*     */     {
/*  48 */       command.add("/r");
/*  49 */     } else if (this.operation.equals(ShutdownOperation.SHUTDOWN)) {
/*  50 */       command.add("/s");
/*     */     }
/*     */     
/*  53 */     ProcessBuilder processBuilder = new ProcessBuilder(command);
/*     */     
/*     */     try
/*     */     {
/*  57 */       String logCommand = "Running " + this.operation.toString();
/*  58 */       this.handler.logOutput(logCommand, false);
/*     */       
/*     */ 
/*  61 */       Process process = processBuilder.start();
/*     */       
/*  63 */       OutputMonitor stdoutMon = new OutputMonitor(this.handler, process.getInputStream(), false);
/*     */       
/*  65 */       OutputMonitor stderrMon = new OutputMonitor(this.handler, process.getErrorStream(), true);
/*     */       
/*  67 */       Thread stdoutThread = new Thread(stdoutMon);
/*  68 */       Thread stderrThread = new Thread(stderrMon);
/*  69 */       stdoutThread.setDaemon(true);
/*  70 */       stderrThread.setDaemon(true);
/*  71 */       stdoutThread.start();
/*  72 */       stderrThread.start();
/*     */       
/*     */       try
/*     */       {
/*  76 */         process.waitFor();
/*     */         
/*  78 */         stopMonitor(stdoutMon, stdoutThread);
/*  79 */         stopMonitor(stderrMon, stderrThread);
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
/*  98 */         process.destroy();
/*  99 */         this.handler.emitError("process interrupted", ie.toString());
/* 100 */         return false;
/*     */       }
/*     */       
/*     */     }
/*     */     catch (IOException ioe)
/*     */     {
/* 106 */       this.handler.emitError("I/O error", ioe.toString());
/* 107 */       return false;
/*     */     }
/*     */     
/* 110 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void stopMonitor(OutputMonitor monitor, Thread thread)
/*     */   {
/* 117 */     monitor.doStop();
/* 118 */     long softTimeout = 500L;
/*     */     try
/*     */     {
/* 121 */       thread.join(softTimeout);
/*     */     }
/*     */     catch (InterruptedException e) {}
/*     */     
/*     */ 
/*     */ 
/* 127 */     if (!thread.isAlive())
/*     */     {
/* 129 */       return;
/*     */     }
/*     */     
/* 132 */     thread.interrupt();
/* 133 */     long hardTimeout = 500L;
/*     */     try
/*     */     {
/* 136 */       thread.join(hardTimeout);
/*     */     }
/*     */     catch (InterruptedException e) {}
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public class OutputMonitor
/*     */     implements Runnable
/*     */   {
/* 146 */     private boolean stderr = false;
/*     */     
/*     */     private AbstractUIProcessHandler handler;
/*     */     
/*     */     private BufferedReader reader;
/*     */     
/* 152 */     private Boolean stop = Boolean.valueOf(false);
/*     */     
/*     */     public OutputMonitor(AbstractUIProcessHandler handler, InputStream is, boolean stderr)
/*     */     {
/* 156 */       this.stderr = stderr;
/* 157 */       this.reader = new BufferedReader(new InputStreamReader(is));
/* 158 */       this.handler = handler;
/*     */     }
/*     */     
/*     */ 
/*     */     public void run()
/*     */     {
/*     */       try
/*     */       {
/*     */         String line;
/* 167 */         while ((line = this.reader.readLine()) != null)
/*     */         {
/* 169 */           this.handler.logOutput(line, this.stderr);
/*     */           
/*     */ 
/*     */ 
/* 173 */           if (Shutdown.this.logfile != null)
/*     */           {
/* 175 */             Shutdown.this.logfile.println(line);
/*     */           }
/*     */           
/* 178 */           synchronized (this.stop)
/*     */           {
/* 180 */             if (this.stop.booleanValue())
/*     */             {
/* 182 */               return;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       catch (IOException ioe)
/*     */       {
/* 189 */         this.handler.logOutput(ioe.toString(), true);
/*     */         
/*     */ 
/*     */ 
/* 193 */         if (Shutdown.this.logfile != null)
/*     */         {
/* 195 */           Shutdown.this.logfile.println(ioe.toString());
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void doStop()
/*     */     {
/* 204 */       synchronized (this.stop)
/*     */       {
/* 206 */         this.stop = Boolean.valueOf(true);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public String getProcessableName()
/*     */   {
/* 214 */     return "Shutdown operation:";
/*     */   }
/*     */ }


/* Location:              D:\stage-installer-01-30-2023\postghost\installer\JCG-Xstore17-InstallerCopy.jar!\com\izforge\izpack\panels\xstprocess\processables\Shutdown.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */