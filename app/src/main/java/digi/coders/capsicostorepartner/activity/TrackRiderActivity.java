package digi.coders.capsicostorepartner.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import digi.coders.capsicostorepartner.databinding.ActivityTrackRiderBinding;

public class TrackRiderActivity extends AppCompatActivity {

    ActivityTrackRiderBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityTrackRiderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //handle back button

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}