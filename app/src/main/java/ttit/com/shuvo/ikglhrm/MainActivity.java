package ttit.com.shuvo.ikglhrm;

//import androidx.activity.result.ActivityResult;
//import androidx.activity.result.ActivityResultCallback;
//import androidx.activity.result.ActivityResultLauncher;
//import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
//import android.app.Activity;
//import android.content.ComponentName;
//import android.content.Context;
//import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
//import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
//import android.os.PowerManager;
//import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import ttit.com.shuvo.ikglhrm.dashboard.Dashboard;

public class MainActivity extends AppCompatActivity {

    TextView permissionMsg;
    private final Handler mHandler = new Handler();
    private final int FINE_LOCATION_ACCESS_REQUEST_CODE = 10001;
//    private final int MY_IGNORE_OPTIMIZATION_REQUEST = 101010;

    SharedPreferences sharedPreferences;
    boolean loginfile = false;

    public static final String LOGIN_ACTIVITY_FILE = "LOGIN_ACTIVITY_FILE";
    public static final String LOGIN_TF = "TRUE_FALSE";

//    PowerManager pm = null;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }
    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(LOGIN_ACTIVITY_FILE, MODE_PRIVATE);
        loginfile = sharedPreferences.getBoolean(LOGIN_TF,false);
        permissionMsg = findViewById(R.id.permission_not_granted_msg);

        System.out.println(loginfile);
//        pm = (PowerManager)getSystemService(Context.POWER_SERVICE);


        enableUserLocation();

    }

    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    private void goToActivityMap() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (loginfile) {
                    Intent intent = new Intent(MainActivity.this, Dashboard.class);
                    startActivity(intent);
                    showSystemUI();
                    finish();
                } else {
                    Intent intent = new Intent(MainActivity.this, Login.class);
                    startActivity(intent);
                    showSystemUI();
                    finish();
                }


            }
        }, 2000);
    }

    private void enableUserLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.i("Ekhane", "3");
            enableFileAccess();

        } else {
            Log.i("Ekhane", "4");
            // Ask For Permission
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                // we need to show user  alert dialog
                Log.i("Ekhane", "5");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE);
            } else {
                Log.i("Ekhane", "6");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE);
            }
        }
    }

    private void enableFileAccess() {

        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_PERMISSION_STORAGE = 100;
            String[] permission = {
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CALL_PHONE,
//                    Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
            };

            for (String str : permission) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    this.requestPermissions(permission, REQUEST_CODE_PERMISSION_STORAGE);
                    return;
                }

            }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED /*&&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS) == PackageManager.PERMISSION_GRANTED*/) {
//                PermissionsAll();
//                DozeMode();
                goToActivityMap();
            }
//            if (ActivityCompat.checkSelfPermission(MainActivity.this,
//                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                return;
//            }

//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                return;
//            }


        }
    }



//    public void DozeMode() {
//        boolean isIgnoringBatteryOptimizations = false;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//            isIgnoringBatteryOptimizations = pm.isIgnoringBatteryOptimizations(getPackageName());
//            if(!isIgnoringBatteryOptimizations){
//                Intent intent = new Intent();
//                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
//                intent.setData(Uri.parse("package:" + getPackageName()));
//                startActivityForResult(intent, MY_IGNORE_OPTIMIZATION_REQUEST);
//            } else {
//                goToActivityMap();
//            }
//        }
//
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == MY_IGNORE_OPTIMIZATION_REQUEST) {
//
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//
//                boolean isIgnoringBatteryOptimizations = pm.isIgnoringBatteryOptimizations(getPackageName());
//                if(isIgnoringBatteryOptimizations){
//                    // Ignoring battery optimization
//
//                    goToActivityMap();
//
//                    System.out.println("IGNORED");
//                }else{
//                    // Not ignoring battery optimization
//
//                    System.out.println("NOT IGNORED");
//                    permissionMsg.setText("Please Allow this Permission for Using This App");
//                }
//            }
//
//
//        }
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FINE_LOCATION_ACCESS_REQUEST_CODE) {
            Log.i("Ekhane", "7");
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i("Ekhane", "8");
                // we have the permission

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    Log.i("Ekhane", "9");
                    return;
                }

                enableFileAccess();

            }
            else {
                Log.i("Ekhane", "10");
                //we do not have the permission
                permissionMsg.setText("Please Give the Permission to Access Your Location for Using This App.");
//                quit.setVisibility(View.VISIBLE);

            }
        } else if (requestCode == 100) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED /*&&
                        ActivityCompat.checkSelfPermission(this, Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS) != PackageManager.PERMISSION_GRANTED*/) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    Log.i("Ekhane", "9");
                }



//                enableGPS();
                // Permission is granted. Continue the action or workflow
//                     in your app.
//                PermissionsAll();
//                DozeMode();
                goToActivityMap();
            }
                else {
                permissionMsg.setText("Please Allow this Permission for Using This App");
                Log.i("Aree", "Pala");
                //quit.setVisibility(View.VISIBLE);
                // Explain to the user that the feature is unavailable because
                // the features requires a permission that the user has denied.
                // At the same time, respect the user's decision. Don't link to
                // system settings in an effort to convince the user to change
                // their decision.
            }

        }

    }
}