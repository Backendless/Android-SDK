package com.backendless.servercode.logging;

import com.backendless.Backendless;

/**
 * Created with IntelliJ IDEA.
 * User: Eugene Chipachenko
 * Date: 13.06.14
 * Time: 13:15
 */
public class Logger
{
  private boolean async;
  private Class clazz;

  private com.backendless.logging.Logger logger;

  public static Logger getLogger( Class clazz )
  {
    return new Logger( clazz, false );
  }

  /**
   * @deprecated async argument is ignored, use {@link #getLogger(Class)} instead
   */
  @Deprecated
  public static Logger getLogger( Class clazz, boolean async )
  {
    return new Logger( clazz, async );
  }

  Logger( Class clazz, boolean async )
  {
    this.clazz = clazz;
    this.async = async;
    this.logger = Backendless.Logging.getLogger( clazz );
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

  private void log( final Level level, final String message, final Throwable t )
  {
    switch( level )
    {
      case DEBUG:
        logger.debug( message );
        break;
      case INFO:
        logger.info( message );
        break;
      case WARNING:
        logger.warn( message, t );
        break;
      case ERROR:
        logger.error( message, t );
        break;
    }
  }

  @Override
  public String toString()
  {
    return "Logger{" +
            "async=" + async +
            ", clazz=" + clazz +
            '}';
  }
}
