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
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.examples.endless.matchmaker.R;
import com.backendless.examples.endless.matchmaker.controllers.shared.Lifecycle;
import com.backendless.examples.endless.matchmaker.controllers.shared.ResponseAsyncCallback;
import com.backendless.examples.endless.matchmaker.models.local.Gender;
import com.backendless.examples.endless.matchmaker.models.local.PreferenceTheme;
import com.backendless.examples.endless.matchmaker.models.persistent.UserPreferences;
import com.backendless.examples.endless.matchmaker.utils.Defaults;
import com.backendless.examples.endless.matchmaker.utils.SimpleMath;
import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.examples.endless.matchmaker.views.UIFactory;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.QueryOptions;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileActivity extends Activity
{
  private Map<String, String> userPreferencesMap = new HashMap<String, String>();
  private ProgressDialog progressDialog;

  @Override
  public void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.profile );

    TextView nameField = (TextView) findViewById( R.id.nameField );
    TextView genderField = (TextView) findViewById( R.id.genderField );
    TextView ageField = (TextView) findViewById( R.id.ageField );
    ImageView avatarImage = (ImageView) findViewById( R.id.avatarImage );
    ImageView genderImage = (ImageView) findViewById( R.id.genderImage );

    BackendlessUser currentUser = Backendless.UserService.CurrentUser();

    //Checking CurrentUser
    if( currentUser == null )
    {
      Lifecycle.runLoginActivity( ProfileActivity.this );
      finish();
    }
    else
    {
      nameField.setText( (String) currentUser.getProperty( Defaults.NAME_PROPERTY ) );
      Gender userGender = Gender.valueOf( (String) currentUser.getProperty( Defaults.GENDER_PROPERTY ) );
      genderField.setText( userGender.toString() );

      if( userGender == Gender.male )
      {
        avatarImage.setImageDrawable( getResources().getDrawable( R.drawable.avatar_default_male ) );
        genderImage.setImageDrawable( getResources().getDrawable( R.drawable.icon_male ) );
      }
      else
      {
        avatarImage.setImageDrawable( getResources().getDrawable( R.drawable.avatar_default_female ) );
        genderImage.setImageDrawable( getResources().getDrawable( R.drawable.icon_female ) );
      }

      Date birthDate = (Date) currentUser.getProperty( Defaults.BIRTH_DATE_PROPERTY );
      ageField.setText( String.valueOf( SimpleMath.getAgeFromDate( birthDate ) ) );
    }

    findViewById( R.id.findButton ).setOnClickListener( findMatchesListener );
    findViewById( R.id.editFoodPreferenceButton ).setOnClickListener( getEditPreferencesListener( PreferenceTheme.FOOD ) );
    findViewById( R.id.editMusicPreferenceButton ).setOnClickListener( getEditPreferencesListener( PreferenceTheme.MUSIC ) );
    findViewById( R.id.editHobbiesPreferenceButton ).setOnClickListener( getEditPreferencesListener( PreferenceTheme.HOBBIES ) );
    findViewById( R.id.editTravelPreferenceButton ).setOnClickListener( getEditPreferencesListener( PreferenceTheme.TRAVEL ) );

    Button button_logout = (Button) findViewById( R.id.buttonLogOut );
    button_logout.setOnClickListener( new View.OnClickListener()
    {
      @Override
      public void onClick( View v )
      {
        progressDialog = UIFactory.getDefaultProgressDialog( ProfileActivity.this );
        Backendless.UserService.logout( new AsyncCallback<Void>()
        {
          @Override
          public void handleResponse( Void aVoid )
          {
            Lifecycle.runLoginActivity( ProfileActivity.this );
            finish();
            progressDialog.cancel();
          }

          @Override
          public void handleFault( BackendlessFault backendlessFault )
          {
            progressDialog.cancel();
            Toast.makeText( ProfileActivity.this, backendlessFault.getMessage(), Toast.LENGTH_LONG ).show();
          }
        } );
      }
    } );
  }

  @Override
  public void onBackPressed()
  {
    Intent setIntent = new Intent( Intent.ACTION_MAIN );
    setIntent.addCategory( Intent.CATEGORY_HOME );
    setIntent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
    startActivity( setIntent );
  }

  //Listeners section
  private View.OnClickListener findMatchesListener = new View.OnClickListener()
  {
    @Override
    public void onClick( View view )
    {
      progressDialog = UIFactory.getDefaultProgressDialog( ProfileActivity.this );
      BackendlessDataQuery backendlessDataQuery = new BackendlessDataQuery( "email = '" + Backendless.UserService.CurrentUser().getEmail() + "'" );
      backendlessDataQuery.setQueryOptions( new QueryOptions( 50, 0 ) );
      Backendless.Persistence.of( UserPreferences.class ).find( backendlessDataQuery, new ResponseAsyncCallback<BackendlessCollection<UserPreferences>>( ProfileActivity.this )
      {
        @Override
        public void handleResponse( BackendlessCollection<UserPreferences> response )
        {
          List<UserPreferences> userPreferenceses = response.getCurrentPage();

          for( UserPreferences userPreferencese : userPreferenceses )
            userPreferencesMap.put( userPreferencese.getPreference(), userPreferencese.getTheme() );

          Lifecycle.runFindMatchesActivity( ProfileActivity.this );
          progressDialog.cancel();

//          if( !userPreferencesMap.containsValue( "Food" ) || !userPreferencesMap.containsValue( "Music" ) || !userPreferencesMap.containsValue( "Hobbies" ) || !userPreferencesMap.containsValue( "Travel" ) )
//          {
//            progressDialog.cancel();
//            Toast.makeText( ProfileActivity.this, "Set your preferences before search", Toast.LENGTH_LONG ).show();
//          }
//          else
//          {
//            progressDialog.cancel();
//            Lifecycle.runFindMatchesActivity( ProfileActivity.this );
//          }
        }
      } );
    }
  };

  private View.OnClickListener getEditPreferencesListener( final PreferenceTheme type )
  {
    return new View.OnClickListener()
    {
      @Override
      public void onClick( View view )
      {
        Lifecycle.runEditPreferencesActivity( ProfileActivity.this, type );
      }
    };
  }
}