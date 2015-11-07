package com.digitcreativestudio.safian.adadosen;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.digitcreativestudio.safian.adadosen.Auth.Auth;

public class LoginActivity extends AppCompatActivity {
    private EditText mNim, mPassword;
    private Button mLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mNim = (EditText) findViewById(R.id.nim);
        mPassword = (EditText) findViewById(R.id.password);
        mLogin = (Button) findViewById(R.id.login);

        //AlertDialogManager alert = new AlertDialogManager();

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get email, password from EditText
                String nim = mNim.getText().toString();
                String password = mPassword.getText().toString();

                // Check if email, password is filled
                if (nim.trim().length() > 0 && password.trim().length() > 0) {
                    // For testing puspose email, password is checked with sample data

                    //RETRIEVE FROM DATABASE SERVER
                    new Auth(LoginActivity.this).execute(nim, password);

                } else {
                    // user didn't entered email or password
                    // Show alert asking him to enter the details
                    //alert.showAlertDialog(LoginActivity.this, "Login failed..", "Please enter email and password", false);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }
}
