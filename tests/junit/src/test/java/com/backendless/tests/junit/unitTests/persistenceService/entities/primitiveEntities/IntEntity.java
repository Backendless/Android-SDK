package com.backendless.tests.junit.unitTests.persistenceService.entities.primitiveEntities;

import com.backendless.tests.junit.unitTests.persistenceService.entities.baseEntities.CreatedEntity;

public class IntEntity extends CreatedEntity
{
  private int intField;

  public int getIntField()
  {
    return intField;
  }

  public void setIntField( int intField )
  {
    this.intField = intField;
  }

  @Override
  public boolean equals( Object o )
  {
    if( o == null )
      return false;

    if( !o.getClass().equals( this.getClass() ) )
      return false;

    return intField == ((IntEntity) o).getIntField();
  }
}
