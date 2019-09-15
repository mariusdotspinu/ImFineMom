package Repository.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import Repository.entity.ContactEntity;
import commons.dto.ContactDto;

public class DbMapper {

    public static List<ContactDto> mapEntitiesToDtos(List<ContactEntity> contactEntities){
        List<ContactDto> contacts = new ArrayList<>();
        for (ContactEntity contactEntity : contactEntities){
            contacts.add(mapEntityToDto(contactEntity));
        }
        return contacts;
    }

    private static ContactDto mapEntityToDto(ContactEntity contactEntity) {
        ContactDto contactDto = new ContactDto();
        contactDto.setId(contactEntity.getId());
        contactDto.setName(contactEntity.getName());
        contactDto.setPhoneNumber(contactEntity.getPhoneNumber());
        contactDto.setSelected(contactEntity.getIsSelected());
        contactDto.setPhotoBitmap(getBitmapFromBase64String(contactEntity.getPhotoBase64Url()));
        return contactDto;
    }

    public static ContactEntity mapDtoToEntity(ContactDto contactDto){
        ContactEntity contactEntity = new ContactEntity();
        contactEntity.setId(contactDto.getId());
        contactEntity.setName(contactDto.getName());
        contactEntity.setPhoneNumber(contactDto.getPhoneNumber());
        contactEntity.setIsSelected(contactDto.isSelected());
        contactEntity.setPhotoBase64Url(getBase64StringFromBitmap(contactDto.getPhotoBitmap()));
        return contactEntity;
    }

    private static Bitmap getBitmapFromBase64String(String base64String){
        byte[] data = Base64.decode(base64String, Base64.DEFAULT);
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inMutable = true;
        return BitmapFactory.decodeByteArray(data, 0, data.length, opt);
    }

    private static String getBase64StringFromBitmap(Bitmap bitmap){
        ByteArrayOutputStream bitmapOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bitmapOutputStream);
        byte[] bai = bitmapOutputStream.toByteArray();
        return Base64.encodeToString(bai, Base64.DEFAULT);
    }
}
