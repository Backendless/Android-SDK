package com.backendless.tests.junit.unitTests.persistenceService.entities.updateEntities;

import com.backendless.tests.junit.unitTests.persistenceService.entities.baseEntities.UpdatedEntity;

public class BaseUpdateEntity extends UpdatedEntity
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
      return false;

    if( !this.getObjectId().equals( ((BaseUpdateEntity) o).getObjectId() ) )
      return false;

    if( !this.getCreated().equals( ((BaseUpdateEntity) o).getCreated() ) )
      return false;

    return (((BaseUpdateEntity) o).getName().equals( name ) && ((BaseUpdateEntity) o).getAge() == age);
  }
}
