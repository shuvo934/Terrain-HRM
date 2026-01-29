package ttit.com.shuvo.ikglhrm.attendance.status;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import static ttit.com.shuvo.ikglhrm.user_login.Login.userInfoLists;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.api_url_front;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.jakewharton.processphoenix.ProcessPhoenix;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AttendanceStatus extends AppCompatActivity {


    TextView statusNot;
    RecyclerView statusView;
    StatusAdapter statusAdapter;
    RecyclerView.LayoutManager layoutManager;

    ArrayList<StatusList> statusLists;
    ArrayList<StatusList> filterLists;

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;

    String emp_id = "";

    TextView allStatusCount;
    String all_status_count = "0";

    TextView pendingStatusCount;
    String pending_status_count = "0";

    TextView approveStatusCount;
    String approved_status_count = "0";

    TextView rejectedStatusCount;
    String rej_status_count = "0";

    MaterialCardView allAppCard;
    RelativeLayout allAppBack;
    MaterialCardView pendingAppCard;
    RelativeLayout pendingAppBack;
    MaterialCardView approvedAppCard;
    RelativeLayout approvedAppBack;
    MaterialCardView rejectedAppCard;
    RelativeLayout rejectedAppBack;

    LinearLayout yearSelection;
    TextView yearText;
    String selected_year = "";
    AlertDialog yearDialogAppoint;

    Logger logger = Logger.getLogger(AttendanceStatus.class.getName());
    String parsing_message = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_status);

        if (userInfoLists == null) {
            restart("Could Not Get Employee Data. Please Restart the App.");
        }
        else {
            if (userInfoLists.isEmpty()) {
                restart("Could Not Get Employee Data. Please Restart the App.");
            }
            else {
                emp_id = userInfoLists.get(0).getEmp_id();
            }
        }

        statusView = findViewById(R.id.status_list_view);
        statusNot = findViewById(R.id.status_not_found_msg);
        allStatusCount = findViewById(R.id.all_application_request_as);
        pendingStatusCount = findViewById(R.id.pending_request_as);
        approveStatusCount = findViewById(R.id.approved_request_as);
        rejectedStatusCount = findViewById(R.id.rejected_request_as);

        allAppCard = findViewById(R.id.total_applications_card);
        allAppBack = findViewById(R.id.all_application_card_background);
        pendingAppCard = findViewById(R.id.pending_applications_card);
        pendingAppBack = findViewById(R.id.pending_application_card_background);
        approvedAppCard = findViewById(R.id.approved_applications_card);
        approvedAppBack = findViewById(R.id.approved_application_card_background);
        rejectedAppCard = findViewById(R.id.rejected_applications_card);
        rejectedAppBack = findViewById(R.id.reject_application_card_background);

        yearSelection = findViewById(R.id.year_selected_layout_for_att_status);
        yearText = findViewById(R.id.year_text_for_attendance_status);

        statusLists = new ArrayList<>();
        filterLists = new ArrayList<>();

        Date nowDate = Calendar.getInstance().getTime();
        SimpleDateFormat ydf = new SimpleDateFormat("yyyy", Locale.ENGLISH);
        selected_year = ydf.format(nowDate);

        String yt = "Application Year : " + selected_year;
        yearText.setText(yt);

        statusView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        statusView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(statusView.getContext(),DividerItemDecoration.VERTICAL);
        statusView.addItemDecoration(dividerItemDecoration);

        allAppCard.setOnClickListener(v -> {
            allAppCard.setCardElevation(2);
            allAppBack.setBackgroundColor(getColor(R.color.black_alpha));
            pendingAppCard.setCardElevation(10);
            pendingAppBack.setBackgroundColor(getColor(R.color.white));
            approvedAppCard.setCardElevation(10);
            approvedAppBack.setBackgroundColor(getColor(R.color.white));
            rejectedAppCard.setCardElevation(10);
            rejectedAppBack.setBackgroundColor(getColor(R.color.white));

            statusAdapter = new StatusAdapter(statusLists, AttendanceStatus.this);
            statusView.setAdapter(statusAdapter);

            if (statusLists.isEmpty()) {
                statusView.setVisibility(View.GONE);
                statusNot.setVisibility(View.VISIBLE);
            } else {
                statusView.setVisibility(View.VISIBLE);
                statusNot.setVisibility(View.GONE);
            }
        });

        pendingAppCard.setOnClickListener(v -> {
            allAppCard.setCardElevation(10);
            allAppBack.setBackgroundColor(getColor(R.color.white));
            pendingAppCard.setCardElevation(2);
            pendingAppBack.setBackgroundColor(getColor(R.color.black_alpha));
            approvedAppCard.setCardElevation(10);
            approvedAppBack.setBackgroundColor(getColor(R.color.white));
            rejectedAppCard.setCardElevation(10);
            rejectedAppBack.setBackgroundColor(getColor(R.color.white));
            getFilteredList("0");
        });

        approvedAppCard.setOnClickListener(v -> {
            allAppCard.setCardElevation(10);
            allAppBack.setBackgroundColor(getColor(R.color.white));
            pendingAppCard.setCardElevation(10);
            pendingAppBack.setBackgroundColor(getColor(R.color.white));
            approvedAppCard.setCardElevation(2);
            approvedAppBack.setBackgroundColor(getColor(R.color.black_alpha));
            rejectedAppCard.setCardElevation(10);
            rejectedAppBack.setBackgroundColor(getColor(R.color.white));
            getFilteredList("1");
        });

        rejectedAppCard.setOnClickListener(v -> {
            allAppCard.setCardElevation(10);
            allAppBack.setBackgroundColor(getColor(R.color.white));
            pendingAppCard.setCardElevation(10);
            pendingAppBack.setBackgroundColor(getColor(R.color.white));
            approvedAppCard.setCardElevation(10);
            approvedAppBack.setBackgroundColor(getColor(R.color.white));
            rejectedAppCard.setCardElevation(2);
            rejectedAppBack.setBackgroundColor(getColor(R.color.black_alpha));
            getFilteredList("2");
        });

        yearSelection.setOnClickListener(v -> {
            Calendar today = Calendar.getInstance();
            Date c = Calendar.getInstance().getTime();
            Date d = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("yyyy", Locale.ENGLISH);

            if (!selected_year.isEmpty()) {
                Date date = null;
                try {
                    date = df.parse(selected_year);
                }
                catch (ParseException e) {
                    logger.log(Level.WARNING,e.getMessage(),e);
                }
                if (date != null) {
                    d = date;
                }
            }

            MonthPickerDialog.Builder appBuilder = new MonthPickerDialog.Builder(AttendanceStatus.this, (selectedMonth, selectedYear) -> {
                String ms = "Application Year : " + selectedYear;
                yearText.setText(ms);
                selected_year = String.valueOf(selectedYear);
                getAttendStatus();

            },today.get(Calendar.YEAR),today.get(Calendar.MONTH));

            appBuilder.setActivatedYear(Integer.parseInt(df.format(d)))
                    .setMinYear(Integer.parseInt(df.format(c))-1)
                    .setMaxYear(Integer.parseInt(df.format(c)))
                    .showYearOnly()
                    .setTitle("Selected Year")
                    .setOnYearChangedListener(year1 -> {
                    });

            yearDialogAppoint = appBuilder.build();
            yearDialogAppoint.show();
        });

        getAttendStatus();

    }

    private void getFilteredList(String daRmApproved) {
        filterLists = new ArrayList<>();
        for (int i = 0; i < statusLists.size(); i++) {
            if (statusLists.get(i).getApproved().equals(daRmApproved)) {
                String darm_app_code = statusLists.get(i).getApp_code();
                String darm_approved = statusLists.get(i).getApproved();
                String darm_date = statusLists.get(i).getReq_date();
                String darm_req_type = statusLists.get(i).getReq_type();
                String darm_update_date = statusLists.get(i).getUp_date();
                String arrival_time = statusLists.get(i).getArr_time();
                String departure_time = statusLists.get(i).getDep_time();
                String emp_name = statusLists.get(i).getApprover();
                filterLists.add(new StatusList(darm_app_code,darm_approved,darm_date,
                        darm_req_type,darm_update_date,arrival_time,
                        departure_time,emp_name,null));
            }
        }

        statusAdapter = new StatusAdapter(filterLists, AttendanceStatus.this);
        statusView.setAdapter(statusAdapter);

        if (filterLists.isEmpty()) {
            statusView.setVisibility(View.GONE);
            statusNot.setVisibility(View.VISIBLE);
        } else {
            statusView.setVisibility(View.VISIBLE);
            statusNot.setVisibility(View.GONE);
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
//                GpiDetails();
//                if (connected) {
//                    conn = true;
//                    message= "Internet Connected";
//                }
//
//            } else {
//                conn = false;
//                connected = false;
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
//                statusAdapter = new StatusAdapter(statusLists, AttendanceStatus.this);
//
//                statusView.setAdapter(statusAdapter);
//
//                if (statusLists.size() == 0) {
//                    statusView.setVisibility(View.GONE);
//                    statusNot.setVisibility(View.VISIBLE);
//                } else {
//                    statusView.setVisibility(View.VISIBLE);
//                    statusNot.setVisibility(View.GONE);
//                }
//
//                conn = false;
//                connected = false;
//
//
//
//            }
//            else {
//                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
//                AlertDialog dialog = new AlertDialog.Builder(AttendanceStatus.this)
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

//    public void GpiDetails() {
//        try {
//            this.connection = createConnection();
//            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();
//
//
//            statusLists = new ArrayList<>();
//            Statement stmt = connection.createStatement();
//
//
//
//
//            ResultSet rs=stmt.executeQuery("SELECT daily_atten_req_mst.darm_id,\n" +
//                    "                     DAILY_ATTEN_REQ_MST.DARM_APP_CODE,\n" +
//                    "                     daily_atten_req_mst.darm_approved,\n" +
//                    "                     TO_CHAR(DAILY_ATTEN_REQ_MST.DARM_DATE,'DD-MON-YYYY') DARM_DATE,DAILY_ATTEN_REQ_MST.DARM_REQ_TYPE,\n" +
//                    "                    TO_CHAR(DAILY_ATTEN_REQ_MST.DARM_UPDATE_DATE,'DD-MON-YYYY') DARM_UPDATE_DATE,\n" +
//                    "                        TO_CHAR(DAILY_ATTEN_REQ_MST.DARM_REQ_ARRIVAL_TIME,'HH:MI AM') ARRIVAL_TIME,\n" +
//                    "                        TO_CHAR(DAILY_ATTEN_REQ_MST.DARM_REQ_DEPART_TIME,'HH:MI AM') DEPARTURE_TIME,\n" +
//                    "                        emp_name\n" +
//                    "                        FROM DAILY_ATTEN_REQ_MST, EMP_MST, DIVISION_MST\n" +
//                    "                    WHERE \n" +
//                    "                      (EMP_MST.EMP_ID(+) = daily_atten_req_mst.darm_app_reject_emp_id)\n" +
//                    "                     AND (DIVISION_MST.DIVM_ID = DAILY_ATTEN_REQ_MST.DARM_DIVM_ID)\n" +
//                    "                     AND (DAILY_ATTEN_REQ_MST.DARM_EMP_ID = "+emp_id+")\n" +
//                    "                     Order by 1 desc");
//
//
//
//            while(rs.next()) {
//
//               statusLists.add(new StatusList(rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9),null));
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

    public void getAttendStatus() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;

        statusLists = new ArrayList<>();
        filterLists = new ArrayList<>();

        pending_status_count = "0";
        approved_status_count = "0";
        rej_status_count = "0";
        all_status_count = "0";

        String url = api_url_front + "attendanceStatus/attStatusNew?p_emp_id="+emp_id+"&p_year="+selected_year;
        String stat_url = api_url_front + "attendanceStatus/getAttStatusCountNew?p_emp_id="+emp_id+"&p_year="+selected_year;

        RequestQueue requestQueue = Volley.newRequestQueue(AttendanceStatus.this);

        StringRequest asCountReq = new StringRequest(Request.Method.GET, stat_url, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject todayAttDataInfo = array.getJSONObject(i);
                        pending_status_count = todayAttDataInfo.getString("pending")
                                .equals("null") ? "0" : todayAttDataInfo.getString("pending");
                        approved_status_count = todayAttDataInfo.getString("approved")
                                .equals("null") ? "0" : todayAttDataInfo.getString("approved");
                        rej_status_count = todayAttDataInfo.getString("rejected")
                                .equals("null") ? "0" : todayAttDataInfo.getString("rejected");
                    }
                }
                connected = true;
                updateLayout();
            } catch (JSONException e) {
                logger.log(Level.WARNING, e.getMessage(), e);
                parsing_message = e.getLocalizedMessage();
                connected = false;
                updateLayout();
            }
        }, error -> {
            logger.log(Level.WARNING, error.getMessage(), error);
            parsing_message = error.getLocalizedMessage();
            conn = false;
            connected = false;
            updateLayout();
        });

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject statusInfo = array.getJSONObject(i);

                        String darm_app_code = statusInfo.getString("darm_app_code")
                                .equals("null") ? "" : statusInfo.getString("darm_app_code");
                        String darm_approved = statusInfo.getString("darm_approved")
                                .equals("null") ? "" : statusInfo.getString("darm_approved");
                        String darm_date = statusInfo.getString("darm_date")
                                .equals("null") ? "" : statusInfo.getString("darm_date");
                        String darm_req_type = statusInfo.getString("darm_req_type")
                                .equals("null") ? "" : statusInfo.getString("darm_req_type");
                        String darm_update_date = statusInfo.getString("darm_update_date")
                                .equals("null") ? "" : statusInfo.getString("darm_update_date");
                        String arrival_time = statusInfo.getString("arrival_time")
                                .equals("null") ? "" : statusInfo.getString("arrival_time");
                        String departure_time = statusInfo.getString("departure_time")
                                .equals("null") ? "" : statusInfo.getString("departure_time");
                        String emp_name = statusInfo.getString("emp_name")
                                .equals("null") ? "" : statusInfo.getString("emp_name");


                        statusLists.add(new StatusList(darm_app_code,darm_approved,darm_date,
                                darm_req_type,darm_update_date,arrival_time,
                                departure_time,emp_name,null));
                    }
                }
                requestQueue.add(asCountReq);
            }
            catch (JSONException e) {
                logger.log(Level.WARNING, e.getMessage(), e);
                parsing_message = e.getLocalizedMessage();
                connected = false;
                updateLayout();
            }
        }, error -> {
            logger.log(Level.WARNING, error.getMessage(), error);
            parsing_message = error.getLocalizedMessage();
            conn = false;
            connected = false;
            updateLayout();
        });

        requestQueue.add(stringRequest);
    }

    private void updateLayout() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                statusAdapter = new StatusAdapter(statusLists, AttendanceStatus.this);

                statusView.setAdapter(statusAdapter);

                if (statusLists.isEmpty()) {
                    statusView.setVisibility(View.GONE);
                    statusNot.setVisibility(View.VISIBLE);
                } else {
                    statusView.setVisibility(View.VISIBLE);
                    statusNot.setVisibility(View.GONE);
                }

                allAppCard.setCardElevation(2);
                allAppBack.setBackgroundColor(getColor(R.color.black_alpha));
                pendingAppCard.setCardElevation(8);
                pendingAppBack.setBackgroundColor(getColor(R.color.white));
                approvedAppCard.setCardElevation(8);
                approvedAppBack.setBackgroundColor(getColor(R.color.white));
                rejectedAppCard.setCardElevation(8);
                rejectedAppBack.setBackgroundColor(getColor(R.color.white));

                all_status_count = String.valueOf(statusLists.size());
                allStatusCount.setText(all_status_count);
                pendingStatusCount.setText(pending_status_count);
                approveStatusCount.setText(approved_status_count);
                rejectedStatusCount.setText(rej_status_count);

                conn = false;
                connected = false;
            }
            else {
                alertMessage();
            }
        }
        else {
            alertMessage();
        }
    }

    public void alertMessage() {
        if (parsing_message != null) {
            if (parsing_message.isEmpty() || parsing_message.equals("null")) {
                parsing_message = "Server problem or Internet not connected";
            }
        }
        else {
            parsing_message = "Server problem or Internet not connected";
        }
        MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(AttendanceStatus.this);
        alertDialogBuilder.setTitle("System Warning!")
                .setIcon(R.drawable.hrm_new_round_icon_custom)
                .setMessage("Message: "+parsing_message+".\n"+"Please try again.")
                .setPositiveButton("Retry", (dialog, which) -> {
                    getAttendStatus();
                    dialog.dismiss();
                })
                .setNegativeButton("Cancel",(dialog, which) -> {
                    dialog.dismiss();
                    finish();
                });

        AlertDialog alert = alertDialogBuilder.create();
        alert.setCancelable(false);
        alert.setCanceledOnTouchOutside(false);
        try {
            alert.show();
        }
        catch (Exception e) {
            restart("App is paused for a long time. Please Start the app again.");
        }
    }

    public void restart(String msg) {
        try {
            ProcessPhoenix.triggerRebirth(getApplicationContext());
        }
        catch (Exception e) {
            Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
            System.exit(0);
        }
    }
}