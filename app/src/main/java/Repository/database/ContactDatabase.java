package Repository.database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import Repository.dao.ContactDao;
import Repository.entity.ContactEntity;

import static commons.util.Constants.DATABASE_NAME;

@Database(entities = {ContactEntity.class}, version = 1, exportSchema = false)
public abstract class ContactDatabase extends RoomDatabase {
    private static ContactDatabase INSTANCE;

    public abstract ContactDao contactDao();

    public static ContactDatabase getContactDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), ContactDatabase.class,
                            DATABASE_NAME)
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}