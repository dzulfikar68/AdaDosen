package com.digitcreativestudio.safian.adadosen;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.digitcreativestudio.safian.adadosen.Auth.Login;
import com.digitcreativestudio.safian.adadosen.Auth.Logout;
import com.digitcreativestudio.safian.adadosen.GCM.RegistrationIntentService;
import com.digitcreativestudio.safian.adadosen.Lecturers.FetchLecturers;
import com.digitcreativestudio.safian.adadosen.Utils.MyAlertDialog;
import com.digitcreativestudio.safian.adadosen.Utils.MyNotificationManager;
import com.digitcreativestudio.safian.adadosen.Utils.SessionManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    SessionManager session;
    ListView listView;

    SharedPreferences sharedPreferences;

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    ProgressDialog pDialog;

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(SessionManager.REGISTRATION_COMPLETE));
        MyNotificationManager notif = new MyNotificationManager(getApplicationContext());
        notif.removeNotifications();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyNotificationManager notif = new MyNotificationManager(getApplicationContext());
        notif.removeNotifications();

        session = new SessionManager(getApplicationContext());

        listView = (ListView) findViewById(R.id.listview_lecturers);
        TextView user = (TextView) findViewById(R.id.user);

        Button login = (Button) findViewById(R.id.login);

        if(session.isLoggedIn()){
            user.setText(" " + (session.getUserDetails()).get(SessionManager.KEY_NAME));
            login.setVisibility(View.GONE);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

            new Login().execute(session.getUserDetails().get(SessionManager.KEY_ID), sdf.format(new Date()));
        }else{
            user.setText(" Guest");
            login.setVisibility(View.VISIBLE);
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(i);
            }
        });



        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                pDialog.dismiss();
                sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(SessionManager.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {
                    /*Intent i = new Intent(Ma, MainActivity.class);
                    startActivity(i);
                    finish();*/
                    //mInformationTextView.setText(getString(R.string.gcm_send_message));
                } else {
                    new MyAlertDialog(MainActivity.this, "Gagal", "Silahkan coba kembali", "Try Again");
                }
            }
        };

        if (checkPlayServices()) {
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Loading..");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
        new FetchLecturers(MainActivity.this).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(session.isLoggedIn()){
            getMenuInflater().inflate(R.menu.menu_logged_in, menu);
        }else{
            getMenuInflater().inflate(R.menu.menu_main, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            new Logout(MainActivity.this).execute();
            return true;
        }else if(id == R.id.action_refresh){
            listView.removeAllViewsInLayout();
            new FetchLecturers(MainActivity.this).execute();
        }else if(id == R.id.action_change_password){
            Intent i = new Intent(MainActivity.this, ChangePasswordActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i("Ada Dosen?", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
}
