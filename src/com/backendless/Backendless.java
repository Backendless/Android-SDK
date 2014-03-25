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

import com.backendless.exceptions.ExceptionMessage;
import com.backendless.io.BackendlessUserWriter;
import weborb.config.ORBConfig;
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
  private static String url = "https://api.backendless.com";
  private static IBackendlessService backendlessService;
  private static IBackendlessService.Init backendlessInitService;
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
      catch( ClassNotFoundException e )
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

    if( isAndroid() )
    {
      backendlessService = AndroidService.recoverService();
      backendlessInitService = new AndroidService.Init();
    }
    else
      backendlessInitService = new JavaService.Init();
  }

  /**
   * Initializes the Backendless API and all Backendless dependencies. This is the first step in using the client API.
   * <p/>
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
    backendlessInitService.initService( context, new IServiceCreatedCallback()
    {
      @Override
      public void onServiceConnected( IBackendlessService result )
      {
        backendlessService = result;
        backendlessService.setAuthKeys( applicationId, secretKey, version );
      }
    } );

    /* Set authKeys temporally if backendlessService is not initialized yet */
    /* Needed for android only */
    if( backendlessService instanceof StubBackendlessService )
      backendlessService.setAuthKeys( applicationId, secretKey, version );
  }

  public static void setUIState( String state )
  {
    if( backendlessService == null )
      throw new IllegalStateException( ExceptionMessage.NOT_INITIALIZED );

    if( state == null || state.equals( "" ) )
      backendlessService.cleanHeaders();
    else
    {
      Map<String, String> headers = backendlessService.getHeaders();
      if( headers == null )
        headers = new HashMap<String, String>();
      headers.put( HeadersManager.HeadersEnum.UI_STATE.getHeader(), state );
      backendlessService.setHeaders( headers );
    }
  }

  protected static String getApplicationId()
  {
    if( backendlessService == null )
      throw new IllegalStateException( ExceptionMessage.NOT_INITIALIZED );

    return backendlessService.getApplicationId();
  }

  protected static String getSecretKey()
  {
    if( backendlessService == null )
      throw new IllegalStateException( ExceptionMessage.NOT_INITIALIZED );

    return backendlessService.getSecretKey();
  }

  protected static String getVersion()
  {
    if( backendlessService == null )
      throw new IllegalStateException( ExceptionMessage.NOT_INITIALIZED );

    return backendlessService.getVersion();
  }

  protected static Map<String, String> getHeaders()
  {
    if( backendlessService == null )
      throw new IllegalStateException( ExceptionMessage.NOT_INITIALIZED );

    return backendlessService.getHeaders();
  }

  public static String getUrl()
  {
    return url;
  }

  public static void setUrl( String url )
  {
    Backendless.url = url;
  }
}
