package com.backendless.tests.junit.unitTests.persistenceService.syncTests;

import com.backendless.Backendless;
import com.backendless.IDataStore;
import com.backendless.tests.junit.unitTests.persistenceService.entities.AndroidPerson;
import com.backendless.tests.junit.unitTests.persistenceService.entities.deleteEntities.BaseDeleteEntity;
import org.junit.Test;

public class DeleteObjectTest extends TestsFrame
{
  @Test
  public void testDeleteObjectWithWrongId() throws Throwable
  {
    AndroidPerson androidPerson = getRandomAndroidPerson();
    Backendless.Persistence.save( androidPerson );
    androidPerson.setObjectId( "foobar" );

    try
    {
      Backendless.Persistence.of( AndroidPerson.class ).remove( androidPerson );
      logTestFailed( "Server didn't throw an exception" );
    }
    catch( Throwable e )
    {
      checkErrorCode( 1000, e );
    }
  }

  @Test
  public void testDeleteObject() throws Throwable
  {
    BaseDeleteEntity entity = new BaseDeleteEntity();
    entity.setName( "bot_#delete" );
    entity.setAge( 20 );

    IDataStore<BaseDeleteEntity> connection = Backendless.Persistence.of( BaseDeleteEntity.class );

    BaseDeleteEntity savedEntity = connection.save( entity );
    connection.remove( savedEntity );

    try
    {
      connection.findById( savedEntity.getObjectId() );
      logTestFailed( "Server probably found a result" );
    }
    catch( Throwable e )
    {
    }
  }
}
