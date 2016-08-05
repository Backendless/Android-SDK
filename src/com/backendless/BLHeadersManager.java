package com.backendless;

import com.backendless.commons.DeviceType;
import com.backendless.exceptions.BackendlessException;
import com.backendless.exceptions.ExceptionMessage;

import java.util.Hashtable;
import java.util.Map;

class BLHeadersManager implements IHeadersManager
{
  private static BLHeadersManager instance = new BLHeadersManager();

  private static ThreadLocal<BLHeadersManagerImpl> threadLocal = new InheritableThreadLocal<BLHeadersManagerImpl>()
  {
    @Override
    protected BLHeadersManagerImpl initialValue()
    {
      return new BLHeadersManagerImpl();
    }
  };

  private BLHeadersManager()
  {

  }

  static BLHeadersManager getInstance()
  {
    return instance;
  }

  public void cleanHeaders()
  {
    threadLocal.remove();
  }

  @Override
  public void addHeader( HeadersManager.HeadersEnum headersEnum, String value )
  {
    threadLocal.get().addHeader( headersEnum, value );
  }

  @Override
  public void addHeaders( Map<String, String> headers )
  {
    threadLocal.get().addHeaders( headers );
  }

  @Override
  public void removeHeader( HeadersManager.HeadersEnum headersEnum )
  {
    threadLocal.get().removeHeader( headersEnum );
  }

  @Override
  public Hashtable<String, String> getHeaders() throws BackendlessException
  {
    return threadLocal.get().getHeaders();
  }

  @Override
  public void setHeaders( Map<String, String> headers )
  {
    threadLocal.get().setHeaders( headers );
  }

  @Override
  public String getHeader( HeadersManager.HeadersEnum headersEnum ) throws BackendlessException
  {
    return threadLocal.get().getHeader( headersEnum );
  }


  static class BLHeadersManagerImpl implements IHeadersManager
  {
    private final Object headersLock = new Object();
    private Hashtable<String, String> headers = new Hashtable<>();

    private BLHeadersManagerImpl()
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
      headers.clear();
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
}


