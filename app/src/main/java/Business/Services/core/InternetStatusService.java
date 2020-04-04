package Business.Services.core;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import androidx.annotation.Nullable;

public class InternetStatusService extends IntentService {

    public InternetStatusService() {
        super(InternetStatusService.class.getName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null && intent.getExtras() != null) {
            Bundle extras = intent.getExtras();
            boolean isNetworkConnected = extras.getBoolean("isNetworkConnected");
            Messenger messenger = (Messenger) extras.get("INTERNET_CONNECTION_HANDLER");
            Message message = Message.obtain();
            Bundle messageBundle = new Bundle();
            if (!isNetworkConnected) {
                messageBundle.putBoolean("IS_CONNECTED", false);
                message.setData(messageBundle);
            } else {
                messageBundle.putBoolean("IS_CONNECTED", true);
                message.setData(messageBundle);
            }
            try {
                messenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
