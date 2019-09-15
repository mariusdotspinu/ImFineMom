package Business.Listeners;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import UI.Activities.SettingsActivity;

import static commons.util.Constants.SETTINGS_REQ_CODE;

public class SettingsListener implements View.OnClickListener {
    private Context context;
    private Activity activity;
    private boolean pressed;

    public SettingsListener(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    @Override
    public void onClick(View view) {
        activity.startActivityForResult(new Intent(context, SettingsActivity.class), SETTINGS_REQ_CODE);
        pressed = true;
    }

    public boolean isPressed() {
        return pressed;
    }

    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }
}
