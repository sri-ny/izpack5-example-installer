package com.izforge.izpack.panels.xstprocess.processables;

import com.izforge.izpack.api.data.Variables;
import com.izforge.izpack.panels.xstprocess.Processable;

public class Echo extends Processable
{
  String echoMessage = "";
  
  public Echo(String echoMessage) {
    this.echoMessage = echoMessage;
  }
  

  public boolean run()
  {
    this.echoMessage = this.variables.replace(this.echoMessage);
    this.handler.logOutput(this.echoMessage, false);
    
    return true;
  }
  
  public String getProcessableName()
  {
    return "Echo Message";
  }
}

