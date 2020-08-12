package com.tudiby.freemusic;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ExitActivity extends AppCompatActivity {
    SweetAlertDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exit);
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
          pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("You have left the app, thank you for using tudiby");
        pDialog.setCancelable(false);
        pDialog.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                finish();

                System.exit(0);
                finishAffinity();
                // change image
            }

        }, 3000);
    }
}