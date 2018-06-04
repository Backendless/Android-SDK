package com.backendless.rt;

public class ReconnectAttempt
{
  private final int timeout;
  private final int attempt;
  private String error;

  public ReconnectAttempt( int timeout, int attempt, String error )
  {
    this( timeout, attempt );
    this.error = error;
  }

  public ReconnectAttempt( int timeout, int attempt )
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

  String getError()
  {
    return error;
  }

  @Override
  public String toString()
  {
    return "ReconnectAttempt{" + "timeout=" + timeout + ", attempt=" + attempt + ", error='" + error + '\'' + '}';
  }
}
