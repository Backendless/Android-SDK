package com.backendless.transaction;

import java.util.Map;

public class OpResultIndex extends OpResult
{
  public OpResultIndex( String tableName, Map<String, Object> reference, OperationType operationType )
  {
    super( tableName, reference, operationType );
  }
}
