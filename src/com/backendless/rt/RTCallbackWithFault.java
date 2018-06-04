package com.backendless.rt;

import com.backendless.exceptions.BackendlessFault;

import java.util.logging.Logger;

public abstract class RTCallbackWithFault implements RTCallback
{
  private static final Logger logger = Logger.getLogger( "RTCallbackWithFault" );

  @Override
  public void handleFault( BackendlessFault fault )
  {
    logger.warning( "got fault " + fault );

    if( usersCallback() != null )
      usersCallback().handleFault( fault );
  }
}
