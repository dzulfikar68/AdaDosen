package com.digitcreativestudio.safian.adadosen.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

/**
 * Created by faqih_000 on 11/8/2015.
 */
public class MyAlertDialog {
    private String mTitle, mMessage, mButton;
    private Activity mActivity;

    public MyAlertDialog(Activity activity, String title, String message){
        mActivity = activity; mTitle = title; mMessage = message; mButton = "OK";
    }

    public MyAlertDialog(Activity activity, String title, String message, String button){
        mActivity = activity; mTitle = title; mMessage = message; mButton = "Try Again";
    }

    private void createDialog(){
        AlertDialog alertDialog = new AlertDialog.Builder(mActivity).create();

        // Setting Dialog Title
        alertDialog.setTitle(mTitle);

        // Setting Dialog Message
        alertDialog.setMessage(mMessage);

        // Setting OK Button
        alertDialog.setButton(mButton, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (!mButton.equals("OK")) {
                    Intent intent = mActivity.getIntent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    mActivity.finish();
                    mActivity.startActivity(intent);
                }
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
}
