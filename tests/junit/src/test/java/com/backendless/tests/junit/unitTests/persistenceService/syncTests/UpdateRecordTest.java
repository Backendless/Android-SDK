package com.backendless.tests.junit.unitTests.persistenceService.syncTests;

import com.backendless.Backendless;
import com.backendless.tests.junit.unitTests.persistenceService.entities.updateEntities.BaseUpdateEntity;
import com.backendless.tests.junit.unitTests.persistenceService.phoneBookEntity.Address;
import com.backendless.tests.junit.unitTests.persistenceService.phoneBookEntity.Contact;
import com.backendless.tests.junit.unitTests.persistenceService.phoneBookEntity.PhoneBook;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class UpdateRecordTest extends TestsFrame
{
  @Test
  public void testBasicUpdate() throws Throwable
  {
    BaseUpdateEntity baseUpdateEntity = new BaseUpdateEntity();
    baseUpdateEntity.setName( "foobar" );
    baseUpdateEntity.setAge( 20 );

    BaseUpdateEntity savedEntity = Backendless.Persistence.save( baseUpdateEntity );
    savedEntity.setName( "foobar1" );
    savedEntity.setAge( 21 );

    Backendless.Persistence.save( savedEntity );

    BaseUpdateEntity foundEntity = Backendless.Persistence.of( BaseUpdateEntity.class ).find().getCurrentPage().get( 0 );

    Assert.assertEquals( "Server didn't update an entity", savedEntity, foundEntity );
    Assert.assertNotNull( "Server didn't set an updated field value", foundEntity.getUpdated() );
  }

  @Test
  public void updatePhoneBookWithOwnerAndContacts()
  {
    String applicationID = "35CB20A0-7533-E662-FFBC-7F477578E600";
    String secretKey = "1E8CE80A-1AA2-0C22-FF22-51F2AB40E900";
    String version = "v1";
    Backendless.initApp( applicationID, secretKey, version );

    Address address = new Address();
    address.setStreet( "TN 55" );
    address.setCity( "Lynchburg" );
    address.setState( "Tennessee" );

    Contact owner = new Contact();
    owner.setName( "Jack Daniels" );
    owner.setAge( 147 );
    owner.setPhone( "777-777-777" );
    owner.setTitle( "Favorites" );
    owner.setAddress( address );

    Address contactAddress = new Address();
    contactAddress.setStreet( "Unknown" );
    contactAddress.setCity( "Smallville" );
    contactAddress.setState( "Kansas" );

    Contact contact = new Contact();
    contact.setName( "Clark Kent" );
    contact.setAge( 75 );
    contact.setPhone( "111-111-111" );
    contact.setTitle( "Super heroes" );
    contact.setAddress( contactAddress );

    List<Contact> contacts = new ArrayList<Contact>();
    contacts.add( contact );

    PhoneBook phoneBook = new PhoneBook();
    phoneBook.setOwner( owner );
    phoneBook.setContacts( contacts );

    PhoneBook savedPhoneBook = Backendless.Persistence.save( phoneBook );
    savedPhoneBook.getContacts().add( contact );

    PhoneBook updatedPhoneBook = Backendless.Persistence.save( savedPhoneBook );
  }
}
