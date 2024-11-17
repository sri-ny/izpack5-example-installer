/*     */ package com.izforge.izpack.panels.xstprocess.processables;
/*     */ 
/*     */ import com.izforge.izpack.api.data.Variables;
/*     */ import com.izforge.izpack.panels.xstprocess.AbstractUIProcessHandler;
/*     */ import com.izforge.izpack.panels.xstprocess.ProcessPanelWorker;
/*     */ import com.izforge.izpack.panels.xstprocess.Processable;
/*     */ import com.izforge.izpack.util.Debug;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.List;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ExecutableClass
/*     */   extends Processable
/*     */ {
/*  24 */   private static final Logger logger = Logger.getLogger(ProcessPanelWorker.class.getName());
/*     */   
/*     */   private final String myClassName;
/*     */   
/*     */   private final List<String> myArguments;
/*     */   
/*     */   protected AbstractUIProcessHandler myHandler;
/*     */   
/*     */   public ExecutableClass(String className, List<String> args)
/*     */   {
/*  34 */     this.myClassName = className;
/*  35 */     this.myArguments = args;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean run()
/*     */   {
/*  41 */     boolean result = false;
/*     */     
/*     */ 
/*  44 */     String[] params = new String[this.myArguments.size()];
/*     */     
/*  46 */     int i = 0;
/*  47 */     for (String myArgument : this.myArguments)
/*     */     {
/*  49 */       params[i] = this.variables.replace(myArgument);
/*  50 */       i++;
/*     */     }
/*     */     
/*     */     try
/*     */     {
/*  55 */       ClassLoader loader = getClass().getClassLoader();
/*  56 */       Class<?> procClass = loader.loadClass(this.myClassName);
/*     */       
/*  58 */       Object instance = procClass.newInstance();
/*  59 */       Method method = procClass.getMethod("run", new Class[] { AbstractUIProcessHandler.class, String[].class });
/*     */       
/*     */ 
/*  62 */       if (method.getReturnType().getName().equals("boolean"))
/*     */       {
/*  64 */         result = ((Boolean)method.invoke(instance, new Object[] { this.myHandler, params })).booleanValue();
/*     */       }
/*     */       else
/*     */       {
/*  68 */         method.invoke(instance, new Object[] { this.myHandler, params });
/*  69 */         result = true;
/*     */       }
/*     */     }
/*     */     catch (SecurityException e)
/*     */     {
/*  74 */       this.myHandler.emitError("Post Processing Error", "Security exception thrown when processing class: " + this.myClassName);
/*     */       
/*  76 */       if (Debug.isSTACKTRACE()) {
/*  77 */         logger.log(Level.SEVERE, "Security exception thrown when processing class: " + this.myClassName, e);
/*     */       }
/*     */     }
/*     */     catch (ClassNotFoundException e)
/*     */     {
/*  82 */       this.myHandler.emitError("Post Processing Error", "Cannot find processing class: " + this.myClassName);
/*     */       
/*  84 */       if (Debug.isSTACKTRACE()) {
/*  85 */         logger.log(Level.SEVERE, "Cannot find processing class: " + this.myClassName, e);
/*     */       }
/*     */     }
/*     */     catch (NoSuchMethodException e)
/*     */     {
/*  90 */       this.myHandler.emitError("Post Processing Error", "Processing class does not have 'run' method: " + this.myClassName);
/*     */       
/*  92 */       if (Debug.isSTACKTRACE()) {
/*  93 */         logger.log(Level.SEVERE, "Processing class does not have 'run' method: " + this.myClassName, e);
/*     */       }
/*     */     }
/*     */     catch (IllegalAccessException e)
/*     */     {
/*  98 */       this.myHandler.emitError("Post Processing Error", "Error accessing processing class: " + this.myClassName);
/*     */       
/* 100 */       if (Debug.isSTACKTRACE()) {
/* 101 */         logger.log(Level.SEVERE, "Error accessing processing class: " + this.myClassName, e);
/*     */       }
/*     */     }
/*     */     catch (InvocationTargetException e)
/*     */     {
/* 106 */       this.myHandler.emitError("Post Processing Error", "Invocation Problem calling: " + this.myClassName + ", " + e.getCause().getMessage());
/*     */       
/* 108 */       if (Debug.isSTACKTRACE()) {
/* 109 */         logger.log(Level.SEVERE, "Invocation Problem calling: " + this.myClassName, e);
/*     */       }
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 114 */       this.myHandler.emitError("Post Processing Error", "Exception when running processing class: " + this.myClassName + ", " + e.getMessage());
/*     */       
/*     */ 
/* 117 */       if (Debug.isSTACKTRACE()) {
/* 118 */         logger.log(Level.SEVERE, "Exception when running processing class: " + this.myClassName, e);
/*     */       }
/*     */     }
/*     */     catch (Error e)
/*     */     {
/* 123 */       this.myHandler.emitError("Post Processing Error", "Error when running processing class: " + this.myClassName + ", " + e.getMessage());
/*     */       
/*     */ 
/* 126 */       if (Debug.isSTACKTRACE()) {
/* 127 */         logger.log(Level.SEVERE, "Error when running processing class: " + this.myClassName, e);
/*     */       }
/*     */     }
/*     */     catch (Throwable e)
/*     */     {
/* 132 */       this.myHandler.emitError("Post Processing Error", "Error when running processing class: " + this.myClassName + ", " + e.getMessage());
/*     */       
/*     */ 
/* 135 */       if (Debug.isSTACKTRACE()) {
/* 136 */         logger.log(Level.SEVERE, "Error when running processing class: " + this.myClassName, e);
/*     */       }
/*     */     }
/* 139 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   public String getProcessableName()
/*     */   {
/* 145 */     return "Executable Class";
/*     */   }
/*     */ }


/* Location:              D:\stage-installer-01-30-2023\postghost\installer\JCG-Xstore17-InstallerCopy.jar!\com\izforge\izpack\panels\xstprocess\processables\ExecutableClass.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */