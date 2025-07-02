package ttit.com.shuvo.ikglhrm.attendance.giveAttendance;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;

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
import android.view.View;
import android.widget.Button;
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
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;


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
import ttit.com.shuvo.ikglhrm.attendance.trackService.Service;

import static ttit.com.shuvo.ikglhrm.Login.userInfoLists;
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

    Logger logger = Logger.getLogger(AttendanceGive.class.getName());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_give);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        currLoc = findViewById(R.id.text_of_cu_loc);
        currLoc.setVisibility(View.GONE);
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

//        software.setText(SoftwareName);

        if (tracking_flag == 1) {

            if (isMyServiceRunning()) {
                String tt = "PUNCH & STOP TRACKER";
                nameOfCheckIN.setText(tt);
            } else {
                String tt = "PUNCH & START TRACKER";
                nameOfCheckIN.setText(tt);
            }

        } else {
            String  tt = "PUNCH";
            nameOfCheckIN.setText(tt);
        }

        emp_id = userInfoLists.get(0).getEmp_id();

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

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Check Auto Start Permission!")
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
                .setNegativeButton("Don't Check", (dialog, which) -> {

                });
        AlertDialog alert = builder.create();
        alert.show();


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
        final LatLng[] lastLatLongitude = {new LatLng(0, 0)};


        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy, hh:mm:ss aa", Locale.ENGLISH);
        SimpleDateFormat dftoShow = new SimpleDateFormat("hh:mm aa", Locale.ENGLISH);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {

                    Log.i("LocationFused ", location.toString());
                    lastLatLongitude[0] = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLatLongitude[0], 18));
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

                        if (isMyServiceRunning()) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(AttendanceGive.this);
                            builder.setTitle("Attendance!")
                                    .setMessage("Do you want to punch & stop your tracker?")
                                    .setPositiveButton("YES", (dialog, which) -> checkAddress())
                                    .setNegativeButton("NO", (dialog, which) -> {
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                        else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(AttendanceGive.this);
                            builder.setTitle("Attendance!")
                                    .setMessage("Do you want to punch & start your tracker?")
                                    .setPositiveButton("YES", (dialog, which) -> checkAddress())
                                    .setNegativeButton("NO", (dialog, which) -> {
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(AttendanceGive.this);
                            builder.setTitle("Punch Attendance!")
                                    .setMessage("Do you want to punch now?")
                                    .setPositiveButton("YES", (dialog, which) -> checkAddress())
                                    .setNegativeButton("NO", (dialog, which) -> {
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                        else {
                            if (areaLists.get(0).isCanGive()) {
                                if (machineCode.isEmpty()) {
                                    machineCode = areaLists.get(0).getMachine_code();
                                }
                                AlertDialog.Builder builder = new AlertDialog.Builder(AttendanceGive.this);
                                builder.setTitle("Punch Attendance!")
                                        .setMessage("Do you want to punch now?")
                                        .setPositiveButton("YES", (dialog, which) -> checkAddress())
                                        .setNegativeButton("NO", (dialog, which) -> {
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                            else {
                                if (prev_distance == 0) {
                                    Toast.makeText(getApplicationContext(),"You are not around your office area",Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(getApplicationContext(),"You are not around your office area. You are "+prev_distance+" Meters away from office.",Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(),"You don't have any permission to give attendance from this app. Please contact with administrator",Toast.LENGTH_LONG).show();
                }
            }
            else {
                Toast.makeText(getApplicationContext(),"Please wait for getting the location",Toast.LENGTH_SHORT).show();
            }
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

    public void zoomToUserLocation(){
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
                if (!areaLists.get(i).getCoverage().equals("0")) {
                    mMap.addCircle(new CircleOptions()
                            .center(new LatLng(Float.parseFloat(areaLists.get(i).getLatitude()), Float.parseFloat(areaLists.get(i).getLongitude())))
                            .radius(Integer.parseInt(areaLists.get(i).getCoverage()))
                            .strokeColor(getColor(R.color.primaryColor))
                            .strokeWidth(4F)
                            .fillColor(getColor(R.color.primaryColor_generatedAlpha)));
                }
            }
        }


    }

//    private void enableGPS() {
//        if (googleApiClient == null) {
//            googleApiClient = new GoogleApiClient.Builder(this)
//                    .addApi(LocationServices.API).addConnectionCallbacks(AttendanceGive.this)
//                    .addOnConnectionFailedListener(AttendanceGive.this).build();
//            googleApiClient.connect();
//            LocationRequest locationRequest = LocationRequest.create();
//            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//            locationRequest.setInterval(5 * 1000);
//            locationRequest.setFastestInterval(1000);
//            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
//                    .addLocationRequest(locationRequest);
//
//            // **************************
//            builder.setAlwaysShow(true); // this is the key ingredient
//            // **************************
//
//            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi
//                    .checkLocationSettings(googleApiClient, builder.build());
//            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
//                @Override
//                public void onResult(LocationSettingsResult result) {
//                    final Status status = result.getStatus();
//                    final LocationSettingsStates state = result
//                            .getLocationSettingsStates();
//                    switch (status.getStatusCode()) {
//                        case LocationSettingsStatusCodes.SUCCESS:
//                            // All location settings are satisfied. The client can
//                            // initialize location
//                            // requests here.
//                            Log.i("Exit", "3");
//                            zoomToUserLocation();
//                            //info.setText("Done");
//
//                            break;
//                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
//                            // Location settings are not satisfied. But could be
//                            // fixed by showing the user
//                            // a dialog.
//                            Log.i("Exit", "4");
//                            try {
//                                // Show the dialog by calling
//                                // startResolutionForResult(),
//                                // and check the result in onActivityResult().
//                                status.startResolutionForResult(AttendanceGive.this, 1000);
//                            } catch (IntentSender.SendIntentException e) {
//                                // Ignore the error.
//                            }
//                            break;
//                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
//                            // Location settings are not satisfied. However, we have
//                            // no way to fix the
//                            // settings so we won't show the dialog.
//                            Log.i("Exit", "5");
//                            break;
//                    }
//                }
//
//            });
//
//
//        }
//    }

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
                    AlertDialog dialog = new AlertDialog.Builder(AttendanceGive.this)
                            .setMessage("Could not get address of the location due to internet disruption. Please try again")
                            .setPositiveButton("Retry", null)
                            .setNegativeButton("Cancel",null)
                            .show();

                    dialog.setCancelable(false);
                    dialog.setCanceledOnTouchOutside(false);
                    Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    positive.setOnClickListener(v -> {

                        checkAddress();
                        dialog.dismiss();
                    });
                    Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                    negative.setOnClickListener(v -> dialog.dismiss());
                }
            });
        }).start();
    }

//    public boolean isConnected() {
//        boolean connected = false;
//        boolean isMobile = false;
//        try {
//            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
//            @SuppressLint("MissingPermission") NetworkInfo nInfo = cm.getActiveNetworkInfo();
//            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
//            return connected;
//        } catch (Exception e) {
//            Log.e("Connectivity Exception", e.getMessage());
//        }
//        return connected;
//    }
//
//    public boolean isOnline() {
//
//        Runtime runtime = Runtime.getRuntime();
//        try {
//            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
//            int     exitValue = ipProcess.waitFor();
//            return (exitValue == 0);
//        }
//        catch (IOException | InterruptedException e)          { logger.log(Level.WARNING,e.getMessage(),e); }
//
//        return false;
//    }
//
//    public class CheckAddress extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            waitProgress.show(getSupportFragmentManager(),"WaitBar");
//            waitProgress.setCancelable(false);
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            if (isConnected() && isOnline()) {
//                conn = true;
//                getAddress(Double.parseDouble(lat),Double.parseDouble(lon));
//            }
//            else {
//                conn = false;
//                message = "Not Connected";
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//
//            if (conn) {
//                conn = false;
//
////                new Check().execute();
//                giveAttendance();
//            }
//            else {
//                waitProgress.dismiss();
//                AlertDialog dialog = new AlertDialog.Builder(AttendanceGive.this)
//                        .setMessage("Please Check Your Internet Connection")
//                        .setPositiveButton("Retry", null)
//                        .setNegativeButton("Cancel",null)
//                        .show();
//
//                dialog.setCancelable(false);
//                dialog.setCanceledOnTouchOutside(false);
//                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
//                positive.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        new CheckAddress().execute();
//                        dialog.dismiss();
//                    }
//                });
//                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
//                negative.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//
//                    }
//                });
//            }
//
//        }
//    }

//    public class Check extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            if (isConnected() && isOnline()) {
//
//                AttendanceClicked();
//                if (connected) {
//                    conn = true;
//                    message= "Internet Connected";
//                }
//
//            } else {
//                conn = false;
//                message = "Not Connected";
//            }
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//
//            waitProgress.dismiss();
//            if (conn) {
//                System.out.println("Ekhane Ashbe 3rd");
//                checkInTime.setVisibility(View.VISIBLE);
//
//                String ss = "Your last recorded time : "+timeToShow;
//                checkInTime.setText(ss);
//
//                SharedPreferences.Editor editor = preferences.edit();
//                editor.remove(timeKey);
//                editor.putString(timeKey,ss);
//                editor.apply();
//                editor.commit();
//                String puncher = "";
//                if (address.isEmpty()) {
//
//                    address = "No Address found for ("+lat+", "+lon+")";
//                    puncher = "Punched at "+ timeToShow + " in ("+address+")";
//                }
//                else {
//                    puncher = "Punched at "+ timeToShow + " in "+address;
//                }
//                currLoc.setText(puncher);
//                currLoc.setVisibility(View.VISIBLE);
//
//                if (tracking_flag == 1) {
//                    if (isMyServiceRunning(Service.class)) {
//                        System.out.println("Service Stopped");
//
//                        stopService();
//                        nameOfCheckIN.setText("PUNCH & START TRACKER");
//                    } else {
//                        System.out.println("Service Started");
//
//                        startService();
//                        nameOfCheckIN.setText("PUNCH & STOP TRACKER");
//                    }
//                }
//
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(AttendanceGive.this);
//                builder
//                        .setMessage("Your Attendance is Recorded!")
//                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//
//                            }
//                        });
//
//                AlertDialog alert = builder.create();
//                alert.show();
//
//                conn = false;
//                connected = false;
//
//
//            }
//            else {
//                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
//                AlertDialog dialog = new AlertDialog.Builder(AttendanceGive.this)
//                        .setMessage("Please Check Your Internet Connection")
//                        .setPositiveButton("Retry", null)
//                        .setNegativeButton("Cancel",null)
//                        .show();
//
//                dialog.setCancelable(false);
//                dialog.setCanceledOnTouchOutside(false);
//                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
//                positive.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        new CheckAddress().execute();
//                        dialog.dismiss();
//                    }
//                });
//                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
//                negative.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//
//                    }
//                });
//            }
//        }
//    }

//    public class LocationGet extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            waitProgress.show(getSupportFragmentManager(),"WaitBar");
//            waitProgress.setCancelable(false);
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            if (isConnected() && isOnline()) {
//                GettingOfficeLocation();
//                if (connected) {
//                    conn = true;
//                    message= "Internet Connected";
//                }
//
//            } else {
//                conn = false;
//                message = "Not Connected";
//            }
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//
//            waitProgress.dismiss();
//            if (conn) {
////                LatLng c_latLng = new LatLng(0,0);
////                if (officeLatitude != null && officeLongitude != null) {
////                    if (!officeLatitude.isEmpty() && !officeLongitude.isEmpty()) {
////                        c_latLng = new LatLng(Double.parseDouble(officeLatitude),Double.parseDouble(officeLongitude));
////                    }
////                }
////                MarkerOptions markerOptions = new MarkerOptions();
////                markerOptions.position(c_latLng);
////
////                mMap.addMarker(markerOptions);
////
////                CircleOptions circleOptions = new CircleOptions();
////                circleOptions.center(c_latLng);
////                circleOptions.strokeWidth(4);
////                circleOptions.strokeColor(Color.parseColor("#d95206"));
////                circleOptions.fillColor(Color.argb(30,242,165,21));
////                circleOptions.radius(40);
////                mMap.addCircle(circleOptions);
//                enableGPS();
//
//                conn = false;
//                connected = false;
//
//
//            }
//            else {
//                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
//                AlertDialog dialog = new AlertDialog.Builder(AttendanceGive.this)
//                        .setMessage("Please Check Your Internet Connection")
//                        .setPositiveButton("Retry", null)
//                        .setNegativeButton("Cancel",null)
//                        .show();
//
//                dialog.setCancelable(false);
//                dialog.setCanceledOnTouchOutside(false);
//                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
//                positive.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        new LocationGet().execute();
//                        dialog.dismiss();
//                    }
//                });
//                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
//                negative.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                        finish();
//                    }
//                });
//            }
//        }
//    }

//    public void AttendanceClicked() {
//        try {
//            this.connection = createConnection();
//            conn = false;
//            connected = false;
//
//            Statement stmt = connection.createStatement();
//
//            CallableStatement callableStatement = connection.prepareCall("begin SET_EMP_ATTENDANCE(?,?,?,?,?,?); end;");
//            callableStatement.setInt(1, Integer.parseInt(emp_id));
//            callableStatement.setTimestamp(2, ts);
//            callableStatement.setString(3,machineCode);
//            callableStatement.setString(4,lat);
//            callableStatement.setString(5,lon);
//            callableStatement.setString(6,address);
//
//            callableStatement.execute();
//
//            callableStatement.close();
//
//            connected = true;
//            connection.close();
//
//        }
//        catch (Exception e) {
//
//            //   Toast.makeText(MainActivity.this, ""+e,Toast.LENGTH_LONG).show();
//            Log.i("ERRRRR", e.getLocalizedMessage());
//            logger.log(Level.WARNING,e.getMessage(),e);
//        }
//    }

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
                    connected = false;
                }
                updateLayout();
            }
            catch (JSONException e) {
                logger.log(Level.WARNING,e.getMessage(),e);
                connected = false;
                updateLayout();
            }
        }, error -> {
            logger.log(Level.WARNING,error.getMessage(),error);
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
                checkInTime.setVisibility(View.VISIBLE);

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
                currLoc.setVisibility(View.VISIBLE);

                if (tracking_flag == 1) {
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


                AlertDialog.Builder builder = new AlertDialog.Builder(AttendanceGive.this);
                builder
                        .setMessage("Your Attendance is Recorded!")
                        .setPositiveButton("OK", (dialog, which) -> dialog.dismiss());

                AlertDialog alert = builder.create();
                alert.show();

                conn = false;
                connected = false;
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(AttendanceGive.this)
                        .setMessage("There is a network issue in the server. Please Try later.")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    checkAddress();
                    dialog.dismiss();
                });
                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negative.setOnClickListener(v -> dialog.dismiss());
            }
        }
        else {
            AlertDialog dialog = new AlertDialog.Builder(AttendanceGive.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel",null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                checkAddress();
                dialog.dismiss();
            });
            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> dialog.dismiss());
        }
    }

//    public void GettingOfficeLocation() {
//        try {
//            this.connection = createConnection();
//            connected = false;
//
//            Statement stmt = connection.createStatement();
//
//            ResultSet resultSet = stmt.executeQuery("Select COA_LATITUDE, COA_LONGITUDE, COA_COVERAGE \n" +
//                    "FROM\n" +
//                    "COMPANY_OFFICE_ADDRESS, EMP_JOB_HISTORY \n" +
//                    "WHERE \n" +
//                    "COMPANY_OFFICE_ADDRESS.COA_ID = EMP_JOB_HISTORY.JOB_PRI_COA_ID\n" +
//                    "AND EMP_JOB_HISTORY.JOB_EMP_ID = "+emp_id+"");
//
//            while (resultSet.next()) {
//                officeLatitude = resultSet.getString(1);
//                officeLongitude = resultSet.getString(2);
//                coverage = resultSet.getString(3);
//            }
//            resultSet.close();
//
//            connected = true;
//            connection.close();
//
//        }
//        catch (Exception e) {
//
//            //   Toast.makeText(MainActivity.this, ""+e,Toast.LENGTH_LONG).show();
//            Log.i("ERRRRR", e.getLocalizedMessage());
//            logger.log(Level.WARNING,e.getMessage(),e);
//        }
//    }

    public void getOfficeLocation() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;

        areaLists = new ArrayList<>();

        String offLocationUrl = api_url_front + "attendance/getNewOffLatLong?emp_id="+emp_id;

        RequestQueue requestQueue = Volley.newRequestQueue(AttendanceGive.this);

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
                logger.log(Level.WARNING,e.getMessage(),e);
                connected = false;
                updateInfo();
            }
        }, error -> {
            logger.log(Level.WARNING,error.getMessage(),error);
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
                enableGPS();

                conn = false;
                connected = false;
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(AttendanceGive.this)
                        .setMessage("There is a network issue in the server. Please Try later.")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    getOfficeLocation();
                    dialog.dismiss();
                });
                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negative.setOnClickListener(v -> {
                    dialog.dismiss();
                    finish();
                });
            }
        }
        else {
            AlertDialog dialog = new AlertDialog.Builder(AttendanceGive.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel",null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                getOfficeLocation();
                dialog.dismiss();
            });
            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> {
                dialog.dismiss();
                finish();
            });
        }
    }
}