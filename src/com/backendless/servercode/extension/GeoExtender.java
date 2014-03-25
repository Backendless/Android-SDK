package com.backendless.servercode.extension;

import com.backendless.BackendlessCollection;
import com.backendless.commons.exception.ExceptionRepresentation;
import com.backendless.geo.BackendlessGeoQuery;
import com.backendless.geo.GeoCategory;
import com.backendless.geo.GeoPoint;
import com.backendless.servercode.ExecutionResult;
import com.backendless.servercode.RunnerContext;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ivanlappo
 * Date: 5/20/13
 * Time: 12:40 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class GeoExtender
{
  public GeoExtender()
  {
  }

  public void beforeSavePoint( RunnerContext context, GeoPoint point ) throws Exception
  {
  }

  public GeoPoint afterSavePoint( RunnerContext context, GeoPoint point,
                                  ExecutionResult<GeoPoint> savedPoint ) throws Exception
  {
    return savedPoint.getResult();
  }

  public void beforeAddPoint( RunnerContext context, GeoPoint point ) throws Exception
  {
  }

  public GeoPoint afterAddPoint( RunnerContext context, GeoPoint point,
                                 ExecutionResult<GeoPoint> addedPoint ) throws Exception
  {
    return addedPoint.getResult();
  }

  public void beforeUpdatePoint( RunnerContext context, GeoPoint point ) throws Exception
  {
  }

  public GeoPoint afterUpdatePoint( RunnerContext context, GeoPoint point,
                                    ExecutionResult<GeoPoint> updatedPoint ) throws Exception
  {
    return updatedPoint.getResult();
  }

  public void beforeRemovePoint( RunnerContext context, String pointId ) throws Exception
  {
  }

  public void afterRemovePoint( RunnerContext context, String pointId,
                                ExceptionRepresentation exceptionRepresentation ) throws Exception
  {
  }

  public void beforeGetCategories( RunnerContext context ) throws Exception
  {
  }

  public List<GeoCategory> afterGetCategories( RunnerContext context,
                                               ExecutionResult<List<GeoCategory>> categories ) throws Exception
  {
    return categories.getResult();
  }

  public void beforeGetPoints( RunnerContext context, BackendlessGeoQuery query ) throws Exception
  {
  }

  public BackendlessCollection<GeoPoint> afterGetPoints( RunnerContext context, BackendlessGeoQuery query,
                                                         ExecutionResult<BackendlessCollection<GeoPoint>> points ) throws Exception
  {
    return points.getResult();
  }

  public void beforeAddCategory( RunnerContext context, String categoryName ) throws Exception
  {
  }

  public GeoCategory afterAddCategory( RunnerContext context, String categoryName,
                                       ExecutionResult<GeoCategory> addedCategory ) throws Exception
  {
    return addedCategory.getResult();
  }

  public void beforeDeleteCategory( RunnerContext context, String categoryName ) throws Exception
  {
  }

  public Boolean afterDeleteCategory( RunnerContext context, String categoryName,
                                      ExecutionResult<Boolean> deleted ) throws Exception
  {
    return deleted.getResult();
  }

  public void beforeRelativeFind( RunnerContext context, BackendlessGeoQuery backendlessGeoQuery ) throws Exception
  {

  }

  public BackendlessCollection afterRelativeFind( RunnerContext context, BackendlessGeoQuery backendlessGeoQuery,
                                                  ExecutionResult<BackendlessCollection<GeoPoint>> findPoints ) throws Exception
  {
    return findPoints.getResult();
  }
}

