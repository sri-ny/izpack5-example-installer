package com.izforge.izpack.panels.xstprocess.specprocessors;

import com.izforge.izpack.api.adaptator.IXMLElement;
import com.izforge.izpack.panels.xstprocess.Processable;
import com.izforge.izpack.panels.xstprocess.processables.FileOperation;
import com.izforge.izpack.panels.xstprocess.processables.FileOperation.FileOp;
import java.util.logging.Logger;

public class FileOperationSpecProcessor
  extends SpecProcessor
{
  Logger logger = Logger.getLogger(FileOperationSpecProcessor.class.getName());
  
  public boolean isApplicable(IXMLElement element)
  {
    return (element.getName().equals("fileoperation")) || (element.getName().equals("fileop"));
  }
  

  public Processable processElement(IXMLElement element)
  {
    String fileOperation = element.getAttribute("operation");
    String srcPath = element.getAttribute("srcpath");
    String dstPath = null;
    

    if ((srcPath == null) || (srcPath.equals(""))) {
      this.logger.severe("Must be supplied a 'srcpath', instead got: " + srcPath);
      return null;
    }
    

    if ((FileOperation.FileOp.COPY.toString().equalsIgnoreCase(fileOperation)) || (FileOperation.FileOp.MOVE.toString().equalsIgnoreCase(fileOperation)))
    {

      dstPath = element.getAttribute("dstpath");
      if ((dstPath == null) || (dstPath.equals(""))) {
        this.logger.severe("Must be supplied a 'dstpath' when COPY or MOVE operation is requested, instead got: " + srcPath);
        
        return null;
      }
    }
    else if ((!FileOperation.FileOp.DELETE.toString().equalsIgnoreCase(fileOperation)) && (!FileOperation.FileOp.MKDIR.toString().equalsIgnoreCase(fileOperation)))
    {

      this.logger.severe("Invalid operation specified, must be MOVE, COPY, MKDIR or DELETE, instead got: " + fileOperation.toString());
      return null;
    }
    

    boolean failOnError = false;
    try {
      String failOnErrorString = element.getAttribute("failonerror");
      
      if (failOnErrorString != null) {
        failOnError = Boolean.parseBoolean(failOnErrorString);
      }
    }
    catch (Exception e) {}
    
    return new FileOperation(FileOperation.FileOp.valueOf(fileOperation.toUpperCase()), srcPath, dstPath, failOnError);
  }
}
