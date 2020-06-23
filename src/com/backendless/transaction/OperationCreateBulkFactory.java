package com.backendless.transaction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class OperationCreateBulkFactory extends OperationFactory<OperationCreateBulkReturned>
{
  @Override
  protected Class<OperationCreateBulkReturned> getClazz()
  {
    return OperationCreateBulkReturned.class;
  }

  @Override
  protected OperationCreateBulkReturned createOperation( OperationType operationType, String table, String opResultId, Object payload )
  {
    if( payload instanceof Map[] )
      payload = new ArrayList<>( Arrays.asList( ( Map[] ) payload ) );
    return new OperationCreateBulkReturned( operationType, table, opResultId, (List) payload );
  }
}
