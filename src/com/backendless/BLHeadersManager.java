package com.backendless;

import com.backendless.exceptions.BackendlessException;

import java.util.Hashtable;
import java.util.Map;

class BLHeadersManager implements IHeadersManager
{
  private static BLHeadersManager instance = new BLHeadersManager();

  static BLHeadersManager getInstance()
  {
    return instance;
  }

  private static ThreadLocal<AndroidHeadersManager> threadLocal = new InheritableThreadLocal<AndroidHeadersManager>()
  {
    @Override
    protected AndroidHeadersManager initialValue()
    {
      return new AndroidHeadersManager();
    }
  };

  BLHeadersManager()
  {

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
}


