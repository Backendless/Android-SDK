package com.backendless.servercode;

/**
 * Created with IntelliJ IDEA.
 * User: Eugene Chipachenko
 * Date: 23.07.14
 * Time: 16:44
 */
public enum ConfigurationItemType
{
  STRING,   // text input
  TEXT,     // text area, paragraph
  NUMBER,   // text input or numeric stepper
  TWO_STATE_BOOLEAN,   // radio button or toggle (yes, no)
  THREE_STATE_BOOLEAN, // yes, no, null
  DATE,     // date picker
  TIME,     // time picker
  DATETIME, // date-time picker
  LIST      // combobox
}

