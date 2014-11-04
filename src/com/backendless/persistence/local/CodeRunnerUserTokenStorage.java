package com.backendless.persistence.local;

/**
 * Created with IntelliJ IDEA.
 * User: Eugene Chipachenko
 * Date: 11/4/14
 * Time: 13:56
 */
public class CodeRunnerUserTokenStorage implements IStorage<String>
{
  private static final CodeRunnerUserTokenStorage instance = new CodeRunnerUserTokenStorage();
  private String userToken;


  public static CodeRunnerUserTokenStorage instance()
  {
    return instance;
  }

  private CodeRunnerUserTokenStorage()
  {

  }

  @Override
  public String get()
  {
    return userToken;
  }

  @Override
  public void set( String value )
  {
    userToken = value;
  }
}
