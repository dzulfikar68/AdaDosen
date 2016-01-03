package com.digitcreativestudio.safian.adadosen.Lecturers;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.digitcreativestudio.safian.adadosen.Data.DBContract;
import com.digitcreativestudio.safian.adadosen.MainActivity;
import com.digitcreativestudio.safian.adadosen.R;
import com.digitcreativestudio.safian.adadosen.Utils.SessionManager;
import com.digitcreativestudio.safian.adadosen.Utils.Utils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by faqih_000 on 11/7/2015.
 */
public class LecturerUpdate extends AsyncTask<String, Void, String> {
    private Context mActivity;
    private ProgressDialog pDialog;
    private String id, status, modifiedBy, position;
    private boolean success = false;
    private SessionManager session;
    private Dialog commentDialog;

    public LecturerUpdate(Context activity, Dialog commentDialog){
        mActivity = activity;
        session = new SessionManager(mActivity.getApplicationContext());
        this.commentDialog = commentDialog;
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
    protected String doInBackground(String... params) {
        id = params[0];
        status = params[1].equals("true") ? "1" : "0";
        modifiedBy = params[2];
        position = params[3];

        String responseString = "";
        InputStream response = null;
        int timeout=10000; //in milisecond = 10 detik
        HttpURLConnection urlConnection = null;
        String charset = "UTF-8";

        try {
            String query = String.format("id=%s&status=%s&modified_by=%s&token=%s&comment=%s",
                    URLEncoder.encode(id, charset),
                    URLEncoder.encode(status, charset),
                    URLEncoder.encode(modifiedBy, charset),
                    URLEncoder.encode(session.getToken(), charset),
                    URLEncoder.encode(params[4], charset));

            URL url = new URL("http://api.arifian.com/AdaDosen/lecturer/update");
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
            success = jObj.getBoolean("success");
            JSONObject update = jObj.getJSONObject("update");

            ContentValues cv = Utils.parseJsonLecturer(update);

            mActivity.getContentResolver().update(DBContract.LecturerEntry.CONTENT_URI, cv, DBContract.LecturerEntry._ID + " = ?", new String[]{update.getString("id")});
            Intent i = new Intent();
            i.setAction(MainActivity.ACTION_REFRESH);
            mActivity.sendBroadcast(i);
            return jObj.getString("message");
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
            Toast.makeText(mActivity, "Terjadi Kesalahan.", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(mActivity, s, Toast.LENGTH_SHORT).show();

        LinearLayout parent = (LinearLayout) ((Activity) mActivity).findViewById(Integer.valueOf(id));
        ToggleButton statusBtn = (ToggleButton) parent.findViewById(R.id.status);

        statusBtn.setOnCheckedChangeListener(null);
        if(!success) statusBtn.setChecked(!status.equals("1"));
        else statusBtn.setChecked(status.equals("1"));
        statusBtn.setOnCheckedChangeListener(new LecturerOnChangeListener(mActivity, Integer.valueOf(position), commentDialog));
    }
}
