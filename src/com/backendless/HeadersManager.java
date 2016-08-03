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

import com.backendless.exceptions.BackendlessException;

import java.util.Hashtable;
import java.util.Map;

public class HeadersManager
{
  private static IHeadersManager headersManager = (Backendless.isAndroid()) ? AndroidHeadersManager.getInstance() : BLHeadersManager.getInstance();

  private static volatile HeadersManager instance = new HeadersManager();

  private HeadersManager()
  {

  }

  public static HeadersManager getInstance()
  {
    return instance;
  }

  static void cleanHeaders()
  {
    headersManager.cleanHeaders();
  }

  public void addHeader( HeadersEnum headersEnum, String value )
  {
    headersManager.addHeader( headersEnum, value );
  }

  public void addHeaders( Map<String, String> headers )
  {
    headersManager.addHeaders( headers );
  }

  public void removeHeader( HeadersEnum headersEnum )
  {
    headersManager.removeHeader( headersEnum );
  }

  public Hashtable<String, String> getHeaders() throws BackendlessException
  {
    return headersManager.getHeaders();
  }

  public void setHeaders( Map<String, String> headers )
  {
    headersManager.setHeaders( headers );
  }

  public String getHeader( HeadersEnum headersEnum ) throws BackendlessException
  {
    return headersManager.getHeader( headersEnum );
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
