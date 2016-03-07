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

public class BackendlessException extends RuntimeException
{
  private static final long serialVersionUID = -7537447408166433783L;
  private BackendlessFault backendlessFault;
  private short httpErrorCode;
  private String httpErrorMessage;

  public BackendlessException()
  {
  }

  public BackendlessException( String message )
  {
    super( message );
    backendlessFault = new BackendlessFault( message );
  }

  public BackendlessException( String message, Throwable throwable )
  {
    super(message, throwable);
  }

  public BackendlessException( Throwable throwable )
  {
    super( throwable );
    backendlessFault = new BackendlessFault( throwable );
  }

  public BackendlessException( String code, String message )
  {
    super( message );
    backendlessFault = new BackendlessFault( code, message );
  }

  public BackendlessException( BackendlessFault fault )
  {
    super( fault.getMessage() == null ? fault.getDetail() : fault.getMessage() );
    backendlessFault = fault;
  }

  public short getHttpErrorCode()
  {
    return httpErrorCode;
  }

  public void setHttpErrorCode( short httpErrorCode )
  {
    this.httpErrorCode = httpErrorCode;
  }

  public String getHttpErrorMessage()
  {
    return httpErrorMessage;
  }

  public void setHttpErrorMessage( String httpErrorMessage )
  {
    this.httpErrorMessage = httpErrorMessage;
  }

  public String getCode()
  {
    return backendlessFault.getCode();
  }

  @Override
  public String getMessage()
  {
    return backendlessFault.getMessage();
  }

  public String getDetail()
  {
    return backendlessFault.getDetail();
  }

  @Override
  public String toString()
  {
    final StringBuffer sb = new StringBuffer();
    sb.append( getClass().getSimpleName() );
    sb.append( "{ code: '" ).append( getCode() ).append( '\'' );
    sb.append( ", message: '" ).append( getMessage() ).append( '\'' );
    sb.append( " }" );
    return sb.toString();
  }
}
