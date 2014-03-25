package com.backendless.servercode;

import com.backendless.commons.exception.ExceptionRepresentation;

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
  private ExceptionRepresentation exception;

  public ExecutionResult()
  {
  }

  public ExecutionResult( T res, Exception exception )
  {
    this.result = res;
    if( exception != null )
      this.exception = new ExceptionRepresentation( exception );
  }

  public ExceptionRepresentation getException()
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

  public void setException( ExceptionRepresentation exception )
  {
    this.exception = exception;
  }

}
