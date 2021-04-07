package Business.Services.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import Business.Callbacks.VerifyCallback;
import Business.Locator;
import UI.Activities.MainActivity;
import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;

import static commons.util.Constants.FIRST_STARTUP;

public class FirebaseAuthentication {

    private static FirebaseAuthentication firebaseAuthentication;

    private static FirebaseAuth firebaseAuthenticator;
    private Context context;

    private FirebaseAuthentication(Context context){
        this.context = context;
    }

    public static FirebaseAuthentication getInstance(Context context){
        if (firebaseAuthentication == null){
            firebaseAuthentication = new FirebaseAuthentication(context);
            firebaseAuthenticator = FirebaseAuth.getInstance();
        }
        return firebaseAuthentication;
    }

    public void sendVerificationCode(Context context, String phoneNumber, EditText smsCodeEditText) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                new VerifyCallback(context, smsCodeEditText));
    }

    public void verifyVerificationCode(String code, String verificationCode) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuthenticator.signInWithCredential(credential)
                .addOnCompleteListener((AppCompatActivity)context, verifyTask -> {
                    if (verifyTask.isSuccessful()) {
                        Locator.getPreferences(context).edit().putBoolean(FIRST_STARTUP, false).apply();
                        Intent intent = new Intent(context, MainActivity.class);
                        context.startActivity(intent);
                        ((AppCompatActivity)context).finish();
                        Toasty.success(context, "Successfully registered!", Toasty.LENGTH_LONG)
                                .show();
                        Log.d("saved-phone-number",
                                Business.Locator.getPreferences(context).getString("VERIFY-PHONE-NUMBER", ""));
                        Log.d("first-startup-value",
                                String.valueOf(Locator.getPreferences(context).getBoolean(FIRST_STARTUP, true)));
                    }
                    else {
                        if (verifyTask.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            Toasty.error(context, "Invalid code!", Toast.LENGTH_LONG).show();
                            Log.d("invalid-code", verifyTask.getException().getMessage());
                        }
                    }
                });
    }
}
