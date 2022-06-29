package digi.coders.capsicostorepartner.helper;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import digi.coders.capsicostorepartner.R;
import digi.coders.capsicostorepartner.activity.DashboardActivity;

import static digi.coders.capsicostorepartner.helper.NotificationUtils.PUSH_NOTIFICATION;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private NotificationUtils notificationUtils;
    String channelId = "channel-01";

    String imageUrl,timestamp;
    long[] pattern = {0, 100, 1000};
    SharedPreferences sPref;
    Vibrator v;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.e(TAG, "From: " + remoteMessage.getFrom());
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        if (remoteMessage == null)
            return;

        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {

                handleDataMessage(remoteMessage.getData().get("body"),remoteMessage.getData().get("title"),remoteMessage.getData().get("status"));
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }

        }

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

    }

    private void handleDataMessage(String title ,String desc,String status) {

//        SharedPreferences.Editor editor = sPref.edit();

        try {

                if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                    // app is in foreground, broadcast the push message
                    Intent fullScreenIntent = new Intent(this, DashboardActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            .addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    fullScreenIntent.putExtra("order_id","order_id");

                    Intent intent = new Intent("GPSLocationUpdates");
                    // You can also include some extra data.
                    intent.putExtra("order_id", "order_id");
                    intent.putExtra("amount", "amount");
                    intent.putExtra("id", "id");
                    intent.putExtra("status", status);
                    if(!status.equalsIgnoreCase("simple")) {
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                    }
                    if (TextUtils.isEmpty(imageUrl)) {
                        showNotificationMessage(getApplicationContext(), desc, title, "timestamp", fullScreenIntent,status);
                    } else {
                        // image is present, show notification with image
                        showNotificationMessageWithBigImage(getApplicationContext(), desc, title, "timestamp", fullScreenIntent, imageUrl,status);
                    }
                } else {
                    // app is in background, show the notification in notification tray
                    Intent fullScreenIntent = new Intent(this, DashboardActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            .addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    fullScreenIntent.putExtra("order_id","order_id");



                    Intent intent = new Intent("GPSLocationUpdates");
                    // You can also include some extra data.
                    intent.putExtra("order_id", "order_id");
                    intent.putExtra("amount", "amount");
                    intent.putExtra("id", "id");
                    intent.putExtra("status", status);
                    if(!status.equalsIgnoreCase("simple")) {
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                    }
//                    startRingtone();
//                    // check for image attachment
                    if (TextUtils.isEmpty(imageUrl)) {
                        showNotificationMessage(getApplicationContext(), desc, title, "timestamp", fullScreenIntent,status);
                    } else {
                        // image is present, show notification with image
                        showNotificationMessageWithBigImage(getApplicationContext(), desc, title, "timestamp", fullScreenIntent, imageUrl,status);
                    }
                }

        } catch (Exception e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        }

    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent,String status) {
        notificationUtils = new NotificationUtils(context,channelId);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent,status);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl,String status) {
        notificationUtils = new NotificationUtils(context,channelId);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl,status);
    }
    MediaPlayer player;
    public void startRingtone(){
        player=MediaPlayer.create(this, R.raw.ring); //select music file
        player.setLooping(true); //set looping
//        v.vibrate(pattern,400);
        player.start();

//        new CountDownTimer(40000,1000) {
//
//            @Override
//            public void onTick(long millisUntilFinished) {
//                // TODO Auto-generated method stub
//
//            }
//
//            @Override
//            public void onFinish() {
//                // TODO Auto-generated method stub
//                if(player.isPlaying())
//                {
//                    player.stop();
//                    v.cancel();
//                }
//
//
//            }
//        }.start();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}