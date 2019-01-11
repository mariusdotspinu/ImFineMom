package Business.Listeners;

import android.content.Context;
import android.location.LocationManager;
import android.support.design.widget.Snackbar;
import android.widget.CompoundButton;

import UI.Activities.MainActivity;
import mspinu.imfinemom.R;

import static android.content.Context.LOCATION_SERVICE;

public class SwitchListener implements CompoundButton.OnCheckedChangeListener {

    private Context context;

    public SwitchListener(Context context) {
        this.context = context;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (isChecked) {
            startService(buttonView, isGpsEnabled);
        } else {
            stopService(buttonView);
        }
    }

    private void startService(CompoundButton buttonView, boolean isGpsEnabled) {
        if (isGpsEnabled) {
            buttonView.setText(R.string.switch_text_on);
            context.getApplicationContext().startService(MainActivity.LOCATION_INTENT);
        } else {
            Snackbar.make(buttonView.getRootView(), R.string.permission_gps_denied,
                    Snackbar.LENGTH_LONG).show();
        }
    }

    private void stopService(CompoundButton buttonView) {
        buttonView.setText(R.string.switch_text_off);
        context.getApplicationContext().stopService(MainActivity.LOCATION_INTENT);
    }
}
