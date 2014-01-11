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
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.*;
import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.examples.endless.matchmaker.R;
import com.backendless.examples.endless.matchmaker.controllers.shared.ResponseAsyncCallback;
import com.backendless.examples.endless.matchmaker.utils.Defaults;
import com.backendless.examples.endless.matchmaker.views.UIFactory;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.geo.BackendlessGeoQuery;
import com.backendless.geo.GeoPoint;

import java.util.List;

public class DialogMessageActivity extends Activity

{
  private String titleMessage;
  private String sendMessage;
  private String checkBoxResult;
  private GeoPoint myLocation;
  private String currentName;
  private String targetName;
  private String type;
  private ProgressDialog progressDialog;
  private SharedPreferences.Editor editor;
  private static final String PREFS_NAME = "Preference";

  @Override
  public void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    this.requestWindowFeature( Window.FEATURE_NO_TITLE );
    setContentView( R.layout.dialog_message );

    LinearLayout linearLayout = (LinearLayout) findViewById( R.id.mainLay );
    linearLayout.setVisibility( View.GONE );

    Intent intent = getIntent();
    currentName = intent.getStringExtra( Defaults.CURRENT_USER_NAME );
    targetName = intent.getStringExtra( Defaults.TARGET_USER_NAME );
    type = intent.getStringExtra( "type" );

    progressDialog  = UIFactory.getDefaultProgressDialog( this );
    BackendlessGeoQuery backendlessGeoQuery = new BackendlessGeoQuery( BackendlessUser.EMAIL_KEY, Backendless.UserService.CurrentUser().getEmail() );
    Backendless.Geo.getPoints( backendlessGeoQuery, new AsyncCallback<BackendlessCollection<GeoPoint>>()
    {
      @Override
      public void handleResponse( BackendlessCollection<GeoPoint> response )
      {
        List<GeoPoint> points = response.getCurrentPage();

        if( !points.isEmpty() )
        {
          myLocation = points.get( 0 );
        }

        if( type.equals( "ping" ) )
        {
          titleMessage = "New ping!";
          sendMessage = "You were pinged by " + currentName;
        }
        else
        {
          titleMessage = "New message!";
          sendMessage = "You got a new message from " + currentName;
        }

        LayoutInflater layoutInflater = getLayoutInflater();
        View checkBoxView = layoutInflater.inflate( R.layout.dialog_message, null );
        final CheckBox checkBox = (CheckBox) checkBoxView.findViewById( R.id.checkBox );

        progressDialog.cancel();
        AlertDialog.Builder builder = new AlertDialog.Builder( DialogMessageActivity.this );
        builder.setTitle( titleMessage );
        builder.setMessage( sendMessage );
        builder.setView( checkBoxView );
        builder.setCancelable( false );
        builder.setPositiveButton( R.string.yes, new DialogInterface.OnClickListener()
        {
          @Override
          public void onClick( DialogInterface dialog, int which )
          {
            checkBoxResult = "NOT checked";
            if( checkBox.isChecked() )
              checkBoxResult = "checked";

            myLocation.putMetadata( currentName, checkBoxResult );
            Backendless.Geo.savePoint( myLocation, new AsyncCallback<GeoPoint>()
            {
              @Override
              public void handleResponse( GeoPoint geoPoint )
              {
                //Toast.makeText( DialogMessageActivity.this, "Changes successfully saved", Toast.LENGTH_LONG ).show();
              }

              @Override
              public void handleFault( BackendlessFault backendlessFault )
              {
                Toast.makeText( DialogMessageActivity.this, backendlessFault.getMessage(), Toast.LENGTH_LONG ).show();
              }
            } );

            dialog.dismiss();
            finish();
          }
        } );

        if( myLocation.getMetadata( currentName ) == null || !myLocation.getMetadata( currentName ).equals( "checked" ) )
        {
          AlertDialog dialog = builder.create();
          dialog.show();
        }

        else
          finish();
      }

      @Override
      public void handleFault( BackendlessFault fault )
      {
        Toast.makeText( DialogMessageActivity.this, fault.getMessage(), Toast.LENGTH_LONG ).show();
      }
    } );
  }

  @Override
  public void onBackPressed()
  {
    finish();
  }
}
