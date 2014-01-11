/*
 * ********************************************************************************************************************
 *  <p/>
 *  BACKENDLESS.COM CONFIDENTIAL
 *  <p/>
 *  ********************************************************************************************************************
 *  <p/>
 *  Copyright 2012 BACKENDLESS.COM. All Rights Reserved.
 *  <p/>
 *  NOTICE: All information contained herein is, and remains the property of Backendless.com and its suppliers,
 *  if any. The intellectual and technical concepts contained herein are proprietary to Backendless.com and its
 *  suppliers and may be covered by U.S. and Foreign Patents, patents in process, and are protected by trade secret
 *  or copyright law. Dissemination of this information or reproduction of this material is strictly forbidden
 *  unless prior written permission is obtained from Backendless.com.
 *  <p/>
 *  ********************************************************************************************************************
 */

package com.backendless.tests.junit.unitTests.persistenceService.syncTests;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.QueryOptions;
import com.backendless.tests.junit.unitTests.persistenceService.entities.findEntities.FindBaseProperties;
import com.backendless.tests.junit.unitTests.persistenceService.entities.findEntities.FindTooBigOffset;
import com.backendless.tests.junit.unitTests.persistenceService.entities.findEntities.FindTooBigPagesize;
import com.backendless.tests.junit.unitTests.persistenceService.entities.findEntities.FindWithPageSizeAndOffsetEntity;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class SuspiciousFindObjectTest extends TestsFrame
{
  //This tests are failing if move to main FindObjectTest class
  //Needs investigation

  @Test
  public void testFindWithPageSizeAndOffset() throws Throwable
  {
    List<FindWithPageSizeAndOffsetEntity> entities = new ArrayList();

    for( int i = 0; i < 10; i++ )
    {
      FindWithPageSizeAndOffsetEntity findWithPageSizeAndOffsetEntity = new FindWithPageSizeAndOffsetEntity();
      findWithPageSizeAndOffsetEntity.setName( "bot_#" + i );
      findWithPageSizeAndOffsetEntity.setAge( 20 + i );

      Backendless.Persistence.save( findWithPageSizeAndOffsetEntity );

      if( i == 1 || i == 2 )
      {
        entities.add( findWithPageSizeAndOffsetEntity );
      }
    }

    BackendlessDataQuery dataQuery = new BackendlessDataQuery( new QueryOptions( 2, 1, "age" ) );
    BackendlessCollection<FindWithPageSizeAndOffsetEntity> backendlessCollection = Backendless.Persistence.of( FindWithPageSizeAndOffsetEntity.class ).find( dataQuery );

    Assert.assertEquals( "Server returned wrong number of objects", entities.size(), backendlessCollection.getCurrentPage().size() );

    for( FindWithPageSizeAndOffsetEntity entity : entities )
    {
      Assert.assertTrue( "Server result didn't contain expected entity", backendlessCollection.getCurrentPage().contains( entity ) );
    }
  }

  @Test
  public void testFindWithTooBigOffset() throws Throwable
  {
    FindTooBigOffset findTooBigOffset = new FindTooBigOffset();
    findTooBigOffset.setName( "bot_#toobigoffset" );
    findTooBigOffset.setAge( 20 );
    Backendless.Persistence.save( findTooBigOffset );

    int totalObjects = Backendless.Persistence.of( FindTooBigOffset.class ).find().getTotalObjects();
    BackendlessDataQuery dataQuery = new BackendlessDataQuery( new QueryOptions( 1, totalObjects + 1 ) );

    try
    {
      BackendlessCollection<FindTooBigOffset> result = Backendless.Persistence.of( FindTooBigOffset.class ).find( dataQuery );
      Assert.assertEquals( "Server return wrong result", 0, result.getCurrentPage().size() );
    }
    catch( Throwable e )
    {
      checkErrorCode( 1004, e );
    }
  }

  @Test
  public void testFindWithTooBigPagesize() throws Throwable
  {
    FindTooBigPagesize findTooBigPagesize = new FindTooBigPagesize();
    findTooBigPagesize.setName( "bot_#toobigpagesize" );
    findTooBigPagesize.setAge( 20 );
    Backendless.Persistence.save( findTooBigPagesize );

    BackendlessDataQuery dataQuery = new BackendlessDataQuery( new QueryOptions( 101, 0 ) );

    try
    {
      BackendlessCollection<FindTooBigPagesize> result = Backendless.Persistence.of( FindTooBigPagesize.class ).find( dataQuery );
      logTestFailed( "Server didn't throw an exception. Requested pagesize 101, but found " + result.getCurrentPage().size() );
    }
    catch( Throwable e )
    {
      checkErrorCode( 1005, e );
    }
  }

  @Test
  public void testFindWithProperties() throws Throwable
  {
    List<Integer> entities = new ArrayList();

    for( int i = 0; i < 20; i++ )
    {
      FindBaseProperties findBaseProperties = new FindBaseProperties();
      findBaseProperties.setName( "bot_#" + i );
      int age = 20 + i;
      entities.add( age );
      findBaseProperties.setAge( age );
      Backendless.Persistence.save( findBaseProperties );
    }

    List<String> properties = new ArrayList();
    properties.add( "age" );

    BackendlessDataQuery dataQuery = new BackendlessDataQuery( properties );
    QueryOptions queryOptions = new QueryOptions( 20, 0, "age" );
    dataQuery.setQueryOptions( queryOptions );

    BackendlessCollection<FindBaseProperties> backendlessCollection = Backendless.Persistence.of( FindBaseProperties.class ).find( dataQuery );

    Assert.assertEquals( "Server found wrong number of objects", entities.size(), backendlessCollection.getTotalObjects() );
    Assert.assertEquals( "Server returned wrong number of objects", entities.size(), backendlessCollection.getCurrentPage().size() );

    for( FindBaseProperties entity : backendlessCollection.getCurrentPage() )
    {
      Assert.assertTrue( "Server result contained wrong age field value", entities.contains( entity.getAge() ) );
      entities.remove( (Integer) entity.getAge() );
      Assert.assertNull( "Server result contained non null name", entity.getName() );
      Assert.assertNull( "Server result contained non null objectid", entity.getObjectId() );
      Assert.assertNull( "Server result contained non null created date", entity.getCreated() );
    }
  }
}
