package com.backendless.transaction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UnitOfWork implements IUnitOfWork
{
  private final UnitOfWorkCreate unitOfWorkCreate;
  private final UnitOFWorkUpdate unitOFWorkUpdate;
  private final UnitOfWorkDelete unitOfWorkDelete;

  public UnitOfWork()
  {
    unitOfWorkCreate = new UnitOfWorkCreateImpl();
    unitOFWorkUpdate = new UnitOfWorkUpdateImpl();
    unitOfWorkDelete = new UnitOfWorkDeleteImpl();
  }

  @Override
  public OpResult create( String tableName, Map<String, Object> objectMap )
  {
    return unitOfWorkCreate.create( tableName, objectMap );
  }

  @Override
  public <E> OpResult create( E instance )
  {
    return unitOfWorkCreate.create( instance );
  }

  @Override
  public OpResult create( String tableName,
                          List<Map<String, Object>> arrayOfObjectMaps )
  {
    return unitOfWorkCreate.create( tableName, arrayOfObjectMaps );
  }

  @Override
  public <E> OpResult create( List<E> instances )
  {
    return unitOfWorkCreate.create( instances );
  }

  @Override
  public OpResult update( String tableName, Map<String, Object> objectMap )
  {
    return unitOFWorkUpdate.update( tableName, objectMap );
  }

  @Override
  public <E> OpResult update( E instance )
  {
    return unitOFWorkUpdate.update( instance );
  }

  @Override
  public OpResult update( String tableName, String query, Map<String, Object> objectMap )
  {
    return unitOFWorkUpdate.update( tableName, query, objectMap );
  }

  @Override
  public OpResult update( String tableName,
                          List<HashMap<String, Object>> arrayOfHashMaps )
  {
    return unitOFWorkUpdate.update( tableName, arrayOfHashMaps );
  }

  @Override
  public <E> OpResult update( List<E> instances )
  {
    return unitOFWorkUpdate.update( instances );
  }

  @Override
  public <E> OpResult bulkUpdate( String query, E instances )
  {
    return unitOFWorkUpdate.bulkUpdate( query, instances );
  }

  @Override
  public OpResult delete( String tableName, Map<String, Object> objectMap )
  {
    return unitOfWorkDelete.delete( tableName, objectMap );
  }

  @Override
  public <E> OpResult delete( E instance )
  {
    return unitOfWorkDelete.delete( instance );
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
  public OpResult delete( String tableName,
                          List<Map<String, Object>> arrayOfObjectMaps )
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

  @Override
  public UnitOfWorkStatus execute()
  {
    return null;
  }
}
