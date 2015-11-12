package com.backendless;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dzidzoiev on 11/8/15.
 */
class BackendlessPrefs
{
  protected AuthKeys authKeys;
  protected Map<String, String> headers;
  private Map<String, String> prefsStore;

  public BackendlessPrefs()
  {
    prefsStore = Collections.synchronizedMap( new HashMap<String, String>() );
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

  public void set( String key, String value )
  {
    prefsStore.put( key, value );
  }

  public String get( String key )
  {
    return prefsStore.get( key );
  }

}
