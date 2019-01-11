package Repository.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import Repository.entity.ContactEntity;

@Dao
public interface ContactDao {
    @Query("SELECT * FROM CONTACT")
    List<ContactEntity> getAll();

    @Query("SELECT PHONE_NUMBER FROM CONTACT WHERE SELECTED IS 1")
    List<String> getPhoneNumbersOfCheckedContacts();

    @Insert
    long addContact(ContactEntity contactEntity);

    @Delete
    void deleteContact(ContactEntity contactEntity);

    @Query("UPDATE CONTACT SET SELECTED = :isSelected WHERE id = :contactId")
    void updateContact(long contactId, boolean isSelected);
}
