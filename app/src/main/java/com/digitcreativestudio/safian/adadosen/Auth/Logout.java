package com.digitcreativestudio.safian.adadosen.Auth;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;


public class Logout extends AsyncTask<Void, Void, Void>{
    Activity mActivity;

    ProgressDialog pDialog;
    SessionManager session;

    public Logout(Activity activity){
        mActivity = activity;
        session = new SessionManager(mActivity.getApplicationContext());
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        pDialog = new ProgressDialog(mActivity);
        pDialog.setMessage("Loading..");
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        session.logoutUser();

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        pDialog.dismiss();

        //Toast.makeText(mActivity, "", Toast.LENGTH_SHORT);
        if(!session.isLoggedIn()){
            Intent intent = mActivity.getIntent();
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
            mActivity.finish();
            mActivity.startActivity(intent);

        }
    }
}
