package com.backendless.transaction;

import com.backendless.transaction.payload.Relation;

public class OperationSetRelationFactory extends OperationFactory<OperationSetRelation>
{
  @Override
  protected Class<OperationSetRelation> getClazz()
  {
    return OperationSetRelation.class;
  }

  @Override
  protected OperationSetRelation createOperation( OperationType operationType, String table, String opResultId, Object payload )
  {
    return new OperationSetRelation( operationType, table, opResultId, (Relation) payload );
  }
}
