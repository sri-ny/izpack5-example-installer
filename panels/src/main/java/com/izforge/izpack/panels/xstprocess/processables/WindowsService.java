package com.izforge.izpack.panels.xstprocess.processables;

import com.izforge.izpack.api.data.Variables;
import com.izforge.izpack.panels.xstprocess.AbstractUIProcessHandler;
import com.izforge.izpack.panels.xstprocess.Processable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class WindowsService
  extends Processable
{
  private PrintWriter logfile = null;
  

  String serviceName;
  
  String serviceOperation;
  

  public static enum ServiceOperation
  {
    START, 
    STOP, 
    RESTART;
    
    private ServiceOperation() {}
  }
  
  public WindowsService(String serviceName, ServiceOperation serviceOperation) { this.serviceName = serviceName;
    this.serviceOperation = serviceOperation.toString().toLowerCase();
  }
  


  public boolean run()
  {
    this.serviceName = this.variables.replace(this.serviceName);
    
    ProcessBuilder processBuilder = new ProcessBuilder(new String[] { "sc", this.serviceOperation.toString().toLowerCase(), this.serviceName });
    


    try
    {
      String logCommand = "Running " + this.serviceOperation + " on service:" + this.serviceName;
      this.handler.logOutput(logCommand, false);
      

      Process process = processBuilder.start();
      
      OutputMonitor stdoutMon = new OutputMonitor(this.handler, process.getInputStream(), false);
      
      OutputMonitor stderrMon = new OutputMonitor(this.handler, process.getErrorStream(), true);
      
      Thread stdoutThread = new Thread(stdoutMon);
      Thread stderrThread = new Thread(stderrMon);
      stdoutThread.setDaemon(true);
      stderrThread.setDaemon(true);
      stdoutThread.start();
      stderrThread.start();
      
      try
      {
        process.waitFor();
        
        stopMonitor(stdoutMon, stdoutThread);
        stopMonitor(stderrMon, stderrThread);








      }
      catch (InterruptedException ie)
      {







        process.destroy();
        this.handler.emitError("process interrupted", ie.toString());
        return false;
      }
      
    }
    catch (IOException ioe)
    {
      this.handler.emitError("I/O error", ioe.toString());
      return false;
    }
    
    return true;
  }
  


  private void stopMonitor(OutputMonitor monitor, Thread thread)
  {
    monitor.doStop();
    long softTimeout = 500L;
    try
    {
      thread.join(softTimeout);
    }
    catch (InterruptedException e) {}
    


    if (!thread.isAlive())
    {
      return;
    }
    
    thread.interrupt();
    long hardTimeout = 500L;
    try
    {
      thread.join(hardTimeout);
    }
    catch (InterruptedException e) {}
  }
  


  public class OutputMonitor
    implements Runnable
  {
    private boolean stderr = false;
    
    private AbstractUIProcessHandler handler;
    
    private BufferedReader reader;
    
    private Boolean stop = Boolean.valueOf(false);
    
    public OutputMonitor(AbstractUIProcessHandler handler, InputStream is, boolean stderr)
    {
      this.stderr = stderr;
      this.reader = new BufferedReader(new InputStreamReader(is));
      this.handler = handler;
    }
    

    public void run()
    {
      try
      {
        String line;
        while ((line = this.reader.readLine()) != null)
        {
          this.handler.logOutput(line, this.stderr);
          


          if (WindowsService.this.logfile != null)
          {
            WindowsService.this.logfile.println(line);
          }
          
          synchronized (this.stop)
          {
            if (this.stop.booleanValue())
            {
              return;
            }
          }
        }
      }
      catch (IOException ioe)
      {
        this.handler.logOutput(ioe.toString(), true);
        


        if (WindowsService.this.logfile != null)
        {
          WindowsService.this.logfile.println(ioe.toString());
        }
      }
    }
    


    public void doStop()
    {
      synchronized (this.stop)
      {
        this.stop = Boolean.valueOf(true);
      }
    }
  }
  

  public String getProcessableName()
  {
    return "Windows Service Operation:";
  }
}

