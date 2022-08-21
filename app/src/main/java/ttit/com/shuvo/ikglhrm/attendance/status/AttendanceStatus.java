package ttit.com.shuvo.ikglhrm.attendance.status;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import ttit.com.shuvo.ikglhrm.EmployeeInfo.jobDesc.JobAdapter;
import ttit.com.shuvo.ikglhrm.EmployeeInfo.jobDesc.JobDescDetails;
import ttit.com.shuvo.ikglhrm.EmployeeInfo.performance.PerformanceApp;
import ttit.com.shuvo.ikglhrm.R;
import ttit.com.shuvo.ikglhrm.WaitProgress;

import static ttit.com.shuvo.ikglhrm.Login.userInfoLists;
import static ttit.com.shuvo.ikglhrm.OracleConnection.createConnection;

public class AttendanceStatus extends AppCompatActivity {


    TextView statusNot;
    Button close;
    RecyclerView statusView;
    public static StatusAdapter statusAdapter;
    RecyclerView.LayoutManager layoutManager;

    public static ArrayList<StatusList> statusLists;

    WaitProgress waitProgress = new WaitProgress();
    private String message = null;
    private Boolean conn = false;
    private Boolean infoConnected = false;
    private Boolean connected = false;

    private Connection connection;

    String emp_id = "";

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

        setContentView(R.layout.activity_attendance_status);

        emp_id = userInfoLists.get(0).getEmp_id();

        statusView = findViewById(R.id.status_list_view);

        close = findViewById(R.id.att_status_finish);

        statusNot = findViewById(R.id.status_not_found_msg);

        statusLists = new ArrayList<>();

        new Check().execute();

        statusView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        statusView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(statusView.getContext(),DividerItemDecoration.VERTICAL);
        statusView.addItemDecoration(dividerItemDecoration);


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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

                GpiDetails();
                if (connected) {
                    conn = true;
                    message= "Internet Connected";
                }

            } else {
                conn = false;
                connected = false;
                message = "Not Connected";
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            waitProgress.dismiss();
            if (conn) {

                statusAdapter = new StatusAdapter(statusLists, AttendanceStatus.this);

                statusView.setAdapter(statusAdapter);

                if (statusLists.size() == 0) {
                    statusView.setVisibility(View.GONE);
                    statusNot.setVisibility(View.VISIBLE);
                } else {
                    statusView.setVisibility(View.VISIBLE);
                    statusNot.setVisibility(View.GONE);
                }

                conn = false;
                connected = false;



            }else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(AttendanceStatus.this)
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

    public void GpiDetails() {
        try {
            this.connection = createConnection();
            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();


            statusLists = new ArrayList<>();
            Statement stmt = connection.createStatement();




            ResultSet rs=stmt.executeQuery("SELECT daily_atten_req_mst.darm_id,\n" +
                    "                     DAILY_ATTEN_REQ_MST.DARM_APP_CODE,\n" +
                    "                     daily_atten_req_mst.darm_approved,\n" +
                    "                     TO_CHAR(DAILY_ATTEN_REQ_MST.DARM_DATE,'DD-MON-YYYY') DARM_DATE,DAILY_ATTEN_REQ_MST.DARM_REQ_TYPE,\n" +
                    "                    TO_CHAR(DAILY_ATTEN_REQ_MST.DARM_UPDATE_DATE,'DD-MON-YYYY') DARM_UPDATE_DATE,\n" +
                    "                        TO_CHAR(DAILY_ATTEN_REQ_MST.DARM_REQ_ARRIVAL_TIME,'HH:MI AM') ARRIVAL_TIME,\n" +
                    "                        TO_CHAR(DAILY_ATTEN_REQ_MST.DARM_REQ_DEPART_TIME,'HH:MI AM') DEPARTURE_TIME,\n" +
                    "                        emp_name\n" +
                    "                        FROM DAILY_ATTEN_REQ_MST, EMP_MST, DIVISION_MST\n" +
                    "                    WHERE \n" +
                    "                      (EMP_MST.EMP_ID(+) = daily_atten_req_mst.darm_app_reject_emp_id)\n" +
                    "                     AND (DIVISION_MST.DIVM_ID = DAILY_ATTEN_REQ_MST.DARM_DIVM_ID)\n" +
                    "                     AND (DAILY_ATTEN_REQ_MST.DARM_EMP_ID = "+emp_id+")\n" +
                    "                     Order by 1 desc");



            while(rs.next()) {

               statusLists.add(new StatusList(rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9),null));

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