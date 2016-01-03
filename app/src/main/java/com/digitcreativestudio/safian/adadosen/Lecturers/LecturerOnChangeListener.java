package com.digitcreativestudio.safian.adadosen.Lecturers;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.digitcreativestudio.safian.adadosen.R;
import com.digitcreativestudio.safian.adadosen.Utils.SessionManager;
import com.digitcreativestudio.safian.adadosen.Utils.Utils;

/**
 * Created by faqih_000 on 11/7/2015.
 */
public class LecturerOnChangeListener implements CompoundButton.OnCheckedChangeListener {
    private Context context;
    SessionManager session;
    private int mPosition;
    Dialog commentDialog;

    public LecturerOnChangeListener(Context context, int position, Dialog dialog){
        this.context = context; mPosition = position; commentDialog = dialog;
        session = new SessionManager(context.getApplicationContext());
    }
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        final int id = (int) compoundButton.getTag();
        final boolean status = b;
        commentDialog.show();
        commentDialog.findViewById(R.id.update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commentDialog.dismiss();
                Utils.checkLecturerUpdate(context, String.valueOf(id), String.valueOf(status), String.valueOf(mPosition), ((EditText) commentDialog.findViewById(R.id.comment)).getText().toString(), commentDialog);
                ((EditText)commentDialog.findViewById(R.id.comment)).setText("");
            }
        });
    }
}
