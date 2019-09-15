package Repository.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "CONTACT")
public class ContactEntity {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "NAME")
    private String name;

    @ColumnInfo(name = "PHONE_NUMBER")
    private String phoneNumber;

    @ColumnInfo(name = "SELECTED")
    private boolean isSelected;

    @ColumnInfo(name = "PHOTO")
    private String photoBase64Url;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public String getPhotoBase64Url() {
        return photoBase64Url;
    }

    public void setPhotoBase64Url(String photoBase64Url) {
        this.photoBase64Url = photoBase64Url;
    }
}
