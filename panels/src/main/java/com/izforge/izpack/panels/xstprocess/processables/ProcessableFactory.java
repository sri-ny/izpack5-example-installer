/*    */ package com.izforge.izpack.panels.xstprocess.processables;
/*    */ 
/*    */ import com.izforge.izpack.api.adaptator.IXMLElement;
/*    */ import com.izforge.izpack.panels.xstprocess.Processable;
/*    */ import com.izforge.izpack.panels.xstprocess.specprocessors.CompressFilesSpecProcessor;
/*    */ import com.izforge.izpack.panels.xstprocess.specprocessors.EchoSpecProcessor;
/*    */ import com.izforge.izpack.panels.xstprocess.specprocessors.EscapeStringSpecProcessor;
/*    */ import com.izforge.izpack.panels.xstprocess.specprocessors.ExecutableClassSpecProcessor;
/*    */ import com.izforge.izpack.panels.xstprocess.specprocessors.ExecutableFileSpecProcessor;
/*    */ import com.izforge.izpack.panels.xstprocess.specprocessors.ExecutableJarSpecProcessor;
/*    */ import com.izforge.izpack.panels.xstprocess.specprocessors.FileOperationSpecProcessor;
/*    */ import com.izforge.izpack.panels.xstprocess.specprocessors.ReadCsvFileSpecProcessor;
/*    */ import com.izforge.izpack.panels.xstprocess.specprocessors.ReadEncryptedPropertyFileSpecProcessor;
/*    */ import com.izforge.izpack.panels.xstprocess.specprocessors.ReadPropertyFileSpecProcessor;
/*    */ import com.izforge.izpack.panels.xstprocess.specprocessors.RunDatabaseScriptSpecProcessor;
/*    */ import com.izforge.izpack.panels.xstprocess.specprocessors.SetPropertyFileSpecProcessor;
/*    */ import com.izforge.izpack.panels.xstprocess.specprocessors.SetVariableRegexpSpecProcessor;
/*    */ import com.izforge.izpack.panels.xstprocess.specprocessors.SetVariableSpecProcessor;
/*    */ import com.izforge.izpack.panels.xstprocess.specprocessors.ShutdownSpecProcessor;
/*    */ import com.izforge.izpack.panels.xstprocess.specprocessors.SleepSpecProcessor;
/*    */ import com.izforge.izpack.panels.xstprocess.specprocessors.SpecProcessor;
/*    */ import com.izforge.izpack.panels.xstprocess.specprocessors.TrimLeadingZeroesSpecProcessor;
/*    */ import com.izforge.izpack.panels.xstprocess.specprocessors.WindowsServiceSpecProcessor;
/*    */ import com.izforge.izpack.panels.xstprocess.specprocessors.WindowsSetSystemEnvSpecProcessor;
/*    */ import com.izforge.izpack.panels.xstprocess.specprocessors.WriteFileSpecProcessor;
/*    */ import com.izforge.izpack.panels.xstprocess.specprocessors.WriteRegistryValueSpecProcessor;
/*    */ import java.util.ArrayList;
/*    */ import java.util.logging.Logger;
/*    */ 
/*    */ 
/*    */ public class ProcessableFactory
/*    */ {
/* 33 */   private static final Logger logger = Logger.getLogger(ProcessableFactory.class.getName());
/*    */   
/* 35 */   ArrayList<SpecProcessor> specProcessors = new ArrayList();
/*    */   
/*    */   public ProcessableFactory() {
/* 38 */     this.specProcessors.add(new ExecutableClassSpecProcessor());
/* 39 */     this.specProcessors.add(new ExecutableFileSpecProcessor());
/* 40 */     this.specProcessors.add(new ExecutableJarSpecProcessor());
/* 41 */     this.specProcessors.add(new SetPropertyFileSpecProcessor());
/* 42 */     this.specProcessors.add(new ReadPropertyFileSpecProcessor());
/* 43 */     this.specProcessors.add(new ReadEncryptedPropertyFileSpecProcessor());
/* 44 */     this.specProcessors.add(new ReadCsvFileSpecProcessor());
/* 45 */     this.specProcessors.add(new SetVariableSpecProcessor());
/* 46 */     this.specProcessors.add(new RunDatabaseScriptSpecProcessor());
/* 47 */     this.specProcessors.add(new FileOperationSpecProcessor());
/* 48 */     this.specProcessors.add(new WriteRegistryValueSpecProcessor());
/* 49 */     this.specProcessors.add(new SleepSpecProcessor());
/* 50 */     this.specProcessors.add(new WriteFileSpecProcessor());
/* 51 */     this.specProcessors.add(new WindowsSetSystemEnvSpecProcessor());
/* 52 */     this.specProcessors.add(new WindowsServiceSpecProcessor());
/* 53 */     this.specProcessors.add(new ShutdownSpecProcessor());
/* 54 */     this.specProcessors.add(new TrimLeadingZeroesSpecProcessor());
/* 55 */     this.specProcessors.add(new SetVariableRegexpSpecProcessor());
/* 56 */     this.specProcessors.add(new CompressFilesSpecProcessor());
/*    */     
/* 58 */     this.specProcessors.add(new EscapeStringSpecProcessor());
/* 59 */     this.specProcessors.add(new EchoSpecProcessor());
/* 60 */     logger.finest(this.specProcessors.size() + " spec processors loaded.");
/*    */   }
/*    */   
/*    */ 
/*    */   public Processable getProcessableForElement(IXMLElement actionElement)
/*    */   {
/* 66 */     String actionType = actionElement.getName();
/* 67 */     if ((actionType == null) || (actionType.equals(""))) {
/* 68 */       return null;
/*    */     }
/*    */     
/*    */ 
/* 72 */     for (SpecProcessor processor : this.specProcessors) {
/* 73 */       if (processor.isApplicable(actionElement)) {
/* 74 */         Processable processable = processor.createProcessable(actionElement);
/*    */         
/* 76 */         logger.fine("Processable class determined for: " + processable.getClass().getCanonicalName() + " for element name: " + actionType);
/*    */         
/* 78 */         return processable;
/*    */       }
/*    */     }
/*    */     
/*    */ 
/* 83 */     logger.warning("No Processable spec parser found for: " + actionType);
/* 84 */     return null;
/*    */   }
/*    */ }


/* Location:              D:\stage-installer-01-30-2023\postghost\installer\JCG-Xstore17-InstallerCopy.jar!\com\izforge\izpack\panels\xstprocess\processables\ProcessableFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */