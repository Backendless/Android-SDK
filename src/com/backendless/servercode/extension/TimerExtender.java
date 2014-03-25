package com.backendless.servercode.extension;

public abstract class TimerExtender
{
  protected TimerExtender()
  {
  }

  public abstract void execute( String appVersionId ) throws Exception;
}
