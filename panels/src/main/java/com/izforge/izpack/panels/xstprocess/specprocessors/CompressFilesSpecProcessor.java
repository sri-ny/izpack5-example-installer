package com.izforge.izpack.panels.xstprocess.specprocessors;

import com.izforge.izpack.api.adaptator.IXMLElement;
import com.izforge.izpack.panels.xstprocess.Processable;
import com.izforge.izpack.panels.xstprocess.processables.CompressFiles;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class CompressFilesSpecProcessor extends SpecProcessor
{
  public boolean isApplicable(IXMLElement element)
  {
    return element.getName().equalsIgnoreCase("zip");
  }
  

  public Processable processElement(IXMLElement element)
  {
    String zipFile = element.getAttribute("zipfile");
    
    if ((zipFile.isEmpty()) || (zipFile == null)) {
      System.err.println("Zip file not specified");
      return null;
    }
    

    List<IXMLElement> zipEntriesXml = element.getChildrenNamed("zipentry");
    if (zipEntriesXml.size() < 1) {
      System.err.println("No zip entries, skipping");
      return null;
    }
    
    List<String> zipEntries = new ArrayList();
    for (IXMLElement zipElement : zipEntriesXml) {
      String entry = zipElement.getContent();
      zipEntries.add(entry);
    }
    


    return new CompressFiles(zipFile, zipEntries);
  }
}

