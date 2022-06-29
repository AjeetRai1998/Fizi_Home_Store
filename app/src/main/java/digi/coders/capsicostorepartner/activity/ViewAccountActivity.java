package digi.coders.capsicostorepartner.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;

import digi.coders.capsicostorepartner.R;
import digi.coders.capsicostorepartner.databinding.ActivityViewAccountBinding;
import digi.coders.capsicostorepartner.model.Vendor;
import digi.coders.capsicostorepartner.singletask.SingleTask;

public class ViewAccountActivity extends AppCompatActivity {

    ActivityViewAccountBinding binding;
    private SingleTask singleTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityViewAccountBinding.inflate(getLayoutInflater());
        singleTask=(SingleTask)getApplication();
        setContentView(binding.getRoot());
        //
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setData();

    }

    private void setData() {
        String js=singleTask.getValue("vendor");
        Vendor vendor=new Gson().fromJson(js,Vendor.class);
        binding.vname.getEditText().setText(vendor.getName());
        binding.vemail.getEditText().setText(vendor.getEmail());
        binding.vmobile.getEditText().setText(vendor.getMobile());
        binding.vpassword.getEditText().setText(vendor.getPassword());
        binding.vstorename.getEditText().setText(vendor.getName());
        binding.vownername.getEditText().setText(vendor.getOwnerName());
        binding.vstoreAddress.getEditText().setText(vendor.getAddress());
    }
}