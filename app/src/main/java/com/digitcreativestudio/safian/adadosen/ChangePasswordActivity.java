package com.digitcreativestudio.safian.adadosen;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.digitcreativestudio.safian.adadosen.Auth.ChangePassword;
import com.digitcreativestudio.safian.adadosen.Utils.MyAlertDialog;
import com.digitcreativestudio.safian.adadosen.Utils.SessionManager;

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
}
