package UI.util;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Patterns;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.util.List;

import commons.dto.ContactDto;
import commons.util.ResourceUtils;
import mspinu.imfinemom.R;

public class CommonUtils {

    public static Bitmap getBitmap(Context context, Cursor cursor) throws FileNotFoundException {
        String photoUri = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
        if (photoUri != null) {
            return BitmapFactory.decodeStream(context.getContentResolver().openInputStream(Uri.parse(photoUri)));
        }
        return BitmapFactory.decodeResource(context.getResources(), R.drawable.photo_placeholder);
    }

    public static boolean isValid(String phoneNumber) {
        return Patterns.PHONE.matcher(phoneNumber).matches();
    }

    public static boolean isDuplicate(Context context, List<ContactDto> contacts ,String phoneNumber){
        for (ContactDto contactDto : contacts){
            if (contactDto.getPhoneNumber().equals(phoneNumber)){
                Toast.makeText(context, ResourceUtils.getStringFrom(context,
                        R.string.duplicate_message), Toast.LENGTH_LONG).show();
                return true;
            }
        }
        return false;
    }
}
