package com.backendless.tests.junit.unitTests.persistenceService.entities.primitiveEntities;

import com.backendless.tests.junit.unitTests.persistenceService.entities.baseEntities.CreatedEntity;

public class BooleanEntity extends CreatedEntity
{
  private boolean booleanField;

  public boolean isBooleanField()
  {
    return booleanField;
  }

  public void setBooleanField( boolean booleanField )
  {
    this.booleanField = booleanField;
  }

  @Override
  public boolean equals( Object o )
  {
    if( o == null )
      return false;

    if( !o.getClass().equals( this.getClass() ) )
      return false;

    return ((BooleanEntity) o).isBooleanField() == booleanField;
  }
}
