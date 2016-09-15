package com.backendless.persistence;

import com.backendless.IDataStore;
import com.backendless.exceptions.ExceptionMessage;
import com.backendless.utils.StringUtils;

import java.util.Collections;
import java.util.Map;

/**
 * BackendlessDataQuery builder for {@link IDataStore#loadRelations}.
 *
 * Accepts page size, offset and child relation name.
 * Parameter {@link R} added for type safety.
 *
 * Examples:
 * <strong>Typed relation find</strong>
 * <code>
 * LoadRelationsQueryBuilder<Employee> queryBuilder = LoadRelationsQueryBuilder
 *    .of( Employee.class )
 *    .setRelationName( "employees" );
 * List<Employee> employees1 = Backendless.Data.of( Department.class )
 *    .loadRelations( "6286C24A-C17A-9897-FF9C-B08D492CB000", queryBuilder );
 * </code>
 *
 * <strong>Map-driven relation find</strong>
 * <code>
 *   LoadRelationsQueryBuilder<Map<String, Object>> queryBuilderMap = LoadRelationsQueryBuilder
 *      .ofMap()
 *      .setRelationName( "employees" );
 *   List<Map<String, Object>> employeesMapList = Backendless.Data.of( "Department" )
 *      .loadRelations( "6286C24A-C17A-9897-FF9C-B08D492CB000", queryBuilderMap );
 * </code>
 *
 * @param <R> child relation type
 */
public final class LoadRelationsQueryBuilder<R>
{
  private Integer pageSize;
  private Integer offset;
  private String relationName;
  private Class<R> relationType;

  private LoadRelationsQueryBuilder( Class<R> relationType )
  {
    pageSize = BackendlessDataQuery.DEFAULT_PAGE_SIZE;
    offset = BackendlessDataQuery.DEFAULT_OFFSET;
    this.relationType = relationType;
  }

  public static LoadRelationsQueryBuilder<Map<String, Object>> ofMap()
  {
    @SuppressWarnings( "unchecked" )
    LoadRelationsQueryBuilder<Map<String, Object>> queryBuilder = new LoadRelationsQueryBuilder( Map.class );
    return queryBuilder;
  }

  public static <R> LoadRelationsQueryBuilder<R> of( Class<R> relationType )
  {
    return new LoadRelationsQueryBuilder<>( relationType );
  }

  public LoadRelationsQueryBuilder<R> setPageSize( int pageSize )
  {
    this.pageSize = pageSize;
    return this;
  }

  public LoadRelationsQueryBuilder<R> setOffset( int offset )
  {
    this.offset = offset;
    return this;
  }

  public LoadRelationsQueryBuilder<R> setRelationName( String relationName )
  {
    this.relationName = relationName;
    return this;
  }

  /**
   * Updates offset to point at next data page by adding pageSize.
   */
  public LoadRelationsQueryBuilder<R> prepareNextPage()
  {
    offset =+ pageSize;
    return this;
  }


  /**
   * Updates offset to point at previous data page by subtracting pageSize.
   */
  public LoadRelationsQueryBuilder<R> preparePreviousPage()
  {
    offset =- pageSize;
    if( offset < 0 )
      offset = 0;
    return this;
  }

  public Class<R> getRelationType()
  {
    return relationType;
  }

  public BackendlessDataQuery build()
  {
    StringUtils.checkEmpty( relationName, ExceptionMessage.NULL_FIELD( "relationName" ) );

    if( pageSize < 0 )
      throw new IllegalArgumentException( ExceptionMessage.WRONG_OFFSET );
    if( offset < 0 )
      throw new IllegalArgumentException( ExceptionMessage.WRONG_PAGE_SIZE );

    BackendlessDataQuery dataQuery = new BackendlessDataQuery();
    dataQuery.setPageSize( pageSize );
    dataQuery.setOffset( offset );

    QueryOptions queryOptions = new QueryOptions();
    queryOptions.setRelated( Collections.singletonList( relationName ) );
    dataQuery.setQueryOptions( queryOptions );

    return dataQuery;
  }
}
