package ttit.com.shuvo.ikglhrm.attendance.report;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dewinjm.monthyearpicker.MonthFormat;
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialog;
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ttit.com.shuvo.ikglhrm.EmployeeInfo.jobDesc.JobAdapter;
import ttit.com.shuvo.ikglhrm.EmployeeInfo.jobDesc.JobDescDetails;
import ttit.com.shuvo.ikglhrm.EmployeeInfo.jobDesc.JobDescription;
import ttit.com.shuvo.ikglhrm.R;
import ttit.com.shuvo.ikglhrm.WaitProgress;
import ttit.com.shuvo.ikglhrm.attendance.approve.AttendanceApprove;
import ttit.com.shuvo.ikglhrm.attendance.update.AttendanceReqType;
import ttit.com.shuvo.ikglhrm.attendance.update.AttendanceUpdate;

import static ttit.com.shuvo.ikglhrm.Login.userInfoLists;
import static ttit.com.shuvo.ikglhrm.OracleConnection.DEFAULT_USERNAME;
import static ttit.com.shuvo.ikglhrm.OracleConnection.createConnection;

public class AttendanceReport extends AppCompatActivity {


    private ProgressDialog pDialog;
    String firstDate = "";
    String lastDate = "";

    Button reportFinish;

    TextInputEditText selecetMonth;
    TextInputLayout selectMonthLay;

    MaterialButton downlaodReport;
    Button showReport;
    TextView errorReport;

    private int mYear, mMonth, mDay;

    CardView report;
    RecyclerView reportview;
    RecyclerView.LayoutManager layoutManager;
    AttenReportAdapter attenReportAdapter;

    TextView fromToDate;
    TextInputEditText empName;
    TextInputEditText empJID;
    TextInputEditText empBand;
    TextInputEditText empStrDes;
    TextInputEditText empFunDes;
    TextInputEditText empjobNo;
    TextInputEditText empStatus;
    TextInputEditText empShift;
    TextInputEditText empDiv;
    TextInputEditText empDep;
    TextInputEditText empJoin;
    TextInputEditText empPemLoc;
    TextInputEditText empSecLoc;
    TextView dateToDate;

    TextView calenderDays;
    TextView totalWorking;
    TextView presentDays;
    TextView absentDays;
    TextView weeklyHolidays;
    TextView holidays;
    TextView workWeekend;
    TextView workHolidays;


    public static ArrayList<ReportInformation> reportInformations;
    public static ArrayList<AttenReportList> attenReportLists;

    WaitProgress waitProgress = new WaitProgress();
    private String message = null;
    private Boolean conn = false;
    private Boolean infoConnected = false;
    private Boolean connected = false;

    private Connection connection;

    LinearLayout attenData;
    LinearLayout attenDataNot;

    String emp_id = "";
    String URL = "";

    String selected_month_full = "";
    String year_full = "";

    String coa_id = "";
    String present_days = "";
    String absent_days = "";
    String weekend = "";
    String present_weekend = "";
    String hol = "";
    String present_holi = "";

    String working_days = "";


    int daysInMonth = 0;

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
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(AttendanceReport.this,R.color.secondaryColor));

        setContentView(R.layout.activity_attendance_report);



        fromToDate = findViewById(R.id.from_to_date);
        empName = findViewById(R.id.name_report);
        empBand = findViewById(R.id.band_report);
        empDep = findViewById(R.id.department_report);
        empDiv = findViewById(R.id.division_report);
        empFunDes = findViewById(R.id.func_designation_report);
        empJID = findViewById(R.id.id_report);
        empjobNo = findViewById(R.id.job_no_report);
        empPemLoc = findViewById(R.id.prm_locat_report);
        empJoin = findViewById(R.id.joining_report);
        empSecLoc = findViewById(R.id.sec_loc_report);
        empShift = findViewById(R.id.shift_report);
        empStatus = findViewById(R.id.status_report);
        empStrDes = findViewById(R.id.str_designation_report);
        dateToDate = findViewById(R.id.date_from_report);

        reportFinish = findViewById(R.id.report_finish);

        selecetMonth = findViewById(R.id.select_month_att_report);
        selectMonthLay = findViewById(R.id.select_month_att_report_lay);

        attenData = findViewById(R.id.attendancebefore_text);
        attenDataNot = findViewById(R.id.no_data_msg_attendance);


        downlaodReport = findViewById(R.id.download_report);
        showReport = findViewById(R.id.show_report);
        errorReport = findViewById(R.id.error_report_msg);

        report = findViewById(R.id.report_card);

        reportview = findViewById(R.id.attnd_list_view);

        calenderDays = findViewById(R.id.days_in_month);
        totalWorking = findViewById(R.id.working_days_in_month);
        presentDays = findViewById(R.id.present_days_in_month);
        absentDays = findViewById(R.id.absent_days_in_month);
        weeklyHolidays = findViewById(R.id.weekly_holidays_days_in_month);
        holidays = findViewById(R.id.holidays_days_in_month);
        workWeekend = findViewById(R.id.work_weekend_days_in_month);
        workHolidays = findViewById(R.id.work_on_holi_days_in_month);



        emp_id = userInfoLists.get(0).getEmp_id();

        reportInformations = new ArrayList<>();
        attenReportLists = new ArrayList<>();



        reportview.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        reportview.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(reportview.getContext(),DividerItemDecoration.VERTICAL);
        reportview.addItemDecoration(dividerItemDecoration);



        reportFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        selecetMonth.setOnClickListener(new View.OnClickListener() {
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

                        selected_month_full = monthName;
                        year_full = String.valueOf(year);
                        firstDate = "01-"+mon+"-"+yearName;
                        //selected_date = "01-"+mon+"-"+yearName;
                        selecetMonth.setText(monthName + "-" + year);
                        selectMonthLay.setHint("Month");
                        SimpleDateFormat sss = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());

                        Date today = null;
                        try {
                            today = sss.parse(firstDate);
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

                        YearMonth yearMonthObject = null;

                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            yearMonthObject = YearMonth.of(Integer.parseInt(year_full), month);
                            daysInMonth = yearMonthObject.lengthOfMonth();
                            System.out.println(daysInMonth);
                        }

                        new Check().execute();
                    }
                });

            }
        });


//        beginDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final Calendar c = Calendar.getInstance();
//                mYear = c.get(Calendar.YEAR);
//                mMonth = c.get(Calendar.MONTH);
//                mDay = c.get(Calendar.DAY_OF_MONTH);
//
//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//                    DatePickerDialog datePickerDialog = new DatePickerDialog(AttendanceReport.this, new DatePickerDialog.OnDateSetListener() {
//                        @Override
//                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//
//                            String monthName = "";
//                            String dayOfMonthName = "";
//                            String yearName = "";
//                            month = month + 1;
//                            if (month == 1) {
//                                monthName = "JAN";
//                            } else if (month == 2) {
//                                monthName = "FEB";
//                            } else if (month == 3) {
//                                monthName = "MAR";
//                            } else if (month == 4) {
//                                monthName = "APR";
//                            } else if (month == 5) {
//                                monthName = "MAY";
//                            } else if (month == 6) {
//                                monthName = "JUN";
//                            } else if (month == 7) {
//                                monthName = "JUL";
//                            } else if (month == 8) {
//                                monthName = "AUG";
//                            } else if (month == 9) {
//                                monthName = "SEP";
//                            } else if (month == 10) {
//                                monthName = "OCT";
//                            } else if (month == 11) {
//                                monthName = "NOV";
//                            } else if (month == 12) {
//                                monthName = "DEC";
//                            }
//
//                            if (dayOfMonth <= 9) {
//                                dayOfMonthName = "0" + String.valueOf(dayOfMonth);
//                            } else {
//                                dayOfMonthName = String.valueOf(dayOfMonth);
//                            }
//                            yearName  = String.valueOf(year);
//                            yearName = yearName.substring(yearName.length()-2);
//                            System.out.println(yearName);
//                            System.out.println(dayOfMonthName);
//                            beginDate.setText(dayOfMonthName + "-" + monthName + "-" + yearName);
//
//                        }
//                    }, mYear, mMonth, mDay);
//                    datePickerDialog.show();
//                }
//            }
//        });
//
//        endDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final Calendar c = Calendar.getInstance();
//                mYear = c.get(Calendar.YEAR);
//                mMonth = c.get(Calendar.MONTH);
//                mDay = c.get(Calendar.DAY_OF_MONTH);
//
//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//                    DatePickerDialog datePickerDialog = new DatePickerDialog(AttendanceReport.this, new DatePickerDialog.OnDateSetListener() {
//                        @Override
//                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//
//                            String monthName = "";
//                            String dayOfMonthName = "";
//                            String yearName = "";
//                            month = month + 1;
//                            if (month == 1) {
//                                monthName = "JAN";
//                            } else if (month == 2) {
//                                monthName = "FEB";
//                            } else if (month == 3) {
//                                monthName = "MAR";
//                            } else if (month == 4) {
//                                monthName = "APR";
//                            } else if (month == 5) {
//                                monthName = "MAY";
//                            } else if (month == 6) {
//                                monthName = "JUN";
//                            } else if (month == 7) {
//                                monthName = "JUL";
//                            } else if (month == 8) {
//                                monthName = "AUG";
//                            } else if (month == 9) {
//                                monthName = "SEP";
//                            } else if (month == 10) {
//                                monthName = "OCT";
//                            } else if (month == 11) {
//                                monthName = "NOV";
//                            } else if (month == 12) {
//                                monthName = "DEC";
//                            }
//
//                            if (dayOfMonth <= 9) {
//                                dayOfMonthName = "0" + String.valueOf(dayOfMonth);
//                            } else {
//                                dayOfMonthName = String.valueOf(dayOfMonth);
//                            }
//                            yearName  = String.valueOf(year);
//                            yearName = yearName.substring(yearName.length()-2);
//                            System.out.println(yearName);
//                            System.out.println(dayOfMonthName);
//                            endDate.setText(dayOfMonthName + "-" + monthName + "-" + yearName);
//
//                        }
//                    }, mYear, mMonth, mDay);
//                    datePickerDialog.show();
//                }
//            }
//        });
//
//
//        showReport.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                errorReport.setVisibility(View.GONE);
//                downlaodReport.setVisibility(View.GONE);
//                report.setVisibility(View.GONE);
//                firstDate = beginDate.getText().toString();
//                lastDate = endDate.getText().toString();
//                System.out.println(firstDate);
//                System.out.println(lastDate);
//
//                if (firstDate.isEmpty() || lastDate.isEmpty() ) {
//                    Toast.makeText(getApplicationContext(), "Please Give Proper Date", Toast.LENGTH_SHORT).show();
//                    errorReport.setVisibility(View.VISIBLE);
//                    errorReport.setText("You must provide Date to get Attendance Report");
//
//                } else {
//
//                    Date bDate = null;
//                    Date eDate = null;
//
//                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy", Locale.getDefault());
//
//                    try {
//                        bDate = sdf.parse(firstDate);
//                        eDate = sdf.parse(lastDate);
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//
//                    if (bDate != null && eDate != null) {
//                        if (eDate.after(bDate)) {
//                            //Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_SHORT).show();
//
//
//                            new Check().execute();
//
//                        }
//                        else {
//                            errorReport.setVisibility(View.VISIBLE);
//                            downlaodReport.setVisibility(View.GONE);
//                            report.setVisibility(View.GONE);
//                            errorReport.setText("Your End Date is Greater than Begin Date. Please Provide Accurate Date");
//                        }
//                    }
//
//
//                }
//            }
//        });

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat sss = new SimpleDateFormat("MMM-yyyy", Locale.getDefault());
        SimpleDateFormat sdff = new SimpleDateFormat("dd-MMM-yyyy",Locale.getDefault());
        firstDate = sss.format(c);
        firstDate = "01-" + firstDate;
        System.out.println(firstDate);

        Date today = null;
        try {
            today = sdff.parse(firstDate);
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

            lastDate = sdff.format(lastDayOfMonth);
            System.out.println(lastDate);

        }

        SimpleDateFormat month_date = new SimpleDateFormat("MMMM",Locale.getDefault());
        String month_name = month_date.format(c);
        month_name = month_name.toUpperCase();
        System.out.println(month_name);

        SimpleDateFormat monthNumb = new SimpleDateFormat("MM",Locale.getDefault());
        String monthNNN = monthNumb.format(c);


        SimpleDateFormat presentYear = new SimpleDateFormat("yyyy",Locale.getDefault());
        String yyyy = presentYear.format(c);

        selecetMonth.setText(month_name+"-"+yyyy);
        selectMonthLay.setHint("Month");

        YearMonth yearMonthObject = null;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            yearMonthObject = YearMonth.of(Integer.parseInt(yyyy), Integer.parseInt(monthNNN));
            daysInMonth = yearMonthObject.lengthOfMonth();
            System.out.println(daysInMonth);
        }


        new Check().execute();


        downlaodReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(AttendanceReport.this);
                builder.setTitle("Download Attendance Report!")
                        .setMessage("Do you want to download this report?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                new DownloadPDF().execute();

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
        });

    }

    public void Download(String url, String title) {

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        String tempTitle = title.replace(" ", "_");
        request.setTitle(tempTitle);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        }

        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, tempTitle+".pdf");
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        request.setMimeType("application/pdf");
        request.allowScanningByMediaScanner();
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        downloadManager.enqueue(request);
        infoConnected = true;

    }

    public boolean isConnected() {
        boolean connected = false;
        boolean isMobile = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
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

    public class DownloadPDF extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(getApplicationContext());
            pDialog.setMessage("Downloading...");
            pDialog.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isConnected() && isOnline()) {

                Download(URL, firstDate+" to "+lastDate);

            } else {
                infoConnected = false;
//                message = "Not Connected";
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            pDialog.dismiss();
            if (infoConnected) {
                Toast.makeText(getApplicationContext(), "Downloading...", Toast.LENGTH_SHORT).show();
                infoConnected = false;
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(AttendanceReport.this)
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

                        new DownloadPDF().execute();
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

            waitProgress.show(getSupportFragmentManager(),"WaitBar");
            waitProgress.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isConnected() && isOnline()) {

                ReportDetails();
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

                report.setVisibility(View.VISIBLE);
                downlaodReport.setVisibility(View.VISIBLE);
                attenReportAdapter = new AttenReportAdapter(attenReportLists, AttendanceReport.this);
                reportview.setAdapter(attenReportAdapter);
                //setListViewHeightBasedOnChildren(reportview);
//                int originalItemSize = attenReportLists.size();
//
//                ViewGroup.LayoutParams params = reportview.getLayoutParams();
//
//                if (attenReportLists.size() > originalItemSize ){
//                    params.height = params.height + 100;
//                    originalItemSize  = attenReportLists.size();
//                    reportview.setLayoutParams(params);
//                }

                if (attenReportLists.size() == 0) {
                    attenDataNot.setVisibility(View.VISIBLE);
                    attenData.setVisibility(View.GONE);
                } else {
                    attenData.setVisibility(View.VISIBLE);
                    attenDataNot.setVisibility(View.GONE);
                }

                for (int i = 0; i < reportInformations.size(); i++) {
                    empName.setText(reportInformations.get(i).getName());
                    empBand.setText(reportInformations.get(i).getBand());
                    empDep.setText(reportInformations.get(i).getDepartment());
                    empDiv.setText(reportInformations.get(i).getDivision());
                    empFunDes.setText(reportInformations.get(i).getFun_des());
                    empJID.setText(reportInformations.get(i).getId());
                    empjobNo.setText(reportInformations.get(i).getJob_no());
                    empJoin.setText(reportInformations.get(i).getJoining());
                    empPemLoc.setText(reportInformations.get(i).getPrm_loc());
                    empSecLoc.setText(reportInformations.get(i).getSec_loc());
                    empShift.setText(reportInformations.get(i).getShift());
                    empStatus.setText(reportInformations.get(i).getStatus());
                    empStrDes.setText(reportInformations.get(i).getStr_des());

                }
                dateToDate.setText(firstDate + " to "+ lastDate);
                fromToDate.setText("'From: " + firstDate+ " To "+lastDate+ ", Employee: "+ reportInformations.get(0).getName()+"'");

                calenderDays.setText(String.valueOf(daysInMonth));
                totalWorking.setText(working_days);
                presentDays.setText(present_days);
                absentDays.setText(absent_days);
                weeklyHolidays.setText(weekend);
                holidays.setText(hol);
                workWeekend.setText(present_weekend);
                workHolidays.setText(present_holi);




            }else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(AttendanceReport.this)
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
                        dialog.dismiss();
                        finish();
                    }
                });
            }
        }
    }

    public void ReportDetails() {
        try {
            this.connection = createConnection();
            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();


            attenReportLists = new ArrayList<>();
            reportInformations = new ArrayList<>();

             present_days = "";
             absent_days = "";
             weekend = "";
             present_weekend = "";
             hol = "";
             present_holi = "";

             working_days = "";

//            attenReportLists.add(new AttenReportList("Date","Status","Shift","Punch Location","In Time",
//                    "In Status", "Out Time", "Out Status"));
            Statement stmt = connection.createStatement();




            ResultSet rs=stmt.executeQuery("SELECT DISTINCT TO_CHAR(DA_CHECK.DAC_IN_DATE_TIME,'HH:MI:SS AM') DAC_IN_DATE_TIME, \n" +
                    "TO_CHAR(DA_CHECK.DAC_LATE_AFTER,'HH:MI:SS AM') DAC_LATE_AFTER, \n" +
                    "TO_CHAR(DA_CHECK.DAC_EARLY_BEFORE,'HH:MI:SS AM') DAC_EARLY_BEFORE, \n" +
                    "DA_CHECK.DAC_END_TIME, \n" +
                    "TO_CHAR(DA_CHECK.DAC_OUT_DATE_TIME,'HH:MI:SS AM') DAC_OUT_DATE_TIME, \n" +
                    "TO_CHAR(DA_CHECK.DAC_DATE,'DD-MON-YY') DAC_DATE1 , DA_CHECK.DAC_ATTN_STATUS, \n" +
                    "TO_DATE(TO_CHAR(DA_CHECK.DAC_DATE, 'MONTH RRRR'), 'MONTH RRRR') MONTH_YEAR, \n" +
                    "DA_CHECK.DAC_OVERTIME_AVAIL_FLAG, LEAVE_CATEGORY.LC_NAME, DA_CHECK.DAC_NOTES, \n" +
                    "OFFICE_SHIFT_MST.OSM_NAME, COMPANY_OFFICE_ADDRESS_A2.COA_NAME, \n" +
                    "DA_CHECK.DAC_AMS_MECHINE_CODE, COMPANY_OFFICE_ADDRESS.COA_ID,NVL(DA_CHECK.DAC_LATE_FLAG,0) DAC_LATE_FLAG,DAC_LEAVE_CONSUM_LC_ID, DAC_LEAVE_TYPE,DA_CHECK.DAC_DATE, ATTENDANCE_MECHINE_SETUP.AMS_COA_ID in_machine_coa_ID, out_machine.AMS_COA_ID out_machine_coa_id,\n" +
                    "DA_CHECK.DAC_IN_ATTD_LATITUDE,DA_CHECK.DAC_IN_ATTD_LONGITUDE, DA_CHECK.DAC_OUT_ATTD_LATITUDE,DA_CHECK.DAC_OUT_ATTD_LONGITUDE,ELR_ID\n" +
                    "FROM EMP_MST,EMP_LOCATION_RECORD, JOB_SETUP_DTL, JOB_SETUP_MST, DEPT_MST, DIVISION_MST, EMP_JOB_HISTORY, EMP_ADOPTED_HISTORY, DESIG_MST, COMPANY_OFFICE_ADDRESS, COMPANY_OFFICE_ADDRESS COMPANY_OFFICE_ADDRESS_A1, DA_CHECK, LEAVE_CATEGORY, OFFICE_SHIFT_MST, ATTENDANCE_MECHINE_SETUP,ATTENDANCE_MECHINE_SETUP out_machine, COMPANY_OFFICE_ADDRESS COMPANY_OFFICE_ADDRESS_A2\n" +
                    "WHERE ((JOB_SETUP_DTL.JSD_JSM_ID = JOB_SETUP_MST.JSM_ID)\n" +
                    "AND (DA_CHECK.DAC_EMP_ID = EMP_LOCATION_RECORD.ELR_EMP_ID(+)\n" +
                    "AND DA_CHECK.DAC_DATE = EMP_LOCATION_RECORD.ELR_DATE(+))\n" +
                    " AND (JOB_SETUP_MST.JSM_DEPT_ID = DEPT_MST.DEPT_ID)\n" +
                    " AND (JOB_SETUP_MST.JSM_DIVM_ID = DIVISION_MST.DIVM_ID)\n" +
                    " AND (EMP_ADOPTED_HISTORY.EAH_JOB_ID(+) = EMP_JOB_HISTORY.JOB_ID)\n" +
                    " AND (JOB_SETUP_MST.JSM_DESIG_ID = DESIG_MST.DESIG_ID)\n" +
                    " AND (EMP_JOB_HISTORY.JOB_PRI_COA_ID = COMPANY_OFFICE_ADDRESS.COA_ID)\n" +
                    " AND (JOB_SETUP_DTL.JSD_ID = EMP_MST.EMP_JSD_ID)\n" +
                    " AND (EMP_JOB_HISTORY.JOB_ID = EMP_MST.EMP_JOB_ID)\n" +
                    " AND (COMPANY_OFFICE_ADDRESS_A1.COA_ID(+) = EMP_JOB_HISTORY.JOB_SEC_COA_ID)\n" +
                    " AND (DA_CHECK.DAC_EMP_ID = EMP_MST.EMP_ID)\n" +
                    " AND (DA_CHECK.DAC_LC_ID = LEAVE_CATEGORY.LC_ID(+))\n" +
                    " AND (DA_CHECK.DAC_OSM_ID = OFFICE_SHIFT_MST.OSM_ID(+))\n" +
                    " AND (DA_CHECK.DAC_AMS_MECHINE_CODE = ATTENDANCE_MECHINE_SETUP.AMS_MECHINE_CODE(+))\n" +
                    " AND (DA_CHECK.DAC_OUT_MACHINE_CODE = out_machine.AMS_MECHINE_CODE(+))\n" +
                    " AND (ATTENDANCE_MECHINE_SETUP.AMS_COA_ID = COMPANY_OFFICE_ADDRESS_A2.COA_ID(+)))\n" +
                    " AND DA_CHECK.DAC_DATE BETWEEN '"+firstDate+"' AND '"+lastDate+"'\n" +
                    "AND EMP_MST.EMP_ID = "+emp_id+"\n" +
                    "ORDER BY DA_CHECK.DAC_DATE");



            while(rs.next()) {

                String date = rs.getString(6);
                String elr_id = rs.getString(26);
                String statusShort = rs.getString(7);
                String status = "";
                String attStatus = "";
                String inCode = rs.getString(20);
                String outCode = rs.getString(21);
                String in_lat = rs.getString(22);
                String in_lon = rs.getString(23);
                String out_lat = rs.getString(24);
                String out_lon = rs.getString(25);

                if (in_lat == null) {
                    in_lat = "";
                }
                if (in_lon == null) {
                    in_lon = "";
                }
                if (out_lat == null) {
                    out_lat = "";
                }
                if (out_lon == null) {
                    out_lon = "";
                }

                if (statusShort != null) {
                    if (rs.getString(1) != null && rs.getString(5) == null) {
                        status = "Out Miss";
                        attStatus = "Out Miss";
                    }else if (statusShort.equals("L") || statusShort.equals("LW") || statusShort.equals("LH")) {
                        status = rs.getString(10);
                        attStatus = "In Leave";
                    } else if (statusShort.equals("H")) {
                        status = "Holiday";
                        attStatus = "Off Day";
                    } else if (statusShort.equals("W")) {
                        status = "Weekend";
                        attStatus = "Off Day";
                    } else if (statusShort.equals("PL") || statusShort.equals("PLH") || statusShort.equals("PLW")) {
                        status = "Present & Leave";
                        attStatus = "Present on Leave Day";
                    } else if (statusShort.equals("PAT")) {
                        status = "Attended training";
                        attStatus = "White";
                    } else if (statusShort.equals("PHD") || statusShort.equals("PWD") || statusShort.equals("PLHD") || statusShort.equals("PLWD")) {
                        status = "Present & Day Off Taken";
                        attStatus = "White";
                    } else if (statusShort.equals("P") || statusShort.equals("PWWO") || statusShort.equals("PHWC") || statusShort.equals("PWWC") || statusShort.equals("PA")) {
                        status = "Present";
                        attStatus = "White";
                    } else if (statusShort.equals("PW") || statusShort.equals("PH")) {
                        status = "Present & Off Day";
                        attStatus = "Present on Off Day";
                    } else if (statusShort.equals("A")) {
                        status = "Absent";
                        attStatus = "Absent";
                    } else if (statusShort.equals("PT")) {
                        status = "Tour";
                        attStatus = "White";
                    } else if (statusShort.equals("SP")) {
                        status = "Suspend";
                        attStatus = "White";
                    } else {
                        status = "Absent";
                        attStatus = "Absent";
                    }
                } else {
                    status = "No Status";
                    attStatus = "White";
                }



                if (inCode != null && outCode != null) {
                    int in = Integer.parseInt(inCode);
                    int out = Integer.parseInt(outCode);
                    if (in != out ) {
                        attStatus = "Multi Station";
                    }
                }

                String shift = rs.getString(12);

                if (shift == null) {
                    shift = "";
                }
                if (rs.getString(10) != null) {
                    shift = "";
                }
                if (status.equals("Weekend")) {
                    shift = "";
                }

                String pl = rs.getString(13);
                if (pl == null) {
                    pl = "";
                }

                String inTime = rs.getString(1);
                String lateAfter = rs.getString(2);

                Date in = null;
                Date late = null;

                String inStatus = "";

                SimpleDateFormat tt = new SimpleDateFormat("hh:mm:ss aa", Locale.getDefault());
                SimpleDateFormat newtt = new SimpleDateFormat("hh:mm aa",Locale.getDefault());

                if (inTime == null) {
                    inTime = "";
                    inStatus = "";
                }
                else {
                    try {
                        in = tt.parse(inTime);
                        late = tt.parse(lateAfter);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if (in != null && late != null) {
                        inTime = newtt.format(in);
                        if (late.after(in)) {
                            inStatus = "";
                        } else {
                            inStatus = "Late";
                        }
                    }
                }


                String outTime = rs.getString(5);
                String earlyB = rs.getString(3);

                String outStatus = "";

                if (outTime == null) {
                    outTime = "";
                    outStatus = "";
                } else {
                    try {
                        in = tt.parse(outTime);
                        late = tt.parse(earlyB);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if (in != null && late != null) {
                        outTime = newtt.format(in);
                        if (late.after(in)) {
                            outStatus = "Early";
                        } else {
                            outStatus = "";
                        }
                    }
                }

                Blob blob = null;

                if (elr_id != null) {
                    PreparedStatement ps = connection.prepareStatement("Select ELR_FILE_NAME, ELR_FILETYPE , ELR_LOCATION_FILE from EMP_LOCATION_RECORD where ELR_ID = "+elr_id+"");
                    ResultSet resultSetBlob = ps.executeQuery();

                    while (resultSetBlob.next()) {
                        blob = resultSetBlob.getBlob(3);
                    }
                } else {
                    blob = null;
                }

                if (blob != null) {
                    System.out.println("Ei "+ date+ " e BLOB ase");
                } else if (blob == null){
                    System.out.println("Ei "+ date+ " e BLOB Null");
                }




                attenReportLists.add(new AttenReportList(date,status,shift,pl,inTime,inStatus,outTime,outStatus,attStatus,in_lat,in_lon,out_lat,out_lon,blob,elr_id));

            }


            ResultSet resultSet = stmt.executeQuery("SELECT DISTINCT EMP_MST.EMP_NAME,\n" +
                    "EMP_MST.EMP_CODE, JOB_SETUP_MST.JSM_CODE, JOB_SETUP_MST.JSM_NAME,\n" +
                    "EMP_JOB_HISTORY.JOB_SHIFT, \n" +
                    "EMP_JOB_HISTORY.JOB_EMAIL, \n" +
                    "DESIG_MST.DESIG_PRIORITY, EMP_JOB_HISTORY.JOB_CALLING_TITLE, \n" +
                    "DEPT_MST.DEPT_NAME, DIVISION_MST.DIVM_NAME,\n" +
                    "COMPANY_OFFICE_ADDRESS.COA_NAME, COMPANY_OFFICE_ADDRESS_A1.COA_NAME, \n" +
                    "TO_CHAR(EMP_JOB_HISTORY.JOB_ACTUAL_DATE,'DD-MON-YY') JOB_ACTUAL_DATE, EMP_JOB_HISTORY.JOB_STATUS,\n" +
                    "COMPANY_OFFICE_ADDRESS.COA_ID\n" +
                    "FROM EMP_MST, JOB_SETUP_DTL, JOB_SETUP_MST, DEPT_MST, DIVISION_MST, EMP_JOB_HISTORY, DESIG_MST, COMPANY_OFFICE_ADDRESS, COMPANY_OFFICE_ADDRESS COMPANY_OFFICE_ADDRESS_A1\n" +
                    "WHERE EMP_MST.EMP_ID = "+emp_id+"\n" +
                    "  AND ((JOB_SETUP_DTL.JSD_JSM_ID = JOB_SETUP_MST.JSM_ID)\n" +
                    " AND (JOB_SETUP_MST.JSM_DEPT_ID = DEPT_MST.DEPT_ID)\n" +
                    " AND (JOB_SETUP_MST.JSM_DIVM_ID = DIVISION_MST.DIVM_ID)\n" +
                    " AND (JOB_SETUP_MST.JSM_DESIG_ID = DESIG_MST.DESIG_ID)\n" +
                    " AND (EMP_JOB_HISTORY.JOB_PRI_COA_ID = COMPANY_OFFICE_ADDRESS.COA_ID)\n" +
                    " AND (JOB_SETUP_DTL.JSD_ID = EMP_MST.EMP_JSD_ID)\n" +
                    " AND (EMP_JOB_HISTORY.JOB_ID = EMP_MST.EMP_JOB_ID)\n" +
                    " AND (COMPANY_OFFICE_ADDRESS_A1.COA_ID(+) = EMP_JOB_HISTORY.JOB_SEC_COA_ID))");

            while (resultSet.next()) {
                reportInformations.add(new ReportInformation(resultSet.getString(1),resultSet.getString(2),resultSet.getString(7),
                        resultSet.getString(4), resultSet.getString(8), resultSet.getString(3),
                        resultSet.getString(14), resultSet.getString(5), resultSet.getString(10),
                        resultSet.getString(9), resultSet.getString(13), resultSet.getString(11),
                        resultSet.getString(12)));
                coa_id = resultSet.getString(15);

            }


            ResultSet resultSet1 = stmt.executeQuery("SELECT SUM (CASE WHEN DAC_ATTN_STATUS IN ('P', 'PA') THEN 1 ELSE 0 END)\n" +
                    "          PRESENT_STATUS,\n" +
                    "       SUM (CASE WHEN DAC_ATTN_STATUS IN ('A') THEN 1 ELSE 0 END)\n" +
                    "          ABSENT_STATUS,\n" +
                    "       SUM (CASE WHEN DAC_ATTN_STATUS IN ('W') THEN 1 ELSE 0 END)\n" +
                    "          WEEKEND_STATUS,\n" +
                    "       SUM (CASE WHEN DAC_ATTN_STATUS IN ('PW') THEN 1 ELSE 0 END)\n" +
                    "          PRESENT_WEEKEND,\n" +
                    "       SUM (CASE WHEN DAC_ATTN_STATUS IN ('H') THEN 1 ELSE 0 END)\n" +
                    "          HOLIDAY_STATUS,\n" +
                    "       SUM (CASE WHEN DAC_ATTN_STATUS IN ('PH') THEN 1 ELSE 0 END)\n" +
                    "          PRESENT_HOLIDAY_STATUS\n" +
                    "  FROM DA_CHECK, EMP_MST\n" +
                    " WHERE     DA_CHECK.DAC_EMP_ID = EMP_MST.EMP_ID\n" +
                    "       AND TRUNC (DA_CHECK.DAC_DATE) BETWEEN TRUNC (TO_DATE ('"+firstDate+"'))\n" +
                    "                                         AND TO_DATE ('"+lastDate+"')\n" +
                    "       AND EMP_MST.EMP_ID = "+emp_id+"");

            while (resultSet1.next()) {
                present_days = resultSet1.getString(1);
                absent_days = resultSet1.getString(2);
                weekend = resultSet1.getString(3);
                present_weekend = resultSet1.getString(4);
                hol = resultSet1.getString(5);
                present_holi = resultSet1.getString(6);
            }

            ResultSet resultSet2 = stmt.executeQuery("SELECT COUNT (HOC_DATE)\n" +
                    "  FROM HRM_OFFICIAL_CALENDAR\n" +
                    " WHERE     HOC_DATE BETWEEN '"+firstDate+"' AND '"+lastDate+"'\n" +
                    "       AND HOC_DAY_CAT = 1\n" +
                    "       AND HOC_COA_ID = '"+coa_id+"'");

            while (resultSet2.next()) {
                working_days = resultSet2.getString(1);
            }

            String criteria = "From: "+firstDate+" To "+lastDate+", Employee: "+reportInformations.get(0).getName()+"";
            if (DEFAULT_USERNAME.equals("IKGL")) {
                URL = "http://103.56.208.123:7778/reports/rwservlet?hrsikgl+report=D:\\ibrahim_knit\\Reports\\EMP_ATTENDANCE_PERSONAL.rep+EMPID="+emp_id+"+BEGIN_DATE='"+firstDate+"'+END_DATE='"+lastDate+"'+CRITERIA='"+criteria+"'";
            }
            else if (DEFAULT_USERNAME.equals("TTRAMS")) {
                URL = "http://103.56.208.123:7778/reports/rwservlet?hrsikgl+report=D:\\ttit_rams\\Reports\\EMP_ATTENDANCE_PERSONAL.rep+EMPID="+emp_id+"+BEGIN_DATE='"+firstDate+"'+END_DATE='"+lastDate+"'+CRITERIA='"+criteria+"'";
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
}