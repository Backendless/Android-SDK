package com.backendless;

import java.util.Map;

/**
 * Created by dzidzoiev on 11/8/15.
 */
class BackendlessPrefs
{
  protected AuthKeys authKeys;
  protected Map<String, String> headers;

  public BackendlessPrefs()
  {
  }

  public void initPreferences( String applicationId, String secretKey, String version )
  {
    AuthKeys authKeys = new AuthKeys( applicationId, secretKey, version );
    this.authKeys = authKeys;
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

  public String getVersion()
  {
    return getAuthKeys().getVersion();
  }

  public synchronized Map getHeaders()
  {
    return headers;
  }

  private synchronized AuthKeys getAuthKeys()
  {
    return authKeys;
  }

}
