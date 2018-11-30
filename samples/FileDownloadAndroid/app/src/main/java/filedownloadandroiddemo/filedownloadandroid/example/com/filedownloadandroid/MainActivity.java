package filedownloadandroiddemo.filedownloadandroid.example.com.filedownloadandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.ThreadPoolService;
import com.backendless.files.BackendlessFileAndroid;

import java.io.*;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class MainActivity extends AppCompatActivity
{

  TextView out;
  Button Download1;
  Button Download2;
  Button Download3;
  Button Download4;
  Button Download5;
  Button Download6;
  Button Cancel1;
  Button Cancel2;
  Button Cancel3;
  Button Cancel4;
  Button Cancel5;
  Button Cancel6;

  @Override
  protected void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView( R.layout.activity_main );

    Backendless.setUrl( Defaults.SERVER_URL );
    Backendless.initApp( getApplicationContext(), Defaults.APPLICATION_ID, Defaults.API_KEY );

    out = findViewById( R.id.out );
    Download1 = findViewById( R.id.Download1 );
    Download2 = findViewById( R.id.Download2 );
    Download3 = findViewById( R.id.Download3 );
    Download4 = findViewById( R.id.Download4 );
    Download5 = findViewById( R.id.Download5 );
    Download6 = findViewById( R.id.Download6 );
    Cancel1 = findViewById( R.id.Cancel1 );
    Cancel2 = findViewById( R.id.Cancel2 );
    Cancel3 = findViewById( R.id.Cancel3 );
    Cancel4 = findViewById( R.id.Cancel4 );
    Cancel5 = findViewById( R.id.Cancel5 );
    Cancel6 = findViewById( R.id.Cancel6 );

    final Future<File>[] downloadTask1 = new Future[]{ null };
    View.OnClickListener ButtonDownload1 = new View.OnClickListener()
    {
      @Override
      public void onClick( View v )
      {
        ThreadPoolService.getPoolExecutor().execute( new Runnable()
        {
          @Override
          public void run()
          {
            downloadTask1[ 0 ] = downloadWithLocalPath();
            File fileWithLocalPath = null;
            try
            {
              fileWithLocalPath = downloadTask1[ 0 ].get();
              System.out.println( fileWithLocalPath.toString() );
            }
            catch( CancellationException | ExecutionException | InterruptedException e )
            {
              e.printStackTrace();
            }
          }
        } );
        out.setText( "File downloading with local path" );
      }
    };
    Download1.setOnClickListener( ButtonDownload1 );
    View.OnClickListener ButtonCancel1 = new View.OnClickListener()
    {
      @Override
      public void onClick( View v )
      {
        cancelDownloadingFile( downloadTask1[ 0 ] );
      }
    };
    Cancel1.setOnClickListener( ButtonCancel1 );

    final Future<Void>[] downloadTask2 = new Future[]{ null };
    View.OnClickListener ButtonDownload2 = new View.OnClickListener()
    {
      @Override
      public void onClick( View v )
      {
        ThreadPoolService.getPoolExecutor().execute( new Runnable()
        {
          @Override
          public void run()
          {
            downloadTask2[ 0 ] = downloadWithOutputStream();
            try
            {
              downloadTask2[ 0 ].get();
              System.out.println( "file downloaded" );
            }
            catch( CancellationException | ExecutionException | InterruptedException e )
            {
              e.printStackTrace();
            }
          }
        } );
        out.setText( "File downloading with OutputStream" );
      }
    };
    Download2.setOnClickListener( ButtonDownload2 );
    View.OnClickListener ButtonCancel2 = new View.OnClickListener()
    {
      @Override
      public void onClick( View v )
      {
        cancelDownloadingVoid( downloadTask2[ 0 ] );
      }
    };
    Cancel2.setOnClickListener( ButtonCancel2 );

    final Future<byte[]>[] downloadTask3 = new Future[]{ null };
    View.OnClickListener ButtonDownload3 = new View.OnClickListener()
    {
      @Override
      public void onClick( View v )
      {
        ThreadPoolService.getPoolExecutor().execute( new Runnable()
        {
          @Override
          public void run()
          {
            downloadTask3[ 0 ] = downloadFileInArray();
            File fileFromByteArray = null;
            try
            {
              fileFromByteArray = writeByteArray( downloadTask3[ 0 ].get() );
              System.out.println( fileFromByteArray.toString() );
            }
            catch( CancellationException | ExecutionException | InterruptedException e )
            {
              e.printStackTrace();
            }
          }
        } );
        out.setText( "File downloading file in ByteArray" );
      }
    };
    Download3.setOnClickListener( ButtonDownload3 );
    View.OnClickListener ButtonCancel3 = new View.OnClickListener()
    {
      @Override
      public void onClick( View v )
      {
        cancelDownloadingByteArray( downloadTask3[ 0 ] );
      }
    };
    Cancel3.setOnClickListener( ButtonCancel3 );

    final Future<File>[] downloadTask4 = new Future[]{ null };
    View.OnClickListener ButtonDownload4 = new View.OnClickListener()
    {
      @Override
      public void onClick( View v )
      {
        ThreadPoolService.getPoolExecutor().execute( new Runnable()
        {
          @Override
          public void run()
          {
            downloadTask4[ 0 ] = downloadWithLocalPathAndProgressBar();
            File fileWithLocalPath = null;
            try
            {
              fileWithLocalPath = downloadTask4[ 0 ].get();
              System.out.println( fileWithLocalPath.toString() );
            }
            catch( CancellationException | InterruptedException | ExecutionException e )
            {
              e.printStackTrace();
            }
          }
        } );
        out.setText( "File downloading file downloading with local path and ProgressBar" );
      }
    };
    Download4.setOnClickListener( ButtonDownload4 );
    View.OnClickListener ButtonCancel4 = new View.OnClickListener()
    {
      @Override
      public void onClick( View v )
      {
        cancelDownloadingFile( downloadTask4[ 0 ] );
      }
    };
    Cancel4.setOnClickListener( ButtonCancel4 );

    final Future<Void>[] downloadTask5 = new Future[]{ null };
    View.OnClickListener ButtonDownload5 = new View.OnClickListener()
    {
      @Override
      public void onClick( View v )
      {
        ThreadPoolService.getPoolExecutor().execute( new Runnable()
        {
          @Override
          public void run()
          {
            downloadTask5[ 0 ] = downloadWithOutputStreamAndProgressBar();
            try
            {
              downloadTask5[ 0 ].get();
              System.out.println( "file downloaded" );
            }
            catch( CancellationException | ExecutionException | InterruptedException e )
            {
              e.printStackTrace();
            }
          }
        } );
        out.setText( "File downloading with OutputStream and ProgressBar" );
      }
    };
    Download5.setOnClickListener( ButtonDownload5 );
    View.OnClickListener ButtonCancel5 = new View.OnClickListener()
    {
      @Override
      public void onClick( View v )
      {
        cancelDownloadingVoid( downloadTask5[ 0 ] );
      }
    };
    Cancel5.setOnClickListener( ButtonCancel5 );

    final Future<byte[]>[] downloadTask6 = new Future[]{ null };
    View.OnClickListener ButtonDownload6 = new View.OnClickListener()
    {
      @Override
      public void onClick( View v )
      {
        ThreadPoolService.getPoolExecutor().execute( new Runnable()
        {
          @Override
          public void run()
          {
            downloadTask6[ 0 ] = downloadFileInByteArrayWithProgressBar();
            File fileFromByteArray = null;
            try
            {
              fileFromByteArray = writeByteArray( downloadTask6[ 0 ].get() );
              System.out.println( fileFromByteArray.toString() );
            }
            catch( CancellationException | ExecutionException | InterruptedException e )
            {
              e.printStackTrace();
            }
          }
        } );
        out.setText( "File downloading file in ByteArray with ProgressBar" );
      }
    };
    Download6.setOnClickListener( ButtonDownload6 );
    View.OnClickListener ButtonCancel6 = new View.OnClickListener()
    {
      @Override
      public void onClick( View v )
      {
        cancelDownloadingByteArray( downloadTask6[ 0 ] );
      }
    };
    Cancel6.setOnClickListener( ButtonCancel6 );
  }

  private void cancelDownloadingFile( Future<File> future )
  {
    if( future != null )
    {
      future.cancel( true );
      out.setText( "Cancel downloading" );
    }
    else
    {
      out.setText( "Downloading do not start yet" );
    }
  }

  private void cancelDownloadingVoid( Future<Void> future )
  {
    if( future != null )
    {
      future.cancel( true );
      out.setText( "Cancel downloading" );
    }
    else
    {
      out.setText( "Downloading do not start yet" );
    }
  }

  private void cancelDownloadingByteArray( Future<byte[]> future )
  {
    if( future != null )
    {
      future.cancel( true );
      out.setText( "Cancel downloading" );
    }
    else
    {
      out.setText( "Downloading do not start yet" );
    }
  }

  private Future<File> downloadWithLocalPath()
  {
    Person person = Backendless.Data.of( Person.class ).findById( Defaults.ID_PERSON );

    File file = new File( getFilesDir(), Defaults.FILE_NAME );
    String localFilePathName = file.toString();
    return person.image.download( localFilePathName );
  }

  private Future<Void> downloadWithOutputStream()
  {
    Person person = Backendless.Data.of( Person.class ).findById( Defaults.ID_PERSON );

    File file = new File( getFilesDir(), Defaults.FILE_NAME );
    final OutputStream[] out = { null };
    try
    {
      out[ 0 ] = new FileOutputStream( file );
    }
    catch( FileNotFoundException e )
    {
      e.printStackTrace();
    }

    return person.image.download( out[ 0 ] );
  }

  private Future<byte[]> downloadFileInArray()
  {
    Person person = Backendless.Data.of( Person.class ).findById( Defaults.ID_PERSON );

    return person.image.download();
  }

  private Future<File> downloadWithLocalPathAndProgressBar()
  {
    Person person = Backendless.Data.of( Person.class ).findById( Defaults.ID_PERSON );
    ProgressBar bar = findViewById( R.id.determinateBar );

    final File file = new File( getFilesDir(), Defaults.FILE_NAME );
    String localFilePathName = file.toString();

    BackendlessFileAndroid android = (BackendlessFileAndroid) person.image;
    return android.download( localFilePathName, bar );
  }

  private Future<Void> downloadWithOutputStreamAndProgressBar()
  {
    Person person = Backendless.Data.of( Person.class ).findById( Defaults.ID_PERSON );
    ProgressBar bar = findViewById( R.id.determinateBar );

    File file = new File( getFilesDir(), Defaults.FILE_NAME );
    final OutputStream[] out = { null };
    try
    {
      out[ 0 ] = new FileOutputStream( file );
    }
    catch( FileNotFoundException e )
    {
      e.printStackTrace();
    }

    BackendlessFileAndroid android = (BackendlessFileAndroid) person.image;
    return android.download( out[ 0 ], bar );
  }

  private Future<byte[]> downloadFileInByteArrayWithProgressBar()
  {
    Person person = Backendless.Data.of( Person.class ).findById( Defaults.ID_PERSON );
    ProgressBar bar = findViewById( R.id.determinateBar );

    BackendlessFileAndroid android = (BackendlessFileAndroid) person.image;
    return android.download( bar );
  }

  private File writeByteArray( byte[] bytes )
  {
    File file = new File( getFilesDir(), Defaults.FILE_NAME );
    FileOutputStream stream = null;
    try
    {
      stream = new FileOutputStream( file );
      stream.write( bytes );
    }
    catch( IOException e )
    {
      e.printStackTrace();
    }
    finally
    {
      try
      {
        if( stream != null )
        {
          stream.close();
        }
      }
      catch( IOException e )
      {
        e.printStackTrace();
      }
    }
    return file;
  }

}