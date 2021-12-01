package com.backendless.transaction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class OperationUpsertBulkFactory extends OperationFactory<OperationUpsertBulkReturned>
{
  @Override
  protected Class<OperationUpsertBulkReturned> getClazz()
  {
    return OperationUpsertBulkReturned.class;
  }

  @Override
  protected OperationUpsertBulkReturned createOperation( OperationType operationType, String table, String opResultId, Object payload )
  {
    if( payload instanceof Map[] )
      payload = new ArrayList<>( Arrays.asList( ( Map[] ) payload ) );
    return new OperationUpsertBulkReturned( operationType, table, opResultId, (List) payload );
  }
}
