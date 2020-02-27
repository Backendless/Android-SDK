package com.backendless.transaction;

import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.DataQueryBuilder;
import com.backendless.transaction.operations.Operation;
import com.backendless.transaction.operations.OperationFind;

import java.util.List;
import java.util.Map;

class UnitOfWorkFindImpl implements UnitOfWorkFind
{
  private final List<Operation<?>> operations;
  private final OpResultIdGenerator opResultIdGenerator;
  private final Map<String, Class> clazzes;

  public UnitOfWorkFindImpl( List<Operation<?>> operations, OpResultIdGenerator opResultIdGenerator,
                             Map<String, Class> clazzes )
  {
    this.operations = operations;
    this.opResultIdGenerator = opResultIdGenerator;
    this.clazzes = clazzes;
  }

  @Override
  public OpResult find( String tableName, DataQueryBuilder queryBuilder )
  {
    BackendlessDataQuery query = queryBuilder.build();

    String operationResultId = opResultIdGenerator.generateOpResultId( OperationType.FIND, tableName );

    OperationFind<?> operationFind = new OperationFind<>( OperationType.FIND, tableName, operationResultId, query );

    operations.add( operationFind );

    return TransactionHelper.makeOpResult( tableName, operationResultId, OperationType.FIND );
  }
}
