package digi.coders.capsicostorepartner.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import digi.coders.capsicostorepartner.R;
import digi.coders.capsicostorepartner.databinding.ActivityBookingTableDetailsBinding;
import digi.coders.capsicostorepartner.databinding.CancelReasonLayoutBinding;

public class BookingTableDetails extends AppCompatActivity {

    ActivityBookingTableDetailsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityBookingTableDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //handle back
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        binding.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BookingTableDetails.this, "Accepted", Toast.LENGTH_SHORT).show();
            }
        });
        binding.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder aler = new AlertDialog.Builder(BookingTableDetails.this);
                AlertDialog alertDialog = aler.create();
                View view = LayoutInflater.from(BookingTableDetails.this).inflate(R.layout.cancel_reason_layout, null);
                CancelReasonLayoutBinding binding = CancelReasonLayoutBinding.bind(view);
                binding.close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                binding.submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Toast.makeText(BookingTableDetails.this, "Successfulky post reason", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }
                });
                alertDialog.setView(view);
                alertDialog.show();
            }
        });


    }
}