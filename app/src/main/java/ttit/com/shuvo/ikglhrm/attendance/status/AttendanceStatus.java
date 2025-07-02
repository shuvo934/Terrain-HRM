package ttit.com.shuvo.ikglhrm.attendance.status;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import ttit.com.shuvo.ikglhrm.R;
import ttit.com.shuvo.ikglhrm.WaitProgress;

import static ttit.com.shuvo.ikglhrm.Login.userInfoLists;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.api_url_front;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AttendanceStatus extends AppCompatActivity {


    TextView statusNot;
    RecyclerView statusView;
    StatusAdapter statusAdapter;
    RecyclerView.LayoutManager layoutManager;

    ArrayList<StatusList> statusLists;

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;

    String emp_id = "";

    TextView pendingStatusCount;
    String pending_status_count = "0";

    TextView approveStatusCount;
    String approved_status_count = "0";

    TextView rejectedStatusCount;
    String rej_status_count = "0";

    Logger logger = Logger.getLogger(AttendanceStatus.class.getName());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_status);

        emp_id = userInfoLists.get(0).getEmp_id();

        statusView = findViewById(R.id.status_list_view);
        statusNot = findViewById(R.id.status_not_found_msg);
        pendingStatusCount = findViewById(R.id.pending_request_as);
        approveStatusCount = findViewById(R.id.approved_request_as);
        rejectedStatusCount = findViewById(R.id.rejected_request_as);

        statusLists = new ArrayList<>();

        getAttendStatus();

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

        pending_status_count = "0";
        approved_status_count = "0";
        rej_status_count = "0";

        String url = api_url_front + "attendanceStatus/attStatus/"+emp_id;
        String stat_url = api_url_front + "attendanceStatus/getAttStatusCount?emp_id="+emp_id;

        RequestQueue requestQueue = Volley.newRequestQueue(AttendanceStatus.this);

        StringRequest asCountReq = new StringRequest(Request.Method.GET, stat_url, response -> {
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
                connected = false;
                updateLayout();
            }
        }, error -> {
            logger.log(Level.WARNING, error.getMessage(), error);
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
                connected = false;
                updateLayout();
            }
        }, error -> {
            logger.log(Level.WARNING, error.getMessage(), error);
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

                pendingStatusCount.setText(pending_status_count);
                approveStatusCount.setText(approved_status_count);
                rejectedStatusCount.setText(rej_status_count);

                conn = false;
                connected = false;
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(AttendanceStatus.this)
                        .setMessage("There is a network issue in the server. Please Try later.")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel", null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    getAttendStatus();
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
            AlertDialog dialog = new AlertDialog.Builder(AttendanceStatus.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel", null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                getAttendStatus();
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