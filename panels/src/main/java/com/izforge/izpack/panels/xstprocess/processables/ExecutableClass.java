package com.izforge.izpack.panels.xstprocess.processables;

import com.izforge.izpack.api.data.Variables;
import com.izforge.izpack.panels.xstprocess.AbstractUIProcessHandler;
import com.izforge.izpack.panels.xstprocess.ProcessPanelWorker;
import com.izforge.izpack.panels.xstprocess.Processable;
import com.izforge.izpack.util.Debug;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;








public class ExecutableClass
  extends Processable
{
  private static final Logger logger = Logger.getLogger(ProcessPanelWorker.class.getName());
  
  private final String myClassName;
  
  private final List<String> myArguments;
  
  protected AbstractUIProcessHandler myHandler;
  
  public ExecutableClass(String className, List<String> args)
  {
    this.myClassName = className;
    this.myArguments = args;
  }
  

  public boolean run()
  {
    boolean result = false;
    

    String[] params = new String[this.myArguments.size()];
    
    int i = 0;
    for (String myArgument : this.myArguments)
    {
      params[i] = this.variables.replace(myArgument);
      i++;
    }
    
    try
    {
      ClassLoader loader = getClass().getClassLoader();
      Class<?> procClass = loader.loadClass(this.myClassName);
      
      Object instance = procClass.newInstance();
      Method method = procClass.getMethod("run", new Class[] { AbstractUIProcessHandler.class, String[].class });
      

      if (method.getReturnType().getName().equals("boolean"))
      {
        result = ((Boolean)method.invoke(instance, new Object[] { this.myHandler, params })).booleanValue();
      }
      else
      {
        method.invoke(instance, new Object[] { this.myHandler, params });
        result = true;
      }
    }
    catch (SecurityException e)
    {
      this.myHandler.emitError("Post Processing Error", "Security exception thrown when processing class: " + this.myClassName);
      
      if (Debug.isSTACKTRACE()) {
        logger.log(Level.SEVERE, "Security exception thrown when processing class: " + this.myClassName, e);
      }
    }
    catch (ClassNotFoundException e)
    {
      this.myHandler.emitError("Post Processing Error", "Cannot find processing class: " + this.myClassName);
      
      if (Debug.isSTACKTRACE()) {
        logger.log(Level.SEVERE, "Cannot find processing class: " + this.myClassName, e);
      }
    }
    catch (NoSuchMethodException e)
    {
      this.myHandler.emitError("Post Processing Error", "Processing class does not have 'run' method: " + this.myClassName);
      
      if (Debug.isSTACKTRACE()) {
        logger.log(Level.SEVERE, "Processing class does not have 'run' method: " + this.myClassName, e);
      }
    }
    catch (IllegalAccessException e)
    {
      this.myHandler.emitError("Post Processing Error", "Error accessing processing class: " + this.myClassName);
      
      if (Debug.isSTACKTRACE()) {
        logger.log(Level.SEVERE, "Error accessing processing class: " + this.myClassName, e);
      }
    }
    catch (InvocationTargetException e)
    {
      this.myHandler.emitError("Post Processing Error", "Invocation Problem calling: " + this.myClassName + ", " + e.getCause().getMessage());
      
      if (Debug.isSTACKTRACE()) {
        logger.log(Level.SEVERE, "Invocation Problem calling: " + this.myClassName, e);
      }
    }
    catch (Exception e)
    {
      this.myHandler.emitError("Post Processing Error", "Exception when running processing class: " + this.myClassName + ", " + e.getMessage());
      

      if (Debug.isSTACKTRACE()) {
        logger.log(Level.SEVERE, "Exception when running processing class: " + this.myClassName, e);
      }
    }
    catch (Error e)
    {
      this.myHandler.emitError("Post Processing Error", "Error when running processing class: " + this.myClassName + ", " + e.getMessage());
      

      if (Debug.isSTACKTRACE()) {
        logger.log(Level.SEVERE, "Error when running processing class: " + this.myClassName, e);
      }
    }
    catch (Throwable e)
    {
      this.myHandler.emitError("Post Processing Error", "Error when running processing class: " + this.myClassName + ", " + e.getMessage());
      

      if (Debug.isSTACKTRACE()) {
        logger.log(Level.SEVERE, "Error when running processing class: " + this.myClassName, e);
      }
    }
    return result;
  }
  

  public String getProcessableName()
  {
    return "Executable Class";
  }
}

