package com.backendless.push;

public class IntentUtils
{

  public static boolean registrationIntent( String action )
  {
    return action.equals( Constants.INTENT_FROM_GCM_REGISTRATION_CALLBACK )
                    || action.equals( Constants.INTENT_FROM_ADM_REGISTRATION_CALLBACK );
  }

  public static boolean messageIntent( String action )
  {
    return action.equals( Constants.INTENT_FROM_GCM_MESSAGE ) || action.equals( Constants.INTENT_FROM_ADM_MESSAGE );
  }

  public static boolean libraryRetry( String action )
  {
    return action.equals( Constants.INTENT_FROM_GCM_LIBRARY_RETRY )
                    || action.equals( Constants.INTENT_FROM_ADM_LIBRARY_RETRY );
  }

}
