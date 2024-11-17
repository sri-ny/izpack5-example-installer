/*     */ package com.izforge.izpack.panels.xstprocess.specprocessors;
/*     */ 
/*     */ import com.izforge.izpack.api.adaptator.IXMLElement;
/*     */ import com.izforge.izpack.panels.xstprocess.Processable;
/*     */ import com.izforge.izpack.panels.xstprocess.processables.SetVariable;
/*     */ import com.izforge.izpack.panels.xstprocess.processables.SetVariable.CaseTransform;
/*     */ import com.izforge.izpack.panels.xstprocess.processables.SetVariable.PadSide;
/*     */ import com.izforge.izpack.panels.xstprocess.processables.SetVariable.PadType;
/*     */ 
/*     */ public class SetVariableSpecProcessor extends SpecProcessor
/*     */ {
/*     */   public boolean isApplicable(IXMLElement element)
/*     */   {
/*  14 */     return element.getName().equals("setvariable");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Processable processElement(IXMLElement element)
/*     */   {
/*  21 */     String variableName = element.getAttribute("variable");
/*  22 */     if ((variableName == null) || (variableName.equals(""))) {
/*  23 */       return null;
/*     */     }
/*     */     
/*  26 */     String variableValue = element.getAttribute("value");
/*  27 */     if (variableValue == null) {
/*  28 */       return null;
/*     */     }
/*     */     
/*  31 */     SetVariable setVariableProcessable = null;
/*     */     
/*     */ 
/*  34 */     String padType = element.getAttribute("paddingtype");
/*  35 */     String mathOperation = element.getAttribute("mathoperation");
/*  36 */     String mathValue = element.getAttribute("mathvalue");
/*  37 */     if (padType != null)
/*     */     {
/*     */ 
/*  40 */       boolean padTypeValid = (padType.equalsIgnoreCase(SetVariable.PadType.MAXSTRINGSIZE.toString())) || (padType.equalsIgnoreCase(SetVariable.PadType.MAXSTRINGSIZETRIM.toString())) || (padType.equalsIgnoreCase(SetVariable.PadType.PADNUMTIMES.toString()));
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  46 */       if (!padTypeValid) {
/*  47 */         return null;
/*     */       }
/*     */       
/*     */ 
/*  51 */       String padSide = element.getAttribute("paddingside");
/*  52 */       boolean padSideValid = (padSide.equalsIgnoreCase(SetVariable.PadSide.LEFT.toString())) || (padSide.equalsIgnoreCase(SetVariable.PadSide.RIGHT.toString()));
/*     */       
/*     */ 
/*     */ 
/*  56 */       if (!padSideValid) {
/*  57 */         return null;
/*     */       }
/*     */       
/*     */ 
/*  61 */       String padSize = element.getAttribute("paddingsize");
/*     */       Integer padSizeInt;
/*     */       try {
/*  64 */         padSizeInt = Integer.valueOf(Integer.parseInt(padSize));
/*     */       } catch (NumberFormatException ex) {
/*  66 */         return null;
/*     */       }
/*     */       
/*     */ 
/*  70 */       String padChar = element.getAttribute("paddingchar");
/*  71 */       if (padChar != null)
/*     */       {
/*  73 */         if (padChar.length() != 1) {
/*  74 */           return null;
/*     */         }
/*     */         
/*     */ 
/*  78 */         setVariableProcessable = new SetVariable(variableName, variableValue, SetVariable.PadType.valueOf(padType), SetVariable.PadSide.valueOf(padSide), padSizeInt.intValue(), padChar.toCharArray()[0]);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  89 */       setVariableProcessable = new SetVariable(variableName, variableValue, SetVariable.PadType.valueOf(padType), SetVariable.PadSide.valueOf(padSide), padSizeInt.intValue());
/*     */ 
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/*     */ 
/*     */ 
/*  97 */       if (mathOperation != null)
/*     */       {
/*  99 */         setVariableProcessable = new SetVariable(variableName, variableValue, com.izforge.izpack.panels.xstprocess.processables.SetVariable.MathOperation.valueOf(mathOperation), mathValue);
/* 100 */         return setVariableProcessable;
/*     */       }
/*     */       
/* 103 */       setVariableProcessable = new SetVariable(variableName, variableValue);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 108 */     String caseTransform = element.getAttribute("casetransform");
/*     */     
/* 110 */     if (caseTransform == null) {
/* 111 */       setVariableProcessable.setCaseTransform(SetVariable.CaseTransform.NONE);
/*     */     }
/* 113 */     else if (caseTransform.equalsIgnoreCase(SetVariable.CaseTransform.UPPER.toString())) {
/* 114 */       setVariableProcessable.setCaseTransform(SetVariable.CaseTransform.UPPER);
/*     */     }
/* 116 */     else if (caseTransform.equalsIgnoreCase(SetVariable.CaseTransform.LOWER.toString())) {
/* 117 */       setVariableProcessable.setCaseTransform(SetVariable.CaseTransform.LOWER);
/*     */     }
/*     */     else {
/* 120 */       setVariableProcessable.setCaseTransform(SetVariable.CaseTransform.NONE);
/*     */     }
/* 122 */     return setVariableProcessable;
/*     */   }
/*     */ }


/* Location:              D:\stage-installer-01-30-2023\postghost\installer\JCG-Xstore17-InstallerCopy.jar!\com\izforge\izpack\panels\xstprocess\specprocessors\SetVariableSpecProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */