/*     */ package com.izforge.izpack.panels.xstprocess.processables;
/*     */ 
/*     */ import com.izforge.izpack.panels.xstprocess.AbstractUIProcessHandler;
/*     */ 
/*     */ public class SetVariable extends com.izforge.izpack.panels.xstprocess.Processable
/*     */ {
/*     */   public static enum PadType {
/*   8 */     MAXSTRINGSIZE, 
/*   9 */     MAXSTRINGSIZETRIM, 
/*  10 */     PADNUMTIMES;
/*     */     
/*     */     private PadType() {} }
/*     */   
/*  14 */   public static enum PadSide { LEFT, 
/*  15 */     RIGHT;
/*     */     
/*     */     private PadSide() {} }
/*     */   
/*  19 */   public static enum CaseTransform { UPPER, 
/*  20 */     LOWER, 
/*  21 */     NONE;
/*     */     
/*     */     private CaseTransform() {} }
/*     */   
/*  25 */   public static enum MathOperation { ADD, 
/*  26 */     SUBTRACT, 
/*  27 */     MULTIPLY, 
/*  28 */     DIVIDE;
/*     */     
/*     */     private MathOperation() {} }
/*     */   
/*  32 */   boolean padVariable = false;
/*  33 */   PadType padType = null;
/*  34 */   PadSide padSide = null;
/*  35 */   int padSize = 0;
/*  36 */   char padChar = '0';
/*     */   
/*     */ 
/*  39 */   CaseTransform caseTransform = CaseTransform.NONE;
/*     */   
/*     */   MathOperation mathOperation;
/*     */   
/*     */   String mathValue;
/*     */   String variableName;
/*     */   String variableValue;
/*     */   
/*     */   public SetVariable(String variableName, String variableValue)
/*     */   {
/*  49 */     this.variableName = variableName;
/*  50 */     this.variableValue = variableValue;
/*     */   }
/*     */   
/*     */   public SetVariable(String variableName, String variableValue, MathOperation mathOperation, String mathValue) {
/*  54 */     this.variableName = variableName;
/*  55 */     this.variableValue = variableValue;
/*  56 */     this.mathOperation = mathOperation;
/*  57 */     this.mathValue = mathValue;
/*     */   }
/*     */   
/*     */   public SetVariable(String variableName, String variableValue, PadType padType, PadSide padSide, int padSize) {
/*  61 */     this.variableName = variableName;
/*  62 */     this.variableValue = variableValue;
/*  63 */     this.padType = padType;
/*  64 */     this.padSide = padSide;
/*  65 */     this.padSize = padSize;
/*  66 */     this.padVariable = true;
/*     */   }
/*     */   
/*     */   public SetVariable(String variableName, String variableValue, PadType padType, PadSide padSide, int padSize, char padChar) {
/*  70 */     this.variableName = variableName;
/*  71 */     this.variableValue = variableValue;
/*  72 */     this.padType = padType;
/*  73 */     this.padSide = padSide;
/*  74 */     this.padSize = padSize;
/*  75 */     this.padChar = padChar;
/*  76 */     this.padVariable = true;
/*     */   }
/*     */   
/*     */   public void setCaseTransform(CaseTransform caseTransform)
/*     */   {
/*  81 */     if (caseTransform == null) {
/*  82 */       caseTransform = CaseTransform.NONE;
/*     */     }
/*     */     
/*  85 */     this.caseTransform = caseTransform;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean run()
/*     */   {
/*  91 */     this.variableValue = this.variables.replace(this.variableValue);
/*     */     
/*  93 */     if (this.padVariable) {
/*  94 */       String before = this.variableValue;
/*  95 */       this.handler.logOutput("Padding value enabled with character: " + this.padChar, false);
/*     */       
/*  97 */       if (this.padType == PadType.MAXSTRINGSIZE) {
/*  98 */         padMaxSize();
/*  99 */       } else if (this.padType == PadType.MAXSTRINGSIZETRIM) {
/* 100 */         padMaxSizeTrim();
/* 101 */       } else if (this.padType == PadType.PADNUMTIMES) {
/* 102 */         padNumTimes();
/*     */       }
/* 104 */       this.handler.logOutput("Value padded from '" + before + "' to '" + this.variableValue + "'", false);
/*     */     }
/*     */     
/*     */ 
/* 108 */     if ((this.caseTransform != null) && (this.caseTransform.equals(CaseTransform.UPPER))) {
/* 109 */       String before = this.variableValue;
/* 110 */       this.variableValue = this.variableValue.toUpperCase();
/* 111 */       this.handler.logOutput("String transformed from '" + before + "' to '" + this.variableValue + "'", false);
/* 112 */     } else if ((this.caseTransform != null) && (this.caseTransform.equals(CaseTransform.LOWER))) {
/* 113 */       String before = this.variableValue;
/* 114 */       this.variableValue = this.variableValue.toLowerCase();
/* 115 */       this.handler.logOutput("String transformed from '" + before + "' to '" + this.variableValue + "'", false);
/*     */     }
/*     */     
/*     */ 
/* 119 */     if (this.mathOperation != null) {
/* 120 */       this.mathValue = this.variables.replace(this.mathValue);
/*     */       int mathValueInt;
/*     */       int variableValueInt;
/*     */       try {
/* 124 */         mathValueInt = Integer.parseInt(this.mathValue);
/* 125 */         variableValueInt = Integer.parseInt(this.variableValue);
/*     */       } catch (Exception ex) {
/* 127 */         this.handler.logOutput("One of these values failed to be converted to Integer: Value: " + this.variableValue + " - mathValue: " + this.mathValue, true);
/* 128 */         return false;
/*     */       }
/*     */       
/*     */ 
/* 132 */       if (this.mathOperation == MathOperation.ADD) {
/* 133 */         this.variableValue = Integer.toString(variableValueInt + mathValueInt);
/*     */       }
/*     */       
/* 136 */       if (this.mathOperation == MathOperation.SUBTRACT) {
/* 137 */         this.variableValue = Integer.toString(variableValueInt - mathValueInt);
/*     */       }
/*     */       
/* 140 */       if (this.mathOperation == MathOperation.MULTIPLY) {
/* 141 */         this.variableValue = Integer.toString(variableValueInt * mathValueInt);
/*     */       }
/*     */       try
/*     */       {
/* 145 */         if (this.mathOperation == MathOperation.DIVIDE) {
/* 146 */           this.variableValue = Integer.toString(variableValueInt / mathValueInt);
/*     */         }
/*     */       } catch (Exception ex) {
/* 149 */         this.handler.logOutput("Cannot divide by ZERO!", true);
/* 150 */         return false;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 155 */     this.handler.logOutput("Setting '" + this.variableName + "' to '" + this.variableValue + "'", false);
/* 156 */     this.variables.set(this.variableName, this.variableValue);
/* 157 */     return true;
/*     */   }
/*     */   
/*     */   protected void padNumTimes() {
/* 161 */     for (int i = 0; i < this.padSize; i++) {
/* 162 */       padOne();
/*     */     }
/*     */   }
/*     */   
/*     */   protected void padMaxSizeTrim() {
/* 167 */     if (this.variableValue.length() < this.padSize) {
/* 168 */       padMaxSize();
/* 169 */     } else if ((this.variableValue.length() > this.padSize) && (this.padSide == PadSide.LEFT)) {
/* 170 */       int startPos = this.variableValue.length() - this.padSize;
/* 171 */       this.variableValue = this.variableValue.substring(startPos, this.variableValue.length());
/* 172 */     } else if ((this.variableValue.length() > this.padSize) && (this.padSide == PadSide.RIGHT)) {
/* 173 */       this.variableValue = this.variableValue.substring(0, this.padSize);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void padMaxSize()
/*     */   {
/* 179 */     if (this.variableValue.length() < this.padSize) {
/* 180 */       int sizeDiff = this.padSize - this.variableValue.length();
/* 181 */       for (int i = 0; i < sizeDiff; i++) {
/* 182 */         padOne();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void padOne() {
/* 188 */     if (this.padSide == PadSide.LEFT)
/* 189 */       leftPad();
/* 190 */     if (this.padSide == PadSide.RIGHT)
/* 191 */       rightPad();
/*     */   }
/*     */   
/*     */   protected void leftPad() {
/* 195 */     this.variableValue = (this.padChar + this.variableValue);
/*     */   }
/*     */   
/*     */   protected void rightPad() {
/* 199 */     this.variableValue += this.padChar;
/*     */   }
/*     */   
/*     */   public String getProcessableName()
/*     */   {
/* 204 */     return "Set Variable";
/*     */   }
/*     */ }


/* Location:              D:\stage-installer-01-30-2023\postghost\installer\JCG-Xstore17-InstallerCopy.jar!\com\izforge\izpack\panels\xstprocess\processables\SetVariable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */