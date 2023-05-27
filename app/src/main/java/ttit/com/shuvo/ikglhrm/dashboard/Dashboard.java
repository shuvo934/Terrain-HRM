package ttit.com.shuvo.ikglhrm.dashboard;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import ttit.com.shuvo.ikglhrm.EmployeeInfo.EmplyeeInformation;
import ttit.com.shuvo.ikglhrm.Login;
import ttit.com.shuvo.ikglhrm.MainPage.MainMenu;
import ttit.com.shuvo.ikglhrm.R;
import ttit.com.shuvo.ikglhrm.UserDesignation;
import ttit.com.shuvo.ikglhrm.UserInfoList;
import ttit.com.shuvo.ikglhrm.WaitProgress;
import ttit.com.shuvo.ikglhrm.attendance.Attendance;
import ttit.com.shuvo.ikglhrm.attendance.trackService.Service;
import ttit.com.shuvo.ikglhrm.directoryBook.Directory;
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
import static ttit.com.shuvo.ikglhrm.OracleConnection.DEFAULT_USERNAME;
import static ttit.com.shuvo.ikglhrm.OracleConnection.createConnection;
import static ttit.com.shuvo.ikglhrm.scheduler.Uploader.channelId;

public class Dashboard extends AppCompatActivity {

    TextView userName;
    TextView designation;
    TextView department;
    TextView comp;
    ImageView userImage;

    TextView appName;
    ImageView home;
    ImageView logout;

    BarChart salaryChart;
    ImageView refreshSalary;

    ArrayList<BarEntry> salaryValue;
    ArrayList<String> year;

    ArrayList<SalaryMonthList> months;
    ArrayList<SalaryMonthList> salaryMonthLists;

    ArrayList<String> monthName;
    ArrayList<String> salary;

    WaitProgress waitProgress = new WaitProgress();
    private String message = null;
    private Boolean conn = false;
    private Boolean connected = false;

    private Connection connection;

    public static String emp_id_for_xml = "";
    String emp_id = "";
    String formattedDate = "";
    public static int trackerAvailable = 0;

    PieChart pieChart;
    ImageView refreshAttendance;

    String beginDate = "";
    String lastDate = "";

    String absent = "";
    String present = "";
    String leave = "";
    String holiday = "";
    String late = "";
    String early = "";

    ArrayList<PieEntry> NoOfEmp;

    public static ArrayList<String> lastTenDaysXml;

    String leaveDate = "";

    ArrayList<BarEntry> balance;
    ArrayList<BarEntry> earn;
    ArrayList<String> shortCode;

    BarChart leaveChart;
    ImageView refreshLeave;

    SharedPreferences sharedPreferences;
    SharedPreferences sharedSchedule;
    public static AlarmManager alarmManager;

    CardView userCard;

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
    public static final String DATABASE_NAME = "DATABASE_NAME";

    public static final String SCHEDULING_FILE = "SCHEDULING FILE";
    public static final String SCHEDULING_EMP_ID = "SCHEDULING EMP ID";
    public static final String TRIGGERING = "TRIGGER TRUE FALSE";

    static SharedPreferences sharedPreferencesDA;
    public static String FILE_OF_DAILY_ACTIVITY = "";
    public static String DISTANCE = "DISTANCE";
    public static String TOTAL_TIME = "TOTAL_TIME";
    public static String STOPPED_TIME = "STOPPED_TIME";

    boolean loginLog_check;

    String android_id = "";
    String model = "";
    String brand = "";
    String ipAddress = "";
    String hostUserName = "";
    String sessionId = "";
    String osName = "";
    public static int RESULT_LOAD_IMG = 1901;
    public static Bitmap selectedImage;
    boolean imageFound = false;
    TextView welcomeText;

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setNavigationBarColor(Color.parseColor("#f0932b"));
        setContentView(R.layout.activity_dashboard);


        userName = findViewById(R.id.user_name_dashboard);
        department = findViewById(R.id.user_depart_dashboard);
        designation = findViewById(R.id.user_desg_dashboard);
        comp = findViewById(R.id.company_name_dashboard);
        userCard = findViewById(R.id.userinfo_card);
        userImage = findViewById(R.id.user_pic_dashboard);
        welcomeText = findViewById(R.id.welcome_text_view);

        int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        String wt = "";
        if (currentHour >= 4 && currentHour <= 11) {
            wt = "GOOD MORNING,";
        }
        else if (currentHour >= 12 && currentHour <= 16) {
            wt = "GOOD AFTERNOON,";
        }
        else if (currentHour >= 17 && currentHour <= 22) {
            wt = "GOOD EVENING,";
        }
        else {
            wt = "HELLO,";
        }
        welcomeText.setText(wt);

        Intent in = getIntent();
        loginLog_check = in.getBooleanExtra("FROMMAINMENU", true);

        System.out.println("Log Needed? :" +loginLog_check);

        sharedPreferences = getSharedPreferences(LOGIN_ACTIVITY_FILE, MODE_PRIVATE);
        boolean loginfile = sharedPreferences.getBoolean(LOGIN_TF,false);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        if(loginfile) {
            if (loginLog_check) {
                System.out.println(loginfile +" PAISE");
                String userName = sharedPreferences.getString(USER_NAME, null);
                String userFname = sharedPreferences.getString(USER_F_NAME, null);
                String userLname = sharedPreferences.getString(USER_L_NAME,null);
                String email = sharedPreferences.getString(EMAIL,null);
                String contact = sharedPreferences.getString(CONTACT, null);
                String emp_id_login = sharedPreferences.getString(EMP_ID_LOGIN,null);

                userInfoLists = new ArrayList<>();
                userInfoLists.add(new UserInfoList(userName,userFname,userLname,email,contact,emp_id_login));

                String jsm_code = sharedPreferences.getString(JSM_CODE, null);
                String jsm_name = sharedPreferences.getString(JSM_NAME, null);
                String jsd_id = sharedPreferences.getString(JSD_ID_LOGIN,null);
                String jsd_obj = sharedPreferences.getString(JSD_OBJECTIVE,null);
                String dept_name = sharedPreferences.getString(DEPT_NAME, null);
                String div_name = sharedPreferences.getString(DIV_NAME, null);
                String desg_name = sharedPreferences.getString(DESG_NAME, null);
                String desg_priority = sharedPreferences.getString(DESG_PRIORITY,null);
                String joining = sharedPreferences.getString(JOINING_DATE, null);
                String div_id = sharedPreferences.getString(DIV_ID,null);

                userDesignations = new ArrayList<>();
                userDesignations.add(new UserDesignation(jsm_code,jsm_name,jsd_id,jsd_obj,dept_name,div_name,desg_name,desg_priority,joining,div_id));

                SoftwareName = sharedPreferences.getString(SOFTWARE, null);
                CompanyName = sharedPreferences.getString(COMPANY,null);

                isApproved = sharedPreferences.getInt(IS_ATT_APPROVED,0);
                isLeaveApproved = sharedPreferences.getInt(IS_LEAVE_APPROVED,0);
                DEFAULT_USERNAME = sharedPreferences.getString(DATABASE_NAME,DEFAULT_USERNAME);
            }



        }

        appName = findViewById(R.id.thrm_app_name);
        home = findViewById(R.id.home_icon);
        logout = findViewById(R.id.log_out_icon);

        salaryChart = findViewById(R.id.barchart_salary_dashboard);
        refreshSalary = findViewById(R.id.refresh_graph_salary_dashboard);

        pieChart = findViewById(R.id.piechart_attendance_dashboard);
        refreshAttendance = findViewById(R.id.refresh_graph_attendance_dashboard);

        leaveChart = findViewById(R.id.multi_bar_chart_leave_dashboard);
        refreshLeave = findViewById(R.id.refresh_graph_leave_dashboard);

        lastTenDaysXml = new ArrayList<>();

        emp_id = userInfoLists.get(0).getEmp_id();

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

        appName.setText(SoftwareName);
        comp.setText(CompanyName);

        model = android.os.Build.MODEL;

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
                e.printStackTrace();
            }

            if (fieldValue == Build.VERSION.SDK_INT) {
                builder.append(": ").append(fieldName);
                //builder.append(" : ").append(fieldName).append(" : ");
                //builder.append("sdk=").append(fieldValue);
            }
        }

        System.out.println("OS: " + builder.toString());
        //Log.d(LOG_TAG, "OS: " + builder.toString());

        //System.out.println("HOSTTTTT: " + getHostName());

        osName = builder.toString();

        android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, MainMenu.class);
                startActivity(intent);
                finish();
            }
        });

        SalaryInit();
        AttendanceInit();
        LeaveInit();

        emp_id_for_xml = emp_id;

        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_YEAR, -12);

        for (int i = 0 ; i < 10 ;i ++) {
            cal.add(Calendar.DAY_OF_YEAR, +1);
            Date calTime = cal.getTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yy", Locale.getDefault());
            String ddd = simpleDateFormat.format(calTime);

            ddd = ddd.toUpperCase();
            System.out.println(ddd);
            lastTenDaysXml.add(ddd);
        }




        new Check().execute();


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMyServiceRunning(Service.class)) {
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(Dashboard.this);
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
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(Dashboard.this);
                    builder.setTitle("LOG OUT!")
                            .setIcon(R.drawable.thrm_logo)
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
                                    editor1.remove(DATABASE_NAME);
                                    editor1.apply();
                                    editor1.commit();

                                    if (trackerAvailable == 1) {
                                        SharedPreferences.Editor editor = sharedSchedule.edit();
                                        editor.remove(SCHEDULING_EMP_ID);
                                        editor.remove(TRIGGERING);
                                        editor.apply();
                                        editor.commit();

                                        Intent intent1 = new Intent(Dashboard.this, Uploader.class);
                                        PendingIntent pendingIntent = null;
                                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                                            pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent1,PendingIntent.FLAG_IMMUTABLE);
                                        }
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

        refreshLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LeaveCheck().execute();
            }
        });

        refreshAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AttendanceCheck().execute();
            }
        });

        refreshSalary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SalaryCheck().execute();
            }
        });

        leaveChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, Leave.class);
                startActivity(intent);
            }
        });

        pieChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, Attendance.class);
                startActivity(intent);
            }
        });

        salaryChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, PayRollInfo.class);
                startActivity(intent);
            }
        });

        userCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, EmplyeeInformation.class);
                startActivity(intent);
            }
        });

//        userImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
//                photoPickerIntent.setType("image/*");
////                imagePickerActivityResult.launch(photoPickerIntent);
//                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
//            }
//        });

        deleteSharedPreferences(getApplicationContext());

    }

//    public static String getPath( Context context, Uri uri ) {
//        String result = null;
//        String[] proj = { MediaStore.Images.Media.DATA };
//        Cursor cursor = context.getContentResolver( ).query( uri, proj, null, null, null );
//        if(cursor != null){
//            if ( cursor.moveToFirst( ) ) {
//                int column_index = cursor.getColumnIndexOrThrow( proj[0] );
//                result = cursor.getString( column_index );
//            }
//            cursor.close( );
//        }
//        if(result == null) {
//            result = "Not found";
//        }
//        return result;
//    }
//
//    public static Bitmap modifyOrientation(Bitmap bitmap, String image_absolute_path) throws IOException {
//        ExifInterface ei = new ExifInterface(image_absolute_path);
//        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
//
//        switch (orientation) {
//            case ExifInterface.ORIENTATION_ROTATE_90:
//                return rotate(bitmap, 90);
//
//            case ExifInterface.ORIENTATION_ROTATE_180:
//                return rotate(bitmap, 180);
//
//            case ExifInterface.ORIENTATION_ROTATE_270:
//                return rotate(bitmap, 270);
//
//            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
//                return flip(bitmap, true, false);
//
//            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
//                return flip(bitmap, false, true);
//
//            default:
//                return bitmap;
//        }
//    }
//
//    public static Bitmap rotate(Bitmap bitmap, float degrees) {
//        Matrix matrix = new Matrix();
//        matrix.postRotate(degrees);
//        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//    }
//
//    public static Bitmap flip(Bitmap bitmap, boolean horizontal, boolean vertical) {
//        Matrix matrix = new Matrix();
//        matrix.preScale(horizontal ? -1 : 1, vertical ? -1 : 1);
//        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            try {
//                final Uri imageUri = data.getData();
//                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
//                selectedImage = BitmapFactory.decodeStream(imageStream);
//                String picturePath = getPath(getApplicationContext(), imageUri);
//                System.out.println(picturePath);
//                selectedImage = modifyOrientation(selectedImage,picturePath);
//                Intent intent = new Intent(Dashboard.this,UpdatePic.class);
//                intent.putExtra("EMP_ID",emp_id);
//                startActivity(intent);
//
////                ImageDialogue imageDialogue = new ImageDialogue();
////                imageDialogue.show(getSupportFragmentManager(),"IMAGE");
////                Glide.with(UserProfile.this)
////                        .load(selectedImage)
////                        .into(userImage);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//                Toast.makeText(Dashboard.this, "Something went wrong", Toast.LENGTH_LONG).show();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }else {
//            Toast.makeText(Dashboard.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
//        }
//    }
//
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
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
                FILE_OF_DAILY_ACTIVITY = emp_id_for_xml +"_"+lastTenDaysXml.get(i)+"_track";
                System.out.println(FILE_OF_DAILY_ACTIVITY + " of " + lastTenDaysXml.get(i));
                File dir = new File(context.getApplicationInfo().dataDir, "shared_prefs/"+FILE_OF_DAILY_ACTIVITY+".xml");
                if(dir.exists()) {
                    System.out.println(true);
                    context.getSharedPreferences(FILE_OF_DAILY_ACTIVITY, MODE_PRIVATE).edit().clear().commit();
                    System.out.println(dir.toString());
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
                        boolean isIPv4 = sAddr.indexOf(':')<0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim<0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
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


        MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(Dashboard.this)
                .setTitle("EXIT!")
                .setMessage("Do you want to exit?")
                .setIcon(R.drawable.thrm_logo)
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
        salaryChart.setExtraOffsets(0,0,0,20);

        // zoom and touch disabled
        salaryChart.setScaleEnabled(false);
        salaryChart.setTouchEnabled(true);
        salaryChart.setDoubleTapToZoomEnabled(false);
        salaryChart.setClickable(true);
        salaryChart.setHighlightPerTapEnabled(false);
        salaryChart.setHighlightPerDragEnabled(false);
        //salaryChart.setOnTouchListener(null);

        salaryChart.getAxisRight().setEnabled(false);
        salaryChart.getAxisLeft().setAxisMinValue(0);
        salaryChart.getLegend().setEnabled(false);

        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy", Locale.getDefault());

        formattedDate = df.format(c);

        Calendar cal =  Calendar.getInstance();

        cal.add(Calendar.MONTH, -1);
        String previousMonthYear  = new SimpleDateFormat("MMM", Locale.getDefault()).format(cal.getTime());
        previousMonthYear = previousMonthYear.toUpperCase();
        months.add(new SalaryMonthList(previousMonthYear,"0"));
        System.out.println(previousMonthYear);

        cal.add(Calendar.MONTH, -1);
        previousMonthYear  = new SimpleDateFormat("MMM", Locale.getDefault()).format(cal.getTime());
        previousMonthYear = previousMonthYear.toUpperCase();
        months.add(new SalaryMonthList(previousMonthYear,"0"));
        System.out.println(previousMonthYear);

        cal.add(Calendar.MONTH, -1);
        previousMonthYear  = new SimpleDateFormat("MMM", Locale.getDefault()).format(cal.getTime());
        previousMonthYear = previousMonthYear.toUpperCase();
        months.add(new SalaryMonthList(previousMonthYear,"0"));
        System.out.println(previousMonthYear);

        cal.add(Calendar.MONTH, -1);
        previousMonthYear  = new SimpleDateFormat("MMM", Locale.getDefault()).format(cal.getTime());
        previousMonthYear = previousMonthYear.toUpperCase();
        months.add(new SalaryMonthList(previousMonthYear,"0"));
        System.out.println(previousMonthYear);

        cal.add(Calendar.MONTH, -1);
        previousMonthYear  = new SimpleDateFormat("MMM", Locale.getDefault()).format(cal.getTime());
        previousMonthYear = previousMonthYear.toUpperCase();
        months.add(new SalaryMonthList(previousMonthYear,"0"));
        System.out.println(previousMonthYear);

        cal.add(Calendar.MONTH, -1);
        previousMonthYear  = new SimpleDateFormat("MMM", Locale.getDefault()).format(cal.getTime());
        previousMonthYear = previousMonthYear.toUpperCase();
        months.add(new SalaryMonthList(previousMonthYear,"0"));
        System.out.println(previousMonthYear);
    }

    public void AttendanceInit() {

        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy", Locale.getDefault());

        lastDate = df.format(c);

        SimpleDateFormat sdf = new SimpleDateFormat("MMM-yy", Locale.getDefault());

        beginDate = sdf.format(c);
        beginDate = "01-"+beginDate;

        NoOfEmp = new ArrayList<>();

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
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setForm(Legend.LegendForm.CIRCLE);

        l.setTextSize(12);
        l.setWordWrapEnabled(false);
        l.setDrawInside(false);
        l.setYOffset(10f);
    }

    public void LeaveInit() {
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy", Locale.getDefault());

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
        leaveChart.setExtraOffsets(0,0,0,20);
        leaveChart.setScaleEnabled(false);
        leaveChart.setTouchEnabled(true);
        leaveChart.setDoubleTapToZoomEnabled(false);
        leaveChart.setHighlightPerTapEnabled(false);
        leaveChart.setHighlightPerDragEnabled(false);
        leaveChart.getAxisLeft().setAxisMinimum(0);

        leaveChart.getAxisRight().setEnabled(false);
    }

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

    public class Check extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            waitProgress.show(getSupportFragmentManager(),"WaitBar");
            waitProgress.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isConnected() && isOnline()) {

                AllGraph();
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

                for (int i = months.size()-1; i >= 0; i--) {

                    monthName.add(months.get(i).getMonth());
                    salary.add(months.get(i).getSalary());

                }

                System.out.println(monthName);
                System.out.println(salary);

                for (int i = 0; i < salary.size(); i++) {
                    salaryValue.add(new BarEntry(i, Float.parseFloat(salary.get(i)),i));
                }

                BarDataSet bardataset = new BarDataSet(salaryValue, "Months");
                salaryChart.animateY(1000);

                BarData data1 = new BarData(bardataset);
                bardataset.setColors(ColorTemplate.VORDIPLOM_COLORS);

                bardataset.setBarBorderColor(Color.DKGRAY);
                bardataset.setValueTextSize(8);
                bardataset.setValueFormatter(new LargeValueFormatter());
                salaryChart.setData(data1);
                salaryChart.getXAxis().setValueFormatter(new MyAxisValueFormatter(monthName));
                salaryChart.getAxisLeft().setValueFormatter(new LargeValueFormatter());



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
                            NoOfEmp.add(new PieEntry(Float.parseFloat(absent), "Absent", 0));
                        }
                    }
                }

                if (present != null) {
                    if (!present.isEmpty()) {
                        if (!present.equals("0")) {
                            NoOfEmp.add(new PieEntry(Float.parseFloat(present),"Present", 1));
                        }
                    }
                }

                if (late != null) {
                    if (!late.isEmpty()) {
                        if (!late.equals("0")) {
                            NoOfEmp.add(new PieEntry(Float.parseFloat(late),"Late", 2));

                        }
                    }
                }

                if (early != null) {
                    if (!early.isEmpty()) {
                        if (!early.equals("0")) {
                            NoOfEmp.add(new PieEntry(Float.parseFloat(early),"Early", 3));
                        }
                    }
                }

                if (leave != null) {
                    if (!leave.isEmpty()) {
                        if (!leave.equals("0")) {
                            NoOfEmp.add(new PieEntry(Float.parseFloat(leave),"Leave", 4));
                        }
                    }
                }

                if (holiday != null) {
                    if (!holiday.isEmpty()) {
                        if (!holiday.equals("0")) {
                            NoOfEmp.add(new PieEntry(Float.parseFloat(holiday),"Holiday/Weekend", 5));
                        }
                    }
                }

                if (NoOfEmp.size() == 0) {
                    NoOfEmp.add(new PieEntry(1,"No Data Found", 6));

                }

                PieDataSet dataSet = new PieDataSet(NoOfEmp, "");
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

                int[] num = new int[NoOfEmp.size()];
                for (int i = 0; i < NoOfEmp.size(); i++) {
                    int neki = (int) NoOfEmp.get(i).getData();
                    num[i] = piecolors[neki];
                }

                dataSet.setColors(ColorTemplate.createColors(num));

                pieChart.setData(data);
                pieChart.invalidate();


                // Leave Multi Bar

                if (balance.size() == 0 || earn.size() == 0 || shortCode.size() == 0) {
                    // do nothing
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
                    if (earn.size() > 5) {
                        leavedata.setValueTextSize(8);
                    }
                    else if (earn.size() > 3){
                        leavedata.setValueTextSize(10);
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


                if (trackerAvailable == 1) {
                    sharedSchedule = getSharedPreferences(SCHEDULING_FILE, MODE_PRIVATE);
                    boolean isNewLogin = sharedSchedule.getBoolean(TRIGGERING,false);

                    // Triggering Schedule
                    System.out.println("SCHEDULING TASK STARTED");
                    SharedPreferences.Editor editor = sharedSchedule.edit();
                    editor.remove(SCHEDULING_EMP_ID);
                    editor.remove(TRIGGERING);

                    editor.putString(SCHEDULING_EMP_ID,emp_id);
                    editor.putBoolean(TRIGGERING, true);
                    editor.apply();
                    editor.commit();
                    createNotificationChannel();

                    Intent intent = new Intent(Dashboard.this, Uploader.class);
                    PendingIntent pendingIntent = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent,PendingIntent.FLAG_IMMUTABLE);
                    }


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

                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_HALF_HOUR,pendingIntent);
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



            }else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(Dashboard.this)
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

                        new Check().execute();
                        dialog.dismiss();
                    }
                });

                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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


                    }
                });
            }
        }
    }

    public class LeaveCheck extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            waitProgress.show(getSupportFragmentManager(),"WaitBar");
            waitProgress.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isConnected() && isOnline()) {

                LeaveBalanceGraph();
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

                if (balance.size() == 0 || earn.size() == 0 || shortCode.size() == 0) {
                    // do nothing
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
                    if (earn.size() > 5) {
                        leavedata.setValueTextSize(8);
                    }
                    else if (earn.size() > 3){
                        leavedata.setValueTextSize(10);
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
                                System.out.println(shortCode.get((int)value));
                                return (shortCode.get((int) value));
                            }

                        }
                    });
                }


                conn = false;
                connected = false;


            }else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(Dashboard.this)
                        .setMessage("Please Check Your Internet Connection")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();

//                dialog.setCancelable(false);
//                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new LeaveCheck().execute();
                        dialog.dismiss();
                    }
                });
            }
        }
    }

    public class AttendanceCheck extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            waitProgress.show(getSupportFragmentManager(),"WaitBar");
            waitProgress.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isConnected() && isOnline()) {

                AttendanceGraph();
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
                            NoOfEmp.add(new PieEntry(Float.parseFloat(absent), "Absent", 0));
                        }
                    }
                }

                if (present != null) {
                    if (!present.isEmpty()) {
                        if (!present.equals("0")) {
                            NoOfEmp.add(new PieEntry(Float.parseFloat(present),"Present", 1));
                        }
                    }
                }

                if (late != null) {
                    if (!late.isEmpty()) {
                        if (!late.equals("0")) {
                            NoOfEmp.add(new PieEntry(Float.parseFloat(late),"Late", 2));

                        }
                    }
                }

                if (early != null) {
                    if (!early.isEmpty()) {
                        if (!early.equals("0")) {
                            NoOfEmp.add(new PieEntry(Float.parseFloat(early),"Early", 3));
                        }
                    }
                }

                if (leave != null) {
                    if (!leave.isEmpty()) {
                        if (!leave.equals("0")) {
                            NoOfEmp.add(new PieEntry(Float.parseFloat(leave),"Leave", 4));
                        }
                    }
                }

                if (holiday != null) {
                    if (!holiday.isEmpty()) {
                        if (!holiday.equals("0")) {
                            NoOfEmp.add(new PieEntry(Float.parseFloat(holiday),"Holiday/Weekend", 5));
                        }
                    }
                }

                if (NoOfEmp.size() == 0) {
                    NoOfEmp.add(new PieEntry(1,"No Data Found", 6));

                }

                PieDataSet dataSet = new PieDataSet(NoOfEmp, "");
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

                int[] num = new int[NoOfEmp.size()];
                for (int i = 0; i < NoOfEmp.size(); i++) {
                    int neki = (int) NoOfEmp.get(i).getData();
                    num[i] = piecolors[neki];
                }

                dataSet.setColors(ColorTemplate.createColors(num));

                pieChart.setData(data);
                pieChart.invalidate();

                conn = false;
                connected = false;


            }else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(Dashboard.this)
                        .setMessage("Please Check Your Internet Connection")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();

//                dialog.setCancelable(false);
//                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new AttendanceCheck().execute();
                        dialog.dismiss();

                    }
                });
            }
        }
    }

    public class SalaryCheck extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            waitProgress.show(getSupportFragmentManager(),"WaitBar");
            waitProgress.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isConnected() && isOnline()) {

                SalaryGraph();
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

                for (int i = months.size()-1; i >= 0; i--) {

                    monthName.add(months.get(i).getMonth());
                    salary.add(months.get(i).getSalary());

                }

                System.out.println(monthName);
                System.out.println(salary);

                for (int i = 0; i < salary.size(); i++) {
                    salaryValue.add(new BarEntry(i, Float.parseFloat(salary.get(i)),i));
                }

                BarDataSet bardataset = new BarDataSet(salaryValue, "Months");
                salaryChart.animateY(1000);

                BarData data1 = new BarData(bardataset);
                bardataset.setColors(ColorTemplate.VORDIPLOM_COLORS);

                bardataset.setBarBorderColor(Color.DKGRAY);
                bardataset.setValueTextSize(8);
                bardataset.setValueFormatter(new LargeValueFormatter());
                salaryChart.setData(data1);
                salaryChart.getXAxis().setValueFormatter(new MyAxisValueFormatter(monthName));
                salaryChart.getAxisLeft().setValueFormatter(new LargeValueFormatter());

                conn = false;
                connected = false;

            }else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(Dashboard.this)
                        .setMessage("Please Check Your Internet Connection")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();

//                dialog.setCancelable(false);
//                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new SalaryCheck().execute();
                        dialog.dismiss();
                    }
                });
            }
        }
    }

    public void SalaryGraph() {
        try {
            this.connection = createConnection();
            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();


            salaryMonthLists = new ArrayList<>();
            Statement stmt = connection.createStatement();




            ResultSet rs=stmt.executeQuery("SELECT TO_CHAR(SM_PMS_MONTH,'MON'),EMP_NAME, NET_SALARY\n" +
                    "  FROM (SELECT SM.SM_PMS_MONTH,\n" +
                    "               E.EMP_NAME,\n" +
                    "               (  (  NVL (SD.SD_ATTD_BONUS_AMT, 0)\n" +
                    "                   + NVL (SD.SD_GROSS_SAL, 0)\n" +
                    "                   + (ROUND (\n" +
                    "                         (NVL (SD.SD_OT_HR, 0) * NVL (SD.SD_OT_RATE, 0))))\n" +
                    "                   + (  NVL (SD.SD_JOB_HABITATION, 0)\n" +
                    "                      + NVL (SD.SD_JOB_UTILITIES, 0)\n" +
                    "                      + NVL (SD.SD_JOB_HOUSE_ALLOWANCE, 0)\n" +
                    "                      + NVL (SD.SD_JOB_ENTERTAINMENT, 0)\n" +
                    "                      + NVL (SD.SD_COMMITTED_SALARY, 0)\n" +
                    "                      + NVL (SD.SD_FIXED_OT_AMT, 0)\n" +
                    "                      + NVL (SD.SD_FOOD_SUBSIDY_AMT, 0)\n" +
                    "                      + NVL (SD.SD_FACTORY_ALLOWANCE_AMT, 0)\n" +
                    "                      + NVL (SD.SD_HOLIDAY_AMT, 0)\n" +
                    "                      + NVL (SD.SD_REFUND_OTH_PAYMENT_AMT, 0)\n" +
                    "                      + NVL (SD.SD_PROFIT_SHARE_AMT, 0)\n" +
                    "                      + NVL (SD.SD_PERFORMANCE_BONUS_AMT, 0)))\n" +
                    "                - (  NVL (SD.SD_PF, 0)\n" +
                    "                   + NVL (SD.SD_LWPAY_AMT, 0)\n" +
                    "                   + NVL (SD.SD_ABSENT_AMT, 0)\n" +
                    "                   + NVL (SD.SD_ADVANCE_DEDUCT, 0)\n" +
                    "                   + NVL (SD.SD_SCH_ADVANCE_DEDUCT, 0)\n" +
                    "                   + NVL (SD.SD_PF_LOAN_DEDUCT, 0)\n" +
                    "                   + NVL (SD.SD_TAX, 0)\n" +
                    "                   + NVL (SD.SD_LUNCH_DEDUCT, 0)\n" +
                    "                   + NVL (SD.SD_STAMP, 0)\n" +
                    "                   + NVL (SD.SD_OTH_DEDUCT, 0)\n" +
                    "                   + NVL (SD.SD_MD_ADVANCE_DEDUCT, 0)))\n" +
                    "                  NET_SALARY\n" +
                    "          FROM SALARY_DTL    SD,\n" +
                    "               SALARY_MST    SM,\n" +
                    "               EMP_MST       E,\n" +
                    "               (SELECT *\n" +
                    "                  FROM EMP_JOB_HISTORY\n" +
                    "                 WHERE JOB_ID IN (  SELECT MAX (JOB_ID)\n" +
                    "                                      FROM EMP_JOB_HISTORY\n" +
                    "                                  GROUP BY JOB_EMP_ID)) EJH,\n" +
                    "               JOB_SETUP_MST JSM,\n" +
                    "               JOB_SETUP_DTL JSD,\n" +
                    "               DIVISION_MST  DM,\n" +
                    "               DEPT_MST      DPT\n" +
                    "         WHERE     SM.SM_ID = SD.SD_SM_ID\n" +
                    "               AND SD.SD_EMP_ID = E.EMP_ID\n" +
                    "               AND E.EMP_ID = EJH.JOB_EMP_ID\n" +
                    "               AND EJH.JOB_JSD_ID = JSD.JSD_ID\n" +
                    "               AND JSD.JSD_JSM_ID = JSM.JSM_ID\n" +
                    "               AND JSM.JSM_DIVM_ID = DM.DIVM_ID\n" +
                    "               AND JSM.JSM_DEPT_ID = DPT.DEPT_ID\n" +
                    "               AND SD.SD_EMP_ID = "+emp_id+"\n" +
                    "               AND SM.SM_PMS_MONTH <=\n" +
                    "                      TRUNC (ADD_MONTHS ( (LAST_DAY ('"+formattedDate+"') + 1), -1))\n" +
                    "order by SM.SM_PMS_MONTH desc)\n" +
                    " WHERE ROWNUM <= 6");


            int i = 0;
            while(rs.next()) {

                salaryMonthLists.add(new SalaryMonthList(rs.getString(1),rs.getString(3)));

            }


            connected = true;

            connection.close();

        }
        catch (Exception e) {

            //   Toast.makeText(MainActivity.this, ""+e,Toast.LENGTH_LONG).show();
            Log.i("ERRRRR", e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    public void AttendanceGraph() {
        try {
            this.connection = createConnection();
            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();


            NoOfEmp = new ArrayList<>();
            absent = "";
            present = "";
            leave = "";
            holiday = "";
            late = "";
            early = "";


            Statement stmt = connection.createStatement();




            ResultSet rs=stmt.executeQuery("SELECT ATTD_HOLIDAY_COUNT ("+emp_id+", '"+ lastDate +"', 'A') ABSENT,\n" +
                    "       (  ATTD_HOLIDAY_COUNT ("+emp_id+", '"+ lastDate +"', 'P')\n" +
                    "        + ATTD_HOLIDAY_COUNT ("+emp_id+", '"+ lastDate +"', 'PW')\n" +
                    "        + ATTD_HOLIDAY_COUNT ("+emp_id+", '"+ lastDate +"', 'PH')\n" +
                    "        + ATTD_HOLIDAY_COUNT ("+emp_id+", '"+ lastDate +"', 'PL')\n" +
                    "        + ATTD_HOLIDAY_COUNT ("+emp_id+", '"+ lastDate +"', 'PLH')\n" +
                    "        + ATTD_HOLIDAY_COUNT ("+emp_id+", '"+ lastDate +"', 'PA')\n" +
                    "        + ATTD_HOLIDAY_COUNT ("+emp_id+", '"+ lastDate +"', 'PLW'))\n" +
                    "          PRESENT,\n" +
                    "       (  ATTD_HOLIDAY_COUNT ("+emp_id+", '"+ lastDate +"', 'L')\n" +
                    "        + ATTD_HOLIDAY_COUNT ("+emp_id+", '"+ lastDate +"', 'LW')\n" +
                    "        + ATTD_HOLIDAY_COUNT ("+emp_id+", '"+ lastDate +"', 'LH'))\n" +
                    "          LEAVE,\n" +
                    "       ATTD_HOLIDAY_COUNT ("+emp_id+", '"+ lastDate +"', 'H') +\n" +
                    "       ATTD_HOLIDAY_COUNT ("+emp_id+", '"+ lastDate +"', 'W') HOLIDAY_WEEKEND\n" +
                    "  FROM DUAL");


            while(rs.next()) {

                absent = rs.getString(1);
                present = rs.getString(2);
                leave = rs.getString(3);
                holiday = rs.getString(4);


            }

            ResultSet resultSet = stmt.executeQuery("SELECT LATE_COUNT_NEW\n" +
                    "("+emp_id+",\n" +
                    " '"+beginDate+"',\n" +
                    " '"+ lastDate +"')\n" +
                    "  FROM DUAL");

            while (resultSet.next()) {
                late = resultSet.getString(1);
            }

            ResultSet resultSet1 = stmt.executeQuery("  SELECT GET_EARLY_COUNT \n" +
                    "("+emp_id+",\n" +
                    " '"+beginDate+"',\n" +
                    " '"+ lastDate +"')\n" +
                    "  FROM DUAL");

            while (resultSet1.next()) {
                early = resultSet1.getString(1);
            }


            connected = true;

            connection.close();

        }
        catch (Exception e) {

            //   Toast.makeText(MainActivity.this, ""+e,Toast.LENGTH_LONG).show();
            Log.i("ERRRRR", e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    public void LeaveBalanceGraph() {
        try {
            this.connection = createConnection();
            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();

            balance = new ArrayList<>();
            earn = new ArrayList<>();
            shortCode = new ArrayList<>();
            Statement stmt = connection.createStatement();




//            ResultSet rs=stmt.executeQuery("SELECT EM.LBEM_EMP_ID, lc.lc_short_code, LD.LBD_BALANCE_QTY,LD.LBD_CURRENT_QTY\n" +
//                    "FROM LEAVE_BALANCE_EMP_MST  EM,\n" +
//                    "LEAVE_BALANCE_YEAR_DTL YD,\n" +
//                    "LEAVE_BALANCE_DTL      LD,\n" +
//                    "leave_category lc\n" +
//                    "WHERE EM.LBEM_ID = YD.LBYD_LBEM_ID\n" +
//                    "AND YD.LBYD_ID = LD.LBD_LBYD_ID\n" +
//                    "and ld.lbd_lc_id = lc.lc_id\n" +
//                    "AND EM.LBEM_EMP_ID = "+emp_id+"\n" +
//                    "AND TO_CHAR (YD.LBYD_YEAR, 'YYYY') = TO_CHAR (TO_DATE('"+ leaveDate +"'), 'YYYY')");

            ResultSet rs=stmt.executeQuery("SELECT EM.LBEM_EMP_ID, lc.lc_short_code, case when lc.lc_short_code = 'LP' then 0 else get_leave_balance(EM.LBEM_EMP_ID,'"+leaveDate+"', lc.lc_short_code) end balance,NVL(LD.LBD_CURRENT_QTY,0) + NVL(ld.lbd_opening_qty,0)\n" +
                    "FROM LEAVE_BALANCE_EMP_MST  EM,\n" +
                    "LEAVE_BALANCE_YEAR_DTL YD,\n" +
                    "LEAVE_BALANCE_DTL      LD,\n" +
                    "leave_category lc\n" +
                    "WHERE EM.LBEM_ID = YD.LBYD_LBEM_ID\n" +
                    "AND YD.LBYD_ID = LD.LBD_LBYD_ID\n" +
                    "and ld.lbd_lc_id = lc.lc_id\n" +
                    "AND EM.LBEM_EMP_ID = "+emp_id+"\n" +
                    "AND TO_CHAR (YD.LBYD_YEAR, 'YYYY') = TO_CHAR (TO_DATE('"+ leaveDate +"'), 'YYYY')");


            int i = 0;
            while(rs.next()) {


                String data = rs.getString(4);
                String sc = rs.getString(2);
                String bl = rs.getString(3);
                if (data != null) {
                    if (!data.equals("0")) {

                        balance.add(new BarEntry(i, Float.parseFloat(rs.getString(3)),i));
                        earn.add(new BarEntry(i, Float.parseFloat(rs.getString(4)),i));
                        shortCode.add(rs.getString(2));
                        i++;
                    }

                }



            }


            connected = true;

            connection.close();

        }
        catch (Exception e) {

            //   Toast.makeText(MainActivity.this, ""+e,Toast.LENGTH_LONG).show();
            Log.i("ERRRRR", e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    public void AllGraph() {
        try {
            this.connection = createConnection();
            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();


            salaryMonthLists = new ArrayList<>();

            NoOfEmp = new ArrayList<>();
            absent = "";
            present = "";
            leave = "";
            holiday = "";
            late = "";
            early = "";
            imageFound = false;
            selectedImage = null;

//            beginDate = "01-MAR-22";
//            lastDate = "31-MAR-22";
            //formattedDate = "30-JUL-21";

            balance = new ArrayList<>();
            earn = new ArrayList<>();
            shortCode = new ArrayList<>();
            Statement stmt = connection.createStatement();




            ResultSet rs=stmt.executeQuery("SELECT TO_CHAR(SM_PMS_MONTH,'MON'),EMP_NAME, NET_SALARY\n" +
                    "  FROM (SELECT SM.SM_PMS_MONTH,\n" +
                    "               E.EMP_NAME,\n" +
                    "               (  (  NVL (SD.SD_ATTD_BONUS_AMT, 0)\n" +
                    "                   + NVL (SD.SD_GROSS_SAL, 0)\n" +
                    "                   + (ROUND (\n" +
                    "                         (NVL (SD.SD_OT_HR, 0) * NVL (SD.SD_OT_RATE, 0))))\n" +
                    "                   + (  NVL (SD.SD_JOB_HABITATION, 0)\n" +
                    "                      + NVL (SD.SD_JOB_UTILITIES, 0)\n" +
                    "                      + NVL (SD.SD_JOB_HOUSE_ALLOWANCE, 0)\n" +
                    "                      + NVL (SD.SD_JOB_ENTERTAINMENT, 0)\n" +
                    "                      + NVL (SD.SD_COMMITTED_SALARY, 0)\n" +
                    "                      + NVL (SD.SD_FIXED_OT_AMT, 0)\n" +
                    "                      + NVL (SD.SD_FOOD_SUBSIDY_AMT, 0)\n" +
                    "                      + NVL (SD.SD_FACTORY_ALLOWANCE_AMT, 0)\n" +
                    "                      + NVL (SD.SD_HOLIDAY_AMT, 0)\n" +
                    "                      + NVL (SD.SD_REFUND_OTH_PAYMENT_AMT, 0)\n" +
                    "                      + NVL (SD.SD_PROFIT_SHARE_AMT, 0)\n" +
                    "                      + NVL (SD.SD_PERFORMANCE_BONUS_AMT, 0)))\n" +
                    "                - (  NVL (SD.SD_PF, 0)\n" +
                    "                   + NVL (SD.SD_LWPAY_AMT, 0)\n" +
                    "                   + NVL (SD.SD_ABSENT_AMT, 0)\n" +
                    "                   + NVL (SD.SD_ADVANCE_DEDUCT, 0)\n" +
                    "                   + NVL (SD.SD_SCH_ADVANCE_DEDUCT, 0)\n" +
                    "                   + NVL (SD.SD_PF_LOAN_DEDUCT, 0)\n" +
                    "                   + NVL (SD.SD_TAX, 0)\n" +
                    "                   + NVL (SD.SD_LUNCH_DEDUCT, 0)\n" +
                    "                   + NVL (SD.SD_STAMP, 0)\n" +
                    "                   + NVL (SD.SD_OTH_DEDUCT, 0)\n" +
                    "                   + NVL (SD.SD_MD_ADVANCE_DEDUCT, 0)))\n" +
                    "                  NET_SALARY\n" +
                    "          FROM SALARY_DTL    SD,\n" +
                    "               SALARY_MST    SM,\n" +
                    "               EMP_MST       E,\n" +
                    "               (SELECT *\n" +
                    "                  FROM EMP_JOB_HISTORY\n" +
                    "                 WHERE JOB_ID IN (  SELECT MAX (JOB_ID)\n" +
                    "                                      FROM EMP_JOB_HISTORY\n" +
                    "                                  GROUP BY JOB_EMP_ID)) EJH,\n" +
                    "               JOB_SETUP_MST JSM,\n" +
                    "               JOB_SETUP_DTL JSD,\n" +
                    "               DIVISION_MST  DM,\n" +
                    "               DEPT_MST      DPT\n" +
                    "         WHERE     SM.SM_ID = SD.SD_SM_ID\n" +
                    "               AND SD.SD_EMP_ID = E.EMP_ID\n" +
                    "               AND E.EMP_ID = EJH.JOB_EMP_ID\n" +
                    "               AND EJH.JOB_JSD_ID = JSD.JSD_ID\n" +
                    "               AND JSD.JSD_JSM_ID = JSM.JSM_ID\n" +
                    "               AND JSM.JSM_DIVM_ID = DM.DIVM_ID\n" +
                    "               AND JSM.JSM_DEPT_ID = DPT.DEPT_ID\n" +
                    "               AND SD.SD_EMP_ID = "+emp_id+"\n" +
                    "               AND SM.SM_PMS_MONTH <=\n" +
                    "                      TRUNC (ADD_MONTHS ( (LAST_DAY ('"+formattedDate+"') + 1), -1))\n" +
                    "order by SM.SM_PMS_MONTH desc)\n" +
                    " WHERE ROWNUM <= 6");


            int i = 0;
            while(rs.next()) {

                salaryMonthLists.add(new SalaryMonthList(rs.getString(1),rs.getString(3)));

            }

            ResultSet rs1=stmt.executeQuery("SELECT ATTD_HOLIDAY_COUNT ("+emp_id+", '"+ lastDate +"', 'A') ABSENT,\n" +
                    "       (  ATTD_HOLIDAY_COUNT ("+emp_id+", '"+ lastDate +"', 'P')\n" +
                    "        + ATTD_HOLIDAY_COUNT ("+emp_id+", '"+ lastDate +"', 'PW')\n" +
                    "        + ATTD_HOLIDAY_COUNT ("+emp_id+", '"+ lastDate +"', 'PH')\n" +
                    "        + ATTD_HOLIDAY_COUNT ("+emp_id+", '"+ lastDate +"', 'PL')\n" +
                    "        + ATTD_HOLIDAY_COUNT ("+emp_id+", '"+ lastDate +"', 'PLH')\n" +
                    "        + ATTD_HOLIDAY_COUNT ("+emp_id+", '"+ lastDate +"', 'PA')\n" +
                    "        + ATTD_HOLIDAY_COUNT ("+emp_id+", '"+ lastDate +"', 'PLW'))\n" +
                    "          PRESENT,\n" +
                    "       (  ATTD_HOLIDAY_COUNT ("+emp_id+", '"+ lastDate +"', 'L')\n" +
                    "        + ATTD_HOLIDAY_COUNT ("+emp_id+", '"+ lastDate +"', 'LW')\n" +
                    "        + ATTD_HOLIDAY_COUNT ("+emp_id+", '"+ lastDate +"', 'LH'))\n" +
                    "          LEAVE,\n" +
                    "       ATTD_HOLIDAY_COUNT ("+emp_id+", '"+ lastDate +"', 'H') +\n" +
                    "       ATTD_HOLIDAY_COUNT ("+emp_id+", '"+ lastDate +"', 'W') HOLIDAY_WEEKEND\n" +
                    "  FROM DUAL");


            while(rs1.next()) {

                absent = rs1.getString(1);
                present = rs1.getString(2);
                leave = rs1.getString(3);
                holiday = rs1.getString(4);


            }

            ResultSet resultSet = stmt.executeQuery("SELECT LATE_COUNT_NEW\n" +
                    "("+emp_id+",\n" +
                    " '"+beginDate+"',\n" +
                    " '"+ lastDate +"')\n" +
                    "  FROM DUAL");

            while (resultSet.next()) {
                late = resultSet.getString(1);
            }

            ResultSet resultSet1 = stmt.executeQuery("  SELECT GET_EARLY_COUNT \n" +
                    "("+emp_id+",\n" +
                    " '"+beginDate+"',\n" +
                    " '"+ lastDate +"')\n" +
                    "  FROM DUAL");

            while (resultSet1.next()) {
                early = resultSet1.getString(1);
            }

//            ResultSet rs2=stmt.executeQuery("SELECT EM.LBEM_EMP_ID, lc.lc_short_code, LD.LBD_BALANCE_QTY,LD.LBD_CURRENT_QTY\n" +
//                    "FROM LEAVE_BALANCE_EMP_MST  EM,\n" +
//                    "LEAVE_BALANCE_YEAR_DTL YD,\n" +
//                    "LEAVE_BALANCE_DTL      LD,\n" +
//                    "leave_category lc\n" +
//                    "WHERE EM.LBEM_ID = YD.LBYD_LBEM_ID\n" +
//                    "AND YD.LBYD_ID = LD.LBD_LBYD_ID\n" +
//                    "and ld.lbd_lc_id = lc.lc_id\n" +
//                    "AND EM.LBEM_EMP_ID = "+emp_id+"\n" +
//                    "AND TO_CHAR (YD.LBYD_YEAR, 'YYYY') = TO_CHAR (TO_DATE('"+ leaveDate +"'), 'YYYY')");
            ResultSet rs2 = stmt.executeQuery("SELECT EM.LBEM_EMP_ID, lc.lc_short_code, case when lc.lc_short_code = 'LP' then 0 else get_leave_balance(EM.LBEM_EMP_ID,'"+leaveDate+"', lc.lc_short_code) end balance,NVL(LD.LBD_CURRENT_QTY,0) + NVL(ld.lbd_opening_qty,0)\n" +
                    "FROM LEAVE_BALANCE_EMP_MST  EM,\n" +
                    "LEAVE_BALANCE_YEAR_DTL YD,\n" +
                    "LEAVE_BALANCE_DTL      LD,\n" +
                    "leave_category lc\n" +
                    "WHERE EM.LBEM_ID = YD.LBYD_LBEM_ID\n" +
                    "AND YD.LBYD_ID = LD.LBD_LBYD_ID\n" +
                    "and ld.lbd_lc_id = lc.lc_id\n" +
                    "AND EM.LBEM_EMP_ID = "+emp_id+"\n" +
                    "AND TO_CHAR (YD.LBYD_YEAR, 'YYYY') = TO_CHAR (TO_DATE('"+ leaveDate +"'), 'YYYY')");


            int j = 0;
            while(rs2.next()) {


                String data = rs2.getString(4);
                if (data != null) {
                    if (!data.equals("0")) {
                        balance.add(new BarEntry(j, Float.parseFloat(rs2.getString(3)),j));
                        earn.add(new BarEntry(j, Float.parseFloat(rs2.getString(4)),j));
                        shortCode.add(rs2.getString(2));
                        j++;
                    }
                }


            }

            ResultSet resultSet2 = stmt.executeQuery("SELECT\n" +
                    "    emp_mst.emp_timeline_tracker_flag\n" +
                    "\n" +
                    "FROM\n" +
                    "         emp_mst\n" +
                    "\n" +
                    "WHERE\n" +
                    "    emp_mst.emp_id = "+emp_id+"");
            while (resultSet2.next()) {
                trackerAvailable = resultSet2.getInt(1);
            }

            if (loginLog_check) {
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

                sessionId = "";

                ResultSet resultSet3 = stmt.executeQuery("SELECT SYS_CONTEXT ('USERENV', 'SESSIONID') --INTO P_IULL_SESSION_ID\n" +
                        "   FROM DUAL\n");

                while (resultSet3.next()) {
                    System.out.println("SESSION ID: "+ resultSet3.getString(1));
                    sessionId = resultSet3.getString(1);
                }

                resultSet3.close();

                CallableStatement callableStatement1 = connection.prepareCall("{call USERLOGINDTL(?,?,?,?,?,?,?,?,?)}");
                callableStatement1.setString(1,userName);
                callableStatement1.setString(2, brand+" "+model);
                callableStatement1.setString(3,ipAddress);
                callableStatement1.setString(4,hostUserName);
                callableStatement1.setInt(5,Integer.parseInt(userId));
                callableStatement1.setInt(6,Integer.parseInt(sessionId));
                callableStatement1.setString(7,"1");
                callableStatement1.setString(8,osName);
                callableStatement1.setInt(9,3);
                callableStatement1.execute();

                callableStatement1.close();
            }

            ResultSet imageResult = stmt.executeQuery("SELECT EMP_IMAGE FROM EMP_MST WHERE EMP_ID = "+emp_id+"");

            while (imageResult.next()) {
                Blob b = imageResult.getBlob(1);
                if (b == null) {
                    imageFound = false;
                }
                else {
                    imageFound = true;
                    byte[] barr =b.getBytes(1,(int)b.length());
                    selectedImage = BitmapFactory.decodeByteArray(barr,0,barr.length);
                }
            }
            imageResult.close();
            stmt.close();

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