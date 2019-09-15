package UI.util;

import android.content.Context;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import android.view.View;
import android.widget.ProgressBar;

import mspinu.imfinemom.R;

public abstract class DialogUtils {

    public static void showRationaleDialog(String message, Context context,
                                 DialogInterface.OnClickListener clickListener) {
        new AlertDialog.Builder(context)
            .setMessage(message)
            .setPositiveButton(R.string.OK, clickListener)
            .create()
            .show();
    }

    public static void showSimpleDialog(Context context, String message, String title, DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener cancelListener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setPositiveButton(R.string.OK, okListener);
        builder.setNegativeButton(R.string.CANCEL, cancelListener);
        builder.setTitle(title);
        AlertDialog dialog = builder.create();
        dialog.setMessage(message);
        dialog.show();
    }

    public static AlertDialog getProgressBarDialog(ProgressBar progressBar) {
        AlertDialog dialog = buildDialog(progressBar.getContext());
        progressBar.setVisibility(View.VISIBLE);
        dialog.setView(progressBar);
        dialog.setTitle(R.string.progress_bar_title);

        return dialog;
    }

    private static AlertDialog buildDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context,
                R.style.Theme_AppCompat_DayNight_Dialog);
        return builder.create();
    }

}
