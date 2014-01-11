package com.backendless.tests.junit.unitTests.persistenceService.asyncEntities.primitiveEntities;

import com.backendless.tests.junit.unitTests.persistenceService.asyncEntities.baseEntities.CreatedEntityAsync;

public class IntEntityAsync extends CreatedEntityAsync
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
    {
      return false;
    }

    if( !o.getClass().equals( this.getClass() ) )
    {
      return false;
    }

    return intField == ((IntEntityAsync) o).getIntField();
  }
}
