package com.backendless.transaction;

import com.backendless.transaction.payload.Relation;

public class OperationAddRelationFactory extends OperationFactory<OperationAddRelation>
{
  @Override
  protected Class<OperationAddRelation> getClazz()
  {
    return OperationAddRelation.class;
  }

  @Override
  protected OperationAddRelation createOperation( OperationType operationType, String table, String opResultId, Object payload )
  {
    return new OperationAddRelation( operationType, table, opResultId, (Relation) payload );
  }
}
