package com.backendless.rt;

public class RTClientFactory
{
  private static final RTClient rtClient = new AsynRTClient();

  public static RTClient get()
  {
     return rtClient;
  }
}
