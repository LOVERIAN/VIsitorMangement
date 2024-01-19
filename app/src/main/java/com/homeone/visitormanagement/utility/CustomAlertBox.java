package com.homeone.visitormanagement.utility;

import android.app.Activity;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

public class CustomAlertBox {

    public static void ExitAppDialog(final Activity activity,String title, String message, String id)
    {
        AlertDialog.Builder alertbox = new AlertDialog.Builder(activity);
        alertbox.setTitle(title);
        alertbox.setMessage(message);
        alertbox.setPositiveButton("Yes", new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });
        alertbox.setNegativeButton("No", new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });
        alertbox.show();
    }
}
