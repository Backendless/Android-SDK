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

package com.backendless.core.responder.policy;

import com.backendless.BackendlessUser;
import weborb.client.IResponder;
import weborb.reader.AnonymousObject;
import weborb.types.IAdaptingType;

public class BackendlessUserAdaptingPolicy extends PoJoAdaptingPolicy<BackendlessUser>
{
  @Override
  public Object adapt( Class<BackendlessUser> clazz, IAdaptingType entity, IResponder nextResponder )
  {
    if(entity instanceof AnonymousObject)
      ((AnonymousObject) entity).setDefaultType( BackendlessUser.class );

    return super.adapt( clazz, entity, nextResponder );
  }
}
