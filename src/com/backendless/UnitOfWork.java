package com.backendless;

import com.backendless.transaction.IUnitOfWork;
import com.backendless.transaction.OpResult;
import com.backendless.transaction.UnitOFWorkUpdate;
import com.backendless.transaction.UnitOfWorkCreate;
import com.backendless.transaction.UnitOfWorkCreateImpl;
import com.backendless.transaction.UnitOfWorkDelete;
import com.backendless.transaction.UnitOfWorkDeleteImpl;
import com.backendless.transaction.UnitOfWorkStatus;
import com.backendless.transaction.UnitOfWorkUpdateImpl;
import com.backendless.utils.ResponderHelper;

import java.util.List;
import java.util.Map;

public class UnitOfWork extends com.backendless.transaction.UnitOfWork implements IUnitOfWork
{
  public final static String TRANSACTION_MANAGER_SERVER_ALIAS = "com.backendless.services.transaction.TransactionService";

  private final UnitOfWorkCreate unitOfWorkCreate;
  private final UnitOFWorkUpdate unitOFWorkUpdate;
  private final UnitOfWorkDelete unitOfWorkDelete;

  public UnitOfWork()
  {
    super();
    unitOfWorkCreate = new UnitOfWorkCreateImpl( super.getOperations() );
    unitOFWorkUpdate = new UnitOfWorkUpdateImpl( super.getOperations() );
    unitOfWorkDelete = new UnitOfWorkDeleteImpl( super.getOperations() );
  }

  @Override
  public UnitOfWorkStatus execute()
  {
    Object[] args = new Object[]{ this };
    return Invoker.invokeSync( TRANSACTION_MANAGER_SERVER_ALIAS, "execute", args, ResponderHelper.getPOJOAdaptingResponder( UnitOfWorkStatus.class ) );
  }

  @Override
  public <E> OpResult create( E instance )
  {
    return unitOfWorkCreate.create( instance );
  }

  @Override
  public OpResult create( String tableName, Map<String, Object> objectMap )
  {
    return unitOfWorkCreate.create( tableName, objectMap );
  }

  @Override
  public <E> OpResult create( List<E> instances )
  {
    return unitOfWorkCreate.create( instances );
  }

  @Override
  public OpResult create( String tableName, List<Map<String, Object>> arrayOfObjectMaps )
  {
    return unitOfWorkCreate.create( tableName, arrayOfObjectMaps );
  }

  @Override
  public <E> OpResult update( E instance )
  {
    return unitOFWorkUpdate.update( instance );
  }

  @Override
  public OpResult update( String tableName, Map<String, Object> objectMap )
  {
    return unitOFWorkUpdate.update( tableName, objectMap );
  }

  @Override
  public <E> OpResult update( List<E> instances )
  {
    return unitOFWorkUpdate.update( instances );
  }

  @Override
  public OpResult update( String tableName, List<Map<String, Object>> arrayOfHashMaps )
  {
    return unitOFWorkUpdate.update( tableName, arrayOfHashMaps );
  }

  @Override
  public <E> OpResult bulkUpdate( String whereClause, List<E> instances )
  {
    return unitOFWorkUpdate.bulkUpdate( whereClause, instances );
  }

  @Override
  public OpResult bulkUpdate( String tableName, String whereClause, Map<String, Object> changes )
  {
    return unitOFWorkUpdate.bulkUpdate( tableName, whereClause, changes );
  }

  @Override
  public <E> OpResult delete( E instance )
  {
    return unitOfWorkDelete.delete( instance );
  }

  @Override
  public OpResult delete( String tableName, Map<String, Object> objectMap )
  {
    return unitOfWorkDelete.delete( tableName, objectMap );
  }

  @Override
  public OpResult delete( String tableName, OpResult result )
  {
    return unitOfWorkDelete.delete( tableName, result );
  }

  @Override
  public OpResult delete( String tableName, OpResult result, int opResultIndex )
  {
    return unitOfWorkDelete.delete( tableName, result, opResultIndex );
  }

  @Override
  public OpResult delete( String tableName, List<Map<String, Object>> arrayOfObjectMaps )
  {
    return unitOfWorkDelete.delete( tableName, arrayOfObjectMaps );
  }

  @Override
  public <E> OpResult delete( List<E> instances )
  {
    return unitOfWorkDelete.delete( instances );
  }

  @Override
  public OpResult bulkDelete( String tableName, String query, OpResult result )
  {
    return unitOfWorkDelete.bulkDelete( tableName, query, result );
  }
}
