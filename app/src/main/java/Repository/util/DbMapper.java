package Repository.util;

import java.util.ArrayList;
import java.util.List;

import Repository.entity.ContactEntity;
import commons.dto.ContactDto;

public class DbMapper {

    public static List<ContactDto> mapEntitiesToDtos(List<ContactEntity> contactEntities){
        List<ContactDto> contactDtos = new ArrayList<>();
        for (ContactEntity contactEntity : contactEntities){
            contactDtos.add(mapEntityToDto(contactEntity));
        }
        return contactDtos;
    }

    private static ContactDto mapEntityToDto(ContactEntity contactEntity){
        ContactDto contactDto = new ContactDto();
        contactDto.setId(contactEntity.getId());
        contactDto.setName(contactEntity.getName());
        contactDto.setPhoneNumber(contactEntity.getPhoneNumber());
        contactDto.setSelected(contactEntity.getIsSelected());

        return contactDto;
    }

    public static ContactEntity mapDtoToEntity(ContactDto contactDto){
        ContactEntity contactEntity = new ContactEntity();
        contactEntity.setId(contactDto.getId());
        contactEntity.setName(contactDto.getName());
        contactEntity.setPhoneNumber(contactDto.getPhoneNumber());
        contactEntity.setIsSelected(contactDto.isSelected());

        return contactEntity;
    }
}
