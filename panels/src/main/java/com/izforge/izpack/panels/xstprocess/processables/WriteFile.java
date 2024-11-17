package com.izforge.izpack.panels.xstprocess.processables;

import com.izforge.izpack.api.data.Variables;
import com.izforge.izpack.panels.xstprocess.AbstractUIProcessHandler;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class WriteFile extends com.izforge.izpack.panels.xstprocess.Processable
{
  String file;
  String contents;
  boolean append = false;
  boolean createParentDir = false;
  boolean failOnError = false;
  boolean isDirectory = false;
  
  public WriteFile(String file, String contents, boolean append, boolean createParentDir, boolean failOnError)
  {
    this.file = file;
    this.contents = contents;
    this.append = append;
    this.createParentDir = createParentDir;
    this.failOnError = failOnError;
  }
  
  public WriteFile(String file, boolean createParentDir, boolean failOnError, boolean isDirectory) {
    this.file = file;
    this.createParentDir = createParentDir;
    this.failOnError = failOnError;
    this.isDirectory = isDirectory;
  }
  
  public boolean run() {
    this.handler.logOutput("Writing to file: " + this.file + " - Append: " + this.append, false);
    
    this.file = this.variables.replace(this.file);
    File theFile = new File(this.file);
    this.contents = this.variables.replace(this.contents);
    


    if (this.createParentDir) {
      theFile.getParentFile().mkdirs();
    }
    
    if (this.isDirectory) {
      try {
        theFile.mkdir();
        this.handler.logOutput("Directory written successfully!", false);
        return true;
      } catch (Exception e) {
        if (this.failOnError) {
          this.handler.logOutput("Writing to file failed! - " + e.getMessage(), true);
          e.printStackTrace();
          return false;
        }
        return true;
      }
    }
    
    try
    {
      FileWriter fw = new FileWriter(theFile, this.append);
      fw.write(this.contents);
      fw.close();
    } catch (IOException e) {
      if (this.failOnError) {
        this.handler.logOutput("Writing to file failed! - " + e.getMessage(), true);
        e.printStackTrace();
        return false;
      }
    }
    
    this.handler.logOutput("File written successfully!", false);
    return true;
  }
  

  public String getProcessableName()
  {
    return "Write File: " + this.file;
  }
}

