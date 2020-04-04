package Business.Broadcasts;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Messenger;
import android.widget.ProgressBar;

import Business.Services.core.InternetStatusService;
import Business.tasks.handler.InternetConnectionHandler;
import UI.util.DialogUtils;
import androidx.appcompat.app.AlertDialog;
import mspinu.imfinemom.R;

public class ConnectivityChangeReceiver extends BroadcastReceiver {
    private InternetConnectionHandler internetConnectionHandler;

    public ConnectivityChangeReceiver(InternetConnectionHandler internetConnectionHandler){
        this.internetConnectionHandler = internetConnectionHandler;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        AlertDialog progressBar = DialogUtils.getProgressBarDialog(new ProgressBar(context), R.string.progress_bar_loading);
        progressBar.show();
        ComponentName component = new ComponentName(context.getPackageName(),
                InternetStatusService.class.getName());
        intent.putExtra("isNetworkConnected", isConnected(context));
        intent.putExtra("INTERNET_CONNECTION_HANDLER", new Messenger(internetConnectionHandler));
        internetConnectionHandler.setProgressBar(progressBar);
        intent.setComponent(component);
        context.startService(intent);
    }

    public boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
