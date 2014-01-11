package com.backendless.tests.junit.unitTests.persistenceService.asyncEntities;

import java.util.Date;

public class UniqueAndroidPersonAsync extends AndroidPersonAsync
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
    {
      return true;
    }

    if( o == null || getClass() != o.getClass() )
    {
      return false;
    }

    if( !super.equals( o ) )
    {
      return false;
    }

    UniqueAndroidPersonAsync that = (UniqueAndroidPersonAsync) o;

    if( !birthday.equals( that.birthday ) )
    {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode()
  {
    return birthday.hashCode();
  }
}
