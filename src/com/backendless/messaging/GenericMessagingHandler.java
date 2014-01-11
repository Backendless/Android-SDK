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

import com.backendless.Backendless;
import com.backendless.Subscription;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessException;
import com.backendless.exceptions.BackendlessFault;

import java.util.List;

public class GenericMessagingHandler implements IMessageHandler
{
  private Runnable subscriptionThread;

  public GenericMessagingHandler( final AsyncCallback<List<Message>> subscriptionResponder,
                                  final Subscription subscription )
  {
    subscriptionThread = new Runnable()
    {
      @Override
      public void run()
      {
        try
        {
          List<Message> messages = Backendless.Messaging.pollMessages( subscription.getChannelName(), subscription.getSubscriptionId() );

          if( !messages.isEmpty() && subscriptionResponder != null )
            subscriptionResponder.handleResponse( messages );
        }
        catch( BackendlessException exception )
        {
          if( subscriptionResponder != null )
            subscriptionResponder.handleFault( new BackendlessFault( exception ) );
        }
      }
    };
  }

  @Override
  public Runnable getSubscriptionThread()
  {
    return subscriptionThread;
  }
}
