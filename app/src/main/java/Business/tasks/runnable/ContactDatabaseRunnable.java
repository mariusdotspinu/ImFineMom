package Business.tasks.runnable;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.os.Parcelable;
import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.List;

import Business.Facade.ContactFacadeImpl;
import Business.tasks.handler.ContactDatabaseHandler;
import UI.Activities.MainActivity;
import commons.dto.ContactDto;

import static commons.util.Constants.ADD;
import static commons.util.Constants.ADD_CONTACT;
import static commons.util.Constants.DELETE;
import static commons.util.Constants.DELETE_CONTACT;
import static commons.util.Constants.GET;
import static commons.util.Constants.GET_ALL_CONTACTS;
import static commons.util.Constants.KEY_ADD;
import static commons.util.Constants.KEY_DELETE;
import static commons.util.Constants.KEY_GET;
import static commons.util.Constants.KEY_UPDATE;
import static commons.util.Constants.UPDATE;
import static commons.util.Constants.UPDATE_CONTACT;

public class ContactDatabaseRunnable implements Runnable {
    private Context context;
    private AlertDialog progressBar;
    private ContactDto contactDto;
    private List<ContactDto> contactDtos;
    private ContactDatabaseHandler handler;
    private ContactFacadeImpl contactFacade;
    private String contextClassName;
    private String bundleKey;


    public ContactDatabaseRunnable(String contextClassName, ContactDatabaseHandler handler,
                                   ContactFacadeImpl contactFacade) {
        this.context = handler.getContext();
        this.contactFacade = contactFacade;
        this.handler = handler;
        this.contextClassName = contextClassName;
    }

    public void setProgressBar(AlertDialog progressBar) {
        this.progressBar = progressBar;
    }

    public void setContactDto(ContactDto contactDto) {
        this.contactDto = contactDto;
    }

    @Override
    public void run() {
        ((MainActivity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.show();
            }
        });
        callDbCrudOperationBasedOnContext();
        postExecute();
    }

    private void callDbCrudOperationBasedOnContext() {
        switch (contextClassName) {
            case GET_ALL_CONTACTS:
                contactDtos = contactFacade.getAllContacts();
                buildBundleKey(GET);
                break;
            case ADD_CONTACT:
                long id = contactFacade.addContact(contactDto);
                contactDto.setId(id);
                buildBundleKey(ADD);
                break;
            case DELETE_CONTACT:
                contactFacade.deleteContact(contactDto);
                buildBundleKey(DELETE);
                break;
            case UPDATE_CONTACT:
                contactFacade.updateContact(contactDto.getId(), contactDto.isSelected());
                buildBundleKey(UPDATE);
                break;
            default:
                break;
        }
    }

    private void buildBundleKey(final String crudCase) {
        switch (crudCase) {
            case GET:
                bundleKey = KEY_GET;
                break;
            case ADD:
                bundleKey = KEY_ADD;
                break;
            case DELETE:
                bundleKey = KEY_DELETE;
                break;
            case UPDATE:
                bundleKey = KEY_UPDATE;
                break;
            default:
                break;
        }
    }

    private void postExecute() {
        handler.setProgressBar(progressBar);
        handler.sendMessage(getMessageFromContactsBundle());
    }

    private Message getMessageFromContactsBundle() {
        Message message = Message.obtain();
        Bundle contactBundle = new Bundle();
        switch (bundleKey) {
            case KEY_GET:
                contactBundle.putParcelableArrayList(bundleKey, (ArrayList<? extends Parcelable>) contactDtos);
                break;
            case KEY_DELETE:
                contactBundle.putParcelable(bundleKey, contactDto);
                break;
            default:
                contactBundle.putParcelable(bundleKey, contactDto);
                break;
        }
        message.setData(contactBundle);
        return message;
    }
}
