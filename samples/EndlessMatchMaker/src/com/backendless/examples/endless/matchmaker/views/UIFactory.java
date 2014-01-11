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

package com.backendless.examples.endless.matchmaker.views;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.backendless.examples.endless.matchmaker.R;
import com.backendless.examples.endless.matchmaker.models.persistent.PreferencesDefaults;

public class UIFactory
{
  private static LayoutInflater layoutInflater;

  public static View.OnClickListener getToastStubListener( final Context context, final CharSequence text )
  {
    return new View.OnClickListener()
    {
      @Override
      public void onClick( View view )
      {
        Toast.makeText( context, text, Toast.LENGTH_SHORT ).show();
      }
    };
  }

  public static CheckBox getPreferenceCheckbox( Context context, PreferencesDefaults preference )
  {
    layoutInflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
    CheckBox result = (CheckBox) layoutInflater.inflate( R.layout.inflate_checkbox, null );
    result.setText( preference.getName() );

    return result;
  }

  public static View getPingsListElement( Context context, CharSequence userName, double totalMatch,
                                          View.OnClickListener clickListener, String gender )
  {
    layoutInflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
    LinearLayout result = (LinearLayout) layoutInflater.inflate( R.layout.inflate_ping, null );
    ImageView avatar = (ImageView) result.findViewById( R.id.avatarImage );
    avatar.setImageResource( gender.equals( "male" ) ? R.drawable.avatar_default_male : R.drawable.avatar_default_female );
    ((TextView) result.findViewById( R.id.nameField )).setText( userName );
    ((TextView) result.findViewById( R.id.totalMatchValue )).setText( String.valueOf( totalMatch ) );
    ((ProgressBar) result.findViewById( R.id.totalProgressBar )).setProgress( (int) totalMatch );
    result.setOnClickListener( clickListener );

    return result;
  }

  public static ProgressDialog getDefaultProgressDialog( Context context )
  {
    return ProgressDialog.show( context, "", context.getString( R.string.loading_text ), true );
  }

  public static Dialog getLocationSettingsDialog( final Context context )
  {
    final Dialog result = new Dialog( context );
    layoutInflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
    LinearLayout dialogLayout = (LinearLayout) layoutInflater.inflate( R.layout.provider_dialog, null );
    dialogLayout.findViewById( R.id.openSettings ).setOnClickListener( new View.OnClickListener()
    {
      @Override
      public void onClick( View view )
      {
        result.dismiss();
        Intent settingsIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS );
        context.startActivity( settingsIntent );
      }
    } );

    result.addContentView( dialogLayout, new ViewGroup.LayoutParams( ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT ) );

    return result;
  }
}