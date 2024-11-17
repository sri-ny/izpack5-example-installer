package com.izforge.izpack.panels.xstprocess.processables;

import com.izforge.izpack.panels.xstprocess.Processable;

public class SleepOperation extends Processable
{
  int sleepMs;
  
  public SleepOperation(int sleepMs) {
    this.sleepMs = sleepMs;
  }
  
  public boolean run()
  {
    try {
      Thread.sleep(this.sleepMs);
    }
    catch (Exception ex) {}
    

    return true;
  }
  
  public String getProcessableName()
  {
    return "Sleep operation: " + this.sleepMs + "ms";
  }
}

