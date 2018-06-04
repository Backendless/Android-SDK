package com.backendless.rt.messaging;

import com.backendless.rt.command.CommandRequest;
import com.backendless.rt.MethodTypes;
import com.backendless.rt.RTCallback;

class MessagingCommandRequest extends CommandRequest
{
  MessagingCommandRequest( String channel, RTCallback callback )
  {
    super( MethodTypes.PUB_SUB_COMMAND, callback );
    putOption( "channel", channel );
  }

  @Override
  protected MessagingCommandRequest setData( Object data )
  {
    super.setData( data );
    return this;
  }

  @Override
  protected MessagingCommandRequest setType( String type )
  {
    super.setType( type );
    return this;
  }
}
