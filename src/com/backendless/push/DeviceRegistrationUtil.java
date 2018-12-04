package com.backendless.push;

import com.backendless.DeviceRegistration;
import com.backendless.Invoker;
import com.backendless.Messaging;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.exceptions.ExceptionMessage;

import java.util.Date;
import java.util.List;


public class DeviceRegistrationUtil
{
  private final static DeviceRegistrationUtil instance = new DeviceRegistrationUtil();

  private DeviceRegistrationUtil()
  {
  }

  public static DeviceRegistrationUtil getInstance()
  {
    return instance;
  }

  public String registerDeviceOnServer( String deviceToken, final List<String> channels, final long expiration )
  {
    if( deviceToken == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_DEVICE_TOKEN );

    DeviceRegistration deviceRegistration = new DeviceRegistration();
    deviceRegistration.setDeviceId( Messaging.getDeviceId() );
    deviceRegistration.setOs( Messaging.OS );
    deviceRegistration.setOsVersion( Messaging.OS_VERSION );
    deviceRegistration.setDeviceToken( deviceToken );
    deviceRegistration.setChannels( channels );
    if( expiration != 0 )
      deviceRegistration.setExpiration( new Date( expiration ) );

    return Invoker.invokeSync( Messaging.DEVICE_REGISTRATION_MANAGER_SERVER_ALIAS, "registerDevice", new Object[] { deviceRegistration } );
  }

  public void registerDeviceOnServer( String deviceToken, final List<String> channels, final long expiration, final AsyncCallback<String> responder )
  {
    try
    {
      if( deviceToken == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_DEVICE_TOKEN );

      DeviceRegistration deviceRegistration = new DeviceRegistration();
      deviceRegistration.setDeviceId( Messaging.getDeviceId() );
      deviceRegistration.setOs( Messaging.OS );
      deviceRegistration.setOsVersion( Messaging.OS_VERSION );
      deviceRegistration.setDeviceToken( deviceToken );
      deviceRegistration.setChannels( channels );
      if( expiration != 0 )
        deviceRegistration.setExpiration( new Date( expiration ) );

      Invoker.invokeAsync( Messaging.DEVICE_REGISTRATION_MANAGER_SERVER_ALIAS, "registerDevice", new Object[] { deviceRegistration }, new AsyncCallback<String>()
      {
        @Override
        public void handleResponse( String response )
        {
          if( responder != null )
            responder.handleResponse( response );
        }

        @Override
        public void handleFault( BackendlessFault fault )
        {
          if( responder != null )
            responder.handleFault( fault );
        }
      } );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  public boolean unregisterDeviceOnServer()
  {
    return (Boolean) Invoker.invokeSync( Messaging.DEVICE_REGISTRATION_MANAGER_SERVER_ALIAS, "unregisterDevice", new Object[] { Messaging.getDeviceId() } );
  }

  public void unregisterDeviceOnServer( final AsyncCallback<Boolean> responder )
  {
    Invoker.invokeAsync( Messaging.DEVICE_REGISTRATION_MANAGER_SERVER_ALIAS, "unregisterDevice", new Object[] { Messaging.getDeviceId() }, responder );
  }

  public int unregisterDeviceOnServer( List<String> channels )
  {
    return (int) Invoker.invokeSync( Messaging.DEVICE_REGISTRATION_MANAGER_SERVER_ALIAS, "unregisterDevice", new Object[] { Messaging.getDeviceId(), channels } );
  }

  public void unregisterDeviceOnServer( List<String> channels, final AsyncCallback<Integer> responder )
  {
    Invoker.invokeAsync( Messaging.DEVICE_REGISTRATION_MANAGER_SERVER_ALIAS, "unregisterDevice", new Object[] { Messaging.getDeviceId(), channels }, responder );
  }
}
