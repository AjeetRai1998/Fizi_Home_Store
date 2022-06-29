package digi.coders.capsicostorepartner.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import digi.coders.capsicostorepartner.R;
import digi.coders.capsicostorepartner.databinding.ActivityMapBinding;
import digi.coders.capsicostorepartner.helper.Constraints;
import digi.coders.capsicostorepartner.helper.FunctionClass;
import digi.coders.capsicostorepartner.helper.SharedPrefManagerLocation;
import digi.coders.capsicostorepartner.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE =2 ;
    ImageButton back;
    private GoogleMap mMap;
    FunctionClass functionClass;
    LocationManager locationManager;
    LocationListener locationListener;
    FloatingActionButton fab;
    Location main_location;
    TextView Location_txt;
    MaterialButton continueAdd;


    private static String address = "";
    private static String state = "";
    private static String city = "";
    private static String userPostalCode = "";
    private static String country = "";
    private static String zip = "";
    private static String userAddressline1 = "";
    private static String latitude;
    private static String longitude;
    ActivityMapBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        functionClass = new FunctionClass();
        Location_txt = findViewById(R.id.location);
        continueAdd = findViewById(R.id.continueAdd);

        continueAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent in=new Intent(MapActivity.this,RegisterationActivity.class);
//                in.putExtra("loc",address);
//                startActivity(in);
                finish();
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        binding.imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!Places.isInitialized()) {
                    Places.initialize(MapActivity.this.getApplicationContext(),getResources().getString(R.string.google_maps_key));
                }

                // Set the fields to specify which types of place data to return.
                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
                // Start the autocomplete intent.
                Intent intent = new Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.FULLSCREEN, fields)
                        .build(MapActivity.this);
                MapActivity.this.startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100000, 100000, locationListener);
                            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100000, 100000, locationListener);

                            try {
                                moveCamera(new LatLng(main_location.getLatitude(), main_location.getLongitude()));
                            } catch (Exception e) {
                                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(intent);
                            }

                        } else {
                            ActivityCompat.requestPermissions(MapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                        }

                    }
                }
        );
        Intent intent = getIntent();
        if (intent.getIntExtra("Place Number", 0) == 0) {
            // Zoom into users location
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    /*googleMap.addCircle(new CircleOptions()
                            .center(new LatLng(location.getLatitude(),location.getLongitude()))
                            .radius(10000)
                            .strokeColor(Color.RED)
                            .fillColor(Color.BLUE));*/
                    List<Address> addresses = functionClass.getAddresses(location.getLatitude(), location.getLongitude(), MapActivity.this);
                    address = addresses.get(0).getAddressLine(0);
                    city = addresses.get(0).getLocality();
                    state = addresses.get(0).getAdminArea();
                    zip = addresses.get(0).getPostalCode();
                    country = addresses.get(0).getCountryName();
                    latitude = String.valueOf(addresses.get(0).getLatitude());
                    longitude = String.valueOf(addresses.get(0).getLongitude());
                    String featur = addresses.get(0).getFeatureName();
                    Location_txt.setText(address);
                    Constraints c =new Constraints(address, String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()), featur);
                    SharedPrefManagerLocation.getInstance(MapActivity.this).userLocation(c);
                    main_location = location;
                    centreMapOnLocation(location, "Your Location");
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            };

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100000, 100000, locationListener);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100000, 100000, locationListener);

                Location lastKnownLocation1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                main_location = lastKnownLocation1;
                centreMapOnLocation(lastKnownLocation1, "Your Location");
            } else {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }

        googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

                if (cameraPosition != null) {
                    List<Address> list = functionClass.getAddresses(cameraPosition.target.latitude, cameraPosition.target.longitude, getApplicationContext());
                    if (list.size() > 0) {
                        String feature = list.get(0).getFeatureName();

                        Location_txt.setText(list.get(0).getAddressLine(0));
                        Log.e("hello", list.toString());
                        address = list.get(0).getAddressLine(0);
                        city = list.get(0).getLocality();
                        state = list.get(0).getAdminArea();
                        zip = list.get(0).getPostalCode();
                        country = list.get(0).getCountryName();
                        latitude = String.valueOf(list.get(0).getLatitude());
                        longitude = String.valueOf(list.get(0).getLongitude());
                        Constraints c = new Constraints(address, String.valueOf(list.get(0).getLatitude()), String.valueOf(list.get(0).getLongitude()), feature);
                        if(!SharedPrefManagerLocation.getInstance(MapActivity.this).locationModel(Constraints.LOCATION).isEmpty())
                        {
                            SharedPrefManagerLocation.getInstance(MapActivity.this).logout();
                            SharedPrefManagerLocation.getInstance(MapActivity.this).userLocation(c);
                        }
                        Location_txt.setText(address);
                    }
                }

                //Constraint.lo=Location_txt.getText().toString();
                /*Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                LatLng userLocation = new LatLng(cameraPosition.target.latitude, cameraPosition.target.latitude);
                List<Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocation(cameraPosition.target.latitude, cameraPosition.target.longitude, 1);
                    //Log.i("address", latitude+"");

                } catch (IOException e) {
                    e.printStackTrace();
                }
                String address = addresses.get(0).getAddressLine(0);
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String zip = addresses.get(0).getPostalCode();
                String country = addresses.get(0).getCountryName();
                */

            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i("requesecodea", requestCode + "");

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1000, locationListener);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1000, locationListener);

                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                centreMapOnLocation(lastKnownLocation, "Your Location");
            }
        }
    }

    public void centreMapOnLocation(Location location, String title) {

        if (location != null) {
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
            List<Address> addresses = null;

            try {
                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                //Log.i("address", latitude+"");

            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.e("dsd", addresses + "");
            address = addresses.get(0).getAddressLine(0);
            city = addresses.get(0).getLocality();
            this.city = addresses.get(0).getSubLocality();
            state = addresses.get(0).getAdminArea();
            zip = addresses.get(0).getPostalCode();
            country = addresses.get(0).getCountryName();
            latitude = String.valueOf(addresses.get(0).getLatitude());
            longitude = String.valueOf(addresses.get(0).getLongitude());
            String featur = addresses.get(0).getFeatureName();
            Constraints c = new Constraints(address, String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()), featur);

                SharedPrefManagerLocation.getInstance(MapActivity.this).logout();
                SharedPrefManagerLocation.getInstance(MapActivity.this).userLocation(c);


            Location_txt.setText(address);
            //Constraint.Loc=Location_txt.getText().toString();
            MarkerOptions marker = new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("You Are Here");
            // Changing marker icon
            int height = 60;
            int width = 60;
            //@SuppressLint("UseCompatLoadingForDrawables") BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.location_new_ico);
            //Bitmap b=bitmapdraw.getBitmap();
            Bitmap icon = BitmapFactory.decodeResource(getResources(),
                    R.drawable.location_new_icon);
            Bitmap smallMarker = Bitmap.createScaledBitmap(icon, width, height, false);
            marker.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
            CircleOptions circleOptions = new CircleOptions();
            circleOptions.center(new LatLng(location.getLatitude(), location.getLongitude()));
            circleOptions.radius(700);
            circleOptions.fillColor(Color.TRANSPARENT);
            circleOptions.strokeWidth(6);
            mMap.addCircle(circleOptions);
            //geocoder = new Geocoder(MapsActivity.this, getDefault());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(new LatLng(location.getLatitude(), location.getLongitude()));
            mMap.addMarker(markerOptions.title(String.valueOf(location)));
            mMap.clear();
            mMap.addMarker(marker);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 17.5f));

        } else {
            Toast.makeText(getApplicationContext(), "Location Not Find", Toast.LENGTH_LONG).show();
        }


    }

    public void moveCamera(LatLng latLng) {
        CameraPosition cameraPos = new CameraPosition.Builder()
                .target(latLng)
                .zoom(17.5f).bearing(0).tilt(0).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPos), null);
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);

                Location_txt.setText(place.getAddress());
                address = place.getAddress();

                LatLng loc = place.getLatLng();
                latitude= String.valueOf(loc.latitude);
                longitude= String.valueOf(loc.longitude);
                Constraints c = new Constraints(address, String.valueOf(loc.latitude), String.valueOf(loc.longitude), "");
                    SharedPrefManagerLocation.getInstance(MapActivity.this).logout();
                    SharedPrefManagerLocation.getInstance(MapActivity.this).userLocation(c);





                //Constraint c = new Constraint(address, String.valueOf(list.get(0).getLatitude()), String.valueOf(list.get(0).getLongitude()), feature);
                //SharedPrefManagerLocation.getInstance(MapActivity.this).userLocation(c);
                Location_txt.setText(address);
                //mLatitude = place.getLatLng().latitude;
                //mLongitude = place.getLatLng().longitude;
                //place_id = place.getId();
                // place_url = String.valueOf(place.getWebsiteUri());bb
                //addMarker();
            }
        }
    }
}