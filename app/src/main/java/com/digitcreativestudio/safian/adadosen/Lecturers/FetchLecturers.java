package com.digitcreativestudio.safian.adadosen.Lecturers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.digitcreativestudio.safian.adadosen.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by faqih_000 on 11/7/2015.
 */
public class FetchLecturers extends AsyncTask <String, Void, String> {
    ArrayList<Lecturer> lecturers = new ArrayList<>();
    ProgressDialog pDialog;

    Activity mActivity;

    public FetchLecturers(Activity activity){
        mActivity = activity;
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
    protected String doInBackground(String... strings) {
        int timeout=10000; //in milisecond = 10 detik
        HttpURLConnection urlConnection = null;
        InputStream response = null;
        String responseString = "";

        try {
            URL url = new URL("http://faqiharifian.xyz/api/ada_dosen/fetch_lecturers.php");
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
                    lecturers.add(new Lecturer(lecturer));
                }
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
        pDialog.dismiss();

        ListView lv = (ListView) mActivity.findViewById(R.id.listview_lecturers);
        TextView notFound = (TextView) mActivity.findViewById(R.id.notFound);

        if(result.equalsIgnoreCase("Exception Caught"))
        {
            Toast.makeText(mActivity, "Unable to connect to server,please check your internet connection!", Toast.LENGTH_LONG).show();
            notFound.setVisibility(View.VISIBLE);
        }

        if(result.equalsIgnoreCase("no results"))
        {
            Toast.makeText(mActivity, "Data empty", Toast.LENGTH_LONG).show();
            notFound.setVisibility(View.VISIBLE);
        }
        else
        {
            lv.setAdapter(new LecturersAdapter(mActivity, lecturers)); //Adapter menampilkan data mahasiswa ke dalam listView
            notFound.setVisibility(View.GONE);
        }
    }
}