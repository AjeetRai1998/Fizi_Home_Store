package digi.coders.capsicostorepartner.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class FunctionClass {

    String address = "";

    public List<Address> getAddresses(Double latitude, Double longitude, Context ctx) {
        Geocoder geocoder = new Geocoder(ctx, Locale.getDefault());

        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() != 0) {
                address = addresses.get(0).getAddressLine(0);
                Log.i("adress", addresses+"");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return addresses;
        }

    String encodedString="";

    public String encodeImagetoString(final Bitmap bitmap) {
        BitmapFactory.Options options = null;
        options = new BitmapFactory.Options();
        // options.inSampleSize = 3;
        options.inSampleSize = 5;

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Must compress the Image to reduce image size to make upload easy
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        byte[] byte_arr = stream.toByteArray();
        // Encode Image to String
        encodedString = Base64.encodeToString(byte_arr, 0);
        // Toast.makeText(getApplicationContext(),encodedString,Toast.LENGTH_SHORT).show();

        return encodedString;
    }

}
