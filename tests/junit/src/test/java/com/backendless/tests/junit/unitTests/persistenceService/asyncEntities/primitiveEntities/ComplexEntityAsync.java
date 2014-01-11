package com.backendless.tests.junit.unitTests.persistenceService.asyncEntities.primitiveEntities;

import com.backendless.tests.junit.unitTests.persistenceService.asyncEntities.baseEntities.CreatedEntityAsync;

import java.util.List;

public class ComplexEntityAsync extends CreatedEntityAsync
{
  private double doubleEntity;
  private List<IntEntityAsync> entities;

  public double getDoubleEntity()
  {
    return doubleEntity;
  }

  public void setDoubleEntity( double doubleEntity )
  {
    this.doubleEntity = doubleEntity;
  }

  public List<IntEntityAsync> getEntities()
  {
    return entities;
  }

  public void setEntities( List<IntEntityAsync> entities )
  {
    this.entities = entities;
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

    return true;
  }
}
