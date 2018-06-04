package com.backendless.rt;

import java.util.Map;

public interface RTRequest
{
  String getId();

  String getName();

  Object getOptions();

  RTCallback getCallback();

  Map<String, Object> toArgs();
}
