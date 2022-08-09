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

package com.backendless.persistence;

import com.backendless.commons.persistence.group.GroupingColumnValue;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ToString( callSuper = true )
@Getter
@Setter
public class BackendlessGroupDataQuery extends BackendlessDataQuery
{
  public static final int GROUP_DEPTH = 3;

  private int groupPageSize = DEFAULT_PAGE_SIZE;
  private int recordsPageSize = DEFAULT_PAGE_SIZE;
  private int groupDepth = GROUP_DEPTH;
  private List<GroupingColumnValue> groupPath;
  private QueryOptions queryOptions;
}