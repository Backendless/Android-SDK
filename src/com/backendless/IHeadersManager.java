package com.backendless;

import com.backendless.exceptions.BackendlessException;

import java.util.Hashtable;
import java.util.Map;

interface IHeadersManager
{
  void cleanHeaders();

  void addHeader( HeadersManager.HeadersEnum headersEnum, String value );

  void addHeaders( Map<String, String> headers );

  void removeHeader( HeadersManager.HeadersEnum headersEnum );

  Hashtable<String, String> getHeaders() throws BackendlessException;

  void setHeaders( Map<String, String> headers );

  String getHeader( HeadersManager.HeadersEnum headersEnum ) throws BackendlessException;
}
