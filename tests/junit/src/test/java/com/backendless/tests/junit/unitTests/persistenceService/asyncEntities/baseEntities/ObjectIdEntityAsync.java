package com.backendless.tests.junit.unitTests.persistenceService.asyncEntities.baseEntities;

public abstract class ObjectIdEntityAsync
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
    {
      return false;
    }

    if( !o.getClass().equals( this.getClass() ) )
    {
      return false;
    }

    return ((ObjectIdEntityAsync) o).getObjectId().equals( objectId );
  }
}
