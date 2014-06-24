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

  public GooglePlaySubscriptionStatus getPlaySubscriptionsStatus( String packageName, String productId, String token )
  {
    return (GooglePlaySubscriptionStatus) Invoker.invokeSync( COMMERCE_MANAGER_SERVER_ALIAS, "getPlaySubscriptionsStatus", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), packageName, productId, token } );
  }

  public void getPlaySubscriptionsStatus( String packageName, String productId, String token,
                                    AsyncCallback<GooglePlaySubscriptionStatus> callback )
  {
    Invoker.invokeAsync( COMMERCE_MANAGER_SERVER_ALIAS, "getPlaySubscriptionsStatus", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), packageName, productId, token }, callback );
  }

  public void cancelPlaySubscription( String packageName, String productId, String token )
  {
    Invoker.invokeSync( COMMERCE_MANAGER_SERVER_ALIAS, "cancelPlaySubscription", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), packageName, productId, token } );
  }

  public void cancelPlaySubscription( String packageName, String productId, String token,
                                    AsyncCallback<Void> callback )
  {
    Invoker.invokeAsync( COMMERCE_MANAGER_SERVER_ALIAS, "cancelPlaySubscription", new Object[] { Backendless.getApplicationId(), Backendless.getVersion(), packageName, productId, token }, callback );
  }
}
