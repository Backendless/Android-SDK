package com.backendless.examples.endless.tagging;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.*;
import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.geo.GeoCategory;

import java.util.ArrayList;
import java.util.List;

public class CreateNewCategoryActivity extends Activity
{
  private List<String> categoriesNames = new ArrayList<String>();
  private ProgressDialog progressDialog;
  private ListView lvMain;
  private String[] array;

  @Override
  public void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.act_new_category );

    TextView textCat = (TextView) findViewById( R.id.textCat );
    TextView textCategory = (TextView) findViewById( R.id.textCategory );
    Typeface typeface = Typeface.createFromAsset( getAssets(), "fonts/verdana.ttf" );
    textCat.setTypeface( typeface );
    textCategory.setTypeface( typeface );

    progressDialog = ProgressDialog.show( CreateNewCategoryActivity.this, "", "Loading", true );
    Backendless.Geo.getCategories( new AsyncCallback<List<GeoCategory>>()
    {
      @Override
      public void handleResponse( List<GeoCategory> geoCategories )
      {
        List<GeoCategory> categories = geoCategories;
        for( GeoCategory category : categories )
          categoriesNames.add( category.getName() );

        lvMain = (ListView) findViewById( R.id.lvMain );
        lvMain.setChoiceMode( ListView.CHOICE_MODE_MULTIPLE );
        ArrayAdapter<String> adapter = new ArrayAdapter<String>( CreateNewCategoryActivity.this, android.R.layout.simple_list_item_multiple_choice, categoriesNames );
        lvMain.setAdapter( adapter );
        progressDialog.cancel();
      }

      @Override
      public void handleFault( BackendlessFault backendlessFault )
      {
        progressDialog.cancel();
        Toast.makeText( CreateNewCategoryActivity.this, backendlessFault.getMessage(), Toast.LENGTH_LONG ).show();
      }
    } );

    Button addCategoryBtn = (Button) findViewById( R.id.addCategoryBtn );
    addCategoryBtn.setTypeface( typeface );
    addCategoryBtn.setOnClickListener( new View.OnClickListener()
    {
      @Override
      public void onClick( View v )
      {
        EditText addNewCategory = (EditText) findViewById( R.id.editNewCategory );
        final String categoryCreateName = addNewCategory.getText().toString();
        SparseBooleanArray sbArray = lvMain.getCheckedItemPositions();
        List<String> searchCategories = new ArrayList<String>();
        for( int i = 0; i < sbArray.size(); i++ )
        {
          int key = sbArray.keyAt( i );
          if( sbArray.get( key ) )
          {
            searchCategories.add( categoriesNames.get( key ) );
          }
        }
        array = new String[ searchCategories.size() ];
        array = searchCategories.toArray( array );

        if( TextUtils.isEmpty( categoryCreateName ) && searchCategories.isEmpty() )
        {
          String alertMessage = "Please, fill in empty field or choose category!";
          Toast.makeText( CreateNewCategoryActivity.this, alertMessage, Toast.LENGTH_LONG ).show();
          return;
        }
        else if( categoryCreateName != null && searchCategories.isEmpty() )
        {
          progressDialog = ProgressDialog.show( CreateNewCategoryActivity.this, "", "Loading", true );
          Backendless.Geo.addCategory( categoryCreateName, new AsyncCallback<GeoCategory>()
          {
            @Override
            public void handleResponse( GeoCategory geoCategory )
            {
              Intent intent = new Intent( CreateNewCategoryActivity.this, AddCommentActivity.class );
              intent.putExtra( Default.NEW_CATEGORY_NAME, categoryCreateName );
              intent.putExtra( Default.CATEGORY_TRIGER, "1" );
              setResult( Default.ADD_NEW_CATEGORY_RESULT, intent );
              progressDialog.cancel();
              finish();
            }

            @Override
            public void handleFault( BackendlessFault backendlessFault )
            {
              progressDialog.cancel();
              Toast.makeText( CreateNewCategoryActivity.this, backendlessFault.getMessage(), Toast.LENGTH_LONG ).show();
            }
          } );
        }
        else if( TextUtils.isEmpty( categoryCreateName ) && !searchCategories.isEmpty() )
        {
          progressDialog = ProgressDialog.show( CreateNewCategoryActivity.this, "", "Loading", true );
          Intent intent = new Intent( CreateNewCategoryActivity.this, AddCommentActivity.class );
          intent.putExtra( Default.SEARCH_CATEGORY_NAME, array );
          intent.putExtra( Default.CATEGORY_TRIGER, "2" );
          setResult( Default.ADD_NEW_CATEGORY_RESULT, intent );
          progressDialog.cancel();
          finish();
        }
        else if( categoryCreateName != null && !searchCategories.isEmpty() )
        {
          progressDialog = ProgressDialog.show( CreateNewCategoryActivity.this, "", "Loading", true );
          Backendless.Geo.addCategory( categoryCreateName, new AsyncCallback<GeoCategory>()
          {
            @Override
            public void handleResponse( GeoCategory geoCategory )
            {
              Intent intent = new Intent( CreateNewCategoryActivity.this, AddCommentActivity.class );
              intent.putExtra( Default.NEW_CATEGORY_NAME, categoryCreateName );
              intent.putExtra( Default.SEARCH_CATEGORY_NAME, array );
              intent.putExtra( Default.CATEGORY_TRIGER, "3" );
              setResult( Default.ADD_NEW_CATEGORY_RESULT, intent );
              progressDialog.cancel();
              finish();
            }

            @Override
            public void handleFault( BackendlessFault backendlessFault )
            {
              progressDialog.cancel();
              Toast.makeText( CreateNewCategoryActivity.this, backendlessFault.getMessage(), Toast.LENGTH_LONG ).show();
            }
          } );
        }
      }
    } );
  }
}
