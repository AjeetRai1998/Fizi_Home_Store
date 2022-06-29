package digi.coders.capsicostorepartner.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;

import digi.coders.capsicostorepartner.adapter.OrderDetailAdapter;
import digi.coders.capsicostorepartner.databinding.ActivityOrderHistoryBinding;
import digi.coders.capsicostorepartner.model.MyOrder;
import digi.coders.capsicostorepartner.model.Orderproduct;
import digi.coders.capsicostorepartner.model.User;
import digi.coders.capsicostorepartner.model.Vendor;
import digi.coders.capsicostorepartner.singletask.SingleTask;

public class OrderHistoryActivity extends AppCompatActivity {

    ActivityOrderHistoryBinding binding;
    public static MyOrder myOrder;
    private SingleTask singleTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityOrderHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        singleTask=(SingleTask)getApplication();

        //handle back
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //hanlde view order
        binding.viewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            startActivity(new Intent(OrderHistoryActivity.this,OrderSummaryActivity.class));
            }
        });
        binding.trackOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrderHistoryActivity.this,TrackOrderActivity.class));
            }
        });

        setData();
        //load item list

    }

    private void setData() {
        if(myOrder!=null) {
            String ven = singleTask.getValue("vendor");
            Vendor vendor = new Gson().fromJson(ven, Vendor.class);
            User user = myOrder.getUser()[0];
            binding.userName.setText(user.getName());
            binding.userPhoneNo.setText(user.getMobile());
            binding.userEmail.setText(user.getEmail());
            /*binding.storeAddress.setText(vendor.getAddress());
            binding.userAddrss.setText(myOrder.getAddress()[0].getAddress());*/
            binding.totalAmount.setText("₹" + myOrder.getAmount());
            loadItemList(myOrder);
        }

    }

    private void loadItemList(MyOrder myOrder) {
        binding.itemList.setLayoutManager(new LinearLayoutManager(OrderHistoryActivity.this,LinearLayoutManager.VERTICAL,false));
        binding.itemList.setAdapter(new OrderDetailAdapter(myOrder));
    }
}