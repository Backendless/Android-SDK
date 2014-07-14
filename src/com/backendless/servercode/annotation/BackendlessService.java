package com.backendless.servercode.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created with IntelliJ IDEA.
 * User: Eugene Chipachenko
 * Date: 09.07.14
 * Time: 13:31
 */
@Target( ElementType.TYPE )
@Retention( RetentionPolicy.RUNTIME )
public @interface BackendlessService
{
  String value();
}
