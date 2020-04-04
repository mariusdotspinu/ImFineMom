package Business.tasks.core;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;

import android.widget.ProgressBar;

import Business.Facade.ContactFacadeImpl;
import Business.tasks.handler.ContactDatabaseHandler;
import Business.tasks.runnable.ContactDatabaseRunnable;
import UI.Adapters.ContactsAdapter;
import UI.util.DialogUtils;
import commons.dto.ContactDto;
import mspinu.imfinemom.R;

public class ContactDatabaseTask {
    private ProgressBar progressBar;
    private ContactDatabaseRunnable contactRunnable;


    public ContactDatabaseTask(String contextClassName, ContactFacadeImpl contactFacade,
                               ContactDto contactDto, ContactsAdapter contactsAdapter) {
        this(contextClassName, contactFacade, contactsAdapter);
        this.contactRunnable.setContactDto(contactDto);

    }

    private ContactDatabaseTask(String contextClassName, ContactFacadeImpl contactFacade,
                                ContactsAdapter contactsAdapter) {
        Context context = contactsAdapter.getContext();
        this.progressBar = new ProgressBar(context);
        this.contactRunnable = new ContactDatabaseRunnable(contextClassName,
                new ContactDatabaseHandler(context, contactsAdapter), contactFacade);
    }


    public void execute() {
        preExecute();
        new Thread(contactRunnable).start();
    }

    private void preExecute() {
        AlertDialog progressBarDialog = DialogUtils.getProgressBarDialog(progressBar, R.string.progress_bar_loading);
        contactRunnable.setProgressBar(progressBarDialog);
    }
}
