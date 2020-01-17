package com.backendless.transaction;

import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.DataQueryBuilder;
import com.backendless.transaction.operations.Operation;
import com.backendless.transaction.operations.OperationFind;
import com.backendless.transaction.payload.FindPayload;

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
    FindPayload<?> payload = convertQueryToPayload( query );

    OperationFind operationFind = new OperationFind( OperationType.FIND, tableName, operationResultId, payload );

    operations.add( operationFind );

    return TransactionHelper.makeOpResult( tableName, operationResultId, OperationType.FIND );
  }

  private FindPayload<?> convertQueryToPayload( BackendlessDataQuery query )
  {
    return new FindPayload<Object>( query.getPageSize(), query.getOffset(), query.getProperties(), query.getWhereClause(),
                                    query.getHavingClause(), query.getQueryOptions().getSortBy(),
                                    query.getQueryOptions().getRelated(), query.getQueryOptions().getRelationsDepth(),
                                    query.getQueryOptions().getRelationsPageSize(), query.getGroupBy(), null );
  }
}
