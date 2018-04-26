package com.backendless.rt.messaging.adapt;

import weborb.exceptions.AdaptingException;
import weborb.types.IAdaptingType;

/**********************************************************************************************************************
 *
 * BACKENDLESS.COM CONFIDENTIAL
 *
 **********************************************************************************************************************
 *
 *  Copyright 2012 BACKENDLESS.COM. All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of Backendless.com and its suppliers,
 * if any.  The intellectual and technical concepts contained herein are proprietary to Backendless.com and its 
 * suppliers and may be covered by U.S. and Foreign Patents, patents in process, and are protected by trade secret
 * or copyright law. Dissemination of this information or reproduction of this material is strictly forbidden 
 * unless prior written permission is obtained from Backendless.com.
 *
 * CREATED ON: 4/20/18 
 *         AT: 4:45 PM
 **********************************************************************************************************************/
public class DefaultAdapter implements IMessagingAdapter
{
  @Override
  public Object adapt( IAdaptingType message, Class clazz ) throws AdaptingException
  {
    return message.adapt( clazz );
  }
}
