package com.backendless.tests.junit.unitTests.persistenceService.multilevelTestEntities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ivanlappo
 * Date: 5/10/13
 * Time: 12:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class Organization
{
  private List<Employee> employees = new ArrayList<Employee>( );
  private String name;

  public List<Employee> getEmployees()
  {
    return employees;
  }

  public void setEmployees( List<Employee> employees )
  {
    this.employees = employees;
  }

  public void addEmployee( Employee founder )
  {
    employees.add( founder );
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public String getName()
  {
    return name;
  }
}
