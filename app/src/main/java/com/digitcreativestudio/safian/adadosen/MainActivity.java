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
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

    public static final String ACTION_REFRESH = "com.digitcreativestudio.safian.adadosen.REFRESH";
    BroadcastReceiver receiver;
    IntentFilter filter;

    SessionManager session;
    ListView listView;
    SwipeRefreshLayout swipeLayout;


    private BroadcastReceiver mRegistrationBroadcastReceiver;
    ProgressDialog pDialog;

    LecturersAdapter adapter;
    Cursor mCursor;

    private static final int LECTURER_LOADER = 0;
    private int mPosition = ListView.INVALID_POSITION;
    private final String SELECTED_KEY = "position";

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        unregisterReceiver(receiver);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(SessionManager.REGISTRATION_COMPLETE));
        registerReceiver(receiver, filter);
        MyNotificationManager notif = new MyNotificationManager(getApplicationContext());
        notif.removeNotifications();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new LecturersAdapter(this, null, 0);

        MyNotificationManager notif = new MyNotificationManager(getApplicationContext());
        notif.removeNotifications();

        session = new SessionManager(getApplicationContext());

        listView = (ListView) findViewById(R.id.listview_lecturers);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipeLayout.setColorSchemeColors(R.color.primary_color);

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new FetchLecturers(MainActivity.this, swipeLayout).execute();
            }
        });

        if(session.isLoggedIn()){
           /* user.setText(" " + (session.getUserDetails()).get(SessionManager.KEY_NAME));
            login.setVisibility(View.GONE);*/
            //Toast.makeText(getApplicationContext(),"Welcome, "+(session.getUserDetails()).get(SessionManager.KEY_NAME),Toast.LENGTH_LONG).show();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

            new Login().execute(session.getUserDetails().get(SessionManager.KEY_ID), sdf.format(new Date()));
        }/*else{
            user.setText(" Guest");
            login.setVisibility(View.VISIBLE);
        }*/

        /*login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(i);
            }
        });*/



        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (session.isTokenSent()) {

                } else {

                }
            }
        };

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                reloadCursor();
            }
        };
        filter = new IntentFilter(ACTION_REFRESH);

        registerReceiver(receiver, filter);

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading..");
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);
        reloadCursor();

        Bundle args = getIntent().getExtras();
        if(args != null){
            (new LecturerUpdate(this)).execute(args.getString("id"), args.getString("status"), args.getString("modifiedBy"), args.getString("lastModify"), args.getString("position"));
            listView.smoothScrollToPositionFromTop(Integer.valueOf(args.getString("position"))-1, 0);
        }
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
        }else if(id == R.id.login){
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(i);
        }else if(id==R.id.about){
            Intent i = new Intent(MainActivity.this, AboutActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mPosition != ListView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }

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

    public void reloadCursor(){
        if(session.isSync()) {
            mCursor = getContentResolver().query(DBContract.LecturerEntry.CONTENT_URI, null, null, null, null);
            if(mCursor.getCount() > 0) {
                adapter.swapCursor(mCursor);
                adapter.notifyDataSetChanged();
            }else {
                session.setIsSync(false);
            }
        }

        if(!session.isSync()){
            pDialog.show();
            new FetchLecturers(MainActivity.this, pDialog).execute();
        }
    }
}
