package digi.coders.capsicostorepartner.adapter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.ncorti.slidetoact.SlideToActView;
import com.skydoves.elasticviews.ElasticButton;
import com.skydoves.elasticviews.ElasticImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

import digi.coders.capsicostorepartner.R;
import digi.coders.capsicostorepartner.activity.OrderDetailsActivity;
import digi.coders.capsicostorepartner.activity.TrackOrderActivity;
import digi.coders.capsicostorepartner.databinding.CancelReasonLayoutBinding;
import digi.coders.capsicostorepartner.databinding.DeliveredOrderLayoutBinding;
import digi.coders.capsicostorepartner.databinding.MissedOrderLayoutBinding;
import digi.coders.capsicostorepartner.databinding.NewOrdersLayoutBinding;
import digi.coders.capsicostorepartner.databinding.ProcessingOrderLayoutBinding;
import digi.coders.capsicostorepartner.fragment.HomeFragment;
import digi.coders.capsicostorepartner.fragment.PreparingOrderFragment;
import digi.coders.capsicostorepartner.helper.MyApi;
import digi.coders.capsicostorepartner.helper.NotificationUtils;
import digi.coders.capsicostorepartner.model.MyOrder;
import digi.coders.capsicostorepartner.model.ProgressDisplay;
import digi.coders.capsicostorepartner.model.Vendor;
import digi.coders.capsicostorepartner.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MissedOrderAdapter extends RecyclerView.Adapter<MissedOrderAdapter.MyHolder> {
    private int status;
    private Activity ctx;
    private List<MyOrder> myOrderList;
    private SingleTask singleTask;
    private MyOrder myOrder;
    String time;
    TextView editText;
    ElasticImageView plus,minus;
    ElasticButton ok_btn;
    MyOrder myOrder1;
    String orderId;
    AlertDialog alertDialog;

    CountDownTimer countDownTimer;
    public MissedOrderAdapter(int status) {
        this.status = status;
    }
    DecimalFormat decim ;
    ProgressDisplay progressDisplay;
    FragmentManager fm;
    public MissedOrderAdapter(int status, List<MyOrder> myOrderList, SingleTask singleTask, Activity ctx, FragmentManager fm) {
        this.status = status;
        this.myOrderList = myOrderList;
        this.singleTask = singleTask;
        this.ctx = ctx;
        this.fm = fm;
        progressDisplay=new ProgressDisplay((Activity)ctx);
        decim = new DecimalFormat("#.##");
    }

    @NonNull

    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.missed_order_layout, parent, false);
        return new MyHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, @SuppressLint("RecyclerView") int position) {

            MyOrder myOrder = myOrderList.get(position);
            if (myOrderList != null) {
                holder.binding1.orderid.setText("Order Id - " + myOrder.getOrderId());
                holder.binding1.paymentType.setText(myOrder.getMethod());
                double totalAmount=0;
                if(myOrder.getDeliveryTip().equalsIgnoreCase("")){
                    totalAmount=  Double.parseDouble(myOrder.getAmount())-Double.parseDouble(myOrder.getShippinCharge());
                }else{
                    totalAmount=  Double.parseDouble(myOrder.getAmount())-( Double.parseDouble(myOrder.getDeliveryTip())+Double.parseDouble(myOrder.getShippinCharge()));
                }

                if(!myOrder.getOtherCharge().equalsIgnoreCase("")){
                    totalAmount=  totalAmount-Double.parseDouble(myOrder.getOtherCharge());
                }


                holder.binding1.totalAmount.setText("₹ " + decim.format (totalAmount));
                 holder.binding1.date.setText(myOrder.getCreatedAt().split(" ")[1]);
                StringBuffer st = new StringBuffer("");
                StringBuffer t = new StringBuffer("");
//                String addproduct = "";
//                for (int i = 0; i < myOrder.getOrderproduct().length; i++) {
//                    t.append(myOrder.getOrderproduct()[i].getAddonproductname());
//                    String addProd = t.toString();
//                    addproduct = addProd.replace(",", ") (");
////                Toast.makeText(getApplicationContext(), ""+addproduct, Toast.LENGTH_SHORT).show();
//                }

                holder.binding1.itemsList.setLayoutManager(new LinearLayoutManager(ctx));
                holder.binding1.itemsList.setHasFixedSize(true);
                holder.binding1.itemsList.setAdapter(new OrderItemsAdapter(myOrder.getOrderproduct(),ctx,1));

//                for (int i = 0; i < myOrder.getOrderproduct().length; i++) {
//                    String addproduct="";
//                    if(myOrder.getOrderproduct()[i].getAddonproductname()!=null) {
//                         addproduct = myOrder.getOrderproduct()[i].getAddonproductname().toString().replace(",", ") (");
//                    }else{
//                         addproduct="";
//                    }
//                    if(addproduct!=null&&!addproduct.equalsIgnoreCase("null")&&!addproduct.equalsIgnoreCase("")) {
//
//                        st.append(myOrder.getOrderproduct()[i].getQty() + " × " + myOrder.getOrderproduct()[i].getName() + "\n(" +addproduct + ")\n\n");
//                    }else{
//                        st.append(myOrder.getOrderproduct()[i].getQty() + " × " + myOrder.getOrderproduct()[i].getName()  + "\n\n");
//
//                    }
//                }
//                holder.binding1.productNewOrder.setText(st);

                StringBuffer usename = new StringBuffer("");
                for (int v = 0; v < myOrder.getUser().length; v++) {
                    usename.append(myOrder.getUser()[v].getName());
                }
                holder.binding1.CustomerName.setText(usename);



                if (!myOrder.getMessage().isEmpty()) {
                    holder.binding1.specialInstruction.setText(myOrder.getMessage());
                } else {
                    Log.e("d", "ds");
                    holder.binding1.s.setVisibility(View.GONE);
                    holder.binding1.specialInstruction.setVisibility(View.GONE);
                }
                holder.binding1.btnOrderDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OrderDetailsActivity.myOrder =myOrderList.get(position);
//                        ctx.startActivity(new Intent(ctx, OrderDetailsActivity.class));
                        Intent intent = new Intent(ctx, OrderDetailsActivity.class);
                        intent.putExtra("order_id", myOrderList.get(position).getOrderId());
//                        Toast.makeText(ctx.getApplicationContext(), ""+myOrder.getOrderId(), Toast.LENGTH_SHORT).show();
                        ctx.startActivity(intent);
                    }
                });
            }



    }




    @Override
    public int getItemCount() {

        if (myOrderList != null) {
            //new order
            return myOrderList.size();
        } else {
            return 5;
        }

    }


    public class MyHolder extends RecyclerView.ViewHolder {
        MissedOrderLayoutBinding binding1;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
                binding1 = MissedOrderLayoutBinding.bind(itemView);

        }
    }


}
