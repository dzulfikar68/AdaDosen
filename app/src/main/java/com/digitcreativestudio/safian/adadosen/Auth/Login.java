package com.digitcreativestudio.safian.adadosen.Auth;

import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by faqih_000 on 11/8/2015.
 */
public class Login extends AsyncTask<String, Void, Void> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(String... params) {
        int timeout=10000; //in milisecond = 10 detik
        HttpURLConnection urlConnection = null;
        String charset = "UTF-8";

        Log.e("Login", params[0]+" "+params[1]);

        try {
            String query = String.format("id=%s&last_login=%s&token=%s",
                    URLEncoder.encode(params[0], charset),
                    URLEncoder.encode(params[1], charset),
                    URLEncoder.encode(params[2], charset));

            URL url = new URL("http://api.arifian.com/AdaDosen/user/login");
            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Accept-Charset", charset);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
            urlConnection.setConnectTimeout(timeout);

            OutputStream output = urlConnection.getOutputStream();
            output.write(query.getBytes(charset));

            InputStream response = urlConnection.getInputStream();


        } catch (Exception e) {
            e.printStackTrace();

        } finally{
            urlConnection.disconnect();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Log.e("Login", "Logged in");
    }
}
