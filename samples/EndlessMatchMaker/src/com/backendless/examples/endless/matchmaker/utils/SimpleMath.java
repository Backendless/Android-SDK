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

package com.backendless.examples.endless.matchmaker.utils;

import java.util.Calendar;
import java.util.Date;

public class SimpleMath
{
  private static final Calendar today = Calendar.getInstance();

  public static int getAgeFromDate( Date birthdate )
  {
    Calendar date = Calendar.getInstance();
    date.setTime( birthdate );

    int age = today.get( Calendar.YEAR ) - date.get( Calendar.YEAR );

    if( today.get( Calendar.MONTH ) < date.get( Calendar.MONTH ) )
      age--;

    else if( today.get( Calendar.MONTH ) == date.get( Calendar.MONTH ) && today.get( Calendar.DAY_OF_MONTH ) < date.get( Calendar.DAY_OF_MONTH ) )
      age--;

    return age;
  }
}
