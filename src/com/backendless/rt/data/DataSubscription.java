package com.backendless.rt.data;

import com.backendless.async.callback.AsyncCallback;
import com.backendless.rt.RTSubscription;
import com.backendless.rt.SubscriptionNames;
import weborb.types.IAdaptingType;

class DataSubscription extends RTSubscription
{
  DataSubscription( RTDataEvents rtDataEvent, String tableName, AsyncCallback<IAdaptingType> callback )
  {
    super( SubscriptionNames.OBJECTS_CHANGES, callback );
    putOption("event", rtDataEvent );
    putOption( "tableName", tableName );
  }

  DataSubscription withWhere( String where )
  {
    putOption( "whereClause", where );
    return this;
  }
}
