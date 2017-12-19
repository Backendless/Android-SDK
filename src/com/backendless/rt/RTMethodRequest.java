package com.backendless.rt;

import com.backendless.async.callback.AsyncCallback;

class RTMethodRequest<T>
{
  private String id;
  private final MethodTypes methodType;
  private AsyncCallback<T> callback;
  private Object[] args;

  RTMethodRequest( MethodTypes methodType )
  {
    this.methodType = methodType;
  }

  public String getId()
  {
    return id;
  }

  public RTMethodRequest setId( String id )
  {
    this.id = id;
    return this;
  }

  public AsyncCallback<T> getCallback()
  {
    return callback;
  }

  public RTMethodRequest setCallback( AsyncCallback<T> callback )
  {
    this.callback = callback;
    return this;
  }

  public Object[] getArgs()
  {
    return args;
  }

  public RTMethodRequest setArgs( Object[] args )
  {
    this.args = args;
    return this;
  }

  public MethodTypes getMethodType()
  {
    return methodType;
  }
}
