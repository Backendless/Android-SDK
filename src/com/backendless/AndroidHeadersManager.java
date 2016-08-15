package com.backendless;

import com.backendless.commons.DeviceType;
import com.backendless.exceptions.BackendlessException;
import com.backendless.exceptions.ExceptionMessage;

import java.util.Hashtable;
import java.util.Map;


class AndroidHeadersManager implements IHeadersManager
{
  private static volatile AndroidHeadersManager instance;

  static AndroidHeadersManager getInstance() throws BackendlessException
  {
    if( instance == null )
      synchronized( AndroidHeadersManager.class )
      {
        if( instance == null )
        {
          instance = new AndroidHeadersManager();
        }
      }
    return instance;
  }


  protected final Object headersLock = new Object();
  protected final Hashtable<String, String> headers = new Hashtable<>();

  AndroidHeadersManager()
  {
    if( Backendless.getApplicationId() == null || Backendless.getSecretKey() == null )
    {
      throw new IllegalStateException( ExceptionMessage.NOT_INITIALIZED );
    }
    initialFill();
  }

  private void initialFill()
  {
    addHeader( HeadersManager.HeadersEnum.APP_TYPE_NAME, DeviceType.ANDROID.name() );
    addHeader( HeadersManager.HeadersEnum.API_VERSION, "1.0" );
    addHeaders( Backendless.getHeaders() );
  }

  public void cleanHeaders()
  {
    synchronized( headersLock )
    {
      headers.clear();
      initialFill();
    }
  }

  public void addHeader( HeadersManager.HeadersEnum headersEnum, String value )
  {
    synchronized( headersLock )
    {
      headers.put( headersEnum.getHeader(), value );
    }
  }

  public void addHeaders( Map<String, String> headers )
  {
    if( headers == null || headers.isEmpty() )
      return;

    synchronized( headersLock )
    {
      this.headers.putAll( headers );
    }
  }

  public void removeHeader( HeadersManager.HeadersEnum headersEnum )
  {
    synchronized( headersLock )
    {
      headers.remove( headersEnum.getHeader() );
    }
  }

  public Hashtable<String, String> getHeaders() throws BackendlessException
  {
    synchronized( headersLock )
    {
      if( headers == null || headers.isEmpty() )
        throw new IllegalStateException( ExceptionMessage.NOT_INITIALIZED );

      return headers;
    }
  }

  public void setHeaders( Map<String, String> headers )
  {
    synchronized( headersLock )
    {
      this.headers.putAll( headers );
    }
  }

  public String getHeader( HeadersManager.HeadersEnum headersEnum ) throws BackendlessException
  {
    synchronized( headersLock )
    {
      if( headers == null || headers.isEmpty() )
        throw new IllegalStateException( ExceptionMessage.NOT_INITIALIZED );

      String header = headers.get( headersEnum );

      if( header == null )
        header = headers.get( headersEnum.getHeader() );

      return header;
    }
  }
}
