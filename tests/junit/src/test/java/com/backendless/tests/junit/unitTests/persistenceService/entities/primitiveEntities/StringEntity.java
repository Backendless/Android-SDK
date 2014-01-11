package com.backendless.tests.junit.unitTests.persistenceService.entities.primitiveEntities;

import com.backendless.tests.junit.unitTests.persistenceService.entities.baseEntities.CreatedEntity;

public class StringEntity extends CreatedEntity
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
      return false;

    if( !o.getClass().equals( this.getClass() ) )
      return false;

    return ((StringEntity) o).getStringEntity().equals( stringEntity );
  }
}
