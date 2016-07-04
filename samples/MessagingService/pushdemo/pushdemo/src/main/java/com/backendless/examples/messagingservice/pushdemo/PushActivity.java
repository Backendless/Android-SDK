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

package com.backendless.examples.messagingservice.pushdemo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.backendless.Backendless;
import com.backendless.async.callback.BackendlessCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.messaging.DeliveryOptions;
import com.backendless.messaging.MessageStatus;
import com.backendless.messaging.PublishOptions;
import com.backendless.messaging.PushBroadcastMask;

public class PushActivity extends Activity
{
  public static final String CUSTOM_ACTION = "com.backendless.examples.messagingservice.pushdemo.NOTIFICATION_CLICKED";

  private TextView historyField;
  private EditText messageField;
  private CheckBox forceNotify;
  public static Handler handler;

  public void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.push );

    historyField = (TextView) findViewById( R.id.historyField );
    messageField = (EditText) findViewById( R.id.messageField );
    forceNotify = (CheckBox) findViewById( R.id.forceNotify );
    handler = new Handler( new Handler.Callback()
    {
      @Override
      public boolean handleMessage( Message message )
      {
        if( !(message.obj instanceof Error) )
          historyField.setText( message.obj + "\n" + historyField.getText() );
        else
          MainActivity.showAlert( PushActivity.this, ((Error) message.obj).getMessage() );

        return true;
      }
    } );

    findViewById( R.id.sendButton ).setOnClickListener( new View.OnClickListener()
    {
      @Override
      public void onClick( View view )
      {
        String message = messageField.getText().toString();

        if( message == null || message.equals( "" ) )
          return;

        final ProgressDialog progressDialog = ProgressDialog.show( PushActivity.this, "", "Sending push message", true );

        DeliveryOptions deliveryOptions = new DeliveryOptions();
        deliveryOptions.setPushBroadcast( PushBroadcastMask.ALL );
        PublishOptions publishOptions = new PublishOptions();
        if( forceNotify.isChecked() )
        {
          publishOptions.putHeader( PublishOptions.ANDROID_TICKER_TEXT_TAG, getString( R.string.app_name ) );
          publishOptions.putHeader( PublishOptions.ANDROID_CONTENT_TITLE_TAG, getString( R.string.app_name ) );
          publishOptions.putHeader( PublishOptions.ANDROID_CONTENT_TEXT_TAG, message );
        }

        Backendless.Messaging.publish( Defaults.CHANNEL_NAME, message, publishOptions, deliveryOptions, new BackendlessCallback<MessageStatus>()
        {
          @Override
          public void handleResponse( MessageStatus response )
          {
            progressDialog.cancel();
            messageField.setText( "" );
          }

          @Override
          public void handleFault( BackendlessFault fault )
          {
            progressDialog.cancel();
            Toast.makeText( PushActivity.this, fault.getMessage(), Toast.LENGTH_SHORT ).show();
          }
        } );
      }
    } );

    findViewById( R.id.unregisterButton ).setOnClickListener( new View.OnClickListener()
    {
      @Override
      public void onClick( View view )
      {
        Backendless.Messaging.unregisterDevice();
        setResult( RESULT_OK, new Intent() );
        finish();
      }
    } );

    getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN );
  }
}