package com.backendless.transaction;

import com.backendless.Invoker;
import com.backendless.exceptions.ExceptionMessage;
import com.backendless.persistence.BackendlessSerializer;
import com.backendless.transaction.operations.OperationCreate;
import com.backendless.utils.MapEntityUtil;
import com.backendless.utils.ResponderHelper;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class UnitOfWorkS extends UnitOfWork implements IUnitOfWork
{
  public final static String TRANSACTION_MANAGER_SERVER_ALIAS = "com.backendless.services.transaction.TransactionService";

  public UnitOfWorkS()
  {
    super();
  }

  AtomicInteger countCreate = new AtomicInteger( 1 );

  @Override
  public UnitOfWorkStatus execute()
  {
    Object[] args = new Object[]{ this };
    return Invoker.invokeSync( TRANSACTION_MANAGER_SERVER_ALIAS, "execute", args, ResponderHelper.getPOJOAdaptingResponder( UnitOfWorkStatus.class ) );
  }

  @Override
  public OpResult create( String tableName, Map<String, Object> objectMap )
  {
    String operationResultId = OperationType.CREATE + "_" + countCreate.getAndIncrement();
    OperationCreate operationCreate = new OperationCreate( OperationType.CREATE, tableName, operationResultId, objectMap );

    addOperation( operationCreate );

    Map<String, Object> reference = new HashMap<>();
    reference.put( UnitOfWork.REFERENCE_MARKER, true );
    reference.put( UnitOfWork.OP_RESULT_ID, operationResultId );
    return new OpResult( reference );
  }

  @Override
  public  <E> OpResult create( E entity )
  {
    Map<String, Object> entityMap = serializeEntityToMap( entity );
    String tableName = BackendlessSerializer.getSimpleName( entity.getClass() );

    return create( tableName, entityMap );
  }

  @Override
  public OpResult bulkCreate( String tableName, List<Map<String, Object>> arrayOfObjectMaps )
  {
    return null;
  }

  @Override
  public <E> OpResult bulkCreate( List<E> instances )
  {
    return null;
  }

  @Override
  public OpResult update( String tableName, Map<String, Object> objectMap )
  {
    return null;
  }

  @Override
  public <E> OpResult update( E instance )
  {
    return null;
  }

  @Override
  public OpResult bulkUpdate( String tableName, List<Map<String, Object>> arrayOfObjectMaps )
  {
    return null;
  }

  @Override
  public <E> OpResult bulkUpdate( List<E> instances )
  {
    return null;
  }

  @Override
  public OpResult delete( String tableName, Map<String, Object> objectMap )
  {
    return null;
  }

  @Override
  public <E> OpResult delete( E instance )
  {
    return null;
  }

  @Override
  public OpResult delete( String tableName, OpResult result )
  {
    return null;
  }

  @Override
  public OpResult delete( String tableName, OpResult result, int opResultIndex )
  {
    return null;
  }

  @Override
  public OpResult bulkDelete( String tableName, List<Map<String, Object>> arrayOfObjectMaps )
  {
    return null;
  }

  @Override
  public <E> OpResult bulkDelete( List<E> instances )
  {
    return null;
  }

  @Override
  public OpResult bulkDelete( String tableName, OpResult result )
  {
    return null;
  }

  private <E> Map<String, Object> serializeEntityToMap( final E entity )
  {
    if( entity == null )
      throw new IllegalArgumentException( ExceptionMessage.NULL_ENTITY );

    checkDeclaredType( entity.getClass() );
    final Map<String, Object> serializedEntity = BackendlessSerializer.serializeToMap( entity );
    MapEntityUtil.removeNullsAndRelations( serializedEntity);

    return serializedEntity;
  }

  private <T> void checkDeclaredType( Class<T> entityClass )
  {
    if( entityClass.isArray() || entityClass.isAssignableFrom( Iterable.class ) || entityClass.isAssignableFrom( Map.class ) )
      throw new IllegalArgumentException( ExceptionMessage.WRONG_ENTITY_TYPE );

    try
    {
      Constructor[] constructors = entityClass.getConstructors();

      if( constructors.length > 0 )
        entityClass.getConstructor();
    }
    catch( NoSuchMethodException e )
    {
      throw new IllegalArgumentException( ExceptionMessage.ENTITY_MISSING_DEFAULT_CONSTRUCTOR );
    }
  }
}
