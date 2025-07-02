package ttit.com.shuvo.ikglhrm.attendance.att_widget;


import static ttit.com.shuvo.ikglhrm.utilities.Constants.CENTER_API_FRONT;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.api_url_front;

import android.Manifest;
import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import ttit.com.shuvo.ikglhrm.R;
import ttit.com.shuvo.ikglhrm.attendance.giveAttendance.arraylists.AreaList;

public class WidgetAttendanceWorker extends Worker {

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

    String machineCode = "";
    private LocationCallback locationCallback;
    ArrayList<AreaList> areaLists;
    SharedPreferences sharedPreferences;

    public WidgetAttendanceWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
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
//        locationRequest = new LocationRequest.Builder().build();
//        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
////        locationRequest.setInterval(5);
//        locationRequest.setInterval(5000);
//        locationRequest.setFastestInterval(1000);
        locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
                .setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis(1000)
                .setMaxUpdateDelayMillis(1500)
                .build();

        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (api_url_front != null) {
            if (!api_url_front.isEmpty()) {
                if (gps) {
                    RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.attendance_widget);
                    views.setViewVisibility(R.id.attendance_widget_button, View.GONE);
                    views.setViewVisibility(R.id.attendance_widget_progress_bar, View.VISIBLE);

                    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(mContext);
                    appWidgetManager.updateAppWidget(new ComponentName(mContext, AttendanceWidget.class), views);
                    locationCallback = new LocationCallback() {
                        @Override
                        public void onLocationResult(@NonNull LocationResult locationResult) {
                            super.onLocationResult(locationResult);
                        }
                    };
//            LocationRequest mLocationRequest = LocationRequest.create();
//            mLocationRequest.setInterval(5000);
//            mLocationRequest.setFastestInterval(1000);
//            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

                    try {
                        fusedLocationProviderClient.requestLocationUpdates(locationRequest, null);
                    } catch (SecurityException unlikely) {
                        //Utils.setRequestingLocationUpdates(this, false);
                        Log.e("TAG", "Lost location permission. Could not request updates. " + unlikely);
                        updateWidget();
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
                                        System.out.println(ts);
                                        inTime = df.format(c);
                                        timeToShow = dftoShow.format(c);
                                        System.out.println("IN TIME : " + inTime);
                                        getOfficeLocation();
                                    } else {
                                        task.addOnFailureListener(e -> {
                                        });
                                        Log.w("TAG", "Failed to get location.");
                                        updateWidget();
                                        getNotification("Attendance System", "Failed to get location.");
                                    }
                                })
                                .addOnFailureListener(e -> {
                                });
                    }
                    catch (SecurityException unlikely) {
                        Log.e("TAG", "Lost location permission." + unlikely);
                        updateWidget();
                        getNotification("Attendance System", "Lost location permission.");
                    }

                } else {
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

//        getOfficeLocation();

        return Result.success();
    }

//    private LocationCallback locationCallback = new LocationCallback() {
//        @Override
//        public void onLocationResult(@NonNull LocationResult locationResult) {
//            if (locationResult == null) {
//                System.out.println("ADADADA!!!");
//                return;
//            }
//            for (Location location : locationResult.getLocations()) {
//                System.out.println("ADADAD!!!!!A!!!");
//                Log.i("LocationFused ", location.toString());
//                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy, hh:mm:ss aa", Locale.getDefault());
//                SimpleDateFormat dftoShow = new SimpleDateFormat("hh:mm aa", Locale.getDefault());
//                lastLatLongitude[0] = new LatLng(location.getLatitude(), location.getLongitude());
//                lat = String.valueOf(lastLatLongitude[0].latitude);
//                lon = String.valueOf(lastLatLongitude[0].longitude);
//                Date c = Calendar.getInstance().getTime();
//                Date date = new Date();
//                ts = new Timestamp(date.getTime());
//                System.out.println(ts.toString());
//                inTime = df.format(c);
//                timeToShow = dftoShow.format(c);
//                System.out.println("IN TIME : " + inTime);
//                //getAddress(lastLatLongitude[0].latitude,lastLatLongitude[0].longitude);
////                stopLocationUpdate();
//            }
//        }
//    };

    private void stopLocationUpdate() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        getOfficeLocation();
    }
    public void getOfficeLocation() {
        areaLists = new ArrayList<>();

        String offLocationUrl = api_url_front + "attendance/getNewOffLatLong?emp_id="+emp_id;

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
                        String coa_latitude = offLocInfo.getString("coa_latitude")
                                .equals("null") ? null : offLocInfo.getString("coa_latitude");
                        String coa_longitude = offLocInfo.getString("coa_longitude")
                                .equals("null") ? null : offLocInfo.getString("coa_longitude");
                        String coa_coverage = offLocInfo.getString("coa_coverage")
                                .equals("null") ? null : offLocInfo.getString("coa_coverage");
                        String co_id = offLocInfo.getString("coa_id")
                                .equals("null") ? null : offLocInfo.getString("coa_id");
                        String code = offLocInfo.getString("machine_code")
                                .equals("null") ? null : offLocInfo.getString("machine_code");
                        String can_give = offLocInfo.getString("can_give")
                                .equals("null") ? "0" : offLocInfo.getString("can_give");

                        areaLists.add(new AreaList(coa_latitude,coa_longitude,coa_coverage,co_id,code,can_give.equals("1")));
                    }
                }
                connected = true;
                updateInfo();
            }
            catch (JSONException e) {
                connected = false;
                updateInfo();
            }
        }, error -> {
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
                    if (!areaLists.isEmpty()) {
                        LatLng c_latLng = new LatLng(0,0);
                        float[] distance = new float[1];
                        boolean found = false;
                        float prev_distance = 0;
                        String prev_mach_code = "";

                        machineCode = "";

                        for (int i = 0; i < areaLists.size(); i++) {
                            String officeLatitude = areaLists.get(i).getLatitude();
                            String officeLongitude = areaLists.get(i).getLongitude();
                            String coverage = areaLists.get(i).getCoverage();

                            if (officeLatitude != null && officeLongitude != null) {
                                if (!officeLatitude.isEmpty() && !officeLongitude.isEmpty()) {
                                    c_latLng = new LatLng(Double.parseDouble(officeLatitude),Double.parseDouble(officeLongitude));
                                }
                            }

                            if (c_latLng.latitude != 0 && c_latLng.longitude != 0) {
                                Location.distanceBetween(c_latLng.latitude,c_latLng.longitude,lastLatLongitude[0].latitude,lastLatLongitude[0].longitude,distance);

                                float radius = 0;
                                if (coverage != null) {
                                    if (!coverage.isEmpty()) {
                                        radius = Float.parseFloat(coverage);
                                    }
                                }

                                machineCode = areaLists.get(i).getMachine_code();

                                if (distance[0] <= radius) {
                                    found = true;
                                    prev_mach_code = machineCode;
                                    break;
                                }
                                else {
                                    float dd = distance[0] - radius;
                                    if (prev_distance == 0) {
                                        prev_distance = dd;
                                        prev_mach_code = machineCode;
                                    }
                                    else if (dd < prev_distance) {
                                        prev_distance = dd;
                                        prev_mach_code = machineCode;
                                    }
                                }
                            }
                        }

                        machineCode = prev_mach_code;

                        if (found) {
                            new CheckAddress().execute();
                        }
                        else {
                            if (areaLists.get(0).isCanGive()) {
                                if (machineCode.isEmpty()) {
                                    machineCode = areaLists.get(0).getMachine_code();
                                }
                                new CheckAddress().execute();
                            }
                            else {
                                updateWidget();
                                if (prev_distance == 0) {
                                    getNotification("Attendance System", "You are not around your office area");
                                }
                                else {
                                    getNotification("Attendance System", "You are not around your office area. You are "+prev_distance+" Meters away from office.");
                                }
                            }
                        }
                    }
                    else {
                        updateWidget();
                        getNotification("Attendance System", "You don't have any permission to give attendance from this app. Please contact with administrator");
                    }
                }
                else {
                    updateWidget();
                    getNotification("Attendance System","Failed to get Location");
                }
            }
            else {
                updateWidget();
                getNotification("Attendance System","There is a network issue in the server. Please Try later.");
            }
        }
        else {
            updateWidget();
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
        catch (IOException | InterruptedException ignored) {}

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
                updateWidget();
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
                address = obj.getAddressLine(0);
                System.out.println("Ekhane ashbe 1st");
            } else {
                address = "";
            }
        } catch (IOException e) {
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
                connected = false;
                updateLayout();
            }
        }, error -> {
            conn = false;
            connected = false;
            updateLayout();
        }) {
            @Override
            public Map<String, String> getHeaders() {
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
                updateWidget();
                getNotification("Attendance System", "Your Attendance is Recorded at "+timeToShow+".");

            }
            else {
                updateWidget();
                getNotification("Attendance System", "There is a network issue in the server. Please Try later.");
            }
        }
        else {
            updateWidget();
            getNotification("Attendance System", "Please Check Your Internet Connection.");
        }
    }

    private void updateWidget() {
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.attendance_widget);
        views.setViewVisibility(R.id.attendance_widget_button, View.VISIBLE);
        views.setViewVisibility(R.id.attendance_widget_progress_bar, View.GONE);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(mContext);
        appWidgetManager.updateAppWidget(new ComponentName(mContext, AttendanceWidget.class),views);
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

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            updateWidget();
            getNotification("Attendance System","Please check your Location Permission to access this feature.");
            return;
        }
        System.out.println("ADADADA");
    }
}
