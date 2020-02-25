package com.backendless.transaction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OpResultIdGenerator
{
  private List<String> opResultIdStrings;
  private final Map<String, Integer> opResultIdMaps = new HashMap<>();

  public OpResultIdGenerator( List<String> opResultIdStrings )
  {
    this.opResultIdStrings = opResultIdStrings;
  }

  String generateOpResultId( OperationType operationType, String tableName )
  {
    String opResultIdGenerated;
    final String key = operationType.getOperationName() + tableName;
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
