package com.backendless.servercode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target( ElementType.TYPE )
@Retention( RetentionPolicy.RUNTIME )
public @interface BackendlessService
{
  String name() default "";
  String version() default "1.0.0";
  String description() default "";
}
