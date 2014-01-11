package com.backendless.tests.junit.unitTests.persistenceService.asyncEntities.updateEntities;

import com.backendless.tests.junit.unitTests.persistenceService.asyncEntities.baseEntities.UpdatedEntityAsync;

public class BaseUpdateEntityAsync extends UpdatedEntityAsync
{
  private int age;
  private String name;

  public int getAge()
  {
    return age;
  }

  public void setAge( int age )
  {
    this.age = age;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  @Override
  public boolean equals( Object o )
  {
    if( !o.getClass().equals( this.getClass() ) )
    {
      return false;
    }

    if( !this.getObjectId().equals( ((BaseUpdateEntityAsync) o).getObjectId() ) )
    {
      return false;
    }

    if( !this.getCreated().equals( ((BaseUpdateEntityAsync) o).getCreated() ) )
    {
      return false;
    }

    return (((BaseUpdateEntityAsync) o).getName().equals( name ) && ((BaseUpdateEntityAsync) o).getAge() == age);
  }
}
