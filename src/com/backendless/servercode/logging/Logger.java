package com.backendless.servercode.logging;

import com.backendless.Backendless;
import com.backendless.HeadersManager;
import com.backendless.exceptions.BackendlessException;
import com.backendless.exceptions.ExceptionMessage;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;

/**
 * Created with IntelliJ IDEA.
 * User: Eugene Chipachenko
 * Date: 13.06.14
 * Time: 13:15
 */
public class Logger
{
  private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat();
  private boolean async = true;
  private Class clazz;

  public static Logger getLogger( Class clazz )
  {
    return new Logger( clazz, true );
  }

  public static Logger getLogger( Class clazz, boolean async )
  {
    return new Logger( clazz, async );
  }

  Logger( Class clazz, boolean async )
  {
    this.clazz = clazz;
    this.async = async;
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
    final StringBuilder builder = new StringBuilder();
    builder.append( String.format( "%1s %2s %3s %4s%n", DATE_FORMAT.format( System.currentTimeMillis() ), level.name(), clazz.getName(), message ) );

    if( t != null )
    {
      builder.append( t.getMessage() );
      builder.append( Logger.dumpStack( t ) );
    }

    Thread thread = new Thread()
    {
      @Override
      public void run()
      {
        HttpURLConnection connection = null;

        try
        {
          URL url = new URL( Backendless.getUrl() + "/" + Backendless.getVersion() + "/servercode/log" );
          connection = (HttpURLConnection) url.openConnection();
          connection.setDoOutput( true );

          for( String key : HeadersManager.getInstance().getHeaders().keySet() )
            connection.addRequestProperty( key, HeadersManager.getInstance().getHeaders().get( key ) );

          OutputStreamWriter out = new OutputStreamWriter( connection.getOutputStream() );
          out.write( "log=" + builder.toString() );
          out.close();

          connection.getResponseCode();
        }
        catch( Exception e )
        {
          throw new BackendlessException( ExceptionMessage.CAN_NOT_SAVE_LOG, e.getMessage() );
        }
        finally
        {
          if( connection != null )
            connection.disconnect();
        }
      }
    };

    if( async )
      thread.start();
    else
      thread.run();
  }

  static String dumpStack( Throwable t )
  {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter( sw );
    t.printStackTrace( pw );
    pw.flush();
    pw.close();
    return sw.toString();
  }
}
