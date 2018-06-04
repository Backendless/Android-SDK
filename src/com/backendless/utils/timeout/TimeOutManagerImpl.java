package com.backendless.utils.timeout;

public class TimeOutManagerImpl implements TimeOutManager
{
  private static final int INITIAL_TIMEOUT = 200;
  private static final int MAX_TIMEOUT = 60 * 1000; //1 min
  private static final int REPEAT_TIMES_BEFORE_INCREASE = 10;

  private int repeatedTimes;
  private int currentTimeOut;

  public TimeOutManagerImpl()
  {
    reset();
  }

  @Override
  public int nextTimeout()
  {
    if( currentTimeOut > MAX_TIMEOUT )
      return MAX_TIMEOUT;

    if( repeatedTimes++ > 0 && ( repeatedTimes % REPEAT_TIMES_BEFORE_INCREASE ) == 0 )
    {
      currentTimeOut *= 2;
    }

    return currentTimeOut;
  }

  @Override
  public int repeatedTimes()
  {
    return repeatedTimes;
  }

  @Override
  public void reset()
  {
     repeatedTimes = 0;
     currentTimeOut = INITIAL_TIMEOUT;
  }
}
