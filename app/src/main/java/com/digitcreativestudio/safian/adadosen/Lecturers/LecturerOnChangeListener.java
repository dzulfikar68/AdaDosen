package com.digitcreativestudio.safian.adadosen.Lecturers;

import android.app.Activity;
import android.content.Intent;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.digitcreativestudio.safian.adadosen.LoginActivity;
import com.digitcreativestudio.safian.adadosen.Utils.SessionManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by faqih_000 on 11/7/2015.
 */
public class LecturerOnChangeListener implements CompoundButton.OnCheckedChangeListener {
    private Activity mActivity;
    SessionManager session;
    private int mPosition;

    public LecturerOnChangeListener(Activity activity, int position){
        mActivity = activity; mPosition = position;
        session = new SessionManager(mActivity.getApplicationContext());
    }
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        int id = (int) compoundButton.getTag();

        if(!session.isLoggedIn()){
            Intent intent = new Intent(mActivity, LoginActivity.class);
            mActivity.startActivity(intent);
            Toast.makeText(mActivity, "Anda Belum Login",Toast.LENGTH_SHORT).show();
        }else{
        String modifiedBy = (session.getUserDetails()).get(SessionManager.KEY_ID);
        boolean status = b;
        Date lastModify = new Date();

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        sdf1.setTimeZone(TimeZone.getTimeZone("GMT"));

        (new LecturerUpdate(mActivity)).execute(String.valueOf(id), String.valueOf(status), modifiedBy, sdf1.format(lastModify), String.valueOf(mPosition));
        }
    }
}
