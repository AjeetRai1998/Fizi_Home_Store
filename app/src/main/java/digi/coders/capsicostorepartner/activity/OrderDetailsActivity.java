package digi.coders.capsicostorepartner.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import digi.coders.capsicostorepartner.R;
import digi.coders.capsicostorepartner.adapter.OrderDetailAdapter;
import digi.coders.capsicostorepartner.adapter.OrderItemsAdapter;
import digi.coders.capsicostorepartner.adapter.ProductAdapter;
import digi.coders.capsicostorepartner.databinding.ActivityOrderDetailsBinding;
import digi.coders.capsicostorepartner.databinding.CancelReasonLayoutBinding;
import digi.coders.capsicostorepartner.helper.Constraints;
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

public class OrderDetailsActivity extends AppCompatActivity {

    ActivityOrderDetailsBinding binding;
    public static MyOrder myOrder;
    public static int position;
    private SingleTask singleTask;

    TextView order;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        singleTask = (SingleTask) getApplication();
        //handle back
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        order = findViewById(R.id.order);
        order.setText(myOrder.getUser()[0].getName());

        //load neworder item list
        setData();

        //accept order
        /*binding.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShowProgress.getShowProgress(OrderDetailsActivity.this).show();
                String ven = singleTask.getValue("vendor");
                Vendor vendor = new Gson().fromJson(ven, Vendor.class);
                MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
                Log.e("merchant_id", vendor.getId() + "");
                Call<JsonArray> call = myApi.orderStatus(myOrder.getOrderId(),vendor.getId(),"Prepared","");
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
                                ShowProgress.getShowProgress(OrderDetailsActivity.this).hide();
                                if (res.equals("success")) {
                                    Toast.makeText(OrderDetailsActivity.this, "Order Accepted", Toast.LENGTH_SHORT).show();Toast.makeText(OrderDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
                                } else {

                                    ShowProgress.getShowProgress(OrderDetailsActivity.this).hide();
                                    Toast.makeText(OrderDetailsActivity.this," Delivery not Available", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        ShowProgress.getShowProgress(OrderDetailsActivity.this).hide();
                        Toast.makeText(OrderDetailsActivity.this,t.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });*/
        //reject order
       /* binding.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder aler = new AlertDialog.Builder(OrderDetailsActivity.this);
                AlertDialog alertDialog = aler.create();
                View view = LayoutInflater.from(OrderDetailsActivity.this).inflate(R.layout.cancel_reason_layout, null);
                CancelReasonLayoutBinding binding = CancelReasonLayoutBinding.bind(view);
                binding.close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                binding.submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String reason=binding.reason.getText().toString();
                        if(reason.isEmpty())
                        {
                            binding.reason.setError("Please Mention Reason");
                            binding.reason.requestFocus();
                        }
                        else
                        {
                            ShowProgress.getShowProgress(OrderDetailsActivity.this).show();
                            String ven = singleTask.getValue("vendor");
                            Vendor vendor = new Gson().fromJson(ven, Vendor.class);
                            MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
                            Log.e("merchant_id", vendor.getId() + "");
                            Call<JsonArray> call = myApi.orderStatus(myOrder.getOrderId(),vendor.getId(),"Rejected",reason);
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
                                            ShowProgress.getShowProgress(OrderDetailsActivity.this).hide();
                                            if (res.equals("success")) {
                                                Toast.makeText(OrderDetailsActivity.this, message, Toast.LENGTH_SHORT).show();Toast.makeText(OrderDetailsActivity.this, message, Toast.LENGTH_SHORT).show();

                                                alertDialog.dismiss();
                                            } else {

                                                ShowProgress.getShowProgress(OrderDetailsActivity.this).hide();
                                                Toast.makeText(OrderDetailsActivity.this, "Delivery Boy Not Available", Toast.LENGTH_SHORT).show();
                                                alertDialog.dismiss();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<JsonArray> call, Throwable t) {
                                    ShowProgress.getShowProgress(OrderDetailsActivity.this).hide();
                                    Toast.makeText(OrderDetailsActivity.this,t.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            });


                        }

                    }
                });
                alertDialog.setView(view);
                alertDialog.show();
            }
        });*/
    }

    private void setData() {
        if (myOrder != null) {
            String ven = singleTask.getValue("vendor");
            Vendor vendor = new Gson().fromJson(ven, Vendor.class);
            User user = myOrder.getUser()[0];
            binding.userName.setText(user.getName());
            binding.userPhoneNo.setText(user.getMobile());
            binding.userEmail.setText(user.getEmail());
            binding.storeAddress.setText(vendor.getAddress());
            binding.userAddrss.setText(myOrder.getAddress()[0].getAddress());
            binding.paymentmethoddd.setText(myOrder.getMethod());

            binding.subtotal.setText("₹ " + myOrder.getSubtotal());
            binding.coupon.setText("₹ " + myOrder.getCouponDiscount());
            binding.taxes.setText("₹ " + myOrder.getOtherCharge());
            binding.otherCharge.setText("₹ " + vendor.getPacking_charge());
            binding.shipingCharge.setText("₹ " + myOrder.getShippinCharge());
            binding.deliveryTip.setText("₹ " + myOrder.getDeliveryTip());
            binding.packingCharge.setText("₹ " + vendor.getPacking_charge());
            binding.totalAmount.setText("₹" + myOrder.getAmount());
            binding.items.setText(myOrder.getOrderproduct().length+" ITEMS");

            binding.itemsList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            binding.itemsList.setHasFixedSize(true);
            binding.itemsList.setAdapter(new OrderItemsAdapter(myOrder.getOrderproduct(),getApplicationContext(),1));

            StringBuffer tt=new StringBuffer("");
            for(int i=0;i<myOrder.getOrderproduct().length;i++)
            {
                String addproduct="";
                if(myOrder.getOrderproduct()[i].getAddonproductname()!=null) {
                    addproduct = myOrder.getOrderproduct()[i].getAddonproductname().toString().replace(",", ") (");
                }else{
                    addproduct="";
                }

                if(addproduct!=null&&!addproduct.equalsIgnoreCase("null")&&!addproduct.equalsIgnoreCase("")) {

                    tt.append(myOrder.getOrderproduct()[i].getQty() + " × " + myOrder.getOrderproduct()[i].getName() + "\n Size \n(" + addproduct + ")" + "\n");
                }else{
                    tt.append(myOrder.getOrderproduct()[i].getQty() + " × " + myOrder.getOrderproduct()[i].getName()  + "\n");

                }
            }
            binding.item.setText(tt);

            StringBuffer ee=new StringBuffer("");
            for(int a=0;a<myOrder.getOrderproduct().length;a++)
            {
                double p=Double.parseDouble(myOrder.getOrderproduct()[a].getSellPrice())*Double.parseDouble(myOrder.getOrderproduct()[a].getQty());
                p=p+Double.parseDouble(myOrder.getOrderproduct()[a].getAddonproduct_prize1());
                ee.append("₹"+p+"\n");
                Log.d("Abhishek",myOrder.getOrderproduct()[a].getAddonproduct_prize1());
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


            /*if (!myOrder.getOrderproduct()[0].getSpecial_intersections().isEmpty())
            {
                binding.specialInstruction.setText(myOrder.getOrderproduct()[0].getSpecial_intersections());
            } else {
                Log.e("d", "ds");
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
            if (!myOrder.getMessage().isEmpty()) {
                binding.deliveryInstruction.setText(myOrder.getMessage());
            } else {
                binding.de.setVisibility(View.GONE);
                binding.deliveryInstruction.setVisibility(View.GONE);
            }
            if (myOrder.getOrderproduct()[0].getAddonproductname()!=null) {
                binding.addOnItems.setText(myOrder.getOrderproduct()[0].getAddonproductname());
            } else {
                binding.addOnItems.setVisibility(View.GONE);
                binding.ad.setVisibility(View.GONE);
            }


            binding.callToRider.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!myOrder.getDeliveryBoyId().equals("0")) {
                        if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + myOrder.getDeliveryBoy()[0].getMobile()));
                            startActivity(intent);
                        }
                    } else {
                        binding.callToRider.setVisibility(View.GONE);
                        //Toast.makeText(OrderDetailsActivity.this, "Delivery Boy not assigned Yet", Toast.LENGTH_SHORT).show();
                    }
                }
            });

//            loadNewOrder(myOrder);

        }

    }

    private void loadNewOrder(MyOrder myOrder) {
        binding.newOrderItemList.setLayoutManager(new LinearLayoutManager(OrderDetailsActivity.this, LinearLayoutManager.VERTICAL, false));
        binding.newOrderItemList.setAdapter(new OrderDetailAdapter(myOrder));
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        } else {
            //requestPermissions(new String[]{Manifest.permission.CALL_PHONE},100);
        }
    }
}