package com.backendless;

import weborb.reader.DateType;
import weborb.reader.StringType;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Map;

public class Footprint
{
  public final static String OBJECT_ID_FIELD_NAME = "objectId";
  public final static String CREATED_FIELD_NAME = "created";
  public final static String UPDATED_ID_FIELD_NAME = "updated";
  public final static String META_FIELD_NAME = "__meta";

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

    if( entity.containsKey( Footprint.OBJECT_ID_FIELD_NAME ) )
    {
      Object objectId = entity.get( Footprint.OBJECT_ID_FIELD_NAME );

      if( objectId instanceof StringType )
        entityFootprint.setObjectId( (String) ((StringType) objectId).defaultAdapt() );
    }

    if( entity.containsKey( Footprint.CREATED_FIELD_NAME ) )
    {
      Object created = entity.get( Footprint.CREATED_FIELD_NAME );

      if( created instanceof DateType )
        entityFootprint.setCreated( (Date) ((DateType) created).defaultAdapt() );
    }

    if( entity.containsKey( Footprint.UPDATED_ID_FIELD_NAME ) )
    {
      Object updated = entity.get( Footprint.UPDATED_ID_FIELD_NAME );

      if( updated instanceof DateType )
        entityFootprint.setUpdated( (Date) ((DateType) updated).defaultAdapt() );
    }

    if( entity.containsKey( Footprint.META_FIELD_NAME ) )
    {
      Object meta = entity.get( Footprint.META_FIELD_NAME );

      if( meta instanceof StringType )
        entityFootprint.set__meta( (String) ((StringType) meta).defaultAdapt() );
    }

    return entityFootprint;
  }

  public void initEntity( Object entity )
  {
    initObjectId( entity );
    initCreated( entity );
    initUpdated( entity );
    init__meta( entity );
  }

  private void initObjectId( Object entity )
  {
    Field objectIdField = null;
    try
    {
      objectIdField = entity.getClass().getDeclaredField( Footprint.OBJECT_ID_FIELD_NAME );
    }
    catch( NoSuchFieldException e )
    {
      //no field - no value set
      return;
    }

    boolean accessibleChange = false;

    if( !objectIdField.isAccessible() )
    {
      objectIdField.setAccessible( true );
      accessibleChange = true;
    }

    try
    {
      objectIdField.set( entity, objectId );
    }
    catch( IllegalAccessException e )
    {
      //never thrown
    }

    if( accessibleChange )
    {
      objectIdField.setAccessible( false );
      accessibleChange = false;
    }
  }

  private void initCreated( Object entity )
  {
    Field createdField = null;
    try
    {
      createdField = entity.getClass().getDeclaredField( Footprint.CREATED_FIELD_NAME );
    }
    catch( NoSuchFieldException e )
    {
      //no field - no value set
      return;
    }

    boolean accessibleChange = false;

    if( !createdField.isAccessible() )
    {
      createdField.setAccessible( true );
      accessibleChange = true;
    }

    try
    {
      createdField.set( entity, created );
    }
    catch( IllegalAccessException e )
    {
      //never thrown
    }

    if( accessibleChange )
    {
      createdField.setAccessible( false );
      accessibleChange = false;
    }
  }

  private void initUpdated( Object entity )
  {
    Field updatedField = null;
    try
    {
      updatedField = entity.getClass().getDeclaredField( Footprint.UPDATED_ID_FIELD_NAME );
    }
    catch( NoSuchFieldException e )
    {
      //no field - no value set
      return;
    }

    boolean accessibleChange = false;

    if( !updatedField.isAccessible() )
    {
      updatedField.setAccessible( true );
      accessibleChange = true;
    }

    try
    {
      updatedField.set( entity, updated );
    }
    catch( IllegalAccessException e )
    {
      //never thrown
    }

    if( accessibleChange )
    {
      updatedField.setAccessible( false );
      accessibleChange = false;
    }
  }

  private void init__meta( Object entity )
  {
    Field __metaField = null;
    try
    {
      __metaField = entity.getClass().getDeclaredField( Footprint.META_FIELD_NAME );
    }
    catch( NoSuchFieldException e )
    {
      //no field - no value set
      return;
    }

    boolean accessibleChange = false;

    if( !__metaField.isAccessible() )
    {
      __metaField.setAccessible( true );
      accessibleChange = true;
    }

    try
    {
      __metaField.set( entity, __meta );
    }
    catch( IllegalAccessException e )
    {
      //never thrown
    }

    if( accessibleChange )
    {
      __metaField.setAccessible( false );
      accessibleChange = false;
    }
  }
}
