package com.digitcreativestudio.safian.adadosen;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.digitcreativestudio.safian.adadosen.Auth.ChangePassword;
import com.digitcreativestudio.safian.adadosen.Auth.SessionManager;
import com.digitcreativestudio.safian.adadosen.Utils.MyAlertDialog;

public class ChangePasswordActivity extends AppCompatActivity {
    private EditText oldPassword, password, passwordConfirmation;
    private Button changeBtn;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        session = new SessionManager(getApplicationContext());

        oldPassword = (EditText) findViewById(R.id.old_password);
        password = (EditText) findViewById(R.id.password);
        passwordConfirmation = (EditText) findViewById(R.id.password_confirmation);

        changeBtn = (Button) findViewById(R.id.change);

        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldPasswordText = oldPassword.getText().toString().trim();
                String passwordText = password.getText().toString().trim();
                String passwordConfirmationText = passwordConfirmation.getText().toString().trim();

                oldPassword.setText(oldPasswordText);
                password.setText(passwordText);
                passwordConfirmation.setText(passwordConfirmationText);

                if(!passwordText.equals(passwordConfirmationText)){
                    new MyAlertDialog(ChangePasswordActivity.this, "Terjadi Kesalahan", "Konfirmasi password baru tidak sesuai.");
                }else{
                    if(passwordText.equals("")){
                        new MyAlertDialog(ChangePasswordActivity.this, "Terjadi Kesalahan", "Password tidak boleh kosong.");
                    }else{
                        new ChangePassword(ChangePasswordActivity.this).execute(session.getUserDetails().get(SessionManager.KEY_ID), oldPasswordText, passwordText, passwordConfirmationText);
                    }

                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_change_password, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        return super.onOptionsItemSelected(item);
    }
}
