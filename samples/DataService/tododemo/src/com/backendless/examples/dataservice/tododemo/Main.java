package com.backendless.examples.dataservice.tododemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.backendless.Backendless;
import com.backendless.Messaging;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.util.ArrayList;
import java.util.List;

public class Main extends ListActivity
{
  private TextView itemsToDoField;
  private TextView itemsDoneField;

  @Override
  public void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    if( Defaults.APPLICATION_ID.equals( "" ) || Defaults.SECRET_KEY.equals( "" ) || Defaults.VERSION.equals( "" ) )
    {
      showAlert( this, "Missing application ID and secret key arguments. Login to Backendless Console, select your app and get the ID and key from the Manage > App Settings screen. Copy/paste the values into the Backendless.initApp call" );
      return;
    }

    Backendless.initApp( this, Defaults.APPLICATION_ID, Defaults.SECRET_KEY, Defaults.VERSION );

    ListView listView = getListView();
    listView.addHeaderView( getLayoutInflater().inflate( R.layout.header, null ), null, false );
    listView.addFooterView( getLayoutInflater().inflate( R.layout.footer, null ), null, false );
    listView.setFooterDividersEnabled( true );
    itemsToDoField = (TextView) findViewById( R.id.itemsToDoField );
    itemsDoneField = (TextView) findViewById( R.id.itemsDoneField );
    final TasksList tasksList = new TasksList( new TasksList.Listener()
    {
      @Override
      public void onLeftEntitiesChanged( int entitiesCount )
      {
        itemsToDoField.setText( entitiesCount == 0 ? "" : entitiesCount + " items left" );
      }

      @Override
      public void onDoneEntitiesChanged( int entitiesCount )
      {
        itemsDoneField.setText( entitiesCount == 0 ? "" : "Clear " + entitiesCount + " completed items" );
      }
    } );
    final TasksAdapter adapter = new TasksAdapter( this, new ArrayList<Task>(), tasksList );
    itemsDoneField.setOnClickListener( new View.OnClickListener()
    {
      @Override
      public void onClick( View view )
      {
        adapter.removeAll( tasksList.getDoneEntities() );
      }
    } );

    setListAdapter( adapter );
    TasksManager.findEntities( new AsyncCallback<List<Task>>()
    {
      @Override
      public void handleResponse( List<Task> response )
      {
        adapter.addAll( response );
      }

      @Override
      public void handleFault( BackendlessFault fault )
      {
      }
    } );

    final EditText addToDoField = (EditText) findViewById( R.id.addToDoField );
    addToDoField.setOnKeyListener( new View.OnKeyListener()
    {
      @Override
      public boolean onKey( View view, int keyCode, KeyEvent keyEvent )
      {
        if( keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_UP )
        {
          String message = addToDoField.getText().toString();

          if( message == null || message.equals( "" ) )
            return true;

          Task task = new Task();
          task.setDeviceId( Messaging.DEVICE_ID );
          task.setTitle( message );

          TasksManager.saveEntity( task, Main.this, new InnerCallback<Task>()
          {
            @Override
            public void handleResponse( Task response )
            {
              adapter.add( response );
              addToDoField.setText( "" );
            }
          } );

          return true;
        }
        return false;
      }
    } );
  }

  public static void showAlert( final Activity context, String message )
  {
    new AlertDialog.Builder( context ).setTitle( "An error occurred" ).setMessage( message ).setPositiveButton( "OK", new DialogInterface.OnClickListener()
    {
      @Override
      public void onClick( DialogInterface dialogInterface, int i )
      {
        context.finish();
      }
    } ).show();
  }
}