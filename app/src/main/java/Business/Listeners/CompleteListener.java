package Business.Listeners;

import android.content.Context;
import android.view.View;
import android.widget.EditText;

import Business.Services.util.FirebaseAuthentication;
import es.dmoral.toasty.Toasty;

public class CompleteListener implements View.OnClickListener {

    private Context context;
    private EditText smsCodeEditText;
    private FirebaseAuthentication firebaseAuthentication;

    public CompleteListener(Context context, EditText smsCodeEditText){
        this.context = context;
        this.smsCodeEditText = smsCodeEditText;
        firebaseAuthentication = FirebaseAuthentication.getInstance(context);
    }

    @Override
    public void onClick(View v) {
        if (smsCodeEditText.getText().toString().isEmpty()){
            Toasty.warning(context, "Please enter code!", Toasty.LENGTH_LONG).show();
        }
        else {
            firebaseAuthentication.verifyVerificationCode(smsCodeEditText.getText().toString(),
                    Business.Locator.getPreferences(context).getString("VERIFY_CODE", ""));
        }
    }
}
