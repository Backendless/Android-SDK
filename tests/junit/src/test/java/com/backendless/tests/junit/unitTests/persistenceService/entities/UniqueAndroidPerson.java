package com.backendless.tests.junit.unitTests.persistenceService.entities;

import java.util.Date;

public class UniqueAndroidPerson extends AndroidPerson
{
  private Date birthday;

  public Date getBirthday()
  {
    return birthday;
  }

  public void setBirthday( Date birthday )
  {
    this.birthday = birthday;
  }

  @Override
  public boolean equals( Object o )
  {
    if( this == o )
      return true;

    if( o == null || getClass() != o.getClass() )
      return false;

    if( !super.equals( o ) )
      return false;

    UniqueAndroidPerson that = (UniqueAndroidPerson) o;

    if( !birthday.equals( that.birthday ) )
      return false;

    return true;
  }

  @Override
  public int hashCode()
  {
    return birthday.hashCode();
  }
}
