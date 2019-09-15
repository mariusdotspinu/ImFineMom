package Business.tasks.handler;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AlertDialog;

import java.util.List;

import UI.Adapters.ContactsAdapter;
import commons.dto.ContactDto;

import static commons.util.Constants.KEY_ADD;
import static commons.util.Constants.KEY_DELETE;
import static commons.util.Constants.KEY_GET;
import static commons.util.Constants.KEY_UPDATE;

public class ContactDatabaseHandler extends Handler {
    private Context context;
    private AlertDialog progressBar;
    private ContactsAdapter contactsAdapter;

    public ContactDatabaseHandler(Context context, ContactsAdapter contactsAdapter) {
        this.context = context;
        this.contactsAdapter = contactsAdapter;
    }

    public void setProgressBar(AlertDialog progressBar) {
        this.progressBar = progressBar;
    }

    public Context getContext() {
        return context;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        Bundle contactBundle = msg.getData();
        postExecute(contactBundle);
    }

    private void postExecute(Bundle contactBundle) {
        refreshContactAdapter(contactBundle);
        contactsAdapter.changeView();
        progressBar.dismiss();
    }

    private void refreshContactAdapter(Bundle contactBundle) {

        if (contactBundle.containsKey(KEY_GET)) {
            contactsAdapter.setContacts(contactBundle.<ContactDto>getParcelableArrayList(KEY_GET));
        } else if (contactBundle.containsKey(KEY_ADD)) {
            contactsAdapter.getContacts().add((ContactDto) contactBundle.getParcelable(KEY_ADD));
        } else if (contactBundle.containsKey(KEY_DELETE)) {
            deleteContact(contactsAdapter.getContacts(), (ContactDto) contactBundle.getParcelable(KEY_DELETE));
        } else {
            updateContact(contactsAdapter.getContacts(), (ContactDto) contactBundle.getParcelable(KEY_UPDATE));
        }
    }

    private void deleteContact(List<ContactDto> contactDtos, ContactDto deletedContact) {
        contactDtos.remove(deletedContact);
    }

    private void updateContact(List<ContactDto> contactDtos, ContactDto contactDto) {
        for (ContactDto contactToBeReplaced : contactDtos) {
            if (contactToBeReplaced.getId() == contactDto.getId()) {
                contactDtos.set(contactDtos.indexOf(contactToBeReplaced), contactDto);
            }
        }
    }
}
