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

import java.text.SimpleDateFormat;

public class Defaults
{
  public static final String APPLICATION_ID = "C662E197-D7A9-532E-FFD4-612EC407CC00";
  public static final String APPLICATION_SECRET_KEY = "C1568C73-6B6B-5C5E-FFDA-FE4F0A675200";
  public static final String APPLICATION_VERSION = "v1";

  public static final String NAME_PROPERTY = "name";
  public static final String EMAIL_PROPERTY = "email";
  public static final String BIRTH_DATE_PROPERTY = "birthdate";
  public static final String GENDER_PROPERTY = "gender";
  public static final String PASSWORD_PROPERTY = "password";
  public static final String DEVICE_REGISTRATION_ID_PROPERTY = "deviceRegistrationId";

  public static final String CURRENT_USER_GEO_POINT_BUNDLE_TAG = "currentGeoPointBundle";
  public static final String TARGET_USER_GEO_POINT_BUNDLE_TAG = "targetGeoPointBundle";
  public static final String CURRENT_USER_NAME = "currentUserName";
  public static final String TARGET_USER_NAME = "targetUserName";
  public static final String PING_TAG = "ping";
  public static final String TRIGER = "triger";

  public static final String MESSAGING_CHANNEL = "default";
  public static final String GCM_SENDER_ID = "149503573442";
  public static final String PUBLISHER_HEADER = "publisher";
  public static final String PUBLISHER_NAME_HEADER = "publisher_name";
  public static final String SUBSCRIBER_HEADER = "subscriber";

  public static final SimpleDateFormat DEFAULT_DATE_FORMATTER = new SimpleDateFormat( "yyyy-MM-dd" );
}
