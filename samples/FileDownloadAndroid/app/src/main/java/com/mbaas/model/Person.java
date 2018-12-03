package com.mbaas.model;

import com.backendless.files.BackendlessFile;

public class Person
{
  private String objectId;
  private BackendlessFile fileRef;

  public String getObjectId()
  {
    return objectId;
  }

  public void setObjectId( String objectId )
  {
    this.objectId = objectId;
  }

  public BackendlessFile getFileRef()
  {
    return fileRef;
  }

  public void setFileRef( BackendlessFile fileRef )
  {
    this.fileRef = fileRef;
  }
}
