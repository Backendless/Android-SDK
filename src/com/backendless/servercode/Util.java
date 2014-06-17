package com.backendless.servercode;

/**
 * Created with IntelliJ IDEA.
 * User: Eugene Chipachenko
 * Date: 11.06.14
 * Time: 17:13
 */
public class Util
{
  public static boolean isCodeRunner()
  {
    return Thread.currentThread().getThreadGroup().getName().equals( "CodeRunner secure group" );
  }
}
