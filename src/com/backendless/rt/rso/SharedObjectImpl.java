package com.backendless.rt.rso;

import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessException;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.rt.ConnectListener;
import com.backendless.rt.MethodRequestHelper;
import com.backendless.rt.MethodTypes;
import com.backendless.rt.RTCallback;
import com.backendless.rt.RTCallbackWithFault;
import com.backendless.rt.RTClient;
import com.backendless.rt.RTClientFactory;
import com.backendless.rt.RTMethodRequest;
import com.backendless.rt.SubscriptionNames;
import com.backendless.rt.command.Command;
import com.backendless.rt.command.CommandListener;
import com.backendless.rt.users.UserInfo;
import com.backendless.rt.users.UserStatus;
import com.backendless.utils.WeborbSerializationHelper;
import weborb.exceptions.AdaptingException;
import weborb.reader.ArrayType;
import weborb.types.IAdaptingType;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SharedObjectImpl implements SharedObject
{
  private static final Logger logger = Logger.getLogger( "SharedObject" );

  private final String name;
  private final RTClient rtClient = RTClientFactory.get();
  private final CopyOnWriteArrayList<RSOSubscription> subscriptions = new CopyOnWriteArrayList<>();

  private Object invocationTarget;
  private AsyncCallback<Object> invocationCallback;

  private final ConnectListener<RSOSubscription> connectListener;
  private final CommandListener<RSOSubscription, SOCommandRequest> commandListener = new CommandListener<RSOSubscription, SOCommandRequest>()
  {

    @Override
    public CopyOnWriteArrayList<RSOSubscription> getSubscriptionHolder()
    {
      return subscriptions;
    }

    @Override
    public RSOSubscription createSubscription( RTCallback rtCallback )
    {
      return RSOSubscription.command( name, rtCallback );
    }

    @Override
    public SOCommandRequest createCommandRequest( RTCallback rtCallback )
    {
      return new SOCommandRequest( name, rtCallback );
    }

    @Override
    public boolean isConnected()
    {
      return connectListener.isConnected();
    }
  };

  SharedObjectImpl( final String name )
  {
    this.name = name;

    connectListener = new ConnectListener<RSOSubscription>( name )
    {
      @Override
      public void connected()
      {
        for( RSOSubscription subscription : subscriptions )
        {
          rtClient.subscribe( subscription );
        }

        commandListener.connected();
      }

      @Override
      public RSOSubscription createSubscription( RTCallback callback )
      {
        return RSOSubscription.connect( name, callback );
      }
    };

    final RSOSubscription invocationSubscription = RSOSubscription.invoke( name, new RTCallbackWithFault()
    {
      @Override
      public AsyncCallback usersCallback()
      {
        return null;
      }

      @Override
      public void handleResponse( IAdaptingType response )
      {
        logger.fine( "handle invocation" );

        String methodName = WeborbSerializationHelper.asString( response, "method" );

        logger.fine( "invocation for method - " + methodName );

        ArrayType arg = (ArrayType) WeborbSerializationHelper.asAdaptingType( response, "args" );

        if( invocationTarget != null )
          try
          {
            Object result = InvocationHelper.invoke( invocationTarget, methodName, arg );
            logger.log( Level.FINE, "Invocation result {0}", new Object[]{ result } );

            if( invocationCallback != null )
              invocationCallback.handleResponse( result );
          }
          catch( BackendlessException e )
          {
            logger.warning( "Invocation fault: " + e.getMessage() );
            if( invocationCallback != null )
              invocationCallback.handleFault( new BackendlessFault( e ) );
          }
          catch( NoSuchMethodException | IllegalAccessException | AdaptingException | InvocationTargetException e )
          {
            logger.warning( "Invocation fault: " + e.getMessage() );
            if( invocationCallback != null )
              invocationCallback.handleFault( new BackendlessFault( e.getMessage() ) );
          }
      }
    } );

    addListener( invocationSubscription );
  }

  @Override
  public void addConnectListener( AsyncCallback<Void> callback )
  {
    connectListener.addConnectListener( callback );
  }

  @Override
  public void removeConnectListener( AsyncCallback<Void> callback )
  {
    connectListener.removeConnectListener( callback );
  }

  @Override
  public void removeConnectListeners()
  {
    connectListener.removeConnectListeners();
  }

  @Override
  public void addChangesListener( final AsyncCallback<SharedObjectChanges> callback )
  {
    checkCallback( callback );

    RTCallback rtCallback = new RTCallbackWithFault()
    {
      @Override
      public AsyncCallback usersCallback()
      {
        return callback;
      }

      @Override
      public void handleResponse( IAdaptingType response )
      {
        SharedObjectChanges sharedObjectChanges = new SharedObjectChanges();

        UserInfo userInfo = new UserInfo();

        sharedObjectChanges.setUserInfo( userInfo );

        userInfo.setConnectionId( WeborbSerializationHelper.asString( response, "connectionId" ) );
        userInfo.setUserId( WeborbSerializationHelper.asString( response, "userId" ) );

        final String key = WeborbSerializationHelper.asString( response, "key" );
        sharedObjectChanges.setKey( key );

        IAdaptingType data = WeborbSerializationHelper.asAdaptingType( response, "data" );

        final Object value = data.defaultAdapt();
        sharedObjectChanges.setValue( value );
        callback.handleResponse( sharedObjectChanges );
      }
    };

    addListener( RSOSubscription.changes( name, rtCallback ) );
  }

  private void checkCallback( AsyncCallback<?> callback )
  {
    if( callback == null )
      throw new BackendlessException( "calback can not be null" );
  }

  @Override
  public void removeChangesListener( AsyncCallback<SharedObjectChanges> callback )
  {
    removeSubscription( callback );
  }

  @Override
  public void removeChangesListeners()
  {
    removeSubscription( SubscriptionNames.RSO_CHANGES );
  }

  @Override
  public void addClearListener( final AsyncCallback<UserInfo> callback )
  {
    checkCallback( callback );
    RTCallback rtCallback = new RTCallbackWithFault()
    {
      @Override
      public AsyncCallback usersCallback()
      {
        return callback;
      }

      @Override
      public void handleResponse( IAdaptingType response )
      {
        final UserInfo userInfo;
        try
        {
          userInfo = (UserInfo) response.adapt( UserInfo.class );
        }
        catch( AdaptingException e )
        {
          handleFault( new BackendlessFault( e.getMessage() ) );
          return;
        }

        callback.handleResponse( userInfo );
      }
    };

    addListener( RSOSubscription.cleared( name, rtCallback ) );
  }

  @Override
  public void removeClearListener( AsyncCallback<UserInfo> callback )
  {
    removeSubscription( callback );
  }

  @Override
  public void removeClearListeners()
  {
    removeSubscription( SubscriptionNames.RSO_CLEARED );
  }

  @Override
  public void addCommandListener( AsyncCallback<Command<String>> callback )
  {
    commandListener.addCommandListener( String.class, callback );
  }

  @Override
  public <T> void addCommandListener( Class<T> dataType, AsyncCallback<Command<T>> callback )
  {
    commandListener.addCommandListener( dataType, callback );
  }

  @Override
  public void removeCommandListener( AsyncCallback<Command> callback )
  {
    commandListener.removeCommandListener( callback );
  }

  @Override
  public void removeCommandListeners()
  {
    removeSubscription( SubscriptionNames.RSO_COMMANDS );
  }

  @Override
  public void addUserStatusListener( final AsyncCallback<UserStatus> callback )
  {
    checkCallback( callback );

    RTCallback rtCallback = new RTCallbackWithFault()
    {
      @Override
      public AsyncCallback usersCallback()
      {
        return callback;
      }

      @Override
      public void handleResponse( IAdaptingType response )
      {
        try
        {
          UserStatus userStatus = (UserStatus) response.adapt( UserStatus.class );
          callback.handleResponse( userStatus );
        }
        catch( AdaptingException e )
        {
          callback.handleFault( new BackendlessFault( e.getMessage() ) );
        }
      }
    };

    addListener( RSOSubscription.userStatus( name, rtCallback ) );
  }

  @Override
  public void removeUserStatusListener( AsyncCallback<UserStatus> callback )
  {
    removeSubscription( callback );
  }

  @Override
  public void removeUserStatusListeners()
  {
    removeSubscription( SubscriptionNames.RSO_USERS );
  }

  @Override
  public void set( final String key, final Object value, AsyncCallback<Void> callback )
  {
    new MethodRequestHelper( connectListener )
    {
      @Override
      public RTMethodRequest createMethodRequest( RTCallback rtCallback )
      {
        return new SOMethodRequest( name, MethodTypes.RSO_SET, rtCallback ).putMethodOption( "key", key ).putMethodOption( "data", value );
      }
    }.invoke( callback );
  }

  @Override
  public void set( String key, Object value )
  {
    set( key, value, null );
  }

  @Override
  public void get( String key, AsyncCallback<String> callback )
  {
    get( key, String.class, callback );
  }

  @Override
  public <T> void get( final String key, Class<T> dataType, AsyncCallback<T> callback )
  {
    new MethodRequestHelper( connectListener )
    {
      @Override
      public RTMethodRequest createMethodRequest( RTCallback rtCallback )
      {
        return new SOMethodRequest( name, MethodTypes.RSO_GET, rtCallback ).putMethodOption( "key", key );
      }
    }.invoke( dataType, callback );
  }

  @Override
  public void clear( final AsyncCallback<Void> callback )
  {
    new MethodRequestHelper( connectListener )
    {
      @Override
      public RTMethodRequest createMethodRequest( RTCallback rtCallback )
      {
        return new SOMethodRequest( name, MethodTypes.RSO_CLEAR, rtCallback );
      }
    }.invoke( callback );
  }

  @Override
  public void clear()
  {
    clear( null );
  }

  @Override
  public void sendCommand( String type, Object value )
  {
    commandListener.sendCommand( type, value, null );
  }

  @Override
  public void sendCommand( String type, Object value, AsyncCallback<Void> callback )
  {
    commandListener.sendCommand( type, value, callback );
  }

  @Override
  public void invoke( String methodName, Object... args )
  {
    invoke( methodName, args, null, null );
  }

  @Override
  public void invoke( String methodName, Object[] args, AsyncCallback<Void> callback )
  {
    invoke( methodName, args, null, callback );
  }

  @Override
  public void invoke( final String methodName, final Object[] args, final Collection<String> target,
                      AsyncCallback<Void> callback )
  {
    new MethodRequestHelper( connectListener )
    {
      @Override
      public RTMethodRequest createMethodRequest( RTCallback rtCallback )
      {
        return new SOMethodRequest( name, MethodTypes.RSO_INVOKE, rtCallback )
                .putMethodOption( "method", methodName )
                .putMethodOption( "args", args )
                .putMethodOption( "targets", target );
      }
    }.invoke( callback );
  }

  @Override
  public void setInvocationTarget( Object target )
  {
    invocationTarget = target;
  }

  @Override
  public void setInvocationTarget( Object target, AsyncCallback<Object> callback )
  {
    setInvocationTarget( target );
    invocationCallback = callback;
  }

  @Override
  public void connect()
  {
    connectListener.connect();
  }

  @Override
  public void disconnect()
  {
    connectListener.disconnect();
  }

  @Override
  public boolean isConnected()
  {
    return connectListener.isConnected();
  }

  private void addListener( RSOSubscription subscription )
  {
    subscriptions.add( subscription );

    if( isConnected() )
      rtClient.subscribe( subscription );
  }

  private void removeSubscription( AsyncCallback callback )
  {
    for( RSOSubscription subscription : subscriptions )
    {
      if( subscription.getCallback() == callback )
        removeSubscription( subscription );
    }
  }

  private void removeSubscription( SubscriptionNames subscriptionName )
  {
    for( RSOSubscription subscription : subscriptions )
    {
      if( subscription.getSubscriptionName() == subscriptionName )
        removeSubscription( subscription );
    }
  }

  private void removeSubscription( RSOSubscription subscription )
  {
    //we can do it because it is CopyOnWriteArrayList so we iterate through the copy
    subscriptions.remove( subscription );

    if( isConnected() )
    {
      rtClient.unsubscribe( subscription.getId() );
    }
  }
}
