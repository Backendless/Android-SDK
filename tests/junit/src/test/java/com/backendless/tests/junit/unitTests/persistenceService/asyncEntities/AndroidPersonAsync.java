package com.backendless.tests.junit.unitTests.persistenceService.asyncEntities;

import com.backendless.tests.junit.unitTests.persistenceService.asyncEntities.baseEntities.CreatedEntityAsync;

public class AndroidPersonAsync extends CreatedEntityAsync
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
    if( o == null )
    {
      return false;
    }

    if( !o.getClass().equals( this.getClass() ) )
    {
      return false;
    }

    if( !((AndroidPersonAsync) o).getName().equals( name ) )
    {
      return false;
    }

    if( ((AndroidPersonAsync) o).getAge() != age )
    {
      return false;
    }

    return true;
  }
}
