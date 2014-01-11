package com.backendless.tests.junit.unitTests.persistenceService.entities.findEntities;

import com.backendless.tests.junit.unitTests.persistenceService.entities.AndroidPerson;

public class BaseFindEntity extends AndroidPerson
{
  @Override
  public boolean equals( Object o )
  {
    if( !o.getClass().equals( this.getClass() ) )
      return false;

    return super.equals( o );
  }
}
