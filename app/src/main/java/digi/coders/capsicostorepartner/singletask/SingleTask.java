package digi.coders.capsicostorepartner.singletask;

import android.app.Application;
import android.content.SharedPreferences;

import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static digi.coders.capsicostorepartner.helper.Constraints.BASE_URL1;

public class SingleTask extends Application {

    private Retrofit retrofit,retrofit1;
    public static final String BASE_URL="http://designerpriya.com/MerchantAPIs/";

    @Override
    public void onCreate() {
        super.onCreate();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        if(retrofit==null)
        {
            retrofit=new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        if(retrofit1==null)
        {
            retrofit1=new Retrofit.Builder()
                    .baseUrl(BASE_URL1)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

    }
    public Retrofit getRetrofit()
    {
        return retrofit;
    }
    public Retrofit getRetrofit1()
    {
        return retrofit1;
    }

    public SharedPreferences getSharedPreferene()
    {
        return getSharedPreferences("capsico_pref",MODE_PRIVATE);
    }
    public void addValue(String key, JSONObject jsonObject)
    {
        getSharedPreferene().edit().putString(key, String.valueOf(jsonObject)).commit();
    }
    public void removeUser(String key)
    {
        getSharedPreferene().edit().remove(key).commit();
    }
    public String getValue(String key)
    {
        return  getSharedPreferene().getString(key,null);
    }
}
