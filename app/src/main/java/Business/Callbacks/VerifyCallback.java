package Business.Callbacks;

import android.content.Context;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import Business.Services.util.FirebaseAuthentication;
import androidx.annotation.NonNull;
import es.dmoral.toasty.Toasty;

public class VerifyCallback extends PhoneAuthProvider.OnVerificationStateChangedCallbacks {

    private Context context;
    private String verificationCode;
    private EditText smsEditText;
    private FirebaseAuthentication firebaseAuthentication;

    public VerifyCallback(Context context, EditText smsEditText){
        this.context = context;
        this.smsEditText = smsEditText;
        firebaseAuthentication = FirebaseAuthentication.getInstance(context);
    }

    @Override
    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
        String code = phoneAuthCredential.getSmsCode();
        if (code != null) {
            smsEditText.setText(code);
            firebaseAuthentication.verifyVerificationCode(code, verificationCode);
        }
    }

    @Override
    public void onVerificationFailed(FirebaseException e) {
        if (e.getMessage().contains("The format")){
            Toasty.warning(context, "The phone number entered is invalid!", Toast.LENGTH_LONG)
                    .show();
        }
        else {
            Toasty.error(context, "Verification failed", Toast.LENGTH_LONG).show();
        }
        Log.d("firebase-verification", e.getMessage());
    }

    @Override
    public void onCodeSent(@NonNull String verificationCode, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
        super.onCodeSent(verificationCode, forceResendingToken);
        this.verificationCode = verificationCode;
        Business.Locator.getPreferences(context).edit().putString("VERIFY_CODE", verificationCode).apply();
    }
}