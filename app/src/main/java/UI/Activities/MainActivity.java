package UI.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.ContactsContract;

import Business.Broadcasts.ConnectivityChangeReceiver;
import Business.Services.util.SmsTimer;
import Business.tasks.handler.InternetConnectionHandler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import Business.Callbacks.DeleteContactCallback;
import Business.Facade.ContactFacadeImpl;
import Business.Listeners.SettingsListener;
import Business.Listeners.StatusListener;
import Business.tasks.core.ContactDatabaseTask;
import Repository.database.ContactDatabase;
import UI.Adapters.ContactsAdapter;
import Business.Listeners.AddListener;
import Business.Services.core.LocationSenderService;
import UI.Locator;
import UI.util.CommonUtils;
import commons.dto.ContactDto;
import commons.util.ResourceUtils;
import es.dmoral.toasty.Toasty;
import mspinu.imfinemom.R;

import static Business.Services.util.Utils.getMillisFrom;
import static commons.util.Constants.CONTACT_REQ_CODE;
import static commons.util.Constants.DEFAULT_TIMER_VALUE;
import static commons.util.Constants.INTERVAL_PREF_KEY;
import static commons.util.Constants.SETTINGS_CHANGED;
import static commons.util.Constants.SETTINGS_REQ_CODE;

public class MainActivity extends AppCompatActivity {

    public static Intent LOCATION_INTENT;
    public static boolean permissionsGranted = false;

    private RecyclerView recyclerView;
    private LinearLayoutCompat switchStatus;
    private LinearLayoutCompat contactsPlaceholder;
    private ContactFacadeImpl contactFacade;
    private ContactsAdapter contactsAdapter;
    private StatusListener statusListener;
    private SharedPreferences sharedPreferences;
    private int orientation;
    private AddListener addListener;
    private SettingsListener settingsListener;
    private ConnectivityChangeReceiver connectivityChangeReceiver;
    private InternetConnectionHandler internetConnectionHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Locator.createNotificationChannel(this);
        TextView addContact = findViewById(R.id.add);
        TextView settings = findViewById(R.id.settings);
        settingsListener = new SettingsListener(this, MainActivity.this);
        settings.setOnClickListener(settingsListener);
        orientation = getResources().getConfiguration().orientation;
        switchStatus = findViewById(R.id.sw);
        contactsPlaceholder = findViewById(R.id.placeholder);
        recyclerView = findViewById(R.id.rec_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        LOCATION_INTENT = new Intent(this, LocationSenderService.class);
        sharedPreferences = Business.Locator.getPreferences(this);
        LOCATION_INTENT.putExtra("timerValue", getMillisFrom(sharedPreferences.getString(INTERVAL_PREF_KEY, DEFAULT_TIMER_VALUE)));
        contactFacade = new ContactFacadeImpl(this);
        contactsAdapter = new ContactsAdapter(contactFacade, this);
        contactsAdapter.setViews(new LinkedList<>(Arrays.asList(contactsPlaceholder, recyclerView, switchStatus)));
        new ContactDatabaseTask(this.getClass().getSimpleName(), contactFacade, null,
                contactsAdapter).execute();
        addListener = new AddListener(this, contactsAdapter);
        addContact.setOnClickListener(addListener);
        statusListener = new StatusListener(this);
        switchStatus.setOnClickListener(statusListener);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new DeleteContactCallback(this, contactFacade, contactsAdapter).getDeleteSimpleCallback());
        itemTouchHelper.attachToRecyclerView(recyclerView);
        handlePermissions();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        internetConnectionHandler = new InternetConnectionHandler(this);
        connectivityChangeReceiver = new ConnectivityChangeReceiver(internetConnectionHandler);
        registerReceiver(connectivityChangeReceiver, intentFilter);
    }

    private void handlePermissions() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (!report.areAllPermissionsGranted()) {
                    Toasty.error(MainActivity.this,
                            "One or more permissions denied ! All must be allowed for the app the be properly used.", Toast.LENGTH_LONG).show();
                }
                else if (report.areAllPermissionsGranted()){
                    permissionsGranted = true;
                }
            }
            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("DATA_KEY", (ArrayList<? extends Parcelable>) contactsAdapter.getContacts());
        if (statusListener.isServiceStarted() && (!settingsListener.isPressed() && !addListener.isPressed())) {
            outState.putInt("ORIENTATION", orientation);
            outState.putBoolean("IS_SERVICE_STARTED", statusListener.isServiceStarted());
            outState.putLong("MILIS", LocationSenderService.getInstance().getRemainingMilis());
        }
        settingsListener.setPressed(false);
        addListener.setPressed(false);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        restoreData(savedInstanceState);
    }

    private void restoreData(Bundle savedInstanceState) {
        contactsAdapter.setContacts(savedInstanceState.<ContactDto>getParcelableArrayList("DATA_KEY"));
        statusListener.setServiceStarted(savedInstanceState.getBoolean("IS_SERVICE_STARTED"));
        if (savedInstanceState.getInt("ORIENTATION") != getResources().getConfiguration().orientation &&
                LocationSenderService.getInstance().getRemainingMilis() != 0) {
            LOCATION_INTENT.putExtra("timerValue", savedInstanceState.getLong("MILIS"));
            LOCATION_INTENT.putExtra("interrupted", true);
            statusListener.startService();
        }
    }

    @Override
    protected void onDestroy() {
        stopService(MainActivity.LOCATION_INTENT);
        SmsTimer.cancelTimer();
        ContactDatabase.destroyInstance();
        super.onDestroy();
        unregisterReceiver(connectivityChangeReceiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CONTACT_REQ_CODE:
                if (resultCode == RESULT_OK) {
                    try {
                        addContact(data);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case SETTINGS_REQ_CODE:
                if (resultCode == RESULT_OK && SETTINGS_CHANGED.equals(String.valueOf(data.getData()))) {
                    if (statusListener.isServiceStarted()) {
                        toggleServiceOff();
                        Toasty.info(this, ResourceUtils.getStringFrom(this,
                                R.string.restart_service_message), Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    private void toggleServiceOff() {
        this.stopService(LOCATION_INTENT);
        LocationSenderService.getInstance().setRemainingMilis(0);
        statusListener = new StatusListener(this);
        TextView textView = ((TextView) switchStatus.getChildAt(1));
        textView.setText(R.string.status_off);
        textView.setTextColor(getResources().getColor(R.color.status_off));
    }

    private void addContact(Intent data) throws FileNotFoundException {
        Uri uri = data.getData();
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
        String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        Bitmap photoBitmap = CommonUtils.getBitmap(this, cursor);
        if (CommonUtils.isValid(phoneNumber) && !CommonUtils.isDuplicate(this, contactsAdapter.getContacts(), phoneNumber)) {
            new ContactDatabaseTask(uri.getClass().getSimpleName(), contactFacade,
                    new ContactDto(name, phoneNumber, photoBitmap), contactsAdapter).execute();
        }
    }
}