package com.backendless.rt.data;

import com.backendless.rt.RTCallback;
import com.backendless.rt.RTSubscription;
import com.backendless.rt.SubscriptionNames;

class DataSubscription extends RTSubscription
{
  DataSubscription( RTDataEvents rtDataEvent, String tableName, RTCallback callback )
  {
    super( SubscriptionNames.OBJECTS_CHANGES, callback );
    putOption("event", rtDataEvent.eventName() );
    putOption( "tableName", tableName );
  }

  DataSubscription withWhere( String where )
  {
    putOption( "whereClause", where );
    return this;
  }

  RTDataEvents getEvent()
  {
    final String eventStr = (String) getOption( "event" );
    return eventStr == null ? null : RTDataEvents.forName( eventStr );
  }

  String getTableName()
  {
    return (String) getOption( "tableName" );
  }

  String getWhereClause()
  {
    return (String) getOption( "whereClause" );
  }

}
