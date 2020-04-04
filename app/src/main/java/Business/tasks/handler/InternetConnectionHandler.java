package Business.tasks.handler;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import androidx.appcompat.app.AlertDialog;
import mspinu.imfinemom.R;

public class InternetConnectionHandler extends Handler {
    private Context context;
    private AlertDialog progressBar;

    public InternetConnectionHandler(Context context){
        this.context = context;
    }
    public void setProgressBar(AlertDialog progressBar){
        this.progressBar = progressBar;
    }

    @Override
    public void handleMessage(Message msg) {
        Bundle message = msg.getData();
        boolean isConnected = message.getBoolean("IS_CONNECTED");
        View mainLayout = ((Activity)context).findViewById(R.id.main_layout);
        View noInternetLayout = ((Activity)context).findViewById(R.id.no_internet_layout);
        if (isConnected){
            mainLayout.setVisibility(View.VISIBLE);
            noInternetLayout.setVisibility(View.GONE);
        }
        else{
            mainLayout.setVisibility(View.GONE);
            noInternetLayout.setVisibility(View.VISIBLE);
        }
        progressBar.dismiss();
    }
}
