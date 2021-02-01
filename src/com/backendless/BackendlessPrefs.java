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
  protected String customDomain;

  public BackendlessPrefs()
  {
  }

  public void initPreferences( String applicationId, String apiKey )
  {
    this.authKeys = new AuthKeys( applicationId, apiKey );
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

    final AuthKeys authKeys = getAuthKeys();
    return authKeys == null ? null : authKeys.getApplicationId();
  }

  public String getApiKey()
  {
    return getAuthKeys().getApiKey();
  }

  public synchronized Map getHeaders()
  {
    return headers;
  }

  private synchronized AuthKeys getAuthKeys()
  {
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

  public String getCustomDomain()
  {
    return customDomain;
  }

  public BackendlessPrefs setCustomDomain( String customDomain )
  {
    this.customDomain = customDomain;
    return this;
  }
}
