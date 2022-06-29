package digi.coders.capsicostorepartner.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.suke.widget.SwitchButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import digi.coders.capsicostorepartner.R;
import digi.coders.capsicostorepartner.activity.DashboardActivity;
import digi.coders.capsicostorepartner.activity.ManageProductActivity;
import digi.coders.capsicostorepartner.activity.MissedOrders;
import digi.coders.capsicostorepartner.adapter.OrderAdapter;
import digi.coders.capsicostorepartner.databinding.FragmentOfflineBinding;
import digi.coders.capsicostorepartner.helper.MyApi;
import digi.coders.capsicostorepartner.helper.NotificationService;
import digi.coders.capsicostorepartner.model.Merchant;
import digi.coders.capsicostorepartner.model.MyOrder;
import digi.coders.capsicostorepartner.model.MyOrder1;
import digi.coders.capsicostorepartner.model.ProgressDisplay;
import digi.coders.capsicostorepartner.model.ShowProgress;
import digi.coders.capsicostorepartner.model.Vendor;
import digi.coders.capsicostorepartner.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OfflineFragment extends Fragment {

    FragmentOfflineBinding binding;
    private SingleTask singleTask;
    double totalSales=0;
    int saleCount=0,orderCount=0,rejectStatus=0;
    public static int status=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentOfflineBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
            singleTask=(SingleTask)getActivity().getApplication();
        //startDuty
        loadProfile();
        loadCompletedOrder();
        getRejectedOrders();
        getMissedOrders();
//        binding.startDuty.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getParentFragmentManager().beginTransaction().replace(R.id.container,new HomeFragment()).commit();
//            }
//        });


        binding.switchButton.setOnCheckedChangeListener(
                new SwitchButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                        if(isChecked){
                            updateStatus("open");

                        }else{
                            updateStatus("close");

                        }
                    }
                }
        );

        binding.cardMissed.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(status==0) {
                            status=1;
                            startActivity(new Intent(getActivity(), MissedOrders.class));
                        }
                    }
                }
        );
        String ven = singleTask.getValue("vendor");
        Vendor vendor = new Gson().fromJson(ven, Vendor.class);

        if(vendor.getIsOpen().equalsIgnoreCase("open")){
            binding.switchButton.setChecked(true);
            binding.text.setText("You Are Online");
            Intent  inetnt=new Intent(getActivity(), NotificationService.class);
            inetnt.putExtra("title","online");
            getActivity().startService(inetnt);
//            getParentFragmentManager().beginTransaction().replace(R.id.container,new HomeFragment()).commit();
        }else{
            binding.switchButton.setChecked(false);
            binding.text.setText("Go Online");
            Intent  inetnt=new Intent(getActivity(), NotificationService.class);
            inetnt.putExtra("title","offline");
            getActivity().startService(inetnt);
//            getParentFragmentManager().beginTransaction().replace(R.id.container,new OfflineFragment()).commit();
        }

        binding.cardWallet.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getParentFragmentManager().beginTransaction().replace(R.id.container,new WalletFragment()).commit();
                    }
                }
        );

        binding.cardProducts.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ManageProductActivity.TransiStatus=1;
                        startActivity(new Intent(getActivity(), ManageProductActivity.class));
                    }
                }
        );

        binding.filterText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(getActivity(), binding.filterText);
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        if (R.id.today == item.getItemId()) {
                            saleCount=0;
                            loadCompletedOrder();
                            getRejectedOrders();
                            binding.txtFilter.setText("Today");
                        } else if (R.id.yesterday == item.getItemId()) {
                            saleCount=-1;
                            loadCompletedOrder();
                            getRejectedOrders();
                            binding.txtFilter.setText("Yesterday");
                        } else if (R.id.sevenDays == item.getItemId()) {
                            saleCount=-7;
                            loadCompletedOrder();
                            getRejectedOrders();
                            binding.txtFilter.setText("Last 7 Days");
                        } else if (R.id.thirtyDays == item.getItemId()) {
                            saleCount=-30;
                            loadCompletedOrder();
                            getRejectedOrders();
                            binding.txtFilter.setText("Last 30 Days");
                        } else if (R.id.allTime == item.getItemId()) {
                            saleCount=777;
                            loadCompletedOrder();
                            getRejectedOrders();
                            binding.txtFilter.setText("All Time");
                        }

                        return true;
                    }
                });
                popup.show();
            }
        });

//        Toast.makeText(getActivity(), vendor.getWallet()+"", Toast.LENGTH_SHORT).show();
        binding.walletAmount.setText("\u20b9 "+vendor.getWallet());

        if(!vendor.getWallet().equalsIgnoreCase("0")){
            binding.msgEarning.setVisibility(View.VISIBLE);
        }else{
            binding.msgEarning.setVisibility(View.GONE);
        }

//        binding.cardEarning.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getParentFragmentManager().beginTransaction()
//                        .replace(R.id.container,new WalletFragment()).commit();
//            }
//        });
//        binding.cardOrders.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getParentFragmentManager().beginTransaction()
//                        .replace(R.id.container,new HomeFragment()).commit();
//            }
//        });
//        binding.cardOrders.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getParentFragmentManager().beginTransaction()
//                        .replace(R.id.container,new HomeFragment()).commit();
//            }
//        });
//        binding.cardOrders.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getParentFragmentManager().beginTransaction()
//                        .replace(R.id.container,new HomeFragment()).commit();
//            }
//        });

    }
    private void updateStatus(String status) {
        String ven = singleTask.getValue("vendor");
        Vendor vendor = new Gson().fromJson(ven, Vendor.class);
        ShowProgress.getShowProgress(getActivity()).show();
        MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
        Call<JsonArray> call = myApi.merchantOpenStatus(vendor.getId(), status);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String res = jsonObject.getString("res");
                        String msg = jsonObject.getString("message");
                         if (res.equals("success")) {

                            if(status.equalsIgnoreCase("open")){
                                binding.text.setText("You Are Online");
                                getParentFragmentManager().beginTransaction().replace(R.id.container,new HomeFragment()).commit();
                                Intent  inetnt=new Intent(getActivity(), NotificationService.class);
                                inetnt.putExtra("title","online");
                                getActivity().startService(inetnt);
                            }else{
                                binding.text.setText("Go Online");
//                                getParentFragmentManager().beginTransaction().replace(R.id.container,new OfflineFragment()).commit();
                                Intent  inetnt=new Intent(getActivity(), NotificationService.class);
                                inetnt.putExtra("title","offline");
                                getActivity().startService(inetnt);
                            }
                            loadMyProfile();
                            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                ShowProgress.getShowProgress(getActivity()).hide();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
    private void loadProfile() {
        Log.e("ddf","dsd");
        String ven = singleTask.getValue("vendor");
        Vendor vendor = new Gson().fromJson(ven, Vendor.class);
        MyApi myApi=singleTask.getRetrofit().create(MyApi.class);
        Call<JsonArray> call=myApi.profile(vendor.getId());
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                try {

                    Log.e("ddf",response.body().toString());
                    JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String res = jsonObject.getString("res");
                    String mes = jsonObject.getString("message");
                    if (res.equals("success")) {
                        JSONArray jsonArray1 = jsonObject.getJSONArray("data");
                        JSONObject jsonObject1= jsonArray1.getJSONObject(0);
                        Merchant merchant = new Gson().fromJson(jsonObject1.toString(), Merchant.class);

                        singleTask.addValue("vendor", jsonObject1);

//                        binding.active.setText(merchant.getActive());
//                        binding.rejectedOrders.setText(merchant.getCancelled());


                        Log.e("ddf", merchant.getActive());

                    } else {

                    }
                } catch (JSONException e) {
                    Log.i("ddf e", e.getMessage());
                    Toast.makeText(getActivity(), e.getMessage() + "e", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage() +"t", Toast.LENGTH_SHORT).show();

            }
        });
    }
    private void loadCompletedOrder() {
        totalSales=0;
        orderCount=0;
        String ven = singleTask.getValue("vendor");
        Vendor vendor = new Gson().fromJson(ven, Vendor.class);
        MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
        Log.e("merchant_id", vendor.getId() + "");
        Call<JsonArray> call = myApi.getOrderwithoutDetails(vendor.getId(),"","0");
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        String res = jsonObject1.getString("res");


                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(new Date());
                        calendar.add(Calendar.DAY_OF_YEAR, saleCount);
                        Date current = calendar.getTime();


                        if (res.equals("success")) {
                            JSONArray jsonArray1=jsonObject1.getJSONArray("data");
                            for(int i=0;i<jsonArray1.length();i++)
                            {
                                MyOrder1 myOrder=new Gson().fromJson(jsonArray1.getJSONObject(i).toString(),MyOrder1.class);

                                if(saleCount==777) {
                                    orderCount=orderCount+1;
                                    if(myOrder.getOrderStatus().equalsIgnoreCase("Delivered")) {
                                        double amount = Double.parseDouble(myOrder.getSubtotal());

                                        totalSales = totalSales + amount;
                                    }
                                }else if(saleCount==0){
                                    Date orderdate = new SimpleDateFormat("yyyy-MM-dd").parse(myOrder.getCreatedAt());
                                    String currentString = new SimpleDateFormat("yyyy-MM-dd").format(current);
                                    Date currentDate=new SimpleDateFormat("yyyy-MM-dd").parse(currentString);
                                    if(currentDate.equals(orderdate)){
                                        if(myOrder.getOrderStatus().equalsIgnoreCase("Delivered")) {
                                            double amount = Double.parseDouble(myOrder.getSubtotal());

                                            totalSales = totalSales + amount;
                                        }
                                        orderCount = orderCount + 1;
                                    }
                                }else if(saleCount==-1){
                                    Date orderdate = new SimpleDateFormat("yyyy-MM-dd").parse(myOrder.getCreatedAt());
                                    String currentString = new SimpleDateFormat("yyyy-MM-dd").format(current);
                                    Date currentDate=new SimpleDateFormat("yyyy-MM-dd").parse(currentString);
                                     if(currentDate.equals(orderdate)){
                                         if(myOrder.getOrderStatus().equalsIgnoreCase("Delivered")) {
                                             double amount = Double.parseDouble(myOrder.getSubtotal());

                                             totalSales = totalSales + amount;
                                         }
                                         orderCount = orderCount + 1;
                                    }
                                }else if(saleCount==-7){
                                    Date orderdate = new SimpleDateFormat("yyyy-MM-dd").parse(myOrder.getCreatedAt());
                                    String currentString = new SimpleDateFormat("yyyy-MM-dd").format(current);
                                    Date currentDate=new SimpleDateFormat("yyyy-MM-dd").parse(currentString);
                                     if(currentDate.equals(orderdate)||currentDate.before(orderdate)){
                                         if(myOrder.getOrderStatus().equalsIgnoreCase("Delivered")) {
                                             double amount = Double.parseDouble(myOrder.getSubtotal());

                                             totalSales = totalSales + amount;
                                         }
                                         orderCount = orderCount + 1;
                                    }
                                }else if(saleCount==-30){
                                    Date orderdate = new SimpleDateFormat("yyyy-MM-dd").parse(myOrder.getCreatedAt());
                                    String currentString = new SimpleDateFormat("yyyy-MM-dd").format(current);
                                    Date currentDate=new SimpleDateFormat("yyyy-MM-dd").parse(currentString);
                                    if(currentDate.equals(orderdate)||currentDate.before(orderdate)){
                                        if(myOrder.getOrderStatus().equalsIgnoreCase("Delivered")) {
                                            double amount = Double.parseDouble(myOrder.getSubtotal());

                                            totalSales = totalSales + amount;
                                        }
                                        orderCount = orderCount + 1;
                                    }
                                }


                            }

                            DecimalFormat decim = new DecimalFormat("#.##");
                            binding.Orders.setText(orderCount+"");
                            binding.sales.setText("\u20b9 "+decim.format(totalSales));
                            double earn=(totalSales*Double.parseDouble(DashboardActivity.AdminCommission))/100;
                            earn=totalSales-earn;
                            binding.earning.setText("\u20b9 " + (decim.format(earn)));

                            binding.salesProgressBar.setVisibility(View.GONE);
                            binding.earningProgressBar.setVisibility(View.GONE);
                            binding.ordersProgressBar.setVisibility(View.GONE);
                            binding.rejectedProgressBar.setVisibility(View.GONE);
                            binding.walletProgressBar.setVisibility(View.GONE);
                            binding.missedProgressBar.setVisibility(View.GONE);
                            binding.sales.setVisibility(View.VISIBLE);
                            binding.earning.setVisibility(View.VISIBLE);
                            binding.Orders.setVisibility(View.VISIBLE);
                            binding.rejectedOrders.setVisibility(View.VISIBLE);
                            binding.walletAmount.setVisibility(View.VISIBLE);
                            binding.txtMissedAmount.setVisibility(View.VISIBLE);
                        } else {

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }
    private void getRejectedOrders() {
      rejectStatus=0;
        String ven = singleTask.getValue("vendor");
        Vendor vendor = new Gson().fromJson(ven, Vendor.class);
        MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
        Log.e("merchant_id", vendor.getId() + "");
        Call<JsonArray> call = myApi.getOrderwithoutDetails(vendor.getId(),"Rejected","0");
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        String res = jsonObject1.getString("res");


                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(new Date());
                        calendar.add(Calendar.DAY_OF_YEAR, saleCount);
                        Date current = calendar.getTime();


                        if (res.equals("success")) {
                            JSONArray jsonArray1=jsonObject1.getJSONArray("data");
                            for(int i=0;i<jsonArray1.length();i++)
                            {
                                MyOrder1 myOrder=new Gson().fromJson(jsonArray1.getJSONObject(i).toString(),MyOrder1.class);

                                if(saleCount==777) {
                                    rejectStatus=rejectStatus+1;
                                }else if(saleCount==0){
                                    Date orderdate = new SimpleDateFormat("yyyy-MM-dd").parse(myOrder.getCreatedAt());
                                    String currentString = new SimpleDateFormat("yyyy-MM-dd").format(current);
                                    Date currentDate=new SimpleDateFormat("yyyy-MM-dd").parse(currentString);
                                    if(currentDate.equals(orderdate)){
                                        rejectStatus=rejectStatus+1;
                                    }
                                }else if(saleCount==-1){
                                    Date orderdate = new SimpleDateFormat("yyyy-MM-dd").parse(myOrder.getCreatedAt());
                                    String currentString = new SimpleDateFormat("yyyy-MM-dd").format(current);
                                    Date currentDate=new SimpleDateFormat("yyyy-MM-dd").parse(currentString);
                                    if(currentDate.equals(orderdate)){
                                        rejectStatus=rejectStatus+1;
                                    }
                                }else if(saleCount==-7){
                                    Date orderdate = new SimpleDateFormat("yyyy-MM-dd").parse(myOrder.getCreatedAt());
                                    String currentString = new SimpleDateFormat("yyyy-MM-dd").format(current);
                                    Date currentDate=new SimpleDateFormat("yyyy-MM-dd").parse(currentString);
                                    if(currentDate.equals(orderdate)||currentDate.before(orderdate)){
                                        rejectStatus=rejectStatus+1;
                                    }
                                }else if(saleCount==-30){
                                    Date orderdate = new SimpleDateFormat("yyyy-MM-dd").parse(myOrder.getCreatedAt());
                                    String currentString = new SimpleDateFormat("yyyy-MM-dd").format(current);
                                    Date currentDate=new SimpleDateFormat("yyyy-MM-dd").parse(currentString);
                                    if(currentDate.equals(orderdate)||currentDate.before(orderdate)){
                                        rejectStatus=rejectStatus+1;
                                    }
                                }


                            }

                            binding.rejectedOrders.setText(rejectStatus+"");
                        } else {

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
            }
        });

    }
    private void loadMyProfile() {

        String ven = singleTask.getValue("vendor");
        Vendor vendor = new Gson().fromJson(ven, Vendor.class);
        MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
        Call<JsonArray> call = myApi.profile(vendor.getId());
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String res = jsonObject.getString("res");
                        String msg = jsonObject.getString("message");
                         if (res.equals("success")) {

                            JSONArray jsonArray1 = jsonObject.getJSONArray("data");
                            Vendor vendor1 = new Gson().fromJson(jsonArray1.getJSONObject(0).toString(), Vendor.class);
                            JSONObject jsonObject1 = jsonArray1.getJSONObject(0);
                            singleTask.addValue("vendor", jsonObject1);


                        } else {
                            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                ShowProgress.getShowProgress(getActivity()).hide();

            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                ShowProgress.getShowProgress(getActivity()).hide();

            }
        });
    }
//    @Override
//    public void onResume() {
//        super.onResume();
//        binding=FragmentOfflineBinding.inflate(getLayoutInflater());
//    }


    private void getMissedOrders() {
        String ven = singleTask.getValue("vendor");
        Vendor vendor = new Gson().fromJson(ven, Vendor.class);
        MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
        Log.e("merchant_id", vendor.getId() + "");
        Call<JsonArray> call = myApi.getOrderwithoutDetails(vendor.getId(),"Missed","0");
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
                        if (res.equals("success")) {

                            JSONArray jsonArray1=jsonObject1.getJSONArray("data");
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(new Date());
                            calendar.add(Calendar.DAY_OF_YEAR, saleCount);
                            Date current = calendar.getTime();

                            int missedCount=0;
                            double amount=0;
                            for(int i=0;i<jsonArray1.length();i++)
                            {
                                MyOrder1 myOrder=new Gson().fromJson(jsonArray1.getJSONObject(i).toString(),MyOrder1.class);

                                Date orderdate = new SimpleDateFormat("yyyy-MM-dd").parse(myOrder.getCreatedAt());
                                String currentString = new SimpleDateFormat("yyyy-MM-dd").format(current);
                                Date currentDate=new SimpleDateFormat("yyyy-MM-dd").parse(currentString);
                                if(currentDate.equals(orderdate)){
                                    missedCount=missedCount+1;
                                    amount=amount+Double.parseDouble(myOrder.getSubtotal());
                                }
                            }


                            binding.txtMissedCount.setText(""+missedCount+" Orders Missed\n"+rejectStatus+" Orders Rejected");
                            binding.txtMissedAmount.setText("\u20b9"+amount);

                        } else {

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("sdsd", e.getMessage());
                    }
                }

            }
            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });
    }
}