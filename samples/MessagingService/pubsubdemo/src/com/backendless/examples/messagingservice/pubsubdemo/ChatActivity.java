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

package com.backendless.examples.messagingservice.pubsubdemo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.backendless.Backendless;
import com.backendless.Subscription;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.messaging.Message;
import com.backendless.messaging.MessageStatus;
import com.backendless.messaging.PublishOptions;

import java.util.List;

public class ChatActivity extends Activity
{
  private Subscription subscription;

  public void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.chat );

    final String name = getIntent().getStringExtra( Defaults.NAME_TAG );
    final EditText historyField = (EditText) findViewById( R.id.historyField );
    final EditText messageField = (EditText) findViewById( R.id.messageField );

    Backendless.initApp( this, Defaults.APPLICATION_ID, Defaults.SECRET_KEY, Defaults.VERSION );
    Backendless.Messaging.subscribe(
        Defaults.CHANNEL_NAME,
        new AsyncCallback<List<Message>>()
        {
          @Override
          public void handleResponse( List<Message> response )
          {
            for ( Message message : response )
              historyField.setText( message.getPublisherId() + ": " + message.getData() + "\n" + historyField.getText() );
          }

          @Override
          public void handleFault( BackendlessFault fault )
          {
            Toast.makeText( ChatActivity.this, fault.getMessage(), Toast.LENGTH_SHORT ).show();
          }
        },
        new AsyncCallback<Subscription>()
        {
          @Override
          public void handleResponse( Subscription response )
          {
            subscription = response;
          }

          @Override
          public void handleFault( BackendlessFault fault )
          {
            Toast.makeText( ChatActivity.this, fault.getMessage(), Toast.LENGTH_SHORT ).show();
          }
        }
    );

    messageField.setOnKeyListener( new View.OnKeyListener()
    {
      @Override
      public boolean onKey( View view, int keyCode, KeyEvent keyEvent )
      {
        if( keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_UP )
        {
          String message = messageField.getText().toString();

          if( message == null || message.equals( "" ) )
            return true;

          final ProgressDialog progressDialog = ProgressDialog.show( ChatActivity.this, "", "Sending", true );
          Backendless.Messaging.publish( Defaults.CHANNEL_NAME, message, new PublishOptions( name ), new AsyncCallback<MessageStatus>()
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
              Toast.makeText( ChatActivity.this, fault.getMessage(), Toast.LENGTH_SHORT ).show();
            }
          } );

          return true;
        }
        return false;
      }
    } );
  }

  @Override
  protected void onDestroy()
  {
    super.onStop();

    if( subscription != null )
      subscription.cancelSubscription();
  }

  @Override
  protected void onResume()
  {
    super.onResume();

    if( subscription != null )
      subscription.resumeSubscription();
  }

  @Override
  protected void onPause()
  {
    super.onPause();

    if( subscription != null )
      subscription.pauseSubscription();
  }
}