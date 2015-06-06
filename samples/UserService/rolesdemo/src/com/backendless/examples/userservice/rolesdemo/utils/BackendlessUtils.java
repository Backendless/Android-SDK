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

package com.backendless.examples.userservice.rolesdemo.utils;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;
import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.async.callback.BackendlessCallback;
import com.backendless.examples.userservice.rolesdemo.Defaults;
import com.backendless.examples.userservice.rolesdemo.Task;
import com.backendless.exceptions.BackendlessFault;

public class BackendlessUtils
{
  public static void signIn( final String userId, final String password, final String role, final View viewForProgress,
                             final Intent intent )
  {
    login( userId, password, new BackendlessCallback<Boolean>()
    {
      @Override
      public void handleResponse( Boolean loggedIn )
      {
        if( loggedIn )
        {
          viewForProgress.getContext().startActivity( intent );
        }
        else
          register( userId, password, new BackendlessCallback<Boolean>()
          {
            @Override
            public void handleResponse( Boolean aBoolean )
            {
              assignRole( userId, role, new BackendlessCallback<Boolean>()
              {
                @Override
                public void handleResponse( Boolean aBoolean )
                {
                  signIn( userId, password, Defaults.READ_WRITE_ROLE, viewForProgress, intent );
                }
              } );
            }
          } );
      }
    } );
  }

  private static void login( String userId, String password, final AsyncCallback<Boolean> callback )
  {
    Backendless.UserService.login( userId, password, new AsyncCallback<BackendlessUser>()
    {
      @Override
      public void handleResponse( BackendlessUser user )
      {
        callback.handleResponse( true );
      }

      @Override
      public void handleFault( BackendlessFault backendlessFault )
      {
        callback.handleResponse( false );
      }
    } );
  }

  private static void assignRole( String userId, String role, final AsyncCallback<Boolean> callback )
  {
    Backendless.UserService.assignRole( userId, role, new BackendlessCallback<Void>()
    {
      @Override
      public void handleResponse( Void aVoid )
      {
        callback.handleResponse( true );
      }
    } );
  }

  private static void register( String userId, String password, final AsyncCallback<Boolean> callback )
  {
    BackendlessUser backendlessUser = new BackendlessUser();
    backendlessUser.setProperty( Defaults.IDENTITY, userId );
    backendlessUser.setPassword( password );

    Backendless.UserService.register( backendlessUser, new BackendlessCallback<BackendlessUser>()
    {
      @Override
      public void handleResponse( BackendlessUser user )
      {
        callback.handleResponse( true );
      }
    } );
  }

  public static void saveTask( final Task task, final View viewForProgress, final AsyncCallback<Task> responder )
  {
    Backendless.Persistence.save( task, new AsyncCallback<Task>()
    {
      @Override
      public void handleResponse( Task savedTask )
      {
        responder.handleResponse( savedTask );
      }

      @Override
      public void handleFault( BackendlessFault backendlessFault )
      {
        Toast.makeText( viewForProgress.getContext(), backendlessFault.getMessage(), Toast.LENGTH_LONG ).show();
      }
    } );
  }

  public static void getAllTasks( final AsyncCallback<BackendlessCollection<Task>> responder )
  {
    Backendless.Persistence.of( Task.class ).find( new BackendlessCallback<BackendlessCollection<Task>>()
    {
      @Override
      public void handleResponse( BackendlessCollection<Task> tasksBackendlessCollection )
      {
        responder.handleResponse( tasksBackendlessCollection );
      }
    } );
  }

  public static void initTasks( final AsyncCallback<BackendlessCollection<Task>> responder )
  {
    Backendless.Persistence.save( new Task( "HelloWorld task", "user" ), new BackendlessCallback<Task>()
    {
      @Override
      public void handleResponse( Task savedTask )
      {
        Backendless.Persistence.of( Task.class ).remove( savedTask, new BackendlessCallback<Long>()
        {
          @Override
          public void handleResponse( Long ignore )
          {
            getAllTasks( responder );
          }
        } );
      }
    } );
  }
}
