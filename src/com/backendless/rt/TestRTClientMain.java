package com.backendless.rt;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.async.callback.Fault;
import com.backendless.async.callback.Result;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.messaging.PublishMessageInfo;
import com.backendless.rt.command.Command;
import com.backendless.rt.data.DataListener;
import com.backendless.rt.messaging.Channel;
import com.backendless.rt.messaging.MessageInfoCallback;
import com.backendless.rt.rso.SharedObject;
import com.backendless.rt.users.UserStatusResponse;

import java.io.IOException;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestRTClientMain
{
  public static void main( String[] args )
  {
    Logger logger = Logger.getAnonymousLogger();
    // LOG this level to the log
    logger.setLevel( Level.ALL );

    ConsoleHandler handler = new ConsoleHandler();
    // PUBLISH this level
    handler.setLevel( Level.ALL );
    logger.addHandler( handler );

    Backendless.setUrl( "http://localhost:9000" );
    Backendless.initApp( "7AD895D5-BEAE-4116-FFD0-EC7A17F5B000", "E591DF47-45DE-A008-FFD5-AF521EB5F000" );
//    Backendless.initApp( "728BEC07-221E-8ABA-FF43-E810821D4C00", "6D545F8F-21AB-CB4E-FFBC-3FEB26513400" );
//    Backendless.setUrl( "http://apitest4.backendless.com" );
//    Backendless.initApp( "D8E1D3F5-D5C2-C3B3-FF60-3642278E3900", "FE59B935-51C3-959E-FFEA-673BD72C9900" );

    Backendless.RT.addConnectErrorEventListener( new Fault()
    {
      @Override
      public void handle( BackendlessFault fault )
      {
        System.out.println("error to connect: " + fault.toString());
      }
    } );

    Backendless.RT.addConnectEventListener( new Result<Void>()
    {
      @Override
      public void handle( Void result )
      {
        System.out.println("rt connected");
      }
    } );

    Backendless.RT.addDisconnectEventListener( new Result<Void>()
    {
      @Override
      public void handle( Void result )
      {
        System.out.println("rt disconnected");
      }
    } );

    Backendless.RT.addReconnectAttemptEventListener( new Result<ReconnectAttempt>()
    {
      @Override
      public void handle( ReconnectAttempt result )
      {
        System.out.println("reconnect " + result.toString());
      }
    } );

    Backendless.RT.Data.of( "test" ).addCreateListener( new AsyncCallback<Map>()
    {
      @Override
      public void handleResponse( Map response )
      {
        System.out.println("added " + response );
      }

      @Override
      public void handleFault( BackendlessFault fault )
      {

      }
    } );

    int i = 0;

    while( true )
    {
      try
      {
        System.in.read();
      }
      catch( IOException e )
      {
        e.printStackTrace();
      }

      if(i++%2 == 0)
        Backendless.RT.disconnect();
      else
        Backendless.RT.connect();
    }
  }
}
