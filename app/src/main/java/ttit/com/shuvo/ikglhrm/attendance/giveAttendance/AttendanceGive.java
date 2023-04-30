package ttit.com.shuvo.ikglhrm.attendance.giveAttendance;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ttit.com.shuvo.ikglhrm.R;
import ttit.com.shuvo.ikglhrm.WaitProgress;
import ttit.com.shuvo.ikglhrm.attendance.report.AttendanceReport;
import ttit.com.shuvo.ikglhrm.attendance.trackService.GPXFileWriter;
import ttit.com.shuvo.ikglhrm.attendance.trackService.Service;
import ttit.com.shuvo.ikglhrm.leaveAll.leaveApprove.LeaveApprove;

import static ttit.com.shuvo.ikglhrm.Login.SoftwareName;
import static ttit.com.shuvo.ikglhrm.Login.userInfoLists;
import static ttit.com.shuvo.ikglhrm.OracleConnection.createConnection;
import static ttit.com.shuvo.ikglhrm.attendance.Attendance.live_tracking_flag;
import static ttit.com.shuvo.ikglhrm.attendance.Attendance.tracking_flag;
import static ttit.com.shuvo.ikglhrm.attendance.trackService.Service.length_multi;
import static ttit.com.shuvo.ikglhrm.attendance.trackService.Service.locationLists;
import static ttit.com.shuvo.ikglhrm.attendance.trackService.Service.trk;

public class AttendanceGive extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    TextView currLoc;
    TextView checkInTime;
    CardView chekInButton;
    TextView nameOfCheckIN;
    Button close;
    private GoogleApiClient googleApiClient;
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
    private String message = null;
    private Boolean conn = false;
    private Boolean connected = false;
    private Connection connection;
    Timestamp ts;

    TextView software;
    ImageView autoStartIcon;

    ActivityResultLauncher<Intent> someActivityResultLauncher;
    String officeLatitude = "";
    String officeLongitude = "";
    String coverage = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_give);
        currLoc = findViewById(R.id.text_of_cu_loc);
        checkInTime = findViewById(R.id.check_int_time);
        chekInButton = findViewById(R.id.check_in_time_button);
        nameOfCheckIN = findViewById(R.id.name_of_punch);
        close = findViewById(R.id.give_att_finish);
        software = findViewById(R.id.name_of_company_attendance_give);
        autoStartIcon = findViewById(R.id.app_auto_start_icon);

        software.setText(SoftwareName);

        if (tracking_flag == 1) {

            if (isMyServiceRunning(Service.class)) {
                nameOfCheckIN.setText("PUNCH & STOP TRACKER");
            } else {
                nameOfCheckIN.setText("PUNCH & START TRACKER");
            }

        } else {
            nameOfCheckIN.setText("PUNCH");
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        locationRequest.setInterval(5);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(1000);

        emp_id = userInfoLists.get(0).getEmp_id();

        preferences = getSharedPreferences(emp_id,MODE_PRIVATE);

        getTime = preferences.getString(timeKey,null);
        if (getTime != null) {
            checkInTime.setText(getTime);
        }

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();
                            System.out.println("EKHANE ASHE CHECK: " + data.toString());
                        }
                    }
                });

        autoStartIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionsAll();
            }
        });

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
                .setPositiveButton("Check", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

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
                    }
                })
                .setNegativeButton("Don't Check", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        final LatLng[] lastLatLongitude = {new LatLng(0, 0)};


        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy, hh:mm:ss aa", Locale.getDefault());

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
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
                    System.out.println(ts.toString());
                    inTime = df.format(c);
                    System.out.println("IN TIME : " + inTime);
                    //getAddress(lastLatLongitude[0].latitude,lastLatLongitude[0].longitude);

                }
            }
        };

        chekInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                        if (distance[0] <= radius || radius == 0) {
                            if (tracking_flag == 1 ) {
                                if (isMyServiceRunning(Service.class)) {

                                    AlertDialog.Builder builder = new AlertDialog.Builder(AttendanceGive.this);
                                    builder.setTitle("Attendance!")
                                            .setMessage("Do you want to punch & stop your tracker?")
                                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {


                                                    new CheckAddress().execute();

                                                }
                                            })
                                            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                }
                                            });
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(AttendanceGive.this);
                                    builder.setTitle("Attendance!")
                                            .setMessage("Do you want to punch & start your tracker?")
                                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {


                                                    new CheckAddress().execute();

                                                }
                                            })
                                            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                }
                                            });
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                }
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(AttendanceGive.this);
                                builder.setTitle("Punch Attendance!")
                                        .setMessage("Do you want to punch now?")
                                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {


                                                new CheckAddress().execute();

                                            }
                                        })
                                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"You are not around your office area",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        if (tracking_flag == 1) {
                            if (isMyServiceRunning(Service.class)) {

                                AlertDialog.Builder builder = new AlertDialog.Builder(AttendanceGive.this);
                                builder.setTitle("Attendance!")
                                        .setMessage("Do you want to punch & stop your tracker?")
                                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {


                                                new CheckAddress().execute();

                                            }
                                        })
                                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(AttendanceGive.this);
                                builder.setTitle("Attendance!")
                                        .setMessage("Do you want to punch & start your tracker?")
                                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {


                                                new CheckAddress().execute();

                                            }
                                        })
                                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(AttendanceGive.this);
                            builder.setTitle("Punch Attendance!")
                                    .setMessage("Do you want to punch now?")
                                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {


                                            new CheckAddress().execute();

                                        }
                                    })
                                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    }

                } else {
                    Toast.makeText(getApplicationContext(),"Please wait for getting the location",Toast.LENGTH_SHORT).show();
                }
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fusedLocationProviderClient.removeLocationUpdates(locationCallback);
                finish();
            }
        });

        new LocationGet().execute();
    }

    @Override
    public void onBackPressed() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        finish();
    }


    public void getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(AttendanceGive.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            if (Geocoder.isPresent()) {
                Address obj = addresses.get(0);
                String adds = obj.getAddressLine(0);
//                String add = "Address from GeoCODE: ";
//                add = add + "\n" + obj.getCountryName();
//                add = add + "\n" + obj.getCountryCode();
//                add = add + "\n" + obj.getAdminArea();
//                add = add + "\n" + obj.getPostalCode();
//                add = add + "\n" + obj.getSubAdminArea();
//                add = add + "\n" + obj.getLocality();
//                add = add + "\n" + obj.getSubThoroughfare();
//                add = add + "\n" + obj.getFeatureName();
//                add = add + "\n" + obj.getPhone();
//                add = add + "\n" + obj.getPremises();
//                add = add + "\n" + obj.getSubLocality();
//                add = add + "\n" + obj.getThoroughfare();
//                add = add + "\n" + obj.getUrl();

//                Log.v("IGA", "Address: " + add);
//                Log.v("NEW ADD", "Address: " + adds);
                address = adds;
                System.out.println("Ekhane ashbe 1st");
                // Toast.makeText(this, "Address=>" + add,
                // Toast.LENGTH_SHORT).show();

            } else {
                address = "";
            }

            // TennisAppActivity.showDialog(add);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            address = "";
            //Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("MissingPermission")
    public void zoomToUserLocation(){

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            Log.i("Ekhane", "1");
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
//                Log.i("lattt", location.toString());
                LatLng latLng = new LatLng(23.6850, 90.3563);


                if (location != null) {
                    latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                } else {
                    latLng = new LatLng(23.6850, 90.3563);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 7));
                }

            }
        });

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());



    }
    private void enableGPS() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API).addConnectionCallbacks(AttendanceGive.this)
                    .addOnConnectionFailedListener(AttendanceGive.this).build();
            googleApiClient.connect();
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(5 * 1000);
            locationRequest.setFastestInterval(1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            // **************************
            builder.setAlwaysShow(true); // this is the key ingredient
            // **************************

            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi
                    .checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    final LocationSettingsStates state = result
                            .getLocationSettingsStates();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            // All location settings are satisfied. The client can
                            // initialize location
                            // requests here.
                            Log.i("Exit", "3");
                            zoomToUserLocation();
                            //info.setText("Done");

                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be
                            // fixed by showing the user
                            // a dialog.
                            Log.i("Exit", "4");
                            try {
                                // Show the dialog by calling
                                // startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(AttendanceGive.this, 1000);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have
                            // no way to fix the
                            // settings so we won't show the dialog.
                            Log.i("Exit", "5");
                            break;
                    }
                }

            });


        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
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
                String result = data.getStringExtra("result");
                //info.setText("Done");
                zoomToUserLocation();
                Log.i("Hoise ", "1");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
                Log.i("Hoise ", "2");
                finish();
//                System.exit(0);
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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
            waitProgress.show(getSupportFragmentManager(),"WaitBar");
            waitProgress.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isConnected() && isOnline()) {
                conn = true;
                getAddress(Double.parseDouble(lat),Double.parseDouble(lon));
            }
            else {
                conn = false;
                message = "Not Connected";
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (conn) {
                conn = false;
                if (address.isEmpty()) {

                    address = "No Address found for ("+lat+", "+lon+")";
                }
                System.out.println("Ekhane Ashbe 2nd");
                currLoc.setText(address);

                new Check().execute();
            } else {
                waitProgress.dismiss();
                AlertDialog dialog = new AlertDialog.Builder(AttendanceGive.this)
                        .setMessage("Please Check Your Internet Connection")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new CheckAddress().execute();
                        dialog.dismiss();
                    }
                });
                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                    }
                });
            }

        }
    }

    public class Check extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isConnected() && isOnline()) {

                AttendanceClicked();
                if (connected) {
                    conn = true;
                    message= "Internet Connected";
                }

            } else {
                conn = false;
                message = "Not Connected";
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            waitProgress.dismiss();
            if (conn) {
                System.out.println("Ekhane Ashbe 3rd");
                checkInTime.setVisibility(View.VISIBLE);

                String ss = "Your last recorded time: "+inTime;
                checkInTime.setText(ss);

                SharedPreferences.Editor editor = preferences.edit();
                editor.remove(timeKey);
                editor.putString(timeKey,ss);
                editor.apply();
                editor.commit();

                if (tracking_flag == 1) {
                    if (isMyServiceRunning(Service.class)) {
                        System.out.println("Service Stopped");

                        stopService();
                        nameOfCheckIN.setText("PUNCH & START TRACKER");
                    } else {
                        System.out.println("Service Started");

                        startService();
                        nameOfCheckIN.setText("PUNCH & STOP TRACKER");
                    }
                }


                AlertDialog.Builder builder = new AlertDialog.Builder(AttendanceGive.this);
                builder
                        .setMessage("Your Attendance is Recorded!")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();

                conn = false;
                connected = false;


            }else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(AttendanceGive.this)
                        .setMessage("Please Check Your Internet Connection")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new CheckAddress().execute();
                        dialog.dismiss();
                    }
                });
                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                    }
                });
            }
        }
    }

    public class LocationGet extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            waitProgress.show(getSupportFragmentManager(),"WaitBar");
            waitProgress.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isConnected() && isOnline()) {
                GettingOfficeLocation();
                if (connected) {
                    conn = true;
                    message= "Internet Connected";
                }

            } else {
                conn = false;
                message = "Not Connected";
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            waitProgress.dismiss();
            if (conn) {
//                LatLng c_latLng = new LatLng(0,0);
//                if (officeLatitude != null && officeLongitude != null) {
//                    if (!officeLatitude.isEmpty() && !officeLongitude.isEmpty()) {
//                        c_latLng = new LatLng(Double.parseDouble(officeLatitude),Double.parseDouble(officeLongitude));
//                    }
//                }
//                MarkerOptions markerOptions = new MarkerOptions();
//                markerOptions.position(c_latLng);
//
//                mMap.addMarker(markerOptions);
//
//                CircleOptions circleOptions = new CircleOptions();
//                circleOptions.center(c_latLng);
//                circleOptions.strokeWidth(4);
//                circleOptions.strokeColor(Color.parseColor("#d95206"));
//                circleOptions.fillColor(Color.argb(30,242,165,21));
//                circleOptions.radius(40);
//                mMap.addCircle(circleOptions);
                enableGPS();

                conn = false;
                connected = false;


            }else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(AttendanceGive.this)
                        .setMessage("Please Check Your Internet Connection")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new LocationGet().execute();
                        dialog.dismiss();
                    }
                });
                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        finish();
                    }
                });
            }
        }
    }

    public void AttendanceClicked() {
        try {
            this.connection = createConnection();
            conn = false;
            connected = false;

            Statement stmt = connection.createStatement();

            CallableStatement callableStatement = connection.prepareCall("begin SET_EMP_ATTENDANCE(?,?,?,?,?,?); end;");
            callableStatement.setInt(1, Integer.parseInt(emp_id));
            callableStatement.setTimestamp(2, ts);
            callableStatement.setString(3,"3");
            callableStatement.setString(4,lat);
            callableStatement.setString(5,lon);
            callableStatement.setString(6,address);

            callableStatement.execute();

            callableStatement.close();

            connected = true;
            connection.close();

        }
        catch (Exception e) {

            //   Toast.makeText(MainActivity.this, ""+e,Toast.LENGTH_LONG).show();
            Log.i("ERRRRR", e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    public void GettingOfficeLocation() {
        try {
            this.connection = createConnection();
            connected = false;

            Statement stmt = connection.createStatement();

            ResultSet resultSet = stmt.executeQuery("Select COA_LATITUDE, COA_LONGITUDE, COA_COVERAGE \n" +
                    "FROM\n" +
                    "COMPANY_OFFICE_ADDRESS, EMP_JOB_HISTORY \n" +
                    "WHERE \n" +
                    "COMPANY_OFFICE_ADDRESS.COA_ID = EMP_JOB_HISTORY.JOB_PRI_COA_ID\n" +
                    "AND EMP_JOB_HISTORY.JOB_EMP_ID = "+emp_id+"");

            while (resultSet.next()) {
                officeLatitude = resultSet.getString(1);
                officeLongitude = resultSet.getString(2);
                coverage = resultSet.getString(3);
            }
            resultSet.close();

            connected = true;
            connection.close();

        }
        catch (Exception e) {

            //   Toast.makeText(MainActivity.this, ""+e,Toast.LENGTH_LONG).show();
            Log.i("ERRRRR", e.getLocalizedMessage());
            e.printStackTrace();
        }
    }
}