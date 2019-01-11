package Business.Facade;

import java.util.List;

import commons.dto.ContactDto;

public interface ContactFacade {
    long addContact(ContactDto contact);
    void deleteContact(ContactDto contact);
    void updateContact(long contactId, boolean isSelected);

    List<ContactDto> getAllContacts();
    List<String> getPhoneNumbersOfCheckedContacts();
}
