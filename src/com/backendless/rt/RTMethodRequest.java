package com.backendless.rt;

public class RTMethodRequest extends AbstractRequest
{
  private final MethodTypes methodType;

  public RTMethodRequest( MethodTypes methodType, RTCallback callback )
  {
    super( callback );
    this.methodType = methodType;
  }

  @Override
  public String getName()
  {
    return methodType.name();
  }

  public MethodTypes getMethodType()
  {
    return methodType;
  }

}
