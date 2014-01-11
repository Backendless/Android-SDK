package com.backendless.tests.junit.unitTests.persistenceService.multilevelTestEntities;

/**
 * Created with IntelliJ IDEA.
 * User: ivanlappo
 * Date: 5/10/13
 * Time: 12:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class AddressNew
{
  private Employee employee;
  private String city;
  private String street;
  private String house;

  public Employee getEmployee()
  {
    return employee;
  }

  public void setEmployee( Employee employee )
  {
    this.employee = employee;
  }

  public void setCity( String city )
  {
    this.city = city;
  }

  public String getCity()
  {
    return city;
  }

  public void setStreet( String street )
  {
    this.street = street;
  }

  public String getStreet()
  {
    return street;
  }

  public void setHouse( String house )
  {
    this.house = house;
  }

  public String getHouse()
  {
    return house;
  }


}
