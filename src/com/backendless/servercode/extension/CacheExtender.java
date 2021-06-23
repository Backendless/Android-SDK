package com.backendless.servercode.extension;

import com.backendless.servercode.ExecutionResult;
import com.backendless.servercode.RunnerContext;

public abstract class CacheExtender
{
  public void beforePut( RunnerContext context, String key, Object value, Integer ttl ) throws Exception
  {

  }

  public void afterPut( RunnerContext context, String key, Object value, Integer ttl, ExecutionResult<Void> result ) throws Exception
  {

  }

  public void beforeGet( RunnerContext context, String key ) throws Exception
  {

  }

  public void afterGet( RunnerContext context, String key, ExecutionResult<Object> result  ) throws Exception
  {

  }

  public void beforeContains( RunnerContext context, String key ) throws Exception
  {

  }

  public void afterContains( RunnerContext context, String key, ExecutionResult<Boolean> result  ) throws Exception
  {

  }

  public void beforeExpireAt( RunnerContext context, String key, long expireAt ) throws Exception
  {

  }

  public void afterExpireAt( RunnerContext context, String key, ExecutionResult<Void> result  ) throws Exception
  {

  }

  public void beforeExpireIn( RunnerContext context, String key, int expireIn ) throws Exception
  {

  }

  public void afterExpireIn( RunnerContext context, String key, ExecutionResult<Void> result  ) throws Exception
  {

  }

  public void beforeDelete( RunnerContext context, String key ) throws Exception
  {

  }

  public void afterDelete( RunnerContext context, String key, ExecutionResult<Void> result  ) throws Exception
  {

  }
}
