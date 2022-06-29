package digi.coders.capsicostorepartner.helper;

/**
 * Created by Atul
 */

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManagerLocation {

//the constants
private static final String SHARED_PREF_NAMEE = "capsicostorepref";

    private static final String LOCATION = "location";
    private static final String FETAURE="feature";
    private static final String LATITUDE="latitude";
    private static final  String LONGITUDE="longitude";


private static SharedPrefManagerLocation mInstance;
private static Context mCtx;

private SharedPrefManagerLocation(Context context) {
        mCtx = context;
}

public static synchronized SharedPrefManagerLocation getInstance(Context context) {
        if (mInstance == null) {
                mInstance = new SharedPrefManagerLocation(context);
        }
        return mInstance;
}

//method to let the user login
//this method will store the user data in shared preferences
public void userLocation(Constraints loc) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAMEE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(LOCATION, loc.getLoc());
        editor.putString(FETAURE,loc.getFeature());
        editor.putString(LATITUDE,loc.getLatitude());
        editor.putString(LONGITUDE,loc.getLongitude());
    //editor.putString(KEY_NAME, loc.getCityName());
        editor.apply();

}

//this method will checker whether user is already logged in or not
public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAMEE, Context.MODE_PRIVATE);
        return sharedPreferences.getString(LOCATION, null) != null;
}

//this method will give the logged in user
public String  locationModel(String key) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAMEE, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key,null);
}

//this method will logout the user
public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAMEE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
//                mCtx.startActivity(new Intent(mCtx, LetsPlayActivity.class));
}

}

