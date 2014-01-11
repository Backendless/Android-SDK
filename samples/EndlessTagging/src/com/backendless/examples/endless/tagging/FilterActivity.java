package com.backendless.examples.endless.tagging;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.*;
import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.geo.GeoCategory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FilterActivity extends Activity
{
  private List<String> categoriesNames = new ArrayList<String>();
  private ProgressDialog progressDialog;
  private ListView listView;

  @Override
  public void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.act_filter );

    TextView textEndless = (TextView) findViewById( R.id.textFilter );
    final Typeface typeface = Typeface.createFromAsset( getAssets(), "fonts/verdana.ttf" );
    textEndless.setTypeface( typeface );

    progressDialog = ProgressDialog.show( FilterActivity.this, "", "Loading", true );
    Backendless.Geo.getCategories( new AsyncCallback<List<GeoCategory>>()
    {
      @Override
      public void handleResponse( List<GeoCategory> geoCategories )
      {
        List<GeoCategory> categories = geoCategories;
        for( GeoCategory category : categories )
          categoriesNames.add( category.getName() );
        listView = (ListView) findViewById( R.id.lvMain );
        listView.setChoiceMode( ListView.CHOICE_MODE_MULTIPLE );
        ArrayAdapter<String> adapter = new ArrayAdapter<String>( FilterActivity.this, android.R.layout.simple_list_item_multiple_choice, categoriesNames );
        listView.setAdapter( adapter );
        progressDialog.cancel();
        Button searchBtnMulti = (Button) findViewById( R.id.searchBtnMulti );
        searchBtnMulti.setTypeface( typeface );
        searchBtnMulti.setOnClickListener( new View.OnClickListener()
        {
          @Override
          public void onClick( View v )
          {
            progressDialog = ProgressDialog.show( FilterActivity.this, "", "Loading", true );
            SparseBooleanArray sbArray = listView.getCheckedItemPositions();
            List<String> searchCategories = new ArrayList<String>();
            for( int i = 0; i < sbArray.size(); i++ )
            {
              int key = sbArray.keyAt( i );
              if( sbArray.get( key ) )
              {
                searchCategories.add( categoriesNames.get( key ) );
              }
            }
            String[] array = new String[ searchCategories.size() ];
            array = searchCategories.toArray( array );
            Intent intent = new Intent();
            intent.putExtra( Default.SEARCH_CATEGORY_NAME, array );
            setResult( Default.CATEGORY_RESULT_SEARCH, intent );
            progressDialog.cancel();
            finish();
          }
        } );

        Intent intent = getIntent();
        if( intent.getExtras() != null )
        {
          List<String> searchCheckBox = Arrays.asList( intent.getStringArrayExtra( Default.SEARCH_CHECK_BOX ) );
          SparseBooleanArray checkedStates = listView.getCheckedItemPositions();
          for( String element : searchCheckBox )
          {
            int position = adapter.getPosition( element );
            checkedStates.put( position, true );
          }
        }
      }

      @Override
      public void handleFault( BackendlessFault backendlessFault )
      {
        progressDialog.cancel();
        Toast.makeText( FilterActivity.this, backendlessFault.getMessage(), Toast.LENGTH_LONG ).show();
      }
    } );
  }
}
