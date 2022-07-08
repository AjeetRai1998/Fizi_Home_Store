package digi.coders.capsicostorepartner.helper;

public class Constraints {
    public  static  final  String BASE_URL="https://hungerji.com/assets/uploads/";
    public  static  final  String BASE_URL1="https://hungerji.com/";
    public static final String MERCHANT="merchant/";
    public static final String MASTER_PRODUCT="master_product/";
    public static final String COUPON="coupon/";
    public  static  final String USER="customer/";


    private String Loc = "";
    private String Latitude = "";
    private String Longitude = "";
    private String feature="";

    public static final String LOCATION = "location";
    public static final String FETAURE="feature";
    public static final String LATITUDE="latitude";
    public static final  String LONGITUDE="longitude";

    public Constraints(String loc, String latitude, String longitude,String feature) {
        Loc = loc;
        Latitude = latitude;
        Longitude = longitude;
        this.feature=feature;
    }

    public String getLoc() {
        return Loc;
    }

    public void setLoc(String loc) {
        Loc = loc;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }
}
