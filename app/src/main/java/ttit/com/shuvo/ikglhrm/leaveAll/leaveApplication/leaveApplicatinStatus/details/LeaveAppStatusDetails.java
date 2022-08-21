package ttit.com.shuvo.ikglhrm.leaveAll.leaveApplication.leaveApplicatinStatus.details;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import ttit.com.shuvo.ikglhrm.R;
import ttit.com.shuvo.ikglhrm.WaitProgress;
import ttit.com.shuvo.ikglhrm.attendance.status.statusDetail.AttendanceStatusDetails;

import static ttit.com.shuvo.ikglhrm.Login.userInfoLists;
import static ttit.com.shuvo.ikglhrm.OracleConnection.createConnection;

public class LeaveAppStatusDetails extends AppCompatActivity {

    WaitProgress waitProgress = new WaitProgress();
    private String message = null;
    private Boolean conn = false;
    private Boolean connected = false;

    private Connection connection;

    String emp_id = "";

    TextView status;
    TextView approver;
    TextView leaCode;
    TextView apporCan;

    CardView statusCard;

    LinearLayout approverLay;

    TextInputEditText name;
    TextInputEditText id;
    TextInputEditText appDate;
    TextInputEditText apptype;
    TextInputEditText leaveType;
    TextInputEditText DateOn;
    TextInputEditText DateTO;
    TextInputEditText totalLeave;
    TextInputEditText leaveDuration;
    TextInputEditText reason;
    TextInputEditText backUp;
    TextInputEditText address;
    TextInputEditText comm;

    String leave_code = "";
    String statttt = "";
    String app_date = "";
    String from_date = "";
    String to_date= "";
    String total_leave = "";
    String apprrroovveerr = "";

    String app_type = "";
    String leave_type = "";
    String leave_dur = "";
    String reasonDesc = "";
    String leaveAddress = "";
    String backupEmployeee = "";
    String comments = "";

    Button close;

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
        setContentView(R.layout.activity_leave_app_status_details);

        status = findViewById(R.id.leave_app_status_of_from_list);
        statusCard = findViewById(R.id.leave_app_status_card_from_list);
        approver = findViewById(R.id.leave_app_approver_name_by_from_list);
        apporCan = findViewById(R.id.leave_app_is_approve_or_canc);
        approverLay = findViewById(R.id.leave_app_approver_layout_from_list);

        leaCode = findViewById(R.id.leave_code_from_list);

        name = findViewById(R.id.name_leave_application_status_details);
        id = findViewById(R.id.id_leave_application_status_details);
        appDate = findViewById(R.id.now_date_leave_application_status_details);

        apptype = findViewById(R.id.app_type_status_details);
        leaveType = findViewById(R.id.leave_type_status_details);
        DateOn = findViewById(R.id.date_on_from_status_details);
        DateTO = findViewById(R.id.date_to_status_details);
        totalLeave = findViewById(R.id.total_days_from_to_status_details);
        leaveDuration = findViewById(R.id.leave_duration_status_details);
        reason = findViewById(R.id.leave_app_reason_status_details);
        address = findViewById(R.id.leave_address_status_details);
        backUp = findViewById(R.id.backUp_employee_status_details);
        comm = findViewById(R.id.comments_for_leave);

        close = findViewById(R.id.leave_app_status_details_finish);

        if (userInfoLists.size() != 0) {
            String firstname = userInfoLists.get(0).getUser_fname();
            String lastName = userInfoLists.get(0).getUser_lname();
            if (firstname == null) {
                firstname = "";
            }
            if (lastName == null) {
                lastName = "";
            }
            String empFullName = firstname+" "+lastName;
            name.setText(empFullName);
        }

        id.setText(userInfoLists.get(0).getUserName());

        emp_id = userInfoLists.get(0).getEmp_id();

        Intent intent = getIntent();
        leave_code = intent.getStringExtra("LEAVE");
        statttt = intent.getStringExtra("STATUS");
        leave_type = intent.getStringExtra("LEAVE_TYPE");
        from_date = intent.getStringExtra("FROM_DATE");
        to_date = intent.getStringExtra("TO_DATE");
        total_leave = intent.getStringExtra("TOTAL");
        apprrroovveerr = intent.getStringExtra("APPROVER");

        leaCode.setText(leave_code);

        if (statttt.equals("PENDING")) {
            status.setText(statttt);
            statusCard.setCardBackgroundColor(Color.parseColor("#636e72"));
            approverLay.setVisibility(View.GONE);
            approver.setText("");
        } else if (statttt.equals("APPROVED")) {
            status.setText(statttt);
            statusCard.setCardBackgroundColor(Color.parseColor("#1abc9c"));
            approverLay.setVisibility(View.VISIBLE);
            approver.setText(apprrroovveerr);
            apporCan.setText("Approved By:");
        } else if (statttt.equals("REJECTED")) {
            status.setText(statttt);
            statusCard.setCardBackgroundColor(Color.parseColor("#d63031"));
            approverLay.setVisibility(View.VISIBLE);
            approver.setText(apprrroovveerr);
            apporCan.setText("Rejected By:");
        } else if (statttt.equals("CANCEL APPROVED LEAVE")) {
            status.setText(statttt);
            statusCard.setCardBackgroundColor(Color.parseColor("#ff7675"));
            if (apprrroovveerr.isEmpty()) {
                approverLay.setVisibility(View.GONE);
                approver.setText("");
            } else {
                approverLay.setVisibility(View.VISIBLE);
                approver.setText(apprrroovveerr);
                apporCan.setText("Cancelled By:");
            }
        }

        leaveType.setText(leave_type);
        DateOn.setText(from_date);
        DateTO.setText(to_date);
        totalLeave.setText(total_leave);

        new Check().execute();



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

                StatusDetails();
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

                apptype.setText(app_type);
                appDate.setText(app_date);
                leaveDuration.setText(leave_dur);
                reason.setText(reasonDesc);
                address.setText(leaveAddress);
                backUp.setText(backupEmployeee);

                if (comments == null) {
                    comm.setText("");
                } else {
                    comm.setText(comments);
                }

            }else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(LeaveAppStatusDetails.this)
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

    public void StatusDetails() {
        try {
            this.connection = createConnection();
            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();


            Statement stmt = connection.createStatement();




            ResultSet rs=stmt.executeQuery("Select LA_APPLICATION_TYPE, TO_CHAR(LEAVE_APPLICATION.LA_DATE,'DD-MON-YY') LA_DATE,\n" +
                    "CASE WHEN LA_LEAVE_TYPE = 0 then 'Full Day' when LA_LEAVE_TYPE = 1 then 'First Half' else 'Second Half' end leave_duration, \n" +
                    "LA_REASON,LA_ADD_DURING_LEAVE,\n" +
                    "(Select EMP_NAME FROM EMP_MST WHERE EMP_ID = LA_WORK_BCK_EMP_ID ) as BACKUP,LA_COMMENTS\n" +
                    "From  LEAVE_APPLICATION\n" +
                    "WHERE LA_APP_CODE = '"+leave_code+"'");



            while(rs.next()) {

                app_type = rs.getString(1);
                app_date = rs.getString(2);
                leave_dur = rs.getString(3);
                reasonDesc = rs.getString(4);
                leaveAddress = rs.getString(5);
                backupEmployeee = rs.getString(6);
                comments = rs.getString(7);

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