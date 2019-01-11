package Repository.database;

import android.util.Log;

import java.util.List;

import Repository.util.DbMapper;
import commons.dto.ContactDto;

public abstract class ContactDatabaseAccess {

    public static long addContact(ContactDatabase db, ContactDto contactDto){
        Log.d("ADDING CONTACT", contactDto.getName());
        return db.contactDao().addContact(DbMapper.mapDtoToEntity(contactDto));
    }


    public static void deleteContact(ContactDatabase db, ContactDto contactDto){
        db.contactDao().deleteContact(DbMapper.mapDtoToEntity(contactDto));
        Log.d("DELETING CONTACT", String.valueOf(contactDto.getId()));
    }

    public static void updateContact(ContactDatabase db, long contactId, boolean isChanged){
        db.contactDao().updateContact(contactId, isChanged);
        Log.d("SELECTED_IS", String.valueOf(isChanged));
    }

    public static List<ContactDto> getAll(ContactDatabase db){
        return DbMapper.mapEntitiesToDtos(db.contactDao().getAll());
    }

    public static List<String> getPhoneNumbersOfCheckedContacts(ContactDatabase db){
        return db.contactDao().getPhoneNumbersOfCheckedContacts();
    }
}
