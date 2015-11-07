package com.digitcreativestudio.safian.adadosen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.digitcreativestudio.safian.adadosen.Auth.Logout;
import com.digitcreativestudio.safian.adadosen.Auth.SessionManager;
import com.digitcreativestudio.safian.adadosen.Lecturers.FetchLecturers;

public class MainActivity extends AppCompatActivity {
    SessionManager session;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session = new SessionManager(getApplicationContext());

        listView = (ListView) findViewById(R.id.listview_lecturers);
        TextView user = (TextView) findViewById(R.id.user);

        Button login = (Button) findViewById(R.id.login);

        if(session.isLoggedIn()){
            user.setText(" "+(session.getUserDetails()).get(SessionManager.KEY_NAME));
            login.setVisibility(View.GONE);
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

        new FetchLecturers(MainActivity.this).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(session.isLoggedIn()){
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
        if (id == R.id.action_options) {
            new Logout(MainActivity.this).execute();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
