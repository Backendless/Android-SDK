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

package com.backendless.fencedemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.geo.GeoPoint;
import com.backendless.geo.geofence.IGeofenceCallback;

import java.util.Iterator;

public class FenceDemo extends Activity
{
  private EditText fenceNameField;
  private GeoPoint geoPoint;

  @Override
  public void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.fence_demo_activity );

    Backendless.initApp( getBaseContext(), Defaults.APPLICATION_ID, Defaults.SECRET_KEY, Defaults.VERSION );

    initGeoPoint();
    initUI();
  }

  private void initGeoPoint()
  {
    // to create a geopoint
    geoPoint = new GeoPoint(  );

    //to add metadata for the created geopoint (optionally)
    geoPoint.addMetadata( "number", 7 );
    geoPoint.addMetadata( "string", "backendless" );
    geoPoint.addMetadata( "__deviceId", Backendless.Messaging.DEVICE_ID );
  }

  private void initUI()
  {
    fenceNameField = (EditText) findViewById( R.id.fence_name );

    findViewById( R.id.start_fence_monitoring_button ).setOnClickListener( new View.OnClickListener()
    {
      @Override
      public void onClick( View v )
      {
        startFenceMonitoring();
      }
    } );

    findViewById( R.id.stop_fence_monitoring_button ).setOnClickListener( new View.OnClickListener()
    {
      @Override
      public void onClick( View v )
      {
        stopFenceMonitoring();
      }
    } );

    findViewById( R.id.run_onenter_button ).setOnClickListener( new View.OnClickListener()
    {
      @Override
      public void onClick( View v )
      {
        runOnEnterAction();
      }
    } );

    findViewById( R.id.run_onstay_button ).setOnClickListener( new View.OnClickListener()
    {
      @Override
      public void onClick( View v )
      {
        runOnStayAction();
      }
    } );

    findViewById( R.id.run_onexit_button ).setOnClickListener( new View.OnClickListener()
    {
      @Override
      public void onClick( View v )
      {
        runOnExitAction();
      }
    } );

    findViewById( R.id.get_points_button ).setOnClickListener( new View.OnClickListener()
    {
      @Override
      public void onClick( View v )
      {
        getPointsFromFence();
      }
    } );
  }

  private void startFenceMonitoring()
  {
    boolean useCustomCallback = ((Switch) findViewById( R.id.customCallbackToggle )).isChecked();
    final String fenceName = fenceNameField.getText().toString();
    IGeofenceCallback customCallback = new CustomCallBack();

    if( fenceName == null || fenceName.isEmpty() )
    {
      if( useCustomCallback )
      {
        Backendless.Geo.startGeofenceMonitoring( customCallback, new AsyncCallback<Void>()
        {
          @Override
          public void handleResponse( Void aVoid )
          {
            Toast.makeText( FenceDemo.this, "Fences monitoring started", Toast.LENGTH_SHORT ).show();
          }

          @Override
          public void handleFault( BackendlessFault backendlessFault )
          {
            Toast.makeText( FenceDemo.this, "Failed: " + backendlessFault.toString(), Toast.LENGTH_LONG ).show();
          }
        } );
      }
      else
      {
        Backendless.Geo.startGeofenceMonitoring( geoPoint, new AsyncCallback<Void>()
        {
          @Override
          public void handleResponse( Void aVoid )
          {
            Toast.makeText( FenceDemo.this, "Fences monitoring started", Toast.LENGTH_SHORT ).show();
          }

          @Override
          public void handleFault( BackendlessFault backendlessFault )
          {
            Toast.makeText( FenceDemo.this, "Failed: " + backendlessFault.toString(), Toast.LENGTH_LONG ).show();
          }
        } );
      }
    }
    else
    {
      if( useCustomCallback )
      {
        Backendless.Geo.startGeofenceMonitoring( fenceName, customCallback, new AsyncCallback<Void>()
        {
          @Override
          public void handleResponse( Void aVoid )
          {
            Toast.makeText( FenceDemo.this, "Fence " + fenceName + " monitoring started", Toast.LENGTH_SHORT ).show();
          }

          @Override
          public void handleFault( BackendlessFault backendlessFault )
          {
            Toast.makeText( FenceDemo.this, "Failed: " + backendlessFault.toString(), Toast.LENGTH_LONG ).show();
          }
        } );
      }
      else
      {
        Backendless.Geo.startGeofenceMonitoring( fenceName, geoPoint, new AsyncCallback<Void>()
        {
          @Override
          public void handleResponse( Void aVoid )
          {
            Toast.makeText( FenceDemo.this, "Fence " + fenceName + " monitoring started", Toast.LENGTH_SHORT ).show();
          }

          @Override
          public void handleFault( BackendlessFault backendlessFault )
          {
            Toast.makeText( FenceDemo.this, "Failed: " + backendlessFault.toString(), Toast.LENGTH_LONG ).show();
          }
        } );
      }
    }
  }

  private void stopFenceMonitoring()
  {
    String fenceName = fenceNameField.getText().toString();

    if( fenceName.isEmpty() )
    {
      try
      {
        Backendless.Geo.stopGeofenceMonitoring();
        Toast.makeText( FenceDemo.this, "Success", Toast.LENGTH_SHORT ).show();
      }
      catch( Exception e )
      {
        Toast.makeText( FenceDemo.this, "Failed " + e.toString(), Toast.LENGTH_LONG ).show();
      }
    }
    else
    {
      try
      {
        Backendless.Geo.stopGeofenceMonitoring( fenceName );
        Toast.makeText( FenceDemo.this, "Success. Fence name: " + fenceName, Toast.LENGTH_SHORT ).show();
      }
      catch( Exception e )
      {
        Toast.makeText( FenceDemo.this, "Failed " + e.toString(), Toast.LENGTH_LONG ).show();
      }
    }
  }

  private void runOnEnterAction()
  {

    final String fenceName = fenceNameField.getText().toString();

    if( fenceName.isEmpty() )
    {
      Toast.makeText( FenceDemo.this, "Fence name is empty!", Toast.LENGTH_SHORT ).show();
    }
    else
    {
      Backendless.Geo.runOnEnterAction( fenceName, new AsyncCallback<Integer>()
      {
        @Override
        public void handleResponse( Integer response )
        {
          Toast.makeText( FenceDemo.this, "Action ran for: " + response + " points", Toast.LENGTH_SHORT ).show();
        }

        @Override
        public void handleFault( BackendlessFault fault )
        {
          Toast.makeText( FenceDemo.this, "Failed: " + fault.toString(), Toast.LENGTH_LONG ).show();
        }
      } );
    }
  }

  private void runOnStayAction()
  {
    final String fenceName = fenceNameField.getText().toString();

    if( fenceName.isEmpty() )
    {
      Toast.makeText( FenceDemo.this, "Fence name is empty!", Toast.LENGTH_SHORT ).show();
    }
    else
    {
      Backendless.Geo.runOnStayAction( fenceName, new AsyncCallback<Integer>()
      {
        @Override
        public void handleResponse( Integer response )
        {
          Toast.makeText( FenceDemo.this, "Action ran for: " + response + " points", Toast.LENGTH_SHORT ).show();
        }

        @Override
        public void handleFault( BackendlessFault fault )
        {
          Toast.makeText( FenceDemo.this, "Failed: " + fault.toString(), Toast.LENGTH_LONG ).show();
        }
      } );
    }
  }

  private void runOnExitAction()
  {

    final String fenceName = fenceNameField.getText().toString();

    if( fenceName.isEmpty() )
    {
      Toast.makeText( FenceDemo.this, "Fence name is empty!", Toast.LENGTH_SHORT ).show();
    }
    else
    {
      Backendless.Geo.runOnExitAction( fenceName, new AsyncCallback<Integer>()
      {
        @Override
        public void handleResponse( Integer response )
        {
          Toast.makeText( FenceDemo.this, "Action ran for: " + response + " points", Toast.LENGTH_SHORT ).show();
        }

        @Override
        public void handleFault( BackendlessFault fault )
        {
          Toast.makeText( FenceDemo.this, "Failed: " + fault.toString(), Toast.LENGTH_LONG ).show();
        }
      } );
    }
  }

  private void getPointsFromFence()
  {
    final String fenceName = fenceNameField.getText().toString();
    final Boolean runForEach = ((Switch) findViewById( R.id.run_for_each_point_toggle )).isChecked();

    if( fenceName.isEmpty() )
    {
      Toast.makeText( FenceDemo.this, "Fence name is empty!", Toast.LENGTH_SHORT ).show();
    }
    else
    {
      Backendless.Geo.getPoints( fenceName, new AsyncCallback<BackendlessCollection<GeoPoint>>()
      {
        @Override
        public void handleResponse( BackendlessCollection<GeoPoint> response )
        {
          int quantity = response.getTotalObjects();
          Toast.makeText( FenceDemo.this, "Found " + quantity + " points", Toast.LENGTH_SHORT ).show();

          if( runForEach && response.getTotalObjects() != 0 )
          {
            Iterator<GeoPoint> iterator = response.getData().iterator();

            while( iterator.hasNext() )
            {
              GeoPoint point = iterator.next();
              // run action for each
              Backendless.Geo.runOnEnterAction( fenceName, point, new AsyncCallback<Void>()
              {
                @Override
                public void handleResponse( Void vvoid )
                {
                  Toast.makeText( FenceDemo.this, "RunOnEnterAction passed", Toast.LENGTH_SHORT ).show();
                }

                @Override
                public void handleFault( BackendlessFault backendlessFault )
                {
                  Toast.makeText( FenceDemo.this, "RunOnEnterAction failed: " + backendlessFault.toString(), Toast.LENGTH_LONG ).show();
                }
              } );

              Backendless.Geo.runOnExitAction( fenceName, point, new AsyncCallback<Void>()
              {
                @Override
                public void handleResponse( Void aVoid )
                {
                  Toast.makeText( FenceDemo.this, "RunOnExitAction passed", Toast.LENGTH_SHORT ).show();
                }

                @Override
                public void handleFault( BackendlessFault backendlessFault )
                {
                  Toast.makeText( FenceDemo.this, "RunOnExitAction failed: " + backendlessFault.toString(), Toast.LENGTH_LONG ).show();
                }
              } );

              Backendless.Geo.runOnStayAction( fenceName, point, new AsyncCallback<Void>()
              {
                @Override
                public void handleResponse( Void aVoid )
                {
                  Toast.makeText( FenceDemo.this, "RunOnStayAction passed", Toast.LENGTH_SHORT ).show();
                }

                @Override
                public void handleFault( BackendlessFault backendlessFault )
                {
                  Toast.makeText( FenceDemo.this, "RunOnStayAction failed: " + backendlessFault.toString(), Toast.LENGTH_LONG ).show();
                }
              } );
            }
          }
        }

        @Override
        public void handleFault( BackendlessFault fault )
        {
          Toast.makeText( FenceDemo.this, "Failed: " + fault.toString(), Toast.LENGTH_LONG ).show();
        }
      } );
    }
  }
}