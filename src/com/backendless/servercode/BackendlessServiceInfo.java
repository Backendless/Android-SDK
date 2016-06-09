package com.backendless.servercode;

public @interface BackendlessServiceInfo
{
  String name();
  String version();
  String description() default "";
}
