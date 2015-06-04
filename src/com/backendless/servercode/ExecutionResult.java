package com.backendless.servercode;

import com.backendless.commons.exception.ExceptionWrapper;

/**
 * Created with IntelliJ IDEA.
 * User: ivanlappo
 * Date: 10/3/13
 * Time: 6:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExecutionResult<T>
{
  private T result;
  private ExceptionWrapper exception;

  public ExecutionResult()
  {
  }

  public ExecutionResult( T result )
  {
    this.result = result;
  }

  public ExecutionResult( T res, Exception exception )
  {
    this.result = res;
    if( exception != null )
      this.exception = new ExceptionWrapper( exception );
  }

  public ExecutionResult( T result, ExceptionWrapper exception )
  {
    this.result = result;
    this.exception = exception;
  }

  public ExceptionWrapper getException()
  {
    return exception;
  }

  public T getResult()
  {
    return result;
  }

  public void setResult( T result )
  {
    this.result = result;
  }

  public void setException( ExceptionWrapper exception )
  {
    this.exception = exception;
  }

  @Override
  public String toString()
  {
    return "ExecutionResult{" +
            "exception=" + exception +
            ", result=" + result +
            '}';
  }
}
