package com.backendless.rt;


public class CommandRequest extends RTMethodRequest
{

  public CommandRequest( MethodTypes methodType, RTCallback callback )
  {
    super( methodType, callback );
  }

  protected CommandRequest setData( Object data )
  {
    putOption( "data", data );
    return this;
  }

  protected CommandRequest setType( String type )
  {
    putOption( "type", type );
    return this;
  }
}
