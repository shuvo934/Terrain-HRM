package ttit.com.shuvo.ikglhrm.dashboard;

import static ttit.com.shuvo.ikglhrm.utilities.Constants.CENTER_API_FRONT;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.api_url_front;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import ttit.com.shuvo.ikglhrm.R;

public class GeoFenceWorker extends Worker {

    FusedLocationProviderClient fusedLocationProviderClient;
    LocationManager locationManager;
    LocationRequest locationRequest;
    String inTime = "";
    String address = "";
    Context mContext;
    public static final String WORKER_W_EMP_ID = "WORKER_W_EMP_ID";
    String emp_id = "";
    LatLng[] lastLatLongitude = {new LatLng(0, 0)};
    String lat = "";
    String lon = "";
    Timestamp ts;
    String timeToShow = "";
    private Boolean conn = false;
    private Boolean connected = false;
    String officeLatitude = "";
    String officeLongitude = "";
    String coverage = "";
    String machineCode = "";
    private LocationCallback locationCallback;
    SharedPreferences sharedPreferences;

    public GeoFenceWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        mContext = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        emp_id = getInputData().getString(WORKER_W_EMP_ID);
        api_url_front = sharedPreferences.getString(CENTER_API_FRONT,null);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
                .setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis(1000)
                .setMaxUpdateDelayMillis(1500)
                .build();

        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (api_url_front != null) {
            if (!api_url_front.isEmpty()) {
                if (gps) {
                    locationCallback = new LocationCallback() {
                        @Override
                        public void onLocationResult(@NonNull LocationResult locationResult) {
                            super.onLocationResult(locationResult);
                        }
                    };

                    try {
                        fusedLocationProviderClient.requestLocationUpdates(locationRequest, null);
                    } catch (SecurityException unlikely) {
                        Log.e("TAG", "Lost location permission. Could not request updates. " + unlikely);
                        getNotification("Attendance System", "Lost location permission. Could not request updates.");
                    }
                    try {
                        fusedLocationProviderClient
                                .getLastLocation()
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful() && task.getResult() != null) {
                                        Location mLocation = task.getResult();
                                        Log.d("TAG", "Location : " + mLocation);
                                        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
                                        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy, hh:mm:ss aa", Locale.getDefault());
                                        SimpleDateFormat dftoShow = new SimpleDateFormat("hh:mm aa", Locale.getDefault());
                                        lastLatLongitude[0] = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
                                        lat = String.valueOf(lastLatLongitude[0].latitude);
                                        lon = String.valueOf(lastLatLongitude[0].longitude);
                                        Date c = Calendar.getInstance().getTime();
                                        Date date = new Date();
                                        ts = new Timestamp(date.getTime());
                                        System.out.println(ts.toString());
                                        inTime = df.format(c);
                                        timeToShow = dftoShow.format(c);
                                        System.out.println("IN TIME : " + inTime);
                                        getOfficeLocation();
                                    } else {
                                        task.addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                e.printStackTrace();
                                            }
                                        });
                                        Log.w("TAG", "Failed to get location.");
                                        getNotification("Attendance System", "Failed to get location.");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        e.printStackTrace();
                                    }
                                });
                    }
                    catch (SecurityException unlikely) {
                        Log.e("TAG", "Lost location permission." + unlikely);
                        getNotification("Attendance System", "Lost location permission.");
                    }

                }
                else {
                    getNotification("Attendance System", "Your GPS is disabled. Please enable it and try again.");
                }
            }
            else {
                getNotification("Attendance System", "Could not find destined server to upload attendance time.");
            }
        }
        else {
            getNotification("Attendance System", "Could not find destined server to upload attendance time.");
        }


        return Result.success();
    }

    public void getOfficeLocation() {

        String offLocationUrl = api_url_front + "attendance/getOffLatLong/"+emp_id;

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        StringRequest offLocReq = new StringRequest(Request.Method.GET, offLocationUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject offLocInfo = array.getJSONObject(i);
                        officeLatitude = offLocInfo.getString("coa_latitude").equals("null") ? null : offLocInfo.getString("coa_latitude");
                        officeLongitude = offLocInfo.getString("coa_longitude").equals("null") ? null : offLocInfo.getString("coa_longitude");
                        coverage = offLocInfo.getString("coa_coverage").equals("null") ? null : offLocInfo.getString("coa_coverage");
                    }
                }
                connected = true;
                updateInfo();
            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                updateInfo();
            }
        }, error -> {
            error.printStackTrace();
            conn = false;
            connected = false;
            updateInfo();
        });

        requestQueue.add(offLocReq);
    }

    public void updateInfo() {
        if (conn) {
            if (connected) {
                conn = false;
                connected = false;
                if (!inTime.isEmpty()) {
                    LatLng c_latLng = new LatLng(0,0);
                    if (officeLatitude != null && officeLongitude != null) {
                        if (!officeLatitude.isEmpty() && !officeLongitude.isEmpty()) {
                            c_latLng = new LatLng(Double.parseDouble(officeLatitude),Double.parseDouble(officeLongitude));
                        }
                    }
                    if (c_latLng.latitude != 0 && c_latLng.longitude != 0) {
                        float[] distance = new float[1];
                        Location.distanceBetween(c_latLng.latitude,c_latLng.longitude,lastLatLongitude[0].latitude,lastLatLongitude[0].longitude,distance);

                        float radius = 0;
                        if (coverage != null) {
                            if (!coverage.isEmpty()) {
                                radius = Float.parseFloat(coverage);
                            }
                        }

                        if (radius == 0) {
                            machineCode = "3";
                        } else {
                            machineCode = "1";
                        }

                        if (distance[0] <= radius || radius == 0) {
                            new CheckAddress().execute();
                        }
                        else {
                            float dd = distance[0] - radius;
                            getNotification("Attendance System","You are not around your office area. You are "+dd+" Meters away from office." );
                        }
                    }
                    else {
                        machineCode = "3";
                        new CheckAddress().execute();
                    }
                }
                else {
                    getNotification("Attendance System","Failed to get Location");
                }
            }
            else {
                getNotification("Attendance System","There is a network issue in the server. Please Try later.");
            }
        }
        else {
            getNotification("Attendance System","Please Check Your Internet Connection.");
        }
    }

    public boolean isConnected() {
        boolean connected = false;
        boolean isMobile = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            @SuppressLint("MissingPermission") NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }

    public boolean isOnline() {

        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        }
        catch (IOException | InterruptedException e)          { e.printStackTrace(); }

        return false;
    }

    public class CheckAddress extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isConnected() && isOnline()) {
                conn = true;
                getAddress(Double.parseDouble(lat),Double.parseDouble(lon));
            }
            else {
                conn = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (conn) {
                conn = false;
                giveAttendance();
            }
            else {
                getNotification("Attendance System","Please Check Your Internet Connection.");
            }
        }
    }

    public void getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            if (Geocoder.isPresent()) {
                Address obj = addresses.get(0);
                String adds = obj.getAddressLine(0);
                address = adds;
                System.out.println("Ekhane ashbe 1st");
            } else {
                address = "";
            }

        } catch (IOException e) {
            e.printStackTrace();
            address = "";
        }
    }

    public void giveAttendance() {
        conn = false;
        connected = false;

        String attendaceUrl = api_url_front + "attendance/giveAttendance";

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        StringRequest attReq = new StringRequest(Request.Method.POST, attendaceUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String string_out = jsonObject.getString("string_out");
                if (string_out.equals("Successfully Created")) {
                    connected = true;
                }
                else {
                    System.out.println(string_out);
                    connected = false;
                }
                updateLayout();
            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                updateLayout();
            }
        }, error -> {
            error.printStackTrace();
            conn = false;
            connected = false;
            updateLayout();
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("P_EMP_ID",emp_id);
                headers.put("P_PUNCH_TIME",ts.toString());
                headers.put("P_MACHINE_CODE",machineCode);
                headers.put("P_LATITUDE",lat);
                headers.put("P_LONGITUDE",lon);
                headers.put("P_ADDRESS",address);
                return  headers;
            }
        };

        requestQueue.add(attReq);
    }

    private  void updateLayout() {
        if (conn) {
            if (connected) {
                connected = false;
                conn = false;
                getNotification("Attendance System", "Your Attendance is Recorded at "+timeToShow+".");

            }
            else {
                getNotification("Attendance System", "There is a network issue in the server. Please Try later.");
            }
        }
        else {
            getNotification("Attendance System", "Please Check Your Internet Connection.");
        }
    }

    @SuppressLint("MissingPermission")
    public void getNotification(String title, String msg) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, mContext.getString(R.string.att_channel_id))
                .setSmallIcon(R.drawable.hrm_new_icon_wb)
                .setContentTitle(title)
                .setContentText(msg)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(mContext);
        notificationManagerCompat.notify(222, builder.build());
    }
}
