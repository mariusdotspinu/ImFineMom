package UI.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.google.android.gms.common.api.Api;

import java.util.ArrayList;
import java.util.List;

import Business.Facade.ContactFacadeImpl;
import Business.Listeners.CheckChangeListener;
import Business.tasks.core.ContactDatabaseTask;
import commons.dto.ContactDto;
import mspinu.imfinemom.R;

import static commons.util.Constants.UPDATE_CONTACT;

/**
 * Created by Marius on 8/10/2017.
 */

public class ContactsAdapter extends RecyclerView.Adapter<Holder>{

    private List<ContactDto> contacts;
    private Context context;
    private ContactFacadeImpl contactFacade;

    public ContactsAdapter(ContactFacadeImpl contactFacade, Context context){
        this.context = context;
        this.contactFacade = contactFacade;
        contacts = new ArrayList<>();
    }

    public Context getContext() {
        return context;
    }

    public void setContacts(List<ContactDto> contacts) {
        this.contacts = contacts;
    }

    public List<ContactDto> getContacts() {
        return contacts;
    }

    @Override
    public long getItemId(int position) {
        return contacts.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_layout,
                parent, false);
        final Holder holder =  new Holder(view);
        final CheckBox check = holder.getCheck();
        CheckChangeListener checkChangeListener = new CheckChangeListener(contacts , holder,
                contactFacade, this);

        check.setOnClickListener(checkChangeListener);
        holder.itemView.setOnClickListener(checkChangeListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {

        holder.setName(contacts.get(position).getName());
        holder.setNumber(contacts.get(position).getPhoneNumber());
        holder.setChecked(contacts.get(position).isSelected());
    }
}


