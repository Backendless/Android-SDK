package com.backendless.transaction;

import java.util.HashMap;
import java.util.Map;

public class TransactionHelper
{
  public static OpResult makeOpResult( String operationResultId )
  {
    Map<String, Object> reference = new HashMap<>();
    reference.put( UnitOfWork.REFERENCE_MARKER, true );
    reference.put( UnitOfWork.OP_RESULT_ID, operationResultId );
    return new OpResult( reference );
  }
}
