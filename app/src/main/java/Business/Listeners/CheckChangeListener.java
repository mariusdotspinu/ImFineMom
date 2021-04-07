package Business.Listeners;

import android.view.View;
import android.widget.TextView;

import java.util.List;

import Business.Facade.ContactFacadeImpl;
import Business.tasks.core.ContactDatabaseTask;
import UI.Adapters.ContactsAdapter;
import UI.Adapters.Holder;
import commons.dto.ContactDto;
import mspinu.imfinemom.R;

public class CheckChangeListener implements View.OnClickListener {
    private List<ContactDto> contacts;
    private Holder holder;
    private ContactFacadeImpl contactFacade;
    private ContactsAdapter contactsAdapter;

    public CheckChangeListener(List<ContactDto> contacts, Holder holder, ContactFacadeImpl contactFacade, ContactsAdapter contactsAdapter){
        this.contacts = contacts;
        this.holder = holder;
        this.contactFacade = contactFacade;
        this.contactsAdapter = contactsAdapter;
    }

    @Override
    public void onClick(View view) {
        if (((TextView)view.findViewById(R.id.check)).getText().equals("Tap to select")){
            holder.setSending(true);
            contacts.get(holder.getAdapterPosition()).setSelected(true);
            new ContactDatabaseTask(this.getClass().getSimpleName(), contactFacade, contacts.get(holder.getAdapterPosition()),
                    contactsAdapter).execute();
        }
        else{
            holder.setSending(false);
            contacts.get(holder.getAdapterPosition()).setSelected(false);
            new ContactDatabaseTask(this.getClass().getSimpleName(), contactFacade, contacts.get(holder.getAdapterPosition()),
                    contactsAdapter).execute();
        }
    }
}
