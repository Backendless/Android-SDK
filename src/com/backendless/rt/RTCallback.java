package com.backendless.rt;

import com.backendless.async.callback.AsyncCallback;
import weborb.types.IAdaptingType;

public interface RTCallback extends AsyncCallback<IAdaptingType>
{
  AsyncCallback usersCallback();
}
