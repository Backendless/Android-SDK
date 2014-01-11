package com.backendless.tests.junit.unitTests.persistenceService.asyncEntities.primitiveEntities;

import com.backendless.tests.junit.unitTests.persistenceService.asyncEntities.baseEntities.CreatedEntityAsync;

import java.util.List;

public class AggregatorAsync extends CreatedEntityAsync
{
  List<PartEntity> entities;
  PartEntity entity;

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

  public List<PartEntity> getEntities()
  {
    return entities;
  }

  public void setEntities( List<PartEntity> entities )
  {
    this.entities = entities;
  }

  public PartEntity getEntity()
  {
    return entity;
  }

  public void setEntity( PartEntity entity )
  {
    this.entity = entity;
  }
}
