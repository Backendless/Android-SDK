package com.backendless.example.dataservice.tododemo;

public interface InnerCallback<T>
{
  void handleResponse( T response );
}
