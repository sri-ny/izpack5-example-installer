package com.izforge.izpack.panels.xstprocess.processables;

import com.izforge.izpack.api.adaptator.IXMLElement;
import com.izforge.izpack.panels.xstprocess.Processable;
import com.izforge.izpack.panels.xstprocess.specprocessors.CompressFilesSpecProcessor;
import com.izforge.izpack.panels.xstprocess.specprocessors.EchoSpecProcessor;
import com.izforge.izpack.panels.xstprocess.specprocessors.EscapeStringSpecProcessor;
import com.izforge.izpack.panels.xstprocess.specprocessors.ExecutableClassSpecProcessor;
import com.izforge.izpack.panels.xstprocess.specprocessors.ExecutableFileSpecProcessor;
import com.izforge.izpack.panels.xstprocess.specprocessors.ExecutableJarSpecProcessor;
import com.izforge.izpack.panels.xstprocess.specprocessors.FileOperationSpecProcessor;
import com.izforge.izpack.panels.xstprocess.specprocessors.ReadCsvFileSpecProcessor;
import com.izforge.izpack.panels.xstprocess.specprocessors.ReadEncryptedPropertyFileSpecProcessor;
import com.izforge.izpack.panels.xstprocess.specprocessors.ReadPropertyFileSpecProcessor;
import com.izforge.izpack.panels.xstprocess.specprocessors.RunDatabaseScriptSpecProcessor;
import com.izforge.izpack.panels.xstprocess.specprocessors.SetPropertyFileSpecProcessor;
import com.izforge.izpack.panels.xstprocess.specprocessors.SetVariableRegexpSpecProcessor;
import com.izforge.izpack.panels.xstprocess.specprocessors.SetVariableSpecProcessor;
import com.izforge.izpack.panels.xstprocess.specprocessors.ShutdownSpecProcessor;
import com.izforge.izpack.panels.xstprocess.specprocessors.SleepSpecProcessor;
import com.izforge.izpack.panels.xstprocess.specprocessors.SpecProcessor;
import com.izforge.izpack.panels.xstprocess.specprocessors.TrimLeadingZeroesSpecProcessor;
import com.izforge.izpack.panels.xstprocess.specprocessors.WindowsServiceSpecProcessor;
import com.izforge.izpack.panels.xstprocess.specprocessors.WindowsSetSystemEnvSpecProcessor;
import com.izforge.izpack.panels.xstprocess.specprocessors.WriteFileSpecProcessor;
import com.izforge.izpack.panels.xstprocess.specprocessors.WriteRegistryValueSpecProcessor;
import java.util.ArrayList;
import java.util.logging.Logger;


public class ProcessableFactory
{
  private static final Logger logger = Logger.getLogger(ProcessableFactory.class.getName());
  
  ArrayList<SpecProcessor> specProcessors = new ArrayList();
  
  public ProcessableFactory() {
    this.specProcessors.add(new ExecutableClassSpecProcessor());
    this.specProcessors.add(new ExecutableFileSpecProcessor());
    this.specProcessors.add(new ExecutableJarSpecProcessor());
    this.specProcessors.add(new SetPropertyFileSpecProcessor());
    this.specProcessors.add(new ReadPropertyFileSpecProcessor());
    this.specProcessors.add(new ReadEncryptedPropertyFileSpecProcessor());
    this.specProcessors.add(new ReadCsvFileSpecProcessor());
    this.specProcessors.add(new SetVariableSpecProcessor());
    this.specProcessors.add(new RunDatabaseScriptSpecProcessor());
    this.specProcessors.add(new FileOperationSpecProcessor());
    this.specProcessors.add(new WriteRegistryValueSpecProcessor());
    this.specProcessors.add(new SleepSpecProcessor());
    this.specProcessors.add(new WriteFileSpecProcessor());
    this.specProcessors.add(new WindowsSetSystemEnvSpecProcessor());
    this.specProcessors.add(new WindowsServiceSpecProcessor());
    this.specProcessors.add(new ShutdownSpecProcessor());
    this.specProcessors.add(new TrimLeadingZeroesSpecProcessor());
    this.specProcessors.add(new SetVariableRegexpSpecProcessor());
    this.specProcessors.add(new CompressFilesSpecProcessor());
    
    this.specProcessors.add(new EscapeStringSpecProcessor());
    this.specProcessors.add(new EchoSpecProcessor());
    logger.finest(this.specProcessors.size() + " spec processors loaded.");
  }
  

  public Processable getProcessableForElement(IXMLElement actionElement)
  {
    String actionType = actionElement.getName();
    if ((actionType == null) || (actionType.equals(""))) {
      return null;
    }
    

    for (SpecProcessor processor : this.specProcessors) {
      if (processor.isApplicable(actionElement)) {
        Processable processable = processor.createProcessable(actionElement);
        
        logger.fine("Processable class determined for: " + processable.getClass().getCanonicalName() + " for element name: " + actionType);
        
        return processable;
      }
    }
    

    logger.warning("No Processable spec parser found for: " + actionType);
    return null;
  }
}

