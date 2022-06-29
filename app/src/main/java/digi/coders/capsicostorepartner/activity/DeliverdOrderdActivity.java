package digi.coders.capsicostorepartner.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import digi.coders.capsicostorepartner.databinding.ActivityDeliverdOrderdBinding;

public class DeliverdOrderdActivity extends AppCompatActivity {

    ActivityDeliverdOrderdBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityDeliverdOrderdBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }
}