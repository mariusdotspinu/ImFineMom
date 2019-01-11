package UI.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.amitshekhar.DebugDB;

import java.util.ArrayList;

import Business.Callbacks.DeleteContactCallback;
import Business.Facade.ContactFacadeImpl;
import Business.Listeners.SwitchListener;
import Business.tasks.core.ContactDatabaseTask;
import Repository.database.ContactDatabase;
import UI.Adapters.ContactsAdapter;
import Business.Listeners.FabListener;
import Business.Services.core.LocationSenderService;
import UI.Init;
import commons.util.Permissions;
import commons.dto.ContactDto;
import commons.util.ResourceUtils;
import mspinu.imfinemom.R;

import static commons.util.Constants.CONTACT_REQ_CODE;
import static commons.util.Constants.SETTINGS_CHANGED;
import static commons.util.Constants.SETTINGS_REQ_CODE;

public class MainActivity extends AppCompatActivity {

    public static Intent LOCATION_INTENT;
    public static int isPermitted = 0;

    private RecyclerView listView;
    private SwitchCompat sw;
    private Permissions permissions;
    private ContactFacadeImpl contactFacade;
    private ContactsAdapter contactsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Init.createNotificationChannel(this);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setSubtitle(R.string.subtitle);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(LinearLayoutManager.VERTICAL);

        sw = findViewById(R.id.sw);
        listView = findViewById(R.id.rec_view);
        listView.setLayoutManager(lm);
        LOCATION_INTENT = new Intent(this, LocationSenderService.class);
        permissions = new Permissions(MainActivity.this);
        isPermitted = permissions.checkPermission(ResourceUtils.getStringFrom(this,
                R.string.permission_denied));

        contactFacade = new ContactFacadeImpl(this);
        contactsAdapter = new ContactsAdapter(contactFacade, this);
        new ContactDatabaseTask(this.getClass().getSimpleName(), contactFacade, null,
                contactsAdapter).execute();
        fab.setOnClickListener(new FabListener(this, contactsAdapter));
        sw.setOnCheckedChangeListener(new SwitchListener(this));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new DeleteContactCallback(this, contactFacade, contactsAdapter).getDeleteSimpleCallback());
        itemTouchHelper.attachToRecyclerView(listView);

        Log.d("DATABASE_LINK", DebugDB.getAddressLog());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("DATA_KEY", (ArrayList<? extends Parcelable>) contactsAdapter.getContacts());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        contactsAdapter.setContacts(savedInstanceState.<ContactDto>getParcelableArrayList("DATA_KEY"));
    }

    @Override
    public void onResume() {
        super.onResume();
        listView.setAdapter(contactsAdapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        isPermitted = this.permissions.onRequestPermissionsResult(permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        stopService(MainActivity.LOCATION_INTENT);
        ContactDatabase.destroyInstance();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivityForResult(new Intent(this, SettingsActivity.class), SETTINGS_REQ_CODE);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CONTACT_REQ_CODE:
                if (resultCode == RESULT_OK) {
                    addContact(data);
                }
                break;
            case SETTINGS_REQ_CODE:
                if (resultCode == RESULT_OK && SETTINGS_CHANGED.equals(String.valueOf(data.getData()))) {
                    toggleServiceOff();
                    Toast.makeText(this, ResourceUtils.getStringFrom(this,
                            R.string.restart_service_message), Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void toggleServiceOff() {
        this.stopService(MainActivity.LOCATION_INTENT);
        sw.setChecked(false);
    }

    private void addContact(Intent data) {
        Uri uri = data.getData();
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
        String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

        if (isValid(phoneNumber)) {
            new ContactDatabaseTask(uri.getClass().getSimpleName(), contactFacade,
                    new ContactDto(name, phoneNumber), contactsAdapter).execute();
        }
    }

    private boolean isValid(String phone) {
        return Patterns.PHONE.matcher(phone).matches();
    }
}

