package com.backendless.rt;

public class ReconnectAttempt
{
  private final int timeout;
  private final int attempt;

  public ReconnectAttempt( int attempt, int timeout )
  {
    this.timeout = timeout;
    this.attempt = attempt;
  }

  public int getTimeout()
  {
    return timeout;
  }

  public int getAttempt()
  {
    return attempt;
  }

  @Override
  public String toString()
  {
    return "ReconnectAttempt{" + "timeout=" + timeout + ", attempt=" + attempt + '}';
  }
}
