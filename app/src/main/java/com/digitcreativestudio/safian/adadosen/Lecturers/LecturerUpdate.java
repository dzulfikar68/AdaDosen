package com.digitcreativestudio.safian.adadosen.Lecturers;

import android.app.Activity;
import android.os.AsyncTask;

/**
 * Created by faqih_000 on 11/7/2015.
 */
public class LecturerUpdate extends AsyncTask<String, Void, String> {
    Activity mActivity;

    public LecturerUpdate(Activity activity){
        mActivity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
