package com.backendless.servercode.extension;

import com.backendless.servercode.ExecutionResult;
import com.backendless.servercode.RunnerContext;

/**
 * Created with IntelliJ IDEA.
 * User: ivanlappo
 * Date: 5/20/13
 * Time: 12:40 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class FilesExtender
{
  public FilesExtender()
  {
  }

  public void beforeMoveToRepository( RunnerContext context, String fileUrlLocation ) throws Exception
  {
  }

  public void afterMoveToRepository( RunnerContext context, String fileUrlLocation, ExecutionResult<String> result ) throws Exception
  {
  }

  public void beforeDeleteFileOrDirectory( RunnerContext context, String fileUrlLocation ) throws Exception
  {
  }

  public void afterDeleteFileOrDirectory( RunnerContext context, String fileUrlLocation,
                                          ExecutionResult result ) throws Exception
  {
  }
}

