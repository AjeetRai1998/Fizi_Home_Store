package digi.coders.capsicostorepartner.helper;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.work.Data;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import digi.coders.capsicostorepartner.adapter.OrderAdapter;
import digi.coders.capsicostorepartner.fragment.HomeFragment;
import digi.coders.capsicostorepartner.model.MyOrder;
import digi.coders.capsicostorepartner.model.Vendor;
import digi.coders.capsicostorepartner.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderService extends Service {

    int mStartMode;

    IBinder mBinder;

    boolean mAllowRebind;
    Timer timer;
    int i=0;
    SimpleDateFormat df;
    Date current_date;
    @Override
    public void onCreate() {
        timer = new Timer();
        singleTask=(SingleTask)getApplicationContext();
        df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US);


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        startTimer();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /** Called when all clients have unbound with unbindService() */
    @Override
    public boolean onUnbind(Intent intent) {
        return mAllowRebind;
    }

    /** Called when a client is binding to the service with bindService()*/
    @Override
    public void onRebind(Intent intent) {

    }

    void startTimer(){

        timer.scheduleAtFixedRate(new TimerTask() {

                                  @Override
                                  public void run() {
                                      loadNewOrderList();
                                  }

                              },
                0,
                10000);
    }
    /** Called when The service is no longer used and is being destroyed */
    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        timer.cancel();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }

    private static void sendMessageToActivity(String order_id, String amount,String id, Context context) {
        Intent intent = new Intent("GPSLocationUpdates");
        // You can also include some extra data.
        intent.putExtra("order_id", order_id);
        intent.putExtra("amount", amount);
        intent.putExtra("id", id);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
    private SingleTask singleTask;
    private void loadNewOrderList() {

        String ven = singleTask.getValue("vendor");
        Vendor vendor = new Gson().fromJson(ven, Vendor.class);
        MyApi myApi = singleTask.getRetrofit().create(MyApi.class);
        Call<JsonArray> call = myApi.getOrder(vendor.getId(),"Placed","0");
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    try {
                        current_date =df.parse(df.format(new Date()));
                        JSONArray jsonArray = new JSONArray(new Gson().toJson(response.body()));
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        String res = jsonObject1.getString("res");
                        String message = jsonObject1.getString("message");

                        if (res.equals("success")) {

                            JSONArray jsonArray1=jsonObject1.getJSONArray("data");

                            for(int i=0;i<jsonArray1.length();i++){
                                JSONObject data=jsonArray1.getJSONObject(i);
                                if(data.getString("seen_status").equalsIgnoreCase("false")) {
                                    Date date = df.parse(data.getString("created_at"));
                                    long diff = current_date.getTime() - date.getTime();
                                    long diffSeconds = TimeUnit.MILLISECONDS.toSeconds(diff);
//                                long diffMinutes = diff / (60 * 1000) % 60;
//                                long diffHours = diff / (60 * 60 * 1000);


                                    if (diffSeconds < 60) {
                                        sendMessageToActivity(data.getString("order_id"), data.getString("amount"), data.getString("id"),getApplicationContext());
                                    }
                                }
                            }
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
                Log.e("sdsd", t.getMessage());
            }
        });
    }
}
