package com.digitcreativestudio.safian.adadosen.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

/**
 * Created by faqih_000 on 11/8/2015.
 */
public class MyAlertDialog {

    public MyAlertDialog(Activity activity, String title, String message){
        AlertDialog alertDialog = new AlertDialog.Builder(activity).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
}
