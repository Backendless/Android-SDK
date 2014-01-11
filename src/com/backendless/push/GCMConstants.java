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

package com.backendless.push;

public class GCMConstants
{
  public static final String INTENT_TO_GCM_REGISTRATION = "com.google.android.c2dm.intent.REGISTER";
  public static final String INTENT_TO_GCM_UNREGISTRATION = "com.google.android.c2dm.intent.UNREGISTER";
  public static final String INTENT_FROM_GCM_REGISTRATION_CALLBACK = "com.google.android.c2dm.intent.REGISTRATION";
  public static final String INTENT_FROM_GCM_LIBRARY_RETRY = "com.google.android.gcm.intent.RETRY";
  public static final String INTENT_FROM_GCM_MESSAGE = "com.google.android.c2dm.intent.RECEIVE";
  public static final String EXTRA_SENDER = "sender";
  public static final String EXTRA_APPLICATION_PENDING_INTENT = "app";
  public static final String EXTRA_UNREGISTERED = "unregistered";
  public static final String EXTRA_ERROR = "error";
  public static final String EXTRA_IS_INTERNAL = "internal_registration";
  public static final String EXTRA_REGISTRATION_ID = "registration_id";
  public static final String EXTRA_DEVICE_TOKEN = "device_token";
  public static final String EXTRA_SPECIAL_MESSAGE = "message_type";
  public static final String VALUE_DELETED_MESSAGES = "deleted_messages";
  public static final String EXTRA_TOTAL_DELETED = "total_deleted";

  public static final String ERROR_SERVICE_NOT_AVAILABLE = "SERVICE_NOT_AVAILABLE";
  public static final String ERROR_ACCOUNT_MISSING = "ACCOUNT_MISSING";
  public static final String ERROR_AUTHENTICATION_FAILED = "AUTHENTICATION_FAILED";
  public static final String ERROR_INVALID_PARAMETERS = "INVALID_PARAMETERS";
  public static final String ERROR_INVALID_SENDER = "INVALID_SENDER";
  public static final String ERROR_PHONE_REGISTRATION_ERROR = "PHONE_REGISTRATION_ERROR";

  public static final String PERMISSION_ANDROID_INTERNET =  "android.permission.INTERNET";
  public static final String PERMISSION_ANDROID_ACCOUNTS = "android.permission.GET_ACCOUNTS";
  public static final String PERMISSION_GCM_MESSAGE = "com.google.android.c2dm.permission.RECEIVE";
  public static final String PERMISSION_GCM_INTENTS = "com.google.android.c2dm.permission.SEND";

  private GCMConstants()
  {
    throw new UnsupportedOperationException();
  }
}