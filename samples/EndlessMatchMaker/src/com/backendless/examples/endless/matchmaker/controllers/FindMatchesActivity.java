package com.backendless.examples.endless.matchmaker.controllers;

import android.app.*;
import android.content.Context;
import android.provider.Settings.Secure;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import com.backendless.Messaging;
import com.backendless.examples.endless.matchmaker.R;
import com.backendless.examples.endless.matchmaker.views.UIFactory;
import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.examples.endless.matchmaker.controllers.shared.Lifecycle;
import com.backendless.examples.endless.matchmaker.controllers.shared.ResponseAsyncCallback;
import com.backendless.examples.endless.matchmaker.models.persistent.UserPreferences;
import com.backendless.examples.endless.matchmaker.utils.Defaults;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.geo.BackendlessGeoQuery;
import com.backendless.geo.GeoPoint;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.QueryOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.*;

import java.util.*;

public class FindMatchesActivity extends Activity
{
  private GoogleMap googleMap;
  private LatLng myPosition;
  private GeoPoint myLocation;
  private PointsAdapter adapter;
  private ProgressDialog progressDialog;
  private Map<Marker, Bitmap> bitmapToMarkerMap = new HashMap<Marker, Bitmap>();
  private Map<String, String> userPreferencesMap = new HashMap<String, String>();
  private ProgressBar progressBar;
  private TextView matchesFound, textFoundGlobal;
  private double latitude, longitude, NELat, NELon, SWLat, SWLon;
  private final static String PREF_TAG = "ENDLESS_LOGIN";
  private final static String EMAIL_PREF = "email";

  @Override
  public void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.findmatches );

    adapter = new PointsAdapter( FindMatchesActivity.this );
    MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById( R.id.map );
    googleMap = mapFragment.getMap();
    googleMap.setMyLocationEnabled( true );

    progressDialog = UIFactory.getDefaultProgressDialog( this );
    progressBar = (ProgressBar) findViewById( R.id.progressBar );
    matchesFound = (TextView) findViewById( R.id.matchesFound );
    matchesFound.setText( "0" );
    textFoundGlobal = (TextView) findViewById( R.id.textFoundGlobal );
    textFoundGlobal.setText( "0" );

    LocationManager locationManager = (LocationManager) getSystemService( LOCATION_SERVICE );
    Criteria criteria = new Criteria();
    criteria.setAccuracy( Criteria.ACCURACY_FINE );
    criteria.setPowerRequirement( Criteria.POWER_LOW );
    criteria.setAltitudeRequired( false );
    criteria.setBearingRequired( false );
    criteria.setSpeedRequired( false );
    criteria.setCostAllowed( true );
    boolean enabledGPS = locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER );

    if( !enabledGPS )
      Toast.makeText( FindMatchesActivity.this, "No GPS signal", Toast.LENGTH_LONG ).show();

    boolean networkIsEnabled = locationManager.isProviderEnabled( LocationManager.NETWORK_PROVIDER );

    if( !networkIsEnabled )
      Toast.makeText( FindMatchesActivity.this, "No Network signal", Toast.LENGTH_LONG ).show();

    String provider = locationManager.getBestProvider( criteria, true );
    LocationListener myLocationListener = new LocationListener()
    {
      @Override
      public void onLocationChanged( Location location )
      {
        if( location == null )
          Toast.makeText( FindMatchesActivity.this, "Location unknown", Toast.LENGTH_LONG ).show();
      }

      @Override
      public void onStatusChanged( String provider, int status, Bundle extras )
      {
        //To change body of implemented methods use File | Settings | File Templates.
      }

      @Override
      public void onProviderEnabled( String provider )
      {
        //To change body of implemented methods use File | Settings | File Templates.
      }

      @Override
      public void onProviderDisabled( String provider )
      {
        //To change body of implemented methods use File | Settings | File Templates.
      }
    };
    Location location = locationManager.getLastKnownLocation( provider );
    locationManager.requestSingleUpdate( criteria, myLocationListener, null );

    if( provider == null )
    {
      UIFactory.getLocationSettingsDialog( this ).show();
      finish();
    }

    if( location != null )
    {
      latitude = location.getLatitude();
      longitude = location.getLongitude();
      myLocation = new GeoPoint( latitude, longitude );
      myPosition = new LatLng( latitude, longitude );
      SharedPreferences settings = getSharedPreferences( PREF_TAG, 0 );
      String email = settings.getString( EMAIL_PREF, null );
      BackendlessGeoQuery backendlessGeoQuery = new BackendlessGeoQuery( BackendlessUser.EMAIL_KEY, email );
      Backendless.Geo.getPoints( backendlessGeoQuery, gotCurrentUserGeoPointCallback );
      BackendlessGeoQuery backendlessGeoQueryGlobal = new BackendlessGeoQuery();
      Backendless.Geo.getPoints( backendlessGeoQueryGlobal, new AsyncCallback<BackendlessCollection<GeoPoint>>()
      {
        @Override
        public void handleResponse( BackendlessCollection<GeoPoint> geoPointBackendlessCollection )
        {
          List<GeoPoint> geoPoints = geoPointBackendlessCollection.getCurrentPage();

          if( !geoPoints.isEmpty() )
            textFoundGlobal.setText( String.valueOf( geoPoints.size() - 1 ) );
        }

        @Override
        public void handleFault( BackendlessFault backendlessFault )
        {
          Toast.makeText( FindMatchesActivity.this, backendlessFault.getMessage(), Toast.LENGTH_LONG ).show();
        }
      } );
    }
    else
      Toast.makeText( FindMatchesActivity.this, "Can't determine your location. Check the settings your device and restart the application.", Toast.LENGTH_LONG ).show();

    googleMap.setInfoWindowAdapter( new GoogleMap.InfoWindowAdapter()
    {
      @Override
      public View getInfoWindow( Marker marker )
      {
        return null;
      }

      @Override
      public View getInfoContents( Marker marker )
      {
        View view = getLayoutInflater().inflate( R.layout.custom_info_window, null );
        ImageView imageView = (ImageView) view.findViewById( R.id.imagePhoto );

        if( bitmapToMarkerMap.containsKey( marker ) )
          imageView.setImageBitmap( bitmapToMarkerMap.get( marker ) );

        TextView title = (TextView) view.findViewById( R.id.textTitlePoint );
        title.setText( marker.getTitle() );
        TextView sniped = (TextView) view.findViewById( R.id.textSnipedPoint );
        sniped.setText( marker.getSnippet() );
        imageView.setImageResource( sniped.getText().equals( "male" ) ? R.drawable.avatar_default_male : R.drawable.avatar_default_female );

        return view;
      }
    } );
    googleMap.setOnInfoWindowClickListener( new GoogleMap.OnInfoWindowClickListener()
    {
      @Override
      public void onInfoWindowClick( Marker marker )
      {
        for( int i = 0; i < adapter.pointsList.size(); i++ )
        {
          GeoPoint pointData = adapter.pointsList.get( i );
          Map<String, String> newMetaData = pointData.getMetadata();
          String pointName = newMetaData.get( Defaults.NAME_PROPERTY );

          if( pointName.equals( marker.getTitle() ) )
            Lifecycle.runMatchViewActivity( FindMatchesActivity.this, myLocation, pointData );
        }
      }
    } );

    googleMap.setOnCameraChangeListener( new GoogleMap.OnCameraChangeListener()
    {
      @Override
      public void onCameraChange( CameraPosition cameraPosition )
      {
        LatLngBounds visibleRegion = googleMap.getProjection().getVisibleRegion().latLngBounds;
        LatLng topRight = visibleRegion.northeast;
        LatLng botDown = visibleRegion.southwest;
        SWLat = botDown.latitude;
        SWLon = botDown.longitude;
        NELat = topRight.latitude;
        NELon = topRight.longitude;
        findMatches();
      }
    } );

    findViewById( R.id.profileButton ).setOnClickListener( profileListener );
    findViewById( R.id.pingsButton ).setOnClickListener( pingsListener );
  }

  private void findMatches()
  {
    if( progressBar.getVisibility() == View.VISIBLE )
      return;

    progressBar.setVisibility( View.VISIBLE );
    BackendlessGeoQuery backendlessGeoQuery = new BackendlessGeoQuery();
    backendlessGeoQuery.setSearchRectangle( new double[] { NELat, SWLon, SWLat, NELon } );
    Backendless.Geo.getPoints( backendlessGeoQuery, gotPointsCallback );
  }

  //Callbacks section
  private AsyncCallback<BackendlessCollection<GeoPoint>> gotPointsCallback = new ResponseAsyncCallback<BackendlessCollection<GeoPoint>>( this )
  {
    @Override
    public void handleResponse( BackendlessCollection<GeoPoint> response )
    {
      List<GeoPoint> geoPoints = response.getCurrentPage();

      if( !geoPoints.isEmpty() )
      {

        for( GeoPoint geoPoint : geoPoints )
        {

          if( !geoPoint.getMetadata().get( BackendlessUser.EMAIL_KEY ).equals( Backendless.UserService.CurrentUser().getEmail() ) )
          {
            adapter.clear();
            adapter.addAll( geoPoints );
            adapter.notifyDataSetChanged();
            double newLatitude = geoPoint.getLatitude();
            double newLongitude = geoPoint.getLongitude();
            Map<String, String> newMetaData = geoPoint.getMetadata();
            String pointName = newMetaData.get( Defaults.NAME_PROPERTY );
            String pointDescription = newMetaData.get( Defaults.GENDER_PROPERTY );
            LatLng newPosition = new LatLng( newLatitude, newLongitude );
            googleMap.addMarker( new MarkerOptions().position( newPosition ).title( pointName ).snippet( pointDescription ).icon( BitmapDescriptorFactory.defaultMarker( pointDescription.equals( "male" ) ? BitmapDescriptorFactory.HUE_BLUE : BitmapDescriptorFactory.HUE_RED ) ) );
          }
        }
        matchesFound.setText( String.valueOf( geoPoints.size() - 1 ) );
      }
      progressBar.setVisibility( View.INVISIBLE );
    }
  };

  private AsyncCallback<BackendlessCollection<GeoPoint>> gotCurrentUserGeoPointCallback = new ResponseAsyncCallback<BackendlessCollection<GeoPoint>>( this )
  {
    @Override
    public void handleResponse( BackendlessCollection<GeoPoint> response )
    {
      List<GeoPoint> points = response.getCurrentPage();
      String hereMessage = "You are here";

      if( !points.isEmpty() )
      {
        myLocation = points.get( 0 );
        Iterable<Map.Entry<String, String>> entries = new ArrayList<Map.Entry<String, String>>( myLocation.getMetadata().entrySet() );
        myLocation.clearMetadata();

        for( Map.Entry<String, String> entry : entries )
        {
          String key = entry.getKey();

          if( entry.getValue().equals( Defaults.PING_TAG ) ||
                  key.equals( BackendlessUser.EMAIL_KEY ) ||
                  key.equals( Defaults.NAME_PROPERTY ) ||
                  key.equals( Defaults.BIRTH_DATE_PROPERTY ) ||
                  key.equals( Defaults.GENDER_PROPERTY ) )
          {
            myLocation.putMetadata( entry.getKey(), entry.getValue() );
          }
        }
        String gender = myLocation.getMetadata( Defaults.GENDER_PROPERTY );
        googleMap.addMarker( new MarkerOptions().position( myPosition ).title( hereMessage ).snippet( gender ).icon( BitmapDescriptorFactory.defaultMarker( gender.equals( "male" ) ? BitmapDescriptorFactory.HUE_BLUE : BitmapDescriptorFactory.HUE_RED ) ) );
        googleMap.moveCamera( CameraUpdateFactory.newLatLngZoom( myPosition, 11 ) );
        googleMap.animateCamera( CameraUpdateFactory.zoomTo( 14 ), 2000, null );
      }
      else
      {
        myLocation.putMetadata( BackendlessUser.EMAIL_KEY, Backendless.UserService.CurrentUser().getEmail() );
        myLocation.putMetadata( Defaults.NAME_PROPERTY, (String) Backendless.UserService.CurrentUser().getProperty( Defaults.NAME_PROPERTY ) );
        myLocation.putMetadata( Defaults.BIRTH_DATE_PROPERTY, Defaults.DEFAULT_DATE_FORMATTER.format( Backendless.UserService.CurrentUser().getProperty( Defaults.BIRTH_DATE_PROPERTY ) ) );
        myLocation.putMetadata( Defaults.GENDER_PROPERTY, String.valueOf( Backendless.UserService.CurrentUser().getProperty( Defaults.GENDER_PROPERTY ) ) );
        String gender = myLocation.getMetadata( Defaults.GENDER_PROPERTY );
        googleMap.addMarker( new MarkerOptions().position( myPosition ).title( hereMessage ).snippet( gender ).icon( BitmapDescriptorFactory.defaultMarker( gender.equals( "male" ) ? BitmapDescriptorFactory.HUE_BLUE : BitmapDescriptorFactory.HUE_RED ) ) );
        googleMap.moveCamera( CameraUpdateFactory.newLatLngZoom( myPosition, 11 ) );
        googleMap.animateCamera( CameraUpdateFactory.zoomTo( 14 ), 2000, null );
      }

      //SharedPreferences sharedPreferences = getSharedPreferences( getResources().getText( R.string.app_name ).toString(), MODE_PRIVATE );
      String deviceId = Messaging.DEVICE_ID;
      //deviceId = Secure.getString( FindMatchesActivity.this.getContentResolver(), Secure.ANDROID_ID );

      if( deviceId != null )
        myLocation.putMetadata( Defaults.DEVICE_REGISTRATION_ID_PROPERTY, deviceId );

      BackendlessDataQuery backendlessDataQuery = new BackendlessDataQuery( "email = '" + Backendless.UserService.CurrentUser().getEmail() + "'" );
      backendlessDataQuery.setQueryOptions( new QueryOptions( 50, 0 ) );
      Backendless.Persistence.of( UserPreferences.class ).find( backendlessDataQuery, new ResponseAsyncCallback<BackendlessCollection<UserPreferences>>( FindMatchesActivity.this )
      {
        @Override
        public void handleResponse( BackendlessCollection<UserPreferences> response )
        {
          List<UserPreferences> userPreferenceses = response.getCurrentPage();

          for( UserPreferences userPreferencese : userPreferenceses )
            userPreferencesMap.put( userPreferencese.getPreference(), userPreferencese.getTheme() );

          myLocation.putAllMetadata( userPreferencesMap );
          Backendless.Geo.savePoint( myLocation, new AsyncCallback<GeoPoint>()
          {
            @Override
            public void handleResponse( GeoPoint geoPoint )
            {
              progressDialog.cancel();
            }

            @Override
            public void handleFault( BackendlessFault backendlessFault )
            {
              progressDialog.cancel();
              Toast.makeText( FindMatchesActivity.this, backendlessFault.getMessage(), Toast.LENGTH_LONG ).show();
            }
          } );
        }
      } );
      progressDialog.cancel();
    }
  };

  //Listeners section
  private View.OnClickListener profileListener = new View.OnClickListener()
  {
    @Override
    public void onClick( View view )
    {
      progressDialog = UIFactory.getDefaultProgressDialog( FindMatchesActivity.this );
      Lifecycle.runProfileActivity( FindMatchesActivity.this );
      finish();
      progressDialog.cancel();
    }
  };

  private View.OnClickListener pingsListener = new View.OnClickListener()
  {
    @Override
    public void onClick( View view )
    {
      //Downloading pings for current user
      progressDialog = UIFactory.getDefaultProgressDialog( FindMatchesActivity.this );
      BackendlessGeoQuery backendlessGeoQuery = new BackendlessGeoQuery();
      backendlessGeoQuery.setPageSize( 50 );
      Backendless.Geo.getPoints( backendlessGeoQuery, gotPingsCallback );
    }
  };
  //Callbacks section
  private AsyncCallback<BackendlessCollection<GeoPoint>> gotPingsCallback = new ResponseAsyncCallback<BackendlessCollection<GeoPoint>>( FindMatchesActivity.this )
  {
    @Override
    public void handleResponse( BackendlessCollection<GeoPoint> response )
    {
      boolean triger = false;

      for( GeoPoint point : response.getCurrentPage() )
      {
        String matchUserEmail = point.getMetadata( BackendlessUser.EMAIL_KEY );
        String currentUserEmail = myLocation.getMetadata( BackendlessUser.EMAIL_KEY );
        Map<String, String> currentData = myLocation.getMetadata();
        Map<String, String> matchData = point.getMetadata();

        if( (currentData.containsKey( matchUserEmail ) && matchData.containsKey( currentUserEmail )) || (matchData.containsKey( currentUserEmail ) && !currentData.containsKey( matchUserEmail )) || (currentData.containsKey( matchUserEmail ) && !matchData.containsKey( currentUserEmail )) )
        {
          triger = true;
          Lifecycle.runPingsActivity( FindMatchesActivity.this, myLocation );
          progressDialog.cancel();
        }
      }

      if( triger == false )
        Toast.makeText( FindMatchesActivity.this, "You have no pings", Toast.LENGTH_LONG ).show();
      progressDialog.cancel();
    }
  };

  @Override
  public void onBackPressed()
  {
    Lifecycle.runProfileActivity( FindMatchesActivity.this );
    finish();
  }

  private static class PointsAdapter extends BaseAdapter
  {
    private LayoutInflater inflater;
    private List<GeoPoint> pointsList = new ArrayList<GeoPoint>();

    public PointsAdapter( Context context )
    {
      inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
    }

    public int getCount()
    {
      return pointsList.size();
    }

    public GeoPoint getItem( int position )
    {
      return pointsList.get( position );
    }

    public void add( GeoPoint geoPoint )
    {
      if( !pointsList.contains( geoPoint ) )
        pointsList.add( geoPoint );
    }

    public long getItemId( int position )
    {
      return position;
    }

    public View getView( int position, View convertView, ViewGroup parent )
    {
      return convertView;
    }

    public void addAll( Collection<? extends GeoPoint> geoPoints )
    {
      for( GeoPoint geoPoint : geoPoints )
        add( geoPoint );
    }

    public GeoPoint getPointByMarker( Marker marker )
    {
      for( int i = 0; i < pointsList.size(); i++ )
      {
        GeoPoint pointData = getItem( i );
        Map<String, String> newMetaData = pointData.getMetadata();
        String pointName = newMetaData.get( "pointName" );
        String markerName = marker.getTitle();
        if( pointName == null || markerName == null )
          return null;

        if( pointName.equals( markerName ) )
          return pointData;
      }

      return null;
    }

    public void clear()
    {
      pointsList.clear();
    }
  }
}

