/*
 * ********************************************************************************************************************
 *  <p/>
 *  BACKENDLESS.COM CONFIDENTIAL
 *  <p/>
 *  ********************************************************************************************************************
 *  <p/>
 *  Copyright 2012 BACKENDLESS.COM. All Rights Reserved.
 *  <p/>
 *  NOTICE: All information contained herein is, and remains the property of Backendless.com and its suppliers,
 *  if any. The intellectual and technical concepts contained herein are proprietary to Backendless.com and its
 *  suppliers and may be covered by U.S. and Foreign Patents, patents in process, and are protected by trade secret
 *  or copyright law. Dissemination of this information or reproduction of this material is strictly forbidden
 *  unless prior written permission is obtained from Backendless.com.
 *  <p/>
 *  ********************************************************************************************************************
 */

package com.backendless;/*
 * ********************************************************************************************************************
 *  <p/>
 *  BACKENDLESS.COM CONFIDENTIAL
 *  <p/>
 *  ********************************************************************************************************************
 *  <p/>
 *  Copyright 2012 BACKENDLESS.COM. All Rights Reserved.
 *  <p/>
 *  NOTICE: All information contained herein is, and remains the property of Backendless.com and its suppliers,
 *  if any. The intellectual and technical concepts contained herein are proprietary to Backendless.com and its
 *  suppliers and may be covered by U.S. and Foreign Patents, patents in process, and are protected by trade secret
 *  or copyright law. Dissemination of this information or reproduction of this material is strictly forbidden
 *  unless prior written permission is obtained from Backendless.com.
 *  <p/>
 *  ********************************************************************************************************************
 */

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessException;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.exceptions.ExceptionMessage;
import com.backendless.messaging.BodyParts;
import com.backendless.messaging.DeliveryOptions;
import com.backendless.messaging.Message;
import com.backendless.messaging.MessageStatus;
import com.backendless.messaging.PublishOptions;
import com.backendless.messaging.PublishStatusEnum;
import com.backendless.messaging.PushBroadcastMask;
import com.backendless.push.BackendlessPushService;
import com.backendless.push.FCMRegistration;
import com.backendless.push.GCMRegistrar;
import com.backendless.rt.messaging.Channel;
import com.backendless.rt.messaging.ChannelFactory;
import weborb.types.Types;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public final class Messaging
{
  private final static String MESSAGING_MANAGER_SERVER_ALIAS = "com.backendless.services.messaging.MessagingService";
  private final static String DEVICE_REGISTRATION_MANAGER_SERVER_ALIAS = "com.backendless.services.messaging.DeviceRegistrationService";
  private final static String EMAIL_MANAGER_SERVER_ALIAS = "com.backendless.services.mail.CustomersEmailService";
  private final static String DEFAULT_CHANNEL_NAME = "default";
  private final static String OS;
  private final static String OS_VERSION;
  private static final Messaging instance = new Messaging();
  private static final ChannelFactory chanelFactory = new ChannelFactory();

  private Messaging()
  {
    Types.addClientClassMapping( "com.backendless.management.DeviceRegistrationDto", DeviceRegistration.class );
    Types.addClientClassMapping( "com.backendless.services.messaging.PublishOptions", PublishOptions.class );
    Types.addClientClassMapping( "com.backendless.services.messaging.DeliveryOptions", DeliveryOptions.class );
    Types.addClientClassMapping( "com.backendless.services.messaging.Message", Message.class );
  }

  static
  {
    if( Backendless.isAndroid() )
    {
      OS_VERSION = String.valueOf( Build.VERSION.SDK_INT );
      OS = "ANDROID";
    }
    else
    {
      OS_VERSION = System.getProperty( "os.version" );
      OS = System.getProperty( "os.name" );
    }
  }

  static class DeviceIdHolder
  {
    static String id;

    static void init( Context context )
    {
      if( android.os.Build.VERSION.SDK_INT < 27 )
        id = Build.SERIAL;
      else
        id = Settings.Secure.getString( context.getContentResolver(), Settings.Secure.ANDROID_ID );
    }

    static void init()
    {
      try
      {
        id = UUID.randomUUID().toString();
      }
      catch( Exception e )
      {
        StringBuilder builder = new StringBuilder();
        builder.append( System.getProperty( "os.name" ) );
        builder.append( System.getProperty( "os.arch" ) );
        builder.append( System.getProperty( "os.version" ) );
        builder.append( System.getProperty( "user.name" ) );
        builder.append( System.getProperty( "java.home" ) );
        id = UUID.nameUUIDFromBytes( builder.toString().getBytes() ).toString();
      }
    }
  }

  public static String getDeviceId()
  {
    return DeviceIdHolder.id;
  }


  static Messaging getInstance()
  {
    return instance;
  }

  /**
   * <b>GCM support will be removed after April 2019.</b>
   */
  @Deprecated
  public void registerDevice( String GCMSenderID )
  {
    registerDevice( GCMSenderID, (String) null );
  }

  /**
   * <b>GCM support will be removed after April 2019.</b>
   */
  @Deprecated
  public void registerDevice( String GCMSenderID, String channel )
  {
    registerDevice( GCMSenderID, channel, null );
  }

  /**
   * <b>GCM support will be removed after April 2019.</b>
   */
  @Deprecated
  public void registerDevice( String GCMSenderID, AsyncCallback<Void> callback )
  {
    registerDevice( GCMSenderID, (String) null, callback );
  }

  /**
   * <b>GCM support will be removed after April 2019.</b>
   */
  @Deprecated
  public void registerDevice( String GCMSenderID, String channel, AsyncCallback<Void> callback )
  {
    registerDevice( GCMSenderID, Arrays.asList( channel ), null, callback );
  }

  /**
   * <b>GCM support will be removed after April 2019.</b>
   */
  @Deprecated
  public void registerDevice( String GCMSenderID, List<String> channels, Date expiration )
  {
    registerDevice( GCMSenderID, channels, expiration, null );
  }

  /**
   * <b>GCM support will be removed after April 2019.</b>
   */
  @Deprecated
  public void registerDevice( final String GCMSenderID, final List<String> channels, final Date expiration, final AsyncCallback<Void> callback )
  {
    new AsyncTask<Void, Void, RuntimeException>()
    {
      @Override
      protected RuntimeException doInBackground( Void... params )
      {
        try
        {
          registerDeviceGCMSync( ContextHandler.getAppContext(), GCMSenderID, channels, expiration );
          return null;
        }
        catch( RuntimeException t )
        {
          return t;
        }
      }

      @Override
      protected void onPostExecute( RuntimeException result )
      {
        if( result != null )
        {
          if( callback == null )
            throw result;

          callback.handleFault( new BackendlessFault( result ) );
        }
        else
        {
          if( callback != null )
            callback.handleResponse( null );
        }
      }
    }.execute();
  }

  /**
   * <b>GCM support will be removed after April 2019.</b>
   */
  @Deprecated
  private synchronized void registerDeviceGCMSync( Context context, String GCMSenderID, List<String> channels,
                                                   Date expiration )
  {
    if (BackendlessPushService.isFCM( ContextHandler.getAppContext() ) )
      throw new IllegalStateException( "The method is intended only for old GCM messaging, which support will be stopped after April 2019." );

    if( channels == null || channels.isEmpty() ||
            (channels.size() == 1 && (channels.get( 0 ) == null || channels.get( 0 ).isEmpty())) )
    {
      channels = Collections.singletonList( DEFAULT_CHANNEL_NAME );
    }

    for( String channel : channels )
      checkChannelName( channel );

    if( expiration != null && expiration.before( Calendar.getInstance().getTime() ) )
      throw new IllegalArgumentException( ExceptionMessage.WRONG_EXPIRATION_DATE );

    GCMRegistrar.checkDevice( context );
    GCMRegistrar.checkManifest( context );
    GCMRegistrar.register( context, GCMSenderID, channels, expiration );
  }

  /**
   * For FireBase messaging only.
   */
  public void registerDevice()
  {
    registerDevice( (AsyncCallback<String>) null );
  }

  /**
   * For FireBase messaging only.
   */
  public void registerDevice( AsyncCallback<String> callback )
  {
    registerDevice( Collections.singletonList( DEFAULT_CHANNEL_NAME ), callback );
  }

  /**
   * For FireBase messaging only.
   */
  public void registerDevice( List<String> channels )
  {
    registerDevice( Collections.singletonList( DEFAULT_CHANNEL_NAME ), (AsyncCallback<String>) null );
  }

  /**
   * For FireBase messaging only.
   */
  public void registerDevice( List<String> channels, AsyncCallback<String> callback )
  {
    registerDevice( channels, (Date) null, callback );
  }

  /**
   * For FireBase messaging only.
   */
  public void registerDevice( List<String> channels, Date expiration )
  {
    registerDevice( channels, expiration, (AsyncCallback<String>) null );
  }

  /**
   * For FireBase messaging only.
   */
  public void registerDevice( List<String> channels, Date expiration, AsyncCallback<String> callback )
  {
    if( !BackendlessPushService.isFCM( ContextHandler.getAppContext() ) )
      throw new IllegalStateException( "The method is intended only for FireBase messaging." );

    if( channels == null || channels.isEmpty() ||
            (channels.size() == 1 && (channels.get( 0 ) == null || channels.get( 0 ).isEmpty())) )
    {
      channels = Collections.singletonList( DEFAULT_CHANNEL_NAME );
    }

    for( String channel : channels )
      checkChannelName( channel );

    long expirationMs = 0;
    if( expiration != null)
    {
      if( expiration.before( Calendar.getInstance().getTime() ) )
        throw new IllegalArgumentException( ExceptionMessage.WRONG_EXPIRATION_DATE );
      else
        expirationMs = expiration.getTime();
    }
    FCMRegistration.registerDevice( ContextHandler.getAppContext(), channels, expirationMs, callback );
  }

  private void checkChannelName( String channelName )
  {
    if( channelName == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_CHANNEL_NAME );

    if( channelName.equals( "" ) )
      throw new IllegalArgumentException( ExceptionMessage.NULL_CHANNEL_NAME );
  }

  public String registerDeviceOnServer( String deviceToken, final List<String> channels, final long expiration )
  {
    if( deviceToken == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_DEVICE_TOKEN );

    DeviceRegistration deviceRegistration = new DeviceRegistration();
    deviceRegistration.setDeviceId( getDeviceId() );
    deviceRegistration.setOs( OS );
    deviceRegistration.setOsVersion( OS_VERSION );
    deviceRegistration.setDeviceToken( deviceToken );
    deviceRegistration.setChannels( channels );
    if( expiration != 0 )
      deviceRegistration.setExpiration( new Date( expiration ) );

    return Invoker.invokeSync( DEVICE_REGISTRATION_MANAGER_SERVER_ALIAS, "registerDevice", new Object[] { deviceRegistration } );
  }

  public void registerDeviceOnServer( String deviceToken, final List<String> channels, final long expiration,
                                      final AsyncCallback<String> responder )
  {
    try
    {
      if( deviceToken == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_DEVICE_TOKEN );

      DeviceRegistration deviceRegistration = new DeviceRegistration();
      deviceRegistration.setDeviceId( getDeviceId() );
      deviceRegistration.setOs( OS );
      deviceRegistration.setOsVersion( OS_VERSION );
      deviceRegistration.setDeviceToken( deviceToken );
      deviceRegistration.setChannels( channels );
      if( expiration != 0 )
        deviceRegistration.setExpiration( new Date( expiration ) );

      Invoker.invokeAsync( DEVICE_REGISTRATION_MANAGER_SERVER_ALIAS, "registerDevice", new Object[] { deviceRegistration }, new AsyncCallback<String>()
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

  public void unregisterDevice()
  {
    unregisterDevice( (List<String>) null );
  }

  public void unregisterDevice( List<String> channels )
  {
    unregisterDevice( channels, null );
  }

  public void unregisterDevice( AsyncCallback<Void> callback )
  {
    unregisterDevice( null, callback );
  }

  public void unregisterDevice( final List<String> channels, final AsyncCallback<Void> callback )
  {
    new AsyncTask<Void, Void, RuntimeException>()
    {
      @Override
      protected RuntimeException doInBackground( Void... params )
      {
        try
        {
          Context context = ContextHandler.getAppContext();

          if ( BackendlessPushService.isFCM( context ) )
          {
            FCMRegistration.unregisterDevice( context, channels );
          }
          else
          {
            if( !GCMRegistrar.isRegistered( context ) )
              return new IllegalArgumentException( ExceptionMessage.DEVICE_NOT_REGISTERED );

            GCMRegistrar.unregister( context );
          }
          return null;
        }
        catch( RuntimeException t )
        {
          return t;
        }
      }

      @Override
      protected void onPostExecute( RuntimeException result )
      {
        if( result != null )
        {
          if( callback == null )
            throw result;

          callback.handleFault( new BackendlessFault( result ) );
        }
        else
        {
          if( callback != null )
            callback.handleResponse( null );
        }
      }
    }.execute();
  }

  public boolean unregisterDeviceOnServer()
  {
    return (Boolean) Invoker.invokeSync( DEVICE_REGISTRATION_MANAGER_SERVER_ALIAS, "unregisterDevice", new Object[] { getDeviceId() } );
  }

  public void unregisterDeviceOnServer( final AsyncCallback<Boolean> responder )
  {
    Invoker.invokeAsync( DEVICE_REGISTRATION_MANAGER_SERVER_ALIAS, "unregisterDevice", new Object[] { getDeviceId() }, responder );
  }

  public int unregisterDeviceOnServer( List<String> channels )
  {
    return (int) Invoker.invokeSync( DEVICE_REGISTRATION_MANAGER_SERVER_ALIAS, "unregisterDevice", new Object[] { getDeviceId(), channels } );
  }

  public void unregisterDeviceOnServer( List<String> channels, final AsyncCallback<Integer> responder )
  {
    Invoker.invokeAsync( DEVICE_REGISTRATION_MANAGER_SERVER_ALIAS, "unregisterDevice", new Object[] { getDeviceId(), channels }, responder );
  }

  public boolean refreshDeviceToken( String newDeviceToken )
  {
    return Invoker.invokeSync( DEVICE_REGISTRATION_MANAGER_SERVER_ALIAS, "refreshDeviceToken", new Object[] { getDeviceId(), newDeviceToken } );
  }

  public void refreshDeviceToken( String newDeviceToken, final AsyncCallback<Boolean> responder )
  {
    Invoker.invokeAsync( DEVICE_REGISTRATION_MANAGER_SERVER_ALIAS, "refreshDeviceToken", new Object[] { getDeviceId(), newDeviceToken }, responder );
  }

  public DeviceRegistration getDeviceRegistration()
  {
    return getRegistrations();
  }

  public DeviceRegistration getRegistrations()
  {
    return (DeviceRegistration) Invoker.invokeSync( DEVICE_REGISTRATION_MANAGER_SERVER_ALIAS, "getDeviceRegistrationByDeviceId", new Object[] { getDeviceId() } );
  }

  public void getDeviceRegistration( AsyncCallback<DeviceRegistration> responder )
  {
    getRegistrations( responder );
  }

  public void getRegistrations( AsyncCallback<DeviceRegistration> responder )
  {
    Invoker.invokeAsync( DEVICE_REGISTRATION_MANAGER_SERVER_ALIAS, "getDeviceRegistrationByDeviceId", new Object[] { getDeviceId() }, responder );
  }

  /**
   * Publishes message to "default" channel. The message is not a push notification, it does not have any headers and
   * does not go into any subtopics.
   *
   * @param   message   object to publish. The object can be of any data type - a primitive value, String, Date, a
   *                    user-defined complex type, a collection or an array of these types.
   * @return a data structure which contains ID of the published message and the status of the publish operation.
   * @throws  BackendlessException
   */
  public MessageStatus publish( Object message )
  {
    if( message instanceof PublishOptions || message instanceof DeliveryOptions )
      throw new IllegalArgumentException( ExceptionMessage.INCORRECT_MESSAGE_TYPE );

    return publish( null, message );
  }

  /**
   * Publishes message to specified channel. The message is not a push notification, it does not have any headers and
   * does not go into any subtopics.
   *
   * @param   channelName name of a channel to publish the message to. If the channel does not exist, Backendless
   *                      automatically creates it.
   * @param   message     object to publish. The object can be of any data type - a primitive value, String, Date, a
   *                      user-defined complex type, a collection or an array of these types.
   * @return ${@link com.backendless.messaging.MessageStatus} - a data structure which contains ID of the published
   *         message and the status of the publish operation.
   * @throws  BackendlessException
   */
  public MessageStatus publish( String channelName, Object message )
  {
    return publish( channelName, message, new PublishOptions() );
  }

  /**
   * Publishes message to specified channel. The message is not a push notification, it may have headers and/or subtopic
   * defined in the publishOptions argument.
   *
   * @param   channelName name of a channel to publish the message to. If the channel does not exist, Backendless
   *                      automatically creates it.
   * @param   message     object to publish. The object can be of any data type - a primitive value, String, Date, a
   *                      user-defined complex type, a collection or an array of these types.
   * @param   publishOptions
   *                      an instance of ${@link com.backendless.messaging.PublishOptions}. When provided may contain
   *                      publisher ID (an arbitrary, application-specific string value identifying the publisher),
   *                      subtopic value and/or a collection of headers.
   * @return a data structure which contains ID of the published message and the status of the publish operation.
   */
  public MessageStatus publish( String channelName, Object message,
                                PublishOptions publishOptions )
  {
    return publish( channelName, message, publishOptions, new DeliveryOptions() );
  }

  /**
   * Publishes message to specified channel.The message may be configured as a push notification. It may have headers
   * and/or subtopic defined in the publishOptions argument.
   *
   * @param   channelName name of a channel to publish the message to. If the channel does not exist, Backendless
   *                      automatically creates it.
   * @param   message     object to publish. The object can be of any data type - a primitive value, String, Date, a
   *                      user-defined complex type, a collection or an array of these types.
   * @param   publishOptions
   *                      an instance of ${@link com.backendless.messaging.PublishOptions}. When provided may contain
   *                      publisher ID (an arbitrary, application-specific string value identifying the publisher),
   *                      subtopic value and/or a collection of headers.
   * @param   deliveryOptions
   *                      an instance of ${@link com.backendless.messaging.DeliveryOptions}. When provided may specify
   *                      options for message delivery such as: deliver as a push notification, deliver to specific
   *                      devices (or a group of devices grouped by the operating system), delayed delivery or repeated
   *                      delivery.
   * @return a data structure which contains ID of the published message and the status of the publish operation.
   */
  public MessageStatus publish( String channelName, Object message, PublishOptions publishOptions,
                                DeliveryOptions deliveryOptions )
  {
    channelName = getCheckedChannelName( channelName );

    if( message == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_MESSAGE );

    if( deliveryOptions.getPushBroadcast() == 0 && deliveryOptions.getPushSinglecast().isEmpty() )
    {
      deliveryOptions.setPushBroadcast( PushBroadcastMask.ALL );
    }

    return (MessageStatus) Invoker.invokeSync( MESSAGING_MANAGER_SERVER_ALIAS, "publish", new Object[] { channelName, message, publishOptions, deliveryOptions } );
  }

  private String getCheckedChannelName( String channelName )
  {
    if( channelName == null )
      return DEFAULT_CHANNEL_NAME;

    if( channelName.equals( "" ) )
      return DEFAULT_CHANNEL_NAME;

    return channelName;
  }

  /**
   * Publishes message to "default" channel. The message is not a push notification, it does not have any headers and
   * does not go into any subtopics.
   *
   * @param   message   object to publish. The object can be of any data type - a primitive value, String, Date, a
   *                    user-defined complex type, a collection or an array of these types.
   * @param   publishOptions
   *                      an instance of ${@link com.backendless.messaging.PublishOptions}. When provided may contain
   *                      publisher ID (an arbitrary, application-specific string value identifying the publisher),
   *                      subtopic value and/or a collection of headers.
   * @return a data structure which contains ID of the published message and the status of the publish operation.
   * @throws  BackendlessException
   */
  public MessageStatus publish( Object message, PublishOptions publishOptions )
  {
    return publish( null, message, publishOptions );
  }

  /**
   * Publishes message to "default" channel.The message may be configured as a push notification. It may have headers
   * and/or subtopic defined in the publishOptions argument.
   *
   * @param   message     object to publish. The object can be of any data type - a primitive value, String, Date, a
   *                      user-defined complex type, a collection or an array of these types.
   * @param   publishOptions
   *                      an instance of ${@link com.backendless.messaging.PublishOptions}. When provided may contain
   *                      publisher ID (an arbitrary, application-specific string value identifying the publisher),
   *                      subtopic value and/or a collection of headers.
   * @param   deliveryOptions
   *                      an instance of ${@link com.backendless.messaging.DeliveryOptions}. When provided may specify
   *                      options for message delivery such as: deliver as a push notification, deliver to specific
   *                      devices (or a group of devices grouped by the operating system), delayed delivery or repeated
   *                      delivery.
   * @return a data structure which contains ID of the published message and the status of the publish operation.
   */
  public MessageStatus publish( Object message, PublishOptions publishOptions,
                                DeliveryOptions deliveryOptions )
  {
    return publish( null, message, publishOptions, deliveryOptions );
  }

  public void publish( Object message, final AsyncCallback<MessageStatus> responder )
  {
    publish( null, message, responder );
  }

  public void publish( String channelName, Object message, final AsyncCallback<MessageStatus> responder )
  {
    publish( channelName, message, new PublishOptions(), responder );
  }

  public void publish( String channelName, Object message, PublishOptions publishOptions,
                       final AsyncCallback<MessageStatus> responder )
  {
    publish( channelName, message, publishOptions, new DeliveryOptions(), responder );
  }

  public void publish( String channelName, Object message, PublishOptions publishOptions,
                       DeliveryOptions deliveryOptions, final AsyncCallback<MessageStatus> responder )
  {
    try
    {
      channelName = getCheckedChannelName( channelName );

      if( message == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_MESSAGE );

      Invoker.invokeAsync( MESSAGING_MANAGER_SERVER_ALIAS, "publish", new Object[] { channelName, message, publishOptions, deliveryOptions }, responder );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  public void publish( Object message, PublishOptions publishOptions, final AsyncCallback<MessageStatus> responder )
  {
    publish( null, message, publishOptions, new DeliveryOptions(), responder );
  }

  public void publish( Object message, PublishOptions publishOptions, DeliveryOptions deliveryOptions,
                       final AsyncCallback<MessageStatus> responder )
  {
    publish( null, message, publishOptions, deliveryOptions, responder );
  }

  public MessageStatus pushWithTemplate( String templateName )
  {
    if( templateName == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_EMPTY_TEMPLATE_NAME );

    return (MessageStatus) Invoker.invokeSync( MESSAGING_MANAGER_SERVER_ALIAS, "pushWithTemplate", new Object[] { templateName } );
  }

  public void pushWithTemplate( String templateName, final AsyncCallback<MessageStatus> responder )
  {
    if( templateName == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_EMPTY_TEMPLATE_NAME );

    Invoker.invokeAsync( MESSAGING_MANAGER_SERVER_ALIAS, "pushWithTemplate", new Object[] { templateName }, responder );
  }

  public MessageStatus getMessageStatus( String messageId )
  {
    if( messageId == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_MESSAGE_ID );
    MessageStatus messageStatus = Invoker.invokeSync( MESSAGING_MANAGER_SERVER_ALIAS, "getMessageStatus", new Object[]
            { messageId } );

    return messageStatus;
  }

  public void getMessageStatus( String messageId, AsyncCallback<MessageStatus> responder )
  {
    try
    {
      if( messageId == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_MESSAGE_ID );

      Invoker.invokeAsync( MESSAGING_MANAGER_SERVER_ALIAS, "getMessageStatus", new Object[] { messageId }, responder );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  public boolean cancel( String messageId )
  {
    if( messageId == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_MESSAGE_ID );

    MessageStatus cancel = Invoker.invokeSync( MESSAGING_MANAGER_SERVER_ALIAS, "cancel", new Object[] { messageId } );
    return cancel.getStatus() == PublishStatusEnum.CANCELLED;
  }

  public void cancel( String messageId, AsyncCallback<MessageStatus> responder )
  {
    try
    {
      if( messageId == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_MESSAGE_ID );

      Invoker.invokeAsync( MESSAGING_MANAGER_SERVER_ALIAS, "cancel", new Object[] { messageId }, responder );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  public Channel subscribe( )
  {
      return subscribe( DEFAULT_CHANNEL_NAME );
  }

  public Channel subscribe( String channelName )
  {
    return chanelFactory.create( channelName );
  }

  public List<Message> pollMessages( String channelName, String subscriptionId )
  {
    checkChannelName( channelName );

    if( subscriptionId == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_SUBSCRIPTION_ID );

    Object[] result = (Object[]) Invoker.invokeSync( MESSAGING_MANAGER_SERVER_ALIAS, "pollMessages", new Object[] { channelName, subscriptionId } );

    return result.length == 0 ? new ArrayList<Message>() : Arrays.asList( (Message[]) result );
  }

  protected void pollMessages( String channelName, String subscriptionId, final AsyncCallback<List<Message>> responder )
  {
    try
    {
      checkChannelName( channelName );

      if( subscriptionId == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_SUBSCRIPTION_ID );

      Invoker.invokeAsync( MESSAGING_MANAGER_SERVER_ALIAS, "pollMessages", new Object[] { channelName, subscriptionId }, new AsyncCallback<Object[]>()
      {
        @Override
        public void handleResponse( Object[] response )
        {
          if( responder != null )
            responder.handleResponse( response.length == 0 ? new ArrayList<Message>() : Arrays.asList( (Message[]) response ) );
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

  public MessageStatus sendTextEmail( String subject, String messageBody, List<String> recipients )
  {
    return sendEmail( subject, new BodyParts( messageBody, null ), recipients, new ArrayList<String>() );
  }

  public MessageStatus sendTextEmail( String subject, String messageBody, String recipient )
  {
    return sendEmail( subject, new BodyParts( messageBody, null ), Arrays.asList( recipient ), new ArrayList<String>() );
  }

  public MessageStatus sendHTMLEmail( String subject, String messageBody, List<String> recipients )
  {
    return sendEmail( subject, new BodyParts( null, messageBody ), recipients, new ArrayList<String>() );
  }

  public MessageStatus sendHTMLEmail( String subject, String messageBody, String recipient )
  {
    return sendEmail( subject, new BodyParts( null, messageBody ), Arrays.asList( recipient ), new ArrayList<String>() );
  }

  public MessageStatus sendEmail( String subject, BodyParts bodyParts, String recipient, List<String> attachments )
  {
    return sendEmail( subject, bodyParts, Arrays.asList( recipient ), attachments );
  }

  public MessageStatus sendEmail( String subject, BodyParts bodyParts, String recipient )
  {
    return sendEmail( subject, bodyParts, Arrays.asList( recipient ), new ArrayList<String>() );
  }

  public MessageStatus sendEmail( String subject, BodyParts bodyParts, List<String> recipients, List<String> attachments )
  {
    if( subject == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_SUBJECT );

    if( bodyParts == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_BODYPARTS );

    if( recipients == null || recipients.isEmpty() )
      throw new IllegalArgumentException( ExceptionMessage.NULL_RECIPIENTS );

    if( attachments == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_ATTACHMENTS );

    return Invoker.invokeSync( EMAIL_MANAGER_SERVER_ALIAS, "send", new Object[] { subject, bodyParts, recipients, attachments } );
  }

  public void sendTextEmail( String subject, String messageBody, List<String> recipients, final AsyncCallback<MessageStatus> responder )
  {
    sendEmail( subject, new BodyParts( messageBody, null ), recipients, new ArrayList<String>(), responder );
  }

  public void sendTextEmail( String subject, String messageBody, String recipient, final AsyncCallback<MessageStatus> responder )
  {
    sendEmail( subject, new BodyParts( messageBody, null ), Arrays.asList( recipient ), new ArrayList<String>(), responder );
  }

  public void sendHTMLEmail( String subject, String messageBody, List<String> recipients,
                             final AsyncCallback<MessageStatus> responder )
  {
    sendEmail( subject, new BodyParts( null, messageBody ), recipients, new ArrayList<String>(), responder );
  }

  public void sendHTMLEmail( String subject, String messageBody, String recipient, final AsyncCallback<MessageStatus> responder )
  {
    sendEmail( subject, new BodyParts( null, messageBody ), Arrays.asList( recipient ), new ArrayList<String>(), responder );
  }

  public void sendEmail( String subject, BodyParts bodyParts, String recipient, List<String> attachments,
                         final AsyncCallback<MessageStatus> responder )
  {
    sendEmail( subject, bodyParts, Arrays.asList( recipient ), attachments, responder );
  }

  public void sendEmail( String subject, BodyParts bodyParts, String recipient, final AsyncCallback<MessageStatus> responder )
  {
    sendEmail( subject, bodyParts, Arrays.asList( recipient ), new ArrayList<String>(), responder );
  }

  public void sendEmail( String subject, BodyParts bodyParts, List<String> recipients, List<String> attachments,
                         final AsyncCallback<MessageStatus> responder )
  {
    try
    {
      if( subject == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_SUBJECT );

      if( bodyParts == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_BODYPARTS );

      if( recipients == null || recipients.isEmpty() )
        throw new IllegalArgumentException( ExceptionMessage.NULL_RECIPIENTS );

      if( attachments == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_ATTACHMENTS );

      Invoker.invokeAsync( EMAIL_MANAGER_SERVER_ALIAS, "send", new Object[] { subject, bodyParts, recipients, attachments }, responder );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }
}