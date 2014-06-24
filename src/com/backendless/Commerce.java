package com.backendless;

import com.backendless.async.callback.AsyncCallback;
import com.backendless.commerce.GooglePlayPurchaseStatus;
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
}
