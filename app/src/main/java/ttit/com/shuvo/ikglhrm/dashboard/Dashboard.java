package ttit.com.shuvo.ikglhrm.dashboard;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.res.ResourcesCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import ttit.com.shuvo.ikglhrm.EmployeeInfo.EmplyeeInformation;
import ttit.com.shuvo.ikglhrm.Login;
import ttit.com.shuvo.ikglhrm.MainPage.MainMenu;
import ttit.com.shuvo.ikglhrm.R;
import ttit.com.shuvo.ikglhrm.UserDesignation;
import ttit.com.shuvo.ikglhrm.UserInfoList;
import ttit.com.shuvo.ikglhrm.WaitProgress;
import ttit.com.shuvo.ikglhrm.attendance.Attendance;
import ttit.com.shuvo.ikglhrm.attendance.giveAttendance.arraylists.AreaList;
import ttit.com.shuvo.ikglhrm.attendance.trackService.Service;
import ttit.com.shuvo.ikglhrm.directoryBook.DirectoryWithDivision;
import ttit.com.shuvo.ikglhrm.leaveAll.Leave;
import ttit.com.shuvo.ikglhrm.payRoll.PayRollInfo;
import ttit.com.shuvo.ikglhrm.payRoll.SalaryMonthList;
import ttit.com.shuvo.ikglhrm.scheduler.Uploader;

import static ttit.com.shuvo.ikglhrm.Login.CompanyName;
import static ttit.com.shuvo.ikglhrm.Login.SoftwareName;
import static ttit.com.shuvo.ikglhrm.Login.isApproved;
import static ttit.com.shuvo.ikglhrm.Login.isLeaveApproved;
import static ttit.com.shuvo.ikglhrm.Login.userDesignations;
import static ttit.com.shuvo.ikglhrm.Login.userInfoLists;
import static ttit.com.shuvo.ikglhrm.scheduler.Uploader.channelId;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.CENTER_API_FRONT;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.COMPANY;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.CONTACT;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.DEPT_NAME;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.DESG_NAME;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.DESG_PRIORITY;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.DIV_ID;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.DIV_NAME;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.EMAIL;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.EMP_ID_LOGIN;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.EMP_PASSWORD;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.FILE_OF_DAILY_ACTIVITY;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.IS_ATT_APPROVED;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.IS_LEAVE_APPROVED;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.JOINING_DATE;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.JSD_ID_LOGIN;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.JSD_OBJECTIVE;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.JSM_CODE;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.JSM_NAME;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.LIVE_FLAG;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.LOGIN_ACTIVITY_FILE;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.LOGIN_TF;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.SCHEDULING_EMP_ID;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.SCHEDULING_FILE;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.SOFTWARE;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.TRIGGERING;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.USER_F_NAME;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.USER_L_NAME;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.USER_NAME;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.WIDGET_EMP_ID;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.WIDGET_FILE;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.WIDGET_TRACKER_FLAG;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.api_url_front;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Dashboard extends AppCompatActivity {

    AppBarLayout appBarLayout;
    CollapsingToolbarLayout collapsingToolbar;
    Toolbar toolbar;

    TextView appName;
    ImageView home;
    ImageView logout;
    ImageView logout_tool;

    // GeoFence Attendance
    SwitchCompat geoSwitch;
    LinearLayout autoAttLay;

    SharedPreferences geoSharedData;
    final String GEO_FILE = "GEO_ACTIVE_FILE";
    final String GEO_ALL_DATA = "GEO_ALL_DATA";
    Gson gson = new Gson();
    String json = "";

    ArrayList<GeoLocationList> geoLocationLists;

    // User Card
    CardView userCard;
    TextView welcomeText;
    TextView userName;
    TextView designation;
    TextView department;
    TextView comp;
    ImageView userImage;

    // Attendance Part
    MaterialCardView refreshAttendance;

    TextView nowDateDashboard;
    TextClock nowTimeDashboard;
    TextView inTimeDashboard;
    TextView outTimeDashboard;
    TextView workHoursDashboard;

    String in_time_dash = "";
    String out_time_dash = "";
    String end_time_dash = "";

    TextView nowMonthDashboard;
    PieChart pieChart;

    ArrayList<PieEntry> attendanceEntry;
    String absent = "";
    String present = "";
    String leave = "";
    String holiday = "";
    String late = "";
    String early = "";

    String beginDate = "";
    String lastDate = "";

    // Leave Part
    MaterialCardView refreshLeave;

    TextView pendingLeaveCount;
    String pending_leave_count = "0";

    TextView approveLeaveCount;
    String approved_leave_count = "0";

    TextView rejectedLeaveCount;
    String rej_leave_count = "0";

    CardView upcomingLeaveCardView;

    TextView upcomingLeaveTypeName;
    String upcoming_leave_type = "";

    TextView upcomingLeaveFromDate;
    String upcoming_leave_from_date = "";

    TextView upcomingLeaveMidText;

    TextView upcomingLeaveToDate;
    String upcoming_leave_to_date = "";

    CardView lastLeaveCardView;

    TextView lastLeaveTypeName;
    String last_leave_type = "";

    TextView lastLeaveFromDate;
    String last_leave_from_date;

    TextView lastLeaveMidText;

    TextView lastLeaveToDate;
    String last_leave_to_date = "";

    BarChart leaveChart;
    String leaveDate = "";

    ArrayList<BarEntry> balance;
    ArrayList<BarEntry> earn;
    ArrayList<String> shortCode;

    // Payroll Part
    MaterialCardView refreshSalary;

    BarChart salaryChart;

    ArrayList<BarEntry> salaryValue;
    ArrayList<String> year;

    ArrayList<SalaryMonthList> months;
    ArrayList<SalaryMonthList> salaryMonthLists;

    ArrayList<String> monthName;
    ArrayList<String> salary;

    String formattedDate = "";

    // Others
    MaterialCardView directory;
    FloatingActionButton attendanceButton;
    BottomNavigationView bottomNavigationView;

    // Connection
    WaitProgress waitProgress = new WaitProgress();
//    private String message = null;
    private Boolean conn = false;
    private Boolean connected = false;

//    private Connection connection;

    public static String emp_id_for_xml = "";
    String emp_id = "";
    String emp_code = "";
    public static int trackerAvailable = 0;

    public static ArrayList<String> lastTenDaysXml;

    SharedPreferences sharedPreferences;
    SharedPreferences sharedSchedule;
    SharedPreferences attendanceWidgetPreferences;

    public static AlarmManager alarmManager;

//    static SharedPreferences sharedPreferencesDA;

    boolean loginLog_check;
    boolean checkEmpFlag = false;

//    String android_id = "";
    String model = "";
    String brand = "";
    String ipAddress = "";
    String hostUserName = "";
//    String sessionId = "";
    String osName = "";
//    public static int RESULT_LOAD_IMG = 1901;
    public static Bitmap selectedImage;
    boolean imageFound = false;


//    float dX;
//    float dY;
//    private static float downRawX, downRawY;
//    private final static float CLICK_DRAG_TOLERANCE = 10;

    FusedLocationProviderClient fusedLocationProviderClient;
    LocationManager locationManager;
    LocationRequest locationRequest;
    String inTime = "";
    String address = "";
    LatLng[] lastLatLongitude = {new LatLng(0, 0)};
    String lat = "";
    String lon = "";
    Timestamp ts;
    String timeToShow = "";

    String machineCode = "";
    //    private LocationCallback locationCallback;
    boolean onResumeLoad = true;

    ArrayList<AreaList> areaLists;

    Logger logger = Logger.getLogger(Dashboard.class.getName());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setNavigationBarColor(Color.parseColor("#f0932b"));
        setContentView(R.layout.activity_dashboard);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        appBarLayout = findViewById(R.id.app_bar);
        collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitleEnabled(false);

        appName = findViewById(R.id.thrm_app_name);
        home = findViewById(R.id.home_icon);
        logout = findViewById(R.id.log_out_icon);
        logout_tool = findViewById(R.id.log_out_icon_toolbar);

        geoSwitch = findViewById(R.id.geo_fence_on_off_switch);
        geoSwitch.setVisibility(View.GONE);
        autoAttLay = findViewById(R.id.auto_att_layoout);
        autoAttLay.setVisibility(View.GONE);

        userCard = findViewById(R.id.userinfo_card);
        welcomeText = findViewById(R.id.welcome_text_view);
        userName = findViewById(R.id.user_name_dashboard);
        department = findViewById(R.id.user_depart_dashboard);
        designation = findViewById(R.id.user_desg_dashboard);
        comp = findViewById(R.id.company_name_dashboard);
        userImage = findViewById(R.id.user_pic_dashboard);

        refreshAttendance = findViewById(R.id.refresh_graph_attendance_dashboard);

        nowDateDashboard = findViewById(R.id.now_date_time_dashboard);
        nowTimeDashboard = findViewById(R.id.text_clock_dashboard);
        inTimeDashboard = findViewById(R.id.in_time_attendance_dashboard);
        outTimeDashboard = findViewById(R.id.out_time_attendance_dashboard);
        workHoursDashboard = findViewById(R.id.working_hours_attendance_dashboard);
        nowMonthDashboard = findViewById(R.id.now_month_dashboard);

        pieChart = findViewById(R.id.piechart_attendance_dashboard);

        refreshLeave = findViewById(R.id.refresh_graph_leave_dashboard);

        pendingLeaveCount = findViewById(R.id.pending_leave_dashboard);
        approveLeaveCount = findViewById(R.id.approved_leave_dashboard);
        rejectedLeaveCount = findViewById(R.id.rejected_leave_dashboard);

        upcomingLeaveCardView = findViewById(R.id.upcoming_leave_layout_dashboard);
        upcomingLeaveCardView.setVisibility(View.GONE);
        upcomingLeaveTypeName = findViewById(R.id.upcoming_leave_type_name_in_main_menu);

        upcomingLeaveFromDate = findViewById(R.id.upcoming_leave_from_date_in_main_menu);
        upcomingLeaveMidText = findViewById(R.id.upcoming_leave_middle_text_in_main_menu);
        upcomingLeaveToDate = findViewById(R.id.upcoming_leave_to_date_in_main_menu);

        lastLeaveCardView = findViewById(R.id.last_taken_leave_layout_dashboard);
        lastLeaveCardView.setVisibility(View.GONE);
        lastLeaveTypeName = findViewById(R.id.last_leave_type_name_in_main_menu);

        lastLeaveFromDate = findViewById(R.id.last_leave_from_date_in_main_menu);
        lastLeaveMidText = findViewById(R.id.last_leave_mid_text_in_main_menu);
        lastLeaveToDate = findViewById(R.id.last_leave_to_date_in_main_menu);

        leaveChart = findViewById(R.id.multi_bar_chart_leave_dashboard);

        salaryChart = findViewById(R.id.barchart_salary_dashboard);
        refreshSalary = findViewById(R.id.refresh_graph_salary_dashboard);

        directory = findViewById(R.id.directory_dashboard);

        attendanceButton = findViewById(R.id.attendance_shortcut);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

//        attendanceButton.setOnTouchListener(this);
        attendanceButton.setOnClickListener(v -> attendanceShortcutTriggered());

        Intent in = getIntent();
        loginLog_check = in.getBooleanExtra("FROMMAINMENU", true);

        System.out.println("Log Needed? :" + loginLog_check);

        attendanceWidgetPreferences = getSharedPreferences(WIDGET_FILE, MODE_PRIVATE);
        geoSharedData = getSharedPreferences(GEO_FILE, MODE_PRIVATE);

        sharedPreferences = getSharedPreferences(LOGIN_ACTIVITY_FILE, MODE_PRIVATE);
        boolean loginfile = sharedPreferences.getBoolean(LOGIN_TF, false);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        appBarLayout.addOnOffsetChangedListener((appBarLayout1, verticalOffset) -> {
            int totalScrollRange = appBarLayout1.getTotalScrollRange();

            if (Math.abs(verticalOffset) >= totalScrollRange) {
                // Fully collapsed
                if (toolbar.getVisibility() != View.VISIBLE) {
                    System.out.println("DASD");
                    toolbar.setVisibility(View.VISIBLE);
                }
            } else {
                // Fully expanded or in-between
                if (toolbar.getVisibility() != View.GONE) {
                    System.out.println("dawsd");
                    toolbar.setVisibility(View.GONE);
                }
            }
        });

        if (loginfile) {
            if (loginLog_check) {
                System.out.println(loginfile + " PAISE");
                String userName = sharedPreferences.getString(USER_NAME, null);
                String userFname = sharedPreferences.getString(USER_F_NAME, null);
                String userLname = sharedPreferences.getString(USER_L_NAME, null);
                String email = sharedPreferences.getString(EMAIL, null);
                String contact = sharedPreferences.getString(CONTACT, null);
                String emp_id_login = sharedPreferences.getString(EMP_ID_LOGIN, null);

                userInfoLists = new ArrayList<>();
                userInfoLists.add(new UserInfoList(userName, userFname, userLname, email, contact, emp_id_login));

                String jsm_code = sharedPreferences.getString(JSM_CODE, null);
                String jsm_name = sharedPreferences.getString(JSM_NAME, null);
                String jsd_id = sharedPreferences.getString(JSD_ID_LOGIN, null);
                String jsd_obj = sharedPreferences.getString(JSD_OBJECTIVE, null);
                String dept_name = sharedPreferences.getString(DEPT_NAME, null);
                String div_name = sharedPreferences.getString(DIV_NAME, null);
                String desg_name = sharedPreferences.getString(DESG_NAME, null);
                String desg_priority = sharedPreferences.getString(DESG_PRIORITY, null);
                String joining = sharedPreferences.getString(JOINING_DATE, null);
                String div_id = sharedPreferences.getString(DIV_ID, null);

                userDesignations = new ArrayList<>();
                userDesignations.add(new UserDesignation(jsm_code, jsm_name, jsd_id, jsd_obj, dept_name, div_name, desg_name, desg_priority, joining, div_id));

                SoftwareName = sharedPreferences.getString(SOFTWARE, null);
                CompanyName = sharedPreferences.getString(COMPANY, null);

                isApproved = sharedPreferences.getInt(IS_ATT_APPROVED, 0);
                isLeaveApproved = sharedPreferences.getInt(IS_LEAVE_APPROVED, 0);
                api_url_front = sharedPreferences.getString(CENTER_API_FRONT,null);
//                DEFAULT_USERNAME = sharedPreferences.getString(DATABASE_NAME,DEFAULT_USERNAME);
            }
        }
        else {
            onResumeLoad = false;
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(Dashboard.this);
            builder.setTitle("FORCED LOG OUT!")
                    .setIcon(R.drawable.hrm_new_round_icon_custom)
                    .setMessage("Some data may have been changed and could not be found due to server maintenance. We are sorry for the disturbance. Please Login again to continue the app.")
                    .setPositiveButton("OK", (dialog, which) -> {

                        userInfoLists.clear();
                        userDesignations.clear();
                        userInfoLists = new ArrayList<>();
                        userDesignations = new ArrayList<>();
                        isApproved = 0;
                        isLeaveApproved = 0;

                        SharedPreferences.Editor widgetEditor = attendanceWidgetPreferences.edit();
                        widgetEditor.remove(WIDGET_EMP_ID);
                        widgetEditor.remove(WIDGET_TRACKER_FLAG);
                        widgetEditor.apply();
                        widgetEditor.commit();

                        SharedPreferences.Editor editor1 = sharedPreferences.edit();
                        editor1.remove(USER_NAME);
                        editor1.remove(USER_F_NAME);
                        editor1.remove(USER_L_NAME);
                        editor1.remove(EMAIL);
                        editor1.remove(CONTACT);
                        editor1.remove(EMP_ID_LOGIN);
                        editor1.remove(EMP_PASSWORD);

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
//                                    editor1.remove(DATABASE_NAME);
                        editor1.apply();
                        editor1.commit();

                        if (trackerAvailable == 1) {
                            SharedPreferences.Editor editor = sharedSchedule.edit();
                            editor.remove(SCHEDULING_EMP_ID);
                            editor.remove(TRIGGERING);
                            editor.apply();
                            editor.commit();

                            Intent intent1 = new Intent(Dashboard.this, Uploader.class);
                            PendingIntent pendingIntent;
                            pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent1, PendingIntent.FLAG_IMMUTABLE);
                            alarmManager.cancel(pendingIntent);
                        }

                        Intent intent = new Intent(Dashboard.this, Login.class);
                        startActivity(intent);
                        finish();
                    });
            AlertDialog alert = builder.create();
            alert.setCancelable(false);
            alert.setCanceledOnTouchOutside(false);
            alert.show();
        }

        emp_id = userInfoLists.get(0).getEmp_id();
        emp_code = userInfoLists.get(0).getUserName();
        emp_id_for_xml = emp_id;

        home.setOnClickListener(v -> {
            Intent intent = new Intent(Dashboard.this, MainMenu.class);
            startActivity(intent);
            finish();
        });

        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_YEAR, -12);

        lastTenDaysXml = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            cal.add(Calendar.DAY_OF_YEAR, +1);
            Date calTime = cal.getTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);
            String ddd = simpleDateFormat.format(calTime);

            ddd = ddd.toUpperCase();
            System.out.println(ddd);
            lastTenDaysXml.add(ddd);
        }

        logout.setOnClickListener(v -> {
            if (isMyServiceRunning()) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(Dashboard.this);
                builder.setMessage("Your Tracking Service is running. You can not Log Out while Running this Service!")
                        .setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(Dashboard.this);
                builder.setTitle("LOG OUT!")
                        .setIcon(R.drawable.hrm_new_round_icon_custom)
                        .setMessage("Do you want to Log Out?")
                        .setPositiveButton("YES", (dialog, which) -> {

                            userInfoLists.clear();
                            userDesignations.clear();
                            userInfoLists = new ArrayList<>();
                            userDesignations = new ArrayList<>();
                            isApproved = 0;
                            isLeaveApproved = 0;

                            SharedPreferences.Editor widgetEditor = attendanceWidgetPreferences.edit();
                            widgetEditor.remove(WIDGET_EMP_ID);
                            widgetEditor.remove(WIDGET_TRACKER_FLAG);
                            widgetEditor.apply();
                            widgetEditor.commit();

                            SharedPreferences.Editor editor1 = sharedPreferences.edit();
                            editor1.remove(USER_NAME);
                            editor1.remove(USER_F_NAME);
                            editor1.remove(USER_L_NAME);
                            editor1.remove(EMAIL);
                            editor1.remove(CONTACT);
                            editor1.remove(EMP_ID_LOGIN);
                            editor1.remove(EMP_PASSWORD);

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
//                                    editor1.remove(DATABASE_NAME);
                            editor1.apply();
                            editor1.commit();

                            if (trackerAvailable == 1) {
                                SharedPreferences.Editor editor = sharedSchedule.edit();
                                editor.remove(SCHEDULING_EMP_ID);
                                editor.remove(TRIGGERING);
                                editor.apply();
                                editor.commit();

                                Intent intent1 = new Intent(Dashboard.this, Uploader.class);
                                PendingIntent pendingIntent;
                                pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent1, PendingIntent.FLAG_IMMUTABLE);
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
                            Intent intent = new Intent(Dashboard.this, Login.class);
                            startActivity(intent);
                            finish();
                            //System.exit(0);
                        })
                        .setNegativeButton("NO", (dialog, which) -> {

                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        logout_tool.setOnClickListener(v -> {
            if (isMyServiceRunning()) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(Dashboard.this);
                builder.setMessage("Your Tracking Service is running. You can not Log Out while Running this Service!")
                        .setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(Dashboard.this);
                builder.setTitle("LOG OUT!")
                        .setIcon(R.drawable.hrm_new_round_icon_custom)
                        .setMessage("Do you want to Log Out?")
                        .setPositiveButton("YES", (dialog, which) -> {

                            userInfoLists.clear();
                            userDesignations.clear();
                            userInfoLists = new ArrayList<>();
                            userDesignations = new ArrayList<>();
                            isApproved = 0;
                            isLeaveApproved = 0;

                            SharedPreferences.Editor widgetEditor = attendanceWidgetPreferences.edit();
                            widgetEditor.remove(WIDGET_EMP_ID);
                            widgetEditor.remove(WIDGET_TRACKER_FLAG);
                            widgetEditor.apply();
                            widgetEditor.commit();

                            SharedPreferences.Editor editor1 = sharedPreferences.edit();
                            editor1.remove(USER_NAME);
                            editor1.remove(USER_F_NAME);
                            editor1.remove(USER_L_NAME);
                            editor1.remove(EMAIL);
                            editor1.remove(CONTACT);
                            editor1.remove(EMP_ID_LOGIN);
                            editor1.remove(EMP_PASSWORD);

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
//                                    editor1.remove(DATABASE_NAME);
                            editor1.apply();
                            editor1.commit();

                            if (trackerAvailable == 1) {
                                SharedPreferences.Editor editor = sharedSchedule.edit();
                                editor.remove(SCHEDULING_EMP_ID);
                                editor.remove(TRIGGERING);
                                editor.apply();
                                editor.commit();

                                Intent intent1 = new Intent(Dashboard.this, Uploader.class);
                                PendingIntent pendingIntent;
                                pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent1, PendingIntent.FLAG_IMMUTABLE);
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
                            Intent intent = new Intent(Dashboard.this, Login.class);
                            startActivity(intent);
                            finish();
                            //System.exit(0);
                        })
                        .setNegativeButton("NO", (dialog, which) -> {

                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        refreshLeave.setOnClickListener(v -> {
//                new LeaveCheck().execute();
            getLeaveGraph();
        });

        refreshAttendance.setOnClickListener(v -> {
//                new AttendanceCheck().execute();
            getAttendanceGraph();
        });

        refreshSalary.setOnClickListener(v -> {
//                new SalaryCheck().execute();
            getSalaryGraph();
        });

        directory.setOnClickListener(view -> {
            Intent intent = new Intent(Dashboard.this, DirectoryWithDivision.class);
            startActivity(intent);
        });

//        leaveChart.setOnClickListener(v -> {
//            Intent intent = new Intent(Dashboard.this, Leave.class);
//            startActivity(intent);
//        });
//
//        pieChart.setOnClickListener(v -> {
//            Intent intent = new Intent(Dashboard.this, Attendance.class);
//            startActivity(intent);
//        });
//
//        salaryChart.setOnClickListener(v -> {
//            Intent intent = new Intent(Dashboard.this, PayRollInfo.class);
//            startActivity(intent);
//        });
//
//        userCard.setOnClickListener(v -> {
//            Intent intent = new Intent(Dashboard.this, EmplyeeInformation.class);
//            startActivity(intent);
//        });

//        userImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
//                photoPickerIntent.setType("image/*");
////                imagePickerActivityResult.launch(photoPickerIntent);
//                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
//            }
//        });

//        setGeoEvent();

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.my_info_menu) {
                Intent intent = new Intent(Dashboard.this, EmplyeeInformation.class);
                startActivity(intent);
            } else if (item.getItemId() == R.id.attendance_menu) {
                Intent intent = new Intent(Dashboard.this, Attendance.class);
                startActivity(intent);
            } else if (item.getItemId() == R.id.leave_menu) {
                Intent intent = new Intent(Dashboard.this, Leave.class);
                startActivity(intent);
            } else if (item.getItemId() == R.id.pay_roll_menu) {
                Intent intent = new Intent(Dashboard.this, PayRollInfo.class);
                startActivity(intent);
            }
            onResumeLoad = true;
            return true;
        });

        initData();

        deleteSharedPreferences(getApplicationContext());

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(Dashboard.this)
                        .setTitle("EXIT!")
                        .setMessage("Do you want to exit?")
                        .setIcon(R.drawable.hrm_new_round_icon_custom)
                        .setPositiveButton("Yes", (dialog, which) -> finish())
                        .setNegativeButton("No", (dialog, which) -> {
                            //Do nothing
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

    }

    private void initData() {
        int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        String wt;
        if (currentHour >= 4 && currentHour <= 11) {
            wt = "GOOD MORNING,";
        } else if (currentHour >= 12 && currentHour <= 16) {
            wt = "GOOD AFTERNOON,";
        } else if (currentHour >= 17 && currentHour <= 22) {
            wt = "GOOD EVENING,";
        } else {
            wt = "HELLO,";
        }
        welcomeText.setText(wt);

        Date dateCalendar = Calendar.getInstance().getTime();

        SimpleDateFormat dateAttFormat = new SimpleDateFormat("EEE, MMM dd", Locale.ENGLISH);
        SimpleDateFormat monthAttFormat = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
        String dc = dateAttFormat.format(dateCalendar) + ",";
        String mc = monthAttFormat.format(dateCalendar).toUpperCase(Locale.ENGLISH);

        nowDateDashboard.setText(dc);
        nowMonthDashboard.setText(mc);

        Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.poppins_bold);
        nowTimeDashboard.setTypeface(typeface);

        if (!userInfoLists.isEmpty()) {
            String firstname = userInfoLists.get(0).getUser_fname();
            String lastName = userInfoLists.get(0).getUser_lname();
            if (firstname == null) {
                firstname = "";
            }
            if (lastName == null) {
                lastName = "";
            }
            String empFullName = firstname + " " + lastName;
            userName.setText(empFullName);
        }

        if (!userDesignations.isEmpty()) {
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

//        appName.setText(SoftwareName);
        comp.setText(CompanyName);

        model = Build.MODEL;

        brand = Build.BRAND;

        ipAddress = getIPAddress(true);

        hostUserName = getHostName("localhost");

        StringBuilder builder = new StringBuilder();
        builder.append("ANDROID: ").append(Build.VERSION.RELEASE);

        Field[] fields = Build.VERSION_CODES.class.getFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            int fieldValue = -1;

            try {
                fieldValue = field.getInt(new Object());
            } catch (IllegalArgumentException | IllegalAccessException | NullPointerException e) {
                logger.log(Level.WARNING, e.getMessage(), e);
            }

            if (fieldValue == Build.VERSION.SDK_INT) {
                builder.append(": ").append(fieldName);
            }
        }

        System.out.println("OS: " + builder);

        osName = builder.toString();

//        android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        SalaryInit();
        AttendanceInit();
        LeaveInit();
    }

    private void setGeoEvent() {
        json = geoSharedData.getString(GEO_ALL_DATA, "");
        Type type = new TypeToken<ArrayList<GeoLocationList>>() {
        }.getType();
        ArrayList<GeoLocationList> savedLocationLists = gson.fromJson(json, type);

        if (savedLocationLists != null) {
            geoSwitch.setChecked(!savedLocationLists.isEmpty());
        } else {
            geoSwitch.setChecked(false);
        }

        geoSwitch.setOnClickListener(view -> {
            if (geoSwitch.isChecked()) {
                boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                if (gps) {
                    addGeoFence();
                } else {
                    Toast.makeText(this, "Your GPS Provider is disabled. Please enable it and try again", Toast.LENGTH_SHORT).show();
                    geoSwitch.setChecked(false);
                }
            } else {
                boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                if (gps) {
                    removeGeoFence();
                } else {
                    Toast.makeText(this, "Your GPS Provider is disabled. Please enable it and try again", Toast.LENGTH_SHORT).show();
                    geoSwitch.setChecked(true);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (onResumeLoad) {
            onResumeLoad = false;
            getAllData();
        }
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

    public static void deleteSharedPreferences(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //return context.deleteSharedPreferences(name);emp_id+"_"+ddd+"_track"
            System.out.println("Pai nai");

            for (int i = 0; i < lastTenDaysXml.size(); i++) {
                FILE_OF_DAILY_ACTIVITY = emp_id_for_xml + "_" + lastTenDaysXml.get(i) + "_track";
                System.out.println(FILE_OF_DAILY_ACTIVITY + " of " + lastTenDaysXml.get(i));
                File dir = new File(context.getApplicationInfo().dataDir, "shared_prefs/" + FILE_OF_DAILY_ACTIVITY + ".xml");
                if (dir.exists()) {
                    System.out.println(true);
                    context.getSharedPreferences(FILE_OF_DAILY_ACTIVITY, MODE_PRIVATE).edit().clear().commit();
                    System.out.println(dir);
                    boolean ddd = dir.delete();

                    if (ddd) {
                        System.out.println("Deleted");
                    }
                } else {
                    System.out.println(false);
                }
            }


//            sharedPreferencesDA = context.getSharedPreferences(FILE_OF_DAILY_ACTIVITY, MODE_PRIVATE);
//
//            String dist = sharedPreferencesDA.getString(DISTANCE,null);
//            String totalTime = sharedPreferencesDA.getString(TOTAL_TIME,null);
//            String stoppedTime = sharedPreferencesDA.getString(STOPPED_TIME,null);

//            System.out.println(dist+" "+totalTime+" "+ stoppedTime);


        }
    }

    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':') < 0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim < 0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.WARNING,ex.getLocalizedMessage(),ex);
        } // for now eat exceptions
        return "";
    }

    public static String getHostName(String defValue) {
        try {
            @SuppressLint("DiscouragedPrivateApi") Method getString = Build.class.getDeclaredMethod("getString", String.class);
            getString.setAccessible(true);
            return getString.invoke(null, "net.hostname").toString();
        } catch (Exception ex) {
            return defValue;
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "ReminderChannel";
            String description = "Channel for Reminder";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void createNotificationChannelForWidget() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(getString(R.string.att_channel_id), "Attendance Info", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("Attendance from Widget");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    public void SalaryInit() {
        months = new ArrayList<>();
        salaryMonthLists = new ArrayList<>();

        monthName = new ArrayList<>();
        salary = new ArrayList<>();

        salaryChart.getDescription().setEnabled(false);
        salaryChart.setPinchZoom(false);

        salaryChart.setDrawBarShadow(false);
        salaryChart.setDrawGridBackground(false);

        salaryChart.getAxisLeft().setDrawGridLines(true);

        salaryValue = new ArrayList<>();

        year = new ArrayList<>();

        XAxis xAxis = salaryChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        xAxis.setDrawGridLines(false);

        salaryChart.getLegend().setFormToTextSpace(10);
        salaryChart.getLegend().setStackSpace(10);
        salaryChart.getLegend().setYOffset(10);
        salaryChart.setExtraOffsets(0, 0, 0, 20);

        // zoom and touch disabled
        salaryChart.setScaleEnabled(false);
        salaryChart.setTouchEnabled(true);
        salaryChart.setDoubleTapToZoomEnabled(false);
        salaryChart.setClickable(true);
        salaryChart.setHighlightPerTapEnabled(false);
        salaryChart.setHighlightPerDragEnabled(false);
        //salaryChart.setOnTouchListener(null);

        salaryChart.getAxisRight().setEnabled(false);
        salaryChart.getAxisLeft().setAxisMinimum(0);
        salaryChart.getLegend().setEnabled(false);

        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);

        formattedDate = df.format(c);

        Calendar cal = Calendar.getInstance();

        cal.add(Calendar.MONTH, -1);
        String previousMonthYear = new SimpleDateFormat("MMM-yy", Locale.ENGLISH).format(cal.getTime());
        previousMonthYear = previousMonthYear.toUpperCase();
        months.add(new SalaryMonthList(previousMonthYear, "0"));
        System.out.println(previousMonthYear);

        cal.add(Calendar.MONTH, -1);
        previousMonthYear = new SimpleDateFormat("MMM-yy", Locale.ENGLISH).format(cal.getTime());
        previousMonthYear = previousMonthYear.toUpperCase();
        months.add(new SalaryMonthList(previousMonthYear, "0"));
        System.out.println(previousMonthYear);

        cal.add(Calendar.MONTH, -1);
        previousMonthYear = new SimpleDateFormat("MMM-yy", Locale.ENGLISH).format(cal.getTime());
        previousMonthYear = previousMonthYear.toUpperCase();
        months.add(new SalaryMonthList(previousMonthYear, "0"));
        System.out.println(previousMonthYear);

        cal.add(Calendar.MONTH, -1);
        previousMonthYear = new SimpleDateFormat("MMM-yy", Locale.ENGLISH).format(cal.getTime());
        previousMonthYear = previousMonthYear.toUpperCase();
        months.add(new SalaryMonthList(previousMonthYear, "0"));
        System.out.println(previousMonthYear);

        cal.add(Calendar.MONTH, -1);
        previousMonthYear = new SimpleDateFormat("MMM-yy", Locale.ENGLISH).format(cal.getTime());
        previousMonthYear = previousMonthYear.toUpperCase();
        months.add(new SalaryMonthList(previousMonthYear, "0"));
        System.out.println(previousMonthYear);

        cal.add(Calendar.MONTH, -1);
        previousMonthYear = new SimpleDateFormat("MMM-yy", Locale.ENGLISH).format(cal.getTime());
        previousMonthYear = previousMonthYear.toUpperCase();
        months.add(new SalaryMonthList(previousMonthYear, "0"));
        System.out.println(previousMonthYear);
    }

    public void AttendanceInit() {

        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);

        lastDate = df.format(c);

        SimpleDateFormat sdf = new SimpleDateFormat("MMM-yy", Locale.ENGLISH);

        beginDate = sdf.format(c);
        beginDate = "01-" + beginDate;

        attendanceEntry = new ArrayList<>();

        //pieChart.setCenterText("Attendance");
        pieChart.setDrawEntryLabels(true);
        pieChart.setCenterTextSize(12);
        pieChart.setHoleRadius(30);
        pieChart.setTransparentCircleRadius(20);

        pieChart.setEntryLabelTextSize(11);
        pieChart.setEntryLabelColor(Color.DKGRAY);
        pieChart.getDescription().setEnabled(false);
        pieChart.setTouchEnabled(true);
        pieChart.setClickable(true);
        pieChart.setHighlightPerTapEnabled(true);
        pieChart.setOnTouchListener(null);


        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setForm(Legend.LegendForm.CIRCLE);

        l.setTextSize(12);
        l.setWordWrapEnabled(false);
        l.setDrawInside(false);
        l.setYOffset(10f);
    }

    public void LeaveInit() {
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);

        leaveDate = df.format(c);

        balance = new ArrayList<>();
        earn = new ArrayList<>();
        shortCode = new ArrayList<>();

        leaveChart.getDescription().setEnabled(false);
        leaveChart.setPinchZoom(false);
        leaveChart.getAxisLeft().setDrawGridLines(true);
        leaveChart.getLegend().setStackSpace(20);
        leaveChart.getLegend().setYOffset(10);
        leaveChart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        leaveChart.setExtraOffsets(0, 0, 0, 20);
        leaveChart.setScaleEnabled(false);
        leaveChart.setTouchEnabled(true);
        leaveChart.setDoubleTapToZoomEnabled(false);
        leaveChart.setHighlightPerTapEnabled(false);
        leaveChart.setHighlightPerDragEnabled(false);
        leaveChart.getAxisLeft().setAxisMinimum(0);

        leaveChart.getAxisRight().setEnabled(false);
    }

//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        int action = event.getAction();
//        if (action == MotionEvent.ACTION_DOWN) {
//            downRawX = event.getRawX();
//            downRawY = event.getRawY();
//            dX = v.getX() - downRawX;
//            dY = v.getY() - downRawY;
//            return true; // Consumed
//        }
//        else if (action == MotionEvent.ACTION_MOVE) {
//            int viewWidth = v.getWidth();
//            int viewHeight = v.getHeight();
//
//            View viewParent = (View)v.getParent();
//            int parentWidth = viewParent.getWidth();
//            int parentHeight = viewParent.getHeight();
//
//            float newX = event.getRawX() + dX;
//            newX = Math.max(0, newX); // Don't allow the FAB past the left hand side of the parent
//            newX = Math.min(parentWidth - viewWidth, newX); // Don't allow the FAB past the right hand side of the parent
//
//            float newY = event.getRawY() + dY;
//            newY = Math.max(0, newY); // Don't allow the FAB past the top of the parent
//            newY = Math.min(parentHeight - viewHeight, newY); // Don't allow the FAB past the bottom of the parent
//
//            v.animate()
//                    .x(newX)
//                    .y(newY)
//                    .setDuration(0)
//                    .start();
//
//            return true; // Consumed
//
//        }
//        else if (action == MotionEvent.ACTION_UP) {
//
//            float upRawX = event.getRawX();
//            float upRawY = event.getRawY();
//
//            float upDX = upRawX - downRawX;
//            float upDY = upRawY - downRawY;
//
//            if (Math.abs(upDX) < CLICK_DRAG_TOLERANCE && Math.abs(upDY) < CLICK_DRAG_TOLERANCE) { // A click
//                return v.performClick();
//            }
//            else { // A drag
//                return true; // Consumed
//            }
//
//        }
//        else {
//            return v.onTouchEvent(event);
//        }
//    }

    public static class MyAxisValueFormatter extends ValueFormatter {
        private final ArrayList<String> mvalues;

        public MyAxisValueFormatter(ArrayList<String> mvalues) {
            this.mvalues = mvalues;
        }

        @Override
        public String getAxisLabel(float value, AxisBase axis) {
            return (mvalues.get((int) value));
        }
    }

    public void getSalaryGraph() {
        waitProgress.show(getSupportFragmentManager(), "WaitBar");
        waitProgress.setCancelable(false);
        connected = false;
        conn = false;

        salaryMonthLists = new ArrayList<>();

        BarData salaryData = new BarData();
        salaryChart.setData(salaryData);
        salaryChart.getData().clearValues();
        salaryChart.notifyDataSetChanged();
        salaryChart.clear();
        salaryChart.invalidate();

        RequestQueue requestQueue = Volley.newRequestQueue(Dashboard.this);

        String salaryDataUrl = api_url_front + "dashboard/getSalaryAndMonth/" + emp_id + "/" + formattedDate;

        StringRequest salaryMonthReq = new StringRequest(Request.Method.GET, salaryDataUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject salaryMonthInfo = array.getJSONObject(i);
                        String mon_name = salaryMonthInfo.getString("mon_name");
                        String net_salary = salaryMonthInfo.getString("net_salary");
                        salaryMonthLists.add(new SalaryMonthList(mon_name, net_salary));
                    }
                }
                connected = true;
                updateSalaryGraph();
            } catch (JSONException e) {
                connected = false;
                logger.log(Level.WARNING, e.getMessage(), e);
                updateSalaryGraph();
            }
        }, error -> {
            conn = false;
            connected = false;
            logger.log(Level.WARNING, error.getMessage(), error);
            updateSalaryGraph();
        });

        requestQueue.add(salaryMonthReq);
    }

    public void updateSalaryGraph() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                monthName = new ArrayList<>();
                salary = new ArrayList<>();
                salaryValue = new ArrayList<>();

                for (int i = 0; i < salaryMonthLists.size(); i++) {
                    for (int j = 0; j < months.size(); j++) {
                        if (months.get(j).getMonth().equals(salaryMonthLists.get(i).getMonth())) {
                            months.get(j).setSalary(salaryMonthLists.get(i).getSalary());
                        }
                    }
                }

                for (int i = months.size() - 1; i >= 0; i--) {

                    monthName.add(months.get(i).getMonth());
                    salary.add(months.get(i).getSalary());

                }

                System.out.println(monthName);
                System.out.println(salary);

                for (int i = 0; i < salary.size(); i++) {
                    salaryValue.add(new BarEntry(i, Float.parseFloat(salary.get(i)), i));
                }

                BarDataSet bardataset = new BarDataSet(salaryValue, "Months");
                salaryChart.animateY(1000);

                BarData data1 = new BarData(bardataset);
                final int[] barColors = new int[]{
                        Color.rgb(146, 197, 249),
                        Color.rgb(154, 216, 216),
                        Color.rgb(175, 220, 143),
                        Color.rgb(251, 224, 114),
                        Color.rgb(113, 164, 201),
                        Color.rgb(188, 228, 216),

                        Color.rgb(129, 236, 236),
                        Color.rgb(255, 118, 117),
                        Color.rgb(253, 121, 168),
                        Color.rgb(96, 163, 188)};
                bardataset.setColors(ColorTemplate.createColors(barColors));

                bardataset.setBarBorderColor(Color.DKGRAY);
                bardataset.setValueTextSize(11);
                bardataset.setValueFormatter(new LargeValueFormatter());
                salaryChart.setData(data1);
                salaryChart.getXAxis().setValueFormatter(new MyAxisValueFormatter(monthName));
                salaryChart.getAxisLeft().setValueFormatter(new LargeValueFormatter());

                conn = false;
                connected = false;
            } else {
                AlertDialog dialog = new AlertDialog.Builder(Dashboard.this)
                        .setMessage("There is a network issue in the server. Please Try later")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel", null)
                        .show();
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {
                    getSalaryGraph();
                    dialog.dismiss();
                });
            }
        } else {
            AlertDialog dialog = new AlertDialog.Builder(Dashboard.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel", null)
                    .show();
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {
                getSalaryGraph();
                dialog.dismiss();
            });
        }
    }

//    public void AttendanceGraph() {
//        try {
//            this.connection = createConnection();
//            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();
//
//
//            NoOfEmp = new ArrayList<>();
//            absent = "";
//            present = "";
//            leave = "";
//            holiday = "";
//            late = "";
//            early = "";
//
//
//            Statement stmt = connection.createStatement();
//
//
//
//
//            ResultSet rs=stmt.executeQuery("SELECT ATTD_HOLIDAY_COUNT ("+emp_id+", '"+ lastDate +"', 'A') ABSENT,\n" +
//                    "       (  ATTD_HOLIDAY_COUNT ("+emp_id+", '"+ lastDate +"', 'P')\n" +
//                    "        + ATTD_HOLIDAY_COUNT ("+emp_id+", '"+ lastDate +"', 'PW')\n" +
//                    "        + ATTD_HOLIDAY_COUNT ("+emp_id+", '"+ lastDate +"', 'PH')\n" +
//                    "        + ATTD_HOLIDAY_COUNT ("+emp_id+", '"+ lastDate +"', 'PL')\n" +
//                    "        + ATTD_HOLIDAY_COUNT ("+emp_id+", '"+ lastDate +"', 'PLH')\n" +
//                    "        + ATTD_HOLIDAY_COUNT ("+emp_id+", '"+ lastDate +"', 'PA')\n" +
//                    "        + ATTD_HOLIDAY_COUNT ("+emp_id+", '"+ lastDate +"', 'PLW'))\n" +
//                    "          PRESENT,\n" +
//                    "       (  ATTD_HOLIDAY_COUNT ("+emp_id+", '"+ lastDate +"', 'L')\n" +
//                    "        + ATTD_HOLIDAY_COUNT ("+emp_id+", '"+ lastDate +"', 'LW')\n" +
//                    "        + ATTD_HOLIDAY_COUNT ("+emp_id+", '"+ lastDate +"', 'LH'))\n" +
//                    "          LEAVE,\n" +
//                    "       ATTD_HOLIDAY_COUNT ("+emp_id+", '"+ lastDate +"', 'H') +\n" +
//                    "       ATTD_HOLIDAY_COUNT ("+emp_id+", '"+ lastDate +"', 'W') HOLIDAY_WEEKEND\n" +
//                    "  FROM DUAL");
//
//
//            while(rs.next()) {
//
//                absent = rs.getString(1);
//                present = rs.getString(2);
//                leave = rs.getString(3);
//                holiday = rs.getString(4);
//
//
//            }
//
//            ResultSet resultSet = stmt.executeQuery("SELECT LATE_COUNT_NEW\n" +
//                    "("+emp_id+",\n" +
//                    " '"+beginDate+"',\n" +
//                    " '"+ lastDate +"')\n" +
//                    "  FROM DUAL");
//
//            while (resultSet.next()) {
//                late = resultSet.getString(1);
//            }
//
//            ResultSet resultSet1 = stmt.executeQuery("  SELECT GET_EARLY_COUNT \n" +
//                    "("+emp_id+",\n" +
//                    " '"+beginDate+"',\n" +
//                    " '"+ lastDate +"')\n" +
//                    "  FROM DUAL");
//
//            while (resultSet1.next()) {
//                early = resultSet1.getString(1);
//            }
//
//
//            connected = true;
//
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

    public void getAttendanceGraph() {
        waitProgress.show(getSupportFragmentManager(), "WaitBar");
        waitProgress.setCancelable(false);
        connected = false;
        conn = false;

        attendanceEntry = new ArrayList<>();
        absent = "";
        present = "";
        leave = "";
        holiday = "";
        late = "";
        early = "";

        in_time_dash = "";
        out_time_dash = "";
        end_time_dash = "";
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat dfffff = new SimpleDateFormat("dd-MMMM-yyyy", Locale.ENGLISH);
        String lastDateForAttBot = dfffff.format(c);

        String attendDataUrl = api_url_front + "dashboard/getAttendanceData/" + beginDate + "/" + lastDate + "/" + emp_id;
        String todayAttDataUrl = api_url_front + "attendance/getTodayAttData/" + emp_id + "/" + lastDateForAttBot;

        RequestQueue requestQueue = Volley.newRequestQueue(Dashboard.this);

        StringRequest todayAttReq = new StringRequest(Request.Method.GET, todayAttDataUrl, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject todayAttDataInfo = array.getJSONObject(i);
                        in_time_dash = todayAttDataInfo.getString("dac_in_date_time")
                                .equals("null") ? "" : todayAttDataInfo.getString("dac_in_date_time");
                        out_time_dash = todayAttDataInfo.getString("dac_out_date_time")
                                .equals("null") ? "" : todayAttDataInfo.getString("dac_out_date_time");
                        end_time_dash = todayAttDataInfo.getString("dac_end_time")
                                .equals("null") ? "" : todayAttDataInfo.getString("dac_end_time");
                    }
                }
                connected = true;
                updateAttendanceGraph();
            } catch (JSONException e) {
                logger.log(Level.WARNING, e.getMessage(), e);
                connected = false;
                updateInterface();
            }
        }, error -> {
            logger.log(Level.WARNING, error.getMessage(), error);
            conn = false;
            connected = false;
            updateInterface();
        });

        StringRequest attendDataReq = new StringRequest(Request.Method.GET, attendDataUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject attendanceInfo = array.getJSONObject(i);
                        absent = attendanceInfo.getString("absent");
                        present = attendanceInfo.getString("present");
                        leave = attendanceInfo.getString("leave");
                        holiday = attendanceInfo.getString("holiday_weekend");
                        late = attendanceInfo.getString("late_count");
                        early = attendanceInfo.getString("early_count");
                    }
                    connected = true;
                    requestQueue.add(todayAttReq);
                } else {
                    connected = false;
                    updateAttendanceGraph();
                }
            } catch (JSONException e) {
                connected = false;
                logger.log(Level.WARNING, e.getMessage(), e);
                updateAttendanceGraph();
            }
        }, error -> {
            conn = false;
            connected = false;
            logger.log(Level.WARNING, error.getMessage(), error);
            updateAttendanceGraph();
        });

        requestQueue.add(attendDataReq);

    }

    public void updateAttendanceGraph() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                final int[] piecolors = new int[]{
                        Color.rgb(116, 185, 255),
                        Color.rgb(85, 239, 196),
                        Color.rgb(162, 155, 254),
                        Color.rgb(223, 230, 233),
                        Color.rgb(255, 234, 167),

                        Color.rgb(250, 177, 160),
                        Color.rgb(129, 236, 236),
                        Color.rgb(255, 118, 117),
                        Color.rgb(253, 121, 168),
                        Color.rgb(96, 163, 188)};


                if (absent != null) {
                    if (!absent.isEmpty()) {
                        if (!absent.equals("0")) {
                            attendanceEntry.add(new PieEntry(Float.parseFloat(absent), "Absent", 0));
                        }
                    }
                }

                if (present != null) {
                    if (!present.isEmpty()) {
                        if (!present.equals("0")) {
                            attendanceEntry.add(new PieEntry(Float.parseFloat(present), "Present", 1));
                        }
                    }
                }

                if (late != null) {
                    if (!late.isEmpty()) {
                        if (!late.equals("0")) {
                            attendanceEntry.add(new PieEntry(Float.parseFloat(late), "Late", 2));

                        }
                    }
                }

                if (early != null) {
                    if (!early.isEmpty()) {
                        if (!early.equals("0")) {
                            attendanceEntry.add(new PieEntry(Float.parseFloat(early), "Early", 3));
                        }
                    }
                }

                if (leave != null) {
                    if (!leave.isEmpty()) {
                        if (!leave.equals("0")) {
                            attendanceEntry.add(new PieEntry(Float.parseFloat(leave), "Leave", 4));
                        }
                    }
                }

                if (holiday != null) {
                    if (!holiday.isEmpty()) {
                        if (!holiday.equals("0")) {
                            attendanceEntry.add(new PieEntry(Float.parseFloat(holiday), "Holiday/Weekend", 5));
                        }
                    }
                }

                if (attendanceEntry.isEmpty()) {
                    attendanceEntry.add(new PieEntry(1, "No Data Found", 6));

                }

                PieDataSet dataSet = new PieDataSet(attendanceEntry, "");
                pieChart.animateXY(1000, 1000);
                pieChart.setEntryLabelColor(Color.TRANSPARENT);

                PieData data = new PieData(dataSet);
                dataSet.setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        return String.valueOf((int) Math.floor(value));
                    }
                });
                String label = dataSet.getValues().get(0).getLabel();
                System.out.println(label);
                if (label.equals("No Data Found")) {
                    dataSet.setValueTextColor(Color.TRANSPARENT);
                } else {
                    dataSet.setValueTextColor(Color.BLACK);
                }
                dataSet.setHighlightEnabled(true);
                dataSet.setValueTextSize(12);

                int[] num = new int[attendanceEntry.size()];
                for (int i = 0; i < attendanceEntry.size(); i++) {
                    int neki = (int) attendanceEntry.get(i).getData();
                    num[i] = piecolors[neki];
                }

                dataSet.setColors(ColorTemplate.createColors(num));

                pieChart.setData(data);
                pieChart.invalidate();

                inTimeDashboard.setText(in_time_dash);
                outTimeDashboard.setText(out_time_dash);
                if (!in_time_dash.isEmpty()) {
                    String work_hours = calculateWorkingHours(in_time_dash, out_time_dash, end_time_dash);
                    workHoursDashboard.setText(work_hours);
                }

                conn = false;
                connected = false;
            } else {
                AlertDialog dialog = new AlertDialog.Builder(Dashboard.this)
                        .setMessage("There is a network issue in the server. Please Try later")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel", null)
                        .show();
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    getAttendanceGraph();
                    dialog.dismiss();
                });
            }
        } else {
            AlertDialog dialog = new AlertDialog.Builder(Dashboard.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel", null)
                    .show();
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                getAttendanceGraph();
                dialog.dismiss();
            });
        }
    }

//    public void LeaveBalanceGraph() {
//        try {
//            this.connection = createConnection();
//            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();
//
//            balance = new ArrayList<>();
//            earn = new ArrayList<>();
//            shortCode = new ArrayList<>();
//            Statement stmt = connection.createStatement();
//
//
//
//
////            ResultSet rs=stmt.executeQuery("SELECT EM.LBEM_EMP_ID, lc.lc_short_code, LD.LBD_BALANCE_QTY,LD.LBD_CURRENT_QTY\n" +
////                    "FROM LEAVE_BALANCE_EMP_MST  EM,\n" +
////                    "LEAVE_BALANCE_YEAR_DTL YD,\n" +
////                    "LEAVE_BALANCE_DTL      LD,\n" +
////                    "leave_category lc\n" +
////                    "WHERE EM.LBEM_ID = YD.LBYD_LBEM_ID\n" +
////                    "AND YD.LBYD_ID = LD.LBD_LBYD_ID\n" +
////                    "and ld.lbd_lc_id = lc.lc_id\n" +
////                    "AND EM.LBEM_EMP_ID = "+emp_id+"\n" +
////                    "AND TO_CHAR (YD.LBYD_YEAR, 'YYYY') = TO_CHAR (TO_DATE('"+ leaveDate +"'), 'YYYY')");
//
//            ResultSet rs=stmt.executeQuery("SELECT EM.LBEM_EMP_ID, lc.lc_short_code, case when lc.lc_short_code = 'LP' then 0 else get_leave_balance(EM.LBEM_EMP_ID,'"+leaveDate+"', lc.lc_short_code) end balance,NVL(LD.LBD_CURRENT_QTY,0) + NVL(ld.lbd_opening_qty,0)\n" +
//                    "FROM LEAVE_BALANCE_EMP_MST  EM,\n" +
//                    "LEAVE_BALANCE_YEAR_DTL YD,\n" +
//                    "LEAVE_BALANCE_DTL      LD,\n" +
//                    "leave_category lc\n" +
//                    "WHERE EM.LBEM_ID = YD.LBYD_LBEM_ID\n" +
//                    "AND YD.LBYD_ID = LD.LBD_LBYD_ID\n" +
//                    "and ld.lbd_lc_id = lc.lc_id\n" +
//                    "AND EM.LBEM_EMP_ID = "+emp_id+"\n" +
//                    "AND TO_CHAR (YD.LBYD_YEAR, 'YYYY') = TO_CHAR (TO_DATE('"+ leaveDate +"'), 'YYYY')");
//
//
//            int i = 0;
//            while(rs.next()) {
//
//
//                String data = rs.getString(4);
//                String sc = rs.getString(2);
//                String bl = rs.getString(3);
//                if (data != null) {
//                    if (!data.equals("0")) {
//
//                        balance.add(new BarEntry(i, Float.parseFloat(rs.getString(3)),i));
//                        earn.add(new BarEntry(i, Float.parseFloat(rs.getString(4)),i));
//                        shortCode.add(rs.getString(2));
//                        i++;
//                    }
//
//                }
//
//
//
//            }
//
//
//            connected = true;
//
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

    public void getLeaveGraph() {
        waitProgress.show(getSupportFragmentManager(), "WaitBar");
        waitProgress.setCancelable(false);
        connected = false;
        conn = false;

        balance = new ArrayList<>();
        earn = new ArrayList<>();
        shortCode = new ArrayList<>();

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat yearFrm = new SimpleDateFormat("yyyy", Locale.ENGLISH);
        String year = yearFrm.format(c);

        pending_leave_count = "0";
        approved_leave_count = "0";
        rej_leave_count = "0";

        upcoming_leave_type = "";
        upcoming_leave_from_date = "";
        upcoming_leave_to_date = "";

        last_leave_type = "";
        last_leave_from_date = "";
        last_leave_to_date = "";

        BarData leavedata = new BarData();
        leaveChart.setData(leavedata);
        leaveChart.getData().clearValues();
        leaveChart.notifyDataSetChanged();
        leaveChart.clear();
        leaveChart.invalidate();

        String leaveDataUrl = api_url_front + "dashboard/getLeaveData/" + emp_id + "/" + leaveDate;
        String leaveCountUrl = api_url_front + "dashboard/getLeaveAppStatusCount?emp_id=" + emp_id + "&start_date=01-JAN-" + year + "&end_date=31-DEC-" + year;
        String upLeaveUrl = api_url_front + "leave/getUpcomingLeave?emp_id=" + emp_id;
        String lastLeaveUrl = api_url_front + "leave/getLastConsumedLeave?emp_id=" + emp_id;

        RequestQueue requestQueue = Volley.newRequestQueue(Dashboard.this);

        StringRequest lastLeaveReq = new StringRequest(Request.Method.GET, lastLeaveUrl, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    JSONObject info = array.getJSONObject(0);
                    last_leave_from_date = info.getString("from_date")
                            .equals("null") ? "" : info.getString("from_date");
                    last_leave_to_date = info.getString("to_date")
                            .equals("null") ? "" : info.getString("to_date");
                    last_leave_type = info.getString("lc_name")
                            .equals("null") ? "" : info.getString("lc_name");
                }
                connected = true;
                updateLeaveGraph();
            } catch (JSONException e) {
                logger.log(Level.WARNING, e.getMessage(), e);
                connected = false;
                updateLeaveGraph();
            }
        }, error -> {
            logger.log(Level.WARNING, error.getMessage(), error);
            conn = false;
            connected = false;
            updateLeaveGraph();
        });

        StringRequest upLeaveReq = new StringRequest(Request.Method.GET, upLeaveUrl, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    JSONObject info = array.getJSONObject(0);
                    upcoming_leave_from_date = info.getString("from_date")
                            .equals("null") ? "" : info.getString("from_date");
                    upcoming_leave_to_date = info.getString("to_date")
                            .equals("null") ? "" : info.getString("to_date");
                    upcoming_leave_type = info.getString("lc_name")
                            .equals("null") ? "" : info.getString("lc_name");
                }
                requestQueue.add(lastLeaveReq);
            } catch (JSONException e) {
                logger.log(Level.WARNING, e.getMessage(), e);
                connected = false;
                updateLeaveGraph();
            }
        }, error -> {
            logger.log(Level.WARNING, error.getMessage(), error);
            conn = false;
            connected = false;
            updateLeaveGraph();
        });

        StringRequest leaveCountReq = new StringRequest(Request.Method.GET, leaveCountUrl, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject todayAttDataInfo = array.getJSONObject(i);
                        pending_leave_count = todayAttDataInfo.getString("pending")
                                .equals("null") ? "0" : todayAttDataInfo.getString("pending");
                        approved_leave_count = todayAttDataInfo.getString("approved")
                                .equals("null") ? "0" : todayAttDataInfo.getString("approved");
                        rej_leave_count = todayAttDataInfo.getString("rejected")
                                .equals("null") ? "0" : todayAttDataInfo.getString("rejected");
                    }
                }
                requestQueue.add(upLeaveReq);
            } catch (JSONException e) {
                logger.log(Level.WARNING, e.getMessage(), e);
                connected = false;
                updateLeaveGraph();
            }
        }, error -> {
            logger.log(Level.WARNING, error.getMessage(), error);
            conn = false;
            connected = false;
            updateLeaveGraph();
        });

        StringRequest leaveDataReq = new StringRequest(Request.Method.GET, leaveDataUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject leaveInfo = array.getJSONObject(i);
//                        String lbem_emp_id = leaveInfo.getString("lbem_emp_id");
                        String lc_short_code = leaveInfo.getString("lc_short_code");
                        String balance_all = leaveInfo.getString("balance");
                        String quantity = leaveInfo.getString("quantity");
                        if (!quantity.equals("null") && !quantity.equals("NULL")) {
                            if (!quantity.equals("0")) {
                                balance.add(new BarEntry(i, Float.parseFloat(balance_all), i));
                                earn.add(new BarEntry(i, Float.parseFloat(quantity), i));
                                shortCode.add(lc_short_code);
                            }
                        }
                    }
                }
                requestQueue.add(leaveCountReq);
            } catch (JSONException e) {
                connected = false;
                logger.log(Level.WARNING, e.getMessage(), e);
                updateLeaveGraph();
            }
        }, error -> {
            conn = false;
            connected = false;
            logger.log(Level.WARNING, error.getMessage(), error);
            updateLeaveGraph();
        });

        requestQueue.add(leaveDataReq);
    }

    public void updateLeaveGraph() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                if (balance.isEmpty() || earn.isEmpty() || shortCode.isEmpty()) {
                    // do nothing
                    System.out.println("NO DATA FOUND");
                } else {
                    XAxis xAxis = leaveChart.getXAxis();
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setDrawGridLines(false);
                    xAxis.setCenterAxisLabels(true);
                    xAxis.setAxisMinimum(0);
                    xAxis.setAxisMaximum(balance.size());
                    xAxis.setGranularity(1);


                    BarDataSet set1 = new BarDataSet(earn, "Earned Leave");
                    set1.setColor(ColorTemplate.VORDIPLOM_COLORS[3]);
                    set1.setValueFormatter(new ValueFormatter() {
                        @Override
                        public String getFormattedValue(float value) {
                            return String.valueOf((int) Math.floor(value));
                        }
                    });
                    BarDataSet set2 = new BarDataSet(balance, "Leave Balance");
                    set2.setValueFormatter(new ValueFormatter() {
                        @Override
                        public String getFormattedValue(float value) {
                            return String.valueOf((int) Math.floor(value));
                        }
                    });
                    set2.setColor(ColorTemplate.VORDIPLOM_COLORS[2]);

                    float groupSpace = 0.04f;
                    float barSpace = 0.02f; // x2 dataset
                    float barWidth = 0.46f;

                    BarData leavedata = new BarData(set1, set2);
                    if (earn.size() > 10) {
                        leavedata.setValueTextSize(8);
                    }
                    else if (earn.size() > 5) {
                        leavedata.setValueTextSize(11);
                    }
                    else {
                        leavedata.setValueTextSize(12);
                    }
                    leavedata.setBarWidth(barWidth); // set the width of each bar
                    leaveChart.animateY(1000);
                    leaveChart.setData(leavedata);
                    leaveChart.groupBars(0, groupSpace, barSpace); // perform the "explicit" grouping
                    leaveChart.invalidate();

                    xAxis.setValueFormatter(new ValueFormatter() {
                        @Override
                        public String getAxisLabel(float value, AxisBase axis) {
                            if (value < 0 || value >= shortCode.size()) {
                                return null;
                            } else {
                                System.out.println(value);
                                System.out.println(axis);
                                System.out.println(shortCode.get((int) value));
                                return (shortCode.get((int) value));
                            }

                        }
                    });
                }

                pendingLeaveCount.setText(pending_leave_count);
                approveLeaveCount.setText(approved_leave_count);
                rejectedLeaveCount.setText(rej_leave_count);

                if (upcoming_leave_type.isEmpty()) {
                    upcomingLeaveCardView.setVisibility(View.GONE);
                } else {
                    upcomingLeaveCardView.setVisibility(View.VISIBLE);
                    upcomingLeaveTypeName.setText(upcoming_leave_type);
                    if (upcoming_leave_from_date.toUpperCase(Locale.ENGLISH).equals(upcoming_leave_to_date.toUpperCase(Locale.ENGLISH))) {
                        upcomingLeaveFromDate.setVisibility(View.GONE);
                        upcomingLeaveMidText.setVisibility(View.GONE);
                        upcomingLeaveToDate.setText(upcoming_leave_to_date);
                    } else {
                        upcomingLeaveFromDate.setVisibility(View.VISIBLE);
                        upcomingLeaveMidText.setVisibility(View.VISIBLE);
                        upcomingLeaveFromDate.setText(upcoming_leave_from_date);
                        upcomingLeaveToDate.setText(upcoming_leave_to_date);
                    }
                }

                if (last_leave_type.isEmpty()) {
                    lastLeaveCardView.setVisibility(View.GONE);
                } else {
                    lastLeaveCardView.setVisibility(View.VISIBLE);
                    lastLeaveTypeName.setText(last_leave_type);
                    if (last_leave_from_date.toUpperCase(Locale.ENGLISH).equals(last_leave_to_date.toUpperCase(Locale.ENGLISH))) {
                        lastLeaveFromDate.setVisibility(View.GONE);
                        lastLeaveMidText.setVisibility(View.GONE);
                        lastLeaveToDate.setText(last_leave_to_date);
                    } else {
                        lastLeaveFromDate.setVisibility(View.VISIBLE);
                        lastLeaveMidText.setVisibility(View.VISIBLE);
                        lastLeaveFromDate.setText(last_leave_from_date);
                        lastLeaveToDate.setText(last_leave_to_date);
                    }
                }

                conn = false;
                connected = false;
            } else {
                AlertDialog dialog = new AlertDialog.Builder(Dashboard.this)
                        .setMessage("There is a network issue in the server. Please Try later")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel", null)
                        .show();

                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    getLeaveGraph();
                    dialog.dismiss();
                });
            }
        } else {
            AlertDialog dialog = new AlertDialog.Builder(Dashboard.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel", null)
                    .show();

            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                getLeaveGraph();
                dialog.dismiss();
            });
        }
    }

//    public void AllGraph() {
//        try {
//            this.connection = createConnection();
//            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();
//
//
//            salaryMonthLists = new ArrayList<>();
//
//            NoOfEmp = new ArrayList<>();
//            absent = "";
//            present = "";
//            leave = "";
//            holiday = "";
//            late = "";
//            early = "";
//            imageFound = false;
//            selectedImage = null;
//
////            beginDate = "01-MAR-22";
////            lastDate = "31-MAR-22";
//            //formattedDate = "30-JUL-21";
//
//            balance = new ArrayList<>();
//            earn = new ArrayList<>();
//            shortCode = new ArrayList<>();
//            Statement stmt = connection.createStatement();
//
//
//
//
//            ResultSet rs=stmt.executeQuery("SELECT TO_CHAR(SM_PMS_MONTH,'MON'),EMP_NAME, NET_SALARY\n" +
//                    "  FROM (SELECT SM.SM_PMS_MONTH,\n" +
//                    "               E.EMP_NAME,\n" +
//                    "               (  (  NVL (SD.SD_ATTD_BONUS_AMT, 0)\n" +
//                    "                   + NVL (SD.SD_GROSS_SAL, 0)\n" +
//                    "                   + (ROUND (\n" +
//                    "                         (NVL (SD.SD_OT_HR, 0) * NVL (SD.SD_OT_RATE, 0))))\n" +
//                    "                   + (  NVL (SD.SD_JOB_HABITATION, 0)\n" +
//                    "                      + NVL (SD.SD_JOB_UTILITIES, 0)\n" +
//                    "                      + NVL (SD.SD_JOB_HOUSE_ALLOWANCE, 0)\n" +
//                    "                      + NVL (SD.SD_JOB_ENTERTAINMENT, 0)\n" +
//                    "                      + NVL (SD.SD_COMMITTED_SALARY, 0)\n" +
//                    "                      + NVL (SD.SD_FIXED_OT_AMT, 0)\n" +
//                    "                      + NVL (SD.SD_FOOD_SUBSIDY_AMT, 0)\n" +
//                    "                      + NVL (SD.SD_FACTORY_ALLOWANCE_AMT, 0)\n" +
//                    "                      + NVL (SD.SD_HOLIDAY_AMT, 0)\n" +
//                    "                      + NVL (SD.SD_REFUND_OTH_PAYMENT_AMT, 0)\n" +
//                    "                      + NVL (SD.SD_PROFIT_SHARE_AMT, 0)\n" +
//                    "                      + NVL (SD.SD_PERFORMANCE_BONUS_AMT, 0)))\n" +
//                    "                - (  NVL (SD.SD_PF, 0)\n" +
//                    "                   + NVL (SD.SD_LWPAY_AMT, 0)\n" +
//                    "                   + NVL (SD.SD_ABSENT_AMT, 0)\n" +
//                    "                   + NVL (SD.SD_ADVANCE_DEDUCT, 0)\n" +
//                    "                   + NVL (SD.SD_SCH_ADVANCE_DEDUCT, 0)\n" +
//                    "                   + NVL (SD.SD_PF_LOAN_DEDUCT, 0)\n" +
//                    "                   + NVL (SD.SD_TAX, 0)\n" +
//                    "                   + NVL (SD.SD_LUNCH_DEDUCT, 0)\n" +
//                    "                   + NVL (SD.SD_STAMP, 0)\n" +
//                    "                   + NVL (SD.SD_OTH_DEDUCT, 0)\n" +
//                    "                   + NVL (SD.SD_MD_ADVANCE_DEDUCT, 0)))\n" +
//                    "                  NET_SALARY\n" +
//                    "          FROM SALARY_DTL    SD,\n" +
//                    "               SALARY_MST    SM,\n" +
//                    "               EMP_MST       E,\n" +
//                    "               (SELECT *\n" +
//                    "                  FROM EMP_JOB_HISTORY\n" +
//                    "                 WHERE JOB_ID IN (  SELECT MAX (JOB_ID)\n" +
//                    "                                      FROM EMP_JOB_HISTORY\n" +
//                    "                                  GROUP BY JOB_EMP_ID)) EJH,\n" +
//                    "               JOB_SETUP_MST JSM,\n" +
//                    "               JOB_SETUP_DTL JSD,\n" +
//                    "               DIVISION_MST  DM,\n" +
//                    "               DEPT_MST      DPT\n" +
//                    "         WHERE     SM.SM_ID = SD.SD_SM_ID\n" +
//                    "               AND SD.SD_EMP_ID = E.EMP_ID\n" +
//                    "               AND E.EMP_ID = EJH.JOB_EMP_ID\n" +
//                    "               AND EJH.JOB_JSD_ID = JSD.JSD_ID\n" +
//                    "               AND JSD.JSD_JSM_ID = JSM.JSM_ID\n" +
//                    "               AND JSM.JSM_DIVM_ID = DM.DIVM_ID\n" +
//                    "               AND JSM.JSM_DEPT_ID = DPT.DEPT_ID\n" +
//                    "               AND SD.SD_EMP_ID = "+emp_id+"\n" +
//                    "               AND SM.SM_PMS_MONTH <=\n" +
//                    "                      TRUNC (ADD_MONTHS ( (LAST_DAY ('"+formattedDate+"') + 1), -1))\n" +
//                    "order by SM.SM_PMS_MONTH desc)\n" +
//                    " WHERE ROWNUM <= 6");
//
//
//            int i = 0;
//            while(rs.next()) {
//
//                salaryMonthLists.add(new SalaryMonthList(rs.getString(1),rs.getString(3)));
//
//            }
//
//            ResultSet rs1=stmt.executeQuery("SELECT ATTD_HOLIDAY_COUNT ("+emp_id+", '"+ lastDate +"', 'A') ABSENT,\n" +
//                    "       (  ATTD_HOLIDAY_COUNT ("+emp_id+", '"+ lastDate +"', 'P')\n" +
//                    "        + ATTD_HOLIDAY_COUNT ("+emp_id+", '"+ lastDate +"', 'PW')\n" +
//                    "        + ATTD_HOLIDAY_COUNT ("+emp_id+", '"+ lastDate +"', 'PH')\n" +
//                    "        + ATTD_HOLIDAY_COUNT ("+emp_id+", '"+ lastDate +"', 'PL')\n" +
//                    "        + ATTD_HOLIDAY_COUNT ("+emp_id+", '"+ lastDate +"', 'PLH')\n" +
//                    "        + ATTD_HOLIDAY_COUNT ("+emp_id+", '"+ lastDate +"', 'PA')\n" +
//                    "        + ATTD_HOLIDAY_COUNT ("+emp_id+", '"+ lastDate +"', 'PLW'))\n" +
//                    "          PRESENT,\n" +
//                    "       (  ATTD_HOLIDAY_COUNT ("+emp_id+", '"+ lastDate +"', 'L')\n" +
//                    "        + ATTD_HOLIDAY_COUNT ("+emp_id+", '"+ lastDate +"', 'LW')\n" +
//                    "        + ATTD_HOLIDAY_COUNT ("+emp_id+", '"+ lastDate +"', 'LH'))\n" +
//                    "          LEAVE,\n" +
//                    "       ATTD_HOLIDAY_COUNT ("+emp_id+", '"+ lastDate +"', 'H') +\n" +
//                    "       ATTD_HOLIDAY_COUNT ("+emp_id+", '"+ lastDate +"', 'W') HOLIDAY_WEEKEND\n" +
//                    "  FROM DUAL");
//
//
//            while(rs1.next()) {
//
//                absent = rs1.getString(1);
//                present = rs1.getString(2);
//                leave = rs1.getString(3);
//                holiday = rs1.getString(4);
//
//
//            }
//
//            ResultSet resultSet = stmt.executeQuery("SELECT LATE_COUNT_NEW\n" +
//                    "("+emp_id+",\n" +
//                    " '"+beginDate+"',\n" +
//                    " '"+ lastDate +"')\n" +
//                    "  FROM DUAL");
//
//            while (resultSet.next()) {
//                late = resultSet.getString(1);
//            }
//
//            ResultSet resultSet1 = stmt.executeQuery("  SELECT GET_EARLY_COUNT \n" +
//                    "("+emp_id+",\n" +
//                    " '"+beginDate+"',\n" +
//                    " '"+ lastDate +"')\n" +
//                    "  FROM DUAL");
//
//            while (resultSet1.next()) {
//                early = resultSet1.getString(1);
//            }
//
////            ResultSet rs2=stmt.executeQuery("SELECT EM.LBEM_EMP_ID, lc.lc_short_code, LD.LBD_BALANCE_QTY,LD.LBD_CURRENT_QTY\n" +
////                    "FROM LEAVE_BALANCE_EMP_MST  EM,\n" +
////                    "LEAVE_BALANCE_YEAR_DTL YD,\n" +
////                    "LEAVE_BALANCE_DTL      LD,\n" +
////                    "leave_category lc\n" +
////                    "WHERE EM.LBEM_ID = YD.LBYD_LBEM_ID\n" +
////                    "AND YD.LBYD_ID = LD.LBD_LBYD_ID\n" +
////                    "and ld.lbd_lc_id = lc.lc_id\n" +
////                    "AND EM.LBEM_EMP_ID = "+emp_id+"\n" +
////                    "AND TO_CHAR (YD.LBYD_YEAR, 'YYYY') = TO_CHAR (TO_DATE('"+ leaveDate +"'), 'YYYY')");
//            ResultSet rs2 = stmt.executeQuery("SELECT EM.LBEM_EMP_ID, lc.lc_short_code, case when lc.lc_short_code = 'LP' then 0 else get_leave_balance(EM.LBEM_EMP_ID,'"+leaveDate+"', lc.lc_short_code) end balance,NVL(LD.LBD_CURRENT_QTY,0) + NVL(ld.lbd_opening_qty,0)\n" +
//                    "FROM LEAVE_BALANCE_EMP_MST  EM,\n" +
//                    "LEAVE_BALANCE_YEAR_DTL YD,\n" +
//                    "LEAVE_BALANCE_DTL      LD,\n" +
//                    "leave_category lc\n" +
//                    "WHERE EM.LBEM_ID = YD.LBYD_LBEM_ID\n" +
//                    "AND YD.LBYD_ID = LD.LBD_LBYD_ID\n" +
//                    "and ld.lbd_lc_id = lc.lc_id\n" +
//                    "AND EM.LBEM_EMP_ID = "+emp_id+"\n" +
//                    "AND TO_CHAR (YD.LBYD_YEAR, 'YYYY') = TO_CHAR (TO_DATE('"+ leaveDate +"'), 'YYYY')");
//
//
//            int j = 0;
//            while(rs2.next()) {
//
//
//                String data = rs2.getString(4);
//                if (data != null) {
//                    if (!data.equals("0")) {
//                        balance.add(new BarEntry(j, Float.parseFloat(rs2.getString(3)),j));
//                        earn.add(new BarEntry(j, Float.parseFloat(rs2.getString(4)),j));
//                        shortCode.add(rs2.getString(2));
//                        j++;
//                    }
//                }
//
//
//            }
//
//            ResultSet resultSet2 = stmt.executeQuery("SELECT\n" +
//                    "    emp_mst.emp_timeline_tracker_flag\n" +
//                    "\n" +
//                    "FROM\n" +
//                    "         emp_mst\n" +
//                    "\n" +
//                    "WHERE\n" +
//                    "    emp_mst.emp_id = "+emp_id+"");
//            while (resultSet2.next()) {
//                trackerAvailable = resultSet2.getInt(1);
//            }
//
//            if (loginLog_check) {
//                String userName = userInfoLists.get(0).getUserName();
//                String userId = userInfoLists.get(0).getEmp_id();
//
//                if (userId != null) {
//                    if (!userId.isEmpty()) {
//                        System.out.println(userId);
//                    } else {
//                        userId = "0";
//                    }
//                } else {
//                    userId = "0";
//                }
//
//                sessionId = "";
//
//                ResultSet resultSet3 = stmt.executeQuery("SELECT SYS_CONTEXT ('USERENV', 'SESSIONID') --INTO P_IULL_SESSION_ID\n" +
//                        "   FROM DUAL\n");
//
//                while (resultSet3.next()) {
//                    System.out.println("SESSION ID: "+ resultSet3.getString(1));
//                    sessionId = resultSet3.getString(1);
//                }
//
//                resultSet3.close();
//
//                CallableStatement callableStatement1 = connection.prepareCall("{call USERLOGINDTL(?,?,?,?,?,?,?,?,?)}");
//                callableStatement1.setString(1,userName);
//                callableStatement1.setString(2, brand+" "+model);
//                callableStatement1.setString(3,ipAddress);
//                callableStatement1.setString(4,hostUserName);
//                callableStatement1.setInt(5,Integer.parseInt(userId));
//                callableStatement1.setInt(6,Integer.parseInt(sessionId));
//                callableStatement1.setString(7,"1");
//                callableStatement1.setString(8,osName);
//                callableStatement1.setInt(9,3);
//                callableStatement1.execute();
//
//                callableStatement1.close();
//            }
//
//            ResultSet imageResult = stmt.executeQuery("SELECT EMP_IMAGE FROM EMP_MST WHERE EMP_ID = "+emp_id+"");
//
//            while (imageResult.next()) {
//                Blob b = imageResult.getBlob(1);
//                if (b == null) {
//                    imageFound = false;
//                }
//                else {
//                    imageFound = true;
//                    byte[] barr =b.getBytes(1,(int)b.length());
//                    selectedImage = BitmapFactory.decodeByteArray(barr,0,barr.length);
//                }
//            }
//            imageResult.close();
//            stmt.close();
//
//            connected = true;
//
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

    public static String calculateWorkingHours(String attendanceIn, String attendanceOut, String officeEnd) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());

        try {
            Date inTime = timeFormat.parse(attendanceIn);
            Date outTime = null;

            if (attendanceOut != null && !attendanceOut.isEmpty()) {
                outTime = timeFormat.parse(attendanceOut);
            } else {
                // Get current time and compare with office end time
                Date now = Calendar.getInstance().getTime();
                Date currentTime = timeFormat.parse(timeFormat.format(now)); // Strip seconds
                Date officeEndTime = timeFormat.parse(officeEnd);

                // Choose the earlier one between current time and office end time
                if (currentTime != null) {
                    outTime = currentTime.before(officeEndTime) ? currentTime : officeEndTime;
                }
            }

            if (inTime == null || outTime == null) return "Invalid time";

            long diffMillis = outTime.getTime() - inTime.getTime();
            if (diffMillis < 0) return "Invalid time range";

            long hours = diffMillis / (1000 * 60 * 60);
            long minutes = (diffMillis / (1000 * 60)) % 60;

            return String.format(Locale.getDefault(), "%02d:%02d Hours", hours, minutes);

        } catch (ParseException e) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.WARNING, e.getMessage(), e);
            return "Error parsing time";
        }
    }

    public void getAllData() {
        waitProgress.show(getSupportFragmentManager(), "WaitBar");
        waitProgress.setCancelable(false);
        connected = false;
        conn = false;

        salaryMonthLists = new ArrayList<>();
        attendanceEntry = new ArrayList<>();
        absent = "";
        present = "";
        leave = "";
        holiday = "";
        late = "";
        early = "";
        imageFound = false;
        selectedImage = null;
        checkEmpFlag = false;

        balance = new ArrayList<>();
        earn = new ArrayList<>();
        shortCode = new ArrayList<>();
        geoLocationLists = new ArrayList<>();

        in_time_dash = "";
        out_time_dash = "";
        end_time_dash = "";
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat dfffff = new SimpleDateFormat("dd-MMMM-yyyy", Locale.ENGLISH);
        SimpleDateFormat yearFrm = new SimpleDateFormat("yyyy", Locale.ENGLISH);
        String lastDateForAttBot = dfffff.format(c);
        String year = yearFrm.format(c);

        pending_leave_count = "0";
        approved_leave_count = "0";
        rej_leave_count = "0";

        upcoming_leave_type = "";
        upcoming_leave_from_date = "";
        upcoming_leave_to_date = "";

        last_leave_type = "";
        last_leave_from_date = "";
        last_leave_to_date = "";

        BarData leavedata = new BarData();
        leaveChart.setData(leavedata);
        leaveChart.getData().clearValues();
        leaveChart.notifyDataSetChanged();
        leaveChart.clear();
        leaveChart.invalidate();

        BarData salaryData = new BarData();
        salaryChart.setData(salaryData);
        salaryChart.getData().clearValues();
        salaryChart.notifyDataSetChanged();
        salaryChart.clear();
        salaryChart.invalidate();

        String empFlagUrl = api_url_front + "dashboard/checkEmpUpdated/" + emp_id;
        String salaryDataUrl = api_url_front + "dashboard/getSalaryAndMonth/" + emp_id + "/" + formattedDate;
        String attendDataUrl = api_url_front + "dashboard/getAttendanceData/" + beginDate + "/" + lastDate + "/" + emp_id;
        String leaveDataUrl = api_url_front + "dashboard/getLeaveData/" + emp_id + "/" + leaveDate;
        String trackerFlagUrl = api_url_front + "utility/getTrackerFlag/" + emp_id;
        String loginLogUrl = api_url_front + "dashboard/loginLog";
//        String userImageUrl = api_url_front + "utility/getUserImage/" + emp_code;
        String userImageUrl = api_url_front + "utility/getProfilePic?p_emp_id=" + emp_id;
        String offLocationUrl = api_url_front + "attendance/getOffLatLong/" + emp_id;
        String todayAttDataUrl = api_url_front + "attendance/getTodayAttData/" + emp_id + "/" + lastDateForAttBot;
        String leaveCountUrl = api_url_front + "dashboard/getLeaveAppStatusCount?emp_id=" + emp_id + "&start_date=01-JAN-" + year + "&end_date=31-DEC-" + year;
        String upLeaveUrl = api_url_front + "leave/getUpcomingLeave?emp_id=" + emp_id;
        String lastLeaveUrl = api_url_front + "leave/getLastConsumedLeave?emp_id=" + emp_id;

        RequestQueue requestQueue = Volley.newRequestQueue(Dashboard.this);

        StringRequest lastLeaveReq = new StringRequest(Request.Method.GET, lastLeaveUrl, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    JSONObject info = array.getJSONObject(0);
                    last_leave_from_date = info.getString("from_date")
                            .equals("null") ? "" : info.getString("from_date");
                    last_leave_to_date = info.getString("to_date")
                            .equals("null") ? "" : info.getString("to_date");
                    last_leave_type = info.getString("lc_name")
                            .equals("null") ? "" : info.getString("lc_name");
                }
                connected = true;
                updateInterface();
            } catch (JSONException e) {
                logger.log(Level.WARNING, e.getMessage(), e);
                connected = false;
                updateInterface();
            }
        }, error -> {
            logger.log(Level.WARNING, error.getMessage(), error);
            conn = false;
            connected = false;
            updateInterface();
        });

        StringRequest upLeaveReq = new StringRequest(Request.Method.GET, upLeaveUrl, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    JSONObject info = array.getJSONObject(0);
                    upcoming_leave_from_date = info.getString("from_date")
                            .equals("null") ? "" : info.getString("from_date");
                    upcoming_leave_to_date = info.getString("to_date")
                            .equals("null") ? "" : info.getString("to_date");
                    upcoming_leave_type = info.getString("lc_name")
                            .equals("null") ? "" : info.getString("lc_name");
                }
                requestQueue.add(lastLeaveReq);
            } catch (JSONException e) {
                logger.log(Level.WARNING, e.getMessage(), e);
                connected = false;
                updateInterface();
            }
        }, error -> {
            logger.log(Level.WARNING, error.getMessage(), error);
            conn = false;
            connected = false;
            updateInterface();
        });

        StringRequest leaveCountReq = new StringRequest(Request.Method.GET, leaveCountUrl, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject todayAttDataInfo = array.getJSONObject(i);
                        pending_leave_count = todayAttDataInfo.getString("pending")
                                .equals("null") ? "0" : todayAttDataInfo.getString("pending");
                        approved_leave_count = todayAttDataInfo.getString("approved")
                                .equals("null") ? "0" : todayAttDataInfo.getString("approved");
                        rej_leave_count = todayAttDataInfo.getString("rejected")
                                .equals("null") ? "0" : todayAttDataInfo.getString("rejected");
                    }
                }
                requestQueue.add(upLeaveReq);
            } catch (JSONException e) {
                logger.log(Level.WARNING, e.getMessage(), e);
                connected = false;
                updateInterface();
            }
        }, error -> {
            logger.log(Level.WARNING, error.getMessage(), error);
            conn = false;
            connected = false;
            updateInterface();
        });

        StringRequest todayAttReq = new StringRequest(Request.Method.GET, todayAttDataUrl, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject todayAttDataInfo = array.getJSONObject(i);
                        in_time_dash = todayAttDataInfo.getString("dac_in_date_time")
                                .equals("null") ? "" : todayAttDataInfo.getString("dac_in_date_time");
                        out_time_dash = todayAttDataInfo.getString("dac_out_date_time")
                                .equals("null") ? "" : todayAttDataInfo.getString("dac_out_date_time");
                        end_time_dash = todayAttDataInfo.getString("dac_end_time")
                                .equals("null") ? "" : todayAttDataInfo.getString("dac_end_time");
                    }
                }
                requestQueue.add(leaveCountReq);
            } catch (JSONException e) {
                logger.log(Level.WARNING, e.getMessage(), e);
                connected = false;
                updateInterface();
            }
        }, error -> {
            logger.log(Level.WARNING, error.getMessage(), error);
            conn = false;
            connected = false;
            updateInterface();
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
                        String lat = offLocInfo.getString("coa_latitude").equals("null") ? "" : offLocInfo.getString("coa_latitude");
                        String lng = offLocInfo.getString("coa_longitude").equals("null") ? "" : offLocInfo.getString("coa_longitude");
                        String cover = offLocInfo.getString("coa_coverage").equals("null") ? "0" : offLocInfo.getString("coa_coverage");
                        String coa_id = offLocInfo.getString("coa_id").equals("null") ? "" : offLocInfo.getString("coa_id");

                        geoLocationLists.add(new GeoLocationList("", lat, lng, cover, coa_id));
                    }
                }
                requestQueue.add(todayAttReq);

            } catch (JSONException e) {
                logger.log(Level.WARNING, e.getMessage(), e);
                connected = false;
                updateInterface();
            }
        }, error -> {
            logger.log(Level.WARNING, error.getMessage(), error);
            conn = false;
            connected = false;
            updateInterface();
        });

        StringRequest imageReq = new StringRequest(Request.Method.GET, userImageUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject userImageInfo = array.getJSONObject(i);
                        String emp_image = userImageInfo.getString("emp_image");
                        if (emp_image.equals("null") || emp_image.isEmpty()) {
                            System.out.println("NULL IMAGE");
                            imageFound = false;
                        } else {
                            byte[] decodedString = Base64.decode(emp_image, Base64.DEFAULT);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                            if (bitmap != null) {
                                imageFound = true;
                                selectedImage = bitmap;
                            } else {
                                imageFound = false;
                            }
                        }
                    }
                }
                requestQueue.add(offLocReq);
            } catch (JSONException e) {
                connected = false;
                logger.log(Level.WARNING, e.getMessage(), e);
                updateInterface();
            }
        }, error -> {
            conn = false;
            connected = false;
            logger.log(Level.WARNING, error.getMessage(), error);
            updateInterface();
        });

        StringRequest loginLogReq = new StringRequest(Request.Method.POST, loginLogUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String string_out = jsonObject.getString("string_out");
                if (string_out.equals("Successfully Created")) {
                    requestQueue.add(imageReq);
                } else {
                    connected = false;
                    updateInterface();
                }

            } catch (JSONException e) {
                logger.log(Level.WARNING, e.getMessage(), e);
                connected = false;
                updateInterface();
            }
        }, error -> {
            conn = false;
            connected = false;
            logger.log(Level.WARNING, error.getMessage(), error);
            updateInterface();
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String userName = userInfoLists.get(0).getUserName();
                String userId = userInfoLists.get(0).getEmp_id();
                if (userId != null) {
                    if (!userId.isEmpty()) {
                        System.out.println(userId);
                    } else {
                        userId = "0";
                    }
                } else {
                    userId = "0";
                }
                headers.put("P_IULL_USER_ID", userName);
                headers.put("P_IULL_CLIENT_HOST_NAME", brand + " " + model);
                headers.put("P_IULL_CLIENT_IP_ADDR", ipAddress);
                headers.put("P_IULL_CLIENT_HOST_USER_NAME", hostUserName);
                headers.put("P_IULL_SESSION_USER_ID", userId);
                headers.put("P_IULL_OS_NAME", osName);
                headers.put("P_LOG_TYPE", "3");
                return headers;
            }
        };

        StringRequest trackerFlagReq = new StringRequest(Request.Method.GET, trackerFlagUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject trackerInfo = array.getJSONObject(i);
                        trackerAvailable =
                                Integer.parseInt(trackerInfo.getString("emp_timeline_tracker_flag")
                                        .equals("null") ? "0" : trackerInfo.getString("emp_timeline_tracker_flag"));
                    }
                }
                if (loginLog_check) {
                    requestQueue.add(loginLogReq);
                } else {
                    requestQueue.add(imageReq);
                }
            } catch (JSONException e) {
                connected = false;
                logger.log(Level.WARNING, e.getMessage(), e);
                updateInterface();
            }
        }, error -> {
            conn = false;
            connected = false;
            logger.log(Level.WARNING, error.getMessage(), error);
            updateInterface();
        });

        StringRequest leaveDataReq = new StringRequest(Request.Method.GET, leaveDataUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject leaveInfo = array.getJSONObject(i);
//                        String lbem_emp_id = leaveInfo.getString("lbem_emp_id");
                        String lc_short_code = leaveInfo.getString("lc_short_code");
                        String balance_all = leaveInfo.getString("balance");
                        String quantity = leaveInfo.getString("quantity");
                        if (!quantity.equals("null") && !quantity.equals("NULL")) {
                            if (!quantity.equals("0")) {
                                balance.add(new BarEntry(i, Float.parseFloat(balance_all), i));
                                earn.add(new BarEntry(i, Float.parseFloat(quantity), i));
                                shortCode.add(lc_short_code);
                            }
                        }
                    }
                }
                requestQueue.add(trackerFlagReq);

            } catch (JSONException e) {
                connected = false;
                logger.log(Level.WARNING, e.getMessage(), e);
                updateInterface();
            }
        }, error -> {
            conn = false;
            connected = false;
            logger.log(Level.WARNING, error.getMessage(), error);
            updateInterface();
        });

        StringRequest attendDataReq = new StringRequest(Request.Method.GET, attendDataUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject attendanceInfo = array.getJSONObject(i);
                        absent = attendanceInfo.getString("absent");
                        present = attendanceInfo.getString("present");
                        leave = attendanceInfo.getString("leave");
                        holiday = attendanceInfo.getString("holiday_weekend");
                        late = attendanceInfo.getString("late_count");
                        early = attendanceInfo.getString("early_count");
                    }
                    requestQueue.add(leaveDataReq);
                } else {
                    connected = false;
                    updateInterface();
                }
            } catch (JSONException e) {
                connected = false;
                logger.log(Level.WARNING, e.getMessage(), e);
                updateInterface();
            }
        }, error -> {
            conn = false;
            connected = false;
            logger.log(Level.WARNING, error.getMessage(), error);
            updateInterface();
        });

        StringRequest salaryMonthReq = new StringRequest(Request.Method.GET, salaryDataUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject salaryMonthInfo = array.getJSONObject(i);
                        String mon_name = salaryMonthInfo.getString("mon_name");
                        String net_salary = salaryMonthInfo.getString("net_salary");
                        salaryMonthLists.add(new SalaryMonthList(mon_name, net_salary));
                    }
                }
                requestQueue.add(attendDataReq);
            } catch (JSONException e) {
                connected = false;
                logger.log(Level.WARNING, e.getMessage(), e);
                updateInterface();
            }
        }, error -> {
            conn = false;
            connected = false;
            logger.log(Level.WARNING, error.getMessage(), error);
            updateInterface();
        });

        StringRequest empFlagCheckReq = new StringRequest(Request.Method.GET, empFlagUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject info = array.getJSONObject(i);

                        String emp_flag = info.getString("emp_flag")
                                .equals("null") ? "" : info.getString("emp_flag");

                        System.out.println(emp_flag);
                        checkEmpFlag = emp_flag.equals("0");
                    }
                }
                System.out.println(checkEmpFlag);
                if (checkEmpFlag) {
                    requestQueue.add(salaryMonthReq);
                } else {
                    connected = true;
                    updateInterface();
                }
            } catch (JSONException e) {
                connected = false;
                logger.log(Level.WARNING, e.getMessage(), e);
                updateInterface();
            }
        }, error -> {
            conn = false;
            connected = false;
            logger.log(Level.WARNING, error.getMessage(), error);
            updateInterface();
        });

        requestQueue.add(empFlagCheckReq);
    }

    public void updateInterface() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                if (checkEmpFlag) {

                    // Pie Chart for Attendance
                    final int[] piecolors = new int[]{
                            Color.rgb(116, 185, 255),
                            Color.rgb(85, 239, 196),
                            Color.rgb(162, 155, 254),
                            Color.rgb(223, 230, 233),
                            Color.rgb(255, 234, 167),

                            Color.rgb(250, 177, 160),
                            Color.rgb(129, 236, 236),
                            Color.rgb(255, 118, 117),
                            Color.rgb(253, 121, 168),
                            Color.rgb(96, 163, 188)};


                    if (absent != null) {
                        if (!absent.isEmpty()) {
                            if (!absent.equals("0")) {
                                attendanceEntry.add(new PieEntry(Float.parseFloat(absent), "Absent", 0));
                            }
                        }
                    }

                    if (present != null) {
                        if (!present.isEmpty()) {
                            if (!present.equals("0")) {
                                attendanceEntry.add(new PieEntry(Float.parseFloat(present), "Present", 1));
                            }
                        }
                    }

                    if (late != null) {
                        if (!late.isEmpty()) {
                            if (!late.equals("0")) {
                                attendanceEntry.add(new PieEntry(Float.parseFloat(late), "Late", 2));

                            }
                        }
                    }

                    if (early != null) {
                        if (!early.isEmpty()) {
                            if (!early.equals("0")) {
                                attendanceEntry.add(new PieEntry(Float.parseFloat(early), "Early", 3));
                            }
                        }
                    }

                    if (leave != null) {
                        if (!leave.isEmpty()) {
                            if (!leave.equals("0")) {
                                attendanceEntry.add(new PieEntry(Float.parseFloat(leave), "Leave", 4));
                            }
                        }
                    }

                    if (holiday != null) {
                        if (!holiday.isEmpty()) {
                            if (!holiday.equals("0")) {
                                attendanceEntry.add(new PieEntry(Float.parseFloat(holiday), "Holiday/Weekend", 5));
                            }
                        }
                    }

                    if (attendanceEntry.isEmpty()) {
                        attendanceEntry.add(new PieEntry(1, "No Data Found", 6));
                    }

                    PieDataSet dataSet = new PieDataSet(attendanceEntry, "");
                    pieChart.animateXY(1000, 1000);
                    pieChart.setEntryLabelColor(Color.TRANSPARENT);

                    PieData data = new PieData(dataSet);
                    dataSet.setValueFormatter(new ValueFormatter() {
                        @Override
                        public String getFormattedValue(float value) {
                            return String.valueOf((int) Math.floor(value));
                        }
                    });
                    String label = dataSet.getValues().get(0).getLabel();
                    System.out.println(label);
                    if (label.equals("No Data Found")) {
                        dataSet.setValueTextColor(Color.TRANSPARENT);
                    } else {
                        dataSet.setValueTextColor(Color.BLACK);
                    }
                    dataSet.setHighlightEnabled(true);
                    dataSet.setValueTextSize(12);

                    int[] num = new int[attendanceEntry.size()];
                    for (int i = 0; i < attendanceEntry.size(); i++) {
                        int neki = (int) attendanceEntry.get(i).getData();
                        num[i] = piecolors[neki];
                    }

                    dataSet.setColors(ColorTemplate.createColors(num));

                    pieChart.setData(data);
                    pieChart.invalidate();

                    inTimeDashboard.setText(in_time_dash);
                    outTimeDashboard.setText(out_time_dash);
                    if (!in_time_dash.isEmpty()) {
                        String work_hours = calculateWorkingHours(in_time_dash, out_time_dash, end_time_dash);
                        workHoursDashboard.setText(work_hours);
                    }


                    // Leave Multi Bar
                    if (!balance.isEmpty() && !earn.isEmpty() && !shortCode.isEmpty()) {
                        XAxis xAxis = leaveChart.getXAxis();
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                        xAxis.setDrawGridLines(false);
                        xAxis.setCenterAxisLabels(true);
                        xAxis.setAxisMinimum(0);
                        xAxis.setAxisMaximum(balance.size());
                        xAxis.setGranularity(1);


                        BarDataSet set1 = new BarDataSet(earn, "Earned Leave");
                        set1.setColor(ColorTemplate.VORDIPLOM_COLORS[3]);
                        set1.setValueFormatter(new ValueFormatter() {
                            @Override
                            public String getFormattedValue(float value) {
                                return String.valueOf((int) Math.floor(value));
                            }
                        });
                        BarDataSet set2 = new BarDataSet(balance, "Leave Balance");
                        set2.setValueFormatter(new ValueFormatter() {
                            @Override
                            public String getFormattedValue(float value) {
                                return String.valueOf((int) Math.floor(value));
                            }
                        });
                        set2.setColor(ColorTemplate.VORDIPLOM_COLORS[2]);

                        float groupSpace = 0.04f;
                        float barSpace = 0.02f; // x2 dataset
                        float barWidth = 0.46f;

                        BarData leavedata = new BarData(set1, set2);
                        if (earn.size() > 10) {
                            leavedata.setValueTextSize(8);
                        }
                        else if (earn.size() > 5) {
                            leavedata.setValueTextSize(11);
                        }
                        else {
                            leavedata.setValueTextSize(12);
                        }
                        leavedata.setBarWidth(barWidth); // set the width of each bar
                        leaveChart.animateY(1000);
                        leaveChart.setData(leavedata);
                        leaveChart.groupBars(0, groupSpace, barSpace); // perform the "explicit" grouping
                        leaveChart.invalidate();

                        xAxis.setValueFormatter(new ValueFormatter() {
                            @Override
                            public String getAxisLabel(float value, AxisBase axis) {
                                if (value < 0 || value >= shortCode.size()) {
                                    return null;
                                } else {
//                                System.out.println(value);
//                                System.out.println(axis);
//                                System.out.println(shortCode.get((int)value));
                                    return (shortCode.get((int) value));
                                }

                            }
                        });
                    }

                    pendingLeaveCount.setText(pending_leave_count);
                    approveLeaveCount.setText(approved_leave_count);
                    rejectedLeaveCount.setText(rej_leave_count);

                    if (upcoming_leave_type.isEmpty()) {
                        upcomingLeaveCardView.setVisibility(View.GONE);
                    } else {
                        upcomingLeaveCardView.setVisibility(View.VISIBLE);
                        upcomingLeaveTypeName.setText(upcoming_leave_type);
                        if (upcoming_leave_from_date.toUpperCase(Locale.ENGLISH).equals(upcoming_leave_to_date.toUpperCase(Locale.ENGLISH))) {
                            upcomingLeaveFromDate.setVisibility(View.GONE);
                            upcomingLeaveMidText.setVisibility(View.GONE);
                            upcomingLeaveToDate.setText(upcoming_leave_to_date);
                        } else {
                            upcomingLeaveFromDate.setVisibility(View.VISIBLE);
                            upcomingLeaveMidText.setVisibility(View.VISIBLE);
                            upcomingLeaveFromDate.setText(upcoming_leave_from_date);
                            upcomingLeaveToDate.setText(upcoming_leave_to_date);
                        }
                    }

                    if (last_leave_type.isEmpty()) {
                        lastLeaveCardView.setVisibility(View.GONE);
                    } else {
                        lastLeaveCardView.setVisibility(View.VISIBLE);
                        lastLeaveTypeName.setText(last_leave_type);
                        if (last_leave_from_date.toUpperCase(Locale.ENGLISH).equals(last_leave_to_date.toUpperCase(Locale.ENGLISH))) {
                            lastLeaveFromDate.setVisibility(View.GONE);
                            lastLeaveMidText.setVisibility(View.GONE);
                            lastLeaveToDate.setText(last_leave_to_date);
                        } else {
                            lastLeaveFromDate.setVisibility(View.VISIBLE);
                            lastLeaveMidText.setVisibility(View.VISIBLE);
                            lastLeaveFromDate.setText(last_leave_from_date);
                            lastLeaveToDate.setText(last_leave_to_date);
                        }
                    }


                    // PayRoll/Salary Bar Chart
                    monthName = new ArrayList<>();
                    salary = new ArrayList<>();
                    salaryValue = new ArrayList<>();

                    for (int i = 0; i < salaryMonthLists.size(); i++) {
                        for (int j = 0; j < months.size(); j++) {
                            if (months.get(j).getMonth().equals(salaryMonthLists.get(i).getMonth())) {
                                months.get(j).setSalary(salaryMonthLists.get(i).getSalary());
                            }
                        }
                    }

                    for (int i = months.size() - 1; i >= 0; i--) {
                        monthName.add(months.get(i).getMonth());
                        salary.add(months.get(i).getSalary());
                    }

                    System.out.println(monthName);
                    System.out.println(salary);

                    for (int i = 0; i < salary.size(); i++) {
                        salaryValue.add(new BarEntry(i, Float.parseFloat(salary.get(i)), i));
                    }

                    BarDataSet bardataset = new BarDataSet(salaryValue, "Months");
                    salaryChart.animateY(1000);

                    BarData data1 = new BarData(bardataset);
                    final int[] barColors = new int[]{
                            Color.rgb(146, 197, 249),
                            Color.rgb(154, 216, 216),
                            Color.rgb(175, 220, 143),
                            Color.rgb(251, 224, 114),
                            Color.rgb(113, 164, 201),
                            Color.rgb(188, 228, 216),

                            Color.rgb(129, 236, 236),
                            Color.rgb(255, 118, 117),
                            Color.rgb(253, 121, 168),
                            Color.rgb(96, 163, 188)};
                    bardataset.setColors(ColorTemplate.createColors(barColors));

                    bardataset.setBarBorderColor(Color.DKGRAY);
                    bardataset.setValueTextSize(11);
                    bardataset.setValueFormatter(new LargeValueFormatter());
                    salaryChart.setData(data1);
                    salaryChart.getXAxis().setValueFormatter(new MyAxisValueFormatter(monthName));
                    salaryChart.getAxisLeft().setValueFormatter(new LargeValueFormatter());

                    // Others
                    createNotificationChannelForWidget();

                    if (trackerAvailable == 1) {
                        sharedSchedule = getSharedPreferences(SCHEDULING_FILE, MODE_PRIVATE);
//                        boolean isNewLogin = sharedSchedule.getBoolean(TRIGGERING,false);

                        // Triggering Schedule
                        System.out.println("SCHEDULING TASK STARTED");
                        SharedPreferences.Editor editor = sharedSchedule.edit();
                        editor.remove(SCHEDULING_EMP_ID);
                        editor.remove(TRIGGERING);

                        editor.putString(SCHEDULING_EMP_ID, emp_id);
                        editor.putBoolean(TRIGGERING, true);
                        editor.apply();
                        editor.commit();
                        createNotificationChannel();

                        Intent intent = new Intent(Dashboard.this, Uploader.class);
                        PendingIntent pendingIntent;
                        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);


                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY, 0);
                        calendar.set(Calendar.MINUTE, 5);
                        calendar.set(Calendar.SECOND, 0);
                        //calendar.set(Calendar.AM_PM, Calendar.PM);

//                    Calendar calendar = Calendar.getInstance();
//                    calendar.setTimeInMillis(System.currentTimeMillis());
//                    calendar.set(Calendar.SECOND, 0);
//                    calendar.set(Calendar.MINUTE, 5);
//                    calendar.set(Calendar.HOUR, 0);
//                    calendar.set(Calendar.AM_PM, Calendar.AM);
//                    calendar.add(Calendar.DAY_OF_MONTH, 1);

                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_HALF_HOUR, pendingIntent);
                    }

                    if (imageFound) {
                        Glide.with(Dashboard.this)
                                .load(selectedImage)
                                .fitCenter()
                                .into(userImage);
                    }
                    else {
                        userImage.setImageResource(R.drawable.profile);
                    }
                    conn = false;
                    connected = false;
                    checkEmpFlag = false;
                    loginLog_check = false;
                }
                else {
                    MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(Dashboard.this)
                            .setTitle("FORCED LOGOUT!")
                            .setMessage("Some Important Data have been changed according to your profile. That's why you have been forced logout from application. To continue please login again.")
                            .setPositiveButton("OK", (dialogInterface, i) -> {
                                userInfoLists.clear();
                                userDesignations.clear();
                                userInfoLists = new ArrayList<>();
                                userDesignations = new ArrayList<>();
                                isApproved = 0;
                                isLeaveApproved = 0;

                                SharedPreferences.Editor widgetEditor = attendanceWidgetPreferences.edit();
                                widgetEditor.remove(WIDGET_EMP_ID);
                                widgetEditor.remove(WIDGET_TRACKER_FLAG);
                                widgetEditor.apply();
                                widgetEditor.commit();

                                SharedPreferences.Editor editor1 = sharedPreferences.edit();
                                editor1.remove(USER_NAME);
                                editor1.remove(USER_F_NAME);
                                editor1.remove(USER_L_NAME);
                                editor1.remove(EMAIL);
                                editor1.remove(CONTACT);
                                editor1.remove(EMP_ID_LOGIN);
                                editor1.remove(EMP_PASSWORD);

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
//                                    editor1.remove(DATABASE_NAME);
                                editor1.apply();
                                editor1.commit();

                                if (trackerAvailable == 1) {
                                    sharedSchedule = getSharedPreferences(SCHEDULING_FILE, MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedSchedule.edit();
                                    editor.remove(SCHEDULING_EMP_ID);
                                    editor.remove(TRIGGERING);
                                    editor.apply();
                                    editor.commit();

                                    Intent intent1 = new Intent(Dashboard.this, Uploader.class);
                                    PendingIntent pendingIntent;
                                    pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent1, PendingIntent.FLAG_IMMUTABLE);
                                    alarmManager.cancel(pendingIntent);
                                }

                                Intent intent = new Intent(Dashboard.this, Login.class);
                                startActivity(intent);
                                finish();
                            });
                    AlertDialog alert = dialogBuilder.create();
                    alert.setCanceledOnTouchOutside(false);
                    alert.setCancelable(false);
                    alert.show();
                }
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(Dashboard.this)
                        .setMessage("There is a network issue in the server. Please Try later")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel", null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    waitProgress.show(getSupportFragmentManager(), "WaitBar");
                    waitProgress.setCancelable(false);
                    getAllData();
                    dialog.dismiss();
                });

                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negative.setOnClickListener(v -> {
                    if (loginLog_check) {
                        userInfoLists.clear();
                        userDesignations.clear();
                        userInfoLists = new ArrayList<>();
                        userDesignations = new ArrayList<>();
                        isApproved = 0;
                        isLeaveApproved = 0;
                        dialog.dismiss();
                        finish();
                    } else {
                        dialog.dismiss();
                    }
                });
            }
        }
        else {
            AlertDialog dialog = new AlertDialog.Builder(Dashboard.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel", null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                waitProgress.show(getSupportFragmentManager(), "WaitBar");
                waitProgress.setCancelable(false);
                getAllData();
                dialog.dismiss();
            });

            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> {
                if (loginLog_check) {
                    userInfoLists.clear();
                    userDesignations.clear();
                    userInfoLists = new ArrayList<>();
                    userDesignations = new ArrayList<>();
                    isApproved = 0;
                    isLeaveApproved = 0;
                    dialog.dismiss();
                    finish();
                } else {
                    dialog.dismiss();
                }
            });
        }
    }

    public void removeGeoFence() {
        json = geoSharedData.getString(GEO_ALL_DATA, "");
        Type type = new TypeToken<ArrayList<GeoLocationList>>() {
        }.getType();
        ArrayList<GeoLocationList> savedLocationLists = gson.fromJson(json, type);

        GeofencingClient geofencingClient = LocationServices.getGeofencingClient(this);

        if (savedLocationLists != null) {
            if (!savedLocationLists.isEmpty()) {

                ArrayList<String> ids = new ArrayList<>();
                for (int i = 0; i < savedLocationLists.size(); i++) {
                    String id = savedLocationLists.get(i).getGeo_id();
                    ids.add(id);
                }
                geofencingClient.removeGeofences(ids)
                        .addOnSuccessListener(unused -> Toast.makeText(getApplicationContext(), "Auto Attendance Stopped Successfully", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> {
                            String errorMessage = getErrorString(e);
                            Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        });
            }
        }

        SharedPreferences.Editor editor1 = geoSharedData.edit();
        editor1.remove(GEO_ALL_DATA);
        editor1.apply();
        editor1.commit();
    }

    public void addGeoFence() {
        GeofencingClient geofencingClient = LocationServices.getGeofencingClient(this);

        ArrayList<Geofence> geofenceList = new ArrayList<>();

        ArrayList<GeoLocationList> newLocationLists = new ArrayList<>();

        for (int i = 0; i < geoLocationLists.size(); i++) {
            String lat = geoLocationLists.get(i).getGeo_lat();
            String lng = geoLocationLists.get(i).getGeo_lng();
            String org_radius = geoLocationLists.get(i).getGeo_radius();
            String radius = geoLocationLists.get(i).getGeo_radius();
            String c_id = geoLocationLists.get(i).getCoa_id();

            LatLng latLng;

            if (!lat.isEmpty() && !lng.isEmpty()) {
                latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                String req_id = "geo_" + emp_id + "_" + c_id + "_" + i;
                if (radius.isEmpty()) {
                    radius = "0";
                } else if (Float.parseFloat(radius) > 100) {
                    float radius_1 = (Float.parseFloat(radius) - 100);
                    radius = String.valueOf(radius_1);
                }
                float rad = Float.parseFloat(radius);

                Geofence geofence = new Geofence.Builder()
                        .setCircularRegion(latLng.latitude, latLng.longitude, rad)
                        .setRequestId(req_id)
                        .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_DWELL | Geofence.GEOFENCE_TRANSITION_EXIT)
                        .setLoiteringDelay(120000)
                        .setExpirationDuration(Geofence.NEVER_EXPIRE)
                        .build();

                geofenceList.add(geofence);
                newLocationLists.add(new GeoLocationList(req_id, lat, lng, org_radius, c_id));
            }
        }

        if (!geofenceList.isEmpty()) {
            GeofencingRequest geofencingRequest;
            try {
                geofencingRequest = new GeofencingRequest.Builder()
                        .addGeofences(geofenceList)
                        .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                        .build();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            Intent intent = new Intent(Dashboard.this, GeofenceBroadcastReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1120, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            geofencingClient.addGeofences(geofencingRequest, pendingIntent)
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(getApplicationContext(), "Auto Attendance Started Successfully", Toast.LENGTH_SHORT).show();
                        System.out.println("HOISE");
                    })
                    .addOnFailureListener(e -> {
                        System.out.println(e.getLocalizedMessage());
                        String errorMsg = getErrorString(e);
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_SHORT).show();
                        System.out.println("HOI NAI");
                    });
        }

        json = gson.toJson(newLocationLists);

        SharedPreferences.Editor editor1 = geoSharedData.edit();
        editor1.remove(GEO_ALL_DATA);
        editor1.putString(GEO_ALL_DATA, json);
        editor1.apply();
        editor1.commit();
    }

    public String getErrorString(Exception e) {
        if (e instanceof ApiException) {
            ApiException apiException = (ApiException) e;
            switch (apiException.getStatusCode()) {
                case GeofenceStatusCodes
                             .GEOFENCE_NOT_AVAILABLE:
                    return "GEOFENCE NOT AVAILABLE";
                case GeofenceStatusCodes
                             .GEOFENCE_TOO_MANY_GEOFENCES:
                    return "TOO MANY GEOFENCES";
                case GeofenceStatusCodes
                             .GEOFENCE_TOO_MANY_PENDING_INTENTS:
                    return "TOO MANY PENDING INTENTS";
                case GeofenceStatusCodes.GEOFENCE_INSUFFICIENT_LOCATION_PERMISSION:
                    return "INSUFFICIENT PERMISSIONS";
            }
        }
        return e.getLocalizedMessage();
    }

    public void attendanceShortcutTriggered() {
        attendanceWidgetPreferences = getSharedPreferences(WIDGET_FILE, MODE_PRIVATE);
        emp_id = attendanceWidgetPreferences.getString(WIDGET_EMP_ID, "");
//        String tracker_flag = attendanceWidgetPreferences.getString(WIDGET_TRACKER_FLAG, "");

        if (!emp_id.isEmpty()) {
//            if (tracker_flag.equals("1")) {
//                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, getString(R.string.att_channel_id))
//                        .setSmallIcon(R.drawable.hrm_new_icon_wb)
//                        .setContentTitle("Attendance System")
//                        .setContentText("Your tracking flag is active. You need to give attendance from the Attendance Module.")
//                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//
//                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
//                notificationManagerCompat.notify(222, builder.build());
//            } else {
//                doWork();
//            }
            doWork();
        } else {
            Toast.makeText(this, "Please Login to Terrain HRM", Toast.LENGTH_SHORT).show();
        }
    }

    public void doWork() {
        waitProgress.show(getSupportFragmentManager(), "WaitBar");
        waitProgress.setCancelable(false);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());

        locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 3000)
                .setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis(1000)
                .setMaxUpdateDelayMillis(1500)
                .build();

        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (gps) {
            startLocationUpdates();
        } else {
            getNotification("Attendance System", "Your GPS is disabled. Please enable it and try again.");
        }
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            getNotification("Attendance System", "Please check your Location Permission to access this feature.");
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    private final LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            for (Location location : locationResult.getLocations()) {
                System.out.println("ADADAD!!!!!A!!!");
                Log.i("LocationFused ", location.toString());
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy, hh:mm:ss aa", Locale.ENGLISH);
                SimpleDateFormat dftoShow = new SimpleDateFormat("hh:mm aa", Locale.ENGLISH);
                lastLatLongitude[0] = new LatLng(location.getLatitude(), location.getLongitude());
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
                stopLocationUpdate();
            }
        }
    };

    private void stopLocationUpdate() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        getOfficeLocation();
    }

    public void getOfficeLocation() {
        areaLists = new ArrayList<>();

        String offLocationUrl = api_url_front + "attendance/getNewOffLatLong?emp_id="+emp_id;

        RequestQueue requestQueue = Volley.newRequestQueue(this);

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
            } catch (JSONException e) {
                logger.log(Level.WARNING, e.getMessage(), e);
                connected = false;
                updateInfo();
            }
        }, error -> {
            logger.log(Level.WARNING, error.getMessage(), error);
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
//                                           to make geofence trigger before entering and exit
//                                           if (radius > 100) {
//                                              radius = radius - 100;
//                                           }
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
                            checkAddress();
                        }
                        else {
                            if (areaLists.get(0).isCanGive()) {
                                if (machineCode.isEmpty()) {
                                    machineCode = areaLists.get(0).getMachine_code();
                                }
                                checkAddress();
                            }
                            else {
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
                        getNotification("Attendance System", "You don't have any permission to give attendance from this app. Please contact with administrator");
                    }
                } else {
                    getNotification("Attendance System", "Failed to get Location");
                }
            } else {
                getNotification("Attendance System", "There is a network issue in the server. Please Try later.");
            }
        } else {
            getNotification("Attendance System", "Please Check Your Internet Connection.");
        }
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
//            int exitValue = ipProcess.waitFor();
//            return (exitValue == 0);
//        } catch (IOException | InterruptedException e) {
//            logger.log(Level.WARNING, e.getMessage(), e);
//        }
//
//        return false;
//    }

    public void checkAddress() {
        new Thread(() -> {
            getAddress(Double.parseDouble(lat),Double.parseDouble(lon));
            runOnUiThread(this::giveAttendance);
        }).start();
    }

//    public class CheckAddress extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            if (isConnected() && isOnline()) {
//                conn = true;
//                getAddress(Double.parseDouble(lat), Double.parseDouble(lon));
//            } else {
//                conn = false;
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
//                giveAttendance();
//            } else {
//                getNotification("Attendance System", "Please Check Your Internet Connection.");
//            }
//
//        }
//    }

    public void getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
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
            logger.log(Level.WARNING, e.getMessage(), e);
            address = "";
        }
    }

    public void giveAttendance() {
        conn = false;
        connected = false;

        String attendaceUrl = api_url_front + "attendance/giveAttendance";

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest attReq = new StringRequest(Request.Method.POST, attendaceUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String string_out = jsonObject.getString("string_out");
                if (string_out.equals("Successfully Created")) {
                    connected = true;
                } else {
                    System.out.println(string_out);
                    connected = false;
                }
                updateLayout();
            } catch (JSONException e) {
                logger.log(Level.WARNING, e.getMessage(), e);
                connected = false;
                updateLayout();
            }
        }, error -> {
            logger.log(Level.WARNING, error.getMessage(), error);
            conn = false;
            connected = false;
            updateLayout();
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("P_EMP_ID", emp_id);
                headers.put("P_PUNCH_TIME", ts.toString());
                headers.put("P_MACHINE_CODE", machineCode);
                headers.put("P_LATITUDE", lat);
                headers.put("P_LONGITUDE", lon);
                headers.put("P_ADDRESS", address);
                return headers;
            }
        };

        requestQueue.add(attReq);
    }

    private void updateLayout() {
        if (conn) {
            if (connected) {
                connected = false;
                conn = false;
                MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(Dashboard.this);
                alertDialogBuilder.setTitle("Attendance!")
                        .setIcon(R.drawable.hrm_new_round_icon_custom)
                        .setMessage("Your Attendance is Recorded at " + timeToShow + ".")
                        .setPositiveButton("OK", (dialog, which) -> dialog.dismiss());

                AlertDialog alert = alertDialogBuilder.create();
                alert.show();

                getNotification("Attendance System", "Your Attendance is Recorded at " + timeToShow + ".");

            } else {
                getNotification("Attendance System", "There is a network issue in the server. Please Try later.");
            }
        } else {
            getNotification("Attendance System", "Please Check Your Internet Connection.");
        }
    }

    @SuppressLint("MissingPermission")
    public void getNotification(String title, String msg) {
        waitProgress.dismiss();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, getString(R.string.att_channel_id))
                .setSmallIcon(R.drawable.hrm_new_icon_wb)
                .setContentTitle(title)
                .setContentText(msg)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(222, builder.build());
    }
}