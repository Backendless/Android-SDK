package com.backendless.tests.junit.unitTests.persistenceService.entities.baseEntities;

import java.util.Date;

public abstract class CreatedEntity extends ObjectIdEntity
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
      return false;

    if( !o.getClass().equals( this.getClass() ) )
      return false;

    if( !((CreatedEntity) o).getCreated().equals( created ) )
      return false;

    return true;
  }
}
