package digi.coders.capsicostorepartner.fragment;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import digi.coders.capsicostorepartner.databinding.FragmentProcessingOrderBinding;
import digi.coders.capsicostorepartner.helper.MyApi;
import digi.coders.capsicostorepartner.helper.RefereshLayout;
import digi.coders.capsicostorepartner.model.MyOrder;
import digi.coders.capsicostorepartner.model.ShowProgress;
import digi.coders.capsicostorepartner.model.Vendor;
import digi.coders.capsicostorepartner.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PreparingOrderFragment extends Fragment  implements RefereshLayout {

    FragmentProcessingOrderBinding binding;
    private SingleTask singleTask;
    private List<MyOrder> myOrderList;
    public static  RefereshLayout refereshLayout;

    FragmentManager fm;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentProcessingOrderBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }



    @Override
    public void onViewCreated(@NonNull  View view, @Nullable  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        refereshLayout=this;
        fm=getParentFragmentManager();
        singleTask=(SingleTask)getActivity().getApplication();

        binding.refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadProcessOrder();
                        binding.refresh.setRefreshing(false);
                    }
                },2000);
            }
        });
        loadProcessOrder();
    }
//    public static void  updateMethod()
//    {
//        new PreparingOrderFragment().loadProcessOrder();
//    }
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
                        Log.e("sdsd", jsonObject1.toString());
                        binding.progressBar.setVisibility(View.GONE);
                        if (res.equals("success")) {

                            int count=jsonObject1.getInt("count");
                            HomeFragment.countPrepare.setText(count+"");
                            HomeFragment.countPrepare.setVisibility(View.VISIBLE);

                            JSONArray jsonArray1=jsonObject1.getJSONArray("data");
                            myOrderList=new ArrayList<>();
                            for(int i=0;i<jsonArray1.length();i++)
                            {

                                MyOrder myOrder=new Gson().fromJson(jsonArray1.getJSONObject(i).toString(),MyOrder.class);
                                myOrderList.add(myOrder);

                            }
                            binding.preparingOrderList.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
                            OrderAdapter adapter=new OrderAdapter(2,myOrderList,singleTask,getActivity(),fm);
                            adapter.findPosition(new OrderAdapter.GetPosition() {
                                @Override
                                public void click() {
                                    loadProcessOrder();
                                }
                            });
                            binding.preparingOrderList.setAdapter(adapter);
                            if(myOrderList.size()>0){
                                binding.noTxt.setVisibility(View.GONE);
                                binding.preparingOrderList.setVisibility(View.VISIBLE);
                            }else{
                                binding.noTxt.setVisibility(View.VISIBLE);
                                binding.preparingOrderList.setVisibility(View.GONE);
                            }
                        } else {
                            HomeFragment.countPrepare.setVisibility(View.GONE);
                            binding.noTxt.setVisibility(View.VISIBLE);
                            binding.preparingOrderList.setVisibility(View.GONE);
                          //  loadProcessOrder();
//                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        binding.noTxt.setVisibility(View.VISIBLE);
                        binding.preparingOrderList.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                binding.noTxt.setVisibility(View.VISIBLE);
                binding.progressBar.setVisibility(View.GONE);
                binding.preparingOrderList.setVisibility(View.GONE);
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void referesh() {
        loadProcessOrder();
    }
}