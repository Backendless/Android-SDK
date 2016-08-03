package com.backendless;

import com.backendless.commons.DeviceType;
import com.backendless.exceptions.BackendlessException;
import com.backendless.exceptions.ExceptionMessage;

import java.util.Hashtable;
import java.util.Map;


public class BLHeadersManager implements IHeadersManager
{
  private final Object headersLock = new Object();
  private Hashtable<String, String> headers = new Hashtable<String, String>();

  private static ThreadLocal<BLHeadersManager> threadLocal = new InheritableThreadLocal<BLHeadersManager>()
  {
    @Override
    protected BLHeadersManager initialValue()
    {
      return new BLHeadersManager();
    }
  };

  public static BLHeadersManager getInstance()
  {
    return BLHeadersManager.threadLocal.get();
  }

  private BLHeadersManager()
  {
    if( Backendless.getApplicationId() == null || Backendless.getSecretKey() == null )
    {
      throw new IllegalStateException( ExceptionMessage.NOT_INITIALIZED );
    }

    addHeader( HeadersManager.HeadersEnum.APP_ID_NAME, Backendless.getApplicationId() );
    addHeader( HeadersManager.HeadersEnum.SECRET_KEY_NAME, Backendless.getSecretKey() );
    addHeader( HeadersManager.HeadersEnum.APP_TYPE_NAME, DeviceType.BL.name() );
    addHeader( HeadersManager.HeadersEnum.API_VERSION, "1.0" );
    addHeaders( Backendless.getHeaders() );
  }

  public void cleanHeaders()
  {
    threadLocal.remove();
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
