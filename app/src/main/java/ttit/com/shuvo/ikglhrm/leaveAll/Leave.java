package ttit.com.shuvo.ikglhrm.leaveAll;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.dewinjm.monthyearpicker.MonthFormat;
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialog;
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ttit.com.shuvo.ikglhrm.R;
import ttit.com.shuvo.ikglhrm.WaitProgress;
import ttit.com.shuvo.ikglhrm.leaveAll.leaveApplication.AllLeaveApplication;
import ttit.com.shuvo.ikglhrm.leaveAll.leaveBalance.LeaveBalance;
import ttit.com.shuvo.ikglhrm.leaveAll.leaveStatus.LeaveStatus;

import static ttit.com.shuvo.ikglhrm.Login.CompanyName;
import static ttit.com.shuvo.ikglhrm.Login.SoftwareName;
import static ttit.com.shuvo.ikglhrm.Login.userInfoLists;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Leave extends AppCompatActivity {

    TextView softName;
    TextView compName;

    CardView leaveBalance;
    CardView leaveApplication;
    CardView leaveStatus;

    Button back;

//    PieChart pieChart;
    TextView refresh;

    WaitProgress waitProgress = new WaitProgress();
//    private String message = null;
    private Boolean conn = false;
    private Boolean connected = false;

//    private Connection connection;

    String emp_id = "";
    String leaveDate = "";

//    ArrayList<PieEntry> NoOfEmp;
//    ArrayList<SalaryMonthList> dataValue;

    ArrayList<BarEntry> balance;
    ArrayList<BarEntry> earn;
    ArrayList<String> shortCode;

    BarChart chart;

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

        setContentView(R.layout.activity_leave);

        softName = findViewById(R.id.name_of_soft_leave);
        compName = findViewById(R.id.name_of_company_leave);

        leaveBalance = findViewById(R.id.leave_balance_sheet);
        leaveApplication = findViewById(R.id.leave_application);
        leaveStatus = findViewById(R.id.leave_status);

        back = findViewById(R.id.leave_back);

//        pieChart = findViewById(R.id.piechart);
        refresh = findViewById(R.id.refresh_graph_leave);

        softName.setText(CompanyName);
        compName.setText(SoftwareName);

//        dataValue = new ArrayList<>();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        leaveBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Leave.this, LeaveBalance.class);
                startActivity(intent);
            }
        });

        leaveApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Leave.this, AllLeaveApplication.class);
                startActivity(intent);
            }
        });

        leaveStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Leave.this, LeaveStatus.class);
                startActivity(intent);
            }
        });

        emp_id = userInfoLists.get(0).getEmp_id();

        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);
        SimpleDateFormat mmm = new SimpleDateFormat("MMMM", Locale.ENGLISH);

        String monnnn = mmm.format(c);

        refresh.setText("Month: "+ monnnn);

        leaveDate = df.format(c);

        chart = findViewById(R.id.multi_bar_chart);

        balance = new ArrayList<>();
        earn = new ArrayList<>();
        shortCode = new ArrayList<>();

        chart.getDescription().setEnabled(false);
        chart.setPinchZoom(false);
        chart.getAxisLeft().setDrawGridLines(true);
        chart.getAxisLeft().setAxisMinimum(0);

//        chart.getLegend().setFormToTextSpace(10);
        chart.getLegend().setStackSpace(20);
        chart.getLegend().setYOffset(10);
        chart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        chart.setExtraOffsets(0,0,0,20);

        // zoom and touch disabled
        chart.setScaleEnabled(false);
        chart.setTouchEnabled(false);
        chart.setDoubleTapToZoomEnabled(false);

        chart.getAxisRight().setEnabled(false);

//
//        NoOfEmp = new ArrayList<>();
//
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





//        pieChart.setCenterText("Leave Balance");
//        pieChart.setDrawEntryLabels(true);
//        pieChart.setCenterTextSize(14);
//        pieChart.setHoleRadius(40);
//        pieChart.setTransparentCircleRadius(40);

        // eta bad
//        pieChart.setTransparentCircleColor(Color.RED);
//        pieChart.setTransparentCircleAlpha(50);
//        pieChart.setCenterTextRadiusPercent(30);


//        pieChart.setEntryLabelTextSize(11);
//        pieChart.setEntryLabelColor(Color.DKGRAY);
//        pieChart.getDescription().setEnabled(false);


//        Legend l = pieChart.getLegend();
//        l.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
//        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
//        l.setOrientation(Legend.LegendOrientation.VERTICAL);
//        l.setXOffset(20);
//        l.setTextSize(12);

        // eta bad
        //l.setYOffset(50);

//        l.setWordWrapEnabled(false);
//        l.setDrawInside(false);
//        l.setYOffset(5f);
//        final int[] piecolors = new int[]{
//                Color.rgb(129, 236, 236),
//                Color.rgb(85, 239, 196),
//                Color.rgb(116, 185, 255),
//                Color.rgb(162, 155, 254),
//                Color.rgb(223, 230, 233),
//                Color.rgb(255, 234, 167),
//                Color.rgb(250, 177, 160),
//                Color.rgb(255, 118, 117),
//                Color.rgb(253, 121, 168),
//                Color.rgb(96, 163, 188)};
//        pieChart.animateXY(1500, 1500);

//        PieData data = new PieData(dataSet);
//
//        dataSet.setHighlightEnabled(true);
//        dataSet.setValueTextSize(12);
//        dataSet.setValueTextColor(Color.BLACK);
//        dataSet.setColors(ColorTemplate.createColors(piecolors));

//        pieChart.setUsePercentValues(true);


//        pieChart.setData(data);
//        pieChart.invalidate();

//        new Check().execute();
        getLeaveGraph();


        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Date c = Calendar.getInstance().getTime();

                String formattedYear = "";
                String monthValue = "";
                String lastformattedYear = "";
                String lastdateView = "";

                SimpleDateFormat df = new SimpleDateFormat("yyyy", Locale.ENGLISH);
                SimpleDateFormat sdf = new SimpleDateFormat("MM",Locale.ENGLISH);

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

                SimpleDateFormat sdff = new SimpleDateFormat("dd",Locale.ENGLISH);
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



                        leaveDate = "01-"+mon+"-"+yearName;
                        //selected_date = "01-"+mon+"-"+yearName;
                        refresh.setText("Month: "+monthName);

                        SimpleDateFormat sss = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);

                        Date today = null;
                        try {
                            today = sss.parse(leaveDate);
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

                            SimpleDateFormat sdff = new SimpleDateFormat("dd",Locale.ENGLISH);
                            String llll = sdff.format(lastDayOfMonth);
                            leaveDate =  llll+ "-" + mon +"-"+ yearName;
                        }
                        System.out.println(leaveDate);



//                        new Check().execute();
                        getLeaveGraph();
                    }
                });

            }
        });
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
//                LeaveBalanceGraph();
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
////                final int[] piecolors = new int[]{
////
////                        Color.rgb(85, 239, 196),
////                        Color.rgb(116, 185, 255),
////                        Color.rgb(162, 155, 254),
////                        Color.rgb(223, 230, 233),
////                        Color.rgb(255, 234, 167),
////                        Color.rgb(129, 236, 236),
////                        Color.rgb(250, 177, 160),
////                        Color.rgb(255, 118, 117),
////                        Color.rgb(253, 121, 168),
////                        Color.rgb(96, 163, 188)};
////                int j = 0;
////
////
////                for (int i = 0; i < dataValue.size(); i++) {
////                    String datt = dataValue.get(i).getMonth();
////                    if (!datt.equals("0")) {
////                        System.out.println(datt);
////
////                        NoOfEmp.add(new PieEntry(Float.parseFloat(datt),dataValue.get(i).getSalary(),j));
////                        j++;
////                    }
////                }
////                PieDataSet dataSet = new PieDataSet(NoOfEmp, "");
////                pieChart.animateXY(1500, 1500);
////                //pieChart.setExtraRightOffset(50);
////
////
////
////                PieData data = new PieData(dataSet);
//////                dataSet.setValueFormatter(new PercentFormatter(pieChart));
////                dataSet.setValueFormatter(new ValueFormatter() {
////                    @Override
////                    public String getFormattedValue(float value) {
////                        return String.valueOf((int) Math.floor(value));
////                    }
////                });
////                dataSet.setHighlightEnabled(true);
////                dataSet.setValueTextSize(12);
////                dataSet.setValueTextColor(Color.BLACK);
////                dataSet.setColors(ColorTemplate.createColors(piecolors));
////
////
//////                pieChart.setUsePercentValues(true);
////
////                pieChart.setData(data);
////                pieChart.invalidate();
//
//                if (balance.size() == 0 || earn.size() == 0 || shortCode.size() == 0) {
//                    // do nothing
//                } else {
//                    XAxis xAxis = chart.getXAxis();
//                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//                    xAxis.setDrawGridLines(false);
//                    xAxis.setCenterAxisLabels(true);
//                    xAxis.setAxisMinimum(0);
//                    xAxis.setAxisMaximum(balance.size());
//                    xAxis.setGranularity(1);
//
//
//                    BarDataSet set1 = new BarDataSet(earn, "Earned Leave");
//                    set1.setColor(ColorTemplate.VORDIPLOM_COLORS[3]);
//                    set1.setValueFormatter(new ValueFormatter() {
//                        @Override
//                        public String getFormattedValue(float value) {
//                            return String.valueOf((int) Math.floor(value));
//                        }
//                    });
//                    BarDataSet set2 = new BarDataSet(balance, "Leave Balance");
//                    set2.setValueFormatter(new ValueFormatter() {
//                        @Override
//                        public String getFormattedValue(float value) {
//                            return String.valueOf((int) Math.floor(value));
//                        }
//                    });
//                    set2.setColor(ColorTemplate.VORDIPLOM_COLORS[2]);
//
//                    float groupSpace = 0.04f;
//                    float barSpace = 0.02f; // x2 dataset
//                    float barWidth = 0.46f;
//
//                    BarData data = new BarData(set1, set2);
//                    data.setValueTextSize(12);
//                    data.setBarWidth(barWidth); // set the width of each bar
//                    chart.animateY(1000);
//                    chart.setData(data);
//                    chart.groupBars(0, groupSpace, barSpace); // perform the "explicit" grouping
//                    chart.invalidate();
//
//                    xAxis.setValueFormatter(new ValueFormatter() {
//                        @Override
//                        public String getAxisLabel(float value, AxisBase axis) {
//                            if (value < 0 || value >= shortCode.size()) {
//                                return null;
//                            } else {
////                                System.out.println(value);
////                                System.out.println(axis);
////                                System.out.println(shortCode.get((int)value));
//                                return (shortCode.get((int) value));
//                            }
//
//                        }
//                    });
//
//                }
//
//
//
//            }
//            else {
//                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
//                AlertDialog dialog = new AlertDialog.Builder(Leave.this)
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
//
//                        dialog.dismiss();
//                        finish();
//
//                    }
//                });
//            }
//        }
//    }

//    public void LeaveBalanceGraph() {
//        try {
//            this.connection = createConnection();
//            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();
//
//
////            NoOfEmp = new ArrayList<>();
////            dataValue = new ArrayList<>();
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
//                if (data != null) {
//                    if (!data.equals("0")) {
//                        balance.add(new BarEntry(i, Float.parseFloat(rs.getString(3)),i));
//                        earn.add(new BarEntry(i, Float.parseFloat(rs.getString(4)),i));
//                        shortCode.add(rs.getString(2));
//                        i++;
//                    }
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
//            e.printStackTrace();
//        }
//    }

    public void getLeaveGraph() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        connected = false;
        conn = false;

        balance = new ArrayList<>();
        earn = new ArrayList<>();
        shortCode = new ArrayList<>();

        String leaveDataUrl = "http://103.56.208.123:8001/apex/ttrams/dashboard/getLeaveData/"+emp_id+"/"+leaveDate+"";

        RequestQueue requestQueue = Volley.newRequestQueue(Leave.this);

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
                                balance.add(new BarEntry(i, Float.parseFloat(balance_all),i));
                                earn.add(new BarEntry(i, Float.parseFloat(quantity),i));
                                shortCode.add(lc_short_code);
                            }
                        }
                    }
                }
                connected = true;
                updateLeaveGraph();
            }
            catch (JSONException e) {
                connected = false;
                e.printStackTrace();
                updateLeaveGraph();
            }
        }, error -> {
            conn = false;
            connected = false;
            error.printStackTrace();
            updateLeaveGraph();
        });

        requestQueue.add(leaveDataReq);
    }

    private void updateLeaveGraph() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                if (balance.size() == 0 || earn.size() == 0 || shortCode.size() == 0) {
                    // do nothing
                } else {
                    XAxis xAxis = chart.getXAxis();
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

                    BarData data = new BarData(set1, set2);
                    data.setValueTextSize(12);
                    data.setBarWidth(barWidth); // set the width of each bar
                    chart.animateY(1000);
                    chart.setData(data);
                    chart.groupBars(0, groupSpace, barSpace); // perform the "explicit" grouping
                    chart.invalidate();

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
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(Leave.this)
                        .setMessage("There is a network issue in the server. Please Try later.")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel", null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {
                    getLeaveGraph();
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
            AlertDialog dialog = new AlertDialog.Builder(Leave.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel", null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {
                getLeaveGraph();
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