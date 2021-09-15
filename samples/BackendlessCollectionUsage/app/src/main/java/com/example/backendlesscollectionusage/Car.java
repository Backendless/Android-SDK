
package com.example.backendlesscollectionusage;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.persistence.BackendlessDataCollection;
import com.backendless.persistence.DataQueryBuilder;

import java.util.Date;
import java.util.List;

public class Car implements BackendlessDataCollection.Identifiable<Car>
{
  private String model;
  private String objectId;
  private String brand;
  private String ownerId;
  private Date updated;
  private Date created;

  @Override
  public void setObjectId( String s )
  {
    this.objectId = objectId;
  }

  public String getModel()
  {
    return model;
  }

  public void setModel( String model )
  {
    this.model = model;
  }

  public String getObjectId()
  {
    return objectId;
  }

  public String getBrand()
  {
    return brand;
  }

  public void setBrand( String brand )
  {
    this.brand = brand;
  }

  public String getOwnerId()
  {
    return ownerId;
  }

  public Date getUpdated()
  {
    return updated;
  }

  public Date getCreated()
  {
    return created;
  }

  public Car save()
  {
    return Backendless.Data.of( Car.class ).save( this );
  }

  public void saveAsync( AsyncCallback<Car> callback )
  {
    Backendless.Data.of( Car.class ).save( this, callback );
  }

  public Long remove()
  {
    return Backendless.Data.of( Car.class ).remove( this );
  }

  public void removeAsync( AsyncCallback<Long> callback )
  {
    Backendless.Data.of( Car.class ).remove( this, callback );
  }

  public static Car findById( String id )
  {
    return Backendless.Data.of( Car.class ).findById( id );
  }

  public static void findByIdAsync( String id, AsyncCallback<Car> callback )
  {
    Backendless.Data.of( Car.class ).findById( id, callback );
  }

  public static Car findFirst()
  {
    return Backendless.Data.of( Car.class ).findFirst();
  }

  public static void findFirstAsync( AsyncCallback<Car> callback )
  {
    Backendless.Data.of( Car.class ).findFirst( callback );
  }

  public static Car findLast()
  {
    return Backendless.Data.of( Car.class ).findLast();
  }

  public static void findLastAsync( AsyncCallback<Car> callback )
  {
    Backendless.Data.of( Car.class ).findLast( callback );
  }

  public static List<Car> find( DataQueryBuilder queryBuilder )
  {
    return Backendless.Data.of( Car.class ).find( queryBuilder );
  }

  public static void findAsync( DataQueryBuilder queryBuilder, AsyncCallback<List<Car>> callback )
  {
    Backendless.Data.of( Car.class ).find( queryBuilder, callback );
  }
}