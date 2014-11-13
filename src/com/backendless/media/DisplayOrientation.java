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

package com.backendless.media;

public enum DisplayOrientation
{
  //It should be 0, 90, 180, or 270.</p>
  LEFT_LANDSCAPE( 0 ), RIGHT_LANDSCAPE( 180 ), PORTRAIT( 90 ), PORTRAIT_UPSIDE_DOWN(270);

  private int value;

  private DisplayOrientation( int value )
  {
    this.value = value;
  }

  public int getValue()
  {
    return value;
  }


}
