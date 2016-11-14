package com.backendless;

public final class Relations
{
  public <T, N> ObjectRelationsStore<T, N> of( Class<T> parent, Class<N> child )
  {
    return new ObjectRelationsStore<>();
  }

  public MapRelationsStore of( String parentTable, String childTable )
  {
    return new MapRelationsStore( parentTable, childTable );
  }
}
