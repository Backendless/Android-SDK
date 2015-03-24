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

public class ExceptionMessage
{
  public final static String ILLEGAL_ARGUMENT_EXCEPTION = "IllegalArgumentException";
  public final static String SERVER_ERROR = "Server returned an error.";
  public final static String CLIENT_ERROR = "Internal client exception.";

  public final static String WRONG_MANIFEST = "Wrong dependencies at the manifest";
  public final static String NOT_INITIALIZED = "Backendless application was not initialized";

  public final static String NULL_USER = "User cannot be null or empty.";
  public final static String NULL_PASSWORD = "User password cannot be null or empty.";
  public final static String NULL_LOGIN = "User login cannot be null or empty.";

  public final static String NULL_FACEBOOK_SESSION = "Facebook session is null or incomplete.";
  public static final String NULL_FACEBOOK_GRAPH_USER = "Facebook graph user cannot be null";

  public final static String NULL_CONTEXT = "Context cannot be null. Use Backendless.initApp( Context context, String applicationId, String secretKey, String version ) for proper initialization.";

  public final static String NULL_CATEGORY_NAME = "Category name cannot be null or empty.";
  public final static String NULL_GEOPOINT = "Geopoint cannot be null.";
  public final static String DEFAULT_CATEGORY_NAME = "cannot add or delete a default category name.";
  public final static String EMPTY_CATEGORY_NAME = "Geo category name can not be empty.";


  public final static String NULL_ENTITY = "Entity cannot be null.";
  public final static String NULL_ENTITY_NAME = "Entity name cannot be null or empty.";
  public final static String NULL_ID = "Id cannot be null or empty.";

  public final static String NULL_UNIT = "Unit type cannot be null or empty.";

  public final static String NULL_CHANNEL_NAME = "Channel name cannot be null or empty.";
  public final static String NULL_MESSAGE = "Message cannot be null. Use an empty string instead.";
  public final static String NULL_MESSAGE_ID = "Message id cannot be null or empty.";
  public final static String NULL_SUBSCRIPTION_ID = "Subscription id cannot be null or empty.";

  public final static String NULL_FILE = "File cannot be null.";
  public final static String NULL_PATH = "File path cannot be null or empty.";
  public final static String NULL_BITMAP = "Bitmap cannot be null";
  public final static String NULL_COMPRESS_FORMAT = "CompressFormat cannot be null";

  public final static String NULL_IDENTITY = "Identity cannot be null";

  public final static String NULL_APPLICATION_ID = "Application id cannot be null";
  public final static String NULL_SECRET_KEY = "Secret key cannot be null";
  public final static String NULL_VERSION = "Version cannot be null";
  public final static String NULL_DEVICE_TOKEN = "Null device token received";

  public final static String NULL_CATHEGORY = "Cannot add category. Category name is NULL.";

  public final static String WRONG_RADIUS = "Wrong radius value.";
  public final static String WRONG_SEARCH_RECTANGLE_QUERY = "Wrong rectangle search query. It should contain four points.";

  public final static String WRONG_FILE = "cannot read the file.";

  public final static String NULL_COORDINATES = "Coordinates cannot be null";
  public final static String NULL_LATITUDE = "Latitude cannot be null";
  public final static String NULL_LONGITUDE = "Longitude cannot be null";
  public final static String WRONG_LATITUDE_VALUE = "Latitude value should be between -90 and 90.";
  public final static String WRONG_LONGITUDE_VALUE = "Longitude value should be between -180 and 180.";

  public final static String WRONG_USER_ID = "User not logged in or wrong user id.";

  public final static String WRONG_GEO_QUERY = "Could not understand Geo query options. Specify any.";
  public final static String INCONSISTENT_GEO_QUERY = "Inconsistent geo query. Query should not contain both rectangle and radius search parameters.";

  public final static String WRONG_OFFSET = "Offset cannot have a negative value.";
  public final static String WRONG_PAGE_SIZE = "Pagesize cannot have a negative value.";

  public final static String WRONG_SUBSCRIPTION_STATE = "cannot resume a subscription, which is not paused.";
  public final static String WRONG_EXPIRATION_DATE = "Wrong expiration date";
  public final static String RECIPIENT_MISSING = "Push notification recipient is missing: call DeliveryOptions.setPushBroadcast() or DeliveryOptions.setPushSinglecast()";
  public final static String WRONG_POLLING_INTERVAL = "Wrong polling interval";
  public final static String DEVICE_NOT_REGISTERED = "Device is not registered.";

  public final static String NOT_READABLE_FILE = "File is not readable.";
  public final static String FILE_UPLOAD_ERROR = "Could not upload a file";

  public final static String ENTITY_MISSING_DEFAULT_CONSTRUCTOR = "No default noargument constructor";
  public final static String ENTITY_WRONG_OBJECT_ID_FIELD_TYPE = "Wrong type of the objectId field";
  public final static String ENTITY_WRONG_CREATED_FIELD_TYPE = "Wrong type of the created field";
  public final static String ENTITY_WRONG_UPDATED_FIELD_TYPE = "Wrong type of the updated field";
  public static final String ENTITY_WRONG_META_FIELD_TYPE = "Wrong type of the meta field";
  public final static String WRONG_ENTITY_TYPE = "Wrong entity type";

  public static final String SERVICE_NOT_DECLARED = "com.backendless.AndroidService is not declared at the application manifest";
  public static final String LOCAL_FILE_EXISTS = "Local file exists";
  public static final String WRONG_REMOTE_PATH = "Remote path cannot be empty";
  public static final String NULL_ROLE_NAME = "Role name cannot be null or empty";
  public static final String FACEBOOK_SESSION_NO_ACCESS = "Could not get access to the Facebook session";
  public static final String WRONG_FACEBOOK_CACHING_STRATEGY = "Facebook session should use NonCachingTokenCachingStrategy";
  public static final String NULL_GRAPH_USER = "Could not get GraphUser from Facebook session";
  public static final String NOT_ANDROID = "Operation may be performed only from an Android environment";
  public static final String NULL_GEO_QUERY = "Geo query should not be null";
  public static final String INCONSISTENT_GEO_RELATIVE = "Geo query should contain relative metadata and a threshold for a relative search";
  public static final String ASSIGN_ROLE = "Assign/unassign role is available only from server code";
  public static final String NULL_BODYPARTS = "BodyParts cannot be null";
  public static final String NULL_SUBJECT = "Subject cannot be null";
  public static final String NULL_RECIPIENTS = "Recipients cannot be empty";
  public static final String NULL_ATTACHMENTS = "Attachments cannot be null";

  public static final String INIT_BEFORE_USE = "Init storage before use";

  public static final String CAN_NOT_SAVE_LOG = "Can not save log";

  public final static String WRONG_CLUSTERISATION_QUERY = "Wrong clusterization parameter's values. All values should be positive number";

  public final static String GEO_QUERY_METHOD_PERMISSION = "Changing the %s property may result in invalid cluster formation. As a result the property is immutable and cannot be changed";
  public final static String GEO_QUERY_SET_PERMISSION = "A geo query cannot be assigned to a cluster";

  public final static String FIELD_NOT_ACCESSIBLE = "Field %s is not accessible";

  public final static String ANONYMOUS_CLASSES_PROHIBITED = "Anonymous class properties are prohibited: %s";
}
