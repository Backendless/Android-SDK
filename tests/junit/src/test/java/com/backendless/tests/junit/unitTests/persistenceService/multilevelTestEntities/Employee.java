package com.backendless.tests.junit.unitTests.persistenceService.multilevelTestEntities;

/**
 * Created with IntelliJ IDEA.
 * User: ivanlappo
 * Date: 5/10/13
 * Time: 12:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class Employee
{
  private Organization organization;
  private AddressNew address;
  private String name;
  private int age;

  public Organization getOrganization()
  {
    return organization;
  }

  public void setOrganization( Organization organization )
  {
    this.organization = organization;
  }

  public AddressNew getAddress()
  {
    return address;
  }

  public void setAddress( AddressNew address )
  {
    this.address = address;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public void setAge( int age )
  {
    this.age = age;
  }

  public int getAge()
  {
    return age;
  }

  public String getName()
  {
    return name;
  }
}
