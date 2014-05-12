package com.backendless.servercode.extension;

import com.backendless.commons.media.MediaObjectInfo;
import com.backendless.commons.media.OperationMeta;
import com.backendless.servercode.RunnerContext;

/**
 * Created with IntelliJ IDEA.
 * User: ivanlappo
 * Date: 5/20/13
 * Time: 12:40 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class MediaExtender
{
  public MediaExtender()
  {
  }

  public void acceptConnection( RunnerContext context, String operationName, String tubeName, Boolean accessGranted ) throws Exception
  {
  }

  public void publishStarted( RunnerContext context, MediaObjectInfo info ) throws Exception
  {
  }

  public void publishFinished( RunnerContext context, MediaObjectInfo info ) throws Exception
  {
  }

  public void streamCreated( RunnerContext context, OperationMeta meta ) throws Exception
  {
  }

  public void streamFinished( RunnerContext context, OperationMeta meta ) throws Exception
  {
  }
}

