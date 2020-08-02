package com.tudiby.freemusic;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import cn.pedant.SweetAlert.SweetAlertDialog;
import com.tudiby.freemusic.R;


public class SplashActivity extends AppCompatActivity {
    InterstitialAd mInterstitialAd;
    SweetAlertDialog pDialog;
    ImageView splashImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Showing Ads");
        pDialog.setCancelable(false);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                showinter();
                // change image
            }

        }, 3000);




    }



    public  void showinter() {


        pDialog.show();


        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interads));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                mInterstitialAd.show();
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                kehome();

                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                kehome();
                // Code to be executed when the interstitial ad is closed.
            }
        });


    }

    public  void kehome(){
        Intent intent = new Intent(SplashActivity.this,MainActivity.class);
        startActivity(intent);
    }


}
