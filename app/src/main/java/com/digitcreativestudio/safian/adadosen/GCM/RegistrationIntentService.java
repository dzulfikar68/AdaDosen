/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.digitcreativestudio.safian.adadosen.GCM;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.digitcreativestudio.safian.adadosen.R;
import com.digitcreativestudio.safian.adadosen.Utils.SessionManager;
import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class RegistrationIntentService extends IntentService {

    private static final String TAG = "RegIntentService";
    private static final String[] TOPICS = {"global"};
    SessionManager session;

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        session = new SessionManager(this);
        try {
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

            if(!session.isTokenSent()){
                sendRegistrationToServer(token);
            }

            subscribeTopics(token);

        } catch (Exception e) {
            session.setSentToken(false);
        }

        Intent registrationComplete = new Intent(SessionManager.REGISTRATION_COMPLETE);

        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private boolean sendRegistrationToServer(String token) {
        int timeout=10000; //in milisecond = 10 detik
        HttpURLConnection urlConnection = null;
        String charset = "UTF-8";

        try {
            String query = String.format("token=%s",
                    URLEncoder.encode(token, charset));

            URL url = new URL("http://api.arifian.com/AdaDosen/user/register");
            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Accept-Charset", charset);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
            urlConnection.setConnectTimeout(timeout);

            OutputStream output = urlConnection.getOutputStream();
            output.write(query.getBytes(charset));

            InputStream response = urlConnection.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    response));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

            String responseString = sb.toString();

            JSONObject jObj = new JSONObject(responseString);
            boolean success = jObj.getBoolean("success");

            session.setToken(jObj.getString("token"));
            session.setSentToken(success);
            return success;
        } catch (Exception e) {
            e.printStackTrace();

        } finally{
            urlConnection.disconnect();
        }
        return false;
    }

    private void subscribeTopics(String token) throws IOException {
        GcmPubSub pubSub = GcmPubSub.getInstance(this);
        for (String topic : TOPICS) {
            pubSub.subscribe(token, "/topics/" + topic, null);
        }
    }
}
