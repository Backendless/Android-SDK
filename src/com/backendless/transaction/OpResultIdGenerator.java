package com.backendless.transaction;

import java.util.List;
import java.util.Map;

public class OpResultIdGenerator
{
  private List<String> opResultIdStrings;
  private Map<String, Integer> opResultIdMaps;

  public OpResultIdGenerator( List<String> opResultIdStrings, Map<String, Integer> opResultIdMaps )
  {
    this.opResultIdStrings = opResultIdStrings;
    this.opResultIdMaps = opResultIdMaps;
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
