package digi.coders.capsicostorepartner.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import digi.coders.capsicostorepartner.R;
import digi.coders.capsicostorepartner.adapter.OrderAdapter;
import digi.coders.capsicostorepartner.adapter.OrderDetailAdapter;
import digi.coders.capsicostorepartner.adapter.ProductAdapter;
import digi.coders.capsicostorepartner.databinding.ActivityOrderStatusBinding;
import digi.coders.capsicostorepartner.fragment.HomeFragment;
import digi.coders.capsicostorepartner.helper.MyApi;
import digi.coders.capsicostorepartner.model.MyOrder;
import digi.coders.capsicostorepartner.model.Orderproduct;
import digi.coders.capsicostorepartner.model.ShowProgress;
import digi.coders.capsicostorepartner.model.User;
import digi.coders.capsicostorepartner.model.Vendor;
import digi.coders.capsicostorepartner.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderStatusActivity extends AppCompatActivity {

    ActivityOrderStatusBinding binding;
    public static MyOrder myOrder;
    public static int position;
    private SingleTask singleTask;
    String orderfhdf_id;


    TextView toolbarOrderText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityOrderStatusBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        singleTask=(SingleTask)getApplication();

        //handle back
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        orderfhdf_id=getIntent().getStringExtra("orderhdfhId");
//        Log.e("kjieyt", getIntent().getStringExtra("orderhdfhId") );
        binding.toolbarOrderText.setText("Order Id - "+orderfhdf_id);
        //load item list
        setData();
//        loadProcessOrder();

//        binding.callToCustomer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(checkSelfPermission(Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED)
//                {
//                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE},100);
//                    return;
//                }
//                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + myOrder.getUser()[0].getMobile()));
//                startActivity(intent);
//            }
//        });


        binding.callToMerchant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkSelfPermission(Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE},100);
                    return;
                }
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + myOrder.getDeliveryBoy()[0].getMobile()));
                startActivity(intent);
            }
        });
        //
       /* binding.markAsDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowProgress.getShowProgress(OrderStatusActivity.this).show();
                String ven = singleTask.getValue("vendor");
                Vendor vendor = new Gson().fromJson(ven, Vendor.class);
                MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
                Log.e("merchant_id", vendor.getId() + "");
                Call<JsonArray> call = myApi.orderStatus(myOrder.getOrderId(),vendor.getId(),"OrderReady","");
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
                                ShowProgress.getShowProgress(OrderStatusActivity.this).hide();
                                if (res.equals("success")) {
                                    Toast.makeText(OrderStatusActivity.this, message, Toast.LENGTH_SHORT).show();
                                } else {

                                    ShowProgress.getShowProgress(OrderStatusActivity.this).hide();
                                    Toast.makeText(OrderStatusActivity.this, "Delivery Boy Not Available", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        ShowProgress.getShowProgress(OrderStatusActivity.this).hide();
                        Toast.makeText(OrderStatusActivity.this,t.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
        binding.trackRider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrderStatusActivity.this,TrackRiderActivity.class));

            }
        });*/

    }

    private void loadProcessOrder() {
        //ShowProgress.getShowProgress(getActivity()).show();
        String ven = singleTask.getValue("vendor");
        Vendor vendor = new Gson().fromJson(ven, Vendor.class);
        MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
        Log.e("order_idnew", orderfhdf_id);

        Call<JsonArray> call = myApi.fullOrderDetails(vendor.getId(),orderfhdf_id);
        Log.e("kjieyt", orderfhdf_id);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                if (response.isSuccessful()) {
                    try {
                        Log.e("nkdnhd", response.body().toString() );
                        JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        String res = jsonObject1.getString("res");
                        String message = jsonObject1.getString("message");

                       /* Log.e("sdsd", jsonObject1.toString());
                        TabLayout.Tab tab0 = HomeFragment.tabLayout.getTabAt(1);
                        BadgeDrawable badge0 = tab0.getOrCreateBadge();
                        //ShowProgress.getShowProgress(getActivity()).hide();
                        binding.progressBar.setVisibility(View.GONE);
                        if (res.equals("success")) {

                            int count=jsonObject1.getInt("count");

                            badge0.setNumber(count);
                            badge0.setBackgroundColor(Color.RED);
                            badge0.setBadgeTextColor(Color.WHITE);

                            JSONArray jsonArray1=jsonObject1.getJSONArray("data");
                            myOrderList=new ArrayList<>();
                            for(int i=0;i<jsonArray1.length();i++)
                            {

                                MyOrder myOrder=new Gson().fromJson(jsonArray1.getJSONObject(i).toString(),MyOrder.class);
                                myOrderList.add(myOrder);

                            }
                            binding.preparingOrderList.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
                            OrderAdapter adapter=new OrderAdapter(2,myOrderList,singleTask);
                            adapter.findPosition(new OrderAdapter.GetPosition() {
                                @Override
                                public void click() {
                                    loadProcessOrder();
                                }
                            });
                            binding.preparingOrderList.setAdapter(adapter);
                        } else {
                            badge0.setVisible(false);
                            //ShowProgress.getShowProgress(getActivity()).hide();
                            binding.noTxt.setVisibility(View.VISIBLE);
                            //  loadProcessOrder();
                            //Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        }*/
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

//                binding.progressBar.setVisibility(View.GONE);
//                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void setData() {
        if(myOrder!=null) {
            String ven = singleTask.getValue("vendor");
            Vendor vendor = new Gson().fromJson(ven, Vendor.class);
            User user = myOrder.getUser()[0];
            binding.userName.setText(user.getName());
            binding.userPhoneNo.setText(user.getMobile());
            binding.userEmail.setText(user.getEmail());
            binding.storeAddress.setText(vendor.getAddress());
            binding.userAddrss.setText(myOrder.getAddress()[0].getAddress());
            binding.totalAmount.setText("₹" + myOrder.getAmount());
            binding.paymentmethoddd.setText(myOrder.getMethod());

            binding.subtotal.setText("₹ "+myOrder.getSubtotal());
            binding.coupon.setText("₹ "+myOrder.getCouponDiscount());
            binding.otherCharge.setText("₹ "+myOrder.getOtherCharge());
            binding.shipingCharge.setText("₹ "+myOrder.getShippinCharge());
            binding.deliveryTip.setText("₹ "+myOrder.getDeliveryTip());
            StringBuffer st=new StringBuffer("");
            StringBuffer tt=new StringBuffer("");
            for(int i=0;i<myOrder.getOrderproduct().length;i++)
            {
                tt.append(myOrder.getOrderproduct()[i].getQty()+" × " +myOrder.getOrderproduct()[i].getName()+"\n");
            }
            binding.item.setText(tt);

            StringBuffer ee=new StringBuffer("");
            for(int a=0;a<myOrder.getOrderproduct().length;a++)
            {
                ee.append("₹"+Double.parseDouble(myOrder.getOrderproduct()[a].getSellPrice())*Double.parseDouble(myOrder.getOrderproduct()[a].getQty())+"\n");
            }
            binding.pricc.setText(ee);

            StringBuffer t=new StringBuffer("");
            String addproduct = "";
            for(int i=0;i<myOrder.getOrderproduct().length;i++)
            {
                t.append(myOrder.getOrderproduct()[i].getAddonproductname());
                String addProd=t.toString();
                addproduct=addProd.replace(",","\n");
//                Toast.makeText(getApplicationContext(), ""+addproduct, Toast.LENGTH_SHORT).show();
            }
            binding.addonItem.setText(addproduct);

            StringBuffer e=new StringBuffer("");
            String addPric="";
            for(int a=0;a<myOrder.getOrderproduct().length;a++)
            {
                e.append(myOrder.getOrderproduct()[a].getAddonproduct_prize());
                String addpric=e.toString();
                addPric="₹"+addpric.replace(",","\n"+"₹");
            }
            binding.addonPricc.setText(addPric);

            /*if(!myOrder.getOrderproduct()[0].getSpecial_intersections().isEmpty())
            {
                binding.specialInstruction.setText(myOrder.getOrderproduct()[0].getSpecial_intersections());

            }
            else
            {
                Log.e("d","ds");
                binding.s.setVisibility(View.GONE);
                binding.specialInstruction.setVisibility(View.GONE);
            }*/
            if (!myOrder.getMessage().isEmpty()) {
                binding.specialInstruction.setText(myOrder.getMessage());
            } else {
                Log.e("d", "ds");
                binding.s.setVisibility(View.GONE);
                binding.specialInstruction.setVisibility(View.GONE);
            }
            if(!myOrder.getMessage().isEmpty())
            {
                binding.deliveryInstruction.setText(myOrder.getMessage());
            }
            else
            {
                binding.de.setVisibility(View.GONE);
                binding.deliveryInstruction.setVisibility(View.GONE);
            }
            try {
                if (!myOrder.getOrderproduct()[0].getAddonproductname().isEmpty()) {
                    binding.addOnItems.setText(myOrder.getOrderproduct()[0].getAddonproductname());
                } else {
                    binding.addOnItems.setVisibility(View.GONE);
                    binding.ad.setVisibility(View.GONE);
                }
            }catch (Exception q){

            }
           // loadItemList(myOrder);
        }
    }

    private void loadItemList(MyOrder myOrder) {

        binding.itemList.setNestedScrollingEnabled(false);
        binding.itemList.setLayoutManager(new LinearLayoutManager(OrderStatusActivity.this,LinearLayoutManager.VERTICAL,false));
        binding.itemList.setAdapter(new OrderDetailAdapter(myOrder));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==100 &&grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {

        }
        else
        {
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE},100);
        }
    }
}