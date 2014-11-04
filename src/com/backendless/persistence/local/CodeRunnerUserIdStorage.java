package com.backendless.persistence.local;

/**
 * Created with IntelliJ IDEA.
 * User: Eugene Chipachenko
 * Date: 11/4/14
 * Time: 13:56
 */
public class CodeRunnerUserIdStorage implements IStorage<String>
{
  private static final CodeRunnerUserIdStorage instance = new CodeRunnerUserIdStorage();
  private String userToken;


  public static CodeRunnerUserIdStorage instance()
  {
    return instance;
  }

  private CodeRunnerUserIdStorage()
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
