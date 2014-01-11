package com.backendless.tests.junit.unitTests.persistenceService.asyncEntities.primitiveEntities;

import com.backendless.tests.junit.unitTests.persistenceService.asyncEntities.baseEntities.CreatedEntityAsync;

public class DoubleEntityAsync extends CreatedEntityAsync
{
  private double doubleEntity;

  public double getDoubleEntity()
  {
    return doubleEntity;
  }

  public void setDoubleEntity( double doubleEntity )
  {
    this.doubleEntity = doubleEntity;
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

    if( doubleEntity - ((DoubleEntityAsync) o).getDoubleEntity() > 0.0000000001 )
    {
      return false;
    }

    return true;
  }
}
