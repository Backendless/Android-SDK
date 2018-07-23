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
import com.backendless.files.BackendlessFile;
import com.backendless.files.BackendlessFileFactory;
import com.backendless.geo.LocationTracker;
import com.backendless.io.BackendlessUserFactory;
import com.backendless.io.BackendlessUserWriter;
import com.backendless.io.DoubleWriter;
import com.backendless.persistence.BackendlessSerializer;
import com.backendless.persistence.QueryOptions;
import com.backendless.persistence.RealmSerializer;
import com.backendless.persistence.local.UserIdStorageFactory;
import com.backendless.persistence.local.UserTokenStorageFactory;
import com.backendless.rt.RTService;
import com.backendless.rt.RTServiceImpl;
import weborb.ORBConstants;
import weborb.config.ORBConfig;
import weborb.util.ObjectFactories;
import weborb.util.log.ILoggingConstants;
import weborb.util.log.Log;
import weborb.writer.IProtocolFormatter;
import weborb.writer.ITypeWriter;
import weborb.writer.MessageWriter;
import weborb.writer.amf.AmfV3Formatter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class Backendless
{
  private static String url = "https://api.backendless.com";
  private static final boolean isAndroid = isAndroidEnvironment();
  private static boolean isCodeRunner = false;
  private static final BackendlessPrefs prefs;

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
  public static final RTService RT = new RTServiceImpl();
  public static Media Media;
  private static boolean initialized;

  private Backendless()
  {
  }

  private static boolean isAndroidEnvironment()
  {
    try
    {
      Class.forName( "android.os.Handler" );
      return true;
    }
    catch ( ClassNotFoundException e )
    {
      return false;
    }
  }

  static
  {
    ORBConfig.getORBConfig();
    Log.removeLogger( ILoggingConstants.DEFAULT_LOGGER );
    prefs = BackendlessPrefsFactory.create( isAndroid );
    if( isAndroid )
      Media = com.backendless.Media.getInstance();

    AmfV3Formatter.AddTypeWriter( QueryOptions.class, new ITypeWriter()
    {
      @Override
      public void write( Object o, IProtocolFormatter iProtocolFormatter ) throws IOException
      {
        QueryOptions queryOptions = (QueryOptions) o;
        Map<String, Object> queryOptionsMap = new HashMap<>(  );
        queryOptionsMap.put( ORBConstants.WEBORB_TYPE_NAME.toString(), QueryOptions.class.getSimpleName() );
        queryOptionsMap.put( "sortBy", queryOptions.getSortBy() );
        queryOptionsMap.put( "related", queryOptions.getRelated() );
        if( queryOptions.getRelationsDepth() != null )
          queryOptionsMap.put( "relationsDepth", queryOptions.getRelationsDepth() );

        MessageWriter.writeObject( queryOptionsMap, iProtocolFormatter );
      }

      @Override
      public boolean isReferenceableType()
      {
        return false;
      }
    } );
  }

  public static boolean isAndroid()
  {
    return isAndroid;
  }

  /**
   * Initializes the Backendless API and all Backendless dependencies. This is the first step in using the client API.
   * <p>
   * There is a low probability for internal API data to be cleared by the garbage collector.
   * In this case, an exception or fault, thrown by any of Backendless API methods, will contain 904 error code.
   *
   * @param applicationId a Backendless application ID, which could be retrieved at the Backendless console
   * @param secretKey     a Backendless application secret key, which could be retrieved at the Backendless console
   */
  public static void initApp( String applicationId, String secretKey )
  {
    initApp( null, applicationId, secretKey );
  }

  public static boolean isInitialized()
  {
    return initialized;
  }

  public static void initApp( Object context, final String applicationId, final String secretKey )
  {
    if( isAndroid && context == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_CONTEXT );

    if( applicationId == null || applicationId.equals( "" ) )
      throw new IllegalArgumentException( ExceptionMessage.NULL_APPLICATION_ID );

    if( secretKey == null || secretKey.equals( "" ) )
      throw new IllegalArgumentException( ExceptionMessage.NULL_SECRET_KEY );

    prefs.onCreate( context );
    prefs.initPreferences( applicationId, secretKey );
    prefs.setUrl( url );

    MessageWriter.addTypeWriter( BackendlessUser.class, new BackendlessUserWriter() );
    MessageWriter.addTypeWriter( Double.class, new DoubleWriter() );
    ObjectFactories.addArgumentObjectFactory( BackendlessUser.class.getName(), new BackendlessUserFactory() );
    ObjectFactories.addArgumentObjectFactory( BackendlessFile.class.getName(), new BackendlessFileFactory() );
    ContextHandler.setContext( context );

    HeadersManager.cleanHeaders();
    Invoker.reinitialize();

    if( isAndroid )
    {
      Context appContext = ( (Context) context ).getApplicationContext();
      UserTokenStorageFactory.instance().init( appContext );
      UserIdStorageFactory.instance().init( appContext );
      com.backendless.Messaging.DeviceIdHolder.init( appContext );
    }
    else
    {
      com.backendless.Messaging.DeviceIdHolder.init( );
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
    initialized = true;
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

    if( prefs != null && prefs.isAuthKeysExist() )
    {
      prefs.setUrl( url );
      Invoker.reinitialize();
    }
  }

  public static boolean isCodeRunner()
  {
    return isCodeRunner;
  }

  public static void savePushTemplates( String pushTemplatesAsJson )
  {
    if( !isAndroid )
      return;

    ((AndroidBackendlessPrefs) prefs).savePushTemplate( pushTemplatesAsJson );
  }

  public static String getPushTemplatesAsJson()
  {
    if( !isAndroid )
      return null;

    return ((AndroidBackendlessPrefs) prefs).getPushTemplateAsJson();
  }

  public static void initApplicationFromProperties( Object context )
  {
    prefs.onCreate( context );

    Backendless.initApp( context, prefs.getApplicationId(), prefs.getSecretKey() );
    Backendless.setUrl( prefs.getUrl() );
  }

  public static int getNotificationIdGeneratorInitValue()
  {
    if( !isAndroid )
      return 0;

    return ((AndroidBackendlessPrefs) prefs).getNotificationIdGeneratorInitValue();
  }

  public static void saveNotificationIdGeneratorState( int value )
  {
    ((AndroidBackendlessPrefs) prefs).saveNotificationIdGeneratorState( value );
  }
}
