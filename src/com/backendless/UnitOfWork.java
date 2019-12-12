package com.backendless;

import com.backendless.transaction.IUnitOfWork;
import com.backendless.transaction.OpResult;
import com.backendless.transaction.UnitOfWorkAddRelation;
import com.backendless.transaction.UnitOfWorkAddRelationImpl;
import com.backendless.transaction.UnitOfWorkUpdate;
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
  private final UnitOfWorkUpdate unitOFWorkUpdate;
  private final UnitOfWorkDelete unitOfWorkDelete;
  private final UnitOfWorkAddRelation unitOfWorkAddRelation;

  public UnitOfWork()
  {
    super();
    unitOfWorkCreate = new UnitOfWorkCreateImpl( super.getOperations() );
    unitOFWorkUpdate = new UnitOfWorkUpdateImpl( super.getOperations() );
    unitOfWorkDelete = new UnitOfWorkDeleteImpl( super.getOperations() );
    unitOfWorkAddRelation = new UnitOfWorkAddRelationImpl( super.getOperations() );
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
  public <E> OpResult bulkCreate( List<E> instances )
  {
    return unitOfWorkCreate.bulkCreate( instances );
  }

  @Override
  public OpResult bulkCreate( String tableName, List<Map<String, Object>> arrayOfObjectMaps )
  {
    return unitOfWorkCreate.bulkCreate( tableName, arrayOfObjectMaps );
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
  public <E> OpResult bulkUpdate( List<E> instances )
  {
    return unitOFWorkUpdate.bulkUpdate( instances );
  }

  @Override
  public OpResult bulkUpdate( String tableName, List<Map<String, Object>> arrayOfHashMaps )
  {
    return unitOFWorkUpdate.bulkUpdate( tableName, arrayOfHashMaps );
  }

  @Override
  public <E> OpResult bulkUpdate( String whereClause, E changes )
  {
    return unitOFWorkUpdate.bulkUpdate( whereClause, changes );
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
  public <E> OpResult bulkDelete( List<E> instances )
  {
    return unitOfWorkDelete.bulkDelete( instances );
  }

  @Override
  public OpResult bulkDelete( String tableName, List<Map<String, Object>> arrayOfObjectMaps )
  {
    return unitOfWorkDelete.bulkDelete( tableName, arrayOfObjectMaps );
  }

  @Override
  public OpResult bulkDelete( String tableName, String whereClause )
  {
    return unitOfWorkDelete.bulkDelete( tableName, whereClause );
  }

  @Override
  public OpResult bulkDelete( String tableName, OpResult result, String propName )
  {
    return unitOfWorkDelete.bulkDelete( tableName, result, propName );
  }

  @Override
  public <E> OpResult addToRelation( String parentTable, Map<String, Object> parentObject, String columnName,
                                     List<E> children )
  {
    return unitOfWorkAddRelation.addToRelation( parentTable, parentObject, columnName, children );
  }

  @Override
  public OpResult addToRelation( String parentTable, Map<String, Object> parentObject, String columnName,
                                 OpResult children )
  {
    return unitOfWorkAddRelation.addToRelation( parentTable, parentObject, columnName, children );
  }

  @Override
  public OpResult addToRelation( String parentTable, Map<String, Object> parentObject, String columnName,
                                 String whereClauseForChildren )
  {
    return unitOfWorkAddRelation.addToRelation( parentTable, parentObject, columnName, whereClauseForChildren );
  }

  @Override
  public <E, U> OpResult addToRelation( E parentObject, String columnName, List<U> children )
  {
    return unitOfWorkAddRelation.addToRelation( parentObject, columnName, children );
  }

  @Override
  public <E> OpResult addToRelation( E parentObject, String columnName, OpResult children )
  {
    return unitOfWorkAddRelation.addToRelation( parentObject, columnName, children );
  }

  @Override
  public <E> OpResult addToRelation( E parentObject, String columnName, String whereClauseForChildren )
  {
    return unitOfWorkAddRelation.addToRelation( parentObject, columnName, whereClauseForChildren );
  }

  @Override
  public <E> OpResult addToRelation( String parentTable, OpResult parentObject, String columnName, List<E> children )
  {
    return unitOfWorkAddRelation.addToRelation( parentTable, parentObject, columnName, children );
  }

  @Override
  public OpResult addToRelation( String parentTable, OpResult parentObject, String columnName, OpResult children )
  {
    return unitOfWorkAddRelation.addToRelation( parentTable, parentObject, columnName, children );
  }

  @Override
  public OpResult addToRelation( String parentTable, OpResult parentObject, String columnName,
                                 String whereClauseForChildren )
  {
    return unitOfWorkAddRelation.addToRelation( parentTable, parentObject, columnName, whereClauseForChildren );
  }
}
