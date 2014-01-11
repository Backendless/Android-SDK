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

package com.backendless.examples.endless.matchmaker.models.persistent;

import com.backendless.examples.endless.matchmaker.models.local.PreferenceTheme;

public class PreferencesDefaults
{
  private String theme;
  private String name;

  public PreferencesDefaults()
  {
  }

  public PreferencesDefaults( String theme, String name )
  {
    this.theme = theme;
    this.name = name;
  }

  public PreferencesDefaults( PreferenceTheme theme, String name )
  {
    this.theme = theme.getPreference();
    this.name = name;
  }

  public String getTheme()
  {
    return theme;
  }

  public void setTheme( String theme )
  {
    this.theme = theme;
  }

  public void setTheme( PreferenceTheme theme )
  {
    this.theme = theme.getPreference();
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }
}
