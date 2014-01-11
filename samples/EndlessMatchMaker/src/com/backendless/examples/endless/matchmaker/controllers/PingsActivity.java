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
import android.widget.LinearLayout;
import com.backendless.examples.endless.matchmaker.R;
import com.backendless.examples.endless.matchmaker.controllers.shared.Lifecycle;
import com.backendless.examples.endless.matchmaker.controllers.shared.ResponseAsyncCallback;
import com.backendless.examples.endless.matchmaker.utils.Defaults;
import com.backendless.examples.endless.matchmaker.views.UIFactory;
import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.geo.BackendlessGeoQuery;
import com.backendless.geo.GeoPoint;
import com.backendless.geo.SearchMatchesResult;

import java.util.HashMap;
import java.util.Map;

public class PingsActivity extends Activity
{
  private LinearLayout pingedContainer, werePingedContainer, callbackPingsContainer;
  private ProgressDialog progressDialog;
  private GeoPoint currentUserGeoPoint;
  private String food = "Food";
  private String music = "Music";
  private String hobbies = "Hobbies";
  private String travel = "Travel";

  @Override
  public void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.pings );

    progressDialog = UIFactory.getDefaultProgressDialog( this );

    pingedContainer = (LinearLayout) findViewById( R.id.pingedContainer );
    werePingedContainer = (LinearLayout) findViewById( R.id.werePingedContainer );
    callbackPingsContainer = (LinearLayout) findViewById( R.id.callbackPingsContainer );

    currentUserGeoPoint = (GeoPoint) getIntent().getSerializableExtra( Defaults.CURRENT_USER_GEO_POINT_BUNDLE_TAG );

    Map<String, String> metaDataForSearch = new HashMap<String, String>();
    metaDataForSearch.put( "Asian", food );
    metaDataForSearch.put( "Caribean", food );
    metaDataForSearch.put( "Bar food", food );
    metaDataForSearch.put( "French", food );
    metaDataForSearch.put( "Mediterranean", food );
    metaDataForSearch.put( "Greek", food );
    metaDataForSearch.put( "Spanish", food );
    metaDataForSearch.put( "Mexican", food );
    metaDataForSearch.put( "Thai", food );
    //Music
    metaDataForSearch.put( "Classical", music );
    metaDataForSearch.put( "Jazz", music );
    metaDataForSearch.put( "Hip-hop", music );
    metaDataForSearch.put( "Reggae", music );
    metaDataForSearch.put( "Blues", music );
    metaDataForSearch.put( "Trance", music );
    metaDataForSearch.put( "House", music );
    metaDataForSearch.put( "Rock", music );
    metaDataForSearch.put( "Folk", music );
    //  Hobbies
    metaDataForSearch.put( "Fishing", hobbies );
    metaDataForSearch.put( "Diving", hobbies );
    metaDataForSearch.put( "Rock climbing", hobbies );
    metaDataForSearch.put( "Hiking", hobbies );
    metaDataForSearch.put( "Reading", hobbies );
    metaDataForSearch.put( "Dancing", hobbies );
    metaDataForSearch.put( "Cooking", hobbies );
    metaDataForSearch.put( "Surfing", hobbies );
    metaDataForSearch.put( "Photography", hobbies );
    //Travel
    metaDataForSearch.put( "Cruise", travel );
    metaDataForSearch.put( "B&B", travel );
    metaDataForSearch.put( "Europe", travel );
    metaDataForSearch.put( "Asia", travel );
    metaDataForSearch.put( "Caribean", travel );
    metaDataForSearch.put( "Mountains", travel );
    metaDataForSearch.put( "Whale watching", travel );
    metaDataForSearch.put( "Active travel", travel );

    int maxPoints = 10;
    BackendlessGeoQuery backendlessGeoQuery = new BackendlessGeoQuery( metaDataForSearch, maxPoints );
    backendlessGeoQuery.setPageSize( 50 );
    backendlessGeoQuery.setIncludeMeta( true );

    Backendless.Geo.relativeFind( backendlessGeoQuery, gotPingsCallback );
  }

  //Listeners section

  public View.OnClickListener getPingDetailListener( final GeoPoint targetUserGeoPoint )
  {
    return new View.OnClickListener()
    {
      @Override
      public void onClick( View view )
      {
        Lifecycle.runMatchViewActivityFromPings( PingsActivity.this, currentUserGeoPoint, targetUserGeoPoint, "1" );
      }
    };
  }

  @Override
  public void onBackPressed()
  {
    Lifecycle.runFindMatchesActivity( PingsActivity.this );
    finish();
  }

  //Callbacks section
  private AsyncCallback<BackendlessCollection<SearchMatchesResult>> gotPingsCallback = new ResponseAsyncCallback<BackendlessCollection<SearchMatchesResult>>( PingsActivity.this )
  {
    @Override
    public void handleResponse( BackendlessCollection<SearchMatchesResult> response )
    {
      for( SearchMatchesResult matchPoint : response.getCurrentPage() )
      {
        String matchUserEmail = matchPoint.getGeoPoint().getMetadata( BackendlessUser.EMAIL_KEY );
        String matchUserName = matchPoint.getGeoPoint().getMetadata( Defaults.NAME_PROPERTY );
        String gender = matchPoint.getGeoPoint().getMetadata( Defaults.GENDER_PROPERTY );
        String currentUserEmail = currentUserGeoPoint.getMetadata( BackendlessUser.EMAIL_KEY );
        Map<String, String> currentData = currentUserGeoPoint.getMetadata();
        Map<String, String> matchData = matchPoint.getGeoPoint().getMetadata();
        double matchCount = matchPoint.getMatches();

        if( matchCount <= 1 )
          matchCount = matchCount * 100;

        //Callback pings
        if( currentData.containsKey( matchUserEmail ) && matchData.containsKey( currentUserEmail ) )
        {
          callbackPingsContainer.addView( UIFactory.getPingsListElement( PingsActivity.this, matchUserName + " - " + matchUserEmail, round( matchCount, 1 ), getPingDetailListener( matchPoint.getGeoPoint() ), gender ) );
        }

        //You pinged
        if( matchData.containsKey( currentUserEmail ) && !currentData.containsKey( matchUserEmail ) )
        {
          pingedContainer.addView( UIFactory.getPingsListElement( PingsActivity.this, matchUserName, round( matchCount, 1 ), getPingDetailListener( matchPoint.getGeoPoint() ), gender ) );
        }

        //You were pinged
        if( currentData.containsKey( matchUserEmail ) && !matchData.containsKey( currentUserEmail ) )
        {
          werePingedContainer.addView( UIFactory.getPingsListElement( PingsActivity.this, matchUserName, round( matchCount, 1 ), getPingDetailListener( matchPoint.getGeoPoint() ), gender ) );
        }
      }
      progressDialog.cancel();
    }
  };

  public static double round( double value, int scale )
  {
    return Math.round( value * Math.pow( 10, scale ) ) / Math.pow( 10, scale );
  }
}
