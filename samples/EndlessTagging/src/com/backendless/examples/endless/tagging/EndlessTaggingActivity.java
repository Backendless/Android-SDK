package com.backendless.examples.endless.tagging;

import android.app.*;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.geo.BackendlessGeoQuery;
import com.backendless.geo.GeoCategory;
import com.backendless.geo.GeoPoint;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.*;

import java.io.InputStream;
import java.util.*;

public class EndlessTaggingActivity extends Activity
{
  private GoogleMap googleMap;
  private LatLng myPosition;
  private List<String> selectedCategories = new ArrayList<String>();
  private List<String> categoriesNames = new ArrayList<String>();
  private List<String> pointCategories = new ArrayList<String>();
  private String categoryInLine = null;
  private String category = null;
  private String pointNameNew = null;
  private String photoUrl = null;
  private String filePath = null;
  private double latitude, longitude, NELat, NELon, SWLat, SWLon, latitudeOnMap, longitudeOnMap;
  private PointsAdapter adapter;
  private BackendlessGeoQuery backendlessGeoQuery;
  private ProgressDialog progressDialog;
  private Map<Marker, Bitmap> bitmapToMarkerMap = new HashMap<Marker, Bitmap>();
  private boolean onMapClick = false;

  @Override
  public void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.act_endless_tagging );

    TextView textEndless = (TextView) findViewById( R.id.textEndless );
    Typeface typeface = Typeface.createFromAsset( getAssets(), "fonts/verdana.ttf" );
    textEndless.setTypeface( typeface );

    adapter = new PointsAdapter( EndlessTaggingActivity.this );
    MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById( R.id.map );
    googleMap = mapFragment.getMap();
    googleMap.setMyLocationEnabled( true );

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
    {
      Toast.makeText( EndlessTaggingActivity.this, "No GPS signal", Toast.LENGTH_LONG ).show();
    }
    boolean networkIsEnabled = locationManager.isProviderEnabled( LocationManager.NETWORK_PROVIDER );
    if( !networkIsEnabled )
    {
      Toast.makeText( EndlessTaggingActivity.this, "No Network signal", Toast.LENGTH_LONG ).show();
    }
    String provider = locationManager.getBestProvider( criteria, true );
    LocationListener myLocationListener = new LocationListener()
    {
      @Override
      public void onLocationChanged( Location location )
      {
        //To change body of implemented methods use File | Settings | File Templates.
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
    if( location != null )
    {
      latitude = location.getLatitude();
      longitude = location.getLongitude();
      myPosition = new LatLng( latitude, longitude );
      googleMap.addMarker( new MarkerOptions().position( myPosition ).icon( BitmapDescriptorFactory.fromResource( R.drawable.marker_red ) ) );
      googleMap.moveCamera( CameraUpdateFactory.newLatLngZoom( myPosition, 11 ) );
      googleMap.animateCamera( CameraUpdateFactory.zoomTo( 14 ), 2000, null );
      getCategory();
    }
    else
      Toast.makeText( EndlessTaggingActivity.this, "Can't determine your location. Check the settings your device and restart the application.", Toast.LENGTH_LONG ).show();

    googleMap.setOnMapLongClickListener( new GoogleMap.OnMapLongClickListener()
    {
      @Override
      public void onMapLongClick( LatLng latLng )
      {
        latitudeOnMap = latLng.latitude;
        longitudeOnMap = latLng.longitude;
        onMapClick = true;
        AlertDialog.Builder builder = new AlertDialog.Builder( EndlessTaggingActivity.this );
        builder.setMessage( R.string.new_point );
        builder.setPositiveButton( R.string.yes, new DialogInterface.OnClickListener()
        {
          @Override
          public void onClick( DialogInterface dialog, int which )
          {
            Intent intent = new Intent( EndlessTaggingActivity.this, MakeChoiceActivity.class );
            startActivityForResult( intent, Default.ADD_NEW_PHOTO_RESULT );
          }
        } );
        builder.setNegativeButton( R.string.no, new DialogInterface.OnClickListener()
        {
          @Override
          public void onClick( DialogInterface dialog, int which )
          {

          }
        } );
        AlertDialog dialog = builder.create();
        dialog.show();
      }
    } );
    googleMap.setOnMarkerClickListener( new GoogleMap.OnMarkerClickListener()
    {
      @Override
      public boolean onMarkerClick( Marker marker )
      {
        GeoPoint pointEndless = adapter.getPointByMarker( marker );

        if( pointEndless == null || !pointEndless.getMetadata().containsKey( "endlessTagging" ) )
        {
          return true;
        }

        if( !bitmapToMarkerMap.containsKey( marker ) )
        {
          String markerPhotoUrl = pointEndless.getMetadata( "photoUrl" );
          new DownloadImageTask( marker ).execute( markerPhotoUrl );
        }
        else
          marker.showInfoWindow();

        return true;
      }
    } );
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

        return view;
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
        searchRectanglePoints( selectedCategories.isEmpty() ? categoriesNames : selectedCategories );
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
          final String geoPointId = pointData.getObjectId();
          String name = null;
          String pointName = newMetaData.get( "pointName" );
          List<String> categoriesPoint = pointData.getCategories();
          for( String category : categoriesPoint )
          {
            name = category.toString();
          }
          if( pointName.equals( marker.getTitle() ) )
          {
            Intent intent = new Intent( EndlessTaggingActivity.this, PointCommentsActivity.class );
            intent.putExtra( Default.SEARCH_CATEGORY_NAME, name );
            intent.putExtra( Default.GEO_POINT_ID, geoPointId );
            intent.putExtra( Default.SEND_BITMAP, bitmapToMarkerMap.get( marker ) );
            startActivityForResult( intent, Default.ADD_NEW_COMMENT );
          }
        }
      }
    } );

    Button tagBtn = (Button) findViewById( R.id.tagBtn );
    tagBtn.setTypeface( typeface );
    tagBtn.setOnClickListener( new View.OnClickListener()
    {
      @Override
      public void onClick( View v )
      {
        Intent intent = new Intent( EndlessTaggingActivity.this, MakeChoiceActivity.class );
        startActivityForResult( intent, Default.ADD_NEW_PHOTO_RESULT );
      }
    } );

    Button filterBtn = (Button) findViewById( R.id.filterBtn );
    filterBtn.setTypeface( typeface );
    filterBtn.setOnClickListener( new View.OnClickListener()
    {
      @Override
      public void onClick( View v )
      {
        if( selectedCategories.size() == 0 )
        {
          Intent intent = new Intent( EndlessTaggingActivity.this, FilterActivity.class );
          startActivityForResult( intent, Default.CATEGORY_RESULT_SEARCH );
        }
        else
        {
          Intent intent = new Intent( EndlessTaggingActivity.this, FilterActivity.class );
          String[] array = new String[ selectedCategories.size() ];
          array = selectedCategories.toArray( array );
          intent.putExtra( Default.SEARCH_CHECK_BOX, array );
          startActivityForResult( intent, Default.CATEGORY_RESULT_SEARCH );
        }
      }
    } );
  }

  public static Bitmap scaleDownBitmap( Bitmap photo, int newHeight, Context context )
  {

    final float densityMultiplier = context.getResources().getDisplayMetrics().density;

    int h = (int) (newHeight * densityMultiplier);
    int w = (int) (h * photo.getWidth() / ((double) photo.getHeight()));

    photo = Bitmap.createScaledBitmap( photo, w, h, true );

    return photo;
  }

  public class DownloadImageTask extends AsyncTask<String, Void, Bitmap>
  {
    Marker marker;

    public DownloadImageTask( Marker marker )
    {
      this.marker = marker;
    }

    protected Bitmap doInBackground( String... urls )
    {
      String urlDisplay = urls[ 0 ];
      Bitmap bitmap = null;
      try
      {
        InputStream inputStream = new java.net.URL( urlDisplay ).openStream();
        bitmap = BitmapFactory.decodeStream( inputStream );
      }
      catch( Exception e )
      {
        e.printStackTrace();
      }
      return bitmap;
    }

    protected void onPostExecute( Bitmap result )
    {
      if( result != null )
      {
        Bitmap sendBitmap = scaleDownBitmap( result, 200, EndlessTaggingActivity.this );
        bitmapToMarkerMap.put( marker, sendBitmap );
      }
      marker.showInfoWindow();
    }
  }

  @Override
  protected void onActivityResult( int requestCode, int resultCode, Intent data )
  {
    if( data == null )
      return;

    switch( requestCode )
    {
      case Default.CATEGORY_RESULT_SEARCH:
        googleMap.clear();
        googleMap.addMarker( new MarkerOptions().position( myPosition ).icon( BitmapDescriptorFactory.fromResource( R.drawable.marker_red ) ) );
        List<String> searchCategory = Arrays.asList( data.getStringArrayExtra( Default.SEARCH_CATEGORY_NAME ) );
        selectedCategories = new ArrayList<String>();
        selectedCategories = searchCategory;
        searchRectanglePoints( selectedCategories.isEmpty() ? categoriesNames : selectedCategories );
        break;
      case Default.ADD_NEW_CATEGORY_RESULT:
        String categoryTriger = data.getStringExtra( Default.CATEGORY_TRIGER );
        if( categoryTriger.equals( "1" ) )
          category = data.getStringExtra( Default.NEW_CATEGORY_NAME );
        if( categoryTriger.equals( "2" ) )
          pointCategories = Arrays.asList( data.getStringArrayExtra( Default.SEARCH_CATEGORY_NAME ) );
        if( categoryTriger.equals( "3" ) )
        {
          category = data.getStringExtra( Default.NEW_CATEGORY_NAME );
          pointCategories = Arrays.asList( data.getStringArrayExtra( Default.SEARCH_CATEGORY_NAME ) );
        }
        Intent intentComment = new Intent( EndlessTaggingActivity.this, AddCommentActivity.class );
        intentComment.putExtra( Default.FILE_PATH, filePath );
        startActivityForResult( intentComment, Default.ADD_NEW_POINT_RESULT );
        break;
      case Default.ADD_NEW_POINT_RESULT:
        pointNameNew = data.getStringExtra( Default.NEW_POINT_NAME );
        googleMap.clear();

        if( category != null && pointCategories.isEmpty() )
        {
          AlertDialog.Builder builder = new AlertDialog.Builder( EndlessTaggingActivity.this );
          builder.setMessage( R.string.save_data );
          builder.setPositiveButton( R.string.yes, new DialogInterface.OnClickListener()
          {
            @Override
            public void onClick( DialogInterface dialog, int which )
            {
              List<String> categories = new ArrayList<String>();
              categories.add( category );
              Map<String, String> metadata = new HashMap<String, String>();
              metadata.put( "pointName", pointNameNew );
              metadata.put( "pointDescription", category );
              metadata.put( "photoUrl", photoUrl );
              metadata.put( "endlessTagging", "true" );
              if( onMapClick )
              {
                latitude = latitudeOnMap;
                longitude = longitudeOnMap;
              }
              GeoPoint geoPoint = new GeoPoint( latitude, longitude, categories, metadata );
              progressDialog = ProgressDialog.show( EndlessTaggingActivity.this, "", "Loading", true );
              Backendless.Geo.savePoint( geoPoint, new AsyncCallback<GeoPoint>()
              {
                @Override
                public void handleResponse( GeoPoint geoPoint )
                {
                  adapter.add( geoPoint );
                  progressDialog.cancel();
                  selectedCategories = new ArrayList<String>();
                  selectedCategories.add( category );
                  searchRectanglePoints( selectedCategories.isEmpty() ? categoriesNames : selectedCategories );
                }

                @Override
                public void handleFault( BackendlessFault backendlessFault )
                {
                  progressDialog.cancel();
                  Toast.makeText( EndlessTaggingActivity.this, backendlessFault.getMessage(), Toast.LENGTH_LONG ).show();
                }
              } );
            }
          } );
          builder.setNegativeButton( R.string.no, new DialogInterface.OnClickListener()
          {
            @Override
            public void onClick( DialogInterface dialog, int which )
            {
              Intent intent = new Intent( EndlessTaggingActivity.this, AddCommentActivity.class );
              startActivity( intent );
              finish();
            }
          } );
          AlertDialog dialog = builder.create();
          dialog.show();
        }
        else if( category == null && !pointCategories.isEmpty() )
        {
          AlertDialog.Builder builder = new AlertDialog.Builder( EndlessTaggingActivity.this );
          builder.setMessage( R.string.save_data );
          builder.setPositiveButton( R.string.yes, new DialogInterface.OnClickListener()
          {
            @Override
            public void onClick( DialogInterface dialog, int which )
            {
              List<String> categories = new ArrayList<String>();
              categories.addAll( pointCategories );
              StringBuilder result = new StringBuilder();
              for( int i = 0; i < pointCategories.size(); i++ )
              {
                result.append( pointCategories.get( i ) );
                if( i < pointCategories.size() - 1 )
                  result.append( ", " );
              }
              categoryInLine = result.toString();
              Map<String, String> metadata = new HashMap<String, String>();
              metadata.put( "pointName", pointNameNew );
              metadata.put( "pointDescription", categoryInLine );
              metadata.put( "photoUrl", photoUrl );
              metadata.put( "endlessTagging", "true" );
              if( onMapClick )
              {
                latitude = latitudeOnMap;
                longitude = longitudeOnMap;
              }
              GeoPoint geoPoint = new GeoPoint( latitude, longitude, categories, metadata );
              progressDialog = ProgressDialog.show( EndlessTaggingActivity.this, "", "Loading", true );
              Backendless.Geo.savePoint( geoPoint, new AsyncCallback<GeoPoint>()
              {
                @Override
                public void handleResponse( GeoPoint geoPoint )
                {
                  adapter.add( geoPoint );
                  selectedCategories = new ArrayList<String>();
                  selectedCategories.addAll( pointCategories );
                  searchRectanglePoints( selectedCategories.isEmpty() ? categoriesNames : selectedCategories );
                  progressDialog.cancel();
                }

                @Override
                public void handleFault( BackendlessFault backendlessFault )
                {
                  progressDialog.cancel();
                  Toast.makeText( EndlessTaggingActivity.this, backendlessFault.getMessage(), Toast.LENGTH_LONG ).show();
                }
              } );
            }
          } );
          builder.setNegativeButton( R.string.no, new DialogInterface.OnClickListener()
          {
            @Override
            public void onClick( DialogInterface dialog, int which )
            {
              Intent intent = new Intent( EndlessTaggingActivity.this, AddCommentActivity.class );
              startActivity( intent );
              finish();
            }
          } );
          AlertDialog dialog = builder.create();
          dialog.show();
        }
        else if( category != null && !pointCategories.isEmpty() )
        {
          AlertDialog.Builder builder = new AlertDialog.Builder( EndlessTaggingActivity.this );
          builder.setMessage( R.string.save_data );
          builder.setPositiveButton( R.string.yes, new DialogInterface.OnClickListener()
          {
            @Override
            public void onClick( DialogInterface dialog, int which )
            {
              List<String> categories = new ArrayList<String>();
              categories.add( category );
              categories.addAll( pointCategories );
              StringBuilder result = new StringBuilder();
              for( int i = 0; i < categories.size(); i++ )
              {
                result.append( categories.get( i ) );
                if( i < categories.size() - 1 )
                  result.append( ", " );
              }
              categoryInLine = result.toString();
              Map<String, String> metadata = new HashMap<String, String>();
              metadata.put( "pointName", pointNameNew );
              metadata.put( "pointDescription", categoryInLine );
              metadata.put( "photoUrl", photoUrl );
              metadata.put( "endlessTagging", "true" );
              if( onMapClick )
              {
                latitude = latitudeOnMap;
                longitude = longitudeOnMap;
              }
              GeoPoint geoPoint = new GeoPoint( latitude, longitude, categories, metadata );
              progressDialog = ProgressDialog.show( EndlessTaggingActivity.this, "", "Loading", true );
              Backendless.Geo.savePoint( geoPoint, new AsyncCallback<GeoPoint>()
              {
                @Override
                public void handleResponse( GeoPoint geoPoint )
                {
                  adapter.add( geoPoint );
                  progressDialog.cancel();
                  selectedCategories = new ArrayList<String>();
                  selectedCategories.add( category );
                  selectedCategories.addAll( pointCategories );
                  searchRectanglePoints( selectedCategories.isEmpty() ? categoriesNames : selectedCategories );
                }

                @Override
                public void handleFault( BackendlessFault backendlessFault )
                {
                  progressDialog.cancel();
                  Toast.makeText( EndlessTaggingActivity.this, backendlessFault.getMessage(), Toast.LENGTH_LONG ).show();
                }
              } );
            }
          } );
          builder.setNegativeButton( R.string.no, new DialogInterface.OnClickListener()
          {
            @Override
            public void onClick( DialogInterface dialog, int which )
            {
              Intent intent = new Intent( EndlessTaggingActivity.this, AddCommentActivity.class );
              startActivity( intent );
              finish();
            }
          } );
          AlertDialog dialog = builder.create();
          dialog.show();
        }
        break;
      case Default.ADD_NEW_COMMENT:
        googleMap.clear();
        googleMap.addMarker( new MarkerOptions().position( myPosition ).icon( BitmapDescriptorFactory.fromResource( R.drawable.marker_red ) ) );
        String nameCategory = data.getStringExtra( Default.SEARCH_CATEGORY_NAME );
        selectedCategories = new ArrayList<String>();
        selectedCategories.add( nameCategory );
        searchRectanglePoints( selectedCategories.isEmpty() ? categoriesNames : selectedCategories );
        break;
      case Default.ADD_NEW_PHOTO_RESULT:
        photoUrl = data.getStringExtra( Default.PHOTO_CAMERA_URL );
        if( photoUrl == null )
          photoUrl = data.getStringExtra( Default.PHOTO_BROWSE_URL );
        filePath = data.getStringExtra( Default.FILE_PATH );
        Intent intent = new Intent( EndlessTaggingActivity.this, CreateNewCategoryActivity.class );
        startActivityForResult( intent, Default.ADD_NEW_CATEGORY_RESULT );
        break;
      default:
        break;
    }
  }

  private void searchRectanglePoints( List<String> categoriesNames )
  {
    final ProgressBar progressBar = (ProgressBar) findViewById( R.id.progressBar );
    final TextView textView = (TextView) findViewById( R.id.textLoading );
    textView.setVisibility( TextView.VISIBLE );
    progressBar.setVisibility( ProgressBar.VISIBLE );
    backendlessGeoQuery = new BackendlessGeoQuery();
    backendlessGeoQuery.setSearchRectangle( new double[] { NELat, SWLon, SWLat, NELon } );
    backendlessGeoQuery.setCategories( categoriesNames );

    Backendless.Geo.getPoints( backendlessGeoQuery, new AsyncCallback<BackendlessCollection<GeoPoint>>()
    {
      @Override
      public void handleResponse( BackendlessCollection<GeoPoint> geoPointBackendlessCollection )
      {
        List<GeoPoint> points = geoPointBackendlessCollection.getCurrentPage();
        double newLatitude, newLongitude;
        adapter.clear();

        if( !points.isEmpty() )
          adapter.addAll( points );

        adapter.notifyDataSetChanged();

        for( GeoPoint point : points )
        {
          newLatitude = point.getLatitude();
          newLongitude = point.getLongitude();
          Map<String, String> newMetaData = point.getMetadata();
          String endlessTagging = newMetaData.get( "endlessTagging" );
          if( endlessTagging != null )
          {
            String pointName = newMetaData.get( "pointName" );
            String pointDescription = newMetaData.get( "pointDescription" );
            LatLng newPosition = new LatLng( newLatitude, newLongitude );
            googleMap.addMarker( new MarkerOptions().position( newPosition ).title( pointName ).snippet( pointDescription ).icon( BitmapDescriptorFactory.fromResource( R.drawable.marker_blue ) ) );
          }
        }
        textView.setVisibility( TextView.INVISIBLE );
        progressBar.setVisibility( ProgressBar.INVISIBLE );
      }

      @Override
      public void handleFault( BackendlessFault backendlessFault )
      {
        textView.setVisibility( TextView.INVISIBLE );
        progressBar.setVisibility( ProgressBar.INVISIBLE );
        Toast.makeText( EndlessTaggingActivity.this, backendlessFault.getMessage(), Toast.LENGTH_LONG ).show();
      }
    } );
  }

  public void getCategory()
  {
    progressDialog = ProgressDialog.show( EndlessTaggingActivity.this, "", "Loading", true );
    Backendless.Geo.getCategories( new AsyncCallback<List<GeoCategory>>()
    {
      @Override
      public void handleResponse( List<GeoCategory> geoCategories )
      {
        List<GeoCategory> categories = geoCategories;

        for( GeoCategory category : categories )
          categoriesNames.add( category.getName() );
        searchRectanglePoints( selectedCategories.isEmpty() ? categoriesNames : selectedCategories );
        progressDialog.cancel();
      }

      @Override
      public void handleFault( BackendlessFault backendlessFault )
      {
        progressDialog.cancel();
        Toast.makeText( EndlessTaggingActivity.this, backendlessFault.getMessage(), Toast.LENGTH_LONG ).show();
      }
    } );
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

