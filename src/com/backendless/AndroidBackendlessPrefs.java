package com.backendless;

import android.content.Context;
import android.content.SharedPreferences;
import com.backendless.utils.AndroidIO;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

/**
 * Created by dzidzoiev on 11/8/15.
 */
class AndroidBackendlessPrefs extends BackendlessPrefs
{
  public static final String PREFS_NAME = "BackendlessPrefs";

  private SharedPreferences sharedPreferences;

  public AndroidBackendlessPrefs()
  {
    super();
  }

  @Override
  public void initPreferences( String applicationId, String secretKey, String version )
  {
    super.initPreferences( applicationId, secretKey, version );
    saveAuthKeysToPreferences( authKeys );
  }

  @Override
  public void setHeaders( Map<String, String> headers )
  {
    super.setHeaders( headers );
    saveHeadersToPreferences( headers );
  }

  @Override
  public void cleanHeaders()
  {
    super.cleanHeaders();
    cleanHeadersFromPreferences();
  }

  @Override
  public void onCreate( Object context )
  {
    super.onCreate( context );
    this.sharedPreferences = ( (Context) context ).getSharedPreferences( PREFS_NAME, Context.MODE_PRIVATE );
  }

  @Override
  public String getApplicationId()
  {
    return getAuthKeys().getApplicationId();
  }

  @Override
  public String getSecretKey()
  {
    return getAuthKeys().getSecretKey();
  }

  @Override
  public String getVersion()
  {
    return getAuthKeys().getVersion();
  }

  @Override
  public synchronized Map getHeaders()
  {
    if( headers == null )
      restoreHeadersFromPreferences();

    return headers;
  }

  @Override
  public void set( String key, String value )
  {
    super.set( key, value );
    saveToPrefs( key, value );
  }

  @Override
  public String get( String key )
  {
    String value = super.get( key );
    if( value == null )
      return getFromPrefs( key );
    return value;
  }

  private boolean restoreHeadersFromPreferences()
  {
    String rawHeaders = sharedPreferences.getString( Type.HEADERS.name64(), null );

    if( rawHeaders != null )
    {
      try
      {
        headers = AndroidIO.mapFromString( rawHeaders );
      }
      catch ( Exception e )
      {
        return false;
      }

      return true;
    }

    return false;
  }

  private synchronized AuthKeys getAuthKeys()
  {
    if( authKeys == null )
      restoreAuthKeysFromPreferences();

    return authKeys;
  }

  private boolean restoreAuthKeysFromPreferences()
  {
    String applicationId = sharedPreferences.getString( Type.APPLICATION_ID.name64(), null );
    String secretKey = sharedPreferences.getString( Type.SECRET_KEY.name64(), null );
    String version = sharedPreferences.getString( Type.VERSION.name64(), null );

    if( applicationId != null && secretKey != null && version != null )
    {
      authKeys = new AuthKeys( applicationId, secretKey, version );
      return true;
    }

    return false;
  }

  private void saveHeadersToPreferences( Map<String, String> headers )
  {
    try
    {
      SharedPreferences.Editor editor = sharedPreferences.edit();
      String rawHeaders = AndroidIO.mapToString( headers );
      editor.putString( Type.APPLICATION_ID.name64(), rawHeaders );
      editor.commit();
    }
    catch ( IOException e )
    {
      //ignored
    }
  }

  private void saveAuthKeysToPreferences( AuthKeys authKeys )
  {
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString( Type.APPLICATION_ID.name64(), authKeys.getApplicationId() );
    editor.putString( Type.SECRET_KEY.name64(), authKeys.getSecretKey() );
    editor.putString( Type.VERSION.name64(), authKeys.getVersion() );
    editor.commit();
  }

  private void cleanHeadersFromPreferences()
  {
    if( sharedPreferences.contains( Type.HEADERS.name64() ) )
    {
      SharedPreferences.Editor editor = sharedPreferences.edit();
      editor.remove( Type.HEADERS.name64() );
      editor.commit();
    }
  }

  private void saveToPrefs( String key, String value )
  {
    String key64 = toBase64(key);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString(key64, value );
    editor.commit();
  }

  private String getFromPrefs(String key)
  {
    String key64 = toBase64(key);
    return sharedPreferences.getString( key64, null );
  }

  private static String toBase64( String key )
  {
    return UUID.nameUUIDFromBytes( key.getBytes() ).toString();
  }

  enum Type
  {
    APPLICATION_ID,
    SECRET_KEY,
    VERSION,
    HEADERS;

    String name64()
    {
      return UUID.nameUUIDFromBytes( this.name().getBytes() ).toString();
    }
  }

}
