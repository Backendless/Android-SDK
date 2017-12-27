package com.backendless.rt;

import com.backendless.async.callback.AsyncCallback;
import weborb.types.IAdaptingType;

public interface RTCallback<T> extends AsyncCallback<IAdaptingType>
{
  AsyncCallback<T> usersCallback();
}
