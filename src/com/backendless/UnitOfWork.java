package com.backendless;

import com.backendless.async.callback.AsyncCallback;
import com.backendless.transaction.IUnitOfWork;
import com.backendless.transaction.OpResult;
import com.backendless.transaction.OpResultIndex;
import com.backendless.transaction.RelationOperation;
import com.backendless.transaction.RelationOperationImpl;
import com.backendless.transaction.UnitOfWorkAddRelation;
import com.backendless.transaction.UnitOfWorkAddRelationImpl;
import com.backendless.transaction.UnitOfWorkDeleteRelation;
import com.backendless.transaction.UnitOfWorkDeleteRelationImpl;
import com.backendless.transaction.UnitOfWorkExecutor;
import com.backendless.transaction.UnitOfWorkExecutorImpl;
import com.backendless.transaction.UnitOfWorkSetRelation;
import com.backendless.transaction.UnitOfWorkSetRelationImpl;
import com.backendless.transaction.UnitOfWorkUpdate;
import com.backendless.transaction.UnitOfWorkCreate;
import com.backendless.transaction.UnitOfWorkCreateImpl;
import com.backendless.transaction.UnitOfWorkDelete;
import com.backendless.transaction.UnitOfWorkDeleteImpl;
import com.backendless.transaction.UnitOfWorkStatus;
import com.backendless.transaction.UnitOfWorkUpdateImpl;
import com.backendless.transaction.operations.Operation;

import java.util.List;
import java.util.Map;

public class UnitOfWork extends com.backendless.transaction.UnitOfWork implements IUnitOfWork
{
  private final List<Operation<?>> operations;

  private final UnitOfWorkCreate unitOfWorkCreate;
  private final UnitOfWorkUpdate unitOFWorkUpdate;
  private final UnitOfWorkDelete unitOfWorkDelete;
  private final RelationOperation relationOperation;
  private final UnitOfWorkAddRelation unitOfWorkAddRelation;
  private final UnitOfWorkSetRelation unitOfWorkSetRelation;
  private final UnitOfWorkDeleteRelation unitOfWorkDeleteRelation;
  private final UnitOfWorkExecutor unitOfWorkExecutor;

  public UnitOfWork()
  {
    super();
    operations = super.getOperations();
    unitOfWorkCreate = new UnitOfWorkCreateImpl( operations );
    unitOFWorkUpdate = new UnitOfWorkUpdateImpl( operations );
    unitOfWorkDelete = new UnitOfWorkDeleteImpl( operations );
    relationOperation = new RelationOperationImpl( operations );
    unitOfWorkAddRelation = new UnitOfWorkAddRelationImpl( relationOperation );
    unitOfWorkSetRelation = new UnitOfWorkSetRelationImpl( relationOperation );
    unitOfWorkDeleteRelation = new UnitOfWorkDeleteRelationImpl( relationOperation );
    unitOfWorkExecutor = new UnitOfWorkExecutorImpl( this );
  }

  @Override
  public UnitOfWorkStatus execute()
  {
    return unitOfWorkExecutor.execute();
  }

  public void execute( AsyncCallback<UnitOfWorkStatus> responder )
  {
    unitOfWorkExecutor.execute( responder );
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
  public OpResult update( String tableName, OpResult objectMap )
  {
    return unitOFWorkUpdate.update( tableName, objectMap );
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
  public OpResult bulkUpdate( String tableName, List<String> objectsForChanges, Map<String, Object> changes )
  {
    return unitOFWorkUpdate.bulkUpdate( tableName, objectsForChanges, changes );
  }

  @Override
  public OpResult bulkUpdate( String tableName, OpResult objectIdsForChanges, Map<String, Object> changes )
  {
    return unitOFWorkUpdate.bulkUpdate( tableName, objectIdsForChanges, changes );
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
  public OpResult delete( String tableName, String objectId )
  {
    return unitOfWorkDelete.delete( tableName, objectId );
  }

  @Override
  public OpResult delete( String tableName, OpResult result )
  {
    return unitOfWorkDelete.delete( tableName, result );
  }

  @Override
  public OpResult delete( String tableName, OpResultIndex opResultIndex )
  {
    return unitOfWorkDelete.delete( tableName, opResultIndex );
  }

  @Override
  public <E> OpResult bulkDelete( List<E> instances )
  {
    return unitOfWorkDelete.bulkDelete( instances );
  }

  @Override
  public <E> OpResult bulkDelete( String tableName, List<E> arrayOfObjects )
  {
    return unitOfWorkDelete.bulkDelete( tableName, arrayOfObjects );
  }

  @Override
  public OpResult bulkDelete( String tableName, String whereClause )
  {
    return unitOfWorkDelete.bulkDelete( tableName, whereClause );
  }

  @Override
  public OpResult bulkDelete( String tableName, OpResult result )
  {
    return unitOfWorkDelete.bulkDelete( tableName, result );
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
  public <E> OpResult addToRelation( String parentTable, String parentObjectId, String columnName, List<E> children )
  {
    return unitOfWorkAddRelation.addToRelation( parentTable, parentObjectId, columnName, children );
  }

  @Override
  public OpResult addToRelation( String parentTable, String parentObjectId, String columnName, OpResult children )
  {
    return unitOfWorkAddRelation.addToRelation( parentTable, parentObjectId, columnName, children );
  }

  @Override
  public OpResult addToRelation( String parentTable, String parentObjectId, String columnName, String whereClauseForChildren )
  {
    return unitOfWorkAddRelation.addToRelation( parentTable, parentObjectId, columnName, whereClauseForChildren );
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

  @Override
  public <E> OpResult addToRelation( String parentTable, OpResultIndex parentObject, String columnName, List<E> children )
  {
    return unitOfWorkAddRelation.addToRelation( parentTable, parentObject, columnName, children );
  }

  @Override
  public OpResult addToRelation( String parentTable, OpResultIndex parentObject, String columnName, OpResult children )
  {
    return unitOfWorkAddRelation.addToRelation( parentTable, parentObject, columnName, children );
  }

  @Override
  public OpResult addToRelation( String parentTable, OpResultIndex parentObject, String columnName, String whereClauseForChildren )
  {
    return unitOfWorkAddRelation.addToRelation( parentTable, parentObject, columnName, whereClauseForChildren );
  }

  @Override
  public <E> OpResult setToRelation( String parentTable, Map<String, Object> parentObject, String columnName,
                                     List<E> children )
  {
    return unitOfWorkSetRelation.setToRelation( parentTable, parentObject, columnName, children );
  }

  @Override
  public OpResult setToRelation( String parentTable, Map<String, Object> parentObject, String columnName,
                                 OpResult children )
  {
    return unitOfWorkSetRelation.setToRelation( parentTable, parentObject, columnName, children );
  }

  @Override
  public OpResult setToRelation( String parentTable, Map<String, Object> parentObject, String columnName,
                                 String whereClauseForChildren )
  {
    return unitOfWorkSetRelation.setToRelation( parentTable, parentObject, columnName, whereClauseForChildren );
  }

  @Override
  public <E> OpResult setToRelation( String parentTable, String parentObjectId, String columnName, List<E> children )
  {
    return unitOfWorkSetRelation.setToRelation( parentTable, parentObjectId, columnName, children );
  }

  @Override
  public OpResult setToRelation( String parentTable, String parentObjectId, String columnName, OpResult children )
  {
    return unitOfWorkSetRelation.setToRelation( parentTable, parentObjectId, columnName, children );
  }

  @Override
  public OpResult setToRelation( String parentTable, String parentObjectId, String columnName, String whereClauseForChildren )
  {
    return unitOfWorkSetRelation.setToRelation( parentTable, parentObjectId, columnName, whereClauseForChildren );
  }

  @Override
  public <E, U> OpResult setToRelation( E parentObject, String columnName, List<U> children )
  {
    return unitOfWorkSetRelation.setToRelation( parentObject, columnName, children );
  }

  @Override
  public <E> OpResult setToRelation( E parentObject, String columnName, OpResult children )
  {
    return unitOfWorkSetRelation.setToRelation( parentObject, columnName, children );
  }

  @Override
  public <E> OpResult setToRelation( E parentObject, String columnName, String whereClauseForChildren )
  {
    return unitOfWorkSetRelation.setToRelation( parentObject, columnName, whereClauseForChildren );
  }

  @Override
  public <E> OpResult setToRelation( String parentTable, OpResult parentObject, String columnName, List<E> children )
  {
    return unitOfWorkSetRelation.setToRelation( parentTable, parentObject, columnName, children );
  }

  @Override
  public OpResult setToRelation( String parentTable, OpResult parentObject, String columnName, OpResult children )
  {
    return unitOfWorkSetRelation.setToRelation( parentTable, parentObject, columnName, children );
  }

  @Override
  public OpResult setToRelation( String parentTable, OpResult parentObject, String columnName,
                                 String whereClauseForChildren )
  {
    return unitOfWorkSetRelation.setToRelation( parentTable, parentObject, columnName, whereClauseForChildren );
  }

  @Override
  public <E> OpResult setToRelation( String parentTable, OpResultIndex parentObject, String columnName, List<E> children )
  {
    return unitOfWorkSetRelation.setToRelation( parentTable, parentObject, columnName, children );
  }

  @Override
  public OpResult setToRelation( String parentTable, OpResultIndex parentObject, String columnName, OpResult children )
  {
    return unitOfWorkSetRelation.setToRelation( parentTable, parentObject, columnName, children );
  }

  @Override
  public OpResult setToRelation( String parentTable, OpResultIndex parentObject, String columnName, String whereClauseForChildren )
  {
    return unitOfWorkSetRelation.setToRelation( parentTable, parentObject, columnName, whereClauseForChildren );
  }

  @Override
  public <E> OpResult deleteRelation( String parentTable, Map<String, Object> parentObject, String columnName,
                                      List<E> children )
  {
    return unitOfWorkDeleteRelation.deleteRelation( parentTable, parentObject, columnName, children );
  }

  @Override
  public OpResult deleteRelation( String parentTable, Map<String, Object> parentObject, String columnName,
                                  OpResult children )
  {
    return unitOfWorkDeleteRelation.deleteRelation( parentTable, parentObject, columnName, children );
  }

  @Override
  public OpResult deleteRelation( String parentTable, Map<String, Object> parentObject, String columnName,
                                  String whereClauseForChildren )
  {
    return unitOfWorkDeleteRelation.deleteRelation( parentTable, parentObject, columnName, whereClauseForChildren );
  }

  @Override
  public <E> OpResult deleteRelation( String parentTable, String parentObjectId, String columnName, List<E> children )
  {
    return unitOfWorkDeleteRelation.deleteRelation( parentTable, parentObjectId, columnName, children );
  }

  @Override
  public OpResult deleteRelation( String parentTable, String parentObjectId, String columnName, OpResult children )
  {
    return unitOfWorkDeleteRelation.deleteRelation( parentTable, parentObjectId, columnName, children );
  }

  @Override
  public OpResult deleteRelation( String parentTable, String parentObjectId, String columnName,
                                  String whereClauseForChildren )
  {
    return unitOfWorkDeleteRelation.deleteRelation( parentTable, parentObjectId, columnName, whereClauseForChildren );
  }

  @Override
  public <E, U> OpResult deleteRelation( E parentObject, String columnName, List<U> children )
  {
    return unitOfWorkDeleteRelation.deleteRelation( parentObject, columnName, children );
  }

  @Override
  public <E> OpResult deleteRelation( E parentObject, String columnName, OpResult children )
  {
    return unitOfWorkDeleteRelation.deleteRelation( parentObject, columnName, children );
  }

  @Override
  public <E> OpResult deleteRelation( E parentObject, String columnName, String whereClauseForChildren )
  {
    return unitOfWorkDeleteRelation.deleteRelation( parentObject, columnName, whereClauseForChildren );
  }

  @Override
  public <E> OpResult deleteRelation( String parentTable, OpResult parentObject, String columnName, List<E> children )
  {
    return unitOfWorkDeleteRelation.deleteRelation( parentTable, parentObject, columnName, children );
  }

  @Override
  public OpResult deleteRelation( String parentTable, OpResult parentObject, String columnName, OpResult children )
  {
    return unitOfWorkDeleteRelation.deleteRelation( parentTable, parentObject, columnName, children );
  }

  @Override
  public OpResult deleteRelation( String parentTable, OpResult parentObject, String columnName,
                                  String whereClauseForChildren )
  {
    return unitOfWorkDeleteRelation.deleteRelation( parentTable, parentObject, columnName, whereClauseForChildren );
  }

  @Override
  public <E> OpResult deleteRelation( String parentTable, OpResultIndex parentObject, String columnName,
                                      List<E> children )
  {
    return unitOfWorkDeleteRelation.deleteRelation( parentTable, parentObject, columnName, children );
  }

  @Override
  public OpResult deleteRelation( String parentTable, OpResultIndex parentObject, String columnName, OpResult children )
  {
    return unitOfWorkDeleteRelation.deleteRelation( parentTable, parentObject, columnName, children );
  }

  @Override
  public OpResult deleteRelation( String parentTable, OpResultIndex parentObject, String columnName,
                                  String whereClauseForChildren )
  {
    return unitOfWorkDeleteRelation.deleteRelation( parentTable, parentObject, columnName, whereClauseForChildren );
  }
}
