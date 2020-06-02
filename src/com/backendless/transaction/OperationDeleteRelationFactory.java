package com.backendless.transaction;

import com.backendless.transaction.payload.Relation;

public class OperationDeleteRelationFactory extends OperationFactory<OperationDeleteRelation>
{
  @Override
  protected Class<OperationDeleteRelation> getClazz()
  {
    return OperationDeleteRelation.class;
  }

  @Override
  protected OperationDeleteRelation createOperation( OperationType operationType, String table, String opResultId, Object payload )
  {
    return new OperationDeleteRelation( operationType, table, opResultId, (Relation) payload );
  }
}
