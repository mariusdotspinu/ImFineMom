package UI.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ProgressBar;

import commons.util.ResourceUtils;
import mspinu.imfinemom.R;

public abstract class DialogUtils {

    public static void showRationaleDialog(String message, Context context,
                                 DialogInterface.OnClickListener clickListener) {
        new AlertDialog.Builder(context)
            .setMessage(message)
            .setPositiveButton(R.string.cast_tracks_chooser_dialog_ok, clickListener)
            .setNegativeButton(R.string.cast_tracks_chooser_dialog_cancel, clickListener)
            .create()
            .show();
    }

    public static void showAboutDialog(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setPositiveButton(R.string.cast_tracks_chooser_dialog_ok,
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        builder.setTitle(R.string.aboutButton);
        AlertDialog dialog = builder.create();
        dialog.setMessage(ResourceUtils.getStringFrom(context, R.string.about_message));
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
