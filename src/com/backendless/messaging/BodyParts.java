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

package com.backendless.messaging;

/**
 * Created with IntelliJ IDEA.
 * User: denis
 * Date: 10/14/13
 * Time: 1:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class BodyParts
{
  private final String textMessage;
  private final String htmlMessage;

  public BodyParts( String textMessage, String htmlMessage )
  {
    this.textMessage = textMessage;
    this.htmlMessage = htmlMessage;
  }

  public String getTextMessage()
  {
    return textMessage;
  }

  public String getHtmlMessage()
  {
    return htmlMessage;
  }
}
