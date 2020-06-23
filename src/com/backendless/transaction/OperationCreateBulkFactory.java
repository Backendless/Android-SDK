package com.backendless.transaction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class OperationCreateBulkFactory extends OperationFactory<OperationCreateBulk>
{
  @Override
  protected Class<OperationCreateBulk> getClazz()
  {
    return OperationCreateBulk.class;
  }

  @Override
  protected OperationCreateBulk createOperation( OperationType operationType, String table, String opResultId, Object payload )
  {
    if( payload instanceof Map[] )
      payload = new ArrayList<>( Arrays.asList( ( Map[] ) payload ) );
    return new OperationCreateBulk( operationType, table, opResultId, (List) payload );
  }
}
