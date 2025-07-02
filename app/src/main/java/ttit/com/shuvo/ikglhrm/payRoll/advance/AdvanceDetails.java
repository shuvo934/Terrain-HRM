package ttit.com.shuvo.ikglhrm.payRoll.advance;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.dewinjm.monthyearpicker.MonthFormat;
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import ttit.com.shuvo.ikglhrm.R;
import ttit.com.shuvo.ikglhrm.WaitProgress;
import static ttit.com.shuvo.ikglhrm.Login.userDesignations;
import static ttit.com.shuvo.ikglhrm.Login.userInfoLists;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.api_url_front;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AdvanceDetails extends AppCompatActivity {

    TextInputEditText selectMonth;
    TextInputLayout selectMonthLay;
    
    CardView reportCard;
    
    TextInputEditText empName;
    TextInputEditText id;
    TextInputEditText band;
    TextInputEditText strDes;
    TextInputEditText jobPosition;

    TextView advTaken;
    TextView advPaid;
    TextView scAdvPaid;
    TextView totalPaid;
    TextView scAdvAllTaken;
    TextView scAdvAllPaid;
    TextView scAdvAllPayable;
    
    String emp_id = "";

    String emp_name = "";
    String user_id = "";
    String ban = "";
    String str_DES = "";
    String job_pos = "";
    
    String selected_date = "";

    WaitProgress waitProgress = new WaitProgress();
//    private String message = null;
    private Boolean conn = false;
    private Boolean connected = false;

//    private Connection connection;

    String adv_taken = "";
    String adv_paid = "";
    String sc_adv_paid = "";
    String total_paid = "";
    String sc_adv_all_taken = "";
    String sc_adv_all_paid = "";
    String sc_adv_all_payable = "";

    Logger logger = Logger.getLogger(AdvanceDetails.class.getName());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advance_details);

        selectMonth = findViewById(R.id.select_month_advance);
        selectMonthLay = findViewById(R.id.select_month_advance_lay);

        reportCard = findViewById(R.id.advance_report_card);
        
        empName = findViewById(R.id.name_advance);
        id = findViewById(R.id.id_advance);
        band = findViewById(R.id.band_advance);
        strDes = findViewById(R.id.str_des_advance);
        jobPosition = findViewById(R.id.job_pos_advance);

        advTaken = findViewById(R.id.advance_taken);
        advPaid = findViewById(R.id.advance_paid);
        scAdvPaid = findViewById(R.id.schedule_advance_paid);
        totalPaid = findViewById(R.id.total_advance_paid);
        scAdvAllTaken = findViewById(R.id.total_schedule_advance_taken);
        scAdvAllPaid = findViewById(R.id.total_scheduling_advance_paid);
        scAdvAllPayable = findViewById(R.id.sc_adv_paya_tot);


        emp_id = userInfoLists.get(0).getEmp_id();

        if (!userInfoLists.isEmpty()) {
            String firstname = userInfoLists.get(0).getUser_fname();
            String lastName = userInfoLists.get(0).getUser_lname();
            if (firstname == null) {
                firstname = "";
            }
            if (lastName == null) {
                lastName = "";
            }
            emp_name = firstname+" "+lastName;
            user_id = userInfoLists.get(0).getUserName();
        }

        if (!userDesignations.isEmpty()) {
            str_DES = userDesignations.get(0).getJsm_name();
            if (str_DES == null) {
                str_DES = "";
            }
            ban = userDesignations.get(0).getDesg_priority();
            if (ban == null) {
                ban = "";
            }
            job_pos = userDesignations.get(0).getDesg_name();
            if (job_pos == null) {
                job_pos = "";
            }
        }
        
        selectMonth.setOnClickListener(v -> {

            Date c = Calendar.getInstance().getTime();

            String formattedYear;
            String monthValue;
            String lastformattedYear;
            String lastdateView;

            SimpleDateFormat df = new SimpleDateFormat("yyyy", Locale.ENGLISH);
            SimpleDateFormat sdf = new SimpleDateFormat("MM",Locale.ENGLISH);

            formattedYear = df.format(c);
            monthValue = sdf.format(c);
            int nowMonNumb = Integer.parseInt(monthValue);
            nowMonNumb = nowMonNumb - 1;
            int lastMonNumb = nowMonNumb - 11;

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

            calendar1.set(Calendar.MONTH, 1);
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
            if (!selected_date.isEmpty()) {
                SimpleDateFormat myf = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);
                Date md = null;
                try {
                    md = myf.parse(selected_date);
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

                selected_date = "01-"+mon+"-"+yearName;
                //selected_date = "01-"+mon+"-"+yearName;
                String tt = monthName + "-" + year;
                selectMonth.setText(tt);
                selectMonthLay.setHint("Month");

                getAdvanceData();
            });

        });

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat sss = new SimpleDateFormat("MMM-yy", Locale.ENGLISH);
        selected_date = sss.format(c);
        selected_date = "01-" + selected_date;
        System.out.println(selected_date);


        SimpleDateFormat month_date = new SimpleDateFormat("MMMM",Locale.ENGLISH);
        String month_name = month_date.format(c);
        month_name = month_name.toUpperCase();
        System.out.println(month_name);

        SimpleDateFormat presentYear = new SimpleDateFormat("yyyy",Locale.ENGLISH);
        String yyyy = presentYear.format(c);

        String smt = month_name+"-"+yyyy;
        selectMonth.setText(smt);
        selectMonthLay.setHint("Month:");
        reportCard.setVisibility(View.GONE);

        getAdvanceData();

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
//                AdvanceData();
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
//                errorMsgMonth.setVisibility(View.GONE);
//                reportCard.setVisibility(View.VISIBLE);
//
//                monthName.setText(selectMonth.getText().toString());
//
//                empName.setText(emp_name);
//                id.setText(user_id);
//                band.setText(ban);
//                strDes.setText(str_DES);
//                jobPosition.setText(job_pos);
//
//                advTaken.setText(adv_taken);
//                advPaid.setText(adv_paid);
//                scAdvPaid.setText(sc_adv_paid);
//                totalPaid.setText(total_paid);
//                scAdvAllTaken.setText(sc_adv_all_taken);
//                scAdvAllPaid.setText(sc_adv_all_paid);
//
//                if (sc_adv_all_taken != null && sc_adv_all_paid != null) {
//                    if (!sc_adv_all_taken.isEmpty() && !sc_adv_all_paid.isEmpty()) {
//                        int taken = Integer.parseInt(sc_adv_all_taken);
//                        int paid = Integer.parseInt(sc_adv_all_paid);
//                        int payable = taken - paid;
//                        scAdvAllPayable.setText(String.valueOf(payable));
//                    } else {
//                        scAdvAllPayable.setText("");
//                    }
//                } else {
//                    scAdvAllPayable.setText("");
//                }
//
//
//
//
//
//            }
//            else {
//                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
//                AlertDialog dialog = new AlertDialog.Builder(AdvanceDetails.this)
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
//    public void AdvanceData() {
//        try {
//            this.connection = createConnection();
//
//            Statement stmt = connection.createStatement();
//
//             adv_taken = "";
//             adv_paid = "";
//             sc_adv_paid = "";
//             total_paid = "";
//             sc_adv_all_taken = "";
//             sc_adv_all_paid = "";
//             sc_adv_all_payable = "";
//
//
//
//            ResultSet resultSet = stmt.executeQuery("SELECT SA.SA_EMP_ID, SUM (SA.SA_AMT) MONTH_ADVANCE\n" +
//                    "    FROM STAFF_ADVANCE SA\n" +
//                    "   WHERE     SA.SA_EMP_ID = "+emp_id+"\n" +
//                    "         AND TO_CHAR (SA.SA_DATE, 'mmrrrr') = TO_CHAR (TO_DATE('"+selected_date+"'), 'mmrrrr')\n" +
//                    "GROUP BY SA.SA_EMP_ID");
//
//            while (resultSet.next()) {
//
//                adv_taken = resultSet.getString(2);
//
//
//            }
//
//            ResultSet resultSet1 = stmt.executeQuery("SELECT SD.SD_EMP_ID, NVL (SD.SD_ADVANCE_DEDUCT, 0) MONTH_ADV_PAID,\n" +
//                    "        NVL (SD.SD_SCH_ADVANCE_DEDUCT, 0)  MONTH_SCH_ADV_PAID,\n" +
//                    "      NVL (SD.SD_ADVANCE_DEDUCT, 0) + NVL (SD.SD_SCH_ADVANCE_DEDUCT, 0) TOTAL_MONTH_PAID\n" +
//                    "  FROM SALARY_MST SM, SALARY_DTL SD\n" +
//                    " WHERE     SM.SM_ID = SD.SD_SM_ID\n" +
//                    "       AND SD.SD_EMP_ID = "+emp_id+"\n" +
//                    "       AND TO_CHAR (SM.SM_PMS_MONTH, 'mmrrrr') = TO_CHAR (TO_DATE('"+selected_date+"'), 'mmrrrr')");
//
//            while (resultSet1.next()) {
//
//                adv_paid = resultSet1.getString(2);
//                sc_adv_paid = resultSet1.getString(3);
//                total_paid = resultSet1.getString(4);
//            }
//
//            ResultSet resultSet2 = stmt.executeQuery("SELECT SAM.SSAM_EMP_ID,\n" +
//                    "       SUM(SAM.SSAM_AMT)        SCH_ADV_AMT\n"+
//                    " --SAD.SSAD_DIDUCT_AMT MONTH_SCH_ADV\n"+
//                    "  FROM STAFF_SCHEDULE_ADV_MST SAM, STAFF_SCHEDULE_ADV_DTL SAD\n" +
//                    " WHERE     SAM.SSAM_ID = SAD.SSAD_SSAM_ID\n" +
//                    "       AND TO_CHAR (SAD.SSAD_MONTH, 'mmrrrr') = TO_CHAR (TO_DATE('"+selected_date+"'), 'mmrrrr')\n" +
//                    "       AND SAM.SSAM_EMP_ID = "+emp_id+"" +
//                    "group by SAM.SSAM_EMP_ID");
//
//            while (resultSet2.next()) {
//                sc_adv_all_taken = resultSet2.getString(2);
//            }
//
//            ResultSet resultSet3 = stmt.executeQuery("SELECT NVL (SUM (SSAD_DIDUCT_AMT), 0) TOTAL_SCH_PAID\n" +
//                    "  FROM STAFF_SCHEDULE_ADV_MST, STAFF_SCHEDULE_ADV_DTL\n" +
//                    " WHERE     STAFF_SCHEDULE_ADV_MST.SSAM_ID =\n" +
//                    "              STAFF_SCHEDULE_ADV_DTL.SSAD_SSAM_ID\n" +
//                    "       AND TRUNC (STAFF_SCHEDULE_ADV_DTL.SSAD_MONTH) <=\n" +
//                    "              TRUNC (ADD_MONTHS ( (LAST_DAY ('"+selected_date+"') + 1), -1))\n" +
//                    "       AND ssam_id IN\n" +
//                    "              (SELECT SAD.SSAD_SSAM_ID\n" +
//                    "                 FROM STAFF_SCHEDULE_ADV_DTL SAD\n" +
//                    "                WHERE TO_CHAR (SAD.SSAD_MONTH, 'mmrrrr') =\n" +
//                    "                         TO_CHAR (TO_DATE('"+selected_date+"'), 'mmrrrr'))\n" +
//                    "       AND ssad_paid_flag = 1\n" +
//                    "       AND STAFF_SCHEDULE_ADV_MST.SSAM_EMP_ID = "+emp_id+"");
//
//            while (resultSet3.next()) {
//                sc_adv_all_paid = resultSet3.getString(1);
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

    public void getAdvanceData() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;

        adv_taken = "0";
        adv_paid = "0";
        sc_adv_paid = "0";
        total_paid = "0";
        sc_adv_all_taken = "0";
        sc_adv_all_paid = "0";
        sc_adv_all_payable = "0";

        String advtakenUrl = api_url_front + "advanceData/getAdvTaken/"+emp_id+"/"+selected_date;
        String advPaidUrl = api_url_front + "advanceData/getAdvancePaid/"+emp_id+"/"+selected_date;
        String schAdvTakenUrl = api_url_front + "advanceData/getSchAdvTaken/"+emp_id+"/"+selected_date;
        String schAdvPaidUrl = api_url_front + "advanceData/getSchAdvPaid/"+emp_id+"/"+selected_date;

        RequestQueue requestQueue = Volley.newRequestQueue(AdvanceDetails.this);

        StringRequest schAdvPaidReq = new StringRequest(Request.Method.GET, schAdvPaidUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject advInfo = array.getJSONObject(i);

                        sc_adv_all_paid = advInfo.getString("total_sch_paid")
                                .equals("null") ? "0" : advInfo.getString("total_sch_paid");
                    }
                }
                connected = true;
                updateLayout();
            }
            catch (JSONException e) {
                logger.log(Level.WARNING, e.getMessage(), e);
                connected = false;
                updateLayout();
            }
        }, error -> {
            logger.log(Level.WARNING, error.getMessage(), error);
            conn = false;
            connected = false;
            updateLayout();
        });

        StringRequest schAdvTakReq = new StringRequest(Request.Method.GET, schAdvTakenUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject advInfo = array.getJSONObject(i);

                        sc_adv_all_taken = advInfo.getString("sch_adv_amt")
                                .equals("null") ? "0" : advInfo.getString("sch_adv_amt");
                    }
                }
                requestQueue.add(schAdvPaidReq);
            }
            catch (JSONException e) {
                logger.log(Level.WARNING, e.getMessage(), e);
                connected = false;
                updateLayout();
            }
        }, error -> {
            logger.log(Level.WARNING, error.getMessage(), error);
            conn = false;
            connected = false;
            updateLayout();
        });

        StringRequest advPaidReq = new StringRequest(Request.Method.GET, advPaidUrl, response -> {
           conn = true;
           try {
               JSONObject jsonObject = new JSONObject(response);
               String items = jsonObject.getString("items");
               String count = jsonObject.getString("count");
               if (!count.equals("0")) {
                   JSONArray array = new JSONArray(items);
                   for (int i = 0; i < array.length(); i++) {
                       JSONObject advInfo = array.getJSONObject(i);

                       adv_paid = advInfo.getString("month_adv_paid")
                               .equals("null") ? "0" : advInfo.getString("month_adv_paid");
                       sc_adv_paid = advInfo.getString("month_sch_adv_paid")
                               .equals("null") ? "0" : advInfo.getString("month_sch_adv_paid");
                       total_paid = advInfo.getString("total_month_paid")
                               .equals("null") ? "0" : advInfo.getString("total_month_paid");

                   }
               }
               requestQueue.add(schAdvTakReq);
           }
           catch (JSONException e) {
               logger.log(Level.WARNING, e.getMessage(), e);
               connected = false;
               updateLayout();
           }
        }, error -> {
            logger.log(Level.WARNING, error.getMessage(), error);
            conn = false;
            connected = false;
            updateLayout();
        });

        StringRequest advtakReq = new StringRequest(Request.Method.GET, advtakenUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject advInfo = array.getJSONObject(i);

                        adv_taken = advInfo.getString("month_advance")
                                .equals("null") ? "0" : advInfo.getString("month_advance");
                    }
                }

                requestQueue.add(advPaidReq);
            }
            catch (JSONException e) {
                logger.log(Level.WARNING, e.getMessage(), e);
                connected = false;
                updateLayout();
            }
        }, error -> {
            logger.log(Level.WARNING, error.getMessage(), error);
            conn = false;
            connected = false;
            updateLayout();
        });

        requestQueue.add(advtakReq);
    }

    private void updateLayout() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                reportCard.setVisibility(View.VISIBLE);
                empName.setText(emp_name);
                id.setText(user_id);
                band.setText(ban);
                strDes.setText(str_DES);
                jobPosition.setText(job_pos);

                advTaken.setText(adv_taken);
                advPaid.setText(adv_paid);
                scAdvPaid.setText(sc_adv_paid);
                totalPaid.setText(total_paid);
                scAdvAllTaken.setText(sc_adv_all_taken);
                scAdvAllPaid.setText(sc_adv_all_paid);

                if (sc_adv_all_taken != null && sc_adv_all_paid != null) {
                    if (!sc_adv_all_taken.isEmpty() && !sc_adv_all_paid.isEmpty()) {
                        int taken = Integer.parseInt(sc_adv_all_taken);
                        int paid = Integer.parseInt(sc_adv_all_paid);
                        int payable = taken - paid;
                        scAdvAllPayable.setText(String.valueOf(payable));
                    } else {
                        scAdvAllPayable.setText("0");
                    }
                } else {
                    scAdvAllPayable.setText("0");
                }
                conn = false;
                connected = false;
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(AdvanceDetails.this)
                        .setMessage("There is a network issue in the server. Please Try later.")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    getAdvanceData();
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
            AlertDialog dialog = new AlertDialog.Builder(AdvanceDetails.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel",null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                getAdvanceData();
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