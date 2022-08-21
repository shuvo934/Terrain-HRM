package ttit.com.shuvo.ikglhrm.MainPage;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;

import ttit.com.shuvo.ikglhrm.EmployeeInfo.EmplyeeInformation;
import ttit.com.shuvo.ikglhrm.Login;
import ttit.com.shuvo.ikglhrm.R;
import ttit.com.shuvo.ikglhrm.attendance.Attendance;
import ttit.com.shuvo.ikglhrm.attendance.trackService.Service;
import ttit.com.shuvo.ikglhrm.dashboard.Dashboard;
import ttit.com.shuvo.ikglhrm.directoryBook.Directory;
import ttit.com.shuvo.ikglhrm.directoryBook.DirectoryWithDivision;
import ttit.com.shuvo.ikglhrm.leaveAll.Leave;
import ttit.com.shuvo.ikglhrm.payRoll.PayRollInfo;
import ttit.com.shuvo.ikglhrm.scheduler.Uploader;

import static ttit.com.shuvo.ikglhrm.Login.CompanyName;
import static ttit.com.shuvo.ikglhrm.Login.SoftwareName;
import static ttit.com.shuvo.ikglhrm.Login.isApproved;
import static ttit.com.shuvo.ikglhrm.Login.isLeaveApproved;
import static ttit.com.shuvo.ikglhrm.Login.userDesignations;
import static ttit.com.shuvo.ikglhrm.Login.userInfoLists;
import static ttit.com.shuvo.ikglhrm.dashboard.Dashboard.alarmManager;
import static ttit.com.shuvo.ikglhrm.dashboard.Dashboard.trackerAvailable;

public class MainMenu extends AppCompatActivity {

    TextView userName;
    TextView designation;
    TextView department;
    TextView comp;

    CardView personalInfo;
    CardView attendance;
    CardView leave;
    CardView payRoll;
    CardView directory;

    //Button logout;
    TextView sofName;
    ImageView dashB;
    ImageView logoutImage;

    SharedPreferences sharedPreferences;
    SharedPreferences sharedSchedule;

    public static final String SCHEDULING_FILE = "SCHEDULING FILE";
    public static final String SCHEDULING_EMP_ID = "SCHEDULING EMP ID";
    public static final String TRIGGERING = "TRIGGER TRUE FALSE";

    public static final String LOGIN_ACTIVITY_FILE = "LOGIN_ACTIVITY_FILE";

    public static final String USER_NAME = "USER_NAME";
    public static final String USER_F_NAME = "USER_F_NAME";
    public static final String USER_L_NAME = "USER_L_NAME";
    public static final String EMAIL = "EMAIL";
    public static final String CONTACT = "CONTACT";
    public static final String EMP_ID_LOGIN = "EMP_ID";

    public static final String JSM_CODE = "JSM_CODE";
    public static final String JSM_NAME = "JSM_NAME";
    public static final String JSD_ID_LOGIN = "JSD_ID";
    public static final String JSD_OBJECTIVE = "JSD_OBJECTIVE";
    public static final String DEPT_NAME = "DEPT_NAME";
    public static final String DIV_NAME = "DIV_NAME";
    public static final String DESG_NAME = "DESG_NAME";
    public static final String DESG_PRIORITY = "DESG_PRIORITY";
    public static final String JOINING_DATE = "JOINING_DATE";
    public static final String DIV_ID = "DIV_ID";
    public static final String LOGIN_TF = "TRUE_FALSE";

    public static final String IS_ATT_APPROVED = "IS_ATT_APPROVED";
    public static final String IS_LEAVE_APPROVED = "IS_LEAVE_APPROVED";
    public static final String COMPANY = "COMPANY";
    public static final String SOFTWARE = "SOFTWARE";
    public static final String LIVE_FLAG = "LIVE_FLAG";

//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        if (hasFocus) {
//            hideSystemUI();
//        }
//    }
//    private void hideSystemUI() {
//        View decorView = getWindow().getDecorView();
//        decorView.setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_IMMERSIVE
//                        // Set the content to appear under the system bars so that the
//                        // content doesn't resize when the system bars hide and show.
//                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                        // Hide the nav bar and status bar
//                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //getWindow().setNavigationBarColor(Color.parseColor("#f0932b"));
//        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window w = getWindow();
//            //w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        }
//        if (Build.VERSION.SDK_INT < 16) {
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        }
//        View decorView = getWindow().getDecorView();
//// Hide the status bar.
//        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
//        decorView.setSystemUiVisibility(uiOptions);


        setContentView(R.layout.activity_main_menu);

        sharedPreferences = getSharedPreferences(LOGIN_ACTIVITY_FILE, MODE_PRIVATE);
        sharedSchedule = getSharedPreferences(SCHEDULING_FILE, MODE_PRIVATE);

        sofName = findViewById(R.id.thrm_app_name_main_menu);
        dashB = findViewById(R.id.dashboard_icon);
        logoutImage = findViewById(R.id.log_out_icon_main_menu);
        comp = findViewById(R.id.company_name_main_menu);


        comp.setText(CompanyName);
        sofName.setText(SoftwareName);

        userName = findViewById(R.id.user_name);
        department = findViewById(R.id.user_depart);
        designation = findViewById(R.id.user_desg);

        personalInfo = findViewById(R.id.personal_info);
        attendance = findViewById(R.id.attendanceee);
        leave = findViewById(R.id.leave_all);
        payRoll = findViewById(R.id.pay_roll_info);
        directory = findViewById(R.id.directory);

//        logout = findViewById(R.id.main_menu_log_out);

        if (userInfoLists.size() != 0) {
            String firstname = userInfoLists.get(0).getUser_fname();
            String lastName = userInfoLists.get(0).getUser_lname();
            if (firstname == null) {
                firstname = "";
            }
            if (lastName == null) {
                lastName = "";
            }
            String empFullName = firstname+" "+lastName;
            userName.setText(empFullName);
        }

        if (userDesignations.size() != 0) {
            String jsmName = userDesignations.get(0).getJsm_name();
            if (jsmName == null) {
                jsmName = "";
            }
            designation.setText(jsmName);

            String deptName = userDesignations.get(0).getDiv_name();
            if (deptName == null) {
                deptName = "";
            }
            department.setText(deptName);
        }

        personalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, EmplyeeInformation.class);
                startActivity(intent);

            }
        });

        attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, Attendance.class);
                startActivity(intent);
            }
        });

        leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, Leave.class);
                startActivity(intent);
            }
        });

        payRoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, PayRollInfo.class);
                startActivity(intent);
            }
        });

        directory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, DirectoryWithDivision.class);
                startActivity(intent);
            }
        });

        dashB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, Dashboard.class);
                intent.putExtra("FROMMAINMENU", false);
                startActivity(intent);
                finish();
            }
        });

        logoutImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMyServiceRunning(Service.class)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainMenu.this);
                    builder.setMessage("Your Tracking Service is running. You can not Log Out while Running this Service!")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainMenu.this);
                    builder.setTitle("LOG OUT!")
                            .setMessage("Do you want to Log Out?")
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {


                                    userInfoLists.clear();
                                    userDesignations.clear();
                                    userInfoLists = new ArrayList<>();
                                    userDesignations = new ArrayList<>();
                                    isApproved = 0;
                                    isLeaveApproved = 0;

                                    SharedPreferences.Editor editor1 = sharedPreferences.edit();
                                    editor1.remove(USER_NAME);
                                    editor1.remove(USER_F_NAME);
                                    editor1.remove(USER_L_NAME);
                                    editor1.remove(EMAIL);
                                    editor1.remove(CONTACT);
                                    editor1.remove(EMP_ID_LOGIN);

                                    editor1.remove(JSM_CODE);
                                    editor1.remove(JSM_NAME);
                                    editor1.remove(JSD_ID_LOGIN);
                                    editor1.remove(JSD_OBJECTIVE);
                                    editor1.remove(DEPT_NAME);
                                    editor1.remove(DIV_NAME);
                                    editor1.remove(DESG_NAME);
                                    editor1.remove(DESG_PRIORITY);
                                    editor1.remove(JOINING_DATE);
                                    editor1.remove(DIV_ID);
                                    editor1.remove(LOGIN_TF);

                                    editor1.remove(IS_ATT_APPROVED);
                                    editor1.remove(IS_LEAVE_APPROVED);
                                    editor1.remove(COMPANY);
                                    editor1.remove(SOFTWARE);
                                    editor1.remove(LIVE_FLAG);
                                    editor1.apply();
                                    editor1.commit();

                                    if (trackerAvailable == 1) {
                                        SharedPreferences.Editor editor = sharedSchedule.edit();
                                        editor.remove(SCHEDULING_EMP_ID);
                                        editor.remove(TRIGGERING);
                                        editor.apply();
                                        editor.commit();

                                        Intent intent1 = new Intent(MainMenu.this, Uploader.class);
                                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent1,PendingIntent.FLAG_UPDATE_CURRENT);
                                        alarmManager.cancel(pendingIntent);
                                    }

//                        if (Build.VERSION.SDK_INT < 16) {
//
//                            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
//                        }
//                        else {
//
//                            // Hide Status Bar.
//                            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
//                            decorView.setSystemUiVisibility(uiOptions);
//                        }
                                    Intent intent = new Intent(MainMenu.this, Login.class);
                                    startActivity(intent);
                                    finish();
                                    //System.exit(0);
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
        });
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
    public void onBackPressed() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("LOG OUT!")
//                .setMessage("Do you want to Log Out?")
//                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//
//                        userInfoLists.clear();
//                        userDesignations.clear();
//                        userInfoLists = new ArrayList<>();
//                        userDesignations = new ArrayList<>();
//                        isApproved = 0;
//                        isLeaveApproved = 0;
////                        if (Build.VERSION.SDK_INT < 16) {
////
////                            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
////                                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
////                        }
////                        else {
////
////                            // Hide Status Bar.
////                            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
////                            decorView.setSystemUiVisibility(uiOptions);
////                        }
//                        finish();
//                        //System.exit(0);
//                    }
//                })
//                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });
//        AlertDialog alert = builder.create();
//        alert.show();
        MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(MainMenu.this)
                .setTitle("EXIT!")
                .setMessage("Do You Want to Exit?")
                .setIcon(R.drawable.thrm_black)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Do nothing
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }
}