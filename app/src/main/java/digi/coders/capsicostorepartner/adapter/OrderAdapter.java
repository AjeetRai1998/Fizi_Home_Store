package digi.coders.capsicostorepartner.adapter;

import static digi.coders.capsicostorepartner.helper.NotificationUtils.vibrator;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorRes;
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
import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import digi.coders.capsicostorepartner.R;
import digi.coders.capsicostorepartner.activity.OrderDetailsActivity;
import digi.coders.capsicostorepartner.activity.OrderHistoryActivity;
import digi.coders.capsicostorepartner.activity.OrderStatusActivity;
import digi.coders.capsicostorepartner.activity.OrderSummaryActivity;
import digi.coders.capsicostorepartner.activity.TrackOrderActivity;
import digi.coders.capsicostorepartner.activity.TrackRiderActivity;
import digi.coders.capsicostorepartner.databinding.CancelReasonLayoutBinding;
import digi.coders.capsicostorepartner.databinding.DeliveredOrderLayoutBinding;
import digi.coders.capsicostorepartner.databinding.NewOrdersLayoutBinding;
import digi.coders.capsicostorepartner.databinding.ProcessingOrderLayoutBinding;
import digi.coders.capsicostorepartner.fragment.HomeFragment;
import digi.coders.capsicostorepartner.fragment.NewOrderFragment;
import digi.coders.capsicostorepartner.fragment.PreparingOrderFragment;
import digi.coders.capsicostorepartner.helper.MyApi;
import digi.coders.capsicostorepartner.helper.NotificationUtils;
import digi.coders.capsicostorepartner.model.MyOrder;
import digi.coders.capsicostorepartner.model.ProgressDisplay;
import digi.coders.capsicostorepartner.model.ShowProgress;
import digi.coders.capsicostorepartner.model.User;
import digi.coders.capsicostorepartner.model.Vendor;
import digi.coders.capsicostorepartner.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyHolder> {
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
    GetPosition getPosition;

    CountDownTimer countDownTimer;
    public OrderAdapter(int status) {
        this.status = status;
    }
    DecimalFormat decim ;
    ProgressDisplay progressDisplay;
    FragmentManager fm;
    String strReason="",strAcceptReson="";
    public OrderAdapter(int status, List<MyOrder> myOrderList, SingleTask singleTask,Activity ctx,FragmentManager fm) {
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
        if (status == 1) {
            //new order
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_orders_layout, parent, false);
        } else if (status == 2) {
            //processorder
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.processing_order_layout, parent, false);
        } else {
            //delivered order
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.delivered_order_layout, parent, false);

        }
        return new MyHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, @SuppressLint("RecyclerView") int position) {
        if (status == 1) {
            MyOrder myOrder = myOrderList.get(position);
            holder.binding1.btnOrderDetails.setVisibility(View.INVISIBLE);
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
                holder.binding1.itemsList.setAdapter(new OrderItemsAdapter(myOrder.getOrderproduct(),ctx,0));

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


        } else if (status == 2) {


            long milliseconds = 0;
            MyOrder myOrder = myOrderList.get(position);
            if (myOrderList != null) {

                try {
//                    Date date = java.util.Calendar.getInstance().getTime();
//                    Date date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(myOrder.getAcceptedTIme());
////                    Date date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(dtf.format(now));
//                    double deTIme = Double.parseDouble(myOrder.getLefttime());
//
//                    Duration remainTIme = Duration.between(date.toInstant(), date1.toInstant());
//                    long minuts = remainTIme.toMillis();
////                    long minuts=  TimeUnit.MILLISECONDS.toMinutes(remainTIme);
//                    minuts = minuts - (720*60*1000);
//                    double mainTIme = deTIme + 0;
//                    int finalTime = (int) ((mainTIme*60*1000) - Math.abs(minuts));


                    double a = Double.parseDouble(myOrder.getLefttime());
                    //Toast.makeText(ctx, "LeftTime:" +a, Toast.LENGTH_SHORT).show();
                     milliseconds = (long) (a * (60 * 1000));
                    holder.binding2.orderid.setText("Order Id: " + myOrder.getOrderId());
                    holder.binding2.paymentType.setText(myOrder.getMethod());
                    if(myOrder.getDelivery_boy_status_accepted().equalsIgnoreCase("true")) {
                        holder.binding2.deliveryBoyName.setText(myOrder.getDeliveryBoy()[0].getName() + " has assigned to deliver this order.");
                        holder.binding2.lineDelivery.setVisibility(View.VISIBLE);
                    }else{
                        holder.binding2.lineDelivery.setVisibility(View.GONE);
                    }
                    double totalAmount=0;
                    if(myOrder.getDeliveryTip().equalsIgnoreCase("")){
                        totalAmount=  Double.parseDouble(myOrder.getAmount())-Double.parseDouble(myOrder.getShippinCharge());
                    }else{
                        totalAmount=  Double.parseDouble(myOrder.getAmount())-( Double.parseDouble(myOrder.getDeliveryTip())+Double.parseDouble(myOrder.getShippinCharge()));
                    }

                    if(!myOrder.getOtherCharge().equalsIgnoreCase("")){
                        totalAmount=  totalAmount-Double.parseDouble(myOrder.getOtherCharge());
                    }


                    holder.binding2.totalAmount.setText("₹ " + decim.format (totalAmount));
                     holder.binding2.date.setText(myOrder.getCreatedAt().split(" ")[0]);
                    if (!myOrder.getLefttime().isEmpty() && milliseconds > 0) {
                        holder.binding2.lineTime.setVisibility(View.VISIBLE);
                     countDownTimer=   new CountDownTimer(milliseconds, 1000) {
                            public void onTick(long millisUntilFinished) {
                                holder.binding2.timer.setText("" + String.format("%d min: %d sec",
                                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                            }

                            public void onFinish() {
                                holder.binding2.lineReady.setVisibility(View.VISIBLE);
                                holder.binding2.lineTime.setVisibility(View.GONE);
                                holder.binding2.txtFood.setVisibility(View.GONE);
                                Glide.with(ctx).load(R.raw.time_order).into(holder.binding2.imgCheck);
                            }
                        }.start();


                    } else {
                        holder.binding2.lineReady.setVisibility(View.VISIBLE);
                        holder.binding2.lineTime.setVisibility(View.GONE);
                        holder.binding2.txtFood.setVisibility(View.GONE);
                        Glide.with(ctx).load(R.raw.time_order).into(holder.binding2.imgCheck);


                    }
                }catch (Exception e){

                }
                if (myOrder.getOrderStatus().equals("Picked")) {
                    holder.binding2.markAsDone.setVisibility(View.GONE);
                    holder.binding2.Done.setVisibility(View.GONE);
                    holder.binding2.otp.setVisibility(View.GONE);
                    holder.binding2.txtOtp.setText("Order is on the way\nAnd will be delivered soon to the customer");
                    holder.binding2.txtOtp.setTextColor(Color.parseColor("#44AC48"));
                    holder.binding2.lineOtp.setVisibility(View.VISIBLE);
                    holder.binding2.lineReady.setVisibility(View.VISIBLE);
                    holder.binding2.txtFood.setVisibility(View.VISIBLE);
                    holder.binding2.lineTime.setVisibility(View.GONE);
                    Glide.with(ctx).load(R.raw.check).into(holder.binding2.imgCheck);
                }else if (myOrder.getOrderStatus().equals("Prepared")) {
                    holder.binding2.markAsDone.setVisibility(View.GONE);
                    holder.binding2.Done.setVisibility(View.GONE);
                    holder.binding2.otp.setText(myOrder.getOrderId().substring(myOrder.getOrderId().length()-4));
                    holder.binding2.lineOtp.setVisibility(View.VISIBLE);
                    holder.binding2.lineReady.setVisibility(View.VISIBLE);
                    holder.binding2.txtFood.setVisibility(View.VISIBLE);
                    holder.binding2.lineTime.setVisibility(View.GONE);
                    Glide.with(ctx).load(R.raw.check).into(holder.binding2.imgCheck);
                }else  if (myOrder.getOrderStatus().equals("Accepted")) {
                    holder.binding2.markAsDone.setVisibility(View.VISIBLE);
                    holder.binding2.Done.setVisibility(View.GONE);
                    holder.binding2.lineOtp.setVisibility(View.GONE);
                    holder.binding2.lineReady.setVisibility(View.GONE);
                    holder.binding2.lineTime.setVisibility(View.VISIBLE);
                    Glide.with(ctx).load(R.raw.check).into(holder.binding2.imgCheck);

                    if (!myOrder.getLefttime().isEmpty() && milliseconds > 0) {
                        holder.binding2.lineTime.setVisibility(View.VISIBLE);
                        holder.binding2.lineReady.setVisibility(View.GONE);
                        holder.binding2.txtFood.setVisibility(View.GONE);
                        Glide.with(ctx).load(R.raw.check).into(holder.binding2.imgCheck);
                    }else{
                        holder.binding2.lineTime.setVisibility(View.GONE);
                        holder.binding2.lineReady.setVisibility(View.VISIBLE);
                        holder.binding2.txtFood.setVisibility(View.GONE);
                        Glide.with(ctx).load(R.raw.time_order).into(holder.binding2.imgCheck);
                    }


                } else if (myOrder.getOrderStatus().equals("Preparing")) {
                    holder.binding2.markAsDone.setVisibility(View.VISIBLE);
                    holder.binding2.Done.setVisibility(View.GONE);
                    holder.binding2.lineOtp.setVisibility(View.GONE);

                    holder.binding2.txtFood.setVisibility(View.GONE);

                    if (!myOrder.getLefttime().isEmpty() && milliseconds > 0) {
                        holder.binding2.lineTime.setVisibility(View.VISIBLE);
                        holder.binding2.lineReady.setVisibility(View.GONE);
                        holder.binding2.txtFood.setVisibility(View.GONE);
                        Glide.with(ctx).load(R.raw.check).into(holder.binding2.imgCheck);
                    }else{
                        holder.binding2.lineTime.setVisibility(View.GONE);
                        holder.binding2.lineReady.setVisibility(View.VISIBLE);
                        holder.binding2.txtFood.setVisibility(View.GONE);
                        Glide.with(ctx).load(R.raw.time_order).into(holder.binding2.imgCheck);
                    }
                }

                if(myOrder.getDelivery_boy_status_accepted().equalsIgnoreCase("true")) {
                    holder.binding2.trackRider.setVisibility(View.VISIBLE);
                }else{
                    holder.binding2.trackRider.setVisibility(View.GONE);
                }

//                StringBuffer st = new StringBuffer("");
//                for (int i = 0; i < myOrder.getOrderproduct().length; i++) {
//                    st.append(myOrder.getOrderproduct()[i].getQty() + " × " + myOrder.getOrderproduct()[i].getName() + "\n\n");
//                }
//                holder.binding2.product.setText(st);

                holder.binding2.itemsList.setLayoutManager(new LinearLayoutManager(ctx));
                holder.binding2.itemsList.setHasFixedSize(true);
                holder.binding2.itemsList.setAdapter(new OrderItemsAdapter(myOrder.getOrderproduct(),ctx,2));
                StringBuffer usename = new StringBuffer("");
                for (int v = 0; v < myOrder.getUser().length; v++) {
                    usename.append(myOrder.getUser()[v].getName());
                }
                holder.binding2.custmName.setText(usename);

                StringBuffer t = new StringBuffer("");
//                String addproduct = "";
//                for (int i = 0; i < myOrder.getOrderproduct().length; i++) {
//                    t.append(myOrder.getOrderproduct()[i].getAddonproductname());
//                    String addProd = t.toString();
//                    addproduct = addProd.replace(",", "\n");
////                Toast.makeText(getApplicationContext(), ""+addproduct, Toast.LENGTH_SHORT).show();
//                }
//                holder.binding2.addonOrder.setText(addproduct);

            }
            if (!myOrder.getMessage().isEmpty()) {
                holder.binding2.specialInstruction.setText(myOrder.getMessage());
            } else {
                Log.e("d", "ds");
                holder.binding2.s.setVisibility(View.GONE);
                holder.binding2.specialInstruction.setVisibility(View.GONE);
            }

            holder.binding2.processingOrderBtn.setOnClickListener(new View.OnClickListener() {
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
        } else {
            if (myOrderList != null) {
                myOrder = myOrderList.get(position);
                holder.binding3.orderid.setText("Order Id - " + myOrder.getOrderId());
                holder.binding3.paymentType.setText(myOrder.getMethod());
                double totalAmount=0;
                if(myOrder.getDeliveryTip().equalsIgnoreCase("")){
                  totalAmount=  Double.parseDouble(myOrder.getAmount())-Double.parseDouble(myOrder.getShippinCharge());
                }else{
                    totalAmount=  Double.parseDouble(myOrder.getAmount())-( Double.parseDouble(myOrder.getDeliveryTip())+Double.parseDouble(myOrder.getShippinCharge()));
                }

                if(!myOrder.getOtherCharge().equalsIgnoreCase("")){
                    totalAmount=  totalAmount-Double.parseDouble(myOrder.getOtherCharge());
                }


                holder.binding3.totalAmount.setText("₹ " + decim.format (totalAmount));
                holder.binding3.preparingTIme.setText("Preparing Time : " + myOrder.getDelivery_time()+" min");
                holder.binding3.preparedTIme.setText("You took " + (int)Double.parseDouble(myOrder.getPreparedtime())+" min to prepare this order");
//                holder.binding3.date.setText(myOrder.getCreatedAt().split(" ")[0]);
                try {
                    Date date = new SimpleDateFormat("yyyy-MM-dd").parse(myOrder.getCreatedAt().split(" ")[0]);
                    String dtt=new SimpleDateFormat("dd MMM, yyyy").format(date);
                    holder.binding3.date.setText(dtt);
                }catch (Exception e){

                }

//                StringBuffer st = new StringBuffer("");
//                for (int i = 0; i < myOrder.getOrderproduct().length; i++) {
//                    st.append(myOrder.getOrderproduct()[i].getQty() + " × " + myOrder.getOrderproduct()[i].getName() + "\n\n");
//                }
//                holder.binding3.product.setText(st);

                holder.binding3.itemsList.setLayoutManager(new LinearLayoutManager(ctx));
                holder.binding3.itemsList.setHasFixedSize(true);
                holder.binding3.itemsList.setAdapter(new OrderItemsAdapter(myOrder.getOrderproduct(),ctx,1));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
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

        //handle all button
        //new order
        if (status == 1) {
            holder.binding1.acceptOrder.setOnSlideCompleteListener(
                    new SlideToActView.OnSlideCompleteListener() {
                        @Override
                        public void onSlideComplete(@NonNull SlideToActView slideToActView) {
                            MyOrder myOrder1 = myOrderList.get(position);

                            AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                            View view = LayoutInflater.from(ctx).inflate(R.layout.gettime_layout, null);
                            builder.setView(view);
                            alertDialog = builder.create();
                            alertDialog.show();
                            alertDialog.setCancelable(true);
                            editText = view.findViewById(R.id.editText);

                            ok_btn = view.findViewById(R.id.ok_btn);
                            plus = view.findViewById(R.id.plus);
                            minus = view.findViewById(R.id.minus);
                            Spinner resonSpinner = view.findViewById(R.id.resonSpinner);
                            LinearLayout lineReason = view.findViewById(R.id.lineReason);

                            ArrayAdapter<String> resonList = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item,new String[]{"Ordered dish is time taking","Rush in the restaurant","Was out of stock and preparing next stock","Electricity Power Cut","Raw Material was out of stock","Restaurant was closed and just opened"});
                            resonList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            resonSpinner.setAdapter(resonList);
                            resonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    strAcceptReson=(String) parent.getItemAtPosition(position);
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });

                            plus.setOnClickListener(
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            int count=Integer.parseInt(editText.getText().toString());

                                            count=count+1;
                                            if(Double.parseDouble(myOrder1.getSubtotal())<=500){
                                                if(count<=30){
                                                    editText.setText(count+"");
                                                    lineReason.setVisibility(View.GONE);
                                                    strAcceptReson="";
                                                }else if(count>45){
                                                    Toast.makeText(ctx, "You can not give more than 45 minute!", Toast.LENGTH_SHORT).show();

                                                } else{
                                                    lineReason.setVisibility(View.VISIBLE);
                                                    editText.setText(count+"");
//                                                    Toast.makeText(ctx, "You can not give more than 30 minute!", Toast.LENGTH_SHORT).show();
                                                }
                                            }else if(Double.parseDouble(myOrder1.getSubtotal())>500){
                                                if(count<=60){
                                                    editText.setText(count+"");
                                                    lineReason.setVisibility(View.GONE);
                                                    strAcceptReson="";
                                                }else if(count>75){
                                                Toast.makeText(ctx, "You can not give more than 75 minute!", Toast.LENGTH_SHORT).show();

                                                 }else{
                                                    editText.setText(count+"");
                                                    lineReason.setVisibility(View.VISIBLE);
//                                                    Toast.makeText(ctx, "You can not give more than 60 minute!", Toast.LENGTH_SHORT).show();
                                                }
                                            }


                                        }
                                    }
                            );

                            minus.setOnClickListener(
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            int count=Integer.parseInt(editText.getText().toString());
                                            if(count==1){
                                                Toast.makeText(ctx, "Value can not be zero !", Toast.LENGTH_SHORT).show();
                                            }else {
                                                count=count-1;
                                                if (Double.parseDouble(myOrder1.getSubtotal()) <= 500) {
                                                    if (count <= 30) {
                                                        editText.setText(count + "");
                                                        resonSpinner.setVisibility(View.GONE);
                                                        strAcceptReson = "";
                                                    } else {
                                                        resonSpinner.setVisibility(View.VISIBLE);
                                                        editText.setText(count + "");
//                                                    Toast.makeText(ctx, "You can not give more than 30 minute!", Toast.LENGTH_SHORT).show();
                                                    }
                                                } else if (Double.parseDouble(myOrder1.getSubtotal()) > 500) {
                                                    if (count <= 60) {
                                                        editText.setText(count + "");
                                                        resonSpinner.setVisibility(View.GONE);
                                                        strAcceptReson = "";
                                                    } else {
                                                        editText.setText(count + "");
                                                        resonSpinner.setVisibility(View.VISIBLE);
//                                                    Toast.makeText(ctx, "You can not give more than 60 minute!", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }

                                        }
                                    }
                            );
                            ok_btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    if (editText.getText().toString().trim().isEmpty()) {
                                        editText.setError("Enter Time");
                                        editText.requestFocus();
                                    } else {
//                                Toast.makeText(ctx, ""+editText.getText().toString().trim()+myOrder1.getOrderId(), Toast.LENGTH_SHORT).show();
                                        orderId = myOrder1.getOrderId();
                                        sendtime(editText.getText().toString().trim(), strAcceptReson,orderId);
                                    }
                                }
                            });

                            alertDialog.setOnDismissListener(
                                    new DialogInterface.OnDismissListener() {
                                        @Override
                                        public void onDismiss(DialogInterface dialog) {
                                            holder.binding1.acceptOrder.resetSlider();
                                        }
                                    }
                            );
                        }
                    }
            );

            holder.binding1.rejectOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyOrder myOrder1 = myOrderList.get(position);
                    AlertDialog.Builder aler = new AlertDialog.Builder(ctx);
                    AlertDialog alertDialog = aler.create();
                    View view = LayoutInflater.from(ctx).inflate(R.layout.cancel_reason_layout, null);
                    CancelReasonLayoutBinding binding = CancelReasonLayoutBinding.bind(view);


                    ArrayAdapter<String> resonList = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item,new String[]{"Ordered item is out of stock","Restaurant Closed","Electricity Power Cut","Raw Material is out of stock","Rush in the restaurant","Restaurant currently closed serviceable after few hours","Other"});
                    resonList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.resonSpinner.setAdapter(resonList);
                    binding.resonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            strReason=(String) parent.getItemAtPosition(position);
                            if(strReason.equalsIgnoreCase("other")){
                                binding.reason.setVisibility(View.VISIBLE);
                            }else{
                                binding.reason.setVisibility(View.GONE);
                            }
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    binding.close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });
                    binding.submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String reason = binding.reason.getText().toString();

                                ProgressDialog progressDialog=ProgressDialog.show(ctx,"","Loading...");
                                String ven = singleTask.getValue("vendor");
                                Vendor vendor = new Gson().fromJson(ven, Vendor.class);
                                MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
                                Log.e("merchant_id", vendor.getId() + "");
                                Call<JsonArray> call = myApi.orderStatus(myOrder1.getOrderId(), vendor.getId(), "Rejected", reason,strReason, time);
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
                                                    Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
                                                    Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
                                                    getPosition.click();
                                                    alertDialog.dismiss();
                                                } else {

                                                    Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
                                                    alertDialog.dismiss();
                                                }
                                                if(NotificationUtils.mMediaPlayer!=null){
                                                    NotificationUtils.mMediaPlayer.stop();
                                                }
                                                if(vibrator!=null){
                                                    vibrator.cancel();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        progressDialog.dismiss();
                                    }

                                    @Override
                                    public void onFailure(Call<JsonArray> call, Throwable t) {
                                        progressDialog.dismiss();
                                        Toast.makeText(ctx, t.getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                });


                        }
                    });
                    alertDialog.setView(view);
                    alertDialog.show();

                }
            });

        }
        //preparing order

        else {
            MyOrder myOrder1 = myOrderList.get(position);
            if (status == 2) {

                if (myOrder1.getOrderStatus().equals("Preparing")) {
                    holder.binding2.markAsDone.setOnSlideCompleteListener(
                            new SlideToActView.OnSlideCompleteListener() {
                                @Override
                                public void onSlideComplete(@NonNull SlideToActView slideToActView) {
                                    if(myOrder1.getDelivery_boy_status_accepted().equalsIgnoreCase("true")) {
//                                        ProgressDialog progressDialog=ProgressDialog.show(ctx,"","Loading...");
                                        progressDisplay.showProgress();
                                        String ven = singleTask.getValue("vendor");
                                        Vendor vendor = new Gson().fromJson(ven, Vendor.class);
                                        MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
                                        //Log.e("merchant_id", vendor.getId() + ""+myOrder.getOrderId());
                                        Call<JsonArray> call = myApi.orderStatus(myOrder1.getOrderId(), vendor.getId(), "Prepared", "","", time);
                                        call.enqueue(new Callback<JsonArray>() {
                                            @SuppressLint("ResourceAsColor")
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
                                                            Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
                                                            getPosition.click();
                                                            holder.binding2.markAsDone.setVisibility(View.GONE);
                                                            holder.binding2.trackRider.setVisibility(View.VISIBLE);
//                                                holder.binding2.markAsDone.setBackgroundColor(android.R.color.holo_green_dark);
//                                                holder.binding2.markAsDone.setText("complete");
                                                        } else {
                                                            Toast.makeText(ctx, "Delivery Boy Not Available", Toast.LENGTH_SHORT).show();
                                                        }
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
//                                                progressDialog.dismiss();
                                                progressDisplay.hideProgress();
                                            }


                                            @Override
                                            public void onFailure(Call<JsonArray> call, Throwable t) {
//                                                progressDialog.dismiss();
                                                progressDisplay.hideProgress();
                                                Toast.makeText(ctx, t.getMessage(), Toast.LENGTH_SHORT).show();

                                            }
                                        });
                                    }else{
                                        Toast.makeText(ctx, "No Delivery Boy Assigned !", Toast.LENGTH_SHORT).show();
                                        holder.binding2.markAsDone.setVisibility(View.VISIBLE);
                                        holder.binding2.markAsDone.resetSlider();

                                    }
                                }
                            }
                    );

                }else if(myOrder1.getOrderStatus().equals("Accepted")){
                    Toast.makeText(ctx,"Delivery Boy still not Accepted Order !",Toast.LENGTH_LONG).show();
                }
                holder.binding2.trackRider.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(requestPermission()) {
                            callCode(myOrder1.getDeliveryBoy()[0].getMobile());
                        }

                    }
                });

            }
            //delivered
            else {
                holder.binding3.viewOrder.setOnClickListener(new View.OnClickListener() {
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
                holder.binding3.trackOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + myOrder.getDeliveryBoy()[0].getMobile()));
//                        ctx.startActivity(intent);
                        ctx.startActivity(new Intent(ctx, TrackOrderActivity.class));
                    }
                });

            }
        }


    }

    private void sendtime(String time, String strReson,String orderId) {
        alertDialog.dismiss();
        ProgressDialog progressDialog=ProgressDialog.show(ctx,"","Loading...");
        String ven = singleTask.getValue("vendor");
        Vendor vendor = new Gson().fromJson(ven, Vendor.class);
        MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
        Call<JsonArray> call = myApi.orderStatus(orderId, vendor.getId(), "Preparing", strReson,"", time);
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
                        progressDialog.dismiss();
                        if (res.equals("success")) {
                            Toast.makeText(ctx, "Order Accepted", Toast.LENGTH_SHORT).show();
                            Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
                            getPosition.click();
//                            HomeFragment.viewPager.setCurrentItem(1);
                            fm.beginTransaction().replace(R.id.frame, new PreparingOrderFragment()).commitAllowingStateLoss();
                            HomeFragment.binding.txtNew.setTextColor(Color.parseColor("#000000"));
                            HomeFragment.binding.viewNewOrder.setVisibility(View.INVISIBLE);
                            HomeFragment.binding.txtPrepare.setTextColor(Color.parseColor("#f7b614"));
                            HomeFragment.binding.viewPrepare.setVisibility(View.VISIBLE);
                            HomeFragment.binding.txtComplete.setTextColor(Color.parseColor("#000000"));
                            HomeFragment.binding.viewComplete.setVisibility(View.INVISIBLE);
                            if(PreparingOrderFragment.refereshLayout!=null) {
                                PreparingOrderFragment.refereshLayout.referesh();
                            }
                            if(NotificationUtils.mMediaPlayer!=null){
                                NotificationUtils.mMediaPlayer.stop();
                            }
                            if(vibrator!=null){
                                vibrator.cancel();
                            }
                        } else {

                            progressDialog.dismiss();
                            Toast.makeText(ctx, "Delivery Boy Not Available", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ctx, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void doTime(String trim) {
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
        NewOrdersLayoutBinding binding1;
        ProcessingOrderLayoutBinding binding2;
        DeliveredOrderLayoutBinding binding3;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            if (status == 1) {
                binding1 = NewOrdersLayoutBinding.bind(itemView);
            } else if (status == 2) {
                binding2 = ProcessingOrderLayoutBinding.bind(itemView);

            } else {

                binding3 = DeliveredOrderLayoutBinding.bind(itemView);
            }
        }
    }

    public void findPosition(GetPosition getPositio) {
        this.getPosition = getPositio;
    }

    public interface GetPosition {
        void click();
    }

    private void callCode(String mobileNo2) {

        try {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:"+mobileNo2));
            ctx.startActivity(callIntent);
        } catch (ActivityNotFoundException activityException) {
            Toast.makeText(ctx, "not working call", Toast.LENGTH_SHORT).show();
            Log.e("Calling a Phone Number", "Call failed", activityException);
        }

    }
    private boolean requestPermission() {
        int result = ContextCompat.checkSelfPermission(ctx, Manifest.permission.CALL_PHONE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(ctx, new String[]{Manifest.permission.CALL_PHONE}, 101);
            return false;
        }
    }


    public void addData(List<MyOrder> arrayList){
        myOrderList.addAll(arrayList);
        notifyDataSetChanged();
    }

    public void addClearData(){
        myOrderList.clear();
        notifyDataSetChanged();
    }
}
