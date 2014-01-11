package com.backendless.tests.junit.unitTests.persistenceService.asyncEntities.findEntities;

import com.backendless.tests.junit.unitTests.persistenceService.asyncEntities.AndroidPersonAsync;

public class BaseFindEntityAsync extends AndroidPersonAsync
{
  @Override
  public boolean equals( Object o )
  {
    if( !o.getClass().equals( this.getClass() ) )
    {
      return false;
    }

    return super.equals( o );
  }
}
