package UI.Activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.google.i18n.phonenumbers.PhoneNumberUtil;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import Business.Listeners.CompleteListener;
import Business.Listeners.ResendListener;
import Business.Listeners.VerifyListener;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;
import mspinu.imfinemom.R;

public class OtpActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_layout);
        Map<String, String> countryCodesMap = getCountryCodesMap();
        LinearLayout mainLayout = findViewById(R.id.verify_main_layout);
        LinearLayout authenticationLayout = findViewById(R.id.code_authentication_layout);
        AppCompatSpinner spinner = findViewById(R.id.prefix_spinner);
        AppCompatButton verify = findViewById(R.id.otp_verify);
        AppCompatButton complete = findViewById(R.id.otp_complete);
        AppCompatButton resend = findViewById(R.id.otp_resend);
        AppCompatEditText verifyEditText = findViewById(R.id.edit_phoneNumber);
        AppCompatEditText smsCodeEditText = findViewById(R.id.edit_sms_code);
        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                new ArrayList<>(countryCodesMap.keySet()));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
        verify.setOnClickListener(new VerifyListener(this, spinner, verifyEditText,smsCodeEditText,
                countryCodesMap, mainLayout, authenticationLayout));
        complete.setOnClickListener(new CompleteListener(this, smsCodeEditText));
        resend.setOnClickListener(new ResendListener(this, smsCodeEditText));
    }

    private Map<String, String> getCountryCodesMap(){
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        Set<String> regions = phoneNumberUtil.getSupportedRegions();
        Map<String, String> regionPrefixMap = new TreeMap<>();
        for(String region : regions){
            String regionCountryCode = "+" + phoneNumberUtil.getCountryCodeForRegion(region);
            regionPrefixMap.put(region + "(" + regionCountryCode + ")", regionCountryCode);
        }
        return regionPrefixMap;
    }
}