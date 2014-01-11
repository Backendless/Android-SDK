/*
 * ********************************************************************************************************************
 *  <p/>
 *  BACKENDLESS.COM CONFIDENTIAL
 *  <p/>
 *  ********************************************************************************************************************
 *  <p/>
 *  Copyright 2012 BACKENDLESS.COM. All Rights Reserved.
 *  <p/>
 *  NOTICE: All information contained herein is, and remains the property of Backendless.com and its suppliers,
 *  if any. The intellectual and technical concepts contained herein are proprietary to Backendless.com and its
 *  suppliers and may be covered by U.S. and Foreign Patents, patents in process, and are protected by trade secret
 *  or copyright law. Dissemination of this information or reproduction of this material is strictly forbidden
 *  unless prior written permission is obtained from Backendless.com.
 *  <p/>
 *  ********************************************************************************************************************
 */

package com.backendless.property;

public class ObjectProperty extends AbstractProperty
{
  private String relatedTable;
  private Object defaultValue;

  public ObjectProperty()
  {
  }

  public ObjectProperty( String name )
  {
    setName( name );
  }

  public ObjectProperty( String name, DateTypeEnum type, boolean isRequired )
  {
    setName( name );
    setRequired( isRequired );
    setType( type );
  }

  public String getRelatedTable()
  {
    return new String( relatedTable );
  }

  public void setRelatedTable( String relatedTable )
  {
    this.relatedTable = relatedTable;
  }

  public Object getDefaultValue()
  {
    return defaultValue;
  }

  public void setDefaultValue( Object defaultValue )
  {
    this.defaultValue = defaultValue;
  }
}
