package com.backendless.tests.junit.unitTests.fileService.syncTests;

import com.backendless.Backendless;
import com.backendless.tests.junit.Defaults;
import com.backendless.tests.junit.ITest;
import org.junit.BeforeClass;

import java.io.*;
import java.util.Calendar;

public class TestsFrame extends ITest
{
  public File createRandomFile() throws IOException
  {
    File file = new File( new java.sql.Timestamp( Calendar.getInstance().getTime().getTime() ).toString().replaceAll( "\\D*", "" ) );
    file.createNewFile();

    PrintWriter printWriter = new PrintWriter( file );

    for( int i = 0; i < 1000; i++ )
      printWriter.append( new java.sql.Timestamp( Calendar.getInstance().getTime().getTime() ).toString() );

    printWriter.flush();
    printWriter.close();

    return file;
  }

  public void deleteFile( File file )
  {
    if( file == null )
      return;

    if( file.exists() )
      file.delete();
  }

  public String getRandomPath()
  {
    return "path_" + new java.sql.Timestamp( Calendar.getInstance().getTime().getTime() ).toString().replaceAll( "\\D*", "" ) + "_n";
  }

  public boolean compareFiles( File file1, File file2 ) throws IOException
  {
    return getFileContent( file1 ).equals( getFileContent( file2 ) );
  }

  private String getFileContent( File file ) throws IOException
  {
    StringBuilder stringBuilder = new StringBuilder();
    FileInputStream fileInputStream = new FileInputStream( file );
    BufferedReader bufferedReader = new BufferedReader( new InputStreamReader( fileInputStream ) );

    String currentLine = "";

    while( (currentLine = bufferedReader.readLine()) != null )
      stringBuilder.append( currentLine );

    fileInputStream.close();

    return stringBuilder.toString();
  }

  @BeforeClass
  public static void setUp() throws Throwable
  {
    Backendless.initApp( Defaults.TEST_APP_ID, Defaults.TEST_SECRET_KEY, Defaults.TEST_VERSION );
  }
}
