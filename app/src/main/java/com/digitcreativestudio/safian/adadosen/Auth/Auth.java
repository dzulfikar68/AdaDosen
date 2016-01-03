package com.digitcreativestudio.safian.adadosen.Auth;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.digitcreativestudio.safian.adadosen.MainActivity;
import com.digitcreativestudio.safian.adadosen.Utils.MyAlertDialog;
import com.digitcreativestudio.safian.adadosen.Utils.SessionManager;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


public class Auth extends AsyncTask<String, Void, String> {
    Activity mActivity;

    ProgressDialog pDialog;

    SessionManager session;

    private boolean success;

    public Auth(Activity activity){
        mActivity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        session = new SessionManager(mActivity.getApplicationContext());
        pDialog = new ProgressDialog(mActivity);
        pDialog.setMessage("Loading..");
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    @Override
    protected String doInBackground(String... params) {
        String responseString = "";
        InputStream response = null;
        int timeout=10000; //in milisecond = 10 detik
        HttpURLConnection urlConnection = null;
        String charset = "UTF-8";

        try {

            String query = String.format("nim=%s&password=%s&token=%s",
                    URLEncoder.encode(params[0], charset),
                    URLEncoder.encode(params[1], charset),
                    URLEncoder.encode(params[2], charset));

            URL url = new URL("http://api.arifian.com/AdaDosen/user/auth");
            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Accept-Charset", charset);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
            urlConnection.setConnectTimeout(timeout);

            OutputStream output = urlConnection.getOutputStream();
            output.write(query.getBytes(charset));

            response = urlConnection.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    response));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

            responseString = sb.toString();

            JSONObject jObj = new JSONObject(responseString);
            String id = jObj.getString(SessionManager.KEY_ID);
            success = jObj.getBoolean("success");
            if(success){
                String nim = jObj.getString(SessionManager.KEY_NIM);
                String name = jObj.getString(SessionManager.KEY_NAME);

                session.createLoginSession(id, nim, name);
            }

            String message = jObj.getString("message");
            return message;
        } catch (Exception e) {
            e.printStackTrace();

            return "Exception Caught";
        } finally{
            urlConnection.disconnect();
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        pDialog.dismiss();

        if(s.equalsIgnoreCase("Exception Caught"))
        {
            Toast.makeText(mActivity, "Connection timeout.", Toast.LENGTH_SHORT).show();
        }else{
            if(success){
                Toast.makeText(mActivity, s, Toast.LENGTH_SHORT).show();
                if(session.isLoggedIn()){
                    Intent i = new Intent(mActivity, MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    mActivity.startActivity(i);
                }
            }else{
                new MyAlertDialog(mActivity, "Login gagal", s);
            }
        }


    }
}
