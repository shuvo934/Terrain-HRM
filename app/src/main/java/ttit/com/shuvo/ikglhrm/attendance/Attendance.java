package ttit.com.shuvo.ikglhrm.attendance;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.dewinjm.monthyearpicker.MonthFormat;
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialog;
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import ttit.com.shuvo.ikglhrm.R;
import ttit.com.shuvo.ikglhrm.WaitProgress;
import ttit.com.shuvo.ikglhrm.attendance.giveAttendance.AttendanceGive;
import ttit.com.shuvo.ikglhrm.attendance.report.AttendanceReport;

import static ttit.com.shuvo.ikglhrm.Login.CompanyName;
import static ttit.com.shuvo.ikglhrm.Login.SoftwareName;
import static ttit.com.shuvo.ikglhrm.Login.userInfoLists;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Attendance extends AppCompatActivity {


    CardView attReport;
    CardView attUpdateall;
    CardView attGive;

    TextView softName;
    TextView compName;

    Button attBack;

    PieChart pieChart;
    TextView refresh;
    FloatingActionButton floatingActionButton;

    int dateCount = 0;

    WaitProgress waitProgress = new WaitProgress();
    private String message = null;
    private Boolean conn = false;
    private Boolean connected = false;

//    private Connection connection;

    String emp_id = "";
    String emp_code = "";
    String beginDate = "";
    String lastDate = "";
    String lastDateForAttBot = "";
    String intTimeAttBot = "";
    String outTimeAttBot = "";
    String lastLtimAttBot = "";

    String absent = "";
    String present = "";
    String leave = "";
    String holiday = "";
    String late = "";
    String early = "";
    ArrayList<String> lastTenDays;
    ArrayList<String> lastTenDaysFromSQL;
    ArrayList<String> updatedFiles;
    ArrayList<String> updatedXml;

    public static int tracking_flag = 0;
    public static int live_tracking_flag = 0;

    ArrayList<PieEntry> NoOfEmp;

    SharedPreferences sharedPreferencesDA;
    public static String FILE_OF_DAILY_ACTIVITY = "";
    public static  String DISTANCE = "DISTANCE";
    public static  String TOTAL_TIME = "TOTAL_TIME";
    public static  String STOPPED_TIME = "STOPPED_TIME";
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

        setContentView(R.layout.activity_attendance);

        softName = findViewById(R.id.name_of_soft_attendance);
        compName = findViewById(R.id.name_of_company_attendance);

        String soft = CompanyName;
        String comp = SoftwareName;
        lastTenDays = new ArrayList<>();
        lastTenDaysFromSQL = new ArrayList<>();
        updatedFiles = new ArrayList<>();
        updatedXml = new ArrayList<>();

        softName.setText(soft);
        compName.setText(comp);

        attReport =findViewById(R.id.attendance_report);
        attUpdateall = findViewById(R.id.atten_update_all);
        attGive = findViewById(R.id.attendance_give);

        attBack = findViewById(R.id.attendance_back);

        pieChart = findViewById(R.id.piechart_attendance);
        refresh = findViewById(R.id.refresh_graph_attendance);
        floatingActionButton = findViewById(R.id.floating_button_att_bottom);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheetDialog();
            }
        });


        attReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Attendance.this, AttendanceReport.class);
                startActivity(intent);
            }
        });

        attUpdateall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Attendance.this, AttendanceUpdateAll.class);
                startActivity(intent);
            }
        });

        attGive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Attendance.this, AttendanceGive.class);
                intent.putExtra("LAST_TIME",lastLtimAttBot);
                intent.putExtra("TODAY_DATE",lastDateForAttBot);
                startActivity(intent);
            }
        });



        attBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        emp_id = userInfoLists.get(0).getEmp_id();
        emp_code = userInfoLists.get(0).getUserName();

        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy", Locale.getDefault());

        lastDate = df.format(c);

        SimpleDateFormat dfffff = new SimpleDateFormat("dd-MMMM-yyyy", Locale.getDefault());

        lastDateForAttBot = dfffff.format(c);

        SimpleDateFormat sdf = new SimpleDateFormat("MMM-yy", Locale.getDefault());

        beginDate = sdf.format(c);
        beginDate = "01-"+beginDate;

        SimpleDateFormat mon = new SimpleDateFormat("MMMM",Locale.getDefault());

        String mmm = mon.format(c);

        refresh.setText("Month: "+mmm);

        NoOfEmp = new ArrayList<>();

//        NoOfEmp.add(new PieEntry(945f, "2008", 0));
//        NoOfEmp.add(new PieEntry(1040f,"2009", 1));
//        NoOfEmp.add(new PieEntry(1133f,"2010", 2));
//        NoOfEmp.add(new PieEntry(1240f,"2011", 3));
//        NoOfEmp.add(new PieEntry(1369f,"2012", 4));
//        NoOfEmp.add(new PieEntry(1487f,"2013", 5));
//        NoOfEmp.add(new PieEntry(1501f,"2014", 6));
//        NoOfEmp.add(new PieEntry(1645f,"2015", 7));
//        NoOfEmp.add(new PieEntry(1578f,"2016", 8));
//        NoOfEmp.add(new PieEntry(1695f,"2017", 9));
//
//        PieDataSet dataSet = new PieDataSet(NoOfEmp, "");

        pieChart.setCenterText("Attendance");
        pieChart.setDrawEntryLabels(true);
        pieChart.setCenterTextSize(14);
        pieChart.setHoleRadius(40);
        pieChart.setTransparentCircleRadius(40);
//        pieChart.setTransparentCircleColor(Color.RED);
//        pieChart.setTransparentCircleAlpha(50);

//        pieChart.setCenterTextRadiusPercent(30);
        pieChart.setEntryLabelTextSize(11);
        pieChart.setEntryLabelColor(Color.DKGRAY);
        pieChart.getDescription().setEnabled(false);


        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setXOffset(20);
        l.setTextSize(12);
        //l.setYOffset(50);
        l.setWordWrapEnabled(false);
        l.setDrawInside(false);
        l.setYOffset(5f);

        final int[] piecolors = new int[]{
                Color.rgb(129, 236, 236),
                Color.rgb(85, 239, 196),
                Color.rgb(116, 185, 255),
                Color.rgb(162, 155, 254),
                Color.rgb(223, 230, 233),
                Color.rgb(255, 234, 167),
                Color.rgb(250, 177, 160),
                Color.rgb(255, 118, 117),
                Color.rgb(253, 121, 168),
                Color.rgb(96, 163, 188)};
        pieChart.animateXY(1000, 1000);

//        PieData data = new PieData(dataSet);
//
////        dataSet.setValueFormatter(new PercentFormatter(pieChart));
//        dataSet.setHighlightEnabled(true);
//        dataSet.setValueTextSize(12);
//        dataSet.setValueTextColor(Color.BLACK);
//        dataSet.setColors(ColorTemplate.createColors(piecolors));

//        pieChart.setUsePercentValues(true);


//        pieChart.setData(data);
//        pieChart.invalidate();

//        new Check().execute();
        getAttendanceGraph();


        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Date c = Calendar.getInstance().getTime();

                String formattedYear = "";
                String monthValue = "";
                String lastformattedYear = "";
                String lastdateView = "";

                SimpleDateFormat df = new SimpleDateFormat("yyyy", Locale.getDefault());
                SimpleDateFormat sdf = new SimpleDateFormat("MM",Locale.getDefault());

                formattedYear = df.format(c);
                monthValue = sdf.format(c);
                int nowMonNumb = Integer.parseInt(monthValue);
                nowMonNumb = nowMonNumb - 1;
                int lastMonNumb = nowMonNumb - 5;

                if (lastMonNumb < 0) {
                    lastMonNumb = lastMonNumb + 12;
                    int formatY = Integer.parseInt(formattedYear);
                    formatY = formatY - 1;
                    lastformattedYear = String.valueOf(formatY);
                } else {
                    lastformattedYear = formattedYear;
                }

                Date today = new Date();

                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTime(today);

                calendar1.add(Calendar.MONTH, 1);
                calendar1.set(Calendar.DAY_OF_MONTH, 1);
                calendar1.add(Calendar.DATE, -1);

                Date lastDayOfMonth = calendar1.getTime();

                SimpleDateFormat sdff = new SimpleDateFormat("dd",Locale.getDefault());
                lastdateView = sdff.format(lastDayOfMonth);

                int yearSelected;
                int monthSelected;
                MonthFormat monthFormat = MonthFormat.LONG;
                String customTitle = "Select Month";
// Use the calendar for create ranges
                Calendar calendar = Calendar.getInstance();
                yearSelected = calendar.get(Calendar.YEAR);
                monthSelected = calendar.get(Calendar.MONTH);
                calendar.clear();
                calendar.set(Integer.parseInt(lastformattedYear), lastMonNumb, 1); // Set minimum date to show in dialog
                long minDate = calendar.getTimeInMillis(); // Get milliseconds of the modified date

                calendar.clear();
                calendar.set(Integer.parseInt(formattedYear), nowMonNumb, Integer.parseInt(lastdateView)); // Set maximum date to show in dialog
                long maxDate = calendar.getTimeInMillis(); // Get milliseconds of the modified date

// Create instance with date ranges values
                MonthYearPickerDialogFragment dialogFragment =  MonthYearPickerDialogFragment
                        .getInstance(monthSelected, yearSelected, minDate, maxDate, customTitle, monthFormat);



                dialogFragment.show(getSupportFragmentManager(), null);

                dialogFragment.setOnDateSetListener(new MonthYearPickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(int year, int monthOfYear) {
                        System.out.println(year);
                        System.out.println(monthOfYear);

                        int month = monthOfYear + 1;
                        String monthName = "";
                        String mon = "";
                        String yearName = "";

                        if (month == 1) {
                            monthName = "JANUARY";
                            mon = "JAN";
                        } else if (month == 2) {
                            monthName = "FEBRUARY";
                            mon = "FEB";
                        } else if (month == 3) {
                            monthName = "MARCH";
                            mon = "MAR";
                        } else if (month == 4) {
                            monthName = "APRIL";
                            mon = "APR";
                        } else if (month == 5) {
                            monthName = "MAY";
                            mon = "MAY";
                        } else if (month == 6) {
                            monthName = "JUNE";
                            mon = "JUN";
                        } else if (month == 7) {
                            monthName = "JULY";
                            mon = "JUL";
                        } else if (month == 8) {
                            monthName = "AUGUST";
                            mon = "AUG";
                        } else if (month == 9) {
                            monthName = "SEPTEMBER";
                            mon = "SEP";
                        } else if (month == 10) {
                            monthName = "OCTOBER";
                            mon = "OCT";
                        } else if (month == 11) {
                            monthName = "NOVEMBER";
                            mon = "NOV";
                        } else if (month == 12) {
                            monthName = "DECEMBER";
                            mon = "DEC";
                        }

                        yearName  = String.valueOf(year);
                        yearName = yearName.substring(yearName.length()-2);


                        beginDate = "01-"+mon+"-"+yearName;
                        //selected_date = "01-"+mon+"-"+yearName;
                        refresh.setText("Month: "+ monthName);

                        SimpleDateFormat sss = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());

                        Date today = null;
                        try {
                            today = sss.parse(beginDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        Calendar calendar1 = Calendar.getInstance();
                        if (today != null) {
                            calendar1.setTime(today);
                            calendar1.add(Calendar.MONTH, 1);
                            calendar1.set(Calendar.DAY_OF_MONTH, 1);
                            calendar1.add(Calendar.DATE, -1);

                            Date lastDayOfMonth = calendar1.getTime();

                            SimpleDateFormat sdff = new SimpleDateFormat("dd",Locale.getDefault());
                            String llll = sdff.format(lastDayOfMonth);
                            lastDate =  llll+ "-" + mon +"-"+ yearName;
                        }


//                        new Check().execute();
                        getAttendanceGraph();
                    }
                });

            }
        });

        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_YEAR, -11);

        for (int i = 0 ; i < 10 ;i ++) {
            cal.add(Calendar.DAY_OF_YEAR, +1);
            Date calTime = cal.getTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yy", Locale.getDefault());
            String ddd = simpleDateFormat.format(calTime);

            ddd = ddd.toUpperCase();
            System.out.println(ddd);
            lastTenDays.add(ddd);
        }

    }

    private void showBottomSheetDialog() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Attendance.this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dial_attendance);
        TextView today_date_att_bot = bottomSheetDialog.findViewById(R.id.today_date_att_bottom);
        TextView inTime_att_bot = bottomSheetDialog.findViewById(R.id.in_time_att_bottom);
        TextView outTime_att_bot = bottomSheetDialog.findViewById(R.id.out_time_att_bottom);
        TextView lastTime = bottomSheetDialog.findViewById(R.id.last_time_att_bottom);
        if (today_date_att_bot != null) {
            today_date_att_bot.setText(lastDateForAttBot);
        }
        if (inTime_att_bot != null) {
            inTime_att_bot.setText(intTimeAttBot);
        }
        if (outTime_att_bot != null) {
            outTime_att_bot.setText(outTimeAttBot);
        }
        if (lastTime != null) {
            lastTime.setText(lastLtimAttBot);
        }
        bottomSheetDialog.show();
    }

//    public boolean isConnected() {
//        boolean connected = false;
//        boolean isMobile = false;
//        try {
//            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
//            NetworkInfo nInfo = cm.getActiveNetworkInfo();
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
//        catch (IOException | InterruptedException e)          { e.printStackTrace(); }
//
//        return false;
//    }
//
//    public class Check extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//            waitProgress.show(getSupportFragmentManager(),"WaitBar");
//            waitProgress.setCancelable(false);
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            if (isConnected() && isOnline()) {
//
//                AttendanceGraph();
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
//
//
//                final int[] piecolors = new int[]{
//
//
//                        Color.rgb(116, 185, 255),
//                        Color.rgb(85, 239, 196),
//                        Color.rgb(162, 155, 254),
//                        Color.rgb(223, 230, 233),
//                        Color.rgb(255, 234, 167),
//
//                        Color.rgb(250, 177, 160),
//                        Color.rgb(129, 236, 236),
//                        Color.rgb(255, 118, 117),
//                        Color.rgb(253, 121, 168),
//                        Color.rgb(96, 163, 188)};
//
//
//                if (absent != null) {
//                    if (!absent.isEmpty()) {
//                        if (!absent.equals("0")) {
//                            NoOfEmp.add(new PieEntry(Float.parseFloat(absent), "Absent", 0));
//                        }
//                    }
//                }
//
//                if (present != null) {
//                    if (!present.isEmpty()) {
//                        if (!present.equals("0")) {
//                            NoOfEmp.add(new PieEntry(Float.parseFloat(present),"Present", 1));
//                        }
//                    }
//                }
//
//                if (late != null) {
//                    if (!late.isEmpty()) {
//                        if (!late.equals("0")) {
//                            NoOfEmp.add(new PieEntry(Float.parseFloat(late),"Late", 2));
//
//                        }
//                    }
//                }
//
//                if (early != null) {
//                    if (!early.isEmpty()) {
//                        if (!early.equals("0")) {
//                            NoOfEmp.add(new PieEntry(Float.parseFloat(early),"Early", 3));
//                        }
//                    }
//                }
//
//                if (leave != null) {
//                    if (!leave.isEmpty()) {
//                        if (!leave.equals("0")) {
//                            NoOfEmp.add(new PieEntry(Float.parseFloat(leave),"Leave", 4));
//                        }
//                    }
//                }
//
//                if (holiday != null) {
//                    if (!holiday.isEmpty()) {
//                        if (!holiday.equals("0")) {
//                            NoOfEmp.add(new PieEntry(Float.parseFloat(holiday),"Holiday/Weekend", 5));
//                        }
//                    }
//                }
//
//
//                if (NoOfEmp.size() == 0) {
//                    NoOfEmp.add(new PieEntry(1,"No Data Found", 6));
//
//                }
//
//
//                PieDataSet dataSet = new PieDataSet(NoOfEmp, "");
//                pieChart.animateXY(1000, 1000);
//                pieChart.setEntryLabelColor(Color.TRANSPARENT);
//
//                //pieChart.setExtraRightOffset(50);
//
//
//
//                PieData data = new PieData(dataSet);
////                dataSet.setValueFormatter(new PercentFormatter(pieChart));
//                dataSet.setValueFormatter(new ValueFormatter() {
//                    @Override
//                    public String getFormattedValue(float value) {
//                        return String.valueOf((int) Math.floor(value));
//                    }
//                });
//                String label = dataSet.getValues().get(0).getLabel();
//                System.out.println(label);
//                if (label.equals("No Data Found")) {
//                    dataSet.setValueTextColor(Color.TRANSPARENT);
//                } else {
//                    dataSet.setValueTextColor(Color.BLACK);
//                }
//                dataSet.setHighlightEnabled(true);
//                dataSet.setValueTextSize(12);
//
//                int[] num = new int[NoOfEmp.size()];
//                for (int i = 0; i < NoOfEmp.size(); i++) {
//                    int neki = (int) NoOfEmp.get(i).getData();
//                    num[i] = piecolors[neki];
//                }
//
//                dataSet.setColors(ColorTemplate.createColors(num));
//
//
////                pieChart.setUsePercentValues(true);
//
//
//                pieChart.setData(data);
//                pieChart.invalidate();
//
//                if (dateCount > 0) {
//
//                    if (updatedFiles.size() != 0) {
//                        for (int i = 0; i < updatedFiles.size(); i++) {
//                            String stringFile = updatedFiles.get(i);
//                            File blobFile = new File(stringFile);
//                            if (blobFile.exists()) {
//                                boolean deleted = blobFile.delete();
//                                if (deleted) {
//                                    System.out.println("Deleted");
//                                }
//                            }
//                        }
//                    }
//
//                    if (updatedXml.size() != 0) {
//                        for (int i = 0 ; i < updatedXml.size(); i++) {
//
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                                File dir = new File(getApplicationInfo().dataDir, "shared_prefs/" + updatedXml.get(i)+ ".xml");
//                                if(dir.exists()) {
//                                    getSharedPreferences(updatedXml.get(i), MODE_PRIVATE).edit().clear().apply();
//                                    boolean ddd = dir.delete();
//                                    System.out.println(ddd);
//                                } else {
//                                    System.out.println(false);
//                                }
//                            }
//                        }
//                    }
//
//                    if (dateCount == 1) {
//                        AlertDialog dialog = new AlertDialog.Builder(Attendance.this)
//                                .setTitle("Tracking Service!")
//                                .setMessage(dateCount + " File Uploaded")
//                                .setPositiveButton("OK",null)
//                                .show();
//                        Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
//                        positive.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                dialog.dismiss();
//                            }
//                        });
//                    } else {
//                        AlertDialog dialog = new AlertDialog.Builder(Attendance.this)
//                                .setTitle("Tracking Service!")
//                                .setMessage(dateCount + " Files Uploaded")
//                                .setPositiveButton("OK",null)
//                                .show();
//                        Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
//                        positive.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                dialog.dismiss();
//                            }
//                        });
//                    }
//
//                }
//
//                if (intTimeAttBot != null) {
//                    if (!intTimeAttBot.isEmpty()) {
//                        intTimeAttBot = intTimeAttBot;
//                    }
//                    else {
//                        intTimeAttBot = "--:--";
//                    }
//                }
//                else {
//                    intTimeAttBot = "--:--";
//                }
//                if (outTimeAttBot != null) {
//                    if (!outTimeAttBot.isEmpty()) {
//                        outTimeAttBot = outTimeAttBot;
//                    }
//                    else {
//                        outTimeAttBot = "--:--";
//                    }
//                }
//                else {
//                    outTimeAttBot = "--:--";
//                }
//                if (!outTimeAttBot.equals("--:--") && !intTimeAttBot.equals("--:--")) {
//                    lastLtimAttBot = outTimeAttBot;
//                }
//                else if (outTimeAttBot.equals("--:--") && !intTimeAttBot.equals("--:--")){
//                    lastLtimAttBot = intTimeAttBot;
//                }
//                else {
//                    lastLtimAttBot = "--:--";
//                }
//                conn = false;
//                connected = false;
//
//            }
//            else {
//                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
//                AlertDialog dialog = new AlertDialog.Builder(Attendance.this)
//                        .setMessage("Please Check Your Internet Connection")
//                        .setPositiveButton("Retry", null)
//                        .setNegativeButton("Cancel", null)
//                        .show();
//
//                dialog.setCancelable(false);
//                dialog.setCanceledOnTouchOutside(false);
//                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
//                positive.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        new Check().execute();
//                        dialog.dismiss();
//                    }
//                });
//
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
//
//    public void AttendanceGraph() {
//        try {
//            this.connection = createConnection();
//            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();
//            lastTenDaysFromSQL = new ArrayList<>();
//
//
//            NoOfEmp = new ArrayList<>();
//             absent = "";
//             present = "";
//             leave = "";
//             holiday = "";
//             late = "";
//             early = "";
//             intTimeAttBot = "";
//             outTimeAttBot = "";
//             lastLtimAttBot = "";
//
////             beginDate = "01-JUN-21";
////             lastDate = "30-JUN-21";
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
//            int job_id = 0;
//            String coa_id = "";
//            String divm_id = "";
//            String dept_id = "";
//            ResultSet resultSet2 = stmt.executeQuery("SELECT\n" +
//                    "    emp_mst.emp_timeline_tracker_flag,\n" +
//                    "    emp_mst.emp_job_id,\n" +
//                    "    emp_job_history.job_pri_coa_id,\n" +
//                    "    job_setup_mst.jsm_divm_id,\n" +
//                    "    job_setup_mst.jsm_dept_id,\n" +
//                    "    emp_mst.EMP_LIVE_LOC_TRACKER_FLAG\n" +
//                    "FROM\n" +
//                    "         emp_mst\n" +
//                    "    INNER JOIN emp_job_history ON emp_mst.emp_id = emp_job_history.job_emp_id\n" +
//                    "    INNER JOIN job_setup_dtl ON emp_mst.emp_jsd_id = job_setup_dtl.jsd_id\n" +
//                    "    INNER JOIN job_setup_mst ON job_setup_dtl.jsd_jsm_id = job_setup_mst.jsm_id\n" +
//                    "where\n" +
//                    "    emp_mst.emp_id = "+emp_id+"");
//            while (resultSet2.next()) {
//                tracking_flag = resultSet2.getInt(1);
//                job_id = resultSet2.getInt(2);
//                coa_id = resultSet2.getString(3);
//                divm_id = resultSet2.getString(4);
//                dept_id = resultSet2.getString(5);
//                live_tracking_flag = resultSet2.getInt(6);
//
//            }
//
//
//
//            if (tracking_flag == 1) {
//
//                String elr_id = "";
//                Date c = Calendar.getInstance().getTime();
//
//
//                Timestamp timestamp = new Timestamp(c.getTime());
//                //SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());
//
//                ResultSet rs2 = stmt.executeQuery("SELECT TO_CHAR(ELR_DATE,'DD-MON-RR') ELR_DATE FROM EMP_LOCATION_RECORD WHERE ELR_EMP_ID = "+emp_id+"\n" +
//                        "AND ELR_DATE > SYSDATE-15\n" +
//                        "ORDER BY ELR_DATE ASC\n");
//
//                while (rs2.next()) {
//                    lastTenDaysFromSQL.add(rs2.getString(1));
//                }
//
//                dateCount = 0;
//
//                for (int i = 0; i < lastTenDays.size(); i++) {
//
//                    boolean dateFound = false;
//                    String date = lastTenDays.get(i);
//
//                    for (int j = 0; j < lastTenDaysFromSQL.size(); j++) {
//                        if (date.equals(lastTenDaysFromSQL.get(j))) {
//                            dateFound = true;
//                            System.out.println(date);
//                        }
//                    }
//
//                    if (!dateFound) {
//                        ResultSet resultSet3 = stmt.executeQuery("select nvl(max(elr_id),0)+1 from emp_location_record");
//
//                        while (resultSet3.next()) {
//                            elr_id = resultSet3.getString(1);
//                            System.out.println(elr_id);
//                        }
//
//                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yy",Locale.getDefault());
//
//                        Date gpxDate = null;
//
//                        gpxDate = simpleDateFormat.parse(date);
//
////                    String fileName = simpleDateFormat.format(c);
//                        String fileName = emp_id+"_"+date+"_track";
//
//                        FILE_OF_DAILY_ACTIVITY = fileName;
//
//                        sharedPreferencesDA = getSharedPreferences(FILE_OF_DAILY_ACTIVITY, MODE_PRIVATE);
//
//                        String dist = sharedPreferencesDA.getString(DISTANCE,null);
//                        String totalTime = sharedPreferencesDA.getString(TOTAL_TIME,null);
//                        String stoppedTime = sharedPreferencesDA.getString(STOPPED_TIME,null);
//
//                        PreparedStatement pstmt = connection.prepareStatement("Insert into emp_location_record (ELR_ID, ELR_EMP_ID, ELR_JOB_ID, ELR_COA_ID, ELR_DIVM_ID, ELR_DEPT_ID, ELR_LOCATION_FILE,\n" +
//                                "\n" +
//                                "ELR_FILE_NAME, ELR_MIMETYPE, ELR_CHARACTERSET, ELR_FILETYPE, ELR_DATE, ELR_USER, ELR_FILE_UPDATED_ON, ELR_ACTIVE, TOTAL_DISTANCE_KM, TOTAL_TIME, TOTAL_STOPPED_TIME) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
//
//                        String stringFIle = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + File.separator +  fileName +".gpx";
//
//                        File blobFile = new File(stringFIle);
//
//                        updatedXml.add(fileName);
//
//                        if (blobFile.exists()) {
//                            dateCount++;
//                            InputStream in = new FileInputStream(blobFile);
//                            pstmt.setBinaryStream(7, in, (int)blobFile.length());
//                            pstmt.setInt(15,1);
//                            pstmt.setString(8,fileName);
//                            pstmt.setString(9,"application/gpx+xml");
//                            pstmt.setString(11,".gpx");
//                            updatedFiles.add(stringFIle);
//
//
//                        } else {
//                            pstmt.setBlob(7, (Blob) null);
//                            pstmt.setInt(15,0);
//                            pstmt.setString(8,null);
//                            pstmt.setString(9,null);
//                            pstmt.setString(11,null);
//                        }
//                        pstmt.setInt(1, Integer.parseInt(elr_id));
//                        System.out.println("File paise");
//                        pstmt.setInt(2,Integer.parseInt(emp_id));
//                        System.out.println("File paise");
//                        pstmt.setInt(3,job_id);
//                        System.out.println("File paise");
//                        pstmt.setInt(4,Integer.parseInt(coa_id));
//                        System.out.println("File paise");
//                        pstmt.setInt(5,Integer.parseInt(divm_id));
//                        System.out.println("File paise");
//                        pstmt.setInt(6,Integer.parseInt(dept_id));
//                        System.out.println("File paise");
//
//
//                        pstmt.setString(10,null);
//                        System.out.println("File paise");
//
//                        System.out.println("File paise");
//                        pstmt.setObject(12, new java.sql.Date(gpxDate.getTime()));
//                        System.out.println("File paise");
//                        pstmt.setString(13,emp_code);
//                        System.out.println("File paise");
//                        pstmt.setTimestamp(14,timestamp);
//                        System.out.println("File paise");
//
//                        pstmt.setString(16,dist);
//                        pstmt.setString(17,totalTime);
//                        pstmt.setString(18,stoppedTime);
//
//
//                        pstmt.executeUpdate();
//                        System.out.println("File paise");
//                        connection.commit();
//                        System.out.println("File paise");
//
//                    } else {
//                        System.out.println("Ei "+date +" database e ase");
//                    }
//                }
//            }
//
//            ResultSet resultSet3 = stmt.executeQuery("SELECT DISTINCT TO_CHAR(DA_CHECK.DAC_IN_DATE_TIME,'HH:MI AM') DAC_IN_DATE_TIME, \n" +
//                    "TO_CHAR(DA_CHECK.DAC_OUT_DATE_TIME,'HH:MI AM') DAC_OUT_DATE_TIME\n" +
//                    "FROM EMP_MST,DA_CHECK\n" +
//                    "WHERE DA_CHECK.DAC_EMP_ID = EMP_MST.EMP_ID\n" +
//                    "AND DA_CHECK.DAC_DATE = '"+lastDateForAttBot+"'\n" +
//                    "AND EMP_MST.EMP_ID = "+emp_id+"");
//
//            while (resultSet3.next()) {
//                intTimeAttBot = resultSet3.getString(1);
//                outTimeAttBot = resultSet3.getString(2);
//            }
//            resultSet3.close();
//
//            stmt.close();
//            connected = true;
//
//            connection.close();
//
//        }
//        catch (Exception e) {
//
//            //   Toast.makeText(MainActivity.this, ""+e,Toast.LENGTH_LONG).show();
//            Log.i("ERRRRR", e.getLocalizedMessage());
//            e.printStackTrace();
//        }
//    }

    public void getAttendanceGraph() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;

        NoOfEmp = new ArrayList<>();
        absent = "";
        present = "";
        leave = "";
        holiday = "";
        late = "";
        early = "";

        String attendDataUrl = "http://103.56.208.123:8001/apex/ttrams/dashboard/getAttendanceData/"+beginDate+"/"+lastDate+"/"+emp_id+"";

        RequestQueue requestQueue = Volley.newRequestQueue(Attendance.this);

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
                    getEmpData();
                }
                else {
                    connected = false;
                    updateLayout();
                }
            }
            catch (JSONException e) {
                connected = false;
                e.printStackTrace();
                updateLayout();
            }
        }, error -> {
            conn = false;
            connected = false;
            error.printStackTrace();
            updateLayout();
        });

        requestQueue.add(attendDataReq);
    }

    public void getEmpData() {
        final int[] job_id = {0};
        final String[] coa_id = {""};
        final String[] divm_id = {""};
        final String[] dept_id = {""};
        conn = false;
        connected = false;

        String empDataUrl = "http://103.56.208.123:8001/apex/ttrams/attendance/getEmpData/"+emp_id+"";

        RequestQueue requestQueue = Volley.newRequestQueue(Attendance.this);

        StringRequest empDataReq = new StringRequest(Request.Method.GET, empDataUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject empDataInfo = array.getJSONObject(i);
                        tracking_flag = empDataInfo.getInt("emp_timeline_tracker_flag");
                        job_id[0] = empDataInfo.getInt("emp_job_id");
                        coa_id[0] = empDataInfo.getString("job_pri_coa_id");
                        divm_id[0] = empDataInfo.getString("jsm_divm_id");
                        dept_id[0] = empDataInfo.getString("jsm_dept_id");
                        live_tracking_flag = empDataInfo.getInt("emp_live_loc_tracker_flag");
                    }
                    if (tracking_flag == 1) {
                        getTrackerUploadDate(job_id[0],coa_id[0],divm_id[0],dept_id[0]);
                    }
                    else {
                        getEmpTodayAttData();
                    }
                }
                else {
                    connected = false;
                    updateLayout();
                }
            }
            catch (JSONException e) {
                connected = false;
                e.printStackTrace();
                updateLayout();
            }
        }, error -> {
            conn = false;
            connected = false;
            error.printStackTrace();
            updateLayout();
        });

        requestQueue.add(empDataReq);

    }

    public void getTrackerUploadDate(int job_id, String coa_id, String divm_id, String dept_id) {
        lastTenDaysFromSQL = new ArrayList<>();
        conn = false;
        connected = false;

        String trackerDataUrl = "http://103.56.208.123:8001/apex/ttrams/attendance/getTrackUploadedDate/"+emp_id+"";

        RequestQueue requestQueue = Volley.newRequestQueue(Attendance.this);

        StringRequest trackerDataReq = new StringRequest(Request.Method.GET, trackerDataUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject trackerDataInfo = array.getJSONObject(i);
                        String elr_date = trackerDataInfo.getString("elr_date");
                        lastTenDaysFromSQL.add(elr_date);
                    }
                }
                dateCount = 0;
                getTrackerDate(job_id,coa_id,divm_id,dept_id);

            }
            catch (JSONException e) {
                connected = false;
                e.printStackTrace();
                updateLayout();
            }
        },error -> {
            conn = false;
            connected = false;
            error.printStackTrace();
            updateLayout();
        });

        requestQueue.add(trackerDataReq);

    }

    public void getTrackerDate(int job_id, String coa_id, String divm_id, String dept_id) {
        boolean noDatetoUp = true;
        for (int i = 0; i < lastTenDays.size(); i++) {
            boolean dateFound = false;
            String date = lastTenDays.get(i);

            for (int j = 0; j < lastTenDaysFromSQL.size(); j++) {
                if (date.equals(lastTenDaysFromSQL.get(j))) {
                    dateFound = true;
                    System.out.println(date);
                }
            }
            if (!dateFound) {
                noDatetoUp = false;
                String fileName = emp_id+"_"+date+"_track";

                FILE_OF_DAILY_ACTIVITY = fileName;

                sharedPreferencesDA = getSharedPreferences(FILE_OF_DAILY_ACTIVITY, MODE_PRIVATE);

                String dist = sharedPreferencesDA.getString(DISTANCE,null);
                String totalTime = sharedPreferencesDA.getString(TOTAL_TIME,null);
                String stoppedTime = sharedPreferencesDA.getString(STOPPED_TIME,null);

                String stringFIle = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + File.separator +  fileName +".gpx";

                File blobFile = new File(stringFIle);

                updatedXml.add(fileName);
                byte[] bytes = null;
                if (blobFile.exists()) {
                    dateCount++;
                    try {
                        bytes = method(blobFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    updatedFiles.add(stringFIle);
                }
                uploadEmpTrackerFile(job_id,coa_id,divm_id,dept_id,date,bytes, blobFile,fileName,dist,totalTime,stoppedTime);
                break;
            }
            else {
                System.out.println("Ei "+date +" database e ase");
            }
        }
        if (noDatetoUp) {
            getEmpTodayAttData();
        }
    }

    public void uploadEmpTrackerFile(int job_id, String coa_id, String divm_id, String dept_id, String date, byte[] bytes, File blobFile, String fileName, String dist, String totalTime, String stoppedTime) {

        String uploadFileUrl = "http://103.56.208.123:8001/apex/ttrams/attendance/uploadTrackerFile";

        RequestQueue requestQueue = Volley.newRequestQueue(Attendance.this);

        StringRequest uploadReq = new StringRequest(Request.Method.POST, uploadFileUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String string_out = jsonObject.getString("string_out");
                System.out.println(string_out);
                if (string_out.equals("Successfully Created")) {
                    lastTenDaysFromSQL.add(date);
                    getTrackerDate(job_id,coa_id,divm_id,dept_id);
                }
                else {
                    System.out.println("EKHANE ASHE 3");
                    connected = false;
                    updateLayout();
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
                System.out.println("EKHANE ASHE 0");
                connected = false;
                updateLayout();
            }
        },error -> {
            error.printStackTrace();
            System.out.println("EKHANE ASHE -1");
            conn = false;
            connected = false;
            updateLayout();
        }){
            @Override
            public byte[] getBody() throws AuthFailureError {
                return bytes;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                if (blobFile.exists()) {
                    headers.put("P_ELR_ACTIVE","1");
                    headers.put("P_ELR_FILE_NAME",fileName);
                    headers.put("P_ELR_MIMETYPE","application/gpx+xml");
                    headers.put("P_ELR_FILETYPE",".gpx");
                }
                else {
                    headers.put("P_ELR_ACTIVE","0");
                    headers.put("P_ELR_FILE_NAME",null);
                    headers.put("P_ELR_MIMETYPE",null);
                    headers.put("P_ELR_FILETYPE",null);
                }
                headers.put("P_ELR_EMP_ID",emp_id);
                headers.put("P_ELR_JOB_ID",String.valueOf(job_id));
                headers.put("P_ELR_COA_ID",coa_id);
                headers.put("P_ELR_DIVM_ID",divm_id);
                headers.put("P_ELR_DEPT_ID",dept_id);
                headers.put("P_ELR_DATE",date);
                headers.put("P_ELR_USER",emp_code);
                headers.put("P_TOTAL_DISTANCE_KM",dist);
                headers.put("P_TOTAL_TIME",totalTime);
                headers.put("P_TOTAL_STOPPED_TIME",stoppedTime);
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/binary";
            }
        };

        requestQueue.add(uploadReq);
    }

    public void getEmpTodayAttData() {
        intTimeAttBot = "";
        outTimeAttBot = "";
        lastLtimAttBot = "";

        conn = false;
        connected = false;

        String todayAttDataUrl = "http://103.56.208.123:8001/apex/ttrams/attendance/getTodayAttData/"+emp_id+"/"+lastDateForAttBot+"";

        RequestQueue requestQueue = Volley.newRequestQueue(Attendance.this);

        StringRequest todayAttReq = new StringRequest(Request.Method.GET,todayAttDataUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject todayAttDataInfo = array.getJSONObject(i);
                        intTimeAttBot = todayAttDataInfo.getString("dac_in_date_time")
                                .equals("null") ? "" : todayAttDataInfo.getString("dac_in_date_time");
                        outTimeAttBot = todayAttDataInfo.getString("dac_out_date_time")
                                .equals("null") ? "" : todayAttDataInfo.getString("dac_out_date_time");
                    }
                }
                connected = true;
                updateLayout();
            }
            catch (JSONException e) {
                connected = false;
                e.printStackTrace();
                updateLayout();
            }
        }, error -> {
            conn = false;
            connected = false;
            error.printStackTrace();
            updateLayout();
        });

        requestQueue.add(todayAttReq);
    }

    public static byte[] method(File file) throws IOException {
        FileInputStream fl = new FileInputStream(file);
        byte[] arr = new byte[(int) file.length()];
        fl.read(arr);
        fl.close();
        return arr;
    }

    private void updateLayout() {
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

                //pieChart.setExtraRightOffset(50);



                PieData data = new PieData(dataSet);
//                dataSet.setValueFormatter(new PercentFormatter(pieChart));
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


//                pieChart.setUsePercentValues(true);


                pieChart.setData(data);
                pieChart.invalidate();

                if (dateCount > 0) {

                    if (updatedFiles.size() != 0) {
                        for (int i = 0; i < updatedFiles.size(); i++) {
                            String stringFile = updatedFiles.get(i);
                            File blobFile = new File(stringFile);
                            if (blobFile.exists()) {
                                boolean deleted = blobFile.delete();
                                if (deleted) {
                                    System.out.println("Deleted");
                                }
                            }
                        }
                    }

                    if (updatedXml.size() != 0) {
                        for (int i = 0 ; i < updatedXml.size(); i++) {

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                File dir = new File(getApplicationInfo().dataDir, "shared_prefs/" + updatedXml.get(i)+ ".xml");
                                if(dir.exists()) {
                                    getSharedPreferences(updatedXml.get(i), MODE_PRIVATE).edit().clear().apply();
                                    boolean ddd = dir.delete();
                                    System.out.println(ddd);
                                } else {
                                    System.out.println(false);
                                }
                            }
                        }
                    }

                    if (dateCount == 1) {
                        AlertDialog dialog = new AlertDialog.Builder(Attendance.this)
                                .setTitle("Tracking Service!")
                                .setMessage(dateCount + " File Uploaded")
                                .setPositiveButton("OK",null)
                                .show();
                        Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        positive.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                    } else {
                        AlertDialog dialog = new AlertDialog.Builder(Attendance.this)
                                .setTitle("Tracking Service!")
                                .setMessage(dateCount + " Files Uploaded")
                                .setPositiveButton("OK",null)
                                .show();
                        Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        positive.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                    }

                }

                if (intTimeAttBot != null) {
                    if (!intTimeAttBot.isEmpty()) {
                        intTimeAttBot = intTimeAttBot;
                    }
                    else {
                        intTimeAttBot = "--:--";
                    }
                }
                else {
                    intTimeAttBot = "--:--";
                }
                if (outTimeAttBot != null) {
                    if (!outTimeAttBot.isEmpty()) {
                        outTimeAttBot = outTimeAttBot;
                    }
                    else {
                        outTimeAttBot = "--:--";
                    }
                }
                else {
                    outTimeAttBot = "--:--";
                }
                if (!outTimeAttBot.equals("--:--") && !intTimeAttBot.equals("--:--")) {
                    lastLtimAttBot = outTimeAttBot;
                }
                else if (outTimeAttBot.equals("--:--") && !intTimeAttBot.equals("--:--")){
                    lastLtimAttBot = intTimeAttBot;
                }
                else {
                    lastLtimAttBot = "--:--";
                }
                conn = false;
                connected = false;
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(Attendance.this)
                        .setMessage("There is a network issue in the server. Please Try later")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel", null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        getAttendanceGraph();
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
        else {
            AlertDialog dialog = new AlertDialog.Builder(Attendance.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel", null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    getAttendanceGraph();
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