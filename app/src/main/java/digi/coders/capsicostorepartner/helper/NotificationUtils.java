package digi.coders.capsicostorepartner.helper;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.text.Html;
import android.text.TextUtils;
import android.util.Patterns;

import androidx.core.app.NotificationCompat;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import digi.coders.capsicostorepartner.R;


/**
 * Created by Dinosoftlabs on 31/03/15.
 */
public class NotificationUtils {


    public static final String PUSH_NOTIFICATION = "pushNotification";

    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String TOPIC_GLOBAL = "global";
    public static final int NOTIFICATION_ID = 100;
    private static String TAG = NotificationUtils.class.getSimpleName();

    private Context mContext;
    String channelId;
    public NotificationUtils(Context mContext, String ChannedID) {
        this.mContext = mContext;
        this.channelId = ChannedID;
    }

    public static MediaPlayer mMediaPlayer;

    public void showNotificationMessage(String title, String message, String timeStamp, Intent intent,String status) {
        showNotificationMessage(title, message, timeStamp, intent,null, status);
    }
    @SuppressWarnings("deprecation")
    public void showNotificationMessage(final String title, final String message, final String timeStamp, Intent intent, String imageUrl,String status) {
        // Check for empty push message
        if (TextUtils.isEmpty(message))
            return;


        // notification icon
        final int icon = R.drawable.noti_icon;

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        final PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        mContext,
                        0,
                        intent,
                        PendingIntent.FLAG_CANCEL_CURRENT
                );

        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                mContext,channelId);

        final Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                + "://" + mContext.getPackageName() + "/raw/noti.mp3");

        if (!TextUtils.isEmpty(imageUrl)) {

            if (imageUrl != null && imageUrl.length() > 4 && Patterns.WEB_URL.matcher(imageUrl).matches()) {

                Bitmap bitmap = getBitmapFromURL(imageUrl);

                if (bitmap != null) {
                    showBigNotification(bitmap, mBuilder, icon, title, message, timeStamp, resultPendingIntent, alarmSound);
                    if(!status.equalsIgnoreCase("simple")&&!status.equalsIgnoreCase("Accepted")) {
                        playNotificationSound();
                        vibrateDevice();
                    }
                } else {
                    showSmallNotification(mBuilder, icon, title, message, timeStamp, resultPendingIntent, alarmSound);
                    if(!status.equalsIgnoreCase("simple")&&!status.equalsIgnoreCase("Accepted")) {
                        playNotificationSound();
                        vibrateDevice();
                    }
                }
            }
        } else {
            showSmallNotification(mBuilder, icon, title, message, timeStamp, resultPendingIntent, alarmSound);
            if(!status.equalsIgnoreCase("simple")&&!status.equalsIgnoreCase("Accepted")) {
                playNotificationSound();
                vibrateDevice();
            }
        }
    }


    private void showSmallNotification(NotificationCompat.Builder mBuilder, int icon, String title, String message, String timeStamp, PendingIntent resultPendingIntent, Uri alarmSound) {

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

        inboxStyle.addLine(message);
/*

        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

*/ if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            /* Create or update. */
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);

            mBuilder.setSmallIcon(R.drawable.logo)
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setContentIntent(resultPendingIntent)
                    .setSound(alarmSound)
                    .setStyle(inboxStyle)
                    .setWhen(getTimeMilliSec(timeStamp))
                    .setSmallIcon(R.drawable.logo)
                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                    .setContentText(message)
                    .setChannelId(channelId)
                    .setPriority(NotificationManager.IMPORTANCE_MAX);
        }
        else {


            mBuilder.setSmallIcon(R.drawable.logo)
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setContentIntent(resultPendingIntent)
                    .setSound(alarmSound)
                    .setStyle(inboxStyle)
                    .setWhen(getTimeMilliSec(timeStamp))
                    .setSmallIcon(R.drawable.logo)
                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                    .setContentText(message)
                    .setChannelId(channelId)
                    .setPriority(NotificationManager.IMPORTANCE_MAX);

        }
        notificationManager.notify(NOTIFICATION_ID, mBuilder.build());


 /*       NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

        inboxStyle.addLine(message);

        Notification notification;
        notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentIntent(resultPendingIntent)
                .setSound(alarmSound)
                .setStyle(inboxStyle)
                .setWhen(getTimeMilliSec(timeStamp))
                .setSmallIcon(R.drawable.logo)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                .setContentText(message)
                .build();

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(Config.NOTIFICATION_ID, notification);*/
    }
    @SuppressWarnings("deprecation")
    private void showBigNotification(Bitmap bitmap, NotificationCompat.Builder mBuilder, int icon, String title, String message, String timeStamp, PendingIntent resultPendingIntent, Uri alarmSound) {
        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.setBigContentTitle(title);
        bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
        bigPictureStyle.bigPicture(bitmap);


        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            /* Create or update. */
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);

            mBuilder.setSmallIcon(R.drawable.logo)
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setContentIntent(resultPendingIntent)
                    .setSound(alarmSound)
                    .setStyle(bigPictureStyle)
                    .setWhen(getTimeMilliSec(timeStamp))
                    .setSmallIcon(R.drawable.logo)
                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                    .setContentText(message)
                    .setChannelId(channelId)
                    .setPriority(NotificationManager.IMPORTANCE_HIGH);
        }
        else {

            mBuilder.setSmallIcon(R.drawable.logo)
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setContentIntent(resultPendingIntent)
                    .setSound(alarmSound)
                    .setStyle(bigPictureStyle)
                    .setWhen(getTimeMilliSec(timeStamp))
                    .setSmallIcon(R.drawable.logo)
                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                    .setContentText(message)
                    .setChannelId(channelId)
                    .setPriority(NotificationManager.IMPORTANCE_HIGH);
        }

        notificationManager.notify(NOTIFICATION_ID, mBuilder.build());

      /*  Notification notification;
        notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentIntent(resultPendingIntent)
                .setSound(alarmSound)
                .setStyle(bigPictureStyle)
                .setWhen(getTimeMilliSec(timeStamp))
                .setSmallIcon(R.drawable.logo)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                .setContentText(message)
                .build();

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(Config.NOTIFICATION_ID_BIG_IMAGE, notification);*/
    }

    /**
     * Downloading push notification image before displaying it in
     * the notification tray
     */
    public Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Playing notification sound
    @SuppressWarnings("deprecation")
    public void playNotificationSound() {
        try {

            mMediaPlayer = new MediaPlayer();
            mMediaPlayer = MediaPlayer.create(mContext, R.raw.ring);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.start();
//
//           Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//            Ringtone r = RingtoneManager.getRingtone(mContext, alarmSound);
//            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Vibrator vibrator;
    public void vibrateDevice(){
        vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(240000);
        } else {
            //deprecated in API 26
            vibrator.vibrate(240000);
        }
    }

    /**
     * Method checks if the app is in background or not
     */
    @SuppressWarnings("deprecation")
    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    // Clears notification tray messages
    public static void clearNotifications(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    public static long getTimeMilliSec(String timeStamp) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = format.parse(timeStamp);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
