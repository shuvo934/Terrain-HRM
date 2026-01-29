package ttit.com.shuvo.ikglhrm.leaveAll.leaveApplication.leaveApplicatinStatus;

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
import ttit.com.shuvo.ikglhrm.attendance.status.StatusList;

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

public class LeaveApplicationStatus extends AppCompatActivity {

    TextView nostatus;
    RecyclerView statusView;
    LeaveAppStatusAdapter statusAdapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<StatusList> leaveAppStatus;
    ArrayList<StatusList> filterLists;

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;
    
    String emp_id = "";

    TextView allLeaveCount;
    String all_leave_count = "0";

    TextView pendingLeaveCount;
    String pending_leave_count = "0";

    TextView approveLeaveCount;
    String approved_leave_count = "0";

    TextView rejectedLeaveCount;
    String rej_leave_count = "0";

    MaterialCardView allLvCard;
    RelativeLayout allLvBack;
    MaterialCardView pendingLvCard;
    RelativeLayout pendingLvBack;
    MaterialCardView approvedLvCard;
    RelativeLayout approvedLvBack;
    MaterialCardView rejectedLvCard;
    RelativeLayout rejectedLvBack;

    LinearLayout yearSelection;
    TextView yearText;
    String selected_year = "";
    AlertDialog yearDialogAppoint;

    Logger logger = Logger.getLogger(LeaveApplicationStatus.class.getName());
    String parsing_message = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_application_status);

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
        statusView = findViewById(R.id.leave_application_status_list_view);
        nostatus = findViewById(R.id.no_status_found_msg_leave);
        allLeaveCount = findViewById(R.id.all_leave_app_request_ls);
        pendingLeaveCount = findViewById(R.id.pending_leave_ls);
        approveLeaveCount = findViewById(R.id.approved_leave_ls);
        rejectedLeaveCount = findViewById(R.id.rejected_leave_ls);

        allLvCard = findViewById(R.id.total_leave_app_card);
        allLvBack = findViewById(R.id.all_leave_app_card_background);
        pendingLvCard = findViewById(R.id.pending_leave_app_card);
        pendingLvBack = findViewById(R.id.pending_leave_app_card_background);
        approvedLvCard = findViewById(R.id.approved_leave_app_card);
        approvedLvBack = findViewById(R.id.approved_leave_app_card_background);
        rejectedLvCard = findViewById(R.id.rejected_leave_app_card);
        rejectedLvBack = findViewById(R.id.rejected_leave_app_card_background);

        yearSelection = findViewById(R.id.year_selected_layout_for_leave_status);
        yearText = findViewById(R.id.year_text_for_leave_status);

        leaveAppStatus = new ArrayList<>();
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

        allLvCard.setOnClickListener(v -> {
            allLvCard.setCardElevation(2);
            allLvBack.setBackgroundColor(getColor(R.color.black_alpha));
            pendingLvCard.setCardElevation(10);
            pendingLvBack.setBackgroundColor(getColor(R.color.white));
            approvedLvCard.setCardElevation(10);
            approvedLvBack.setBackgroundColor(getColor(R.color.white));
            rejectedLvCard.setCardElevation(10);
            rejectedLvBack.setBackgroundColor(getColor(R.color.white));

            statusAdapter = new LeaveAppStatusAdapter(leaveAppStatus, LeaveApplicationStatus.this);
            statusView.setAdapter(statusAdapter);

            if (leaveAppStatus.isEmpty()) {
                statusView.setVisibility(View.GONE);
                nostatus.setVisibility(View.VISIBLE);
            } else {
                statusView.setVisibility(View.VISIBLE);
                nostatus.setVisibility(View.GONE);
            }
        });

        pendingLvCard.setOnClickListener(v -> {
            allLvCard.setCardElevation(10);
            allLvBack.setBackgroundColor(getColor(R.color.white));
            pendingLvCard.setCardElevation(2);
            pendingLvBack.setBackgroundColor(getColor(R.color.black_alpha));
            approvedLvCard.setCardElevation(10);
            approvedLvBack.setBackgroundColor(getColor(R.color.white));
            rejectedLvCard.setCardElevation(10);
            rejectedLvBack.setBackgroundColor(getColor(R.color.white));
            getFilteredList("0");
        });

        approvedLvCard.setOnClickListener(v -> {
            allLvCard.setCardElevation(10);
            allLvBack.setBackgroundColor(getColor(R.color.white));
            pendingLvCard.setCardElevation(10);
            pendingLvBack.setBackgroundColor(getColor(R.color.white));
            approvedLvCard.setCardElevation(2);
            approvedLvBack.setBackgroundColor(getColor(R.color.black_alpha));
            rejectedLvCard.setCardElevation(10);
            rejectedLvBack.setBackgroundColor(getColor(R.color.white));
            getFilteredList("1");
        });

        rejectedLvCard.setOnClickListener(v -> {
            allLvCard.setCardElevation(10);
            allLvBack.setBackgroundColor(getColor(R.color.white));
            pendingLvCard.setCardElevation(10);
            pendingLvBack.setBackgroundColor(getColor(R.color.white));
            approvedLvCard.setCardElevation(10);
            approvedLvBack.setBackgroundColor(getColor(R.color.white));
            rejectedLvCard.setCardElevation(2);
            rejectedLvBack.setBackgroundColor(getColor(R.color.black_alpha));
            getRejectFilteredList();
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

            MonthPickerDialog.Builder appBuilder = new MonthPickerDialog.Builder(LeaveApplicationStatus.this, (selectedMonth, selectedYear) -> {
                String ms = "Application Year : " + selectedYear;
                yearText.setText(ms);
                selected_year = String.valueOf(selectedYear);
                getLeaveStatus();

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

        getLeaveStatus();
    }

    private void getFilteredList(String laApproved) {
        filterLists = new ArrayList<>();
        for (int i = 0; i < leaveAppStatus.size(); i++) {
            if (leaveAppStatus.get(i).getApproved().equals(laApproved)) {
                String la_app_code_new = leaveAppStatus.get(i).getApp_code();
                String la_approved_new = leaveAppStatus.get(i).getApproved();
                String la_date_new = leaveAppStatus.get(i).getReq_date();
                String leave_type_new = leaveAppStatus.get(i).getReq_type();
                String la_from_date_new = leaveAppStatus.get(i).getUp_date();
                String la_to_date_new = leaveAppStatus.get(i).getArr_time();
                String la_leave_days_new = leaveAppStatus.get(i).getDep_time();
                String emp_name_new = leaveAppStatus.get(i).getApprover();
                String canceller = leaveAppStatus.get(i).getCanceler();

                filterLists.add(new StatusList(la_app_code_new,la_approved_new,la_date_new,
                        leave_type_new,la_from_date_new,la_to_date_new,la_leave_days_new,
                        emp_name_new,canceller));
            }
        }

        statusAdapter = new LeaveAppStatusAdapter(filterLists, LeaveApplicationStatus.this);
        statusView.setAdapter(statusAdapter);

        if (filterLists.isEmpty()) {
            statusView.setVisibility(View.GONE);
            nostatus.setVisibility(View.VISIBLE);
        } else {
            statusView.setVisibility(View.VISIBLE);
            nostatus.setVisibility(View.GONE);
        }
    }

    private void getRejectFilteredList() {
        filterLists = new ArrayList<>();
        for (int i = 0; i < leaveAppStatus.size(); i++) {
            if (leaveAppStatus.get(i).getApproved().equals("2") || leaveAppStatus.get(i).getApproved().equals("3")) {
                String la_app_code_new = leaveAppStatus.get(i).getApp_code();
                String la_approved_new = leaveAppStatus.get(i).getApproved();
                String la_date_new = leaveAppStatus.get(i).getReq_date();
                String leave_type_new = leaveAppStatus.get(i).getReq_type();
                String la_from_date_new = leaveAppStatus.get(i).getUp_date();
                String la_to_date_new = leaveAppStatus.get(i).getArr_time();
                String la_leave_days_new = leaveAppStatus.get(i).getDep_time();
                String emp_name_new = leaveAppStatus.get(i).getApprover();
                String canceller = leaveAppStatus.get(i).getCanceler();

                filterLists.add(new StatusList(la_app_code_new,la_approved_new,la_date_new,
                        leave_type_new,la_from_date_new,la_to_date_new,la_leave_days_new,
                        emp_name_new,canceller));

            }
        }

        statusAdapter = new LeaveAppStatusAdapter(filterLists, LeaveApplicationStatus.this);
        statusView.setAdapter(statusAdapter);

        if (filterLists.isEmpty()) {
            statusView.setVisibility(View.GONE);
            nostatus.setVisibility(View.VISIBLE);
        } else {
            statusView.setVisibility(View.VISIBLE);
            nostatus.setVisibility(View.GONE);
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
//                statusAdapter = new LeaveAppStatusAdapter(leaveAppStatus, LeaveApplicationStatus.this);
//
//                statusView.setAdapter(statusAdapter);
//
//                if (leaveAppStatus.size() == 0) {
//                    statusView.setVisibility(View.GONE);
//                    nostatus.setVisibility(View.VISIBLE);
//                } else {
//                    statusView.setVisibility(View.VISIBLE);
//                    nostatus.setVisibility(View.GONE);
//                }
//
//
//
//            }
//            else {
//                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
//                AlertDialog dialog = new AlertDialog.Builder(LeaveApplicationStatus.this)
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
//
//    public void GpiDetails() {
//        try {
//            this.connection = createConnection();
//            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();
//
//
//            leaveAppStatus = new ArrayList<>();
//            Statement stmt = connection.createStatement();
//
//
//
//
//            ResultSet rs=stmt.executeQuery("SELECT LEAVE_APPLICATION.LA_ID,\n" +
//                    "                                         LEAVE_APPLICATION.LA_APP_CODE,\n" +
//                    "                                         LEAVE_APPLICATION.LA_APPROVED,\n" +
//                    "                                         TO_CHAR(LEAVE_APPLICATION.LA_DATE,'DD-MON-YYYY') LA_DATE,\n" +
//                    "                                         (Select LEAVE_CATEGORY.LC_NAME FROM LEAVE_CATEGORY WHERE LEAVE_CATEGORY.LC_ID = LEAVE_APPLICATION.LA_LC_ID) as LEAVE_TYPE,\n" +
//                    "                                        TO_CHAR(LEAVE_APPLICATION.LA_FROM_DATE,'DD-MON-YYYY') LA_FROM_DATE,\n" +
//                    "                                            TO_CHAR(LEAVE_APPLICATION.LA_TO_DATE,'DD-MON-YYYY') LA_TO_DATE,\n" +
//                    "                                            LEAVE_APPLICATION.LA_LEAVE_DAYS,\n" +
//                    "                                            emp_mst.emp_name, cncl.emp_name\n" +
//                    "                                            FROM LEAVE_APPLICATION, EMP_MST, DIVISION_MST, emp_mst cncl\n" +
//                    "                                        WHERE \n" +
//                    "                                          (EMP_MST.EMP_ID(+) = LEAVE_APPLICATION.LA_APP_REJECT_EMP_ID)\n" +
//                    "                                          and cncl.emp_id(+) = leave_application.la_cancel_emp_id\n" +
//                    "                                         AND (DIVISION_MST.DIVM_ID = LEAVE_APPLICATION.LA_DIVM_ID)\n" +
//                    "                                         AND (LEAVE_APPLICATION.LA_EMP_ID = "+emp_id+")\n" +
//                    "                                         Order by 1 desc");
//
//
//
//            while(rs.next()) {
//
//                leaveAppStatus.add(new StatusList(rs.getString(2),rs.getString(3),rs.getString(4),
//                        rs.getString(5),rs.getString(6),rs.getString(7),
//                        rs.getString(8),rs.getString(9),rs.getString(10)));
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

    public void getLeaveStatus() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;

        leaveAppStatus = new ArrayList<>();
        filterLists = new ArrayList<>();

        pending_leave_count = "0";
        approved_leave_count = "0";
        rej_leave_count = "0";
        all_leave_count = "0";

        String url = api_url_front + "leaveRequest/leaveReqStatNew?p_emp_id="+emp_id+"&p_year="+selected_year;
        String leaveCountUrl = api_url_front + "dashboard/getLeaveAppStatusCount?emp_id=" + emp_id + "&start_date=01-JAN-" + selected_year + "&end_date=31-DEC-" + selected_year;

        RequestQueue requestQueue = Volley.newRequestQueue(LeaveApplicationStatus.this);

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
                connected = true;
                updateLay();
            } catch (JSONException e) {
                logger.log(Level.WARNING, e.getMessage(), e);
                parsing_message = e.getLocalizedMessage();
                connected = false;
                updateLay();
            }
        }, error -> {
            logger.log(Level.WARNING, error.getMessage(), error);
            parsing_message = error.getLocalizedMessage();
            conn = false;
            connected = false;
            updateLay();
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
                        JSONObject leaveStatInfo = array.getJSONObject(i);

//                        String la_id_new = leaveStatInfo.getString("la_id")
//                                .equals("null") ? "" : leaveStatInfo.getString("la_id");
                        String la_app_code_new = leaveStatInfo.getString("la_app_code")
                                .equals("null") ? "" : leaveStatInfo.getString("la_app_code");
                        String la_approved_new = leaveStatInfo.getString("la_approved")
                                .equals("null") ? "" : leaveStatInfo.getString("la_approved");
                        String la_date_new = leaveStatInfo.getString("la_date")
                                .equals("null") ? "" : leaveStatInfo.getString("la_date");
                        String leave_type_new = leaveStatInfo.getString("leave_type")
                                .equals("null") ? "" : leaveStatInfo.getString("leave_type");
                        String la_from_date_new = leaveStatInfo.getString("la_from_date")
                                .equals("null") ? "" : leaveStatInfo.getString("la_from_date");
                        String la_to_date_new = leaveStatInfo.getString("la_to_date")
                                .equals("null") ? "" : leaveStatInfo.getString("la_to_date");
                        String la_leave_days_new = leaveStatInfo.getString("la_leave_days")
                                .equals("null") ? "" : leaveStatInfo.getString("la_leave_days");
                        String emp_name_new = leaveStatInfo.getString("emp_name")
                                .equals("null") ? "" : leaveStatInfo.getString("emp_name");
                        String canceller = leaveStatInfo.getString("emp_name_other")
                                .equals("null") ? null : leaveStatInfo.getString("emp_name_other");


                        leaveAppStatus.add(new StatusList(la_app_code_new,la_approved_new,la_date_new,
                                leave_type_new,la_from_date_new,la_to_date_new,la_leave_days_new,
                                emp_name_new,canceller));

                    }
                }

                requestQueue.add(leaveCountReq);
            }
            catch (JSONException e) {
                logger.log(Level.WARNING, e.getMessage(), e);
                parsing_message = e.getLocalizedMessage();
                connected = false;
                updateLay();
            }
        }, error -> {
           logger.log(Level.WARNING, error.getMessage(), error);
           parsing_message = error.getLocalizedMessage();
           conn = false;
           connected = false;
           updateLay();
        });

        requestQueue.add(stringRequest);

    }

    private void updateLay() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                statusAdapter = new LeaveAppStatusAdapter(leaveAppStatus, LeaveApplicationStatus.this);

                statusView.setAdapter(statusAdapter);

                if (leaveAppStatus.isEmpty()) {
                    statusView.setVisibility(View.GONE);
                    nostatus.setVisibility(View.VISIBLE);
                } else {
                    statusView.setVisibility(View.VISIBLE);
                    nostatus.setVisibility(View.GONE);
                }

                allLvCard.setCardElevation(2);
                allLvBack.setBackgroundColor(getColor(R.color.black_alpha));
                pendingLvCard.setCardElevation(8);
                pendingLvBack.setBackgroundColor(getColor(R.color.white));
                approvedLvCard.setCardElevation(8);
                approvedLvBack.setBackgroundColor(getColor(R.color.white));
                rejectedLvCard.setCardElevation(8);
                rejectedLvBack.setBackgroundColor(getColor(R.color.white));

                all_leave_count = String.valueOf(leaveAppStatus.size());
                allLeaveCount.setText(all_leave_count);
                pendingLeaveCount.setText(pending_leave_count);
                approveLeaveCount.setText(approved_leave_count);
                rejectedLeaveCount.setText(rej_leave_count);
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
        MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(LeaveApplicationStatus.this);
        alertDialogBuilder.setTitle("System Warning!")
                .setIcon(R.drawable.hrm_new_round_icon_custom)
                .setMessage("Message: "+parsing_message+".\n"+"Please try again.")
                .setPositiveButton("Retry", (dialog, which) -> {
                    getLeaveStatus();
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