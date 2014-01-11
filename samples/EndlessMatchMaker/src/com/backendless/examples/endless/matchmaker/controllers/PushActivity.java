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

package com.backendless.examples.endless.matchmaker.controllers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.backendless.examples.endless.matchmaker.R;
import com.backendless.examples.endless.matchmaker.controllers.shared.Lifecycle;
import com.backendless.examples.endless.matchmaker.utils.Defaults;

public class PushActivity extends Activity
{
  @Override
  public void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.push );

    Intent intent = getIntent();
    String userName = intent.getStringExtra( Defaults.TARGET_USER_NAME );
    String currentName = intent.getStringExtra( Defaults.CURRENT_USER_NAME );
    String type = intent.getStringExtra( "type" );
    String message = null;

    if( type.equals( "ping" ) )
      message = userName + ", " + getResources().getString( R.string.textfield_push_message ) + " " + currentName;
    else
      message = userName + ", " + getResources().getString( R.string.textfield_send_message ) + " " + currentName;

    ((TextView) findViewById( R.id.pushMessage )).setText( message );
    findViewById( R.id.loginButton ).setOnClickListener( loginListener );
  }

  //Listeners section
  private View.OnClickListener loginListener = new View.OnClickListener()
  {
    @Override
    public void onClick( View view )
    {
      Lifecycle.runLoginActivity( PushActivity.this );
      finish();
    }
  };
}
