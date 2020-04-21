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

  public final static String NULL_FACEBOOK_RESPONSE_OBJECT = "Facebook response cannot be null.";
  public static final String NULL_FACEBOOK_USER_ID = "Facebook user id cannot be null";

  public final static String NULL_CONTEXT = "Context cannot be null. Use Backendless.initApp( Context context, String applicationId, String secretKey ) for proper initialization.";

  public final static String NULL_CATEGORY_NAME = "Category name cannot be null or empty.";
  public final static String NULL_GEOPOINT = "Geopoint cannot be null.";
  public final static String DEFAULT_CATEGORY_NAME = "cannot add or delete a default category name.";
  public final static String EMPTY_CATEGORY_NAME = "Geo category name can not be empty.";

  public final static String NULL_BULK = "Object array for bulk operations cannot be null";
  public final static String NULL_ENTITY = "Entity cannot be null.";
  public final static String NULL_ENTITY_NAME = "Entity name cannot be null or empty.";
  public final static String NULL_ID = "Id cannot be null or empty.";

  public final static String NULL_UNIT = "Unit type cannot be null or empty.";

  public final static String NULL_CHANNEL_NAME = "Channel name cannot be null or empty.";
  public final static String NULL_MESSAGE = "Message cannot be null. Use an empty string instead.";
  public final static String NULL_MESSAGE_ID = "Message id cannot be null or empty.";
  public final static String NULL_SUBSCRIPTION_ID = "Subscription id cannot be null or empty.";
  public final static String NULL_EMPTY_TEMPLATE_NAME = "Template name can not be null or empty.";
  public final static String NULL_EMAIL_ENVELOPE = "EmailEnvelope can not be null.";

  public final static String NULL_FILE = "File cannot be null.";
  public final static String NULL_PATH = "File path cannot be null or empty.";
  public final static String NULL_NAME = "File name cannot be null or empty.";
  public final static String NULL_BITMAP = "Bitmap cannot be null";
  public final static String NULL_COMPRESS_FORMAT = "CompressFormat cannot be null";

  public final static String NULL_IDENTITY = "Identity cannot be null";
  public final static String NULL_EMAIL = "Email cannot be null or empty.";

  public final static String NULL_APPLICATION_ID = "Application id cannot be null";
  public final static String NULL_API_KEY = "Secret key cannot be null";
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
  public final static String RECIPIENT_MISSING = "Push notification recipient is missing: use either DeliveryOptions.setPushBroadcast() or DeliveryOptions.setPushSinglecast() to set the recipients";
  public final static String WRONG_POLLING_INTERVAL = "Wrong polling interval";
  public final static String DEVICE_NOT_REGISTERED = "Device is not registered.";

  public final static String NOT_READABLE_FILE = "File is not readable.";
  public final static String FILE_UPLOAD_ERROR = "Could not upload a file";

  public final static String ENTITY_MISSING_DEFAULT_CONSTRUCTOR = "Missing public, default no-argument constructor";
  public final static String ENTITY_WRONG_OBJECT_ID_FIELD_TYPE = "Wrong type of the objectId field";
  public final static String ENTITY_WRONG_CREATED_FIELD_TYPE = "Wrong type of the created field";
  public final static String ENTITY_WRONG_UPDATED_FIELD_TYPE = "Wrong type of the updated field";
  public static final String ENTITY_WRONG_META_FIELD_TYPE = "Wrong type of the meta field";
  public final static String WRONG_ENTITY_TYPE = "Wrong entity type";

  public static final String LOCAL_FILE_EXISTS = "Local file exists";
  public static final String WRONG_REMOTE_PATH = "Remote path cannot be empty";
  public static final String NULL_ROLE_NAME = "Role name cannot be null or empty";
  public static final String FACEBOOK_SESSION_NO_ACCESS = "Could not get access to the Facebook session";
  public static final String WRONG_FACEBOOK_CACHING_STRATEGY = "Facebook session should use NonCachingTokenCachingStrategy";
  public static final String NULL_GRAPH_USER = "Could not get GraphUser from Facebook session";
  public final static String FACEBOOK_LOGINNING_CANCELED = "Facebook login canceled";
  public static final String NOT_ANDROID = "Operation may be performed only from an Android environment";
  public static final String NULL_GEO_QUERY = "Geo query should not be null";
  public static final String INCONSISTENT_GEO_RELATIVE = "Geo query should contain relative metadata and a threshold for a relative search";
  public static final String ASSIGN_ROLE = "Assigning/Unassigning/Creation/Deletion a role can be done only from server code";
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

  public final static String GEOFENCE_ALREADY_MONITORING = "The %s geofence is already being monitored. Monitoring of the geofence must be stopped before you start it again";
  public final static String GEOFENCES_MONITORING = "Cannot start geofence monitoring for all available geofences. There is another monitoring session in progress on the client-side. Make sure to stop all monitoring sessions before starting it for all available geo fences.";
  public final static String NOT_FOUND_GEOFENCE = "Cannot find geofences.";
  public final static String NOT_ADD_SERVICE_TO_MANIFEST = "Android.manifest does not contain %s service.";


  public final static String NOT_FOUND_PROVIDER = "The provider for tracking location cannot be found. Please check if the permission android.permission.ACCESS_FINE_LOCATION has been added to the application manifest.";

  public final static String INVALID_LOG_POLICY = "Either the number of messages or the time frequency must be a positive value";
  public final static String EMPTY_LOG_NAME = "Log name cannot be empty";

  public final static String INCORRECT_MESSAGE_TYPE = "Message object can not be instance of PublishOptions or DeliveryOptions class";
  public final static String INVALID_CLASS = "Cannot use inner or anonymous classes. Make sure the class is public. It cannot be inner or anonymous";

  public static String NULL_FIELD( String field )
  {
    return field + " cannot be null or empty.";
  }

  public final static String NULL_EMPTY_BULK = "Array of objects/maps cannot be null or empty";
  public final static String NULL_MAP = "Entity map cannot be null";
  public final static String NULL_EMPTY_MAP = "Map cannot be null or empty";
  public final static String NULL_OP_RESULT = "OpResult can not be null";
  public final static String NULL_OP_RESULT_VALUE_REFERENCE = "OpResultValueReference can not be null";
  public final static String NULL_OBJECT_ID_IN_OBJECT_MAP = "Object map must contain string objectId and objectId can not be null";
  public final static String NULL_OBJECT_ID_IN_INSTANCE = "Instance must contain objectId and objectId can not be null";
  public final static String NULL_WHERE_CLAUSE = "WhereClause can not be null";
  public final static String NULL_RELATION_COLUMN_NAME = "Relation column name can not be null or empty";
  public final static String NULL_PARENT_TABLE_NAME = "Parent table name can not be null or empty";
  public final static String LIST_NOT_INSTANCES = "Array can be only of instances";
  public final static String LIST_OPERATIONS_NULL_EMPTY = "List of operations in unitOfWork can not be null or empty";
  public final static String REF_TYPE_NOT_SUPPORT = "This operation result not supported in this operation";
  public final static String OP_RESULT_ID_ALREADY_PRESENT = "This opResultId already present. OpResultId must be unique";
  public final static String OP_RESULT_INDEX_YES_PROP_NAME_NOT = "This operation result in this operation must resolved only to resultIndex";
  public final static String OP_RESULT_FROM_THIS_OPERATION_NOT_SUPPORT_IN_THIS_PLACE = "OpResult/OpResultValueReference from this operation in this place not supported";
  public final static String RELATION_USE_LIST_OF_MAPS = "Unable to execute the relation operation. Use the relation method which accepts list of Maps child objects";
  public final static String USE_ANOTHER_METHOD_WITH_TABLE_NAME_AND_LIST_IDS =
          "Unable to execute the bulkDelete operation. Use the bulkDelete method which accepts both table name and a collection of objectId values.";
  public final static String USE_ANOTHER_METHOD_WITH_TABLE_NAME_AND_LIST_OBJECT_MAPS =
          "Unable to execute the bulkDelete operation. Use the bulkDelete method which accepts both table name and a collection of objects.";
}
