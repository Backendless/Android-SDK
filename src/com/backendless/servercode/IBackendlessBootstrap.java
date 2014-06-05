package com.backendless.servercode;

/**
 * Created with IntelliJ IDEA.
 * User: Eugene Chipachenko
 * Date: 05.06.14
 * Time: 11:53
 */
public interface IBackendlessBootstrap
{
  /**
   * Receives notification that the classes initialization process is starting.
   */
  public void onStart();

  /**
   * Receives notification that the CodeRunner is about to be shut down.
   */
  public void onStop();
}
