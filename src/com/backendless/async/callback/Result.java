package com.backendless.async.callback;

public interface Result<T>
{
  void handle(T result);
}
