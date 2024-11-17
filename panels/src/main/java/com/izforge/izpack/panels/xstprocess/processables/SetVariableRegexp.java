/*    */ package com.izforge.izpack.panels.xstprocess.processables;
/*    */ 
/*    */ import com.izforge.izpack.api.data.Variables;
/*    */ import com.izforge.izpack.panels.xstprocess.AbstractUIProcessHandler;
/*    */ import com.izforge.izpack.panels.xstprocess.Processable;
/*    */ import java.util.regex.Matcher;
/*    */ import java.util.regex.Pattern;
/*    */ 
/*    */ public class SetVariableRegexp extends Processable
/*    */ {
/*    */   String regularExpression;
/*    */   String inputString;
/*    */   String setString;
/*    */   String setVariable;
/* 15 */   boolean failOnNoMatch = false;
/*    */   
/*    */   public SetVariableRegexp(String regularExpression, String inputString, String setString, String setVariable, boolean failOnNoMatch) {
/* 18 */     this.regularExpression = regularExpression;
/* 19 */     this.inputString = inputString;
/* 20 */     this.setString = setString;
/* 21 */     this.setVariable = setVariable;
/* 22 */     this.failOnNoMatch = failOnNoMatch;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean run()
/*    */   {
/* 30 */     this.inputString = this.variables.replace(this.inputString);
/* 31 */     this.setString = this.variables.replace(this.setString);
/* 32 */     this.setVariable = this.variables.replace(this.setVariable);
/*    */     
/* 34 */     this.handler.logOutput("String to match against: " + this.inputString, false);
/* 35 */     this.handler.logOutput("String to set variable to after regexp: " + this.setString, false);
/* 36 */     this.handler.logOutput("Variable to set result to: " + this.setVariable, false);
/*    */     
/* 38 */     Pattern regExpPattern = Pattern.compile(this.regularExpression);
/* 39 */     Matcher matcher = regExpPattern.matcher(this.inputString);
/*    */     
/*    */ 
/* 42 */     boolean matchFound = matcher.matches();
/*    */     
/*    */ 
/* 45 */     if ((!matchFound) && (this.failOnNoMatch)) {
/* 46 */       this.variables.set(this.setVariable, "");
/* 47 */       this.handler.logOutput("No match found at all, fail on no match set....failing....", true);
/* 48 */       return false; }
/* 49 */     if ((!matchFound) && (!this.failOnNoMatch)) {
/* 50 */       this.variables.set(this.setVariable, this.setString);
/* 51 */       this.handler.logOutput("No match found at all, fail on no match set to false.\nSetting variable to set string: " + this.setString, true);
/* 52 */       return true;
/*    */     }
/*    */     
/* 55 */     this.handler.logOutput("\nMatch found!\n", false);
/*    */     
/*    */ 
/* 58 */     int groupCount = matcher.groupCount();
/* 59 */     this.handler.logOutput("\nMatching groups found in pattern: " + groupCount + "\n", false);
/*    */     
/*    */ 
/* 62 */     for (int i = 1; i <= groupCount; i++) {
/* 63 */       String groupMatch = matcher.group(i);
/*    */       
/* 65 */       if (groupMatch == null) {
/* 66 */         this.handler.logOutput("No match found for match group " + i + ", not replacing. ", true);
/*    */       }
/*    */       else
/*    */       {
/* 70 */         this.handler.logOutput("Replacing all \\" + i + " with " + groupMatch, false);
/* 71 */         this.setString = this.setString.replaceAll("\\\\" + i, groupMatch);
/*    */       }
/*    */     }
/*    */     
/*    */ 
/* 76 */     this.setString = this.setString.replaceAll("\\\\\\d", "");
/*    */     
/*    */ 
/* 79 */     this.variables.set(this.setVariable, this.setString);
/* 80 */     this.handler.logOutput("\nAssigning Final string to variable '" + this.setVariable + "' : " + this.setString, false);
/*    */     
/* 82 */     return true;
/*    */   }
/*    */   
/*    */   public String getProcessableName()
/*    */   {
/* 87 */     return "Set variable regular expression.";
/*    */   }
/*    */ }


/* Location:              D:\stage-installer-01-30-2023\postghost\installer\JCG-Xstore17-InstallerCopy.jar!\com\izforge\izpack\panels\xstprocess\processables\SetVariableRegexp.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */