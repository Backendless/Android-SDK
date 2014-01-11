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

public abstract class AbstractProperty
{
  private String name;
  private boolean required;
  private DateTypeEnum type;

  public String getName()
  {
    return new String( name );
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public boolean isRequired()
  {
    return required;
  }

  public void setRequired( Boolean required )
  {
    this.required = required;
  }

  public DateTypeEnum getType()
  {
    return type;
  }

  public void setType( DateTypeEnum type )
  {
    this.type = type;
  }
}
