/*     */ package com.izforge.izpack.panels.xstprocess;
/*     */ 
/*     */ import com.izforge.izpack.api.adaptator.IXMLElement;
/*     */ import com.izforge.izpack.api.data.Panel;
/*     */ import com.izforge.izpack.api.resource.Resources;
/*     */ import com.izforge.izpack.api.rules.RulesEngine;
/*     */ import com.izforge.izpack.installer.data.GUIInstallData;
/*     */ import com.izforge.izpack.installer.gui.InstallerFrame;
/*     */ import com.izforge.izpack.installer.gui.IzPanel;
/*     */ import com.izforge.izpack.util.PlatformModelMatcher;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Color;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.util.List;
/*     */ import javax.swing.BoxLayout;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JProgressBar;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JTextPane;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import javax.swing.text.SimpleAttributeSet;
/*     */ import javax.swing.text.StyleConstants;
/*     */ import javax.swing.text.StyledDocument;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XstProcessPanel
/*     */   extends IzPanel
/*     */   implements AbstractUIProcessHandler
/*     */ {
/*     */   private static final long serialVersionUID = 3258417209583155251L;
/*     */   protected JLabel processLabel;
/*     */   protected JProgressBar overallProgressBar;
/*  82 */   private boolean validated = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private ProcessPanelWorker worker;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  92 */   private int noOfJobs = 0;
/*     */   
/*  94 */   private int currentJob = 0;
/*     */   
/*     */ 
/*     */ 
/*     */   private JTextPane outputPane;
/*     */   
/*     */ 
/*     */ 
/* 102 */   private boolean finishedWork = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public XstProcessPanel(Panel panel, InstallerFrame parent, GUIInstallData installData, Resources resources, RulesEngine rules, PlatformModelMatcher matcher)
/*     */   {
/* 117 */     super(panel, parent, installData, resources);
/*     */     
/* 119 */     this.worker = new ProcessPanelWorker(installData, rules, resources, matcher);
/* 120 */     this.worker.setHandler(this);
/* 121 */     JLabel heading = new JLabel();
/* 122 */     Font font = heading.getFont();
/* 123 */     font = font.deriveFont(1, font.getSize() * 2.0F);
/* 124 */     heading.setFont(font);
/* 125 */     heading.setHorizontalAlignment(0);
/* 126 */     heading.setText(getString("ProcessPanel.heading"));
/* 127 */     heading.setVerticalAlignment(1);
/* 128 */     BorderLayout layout = new BorderLayout();
/* 129 */     layout.setHgap(2);
/* 130 */     layout.setVgap(2);
/* 131 */     setLayout(layout);
/* 132 */     add(heading, "North");
/*     */     
/*     */ 
/*     */ 
/* 136 */     JPanel subpanel = new JPanel();
/*     */     
/* 138 */     subpanel.setAlignmentX(0.5F);
/* 139 */     subpanel.setLayout(new BoxLayout(subpanel, 1));
/*     */     
/* 141 */     this.processLabel = new JLabel();
/* 142 */     this.processLabel.setAlignmentX(0.5F);
/* 143 */     this.processLabel.setText(" ");
/* 144 */     subpanel.add(this.processLabel);
/*     */     
/* 146 */     this.overallProgressBar = new JProgressBar();
/* 147 */     this.overallProgressBar.setAlignmentX(0.5F);
/* 148 */     this.overallProgressBar.setStringPainted(true);
/* 149 */     subpanel.add(this.overallProgressBar);
/*     */     
/*     */ 
/* 152 */     this.outputPane = new JTextPane();
/*     */     
/* 154 */     this.outputPane.setEditable(false);
/* 155 */     JScrollPane outputScrollPane = new JScrollPane(this.outputPane);
/* 156 */     subpanel.add(outputScrollPane);
/*     */     
/* 158 */     add(subpanel, "Center");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isValidated()
/*     */   {
/* 168 */     return this.validated;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void startProcessing(final int no_of_jobs)
/*     */   {
/* 176 */     this.noOfJobs = no_of_jobs;
/* 177 */     SwingUtilities.invokeLater(new Runnable()
/*     */     {
/*     */       public void run()
/*     */       {
/* 181 */         XstProcessPanel.this.overallProgressBar.setMaximum(no_of_jobs);
/* 182 */         XstProcessPanel.this.overallProgressBar.setIndeterminate(true);
/* 183 */         XstProcessPanel.this.parent.lockPrevButton();
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void finishProcessing(final boolean unlockPrev, final boolean unlockNext)
/*     */   {
/* 193 */     SwingUtilities.invokeLater(new Runnable()
/*     */     {
/*     */       public void run()
/*     */       {
/* 197 */         XstProcessPanel.this.overallProgressBar.setIndeterminate(false);
/*     */         
/* 199 */         String no_of_jobs = Integer.toString(XstProcessPanel.this.noOfJobs);
/* 200 */         XstProcessPanel.this.overallProgressBar.setString(no_of_jobs + " / " + no_of_jobs);
/*     */         
/* 202 */         XstProcessPanel.this.processLabel.setText(" ");
/* 203 */         XstProcessPanel.this.processLabel.setEnabled(false);
/*     */         
/* 205 */         XstProcessPanel.this.validated = true;
/* 206 */         XstProcessPanel.this.installData.setInstallSuccess(XstProcessPanel.this.worker.getResult());
/* 207 */         if (XstProcessPanel.this.installData.getPanels().indexOf(XstProcessPanel.this) != XstProcessPanel.this.installData.getPanels().size() - 1)
/*     */         {
/*     */ 
/* 210 */           if (unlockNext)
/*     */           {
/* 212 */             XstProcessPanel.this.parent.unlockNextButton();
/*     */           }
/*     */         }
/* 215 */         if (unlockPrev)
/*     */         {
/* 217 */           XstProcessPanel.this.parent.unlockPrevButton();
/*     */         }
/*     */         
/*     */ 
/* 221 */         XstProcessPanel.this.finishedWork = XstProcessPanel.this.installData.isInstallSuccess();
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void logOutput(String message, boolean stderr)
/*     */   {
/* 235 */     final boolean stdError = stderr;
/* 236 */     final String logMessage = message;
/*     */     
/*     */ 
/*     */ 
/* 240 */     final StyledDocument doc = this.outputPane.getStyledDocument();
/* 241 */     final SimpleAttributeSet style = new SimpleAttributeSet();
/*     */     
/*     */ 
/*     */ 
/* 245 */     SwingUtilities.invokeLater(new Runnable()
/*     */     {
/*     */ 
/*     */       public void run()
/*     */       {
/* 250 */         if (stdError) {
/* 251 */           StyleConstants.setForeground(style, Color.RED);
/*     */           try
/*     */           {
/* 254 */             doc.insertString(doc.getLength(), "\n" + logMessage, style);
/*     */           } catch (BadLocationException e) {
/* 256 */             e.printStackTrace();
/*     */           }
/*     */         }
/*     */         else {
/*     */           try {
/* 261 */             doc.insertString(doc.getLength(), "\n" + logMessage, style);
/*     */           } catch (BadLocationException e) {
/* 263 */             e.printStackTrace();
/*     */           }
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void startProcess(final String jobName)
/*     */   {
/* 279 */     final StyledDocument doc = this.outputPane.getStyledDocument();
/* 280 */     final SimpleAttributeSet style = new SimpleAttributeSet();
/*     */     
/*     */ 
/* 283 */     StyleConstants.setBold(style, true);
/*     */     
/* 285 */     SwingUtilities.invokeLater(new Runnable()
/*     */     {
/*     */ 
/*     */       public void run()
/*     */       {
/* 290 */         XstProcessPanel.this.processLabel.setText(jobName);
/* 291 */         XstProcessPanel.access$1108(XstProcessPanel.this);
/* 292 */         XstProcessPanel.this.overallProgressBar.setValue(XstProcessPanel.this.currentJob);
/* 293 */         XstProcessPanel.this.overallProgressBar.setString(String.valueOf(XstProcessPanel.this.currentJob) + " / " + String.valueOf(XstProcessPanel.this.noOfJobs));
/*     */         
/*     */ 
/*     */ 
/* 297 */         String message = "->Starting Job: " + jobName + " <-";
/*     */         
/*     */         try
/*     */         {
/* 301 */           doc.insertString(doc.getLength(), "\n\n" + message, style);
/*     */         } catch (BadLocationException e) {
/* 303 */           e.printStackTrace();
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void finishProcess()
/*     */   {
/* 313 */     final StyledDocument doc = this.outputPane.getStyledDocument();
/* 314 */     final SimpleAttributeSet style = new SimpleAttributeSet();
/*     */     
/*     */ 
/* 317 */     StyleConstants.setBold(style, true);
/*     */     
/* 319 */     SwingUtilities.invokeLater(new Runnable()
/*     */     {
/*     */       public void run()
/*     */       {
/* 323 */         String message = "\n->Ended Job<-\n";
/*     */         
/*     */         try
/*     */         {
/* 327 */           doc.insertString(doc.getLength(), message, style);
/*     */         } catch (BadLocationException e) {
/* 329 */           e.printStackTrace();
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void panelActivate()
/*     */   {
/* 343 */     Dimension dimension = this.parent.getPanelsContainerSize();
/* 344 */     dimension.width -= dimension.width / 4;
/* 345 */     dimension.height = 150;
/* 346 */     setMinimumSize(dimension);
/* 347 */     setMaximumSize(dimension);
/* 348 */     setPreferredSize(dimension);
/*     */     
/* 350 */     this.parent.lockNextButton();
/*     */     
/* 352 */     this.currentJob = 0;
/*     */     
/*     */ 
/* 355 */     if (!this.finishedWork)
/*     */     {
/* 357 */       this.worker.startThread();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void makeXMLData(IXMLElement panelRoot) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void skipProcess(String name, String message)
/*     */   {
/* 373 */     this.processLabel.setText(name);
/* 374 */     this.currentJob += 1;
/* 375 */     this.overallProgressBar.setValue(this.currentJob);
/* 376 */     this.overallProgressBar.setString(String.valueOf(this.currentJob) + " / " + String.valueOf(this.noOfJobs));
/*     */     
/*     */ 
/* 379 */     final StyledDocument doc = this.outputPane.getStyledDocument();
/* 380 */     final SimpleAttributeSet style = new SimpleAttributeSet();
/*     */     
/* 382 */     final String jobName = name;
/* 383 */     final String jobMessage = message;
/*     */     
/*     */ 
/* 386 */     StyleConstants.setBold(style, true);
/*     */     
/* 388 */     SwingUtilities.invokeLater(new Runnable()
/*     */     {
/*     */       public void run()
/*     */       {
/* 392 */         String message = "\n->Skipping job: " + jobName + "<-\nReason:" + jobMessage + "\n\n";
/*     */         
/*     */         try
/*     */         {
/* 396 */           doc.insertString(doc.getLength(), message, style);
/*     */         } catch (BadLocationException e) {
/* 398 */           e.printStackTrace();
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */ }


/* Location:              D:\stage-installer-01-30-2023\postghost\installer\JCG-Xstore17-InstallerCopy.jar!\com\izforge\izpack\panels\xstprocess\XstProcessPanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */