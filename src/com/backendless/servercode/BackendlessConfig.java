package com.backendless.servercode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created with IntelliJ IDEA.
 * User: Eugene Chipachenko
 * Date: 11.08.2015
 * Time: 10:26
 */
@Target( value = ElementType.FIELD )
@Retention( value = RetentionPolicy.RUNTIME )
public @interface BackendlessConfig
{
  String value();

  boolean required() default false;

  String[] options() default {};
}
