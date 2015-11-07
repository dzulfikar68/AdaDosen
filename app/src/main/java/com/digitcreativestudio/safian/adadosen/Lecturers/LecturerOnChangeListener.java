package com.digitcreativestudio.safian.adadosen.Lecturers;

import android.app.Activity;
import android.widget.CompoundButton;

import com.digitcreativestudio.safian.adadosen.Auth.SessionManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

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
        int id = (int) compoundButton.getTag();
        String modifiedBy = (session.getUserDetails()).get(SessionManager.KEY_ID);
        boolean status = b;
        Date lastModify = new Date();

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        sdf1.setTimeZone(TimeZone.getTimeZone("GMT"));

        (new LecturerUpdate(mActivity)).execute(String.valueOf(id), String.valueOf(status), modifiedBy, sdf1.format(lastModify));

    }
}
