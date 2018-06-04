package com.backendless.rt.rso;

import com.backendless.rt.MethodTypes;
import com.backendless.rt.RTCallback;
import com.backendless.rt.RTMethodRequest;

class SOMethodRequest extends RTMethodRequest
{
  SOMethodRequest( String name, MethodTypes methodType, RTCallback callback )
  {
    super( methodType, callback );
    putOption( "name", name );
  }

  SOMethodRequest putMethodOption( String key, Object value )
  {
    putOption( key, value );
    return this;
  }
}
