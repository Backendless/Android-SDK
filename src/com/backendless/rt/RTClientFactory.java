package com.backendless.rt;

public class RTClientFactory
{
  private static final RTClient rtClient;

  static
  {
    RTClient rt;
    try
    {
      rt = new AsynRTClient();
    }
    catch( NoClassDefFoundError e )
    {
      rt = new RTClientWithoutSocketIO();
    }

    rtClient = rt;
  }

  public static RTClient get()
  {
     return rtClient;
  }
}
