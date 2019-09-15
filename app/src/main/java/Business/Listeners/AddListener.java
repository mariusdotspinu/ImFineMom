package Business.Listeners;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.provider.ContactsContract;
import com.google.android.material.snackbar.Snackbar;
import android.text.Html;
import android.view.View;
import android.widget.Toast;

import UI.Activities.MainActivity;
import UI.Adapters.ContactsAdapter;
import mspinu.imfinemom.R;

import static commons.util.Constants.CONTACT_REQ_CODE;
import static commons.util.Constants.MAX_SIZE;
import static commons.util.Constants.SNACKBAR_MESSAGE;

public class AddListener implements View.OnClickListener {
    private Context context;
    private ContactsAdapter contactsAdapter;
    private boolean pressed;

    public AddListener(Context context, ContactsAdapter contactsAdapter) {
        this.context = context;
        this.contactsAdapter = contactsAdapter;
    }

    @Override
    public void onClick(View v) {
        Resources resources = context.getResources();
        if (MainActivity.permissionsGranted) {
            if (contactsAdapter.getContacts().size() < MAX_SIZE) {
                startAddingContactIntent();
            } else {
                Snackbar.make(v, Html.fromHtml(String.format(SNACKBAR_MESSAGE,
                        resources.getString(R.string.max_contact_size_reached))), Snackbar.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(context,
                    "One or more permissions denied ! All must be allowed for the app the be properly used.", Toast.LENGTH_LONG).show();
        }
    }

    private void startAddingContactIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        ((Activity) context).startActivityForResult(intent, CONTACT_REQ_CODE);
        pressed = true;
    }

    public boolean isPressed() {
        return pressed;
    }

    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }
}