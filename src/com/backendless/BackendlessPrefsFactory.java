package com.backendless;

/**
 * Created by dzidzoiev on 11/9/15.
 */
class BackendlessPrefsFactory
{
  static BackendlessPrefs create( boolean isAndroid )
  {
    return isAndroid ?
        new AndroidBackendlessPrefs() :
        new BackendlessPrefs();
  }
}
