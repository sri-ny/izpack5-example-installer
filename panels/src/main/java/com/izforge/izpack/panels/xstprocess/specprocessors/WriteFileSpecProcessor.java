package com.izforge.izpack.panels.xstprocess.specprocessors;

import com.izforge.izpack.api.adaptator.IXMLElement;
import com.izforge.izpack.panels.xstprocess.Processable;
import com.izforge.izpack.panels.xstprocess.processables.WriteFile;
import java.util.logging.Logger;

public class WriteFileSpecProcessor
  extends SpecProcessor
{
  Logger logger = Logger.getLogger(WriteFileSpecProcessor.class.getName());
  
  public boolean isApplicable(IXMLElement element)
  {
    return element.getName().equalsIgnoreCase("writefile");
  }
  
  public Processable processElement(IXMLElement element)
  {
    String file = element.getAttribute("file");
    String contents = element.getContent();
    String createParentDir = element.getAttribute("createparentdir");
    String append = element.getAttribute("append");
    String failOnError = element.getAttribute("failonerror");
    String isDirectory = element.getAttribute("isdirectory");
    
    boolean createParentDirBool = false;
    boolean appendBool = false;
    boolean failOnErrorBool = false;
    boolean isDirectoryBool = false;
    

    if ((file == null) || (file.equals(""))) {
      this.logger.severe("Skipping writing file...A file attribute is required!");
      return null;
    }
    

    if (contents == null) {
      contents = "";
    }
    
    if ((createParentDir != null) && (createParentDir.equalsIgnoreCase("true"))) {
      createParentDirBool = true;
    }
    if ((append != null) && (append.equalsIgnoreCase("true"))) {
      appendBool = true;
    }
    if ((failOnError != null) && (failOnError.equalsIgnoreCase("true"))) {
      failOnErrorBool = true;
    }
    if ((isDirectory != null) && (isDirectory.equalsIgnoreCase("true"))) {
      isDirectoryBool = true;
    }
    
    if (isDirectoryBool) {
      return new WriteFile(file, createParentDirBool, failOnErrorBool, isDirectoryBool);
    }
    return new WriteFile(file, contents, appendBool, createParentDirBool, failOnErrorBool);
  }
}
