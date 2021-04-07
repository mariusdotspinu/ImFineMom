package Business.Listeners;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import Business.Services.core.LocationSenderService;
import androidx.core.app.NotificationManagerCompat;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import Business.Services.util.SmsTimer;
import UI.util.NotificationUtils;
import commons.util.ResourceUtils;
import es.dmoral.toasty.Toasty;
import mspinu.imfinemom.R;

import static Business.Services.util.Utils.getMillisFrom;
import static commons.util.Constants.DEFAULT_MILLIS_FUTURE;
import static commons.util.Constants.DEFAULT_TIMER_VALUE;
import static commons.util.Constants.INTERVAL_PREF_KEY;
import static commons.util.Constants.NOTIFICATION_CHANNEL;
import static commons.util.Constants.SMS_SENT_NOTIFICATION_ID;

public class LocationListener implements OnSuccessListener<Location> {

    private Context context;
    private List<String> phoneNumbers;
    private boolean interrupted;
    private SmsTimer smsTimer;
    private int numberOfSmsSent = 0;

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
            String interval = Business.Locator.getPreferences(context).getString(INTERVAL_PREF_KEY, DEFAULT_TIMER_VALUE);
            smsTimer = SmsTimer.getInstance(getMillisFrom(interval), DEFAULT_MILLIS_FUTURE, context, LocationServices.getFusedLocationProviderClient(context), interrupted);
        }
        smsTimer.start();
    }

    private void shareStatus(Location location) {
        if (location != null) {
            String locationMessage = getLocationMessage(location);
            if (locationMessage != null) {
                sendSmsRequest(this.phoneNumbers, locationMessage);
                notifyUserOfSentMessages();
            }
        } else {
            Toasty.warning(context, R.string.location_not_found,
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void sendSmsRequest(List<String> phoneNumbers, String message){
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = buildRequestUrl(phoneNumbers, message);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {},
                error -> {});
        queue.add(stringRequest);
    }

    private String buildRequestUrl(List<String> phoneNumbers, String message){
        String baseUrl = "http://imfinemom.herokuapp.com/send?numbers=PHONE&message=MESSAGE";
        StringBuilder builder = new StringBuilder();
        for(String phoneNumber : phoneNumbers){
            builder.append(phoneNumber).append(';');
        }
        builder.deleteCharAt(builder.length() - 1);
        return baseUrl.replace("PHONE", builder.toString()).replace("MESSAGE", message);
    }

    private void notifyUserOfSentMessages() {
        NotificationManagerCompat.from(context).notify(SMS_SENT_NOTIFICATION_ID,
                NotificationUtils.buildMessageStatusNotification(context, R.drawable.baseline_track_changes_black_24,
                        ResourceUtils.getStringFrom(context, R.string.message_sent),
                        ResourceUtils.getStringFrom(context, R.string.message_status),
                        String.valueOf(++numberOfSmsSent),
                        null,
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
