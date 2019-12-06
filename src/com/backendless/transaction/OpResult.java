package com.backendless.transaction;

import java.util.Map;

public class OpResult
{
  private Map<String, Object> reference;

  public OpResult( Map<String, Object> reference )
  {
    this.reference = reference;
  }

  public Map<String, Object> getReference()
  {
    return reference;
  }
}
