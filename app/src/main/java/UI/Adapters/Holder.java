package UI.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import mspinu.imfinemom.R;


public class Holder extends RecyclerView.ViewHolder {

    private TextView name;
    private TextView number;
    private CheckBox check;

    public Holder(View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.name);
        number = itemView.findViewById(R.id.number);
        check = itemView.findViewById(R.id.check);

    }

    public void setName(String name) {
        this.name.setText(name);
    }

    public void setNumber(String number) {
        this.number.setText(number);
    }

    public void setChecked(boolean checked) {
        this.check.setChecked(checked);
    }

    public CheckBox getCheck() {
        return check;
    }
}

