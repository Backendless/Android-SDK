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
 * Parameter R added for type safety.
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
  private String relationName;
  private Class<R> relationType;
  private PagedQueryBuilder<LoadRelationsQueryBuilder<R>> pagedQueryBuilder;

  private LoadRelationsQueryBuilder( Class<R> relationType )
  {
    pagedQueryBuilder = new PagedQueryBuilder<>( this );
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

  public BackendlessDataQuery build()
  {
    StringUtils.checkEmpty( relationName, ExceptionMessage.NULL_FIELD( "relationName" ) );
    BackendlessDataQuery dataQuery = pagedQueryBuilder.build();
    QueryOptions queryOptions = new QueryOptions();
    queryOptions.setRelated( Collections.singletonList( relationName ) );
    dataQuery.setQueryOptions( queryOptions );

    return dataQuery;
  }

  public LoadRelationsQueryBuilder<R> setRelationName( String relationName )
  {
    this.relationName = relationName;
    return this;
  }

  public LoadRelationsQueryBuilder<R> setPageSize( int pageSize )
  {
    return pagedQueryBuilder.setPageSize( pageSize );
  }

  public LoadRelationsQueryBuilder<R> setOffset( int offset )
  {
    return pagedQueryBuilder.setOffset( offset );
  }

  public LoadRelationsQueryBuilder<R> prepareNextPage()
  {
    return pagedQueryBuilder.prepareNextPage();
  }

  public LoadRelationsQueryBuilder<R> preparePreviousPage()
  {
    return pagedQueryBuilder.preparePreviousPage();
  }

  public Class<R> getRelationType()
  {
    return relationType;
  }
}
