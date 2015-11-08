package com.digitcreativestudio.safian.adadosen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by faqih_000 on 5/12/2015.
 */
public class splashscreen extends Activity {

    private static int splashInterval = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = new Intent(splashscreen.this, MainActivity.class);
        startActivity(i);
        this.finish();
    }
}

