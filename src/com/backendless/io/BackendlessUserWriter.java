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

package com.backendless.io;

import com.backendless.BackendlessUser;
import com.backendless.Persistence;
import com.backendless.UserService;
import weborb.writer.IProtocolFormatter;
import weborb.writer.ITypeWriter;
import weborb.writer.MessageWriter;

import java.io.IOException;
import java.util.Map;

public class BackendlessUserWriter implements ITypeWriter
{
  @Override
  public void write( Object o, IProtocolFormatter iProtocolFormatter ) throws IOException
  {
    BackendlessUser user = (BackendlessUser) o;

    Map<String, Object> props = user.getProperties();
    props.put( Persistence.REST_CLASS_FIELD, UserService.USERS_TABLE_NAME );
    props.put( BackendlessUser.ID_KEY, user.getUserId() );
    MessageWriter.writeObject( props, iProtocolFormatter );
  }

  @Override
  public boolean isReferenceableType()
  {
    return false;
  }
}
