package com.backendless;

import weborb.reader.DateType;
import weborb.reader.StringType;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Map;

public class Footprint
{
  private String objectId;
  private Date created;
  private Date updated;
  private String __meta;

  public String getObjectId()
  {
    return objectId;
  }

  public void setObjectId( String objectId )
  {
    this.objectId = objectId;
  }

  public Date getCreated()
  {
    return created;
  }

  public void setCreated( Date created )
  {
    this.created = created;
  }

  public Date getUpdated()
  {
    return updated;
  }

  public void setUpdated( Date updated )
  {
    this.updated = updated;
  }

  public String get__meta()
  {
    return __meta;
  }

  public void set__meta( String __meta )
  {
    this.__meta = __meta;
  }

  @Override
  public String toString()
  {
    final StringBuilder sb = new StringBuilder( "Footprint{" );
    sb.append( "objectId='" ).append( objectId ).append( '\'' );
    sb.append( ", created=" ).append( created );

    if( updated != null )
      sb.append( ", updated=" ).append( updated );

    if( __meta != null )
      sb.append( ", __meta='" ).append( __meta );

    sb.append( '\'' ).append( '}' );

    return sb.toString();
  }

  static Footprint initFromEntity( Map<String, Object> entity )
  {
    Footprint entityFootprint = new Footprint();

    if( entity.containsKey( Persistence.DEFAULT_OBJECT_ID_FIELD ) )
    {
      Object objectId = entity.get( Persistence.DEFAULT_OBJECT_ID_FIELD );

      if( objectId instanceof StringType )
        entityFootprint.setObjectId( (String) ((StringType) objectId).defaultAdapt() );
    }

    if( entity.containsKey( Persistence.DEFAULT_CREATED_FIELD ) )
    {
      Object created = entity.get( Persistence.DEFAULT_CREATED_FIELD );

      if( created instanceof DateType )
        entityFootprint.setCreated( (Date) ((DateType) created).defaultAdapt() );
    }

    if( entity.containsKey( Persistence.DEFAULT_UPDATED_FIELD ) )
    {
      Object updated = entity.get( Persistence.DEFAULT_UPDATED_FIELD );

      if( updated instanceof DateType )
        entityFootprint.setUpdated( (Date) ((DateType) updated).defaultAdapt() );
    }

    if( entity.containsKey( Persistence.DEFAULT_META_FIELD ) )
    {
      Object meta = entity.get( Persistence.DEFAULT_META_FIELD );

      if( meta instanceof StringType )
        entityFootprint.set__meta( (String) ((StringType) meta).defaultAdapt() );
    }

    return entityFootprint;
  }

  public void initObjectId( Object entity )
  {
    Field objectIdField = null;
    try
    {
      objectIdField = entity.getClass().getDeclaredField( Persistence.DEFAULT_OBJECT_ID_FIELD );
    }
    catch( NoSuchFieldException e )
    {
      //no field - no value set
      return;
    }

    if( !objectIdField.isAccessible() )
    {
      objectIdField.setAccessible( true );
    }

    try
    {
      objectIdField.set( entity, objectId );
    }
    catch( IllegalAccessException e )
    {
      //never thrown
    }
  }
}
