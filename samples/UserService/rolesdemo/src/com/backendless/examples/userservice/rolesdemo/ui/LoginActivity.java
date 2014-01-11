package com.backendless.examples.userservice.rolesdemo.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import com.backendless.Backendless;
import com.backendless.examples.userservice.rolesdemo.Defaults;
import com.backendless.examples.userservice.rolesdemo.R;

public class LoginActivity extends FragmentActivity
{
  private FragmentTabHost mTabHost;

  @Override
  protected void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.activity_login );

    Backendless.initApp( this, Defaults.APP_ID, Defaults.SECRET_KEY, Defaults.VERSION );

    Bundle readBundle = new Bundle(  );
    readBundle.putString( Defaults.ROLE_TAG, Defaults.READ_ROLE );

    Bundle writeBundle = new Bundle(  );
    writeBundle.putString( Defaults.ROLE_TAG, Defaults.READ_WRITE_ROLE );

    mTabHost = (FragmentTabHost) findViewById( android.R.id.tabhost );
    mTabHost.setup( this, getSupportFragmentManager(), R.id.tabFrameLayout );
    mTabHost.addTab( mTabHost.newTabSpec( "read" ).setIndicator( "ReadRole" ), LoginFragment.class, readBundle );
    mTabHost.addTab( mTabHost.newTabSpec( "write" ).setIndicator( "ReadWriteRole" ), LoginFragment.class, writeBundle );
  }
}
