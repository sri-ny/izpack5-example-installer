package com.izforge.izpack.panels.xstprocess.specprocessors;

import com.izforge.izpack.api.adaptator.IXMLElement;
import com.izforge.izpack.api.data.InstallData;
import com.izforge.izpack.api.data.Pack;
import com.izforge.izpack.api.data.binding.OsModel;
import com.izforge.izpack.api.rules.Condition;
import com.izforge.izpack.api.rules.RulesEngine;
import com.izforge.izpack.util.PlatformModelMatcher;
import java.util.Iterator;
import java.util.List;

public class ConditionCheckerImpl implements ConditionChecker
{
  InstallData installData;
  PlatformModelMatcher platformModelMatcher;
  RulesEngine rules;
  
  public ConditionCheckerImpl(InstallData installData, RulesEngine rules, PlatformModelMatcher platformModelMatcher)
  {
    this.installData = installData;
    this.rules = rules;
    this.platformModelMatcher = platformModelMatcher;
  }
  

  public boolean conditionsMet(IXMLElement jobElement)
  {
    String conditionId = null;
    

    if (jobElement.hasAttribute("condition")) {
      conditionId = jobElement.getAttribute("condition");
    } else if (jobElement.hasAttribute("conditionid")) {
      conditionId = jobElement.getAttribute("conditionid");
    }
    

    if ((conditionId == null) || (conditionId.isEmpty())) {
      return true;
    }
    

    return definedConditionsMet(conditionId);
  }
  

  public boolean conditionsMet(String conditionId)
  {
    if ((conditionId == null) || (conditionId.isEmpty())) {
      return true;
    }
    
    return definedConditionsMet(conditionId);
  }
  

  public boolean conditionsMet(List<String> conditionIds)
  {
    if (conditionIds.isEmpty()) {
      return true;
    }
    
    for (String conditionId : conditionIds) {
      if (!conditionsMet(conditionId)) {
        return false;
      }
    }
    return true;
  }
  
  public boolean checkOS(IXMLElement element)
  {
    List<OsModel> constraints = com.izforge.izpack.util.OsConstraintHelper.getOsList(element);
    return this.platformModelMatcher.matchesCurrentPlatform(constraints);
  }
  

  public boolean checkPacks(IXMLElement element)
  {
    List<IXMLElement> conditionPacks = element.getChildrenNamed("executeForPack");
    

    if (conditionPacks.size() == 0) {
      return true;
    }
    

    for (Iterator i$ = this.installData.getSelectedPacks().iterator(); i$.hasNext();) { pack = (Pack)i$.next();
      for (IXMLElement conditionPack : conditionPacks) {
        if (conditionPack.getAttribute("name", "").equals(pack.getName())) {
          return true;
        }
      }
    }
    Pack pack;
    return false;
  }
  

  boolean definedConditionsMet(String conditionID)
  {
    Condition cond = this.rules.getCondition(conditionID);
    
    if ((cond != null) && (!cond.isTrue()))
    {
      return false;
    }
    

    return true;
  }
}

