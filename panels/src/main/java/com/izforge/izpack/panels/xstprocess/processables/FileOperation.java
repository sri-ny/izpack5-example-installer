package com.izforge.izpack.panels.xstprocess.processables;

import com.izforge.izpack.api.data.Variables;
import com.izforge.izpack.panels.xstprocess.AbstractUIProcessHandler;
import com.izforge.izpack.panels.xstprocess.Processable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;
import org.apache.commons.io.FileUtils;



public class FileOperation
  extends Processable
{
  FileOp fileOperation;
  String srcPath;
  String dstPath;
  
  public static enum FileOp
  {
    COPY, 
    MOVE, 
    DELETE, 
    MKDIR;
    
    private FileOp() {}
  }
  
  boolean failOnError = false;
  
  public FileOperation(FileOp fileOperation, String srcPath, String dstPath, boolean failOnError) {
    this.fileOperation = fileOperation;
    this.srcPath = srcPath;
    this.dstPath = dstPath;
    this.failOnError = failOnError;
  }
  
  public boolean run()
  {
    this.handler.logOutput("Fail on error is set to: " + this.failOnError, false);
    
    File srcPath = new File(this.variables.replace(this.srcPath));
    File dstPath = null;
    
    if (this.dstPath != null) {
      dstPath = new File(this.variables.replace(this.dstPath));
    }
    

    if ((this.fileOperation == FileOp.MOVE) || (this.fileOperation == FileOp.COPY))
    {
      try
      {
        if (srcPath.isDirectory()) {
          this.handler.logOutput("Source determined to be directory: " + srcPath.getAbsolutePath(), false);
          FileUtils.copyDirectoryToDirectory(srcPath, dstPath);
        } else {
          this.handler.logOutput("Source determined to be file: " + srcPath.getAbsolutePath(), false);
          
          String fileAbsPath = dstPath.getAbsolutePath();
          

          if ((fileAbsPath.endsWith("/")) || (fileAbsPath.endsWith("\\")))
          {
            this.handler.logOutput("Copying file: " + srcPath.getAbsolutePath() + "to directory: " + dstPath.getAbsolutePath(), false);
            

            FileUtils.copyFileToDirectory(srcPath, dstPath);
          }
          else
          {
            this.handler.logOutput("Copying file: " + srcPath.getAbsolutePath() + "to file: " + dstPath.getAbsolutePath(), false);
            

            FileUtils.copyFile(srcPath, dstPath);
          }
        }
        

        if (this.fileOperation == FileOp.MOVE) {
          this.handler.logOutput("Deleting source: " + srcPath, false);
          FileUtils.forceDelete(srcPath);
        }
      }
      catch (IOException e)
      {
        this.handler.logOutput("File operation error!", true);
        this.handler.logOutput(e.getMessage(), true);
        
        if (this.failOnError) {
          this.handler.logOutput("Fail on error set!", true);
          e.printStackTrace();
          return false;

        }
        

      }
      
    }
    else if (this.fileOperation == FileOp.DELETE) {
      try
      {
        FileUtils.forceDelete(srcPath);
        
        this.handler.logOutput("Deleted: " + srcPath.getAbsolutePath(), false);
      }
      catch (IOException e)
      {
        this.handler.logOutput("Error deleteing: " + srcPath.getAbsolutePath(), true);
        this.handler.logOutput(e.getMessage(), true);
        
        if (this.failOnError) {
          this.handler.logOutput("Fail on error set!", true);
          e.printStackTrace();
          return false;

        }
        
      }
      
    }
    else if (this.fileOperation == FileOp.MKDIR) {
      try {
        Files.createDirectories(srcPath.toPath(), new FileAttribute[0]);
        this.handler.logOutput("Created: " + srcPath.getAbsolutePath(), false);
      }
      catch (IOException e) {
        this.handler.logOutput("Error creating directory: " + srcPath.getAbsolutePath(), true);
        
        if (this.failOnError) {
          this.handler.logOutput(e.getMessage(), true);
          e.printStackTrace();
          return false;
        }
      }
    }
    
    return true;
  }
  
  public String getProcessableName()
  {
    return "File operation: " + this.fileOperation.toString();
  }
}
