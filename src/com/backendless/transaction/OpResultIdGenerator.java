package com.backendless.transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OpResultIdGenerator
{
  private final List<String> opResultIdStrings = new ArrayList<>();
  private final Map<String, Integer> opResultIdMaps = new HashMap<>();

  String generateOpResultId( OperationType operationType, String tableName )
  {
    String opResultIdGenerated;
    final String key = operationType.name().toLowerCase() + tableName;
    if( opResultIdMaps.containsKey( key ) )
    {
      int count = opResultIdMaps.get( key );
      opResultIdMaps.put( key, ++count );
      opResultIdGenerated = key + count;
    }
    else
    {
      opResultIdMaps.put( key, 1 );
      opResultIdGenerated = key + 1;
    }
    opResultIdStrings.add( opResultIdGenerated );
    return opResultIdGenerated;
  }
}
