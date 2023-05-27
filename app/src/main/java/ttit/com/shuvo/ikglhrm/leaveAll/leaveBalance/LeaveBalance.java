package ttit.com.shuvo.ikglhrm.leaveAll.leaveBalance;

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

import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ttit.com.shuvo.ikglhrm.R;
import ttit.com.shuvo.ikglhrm.WaitProgress;
import ttit.com.shuvo.ikglhrm.attendance.report.AttenReportAdapter;
import ttit.com.shuvo.ikglhrm.attendance.report.AttendanceReport;
import ttit.com.shuvo.ikglhrm.attendance.status.StatusList;

import static ttit.com.shuvo.ikglhrm.Login.userDesignations;
import static ttit.com.shuvo.ikglhrm.Login.userInfoLists;
import static ttit.com.shuvo.ikglhrm.OracleConnection.createConnection;

public class LeaveBalance extends AppCompatActivity {

    TextInputEditText name;
    TextInputEditText join;
    TextInputEditText title;

    TextView year;
    String formattedDate = "";

    RecyclerView leaveView;
    RecyclerView.LayoutManager layoutManager;
    LeaveBalanceAdapter leaveBalanceAdapter;

    Button close;

    public static ArrayList<LeaveBalanceList> leaveBalanceLists;

    WaitProgress waitProgress = new WaitProgress();
    private String message = null;
    private Boolean conn = false;
    private Boolean connected = false;

    private Connection connection;

    String emp_id = "";
    String yearrr = "";


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

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(LeaveBalance.this,R.color.secondaryColor));
        setContentView(R.layout.activity_leave_balance);

        name = findViewById(R.id.name_leave);
        join = findViewById(R.id.joining_leave);
        title = findViewById(R.id.calling_title_leave);

        year = findViewById(R.id.from_to_year);

        leaveView = findViewById(R.id.leave_list_view);

        close = findViewById(R.id.leave_balance_finish);

        leaveBalanceLists = new ArrayList<>();

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
            emp_id = userInfoLists.get(0).getEmp_id();
        }

        if (userDesignations.size() != 0) {
            String jsmName = userDesignations.get(0).getJsm_name();
            if (jsmName == null) {
                jsmName = "";
            }

            String joinDate = userDesignations.get(0).getJoining_date();
            if (joinDate == null) {
                joinDate = "";
            }
            title.setText(jsmName);
            join.setText(joinDate);
        }

        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy", Locale.getDefault());

        formattedDate = df.format(c);

        leaveView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        leaveView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(leaveView.getContext(),DividerItemDecoration.VERTICAL);
        leaveView.addItemDecoration(dividerItemDecoration);

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

                LeaveDetails();
                if (connected) {
                    conn = true;
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


                leaveBalanceAdapter = new LeaveBalanceAdapter(leaveBalanceLists, LeaveBalance.this);
                leaveView.setAdapter(leaveBalanceAdapter);

                year.setText("YEAR: "+yearrr);

            }else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(LeaveBalance.this)
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

    public void LeaveDetails() {
        try {
            this.connection = createConnection();
            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();


            leaveBalanceLists = new ArrayList<>();
            Statement stmt = connection.createStatement();




            ResultSet rs=stmt.executeQuery("SELECT EM.LBEM_EMP_ID,\n" +
                    "       TO_CHAR(YD.LBYD_YEAR,'YYYY'),\n" +
                    "       \n" +
                    "       \n" +
                    "       lc.lc_name,\n" +
                    "       lc.lc_short_code,\n" +
                    "       LD.LBD_BALANCE_QTY,\n" +
                    "       \n" +
                    "       LD.LBD_OPENING_QTY,\n" +
                    "       LD.LBD_CURRENT_QTY,\n" +
                    "       LD.LBD_TAKEN_QTY,\n" +
                    "       LD.LBD_CASH_TAKEN_QTY,\n" +
                    "       LD.LBD_TRANSFER_QTY\n" +
                    "  FROM LEAVE_BALANCE_EMP_MST  EM,\n" +
                    "       LEAVE_BALANCE_YEAR_DTL YD,\n" +
                    "       LEAVE_BALANCE_DTL      LD,\n" +
                    "       leave_category lc\n" +
                    " WHERE     EM.LBEM_ID = YD.LBYD_LBEM_ID\n" +
                    "       AND YD.LBYD_ID = LD.LBD_LBYD_ID\n" +
                    "       and ld.lbd_lc_id = lc.lc_id\n" +
                    "       AND EM.LBEM_EMP_ID = "+emp_id+"\n" +
                    "       AND TO_CHAR (YD.LBYD_YEAR, 'YYYY') = TO_CHAR (TO_DATE('"+formattedDate+"'), 'YYYY')");



            while(rs.next()) {

                leaveBalanceLists.add(new LeaveBalanceList(rs.getString(3),rs.getString(4),rs.getString(6),
                        rs.getString(7),rs.getString(8),rs.getString(10),rs.getString(9),rs.getString(5)));

                yearrr = rs.getString(2);
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