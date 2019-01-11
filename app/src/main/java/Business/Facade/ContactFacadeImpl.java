package Business.Facade;

import android.content.Context;

import java.util.List;

import Repository.database.ContactDatabase;
import Repository.database.ContactDatabaseAccess;
import commons.dto.ContactDto;

public class ContactFacadeImpl implements ContactFacade {

    private ContactDatabase db;

    public ContactFacadeImpl(Context context){
        this.db = ContactDatabase.getContactDatabase(context);
    }

    @Override
    public long addContact(ContactDto contact) {
        return ContactDatabaseAccess.addContact(db, contact);
    }

    @Override
    public void deleteContact(ContactDto contact) {
        ContactDatabaseAccess.deleteContact(db, contact);
    }

    @Override
    public void updateContact(long contactId, boolean isChanged){
        ContactDatabaseAccess.updateContact(db, contactId, isChanged);
    }

    @Override
    public List<ContactDto> getAllContacts() {
        return ContactDatabaseAccess.getAll(db);
    }

    @Override
    public List<String> getPhoneNumbersOfCheckedContacts() {
        return ContactDatabaseAccess.getPhoneNumbersOfCheckedContacts(db);
    }
}