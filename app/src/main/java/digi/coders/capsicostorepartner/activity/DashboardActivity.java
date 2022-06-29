package digi.coders.capsicostorepartner.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.razorpay.PaymentResultListener;
import com.skydoves.elasticviews.ElasticButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import digi.coders.capsicostorepartner.BuildConfig;
import digi.coders.capsicostorepartner.R;
import digi.coders.capsicostorepartner.adapter.OrderAdapter;
import digi.coders.capsicostorepartner.adapter.OrderItemsAdapter;
import digi.coders.capsicostorepartner.adapter.OrderItemsListAdapter;
import digi.coders.capsicostorepartner.databinding.ActivityDashboardBinding;
import digi.coders.capsicostorepartner.fragment.AccountFragment;
import digi.coders.capsicostorepartner.fragment.BookingsFragment;
import digi.coders.capsicostorepartner.fragment.HomeFragment;
import digi.coders.capsicostorepartner.fragment.NewOrderFragment;
import digi.coders.capsicostorepartner.fragment.OfflineFragment;
import digi.coders.capsicostorepartner.fragment.PreparingOrderFragment;
import digi.coders.capsicostorepartner.fragment.WalletFragment;
import digi.coders.capsicostorepartner.helper.MyApi;
import digi.coders.capsicostorepartner.helper.NotificationService;
import digi.coders.capsicostorepartner.helper.NotificationUtils;
import digi.coders.capsicostorepartner.helper.OrderService;
import digi.coders.capsicostorepartner.model.MyOrder;
import digi.coders.capsicostorepartner.model.ShowProgress;
import digi.coders.capsicostorepartner.model.Vendor;
import digi.coders.capsicostorepartner.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static digi.coders.capsicostorepartner.helper.NotificationUtils.NOTIFICATION_ID;
import static digi.coders.capsicostorepartner.helper.NotificationUtils.PUSH_NOTIFICATION;
import static digi.coders.capsicostorepartner.helper.NotificationUtils.REGISTRATION_COMPLETE;
import static digi.coders.capsicostorepartner.helper.NotificationUtils.TOPIC_GLOBAL;
import static digi.coders.capsicostorepartner.helper.NotificationUtils.vibrator;

public class DashboardActivity extends AppCompatActivity {

    ActivityDashboardBinding binding;
    Fragment fragment = null;
    private SingleTask singleTask;

//    private BroadcastReceiver mRegistrationBroadcastReceiver;

    public static String AdminCommission="";
    int status=0;

    public static Activity Dashboard;
    Activity myActivity;
    public static int updateStatus=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setTurnScreenOn(true);
            setShowWhenLocked(true);
        }


        super.onCreate(savedInstanceState);



        Dashboard=this;
        myActivity=this;
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        singleTask = (SingleTask) getApplication();
        player=MediaPlayer.create(this, R.raw.ring);
        player.setLooping(false);
        //bot
        //tomnaviation


        //send Notiifcation

        //Register Reciver

//        startService(new Intent(getBaseContext(), OrderService.class));
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(
                mMessageReceiver, new IntentFilter("GPSLocationUpdates"));


        String ven = singleTask.getValue("vendor");
        if(updateStatus==0) {
            updateStatus=1;
            getUpdate();
        }

        Vendor vendor = new Gson().fromJson(ven, Vendor.class);
        if (vendor != null) {
            loadMyProfile();
        }


//        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//
//
//                if (intent.getAction().equals(REGISTRATION_COMPLETE)) {
//                    FirebaseMessaging.getInstance().subscribeToTopic(TOPIC_GLOBAL);
//
//
//                } else if (intent.getAction().equals(PUSH_NOTIFICATION)) {
//
//                    String message = intent.getStringExtra("message");
//
//                    sendMyNotification(message);
//
//                    sendBroadcast(new Intent("newOrder"));
//
//                }
//            }
//        };

        //handle switch of store


        binding.bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        fragment = new OfflineFragment();
                        break;
                    case R.id.wallet:
                        fragment = new HomeFragment();
                        break;
//                    case R.id.booking:
//                        fragment = new BookingsFragment();
//                        break;
                    case R.id.account:
                        fragment = new AccountFragment();
                        break;
                    case R.id.product:
                        ManageProductActivity.TransiStatus=0;
                        startActivity(new Intent(DashboardActivity.this,MyReviewActivity.class));
                        break;

                }
                exchangeFragment(fragment);
                return true;
            }
        });
        binding.chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(requestPermission()) {
                    try {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + "8188943693"));
                        startActivity(callIntent);
                    } catch (ActivityNotFoundException activityException) {
                        Toast.makeText(getApplicationContext(), "not working call", Toast.LENGTH_SHORT).show();
                        Log.e("Calling a Phone Number", "Call failed", activityException);
                    }
                }
//                }
//                startActivity(new Intent(DashboardActivity.this, ChatActivity.class));

            }
        });

    }
    private boolean requestPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(DashboardActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 101);
            return false;
        }
    }

    private void sendMyNotification(String message) {
        Intent intent = new Intent(this, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        	.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel("channel-01",
                    "Foodomia",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"channel-01");
        builder.setSmallIcon(R.drawable.logo)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo))
                .setColor(ContextCompat.getColor(DashboardActivity.this, R.color.red))
                .setContentTitle("Foodomia")
                .setContentIntent(pendingIntent)
                .setContentText(String.format(message))
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setChannelId("channel-01");


        notificationManager.notify(0, builder.build());
    }

    private void sendNotification(String token) {
        String ven=singleTask.getValue("vendor");
        Vendor vendor=new Gson().fromJson(ven,Vendor.class);
        MyApi myApi=singleTask.getRetrofit().create(MyApi.class);
        Call<JsonArray> call=myApi.sentToken(vendor.getId(),token);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if(response.isSuccessful())
                {
                    Log.e("sdsd",response.toString());

                    try {
                        JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
                        JSONObject jsonObject=jsonArray.getJSONObject(0);
                        String res= jsonObject.getString("res");
                        String msg= jsonObject.getString("msg");
                        if(res.equals(""))
                        {
                            Log.e("sdsd",msg);
                        }
                        Log.e("dcdfd",res);
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

    private void loadMyProfile() {

        String ven = singleTask.getValue("vendor");
        Vendor vendor = new Gson().fromJson(ven, Vendor.class);
//        ShowProgress.getShowProgress(DashboardActivity.this).show();
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
                            AdminCommission=vendor1.getAdminCommission();
                            FirebaseMessaging.getInstance().getToken()
                                    .addOnCompleteListener(new OnCompleteListener<String>() {
                                        @Override
                                        public void onComplete(@NonNull Task<String> task) {
                                            if (!task.isSuccessful()) {
                                                Log.w("sd", "Fetching FCM registration token failed", task.getException());
                                                return;
                                            }
                                            Log.e("sdsd","firbase");

                                            // Get new FCM registration token
                                            String token = task.getResult();

                                            Log.e("sds","ge"+token);
                                            Log.d("sads", token + "");
                                            sendNotification(token);
                                        }
                                    });


//                            if (vendor.getIsStatus().equalsIgnoreCase("open")) {
//                                binding.onOff.setChecked(true);
//                                exchangeFragment(new HomeFragment());
//                                binding.onOff.setText("Open");
//
//                            } else {
//                                binding.onOff.setChecked(false);
//                                binding.onOff.setText("Close");
                                exchangeFragment(new OfflineFragment());
//                            }
//                            binding.onOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                                @Override
//                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                                    if (isChecked) {
//                                        updateStatus("open");
//                                        exchangeFragment(new HomeFragment());
//                                        binding.onOff.setText("Open");
//                                        binding.bottomNavigation.setSelectedItemId(R.id.home);
//
//                                    } else {
//                                        updateStatus("close");
//                                        exchangeFragment(new OfflineFragment());
//                                        binding.onOff.setText("Close");
//                                        binding.bottomNavigation.setSelectedItemId(R.id.home);
//                                    }
//                                }
//                            });

                        } else {
                            Toast.makeText(DashboardActivity.this, msg, Toast.LENGTH_SHORT).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                ShowProgress.getShowProgress(DashboardActivity.this).hide();
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                ShowProgress.getShowProgress(DashboardActivity.this).hide();
                Toast.makeText(DashboardActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void updateStatus(String status) {
        String ven = singleTask.getValue("vendor");
        Vendor vendor = new Gson().fromJson(ven, Vendor.class);
//        ShowProgress.getShowProgress(DashboardActivity.this).show();
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
                        ShowProgress.getShowProgress(DashboardActivity.this).hide();
                        if (res.equals("success")) {

                            Toast.makeText(DashboardActivity.this, msg, Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(DashboardActivity.this, msg, Toast.LENGTH_SHORT).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                ShowProgress.getShowProgress(DashboardActivity.this).hide();
                Toast.makeText(DashboardActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void exchangeFragment(androidx.fragment.app.Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commitAllowingStateLoss();

        }
    }

    private void checkNewOrder() {
//        ShowProgress.getShowProgress(DashboardActivity.this).show();
        String ven = singleTask.getValue("vendor");
        Vendor vendor = new Gson().fromJson(ven, Vendor.class);
        MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
        Log.e("merchant_id", vendor.getId() + "");
        Call<JsonArray> call = myApi.checkNewOrder(vendor.getId());
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
                        ShowProgress.getShowProgress(DashboardActivity.this).hide();
                        if (res.equals("success")) {
                            ShowProgress.getShowProgress(DashboardActivity.this).hide();
                        } else {
                            ShowProgress.getShowProgress(DashboardActivity.this).hide();
                            Toast.makeText(DashboardActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                ShowProgress.getShowProgress(DashboardActivity.this).hide();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        unregisterReceiver(myReceiver);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("GPSLocationUpdates"));
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String order_id = intent.getStringExtra("order_id");
            String id = intent.getStringExtra("id");
            String status1 = intent.getStringExtra("status");
            String  amount = "90";//intent.getStringExtra("0amount");

            if(status1.equalsIgnoreCase("Accepted")){
                if(PreparingOrderFragment.refereshLayout!=null){
                    PreparingOrderFragment.refereshLayout.referesh();
                }
            }else {
                if (status == 0) {
                    status = 1;
                    getLastOrders();
                }
            }

//             startRingtone(amount,order_id);
//             seen(id);

        }
    };
    MediaPlayer player;
//    public void startRingtone(String amount,String order_id){
//        if(status==0) {
//            showAlert(a,DashboardActivity.this);
//            status=1;
////            player.start();
//        }
//    }
    private void seen(String id) {
        MyApi myApi = singleTask.getRetrofit1().create(MyApi.class);
        Call<JsonObject> call = myApi.seen(id,"seen");
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                try{
                    JSONObject jsonObject1=new JSONObject(new Gson().toJson(response.body()));
                    if(jsonObject1.getString("res").equalsIgnoreCase("success")){
                        Toast.makeText(getApplicationContext(),"order seen",Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getApplicationContext(),"order not seen",Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage().toString(),Toast.LENGTH_LONG).show();
            }
        });
    }

    public void showAlert(MyOrder myOrder, Activity ctx) {
        LayoutInflater layoutInflater = LayoutInflater.from(ctx);
        View promptView = layoutInflater.inflate(R.layout.order_alert_layout, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctx);
        alertDialogBuilder.setView(promptView);
        final AlertDialog alert2 = alertDialogBuilder.create();
        alert2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ElasticButton ok=promptView.findViewById(R.id.ok);
        TextView time=promptView.findViewById(R.id.time);
        TextView productPrice=promptView.findViewById(R.id.productPrice);

        RecyclerView itemsList=promptView.findViewById(R.id.itemsList);
        itemsList.setLayoutManager(new LinearLayoutManager(ctx));
        itemsList.setHasFixedSize(true);
        itemsList.setAdapter(new OrderItemsListAdapter(myOrder.getOrderproduct(),ctx));
        time.setText(myOrder.getCreatedAt());

//        double totalAmount=0;
//        for(int i=0;i<myOrder.getOrderproduct().length;i++){
//            totalAmount=totalAmount+Double.parseDouble(myOrder.getOrderproduct()[i].getSellPrice());
//        }
        productPrice.setText("\u20b9 "+myOrder.getSubtotal());
        ok.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        player.stop();
                        status=0;
                        alert2.dismiss();
                        if(NewOrderFragment.refre!=null){
                            NewOrderFragment.refre.onRefresh();
                        }
                        if(NotificationUtils.mMediaPlayer!=null){
                            NotificationUtils.mMediaPlayer.stop();
                        }
                        if(vibrator!=null){
                            vibrator.cancel();
                        }
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,new HomeFragment()).commit();
                    }
                }
        );


        alert2.show();
    }

    private void getLastOrders() {

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

                        if (res.equals("success")) {
                            int count=jsonObject1.getInt("count");

                            JSONArray jsonArray1=jsonObject1.getJSONArray("data");

                                MyOrder myOrder=new Gson().fromJson(jsonArray1.getJSONObject(0).toString(),MyOrder.class);
                                if(!myActivity.isFinishing()) {
                                    showAlert(myOrder, myActivity);


                                }else{

                                }

                        } else {

                        }
                    } catch (JSONException e) {
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


    public void createNotification () {
        try {
            Intent notificationIntent = new Intent(getApplicationContext(), DashboardActivity.class);
            notificationIntent.putExtra("fromNotification", true);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), default_notification_channel_id);
            mBuilder.setContentTitle("My Notification");
            mBuilder.setContentIntent(pendingIntent);
            mBuilder.setContentText("Notification Listener Service Example");
            mBuilder.setSmallIcon(R.drawable.ic_launcher_foreground);
            mBuilder.setAutoCancel(true);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel notificationChannel = new
                        NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
                mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
                assert mNotificationManager != null;
                mNotificationManager.createNotificationChannel(notificationChannel);
            }
            assert mNotificationManager != null;
            mNotificationManager.notify((int) System.currentTimeMillis(), mBuilder.build());
        }catch (Exception e){

        }
//        throw new UnsupportedOperationException( "Not yet implemented" ) ;
         }

    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;

    public void showUpdate(Context ctx,String important) {
        LayoutInflater layoutInflater = LayoutInflater.from(ctx);
        View promptView = layoutInflater.inflate(R.layout.app_update_layout, null);
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ctx);
        alertDialogBuilder.setView(promptView);


        final android.app.AlertDialog alert2 = alertDialogBuilder.create();
        alert2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView add=promptView.findViewById(R.id.add);
        TextView noNow=promptView.findViewById(R.id.notNow);

        if(important.equalsIgnoreCase("true")){
            noNow.setVisibility(View.GONE);
        }else{
            noNow.setVisibility(View.VISIBLE);
        }
        add.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse("https://play.google.com/store/apps/details?id="+ BuildConfig.APPLICATION_ID));
                        startActivity(intent);
                        alert2.dismiss();
                    }
                }
        );

        noNow.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert2.dismiss();
                    }
                }
        );

        alert2.setCanceledOnTouchOutside(false);

        alert2.show();

    }

    private void getUpdate() {

        MyApi myApi=singleTask.getRetrofit1().create(MyApi.class);
        Call<JsonObject> call=myApi.getUpdate("Vendor");
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful())
                {
                    try {
                        JSONObject jsonObject=new JSONObject(new Gson().toJson(response.body()));
                        String res=jsonObject.getString("res");
                        if(res.equals("success"))
                        {
                            JSONObject jsonObject1=jsonObject.getJSONObject("data");
                            String versionName=jsonObject1.getString("version_name");
                            String VersionCode=jsonObject1.getString("version_code");
                            String important=jsonObject1.getString("importance");
//
                            if(Integer.parseInt(VersionCode)> BuildConfig.VERSION_CODE){
                                showUpdate(DashboardActivity.this,important);
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });

    }
}
