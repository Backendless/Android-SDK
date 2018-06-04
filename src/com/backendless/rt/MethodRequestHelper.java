package com.backendless.rt;

import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import weborb.exceptions.AdaptingException;
import weborb.types.IAdaptingType;

import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class MethodRequestHelper
{
  private static final Logger logger = Logger.getLogger( "MethodRequestHelper" );

  private final RTClient rtClient = RTClientFactory.get();
  private final ConcurrentLinkedDeque<RTMethodRequest> methodsToSend = new ConcurrentLinkedDeque<>();
  private final ConnectListener connectListener;

  protected MethodRequestHelper( ConnectListener connectListener )
  {
    this.connectListener = connectListener;
  }

  public abstract RTMethodRequest createMethodRequest( RTCallback rtCallback );

  public void connected()
  {
    RTMethodRequest methodRequest = methodsToSend.poll();

    while( methodRequest != null )
    {
      rtClient.invoke( methodRequest );
      methodRequest = methodsToSend.poll();
    }
  }

  public void invoke( final AsyncCallback<Void> callback )
  {

    logger.log( Level.FINE, "Send invocation with options" );
    RTMethodRequest rtMethodRequest = createMethodRequest( new RTCallbackWithFault()
    {
      @Override
      public AsyncCallback usersCallback()
      {
        return callback;
      }

      @Override
      public void handleResponse( IAdaptingType response )
      {
        logger.info( "invocation sent" );

        if( callback != null )
          callback.handleResponse( null );
      }
    } );

    invoke( rtMethodRequest );
  }

  public <T> void invoke( final Class<T> tClass, final AsyncCallback<T> callback )
  {

    logger.log( Level.FINE, "Send invocation with options" );
    RTMethodRequest rtMethodRequest = createMethodRequest( new RTCallbackWithFault()
    {
      @Override
      public AsyncCallback usersCallback()
      {
        return callback;
      }

      @Override
      public void handleResponse( IAdaptingType response )
      {
        logger.info( "got result" );

        if( callback != null )
        {
          if(response == null)
          {
            callback.handleResponse( null );
          }
          else
          {
            try
            {
              callback.handleResponse( (T) response.adapt( tClass ) );
            }
            catch( AdaptingException e )
            {
              callback.handleFault( new BackendlessFault( e.getMessage() ) );
            }
          }
        }
      }
    } );

    invoke( rtMethodRequest );
  }

  public void invokeWithDefaultAdapt( final AsyncCallback<Object> callback )
  {

    logger.log( Level.FINE, "Send invocation with options" );
    RTMethodRequest rtMethodRequest = createMethodRequest( new RTCallbackWithFault()
    {
      @Override
      public AsyncCallback usersCallback()
      {
        return callback;
      }

      @Override
      public void handleResponse( IAdaptingType response )
      {
        logger.info( "got result" );

        if( callback != null )
        {
          if(response == null)
          {
            callback.handleResponse( null );
          }
          else
          {
            callback.handleResponse( response.defaultAdapt() );
          }
        }
      }
    } );

    invoke( rtMethodRequest );
  }

  private void invoke( RTMethodRequest rtMethodRequest )
  {
    if( isConnected() )
    {
      rtClient.invoke( rtMethodRequest );
    }
    else
    {
      methodsToSend.addFirst( rtMethodRequest );
    }
  }

  private boolean isConnected()
  {
    return connectListener.isConnected();
  }
}
