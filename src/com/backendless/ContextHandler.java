package com.backendless;

import android.app.Application;
import android.content.Context;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by dzidzoiev on 11/10/15.
 */
public class ContextHandler
{
  static private Context appContext;

  private ContextHandler()
  {
    throw new IllegalStateException( "static" );
  }

  public synchronized static Context getAppContext()
  {
    if(appContext == null)
      appContext = recoverAppContext();
    return appContext;
  }

  public synchronized static void setContext( Object anyContext )
  {
    if( Backendless.isAndroid() )
      appContext = ( (Context) anyContext ).getApplicationContext();
  }

  /**
   * Magic methods
   */
  private synchronized static Context recoverAppContext()
  {
    try
    {
      final Class<?> activityThreadClass = Class.forName( "android.app.ActivityThread" );
      final Method method = activityThreadClass.getMethod( "currentApplication" );

      Application app = (Application) method.invoke( null, (Object[]) null );
      return app.getApplicationContext();
    }
    catch ( NoSuchMethodException e )
    {
      return recoverAppContextOldAndroid();
    }
    catch ( Throwable e )
    {
      e.printStackTrace();
    }
    return null;
  }

  private synchronized static Context recoverAppContextOldAndroid()
  {
    try
    {
      final Class<?> activityThreadClass = Class.forName( "android.app.ActivityThread" );
      final Method method = activityThreadClass.getMethod( "currentActivityThread" );
      Object activityThread = method.invoke( null, (Object[]) null );
      final Field field = activityThreadClass.getDeclaredField( "mInitialApplication" );
      field.setAccessible( true );
      Application app = (Application) field.get( activityThread );
      return app.getApplicationContext();
    }
    catch ( Throwable e )
    {
      e.printStackTrace();
    }
    return null;
  }
}
