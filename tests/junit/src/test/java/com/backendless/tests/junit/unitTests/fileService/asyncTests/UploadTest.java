package com.backendless.tests.junit.unitTests.fileService.asyncTests;

import com.backendless.Backendless;
import com.backendless.files.BackendlessFile;
import com.backendless.tests.junit.Defaults;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class UploadTest extends TestsFrame
{
  @Test
  public void testUploadSingleFile() throws Throwable
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        final File fileToUpload = createRandomFile();
        final String path = getRandomPath();
        Backendless.Files.upload( fileToUpload, path, new ResponseCallback<BackendlessFile>()
        {
          @Override
          public void handleResponse( BackendlessFile backendlessFile )
          {
            File uploadedFile = null;

            try
            {
              Assert.assertNotNull( "Server returned a null", backendlessFile );
              Assert.assertNotNull( "Server returned a null url", backendlessFile.getFileURL() );
              Assert.assertEquals( "Server returned wrong url " + backendlessFile.getFileURL(), "https://api.backendless.com/" + Defaults.TEST_APP_ID.toLowerCase() + "/" + Defaults.TEST_VERSION.toLowerCase() + "/files/" + path + "/" + fileToUpload.getName(), backendlessFile.getFileURL() );
              //TODO add check for the generated file by url
              uploadedFile = new File( Defaults.REPO_DIR + "/" + Defaults.TEST_APP_ID.toLowerCase() + "/" + path+ "/" + fileToUpload.getName() );

              Assert.assertTrue( "Server didn't create a file at the expected repo path: " + uploadedFile.getAbsolutePath(), uploadedFile.exists() && uploadedFile.isFile() );
              Assert.assertTrue( "File to upload and uploaded files doesn't have the same content", compareFiles( fileToUpload, uploadedFile ) );
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

  @Test
  public void testUploadInvalidPath() throws Throwable
  {
    runInNewThreadAndAwait( new Runnable()
    {
      @Override
      public void run()
      {
        final File fileToUpload = createRandomFile();
        final String path = "2!@%^&*(){}[]/?|`~";

        Backendless.Files.upload( fileToUpload, path, new ResponseCallback<BackendlessFile>()
        {
          @Override
          public void handleResponse( BackendlessFile backendlessFile )
          {
            try
            {
              String expected = "https://api.backendless.com/" + Defaults.TEST_APP_ID.toLowerCase() + "/" + Defaults.TEST_VERSION.toLowerCase() + "/files/" + path + "/" + fileToUpload.getName();
              Assert.assertNotNull( "Server returned null result", backendlessFile );
              Assert.assertEquals( "Server returned wrong file url", expected, backendlessFile.getFileURL() );
            }
            catch( Throwable t )
            {
              failCountDownWith( t );
            }

            countDown();
          }
        } );
      }
    } );
  }
}