package Business.Services.core;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;


import java.util.concurrent.TimeUnit;

import Business.Locator;
import Business.Services.util.SmsTimer;
import UI.Activities.MainActivity;
import UI.util.NotificationUtils;
import commons.util.Permissions;
import commons.util.ResourceUtils;
import mspinu.imfinemom.R;

import static commons.util.Constants.DEFAULT_MILLIS_FUTURE;
import static commons.util.Constants.DEFAULT_TIMER_VALUE;
import static commons.util.Constants.INTERVAL_PREF_KEY;
import static commons.util.Constants.NOTIFICATION_IDENTIFIER;
import static commons.util.Constants.NOTIFICATION_CHANNEL;

/**
 * Created by Marius on 9/8/2017.
 */

public class LocationSenderService extends Service
                                   implements SharedPreferences.OnSharedPreferenceChangeListener{

    private FusedLocationProviderClient client;
    private String interval;
    private SmsTimer timer;
    private SharedPreferences preferences;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        preferences = Locator.getPreferences(this);

        preferences.registerOnSharedPreferenceChangeListener(this);
        interval = preferences.getString(INTERVAL_PREF_KEY, DEFAULT_TIMER_VALUE);

        client = LocationServices.getFusedLocationProviderClient(this);
        timer = new SmsTimer(getMillisFrom(interval),DEFAULT_MILLIS_FUTURE , this, client);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(MainActivity.isPermitted == Permissions.PERMISSIONS_ALLOWED_IMPLICIT ||
                MainActivity.isPermitted == Permissions.PERMISSIONS_ALLOWED_RUNTIME) {

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                    new Intent(this, MainActivity.class), 0);

            startForeground(NOTIFICATION_IDENTIFIER, NotificationUtils.buildStatusNotification(
                    this, R.mipmap.service_running,
                    ResourceUtils.getStringFrom(this, R.string.notification_running),
                    ResourceUtils.getStringFrom(this, R.string.app_name),
                    ResourceUtils.getStringFrom(this, R.string.sharing_status),
                    pendingIntent, NOTIFICATION_CHANNEL));

            timer.start();
        }

        else{
            Toast.makeText(this, R.string.permission_gps_denied, Toast.LENGTH_SHORT)
                    .show();
        }
        return START_NOT_STICKY;

    }

    @Override
    public void onDestroy() {
        timer.cancel();
        preferences.unregisterOnSharedPreferenceChangeListener(this);
        stopForeground(true);
        super.onDestroy();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        String prefKeyValue = sharedPreferences.getString(INTERVAL_PREF_KEY, null);

            if (prefKeyValue != null){
                interval = prefKeyValue;
                timer = new SmsTimer(getMillisFrom(interval), DEFAULT_MILLIS_FUTURE,
                        this, client);
            }
    }

    private long getMillisFrom(String minutes){
        return TimeUnit.MINUTES.toMillis(Long.parseLong(minutes));
    }
}