package com.backendless.tests.junit.unitTests.fileService.syncTests;

import com.backendless.Backendless;
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
    File fileToUpload = null;
    File uploadedFile = null;

    try
    {
      fileToUpload = createRandomFile();
      String path = getRandomPath();

      BackendlessFile backendlessFile = Backendless.Files.upload( fileToUpload, path );
      Assert.assertNotNull( "Server returned a null", backendlessFile );
      Assert.assertNotNull( "Server returned a null url", backendlessFile.getFileURL() );
      Assert.assertEquals( "Server returned wrong url " + backendlessFile.getFileURL(), "https://api.backendless.com/" + Defaults.TEST_APP_ID.toLowerCase() + "/" + Defaults.TEST_VERSION.toLowerCase() + "/files/" + path + "/" + fileToUpload.getName(), backendlessFile.getFileURL() );
      uploadedFile = new File( Defaults.REPO_DIR + "/" + Defaults.TEST_APP_ID.toLowerCase() + "/" + path + "/" + fileToUpload.getName() );
      Assert.assertTrue( "Server didn't create a file at the expected repo path", uploadedFile.exists() );
      Assert.assertTrue( "File to upload and uploaded files doesn't have the same content", compareFiles( fileToUpload, uploadedFile ) );

      Backendless.Files.remove( path );
      Assert.assertTrue( "Server didn't delete a file at the expected repo path", !uploadedFile.exists() );
    }
    finally
    {
      deleteFile( fileToUpload );
      deleteFile( uploadedFile );
    }
  }

  @Test
  public void testDeleteNonExistingFile() throws Throwable
  {
    try
    {
      Backendless.Files.remove( "foobarfoo" );
      Assert.fail( "Server didn't send an exception" );
    }
    catch( Throwable e )
    {
      checkErrorCode( 6000, e );
    }
  }

  @Test
  public void testDeleteEmptyDirectory() throws Throwable
  {
    File fileToUpload = null;
    File uploadedFile = null;
    File dir = null;

    try
    {
      fileToUpload = createRandomFile();
      String path = getRandomPath();
      String dirName = "somedir";

      BackendlessFile backendlessFile = Backendless.Files.upload( fileToUpload, dirName + "/" + path );
      Assert.assertNotNull( "Server returned a null", backendlessFile );
      Assert.assertNotNull( "Server returned a null url", backendlessFile.getFileURL() );
      Assert.assertEquals( "Server returned wrong url " + backendlessFile.getFileURL(), "https://api.backendless.com/" + Defaults.TEST_APP_ID.toLowerCase() + "/" + Defaults.TEST_VERSION.toLowerCase() + "/files/" + dirName + "/" + path + "/" + fileToUpload.getName(), backendlessFile.getFileURL() );
      uploadedFile = new File( Defaults.REPO_DIR + "/" + Defaults.TEST_APP_ID.toLowerCase() + "/" + dirName + "/" + path + "/" + fileToUpload.getName() );
      Assert.assertTrue( "Server didn't create a file at the expected repo path", uploadedFile.exists() );
      Assert.assertTrue( "File to upload and uploaded files doesn't have the same content", compareFiles( fileToUpload, uploadedFile ) );
      deleteFile( uploadedFile );

      Backendless.Files.removeDirectory( dirName );
      dir = new File( Defaults.REPO_DIR + "/" + dirName );
      Assert.assertTrue( "Server didn't delete a directory at the expected repo path", !dir.exists() );
    }
    finally
    {
      deleteFile( fileToUpload );
      deleteFile( dir );
    }
  }

  @Test
  public void testDeleteNonExistingDirectory() throws Throwable
  {
    try
    {
      Backendless.Files.removeDirectory( "foobarfoodir" );
      Assert.fail( "Server didn't throw an expected exception" );
    }
    catch( Throwable e )
    {
      checkErrorCode( 6000, e );
    }
  }

  @Test
  public void testDeleteDirectoryWithFiles() throws Throwable
  {
    File fileToUpload = null;
    File uploadedFile = null;

    try
    {
      fileToUpload = createRandomFile();
      String path = getRandomPath();
      String dirName = "somedir";

      BackendlessFile backendlessFile = Backendless.Files.upload( fileToUpload, dirName + "/" + path );
      Assert.assertNotNull( "Server returned a null", backendlessFile );
      Assert.assertNotNull( "Server returned a null url", backendlessFile.getFileURL() );
      Assert.assertEquals( "Server returned wrong url " + backendlessFile.getFileURL(), "https://api.backendless.com/" + Defaults.TEST_APP_ID.toLowerCase() + "/" + Defaults.TEST_VERSION.toLowerCase() + "/files/" + dirName + "/" + path + "/" + fileToUpload.getName(), backendlessFile.getFileURL() );
      uploadedFile = new File( Defaults.REPO_DIR + "/" + Defaults.TEST_APP_ID.toLowerCase() + "/" + dirName + "/" + path + "/" + fileToUpload.getName() );
      Assert.assertTrue( "Server didn't create a file at the expected repo path", uploadedFile.exists() );
      Assert.assertTrue( "File to upload and uploaded files doesn't have the same content", compareFiles( fileToUpload, uploadedFile ) );

      Backendless.Files.removeDirectory( dirName );
      Assert.assertTrue( "Server didn't delete a directory at the expected repo path", !uploadedFile.exists() );
    }
    finally
    {
      deleteFile( fileToUpload );
      deleteFile( uploadedFile );
    }
  }
}
