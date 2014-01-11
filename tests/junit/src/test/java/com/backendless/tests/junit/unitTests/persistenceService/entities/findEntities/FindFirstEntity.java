package com.backendless.tests.junit.unitTests.persistenceService.entities.findEntities;

public class FindFirstEntity extends BaseFindEntity
{
  @Override
  public boolean equals( Object o )
  {
    if( !o.getClass().equals( this.getClass() ) )
      return false;

    return super.equals( o );
  }
}
