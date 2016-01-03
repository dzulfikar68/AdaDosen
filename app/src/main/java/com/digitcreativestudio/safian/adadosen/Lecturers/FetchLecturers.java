package com.digitcreativestudio.safian.adadosen.Lecturers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.digitcreativestudio.safian.adadosen.Data.DBContract;
import com.digitcreativestudio.safian.adadosen.MainActivity;
import com.digitcreativestudio.safian.adadosen.R;
import com.digitcreativestudio.safian.adadosen.Utils.SessionManager;
import com.digitcreativestudio.safian.adadosen.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by faqih_000 on 11/7/2015.
 */
public class FetchLecturers extends AsyncTask <String, Void, String> {
    ProgressDialog pDialog = null;
    int position = 0;
    SwipeRefreshLayout swipeLayout = null;

    Activity mActivity = null;
    Context context;

    public FetchLecturers(Activity activity, ProgressDialog progressDialog){
        mActivity = activity;
        pDialog = progressDialog;
    }

    public FetchLecturers(Activity activity, SwipeRefreshLayout swipe){
        mActivity = activity;
        swipeLayout = swipe;
    }

    public FetchLecturers(Context context){
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        int timeout=10000; //in milisecond = 10 detik
        HttpURLConnection urlConnection = null;
        InputStream response = null;
        String responseString = "";

        List<ContentValues> listCV = new ArrayList<>();

        try {
            URL url = new URL("http://api.arifian.com/AdaDosen/lecturer/fetch");
            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setConnectTimeout(timeout);

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
            if(jObj.getBoolean("success")){
                JSONArray jArray = jObj.getJSONArray("data");
                for(int i = 0; i < jArray.length(); i++){
                    JSONObject lecturer = jArray.getJSONObject(i);
                    listCV.add(Utils.parseJsonLecturer(lecturer));
                }
                ContentValues[] arrayCV = new ContentValues[listCV.size()];
                listCV.toArray(arrayCV);
                if(mActivity != null)
                    mActivity.getContentResolver().bulkInsert(DBContract.LecturerEntry.CONTENT_URI, arrayCV);
                else context.getContentResolver().bulkInsert(DBContract.LecturerEntry.CONTENT_URI, arrayCV);

            }else{
                return "no results";
            }

        } catch (Exception e) {
            e.printStackTrace();

            return "Exception Caught";
        } finally{
            urlConnection.disconnect();
        }

        return "success";
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if(mActivity != null) {
            if (pDialog != null)
                if (pDialog.isShowing())
                    pDialog.dismiss();
            if (swipeLayout != null)
                if (swipeLayout.isRefreshing())
                    swipeLayout.setRefreshing(false);

            TextView notFound = (TextView) ((Activity) mActivity).findViewById(R.id.notFound);

            if (result.equalsIgnoreCase("Exception Caught")) {
                Toast.makeText(mActivity, "Terjadi Kesalahan.", Toast.LENGTH_SHORT).show();
                notFound.setVisibility(View.VISIBLE);
            }

            if (result.equalsIgnoreCase("no results")) {
                Toast.makeText(mActivity, "Data empty", Toast.LENGTH_LONG).show();
                notFound.setVisibility(View.VISIBLE);
            } else {
                Intent i = new Intent();
                i.setAction(MainActivity.ACTION_REFRESH);
                mActivity.sendBroadcast(i);
                notFound.setVisibility(View.GONE);
            }
            SessionManager session = new SessionManager(mActivity);
            session.setIsSync(true);
        }else{
            Intent i = new Intent();
            i.setAction(MainActivity.ACTION_REFRESH);
            context.sendBroadcast(i);
            SessionManager session = new SessionManager(context);
            session.setIsSync(true);
        }
    }

    public void setPosition(int position){
        this.position = position;
    }
}
