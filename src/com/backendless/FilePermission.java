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

package com.backendless;

import com.backendless.files.security.AbstractFilePermission;
import com.backendless.files.security.FileOperation;

public class FilePermission
{
    public static final Read READ = new Read();
    public static final Delete DELETE = new Delete();
    public static final Write WRITE = new Write();
    public static final Permission PERMISSION = new Permission();

    public static class Read extends AbstractFilePermission
    {
        @Override
        protected FileOperation getOperation()
        {
            return FileOperation.READ;
        }
    }

    public static class Delete extends AbstractFilePermission
    {
        @Override
        protected FileOperation getOperation()
        {
            return FileOperation.DELETE;
        }
    }

    public static class Write extends AbstractFilePermission
    {
        @Override
        protected FileOperation getOperation()
        {
            return FileOperation.WRITE;
        }
    }

    public static class Permission extends AbstractFilePermission
    {
        @Override
        protected FileOperation getOperation()
        {
            return FileOperation.PERMISSION;
        }
    }
}