package com.backendless.tests.junit.unitTests.persistenceService.asyncTests;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.tests.junit.unitTests.persistenceService.asyncEntities.updateEntities.BaseUpdateEntityAsync;
import org.junit.Assert;
import org.junit.Test;

public class UpdateRecordTest extends TestsFrame
{
  @Test
  public void testBasicUpdate() throws Throwable
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        final BaseUpdateEntityAsync baseUpdateEntity = new BaseUpdateEntityAsync();
        baseUpdateEntity.setName( "foobar" );
        baseUpdateEntity.setAge( 20 );

        Backendless.Persistence.save( baseUpdateEntity, new ResponseCallback<BaseUpdateEntityAsync>()
        {
          @Override
          public void handleResponse( final BaseUpdateEntityAsync savedEntity )
          {
            savedEntity.setName( "foobar1" );
            savedEntity.setAge( 21 );

            Backendless.Persistence.save( savedEntity, new ResponseCallback<BaseUpdateEntityAsync>()
            {
              @Override
              public void handleResponse( BaseUpdateEntityAsync response )
              {
                Backendless.Persistence.of( BaseUpdateEntityAsync.class ).find( new ResponseCallback<BackendlessCollection<BaseUpdateEntityAsync>>()
                {
                  @Override
                  public void handleResponse( BackendlessCollection<BaseUpdateEntityAsync> response )
                  {
                    BaseUpdateEntityAsync foundEntity = response.getCurrentPage().get( 0 );

                    try
                    {
                      Assert.assertEquals( "Server didn't update an entity", savedEntity, foundEntity );
                      Assert.assertNotNull( "Server didn't set an updated field value", foundEntity.getUpdated() );
                    }
                    catch( Throwable t )
                    {
                      failCountDownWith( t );
                    }

                    countDown();
                  }
                } );
              }
            } );
          }
        } );
      }
    } );
  }
}
