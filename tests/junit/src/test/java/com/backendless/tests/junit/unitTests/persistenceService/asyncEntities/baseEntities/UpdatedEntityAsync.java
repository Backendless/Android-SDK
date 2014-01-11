package com.backendless.tests.junit.unitTests.persistenceService.asyncEntities.baseEntities;

import java.util.Date;

public abstract class UpdatedEntityAsync extends CreatedEntityAsync
{
  private Date updated;

  public Date getUpdated()
  {
    return updated;
  }

  public void setUpdated( Date updated )
  {
    this.updated = updated;
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

    if( updated.getTime() - ((UpdatedEntityAsync) o).getUpdated().getTime() > 1000 )
    {
      return false;
    }

    return super.equals( o );
  }
}
