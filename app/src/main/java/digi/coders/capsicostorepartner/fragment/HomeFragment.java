package digi.coders.capsicostorepartner.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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

import digi.coders.capsicostorepartner.R;
import digi.coders.capsicostorepartner.adapter.HomeTabAdapter;
import digi.coders.capsicostorepartner.adapter.OrderAdapter;
import digi.coders.capsicostorepartner.databinding.FragmentHomeBinding;
import digi.coders.capsicostorepartner.helper.MyApi;
import digi.coders.capsicostorepartner.model.MyOrder;
import digi.coders.capsicostorepartner.model.Vendor;
import digi.coders.capsicostorepartner.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {
    public static FragmentHomeBinding binding;
    SingleTask singleTask;
    int pageStatus=0;
    public static TextView countNewOrder,countPrepare,countComplete;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentHomeBinding.inflate(inflater,container,false);
        singleTask=(SingleTask) getActivity().getApplication();
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        countPrepare=view.findViewById(R.id.countPrepare);
        countComplete=view.findViewById(R.id.countComplete);
        countNewOrder=view.findViewById(R.id.countNewOrder);
        Calendar calendar = Calendar.getInstance();
//date format is:  "Date-Month-Year Hour:Minutes am/pm"

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy"); //Date and time
        String currentDateandTime = sdf.format(new Date());
        //String currentDate = sdf.format(calendar.getTime());

//Day of Name in full form like,"Saturday", or if you need the first three characters you have to put "EEE" in the date format and your result will be "Sat".
        SimpleDateFormat sdf_ = new SimpleDateFormat("EEEE");

        Date date = new Date();
        String dayName = sdf_.format(date);
        binding.currentDate.setText("" + dayName + " " +sdf.format(new Date()));


        getChildFragmentManager().beginTransaction().replace(R.id.frame, new NewOrderFragment()).commitAllowingStateLoss();

        binding.lineNew.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        binding.txtNew.setTextColor(Color.parseColor("#f7b614"));
                        binding.viewNewOrder.setVisibility(View.VISIBLE);
                        binding.txtPrepare.setTextColor(Color.parseColor("#000000"));
                        binding.viewPrepare.setVisibility(View.INVISIBLE);
                        binding.txtComplete.setTextColor(Color.parseColor("#000000"));
                        binding.viewComplete.setVisibility(View.INVISIBLE);
                        getChildFragmentManager().beginTransaction().replace(R.id.frame, new NewOrderFragment()).commitAllowingStateLoss();

                    }
                }
        );

        binding.linePrepare.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        binding.txtNew.setTextColor(Color.parseColor("#000000"));
                        binding.viewNewOrder.setVisibility(View.INVISIBLE);
                        binding.txtPrepare.setTextColor(Color.parseColor("#f7b614"));
                        binding.viewPrepare.setVisibility(View.VISIBLE);
                        binding.txtComplete.setTextColor(Color.parseColor("#000000"));
                        binding.viewComplete.setVisibility(View.INVISIBLE);
                        getChildFragmentManager().beginTransaction().replace(R.id.frame, new PreparingOrderFragment()).commitAllowingStateLoss();

                    }
                }
        );

        binding.lineComplete.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        binding.txtNew.setTextColor(Color.parseColor("#000000"));
                        binding.viewNewOrder.setVisibility(View.INVISIBLE);
                        binding.txtPrepare.setTextColor(Color.parseColor("#000000"));
                        binding.viewPrepare.setVisibility(View.INVISIBLE);
                        binding.txtComplete.setTextColor(Color.parseColor("#f7b614"));
                        binding.viewComplete.setVisibility(View.VISIBLE);
                        getChildFragmentManager().beginTransaction().replace(R.id.frame, new CompleteOrderFragment()).commitAllowingStateLoss();

                    }
                }
        );

        binding.left.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       if(pageStatus==1){
                            binding.txtNew.setTextColor(Color.parseColor("#f7b614"));
                            binding.viewNewOrder.setVisibility(View.VISIBLE);
                            binding.txtPrepare.setTextColor(Color.parseColor("#000000"));
                            binding.viewPrepare.setVisibility(View.INVISIBLE);
                            binding.txtComplete.setTextColor(Color.parseColor("#000000"));
                            binding.viewComplete.setVisibility(View.INVISIBLE);
                            getChildFragmentManager().beginTransaction().replace(R.id.frame, new NewOrderFragment()).commitAllowingStateLoss();
                            pageStatus=0;
                            binding.left.setVisibility(View.GONE);
                        }else if(pageStatus==2){
                            binding.txtNew.setTextColor(Color.parseColor("#000000"));
                            binding.viewNewOrder.setVisibility(View.INVISIBLE);
                            binding.txtPrepare.setTextColor(Color.parseColor("#f7b614"));
                            binding.viewPrepare.setVisibility(View.VISIBLE);
                            binding.txtComplete.setTextColor(Color.parseColor("#000000"));
                            binding.viewComplete.setVisibility(View.INVISIBLE);
                            getChildFragmentManager().beginTransaction().replace(R.id.frame, new PreparingOrderFragment()).commitAllowingStateLoss();
                            pageStatus=1;
                            binding.right.setVisibility(View.VISIBLE);
                        }
                    }
                }
        );

        binding.right.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(pageStatus==0){
                            binding.txtNew.setTextColor(Color.parseColor("#000000"));
                            binding.viewNewOrder.setVisibility(View.INVISIBLE);
                            binding.txtPrepare.setTextColor(Color.parseColor("#f7b614"));
                            binding.viewPrepare.setVisibility(View.VISIBLE);
                            binding.txtComplete.setTextColor(Color.parseColor("#000000"));
                            binding.viewComplete.setVisibility(View.INVISIBLE);
                            getChildFragmentManager().beginTransaction().replace(R.id.frame, new PreparingOrderFragment()).commitAllowingStateLoss();
                            pageStatus=1;
                            binding.left.setVisibility(View.VISIBLE);
                        }else if(pageStatus==1){
                            binding.txtNew.setTextColor(Color.parseColor("#000000"));
                            binding.viewNewOrder.setVisibility(View.INVISIBLE);
                            binding.txtPrepare.setTextColor(Color.parseColor("#000000"));
                            binding.viewPrepare.setVisibility(View.INVISIBLE);
                            binding.txtComplete.setTextColor(Color.parseColor("#f7b614"));
                            binding.viewComplete.setVisibility(View.VISIBLE);
                            getChildFragmentManager().beginTransaction().replace(R.id.frame, new CompleteOrderFragment()).commitAllowingStateLoss();
                            pageStatus=2;
                            binding.right.setVisibility(View.GONE);
                        }
                    }
                }
        );

        loadCompletedOrder();
        loadProcessOrder();
    }

    private void loadCompletedOrder() {
        String ven = singleTask.getValue("vendor");
        Vendor vendor = new Gson().fromJson(ven, Vendor.class);
        MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
        Log.e("merchant_id", vendor.getId() + "");
        Call<JsonArray> call = myApi.getOrderwithoutDetails(vendor.getId(),"Delivered","0");
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
                            int count=jsonObject1.getInt("count");
                            countComplete.setText(count+"");
                           countComplete.setVisibility(View.VISIBLE);
                        } else {
                            HomeFragment.countComplete.setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
            }
        });

    }

    private void loadProcessOrder() {
        //ShowProgress.getShowProgress(getActivity()).show();
        String ven = singleTask.getValue("vendor");
        Vendor vendor = new Gson().fromJson(ven, Vendor.class);
//        Toast.makeText(getActivity(), "ID: "+vendor.getId(), Toast.LENGTH_SHORT).show();
        MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
        Log.e("merchant_id", vendor.getId() + "");
        Call<JsonArray> call = myApi.getOrder(vendor.getId(),"Prepared","0");
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    try {
                        Log.e("grgrr", response.body().toString());
                        JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        String res = jsonObject1.getString("res");

                        String message = jsonObject1.getString("message");
                        if (res.equals("success")) {

                            int count=jsonObject1.getInt("count");
                            HomeFragment.countPrepare.setText(count+"");
                            HomeFragment.countPrepare.setVisibility(View.VISIBLE);

                        } else {
                            HomeFragment.countPrepare.setVisibility(View.GONE);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {


            }
        });
    }
}