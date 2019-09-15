package commons.dto;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Marius on 8/13/2017.
 */

public class ContactDto implements Parcelable {
    private String phoneNumber;
    private String name;
    private Bitmap photoBitmap;
    private long id;

    private boolean isSelected;

    public ContactDto() {
    }

    public ContactDto(String name, String phoneNumber, Bitmap photoBitmap) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.photoBitmap = photoBitmap;
    }

    protected ContactDto(Parcel in) {
        phoneNumber = in.readString();
        name = in.readString();
        photoBitmap = in.readParcelable(Bitmap.class.getClassLoader());
        isSelected = Boolean.parseBoolean(in.readString());
    }

    public static final Creator<ContactDto> CREATOR = new Creator<ContactDto>() {
        @Override
        public ContactDto createFromParcel(Parcel in) {
            return new ContactDto(in);
        }

        @Override
        public ContactDto[] newArray(int size) {
            return new ContactDto[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getPhotoBitmap() {
        return photoBitmap;
    }

    public void setPhotoBitmap(Bitmap photoBitmap) {
        this.photoBitmap = photoBitmap;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public static Creator<ContactDto> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(phoneNumber);
        parcel.writeString(name);
        parcel.writeParcelable(photoBitmap, 1);
        parcel.writeString(String.valueOf(isSelected));
    }
}
