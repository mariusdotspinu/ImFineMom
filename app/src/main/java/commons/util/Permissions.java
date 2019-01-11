package commons.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import UI.util.DialogUtils;

import static commons.util.Constants.PERMISSIONS;

/**
 * Created by Marius on 8/13/2017.
 */

public class Permissions {
    private static final int PERMISSION_REQUEST_CODE = 0;
    public static final int PERMISSIONS_ALLOWED_RUNTIME = 1;
    public static final int PERMISSIONS_DENIED = 2;
    public static final int PERMISSIONS_ALLOWED_IMPLICIT = 3;

    private Context context;
    private ArrayList<String> listPermissionsNeeded=new ArrayList<>();
    private String dialog_content="";

    public Permissions(Context context)
    {
        this.context=context;

    }


    public int checkPermission(String dialog_content)
    {
        this.dialog_content=dialog_content;

        boolean permission;
        if(Build.VERSION.SDK_INT >= 23) {
            permission = checkAndRequestPermissions(PERMISSIONS);

            if (permission) {
                return PERMISSIONS_ALLOWED_RUNTIME;
            } else {
                return PERMISSIONS_DENIED;
            }
        }
        return PERMISSIONS_ALLOWED_IMPLICIT;
    }


    private boolean checkAndRequestPermissions(String[] permissions) {

        if(permissions.length > 0)
        {
            listPermissionsNeeded = new ArrayList<>();

            for(String permission : permissions)
            {
                int hasPermission = ContextCompat.checkSelfPermission(context, permission);

                if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(permission);
                }

            }
            if (!listPermissionsNeeded.isEmpty())
            {
                ActivityCompat.requestPermissions((Activity) context, listPermissionsNeeded
                        .toArray(new String[listPermissionsNeeded.size()]),PERMISSION_REQUEST_CODE);
                return false;
            }
        }

        return true;
    }
    public int onRequestPermissionsResult(String permissions[], int[] grantResults) {

                if (grantResults.length > 0) {
                    Map<String, Integer> perms = new HashMap<>();

                    for (int i = 0; i < permissions.length; i++) {
                        perms.put(permissions[i], grantResults[i]);
                    }

                    final ArrayList<String> pending_permissions = new ArrayList<>();
                    for (int i = 0; i < listPermissionsNeeded.size(); i++) {

                        if (perms.get(listPermissionsNeeded.get(i)) != PackageManager.PERMISSION_GRANTED) {

                            if (ActivityCompat.shouldShowRequestPermissionRationale(
                                    (Activity)context, listPermissionsNeeded.get(i)))
                                pending_permissions.add(listPermissionsNeeded.get(i));

                            else {

                                Toast.makeText(context, "Go to settings and enable permissions",
                                        Toast.LENGTH_LONG).show();

                                return PERMISSIONS_DENIED;
                            }
                        }

                    }

                    if (pending_permissions.size() > 0) {
                        DialogUtils.showRationaleDialog(dialog_content,context,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        switch (which) {
                                            case DialogInterface.BUTTON_POSITIVE:
                                                checkPermission(dialog_content);
                                                break;
                                            case DialogInterface.BUTTON_NEGATIVE:
                                                //not all permissions allowed
                                        }

                                    }
                                });

                    } else {

                        return PERMISSIONS_ALLOWED_RUNTIME;
                    }
                }
                return PERMISSIONS_DENIED;
        }


}
