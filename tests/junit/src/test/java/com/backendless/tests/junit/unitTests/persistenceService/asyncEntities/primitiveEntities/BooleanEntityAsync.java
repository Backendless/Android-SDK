package com.backendless.tests.junit.unitTests.persistenceService.asyncEntities.primitiveEntities;

import com.backendless.tests.junit.unitTests.persistenceService.asyncEntities.baseEntities.CreatedEntityAsync;

public class BooleanEntityAsync extends CreatedEntityAsync
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
    {
      return false;
    }

    if( !o.getClass().equals( this.getClass() ) )
    {
      return false;
    }

    return ((BooleanEntityAsync) o).isBooleanField() == booleanField;
  }
}
