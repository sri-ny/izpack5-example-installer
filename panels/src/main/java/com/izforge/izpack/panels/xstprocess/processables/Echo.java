/*    */ package com.izforge.izpack.panels.xstprocess.processables;
/*    */ 
/*    */ import com.izforge.izpack.api.data.Variables;
/*    */ import com.izforge.izpack.panels.xstprocess.Processable;
/*    */ 
/*    */ public class Echo extends Processable
/*    */ {
/*  8 */   String echoMessage = "";
/*    */   
/*    */   public Echo(String echoMessage) {
/* 11 */     this.echoMessage = echoMessage;
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean run()
/*    */   {
/* 17 */     this.echoMessage = this.variables.replace(this.echoMessage);
/* 18 */     this.handler.logOutput(this.echoMessage, false);
/*    */     
/* 20 */     return true;
/*    */   }
/*    */   
/*    */   public String getProcessableName()
/*    */   {
/* 25 */     return "Echo Message";
/*    */   }
/*    */ }


/* Location:              D:\stage-installer-01-30-2023\postghost\installer\JCG-Xstore17-InstallerCopy.jar!\com\izforge\izpack\panels\xstprocess\processables\Echo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */