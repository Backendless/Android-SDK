package com.backendless.push;
/**
 * *******************************************************************************************************************
 * <p/>
 * BACKENDLESS.COM CONFIDENTIAL
 * <p/>
 * *********************************************************************************************************************
 * <p/>
 * Copyright 2012 BACKENDLESS.COM. All Rights Reserved.
 * <p/>
 * NOTICE:  All information contained herein is, and remains the property of Backendless.com and its suppliers,
 * if any.  The intellectual and technical concepts contained herein are proprietary to Backendless.com and its
 * suppliers and may be covered by U.S. and Foreign Patents, patents in process, and are protected by trade secret
 * or copyright law. Dissemination of this information or reproduction of this material is strictly forbidden
 * unless prior written permission is obtained from Backendless.com.
 * <p/>
 * CREATED ON: 1/27/13
 * AT: 1:07 PM
 * ********************************************************************************************************************
 */

import android.app.Notification;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NotificationLookAndFeel
{
  private static final String COLOR_SEARCH_RECURSE_TIP = "SOME_SAMPLE_TEXT";
  private static float notification_text_size;
  private static Integer notification_text_color = null;
  private static float notification_title_size_factor = (float) 1.0;
  private static float notification_description_size_factor = (float) 0.8;

  private boolean recurseGroup( Context context, ViewGroup gp )
  {
    final int count = gp.getChildCount();

    for ( int i = 0; i < count; ++i )
    {
      if( gp.getChildAt( i ) instanceof TextView )
      {
        final TextView text = (TextView) gp.getChildAt( i );
        final String szText = text.getText().toString();

        if( COLOR_SEARCH_RECURSE_TIP.equals( szText ) )
        {
          notification_text_color = text.getTextColors().getDefaultColor();
          notification_text_size = text.getTextSize();
          DisplayMetrics metrics = new DisplayMetrics();
          WindowManager systemWM = (WindowManager) context.getSystemService( Context.WINDOW_SERVICE );
          systemWM.getDefaultDisplay().getMetrics( metrics );
          notification_text_size /= metrics.scaledDensity;
          return true;
        }
      }
      else if( gp.getChildAt( i ) instanceof ViewGroup )
      {
        return recurseGroup( context, (ViewGroup) gp.getChildAt( i ) );
      }
    }
    return false;
  }

  void extractColors( Context context )
  {
    if( notification_text_color != null )
      return;

    try
    {
      Notification ntf = new Notification();
      ntf.setLatestEventInfo( context, COLOR_SEARCH_RECURSE_TIP, "Utest", null );
      LinearLayout group = new LinearLayout( context );
      ViewGroup event = (ViewGroup) ntf.contentView.apply( context, group );
      recurseGroup( context, event );
      group.removeAllViews();
    }
    catch ( Exception e )
    {
      notification_text_color = android.R.color.black;
    }
  }

  public int getTextColor()
  {
    return notification_text_color;
  }

  public float getTextSize()
  {
    return notification_title_size_factor * notification_text_size;
  }
}
