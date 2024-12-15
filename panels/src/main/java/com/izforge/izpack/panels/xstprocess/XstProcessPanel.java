 package com.izforge.izpack.panels.xstprocess;
 
 import com.izforge.izpack.api.adaptator.IXMLElement;
 import com.izforge.izpack.api.data.Panel;
 import com.izforge.izpack.api.resource.Resources;
 import com.izforge.izpack.api.rules.RulesEngine;
 import com.izforge.izpack.installer.data.GUIInstallData;
 import com.izforge.izpack.installer.gui.InstallerFrame;
 import com.izforge.izpack.installer.gui.IzPanel;
 import com.izforge.izpack.util.PlatformModelMatcher;
 import java.awt.BorderLayout;
 import java.awt.Color;
 import java.awt.Dimension;
 import java.awt.Font;
 import java.util.List;
 import javax.swing.BoxLayout;
 import javax.swing.JLabel;
 import javax.swing.JPanel;
 import javax.swing.JProgressBar;
 import javax.swing.JScrollPane;
 import javax.swing.JTextPane;
 import javax.swing.SwingUtilities;
 import javax.swing.text.BadLocationException;
 import javax.swing.text.SimpleAttributeSet;
 import javax.swing.text.StyleConstants;
 import javax.swing.text.StyledDocument;

 public class XstProcessPanel
   extends IzPanel
   implements AbstractUIProcessHandler
 {
   private static final long serialVersionUID = 3258417209583155251L;
   protected JLabel processLabel;
   protected JProgressBar overallProgressBar;
   private boolean validated = false;
 
   private ProcessPanelWorker worker;
 
   private int noOfJobs = 0;
   
   private int currentJob = 0;
 
   private JTextPane outputPane;
 
   private boolean finishedWork = false;

   public XstProcessPanel(Panel panel, InstallerFrame parent, GUIInstallData installData, Resources resources, RulesEngine rules, PlatformModelMatcher matcher)
   {
     super(panel, parent, installData, resources);
     
     this.worker = new ProcessPanelWorker(installData, rules, resources, matcher);
     this.worker.setHandler(this);
     JLabel heading = new JLabel();
     Font font = heading.getFont();
     font = font.deriveFont(1, font.getSize() * 2.0F);
     heading.setFont(font);
     heading.setHorizontalAlignment(0);
     heading.setText(getString("ProcessPanel.heading"));
     heading.setVerticalAlignment(1);
     BorderLayout layout = new BorderLayout();
     layout.setHgap(2);
     layout.setVgap(2);
     setLayout(layout);
     add(heading, "North");
 

     JPanel subpanel = new JPanel();
     
     subpanel.setAlignmentX(0.5F);
     subpanel.setLayout(new BoxLayout(subpanel, 1));
     
     this.processLabel = new JLabel();
     this.processLabel.setAlignmentX(0.5F);
     this.processLabel.setText(" ");
     subpanel.add(this.processLabel);
     
     this.overallProgressBar = new JProgressBar();
     this.overallProgressBar.setAlignmentX(0.5F);
     this.overallProgressBar.setStringPainted(true);
     subpanel.add(this.overallProgressBar);
     
 
     this.outputPane = new JTextPane();
     
     this.outputPane.setEditable(false);
     JScrollPane outputScrollPane = new JScrollPane(this.outputPane);
     subpanel.add(outputScrollPane);
     
     add(subpanel, "Center");
   }
   
 
 
 
 
 
   public boolean isValidated()
   {
     return this.validated;
   }
   
 
 
 
   public void startProcessing(final int no_of_jobs)
   {
     this.noOfJobs = no_of_jobs;
     SwingUtilities.invokeLater(new Runnable()
     {
       public void run()
       {
         XstProcessPanel.this.overallProgressBar.setMaximum(no_of_jobs);
         XstProcessPanel.this.overallProgressBar.setIndeterminate(true);
         XstProcessPanel.this.parent.lockPrevButton();
       }
     });
   }

   public void finishProcessing(final boolean unlockPrev, final boolean unlockNext)
   {
     SwingUtilities.invokeLater(new Runnable()
     {
       public void run()
       {
         XstProcessPanel.this.overallProgressBar.setIndeterminate(false);
         
         String no_of_jobs = Integer.toString(XstProcessPanel.this.noOfJobs);
         XstProcessPanel.this.overallProgressBar.setString(no_of_jobs + " / " + no_of_jobs);
         
         XstProcessPanel.this.processLabel.setText(" ");
         XstProcessPanel.this.processLabel.setEnabled(false);
         
         XstProcessPanel.this.validated = true;
         XstProcessPanel.this.installData.setInstallSuccess(XstProcessPanel.this.worker.getResult());
         if (XstProcessPanel.this.installData.getPanels().indexOf(XstProcessPanel.this) != XstProcessPanel.this.installData.getPanels().size() - 1)
         {
 
           if (unlockNext)
           {
             XstProcessPanel.this.parent.unlockNextButton();
           }
         }
         if (unlockPrev)
         {
           XstProcessPanel.this.parent.unlockPrevButton();
         }
         
 
         XstProcessPanel.this.finishedWork = XstProcessPanel.this.installData.isInstallSuccess();
       }
     });
   }
 
   public void logOutput(String message, boolean stderr)
   {
     final boolean stdError = stderr;
     final String logMessage = message;
 
     final StyledDocument doc = this.outputPane.getStyledDocument();
     final SimpleAttributeSet style = new SimpleAttributeSet();

    SwingUtilities.invokeLater(new Runnable()
     {
 
       public void run()
       {
         if (stdError) {
           StyleConstants.setForeground(style, Color.RED);
           try
           {
             doc.insertString(doc.getLength(), "\n" + logMessage, style);
           } catch (BadLocationException e) {
             e.printStackTrace();
           }
         }
         else {
           try {
             doc.insertString(doc.getLength(), "\n" + logMessage, style);
           } catch (BadLocationException e) {
             e.printStackTrace();
           }
         }
       }
     });
   }
 
   public void startProcess(final String jobName)
   {
     final StyledDocument doc = this.outputPane.getStyledDocument();
     final SimpleAttributeSet style = new SimpleAttributeSet();
     
 
     StyleConstants.setBold(style, true);
     
     SwingUtilities.invokeLater(new Runnable()
     {
 
       public void run()
       {
         XstProcessPanel.this.processLabel.setText(jobName);
         XstProcessPanel.access$1108(XstProcessPanel.this);
         XstProcessPanel.this.overallProgressBar.setValue(XstProcessPanel.this.currentJob);
         XstProcessPanel.this.overallProgressBar.setString(String.valueOf(XstProcessPanel.this.currentJob) + " / " + String.valueOf(XstProcessPanel.this.noOfJobs));

         String message = "->Starting Job: " + jobName + " <-";
         
         try
         {
           doc.insertString(doc.getLength(), "\n\n" + message, style);
         } catch (BadLocationException e) {
           e.printStackTrace();
         }
       }
     });
   }

   public void finishProcess()
   {
     final StyledDocument doc = this.outputPane.getStyledDocument();
     final SimpleAttributeSet style = new SimpleAttributeSet();
     
 
     StyleConstants.setBold(style, true);
     
     SwingUtilities.invokeLater(new Runnable()
     {
       public void run()
       {
         String message = "\n->Ended Job<-\n";
         
         try
         {
           doc.insertString(doc.getLength(), message, style);
         } catch (BadLocationException e) {
           e.printStackTrace();
         }
       }
     });
   }
 
   public void panelActivate()
   {
     Dimension dimension = this.parent.getPanelsContainerSize();
     dimension.width -= dimension.width / 4;
     dimension.height = 150;
     setMinimumSize(dimension);
     setMaximumSize(dimension);
     setPreferredSize(dimension);
     
     this.parent.lockNextButton();
     
     this.currentJob = 0;
     
 
     if (!this.finishedWork)
     {
       this.worker.startThread();
     }
   }

   public void makeXMLData(IXMLElement panelRoot) {}

   public void skipProcess(String name, String message)
   {
     this.processLabel.setText(name);
     this.currentJob += 1;
     this.overallProgressBar.setValue(this.currentJob);
     this.overallProgressBar.setString(String.valueOf(this.currentJob) + " / " + String.valueOf(this.noOfJobs));
     
 
     final StyledDocument doc = this.outputPane.getStyledDocument();
     final SimpleAttributeSet style = new SimpleAttributeSet();
     
     final String jobName = name;
     final String jobMessage = message;
     
 
     StyleConstants.setBold(style, true);
     
     SwingUtilities.invokeLater(new Runnable()
     {
       public void run()
       {
         String message = "\n->Skipping job: " + jobName + "<-\nReason:" + jobMessage + "\n\n";
         
         try
         {
           doc.insertString(doc.getLength(), message, style);
         } catch (BadLocationException e) {
           e.printStackTrace();
         }
       }
     });
   }
 }

