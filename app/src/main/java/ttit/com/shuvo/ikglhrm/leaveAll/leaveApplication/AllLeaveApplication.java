package ttit.com.shuvo.ikglhrm.leaveAll.leaveApplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
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
import ttit.com.shuvo.ikglhrm.attendance.AttendanceUpdateAll;
import ttit.com.shuvo.ikglhrm.attendance.reqUpdate.AttendanceReqUpdate;
import ttit.com.shuvo.ikglhrm.attendance.update.dialogue.SelectAllList;
import ttit.com.shuvo.ikglhrm.leaveAll.Leave;
import ttit.com.shuvo.ikglhrm.leaveAll.LeaveApplication;
import ttit.com.shuvo.ikglhrm.leaveAll.leaveApplication.leaveApplicatinStatus.LeaveApplicationStatus;
import ttit.com.shuvo.ikglhrm.leaveAll.leaveApprove.LeaveApprove;

import static ttit.com.shuvo.ikglhrm.Login.SoftwareName;
import static ttit.com.shuvo.ikglhrm.Login.isLeaveApproved;
import static ttit.com.shuvo.ikglhrm.Login.userInfoLists;
import static ttit.com.shuvo.ikglhrm.OracleConnection.createConnection;

public class AllLeaveApplication extends AppCompatActivity {

    CardView newApp;
    CardView appStat;
    CardView leaveApprove;

    TextView softname;


    Button appClose;

    WaitProgress waitProgress = new WaitProgress();
    private String message = null;
    private Boolean conn = false;
    private Boolean connected = false;

    private Connection connection;

    String user_id = "";
    int isLeaveApprovedCheck = 0;

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

        setContentView(R.layout.activity_all_leave_application);



        newApp = findViewById(R.id.leave_application_new_app);
        appStat = findViewById(R.id.leave_application_status_show);
        leaveApprove = findViewById(R.id.leave_req_approval);
        softname = findViewById(R.id.name_of_company_all_leave_application);

        softname.setText(SoftwareName);

        appClose = findViewById(R.id.leave_app_stat_all_back);

        if (isLeaveApproved > 0) {
            leaveApprove.setVisibility(View.VISIBLE);
        } else {
            leaveApprove.setVisibility(View.GONE);
        }

        user_id = userInfoLists.get(0).getUserName();

        newApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllLeaveApplication.this, LeaveApplication.class);
                startActivity(intent);
            }
        });

        appStat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllLeaveApplication.this, LeaveApplicationStatus.class);
                startActivity(intent);
            }
        });

        appClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        leaveApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllLeaveApplication.this, LeaveApprove.class);
                startActivity(intent);
            }
        });

        new Check().execute();
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

                LeaveApproveCheck();
                if (connected) {
                    conn = true;
                    System.out.println("INSERTED");
                    message= "Internet Connected";
                }

            } else {
                conn = false;
                message = "Not Connected";
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            waitProgress.dismiss();
            if (conn) {


                if (isLeaveApprovedCheck > 0) {
                    leaveApprove.setVisibility(View.VISIBLE);
                } else {
                    leaveApprove.setVisibility(View.GONE);
                }

                conn = false;
                connected = false;



            }else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(AllLeaveApplication.this)
                        .setMessage("Please Check Your Internet Connection")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();

//                dialog.setCancelable(false);
//                dialog.setCanceledOnTouchOutside(false);
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
                    }
                });
            }
        }
    }

    public void LeaveApproveCheck() {
        try {
            this.connection = createConnection();

            Statement stmt = connection.createStatement();

            isLeaveApprovedCheck = 0;

            ResultSet rs = stmt.executeQuery("SELECT count(DISTINCT LA_APP_CODE)\n" +
                    "  FROM (SELECT LA.LA_APP_CODE\n" +
                    "          FROM LEAVE_APPLICATION LA\n" +
                    "         WHERE     LA.LA_EMP_ID IN\n" +
                    "                      (SELECT C.JOB_EMP_ID\n" +
                    "                         FROM JOB_SETUP_MST A,\n" +
                    "                              JOB_SETUP_DTL B,\n" +
                    "                              (SELECT JOB_EMP_ID, JOB_JSD_ID\n" +
                    "                                 FROM EMP_JOB_HISTORY EJH\n" +
                    "                                WHERE JOB_ID =\n" +
                    "                                         (SELECT MAX (JOB_ID)\n" +
                    "                                            FROM EMP_JOB_HISTORY EH\n" +
                    "                                           WHERE EJH.JOB_EMP_ID =\n" +
                    "                                                    EH.JOB_EMP_ID)) C,\n" +
                    "                              DESIG_MST     D   --, leave_approval_hierarchy e\n" +
                    "                        WHERE     A.JSM_ID = B.JSD_JSM_ID\n" +
                    "                              AND C.JOB_JSD_ID = B.JSD_ID\n" +
                    "                              AND D.DESIG_ID = A.JSM_DESIG_ID\n" +
                    "                              AND D.DESIG_PRIORITY IN\n" +
                    "                                     (SELECT L.LAH_BAND_NO\n" +
                    "                                        FROM LEAVE_APPROVAL_HIERARCHY L\n" +
                    "                                       WHERE INSTR (\n" +
                    "                                                   ','\n" +
                    "                                                || L.LAH_APPROVAL_BAND\n" +
                    "                                                || ',',\n" +
                    "                                                   ','\n" +
                    "                                                || EMP_PACKAGE.GET_BAND_BY_EMP_CODE (\n" +
                    "                                                      '"+user_id+"')\n" +
                    "                                                || ',') > 0))\n" +
                    "               AND NVL (LA.LA_APPROVED, 0) = 0\n" +
                    "        UNION ALL\n" +
                    "        SELECT LA.LA_APP_CODE\n" +
                    "          FROM LEAVE_APPLICATION LA\n" +
                    "         WHERE     LA.LA_EMP_ID IN\n" +
                    "                      (SELECT C.JOB_EMP_ID\n" +
                    "                         FROM JOB_SETUP_MST A,\n" +
                    "                              JOB_SETUP_DTL B,\n" +
                    "                              (SELECT JOB_EMP_ID, JOB_JSD_ID\n" +
                    "                                 FROM EMP_JOB_HISTORY EJH\n" +
                    "                                WHERE JOB_ID =\n" +
                    "                                         (SELECT MAX (JOB_ID)\n" +
                    "                                            FROM EMP_JOB_HISTORY EH\n" +
                    "                                           WHERE EJH.JOB_EMP_ID =\n" +
                    "                                                    EH.JOB_EMP_ID)) C,\n" +
                    "                              DESIG_MST     D   --, leave_approval_hierarchy e\n" +
                    "                        WHERE     A.JSM_ID = B.JSD_JSM_ID\n" +
                    "                              AND C.JOB_JSD_ID = B.JSD_ID\n" +
                    "                              AND D.DESIG_ID = A.JSM_DESIG_ID\n" +
                    "                              AND D.DESIG_PRIORITY IN\n" +
                    "                                     ( (SELECT LAH.LAH_BAND_NO\n" +
                    "                                          FROM LEAVE_APPROVAL_HIERARCHY LAH\n" +
                    "                                         WHERE INSTR (\n" +
                    "                                                     ','\n" +
                    "                                                  || LAH.LAH_SP_APPROVAL_CODE\n" +
                    "                                                  || ',',\n" +
                    "                                                  ',' || '"+user_id+"' || ',') > 0)))\n" +
                    "               AND NVL (LA.LA_APPROVED, 0) = 0)\n" +
                    " WHERE LA_APP_CODE NOT IN\n" +
                    "       (SELECT CASE\n" +
                    "          WHEN COM_PACK.GET_EMPLOYEE_ID_BY_USER ('"+user_id+"') =\n" +
                    "                  LAD_FORWARD_TO_ID\n" +
                    "          THEN\n" +
                    "             'No code'\n" +
                    "          ELSE\n" +
                    "             GET_LEAVE_APP_CODE (LAD_LA_ID)\n" +
                    "       END\n" +
                    "  FROM LEAVE_APPLICATION_DTL\n" +
                    " WHERE LAD_ID IN (  SELECT MAX (LAD_ID)\n" +
                    "                      FROM LEAVE_APPLICATION_DTL D, LEAVE_APPLICATION M\n" +
                    "                     WHERE M.LA_ID = D.LAD_LA_ID\n" +
                    "                  GROUP BY LAD_LA_ID))");



            while(rs.next())  {
                isLeaveApprovedCheck = rs.getInt(1);
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