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

import static com.backendless.Backendless.getPreferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import weborb.messaging.v3.Subscriber;
import weborb.types.Types;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessException;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.exceptions.ExceptionMessage;
import com.backendless.messaging.AndroidHandler;
import com.backendless.messaging.BodyParts;
import com.backendless.messaging.DeliveryMethodEnum;
import com.backendless.messaging.DeliveryOptions;
import com.backendless.messaging.GenericMessagingHandler;
import com.backendless.messaging.IMessageHandler;
import com.backendless.messaging.Message;
import com.backendless.messaging.MessageStatus;
import com.backendless.messaging.PublishOptions;
import com.backendless.messaging.PublishStatusEnum;
import com.backendless.messaging.PushBroadcastMask;
import com.backendless.messaging.SubscriptionOptions;
import com.backendless.push.AbstractRegistrar;
import com.backendless.push.BackendlessPushBroadcastReceiver;
import com.backendless.push.adm.ADMRegistrar;
import com.backendless.push.gcm.GCMRegistrar;

public final class Messaging
{

  private static final String IS_PUSH_PUB_SUB_PREFERENCES_KEY = "isPushPubSub";
  public final static String DEVICE_ID;
  private final static String MESSAGING_MANAGER_SERVER_ALIAS = "com.backendless.services.messaging.MessagingService";
  private final static String DEVICE_REGISTRATION_MANAGER_SERVER_ALIAS = "com.backendless.services.messaging.DeviceRegistrationService";
  private final static String EMAIL_MANAGER_SERVER_ALIAS = "com.backendless.services.mail.CustomersEmailService";
  private final static String DEFAULT_CHANNEL_NAME = "default";
  private final static String OS;
  private final static String OS_VERSION;
  private static final Messaging instance = new Messaging();
  private static final Map<String, Subscription> subscriptions = new HashMap<String, Subscription>();
  private static Boolean isPushPubSub;
  private static String gcmSenderId;
  private AsyncCallback<Void> deviceRegistrationCallback;
  private AsyncCallback<String> deviceSubscriptionCallback;
  private static final SubscriptionOptions defaultSubscriptionOptions = new SubscriptionOptions();
  private static final AbstractRegistrar registrar = Backendless.isFireOS() ? new ADMRegistrar() : new GCMRegistrar();
  private static final ScheduledExecutorService executor = Executors
                  .newSingleThreadScheduledExecutor( ThreadFactoryService.getThreadFactory() );
  private AsyncCallback<List<Message>> subscriptionResponder;
  private ScheduledFuture<?> currentTask;
  public static SubscriptionOptions options;

  private IMessageHandler handler;

  private String GCM_SENDER_ID_PREFERENCES_KEY = "gcmSenderId";

  private Messaging()
  {
    Types.addClientClassMapping( "com.backendless.management.DeviceRegistrationDto", DeviceRegistration.class );
    Types.addClientClassMapping( "com.backendless.services.messaging.MessageStatus", MessageStatus.class );
    Types.addClientClassMapping( "com.backendless.services.messaging.PublishStatusEnum", PublishStatusEnum.class );
    Types.addClientClassMapping( "com.backendless.services.messaging.PublishOptions", PublishOptions.class );
    Types.addClientClassMapping( "com.backendless.services.messaging.DeliveryOptions", DeliveryOptions.class );
    Types.addClientClassMapping( "com.backendless.services.messaging.Message", Message.class );

  }

  static
  {
    String id = null;

    if( Backendless.isFireOS() )
    {
      id = Build.SERIAL;
      OS_VERSION = String.valueOf( Build.VERSION.SDK_INT );
      OS = "FIRE_OS";
    }
    else if( Backendless.isAndroid() )
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

  private final class PushSubscriptionCallback implements AsyncCallback<String>
  {
    private final int pollingInterval;
    private final String channelName;
    private final AsyncCallback<Subscription> responder;

    private PushSubscriptionCallback( AsyncCallback<List<Message>> subscriptionResponder, int pollingInterval,
                                      String channelName, AsyncCallback<Subscription> responder )
    {
      Backendless.Messaging.subscriptionResponder = subscriptionResponder;
      this.pollingInterval = pollingInterval;
      this.channelName = channelName;
      this.responder = responder;
    }

    @Override
    public void handleResponse( String subscriptionId )
    {
      Subscription subscription = new Subscription();
      subscription.setChannelName( channelName );
      subscription.setSubscriptionId( subscriptionId );

      if( !isPushPubSub() )
      {
        Backendless.Messaging.subscribeForPollingMessageByInterval( subscription, pollingInterval );
      }
      if( responder != null )
        responder.handleResponse( subscription );
    }

    @Override
    public void handleFault( BackendlessFault fault )
    {
      if( responder != null )
        responder.handleFault( fault );
    }
  }

  private static void initReceiverVariables()
  {
    Context context = ContextHandler.getAppContext();
    PackageManager packageManager = context.getPackageManager();
    String packageName = context.getPackageName();
    ActivityInfo[] receivers = getReceivers( packageManager, packageName );
    if( receivers != null )
    {
      for( ActivityInfo receiver : receivers )
      {
        if( receiverExtendsPushBroadcast( receiver ) )
        {
          if( Backendless.isAndroid() )
            retrieveSenderIdMetaPresent( context, receiver.name );
          isPushPubSub = true;
        }
      }
    }
  }

  private void subscribeForPollingMessageByInterval( Subscription subscription, int pollingInterval )
  {
    checkPollingInterval( pollingInterval );

    if( pollingInterval != 0 )
      subscription.setPollingInterval( pollingInterval );

    onSubscribe( subscription, subscriptionResponder );
  }

  public synchronized boolean cancelSubscription( Subscription subscription )
  {
    if( currentTask != null )
    {
      currentTask.cancel( true );
      currentTask = null;
    }

    subscription.setSubscriptionId( null );

    return true;
  }

  protected void onSubscribe( Subscription subscription, final AsyncCallback<List<Message>> subscriptionResponder )
  {
    handler = Backendless.isAndroid() ? new AndroidHandler( subscriptionResponder, subscription )
                    : new GenericMessagingHandler( subscriptionResponder, subscription );

    this.subscriptionResponder = subscriptionResponder;

    if( Backendless.isAndroid() )
      executor.scheduleWithFixedDelay( handler.getSubscriptionThread(), 0, subscription.getPollingInterval(),
                      TimeUnit.MILLISECONDS );

    Backendless.Messaging.setSubscription( subscription.getChannelName(), subscription );
  }

  public void handlerMessage( List<Message> messages )
  {
    if( messages instanceof BackendlessException )
      subscriptionResponder.handleFault( new BackendlessFault( (BackendlessException) messages ) );
    else
      subscriptionResponder.handleResponse( messages );
  }

  public synchronized void pauseSubscription()
  {
    if( executor.isShutdown() )
      return;

    executor.shutdown();
  }

  public void unsubscribe( String subscriptionId, AsyncCallback<Void> callback )
  {
    Invoker.invokeAsync( MESSAGING_MANAGER_SERVER_ALIAS, "unsubscribe", new Object[]
                    { subscriptionId }, callback );
    for( Subscription subscription : subscriptions.values() )
    {
      if( subscription.getSubscriptionId().equals( subscriptionId ) )
      {
        subscriptions.values().remove( subscription );
      }
    }
  }

  public synchronized void resumeSubscription( Subscription subscription )
  {
    Runnable subscriptionThread = handler.getSubscriptionThread();

    if( subscription.getSubscriptionId() == null || subscription.getChannelName() == null || handler == null
                    || subscriptionThread == null )
      throw new IllegalStateException( ExceptionMessage.WRONG_SUBSCRIPTION_STATE );

    if( ( executor.isShutdown() ) && subscriptionThread != null )
    {
      executor.scheduleWithFixedDelay( subscriptionThread, 0, subscription.getPollingInterval(), TimeUnit.MILLISECONDS );
    }
  }

  private String gcmSenderId()
  {
    if( gcmSenderId == null )
    {
      if( getPreferences().get( GCM_SENDER_ID_PREFERENCES_KEY ) == null )
      {
        initReceiverVariables();
        saveToSharedPrefs( GCM_SENDER_ID_PREFERENCES_KEY, gcmSenderId );
      }
      else
      {
        gcmSenderId = getPreferences().get( GCM_SENDER_ID_PREFERENCES_KEY );
      }
    }
    return gcmSenderId;
  }

  private void saveToSharedPrefs( String key, String value )
  {
    getPreferences().set( key, value );
  }

  private boolean isPushPubSub()
  {
    if( isPushPubSub == null )
    {
      if( getPreferences().get( IS_PUSH_PUB_SUB_PREFERENCES_KEY ) == null )
      {
        initReceiverVariables();
        if( isPushPubSub == null )
          isPushPubSub = false;
        saveToSharedPrefs( IS_PUSH_PUB_SUB_PREFERENCES_KEY, isPushPubSub.toString() );
      }
      else
      {
        isPushPubSub = Boolean.valueOf( getPreferences().get( IS_PUSH_PUB_SUB_PREFERENCES_KEY ) );
      }
    }
    return isPushPubSub;
  }

  public void registerDevice( String GCMSenderID )
  {
    registerDevice( GCMSenderID, DEFAULT_CHANNEL_NAME );
  }

  public void registerDevice( String GCMSenderID, AsyncCallback<Void> callback )
  {
    registerDevice( GCMSenderID, DEFAULT_CHANNEL_NAME, callback );
  }

  public void registerDevice( String GCMSenderID, String channel )
  {
    registerDevice( GCMSenderID, channel, null );
  }

  public void registerDevice( String GCMSenderID, String channel, AsyncCallback<Void> callback )
  {
    List<String> channels = ( channel == null || channel.equals( "" ) ) ? null : Arrays.asList( channel );
    registerDevice( GCMSenderID, channels, null, callback );
  }

  public void registerDevice( String GCMSenderID, List<String> channels, Date expiration )
  {
    registerDevice( GCMSenderID, channels, expiration, null );
  }

  public void subscribeDevice( final String gcMSenderID, final String channelName, final AsyncCallback<String> callback )
  {
    deviceSubscriptionCallback = callback;

    new AsyncTask<String, Void, RuntimeException>()
    {

      @Override
      protected RuntimeException doInBackground( String... params )
      {
        try
        {
          BackendlessPushBroadcastReceiver.isPubSubRegisterIntent = true;
          subscribeDeviceGCMSync( ContextHandler.getAppContext(), gcMSenderID, channelName );
          if( params.length != 0 && deviceSubscriptionCallback != null )
            deviceSubscriptionCallback.handleResponse( params[ 0 ] );
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

  public String subscribeDeviceSyncForPush( final String gcMSenderID, final String channelName )
  {
    BackendlessPushBroadcastReceiver.isPubSubRegisterIntent = true;
    final String result = "";
    Backendless.Messaging.deviceSubscriptionCallback = new AsyncCallback<String>()
    {
      @Override
      public void handleResponse( String response )
      {
        if( response != null )
        {
          result.concat( response );
        }
      }

      @Override
      public void handleFault( BackendlessFault fault )
      {
        throw new BackendlessException( fault );
      }

    };

    BackendlessPushBroadcastReceiver.isPubSubRegisterIntent = true;
    subscribeDeviceGCMSync( ContextHandler.getAppContext(), gcMSenderID, channelName );
    return result;
  }


  public void registerDevice( final String gcMSenderID, final List<String> channels, final Date expiration,
                              final AsyncCallback<Void> callback )
  {
    deviceRegistrationCallback = callback;

    new AsyncTask<Void, Void, RuntimeException>()
    {
      @Override
      protected RuntimeException doInBackground( Void... params )
      {
        try
        {
          BackendlessPushBroadcastReceiver.isPubSubRegisterIntent = false;
          registerDeviceGCMSync( ContextHandler.getAppContext(), gcMSenderID, channels, expiration );
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

    registrar.checkPossibility( context );
    registrar.register( context, GCMSenderID, channels, expiration );
  }

  private synchronized void subscribeDeviceGCMSync( Context context, String gcMSenderID, String channelName )
                  throws BackendlessException
  {
    checkChannelName( channelName );

    registrar.checkPossibility( context );
    registrar.subscribe( context, gcMSenderID, channelName );
  }

  private void checkChannelName( String channelName ) throws BackendlessException
  {
    if( channelName == null || channelName.equals( "" ) )
      throw new IllegalArgumentException( ExceptionMessage.NULL_CHANNEL_NAME );
  }

  public String registerDeviceOnServer( String deviceToken, final List<String> channels, final long expiration )
                  throws BackendlessException
  {
    checkDeviceToken( deviceToken );

    DeviceRegistration deviceRegistration = new DeviceRegistration();
    deviceRegistration.setDeviceId( DEVICE_ID );
    deviceRegistration.setOs( OS );
    deviceRegistration.setOsVersion( OS_VERSION );
    deviceRegistration.setDeviceToken( deviceToken );
    deviceRegistration.setChannels( channels );
    if( expiration != 0 )
      deviceRegistration.setExpiration( new Date( expiration ) );

    return Invoker.invokeSync( DEVICE_REGISTRATION_MANAGER_SERVER_ALIAS, "registerDevice", new Object[]
    { Backendless.getApplicationId(), Backendless.getVersion(), deviceRegistration } );
  }

  private void checkDeviceToken( String deviceToken )
  {
    if( deviceToken == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_DEVICE_TOKEN );
  }

  public void subscribeDeviceForPush( String deviceToken, String channelName, final AsyncCallback<String> serverCallback )
  {
    checkDeviceToken( deviceToken );

    SubscriptionOptions options = Messaging.options;
    options.setDeliveryMethod( DeliveryMethodEnum.PUSH );

    DeviceRegistration registration = new DeviceRegistration();
    registration.setDeviceId( DEVICE_ID );
    registration.setDeviceToken( deviceToken );
    registration.setOs( OS );
    registration.setOsVersion( OS_VERSION );

    Invoker.invokeAsync( MESSAGING_MANAGER_SERVER_ALIAS, "subscribeForPollingAccess", new Object[]
    { Backendless.getApplicationId(), Backendless.getVersion(), channelName, options, registration },
                    new AsyncCallback<String>()
    {
      @Override
      public void handleResponse( String response )
      {
        if( serverCallback != null )
          serverCallback.handleResponse( response );
      }

      @Override
      public void handleFault( BackendlessFault fault )
      {
        if( serverCallback != null )
          serverCallback.handleFault( fault );
      }
    } );

  }

  public void registerDeviceOnServer( String deviceToken, final List<String> channels, final long expiration,
                                      final AsyncCallback<String> responder )
  {
    try
    {
      checkDeviceToken( deviceToken );
      
      DeviceRegistration deviceRegistration = new DeviceRegistration();
      deviceRegistration.setDeviceId( DEVICE_ID );
      deviceRegistration.setOs( OS );
      deviceRegistration.setOsVersion( OS_VERSION );
      deviceRegistration.setDeviceToken( deviceToken );
      deviceRegistration.setChannels( channels );
      if( expiration != 0 )
        deviceRegistration.setExpiration( new Date( expiration ) );

      Invoker.invokeAsync( DEVICE_REGISTRATION_MANAGER_SERVER_ALIAS, "registerDevice", new Object[]
      { Backendless.getApplicationId(), Backendless.getVersion(), deviceRegistration },
                      new AsyncCallback<String>()
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

          if( !registrar.isRegistered( context ) )
            return new IllegalArgumentException( ExceptionMessage.DEVICE_NOT_REGISTERED );

          registrar.unregister( context );
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
    return (Boolean) Invoker.invokeSync( DEVICE_REGISTRATION_MANAGER_SERVER_ALIAS, "unregisterDevice", new Object[]
    { Backendless.getApplicationId(), Backendless.getVersion(), DEVICE_ID } );
  }

  public void unregisterDeviceOnServer( final AsyncCallback<Boolean> responder )
  {
    Invoker.invokeAsync( DEVICE_REGISTRATION_MANAGER_SERVER_ALIAS, "unregisterDevice", new Object[]
    { Backendless.getApplicationId(), Backendless.getVersion(), DEVICE_ID }, responder );
  }

  public DeviceRegistration getDeviceRegistration() throws BackendlessException
  {
    return getRegistrations();
  }

  public DeviceRegistration getRegistrations() throws BackendlessException
  {
    return (DeviceRegistration) Invoker.invokeSync( DEVICE_REGISTRATION_MANAGER_SERVER_ALIAS,
                    "getDeviceRegistrationByDeviceId", new Object[]
                    { Backendless.getApplicationId(), Backendless.getVersion(), DEVICE_ID } );
  }

  public void getDeviceRegistration( AsyncCallback<DeviceRegistration> responder )
  {
    getRegistrations( responder );
  }

  public void getRegistrations( AsyncCallback<DeviceRegistration> responder )
  {
    Invoker.invokeAsync( DEVICE_REGISTRATION_MANAGER_SERVER_ALIAS, "getDeviceRegistrationByDeviceId", new Object[]
    { Backendless.getApplicationId(), Backendless.getVersion(), DEVICE_ID }, responder );
  }

  public MessageStatus publish( Object message ) throws BackendlessException
  {
    if( message instanceof PublishOptions || message instanceof DeliveryOptions )
      throw new IllegalArgumentException( ExceptionMessage.INCORRECT_MESSAGE_TYPE );

    return publish( null, message );
  }

  public MessageStatus publish( String channelName, Object message ) throws BackendlessException
  {
    return publish( channelName, message, new PublishOptions() );
  }

  public MessageStatus publish( String channelName, Object message, PublishOptions publishOptions )
                  throws BackendlessException
  {
    return publish( channelName, message, publishOptions, new DeliveryOptions() );
  }

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

    return (MessageStatus) Invoker
                    .invokeSync( MESSAGING_MANAGER_SERVER_ALIAS, "publish", new Object[]
                    { Backendless.getApplicationId(), Backendless.getVersion(), channelName, message, publishOptions,
                        deliveryOptions } );
  }

  private String getCheckedChannelName( String channelName )
  {
    if( channelName == null )
      return DEFAULT_CHANNEL_NAME;

    if( channelName.equals( "" ) )
      return DEFAULT_CHANNEL_NAME;

    return channelName;
  }

  public MessageStatus publish( Object message, PublishOptions publishOptions ) throws BackendlessException
  {
    return publish( null, message, publishOptions );
  }

  public MessageStatus publish( Object message, PublishOptions publishOptions, DeliveryOptions deliveryOptions )
                  throws BackendlessException
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

      Invoker.invokeAsync( MESSAGING_MANAGER_SERVER_ALIAS, "publish",
                      new Object[]
                      { Backendless.getApplicationId(), Backendless.getVersion(), channelName, message, publishOptions,
                          deliveryOptions }, responder );
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

      Invoker.invokeAsync( MESSAGING_MANAGER_SERVER_ALIAS, "getMessageStatus", new Object[]
      { Backendless.getApplicationId(), Backendless.getVersion(), messageId }, responder );
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

    MessageStatus cancel = Invoker.invokeSync( MESSAGING_MANAGER_SERVER_ALIAS, "cancel", new Object[]
    { Backendless.getApplicationId(), Backendless.getVersion(), messageId } );
    return cancel.getStatus() == PublishStatusEnum.CANCELLED;
  }

  public void cancel( String messageId, AsyncCallback<MessageStatus> responder )
  {
    try
    {
      if( messageId == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_MESSAGE_ID );

      Invoker.invokeAsync( MESSAGING_MANAGER_SERVER_ALIAS, "cancel", new Object[]
      { Backendless.getApplicationId(), Backendless.getVersion(), messageId }, responder );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  public Subscription subscribe( AsyncCallback<List<Message>> subscriptionResponder ) throws BackendlessException
  {
    return subscribe( DEFAULT_CHANNEL_NAME, subscriptionResponder, defaultSubscriptionOptions, 0 );
  }

  public Subscription subscribe( String channelName, AsyncCallback<List<Message>> subscriptionResponder,
                                 SubscriptionOptions subscriptionOptions, int pollingInterval )
                  throws BackendlessException
  {
    checkChannelName( channelName );

    if( subscriptionOptions == null )
      subscriptionOptions = defaultSubscriptionOptions;

    Subscription subscription = new Subscription();
    subscription.setChannelName( channelName );

    String subscriptionId = null;
    // if( isPushPubSub() )
    // {
    // subscriptionId = subscribeSyncForPush( channelName, subscriptionOptions
    // );
    // }
    // else
    // {
    subscriptionId = subscribeSyncForPolling( channelName, subscriptionOptions );
    subscribeForPollingMessageByInterval( subscription, pollingInterval );
    // }

    subscription.setSubscriptionId( subscriptionId );
    return subscription;
  }

  private static void retrieveSenderIdMetaPresent( Context context, String receiverName )
  {
    ActivityInfo appi = null;
    try
    {
      appi = context.getPackageManager().getReceiverInfo( new ComponentName( context, receiverName ),
                      PackageManager.GET_META_DATA );
    }
    catch( NameNotFoundException e1 )
    {
      throw new BackendlessException( e1 );
    }
    if( appi != null )
    {
      Bundle bundle = appi.metaData;
      if( bundle == null || bundle.get( "GCMSenderId" ) == null )
        throw new BackendlessException( ExceptionMessage.GCM_SENDER_ID_NOT_DECLARED );
      gcmSenderId = bundle.get( "GCMSenderId" ).toString();
    }
    else
    {
      throw new BackendlessException( ExceptionMessage.CAN_NOT_RETRIEVE_RECEIVER_INFORMATION_FROM_MANIGEST );
    }
  }

  private static boolean receiverExtendsPushBroadcast( ActivityInfo receiver )
  {
    try
    {
      Object receiverObj = Class.forName( receiver.name ).newInstance();
      return receiverObj instanceof BackendlessPushBroadcastReceiver;
    }
    catch( InstantiationException e )
    {
      e.printStackTrace();
    }
    catch( IllegalAccessException e )
    {
      e.printStackTrace();
    }
    catch( ClassNotFoundException e )
    {
      e.printStackTrace();
    }
    return false;
  }

  private static ActivityInfo[] getReceivers( PackageManager packageManager, String packageName )
  {
    // check receivers
    PackageInfo receiversInfo;
    try
    {
      receiversInfo = packageManager.getPackageInfo( packageName, PackageManager.GET_RECEIVERS );
    }
    catch( PackageManager.NameNotFoundException e )
    {
      throw new IllegalStateException( "Could not get receivers for package " + packageName );
    }

    if( receiversInfo == null )
    {
      throw new BackendlessException( ExceptionMessage.NO_RECEIVER_PRESENTS );
    }
    ActivityInfo[] receivers = receiversInfo.receivers;
    return receivers;
  }

  private void checkPollingInterval( int pollingInterval )
  {
    if( pollingInterval < 0 )
      throw new IllegalArgumentException( ExceptionMessage.WRONG_POLLING_INTERVAL );
  }

  private String subscribeSyncForPush( final String channelName, final SubscriptionOptions subscriptionOptions )
                  throws BackendlessException
  {
    checkChannelName( channelName );

    subscriptionOptions.setDeviceId( Messaging.DEVICE_ID );

    if( gcmSenderId() == null )
    {
      throw new BackendlessException( ExceptionMessage.GCM_SENDER_ID_NOT_DECLARED );
    }

    return subscribeDeviceSyncForPush( gcmSenderId(), channelName );

  }

  private String subscribeSyncForPolling( final String channelName, final SubscriptionOptions subscriptionOptions )
                  throws BackendlessException
  {
    checkChannelName( channelName );

    subscriptionOptions.setDeviceId( Messaging.DEVICE_ID );
    subscriptionOptions.setDeliveryMethod( DeliveryMethodEnum.POLL );
    final String result = "";

    new AsyncTask<Void, Void, RuntimeException>()
    {
      @Override
      protected RuntimeException doInBackground( Void... params )
      {
        try
        {
          result.concat( (String) Invoker
                          .invokeSync( MESSAGING_MANAGER_SERVER_ALIAS, "subscribeForPollingAccess",
                                          new Object[]
                          { Backendless.getApplicationId(), Backendless.getVersion(), channelName, subscriptionOptions,
                              new DeviceRegistration() } ) );
          return null;
        }
        catch( RuntimeException t )
        {
          return t;
        }
      }
    }.execute();

    return result;
  }

  private void subscribeAsyncForPolling( final String channelName, final SubscriptionOptions subscriptionOptions,
                                         final AsyncCallback<String> responder )
  {
    try
    {
      checkChannelName( channelName );
      subscriptionOptions.setDeviceId( Messaging.DEVICE_ID );

      subscriptionOptions.setDeliveryMethod( DeliveryMethodEnum.POLL );
      Invoker.invokeAsync( MESSAGING_MANAGER_SERVER_ALIAS, "subscribeForPollingAccess", new Object[]
      { Backendless.getApplicationId(), Backendless.getVersion(), channelName, subscriptionOptions,
          new DeviceRegistration() }, responder );

    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  // By channel name
  public Subscription subscribe( String channelName, AsyncCallback<List<Message>> subscriptionResponder )
                  throws BackendlessException
  {
    return subscribe( channelName, subscriptionResponder, null, 0 );
  }

  public Subscription subscribe( String channelName, int pollingInterval,
                                 AsyncCallback<List<Message>> subscriptionResponder ) throws BackendlessException
  {
    return subscribe( channelName, subscriptionResponder, null, pollingInterval );
  }

  public Subscription subscribe( String channelName, AsyncCallback<List<Message>> subscriptionResponder,
                                 SubscriptionOptions subscriptionOptions ) throws BackendlessException
  {
    return subscribe( channelName, subscriptionResponder, subscriptionOptions, 0 );
  }

  public Subscription subscribe( int pollingInterval, AsyncCallback<List<Message>> subscriptionResponder )
                  throws BackendlessException
  {
    return subscribe( DEFAULT_CHANNEL_NAME, subscriptionResponder, null, pollingInterval );
  }

  public Subscription subscribe( AsyncCallback<List<Message>> subscriptionResponder,
                                 SubscriptionOptions subscriptionOptions ) throws BackendlessException
  {
    return subscribe( DEFAULT_CHANNEL_NAME, subscriptionResponder, subscriptionOptions, 0 );
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

      checkPollingInterval( pollingInterval );
      if( subscriptionOptions == null )
        subscriptionOptions = defaultSubscriptionOptions;

      AsyncCallback<String> serverCallback = new PushSubscriptionCallback( subscriptionResponder, pollingInterval,
                      channelName, responder );
      if( isPushPubSub() )
      {
        subscribeAsyncForPush( channelName, subscriptionOptions, serverCallback );
      }
      else
      {
        subscribeAsyncForPolling( channelName, subscriptionOptions, serverCallback );
      }

    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  private void subscribeAsyncForPush( final String channelName, final SubscriptionOptions subscriptionOptions,
                                      final AsyncCallback<String> callback )
  {
    try
    {
      this.options = subscriptionOptions;
      checkChannelName( channelName );
      subscribeDevice( gcmSenderId(), channelName, callback );
    }
    catch( Throwable e )
    {
      if( callback != null )
        callback.handleFault( new BackendlessFault( e ) );
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

    Object[] result = (Object[]) Invoker.invokeSync( MESSAGING_MANAGER_SERVER_ALIAS, "pollMessages", new Object[]
    { Backendless.getApplicationId(), Backendless.getVersion(), channelName, subscriptionId } );

    return result.length == 0 ? new ArrayList<Message>() : Arrays.asList( (Message[]) result );
  }

  protected void pollMessages( String channelName, String subscriptionId, final AsyncCallback<List<Message>> responder )
  {
    try
    {
      checkChannelName( channelName );

      if( subscriptionId == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_SUBSCRIPTION_ID );

      Invoker.invokeAsync( MESSAGING_MANAGER_SERVER_ALIAS, "pollMessages", new Object[]
      { Backendless.getApplicationId(), Backendless.getVersion(), channelName, subscriptionId },
                      new AsyncCallback<Object[]>()
                      {
                        @Override
                        public void handleResponse( Object[] response )
                        {
                          if( responder != null )
                            responder.handleResponse( response.length == 0 ? new ArrayList<Message>() : Arrays
                                            .asList( (Message[]) response ) );
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

    Invoker.invokeSync( EMAIL_MANAGER_SERVER_ALIAS, "send", new Object[]
    { Backendless.getApplicationId(), Backendless.getVersion(), subject, bodyParts, recipients, attachments } );
  }

  public void sendTextEmail( String subject, String messageBody, List<String> recipients,
                             final AsyncCallback<Void> responder )
  {
    sendEmail( subject, new BodyParts( messageBody, null ), recipients, new ArrayList<String>(), responder );
  }

  public void sendTextEmail( String subject, String messageBody, String recipient, final AsyncCallback<Void> responder )
  {
    sendEmail( subject, new BodyParts( messageBody, null ), Arrays.asList( recipient ), new ArrayList<String>(),
                    responder );
  }

  public void sendHTMLEmail( String subject, String messageBody, List<String> recipients,
                             final AsyncCallback<Void> responder )
  {
    sendEmail( subject, new BodyParts( null, messageBody ), recipients, new ArrayList<String>(), responder );
  }

  public void sendHTMLEmail( String subject, String messageBody, String recipient, final AsyncCallback<Void> responder )
  {
    sendEmail( subject, new BodyParts( null, messageBody ), Arrays.asList( recipient ), new ArrayList<String>(),
                    responder );
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

      Invoker.invokeAsync( EMAIL_MANAGER_SERVER_ALIAS, "send", new Object[]
      { Backendless.getApplicationId(), Backendless.getVersion(), subject, bodyParts, recipients, attachments },
                      responder );
    }
    catch( Throwable e )
    {
      if( responder != null )
        responder.handleFault( new BackendlessFault( e ) );
    }
  }

  public List<Subscription> getSubscriptions()
  {
    final List<Subscription> subscriptionList = new ArrayList<Subscription>();
    Invoker.invokeAsync( MESSAGING_MANAGER_SERVER_ALIAS, "getSubscriptions",
                    new Object[] {}, new AsyncCallback<List<Subscriber>>()
                    {
                      @Override
                      public void handleResponse( List<Subscriber> subscribers )
                      {
                        if( subscribers != null )
                        {
                          for( Subscriber subscriber : subscribers )
                          {
                            Subscription subscription = new Subscription( subscriber.getDSId(), subscriber
                                            .getDestination().getName() );
                            subscriptionList.add( subscription );
                          }
                        }

                      }

                      @Override
                      public void handleFault( BackendlessFault fault )
                      {
                        // TODO Auto-generated method stub

                      }
                    } );
    return subscriptionList;
  }

  public Subscription getSubscription( String channelName )
  {
    return subscriptions.get( channelName );
  }


  public void setSubscription( String channelName, Subscription subscription )
  {
    subscriptions.put( channelName, subscription );
  }

  public AsyncCallback<Void> getDeviceRegistrationCallback()
  {
    return deviceRegistrationCallback;
  }

  public static AbstractRegistrar getRegistrar()
  {
    return registrar;
  }

  public AsyncCallback<String> getDeviceSubscriptionCallback()
  {
    return deviceSubscriptionCallback;
  }
}