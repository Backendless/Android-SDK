package com.backendless.tests.junit.unitTests.fileService.asyncTests;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.files.BackendlessFile;
import com.backendless.tests.junit.Defaults;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class DeleteTest extends TestsFrame
{
  @Test
  public void testDeleteSingleFile() throws Throwable
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        final File fileToUpload = createRandomFile();
        final String path = getRandomPath();
        final File uploadedFile = new File( Defaults.REPO_DIR + "/" + Defaults.TEST_APP_ID.toLowerCase() + "/" + path + "/" + fileToUpload.getName() );

        Backendless.Files.upload( fileToUpload, path, new ResponseCallback<BackendlessFile>()
        {
          @Override
          public void handleResponse( BackendlessFile backendlessFile )
          {
            try
            {
              Assert.assertNotNull( "Server returned a null", backendlessFile );
              Assert.assertNotNull( "Server returned a null url", backendlessFile.getFileURL() );
              Assert.assertEquals( "Server returned wrong url " + backendlessFile.getFileURL(), "https://api.backendless.com/" + Defaults.TEST_APP_ID.toLowerCase() + "/" + Defaults.TEST_VERSION.toLowerCase() + "/files/" + path + "/" + fileToUpload.getName(), backendlessFile.getFileURL() );
              Assert.assertTrue( "Server didn't create a file at the expected repo path", uploadedFile.exists() );
              Assert.assertTrue( "File to upload and uploaded files doesn't have the same content", compareFiles( fileToUpload, uploadedFile ) );
            }
            catch( Throwable t )
            {
              failCountDownWith( t );
            }

            Backendless.Files.remove( path, new ResponseCallback<Void>()
            {
              @Override
              public void handleResponse( Void response )
              {
                try
                {
                  Assert.assertTrue( "Server didn't delete a file at the expected repo path", !uploadedFile.exists() );
                }
                catch( Throwable t )
                {
                  failCountDownWith( t );
                }
                finally
                {
                  deleteFile( fileToUpload );
                  deleteFile( uploadedFile );
                }

                countDown();
              }
            } );
          }
        } );
      }
    } );
  }

  @Test
  public void testDeleteNonExistingFile() throws Throwable
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        Backendless.Files.remove( "foobarfoo", new AsyncCallback<Void>()
        {
          @Override
          public void handleResponse( Void response )
          {
            failCountDownWith( "Server didn't send an exception" );
          }

          @Override
          public void handleFault( BackendlessFault fault )
          {
            checkErrorCode( 6000, fault );
          }
        } );
      }
    } );
  }

  @Test
  public void testDeleteEmptyDirectory() throws Throwable
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        final File fileToUpload = createRandomFile();
        final String path = getRandomPath();
        final String dirName = "somedir";
        final File uploadedFile = new File( Defaults.REPO_DIR + "/" + Defaults.TEST_APP_ID.toLowerCase() + "/" + dirName + "/" + path+ "/" + fileToUpload.getName() );

        Backendless.Files.upload( fileToUpload, dirName + "/" + path, new ResponseCallback<BackendlessFile>()
        {
          @Override
          public void handleResponse( BackendlessFile backendlessFile )
          {
            try
            {
              Assert.assertNotNull( "Server returned a null", backendlessFile );
              Assert.assertNotNull( "Server returned a null url", backendlessFile.getFileURL() );
              Assert.assertEquals( "Server returned wrong url " + backendlessFile.getFileURL(), "https://api.backendless.com/" + Defaults.TEST_APP_ID.toLowerCase() + "/" + Defaults.TEST_VERSION.toLowerCase() + "/files/" + dirName + "/" + path + "/" + fileToUpload.getName(), backendlessFile.getFileURL() );
              Assert.assertTrue( "Server didn't create a file at the expected repo path", uploadedFile.exists() );
              Assert.assertTrue( "File to upload and uploaded files doesn't have the same content", compareFiles( fileToUpload, uploadedFile ) );
              deleteFile( uploadedFile );
            }
            catch( Throwable t )
            {
              failCountDownWith( t );
            }

            Backendless.Files.removeDirectory( dirName, new ResponseCallback<Void>()
            {
              @Override
              public void handleResponse( Void response )
              {
                File dir = new File( Defaults.REPO_DIR + "/" + dirName );

                try
                {
                  Assert.assertTrue( "Server didn't delete a directory at the expected repo path", !dir.exists() );
                }
                catch( Throwable t )
                {
                  failCountDownWith( t );
                }
                finally
                {
                  deleteFile( fileToUpload );
                  deleteFile( dir );
                }

                countDown();
              }
            } );
          }
        } );
      }
    } );
  }

  @Test
  public void testDeleteNonExistingDirectory() throws Throwable
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        Backendless.Files.removeDirectory( "foobarfoodir", new AsyncCallback<Void>()
        {
          @Override
          public void handleResponse( Void response )
          {
            failCountDownWith( "Server didn't throw an expected exception" );
          }

          @Override
          public void handleFault( BackendlessFault fault )
          {
            checkErrorCode( 6000, fault );
          }
        } );
      }
    } );
  }

  @Test
  public void testDeleteDirectoryWithFiles() throws Throwable
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        final File fileToUpload = createRandomFile();
        final String path = getRandomPath();
        final String dirName = "somedir";
        final File uploadedFile = new File( Defaults.REPO_DIR + "/" + Defaults.TEST_APP_ID.toLowerCase() + "/" + dirName + "/" + path+ "/" + fileToUpload.getName() );

        Backendless.Files.upload( fileToUpload, dirName + "/" + path, new ResponseCallback<BackendlessFile>()
        {
          @Override
          public void handleResponse( BackendlessFile backendlessFile )
          {
            try
            {
              Assert.assertNotNull( "Server returned a null", backendlessFile );
              Assert.assertNotNull( "Server returned a null url", backendlessFile.getFileURL() );
              Assert.assertEquals( "Server returned wrong url " + backendlessFile.getFileURL(), "https://api.backendless.com/" + Defaults.TEST_APP_ID.toLowerCase() + "/" + Defaults.TEST_VERSION.toLowerCase() + "/files/" + dirName + "/" + path + "/" + fileToUpload.getName(), backendlessFile.getFileURL() );
              Assert.assertTrue( "Server didn't create a file at the expected repo path", uploadedFile.exists() );
              Assert.assertTrue( "File to upload and uploaded files doesn't have the same content", compareFiles( fileToUpload, uploadedFile ) );
            }
            catch( Throwable t )
            {
              failCountDownWith( t );
            }

            Backendless.Files.removeDirectory( dirName, new ResponseCallback<Void>()
            {
              @Override
              public void handleResponse( Void response )
              {
                try
                {
                  Assert.assertTrue( "Server didn't delete a directory at the expected repo path", !uploadedFile.exists() );
                }
                catch( Throwable t )
                {
                  failCountDownWith( t );
                }
                finally
                {
                  deleteFile( fileToUpload );
                  deleteFile( uploadedFile );
                }

                countDown();
              }
            } );
          }
        } );
      }
    } );
  }
}
