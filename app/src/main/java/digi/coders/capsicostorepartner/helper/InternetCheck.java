package digi.coders.capsicostorepartner.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class InternetCheck {

    public boolean isNetworkAvailable(Context context){
        ConnectivityManager connectivityManager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        return networkInfo!=null && networkInfo.isConnected();
    }
}
