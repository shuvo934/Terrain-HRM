package ttit.com.shuvo.ikglhrm.attendance.giveAttendance;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.ColorUtils;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.Priority;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.jakewharton.processphoenix.ProcessPhoenix;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
import java.util.logging.Level;
import java.util.logging.Logger;

import ttit.com.shuvo.ikglhrm.R;
import ttit.com.shuvo.ikglhrm.WaitProgress;
import ttit.com.shuvo.ikglhrm.attendance.giveAttendance.arraylists.AreaList;
import ttit.com.shuvo.ikglhrm.attendance.trackService.GPXFileDecoder;
import ttit.com.shuvo.ikglhrm.attendance.trackService.GPXFileWriter;
import ttit.com.shuvo.ikglhrm.attendance.trackService.MarkerData;
import ttit.com.shuvo.ikglhrm.attendance.trackService.Service;
import ttit.com.shuvo.ikglhrm.attendance.trackService.WaypointList;

import static ttit.com.shuvo.ikglhrm.user_login.Login.userInfoLists;
import static ttit.com.shuvo.ikglhrm.attendance.Attendance.live_tracking_flag;
import static ttit.com.shuvo.ikglhrm.attendance.Attendance.tracking_flag;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.api_url_front;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AttendanceGive extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    TextView currLoc;
    TextView checkInTime;
    CardView chekInButton;
    TextView nameOfCheckIN;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationManager locationManager;
    LocationRequest locationRequest;
    LocationCallback locationCallback;
    String inTime = "";
    String address = "";
    String emp_id = "";
    String timeKey = "last time";
    String getTime = "";
    String lat = "";
    String lon = "";
    SharedPreferences preferences;

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;
//    private Connection connection;
    Timestamp ts;

//    TextView software;
    ImageView autoStartIcon;

    ActivityResultLauncher<Intent> someActivityResultLauncher;

    String machineCode = "";
    String last_time = "";
    String today_date = "";
    String timeToShow = "";
    TextClock digitalClock;
    TextView todayTime;

    ArrayList<AreaList> areaLists;
    String tr_option = "1";
    ArrayList<WaypointList> wptList;
    ArrayList<MarkerData> markerData;

    Logger logger = Logger.getLogger(AttendanceGive.class.getName());
    String parsing_message = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_give);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        currLoc = findViewById(R.id.text_of_cu_loc);
        currLoc.setVisibility(GONE);
        checkInTime = findViewById(R.id.check_int_time);
        chekInButton = findViewById(R.id.check_in_time_button);
        nameOfCheckIN = findViewById(R.id.name_of_punch);
//        software = findViewById(R.id.name_of_company_attendance_give);
        autoStartIcon = findViewById(R.id.app_auto_start_icon);
        digitalClock = findViewById(R.id.text_clock_give_att);
        todayTime = findViewById(R.id.today_date_time_give_att);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
                .setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis(1000)
                .setMaxUpdateDelayMillis(2000)
                .build();

        Intent intent = getIntent();
        last_time = intent.getStringExtra("LAST_TIME");
        today_date = intent.getStringExtra("TODAY_DATE");

        Typeface typeface = ResourcesCompat.getFont(getApplicationContext(),R.font.poppins_bold);
        digitalClock.setTypeface(typeface);

        todayTime.setText(today_date);
        wptList = new ArrayList<>();
        markerData = new ArrayList<>();
        areaLists = new ArrayList<>();
//        software.setText(SoftwareName);

        if (userInfoLists == null) {
            restart("Could Not Get Employee Data. Please Restart the App.");
        }
        else {
            if (userInfoLists.isEmpty()) {
                restart("Could Not Get Employee Data. Please Restart the App.");
            }
            else {
                emp_id = userInfoLists.get(0).getEmp_id();
            }
        }

        preferences = getSharedPreferences(emp_id,MODE_PRIVATE);

        getTime = preferences.getString(timeKey,null);

        String lt = "Your last recorded time : " + last_time;
        checkInTime.setText(lt);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        assert data != null;
                        System.out.println("EKHANE ASHE CHECK: " + data);
                    }
                });

        autoStartIcon.setOnClickListener(v -> PermissionsAll());

    }

    public void PermissionsAll() {
        final Boolean[] paise = {false};
        final Intent[] POWERMANAGER_INTENTS = {

                new Intent().setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity")),
                new Intent().setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity")),
                new Intent().setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity")),
                new Intent().setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.appcontrol.activity.StartupAppControlActivity")),
                new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity")),
                new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.startupapp.StartupAppListActivity")),
                new Intent().setComponent(new ComponentName("com.oppo.safe", "com.oppo.safe.permission.startup.StartupAppListActivity")),
                new Intent().setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity")),
                new Intent().setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.BgStartUpManager")),
                new Intent().setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity")),
                new Intent().setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity")),
                new Intent().setComponent(new ComponentName("com.samsung.android.lool", "com.samsung.android.sm.battery.ui.BatteryActivity")),
                new Intent().setComponent(new ComponentName("com.samsung.android.lool", "com.samsung.android.sm.ui.battery.BatteryActivity")),
                new Intent().setComponent(new ComponentName("com.htc.pitroad", "com.htc.pitroad.landingpage.activity.LandingPageActivity")),
                new Intent().setComponent(new ComponentName("com.asus.mobilemanager", "com.asus.mobilemanager.MainActivity")),
                new Intent().setComponent(new ComponentName("com.transsion.phonemanager", "com.itel.autobootmanager.activity.AutoBootMgrActivity"))
        };

        MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(this);
        alertDialogBuilder.setTitle("Check Auto Start Permission!")
                .setIcon(R.drawable.hrm_new_round_icon_custom)
                .setMessage("Check the App Auto Start Option is On or Off. Auto Start On will provide better solution for the service in the background.")
                .setPositiveButton("Check", (dialog, which) -> {

//                        for (Intent intent : POWERMANAGER_INTENTS)
//                            if (getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
//                                // show dialog to ask user action
//                                System.out.println("PAISE KISU MISU: "+ intent.getComponent().toString());
//
//
//                                break;
//                            }

                    for (Intent intent : POWERMANAGER_INTENTS)
                        if (getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
                            System.out.println("PAISE KISU MISU: "+ intent.getComponent().toString());
                            paise[0] = true;
                            someActivityResultLauncher.launch(intent);
                            break;
                        }
                    if (!paise[0]){
                        Toast.makeText(getApplicationContext(),"Could not find Auto Start Permission Settings.",Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Don't Check", (dialog, which) -> dialog.dismiss());
        AlertDialog alert = alertDialogBuilder.create();
        try {
            alert.show();
        }
        catch (Exception e) {
            restart("App is paused for a long time. Please Start the app again.");
        }


    }

    public void startService() {

        Intent serviceIntent = new Intent(this, Service.class);
        serviceIntent.putExtra("inputExtra", live_tracking_flag);

        startService(serviceIntent);

    }

    public void stopService() {
        Intent serviceIntent = new Intent(this, Service.class);
        stopService(serviceIntent);

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        final LatLng[] lastLatLongitude = {new LatLng(0, 0)};


        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy, hh:mm:ss aa", Locale.ENGLISH);
        SimpleDateFormat dftoShow = new SimpleDateFormat("hh:mm aa", Locale.ENGLISH);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {

                    Log.i("LocationFused ", location.toString());
                    lastLatLongitude[0] = new LatLng(location.getLatitude(), location.getLongitude());
//                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLatLongitude[0], 18));
                    System.out.println(lastLatLongitude[0]);
                    lat = String.valueOf(lastLatLongitude[0].latitude);
                    lon = String.valueOf(lastLatLongitude[0].longitude);
                    Date c = Calendar.getInstance().getTime();
                    Date date = new Date();
                    ts = new Timestamp(date.getTime());
                    System.out.println(ts);
                    inTime = df.format(c);
                    timeToShow = dftoShow.format(c);
                    System.out.println("IN TIME : " + inTime);
                    //getAddress(lastLatLongitude[0].latitude,lastLatLongitude[0].longitude);

                }
            }
        };

        chekInButton.setOnClickListener(v -> {
            if (!inTime.isEmpty()) {
                if (!areaLists.isEmpty()) {
                    if (tracking_flag == 1) {
                        LatLng c_latLng = new LatLng(0,0);
                        float[] distance = new float[1];
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

                        if (machineCode.isEmpty()) {
                            machineCode = areaLists.get(0).getMachine_code();
                        }

                        if (tr_option.equals("1")) {
                            if (isMyServiceRunning()) {
                                MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(AttendanceGive.this);
                                alertDialogBuilder.setTitle("Attendance!")
                                        .setIcon(R.drawable.hrm_new_round_icon_custom)
                                        .setMessage("Do you want to punch & stop your tracker?")
                                        .setPositiveButton("YES", (dialog, which) -> {
                                            dialog.dismiss();
                                            checkAddress();
                                        })
                                        .setNegativeButton("NO", (dialog, which) -> dialog.dismiss());
                                AlertDialog alert = alertDialogBuilder.create();
                                try {
                                    alert.show();
                                }
                                catch (Exception e) {
                                    restart("App is paused for a long time. Please Start the app again.");
                                }
                            }
                            else {
                                MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(AttendanceGive.this);
                                alertDialogBuilder.setTitle("Attendance!")
                                        .setIcon(R.drawable.hrm_new_round_icon_custom)
                                        .setMessage("Do you want to punch & start your tracker?")
                                        .setPositiveButton("YES", (dialog, which) -> {
                                            dialog.dismiss();
                                            checkAddress();
                                        })
                                        .setNegativeButton("NO", (dialog, which) -> dialog.dismiss());
                                AlertDialog alert = alertDialogBuilder.create();
                                try {
                                    alert.show();
                                }
                                catch (Exception e) {
                                    restart("App is paused for a long time. Please Start the app again.");
                                }
                            }
                        }
                        else {
                            MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(AttendanceGive.this);
                            alertDialogBuilder.setTitle("Attendance!")
                                    .setIcon(R.drawable.hrm_new_round_icon_custom)
                                    .setMessage("Do you want to punch & mark location for your current position?")
                                    .setPositiveButton("YES", (dialog, which) -> {
                                        dialog.dismiss();
                                        checkAddress();
                                    })
                                    .setNegativeButton("NO", (dialog, which) -> dialog.dismiss());
                            AlertDialog alert = alertDialogBuilder.create();
                            try {
                                alert.show();
                            }
                            catch (Exception e) {
                                restart("App is paused for a long time. Please Start the app again.");
                            }
                        }
                    }
                    else {
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
                            MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(AttendanceGive.this);
                            alertDialogBuilder.setTitle("Punch Attendance!")
                                    .setIcon(R.drawable.hrm_new_round_icon_custom)
                                    .setMessage("Do you want to punch now?")
                                    .setPositiveButton("YES", (dialog, which) -> {
                                        dialog.dismiss();
                                        checkAddress();
                                    })
                                    .setNegativeButton("NO", (dialog, which) -> dialog.dismiss());
                            AlertDialog alert = alertDialogBuilder.create();
                            try {
                                alert.show();
                            }
                            catch (Exception e) {
                                restart("App is paused for a long time. Please Start the app again.");
                            }
                        }
                        else {
                            if (areaLists.get(0).isCanGive()) {
                                if (machineCode.isEmpty()) {
                                    machineCode = areaLists.get(0).getMachine_code();
                                }
                                MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(AttendanceGive.this);
                                alertDialogBuilder.setTitle("Punch Attendance!")
                                        .setIcon(R.drawable.hrm_new_round_icon_custom)
                                        .setMessage("Do you want to punch now?")
                                        .setPositiveButton("YES", (dialog, which) -> {
                                            dialog.dismiss();
                                            checkAddress();
                                        })
                                        .setNegativeButton("NO", (dialog, which) -> dialog.dismiss());
                                AlertDialog alert = alertDialogBuilder.create();
                                try {
                                    alert.show();
                                }
                                catch (Exception e) {
                                    restart("App is paused for a long time. Please Start the app again.");
                                }
                            }
                            else {
                                if (prev_distance == 0) {
                                    Toast.makeText(getApplicationContext(),"You're not within the required location to mark attendance. Please move closer to the designated area and try again.",Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    int pr_d = Math.round(prev_distance);
                                    Toast.makeText(getApplicationContext(),"You are "+pr_d+" meters away from the designated location. Please move closer to mark your attendance",Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(),"The area of office is not initialized. Please contact with HR administrator",Toast.LENGTH_LONG).show();
                }
            }
            else {
                Toast.makeText(getApplicationContext(),"Please wait for getting the location",Toast.LENGTH_SHORT).show();
            }
        });

        mMap.setOnMapClickListener(latLng -> refreshMarker());

        mMap.setOnMarkerClickListener(marker -> {
            refreshMarker();
            if (marker.getZIndex() == 9999) {
                marker.showInfoWindow();
            }
            else {
                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.location_icon_active));
                marker.showInfoWindow();
            }
            return true;
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                fusedLocationProviderClient.removeLocationUpdates(locationCallback);
                finish();
            }
        });

        getOfficeLocation();
    }

    public void refreshMarker() {
        for (int i = 0; i < markerData.size(); i++) {
            Marker marker = markerData.get(i).getMarker();
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.location_icon));
            marker.hideInfoWindow();
        }
    }

    public void getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(AttendanceGive.this, Locale.ENGLISH);
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            if (Geocoder.isPresent()) {
                Address obj = addresses.get(0);
                address = obj.getAddressLine(0);
                System.out.println("Ekhane ashbe 1st");
            } else {
                address = "";
            }
        }
        catch (IOException e) {
            address = "";
        }
    }

    public void zoomToUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.i("Ekhane", "1");
            return;
        }
        mMap.setMyLocationEnabled(true);
        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
        locationTask.addOnSuccessListener(location -> {
            LatLng latLng;
            if (location != null) {
                latLng = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
            } else {
                latLng = new LatLng(23.6850, 90.3563);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 7));
            }

        });

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());

        for (int i = 0; i < areaLists.size(); i++) {
            if (areaLists.get(i).getLatitude() != null && areaLists.get(i).getLongitude() != null && areaLists.get(i).getCoverage() != null)  {
                LatLng wpt = new LatLng(Double.parseDouble(areaLists.get(i).getLatitude()), Double.parseDouble(areaLists.get(i).getLongitude()));
                mMap.addMarker(new MarkerOptions().position(wpt)
                            .title(areaLists.get(i).getCoa_name())
                            .snippet(areaLists.get(i).getCoa_address()).zIndex(9999)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.office_32_icon)));
                if (!areaLists.get(i).getCoverage().equals("0")) {
                    mMap.addCircle(new CircleOptions()
                            .center(new LatLng(Float.parseFloat(areaLists.get(i).getLatitude()), Float.parseFloat(areaLists.get(i).getLongitude())))
                            .radius(Integer.parseInt(areaLists.get(i).getCoverage()))
                            .strokeColor(getColor(R.color.primaryColor))
                            .strokeWidth(4F)
                            .fillColor(ColorUtils.setAlphaComponent(
                                    ContextCompat.getColor(this, R.color.primaryColor), 50)));
                }
            }
        }


    }

    private void enableGPS() {
        LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 3000)
                .setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis(1000)
                .setMaxUpdateDelayMillis(2000)
                .build();
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(this, locationSettingsResponse -> zoomToUserLocation());

        task.addOnFailureListener(this, e -> {
            if (e instanceof ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    resolvable.startResolutionForResult(AttendanceGive.this,
                            1000);
                } catch (IntentSender.SendIntentException sendEx) {
                    // Ignore the error.
                }
            }
        });
    }

    private boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (Service.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                zoomToUserLocation();
                Log.i("Hoise ", "1");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("Hoise ", "2");
                finish();
            }
        }
    }

    public void checkAddress() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        new Thread(() -> {
            getAddress(Double.parseDouble(lat),Double.parseDouble(lon));
            runOnUiThread(() -> {
                if (!address.isEmpty()) {
                    giveAttendance();
                }
                else {
                    waitProgress.dismiss();
                    MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(AttendanceGive.this);
                    alertDialogBuilder.setTitle("Failed to get address")
                            .setIcon(R.drawable.hrm_new_round_icon_custom)
                            .setMessage("Could not get address of the location due to internet disruption. Please try again")
                            .setPositiveButton("Retry", (dialog, which) -> {
                                checkAddress();
                                dialog.dismiss();
                            })
                            .setNegativeButton("Cancel",(dialog, which) ->  dialog.dismiss());


                    AlertDialog alert = alertDialogBuilder.create();
                    alert.setCancelable(false);
                    alert.setCanceledOnTouchOutside(false);
                    try {
                        alert.show();
                    }
                    catch (Exception e) {
                        restart("App is paused for a long time. Please Start the app again.");
                    }
                }
            });
        }).start();
    }

    public void giveAttendance() {
        conn = false;
        connected = false;

        String attendaceUrl = api_url_front + "attendance/giveAttendance";

        RequestQueue requestQueue = Volley.newRequestQueue(AttendanceGive.this);

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
                    parsing_message = string_out;
                    connected = false;
                }
                updateLayout();
            }
            catch (JSONException e) {
                logger.log(Level.WARNING,e.getMessage(),e);
                parsing_message = e.getLocalizedMessage();
                connected = false;
                updateLayout();
            }
        }, error -> {
            logger.log(Level.WARNING,error.getMessage(),error);
            parsing_message = error.getLocalizedMessage();
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
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                System.out.println("Ekhane Ashbe 3rd");
                checkInTime.setVisibility(VISIBLE);

                String ss = "Your last recorded time : "+timeToShow;
                checkInTime.setText(ss);

                SharedPreferences.Editor editor = preferences.edit();
                editor.remove(timeKey);
                editor.putString(timeKey,ss);
                editor.apply();
                editor.commit();
                String puncher;
                if (address.isEmpty()) {
                    address = "No Address found for ("+lat+", "+lon+")";
                    puncher = "Punched at "+ timeToShow + " in ("+address+")";
                }
                else {
                    puncher = "Punched at "+ timeToShow + " in "+address;
                }
                currLoc.setText(puncher);
                currLoc.setVisibility(VISIBLE);

                if (tracking_flag == 1) {
                    if (tr_option.equals("1")) {
                        if (isMyServiceRunning()) {
                            System.out.println("Service Stopped");
                            stopService();
                            String tt = "PUNCH & START TRACKER";
                            nameOfCheckIN.setText(tt);
                        } else {
                            System.out.println("Service Started");
                            startService();
                            String tt = "PUNCH & STOP TRACKER";
                            nameOfCheckIN.setText(tt);
                        }
                    }
                    else {
                        saveLocationFile();
                        LatLng wpt = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));
                        Marker marker = mMap.addMarker(new MarkerOptions().position(wpt).title(address).snippet(timeToShow).icon(BitmapDescriptorFactory.fromResource(R.drawable.location_icon)));
                        int x = markerData.size();
                        markerData.add(new MarkerData(marker,String.valueOf(x)));
                    }
                }

                MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(AttendanceGive.this);
                alertDialogBuilder.setTitle("Success!")
                        .setIcon(R.drawable.hrm_new_round_icon_custom)
                        .setMessage("Your Attendance is Recorded!")
                        .setPositiveButton("OK", (dialog, which) -> dialog.dismiss());

                AlertDialog alert = alertDialogBuilder.create();
                try {
                    alert.show();
                }
                catch (Exception e) {
                    restart("App is paused for a long time. Please Start the app again.");
                }

                conn = false;
                connected = false;
            }
            else {
                alertMessageAtt();
            }
        }
        else {
            alertMessageAtt();
        }
    }

    public void alertMessageAtt() {
        if (parsing_message != null) {
            if (parsing_message.isEmpty() || parsing_message.equals("null")) {
                parsing_message = "Server problem or Internet not connected";
            }
        }
        else {
            parsing_message = "Server problem or Internet not connected";
        }
        MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(AttendanceGive.this);
        alertDialogBuilder.setTitle("System Warning!")
                .setIcon(R.drawable.hrm_new_round_icon_custom)
                .setMessage("Message: "+parsing_message+".\n"+"Please try again.")
                .setPositiveButton("Retry", (dialog, which) -> {
                    checkAddress();
                    dialog.dismiss();
                })
                .setNegativeButton("Cancel",(dialog, which) -> dialog.dismiss());

        AlertDialog alert = alertDialogBuilder.create();
        alert.setCancelable(false);
        alert.setCanceledOnTouchOutside(false);
        try {
            alert.show();
        }
        catch (Exception e) {
            restart("App is paused for a long time. Please Start the app again.");
        }
    }

    public void getOfficeLocation() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;

        areaLists = new ArrayList<>();
        tr_option = "1";

        String offLocationUrl = api_url_front + "attendance/getNewOffLatLong?emp_id="+emp_id;
        String trOptionUrl = api_url_front + "utility/getTrackerOption";

        RequestQueue requestQueue = Volley.newRequestQueue(AttendanceGive.this);

        StringRequest trOptionReq = new StringRequest(Request.Method.GET, trOptionUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    JSONObject info = array.getJSONObject(0);
                    tr_option = info.getString("tr_option")
                            .equals("null") ? "1" : info.getString("tr_option");
                }
                if (tracking_flag == 1) {
                    if (tr_option.equals("2")) {
                        getMapData();
                    }
                    else {
                        connected = true;
                        updateInfo();
                    }
                }
                else {
                    connected = true;
                    updateInfo();
                }
            } catch (JSONException e) {
                logger.log(Level.WARNING, e.getMessage(), e);
                parsing_message = e.getLocalizedMessage();
                connected = false;
                updateInfo();
            }
        }, error -> {
            logger.log(Level.WARNING, error.getMessage(), error);
            parsing_message = error.getLocalizedMessage();
            conn = false;
            connected = false;
            updateInfo();
        });

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
                        String coa_name = offLocInfo.getString("coa_name")
                                .equals("null") ? "" : offLocInfo.getString("coa_name");
                        String coa_address = offLocInfo.getString("coa_address")
                                .equals("null") ? "" : offLocInfo.getString("coa_address");

                        areaLists.add(new AreaList(coa_latitude,coa_longitude,coa_coverage,co_id,code,can_give.equals("1"),coa_name,coa_address));

                    }
                }
                requestQueue.add(trOptionReq);
            }
            catch (JSONException e) {
                logger.log(Level.WARNING,e.getMessage(),e);
                parsing_message = e.getLocalizedMessage();
                connected = false;
                updateInfo();
            }
        }, error -> {
            logger.log(Level.WARNING,error.getMessage(),error);
            parsing_message = error.getLocalizedMessage();
            conn = false;
            connected = false;
            updateInfo();
        });

        requestQueue.add(offLocReq);
    }

    private void updateInfo() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                if (tracking_flag == 1) {
                    if (tr_option.equals("1")) {
                        if (isMyServiceRunning()) {
                            String tt = "PUNCH & STOP TRACKER";
                            nameOfCheckIN.setText(tt);
                        } else {
                            String tt = "PUNCH & START TRACKER";
                            nameOfCheckIN.setText(tt);
                        }
                        autoStartIcon.setVisibility(VISIBLE);
                    }
                    else if (tr_option.equals("2")){
                        String  tt = "PUNCH & MARK";
                        nameOfCheckIN.setText(tt);
                        autoStartIcon.setVisibility(GONE);
                    }
                    else {
                        String  tt = "PUNCH";
                        nameOfCheckIN.setText(tt);
                        autoStartIcon.setVisibility(GONE);
                    }
                }
                else {
                    String  tt = "PUNCH";
                    nameOfCheckIN.setText(tt);
                    autoStartIcon.setVisibility(GONE);
                }
                enableGPS();

                conn = false;
                connected = false;
            }
            else {
                alertMessageOff();
            }
        }
        else {
            alertMessageOff();
        }
    }

    public void alertMessageOff() {
        if (parsing_message != null) {
            if (parsing_message.isEmpty() || parsing_message.equals("null")) {
                parsing_message = "Server problem or Internet not connected";
            }
        }
        else {
            parsing_message = "Server problem or Internet not connected";
        }
        MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(AttendanceGive.this);
        alertDialogBuilder.setTitle("System Warning!")
                .setIcon(R.drawable.hrm_new_round_icon_custom)
                .setMessage("Message: "+parsing_message+".\n"+"Please try again.")
                .setPositiveButton("Retry", (dialog, which) -> {
                    getOfficeLocation();
                    dialog.dismiss();
                })
                .setNegativeButton("Cancel",(dialog, which) -> {
                    dialog.dismiss();
                    finish();
                });

        AlertDialog alert = alertDialogBuilder.create();
        alert.setCancelable(false);
        alert.setCanceledOnTouchOutside(false);
        try {
            alert.show();
        }
        catch (Exception e) {
            restart("App is paused for a long time. Please Start the app again.");
        }
    }

    public void getMapData() {
        wptList = new ArrayList<>();
        markerData = new ArrayList<>();
        ArrayList<String> addresses = new ArrayList<>();
        mMap.clear();
        new Thread(() -> {
            Date c = Calendar.getInstance().getTime();

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);

            String fileName = sdf.format(c);
            fileName = fileName.toUpperCase();
            fileName = emp_id+"_"+fileName+"_track";

            File myExternalFile = new File(getExternalFilesDir(null),fileName+".gpx");

            if (!myExternalFile.exists()) {
                System.out.println("No Data Found");
            }
            else {
                wptList = GPXFileDecoder.decodeWPT(myExternalFile);

                if (wptList.isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Saved Location Not Found",Toast.LENGTH_SHORT).show();
                }
                else {
                    for (int i = 0; i< wptList.size(); i++) {
                        String addss = getMapAddress(wptList.get(i).getLocation().getLatitude(), wptList.get(i).getLocation().getLongitude());
                        addresses.add(addss);
                    }
                }
            }
            runOnUiThread(() -> {
                for (int i = 0; i < wptList.size(); i++) {
                    LatLng wpt = new LatLng(wptList.get(i).getLocation().getLatitude(), wptList.get(i).getLocation().getLongitude());
                    Marker marker = mMap.addMarker(new MarkerOptions().position(wpt).title(addresses.get(i)).snippet(wptList.get(i).getTime()).icon(BitmapDescriptorFactory.fromResource(R.drawable.location_icon)));
                    markerData.add(new MarkerData(marker,String.valueOf(i)));
                }
                connected = true;
                updateInfo();
            });
        }).start();

    }

    public String getMapAddress(double lat, double lng) {
        String adds = "";
        Geocoder geocoder = new Geocoder(AttendanceGive.this, Locale.ENGLISH);
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            if (Geocoder.isPresent()) {
                Address obj = addresses.get(0);
                adds = obj.getAddressLine(0);
            }
            else {
                adds = "";
            }
            return adds;
        }
        catch (IOException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            return adds;
        }
    }

    private void saveLocationFile() {
        ArrayList<String> trk = new ArrayList<>();

        String wpt = "\t<wpt lat=\""+ lat +"\" lon=\""+ lon+"\">\n" +
                "\t\t<name>TTIT</name>\n" +
                "\t\t<time>"+timeToShow+"</time>\n"+
                "\t</wpt>";
        trk.add(wpt);

        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);

        String fileName = sdf.format(c);
        fileName = fileName.toUpperCase();
        fileName = emp_id+"_"+fileName+"_track";

        File myExternalFile = new File(getExternalFilesDir(null),fileName+".gpx");

        if (myExternalFile.exists()) {
            try {
                System.out.println("EXISTING FILE");
                String gpxFile = getExternalFilesDir(null).getPath() + File.separator +  fileName +".gpx";
                BufferedReader bufferedReader = new BufferedReader(new FileReader(gpxFile));
                String line;
                String input = "";

                while ((line = bufferedReader.readLine()) != null) {
                    input += line + '\n';
                }

                bufferedReader.close();

                if (input.contains("</gpx>")){
                    System.out.println("Got It");
                    String newInput = input.replace("</gpx>","");
                    GPXFileWriter.upDateGpxFile("TTITGenerator",trk,myExternalFile,newInput);
                    Toast.makeText(getApplicationContext(), "Your Attendance location has been saved", Toast.LENGTH_SHORT).show();
                }
            }
            catch (IOException e) {
                logger.log(Level.WARNING, e.getMessage(), e);
                Toast.makeText(getApplicationContext(), "Your Attendance location could not save", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            try {
                GPXFileWriter.writeGpxFile("TTITGenerator", trk, myExternalFile);
                Toast.makeText(getApplicationContext(), "Your Attendance location has been saved", Toast.LENGTH_SHORT).show();
            }
            catch (IOException e) {
                logger.log(Level.WARNING, e.getMessage(), e);
                Toast.makeText(getApplicationContext(), "Your Attendance location could not save", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void restart(String msg) {
        try {
            ProcessPhoenix.triggerRebirth(getApplicationContext());
        }
        catch (Exception e) {
            Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
            System.exit(0);
        }
    }
}