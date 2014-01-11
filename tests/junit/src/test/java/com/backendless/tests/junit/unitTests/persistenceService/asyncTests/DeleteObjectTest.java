package com.backendless.tests.junit.unitTests.persistenceService.asyncTests;

import com.backendless.Backendless;
import com.backendless.IDataStore;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.tests.junit.unitTests.persistenceService.asyncEntities.AndroidPersonAsync;
import com.backendless.tests.junit.unitTests.persistenceService.asyncEntities.deleteEntities.BaseDeleteEntityAsync;
import org.junit.Test;

public class DeleteObjectTest extends TestsFrame
{
  @Test
  public void testDeleteObjectWithWrongId()
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        AndroidPersonAsync androidPerson = getRandomAndroidPersonAsync();
        Backendless.Persistence.save( androidPerson, new ResponseCallback<AndroidPersonAsync>()
        {
          @Override
          public void handleResponse( AndroidPersonAsync androidPersonAsync )
          {
            androidPersonAsync.setObjectId( "foobar" );
            Backendless.Persistence.of( AndroidPersonAsync.class ).remove( androidPersonAsync, new AsyncCallback<Long>()
            {
              @Override
              public void handleResponse( Long response )
              {
                failCountDownWith( "Server didn't throw an exception" );
              }

              @Override
              public void handleFault( BackendlessFault fault )
              {
                checkErrorCode( 1000, fault );
              }
            } );
          }
        } );
      }
    } );
  }

  @Test
  public void testDeleteObject()
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        BaseDeleteEntityAsync entity = new BaseDeleteEntityAsync();
        entity.setName( "bot_#delete" );
        entity.setAge( 20 );

        final IDataStore<BaseDeleteEntityAsync> connection = Backendless.Persistence.of( BaseDeleteEntityAsync.class );

        connection.save( entity, new ResponseCallback<BaseDeleteEntityAsync>()
        {
          @Override
          public void handleResponse( final BaseDeleteEntityAsync savedEntity )
          {
            connection.remove( savedEntity, new ResponseCallback<Long>()
            {
              @Override
              public void handleResponse( Long response )
              {
                connection.findById( savedEntity.getObjectId(), new AsyncCallback<BaseDeleteEntityAsync>()
                {
                  @Override
                  public void handleResponse( BaseDeleteEntityAsync response )
                  {
                    failCountDownWith( "Server probably found a result" );
                  }

                  @Override
                  public void handleFault( BackendlessFault fault )
                  {
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
