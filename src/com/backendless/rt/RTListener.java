package com.backendless.rt;

public interface RTListener
{
  interface Predicate
  {
    boolean test( RTSubscription subscription );
  }
}
