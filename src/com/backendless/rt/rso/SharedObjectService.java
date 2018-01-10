package com.backendless.rt.rso;

public class SharedObjectService
{
  private final SharedObjectFactory sharedObjectFactory = new SharedObjectFactory();

  public SharedObject connect( String name )
  {
    final SharedObject sharedObject = sharedObjectFactory.get( name );
    sharedObject.connect();
    return sharedObject;
  }
}
