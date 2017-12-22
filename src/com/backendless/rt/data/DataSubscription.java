package com.backendless.rt.data;

import com.backendless.rt.RTSubscription;
import com.backendless.rt.SubscriptionNames;

class DataSubscription extends RTSubscription
{
  DataSubscription( RTDataEvents rtDataEvent, String tableName )
  {
    super( SubscriptionNames.OBJECTS_CHANGES );
    putOption("event", rtDataEvent );
    putOption( "tableName", tableName );
  }

  DataSubscription withWhere( String where )
  {
    putOption( "whereClause", where );
    return this;
  }
}
