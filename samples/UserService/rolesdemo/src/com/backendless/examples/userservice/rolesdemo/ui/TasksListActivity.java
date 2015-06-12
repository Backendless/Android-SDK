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

package com.backendless.examples.userservice.rolesdemo.ui;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.BackendlessCallback;
import com.backendless.examples.userservice.rolesdemo.Defaults;
import com.backendless.examples.userservice.rolesdemo.R;
import com.backendless.examples.userservice.rolesdemo.Task;
import com.backendless.examples.userservice.rolesdemo.utils.BackendlessUtils;

public class TasksListActivity extends Activity
{
  @Override
  protected void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.acitivity_taskslist );

    final EditText newTask = (EditText) findViewById( R.id.task );
    final ArrayAdapter<String> adapter = new ArrayAdapter<String>( this, R.layout.list_task );
    final ListView listView = (ListView) findViewById( R.id.listView );
    listView.setAdapter( adapter );

    findViewById( R.id.add ).setOnClickListener( new View.OnClickListener()
    {
      @Override
      public void onClick( View v )
      {
        if( TextUtils.isEmpty( newTask.getText() ) )
        {
          newTask.setError( "Task cannot be empty" );
          newTask.requestFocus();

          return;
        }

        String taskName = newTask.getText().toString();
        BackendlessUser currentUser = Backendless.UserService.CurrentUser();
        String userId = (String) currentUser.getProperty( Defaults.IDENTITY );
        Task task = new Task( taskName, userId );

        BackendlessUtils.saveTask( task, listView, new BackendlessCallback<Task>()
        {
          @Override
          public void handleResponse( Task task )
          {
            adapter.add( task.getMessage() );
          }
        } );

        newTask.setText( "" );
        listView.requestFocus();
      }
    } );

    BackendlessUtils.initTasks( new BackendlessCallback<BackendlessCollection<Task>>()
    {
      @Override
      public void handleResponse( BackendlessCollection<Task> taskBackendlessCollection )
      {
        for( Task task : taskBackendlessCollection.getCurrentPage() )
          adapter.add( task.getMessage() );
      }
    } );
  }
}
