package digi.coders.capsicostorepartner.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import digi.coders.capsicostorepartner.R;
import digi.coders.capsicostorepartner.databinding.ActivityKycstatusBinding;

public class KYCStatusActivity extends AppCompatActivity {

    ActivityKycstatusBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityKycstatusBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //handle back
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}