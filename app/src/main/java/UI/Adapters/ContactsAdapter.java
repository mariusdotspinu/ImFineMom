package UI.Adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import Business.Facade.ContactFacadeImpl;
import Business.Listeners.CheckChangeListener;
import commons.dto.ContactDto;
import mspinu.imfinemom.R;


/**
 * Created by Marius on 8/10/2017.
 */

public class ContactsAdapter extends RecyclerView.Adapter<Holder> {

    private List<ContactDto> contacts;
    private Context context;
    private ContactFacadeImpl contactFacade;
    private LinkedList<View> views;

    public ContactsAdapter(ContactFacadeImpl contactFacade, Context context) {
        this.context = context;
        this.contactFacade = contactFacade;
        contacts = new ArrayList<>();
    }

    public void setViews(LinkedList<View> views){
        this.views = views;
    }

    public void changeView(){
        this.notifyDataSetChanged();
        if (!contacts.isEmpty()) {
            views.get(0).setVisibility(View.GONE);
            views.get(1).setVisibility(View.VISIBLE);
            ((RecyclerView)views.get(1)).setAdapter(this);
            views.get(2).setVisibility(View.VISIBLE);
        }
        else{
            views.get(0).setVisibility(View.VISIBLE);
            views.get(1).setVisibility(View.GONE);
            views.get(2).setVisibility(View.GONE);
        }
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

        final Holder holder = new Holder(view);
        final LinearLayout linearLayout = holder.getContactLayout();

        CheckChangeListener checkChangeListener = new CheckChangeListener(contacts, holder,
                contactFacade, this);

        linearLayout.setOnClickListener(checkChangeListener);
        holder.itemView.setOnClickListener(checkChangeListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.setPhoto(contacts.get(position).getPhotoBitmap());
        holder.setName(contacts.get(position).getName());
        holder.setNumber(contacts.get(position).getPhoneNumber());
        holder.setSending(contacts.get(position).isSelected());
    }
}


