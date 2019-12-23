package com.backendless.transaction;

import java.util.Map;

public class OpResultIndex extends OpResult
{
  public OpResultIndex( Map<String, Object> reference, OperationType operationType )
  {
    super( reference, operationType );
  }
}
