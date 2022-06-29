package digi.coders.capsicostorepartner.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import digi.coders.capsicostorepartner.databinding.ActivityAddProductSuccessFullyBinding;

public class AddProductSuccessFullyActivity extends AppCompatActivity {

    ActivityAddProductSuccessFullyBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAddProductSuccessFullyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        //handle go back
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddProductSuccessFullyActivity.this,ManageProductActivity.class));
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AddProductSuccessFullyActivity.this,ManageProductActivity.class));
        finish();
    }
}