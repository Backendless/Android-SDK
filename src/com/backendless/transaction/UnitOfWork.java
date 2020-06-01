package com.backendless.transaction;

import com.backendless.async.callback.AsyncCallback;
import com.backendless.persistence.DataQueryBuilder;
import weborb.util.ObjectFactories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class UnitOfWork implements IUnitOfWork
{
  public final static String REFERENCE_MARKER = "___ref";
  public final static String OP_RESULT_ID = "opResultId";
  public final static String RESULT_INDEX = "resultIndex";
  public final static String PROP_NAME = "propName";

  static
  {
    ObjectFactories.addArgumentObjectFactory( OperationCreate.class.getName(), new OperationFactory() );
    ObjectFactories.addArgumentObjectFactory( OperationCreateBulk.class.getName(), new OperationFactory() );
    ObjectFactories.addArgumentObjectFactory( OperationUpdate.class.getName(), new OperationFactory() );
    ObjectFactories.addArgumentObjectFactory( OperationUpdateBulk.class.getName(), new OperationFactory() );
    ObjectFactories.addArgumentObjectFactory( OperationDelete.class.getName(), new OperationFactory() );
    ObjectFactories.addArgumentObjectFactory( OperationDeleteBulk.class.getName(), new OperationFactory() );
    ObjectFactories.addArgumentObjectFactory( OperationFind.class.getName(), new OperationFactory() );
    ObjectFactories.addArgumentObjectFactory( OperationAddRelation.class.getName(), new OperationFactory() );
    ObjectFactories.addArgumentObjectFactory( OperationSetRelation.class.getName(), new OperationFactory() );
    ObjectFactories.addArgumentObjectFactory( OperationDeleteRelation.class.getName(), new OperationFactory() );
  }

  private final UnitOfWorkCreate unitOfWorkCreate;
  private final UnitOfWorkUpdate unitOFWorkUpdate;
  private final UnitOfWorkDelete unitOfWorkDelete;
  private final UnitOfWorkFind unitOfWorkFind;
  private final UnitOfWorkAddRelation unitOfWorkAddRelation;
  private final UnitOfWorkSetRelation unitOfWorkSetRelation;
  private final UnitOfWorkDeleteRelation unitOfWorkDeleteRelation;
  private final UnitOfWorkExecutor unitOfWorkExecutor;

  private IsolationLevelEnum transactionIsolation = IsolationLevelEnum.REPEATABLE_READ;
  private List<Operation> operations;
  private List<String> opResultIdStrings;

  public UnitOfWork()
  {
    Map<String, Class> clazzes = new HashMap<>();
    operations = new LinkedList<>();
    opResultIdStrings = new ArrayList<>();
    OpResultIdGenerator opResultIdGenerator = new OpResultIdGenerator( opResultIdStrings );
    unitOfWorkCreate = new UnitOfWorkCreateImpl( operations, opResultIdGenerator, clazzes );
    unitOFWorkUpdate = new UnitOfWorkUpdateImpl( operations, opResultIdGenerator, clazzes );
    unitOfWorkDelete = new UnitOfWorkDeleteImpl( operations, opResultIdGenerator );
    unitOfWorkFind = new UnitOfWorkFindImpl( operations, opResultIdGenerator );
    RelationOperation relationOperation = new RelationOperationImpl( operations, opResultIdGenerator );
    unitOfWorkAddRelation = new UnitOfWorkAddRelationImpl( relationOperation );
    unitOfWorkSetRelation = new UnitOfWorkSetRelationImpl( relationOperation );
    unitOfWorkDeleteRelation = new UnitOfWorkDeleteRelationImpl( relationOperation );
    unitOfWorkExecutor = new UnitOfWorkExecutorImpl( this, clazzes );
  }

  public IsolationLevelEnum getTransactionIsolation()
  {
    return transactionIsolation;
  }

  public void setTransactionIsolation( IsolationLevelEnum transactionIsolation )
  {
    this.transactionIsolation = transactionIsolation;
  }

  public List<Operation> getOperations()
  {
    return operations;
  }

  public List<String> getOpResultIdStrings()
  {
    return opResultIdStrings;
  }

  @Override
  public UnitOfWorkResult execute()
  {
    return unitOfWorkExecutor.execute();
  }

  public void execute( AsyncCallback<UnitOfWorkResult> responder )
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
  public OpResult update( OpResult result, Map<String, Object> changes )
  {
    return unitOFWorkUpdate.update( result, changes );
  }

  @Override
  public OpResult update( OpResult result, String propertyName, Object propertyValue )
  {
    return unitOFWorkUpdate.update( result, propertyName, propertyValue );
  }

  @Override
  public OpResult update( OpResultValueReference result, Map<String, Object> changes )
  {
    return unitOFWorkUpdate.update( result, changes );
  }

  @Override
  public OpResult update( OpResultValueReference result, String propertyName, Object propertyValue )
  {
    return unitOFWorkUpdate.update( result, propertyName, propertyValue );
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
  public OpResult bulkUpdate( OpResult objectIdsForChanges, Map<String, Object> changes )
  {
    return unitOFWorkUpdate.bulkUpdate( objectIdsForChanges, changes );
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
  public OpResult delete( OpResult result )
  {
    return unitOfWorkDelete.delete( result );
  }

  @Override
  public OpResult delete( OpResultValueReference opResultValueReference )
  {
    return unitOfWorkDelete.delete( opResultValueReference );
  }

  @Override
  public <E> OpResult bulkDelete( List<E> instances )
  {
    return unitOfWorkDelete.bulkDelete( instances );
  }

  @Override
  public OpResult bulkDelete( String tableName, String[] objectIdValues )
  {
    return unitOfWorkDelete.bulkDelete( tableName, objectIdValues );
  }

  @Override
  public OpResult bulkDelete( String tableName, List<Map<String, Object>> arrayOfObjects )
  {
    return unitOfWorkDelete.bulkDelete( tableName, arrayOfObjects );
  }

  @Override
  public OpResult bulkDelete( String tableName, String whereClause )
  {
    return unitOfWorkDelete.bulkDelete( tableName, whereClause );
  }

  @Override
  public OpResult bulkDelete( OpResult result )
  {
    return unitOfWorkDelete.bulkDelete( result );
  }

  @Override
  public OpResult find( String tableName, DataQueryBuilder queryBuilder )
  {
    return unitOfWorkFind.find( tableName, queryBuilder );
  }

  @Override
  public OpResult addToRelation( String parentTable, Map<String, Object> parentObject, String columnName,
                                 String[] childrenObjectIds )
  {
    return unitOfWorkAddRelation.addToRelation( parentTable, parentObject, columnName, childrenObjectIds );
  }

  @Override
  public <E> OpResult addToRelation( String parentTable, Map<String, Object> parentObject, String columnName,
                                     E[] childrenInstance )
  {
    return unitOfWorkAddRelation.addToRelation( parentTable, parentObject, columnName, childrenInstance );
  }

  @Override
  public OpResult addToRelation( String parentTable, Map<String, Object> parentObject, String columnName,
                                 List<Map<String, Object>> childrenMaps )
  {
    return unitOfWorkAddRelation.addToRelation( parentTable, parentObject, columnName, childrenMaps );
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
  public OpResult addToRelation( String parentTable, String parentObjectId, String columnName,
                                 String[] childrenObjectIds )
  {
    return unitOfWorkAddRelation.addToRelation( parentTable, parentObjectId, columnName, childrenObjectIds );
  }

  @Override
  public <E> OpResult addToRelation( String parentTable, String parentObjectId, String columnName,
                                     E[] childrenInstances )
  {
    return unitOfWorkAddRelation.addToRelation( parentTable, parentObjectId, columnName, childrenInstances );
  }

  @Override
  public OpResult addToRelation( String parentTable, String parentObjectId, String columnName,
                                 List<Map<String, Object>> childrenMaps )
  {
    return unitOfWorkAddRelation.addToRelation( parentTable, parentObjectId, columnName, childrenMaps );
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
  public <E> OpResult addToRelation( E parentObject, String columnName, String[] childrenObjectIds )
  {
    return unitOfWorkAddRelation.addToRelation( parentObject, columnName, childrenObjectIds );
  }

  @Override
  public <E, U> OpResult addToRelation( E parentObject, String columnName, U[] childrenInstances )
  {
    return unitOfWorkAddRelation.addToRelation( parentObject, columnName, childrenInstances );
  }

  @Override
  public <E> OpResult addToRelation( E parentObject, String columnName, List<Map<String, Object>> childrenMaps )
  {
    return unitOfWorkAddRelation.addToRelation( parentObject, columnName, childrenMaps );
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
  public OpResult addToRelation( OpResult parentObject, String columnName, String[] childrenObjectIds )
  {
    return unitOfWorkAddRelation.addToRelation( parentObject, columnName, childrenObjectIds );
  }

  @Override
  public <E> OpResult addToRelation( OpResult parentObject, String columnName, E[] childrenInstances )
  {
    return unitOfWorkAddRelation.addToRelation( parentObject, columnName, childrenInstances );
  }

  @Override
  public OpResult addToRelation( OpResult parentObject, String columnName, List<Map<String, Object>> childrenMaps )
  {
    return unitOfWorkAddRelation.addToRelation( parentObject, columnName, childrenMaps );
  }

  @Override
  public OpResult addToRelation( OpResult parentObject, String columnName, OpResult children )
  {
    return unitOfWorkAddRelation.addToRelation( parentObject, columnName, children );
  }

  @Override
  public OpResult addToRelation( OpResult parentObject, String columnName, String whereClauseForChildren )
  {
    return unitOfWorkAddRelation.addToRelation( parentObject, columnName, whereClauseForChildren );
  }

  @Override
  public OpResult addToRelation( OpResultValueReference parentObject, String columnName, String[] childrenObjectIds )
  {
    return unitOfWorkAddRelation.addToRelation( parentObject, columnName, childrenObjectIds );
  }

  @Override
  public <E> OpResult addToRelation( OpResultValueReference parentObject, String columnName, E[] childrenInstances )
  {
    return unitOfWorkAddRelation.addToRelation( parentObject, columnName, childrenInstances );
  }

  @Override
  public OpResult addToRelation( OpResultValueReference parentObject, String columnName,
                                 List<Map<String, Object>> childrenMaps )
  {
    return unitOfWorkAddRelation.addToRelation( parentObject, columnName, childrenMaps );
  }

  @Override
  public OpResult addToRelation( OpResultValueReference parentObject, String columnName, OpResult children )
  {
    return unitOfWorkAddRelation.addToRelation( parentObject, columnName, children );
  }

  @Override
  public OpResult addToRelation( OpResultValueReference parentObject, String columnName, String whereClauseForChildren )
  {
    return unitOfWorkAddRelation.addToRelation( parentObject, columnName, whereClauseForChildren );
  }

  @Override
  public OpResult setRelation( String parentTable, Map<String, Object> parentObject, String columnName,
                               String[] childrenObjectIds )
  {
    return unitOfWorkSetRelation.setRelation( parentTable, parentObject, columnName, childrenObjectIds );
  }

  @Override
  public <E> OpResult setRelation( String parentTable, Map<String, Object> parentObject, String columnName,
                                   E[] childrenInstance )
  {
    return unitOfWorkSetRelation.setRelation( parentTable, parentObject, columnName, childrenInstance );
  }

  @Override
  public OpResult setRelation( String parentTable, Map<String, Object> parentObject, String columnName,
                               List<Map<String, Object>> childrenMaps )
  {
    return unitOfWorkSetRelation.setRelation( parentTable, parentObject, columnName, childrenMaps );
  }

  @Override
  public OpResult setRelation( String parentTable, Map<String, Object> parentObject, String columnName,
                               OpResult children )
  {
    return unitOfWorkSetRelation.setRelation( parentTable, parentObject, columnName, children );
  }

  @Override
  public OpResult setRelation( String parentTable, Map<String, Object> parentObject, String columnName,
                               String whereClauseForChildren )
  {
    return unitOfWorkSetRelation.setRelation( parentTable, parentObject, columnName, whereClauseForChildren );
  }

  @Override
  public OpResult setRelation( String parentTable, String parentObjectId, String columnName,
                               String[] childrenObjectIds )
  {
    return unitOfWorkSetRelation.setRelation( parentTable, parentObjectId, columnName, childrenObjectIds );
  }

  @Override
  public <E> OpResult setRelation( String parentTable, String parentObjectId, String columnName, E[] childrenInstances )
  {
    return unitOfWorkSetRelation.setRelation( parentTable, parentObjectId, columnName, childrenInstances );
  }

  @Override
  public OpResult setRelation( String parentTable, String parentObjectId, String columnName,
                               List<Map<String, Object>> childrenMaps )
  {
    return unitOfWorkSetRelation.setRelation( parentTable, parentObjectId, columnName, childrenMaps );
  }

  @Override
  public OpResult setRelation( String parentTable, String parentObjectId, String columnName, OpResult children )
  {
    return unitOfWorkSetRelation.setRelation( parentTable, parentObjectId, columnName, children );
  }

  @Override
  public OpResult setRelation( String parentTable, String parentObjectId, String columnName, String whereClauseForChildren )
  {
    return unitOfWorkSetRelation.setRelation( parentTable, parentObjectId, columnName, whereClauseForChildren );
  }

  @Override
  public <E> OpResult setRelation( E parentObject, String columnName, String[] childrenObjectIds )
  {
    return unitOfWorkSetRelation.setRelation( parentObject, columnName, childrenObjectIds );
  }

  @Override
  public <E, U> OpResult setRelation( E parentObject, String columnName, U[] childrenInstances )
  {
    return unitOfWorkSetRelation.setRelation( parentObject, columnName, childrenInstances );
  }

  @Override
  public <E> OpResult setRelation( E parentObject, String columnName, List<Map<String, Object>> childrenMaps )
  {
    return unitOfWorkSetRelation.setRelation( parentObject, columnName, childrenMaps );
  }

  @Override
  public <E> OpResult setRelation( E parentObject, String columnName, OpResult children )
  {
    return unitOfWorkSetRelation.setRelation( parentObject, columnName, children );
  }

  @Override
  public <E> OpResult setRelation( E parentObject, String columnName, String whereClauseForChildren )
  {
    return unitOfWorkSetRelation.setRelation( parentObject, columnName, whereClauseForChildren );
  }

  @Override
  public OpResult setRelation( OpResult parentObject, String columnName, String[] childrenObjectIds )
  {
    return unitOfWorkSetRelation.setRelation( parentObject, columnName, childrenObjectIds );
  }

  @Override
  public <E> OpResult setRelation( OpResult parentObject, String columnName, E[] childrenInstances )
  {
    return unitOfWorkSetRelation.setRelation( parentObject, columnName, childrenInstances );
  }

  @Override
  public OpResult setRelation( OpResult parentObject, String columnName, List<Map<String, Object>> childrenMaps )
  {
    return unitOfWorkSetRelation.setRelation( parentObject, columnName, childrenMaps );
  }

  @Override
  public OpResult setRelation( OpResult parentObject, String columnName, OpResult children )
  {
    return unitOfWorkSetRelation.setRelation( parentObject, columnName, children );
  }

  @Override
  public OpResult setRelation( OpResult parentObject, String columnName, String whereClauseForChildren )
  {
    return unitOfWorkSetRelation.setRelation( parentObject, columnName, whereClauseForChildren );
  }

  @Override
  public OpResult setRelation( OpResultValueReference parentObject, String columnName, String[] childrenObjectIds )
  {
    return unitOfWorkSetRelation.setRelation( parentObject, columnName, childrenObjectIds );
  }

  @Override
  public <E> OpResult setRelation( OpResultValueReference parentObject, String columnName, E[] childrenInstances )
  {
    return unitOfWorkSetRelation.setRelation( parentObject, columnName, childrenInstances );
  }

  @Override
  public OpResult setRelation( OpResultValueReference parentObject, String columnName,
                               List<Map<String, Object>> childrenMaps )
  {
    return unitOfWorkSetRelation.setRelation( parentObject, columnName, childrenMaps );
  }

  @Override
  public OpResult setRelation( OpResultValueReference parentObject, String columnName, OpResult children )
  {
    return unitOfWorkSetRelation.setRelation( parentObject, columnName, children );
  }

  @Override
  public OpResult setRelation( OpResultValueReference parentObject, String columnName, String whereClauseForChildren )
  {
    return unitOfWorkSetRelation.setRelation( parentObject, columnName, whereClauseForChildren );
  }

  @Override
  public OpResult deleteRelation( String parentTable, Map<String, Object> parentObject, String columnName,
                                  String[] childrenObjectIds )
  {
    return unitOfWorkDeleteRelation.deleteRelation( parentTable, parentObject, columnName, childrenObjectIds );
  }

  @Override
  public <E> OpResult deleteRelation( String parentTable, Map<String, Object> parentObject, String columnName,
                                      E[] childrenInstance )
  {
    return unitOfWorkDeleteRelation.deleteRelation( parentTable, parentObject, columnName, childrenInstance );
  }

  @Override
  public OpResult deleteRelation( String parentTable, Map<String, Object> parentObject, String columnName,
                                  List<Map<String, Object>> childrenMaps )
  {
    return unitOfWorkDeleteRelation.deleteRelation( parentTable, parentObject, columnName, childrenMaps );
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
  public OpResult deleteRelation( String parentTable, String parentObjectId, String columnName,
                                  String[] childrenObjectIds )
  {
    return unitOfWorkDeleteRelation.deleteRelation( parentTable, parentObjectId, columnName, childrenObjectIds );
  }

  @Override
  public <E> OpResult deleteRelation( String parentTable, String parentObjectId, String columnName,
                                      E[] childrenInstances )
  {
    return unitOfWorkDeleteRelation.deleteRelation( parentTable, parentObjectId, columnName, childrenInstances );
  }

  @Override
  public OpResult deleteRelation( String parentTable, String parentObjectId, String columnName,
                                  List<Map<String, Object>> childrenMaps )
  {
    return unitOfWorkDeleteRelation.deleteRelation( parentTable, parentObjectId, columnName, childrenMaps );
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
  public <E> OpResult deleteRelation( E parentObject, String columnName, String[] childrenObjectIds )
  {
    return unitOfWorkDeleteRelation.deleteRelation( parentObject, columnName, childrenObjectIds );
  }

  @Override
  public <E, U> OpResult deleteRelation( E parentObject, String columnName, U[] childrenInstances )
  {
    return unitOfWorkDeleteRelation.deleteRelation( parentObject, columnName, childrenInstances );
  }

  @Override
  public <E> OpResult deleteRelation( E parentObject, String columnName, List<Map<String, Object>> childrenMaps )
  {
    return unitOfWorkDeleteRelation.deleteRelation( parentObject, columnName, childrenMaps );
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
  public OpResult deleteRelation( OpResult parentObject, String columnName, String[] childrenObjectIds )
  {
    return unitOfWorkDeleteRelation.deleteRelation( parentObject, columnName, childrenObjectIds );
  }

  @Override
  public <E> OpResult deleteRelation( OpResult parentObject, String columnName, E[] childrenInstances )
  {
    return unitOfWorkDeleteRelation.deleteRelation( parentObject, columnName, childrenInstances );
  }

  @Override
  public OpResult deleteRelation( OpResult parentObject, String columnName, List<Map<String, Object>> childrenMaps )
  {
    return unitOfWorkDeleteRelation.deleteRelation( parentObject, columnName, childrenMaps );
  }

  @Override
  public OpResult deleteRelation( OpResult parentObject, String columnName, OpResult children )
  {
    return unitOfWorkDeleteRelation.deleteRelation( parentObject, columnName, children );
  }

  @Override
  public OpResult deleteRelation( OpResult parentObject, String columnName, String whereClauseForChildren )
  {
    return unitOfWorkDeleteRelation.deleteRelation( parentObject, columnName, whereClauseForChildren );
  }

  @Override
  public OpResult deleteRelation( OpResultValueReference parentObject, String columnName, String[] childrenObjectIds )
  {
    return unitOfWorkDeleteRelation.deleteRelation( parentObject, columnName, childrenObjectIds );
  }

  @Override
  public <E> OpResult deleteRelation( OpResultValueReference parentObject, String columnName, E[] childrenInstances )
  {
    return unitOfWorkDeleteRelation.deleteRelation( parentObject, columnName, childrenInstances );
  }

  @Override
  public OpResult deleteRelation( OpResultValueReference parentObject, String columnName,
                                  List<Map<String, Object>> childrenMaps )
  {
    return unitOfWorkDeleteRelation.deleteRelation( parentObject, columnName, childrenMaps );
  }

  @Override
  public OpResult deleteRelation( OpResultValueReference parentObject, String columnName, OpResult children )
  {
    return unitOfWorkDeleteRelation.deleteRelation( parentObject, columnName, children );
  }

  @Override
  public OpResult deleteRelation( OpResultValueReference parentObject, String columnName, String whereClauseForChildren )
  {
    return unitOfWorkDeleteRelation.deleteRelation( parentObject, columnName, whereClauseForChildren );
  }
}
