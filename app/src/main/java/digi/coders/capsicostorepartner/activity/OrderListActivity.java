package digi.coders.capsicostorepartner.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import digi.coders.capsicostorepartner.R;
import digi.coders.capsicostorepartner.adapter.OrderHistoryAdapter;
import digi.coders.capsicostorepartner.databinding.ActivityOrderListBinding;
import digi.coders.capsicostorepartner.helper.MyApi;
import digi.coders.capsicostorepartner.model.MyOrder;
import digi.coders.capsicostorepartner.model.ShowProgress;
import digi.coders.capsicostorepartner.model.Vendor;
import digi.coders.capsicostorepartner.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderListActivity extends AppCompatActivity {

    ActivityOrderListBinding binding;
    private SingleTask singleTask;
    private List<MyOrder> myOrderList=new ArrayList<>();
    OrderHistoryAdapter orderHistoryAdapter;
    int scrollStatus=0,scrollStatusForData=0;
    int page=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityOrderListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        singleTask=(SingleTask)getApplication();
        //handle back
        Glide.with(this).load(R.raw.loading).into(binding.loading);
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        binding.orderHistoryList.setLayoutManager(new LinearLayoutManager(OrderListActivity.this,LinearLayoutManager.VERTICAL,false));
        orderHistoryAdapter=new OrderHistoryAdapter(myOrderList);
        binding.orderHistoryList.setAdapter(orderHistoryAdapter);

        binding.mainLayout.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // on scroll change we are checking when users scroll as bottom.
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    if(scrollStatusForData==0) {
                        if (scrollStatus == 0) {
                            scrollStatus = 1;
                            page = page + 25;
                            loadOrderHistoryList();
                        }
                    }
                }
            }
        });

        //load order History list
        loadOrderHistoryList();

    }

    private void loadOrderHistoryList() {
        binding.loading.setVisibility(View.VISIBLE);
        ShowProgress.getShowProgress(OrderListActivity.this).show();
        String  ven=singleTask.getValue("vendor");
        Vendor vendor=new Gson().fromJson(ven,Vendor.class);
        MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
        Call<JsonArray> call = myApi.getOrder(vendor.getId(),"Delivered",page+"");
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        String res = jsonObject1.getString("res");
                        String message = jsonObject1.getString("message");
                        Log.e("sdsd", jsonObject1.toString());

                        if (res.equals("success")) {
                            ShowProgress.getShowProgress(OrderListActivity.this).hide();
                            JSONArray jsonArray1=jsonObject1.getJSONArray("data");
                            myOrderList=new ArrayList<>();
                            for(int i=0;i<jsonArray1.length();i++)
                            {

                                MyOrder myOrder=new Gson().fromJson(jsonArray1.getJSONObject(i).toString(),MyOrder.class);
                                myOrderList.add(myOrder);

                            }
                            orderHistoryAdapter.addData(myOrderList);

                            scrollStatus=0;
                        } else {

                            ShowProgress.getShowProgress(OrderListActivity.this).hide();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                binding.loading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                ShowProgress.getShowProgress(OrderListActivity.this).hide();
                binding.noTxt.setVisibility(View.VISIBLE);
                binding.loading.setVisibility(View.VISIBLE);
            }
        });


    }


}