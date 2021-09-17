package com.backendless.examples.dataservice.tododemo;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import com.backendless.Messaging;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.example.dataservice.tododemo.R;
import com.backendless.exceptions.BackendlessFault;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends ListFragment
{
  private TextView itemsToDoField;
  private TextView itemsDoneField;

  @Override
  public void onViewCreated( @NonNull View view, @Nullable Bundle savedInstanceState )
  {
    super.onViewCreated( view, savedInstanceState );
    ListView listView = getListView();
    listView.addHeaderView( getLayoutInflater().inflate( R.layout.header, null ), null, false );
    listView.addFooterView( getLayoutInflater().inflate( R.layout.footer, null ), null, false );
    listView.setFooterDividersEnabled( true );
    itemsToDoField = listView.findViewById( R.id.itemsToDoField );
    itemsDoneField = listView.findViewById( R.id.itemsDoneField );
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
    final TasksAdapter adapter = new TasksAdapter( getActivity(), new ArrayList<>(), tasksList );
    itemsDoneField.setOnClickListener( view1 -> adapter.removeAll( tasksList.getDoneEntities() ) );

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

    final EditText addToDoField = listView.findViewById( R.id.addToDoField );
    addToDoField.setOnKeyListener( ( view12, keyCode, keyEvent ) -> {
      if( keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_UP )
      {
        String message = addToDoField.getText().toString();

        if( message == null || message.equals( "" ) )
          return true;

        Task task = new Task();
        task.setDeviceId( Messaging.getDeviceId() );
        task.setTitle( message );

        TasksManager.saveEntity( task, MainFragment.this.getActivity(), response -> {
          adapter.add( response );
          addToDoField.setText( "" );
        } );

        return true;
      }
      return false;
    } );
  }
}