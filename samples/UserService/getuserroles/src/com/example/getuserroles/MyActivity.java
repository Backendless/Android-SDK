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

package com.example.getuserroles;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.util.ArrayList;
import java.util.List;

public class MyActivity extends Activity
{
  private ListView listView;
  private List<String> rolesName = new ArrayList<String>();
  private ProgressDialog progressDialog;
  /**
   * Called when the activity is first created.
   */
  @Override
  public void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.main );


    Backendless.initApp( MyActivity.this, Defaults.APP_ID, Defaults.SECRET_KEY, Defaults.VERSION );

    Button getBtn = (Button) findViewById( R.id.searchBtnMulti );
    getBtn.setOnClickListener(new View.OnClickListener()
    {
      @Override
      public void onClick( View v )
      {
        Backendless.UserService.login( "q@q.com", "111", new AsyncCallback<BackendlessUser>()
        {
          @Override
          public void handleResponse( BackendlessUser backendlessUser )
          {
            progressDialog = ProgressDialog.show( MyActivity.this, "", "Loading", true );
            Backendless.UserService.getUserRoles( new AsyncCallback<List<String>>()
            {
              @Override
              public void handleResponse( List<String> strings )
              {
                rolesName.clear();
                List<String> roles = strings;
                for( String role : roles )
                  rolesName.add( role );

                listView = (ListView) findViewById( R.id.lvMain );
                ArrayAdapter<String> adapter = new ArrayAdapter<String>( MyActivity.this, android.R.layout.simple_list_item_multiple_choice, rolesName );
                listView.setAdapter( adapter );
                progressDialog.cancel();
              }

              @Override
              public void handleFault( BackendlessFault backendlessFault )
              {
                progressDialog.cancel();
                Toast.makeText( MyActivity.this, backendlessFault.getMessage(), Toast.LENGTH_LONG ).show();
              }
            } );
          }

          @Override
          public void handleFault( BackendlessFault backendlessFault )
          {
            progressDialog.cancel();
            Toast.makeText( MyActivity.this, backendlessFault.getMessage(), Toast.LENGTH_LONG ).show();
          }
        } );
      }
    });

  }
}
