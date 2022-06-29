package digi.coders.capsicostorepartner.fragment;

import
        android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Ref;
import java.util.ArrayList;
import java.util.List;

import digi.coders.capsicostorepartner.R;
import digi.coders.capsicostorepartner.activity.AddProductFormActivity;
import digi.coders.capsicostorepartner.activity.DashboardActivity;
import digi.coders.capsicostorepartner.activity.OrderDetailsActivity;
import digi.coders.capsicostorepartner.adapter.OrderAdapter;
import digi.coders.capsicostorepartner.databinding.FragmentNewOrderBinding;
import digi.coders.capsicostorepartner.helper.MyApi;
import digi.coders.capsicostorepartner.helper.Refre;
import digi.coders.capsicostorepartner.model.Category;
import digi.coders.capsicostorepartner.model.MyOrder;
import digi.coders.capsicostorepartner.model.ShowProgress;
import digi.coders.capsicostorepartner.model.Vendor;
import digi.coders.capsicostorepartner.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NewOrderFragment extends Fragment implements Refre {

    FragmentNewOrderBinding binding;
    private SingleTask singleTask;
    private List<MyOrder> myOrderList;
    int len=0;

    public int counter=0;
    CountDownTimer countDownTimer;

    public static Refre refre;
    FragmentManager fm;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding=FragmentNewOrderBinding.inflate(inflater,container,false);
        refre=this;
        fm=getParentFragmentManager();

        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        singleTask=(SingleTask)getActivity().getApplication();

        binding.refersh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        loadNewOrderList();
                        binding.refersh.setRefreshing(false);
                    }
                },2000);
            }
        });

//        loadNewOrderList();

    }

    private void loadNewOrderList() {
        //ShowProgress.getShowProgress(getActivity()).show();
        if (countDownTimer!=null){
            countDownTimer.cancel();
        }
        String ven = singleTask.getValue("vendor");
        Vendor vendor = new Gson().fromJson(ven, Vendor.class);
        MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
        Log.e("merchant_id", vendor.getId() + "");
        Call<JsonArray> call = myApi.getOrder(vendor.getId(),"Placed","0");
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

                        Log.e("sdsd", jsonObject1.toString());
                        binding.progress.setVisibility(View.GONE);
                        if (res.equals("success")) {
                            int count=jsonObject1.getInt("count");
                            HomeFragment.countNewOrder.setText(count+"");
                            HomeFragment.countNewOrder.setVisibility(View.VISIBLE);
                                JSONArray jsonArray1=jsonObject1.getJSONArray("data");
                                if (len<jsonArray1.length()){

                                }
                                len=jsonArray1.length();
                            myOrderList=new ArrayList<>();
                            for(int i=0;i<jsonArray1.length();i++)
                            {
                                MyOrder myOrder=new Gson().fromJson(jsonArray1.getJSONObject(i).toString(),MyOrder.class);
                                myOrderList.add(myOrder);
                            }
                            binding.newOrderList.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL, false));
                            OrderAdapter adapter=new OrderAdapter(1,myOrderList,singleTask,getActivity(),fm);
                            adapter.findPosition(new OrderAdapter.GetPosition() {
                                @Override
                                public void click() {
                                    loadNewOrderList();
                                    //PreparingOrderFragment.updateMethod();
                                }
                            });
                            binding.newOrderList.setAdapter(adapter);

                            binding.noTxt.setVisibility(View.GONE);
                            binding.newOrderList.setVisibility(View.VISIBLE);
                            binding.lottieAnimation.setVisibility(View.GONE);
                            getTimer();

                        } else {

                            getTimer();

                            HomeFragment.countNewOrder.setVisibility(View.GONE);

                            String ven = singleTask.getValue("vendor");
                            Vendor vendor = new Gson().fromJson(ven, Vendor.class);
                            if(vendor.getIsOpen().equalsIgnoreCase("open")) {
                                binding.noTxt.setText("Waiting For New Orders....");
                                binding.lottieAnimation.setVisibility(View.VISIBLE);
                            }else{
                                binding.noTxt.setText("You Are Offline");
                                binding.lottieAnimation.setVisibility(View.GONE);
                            }
                            binding.progress.setVisibility(View.GONE);
                            binding.noTxt.setVisibility(View.VISIBLE);
                            binding.newOrderList.setVisibility(View.GONE);

                            //loadNewOrderList();
                            //Toast.makeText(getActivity()," Delivery not Available", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("sdsd", e.getMessage());
                    }
                }
            }
            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                //ShowProgress.getShowProgress(getActivity()).hide();
                binding.progress.setVisibility(View.GONE);
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTimer () {
        counter=10000;
        countDownTimer= new CountDownTimer(counter,1000){
            @Override
            public void onTick(long millisUntilFinished) {

                int seconds = (int) (millisUntilFinished / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;

            }

            @Override
            public void onFinish() {
//                loadNewOrderList();
            }
        };
        countDownTimer.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadNewOrderList();
        Log.e("upcom","on resume");
    }

    @Override
    public void onStart() {
        super.onStart();
        loadNewOrderList();
    }

    @Override
    public void onRefresh() {
        loadNewOrderList();
    }
}
