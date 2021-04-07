package Business.Listeners;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;

import Business.Services.core.LocationSenderService;
import Business.Services.util.SmsTimer;
import androidx.appcompat.widget.LinearLayoutCompat;
import android.view.View;
import android.widget.TextView;

import UI.Activities.MainActivity;
import UI.util.DialogUtils;
import mspinu.imfinemom.R;

public class StatusListener implements LinearLayoutCompat.OnClickListener {

    private Context context;
    private boolean isServiceStarted;
    private TextView statusTitle;

    public StatusListener(Context context) {
        this.context = context;
        statusTitle = ((Activity)context).findViewById(R.id.status_switch);
    }

    public boolean isServiceStarted() {
        return isServiceStarted;
    }

    public void setServiceStarted(boolean serviceStarted) {
        isServiceStarted = serviceStarted;
    }

    @Override
    public void onClick(View view) {
        final TextView textView = view.getRootView().findViewById(R.id.status_switch);
        if (textView.getText().equals("Off")) {
            DialogUtils.showSimpleDialog(context, "This will start sending sms to your selected contacts.", "Start service?",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            isServiceStarted = true;
                            startService();
                        }
                    },
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
        } else {
            DialogUtils.showSimpleDialog(context, "This will stop sending sms to your selected contacts.", "Stop service?",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            isServiceStarted = false;
                            stopService();
                        }
                    },
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
        }
    }

    public void startService() {
            statusTitle.setText(R.string.status_on);
            statusTitle.setTextColor(context.getResources().getColor(R.color.status_on));
            context.startService(MainActivity.LOCATION_INTENT);
    }

    private void stopService() {
        statusTitle.setText(R.string.status_off);
        statusTitle.setTextColor(context.getResources().getColor(R.color.status_off));
        LocationSenderService.getInstance().setRemainingMilis(0);
        context.stopService(MainActivity.LOCATION_INTENT);
        SmsTimer.cancelTimer();
    }
}
