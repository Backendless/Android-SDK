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

package com.backendless;

import android.content.Context;
import android.content.Intent;
import com.backendless.exceptions.ExceptionMessage;
import com.backendless.geo.LocationTracker;
import com.backendless.io.BackendlessUserFactory;
import com.backendless.io.BackendlessUserWriter;
import com.backendless.io.DoubleWriter;
import com.backendless.persistence.BackendlessSerializer;
import com.backendless.persistence.RealmSerializer;
import com.backendless.persistence.local.UserIdStorageFactory;
import com.backendless.persistence.local.UserTokenStorageFactory;
import weborb.config.ORBConfig;
import weborb.util.ObjectFactories;
import weborb.util.log.ILoggingConstants;
import weborb.util.log.Log;
import weborb.writer.MessageWriter;

import java.util.HashMap;
import java.util.Map;

public final class Backendless
{
  public static final FootprintsManager FootprintsManager = com.backendless.FootprintsManager.getInstance();
  public static final UserService UserService = com.backendless.UserService.getInstance();
  public static final Persistence Persistence = com.backendless.Persistence.getInstance();
  public static final Persistence Data = com.backendless.Persistence.getInstance();
  public static final Messaging Messaging = com.backendless.Messaging.getInstance();
  public static final Geo Geo = com.backendless.Geo.getInstance();

  public static final Files Files = com.backendless.Files.getInstance();
  public static final Commerce Commerce = com.backendless.Commerce.getInstance();
  public static final Events Events = com.backendless.Events.getInstance();
  public static final Cache Cache = com.backendless.Cache.getInstance();
  public static final Counters Counters = com.backendless.Counters.getInstance();
  public static final CustomService CustomService = com.backendless.CustomService.getInstance();
  public static final Logging Logging = com.backendless.Logging.getInstance();
  public static Media Media;
  private static String url = "https://api.backendless.com";
  private static final BackendlessPrefs prefs;
  private static Boolean isAndroid;

  private Backendless()
  {
  }

  public static boolean isAndroid()
  {
    if( isAndroid == null )
    {
      try
      {
        Class.forName( "android.os.Handler" );
        isAndroid = true;
      }
      catch ( ClassNotFoundException e )
      {
        isAndroid = false;
      }
    }

    return isAndroid;
  }

  static
  {
    ORBConfig.getORBConfig();
    Log.removeLogger( ILoggingConstants.DEFAULT_LOGGER );
    prefs = BackendlessPrefsFactory.create( isAndroid() );
    if( isAndroid )
      Media = com.backendless.Media.getInstance();
  }

  /**
   * Initializes the Backendless API and all Backendless dependencies. This is the first step in using the client API.
   * <p>
   * There is a low probability for internal API data to be cleared by the garbage collector.
   * In this case, an exception or fault, thrown by any of Backendless API methods, will contain 904 error code.
   *
   * @param applicationId a Backendless application ID, which could be retrieved at the Backendless console
   * @param secretKey     a Backendless application secret key, which could be retrieved at the Backendless console
   * @param version       identifies the version of the application. A version represents a snapshot of the configuration settings, set of schemas, user properties, etc.
   */
  public static void initApp( String applicationId, String secretKey, String version )
  {
    if( isAndroid() )
      throw new IllegalArgumentException( ExceptionMessage.NULL_CONTEXT );

    initApp( null, applicationId, secretKey, version );
  }

  public static void initApp( Object context, final String applicationId, final String secretKey, final String version )
  {
    if( isAndroid() && context == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_CONTEXT );

    if( applicationId == null || applicationId.equals( "" ) )
      throw new IllegalArgumentException( ExceptionMessage.NULL_APPLICATION_ID );

    if( secretKey == null || secretKey.equals( "" ) )
      throw new IllegalArgumentException( ExceptionMessage.NULL_SECRET_KEY );

    if( version == null || version.equals( "" ) )
      throw new IllegalArgumentException( ExceptionMessage.NULL_VERSION );

    HeadersManager.cleanHeaders();
    MessageWriter.addTypeWriter( BackendlessUser.class, new BackendlessUserWriter() );
    MessageWriter.addTypeWriter( Double.class, new DoubleWriter() );
    ObjectFactories.addArgumentObjectFactory( BackendlessUser.class.getName(), new BackendlessUserFactory() );
    ContextHandler.setContext( context );
    prefs.onCreate( context );
    prefs.initPreferences( applicationId, secretKey, version );

    if( isAndroid )
    {
      Context appContext = ( (Context) context ).getApplicationContext();
      UserTokenStorageFactory.instance().init( appContext );
      UserIdStorageFactory.instance().init( appContext );
    }

    if( isCodeRunner() )
      return;

    String userToken = UserTokenStorageFactory.instance().getStorage().get();

    if( userToken != null && !userToken.equals( "" ) )
      HeadersManager.getInstance().addHeader( HeadersManager.HeadersEnum.USER_TOKEN_KEY, userToken );

    if( isAndroid && LocationTracker.getInstance() == null )
    {
      Intent intent = new Intent( ( (Context) context ).getApplicationContext(), LocationTracker.class );
      ( (Context) context ).getApplicationContext().startService( intent );
    }

    // check if Realm is present in classpath
    try
    {
      Class realmObjectClass = Class.forName( "io.realm.RealmObject" );
      BackendlessSerializer.addSerializer( realmObjectClass, new RealmSerializer() );
    }
    catch( Throwable t )
    {

    }
  }

  public static void setUIState( String state )
  {
    if( prefs == null )
      throw new IllegalStateException( ExceptionMessage.NOT_INITIALIZED );

    if( state == null || state.equals( "" ) )
      prefs.cleanHeaders();
    else
    {
      Map<String, String> headers = prefs.getHeaders();
      if( headers == null )
        headers = new HashMap<String, String>();
      headers.put( HeadersManager.HeadersEnum.UI_STATE.getHeader(), state );
      prefs.setHeaders( headers );
    }
  }

  public static String getApplicationId()
  {
    if( prefs == null )
      throw new IllegalStateException( ExceptionMessage.NOT_INITIALIZED );

    return prefs.getApplicationId();
  }

  public static String getSecretKey()
  {
    if( prefs == null )
      throw new IllegalStateException( ExceptionMessage.NOT_INITIALIZED );

    return prefs.getSecretKey();
  }

  public static String getVersion()
  {
    if( prefs == null )
      throw new IllegalStateException( ExceptionMessage.NOT_INITIALIZED );

    return prefs.getVersion();
  }

  protected static Map<String, String> getHeaders()
  {
    if( prefs == null )
      throw new IllegalStateException( ExceptionMessage.NOT_INITIALIZED );

    return prefs.getHeaders();
  }

  public static String getUrl()
  {
    return url;
  }

  public static void setUrl( String url )
  {
    Backendless.url = url;
  }

  public static boolean isCodeRunner()
  {
    return Thread.currentThread().getThreadGroup().getName().equals( "CodeRunner secure group" );
  }
}
