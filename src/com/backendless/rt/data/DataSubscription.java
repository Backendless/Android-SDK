package com.backendless.rt.data;

import com.backendless.rt.RTCallback;
import com.backendless.rt.RTEvent;
import com.backendless.rt.RTSubscription;
import com.backendless.rt.SubscriptionNames;

import java.util.List;

class DataSubscription extends RTSubscription
{
  DataSubscription( ObjectEvents objectEvent, String tableName, RTCallback callback )
  {
    super( SubscriptionNames.OBJECTS_CHANGES, callback );
    putOption( "event", objectEvent.eventName() );
    putOption( "tableName", tableName );
  }

  DataSubscription( RelationEvents relationEvent, String tableName, String relationColumnName,
                    RTCallback callback )
  {
    super( SubscriptionNames.RELATIONS_CHANGES, callback );
    putOption( "event", relationEvent.eventName() );
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

  ObjectEvents getObjectEvent()
  {
    final String eventStr = (String) getOption( "event" );
    return eventStr == null ? null : ObjectEvents.forName( eventStr );
  }

  RelationEvents getRelationEvent()
  {
    final String eventStr = (String) getOption( "event" );
    return eventStr == null ? null : RelationEvents.forName( eventStr );
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
