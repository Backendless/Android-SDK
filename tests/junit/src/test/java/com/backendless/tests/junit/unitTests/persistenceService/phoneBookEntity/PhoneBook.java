package com.backendless.tests.junit.unitTests.persistenceService.phoneBookEntity;

import java.util.List;

public class PhoneBook
{
  private String objectId;
  private Contact owner;
  private List<Contact> contacts;
  private String __meta;

  public String get__meta()
  {
    return __meta;
  }

  public void set__meta( String __meta )
  {
    this.__meta = __meta;
  }

  public String getObjectId()
  {
    return objectId;
  }

  public void setObjectId( String objectId )
  {
    this.objectId = objectId;
  }

  public Contact getOwner()
  {
    return owner;
  }

  public void setOwner( Contact owner )
  {
    this.owner = owner;
  }

  public List<Contact> getContacts()
  {
    return contacts;
  }

  public void setContacts( List<Contact> contacts )
  {
    this.contacts = contacts;
  }
}