package com.backendless.servercode;

import java.util.List;
import java.util.Map;

public class RunnerContext extends AbstractContext
{
  private Map missingProperties;
  private Object prematureResult;

  @Deprecated
  public List<String> getUserRole()
  {
    return this.userRoles;
  }

  @Deprecated
  public void setUserRole( List<String> userRole )
  {
    this.userRoles = userRole;
  }

  public Map getMissingProperties()
  {
    return missingProperties;
  }

  public void setMissingProperties( Map missingProperties )
  {
    this.missingProperties = missingProperties;
  }

  public Object getPrematureResult()
  {
    return prematureResult;
  }

  public void setPrematureResult( Object prematureResult )
  {
    this.prematureResult = prematureResult;
  }

  @Override
  public String toString()
  {
    final StringBuilder sb = new StringBuilder( "RunnerContext{" );
    sb.append( "missingProperties=" ).append( missingProperties );
    sb.append( ", prematureResult=" ).append( prematureResult );
    sb.append( ", " ).append(super.toString());
    sb.append( "}" );

    return sb.toString();
  }
}
