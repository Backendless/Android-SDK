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
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.backendless.examples.endless.matchmaker.R;
import com.backendless.examples.endless.matchmaker.controllers.shared.Lifecycle;
import com.backendless.examples.endless.matchmaker.controllers.shared.ResponseAsyncCallback;
import com.backendless.examples.endless.matchmaker.utils.Defaults;
import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.Subscription;
import com.backendless.geo.GeoPoint;
import com.backendless.messaging.*;

import java.util.List;

public class SendMessageActivity extends Activity
{
  private EditText messageText;
  private TextView messagesContainer;
  private String currentUserEmail = Backendless.UserService.CurrentUser().getEmail();
  private String currentUserName = (String) Backendless.UserService.CurrentUser().getProperty( Defaults.NAME_PROPERTY );
  private GeoPoint currentUserGeoPoint;
  private GeoPoint targetUserGeoPoint;
  private String targetUserEmail;

  @Override
  public void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.message );

    currentUserGeoPoint = (GeoPoint) getIntent().getSerializableExtra( Defaults.CURRENT_USER_GEO_POINT_BUNDLE_TAG );
    targetUserGeoPoint = (GeoPoint) getIntent().getSerializableExtra( Defaults.TARGET_USER_GEO_POINT_BUNDLE_TAG );
    targetUserEmail = targetUserGeoPoint.getMetadata( BackendlessUser.EMAIL_KEY );
    String targetUserName = targetUserGeoPoint.getMetadata( Defaults.NAME_PROPERTY );

    messageText = (EditText) findViewById( R.id.messageText );
    ((TextView) findViewById( R.id.title )).setText( "Message to " + targetUserName );
    messagesContainer = (TextView) findViewById( R.id.messagesContainer );

    findViewById( R.id.sendButton ).setOnClickListener( sendListener );

    SubscriptionOptions subscriptionOptions = new SubscriptionOptions();
    String selector = "(" + Defaults.PUBLISHER_HEADER + " = '" + targetUserEmail + "' and " + Defaults.SUBSCRIBER_HEADER + " = '" + currentUserEmail + "')";
    selector += "or (" + Defaults.PUBLISHER_HEADER + " = '" + currentUserEmail + "' and " + Defaults.SUBSCRIBER_HEADER + " = '" + targetUserEmail + "')";
    subscriptionOptions.setSelector( selector );
    Backendless.Messaging.subscribe( Defaults.MESSAGING_CHANNEL, new ResponseAsyncCallback<List<Message>>( this )
    {
      @Override
      public void handleResponse( List<Message> response )
      {
        for( Message message : response )
        {
          messagesContainer.append( "\n" + message.getHeaders().get( Defaults.PUBLISHER_NAME_HEADER ) + ": " + message.getData() );
        }
      }
    }, subscriptionOptions, new ResponseAsyncCallback<Subscription>( SendMessageActivity.this ) );
  }

  //Listeners section
  private View.OnClickListener sendListener = new View.OnClickListener()
  {
    @Override
    public void onClick( View view )
    {
      if( messageText.getText() == null || messageText.getText().toString().equals( "" ) )
      {
        Toast.makeText( SendMessageActivity.this, "Nothing to send...", Toast.LENGTH_SHORT ).show();
        return;
      }

      findViewById( R.id.sendButton ).setClickable( false );

      PublishOptions publishOptions = new PublishOptions();
      publishOptions.putHeader( Defaults.PUBLISHER_HEADER, currentUserEmail );
      publishOptions.putHeader( Defaults.PUBLISHER_NAME_HEADER, currentUserName );
      publishOptions.putHeader( Defaults.SUBSCRIBER_HEADER, targetUserEmail );
      publishOptions.putHeader( "type", "message" );
      publishOptions.putHeader( "currentUserName", currentUserName );
      publishOptions.putHeader( "targetUserName", targetUserGeoPoint.getMetadata( Defaults.NAME_PROPERTY ) );
      String targetUserDeviceRegistrationId = targetUserGeoPoint.getMetadata( Defaults.DEVICE_REGISTRATION_ID_PROPERTY );
      DeliveryOptions deliveryOptions = new DeliveryOptions();
      deliveryOptions.setPushPolicy( PushPolicyEnum.ALSO );
      deliveryOptions.addPushSinglecast( targetUserDeviceRegistrationId );

      Backendless.Messaging.publish( Defaults.MESSAGING_CHANNEL, String.valueOf( messageText.getText() ), publishOptions, deliveryOptions, new ResponseAsyncCallback<MessageStatus>( SendMessageActivity.this )
      {
        @Override
        public void handleResponse( MessageStatus response )
        {
          findViewById( R.id.sendButton ).setClickable( true );
        }
      } );
      messageText.setText( "" );
    }
  };

  @Override
  public void onBackPressed()
  {
    Lifecycle.runPingsActivity( SendMessageActivity.this, currentUserGeoPoint );
    finish();
  }
}
