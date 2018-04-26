package com.backendless.rt.messaging.adapt;

import weborb.exceptions.AdaptingException;
import weborb.reader.AnonymousObject;
import weborb.reader.CacheableAdaptingTypeWrapper;
import weborb.types.IAdaptingType;

import java.util.HashMap;

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
 *         AT: 4:54 PM
 **********************************************************************************************************************/
public class HashMapAdapter implements IMessagingAdapter
{
  @Override
  public Object adapt( IAdaptingType message, Class clazz ) throws AdaptingException
  {
    if( message instanceof CacheableAdaptingTypeWrapper )
    {
      IAdaptingType internalType = ((CacheableAdaptingTypeWrapper) message).getType();
      return adapt( internalType, clazz );
    }


    // TODO: finish up when BKNDLSS-16912 and BKNDLSS-16911 are fixed
    /*if( message instanceof AnonymousObject )
    {
      HashMap props = ((AnonymousObject) message).getProperties();
    }
    */
    return null;
  }
}
