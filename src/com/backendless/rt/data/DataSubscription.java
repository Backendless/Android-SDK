package com.backendless.rt.data;

import com.backendless.rt.RTCallback;
import com.backendless.rt.RTSubscription;
import com.backendless.rt.SubscriptionNames;

import java.util.List;

class DataSubscription extends RTSubscription
{
  DataSubscription( DataEvents rtDataEvent, String tableName, RTCallback callback )
  {
    super( SubscriptionNames.OBJECTS_CHANGES, callback );
    putOption( "event", rtDataEvent.eventName() );
    putOption( "tableName", tableName );
  }

  DataSubscription( DataEvents rtDataEvent, String tableName, String relationColumnName,
                    RTCallback callback )
  {
    super( SubscriptionNames.RELATIONS_CHANGES, callback );
    putOption( "event", rtDataEvent.eventName() );
    putOption( "tableName", tableName );
    putOption( "relationColumnName", relationColumnName );
  }

  DataSubscription withWhere( String where )
  {
    putOption( "whereClause", where );
    return this;
  }

  DataSubscription withParentObjects( List<String> parentObjects )
  {
    putOption( "parentObjects", parentObjects );
    return this;
  }

  DataEvents getEvent()
  {
    final String eventStr = (String) getOption( "event" );
    return eventStr == null ? null : DataEvents.forName( eventStr );
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
