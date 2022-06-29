package digi.coders.capsicostorepartner.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import digi.coders.capsicostorepartner.R;
import digi.coders.capsicostorepartner.databinding.ActivityNoInternetBinding;

public class NoInternetActivity extends AppCompatActivity {

    ActivityNoInternetBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityNoInternetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
    public void retry(View view) {
        ConnectivityManager connectivityManager= (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            startActivity(new Intent(this,SplashActivity.class));
            finish();
        }
        else
        {

            Snackbar snackbar=Snackbar.make(view,"No internet connection ",2000);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                snackbar.setActionTextColor(getColor(R.color.color_green));
            }
            snackbar.setAction("Ok", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            });

            snackbar.show();
        }
    }
}