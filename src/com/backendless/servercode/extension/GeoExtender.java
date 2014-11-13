package com.backendless.servercode.extension;

import com.backendless.BackendlessCollection;
import com.backendless.geo.BackendlessGeoQuery;
import com.backendless.geo.GeoCategory;
import com.backendless.geo.GeoPoint;
import com.backendless.servercode.ExecutionResult;
import com.backendless.servercode.RunnerContext;

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

  public void beforeAddPoint( RunnerContext context, GeoPoint point ) throws Exception
  {
  }

  public void afterAddPoint( RunnerContext context, GeoPoint point,
                             ExecutionResult<GeoPoint> addedPoint ) throws Exception
  {
  }

  public void beforeUpdatePoint( RunnerContext context, GeoPoint point ) throws Exception
  {
  }

  public void afterUpdatePoint( RunnerContext context, GeoPoint point,
                                ExecutionResult<GeoPoint> updatedPoint ) throws Exception
  {
  }

  public void beforeRemovePoint( RunnerContext context, String pointId ) throws Exception
  {
  }

  public void afterRemovePoint( RunnerContext context, String pointId, ExecutionResult result ) throws Exception
  {
  }

  public void beforeGetCategories( RunnerContext context ) throws Exception
  {
  }

  public void afterGetCategories( RunnerContext context,
                                  ExecutionResult<GeoCategory[]> categories ) throws Exception
  {
  }

  public void beforeGetPoints( RunnerContext context, BackendlessGeoQuery query ) throws Exception
  {
  }

  public void afterGetPoints( RunnerContext context, BackendlessGeoQuery query,
                              ExecutionResult<BackendlessCollection<GeoPoint>> points ) throws Exception
  {
  }

  public void beforeAddCategory( RunnerContext context, String categoryName ) throws Exception
  {
  }

  public void afterAddCategory( RunnerContext context, String categoryName,
                                ExecutionResult<GeoCategory> addedCategory ) throws Exception
  {
  }

  public void beforeDeleteCategory( RunnerContext context, String categoryName ) throws Exception
  {
  }

  public void afterDeleteCategory( RunnerContext context, String categoryName,
                                   ExecutionResult<Boolean> deleted ) throws Exception
  {
  }

  public void beforeRelativeFind( RunnerContext context, BackendlessGeoQuery backendlessGeoQuery ) throws Exception
  {

  }

  public void afterRelativeFind( RunnerContext context, BackendlessGeoQuery backendlessGeoQuery,
                                 ExecutionResult<BackendlessCollection<GeoPoint>> findPoints ) throws Exception
  {
  }
}

