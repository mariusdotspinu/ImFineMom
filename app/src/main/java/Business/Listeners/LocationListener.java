package Business.Listeners;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.provider.Settings;

import Business.Services.core.LocationSenderService;
import androidx.core.app.NotificationManagerCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import Business.Locator;
import Business.Services.util.SmsTimer;
import UI.util.NotificationUtils;
import commons.util.ResourceUtils;
import mspinu.imfinemom.R;

import static Business.Services.util.Utils.getMillisFrom;
import static commons.util.Constants.DEFAULT_MILLIS_FUTURE;
import static commons.util.Constants.DEFAULT_SMS_APP;
import static commons.util.Constants.DEFAULT_TIMER_VALUE;
import static commons.util.Constants.INTERVAL_PREF_KEY;
import static commons.util.Constants.NOTIFICATION_CHANNEL;
import static commons.util.Constants.SMS_SENT_NOTIFICATION_ID;

public class LocationListener implements OnSuccessListener<Location> {

    private Context context;
    private List<String> phoneNumbers;
    private boolean interrupted;
    private SmsTimer smsTimer;

    public LocationListener(Context context, List<String> phoneNumbers, SmsTimer smsTimer, boolean interrupted) {

        this.context = context;
        this.phoneNumbers = phoneNumbers;
        this.interrupted = interrupted;
        this.smsTimer = smsTimer;
    }

    @Override
    public void onSuccess(Location location) {
        shareStatus(location);
        if (interrupted) {
            interrupted = false;
            smsTimer.cancel();
            smsTimer = SmsTimer.getInstance(LocationSenderService.getInstance().getRemainingMilis(), DEFAULT_MILLIS_FUTURE, context, LocationServices.getFusedLocationProviderClient(context), interrupted);
        }
        else{
            String interval = Locator.getPreferences(context).getString(INTERVAL_PREF_KEY, DEFAULT_TIMER_VALUE);
            smsTimer = SmsTimer.getInstance(getMillisFrom(interval), DEFAULT_MILLIS_FUTURE, context, LocationServices.getFusedLocationProviderClient(context), interrupted);
        }
        smsTimer.start();
    }

    private void shareStatus(Location location) {
        if (location != null) {
            String locationMessage = getLocationMessage(location);
            if (locationMessage != null) {
                sendSms(this.phoneNumbers, locationMessage);
                notifyUserOfSentMessages();
            }
        } else {
            Toast.makeText(context, R.string.location_not_found,
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void sendSms(List<String> phoneNumbers, String message) {
        SmsManager smsManager = SmsManager.getDefault();
        for (String phone : phoneNumbers) {
            ArrayList<String> parts = smsManager.divideMessage(message);
            smsManager.sendMultipartTextMessage(phone, null, parts, null,
                    null);
        }
    }

    private void notifyUserOfSentMessages() {
        String defaultSmsApplication = Settings.Secure.getString(context.getContentResolver(), DEFAULT_SMS_APP);
        Intent smsIntent = context.getPackageManager().getLaunchIntentForPackage(defaultSmsApplication);
        smsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent smsPendingIntent = PendingIntent.getActivity(
                context, 0, smsIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManagerCompat.from(context).notify(SMS_SENT_NOTIFICATION_ID,
                NotificationUtils.buildMessageStatusNotification(context, R.drawable.baseline_track_changes_black_24,
                        ResourceUtils.getStringFrom(context, R.string.message_sent),
                        ResourceUtils.getStringFrom(context, R.string.message_status),
                        ResourceUtils.getStringFrom(context, R.string.notification_tap_text),
                        smsPendingIntent,
                        NOTIFICATION_CHANNEL));
    }

    private String getLocationMessage(Location location) {
        String message = null;
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            final int MAX_RESULTS = 5;
            final int FIRST = 0;
            Address firstAddress = geocoder.getFromLocation(location.getLatitude(),
                    location.getLongitude(), MAX_RESULTS).get(FIRST);
            message = "Hey I'm here , " + firstAddress.getAddressLine(FIRST) + ", " +
                    firstAddress.getLocality() + ","
                    + " .. " + firstAddress.getCountryName() + " http://www.google.com/maps/place/" +
                    location.getLatitude() + "," + location.getLongitude();
            Log.d("MESSAGE_SENT", message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return message;
    }
}
