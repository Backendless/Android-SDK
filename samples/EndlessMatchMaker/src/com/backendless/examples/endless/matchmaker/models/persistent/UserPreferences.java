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

public class UserPreferences
{
  private String objectId;
  private String email;
  private String preference;
  private String theme;

  public UserPreferences()
  {
  }

  public UserPreferences( String email, String preference, String theme )
  {
    this.email = email;
    this.preference = preference;
    this.theme = theme;
  }

  public UserPreferences( String email, String preference, PreferenceTheme theme )
  {
    this.email = email;
    this.preference = preference;
    this.theme = theme.getPreference();
  }

  public String getObjectId()
  {
    return objectId;
  }

  public void setObjectId( String objectId )
  {
    this.objectId = objectId;
  }

  public String getEmail()
  {
    return email;
  }

  public void setEmail( String email )
  {
    this.email = email;
  }

  public String getPreference()
  {
    return preference;
  }

  public void setPreference( String preference )
  {
    this.preference = preference;
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
}
