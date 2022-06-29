package digi.coders.capsicostorepartner.helper;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import digi.coders.capsicostorepartner.R;
import digi.coders.capsicostorepartner.activity.DashboardActivity;

public class NotificationService extends Service {
    String title;
    NotificationManager mNotificationManager;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        title=intent.getExtras().getString("title");
        createNotification();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


    public void createNotification () {
        try {
            if(mNotificationManager!=null) {
                mNotificationManager.cancelAll();
            }
            Intent notificationIntent = new Intent(getApplicationContext(), DashboardActivity.class);
            notificationIntent.putExtra("fromNotification", true);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
             mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), default_notification_channel_id);
            mBuilder.setContentTitle("Capsico real time ordering");
            mBuilder.setContentIntent(pendingIntent);
            if(title.equalsIgnoreCase("online")){
                mBuilder.setContentText("Status : Accepting Orders");
            }else{
                mBuilder.setContentText("Status : Offline");
            }

            mBuilder.setSmallIcon(R.drawable.splash_icon);
            mBuilder.setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(),
                    R.drawable.noti_icon));
            mBuilder.setAutoCancel(false);
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
}
