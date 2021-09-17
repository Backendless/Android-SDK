package com.backendless.examples.dataservice.tododemo;

public interface InnerCallback<T>
{
  void handleResponse( T response );
}
