package com.digitcreativestudio.safian.adadosen;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.digitcreativestudio.safian.adadosen.Auth.Auth;
import com.digitcreativestudio.safian.adadosen.Utils.MyAlertDialog;
import com.digitcreativestudio.safian.adadosen.Utils.SessionManager;

public class LoginActivity extends AppCompatActivity {
    private EditText mNim, mPassword;
    private Button mLogin;

    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        session = new SessionManager(this);

        mNim = (EditText) findViewById(R.id.nim);
        mPassword = (EditText) findViewById(R.id.password);
        mLogin = (Button) findViewById(R.id.login);

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nim = mNim.getText().toString();
                String password = mPassword.getText().toString();

                if (nim.trim().length() > 0 && password.trim().length() > 0) {

                    Bundle args = getIntent().getExtras();
                    if(args != null){
                        new Auth(LoginActivity.this,
                            args.getString("id"),
                            args.getString("status"),
                            args.getString("position")
                        ).execute(nim, password, session.getToken());
                    }else{
                        new Auth(LoginActivity.this).execute(nim, password, session.getToken());
                    }
                } else {
                    new MyAlertDialog(LoginActivity.this, "Terjadi Kesalahan", "");
                }
            }
        });
    }
}
