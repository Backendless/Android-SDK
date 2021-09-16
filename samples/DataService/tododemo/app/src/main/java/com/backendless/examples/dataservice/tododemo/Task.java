package com.backendless.examples.dataservice.tododemo;

public class Task
{
  private String objectId;
  private String deviceId;
  private String title;
  private boolean completed = false;

  public String getObjectId()
  {
    return objectId;
  }

  public void setObjectId( String objectId )
  {
    this.objectId = objectId;
  }

  public String getDeviceId()
  {
    return deviceId;
  }

  public void setDeviceId( String deviceId )
  {
    this.deviceId = deviceId;
  }

  public String getTitle()
  {
    return title;
  }

  public void setTitle( String title )
  {
    this.title = title;
  }

  public boolean isCompleted()
  {
    return completed;
  }

  public void setCompleted( boolean completed )
  {
    this.completed = completed;
  }
}
