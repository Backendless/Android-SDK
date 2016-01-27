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

package com.backendless.example.loginwithgoogleplussdk.app;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.backendless.Backendless;

public class MainActivity extends FragmentActivity
{
  LoginFragment loginFragment;
  TextView textView;
  Button googleButton;
  ListView listView;
  ProgressDialog progressDialog;

  @Override
  protected void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.activity_main );

    initUI();

    Backendless.setUrl( Defaults.SERVER_URL );
    Backendless.initApp( this, Defaults.APPLICATION_ID, Defaults.SECRET_KEY, Defaults.VERSION );
  }

  private void initUI()
  {
    // status text
    textView = (TextView)this.findViewById( R.id.textView );

    // initialize ListView for output result
    listView = (ListView)this.findViewById( R.id.listView_accountInfo );
    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
    listView.setAdapter(adapter);

    // create fragment for login/logout
    FragmentManager fm = this.getFragmentManager();
    loginFragment = (LoginFragment) fm.findFragmentByTag( LoginFragment.TAG );
    if( loginFragment == null )
    {
      FragmentTransaction ft = fm.beginTransaction();
      loginFragment = new LoginFragment();
      // fragmentTransaction.add(R.id.fragment_container, hello, "HELLO");
      ft.add( loginFragment, LoginFragment.TAG );
      ft.commit();
    }

    // add button handler
    googleButton = (Button) findViewById(R.id.button_login);
    googleButton.setOnClickListener( new View.OnClickListener()
    {
      @Override
      public void onClick( View view )
      {
        loginFragment.login_logout( );
      }
    });

    // prepare progress dialog
    progressDialog = new ProgressDialog(this);
    progressDialog.setMessage( getString( R.string.progressDialog_authentication ) );
    progressDialog.setIndeterminate( true );
  }

  @Override
  public boolean onCreateOptionsMenu( Menu menu )
  {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate( R.menu.menu_main, menu );
    return true;
  }

  @Override
  public boolean onOptionsItemSelected( MenuItem item )
  {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if( id == R.id.action_settings )
    {
      return true;
    }

    return super.onOptionsItemSelected( item );
  }
}
