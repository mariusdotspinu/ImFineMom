package Business.Listeners;

import android.view.View;

import java.util.List;

import Business.Facade.ContactFacadeImpl;
import Business.tasks.core.ContactDatabaseTask;
import UI.Adapters.ContactsAdapter;
import UI.Adapters.Holder;
import commons.dto.ContactDto;

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
        holder.getCheck().setChecked(!contacts.get(holder.getAdapterPosition()).isSelected());
        contacts.get(holder.getAdapterPosition()).setSelected(holder.getCheck().isChecked());
        new ContactDatabaseTask(this.getClass().getSimpleName(), contactFacade, contacts.get(holder.getAdapterPosition()),
                contactsAdapter).execute();
    }
}
