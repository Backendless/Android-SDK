package com.backendless.tests.junit.unitTests.persistenceService.asyncEntities.primitiveEntities;

import com.backendless.tests.junit.unitTests.persistenceService.asyncEntities.baseEntities.CreatedEntityAsync;

public class StringEntityAsync extends CreatedEntityAsync
{
  private String stringEntity;

  public String getStringEntity()
  {
    return stringEntity;
  }

  public void setStringEntity( String stringEntity )
  {
    this.stringEntity = stringEntity;
  }

  @Override
  public boolean equals( Object o )
  {
    if( o == null )
    {
      return false;
    }

    if( !o.getClass().equals( this.getClass() ) )
    {
      return false;
    }

    return ((StringEntityAsync) o).getStringEntity().equals( stringEntity );
  }
}
