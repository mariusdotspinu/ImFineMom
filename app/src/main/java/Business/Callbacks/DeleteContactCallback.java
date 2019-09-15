package Business.Callbacks;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.view.View;

import Business.Facade.ContactFacadeImpl;
import Business.tasks.core.ContactDatabaseTask;
import UI.Adapters.ContactsAdapter;
import commons.dto.ContactDto;
import mspinu.imfinemom.R;

public class DeleteContactCallback{
    private ItemTouchHelper.SimpleCallback simpleTouchCallback;

    public DeleteContactCallback(final Context context, final ContactFacadeImpl contactFacade, final ContactsAdapter contactsAdapter){

        simpleTouchCallback = new ItemTouchHelper.SimpleCallback(
                0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView,
                                    RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                    int actionState, boolean isCurrentlyActive) {
                Drawable icon = ContextCompat.getDrawable(context, R.drawable.ic_delete_sweep);
                ColorDrawable background = new ColorDrawable(context.getResources()
                        .getColor(R.color.colorDeleteAction));
                setBounds(viewHolder, icon, background, (int) dX);
                background.draw(c);
                icon.draw(c);
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

            private void setBounds(RecyclerView.ViewHolder viewHolder, Drawable icon,
                                   ColorDrawable background, int dX){
                View itemView = viewHolder.itemView;
                int itemHeight = itemView.getBottom() - itemView.getTop();
                int iconHeight = icon.getIntrinsicHeight();
                int iconWidth = icon.getIntrinsicWidth();
                int iconTop = itemView.getTop() + (itemHeight - iconHeight) / 2;
                int iconMargin = (itemHeight - iconHeight) / 2;
                int iconBottom = iconTop + iconHeight;
                if (dX > 0) {
                    background.setBounds(0, itemView.getTop(), itemView.getRight() + dX, itemView.getBottom());
                    icon.setBounds(itemView.getLeft() + iconMargin + iconWidth, iconTop, itemView.getLeft() + iconMargin, iconBottom);
                }
                else {
                    background.setBounds(itemView.getRight() - dX, itemView.getTop(), 0 , itemView.getBottom());
                    icon.setBounds(itemView.getRight() - iconMargin - iconWidth, iconTop, itemView.getRight() - iconMargin, iconBottom);
                }
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                deleteItem(this.getClass().getEnclosingClass().getSimpleName(), contactFacade,
                        contactsAdapter.getContacts().get(position),contactsAdapter);
            }
        };
    }

    private void deleteItem(String className, ContactFacadeImpl contactFacade, ContactDto contactDto, ContactsAdapter contactsAdapter) {
        new ContactDatabaseTask(className, contactFacade, contactDto,
                contactsAdapter).execute();
    }

    public ItemTouchHelper.Callback getDeleteSimpleCallback(){
        return simpleTouchCallback;
    }

}
