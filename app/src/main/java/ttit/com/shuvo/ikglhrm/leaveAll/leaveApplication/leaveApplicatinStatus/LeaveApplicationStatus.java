package ttit.com.shuvo.ikglhrm.leaveAll.leaveApplication.leaveApplicatinStatus;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
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

import ttit.com.shuvo.ikglhrm.R;
import ttit.com.shuvo.ikglhrm.WaitProgress;
import ttit.com.shuvo.ikglhrm.attendance.status.AttendanceStatus;
import ttit.com.shuvo.ikglhrm.attendance.status.StatusAdapter;
import ttit.com.shuvo.ikglhrm.attendance.status.StatusList;
import ttit.com.shuvo.ikglhrm.leaveAll.leaveStatus.LeaveStatus;

import static ttit.com.shuvo.ikglhrm.Login.userInfoLists;
import static ttit.com.shuvo.ikglhrm.OracleConnection.createConnection;

public class LeaveApplicationStatus extends AppCompatActivity {

    TextView nostatus;
    Button close;
    RecyclerView statusView;
    public static LeaveAppStatusAdapter statusAdapter;
    RecyclerView.LayoutManager layoutManager;

    public static ArrayList<StatusList> leaveAppStatus;

    WaitProgress waitProgress = new WaitProgress();
    private String message = null;
    private Boolean conn = false;
    private Boolean infoConnected = false;
    private Boolean connected = false;

    private Connection connection;

    String emp_id = "";

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
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(LeaveApplicationStatus.this,R.color.secondaryColor));
        setContentView(R.layout.activity_leave_application_status);

        emp_id = userInfoLists.get(0).getEmp_id();

        statusView = findViewById(R.id.leave_application_status_list_view);

        nostatus = findViewById(R.id.no_status_found_msg_leave);

        close = findViewById(R.id.leave_app_status_finish);

        leaveAppStatus = new ArrayList<>();

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

                statusAdapter = new LeaveAppStatusAdapter(leaveAppStatus, LeaveApplicationStatus.this);

                statusView.setAdapter(statusAdapter);

                if (leaveAppStatus.size() == 0) {
                    statusView.setVisibility(View.GONE);
                    nostatus.setVisibility(View.VISIBLE);
                } else {
                    statusView.setVisibility(View.VISIBLE);
                    nostatus.setVisibility(View.GONE);
                }



            }else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(LeaveApplicationStatus.this)
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

    public void GpiDetails() {
        try {
            this.connection = createConnection();
            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();


            leaveAppStatus = new ArrayList<>();
            Statement stmt = connection.createStatement();




            ResultSet rs=stmt.executeQuery("SELECT LEAVE_APPLICATION.LA_ID,\n" +
                    "                                         LEAVE_APPLICATION.LA_APP_CODE,\n" +
                    "                                         LEAVE_APPLICATION.LA_APPROVED,\n" +
                    "                                         TO_CHAR(LEAVE_APPLICATION.LA_DATE,'DD-MON-YYYY') LA_DATE,\n" +
                    "                                         (Select LEAVE_CATEGORY.LC_NAME FROM LEAVE_CATEGORY WHERE LEAVE_CATEGORY.LC_ID = LEAVE_APPLICATION.LA_LC_ID) as LEAVE_TYPE,\n" +
                    "                                        TO_CHAR(LEAVE_APPLICATION.LA_FROM_DATE,'DD-MON-YYYY') LA_FROM_DATE,\n" +
                    "                                            TO_CHAR(LEAVE_APPLICATION.LA_TO_DATE,'DD-MON-YYYY') LA_TO_DATE,\n" +
                    "                                            LEAVE_APPLICATION.LA_LEAVE_DAYS,\n" +
                    "                                            emp_mst.emp_name, cncl.emp_name\n" +
                    "                                            FROM LEAVE_APPLICATION, EMP_MST, DIVISION_MST, emp_mst cncl\n" +
                    "                                        WHERE \n" +
                    "                                          (EMP_MST.EMP_ID(+) = LEAVE_APPLICATION.LA_APP_REJECT_EMP_ID)\n" +
                    "                                          and cncl.emp_id(+) = leave_application.la_cancel_emp_id\n" +
                    "                                         AND (DIVISION_MST.DIVM_ID = LEAVE_APPLICATION.LA_DIVM_ID)\n" +
                    "                                         AND (LEAVE_APPLICATION.LA_EMP_ID = "+emp_id+")\n" +
                    "                                         Order by 1 desc");



            while(rs.next()) {

                leaveAppStatus.add(new StatusList(rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9),rs.getString(10)));

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