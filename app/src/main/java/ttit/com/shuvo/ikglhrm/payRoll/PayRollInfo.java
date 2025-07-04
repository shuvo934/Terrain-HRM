package ttit.com.shuvo.ikglhrm.payRoll;

import static ttit.com.shuvo.ikglhrm.Login.userInfoLists;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.api_url_front;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.dewinjm.monthyearpicker.MonthFormat;
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import ttit.com.shuvo.ikglhrm.R;
import ttit.com.shuvo.ikglhrm.WaitProgress;
import ttit.com.shuvo.ikglhrm.payRoll.advance.AdvanceDetails;
import ttit.com.shuvo.ikglhrm.payRoll.paySlip.PaySlip;

public class PayRollInfo extends AppCompatActivity {
    
    MaterialCardView paySlip;
    MaterialCardView advance;

    BarChart chart;
    TextView refresh;

    ArrayList<BarEntry> NoOfEmp;
    ArrayList<String> year;

    ArrayList<SalaryMonthList> months;
    ArrayList<SalaryMonthList> salaryMonthLists;

    ArrayList<String> monthName;
    ArrayList<String> salary;

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;
    
    String emp_id = "";
    String formattedDate = "";

    Logger logger = Logger.getLogger(PayRollInfo.class.getName());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_roll_info);

        emp_id = userInfoLists.get(0).getEmp_id();

        paySlip = findViewById(R.id.pay_slip);
        advance = findViewById(R.id.advance_details);
        refresh = findViewById(R.id.refresh_graph);

        chart = findViewById(R.id.barchart);

        months = new ArrayList<>();
        salaryMonthLists = new ArrayList<>();

        monthName = new ArrayList<>();
        salary = new ArrayList<>();

        chart.getDescription().setEnabled(false);
        chart.setPinchZoom(false);

        chart.setDrawBarShadow(false);
        chart.setDrawGridBackground(false);

        chart.getAxisLeft().setDrawGridLines(true);

        NoOfEmp = new ArrayList<>();

        year = new ArrayList<>();

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        xAxis.setDrawGridLines(false);

        chart.getLegend().setFormToTextSpace(10);
        chart.getLegend().setStackSpace(10);
        chart.getLegend().setYOffset(10);
        chart.setExtraOffsets(0,0,0,20);

        // zoom and touch disabled
        chart.setScaleEnabled(false);
        chart.setTouchEnabled(false);
        chart.setDoubleTapToZoomEnabled(false);

        chart.getAxisRight().setEnabled(false);
        chart.getAxisLeft().setAxisMinimum(0);
        chart.getLegend().setEnabled(false);

        
        
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);
        SimpleDateFormat mom = new SimpleDateFormat("MMMM",Locale.ENGLISH);
        String monnn = mom.format(c).toUpperCase();

        String rt = "Month: "+monnn;
        refresh.setText(rt);

        formattedDate = df.format(c);


        Calendar cal =  Calendar.getInstance();

        cal.add(Calendar.MONTH, -1);
        String previousMonthYear  = new SimpleDateFormat("MMM-yy", Locale.ENGLISH).format(cal.getTime());
        previousMonthYear = previousMonthYear.toUpperCase();
        months.add(new SalaryMonthList(previousMonthYear,"0"));
        System.out.println(previousMonthYear);

        cal.add(Calendar.MONTH, -1);
        previousMonthYear  = new SimpleDateFormat("MMM-yy", Locale.ENGLISH).format(cal.getTime());
        previousMonthYear = previousMonthYear.toUpperCase();
        months.add(new SalaryMonthList(previousMonthYear,"0"));
        System.out.println(previousMonthYear);

        cal.add(Calendar.MONTH, -1);
        previousMonthYear  = new SimpleDateFormat("MMM-yy", Locale.ENGLISH).format(cal.getTime());
        previousMonthYear = previousMonthYear.toUpperCase();
        months.add(new SalaryMonthList(previousMonthYear,"0"));
        System.out.println(previousMonthYear);

        cal.add(Calendar.MONTH, -1);
        previousMonthYear  = new SimpleDateFormat("MMM-yy", Locale.ENGLISH).format(cal.getTime());
        previousMonthYear = previousMonthYear.toUpperCase();
        months.add(new SalaryMonthList(previousMonthYear,"0"));
        System.out.println(previousMonthYear);

        cal.add(Calendar.MONTH, -1);
        previousMonthYear  = new SimpleDateFormat("MMM-yy", Locale.ENGLISH).format(cal.getTime());
        previousMonthYear = previousMonthYear.toUpperCase();
        months.add(new SalaryMonthList(previousMonthYear,"0"));
        System.out.println(previousMonthYear);

        cal.add(Calendar.MONTH, -1);
        previousMonthYear  = new SimpleDateFormat("MMM-yy", Locale.ENGLISH).format(cal.getTime());
        previousMonthYear = previousMonthYear.toUpperCase();
        months.add(new SalaryMonthList(previousMonthYear,"0"));
        System.out.println(previousMonthYear);

//        new Check().execute();
        getSalaryGraph();


        paySlip.setOnClickListener(v -> {
            Intent intent = new Intent(PayRollInfo.this, PaySlip.class);
            startActivity(intent);
        });

        advance.setOnClickListener(v -> {
            Intent intent = new Intent(PayRollInfo.this, AdvanceDetails.class);
            startActivity(intent);
        });

        refresh.setOnClickListener(v -> {

            Date c1 = Calendar.getInstance().getTime();

            String formattedYear;
            String monthValue;
            String lastformattedYear;
            String lastdateView;

            SimpleDateFormat df1 = new SimpleDateFormat("yyyy", Locale.ENGLISH);
            SimpleDateFormat sdf = new SimpleDateFormat("MM",Locale.ENGLISH);

            formattedYear = df1.format(c1);
            monthValue = sdf.format(c1);
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
            if (!formattedDate.isEmpty()) {
                SimpleDateFormat myf = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);
                Date md = null;
                try {
                    md = myf.parse(formattedDate);
                } catch (ParseException e) {
                    logger.log(Level.WARNING, e.getMessage(), e);
                }

                if (md != null) {
                    calendar.setTime(md);
                }
            }
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

            dialogFragment.setOnDateSetListener((year, monthOfYear) -> {
                System.out.println(year);
                System.out.println(monthOfYear);

                int month = monthOfYear + 1;
                String monthName = "";
                String mon = "";
                String yearName;

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


                formattedDate = "15-"+mon+"-"+yearName;
                //selected_date = "01-"+mon+"-"+yearName;
                String rtt = "Month: " +monthName;
                refresh.setText(rtt);
                SimpleDateFormat sss = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);

                Date today1 = null;
                try {
                    today1 = sss.parse(formattedDate);
                } catch (ParseException e) {
                    logger.log(Level.WARNING, e.getMessage(), e);
                }

                Calendar calendar11 = Calendar.getInstance();
                if (today1 != null) {
                    calendar11.setTime(today1);
                    calendar11.add(Calendar.MONTH, 1);
                    calendar11.set(Calendar.DAY_OF_MONTH, 1);
                    calendar11.add(Calendar.DATE, -1);

                    Date lastDayOfMonth1 = calendar11.getTime();

                    SimpleDateFormat sdff1 = new SimpleDateFormat("dd",Locale.ENGLISH);
                    String llll = sdff1.format(lastDayOfMonth1);
                    formattedDate =  llll+ "-" + mon +"-"+ yearName;

                    months = new ArrayList<>();

                    calendar11.add(Calendar.MONTH, -1);
                    String previousMonthYear1 = new SimpleDateFormat("MMM-yy", Locale.ENGLISH).format(calendar11.getTime());
                    previousMonthYear1 = previousMonthYear1.toUpperCase();
                    months.add(new SalaryMonthList(previousMonthYear1,"0"));
                    System.out.println(previousMonthYear1);

                    calendar11.add(Calendar.MONTH, -1);
                    previousMonthYear1 = new SimpleDateFormat("MMM-yy", Locale.ENGLISH).format(calendar11.getTime());
                    previousMonthYear1 = previousMonthYear1.toUpperCase();
                    months.add(new SalaryMonthList(previousMonthYear1,"0"));
                    System.out.println(previousMonthYear1);

                    calendar11.add(Calendar.MONTH, -1);
                    previousMonthYear1 = new SimpleDateFormat("MMM-yy", Locale.ENGLISH).format(calendar11.getTime());
                    previousMonthYear1 = previousMonthYear1.toUpperCase();
                    months.add(new SalaryMonthList(previousMonthYear1,"0"));
                    System.out.println(previousMonthYear1);

                    calendar11.add(Calendar.MONTH, -1);
                    previousMonthYear1 = new SimpleDateFormat("MMM-yy", Locale.ENGLISH).format(calendar11.getTime());
                    previousMonthYear1 = previousMonthYear1.toUpperCase();
                    months.add(new SalaryMonthList(previousMonthYear1,"0"));
                    System.out.println(previousMonthYear1);

                    calendar11.add(Calendar.MONTH, -1);
                    previousMonthYear1 = new SimpleDateFormat("MMM-yy", Locale.ENGLISH).format(calendar11.getTime());
                    previousMonthYear1 = previousMonthYear1.toUpperCase();
                    months.add(new SalaryMonthList(previousMonthYear1,"0"));
                    System.out.println(previousMonthYear1);

                    calendar11.add(Calendar.MONTH, -1);
                    previousMonthYear1 = new SimpleDateFormat("MMM-yy", Locale.ENGLISH).format(calendar11.getTime());
                    previousMonthYear1 = previousMonthYear1.toUpperCase();
                    months.add(new SalaryMonthList(previousMonthYear1,"0"));
                    System.out.println(previousMonthYear1);


                }



//                        new Check().execute();
                getSalaryGraph();
                chart.resetZoom();
                chart.fitScreen();
            });

        });


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
//        catch (IOException | InterruptedException e)          { logger.log(Level.WARNING, e.getMessage(), e); }
//
//        return false;
//    }

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
//                SalaryGraph();
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
//                monthName = new ArrayList<>();
//                salary = new ArrayList<>();
//                NoOfEmp = new ArrayList<>();
//
//                for (int i = 0; i < salaryMonthLists.size(); i++) {
//                    for (int j = 0; j < months.size(); j++) {
//                        String month = months.get(j).getMonth();
//                        month = month.substring(0, month.length() -3);
//                        if (month.equals(salaryMonthLists.get(i).getMonth())) {
//                            months.get(j).setSalary(salaryMonthLists.get(i).getSalary());
//                        }
//                    }
//                }
//
//                for (int i = months.size()-1; i >= 0; i--) {
//
//                    monthName.add(months.get(i).getMonth());
//                    salary.add(months.get(i).getSalary());
//
//                }
//
//                System.out.println(monthName);
//                System.out.println(salary);
//
//                for (int i = 0; i < salary.size(); i++) {
//                    NoOfEmp.add(new BarEntry(i, Float.parseFloat(salary.get(i)),i));
//                }
//
////                NoOfEmp.add(new BarEntry(0,900f, 0));
////                NoOfEmp.add(new BarEntry(1,1000f, 1));
////                NoOfEmp.add(new BarEntry(2,500f, 2));
////                NoOfEmp.add(new BarEntry(3,1300f, 3));
////                NoOfEmp.add(new BarEntry(4,1500f, 4));
////                NoOfEmp.add(new BarEntry(5,2050f, 5));
//
//                BarDataSet bardataset = new BarDataSet(NoOfEmp, "Months");
//                chart.animateY(1000);
//
//                BarData data1 = new BarData(bardataset);
//                bardataset.setColors(ColorTemplate.VORDIPLOM_COLORS);
//
//                bardataset.setBarBorderColor(Color.DKGRAY);
//                bardataset.setValueTextSize(11);
//                chart.setData(data1);
//
//                chart.getXAxis().setValueFormatter(new MyAxisValueFormatter(monthName));
//                chart.getAxisLeft().setValueFormatter(new LargeValueFormatter());
//
//
//
//
//
//            }
//            else {
//                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
//                AlertDialog dialog = new AlertDialog.Builder(PayRollInfo.this)
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
//                        new Check().execute();
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

//    public void SalaryGraph() {
//        try {
//            this.connection = createConnection();
//            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();
//
//
//            salaryMonthLists = new ArrayList<>();
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
//            logger.log(Level.WARNING, e.getMessage(), e);
//        }
//    }

    public void getSalaryGraph() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        connected = false;
        conn = false;

        salaryMonthLists = new ArrayList<>();

        BarData salaryData = new BarData();
        chart.setData(salaryData);
        chart.getData().clearValues();
        chart.notifyDataSetChanged();
        chart.clear();
        chart.invalidate();

        RequestQueue requestQueue = Volley.newRequestQueue(PayRollInfo.this);

        String salaryDataUrl = api_url_front + "dashboard/getSalaryAndMonth/"+emp_id+"/"+formattedDate;

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
                        salaryMonthLists.add(new SalaryMonthList(mon_name,net_salary));
                    }
                }
                connected = true;
                updateSalaryGraph();
            }
            catch (JSONException e) {
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

    private void updateSalaryGraph() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                monthName = new ArrayList<>();
                salary = new ArrayList<>();
                NoOfEmp = new ArrayList<>();

                for (int i = 0; i < salaryMonthLists.size(); i++) {
                    for (int j = 0; j < months.size(); j++) {
                        String month = months.get(j).getMonth();
//                        month = month.substring(0, month.length() -3);
                        if (month.equals(salaryMonthLists.get(i).getMonth())) {
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
                    NoOfEmp.add(new BarEntry(i, Float.parseFloat(salary.get(i)),i));
                }


                BarDataSet bardataset = new BarDataSet(NoOfEmp, "Months");
                chart.animateY(1000);

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
                chart.setData(data1);
                chart.getXAxis().setValueFormatter(new MyAxisValueFormatter(monthName));
                chart.getAxisLeft().setValueFormatter(new LargeValueFormatter());
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(PayRollInfo.this)
                        .setMessage("There is a network issue in the server. Please Try later.")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    getSalaryGraph();
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
            AlertDialog dialog = new AlertDialog.Builder(PayRollInfo.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel",null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                getSalaryGraph();
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