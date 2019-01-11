package Business.Services.util;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;

import java.util.List;

import Business.Facade.ContactFacadeImpl;
import Business.Listeners.LocationListener;
import Business.Services.core.LocationSenderService;
import UI.Activities.MainActivity;
import commons.dto.ContactDto;
import mspinu.imfinemom.R;

public class SmsTimer extends CountDownTimer {

    private FusedLocationProviderClient client;
    private Context context;
    private List<String> phoneNumbersOfCheckedContacts;

    public SmsTimer(long millisInFuture, long countDownInterval, Context context ,
             FusedLocationProviderClient client) {

        super(millisInFuture, countDownInterval);
        this.client = client;
        this.context = context;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        Log.d("MILIS", String.valueOf(millisUntilFinished));
    }

    @Override
    public void onFinish() {
        getLastLocation();
    }

    private void getLastLocation(){

        try {
            setCheckedContacts();
            client.getLastLocation().addOnSuccessListener(new LocationListener(context,
                    phoneNumbersOfCheckedContacts, this));
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
            }
        });
        thread.start();
        thread.join();
    }
}
