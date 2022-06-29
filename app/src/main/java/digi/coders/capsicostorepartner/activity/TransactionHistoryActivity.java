package digi.coders.capsicostorepartner.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import digi.coders.capsicostorepartner.R;
import digi.coders.capsicostorepartner.databinding.ActivityTransactionHistoryBinding;

public class TransactionHistoryActivity extends AppCompatActivity {

    ActivityTransactionHistoryBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityTransactionHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}