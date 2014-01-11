package com.backendless.tests.junit.unitTests.persistenceService.asyncEntities.baseEntities;

import java.util.Date;

public abstract class CreatedEntityAsync extends ObjectIdEntityAsync
{
  private Date created;

  public Date getCreated()
  {
    return created;
  }

  public void setCreated( Date created )
  {
    this.created = created;
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

    if( !((CreatedEntityAsync) o).getCreated().equals( created ) )
    {
      return false;
    }

    return true;
  }
}
