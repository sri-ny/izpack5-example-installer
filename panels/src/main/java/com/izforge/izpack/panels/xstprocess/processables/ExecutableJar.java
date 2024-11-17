package com.izforge.izpack.panels.xstprocess.processables;

import com.izforge.izpack.api.data.Variables;
import com.izforge.izpack.panels.xstprocess.AbstractUIProcessHandler;
import com.izforge.izpack.panels.xstprocess.Processable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;

public class ExecutableJar
  extends Processable
{
  String jrePath = System.getProperty("java.home");
  String jarPath = null;
  String workingPath = null;
  String[] args = null;
  


  boolean jarRelativeToWorkingDir = false;
  
  Logger logger = Logger.getLogger(ExecutableJar.class.getName());
  
  public ExecutableJar(String javaHome, String jarPath, String workingPath, String... args)
  {
    this.jarPath = jarPath;
    this.workingPath = workingPath;
    this.args = args;
    
    if (javaHome != null) {
      this.jrePath = javaHome;
    }
  }
  

  protected void setWorkingPath()
  {
    File file = new File(this.jarPath);
    this.workingPath = file.getParentFile().getName();
  }
  


  public boolean run()
  {
    this.jrePath = this.variables.replace(this.jrePath);
    String localJrePath = this.jrePath + "/bin/java.exe";
    File jre = new File(localJrePath);
    

    if (!jre.exists()) {
      this.logger.severe("Could not locate JAVA: " + jre.getAbsolutePath());
      this.handler.logOutput("Could not locate JAVA: " + jre.getAbsolutePath(), true);
      return false;
    }
    

    this.jarPath = this.variables.replace(this.jarPath);
    File jar = new File(this.jarPath);
    

    if ((!jar.isAbsolute()) && (this.jarRelativeToWorkingDir)) {
      String jarExecutePath = this.workingPath + '/' + this.jarPath;
      
      this.logger.fine("JAR path built relative to working path set: " + jarExecutePath);
      jar = new File(jarExecutePath);


    }
    else if ((jar.isAbsolute()) && (this.jarRelativeToWorkingDir)) {
      this.logger.warning("Option set that jar is relative to working path, yet an absolute path was supplied.\nYou can't do this! - JAR PATH SUPPLIED: " + this.jarPath);
      
      this.logger.warning("Using this path for JAR, ignoring the relative to working path option: " + this.jarPath);
    }
    

    if (!jar.exists()) {
      this.logger.severe("Could not locate JAR: " + jar.getAbsolutePath());
      this.handler.logOutput("Could not locate JAR: " + jar.getAbsolutePath(), true);
      return false;
    }
    
    this.workingPath = this.variables.replace(this.workingPath);
    File workingDir = new File(this.workingPath);
    if (!workingDir.exists()) {
      this.logger.severe("Could not locate working directory: " + workingDir.getAbsolutePath());
      this.handler.logOutput("Could not locate working directory: " + workingDir.getAbsolutePath(), true);
      return false;
    }
    
    ArrayList<String> launchArgs = new ArrayList();
    launchArgs.add(jre.getAbsolutePath());
    launchArgs.add("-jar");
    launchArgs.add(jar.getAbsolutePath());
    

    if (this.args != null) {
      launchArgs.addAll(Arrays.asList(this.args));
    }
    
    Process proc = null;
    try {
      ProcessBuilder pb = new ProcessBuilder(launchArgs);
      pb.directory(new File(this.workingPath));
      
      proc = pb.start();
    } catch (IOException e) {
      this.logger.severe("Error launching JAR!");
      this.logger.severe(e.toString());
    }
    
    final InputStream out = proc.getInputStream();
    final InputStream err = proc.getErrorStream();
    final AbstractUIProcessHandler uiHandler = this.handler;
    

    Runnable logRunnable = new Runnable()
    {

      public void run()
      {
        try
        {
          while ((!Thread.currentThread().isInterrupted()) && (out.available() != 0) && (err.available() != 0))
          {

            if (out.available() > 0) {
              uiHandler.logOutput(IOUtils.toString(out), false);
            }
            
            if (err.available() > 0) {
              uiHandler.logOutput(IOUtils.toString(err), true);
            }
          }
        }
        catch (IOException e) {
          ExecutableJar.this.logger.warning("Error reading from process stream err or out.");
          ExecutableJar.this.logger.warning(e.toString());
        }
        
      }
      
    };
    Thread loggingThread = new Thread(logRunnable);
    loggingThread.start();
    
    try
    {
      proc.waitFor();
    } catch (InterruptedException e) {
      this.logger.warning("JAR process was interuppted?");
      this.logger.warning(e.toString());
    }
    

    loggingThread.interrupt();
    try {
      loggingThread.join(15000L);
    } catch (InterruptedException e) {
      this.logger.warning(e.toString());
    }
    
    return true;
  }
  
  public String getJrePath() {
    return this.jrePath;
  }
  
  public void setJrePath(String jrePath) {
    this.jrePath = jrePath;
  }
  
  public boolean isJarRelativeToWorkingDir() {
    return this.jarRelativeToWorkingDir;
  }
  
  public void setJarRelativeToWorkingDir(boolean jarRelativeToWorkingDir) {
    this.jarRelativeToWorkingDir = jarRelativeToWorkingDir;
  }
  
  public String getProcessableName()
  {
    return "Executable JAR";
  }
}

