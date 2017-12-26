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

package com.backendless.exceptions;

import weborb.client.Fault;

import java.util.HashMap;
import java.util.Map;

public class BackendlessFault extends Fault
{
  private final Map<String, Object> extendedData;

  public BackendlessFault( Fault fault )
  {
    super( fault.getMessage(), fault.getDetail(), fault.getCode() );
    this.extendedData = new HashMap<>();
  }

  public BackendlessFault( Fault fault, Map<String, Object> extendedData )
  {
    super( fault.getMessage(), fault.getDetail(), fault.getCode() );
    this.extendedData = extendedData;
  }

  public BackendlessFault( BackendlessFault fault )
  {
    super( fault.getMessage(), fault.getDetail(), fault.getCode() );
    this.extendedData = fault.getExtendedData();
  }

  public BackendlessFault( String faultCode, String message )
  {
    super( message, null, faultCode );
    this.extendedData = new HashMap<>();
  }

  public BackendlessFault( String message )
  {
    super( message, null );
    this.extendedData = new HashMap<>();
  }

  public BackendlessFault( BackendlessException e )
  {
    super( e.getMessage(), e.getDetail(), e.getCode() );
    this.extendedData = e.getExtendedData();
  }

  public BackendlessFault( Throwable e )
  {
    this( ExceptionMessage.ILLEGAL_ARGUMENT_EXCEPTION, e.getMessage() );
  }

  public Map<String, Object> getExtendedData()
  {
    return extendedData;
  }

  @Override
  public String toString()
  {
    return getClass().getSimpleName() +
            "{ code: '" + getCode() + '\'' +
            ", message: '" + getMessage() + '\'' +
            ", detail: '" + getDetail() + '\'' +
            ", extendedData: '" + getExtendedData() + '\'' +
            " }";
  }
}
