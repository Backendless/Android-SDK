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
class BackendlessPrefs
{
  public static final String PREFS_NAME = "BackendlessPrefs";

  private final SharedPreferences sharedPreferences;
  private AuthKeys authKeys;
  private Map<String, String> headers;

  public BackendlessPrefs( Context context )
  {
    this.sharedPreferences = context.getSharedPreferences( PREFS_NAME, Context.MODE_PRIVATE );
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
    if( headers == null )
      restoreHeadersFromPreferences();

    return headers;
  }

  private boolean restoreHeadersFromPreferences()
  {
    String rawHeaders = sharedPreferences.getString( IBackendlessService.Type.HEADERS.name64(), null );

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
    editor.putString( IBackendlessService.Type.APPLICATION_ID.name64(), authKeys.getApplicationId() );
    editor.putString( IBackendlessService.Type.SECRET_KEY.name64(), authKeys.getSecretKey() );
    editor.putString( IBackendlessService.Type.VERSION.name64(), authKeys.getVersion() );
    editor.commit();
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
