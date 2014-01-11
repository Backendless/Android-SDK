package com.backendless.tests.junit.unitTests.persistenceService.entities.baseEntities;

public abstract class ObjectIdEntity
{
  private String objectId;

  public String getObjectId()
  {
    return objectId;
  }

  public void setObjectId( String objectId )
  {
    this.objectId = objectId;
  }

  @Override
  public boolean equals( Object o )
  {
    if( o == null )
      return false;

    if( !o.getClass().equals( this.getClass() ) )
      return false;

    return ((ObjectIdEntity) o).getObjectId().equals( objectId );
  }
}
