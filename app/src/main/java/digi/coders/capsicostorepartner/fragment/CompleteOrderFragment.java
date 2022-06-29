package digi.coders.capsicostorepartner.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import digi.coders.capsicostorepartner.R;
import digi.coders.capsicostorepartner.adapter.OrderAdapter;
import digi.coders.capsicostorepartner.databinding.FragmentCompleteOrderBinding;
import digi.coders.capsicostorepartner.helper.MyApi;
import digi.coders.capsicostorepartner.model.MyOrder;
import digi.coders.capsicostorepartner.model.ShowProgress;
import digi.coders.capsicostorepartner.model.Vendor;
import digi.coders.capsicostorepartner.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CompleteOrderFragment extends Fragment {

    FragmentCompleteOrderBinding binding;
    private SingleTask singleTask;
    private List<MyOrder> myOrderList=new ArrayList<>();
    FragmentManager fm;
    OrderAdapter orderAdapter;
    int scrollStatus=0,scrollStatusForData=0;
    int page=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentCompleteOrderBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        singleTask=(SingleTask)getActivity().getApplication();
        fm=getParentFragmentManager();
        binding.completedOrderList.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        orderAdapter=new OrderAdapter(3,myOrderList,singleTask,getActivity(),fm);
        binding.completedOrderList.setAdapter(orderAdapter);

        Glide.with(getActivity()).load(R.raw.loading).into(binding.loading);
        loadCompletedOrder();
        binding.refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.e("sdsd34","re");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        orderAdapter.addClearData();

                        loadCompletedOrder();
                        binding.refresh.setRefreshing(false);
                    }
                },2000);
            }
        });

        binding.mainLayout.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // on scroll change we are checking when users scroll as bottom.
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    if(scrollStatusForData==0) {
                        if (scrollStatus == 0) {
                            scrollStatus = 1;
                            page = page + 50;
                            loadCompletedOrder();
                        }
                    }
                }
            }
        });
    }

    private void loadCompletedOrder() {
        binding.loading.setVisibility(View.VISIBLE);
        String ven = singleTask.getValue("vendor");
        Vendor vendor = new Gson().fromJson(ven, Vendor.class);
        MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
        Log.e("merchant_id", vendor.getId() + "");
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
                        binding.progressBar.setVisibility(View.GONE);
                        //progressDialog.dismiss();
                        if (res.equals("success")) {
                            int count=jsonObject1.getInt("count");
//                            HomeFragment.countComplete.setText(count+"");
//                            HomeFragment.countComplete.setVisibility(View.VISIBLE);
                            JSONArray jsonArray1=jsonObject1.getJSONArray("data");
                            myOrderList=new ArrayList<>();
                            for(int i=0;i<jsonArray1.length();i++)
                            {
                                MyOrder myOrder=new Gson().fromJson(jsonArray1.getJSONObject(i).toString(),MyOrder.class);
                                myOrderList.add(myOrder);
                            }
                            orderAdapter.addData(myOrderList);
                            scrollStatus=0;
                        } else {
                            binding.progressBar.setVisibility(View.GONE);
                            HomeFragment.countComplete.setVisibility(View.GONE);
                            binding.noTxt.setVisibility(View.VISIBLE);
                            //Toast.makeText(getActivity()," Delivery not Available", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                binding.loading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                binding.loading.setVisibility(View.GONE);
            }
        });

    }

}