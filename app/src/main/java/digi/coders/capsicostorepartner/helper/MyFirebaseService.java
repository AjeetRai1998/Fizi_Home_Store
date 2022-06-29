package digi.coders.capsicostorepartner.helper;

import android.media.MediaPlayer;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import digi.coders.capsicostorepartner.R;

public class MyFirebaseService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

    }
}
