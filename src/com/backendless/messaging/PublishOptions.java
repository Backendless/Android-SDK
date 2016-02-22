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

package com.backendless.messaging;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * Instance of PublishOptions class may contain publisher ID (an arbitrary, application-specific string value
 * identifying the publisher), subtopic value and/or a collection of headers.
 *
 * Headers are used for sending push notification for various devices, i.e. message to send as push to Android device
 * must include ANDROID_TICKER_TEXT_TAG, ANDROID_CONTENT_TITLE_TAG, ANDROID_CONTENT_TEXT_TAG headers.
 *
 * @see com.backendless.Messaging
 * @see <a href="https://backendless.com/documentation/messaging/android/messaging_publish_push_notifications.htm">Publish Push Notifications</a>
 */
public class PublishOptions
{
  private String publisherId;
  private Map<String, String> headers;
  private String subtopic;

  public final static String MESSAGE_TAG = "message";

  public final static String IOS_ALERT_TAG = "ios-alert";
  public final static String IOS_BADGE_TAG = "ios-badge";
  public final static String IOS_SOUND_TAG = "ios-sound";

  public final static String ANDROID_TICKER_TEXT_TAG = "android-ticker-text";
  public final static String ANDROID_CONTENT_TITLE_TAG = "android-content-title";
  public final static String ANDROID_CONTENT_TEXT_TAG = "android-content-text";
  public final static String ANDROID_ACTION_TAG = "android-action";

  public final static String WP_TYPE_TAG = "wp-type";
  public final static String WP_TITLE_TAG = "wp-title";
  public final static String WP_TOAST_SUBTITLE_TAG = "wp-subtitle";
  public final static String WP_TOAST_PARAMETER_TAG = "wp-parameter";
  public final static String WP_TILE_BACKGROUND_IMAGE = "wp-backgroundImage";
  public final static String WP_TILE_COUNT = "wp-count";
  public final static String WP_TILE_BACK_TITLE = "wp-backTitle";
  public final static String WP_TILE_BACK_BACKGROUND_IMAGE = "wp-backImage";
  public final static String WP_TILE_BACK_CONTENT = "wp-backContent";
  public final static String WP_RAW_DATA = "wp-raw";
  public final static String WP_CONTENT_TAG = "wp-content";
  public final static String WP_BADGE_TAG = "wp-badge";

  public PublishOptions()
  {
  }

  public PublishOptions( String publisherId )
  {
    this.publisherId = publisherId;
  }

  public PublishOptions( String publisherId, String subtopic )
  {
    this.publisherId = publisherId;
    this.subtopic = subtopic;
  }

  public PublishOptions( String publisherId, Hashtable<String, String> headers, String subtopic )
  {
    this.publisherId = publisherId;
    this.headers = new Hashtable<String, String>();
    this.headers.putAll( headers );
    this.subtopic = subtopic;
  }

  public PublishOptions( PublishMessageInfo info )
  {
    this.publisherId = info.getPublisherId();
    this.headers = info.getHeaders();
    this.subtopic = info.getSubtopic();
  }

  public String getPublisherId()
  {
    return publisherId;
  }

  public void setPublisherId( String publisherId )
  {
    this.publisherId = publisherId;
  }

  public Map<String, String> getHeaders()
  {
    if( headers == null )
      return headers = new HashMap<String, String>();

    return new HashMap<String, String>( headers );
  }

  public void setHeaders( Map<String, String> headers )
  {
    this.headers = headers;
  }

  public void putHeader( String headerKey, String headerValue )
  {
    if( headerKey == null || headerKey.equals( "" ) || headerValue == null )
      return;

    if( headers == null )
      headers = new HashMap<String, String>();

    headers.put( headerKey, headerValue );
  }

  public String getSubtopic()
  {
    return subtopic;
  }

  public void setSubtopic( String subtopic )
  {
    this.subtopic = subtopic;
  }
}
