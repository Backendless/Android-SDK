package com.backendless.utils.timeout;

public interface TimeOutManager
{
  int nextTimeout();

  int repeatedTimes();

  void reset();
}
