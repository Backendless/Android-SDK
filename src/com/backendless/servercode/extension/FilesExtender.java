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

import com.backendless.files.FileInfo;
import com.backendless.servercode.ExecutionResult;
import com.backendless.servercode.RunnerContext;

import java.util.List;


public abstract class FilesExtender
{
  public FilesExtender()
  {
  }

  /**
   * Use beforeUpload method
   *
   * @param context
   * @param fileUrlLocation
   * @throws Exception
   */
  @Deprecated
  public void beforeMoveToRepository( RunnerContext context, String fileUrlLocation ) throws Exception
  {
  }

  /**
   * Use afterUpload method
   *
   * @param context
   * @param fileUrlLocation
   * @param result
   * @throws Exception
   */
  @Deprecated
  public void afterMoveToRepository( RunnerContext context, String fileUrlLocation, ExecutionResult<String> result ) throws Exception
  {
  }

  public void beforeUpload( RunnerContext context, String fileUrlLocation ) throws Exception
  {
    beforeMoveToRepository( context, fileUrlLocation );
  }

  public void afterUpload( RunnerContext context, String fileUrlLocation,
                           ExecutionResult<String> result ) throws Exception
  {
    afterMoveToRepository( context, fileUrlLocation, result );
  }

  @Deprecated
  public void beforeDeleteFileOrDirectory( RunnerContext context, String fileUrlLocation ) throws Exception
  {
  }

  @Deprecated
  public void afterDeleteFileOrDirectory( RunnerContext context, String fileUrlLocation,
                                          ExecutionResult<Integer> result ) throws Exception
  {
  }

  public void beforeDeleteFileOrDirectory( RunnerContext context, String fileUrlLocation, String pattern, boolean recursive ) throws Exception
  {
    this.beforeDeleteFileOrDirectory( context, fileUrlLocation );
  }

  public void afterDeleteFileOrDirectory( RunnerContext context, String fileUrlLocation, String pattern, boolean recursive,
                                          ExecutionResult<Integer> result ) throws Exception
  {
    this.afterDeleteFileOrDirectory( context, fileUrlLocation, result );
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

  public void beforeCount( RunnerContext context, String path, String pattern, boolean recursive, boolean isCountDirectories )
          throws Exception
  {
  }

  public void afterCount( RunnerContext context, String path, String pattern, boolean recursive, boolean isCountDirectories,
                          ExecutionResult<Integer> result ) throws Exception
  {
  }

  public void beforeExists( RunnerContext context, String path )
          throws Exception
  {
  }

  public void afterExists( RunnerContext context, String path,
                           ExecutionResult<Boolean> result ) throws Exception
  {
  }

  public void beforeListing( RunnerContext context, String path, String pattern,
                             boolean recursive, int pageSize,
                             int offset )
          throws Exception
  {
  }

  public void afterListing( RunnerContext context, String path, String pattern,
                            boolean recursive, int pageSize,
                            int offset, ExecutionResult<List<FileInfo>> result ) throws Exception
  {
  }
}

