package com.backendless.tests.junit.unitTests.persistenceService.asyncEntities.findEntities;

public class FindFirstEntityAsync extends BaseFindEntityAsync
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
