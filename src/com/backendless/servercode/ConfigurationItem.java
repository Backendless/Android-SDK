package com.backendless.servercode;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Eugene Chipachenko
 * Date: 23.07.14
 * Time: 16:43
 */
public class ConfigurationItem
{
  public String name;
  public ConfigurationItemType type;
  public boolean required;
  public String validator;
  public List<String> listItems;
}
