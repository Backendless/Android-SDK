package com.backendless.tests.junit.unitTests.persistenceService.entities.primitiveEntities;

import com.backendless.tests.junit.unitTests.persistenceService.entities.baseEntities.CreatedEntity;

import java.util.Date;

public class DateEntity extends CreatedEntity
{
  private Date dateField;

  public Date getDateField()
  {
    return dateField;
  }

  public void setDateField( Date dateField )
  {
    this.dateField = dateField;
  }

  @Override
  public boolean equals( Object o )
  {
    if( this == o )
      return true;

    if( o == null || getClass() != o.getClass() )
      return false;

    if( dateField.getTime() - ((DateEntity) o).getDateField().getTime() > 1000 )
      return false;

    return true;
  }
}
