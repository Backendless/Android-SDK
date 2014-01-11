package com.backendless.examples.endless.matchmaker.controllers;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;
import com.backendless.Backendless;
import com.backendless.async.callback.BackendlessCallback;
import com.backendless.examples.endless.matchmaker.R;
import com.backendless.examples.endless.matchmaker.models.persistent.PreferencesDefaults;
import com.backendless.examples.endless.matchmaker.models.persistent.UserPreferences;
import com.backendless.examples.endless.matchmaker.utils.Defaults;


/**
 * Created with IntelliJ IDEA.
 * User: denis
 * Date: 8/12/13
 * Time: 10:56 AM
 * To change this template use File | Settings | File Templates.
 */
public class CreateTablesActivity extends Activity

{
  @Override
  public void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.create_tables );

    //Backendless.initApp( this, Defaults.APPLICATION_ID, Defaults.APPLICATION_SECRET_KEY, Defaults.APPLICATION_VERSION );
    //create PreferencesDefaults
    saveToTableData("Food", "Asian");
    saveToTableData("Food", "Caribean");
    saveToTableData("Food", "Bar food");
    saveToTableData("Food", "French");
    saveToTableData("Food", "Mediterranean");
    saveToTableData("Food", "Greek");
    saveToTableData("Food", "Spanish");
    saveToTableData("Food", "Mexican");
    saveToTableData("Food", "Thai");
    //Music
    saveToTableData("Music", "Classical");
    saveToTableData("Music", "Jazz");
    saveToTableData("Music", "Hip-hop");
    saveToTableData("Music", "Reggae");
    saveToTableData("Music", "Blues");
    saveToTableData("Music", "Trance");
    saveToTableData("Music", "House");
    saveToTableData("Music", "Rock");
    saveToTableData("Music", "Folk");
    //  Hobbies
    saveToTableData("Hobbies", "Fishing");
    saveToTableData("Hobbies", "Diving");
    saveToTableData("Hobbies", "Rock climbing");
    saveToTableData("Hobbies", "Hiking");
    saveToTableData("Hobbies", "Reading");
    saveToTableData("Hobbies", "Dancing");
    saveToTableData("Hobbies", "Cooking");
    saveToTableData("Hobbies", "Surfing");
    saveToTableData("Hobbies", "Photography");
    //Travel
    saveToTableData("Travel", "Cruise");
    saveToTableData("Travel", "B&B");
    saveToTableData("Travel", "Europe");
    saveToTableData("Travel", "Asia");
    saveToTableData("Travel", "Caribean");
    saveToTableData("Travel", "Mountains");
    saveToTableData("Travel", "Whale watching");
    saveToTableData("Travel", "Active travel");
    saveToTableData("Travel", "Passive travel");

    Toast.makeText( CreateTablesActivity.this, "A new table was successfully added", Toast.LENGTH_LONG ).show();
    //create UserPreferences
    saveDefaultColum("q@q.com", "Dancing", "Hobbies");
  }

  private void saveDefaultColum(String email, String preference, String theme)
  {
    UserPreferences userPreferences = new UserPreferences( email, preference, theme  );
    Backendless.Persistence.save(userPreferences, new BackendlessCallback<UserPreferences>()
    {
      @Override
      public void handleResponse( UserPreferences userPreferences )
      {
        Toast.makeText( CreateTablesActivity.this, "A new comment was successfully added", Toast.LENGTH_LONG ).show();
      }
    });
  }

  private void saveToTableData( String theme, String name )
  {
    PreferencesDefaults preferencesDefaults = new PreferencesDefaults( theme, name );
    Backendless.Persistence.save( preferencesDefaults, new BackendlessCallback<PreferencesDefaults>()
    {
      @Override
      public void handleResponse( PreferencesDefaults preferencesDefaults )
      {

      }
    } );
  }
}
