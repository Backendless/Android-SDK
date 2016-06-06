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

import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessException;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.exceptions.ExceptionMessage;
import com.backendless.messaging.*;
import com.backendless.push.GCMRegistrar;

import com.backendless.services.messaging.MessageStatus;
import com.backendless.services.messaging.PublishStatusEnum;
import weborb.types.Types;

import java.util.*;

public final class Messaging
{
  public static String DEVICE_ID;
  private final static String MESSAGING_MANAGER_SERVER_ALIAS = "com.backendless.services.messaging.MessagingService";
  private final static String DEVICE_REGISTRATION_MANAGER_SERVER_ALIAS = "com.backendless.services.messaging.DeviceRegistrationService";
  private final static String EMAIL_MANAGER_SERVER_ALIAS = "com.backendless.services.mail.CustomersEmailService";
  private final static String DEFAULT_CHANNEL_NAME = "default";
  private final static String OS;
  private final static String OS_VERSION;
  private static final Messaging instance = new Messaging();

  private Messaging()
  {
    Types.addClientClassMapping( "com.backendless.management.DeviceRegistrationDto", DeviceRegistration.class );
    Types.addClientClassMapping( "com.backendless.services.messaging.PublishOptions", PublishOptions.class );
    Types.addClientClassMapping( "com.backendless.services.messaging.DeliveryOptions", DeliveryOptions.class );
    Types.addClientClassMapping( "com.backendless.services.messaging.Message", Message.class );
  }

  static
  {
    String id = null;
    if( Backendless.isAndroid() )
    {
      id = Build.SERIAL;
      OS_VERSION = String.valueOf( Build.VERSION.SDK_INT );
      OS = "ANDROID";
    }
    else
    {
      OS_VERSION = System.getProperty( "os.version" );
      OS = System.getProperty( "os.name" );
    }

    if( id == null || id.equals( "" ) )
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

    DEVICE_ID = id;
  }

  static Messaging getInstance()
  {
    return instance;
  }

  public void registerDevice( String GCMSenderID )
  {
    registerDevice( GCMSenderID, "" );
  }

  public void registerDevice( String GCMSenderID, String channel )
  {
    registerDevice( GCMSenderID, channel, null );
  }

  public void registerDevice( String GCMSenderID, AsyncCallback<Void> callback )
  {
    registerDevice( GCMSenderID, "", callback );
  }

  public void registerDevice( String GCMSenderID, String channel, AsyncCallback<Void> callback )
  {
    registerDevice( GCMSenderID, (channel == null || channel.equals( "" )) ? null : Arrays.asList( channel ), null, callback );
  }

  public void registerDevice( String GCMSenderID, List<String> channels, Date expiration )
  {
    registerDevice( GCMSenderID, channels, expiration, null );
  }

  public void registerDevice( final String GCMSenderID, final List<String> channels, final Date expiration,
                              final AsyncCallback<Void> callback )
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

  private synchronized void registerDeviceGCMSync( Context context, String GCMSenderID, List<String> channels,
                                                   Date expiration ) throws BackendlessException
  {
    if( channels != null )
      for( String channel : channels )
        checkChannelName( channel );

    if( expiration != null && expiration.before( Calendar.getInstance().getTime() ) )
      throw new IllegalArgumentException( ExceptionMessage.WRONG_EXPIRATION_DATE );

    GCMRegistrar.checkDevice( context );
    GCMRegistrar.checkManifest( context );
    GCMRegistrar.register( context, GCMSenderID, channels, expiration );
  }

  private void checkChannelName( String channelName ) throws BackendlessException
  {
    if( channelName == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_CHANNEL_NAME );

    if( channelName.equals( "" ) )
      throw new IllegalArgumentException( ExceptionMessage.NULL_CHANNEL_NAME );
  }

  public String registerDeviceOnServer( String deviceToken, final List<String> channels,
                                        final long expiration ) throws BackendlessException
  {
    if( deviceToken == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_DEVICE_TOKEN );

    DeviceRegistration deviceRegistration = new DeviceRegistration();
    deviceRegistration.setDeviceId( DEVICE_ID );
    deviceRegistration.setOs( OS );
    deviceRegistration.setOsVersion( OS_VERSION );
    deviceRegistration.setDeviceToken( deviceToken );
    deviceRegistration.setChannels( channels );
    if( expiration != 0 )
      deviceRegistration.setExpiration( new Date( expiration ) );

    return Invoker.invokeSync( DEVICE_REGISTRATION_MANAGER_SERVER_ALIAS, "registerDevice", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), deviceRegistration } );
  }

  public void registerDeviceOnServer( String deviceToken, final List<String> channels, final long expiration,
                                      final AsyncCallback<String> responder )
  {
    try
    {
      if( deviceToken == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_DEVICE_TOKEN );

      DeviceRegistration deviceRegistration = new DeviceRegistration();
      deviceRegistration.setDeviceId( DEVICE_ID );
      deviceRegistration.setOs( OS );
      deviceRegistration.setOsVersion( OS_VERSION );
      deviceRegistration.setDeviceToken( deviceToken );
      deviceRegistration.setChannels( channels );
      if( expiration != 0 )
        deviceRegistration.setExpiration( new Date( expiration ) );

      Invoker.invokeAsync( DEVICE_REGISTRATION_MANAGER_SERVER_ALIAS, "registerDevice", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), deviceRegistration }, new AsyncCallback<String>()
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
    unregisterDevice( null );
  }

  public void unregisterDevice( final AsyncCallback<Void> callback )
  {
    new AsyncTask<Void, Void, RuntimeException>()
    {
      @Override
      protected RuntimeException doInBackground( Void... params )
      {
        try
        {
          Context context = ContextHandler.getAppContext();

          if( !GCMRegistrar.isRegistered( context ) )
            return new IllegalArgumentException( ExceptionMessage.DEVICE_NOT_REGISTERED );

          GCMRegistrar.unregister( context );
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

  public boolean unregisterDeviceOnServer() throws BackendlessException
  {
    return (Boolean) Invoker.invokeSync( DEVICE_REGISTRATION_MANAGER_SERVER_ALIAS, "unregisterDevice", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), DEVICE_ID } );
  }

  public void unregisterDeviceOnServer( final AsyncCallback<Boolean> responder )
  {
    Invoker.invokeAsync( DEVICE_REGISTRATION_MANAGER_SERVER_ALIAS, "unregisterDevice", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), DEVICE_ID }, responder );
  }

  public DeviceRegistration getDeviceRegistration() throws BackendlessException
  {
    return getRegistrations();
  }

  public DeviceRegistration getRegistrations() throws BackendlessException
  {
    return (DeviceRegistration) Invoker.invokeSync( DEVICE_REGISTRATION_MANAGER_SERVER_ALIAS, "getDeviceRegistrationByDeviceId", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), DEVICE_ID } );
  }

  public void getDeviceRegistration( AsyncCallback<DeviceRegistration> responder )
  {
    getRegistrations( responder );
  }

  public void getRegistrations( AsyncCallback<DeviceRegistration> responder )
  {
    Invoker.invokeAsync( DEVICE_REGISTRATION_MANAGER_SERVER_ALIAS, "getDeviceRegistrationByDeviceId", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), DEVICE_ID }, responder );
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
  public MessageStatus publish( Object message ) throws BackendlessException
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
  public MessageStatus publish( String channelName, Object message ) throws BackendlessException
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
   * @throws BackendlessException
   */
  public MessageStatus publish( String channelName, Object message,
                                PublishOptions publishOptions ) throws BackendlessException
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
   * @throws BackendlessException
   */
  public MessageStatus publish( String channelName, Object message, PublishOptions publishOptions,
                                DeliveryOptions deliveryOptions ) throws BackendlessException
  {
    channelName = getCheckedChannelName( channelName );

    if( message == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_MESSAGE );

    if( deliveryOptions.getPushBroadcast() == 0 && deliveryOptions.getPushSinglecast().isEmpty() )
    {
      deliveryOptions.setPushBroadcast( PushBroadcastMask.ALL );
    }

    return (MessageStatus) Invoker.invokeSync( MESSAGING_MANAGER_SERVER_ALIAS, "publish", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), channelName, message, publishOptions, deliveryOptions } );
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
  public MessageStatus publish( Object message, PublishOptions publishOptions ) throws BackendlessException
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
   * @throws BackendlessException
   */
  public MessageStatus publish( Object message, PublishOptions publishOptions,
                                DeliveryOptions deliveryOptions ) throws BackendlessException
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

      Invoker.invokeAsync( MESSAGING_MANAGER_SERVER_ALIAS, "publish", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), channelName, message, publishOptions, deliveryOptions }, responder );
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

  public MessageStatus getMessageStatus( String messageId )
  {
    if( messageId == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_MESSAGE_ID );
    MessageStatus messageStatus = Invoker.invokeSync( MESSAGING_MANAGER_SERVER_ALIAS, "getMessageStatus", new Object[]
            { Backendless.getApplicationId(), Backendless.getVersion(), messageId } );

    return messageStatus;
  }

  public void getMessageStatus( String messageId, AsyncCallback<MessageStatus> responder )
  {
    try
    {
      if( messageId == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_MESSAGE_ID );

      Invoker.invokeAsync( MESSAGING_MANAGER_SERVER_ALIAS, "getMessageStatus", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), messageId }, responder );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  public boolean cancel( String messageId ) throws BackendlessException
  {
    if( messageId == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_MESSAGE_ID );

    MessageStatus cancel = Invoker.invokeSync( MESSAGING_MANAGER_SERVER_ALIAS, "cancel", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), messageId } );
    return cancel.getStatus() == PublishStatusEnum.CANCELLED;
  }

  public void cancel( String messageId, AsyncCallback<MessageStatus> responder )
  {
    try
    {
      if( messageId == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_MESSAGE_ID );

      Invoker.invokeAsync( MESSAGING_MANAGER_SERVER_ALIAS, "cancel", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), messageId }, responder );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  public Subscription subscribe( AsyncCallback<List<Message>> subscriptionResponder ) throws BackendlessException
  {
    return subscribe( DEFAULT_CHANNEL_NAME, subscriptionResponder, null, 0 );
  }

  public Subscription subscribe( String channelName, AsyncCallback<List<Message>> subscriptionResponder,
                                 SubscriptionOptions subscriptionOptions,
                                 int pollingInterval ) throws BackendlessException
  {
    checkChannelName( channelName );

    if( pollingInterval < 0 )
      throw new IllegalArgumentException( ExceptionMessage.WRONG_POLLING_INTERVAL );

    String subscriptionId = subscribeForPollingAccess( channelName, subscriptionOptions );

    Subscription subscription = new Subscription();
    subscription.setChannelName( channelName );
    subscription.setSubscriptionId( subscriptionId );

    if( pollingInterval != 0 )
      subscription.setPollingInterval( pollingInterval );

    subscription.onSubscribe( subscriptionResponder );

    return subscription;
  }

  private String subscribeForPollingAccess( String channelName,
                                            SubscriptionOptions subscriptionOptions ) throws BackendlessException
  {
    if( channelName == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_CHANNEL_NAME );

    if( subscriptionOptions == null )
      subscriptionOptions = new SubscriptionOptions();

    return Invoker.invokeSync( MESSAGING_MANAGER_SERVER_ALIAS, "subscribeForPollingAccess", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), channelName, subscriptionOptions } );
  }

  public Subscription subscribe( String channelName,
                                 AsyncCallback<List<Message>> subscriptionResponder ) throws BackendlessException
  {
    return subscribe( channelName, subscriptionResponder, null, 0 );
  }

  public Subscription subscribe( int pollingInterval,
                                 AsyncCallback<List<Message>> subscriptionResponder ) throws BackendlessException
  {
    return subscribe( DEFAULT_CHANNEL_NAME, subscriptionResponder, null, pollingInterval );
  }

  public Subscription subscribe( String channelName, int pollingInterval,
                                 AsyncCallback<List<Message>> subscriptionResponder ) throws BackendlessException
  {
    return subscribe( channelName, subscriptionResponder, null, pollingInterval );
  }

  public Subscription subscribe( AsyncCallback<List<Message>> subscriptionResponder,
                                 SubscriptionOptions subscriptionOptions ) throws BackendlessException
  {
    return subscribe( DEFAULT_CHANNEL_NAME, subscriptionResponder, subscriptionOptions, 0 );
  }

  public Subscription subscribe( String channelName, AsyncCallback<List<Message>> subscriptionResponder,
                                 SubscriptionOptions subscriptionOptions ) throws BackendlessException
  {
    return subscribe( channelName, subscriptionResponder, subscriptionOptions, 0 );
  }

  public void subscribe( AsyncCallback<List<Message>> subscriptionResponder, AsyncCallback<Subscription> responder )
  {
    subscribe( DEFAULT_CHANNEL_NAME, subscriptionResponder, null, 0, responder );
  }

  public void subscribe( final String channelName, final AsyncCallback<List<Message>> subscriptionResponder,
                         SubscriptionOptions subscriptionOptions, final int pollingInterval,
                         final AsyncCallback<Subscription> responder )
  {
    try
    {
      checkChannelName( channelName );

      if( pollingInterval < 0 )
        throw new IllegalArgumentException( ExceptionMessage.WRONG_POLLING_INTERVAL );

      subscribeForPollingAccess( channelName, subscriptionOptions, new AsyncCallback<String>()
      {
        @Override
        public void handleResponse( String subscriptionId )
        {
          Subscription subscription = new Subscription();
          subscription.setChannelName( channelName );
          subscription.setSubscriptionId( subscriptionId );

          if( pollingInterval != 0 )
            subscription.setPollingInterval( pollingInterval );

          subscription.onSubscribe( subscriptionResponder );

          if( responder != null )
            responder.handleResponse( subscription );
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

  private void subscribeForPollingAccess( String channelName, SubscriptionOptions subscriptionOptions,
                                          AsyncCallback<String> responder )
  {
    try
    {
      if( channelName == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_CHANNEL_NAME );

      if( subscriptionOptions == null )
        subscriptionOptions = new SubscriptionOptions();

      Invoker.invokeAsync( MESSAGING_MANAGER_SERVER_ALIAS, "subscribeForPollingAccess", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), channelName, subscriptionOptions }, responder );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  public void subscribe( String channelName, AsyncCallback<List<Message>> subscriptionResponder,
                         AsyncCallback<Subscription> responder )
  {
    subscribe( channelName, subscriptionResponder, null, 0, responder );
  }

  public void subscribe( int pollingInterval, AsyncCallback<List<Message>> subscriptionResponder,
                         AsyncCallback<Subscription> responder )
  {
    subscribe( DEFAULT_CHANNEL_NAME, subscriptionResponder, null, pollingInterval, responder );
  }

  public void subscribe( String channelName, int pollingInterval, AsyncCallback<List<Message>> subscriptionResponder,
                         AsyncCallback<Subscription> responder )
  {
    subscribe( channelName, subscriptionResponder, null, pollingInterval, responder );
  }

  public void subscribe( AsyncCallback<List<Message>> subscriptionResponder, SubscriptionOptions subscriptionOptions,
                         AsyncCallback<Subscription> responder )
  {
    subscribe( DEFAULT_CHANNEL_NAME, subscriptionResponder, subscriptionOptions, 0, responder );
  }

  public void subscribe( String channelName, AsyncCallback<List<Message>> subscriptionResponder,
                         SubscriptionOptions subscriptionOptions, AsyncCallback<Subscription> responder )
  {
    subscribe( channelName, subscriptionResponder, subscriptionOptions, 0, responder );
  }

  public List<Message> pollMessages( String channelName, String subscriptionId ) throws BackendlessException
  {
    checkChannelName( channelName );

    if( subscriptionId == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_SUBSCRIPTION_ID );

    Object[] result = (Object[]) Invoker.invokeSync( MESSAGING_MANAGER_SERVER_ALIAS, "pollMessages", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), channelName, subscriptionId } );

    return result.length == 0 ? new ArrayList<Message>() : Arrays.asList( (Message[]) result );
  }

  protected void pollMessages( String channelName, String subscriptionId, final AsyncCallback<List<Message>> responder )
  {
    try
    {
      checkChannelName( channelName );

      if( subscriptionId == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_SUBSCRIPTION_ID );

      Invoker.invokeAsync( MESSAGING_MANAGER_SERVER_ALIAS, "pollMessages", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), channelName, subscriptionId }, new AsyncCallback<Object[]>()
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

  public void sendTextEmail( String subject, String messageBody, List<String> recipients )
  {
    sendEmail( subject, new BodyParts( messageBody, null ), recipients, new ArrayList<String>() );
  }

  public void sendTextEmail( String subject, String messageBody, String recipient )
  {
    sendEmail( subject, new BodyParts( messageBody, null ), Arrays.asList( recipient ), new ArrayList<String>() );
  }

  public void sendHTMLEmail( String subject, String messageBody, List<String> recipients )
  {
    sendEmail( subject, new BodyParts( null, messageBody ), recipients, new ArrayList<String>() );
  }

  public void sendHTMLEmail( String subject, String messageBody, String recipient )
  {
    sendEmail( subject, new BodyParts( null, messageBody ), Arrays.asList( recipient ), new ArrayList<String>() );
  }

  public void sendEmail( String subject, BodyParts bodyParts, String recipient, List<String> attachments )
  {
    sendEmail( subject, bodyParts, Arrays.asList( recipient ), attachments );
  }

  public void sendEmail( String subject, BodyParts bodyParts, String recipient )
  {
    sendEmail( subject, bodyParts, Arrays.asList( recipient ), new ArrayList<String>() );
  }

  public void sendEmail( String subject, BodyParts bodyParts, List<String> recipients, List<String> attachments )
  {
    if( subject == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_SUBJECT );

    if( bodyParts == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_BODYPARTS );

    if( recipients == null || recipients.isEmpty() )
      throw new IllegalArgumentException( ExceptionMessage.NULL_RECIPIENTS );

    if( attachments == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_ATTACHMENTS );

    Invoker.invokeSync( EMAIL_MANAGER_SERVER_ALIAS, "send", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), subject, bodyParts, recipients, attachments } );
  }

  public void sendTextEmail( String subject, String messageBody, List<String> recipients, final AsyncCallback<Void> responder )
  {
    sendEmail( subject, new BodyParts( messageBody, null ), recipients, new ArrayList<String>(), responder );
  }

  public void sendTextEmail( String subject, String messageBody, String recipient, final AsyncCallback<Void> responder )
  {
    sendEmail( subject, new BodyParts( messageBody, null ), Arrays.asList( recipient ), new ArrayList<String>(), responder );
  }

  public void sendHTMLEmail( String subject, String messageBody, List<String> recipients,
                             final AsyncCallback<Void> responder )
  {
    sendEmail( subject, new BodyParts( null, messageBody ), recipients, new ArrayList<String>(), responder );
  }

  public void sendHTMLEmail( String subject, String messageBody, String recipient, final AsyncCallback<Void> responder )
  {
    sendEmail( subject, new BodyParts( null, messageBody ), Arrays.asList( recipient ), new ArrayList<String>(), responder );
  }

  public void sendEmail( String subject, BodyParts bodyParts, String recipient, List<String> attachments,
                         final AsyncCallback<Void> responder )
  {
    sendEmail( subject, bodyParts, Arrays.asList( recipient ), attachments, responder );
  }

  public void sendEmail( String subject, BodyParts bodyParts, String recipient, final AsyncCallback<Void> responder )
  {
    sendEmail( subject, bodyParts, Arrays.asList( recipient ), new ArrayList<String>(), responder );
  }

  public void sendEmail( String subject, BodyParts bodyParts, List<String> recipients, List<String> attachments,
                         final AsyncCallback<Void> responder )
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

      Invoker.invokeAsync( EMAIL_MANAGER_SERVER_ALIAS, "send", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), subject, bodyParts, recipients, attachments }, responder );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }
}