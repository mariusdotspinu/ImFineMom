package Business.Services.util;

import android.content.Context;
import android.os.CountDownTimer;
import com.google.android.material.snackbar.Snackbar;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;

import java.util.List;

import Business.Facade.ContactFacadeImpl;
import Business.Listeners.LocationListener;
import Business.Services.core.LocationSenderService;
import UI.Activities.MainActivity;
import mspinu.imfinemom.R;

public class SmsTimer extends CountDownTimer {

    private FusedLocationProviderClient client;
    private Context context;
    private List<String> phoneNumbersOfCheckedContacts;
    private boolean interrupted;
    private static SmsTimer smsTimer;

    private SmsTimer(long millisInFuture, long countDownInterval, Context context ,
             FusedLocationProviderClient client, boolean interrupted) {
        super(millisInFuture, countDownInterval);
        this.client = client;
        this.context = context;
        this.interrupted = interrupted;
    }

    public static SmsTimer getInstance(long millisInFuture, long countDownInterval, Context context ,
                                       FusedLocationProviderClient client, boolean interrupted){
        if (smsTimer != null){
            smsTimer.cancel();
        }
        smsTimer = new SmsTimer(millisInFuture, countDownInterval, context , client, interrupted);
        return smsTimer;
    }

    public static void cancelTimer(){
        if (smsTimer != null){
            smsTimer.cancel();
        }
    }

    @Override
    public void onTick(long millisUntilFinished) {
        Log.d("SECONDS", String.valueOf(millisUntilFinished / 1000));
        LocationSenderService.getInstance().setRemainingMilis(millisUntilFinished);
    }

    @Override
    public void onFinish() {
        getLastLocation();
    }

    private void getLastLocation(){
        try {
            setCheckedContacts();
        }
        catch (InterruptedException ie){
            ie.printStackTrace();
        }
        catch (SecurityException se){
            Snackbar.make(((MainActivity)context).getWindow().getDecorView().getRootView(),
                    R.string.permission_access_location_denied, Snackbar.LENGTH_SHORT).show();
        }
    }

    private void setCheckedContacts() throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                phoneNumbersOfCheckedContacts = new ContactFacadeImpl(context)
                        .getPhoneNumbersOfCheckedContacts();
                client.getLastLocation().addOnSuccessListener(new LocationListener(context,
                        phoneNumbersOfCheckedContacts, smsTimer, interrupted));
            }
        });
        thread.start();
        thread.join();
    }
}