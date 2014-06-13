package com.backendless.servercode.logging;

import java.nio.CharBuffer;

/**
 * Created with IntelliJ IDEA.
 * User: Eugene Chipachenko
 * Date: 13.06.14
 * Time: 13:36
 */
class LogBuffer
{
  private static CharBuffer buffer = CharBuffer.allocate( 1024 * 512 );

  static void append( CharSequence sequence )
  {
    buffer.append( sequence );
  }

  static void read()
  {
    buffer.flip();
  }
}
