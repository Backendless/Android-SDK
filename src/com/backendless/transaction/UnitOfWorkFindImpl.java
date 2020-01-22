package com.backendless.transaction;

import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.DataQueryBuilder;
import com.backendless.transaction.operations.Operation;
import com.backendless.transaction.operations.OperationFind;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class UnitOfWorkFindImpl implements UnitOfWorkFind
{
  AtomicInteger countFind = new AtomicInteger( 1 );

  private final List<Operation<?>> operations;

  public UnitOfWorkFindImpl( List<Operation<?>> operations )
  {
    this.operations = operations;
  }

  @Override
  public OpResult find( String tableName, DataQueryBuilder queryBuilder )
  {
    BackendlessDataQuery query = queryBuilder.build();

    String operationResultId = OperationType.FIND + "_" + countFind.getAndIncrement();

    OperationFind<?> operationFind = new OperationFind<>( OperationType.FIND, tableName, operationResultId, query );

    operations.add( operationFind );

    return TransactionHelper.makeOpResult( tableName, operationResultId, OperationType.FIND );
  }
}
