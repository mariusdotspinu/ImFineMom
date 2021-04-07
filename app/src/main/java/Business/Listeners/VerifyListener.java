package Business.Listeners;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Map;

import Business.Services.util.FirebaseAuthentication;
import UI.util.CommonUtils;
import androidx.appcompat.widget.AppCompatSpinner;
import es.dmoral.toasty.Toasty;

public class VerifyListener implements View.OnClickListener {
    private Context context;
    private EditText verifyEditText;
    private EditText smsCodeEditText;
    private LinearLayout mainLayout;
    private LinearLayout authenticationLayout;
    private AppCompatSpinner spinner;
    private Map<String, String> countryCodesMap;
    private FirebaseAuthentication firebaseAuthentication;

    public VerifyListener(Context context, AppCompatSpinner spinner, EditText verifyEditText,
                          EditText smsCodeEditText,
                          Map<String, String> countryCodesMap,
                          LinearLayout mainLayout,
                          LinearLayout authenticationLayout){
        this.context = context;
        this.verifyEditText = verifyEditText;
        this.smsCodeEditText = smsCodeEditText;
        this.spinner = spinner;
        this.countryCodesMap = countryCodesMap;
        this.mainLayout = mainLayout;
        this.authenticationLayout = authenticationLayout;
        firebaseAuthentication = FirebaseAuthentication.getInstance(context);
    }

    @Override
    public void onClick(View v) {
        String phoneNumber = countryCodesMap.get(spinner.getSelectedItem().toString())
                + verifyEditText.getText();
        if (phoneNumber.isEmpty() || !CommonUtils.isValid(phoneNumber)) {
            Toasty.warning(context, "Please provide a valid phone number", Toast.LENGTH_SHORT).show();
        }
        else {
            mainLayout.setVisibility(View.GONE);
            authenticationLayout.setVisibility(View.VISIBLE);
            Business.Locator.getPreferences(context).edit().putString("VERIFY_PHONE_NUMBER", phoneNumber)
                    .apply();
            firebaseAuthentication.sendVerificationCode(context, phoneNumber, smsCodeEditText);
        }
    }
}