package com.backendless.messaging;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PublishMessageInfo
{
  private Object message;
  private String publisherId;
  private String subtopic;
  private List<String> pushSinglecast;
  private String pushBroadcast;
  private String pushPolicy;
  private long publishAt;
  private long repeatEvery;
  private long repeatExpiresAt;
  private Map<String, String> headers = new HashMap<String, String>();

  public PublishMessageInfo()
  {
  }

  public PublishMessageInfo( Object message, String subtopic )
  {
    this.message = message;
    this.subtopic = subtopic;
  }

  public Object getMessage()
  {
    return message;
  }

  public void setMessage( Object message )
  {
    this.message = message;
  }

  public String getPublisherId()
  {
    return publisherId;
  }

  public void setPublisherId( String publisherId )
  {
    this.publisherId = publisherId;
  }

  public String getSubtopic()
  {
    return subtopic;
  }

  public void setSubtopic( String subtopic )
  {
    this.subtopic = subtopic;
  }

  public String getPushBroadcast()
  {
    return pushBroadcast;
  }

  public void setPushBroadcast( String pushBroadcast )
  {
    this.pushBroadcast = pushBroadcast;
  }

  public Map<String, String> getHeaders()
  {
    return headers;
  }

  public void setHeaders( Map<String, String> headers )
  {
    this.headers = headers;
  }

  public long getPublishAt()
  {
    return publishAt;
  }

  public void setPublishAt( long publishAt )
  {
    this.publishAt = publishAt;
  }

  public long getRepeatEvery()
  {
    return repeatEvery;
  }

  public void setRepeatEvery( long repeatEvery )
  {
    this.repeatEvery = repeatEvery;
  }

  public long getRepeatExpiresAt()
  {
    return repeatExpiresAt;
  }

  public void setRepeatExpiresAt( Long repeatExpiresAt )
  {
    this.repeatExpiresAt = repeatExpiresAt == null ? 0 : repeatExpiresAt;
  }

  public List<String> getPushSinglecast()
  {
    return pushSinglecast;
  }

  public void setPushSinglecast( List<String> pushSinglecast )
  {
    this.pushSinglecast = pushSinglecast;
  }

  public String getPushPolicy()
  {
    return pushPolicy;
  }

  public void setPushPolicy( String pushPolicy )
  {
    this.pushPolicy = pushPolicy;
  }
}
