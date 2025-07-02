package ttit.com.shuvo.ikglhrm.leaveAll.leaveApplication.leaveApplicatinStatus;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

import static ttit.com.shuvo.ikglhrm.Login.userInfoLists;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.api_url_front;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LeaveApplicationStatus extends AppCompatActivity {

    TextView nostatus;
    RecyclerView statusView;
    LeaveAppStatusAdapter statusAdapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<StatusList> leaveAppStatus;

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;
    
    String emp_id = "";

    TextView pendingLeaveCount;
    String pending_leave_count = "0";

    TextView approveLeaveCount;
    String approved_leave_count = "0";

    TextView rejectedLeaveCount;
    String rej_leave_count = "0";

    Logger logger = Logger.getLogger(LeaveApplicationStatus.class.getName());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_application_status);

        emp_id = userInfoLists.get(0).getEmp_id();
        statusView = findViewById(R.id.leave_application_status_list_view);
        nostatus = findViewById(R.id.no_status_found_msg_leave);
        pendingLeaveCount = findViewById(R.id.pending_leave_ls);
        approveLeaveCount = findViewById(R.id.approved_leave_ls);
        rejectedLeaveCount = findViewById(R.id.rejected_leave_ls);

        leaveAppStatus = new ArrayList<>();

        getLeaveStatus();

        statusView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        statusView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(statusView.getContext(),DividerItemDecoration.VERTICAL);
        statusView.addItemDecoration(dividerItemDecoration);
        
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

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat yearFrm = new SimpleDateFormat("yyyy", Locale.ENGLISH);
        String year = yearFrm.format(c);

        pending_leave_count = "0";
        approved_leave_count = "0";
        rej_leave_count = "0";

        String url = api_url_front + "leaveRequest/leaveReqStat/"+emp_id;
        String leaveCountUrl = api_url_front + "dashboard/getLeaveAppStatusCount?emp_id=" + emp_id + "&start_date=01-JAN-" + year + "&end_date=31-DEC-" + year;

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
                connected = false;
                updateLay();
            }
        }, error -> {
            logger.log(Level.WARNING, error.getMessage(), error);
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
                connected = false;
                updateLay();
            }
        }, error -> {
           logger.log(Level.WARNING, error.getMessage(), error);
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

                pendingLeaveCount.setText(pending_leave_count);
                approveLeaveCount.setText(approved_leave_count);
                rejectedLeaveCount.setText(rej_leave_count);
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(LeaveApplicationStatus.this)
                        .setMessage("There is a network issue in the server. Please Try later.")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    getLeaveStatus();
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
            AlertDialog dialog = new AlertDialog.Builder(LeaveApplicationStatus.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel",null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                getLeaveStatus();
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