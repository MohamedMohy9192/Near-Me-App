package com.androideradev.www.nearme.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;

import com.androideradev.www.nearme.R;

public class PermissionUtilities {

    public static final int REQUEST_ACCESS_FINE_LOCATION = 1;

    public static void requestFineLocationPermission(Activity activity) {

        ActivityCompat.requestPermissions(activity,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_ACCESS_FINE_LOCATION);

    }

    public static void showPermissionExplanationDialog(final Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage(R.string.permission_explanation_dialog_message)
                .setTitle(R.string.permission_explanation_dialog_title)
                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Activity activity = (Activity) context;
                        requestFineLocationPermission(activity);
                        dialog.dismiss();

                    }
                })
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    public static boolean shouldShowPermissionExplanationDialog(Activity activity) {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
    }
}
