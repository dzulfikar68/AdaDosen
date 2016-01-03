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

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.digitcreativestudio.safian.adadosen.Data.DBContract;
import com.digitcreativestudio.safian.adadosen.Lecturers.FetchLecturers;
import com.digitcreativestudio.safian.adadosen.MainActivity;
import com.digitcreativestudio.safian.adadosen.Utils.MyNotificationManager;
import com.digitcreativestudio.safian.adadosen.Utils.Utils;
import com.google.android.gms.gcm.GcmListenerService;

import org.json.JSONException;
import org.json.JSONObject;

public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";
    private static int NOTIFICATION_ID = 1756;

    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);

        if (from.startsWith("/topics/")) {
            // message received from some topic.
        } else {
            // normal downstream message.
        }
        sendNotification(message);
    }
    private void sendNotification(String message) {
        try {
            Log.e("notification", message);

            JSONObject jObj = new JSONObject(message);

            if(!jObj.getString("data").equals("")) {
                JSONObject data = jObj.getJSONObject("data");
                ContentValues cv = Utils.parseJsonLecturer(data);

                this.getContentResolver().update(DBContract.LecturerEntry.CONTENT_URI, cv, DBContract.LecturerEntry._ID + " = ?", new String[]{data.getString("id")});
            }
            if(!jObj.getString("message").equals("update")) {
                MyNotificationManager notif = new MyNotificationManager(this.getApplicationContext());
                notif.createNotification(jObj.getString("message"));

                Intent i = new Intent();
                i.setAction(MainActivity.ACTION_REFRESH);
                sendBroadcast(i);
            }else{
                new FetchLecturers(this).execute();
            }
        }catch(JSONException je){
            je.printStackTrace();
        }
    }
}
