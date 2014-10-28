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

import com.backendless.async.callback.AsyncCallback;
import com.backendless.commerce.GooglePlayPurchaseStatus;
import com.backendless.commerce.GooglePlaySubscriptionStatus;
import weborb.types.Types;

/**
 * Created by scadge on 6/24/14.
 */
public final class Commerce
{
  private static final String COMMERCE_MANAGER_SERVER_ALIAS = "com.backendless.services.commerce.CommerceService";

  private static final Commerce instance = new Commerce();

  static Commerce getInstance()
  {
    return instance;
  }

  private Commerce()
  {
    Types.addClientClassMapping( "com.backendless.management.commerce.GooglePlayPurchaseStatus", GooglePlayPurchaseStatus.class );
    Types.addClientClassMapping( "com.backendless.management.commerce.GooglePlaySubscriptionStatus", GooglePlaySubscriptionStatus.class );
  }

  public GooglePlayPurchaseStatus validatePlayPurchase( String packageName, String productId, String token )
  {
    return (GooglePlayPurchaseStatus) Invoker.invokeSync( COMMERCE_MANAGER_SERVER_ALIAS, "validatePlayPurchase", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), packageName, productId, token } );
  }

  public void validatePlayPurchase( String packageName, String productId, String token,
                                    AsyncCallback<GooglePlayPurchaseStatus> callback )
  {
    Invoker.invokeAsync( COMMERCE_MANAGER_SERVER_ALIAS, "validatePlayPurchase", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), packageName, productId, token }, callback );
  }

  public GooglePlaySubscriptionStatus getPlaySubscriptionsStatus( String packageName, String subscriptionId, String token )
  {
    return (GooglePlaySubscriptionStatus) Invoker.invokeSync( COMMERCE_MANAGER_SERVER_ALIAS, "getPlaySubscriptionsStatus", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), packageName, subscriptionId, token } );
  }

  public void getPlaySubscriptionsStatus( String packageName, String subscriptionId, String token,
                                    AsyncCallback<GooglePlaySubscriptionStatus> callback )
  {
    Invoker.invokeAsync( COMMERCE_MANAGER_SERVER_ALIAS, "getPlaySubscriptionsStatus", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), packageName, subscriptionId, token }, callback );
  }

  public void cancelPlaySubscription( String packageName, String subscriptionId, String token )
  {
    Invoker.invokeSync( COMMERCE_MANAGER_SERVER_ALIAS, "cancelPlaySubscription", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), packageName, subscriptionId, token } );
  }

  public void cancelPlaySubscription( String packageName, String subscriptionId, String token,
                                    AsyncCallback<Void> callback )
  {
    Invoker.invokeAsync( COMMERCE_MANAGER_SERVER_ALIAS, "cancelPlaySubscription", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), packageName, subscriptionId, token }, callback );
  }
}
