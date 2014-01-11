package com.backendless.tests.junit.unitTests.persistenceService.asyncEntities.primitiveEntities;

import com.backendless.tests.junit.unitTests.persistenceService.asyncEntities.baseEntities.CreatedEntityAsync;

import java.util.Date;

public class DateEntityAsync extends CreatedEntityAsync
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
    {
      return true;
    }

    if( o == null || getClass() != o.getClass() )
    {
      return false;
    }

    if( dateField.getTime() - ((DateEntityAsync) o).getDateField().getTime() > 1000 )
    {
      return false;
    }

    return true;
  }
}
