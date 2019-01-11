package Business.Listeners;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import UI.Activities.MainActivity;
import UI.Adapters.ContactsAdapter;
import commons.util.Permissions;
import mspinu.imfinemom.R;

import static commons.util.Constants.CONTACT_REQ_CODE;
import static commons.util.Constants.MAX_SIZE;

public class FabListener implements View.OnClickListener {
    private Context context;
    private ContactsAdapter contactsAdapter;

    public FabListener(Context context, ContactsAdapter contactsAdapter) {
        this.context = context;
        this.contactsAdapter = contactsAdapter;
    }

    @Override
    public void onClick(View v) {
        View rootView= v.getRootView();
        if (MainActivity.isPermitted == Permissions.PERMISSIONS_ALLOWED_RUNTIME ||
                MainActivity.isPermitted == Permissions.PERMISSIONS_ALLOWED_IMPLICIT) {

            if (contactsAdapter.getContacts().size() < MAX_SIZE) {
                startAddingContactIntent();
            } else {
                Snackbar.make(rootView, R.string.max_contact_size_reached,
                        Snackbar.LENGTH_SHORT).show();
            }
        } else {
            Snackbar.make(rootView, R.string.permission_denied,
                    Snackbar.LENGTH_SHORT).show();
        }
    }

    private void startAddingContactIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        ((Activity) context).startActivityForResult(intent, CONTACT_REQ_CODE);
    }
}
