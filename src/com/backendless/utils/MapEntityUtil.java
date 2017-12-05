package com.backendless.utils;

import com.backendless.Persistence;
import com.backendless.commons.persistence.EntityDescription;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * Operations on a data entity represented by {@link java.util.Map}
 * (e.g. a serialized object).
 */
public final class MapEntityUtil
{
  /**
   * Removes relations and {@code null} fields (except system ones) from {@code entityMap}.
   * System fields like "created", "updated", "__meta" etc. are not removed if {@code null}.
   *
   * @param entityMap entity object to clean up
   */
  public static void removeNullsAndRelations( Map<String, Object> entityMap )
  {
    Iterator<Map.Entry<String, Object>> entryIterator = entityMap.entrySet().iterator();
    while( entryIterator.hasNext() )
    {
      Map.Entry<String, Object> entry = entryIterator.next();
      if( !isSystemField( entry ) )
      {
        if( isNullField( entry ) || isRelationField( entry ) )
        {
          entryIterator.remove();
        }
      }
    }
  }

  private static boolean isNullField( Map.Entry<String, Object> entry )
  {
    return entry.getValue() == null;
  }

  private static boolean isRelationField( Map.Entry<String, Object> entry )
  {
    return entry.getValue() instanceof Map
            || entry.getValue() instanceof EntityDescription
            || entry.getValue() instanceof Collection
            || entry.getValue().getClass().isArray();
  }

  private static boolean isSystemField( Map.Entry<String, Object> entry )
  {
    return entry.getKey().equals( Persistence.DEFAULT_OBJECT_ID_FIELD )
            || entry.getKey().equals( Persistence.DEFAULT_CREATED_FIELD )
            || entry.getKey().equals( Persistence.DEFAULT_UPDATED_FIELD )
            || entry.getKey().equals( Persistence.DEFAULT_META_FIELD );
  }
}
