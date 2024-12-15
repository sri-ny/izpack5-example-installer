package com.izforge.izpack.panels.xstprocess;

import com.izforge.izpack.api.handler.AbstractUIHandler;

public abstract interface AbstractUIProcessHandler
  extends AbstractUIHandler
{
  public abstract void logOutput(String paramString, boolean paramBoolean);
  
  public abstract void startProcessing(int paramInt);
  
  public abstract void startProcess(String paramString);
  
  public abstract void finishProcess();
  
  public abstract void finishProcessing(boolean paramBoolean1, boolean paramBoolean2);
  
  public abstract void skipProcess(String paramString1, String paramString2);
}

