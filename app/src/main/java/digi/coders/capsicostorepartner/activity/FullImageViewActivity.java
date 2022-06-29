package digi.coders.capsicostorepartner.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import digi.coders.capsicostorepartner.databinding.ActivityFullImageViewBinding;

public class FullImageViewActivity extends AppCompatActivity {

    ActivityFullImageViewBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityFullImageViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        /*//getBundle
        Bundle bundle=getIntent().getBundleExtra("data");
        byte[] arr=bundle.getByteArray("sdsd");
        Bitmap bitmap=BitmapFactory.decodeByteArray(arr,0,0);
        binding.image.setImageBitmap(bitmap);*/







        //get Intent
        //setImage
        String s= getIntent().getStringExtra("data");
        Uri uri=Uri.parse(s);
        binding.image.setImageURI(uri);



        //handle close button
        binding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }
}