package Business.Listeners;

import android.content.Context;
import android.view.View;
import android.widget.EditText;

import Business.Services.util.FirebaseAuthentication;

public class ResendListener implements View.OnClickListener {

    private Context context;
    private EditText smsCodeEditText;
    private FirebaseAuthentication firebaseAuthentication;

    public ResendListener(Context context, EditText smsCodeEditText){
        this.context = context;
        this.smsCodeEditText = smsCodeEditText;
        firebaseAuthentication = FirebaseAuthentication.getInstance(context);
    }

    @Override
    public void onClick(View v) {
        firebaseAuthentication.sendVerificationCode(context,
                Business.Locator.getPreferences(context).getString("VERIFY_PHONE_NUMBER", ""),
                smsCodeEditText);
    }
}