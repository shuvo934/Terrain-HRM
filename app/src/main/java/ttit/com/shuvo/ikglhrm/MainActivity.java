package ttit.com.shuvo.ikglhrm;

import static com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE;

import static ttit.com.shuvo.ikglhrm.utilities.Constants.LOGIN_ACTIVITY_FILE;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.LOGIN_TF;

import androidx.activity.EdgeToEdge;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.appupdate.AppUpdateOptions;
import com.google.android.play.core.install.model.UpdateAvailability;

import ttit.com.shuvo.ikglhrm.dashboard.Dashboard;

public class MainActivity extends AppCompatActivity {

    private final Handler mHandler = new Handler();
//    private final int MY_IGNORE_OPTIMIZATION_REQUEST = 101010;

    SharedPreferences sharedPreferences;
    boolean loginfile = false;

//    PowerManager pm = null;
    AppUpdateManager appUpdateManager;

    boolean perm = false;

    ActivityResultLauncher<IntentSenderRequest> activityResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(),
                    result -> {
                        if (result.getResultCode() != RESULT_OK) {

                            MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(MainActivity.this)
                                    .setTitle("Update Failed!")
                                    .setMessage("Failed to update the app. Please try again.")
                                    .setIcon(R.drawable.hrm_new_round_icon_custom)
                                    .setPositiveButton("Retry", (dialog, which) -> getAppUpdate())
                                    .setNegativeButton("Cancel", (dialog, which) -> finish());
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                        }
                    });

    private ActivityResultLauncher<String> cameraPermResultLauncher;

    private final ActivityResultLauncher<String> notifyPermResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
        if (result) {
            System.out.println("HOLA9");
            goToActivityMap();
        }
        else {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.POST_NOTIFICATIONS)) {
                showDialog("Notification Permission!", "This app needs the Notification permission to function. Please Allow that permission from settings.", "Go to Settings", (dialogInterface, i) -> {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:"+ getPackageName()));
                    startActivity(intent);
                    perm = true;
                });
            }
            else {
                System.out.println("HOLA10");
                enableNotification();
            }
        }
    });

    private final ActivityResultLauncher<String[]> locationResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
        System.out.println("OnActivityResult: " +result);
        boolean allGranted = true;
        for (String key: result.keySet()) {
            allGranted = allGranted && Boolean.TRUE.equals(result.get(key));
        }
        if (allGranted) {
            System.out.println("HOLA1");
//            enableBackgroundLocation();
            enableCameraPermission();
        }
        else {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                showDialog("Location Permission!", "This app needs the precise location permission to function. Please Allow that permission from settings.", "Go to Settings", (dialogInterface, i) -> {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:"+ getPackageName()));
                    startActivity(intent);
                    perm = true;
                });
            }
            else {
                System.out.println("HOLA2");
                enableLocation();
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        appUpdateManager = AppUpdateManagerFactory.create(MainActivity.this);

        sharedPreferences = getSharedPreferences(LOGIN_ACTIVITY_FILE, MODE_PRIVATE);
        loginfile = sharedPreferences.getBoolean(LOGIN_TF,false);

        System.out.println(loginfile);
//        pm = (PowerManager)getSystemService(Context.POWER_SERVICE);

        cameraPermResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
            System.out.println("OnActivityResult: " +result);
            if (result) {
                System.out.println("HOLA5");
                enableNotification();
            }
            else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                    showDialog("Camera Permission!", "This app needs the Camera permission to update user image. Please Allow that permission from settings.", "Go to Settings", (dialogInterface, i) -> {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:"+ getPackageName()));
                        startActivity(intent);
                        perm = true;
                    });
                }
                else {
                    System.out.println("HOLA6");
                    enableCameraPermission();
                }
            }
        });

        perm = false;
        getAppUpdate();

    }

    private void getAppUpdate() {
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(IMMEDIATE))  {

                appUpdateManager.startUpdateFlowForResult(appUpdateInfo,
                        activityResultLauncher, AppUpdateOptions
                                .newBuilder(IMMEDIATE)
                                .build());
            }
            else {
                System.out.println("No update available");
                enableLocation();
            }
        });
        appUpdateInfoTask.addOnFailureListener(e -> {
            System.out.println("FAILED TO LISTEN");
            enableLocation();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        appUpdateManager
                .getAppUpdateInfo()
                .addOnSuccessListener(
                        appUpdateInfo -> {
                            if (appUpdateInfo.updateAvailability()
                                    == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                                // If an in-app update is already running, resume the update.
                                appUpdateManager.startUpdateFlowForResult(appUpdateInfo,
                                        activityResultLauncher,AppUpdateOptions
                                                .newBuilder(IMMEDIATE)
                                                .build());
                            }
                        });
        if (perm) {
            perm = false;
            enableLocation();
        }
    }

    private void goToActivityMap() {
        mHandler.postDelayed(() -> {
            Intent intent;
            if (loginfile) {
                intent = new Intent(MainActivity.this, Dashboard.class);
            } else {
                intent = new Intent(MainActivity.this, Login.class);
            }
            startActivity(intent);
            finish();


        }, 1500);
    }

    public void showDialog(String title, String message, String positiveButtonTitle, DialogInterface.OnClickListener positiveListener) {
        MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(this);
        alertDialogBuilder.setIcon(R.drawable.hrm_new_round_icon_custom)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveButtonTitle, positiveListener);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

//    private void enableFileAccess() {
//        if (Build.VERSION.SDK_INT < 33) {
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
//                    == PackageManager.PERMISSION_GRANTED &&
//                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                            == PackageManager.PERMISSION_GRANTED) {
//
//                Log.i("Ekhane", "1");
//                enableLocation();
//            }
//            else {
//                Log.i("Ekhane", "2");
//                if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
//                        || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//
//                    Log.i("Ekhane", "3");
//                    showDialog("Storage Permission!", "This app needs the storage permission for functioning.", "OK", (dialogInterface, i) -> storageResultLauncher.launch(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
//                            Manifest.permission.WRITE_EXTERNAL_STORAGE}));
//                }
//                else {
//                    Log.i("Ekhane", "4");
//                    storageResultLauncher.launch(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE,
//                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE});
//                }
//            }
//        }
//        else {
//            enableLocation();
//        }
//
//    }
//
//    private final ActivityResultLauncher<String[]> storageResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
//                System.out.println("OnActivityResult: " +result);
//                boolean allGranted = true;
//                for (String key: result.keySet()) {
//                    allGranted = allGranted && Boolean.TRUE.equals(result.get(key));
//                }
//                if (allGranted) {
//                    System.out.println("HOLA1");
//                    enableLocation();
//                }
//                else {
//                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)
//                            || !ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                        showDialog("Storage Permission!", "This app needs the storage permission to function. Please Allow that permission from settings.", "Go to Settings", (dialogInterface, i) -> {
//                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:"+ getPackageName()));
//                            startActivity(intent);
//                            perm = true;
//                        });
//                    }
//                    else {
//                        System.out.println("HOLA2");
//                        enableFileAccess();
//                    }
//                }
//            });

    private void enableLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {

            Log.i("Ekhane", "1");

//            enableBackgroundLocation();
            enableCameraPermission();

        }
        else {
            Log.i("Ekhane", "2");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)) {

                Log.i("Ekhane", "3");
                showDialog("Location Permission!", "This app needs the location permission for functioning.", "OK", (dialogInterface, i) -> locationResultLauncher.launch(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION}));
            }
            else {
                Log.i("Ekhane", "4");
                locationResultLauncher.launch(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION});
            }
        }
    }

//    private void enableBackgroundLocation() {
//        if (Build.VERSION.SDK_INT >= 29) {
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
//                    == PackageManager.PERMISSION_GRANTED) {
//
//                Log.i("Ekhane", "9");
//
//                enableCameraPermission();
//            }
//            else {
//                Log.i("Ekhane", "10");
//                if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
//
//                    Log.i("Ekhane", "11");
//                    showDialog("Background Location Permission!", "This app needs the background location permission for functioning.", "OK", (dialogInterface, i) -> backLocationResultLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION));
//                }
//                else {
//                    Log.i("Ekhane", "12");
//                    backLocationResultLauncher.launch(android.Manifest.permission.ACCESS_BACKGROUND_LOCATION);
//                }
//            }
//        }
//        else {
//            enableCameraPermission();
//        }
//    }
//
//    private final ActivityResultLauncher<String> backLocationResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
//        if (result) {
//            System.out.println("HOLA3");
//            enableCameraPermission();
//        }
//        else {
//            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
//                showDialog("Background Location Permission!", "This app needs the background location permission to function. Please Allow that permission from settings.", "Go to Settings", (dialogInterface, i) -> {
//                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:"+ getPackageName()));
//                    startActivity(intent);
//                    perm = true;
//                });
//            }
//            else {
//                System.out.println("HOLA2");
//                enableBackgroundLocation();
//            }
//        }
//    });

    private void enableCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {

            Log.i("Ekhane", "13");
            enableNotification();
        }
        else {
            Log.i("Ekhane", "14");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                Log.i("Ekhane", "15");
                showDialog("Camera Permission!", "This app needs the Camera permission for functioning.", "OK", (dialogInterface, i) -> cameraPermResultLauncher.launch(Manifest.permission.CAMERA));
            }
            else {
                Log.i("Ekhane", "16");
                cameraPermResultLauncher.launch(Manifest.permission.CAMERA);
            }
        }
    }

    public void enableNotification() {
        if (Build.VERSION.SDK_INT >= 33) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    == PackageManager.PERMISSION_GRANTED) {

                Log.i("Ekhane", "17");
                goToActivityMap();
            } else {
                Log.i("Ekhane", "18");
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.POST_NOTIFICATIONS)) {
                    Log.i("Ekhane", "19");
                    showDialog("Notification Permission!", "This app needs the Notification permission for functioning.", "OK", (dialogInterface, i) -> notifyPermResultLauncher.launch(Manifest.permission.POST_NOTIFICATIONS));
                } else {
                    Log.i("Ekhane", "20");
                    notifyPermResultLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS);
                }
            }
        }
        else {
            goToActivityMap();
        }
    }

//    @RequiresApi(api = Build.VERSION_CODES.Q)
//    private void enableUserLocation() {
//
//        String[] permission = {
//                Manifest.permission.ACCESS_FINE_LOCATION,
//                Manifest.permission.ACCESS_COARSE_LOCATION,
//                Manifest.permission.ACCESS_BACKGROUND_LOCATION
//        };
//        for (String str : permission) {
//            if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
//                this.requestPermissions(permission, FINE_LOCATION_ACCESS_REQUEST_CODE);
//                return;
//            }
//        }
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
//                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
//                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            Log.i("Ekhane", "3");
//            enableFileAccess();
//        }
//
//        // Old Request Type
////        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
////            Log.i("Ekhane", "3");
////            enableFileAccess();
////
////        } else {
////            Log.i("Ekhane", "4");
////            // Ask For Permission
////            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
////                // we need to show user  alert dialog
////                Log.i("Ekhane", "5");
////                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE);
////            } else {
////                Log.i("Ekhane", "6");
////                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE);
////            }
////        }
//    }

//    private void enableFileAccess() {
//
//        if (Build.VERSION.SDK_INT >= 34) {
//            int REQUEST_CODE_PERMISSION_STORAGE = 100;
//            String[] permission = {
//                    Manifest.permission.READ_MEDIA_IMAGES,
//                    Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                    Manifest.permission.CALL_PHONE,
//                    Manifest.permission.POST_NOTIFICATIONS
//            };
//
//            for (String str : permission) {
//                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
//                    this.requestPermissions(permission, REQUEST_CODE_PERMISSION_STORAGE);
//                    return;
//                }
//            }
//
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED &&
//                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED) == PackageManager.PERMISSION_GRANTED &&
//                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
//                ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED &&
//                ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
//
//                goToActivityMap();
//            }
//        }
//        else if (Build.VERSION.SDK_INT < 33) {
//            int REQUEST_CODE_PERMISSION_STORAGE = 100;
//            String[] permission = {
//                    Manifest.permission.READ_EXTERNAL_STORAGE,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                    Manifest.permission.CALL_PHONE
////                    Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
//            };
//
//            for (String str : permission) {
//                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
//                    this.requestPermissions(permission, REQUEST_CODE_PERMISSION_STORAGE);
//                    return;
//                }
//
//            }
//
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
//                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
//                    ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED /*&&
//                    ContextCompat.checkSelfPermission(this, Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS) == PackageManager.PERMISSION_GRANTED*/) {
////                PermissionsAll();
////                DozeMode();
//                goToActivityMap();
//            }
////            if (ActivityCompat.checkSelfPermission(MainActivity.this,
////                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
////                return;
////            }
//
////            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
////                return;
////            }
//
//
//        }
//        else {
//            int REQUEST_CODE_PERMISSION_STORAGE = 100;
//            String[] permission = {
//                    Manifest.permission.READ_MEDIA_IMAGES,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                    Manifest.permission.CALL_PHONE,
//                    Manifest.permission.POST_NOTIFICATIONS
//            };
//
//            for (String str : permission) {
//                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
//                    this.requestPermissions(permission, REQUEST_CODE_PERMISSION_STORAGE);
//                    return;
//                }
//            }
//
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED &&
//                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
//                ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED &&
//                ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
//
//                goToActivityMap();
//            }
//        }
//    }





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

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == FINE_LOCATION_ACCESS_REQUEST_CODE) {
//            Log.i("Ekhane", "7");
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Log.i("Ekhane", "8");
//                // we have the permission
//
//                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                    // TODO: Consider calling
//                    //    ActivityCompat#requestPermissions
//                    // here to request the missing permissions, and then overriding
//                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                    //                                          int[] grantResults)
//                    // to handle the case where the user grants the permission. See the documentation
//                    // for ActivityCompat#requestPermissions for more details.
//                    Log.i("Ekhane", "9");
//                    return;
//                }
//
//                enableFileAccess();
//
//            }
//            else {
//                Log.i("Ekhane", "10");
//                //we do not have the permission
//                permissionMsg.setText("Please Give the Permission to Access Your Location for Using This App.");
////                quit.setVisibility(View.VISIBLE);
//
//            }
//        } else if (requestCode == 100) {
//            if (grantResults.length > 0 &&
//                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
//                        ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
//                        ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED /*&&
//                        ActivityCompat.checkSelfPermission(this, Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS) != PackageManager.PERMISSION_GRANTED*/) {
//                    // TODO: Consider calling
//                    //    ActivityCompat#requestPermissions
//                    // here to request the missing permissions, and then overriding
//                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                    //                                          int[] grantResults)
//                    // to handle the case where the user grants the permission. See the documentation
//                    // for ActivityCompat#requestPermissions for more details.
//                    Log.i("Ekhane", "9");
//                }
//
//
//
////                enableGPS();
//                // Permission is granted. Continue the action or workflow
////                     in your app.
////                PermissionsAll();
////                DozeMode();
//                goToActivityMap();
//            }
//                else {
//                permissionMsg.setText("Please Allow this Permission for Using This App");
//                Log.i("Aree", "Pala");
//                //quit.setVisibility(View.VISIBLE);
//                // Explain to the user that the feature is unavailable because
//                // the features requires a permission that the user has denied.
//                // At the same time, respect the user's decision. Don't link to
//                // system settings in an effort to convince the user to change
//                // their decision.
//            }
//
//        }
//
//    }
}