package UI.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import mspinu.imfinemom.R;;


public class Holder extends RecyclerView.ViewHolder {

    private TextView name;
    private TextView number;
    private LinearLayout contactLayout;
    private ImageView photo;
    private boolean isSending;
    private Context context;

    public Holder(View itemView) {
        super(itemView);
        context = itemView.getContext();
        name = itemView.findViewById(R.id.name);
        number = itemView.findViewById(R.id.number);
        contactLayout = itemView.findViewById(R.id.contact_layout);
        photo = itemView.findViewById(R.id.contact_photo);
    }

    public void setName(String name) {
        this.name.setText(name);
    }

    public void setNumber(String number) {
        this.number.setText(number);
    }

    public void setSending(boolean isSending) {
        this.isSending = isSending;
        TextView textView = contactLayout.findViewById(R.id.check);
        if (isSending){
            textView.setTextColor(this.contactLayout.getResources().getColor(R.color.colorSending));
            textView.setText("\u2713");
            textView.setTextSize(20);
        }
        else{
            textView.setTextColor(this.contactLayout.getResources().getColor(R.color.colorNotSending));
            textView.setText("Tap to select");
            textView.setTextSize(15);
        }
    }

    public boolean isSending() {
        return isSending;
    }

    public LinearLayout getContactLayout() {
        return contactLayout;
    }

    public ImageView getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        Glide.with(context)
                .asBitmap()
                .load(photo)
                .apply(RequestOptions.circleCropTransform())
                .into(this.photo);
    }
}

