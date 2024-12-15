package com.izforge.izpack.panels.xstprocess.processables;

import com.izforge.izpack.panels.xstprocess.AbstractUIProcessHandler;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipOutputStream;

public class CompressFiles extends com.izforge.izpack.panels.xstprocess.Processable
{
  String zipFile;
  List<String> fileList;
  
  public static enum COMPRESSION_TYPE
  {
    ZIP;
    
    private COMPRESSION_TYPE() {}
  }
  
  public CompressFiles(String zipFile, List<String> fileList)
  {
    this.zipFile = zipFile;
    this.fileList = fileList;
  }
  

  public boolean run()
  {
    return zip();
  }
  

  public boolean zip()
  {
    this.zipFile = this.variables.replace(this.zipFile);
    try
    {
      java.io.FileOutputStream fos = new java.io.FileOutputStream(this.zipFile);
      ZipOutputStream zos = new ZipOutputStream(fos);
      
      this.handler.logOutput("*** Zipping to Zip : " + this.zipFile + " ***", false);
      
      for (String file : this.fileList)
      {
        theFile = new File(this.variables.replace(file));
        

        if (!theFile.isAbsolute()) {
          this.handler.logOutput("Path must be absolute, skipping: " + file, true);



        }
        else if (!theFile.exists()) {
          this.handler.logOutput("Can not locate file to add to zip, skipping: " + file, true);



        }
        else if (theFile.isFile())
        {
          this.handler.logOutput("Adding file: " + theFile + " to root of ZIP", false);
          addFileToZip(theFile, theFile.getName(), zos);
        }
        else {
          this.handler.logOutput("Adding files in: " + theFile.getAbsolutePath(), false);
          
          List<File> fileInDir = generateFileList(theFile);
          
          for (File dirFile : fileInDir)
          {
            String parentPath = theFile.getParent();
            parentPath = parentPath.replaceAll("\\\\+$", "");
            parentPath = parentPath.replaceAll("\\/+$", "");
            

            int pathLength = parentPath.length();
            String fileRelativeToSource = dirFile.getAbsolutePath().substring(pathLength);
            fileRelativeToSource = fileRelativeToSource.replaceAll("^\\\\+", "");
            fileRelativeToSource = fileRelativeToSource.replaceAll("^\\/", "");
            
            this.handler.logOutput("-->Adding: " + fileRelativeToSource, false);
            addFileToZip(dirFile, fileRelativeToSource, zos);
          }
        }
      }
      
      File theFile;
      
      zos.closeEntry();
      
      zos.close();
      
      this.handler.logOutput("*** Zip complete ***", false);
    } catch (IOException ex) {
      this.handler.logOutput("Error zipping!", true);
      this.handler.logOutput(ex.getMessage(), true);
      ex.printStackTrace();
      
      return false;
    }
    
    return true;
  }
  
  protected void addFileToZip(File sourceFile, String relativePath, ZipOutputStream zos) throws IOException
  {
    java.util.zip.ZipEntry ze = new java.util.zip.ZipEntry(relativePath);
    zos.putNextEntry(ze);
    
    byte[] buffer = new byte['Ѐ'];
    
    FileInputStream in = new FileInputStream(sourceFile);
    
    int len;
    while ((len = in.read(buffer)) > 0) {
      zos.write(buffer, 0, len);
    }
    
    in.close();
  }
  
  public List<File> generateFileList(File node)
  {
    List<File> list = new java.util.ArrayList();
    

    if (node.isFile()) {
      list.add(node);
    }
    
    if (node.isDirectory()) {
      String[] subNote = node.list();
      for (String filename : subNote) {
        List<File> returnList = generateFileList(new File(node, filename));
        list.addAll(returnList);
      }
    }
    
    return list;
  }
  
  public String getProcessableName()
  {
    return "Compress Files";
  }
}

