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

import android.app.Service;
import android.content.*;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.IBinder;
import com.backendless.exceptions.ExceptionMessage;
import com.backendless.persistence.local.UserTokenStorageFactory;
import com.backendless.utils.AndroidIO;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class AndroidService extends Service implements IBackendlessService
{
  private static IBackendlessService androidService;
  private final IBinder binder = new BackendlessBinder();
  private AuthKeys authKeys;
  private Map<String, String> headers;
  private SharedPreferences sharedPreferences;

  //Break encapsulation, but provides recovery after GC cleaning
  static IBackendlessService recoverService()
  {
    if( androidService == null )
      androidService = new StubBackendlessService();

    return androidService;
  }

  @Override
  public void setAuthKeys( String applicationId, String secretKey, String version )
  {
    authKeys = new AuthKeys( applicationId, secretKey, version );
    saveAuthKeysToPreferences();
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
  public synchronized void cleanHeaders()
  {
    headers = null;
    cleanHeadersFromPreferences();
  }

  @Override
  public synchronized void setHeaders( Map<String, String> headers )
  {
    this.headers = headers;
    saveHeadersToPreferences();
  }

  private void saveAuthKeysToPreferences()
  {
    if( authKeys == null )
      return;

    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString( Type.APPLICATION_ID.name64(), authKeys.getApplicationId() );
    editor.putString( Type.SECRET_KEY.name64(), authKeys.getSecretKey() );
    editor.putString( Type.VERSION.name64(), authKeys.getVersion() );
    editor.commit();
  }

  private void saveHeadersToPreferences()
  {
    if( headers == null )
      return;

    try
    {
      SharedPreferences.Editor editor = sharedPreferences.edit();
      String rawHeaders = AndroidIO.mapToString( headers );
      editor.putString( Type.APPLICATION_ID.name64(), rawHeaders );
      editor.commit();
    }
    catch( IOException e )
    {
    }
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

  private boolean restoreHeadersFromPreferences()
  {
    String rawHeaders = sharedPreferences.getString( Type.HEADERS.name64(), null );

    if( rawHeaders != null )
    {
      try
      {
        headers = AndroidIO.mapFromString( rawHeaders );
      }
      catch( Exception e )
      {
        return false;
      }

      return true;
    }

    return false;
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

  @Override
  public void onCreate()
  {
    super.onCreate();

    sharedPreferences = getSharedPreferences( TAG, MODE_PRIVATE );
    if( !restoreAuthKeysFromPreferences() )
      restoreAuthKeysFromFS();

    if( !restoreHeadersFromPreferences() )
      restoreHeadersFromFS();

    androidService = this;
  }

  private boolean restoreAuthKeysFromFS()
  {
    String applicationId = AndroidIO.readFromFile( this, Type.APPLICATION_ID.name64() );
    String secretKey = AndroidIO.readFromFile( this, Type.SECRET_KEY.name64() );
    String version = AndroidIO.readFromFile( this, Type.VERSION.name64() );

    if( applicationId != null && secretKey != null && version != null )
    {
      authKeys = new AuthKeys( applicationId, secretKey, version );
      return true;
    }

    return false;
  }

  private boolean restoreHeadersFromFS()
  {
    Map headers = AndroidIO.readMapFromFile( this, Type.HEADERS.name64() );
    if( headers != null && !headers.isEmpty() )
    {
      this.headers = headers;
      return true;
    }

    return false;
  }

  @Override
  public int onStartCommand( Intent intent, int flags, int startId )
  {
    return START_STICKY;
  }

  @Override
  public void onDestroy()
  {
    if( authKeys != null )
      saveAuthKeysToFS();
  }

  private void saveAuthKeysToFS()
  {
    if( authKeys == null )
      return;

    AndroidIO.writeToFile( this, Type.APPLICATION_ID.name64(), authKeys.getApplicationId() );
    AndroidIO.writeToFile( this, Type.SECRET_KEY.name64(), authKeys.getSecretKey() );
    AndroidIO.writeToFile( this, Type.VERSION.name64(), authKeys.getVersion() );
  }

  private void saveHeadersToFS()
  {
    if( headers == null )
      return;

    AndroidIO.writeToFile( this, Type.HEADERS.name64(), headers );
  }

  @Override
  public IBinder onBind( Intent intent )
  {
    return binder;
  }

  protected static final class Init implements IBackendlessService.Init
  {
    @Override
    public void initService( final Object arg, final IServiceCreatedCallback callback )
    {
      if( arg == null )
        throw new IllegalArgumentException( ExceptionMessage.NULL_CONTEXT );

      Context applicationContext = ((Context) arg).getApplicationContext();

      UserTokenStorageFactory.instance().init( applicationContext );

      PackageManager packageManager = applicationContext.getPackageManager();
      final Intent intent = new Intent( applicationContext, AndroidService.class );
      List resolveInfo = packageManager.queryIntentServices( intent, PackageManager.MATCH_DEFAULT_ONLY );
      if( resolveInfo.size() == 0 )
        throw new IllegalStateException( ExceptionMessage.SERVICE_NOT_DECLARED );

      applicationContext.startService( new Intent( applicationContext, AndroidService.class ) );
      applicationContext.bindService( new Intent( applicationContext, AndroidService.class ), new ServiceConnection()
      {
        public void onServiceConnected( ComponentName className, IBinder service )
        {
          callback.onServiceConnected( ((AndroidService.BackendlessBinder) service).getService() );
        }

        public void onServiceDisconnected( ComponentName className )
        {/*STUB*/}
      }, Context.BIND_AUTO_CREATE );
    }
  }

  protected final class BackendlessBinder extends Binder
  {
    AndroidService getService()
    {
      return AndroidService.this;
    }
  }
}