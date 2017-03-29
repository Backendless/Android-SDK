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

public class BackendlessFault extends Fault
{
  public BackendlessFault( Fault fault )
  {
    super( fault.getMessage(), fault.getDetail(), fault.getCode() );
  }

  public BackendlessFault( String faultCode, String message )
  {
    super( message, null, faultCode );
  }

  public BackendlessFault( String message )
  {
    super( message, null );
  }

  public BackendlessFault( BackendlessException e )
  {
    super( e.getMessage(), e.getDetail(), e.getCode() );
  }

  public BackendlessFault( Throwable e )
  {
    this( ExceptionMessage.ILLEGAL_ARGUMENT_EXCEPTION, e.getMessage() );
  }

  @Override
  public String toString()
  {
    return getClass().getSimpleName() +
            "{ code: '" + getCode() + '\'' +
            ", message: '" + getMessage() + '\'' +
            ", detail: '" + getDetail() + '\'' +
            " }";
  }
}
