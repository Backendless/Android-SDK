/*
 * ********************************************************************************************************************
 *  <p/>
 *  BACKENDLESS.COM CONFIDENTIAL
 *  <p/>
 *  ********************************************************************************************************************
 *  <p/>
 *  Copyright 2012 BACKENDLESS.COM. All Rights Reserved.
 *  <p/>
 *  NOTICE: All information contained herein is, and remains the property of Backendless.com and its suppliers,
 *  if any. The intellectual and technical concepts contained herein are proprietary to Backendless.com and its
 *  suppliers and may be covered by U.S. and Foreign Patents, patents in process, and are protected by trade secret
 *  or copyright law. Dissemination of this information or reproduction of this material is strictly forbidden
 *  unless prior written permission is obtained from Backendless.com.
 *  <p/>
 *  ********************************************************************************************************************
 */

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

  public void beforeSaveFileFromByteArray( RunnerContext context, String fileUrlLocation, Boolean overwrite ) throws Exception
  {
  }

  public void afterSaveFileFromByteArray( RunnerContext context, String fileUrlLocation, Boolean overwrite,
                                          ExecutionResult result ) throws Exception
  {
  }

  public void beforeMoveFileOrDirectory( RunnerContext context, String sourcePath, String targetPath )
      throws Exception
  {
  }

  public void afterMoveFileOrDirectory( RunnerContext context, String sourcePath, String targetPath,
                                        ExecutionResult<String> result ) throws Exception
  {
  }

  public void beforeCopyFileOrDirectory( RunnerContext context, String sourcePath, String targetPath )
      throws Exception
  {
  }

  public void afterCopyFileOrDirectory( RunnerContext context, String sourcePath, String targetPath,
                                        ExecutionResult<String> result ) throws Exception
  {
  }

}

