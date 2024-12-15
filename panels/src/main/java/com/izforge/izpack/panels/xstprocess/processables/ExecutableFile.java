package com.izforge.izpack.panels.xstprocess.processables;

import com.izforge.izpack.api.data.Variables;
import com.izforge.izpack.panels.xstprocess.AbstractUIProcessHandler;
import com.izforge.izpack.panels.xstprocess.Processable;
import com.izforge.izpack.util.IoHelper;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExecutableFile
  extends Processable
{
  private PrintWriter logfile = null;
  
  private String filename;
  
  private String workingDir;
  
  private List<String> arguments;
  
  private List<String> envvariables;
  
  protected boolean reloadPath = false;
  
  public ExecutableFile(String fn, List<String> args, List<String> envvars, String workingDir)
  {
    this.filename = fn;
    this.arguments = args;
    this.envvariables = envvars;
    this.workingDir = workingDir;
  }
  
  public ExecutableFile(String fn, List<String> args, List<String> envvars, String workingDir, boolean reloadPath)
  {
    this.filename = fn;
    this.arguments = args;
    this.envvariables = envvars;
    this.workingDir = workingDir;
    this.reloadPath = reloadPath;
  }
  
  public boolean run()
  {
    List<String> params = new ArrayList(this.arguments.size() + 1);
    
    try
    {
      params.add(this.variables.replace(this.filename));
    } catch (Exception e) {
      params.add(this.filename);
    }
    

    for (String argument : this.arguments) {
      try
      {
        params.add(this.variables.replace(argument));
      } catch (Exception e) {
        params.add(argument);
      }
    }
    
    ProcessBuilder processBuilder = new ProcessBuilder(params);
    

    if ((this.workingDir != null) && (!this.workingDir.equals("")))
    {
      this.workingDir = IoHelper.translatePath(this.workingDir, this.variables);
      processBuilder.directory(new File(this.workingDir));
    }
    

    Map<String, String> environment = processBuilder.environment();
    for (String envvar : this.envvariables)
    {
      String ev = this.variables.replace(envvar);
      int i = ev.indexOf("=");
      if (i > 0) {
        environment.put(ev.substring(0, i), ev.substring(i + 1));
      }
    }
    

    if (this.reloadPath) {
      String path = reloadPath();
      environment.put("PATH", path);
    }
    

    try
    {
      String logCommand = "Starting Executable: ";
      for (String arg : processBuilder.command()) {
        logCommand = logCommand + arg + " ";
      }
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
  
  private String reloadPath()
  {
    String path = "";
    

    ArrayList<String> regLocs = new ArrayList();
    regLocs.add("HKLM\\SYSTEM\\CurrentControlSet\\Control\\Session Manager\\Environment");
    regLocs.add("HKCU\\Environment");
    

    for (String regLoc : regLocs)
    {
      ProcessBuilder systemPathPb = new ProcessBuilder(new String[] { "reg.exe", "query", regLoc, "/v", "Path" });
      
      systemPathPb.redirectErrorStream(false);
      
      try
      {
        Process systemPathProcess = systemPathPb.start();
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(systemPathProcess.getInputStream()));
        

        Thread.sleep(500L);
        String s = null;
        String tempPath = "";
        while ((s = stdInput.readLine()) != null) {
          tempPath = tempPath + s;
        }
        

        String[] parsed = tempPath.split("REG_EXPAND_SZ");
        path = path + parsed[(parsed.length - 1)].trim();
        path = path + ";";
      }
      catch (Exception e) {}
    }
    


    this.handler.logOutput("Path reloaded, New path: " + path, false);
    return path;
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
          
          if (ExecutableFile.this.logfile != null)
          {
            ExecutableFile.this.logfile.println(line);
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
        


        if (ExecutableFile.this.logfile != null)
        {
          ExecutableFile.this.logfile.println(ioe.toString());
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
    return "Executable File";
  }
}
