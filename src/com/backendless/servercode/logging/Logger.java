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
    logger.debug( message );
  }

  public void info( String message )
  {
    logger.info( message );
  }

  public void warn( String message )
  {
    logger.warn( message );
  }

  public void warn( String message, Throwable t )
  {
    logger.warn( message, t );
  }

  public void error( String message )
  {
    logger.error( message );
  }

  public void error( String message, Throwable t )
  {
    logger.error( message, t );
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
