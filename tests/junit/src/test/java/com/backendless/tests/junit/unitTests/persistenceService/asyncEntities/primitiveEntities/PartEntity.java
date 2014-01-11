package com.backendless.tests.junit.unitTests.persistenceService.asyncEntities.primitiveEntities;

import com.backendless.tests.junit.unitTests.persistenceService.asyncEntities.baseEntities.CreatedEntityAsync;

public class PartEntity extends CreatedEntityAsync
{
  AggregatorAsync parentEntity;

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

    return true;
  }

  public AggregatorAsync getParentEntity()
  {
    return parentEntity;
  }

  public void setParentEntity( AggregatorAsync parentEntity )
  {
    this.parentEntity = parentEntity;
  }
}
