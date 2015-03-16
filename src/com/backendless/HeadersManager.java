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

import com.backendless.commons.DeviceType;
import com.backendless.exceptions.BackendlessException;
import com.backendless.exceptions.ExceptionMessage;

import java.util.Hashtable;
import java.util.Map;

public class HeadersManager
{
  private static final Object headersLock = new Object();
  private static volatile HeadersManager instance;
  private Hashtable<String, String> headers = new Hashtable<String, String>();

  private HeadersManager()
  {
  }

  public static HeadersManager getInstance() throws BackendlessException
  {
    if( instance == null )
      synchronized( headersLock )
      {
        if( instance == null )
        {
          if( Backendless.getApplicationId() == null || Backendless.getSecretKey() == null )
          {
            throw new IllegalStateException( ExceptionMessage.NOT_INITIALIZED );
          }

          instance = new HeadersManager();
          instance.addHeader( HeadersEnum.APP_ID_NAME, Backendless.getApplicationId() );
          instance.addHeader( HeadersEnum.SECRET_KEY_NAME, Backendless.getSecretKey() );

          if( Backendless.isCodeRunner() )
            instance.addHeader( HeadersEnum.APP_TYPE_NAME, DeviceType.BL.name() );
          else
            instance.addHeader( HeadersEnum.APP_TYPE_NAME, DeviceType.ANDROID.name() );

          instance.addHeader( HeadersEnum.API_VERSION, "1.0" );
          instance.addHeaders( Backendless.getHeaders() );
        }
      }

    return instance;
  }

  static void cleanHeaders()
  {
    synchronized( headersLock )
    {
      instance = null;
    }
  }

  public void addHeader( HeadersEnum headersEnum, String value )
  {
    synchronized( headersLock )
    {
      headers.put( headersEnum.getHeader(), value );
    }
  }

  public void addHeaders( Map<String, String> headers )
  {
    if( headers == null || headers.isEmpty() )
      return;

    synchronized( headersLock )
    {
      this.headers.putAll( headers );
    }
  }

  public void removeHeader( HeadersEnum headersEnum )
  {
    synchronized( headersLock )
    {
      headers.remove( headersEnum.getHeader() );
    }
  }

  public Hashtable<String, String> getHeaders() throws BackendlessException
  {
    synchronized( headersLock )
    {
      if( headers == null || headers.isEmpty() )
        throw new IllegalStateException( ExceptionMessage.NOT_INITIALIZED );

      return headers;
    }
  }

  public void setHeaders( Map<String, String> headers )
  {
    synchronized( headersLock )
    {
      this.headers.putAll( headers );
    }
  }

  public String getHeader( HeadersEnum headersEnum ) throws BackendlessException
  {
    synchronized( headersLock )
    {
      if( headers == null || headers.isEmpty() )
        throw new IllegalStateException( ExceptionMessage.NOT_INITIALIZED );

      String header = headers.get( headersEnum );

      if( header == null )
        header = headers.get( headersEnum.getHeader() );

      return header;
    }
  }

  public enum HeadersEnum
  {
    USER_TOKEN_KEY( "user-token" ), LOGGED_IN_KEY( "logged-in" ), SESSION_TIME_OUT_KEY( "session-time-out" ),
    APP_ID_NAME( "application-id" ), SECRET_KEY_NAME( "secret-key" ), APP_TYPE_NAME( "application-type" ),
    API_VERSION( "api-version" ), UI_STATE( "uiState" );

    private String header;

    HeadersEnum( String header )
    {
      this.header = header;
    }

    String getHeader()
    {
      return header;
    }
  }
}
