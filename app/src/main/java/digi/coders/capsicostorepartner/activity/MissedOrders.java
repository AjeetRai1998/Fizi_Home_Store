package digi.coders.capsicostorepartner.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import digi.coders.capsicostorepartner.adapter.MissedOrderAdapter;
import digi.coders.capsicostorepartner.adapter.OrderAdapter;
import digi.coders.capsicostorepartner.databinding.ActivityAddCouponBinding;
import digi.coders.capsicostorepartner.databinding.ActivityMissedOrdersBinding;
import digi.coders.capsicostorepartner.fragment.HomeFragment;
import digi.coders.capsicostorepartner.fragment.OfflineFragment;
import digi.coders.capsicostorepartner.helper.MyApi;
import digi.coders.capsicostorepartner.model.MyOrder;
import digi.coders.capsicostorepartner.model.ProgressDisplay;
import digi.coders.capsicostorepartner.model.Vendor;
import digi.coders.capsicostorepartner.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MissedOrders extends AppCompatActivity {

    ActivityMissedOrdersBinding binding;
    private SingleTask singleTask;
//    ProgressDisplay progressDisplay;
    private List<MyOrder> myOrderList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityMissedOrdersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        singleTask=(SingleTask)getApplication();
//        progressDisplay=new ProgressDisplay(this);
        binding.back.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        OfflineFragment.status=0;
                        finish();
                    }
                }
        );
        getMissedOrders();
    }

    private void getMissedOrders() {
//        progressDisplay.showProgress();
        String ven = singleTask.getValue("vendor");
        Vendor vendor = new Gson().fromJson(ven, Vendor.class);
        MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
        Log.e("merchant_id", vendor.getId() + "");
        Call<JsonArray> call = myApi.getOrder(vendor.getId(),"Missed","0");
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    try {
                        Log.e("hiheihiw", response.body().toString() );
                        JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        String res = jsonObject1.getString("res");
                        String message = jsonObject1.getString("message");

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(new Date());
                        calendar.add(Calendar.DAY_OF_YEAR, 0);
                        Date current = calendar.getTime();



                        Log.e("sdsd", jsonObject1.toString());
                        if (res.equals("success")) {

                            JSONArray jsonArray1=jsonObject1.getJSONArray("data");

                            myOrderList=new ArrayList<>();
                            for(int i=0;i<jsonArray1.length();i++)
                            {
                                MyOrder myOrder=new Gson().fromJson(jsonArray1.getJSONObject(i).toString(),MyOrder.class);


                                Date orderdate = new SimpleDateFormat("yyyy-MM-dd").parse(myOrder.getCreatedAt());
                                String currentString = new SimpleDateFormat("yyyy-MM-dd").format(current);
                                Date currentDate=new SimpleDateFormat("yyyy-MM-dd").parse(currentString);
                                if(currentDate.equals(orderdate)) {
                                    myOrderList.add(myOrder);
                                }
                            }
                            binding.missedOrders.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL, false));
                            MissedOrderAdapter adapter=new MissedOrderAdapter(1,myOrderList,singleTask,MissedOrders.this,getSupportFragmentManager());
                            binding.missedOrders.setAdapter(adapter);

                            binding.noTxt.setVisibility(View.GONE);
                            binding.missedOrders.setVisibility(View.VISIBLE);

//                            progressDisplay.hideProgress();
                        } else {


                            String ven = singleTask.getValue("vendor");
                            Vendor vendor = new Gson().fromJson(ven, Vendor.class);

                            binding.noTxt.setVisibility(View.VISIBLE);
                            binding.missedOrders.setVisibility(View.GONE);
//                            progressDisplay.hideProgress();
                          }
                    } catch (Exception e) {
                        e.printStackTrace();
//                        progressDisplay.hideProgress();
//                        Log.e("sdsd", e.getMessage());
                    }
                }


            }
            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                //ShowProgress.getShowProgress(getApplicationContext()).hide();
//               progressDisplay.hideProgress();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        OfflineFragment.status=0;
        super.onBackPressed();
    }
}