package com.backendless.servercode.extension;

import com.backendless.servercode.ExecutionResult;
import com.backendless.servercode.RunnerContext;

import java.util.Set;

public abstract class AtomicOperationExtender
{
  public void beforeReset( RunnerContext context, String counterName ) throws Exception
  {

  }

  public void afterReset( RunnerContext context, String counterName, ExecutionResult<Void> result ) throws Exception
  {

  }

  public void beforeGetAndIncrement( RunnerContext context, String counterName ) throws Exception
  {

  }

  public void afterGetAndIncrement( RunnerContext context, String counterName, ExecutionResult<Long> result ) throws Exception
  {

  }

  public void beforeIncrementAndGet( RunnerContext context, String counterName ) throws Exception
  {

  }

  public void afterIncrementAndGet( RunnerContext context, String counterName, ExecutionResult<Long> result ) throws Exception
  {

  }

  public void beforeDecrementAndGet( RunnerContext context, String counterName ) throws Exception
  {

  }

  public void afterDecrementAndGet( RunnerContext context, String counterName, ExecutionResult<Long> result ) throws Exception
  {

  }

  public void beforeGetAndDecrement( RunnerContext context, String counterName ) throws Exception
  {

  }

  public void afterGetAndDecrement( RunnerContext context, String counterName, ExecutionResult<Long> result ) throws Exception
  {

  }

  public void beforeAddAndGet( RunnerContext context, String counterName, long value ) throws Exception
  {

  }

  public void afterAddAndGet( RunnerContext context, String counterName, long value, ExecutionResult<Long> result ) throws Exception
  {

  }

  public void beforeGetAndAdd( RunnerContext context, String counterName, long value ) throws Exception
  {

  }

  public void afterGetAndAdd( RunnerContext context, String counterName, long value,
                              ExecutionResult<Long> result ) throws Exception
  {

  }

  public void beforeGet( RunnerContext context, String counterName ) throws Exception
  {

  }

  public void afterGet( RunnerContext context, String counterName, ExecutionResult<Long> result ) throws Exception
  {

  }

  public void beforeCompareAndSet( RunnerContext context, String counterName, long expected, long updated ) throws Exception
  {

  }

  public void afterCompareAndSet( RunnerContext context, String counterName, long expected, long updated, ExecutionResult<Boolean> result ) throws Exception
  {

  }

  public void beforeList( RunnerContext context,String pattern ) throws Exception
  {

  }

  public void afterList( RunnerContext context, String pattern, ExecutionResult<Set<String>> result ) throws Exception
  {

  }

}
