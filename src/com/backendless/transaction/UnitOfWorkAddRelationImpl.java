package com.backendless.transaction;

import com.backendless.exceptions.ExceptionMessage;
import com.backendless.persistence.BackendlessSerializer;
import com.backendless.transaction.operations.Operation;
import com.backendless.transaction.operations.OperationAddRelation;
import com.backendless.transaction.payload.Relation;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class UnitOfWorkAddRelationImpl implements UnitOfWorkAddRelation
{
  AtomicInteger countAddRelation = new AtomicInteger( 1 );

  private final List<Operation> operations;

  public UnitOfWorkAddRelationImpl( List<Operation> operations )
  {
    this.operations = operations;
  }

  @Override
  public <E, U> OpResult addToRelation( E parentObject, String columnName, List<U> children )
  {
    Map<String, Object> parentObjectMap = SerializationHelper.serializeEntityToMap( parentObject );
    String parentTable = BackendlessSerializer.getSimpleName( parentObject.getClass() );

    List<Map<String, Object>> childrenMaps = TransactionHelper.convertInstancesToMaps( children );

    return addToRelation( parentTable, parentObjectMap, columnName, childrenMaps );
  }

  @Override
  public OpResult addToRelation( String parentTable, Map<String, Object> parentObject, String columnName,
                                 List<Map<String, Object>> children )
  {
    if( children == null || children.isEmpty() )
      throw new IllegalArgumentException( ExceptionMessage.NULL_EMPTY_BULK );

    return addToRelation( parentTable, parentObject, columnName, null, children );
  }

  @Override
  public <E> OpResult addToRelation( String parentTable, OpResult parentObject, String columnName, List<E> children )
  {
    if( children == null || children.isEmpty() )
      throw new IllegalArgumentException( ExceptionMessage.NULL_EMPTY_BULK );

    List<Map<String, Object>> childrenMaps;
    if( !children.get( 0 ).getClass().isAssignableFrom( Map.class ) )
      childrenMaps = TransactionHelper.convertInstancesToMaps( children );
    else
      childrenMaps = (List<Map<String, Object>>) children;

    if( OperationType.supportEntityDescriptionResultType.contains( parentObject.getOperationType() ) )
      throw new IllegalArgumentException( ExceptionMessage.REF_TYPE_NOT_SUPPORT );

    return addToRelation( parentTable, parentObject.getReference(), columnName, null, childrenMaps );
  }

  @Override
  public <E> OpResult addToRelation( E parentObject, String columnName, String whereClauseForChildren )
  {
    Map<String, Object> parentObjectMap = SerializationHelper.serializeEntityToMap( parentObject );
    String parentTable = BackendlessSerializer.getSimpleName( parentObject.getClass() );

    return addToRelation( parentTable, parentObjectMap, columnName, whereClauseForChildren, null );
  }

  @Override
  public OpResult addToRelation( String parentTable, Map<String, Object> parentObject, String columnName,
                                 String whereClauseForChildren )
  {
    return addToRelation( parentTable, parentObject, columnName, whereClauseForChildren, null );
  }

  @Override
  public OpResult addToRelation( String parentTable, OpResult parentObject, String columnName,
                                 String whereClauseForChildren )
  {
    if( OperationType.supportEntityDescriptionResultType.contains( parentObject.getOperationType() ) )
      throw new IllegalArgumentException( ExceptionMessage.REF_TYPE_NOT_SUPPORT );

    return addToRelation( parentTable, parentObject.getReference(), columnName, whereClauseForChildren, null );
  }

  private OpResult addToRelation( String parentTable, Map<String, Object> parentObject, String columnName,      //TODO parentObject change to String objectId or change server
                                  String whereClauseForChildren, List<Map<String, Object>> children )
  {
    if( parentObject.isEmpty() )
      throw new IllegalArgumentException( ExceptionMessage.NULL_EMPTY_MAP );

    String operationResultId = OperationType.ADD_RELATION + "_" + countAddRelation.getAndIncrement();

    Relation relation = new Relation();
    relation.setParentObject( parentObject );
    relation.setRelationColumn( columnName );
    relation.setConditional( whereClauseForChildren );
    relation.setUnconditional( children );
    OperationAddRelation operationAddRelation = new OperationAddRelation( OperationType.ADD_RELATION, parentTable,
                                                                          operationResultId, relation );

    operations.add( operationAddRelation );

    return TransactionHelper.makeOpResult( operationResultId, OperationType.ADD_RELATION );
  }
}
