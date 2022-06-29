package digi.coders.capsicostorepartner.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import digi.coders.capsicostorepartner.adapter.OrderDetailAdapter;
import digi.coders.capsicostorepartner.databinding.ActivityTrackOrderBinding;

public class TrackOrderActivity extends AppCompatActivity {

    ActivityTrackOrderBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityTrackOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //hadle back
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //handle view order
        binding.viewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TrackOrderActivity.this,OrderSummaryActivity.class));
            }
        });




    }


}