package com.digitcreativestudio.safian.adadosen.Lecturers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.digitcreativestudio.safian.adadosen.Utils.SessionManager;
import com.digitcreativestudio.safian.adadosen.R;

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
    private Activity mActivity;
    private ProgressDialog pDialog;
    private String id, status, modifiedBy, lastModify, position;
    private boolean success = false;
    private SessionManager session;

    public LecturerUpdate(Activity activity){
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
    protected String doInBackground(String... params) {
        id = params[0]; status = params[1].equals("true") ? "1" : "0"; modifiedBy = params[2]; lastModify = params[3]; position = params[4];
        //Log.e("Updated", id + " By: " + modifiedBy + " At: " + lastModify + " To: " + status);
        String responseString = "";
        InputStream response = null;
        int timeout=10000; //in milisecond = 10 detik
        HttpURLConnection urlConnection = null;
        String charset = "UTF-8";

        try {
            String query = String.format("id=%s&status=%s&modified_by=%s&last_modify=%s",
                    URLEncoder.encode(id, charset),
                    URLEncoder.encode(status, charset),
                    URLEncoder.encode(modifiedBy, charset),
                    URLEncoder.encode(lastModify, charset));

            URL url = new URL("http://faqiharifian.xyz/api/ada_dosen/update_lecturer.php");
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
        }else{
            Toast.makeText(mActivity, s, Toast.LENGTH_SHORT).show();

            LinearLayout parent = (LinearLayout) mActivity.findViewById(Integer.valueOf(id));
            ToggleButton statusBtn = (ToggleButton) parent.findViewById(R.id.status);
            if(success){
                try{
                   /* SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    SimpleDateFormat sdf2nd = new SimpleDateFormat("HH:mm dd-MM-yyyy");
                    sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                    lastModify = sdf2nd.format(sdf.parse(lastModify));

                    ((TextView) parent.findViewById(R.id.lastModify)).setText(lastModify);
                    ((TextView) parent.findViewById(R.id.modifiedBy)).setText((session.getUserDetails()).get(SessionManager.KEY_NAME));

                    ListView lv = (ListView) mActivity.findViewById(R.id.listview_lecturers);
                    LecturersAdapter adapter = (LecturersAdapter) lv.getAdapter();
                    ArrayList<Lecturer> lecturers = adapter.getLecturers();
                    Lecturer lecturer = lecturers.get(Integer.valueOf(position));
                    lecturer.setStatus(status.equals("true"));
                    lecturer.setModifiedBy((session.getUserDetails()).get(SessionManager.KEY_NAME));
                    lecturer.setLastModify(lastModify);

                    lecturers.set(lecturer.getPosition(), lecturer);

                    lv.setAdapter(null);
                    lv.setAdapter(new LecturersAdapter(mActivity, lecturers));*/
                    FetchLecturers fl = new FetchLecturers(mActivity);
                    fl.setPosition(Integer.valueOf(position));
                    fl.execute();
                }catch(Exception e){
                    e.printStackTrace();
                }

            }else{
                statusBtn.setOnCheckedChangeListener(null);
                statusBtn.setChecked(!status.equals("1"));
                statusBtn.setOnCheckedChangeListener(new LecturerOnChangeListener(mActivity, Integer.valueOf(position)));
            }
        }


    }
}
