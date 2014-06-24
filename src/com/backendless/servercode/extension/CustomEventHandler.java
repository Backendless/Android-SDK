package com.backendless.servercode.extension;

import javax.naming.event.EventContext;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Eugene Chipachenko
 * Date: 24.06.14
 * Time: 16:03
 */
public abstract class CustomEventHandler
{
  public Map handleEvent( EventContext context, Map eventArgs )
  {
    return null;
  }
}
