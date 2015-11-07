package com.digitcreativestudio.safian.adadosen.Lecturers;

import android.app.Activity;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.digitcreativestudio.safian.adadosen.Auth.SessionManager;
import com.digitcreativestudio.safian.adadosen.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by faqih_000 on 11/7/2015.
 */
public class LecturerOnChangeListener implements CompoundButton.OnCheckedChangeListener {
    private Activity mActivity;
    SessionManager session;

    public LecturerOnChangeListener(Activity activity){
        mActivity = activity;
        session = new SessionManager(mActivity.getApplicationContext());
    }
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        int id_lecturer = (int) compoundButton.getTag();
        String id_user = (session.getUserDetails()).get(SessionManager.KEY_ID);
        boolean status = b;
        Date lastModify = new Date();

        Log.e("Updated", id_lecturer+" By: "+id_user+" At: "+lastModify+" To: "+status);
        LinearLayout parent = (LinearLayout) (compoundButton.getParent()).getParent();

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd-MM-yyyy");

        ((TextView) parent.findViewById(R.id.lastModify)).setText(sdf.format(lastModify));
        ((TextView) parent.findViewById(R.id.modifiedBy)).setText((session.getUserDetails()).get(SessionManager.KEY_NAME));

        compoundButton.setOnCheckedChangeListener(null);
        compoundButton.setChecked(status);
        compoundButton.setOnCheckedChangeListener(new LecturerOnChangeListener(mActivity));
    }
}
