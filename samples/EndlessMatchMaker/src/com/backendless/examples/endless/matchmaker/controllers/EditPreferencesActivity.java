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
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.backendless.examples.endless.matchmaker.R;
import com.backendless.examples.endless.matchmaker.controllers.shared.Lifecycle;
import com.backendless.examples.endless.matchmaker.controllers.shared.ResponseAsyncCallback;
import com.backendless.examples.endless.matchmaker.models.local.PreferenceTheme;
import com.backendless.examples.endless.matchmaker.models.persistent.PreferencesDefaults;
import com.backendless.examples.endless.matchmaker.models.persistent.UserPreferences;
import com.backendless.examples.endless.matchmaker.views.UIFactory;
import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.IDataStore;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.QueryOptions;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class EditPreferencesActivity extends Activity
{
  private LinearLayout preferencesContainer;
  private PreferenceTheme preferenceTheme;
  private IDataStore<UserPreferences> preferenceStore;
  private Map<String, CheckBox> preferenceNameToCheckboxMap = new WeakHashMap<String, CheckBox>();
  private Map<String, UserPreferences> preferenceNameToPreferenceMap = new WeakHashMap<String, UserPreferences>();
  private ProgressDialog progressDialog;

  @Override
  public void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.editpreference );

    progressDialog = UIFactory.getDefaultProgressDialog( this );
    TextView preferenceName = (TextView) findViewById( R.id.preferenceName );
    preferencesContainer = (LinearLayout) findViewById( R.id.preferencesContainer );

    preferenceTheme = PreferenceTheme.valueOf( getIntent().getStringExtra( Lifecycle.PREFERENCE_TYPE_TAG ) );
    preferenceStore = Backendless.Persistence.of( UserPreferences.class );

    //Naming the page
    preferenceName.setText( preferenceTheme.getPreference() );
    ((TextView) findViewById( R.id.preferenceLabel )).setText( getString( R.string.textfield_preferences_label_base ) + " " + preferenceTheme.getPreference().toLowerCase() + ":" );

    //Getting preference structure
    BackendlessDataQuery backendlessDataQuery = new BackendlessDataQuery( "theme = '" + preferenceTheme.getPreference() + "'" );
    QueryOptions queryOptions = new QueryOptions();
    queryOptions.setPageSize( 50 );
    queryOptions.addSortByOption( "name" );
    backendlessDataQuery.setQueryOptions( queryOptions );
    Backendless.Persistence.of( PreferencesDefaults.class ).find( backendlessDataQuery, new AsyncCallback<BackendlessCollection<PreferencesDefaults>>()
    {
      @Override
      public void handleResponse( BackendlessCollection<PreferencesDefaults> preferencesDefaultsBackendlessCollection )
      {
        //Creating a map with predefined checkboxes
        List<PreferencesDefaults> preferencesDefaults = preferencesDefaultsBackendlessCollection.getCurrentPage();

        for( PreferencesDefaults preference : preferencesDefaults )
        {
          CheckBox checkBox = UIFactory.getPreferenceCheckbox( EditPreferencesActivity.this, preference );
          checkBox.setOnClickListener( getCheckboxListener( checkBox ) );
          preferencesContainer.addView( checkBox );
          preferenceNameToCheckboxMap.put( preference.getName(), checkBox );
        }

        //Getting preferences, which should be enabled for current user
        String whereClause = "email = '" + Backendless.UserService.CurrentUser().getEmail() + "'";
        whereClause += " and theme = '" + preferenceTheme.getPreference() + "'";
        BackendlessDataQuery backendlessDataQuery = new BackendlessDataQuery();
        backendlessDataQuery.setWhereClause( whereClause );
        backendlessDataQuery.setQueryOptions( new QueryOptions( 50, 0 ) );
        Backendless.Persistence.of( UserPreferences.class ).find( backendlessDataQuery, new AsyncCallback<BackendlessCollection<UserPreferences>>()
        {
          @Override
          public void handleResponse( BackendlessCollection<UserPreferences> userPreferencesBackendlessCollection )
          {
            List<UserPreferences> userPreferences = userPreferencesBackendlessCollection.getCurrentPage();

            for( UserPreferences userPreference : userPreferences )
            {
              CheckBox checkBox = preferenceNameToCheckboxMap.get( userPreference.getPreference() );
              preferenceNameToPreferenceMap.put( userPreference.getPreference(), userPreference );
              checkBox.setChecked( true );
            }

            progressDialog.cancel();
          }

          @Override
          public void handleFault( BackendlessFault backendlessFault )
          {
            progressDialog.cancel();
            Toast.makeText( EditPreferencesActivity.this, backendlessFault.getMessage(), Toast.LENGTH_LONG ).show();
          }
        } );
      }

      @Override
      public void handleFault( BackendlessFault backendlessFault )
      {
        progressDialog.cancel();
        Toast.makeText( EditPreferencesActivity.this, backendlessFault.getMessage(), Toast.LENGTH_LONG ).show();
      }
    } );
  }

  //Listeners section
  @Override
  public void onBackPressed()
  {
    Lifecycle.runProfileActivity( EditPreferencesActivity.this );
    finish();
  }

  private View.OnClickListener getCheckboxListener( final CheckBox checkBox )
  {
    return new View.OnClickListener()
    {
      @Override
      public void onClick( View view )
      {
        final View check = view;
        String preferenceName = ((CheckBox) view).getText().toString();
        boolean isChecked = checkBox.isChecked();
        UserPreferences currentPreference;

        if( isChecked )
        {
          check.setEnabled( false );
          currentPreference = new UserPreferences( Backendless.UserService.CurrentUser().getEmail(), preferenceName, preferenceTheme.getPreference() );
          preferenceStore.save( currentPreference, new ResponseAsyncCallback<UserPreferences>( EditPreferencesActivity.this )
          {
            @Override
            public void handleResponse( UserPreferences response )
            {
              preferenceNameToPreferenceMap.put( response.getPreference(), response );
              check.setEnabled( true );
            }
          } );
        }
        else
        {
          check.setEnabled( false );
          currentPreference = preferenceNameToPreferenceMap.get( preferenceName );
          preferenceStore.remove( currentPreference, new ResponseAsyncCallback<Long>( EditPreferencesActivity.this )
          {
            @Override
            public void handleResponse( Long response )
            {
              check.setEnabled( true );
            }
          } );
        }
      }
    };
  }
}