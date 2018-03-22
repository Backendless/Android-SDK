package com.backendless;

import com.backendless.exceptions.ExceptionMessage;

import java.util.Map;

/**
 * Created by dzidzoiev on 11/8/15.
 */
class BackendlessPrefs
{
  protected AuthKeys authKeys;
  protected Map<String, String> headers;
  protected String url;

  public BackendlessPrefs()
  {
  }

  public void initPreferences( String applicationId, String secretKey )
  {
    this.authKeys = new AuthKeys( applicationId, secretKey );
  }

  public void setHeaders( Map<String, String> headers )
  {
    this.headers = headers;
  }

  public synchronized void cleanHeaders()
  {
    headers = null;
  }

  /**
   * Should be called before init
   *
   * @param context - mandatory for android service
   */
  public void onCreate( Object context )
  {

  }

  public String getApplicationId()
  {
    return getAuthKeys().getApplicationId();
  }

  public String getSecretKey()
  {
    return getAuthKeys().getSecretKey();
  }

  public synchronized Map getHeaders()
  {
    return headers;
  }

  private synchronized AuthKeys getAuthKeys()
  {
    if( authKeys == null)
      throw new IllegalStateException( ExceptionMessage.NOT_INITIALIZED );
    
    return authKeys;
  }

  boolean isAuthKeysExist()
  {
    return authKeys != null;
  }

  public void setUrl( String url )
  {
    this.url = url;
  }

  public String getUrl()
  {
    if( this.url == null)
      throw new IllegalStateException( ExceptionMessage.NOT_INITIALIZED );

    return this.url;
  }
}
