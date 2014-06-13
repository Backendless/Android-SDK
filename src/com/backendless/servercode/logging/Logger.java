package com.backendless.servercode.logging;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created with IntelliJ IDEA.
 * User: Eugene Chipachenko
 * Date: 13.06.14
 * Time: 13:15
 */
public class Logger
{
  private static final String FORMAT = "%1$tY.%1$tm.d %1$tH:%1$tM%1$tS %2$s %3$s %4$s\n";
  private Class clazz;

  public Logger( Class clazz )
  {
    this.clazz = clazz;
  }

  public void debug( String message )
  {
    log( Level.DEBUG, message );
  }

  public void info( String message )
  {
    log( Level.INFO, message );
  }

  public void warn( String message )
  {
    log( Level.WARNING, message );
  }

  public void warn( String message, Throwable t )
  {
    log( Level.WARNING, message, t );
  }

  public void error( String message )
  {
    log( Level.ERROR, message );
  }

  public void error( String message, Throwable t )
  {
    log( Level.ERROR, message, t );
  }

  private void log( Level level, String message )
  {
    log( level, message, null );
  }

  private void log( Level level, String message, Throwable t )
  {
    LogBuffer.append( String.format( FORMAT, level, clazz.getName(), message, t ) );
    if( t != null )
    {
      LogBuffer.append( t.getMessage() );
      LogBuffer.append( dumpStack( t ) );
    }
  }

  private String dumpStack( Throwable t )
  {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter( sw );
    t.printStackTrace( pw );
    pw.flush();
    pw.close();
    return sw.toString();
  }
}
