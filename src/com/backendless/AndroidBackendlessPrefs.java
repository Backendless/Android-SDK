package com.backendless;

import android.content.Context;
import android.content.SharedPreferences;
import com.backendless.exceptions.ExceptionMessage;
import com.backendless.utils.AndroidIO;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;


class AndroidBackendlessPrefs extends BackendlessPrefs
{
  public static final String PREFS_NAME = "BackendlessPrefs";

  private SharedPreferences sharedPreferences;

  public AndroidBackendlessPrefs()
  {
    super();
  }

  public void initPreferences( String applicationId, String apiKey )
  {
    super.initPreferences( applicationId, apiKey );
    saveAuthKeysToPreferences( authKeys );
  }

  public void setHeaders( Map<String, String> headers )
  {
    super.setHeaders( headers );
    saveHeadersToPreferences( headers );
  }

  public void cleanHeaders()
  {
    super.cleanHeaders();
    cleanHeadersFromPreferences();
  }

  public void onCreate( Object context )
  {
    super.onCreate( context );
    this.sharedPreferences = ( (Context) context ).getSharedPreferences( PREFS_NAME, Context.MODE_PRIVATE );
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

  public void setUrl( String url )
  {
    if( sharedPreferences == null )
      throw new IllegalStateException( ExceptionMessage.NOT_INITIALIZED );

    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString( Type.URL_KEY.name64(), url );
    editor.commit();

    this.url = url;
  }

  public String getUrl()
  {
    if( sharedPreferences == null )
      throw new IllegalStateException( ExceptionMessage.NOT_INITIALIZED );

    if( this.url == null )
      this.url = sharedPreferences.getString( Type.URL_KEY.name64(), Backendless.getUrl() );

    return this.url;
  }

  public AndroidBackendlessPrefs setCustomDomain( String customDomain )
  {
    if( sharedPreferences == null )
      throw new IllegalStateException( ExceptionMessage.NOT_INITIALIZED );

    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString( Type.CUSTOM_DOMAIN_KEY.name64(), customDomain );
    editor.commit();

    this.customDomain = customDomain;
    return this;
  }

  public String getCustomDomain()
  {
    if( sharedPreferences == null )
      throw new IllegalStateException( ExceptionMessage.NOT_INITIALIZED );

    if( this.customDomain == null )
      this.customDomain = sharedPreferences.getString( Type.CUSTOM_DOMAIN_KEY.name64(), Backendless.getUrl() );

    return this.customDomain;
  }

  public synchronized Map getHeaders()
  {
    if( headers == null )
      restoreHeadersFromPreferences();

    return headers;
  }

  private boolean restoreHeadersFromPreferences()
  {
    if( sharedPreferences == null )
      throw new IllegalStateException( ExceptionMessage.NOT_INITIALIZED );

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

    if( authKeys == null && getCustomDomain() == null )
      throw new IllegalStateException( ExceptionMessage.NOT_INITIALIZED );

    return authKeys;
  }

  private boolean restoreAuthKeysFromPreferences()
  {
    if( sharedPreferences == null )
      throw new IllegalStateException( ExceptionMessage.NOT_INITIALIZED );

    String applicationId = sharedPreferences.getString( Type.APPLICATION_ID_KEY.name64(), null );
    String apiKey = sharedPreferences.getString( Type.API_KEY.name64(), null );

    if( applicationId != null && apiKey != null )
    {
      authKeys = new AuthKeys( applicationId, apiKey );
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
      editor.putString( Type.APPLICATION_ID_KEY.name64(), rawHeaders );
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
    editor.putString( Type.APPLICATION_ID_KEY.name64(), authKeys.getApplicationId() );
    editor.putString( Type.API_KEY.name64(), authKeys.getApiKey() );
    editor.commit();
  }

  private void cleanHeadersFromPreferences()
  {
    if( sharedPreferences == null )
      throw new IllegalStateException( ExceptionMessage.NOT_INITIALIZED );

    if( sharedPreferences.contains( Type.HEADERS.name64() ) )
    {
      SharedPreferences.Editor editor = sharedPreferences.edit();
      editor.remove( Type.HEADERS.name64() );
      editor.commit();
    }
  }

  void savePushTemplate( String pushTemplatesAsJson )
  {
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString( Type.PUSH_TEMPLATES.name64(), pushTemplatesAsJson );
    editor.commit();
  }

  String getPushTemplateAsJson()
  {
    return sharedPreferences.getString( Type.PUSH_TEMPLATES.name64(), null );
  }

  int getNotificationIdGeneratorInitValue()
  {
    return sharedPreferences.getInt( Type.NOTIFICATION_ID_GENERATOR.name64(), 0 );
  }

  void saveNotificationIdGeneratorState( int value )
  {
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putInt( Type.NOTIFICATION_ID_GENERATOR.name64(), value );
    editor.commit();
  }

  enum Type
  {
    APPLICATION_ID_KEY,
    API_KEY,
    URL_KEY,
    CUSTOM_DOMAIN_KEY,
    HEADERS,
    PUSH_TEMPLATES,
    NOTIFICATION_ID_GENERATOR;

    String name64()
    {
      return UUID.nameUUIDFromBytes( this.name().getBytes() ).toString();
    }
  }
}
