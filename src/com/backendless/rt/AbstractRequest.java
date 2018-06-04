package com.backendless.rt;

import weborb.v3types.GUID;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractRequest implements RTRequest
{
  private final String id;
  private final RTCallback callback;
  private final Map<String, Object> options = new HashMap<>(  );

  protected AbstractRequest( RTCallback callback )
  {
    this.id = new GUID().toString();
    this.callback = callback;
  }

  @Override
  public RTCallback getCallback()
  {
    return callback;
  }

  public String getId()
  {
    return id;
  }

  @Override
  public Object getOptions()
  {
    return options;
  }

  protected void putOption(String key, Object value)
  {
    options.put( key, value );
  }

  public Object getOption( String key )
  {
    return options.get( key );
  }

  @Override
  public Map<String, Object> toArgs()
  {
    final Map<String, Object> args = new HashMap<>(  );

    args.put( "id", getId() );
    args.put( "name", getName() );
    args.put( "options", getOptions() );

    return args;
  }
}
