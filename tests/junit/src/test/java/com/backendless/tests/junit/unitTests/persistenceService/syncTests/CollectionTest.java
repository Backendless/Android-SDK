package com.backendless.tests.junit.unitTests.persistenceService.syncTests;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.QueryOptions;
import com.backendless.tests.junit.unitTests.persistenceService.entities.collectionEntities.GetPageEntity;
import com.backendless.tests.junit.unitTests.persistenceService.entities.collectionEntities.NextPageEntity;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class CollectionTest extends TestsFrame
{
  @Test
  public void testCollectionNextPage() throws Throwable
  {
    List<NextPageEntity> nextPageEntities = new ArrayList<NextPageEntity>();

    for( int i = 10; i < 30; i++ )
    {
      NextPageEntity entity = new NextPageEntity();
      entity.setName( "name#" + i );
      entity.setAge( 20 + i );

      Backendless.Persistence.save( entity );

      if( i >= 20 )
      {
        nextPageEntities.add( entity );
      }

      Thread.sleep( 1000 );
    }

    BackendlessDataQuery dataQuery = new BackendlessDataQuery( new QueryOptions( 10, 0, "age" ) );
    BackendlessCollection<NextPageEntity> collection = Backendless.Persistence.of( NextPageEntity.class ).find( dataQuery ).nextPage();

    Assert.assertNotNull( "Next page returned a null object", collection );
    Assert.assertNotNull( "Next page contained a wrong data size", collection.getCurrentPage() );
    Assert.assertEquals( "Next page returned a wrong size", nextPageEntities.size(), collection.getCurrentPage().size() );

    for( NextPageEntity entity : nextPageEntities )
    {
      Assert.assertTrue( "Server result didn't contain expected entity", collection.getCurrentPage().contains( entity ) );
    }
  }

  @Test
  public void testCollectionGetPage() throws Throwable
  {
    List<GetPageEntity> getPageEntities = new ArrayList<GetPageEntity>();

    for( int i = 10; i < 30; i++ )
    {
      GetPageEntity entity = new GetPageEntity();
      entity.setName( "name#" + i );
      entity.setAge( 20 + i );

      Backendless.Persistence.save( entity );

      if( i > 19 && i < 30 )
      {
        getPageEntities.add( entity );
      }

      Thread.sleep( 1000 );
    }

    BackendlessDataQuery dataQuery = new BackendlessDataQuery( new QueryOptions( 10, 0, "age" ) );
    BackendlessCollection<GetPageEntity> collection = Backendless.Persistence.of( GetPageEntity.class ).find( dataQuery ).getPage( 10, 10 );

    Assert.assertNotNull( "Next page returned a null object", collection );
    Assert.assertNotNull( "Next page contained a wrong data size", collection.getCurrentPage() );
    Assert.assertEquals( "Next page returned a wrong size", getPageEntities.size(), collection.getCurrentPage().size() );

    for( GetPageEntity entity : getPageEntities )
    {
      Assert.assertTrue( "Server result didn't contain expected entity", collection.getCurrentPage().contains( entity ) );
    }
  }
}
