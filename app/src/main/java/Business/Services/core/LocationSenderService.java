package Business.Services.core;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import androidx.annotation.Nullable;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import Business.Locator;
import Business.Services.util.SmsTimer;
import UI.Activities.MainActivity;
import UI.util.NotificationUtils;
import commons.util.ResourceUtils;
import mspinu.imfinemom.R;

import static Business.Services.util.Utils.getMillisFrom;
import static commons.util.Constants.DEFAULT_MILLIS_FUTURE;
import static commons.util.Constants.INTERVAL_PREF_KEY;
import static commons.util.Constants.NOTIFICATION_IDENTIFIER;
import static commons.util.Constants.NOTIFICATION_CHANNEL;

/**
 * Created by Marius on 9/8/2017.
 */

public class LocationSenderService extends Service
                                   implements SharedPreferences.OnSharedPreferenceChangeListener{

    private FusedLocationProviderClient client;
    private SharedPreferences preferences;
    private static LocationSenderService locationSenderService;
    private long remainingMilis;
    private SmsTimer smsTimer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public static LocationSenderService getInstance(){
        if(locationSenderService == null){
            locationSenderService = new LocationSenderService();
        }
        return locationSenderService;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        preferences = Locator.getPreferences(this);
        preferences.registerOnSharedPreferenceChangeListener(this);
        client = LocationServices.getFusedLocationProviderClient(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                    new Intent(this, MainActivity.class), 0);
            startForeground(NOTIFICATION_IDENTIFIER, NotificationUtils.buildStatusNotification(
                    this, R.drawable.baseline_track_changes_black_24,
                    ResourceUtils.getStringFrom(this, R.string.notification_running),
                    ResourceUtils.getStringFrom(this, R.string.app_name),
                    ResourceUtils.getStringFrom(this, R.string.sharing_status),
                    pendingIntent, NOTIFICATION_CHANNEL));
            long intervalMillis = intent.getExtras().getLong("timerValue");
            boolean interrupted = intent.getExtras().getBoolean("interrupted");
        smsTimer = SmsTimer.getInstance(intervalMillis, DEFAULT_MILLIS_FUTURE , this, client, interrupted);
        smsTimer.start();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        if (smsTimer != null) {
            smsTimer.cancel();
        }
        preferences.unregisterOnSharedPreferenceChangeListener(this);
        stopForeground(true);
        super.onDestroy();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        String prefKeyValue = sharedPreferences.getString(INTERVAL_PREF_KEY, null);
            if (prefKeyValue != null){
                smsTimer.cancel();
                smsTimer = SmsTimer.getInstance(getMillisFrom(prefKeyValue), DEFAULT_MILLIS_FUTURE,
                        this, client, false);
            }
    }

    public void setRemainingMilis(long milis){
        this.remainingMilis = milis;
    }

    public long getRemainingMilis() {
        return remainingMilis;
    }
}