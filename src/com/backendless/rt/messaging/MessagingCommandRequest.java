package com.backendless.rt.messaging;

import com.backendless.rt.MethodTypes;
import com.backendless.rt.RTCallback;
import com.backendless.rt.RTMethodRequest;

class MessagingCommandRequest extends RTMethodRequest
{
  MessagingCommandRequest( String channel, RTCallback callback )
  {
    super( MethodTypes.PUB_SUB_COMMAND, callback );
    putOption( "channel", channel );
  }

  MessagingCommandRequest setData( Object data )
  {
    putOption( "data", data );
    return this;
  }

  MessagingCommandRequest setType( String type )
  {
    putOption( "type", type );
    return this;
  }
}
