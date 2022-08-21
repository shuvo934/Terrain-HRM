package ttit.com.shuvo.ikglhrm.attendance.status.statusDetail;

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
import java.util.ArrayList;

import ttit.com.shuvo.ikglhrm.R;
import ttit.com.shuvo.ikglhrm.WaitProgress;
import ttit.com.shuvo.ikglhrm.attendance.status.AttendanceStatus;
import ttit.com.shuvo.ikglhrm.attendance.status.StatusAdapter;
import ttit.com.shuvo.ikglhrm.attendance.status.StatusList;

import static ttit.com.shuvo.ikglhrm.Login.userInfoLists;
import static ttit.com.shuvo.ikglhrm.OracleConnection.createConnection;

public class AttendanceStatusDetails extends AppCompatActivity {

    WaitProgress waitProgress = new WaitProgress();
    private String message = null;
    private Boolean conn = false;
    private Boolean infoConnected = false;
    private Boolean connected = false;

    private Connection connection;

    String emp_id = "";

    TextView status;
    TextView approver;
    TextView reqCode;

    TextInputEditText reqType;
    TextInputEditText attType;
    TextInputEditText locUpdate;
    TextInputEditText shift;
    TextInputEditText reason;
    TextInputEditText forwarder;
    TextInputEditText comm;

    CardView statusCard;

    LinearLayout approverLay;
    LinearLayout intimeLay;
    LinearLayout outTimeLay;

    TextInputEditText name;
    TextInputEditText id;
    TextInputEditText appDate;
    TextInputEditText upDate;
    TextInputEditText inTime;
    TextInputEditText outTime;
    TextInputEditText machCode;
    TextInputEditText machType;
    TextInputEditText reasonDesc;
    TextInputEditText address;

    String request = "";
    String statttt = "";
    String request_date = "";
    String update_date = "";
    String arrival= "";
    String departure = "";
    String apprrroovveerr = "";

    String request_type = "";
    String application_type = "";
    String locotion_updated = "";
    String machineCode = "";
    String machineType = "";
    String shiftName = "";
    String reasonName = "";
    String reasonDescription = "";
    String addressOut = "";
    String forwardTo = "";
    String locationId = "";
    String dateNow = "";
    String comments = "";

    Button close;

    TextView appRej;

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

        setContentView(R.layout.activity_attendance_status_details);

        status = findViewById(R.id.status_of_att_from_list);
        approver = findViewById(R.id.approver_name_by_from_list);
        reqCode = findViewById(R.id.request_code_from_list);
        reqType = findViewById(R.id.req_type_st_dt);
        attType = findViewById(R.id.att_type_st_dt);
        locUpdate = findViewById(R.id.loc_updated_st_dt);
        shift = findViewById(R.id.shift_to_be_st_dt);
        reason = findViewById(R.id.reason_type_st_dt);
        forwarder = findViewById(R.id.approver_description_st_dt);
        comm = findViewById(R.id.comments_from_approver);

        name = findViewById(R.id.name_att_st_dt);
        id = findViewById(R.id.id_att_st_dt);
        appDate = findViewById(R.id.now_date_att_st_dt);
        upDate = findViewById(R.id.date_to_be_updated_st_dt);
        inTime = findViewById(R.id.arrival_time_to_be_updated_st_dt);
        outTime = findViewById(R.id.departure_time_to_be_updated_st_dt);
        machCode = findViewById(R.id.updated_machine_code_st_dt);
        machType = findViewById(R.id.updated_machine_type_st_dt);
        reasonDesc = findViewById(R.id.reason_description_update_st_dt);
        address = findViewById(R.id.address_outside_sta_st_dt);

        close = findViewById(R.id.status_details_finish);

        appRej = findViewById(R.id.is_app_or_rej);

        statusCard = findViewById(R.id.status_card_from_list);

        approverLay = findViewById(R.id.approver_layout_from_list);
        intimeLay = findViewById(R.id.in_time_lay_st_dt);
        outTimeLay = findViewById(R.id.out_time_lay_st_dt);

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

        id.setText(userInfoLists.get(0).getUserName());

        emp_id = userInfoLists.get(0).getEmp_id();

        Intent intent = getIntent();
        request = intent.getStringExtra("Request");
        statttt = intent.getStringExtra("Status");
        request_date = intent.getStringExtra("APP_DATE");
        update_date = intent.getStringExtra("UPDATE_DATE");
        arrival = intent.getStringExtra("ARRIVAL");
        departure = intent.getStringExtra("DEPARTURE");
        apprrroovveerr = intent.getStringExtra("APPROVER");

        reqCode.setText(request);

        if (statttt.equals("PENDING")) {
            status.setText(statttt);
            statusCard.setCardBackgroundColor(Color.parseColor("#636e72"));
            approverLay.setVisibility(View.GONE);
            approver.setText("");
        } else if (statttt.equals("APPROVED")){
            status.setText(statttt);
            approverLay.setVisibility(View.VISIBLE);
            statusCard.setCardBackgroundColor(Color.parseColor("#1abc9c"));
            approver.setText(apprrroovveerr);
            appRej.setText("Approved By:");

        } else if (statttt.equals("REJECTED")) {
            status.setText(statttt);
            statusCard.setCardBackgroundColor(Color.parseColor("#c0392b"));
            approverLay.setVisibility(View.VISIBLE);
            approver.setText(apprrroovveerr);
            appRej.setText("Rejected By:");
        }


        appDate.setText(request_date);
        upDate.setText(update_date);

        if (arrival.isEmpty()) {
            intimeLay.setVisibility(View.GONE);
        } else {
            inTime.setText(arrival);
            intimeLay.setVisibility(View.VISIBLE);
        }

        if (departure.isEmpty()) {
            outTimeLay.setVisibility(View.GONE);
        } else {
            outTime.setText(departure);
            outTimeLay.setVisibility(View.VISIBLE);
        }


//        if (apprrroovveerr.isEmpty()) {
//            approverLay.setVisibility(View.GONE);
//        } else {
//            approverLay.setVisibility(View.VISIBLE);
//            approver.setText(apprrroovveerr);
//        }

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

                reqType.setText(request_type);
                attType.setText(application_type);
                locUpdate.setText(locotion_updated);
                machCode.setText(machineCode);
                machType.setText(machineType);
                shift.setText(shiftName);
                reason.setText(reasonName);
                reasonDesc.setText(reasonDescription);
                if (addressOut == null) {
                    address.setText("No Address Given");
                } else {
                    address.setText(addressOut);
                }

                if (comments == null) {
                    comm.setText("");
                } else {
                    comm.setText(comments);
                }

                forwarder.setText(forwardTo);
                appDate.setText(dateNow);



            }else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(AttendanceStatusDetails.this)
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




            ResultSet rs=stmt.executeQuery("Select DARM_APPLICATION_TYPE,\n" +
                    "cASE WHEN DARM_REQ_TYPE = 'Late' then 'Late Arrival Information' when DARM_REQ_TYPE = 'Early' then 'Early Departure Information' else 'Work Time Update' end req_type,\n" +
                    "(Select ATTD_UP_REQ_REASON_MST.AURRM_NAME FROM ATTD_UP_REQ_REASON_MST where ATTD_UP_REQ_REASON_MST.AURRM_ID = DARM_AURRM_ID) as REASON,\n" +
                    "                    TO_CHAR(DAILY_ATTEN_REQ_MST.DARM_DATE,'DD-MON-YY') DARM_DATE,\n" +
                    "                    TO_CHAR(DAILY_ATTEN_REQ_MST.DARM_UPDATE_DATE,'DD-MON-YYYY') DARM_UPDATE_DATE,\n" +
                    "                    TO_CHAR(DAILY_ATTEN_REQ_MST.DARM_REQ_ARRIVAL_TIME,'HH:MI AM') ARRIVAL_TIME,\n" +
                    "                    TO_CHAR(DAILY_ATTEN_REQ_MST.DARM_REQ_DEPART_TIME,'HH:MI AM') DEPARTURE_TIME,\n" +
                    "                   (Select COMPANY_OFFICE_ADDRESS.COA_NAME FROM COMPANY_OFFICE_ADDRESS Where COMPANY_OFFICE_ADDRESS.COA_ID = DARM_REQ_LOCATION_ID)\n" +
                    "                   as LOCATION,\n" +
                    "                   (Select OFFICE_SHIFT_MST.OSM_NAME FROM OFFICE_SHIFT_MST Where OFFICE_SHIFT_MST.OSM_ID =  DARM_REQ_OSM_ID) as SHIFT,\n" +
                    "                    DARM_REASON,DARM_ADD_DURING_CAUSE,(Select EMP_NAME FROM EMP_MST WHERE EMP_ID = DARM_APPLIED_TO_ID ) as APPROVER, DARM_REQ_LOCATION_ID,DARM_COMMENTS\n" +
                    "                    From DAILY_ATTEN_REQ_MST \n" +
                    "                    WHERE DARM_APP_CODE = '"+request+"'");



            while(rs.next()) {

                request_type = rs.getString(1);
                application_type = rs.getString(2);
                reasonName = rs.getString(3);
                dateNow = rs.getString(4);
                locotion_updated = rs.getString(8);
                shiftName = rs.getString(9);
                reasonDescription = rs.getString(10);
                addressOut = rs.getString(11);
                forwardTo = rs.getString(12);
                locationId = rs.getString(13);
                comments = rs.getString(14);

            }

            if (locationId != null) {
                ResultSet resultSet = stmt.executeQuery("SELECT AMS_MECHINE_CODE, AMS_ATTENDANCE_TYPE\n" +
                        "                    FROM ATTENDANCE_MECHINE_SETUP\n" +
                        "                    WHERE AMS_ID = (SELECT MAX(AMS_ID)\n" +
                        "                   FROM ATTENDANCE_MECHINE_SETUP\n" +
                        "                    WHERE AMS_COA_ID = "+locationId+")");

                while (resultSet.next()) {
                    machineCode = resultSet.getString(1);
                    machineType = resultSet.getString(2);
                }
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