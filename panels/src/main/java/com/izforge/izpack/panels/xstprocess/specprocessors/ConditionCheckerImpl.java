/*     */ package com.izforge.izpack.panels.xstprocess.specprocessors;
/*     */ 
/*     */ import com.izforge.izpack.api.adaptator.IXMLElement;
/*     */ import com.izforge.izpack.api.data.InstallData;
/*     */ import com.izforge.izpack.api.data.Pack;
/*     */ import com.izforge.izpack.api.data.binding.OsModel;
/*     */ import com.izforge.izpack.api.rules.Condition;
/*     */ import com.izforge.izpack.api.rules.RulesEngine;
/*     */ import com.izforge.izpack.util.PlatformModelMatcher;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ public class ConditionCheckerImpl implements ConditionChecker
/*     */ {
/*     */   InstallData installData;
/*     */   PlatformModelMatcher platformModelMatcher;
/*     */   RulesEngine rules;
/*     */   
/*     */   public ConditionCheckerImpl(InstallData installData, RulesEngine rules, PlatformModelMatcher platformModelMatcher)
/*     */   {
/*  21 */     this.installData = installData;
/*  22 */     this.rules = rules;
/*  23 */     this.platformModelMatcher = platformModelMatcher;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean conditionsMet(IXMLElement jobElement)
/*     */   {
/*  29 */     String conditionId = null;
/*     */     
/*     */ 
/*  32 */     if (jobElement.hasAttribute("condition")) {
/*  33 */       conditionId = jobElement.getAttribute("condition");
/*  34 */     } else if (jobElement.hasAttribute("conditionid")) {
/*  35 */       conditionId = jobElement.getAttribute("conditionid");
/*     */     }
/*     */     
/*     */ 
/*  39 */     if ((conditionId == null) || (conditionId.isEmpty())) {
/*  40 */       return true;
/*     */     }
/*     */     
/*     */ 
/*  44 */     return definedConditionsMet(conditionId);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean conditionsMet(String conditionId)
/*     */   {
/*  50 */     if ((conditionId == null) || (conditionId.isEmpty())) {
/*  51 */       return true;
/*     */     }
/*     */     
/*  54 */     return definedConditionsMet(conditionId);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean conditionsMet(List<String> conditionIds)
/*     */   {
/*  60 */     if (conditionIds.isEmpty()) {
/*  61 */       return true;
/*     */     }
/*     */     
/*  64 */     for (String conditionId : conditionIds) {
/*  65 */       if (!conditionsMet(conditionId)) {
/*  66 */         return false;
/*     */       }
/*     */     }
/*  69 */     return true;
/*     */   }
/*     */   
/*     */   public boolean checkOS(IXMLElement element)
/*     */   {
/*  74 */     List<OsModel> constraints = com.izforge.izpack.util.OsConstraintHelper.getOsList(element);
/*  75 */     return this.platformModelMatcher.matchesCurrentPlatform(constraints);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean checkPacks(IXMLElement element)
/*     */   {
/*  81 */     List<IXMLElement> conditionPacks = element.getChildrenNamed("executeForPack");
/*     */     
/*     */ 
/*  84 */     if (conditionPacks.size() == 0) {
/*  85 */       return true;
/*     */     }
/*     */     
/*     */ 
/*  89 */     for (Iterator i$ = this.installData.getSelectedPacks().iterator(); i$.hasNext();) { pack = (Pack)i$.next();
/*  90 */       for (IXMLElement conditionPack : conditionPacks) {
/*  91 */         if (conditionPack.getAttribute("name", "").equals(pack.getName())) {
/*  92 */           return true;
/*     */         }
/*     */       }
/*     */     }
/*     */     Pack pack;
/*  97 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   boolean definedConditionsMet(String conditionID)
/*     */   {
/* 103 */     Condition cond = this.rules.getCondition(conditionID);
/*     */     
/* 105 */     if ((cond != null) && (!cond.isTrue()))
/*     */     {
/* 107 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 111 */     return true;
/*     */   }
/*     */ }


/* Location:              D:\stage-installer-01-30-2023\postghost\installer\JCG-Xstore17-InstallerCopy.jar!\com\izforge\izpack\panels\xstprocess\specprocessors\ConditionCheckerImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */