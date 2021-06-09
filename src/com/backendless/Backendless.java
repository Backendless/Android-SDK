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

import com.backendless.exceptions.BackendlessException;
import com.backendless.exceptions.ExceptionMessage;
import com.backendless.files.BackendlessFile;
import com.backendless.files.BackendlessFileFactory;
import com.backendless.persistence.BackendlessGeometryFactory;
import com.backendless.persistence.BackendlessGeometryWriter;
import com.backendless.io.BackendlessUserFactory;
import com.backendless.io.BackendlessUserWriter;
import com.backendless.io.DoubleWriter;
import com.backendless.persistence.BackendlessSerializer;
import com.backendless.persistence.Geometry;
import com.backendless.persistence.GeometryDTO;
import com.backendless.persistence.JsonDTO;
import com.backendless.persistence.JsonDTOAdaptingType;
import com.backendless.persistence.LineString;
import com.backendless.persistence.Point;
import com.backendless.persistence.Polygon;
import com.backendless.persistence.QueryOptions;
import com.backendless.persistence.RealmSerializer;
import com.backendless.persistence.local.UserIdStorageFactory;
import com.backendless.persistence.local.UserTokenStorageFactory;
import com.backendless.rt.RTService;
import com.backendless.rt.RTServiceImpl;
import com.backendless.util.JSONUtil;
import com.backendless.utils.JSONConverterWeborbImpl;
import org.json.JSONException;
import org.json.JSONObject;
import weborb.ORBConstants;
import weborb.config.ORBConfig;
import weborb.types.Types;
import weborb.util.ObjectFactories;
import weborb.util.log.ILoggingConstants;
import weborb.util.log.Log;
import weborb.writer.IProtocolFormatter;
import weborb.writer.ITypeWriter;
import weborb.writer.MessageWriter;
import weborb.writer.amf.AmfV3Formatter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public final class Backendless
{
  private final static String DEFAULT_API_ENDPOINT = "https://api.backendless.com";
  private static String url;
  private static final boolean isAndroid = isAndroidEnvironment();
  // do not remove or change 'isCoderunner', because it is used in CodeRunner initialization process
  private static boolean isCodeRunner = false;
  private static final BackendlessPrefs prefs;

  public static final FootprintsManager FootprintsManager = com.backendless.FootprintsManager.getInstance();
  public static final UserService UserService = com.backendless.UserService.getInstance();
  public static final Persistence Persistence = com.backendless.Persistence.getInstance();
  public static final Persistence Data = com.backendless.Persistence.getInstance();
  public static final Messaging Messaging = com.backendless.Messaging.getInstance();

  public static final Files Files = com.backendless.Files.getInstance();
  public static final Commerce Commerce = com.backendless.Commerce.getInstance();
  public static final Events Events = com.backendless.Events.getInstance();
  public static final Cache Cache = com.backendless.Cache.getInstance();
  public static final Counters Counters = com.backendless.Counters.getInstance();
  public static final CustomService CustomService = com.backendless.CustomService.getInstance();
  public static final Logging Logging = com.backendless.Logging.getInstance();
  public static final RTService RT = new RTServiceImpl();
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
    {
      prefs.onCreate( ContextHandler.getAppContext() );
    }

    JSONUtil.setJsonConverter( new JSONConverterWeborbImpl() );

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
        if( queryOptions.getRelationsPageSize() != null )
          queryOptionsMap.put( "relationsPageSize", queryOptions.getRelationsPageSize() );

        MessageWriter.writeObject( queryOptionsMap, iProtocolFormatter );
      }

      @Override
      public boolean isReferenceableType()
      {
        return false;
      }
    } );

    try
    {
      if( !initialized && prefs.getApplicationId() != null && prefs.getApiKey() != null )
        Backendless.initApplicationFromProperties( ContextHandler.getAppContext() );
    }
    catch( IllegalStateException e )
    {
      if( !ExceptionMessage.NOT_INITIALIZED.equals( e.getMessage() ) )
        throw e;
    }
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
   * @param apiKey     a Backendless application api key, which could be retrieved at the Backendless console
   */
  public static void initApp( String applicationId, String apiKey )
  {
    initApp( null, applicationId, apiKey );
  }

  /**
   * Initializes the Backendless API and all Backendless dependencies. This is the first step in using the client API.
   * <p>
   * There is a low probability for internal API data to be cleared by the garbage collector.
   * In this case, an exception or fault, thrown by any of Backendless API methods, will contain 904 error code.
   *
   * @param customDomain custom domain which you setup in Backendless console https://backendless.com/docs/android/mgmt_custom_domain.html
   */
  public static void initApp( String customDomain )
  {
    initApp( (Object) null, customDomain );
  }

  public static boolean isInitialized()
  {
    return initialized;
  }

  public static void initApp( Object context, final String customDomain )
  {
    if( customDomain == null || customDomain.equals( "" ) )
      throw new IllegalArgumentException( "Custom domain cant be null or empty" );

    if( customDomain.startsWith( "http" ) )
      setUrl( customDomain );
    else
      setUrl( "http://" + customDomain );

    URI uri;
    try
    {
      uri = new URI( Backendless.getUrl() );
    }
    catch( URISyntaxException e )
    {
      throw new RuntimeException( e );
    }

    prefs.setCustomDomain( uri.getHost() );

    initApp( context );
  }

  public static void initApp( Object context, final String applicationId, final String apiKey )
  {
    if( applicationId == null || applicationId.equals( "" ) )
      throw new IllegalArgumentException( ExceptionMessage.NULL_APPLICATION_ID );

    if( apiKey == null || apiKey.equals( "" ) )
      throw new IllegalArgumentException( ExceptionMessage.NULL_API_KEY );

    prefs.initPreferences( applicationId, apiKey );

    if( url == null )
      url = discoverApiEndpoint( applicationId );

    prefs.setUrl( url );

    initApp( context );
  }

  private static String discoverApiEndpoint( String applicationId )
  {
    String discoverUrl = DEFAULT_API_ENDPOINT + "/discover-api-endpoint?appId=" + applicationId;

    try
    {
      URL connectionUrl = new URL( discoverUrl );

      HttpURLConnection connection = (HttpURLConnection) connectionUrl.openConnection();
      connection.setRequestMethod( "GET" );

      BufferedReader in = new BufferedReader( new InputStreamReader( connection.getInputStream() ) );
      String inputLine;
      StringBuilder response = new StringBuilder();

      while( ( inputLine = in.readLine() ) != null )
      {
        response.append( inputLine );
      }
      in.close();

      return new JSONObject( response.toString() ).getString( "url" );
    }
    catch( IOException | JSONException e )
    {
      throw new BackendlessException( ExceptionMessage.DISCOVERY_ENDPOINT_EXCEPTION, e.getMessage() );
    }
  }

  private static void initApp( Object context )
  {
    if( isAndroid && context == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_CONTEXT );

    prefs.onCreate( context );
    MessageWriter.addTypeWriter( BackendlessUser.class, new BackendlessUserWriter() );
    MessageWriter.addTypeWriter( Double.class, new DoubleWriter() );
    MessageWriter.addTypeWriter( Geometry.class, new BackendlessGeometryWriter() );
    MessageWriter.addTypeWriter( Point.class, new BackendlessGeometryWriter() );
    MessageWriter.addTypeWriter( LineString.class, new BackendlessGeometryWriter() );
    MessageWriter.addTypeWriter( Polygon.class, new BackendlessGeometryWriter() );
    ObjectFactories.addArgumentObjectFactory( BackendlessUser.class.getName(), new BackendlessUserFactory() );
    ObjectFactories.addArgumentObjectFactory( BackendlessFile.class.getName(), new BackendlessFileFactory() );
    ObjectFactories.addArgumentObjectFactory( GeometryDTO.class.getName(), new BackendlessGeometryFactory() );
    ObjectFactories.addArgumentObjectFactory( Geometry.class.getName(), new BackendlessGeometryFactory() );
    ObjectFactories.addArgumentObjectFactory( Point.class.getName(), new BackendlessGeometryFactory() );
    ObjectFactories.addArgumentObjectFactory( LineString.class.getName(), new BackendlessGeometryFactory() );
    ObjectFactories.addArgumentObjectFactory( Polygon.class.getName(), new BackendlessGeometryFactory() );
    Types.addClientClassMapping( JsonDTO.class.getName(), JsonDTOAdaptingType.class );
    //ObjectFactories.addArgumentObjectFactory( JsonDTO.class.getName(), new BackendlessJsonFactory() );
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
    {
      ThreadPoolService.getThreadPoolExecutor();
      return;
    }

    String userToken = UserTokenStorageFactory.instance().getStorage().get();

    if( userToken != null && !userToken.equals( "" ) )
      HeadersManager.getInstance().addHeader( HeadersManager.HeadersEnum.USER_TOKEN_KEY, userToken );

    // check if Realm is present in classpath
    try
    {
      Class realmObjectClass = Class.forName( "io.realm.RealmObject" );
      BackendlessSerializer.addSerializer( realmObjectClass, new RealmSerializer() );
    }
    catch( Throwable ignore )
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

  public static String getApplicationIdOrDomain()
  {
    if( prefs == null )
      throw new IllegalStateException( ExceptionMessage.NOT_INITIALIZED );

    if( prefs.getApplicationId() != null )
       return prefs.getApplicationId();

    if( prefs.getCustomDomain() != null )
      return prefs.getCustomDomain();

    throw new IllegalStateException( ExceptionMessage.NOT_INITIALIZED );
  }

  public static String getApiKey()
  {
    if( prefs == null )
      throw new IllegalStateException( ExceptionMessage.NOT_INITIALIZED );

    return prefs.getApiKey();
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

    if( prefs != null )
    {
      prefs.setUrl( url );
      Invoker.reinitialize();
    }
  }

  /**
   * @return url to call to backendless
   * If application initialized with appId and api key returns url with the following pattern
   * "http(s)://<backendless-host>/<appId>/<apiKey>/..."
   * Else if application initialized with domain returns url with the following pattern
   * "http(s)://<backendless-host>/..."
   */
  public static String getApplicationUrl()
  {
    final String applicationId = prefs.getApplicationId();
    return applicationId == null
            ? getUrl() + "/api"
            : getUrl() + '/' + applicationId + '/' + getApiKey();
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

    Backendless.initApp( context, prefs.getApplicationId(), prefs.getApiKey() );
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
