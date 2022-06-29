package digi.coders.capsicostorepartner.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import digi.coders.capsicostorepartner.R;
import digi.coders.capsicostorepartner.adapter.CouponAdapter;
import digi.coders.capsicostorepartner.adapter.ProductAdapter;
import digi.coders.capsicostorepartner.databinding.ActivityCouponBinding;
import digi.coders.capsicostorepartner.helper.MyApi;
import digi.coders.capsicostorepartner.model.Coupon;
import digi.coders.capsicostorepartner.model.Product;
import digi.coders.capsicostorepartner.model.ShowProgress;
import digi.coders.capsicostorepartner.model.Vendor;
import digi.coders.capsicostorepartner.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CouponActivity extends AppCompatActivity {

    ActivityCouponBinding binding;
    private SingleTask singleTask;
    private List<Coupon> couponList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCouponBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        singleTask=(SingleTask)getApplication();
        loadCouponList();

        //addCouon
        binding.addCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CouponActivity.this,AddCouponActivity.class));

            }
        });
    }
    private void loadCouponList() {
        ShowProgress.getShowProgress(CouponActivity.this).show();
        String js=singleTask.getValue("vendor");
        Vendor vendor=new Gson().fromJson(js,Vendor.class);
        MyApi myApi=singleTask.getRetrofit().create(MyApi.class);
        Call<JsonArray> call=myApi.getAllCoupon(vendor.getId());
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if(response.isSuccessful())
                {
                    try {
                        JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
                        JSONObject jsonObject=jsonArray.getJSONObject(0);
                        String res=jsonObject.getString("res");
                        String msg=jsonObject.getString("message");

                        if(res.equals("success"))
                        {

                            ShowProgress.getShowProgress(CouponActivity.this).hide();
                            couponList=new ArrayList<>();
                            JSONArray jsonArray1=jsonObject.getJSONArray("data");
                            for(int i=0;i<jsonArray1.length();i++)
                            {
                                Coupon coupon=new Gson().fromJson(jsonArray1.getJSONObject(i).toString(),Coupon.class);
                                couponList.add(coupon);
                            }
                            binding.couponList.setLayoutManager(new LinearLayoutManager(CouponActivity.this,LinearLayoutManager.VERTICAL,false));
                            binding.couponList.setAdapter(new CouponAdapter(couponList));
                        }
                        else
                        {
                            ShowProgress.getShowProgress(CouponActivity.this).hide();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

                ShowProgress.getShowProgress(CouponActivity.this).hide();
                Toast.makeText(CouponActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}