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

package com.backendless.facebooklogin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends FragmentActivity
{
  private CallbackManager callbackManager;

  @Override
  public void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.main );

    //init backendless sdk
    Backendless.setUrl( Defaults.SERVER_URL );
    Backendless.initApp( this, Defaults.APPLICATION_ID, Defaults.SECRET_KEY, Defaults.VERSION );

    FacebookSdk.sdkInitialize( this.getApplicationContext() );
    callbackManager = CallbackManager.Factory.create();

    Button button = (Button) findViewById(R.id.button);
    button.setOnClickListener( new View.OnClickListener()
    {
      public void onClick( View view )
      {
        Map<String, String> facebookFieldsMappings = new HashMap<>();
        facebookFieldsMappings.put( "email", "email" );
        facebookFieldsMappings.put( "first_name", "fb_first_name" );
        facebookFieldsMappings.put( "last_name", "fb_last_name" );
        facebookFieldsMappings.put( "gender", "fb_gender" );

        List<String> permissions = new ArrayList<>();
        permissions.add( "email" );
        permissions.add("public_profile");

        Backendless.UserService.loginWithFacebookSdk( MainActivity.this, facebookFieldsMappings, permissions, callbackManager, new AsyncCallback<BackendlessUser>()
        {
          @Override
          public void handleResponse( BackendlessUser backendlessUser )
          {
            Toast toast = Toast.makeText( MainActivity.this, "Ok, User ID ="+backendlessUser.getUserId(), Toast.LENGTH_LONG );
            toast.show();
          }

          @Override
          public void handleFault( BackendlessFault backendlessFault )
          {
            Toast toast = Toast.makeText( MainActivity.this, backendlessFault.toString(), Toast.LENGTH_LONG );
            toast.show();
          }
        } );
      }
    } );
  }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult( requestCode, resultCode, data);
    callbackManager.onActivityResult(requestCode, resultCode, data);
  }
}
