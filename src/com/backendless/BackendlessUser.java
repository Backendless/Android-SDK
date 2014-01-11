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

package com.backendless;

import java.util.HashMap;
import java.util.Map;

public class BackendlessUser
{
  private final Map<String, Object> properties = new HashMap<String, Object>();

  public static final String PASSWORD_KEY = "password";
  public static final String EMAIL_KEY = "email";
  private static final String ID_KEY = "objectId";

  public BackendlessUser()
  {
  }

  public Map<String, Object> getProperties()
  {
    return new HashMap<String, Object>( properties );
  }

  public void setProperties( Map<String, Object> properties )
  {
    synchronized( this )
    {
      this.properties.putAll( properties );
    }
  }

  public void putProperties( Map<String, Object> properties )
  {
    synchronized( this )
    {
      this.properties.putAll( properties );
    }
  }

  public Object getProperty( String key )
  {
    synchronized( this )
    {
      if( properties == null )
        return null;

      return properties.get( key );
    }
  }

  public void setProperty( String key, Object value )
  {
    synchronized( this )
    {
      properties.put( key, value );
    }
  }

  public String getUserId()
  {
    Object result = getProperty( ID_KEY );

    return result == null ? null : String.valueOf( result );
  }

  public void setPassword( String password )
  {
    setProperty( PASSWORD_KEY, password );
  }

  public String getPassword()
  {
    Object result = getProperty( PASSWORD_KEY );

    return result == null ? null : String.valueOf( result );
  }

  public void setEmail( String email )
  {
    setProperty( EMAIL_KEY, email );
  }

  public String getEmail()
  {
    Object result = getProperty( EMAIL_KEY );

    return result == null ? null : String.valueOf( result );
  }

  public void clearProperties()
  {
    synchronized( this )
    {
      properties.clear();
    }
  }

  @Override
  public boolean equals( Object o )
  {
    if( this == o )
      return true;

    if( o == null || getClass() != o.getClass() )
      return false;

    BackendlessUser that = (BackendlessUser) o;

    if( !properties.equals( that.properties ) )
      return false;

    return true;
  }

  @Override
  public int hashCode()
  {
    return properties.hashCode();
  }
}