package ttit.com.shuvo.ikglhrm.attendance;

import static ttit.com.shuvo.ikglhrm.Login.SoftwareName;
import static ttit.com.shuvo.ikglhrm.Login.isApproved;
import static ttit.com.shuvo.ikglhrm.Login.userInfoLists;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import ttit.com.shuvo.ikglhrm.R;
import ttit.com.shuvo.ikglhrm.WaitProgress;
import ttit.com.shuvo.ikglhrm.attendance.approve.AttendanceApprove;
import ttit.com.shuvo.ikglhrm.attendance.status.AttendanceStatus;
import ttit.com.shuvo.ikglhrm.attendance.update.AttendanceUpdate;

public class AttendanceUpdateAll extends AppCompatActivity {

    CardView attUpdate;
    CardView attReqUpdate;
    CardView attStatus;
    CardView attApprove;

    Button attClose;
    TextView softName;

    WaitProgress waitProgress = new WaitProgress();
//    private String message = null;
    private Boolean conn = false;
    private Boolean connected = false;

//    private Connection connection;

    String userName = "";
    int isApprovedCheckAgain = 0;


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

        setContentView(R.layout.activity_attendance_update_all);

        userName = userInfoLists.get(0).getUserName();

        softName = findViewById(R.id.name_of_company_attendance_update_all);

        attUpdate = findViewById(R.id.atten_update_req);
        attReqUpdate = findViewById(R.id.attendance_update_request_upd);
        attStatus = findViewById(R.id.attendane_update_status);
        attApprove = findViewById(R.id.atten_update_req_approval);

        attClose = findViewById(R.id.attendance_update_all_back);

        softName.setText(SoftwareName);

        if (isApproved > 0) {
            attApprove.setVisibility(View.VISIBLE);
        } else {
            attApprove.setVisibility(View.GONE);
        }

        attUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AttendanceUpdateAll.this, AttendanceUpdate.class);
                startActivity(intent);
            }
        });

//        attReqUpdate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(AttendanceUpdateAll.this, AttendanceReqUpdate.class);
//                startActivity(intent);
//            }
//        });

        attStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AttendanceUpdateAll.this, AttendanceStatus.class);
                startActivity(intent);
            }
        });

        attClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        attApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AttendanceUpdateAll.this, AttendanceApprove.class);
                startActivity(intent);
            }
        });

//        new Check().execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        approvedButtonCheck();
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
//                ApproveButtonCheck();
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
//                if (isApprovedCheckAgain > 0) {
//                    attApprove.setVisibility(View.VISIBLE);
//                } else {
//                    attApprove.setVisibility(View.GONE);
//                }
//
//                conn = false;
//                connected = false;
//
//
//            }
//            else {
//                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
//                AlertDialog dialog = new AlertDialog.Builder(AttendanceUpdateAll.this)
//                        .setMessage("Please Check Your Internet Connection")
//                        .setPositiveButton("Retry", null)
//                        .show();
//
////                dialog.setCancelable(false);
////                dialog.setCanceledOnTouchOutside(false);
//                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
//                positive.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        new Check().execute();
//                        dialog.dismiss();
//                    }
//                });
//            }
//        }
//    }
//
//    public void ApproveButtonCheck() {
//        try {
//            this.connection = createConnection();
//
//            Statement stmt = connection.createStatement();
//
//            isApprovedCheckAgain = 0;
//
//            ResultSet resultSet2 = stmt.executeQuery("SELECT COUNT (1) VAL\n" +
//                    "  FROM DAILY_ATTEN_REQ_MST\n" +
//                    " WHERE DARM_APP_CODE IN\n" +
//                    "          (SELECT DAAHL_APP_CODE\n" +
//                    "             FROM DAILY_ATTEN_APP_HIERARCHY_LOG, DAILY_ATTEN_REQ_MST\n" +
//                    "            WHERE     DAILY_ATTEN_APP_HIERARCHY_LOG.DAAHL_APP_CODE =\n" +
//                    "                         DAILY_ATTEN_REQ_MST.DARM_APP_CODE\n" +
//                    "                  AND NVL (DAILY_ATTEN_REQ_MST.DARM_APPROVED, 0) = 0\n" +
//                    "                  AND (DAILY_ATTEN_APP_HIERARCHY_LOG.DAAHL_APPROVER_BAND_ID =\n" +
//                    "                          (SELECT EMP_ID\n" +
//                    "                             FROM EMP_MST\n" +
//                    "                            WHERE EMP_CODE = '"+userName+"')))");
//
//            while (resultSet2.next()) {
//                isApprovedCheckAgain = resultSet2.getInt(1);
//                System.out.println(isApprovedCheckAgain);
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

    public void approvedButtonCheck() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;

        isApprovedCheckAgain = 0;

        String attendanceAppUrl = "http://103.56.208.123:8001/apex/ttrams/approval_flag/getAttendanceApproval/"+userName+"";

        RequestQueue requestQueue = Volley.newRequestQueue(AttendanceUpdateAll.this);

        StringRequest attendAppReq = new StringRequest(Request.Method.GET,attendanceAppUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject attAppInfo = array.getJSONObject(i);
                        isApprovedCheckAgain = attAppInfo.getInt("val");
                    }
                }
                connected = true;
                updateLay();
            }
            catch (JSONException e) {
                connected = false;
                e.printStackTrace();
                updateLay();
            }
        },error -> {
            conn = false;
            connected = false;
            error.printStackTrace();
            updateLay();
        });

        requestQueue.add(attendAppReq);
    }

    private void updateLay() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                if (isApprovedCheckAgain > 0) {
                    attApprove.setVisibility(View.VISIBLE);
                } else {
                    attApprove.setVisibility(View.GONE);
                }

                conn = false;
                connected = false;
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(AttendanceUpdateAll.this)
                        .setMessage("There is a network issue in the server. Please Try later")
                        .setPositiveButton("Retry", null)
                        .show();

                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    approvedButtonCheck();
                    dialog.dismiss();
                });
            }
        }
        else {
            AlertDialog dialog = new AlertDialog.Builder(AttendanceUpdateAll.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .show();

            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                approvedButtonCheck();
                dialog.dismiss();
            });
        }
    }
}