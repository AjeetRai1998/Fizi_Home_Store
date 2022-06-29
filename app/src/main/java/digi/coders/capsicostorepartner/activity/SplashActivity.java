package digi.coders.capsicostorepartner.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.gson.Gson;

import digi.coders.capsicostorepartner.R;
import digi.coders.capsicostorepartner.helper.InternetCheck;
import digi.coders.capsicostorepartner.helper.OrderService;
import digi.coders.capsicostorepartner.model.Vendor;
import digi.coders.capsicostorepartner.singletask.SingleTask;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT=3000;
    private ImageView logo;
    private SingleTask singleTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        logo=findViewById(R.id.logo);



        logo.setAnimation(AnimationUtils.loadAnimation(this, R.anim.zoom));
        singleTask=(SingleTask)getApplication();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(new InternetCheck().isNetworkAvailable(getApplicationContext())) {

                    String js=singleTask.getValue("vendor");
                    Vendor vendor=new Gson().fromJson(js,Vendor.class);
                    if(vendor!=null)
                    {

                        startActivity(new Intent(SplashActivity.this, DashboardActivity.class));
                        finish();
                    }
                    else
                    {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        finish();
                    }

                }
                else
                {
                    startActivity(new Intent(SplashActivity.this, NoInternetActivity.class));
                    finish();
                }

            }
        },SPLASH_TIME_OUT);
    }
}