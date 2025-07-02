package ttit.com.shuvo.ikglhrm.leaveAll.leaveBalance;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import ttit.com.shuvo.ikglhrm.R;
import ttit.com.shuvo.ikglhrm.WaitProgress;

import static ttit.com.shuvo.ikglhrm.Login.userDesignations;
import static ttit.com.shuvo.ikglhrm.Login.userInfoLists;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.api_url_front;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LeaveBalance extends AppCompatActivity {

    TextInputEditText name;
    TextInputEditText join;
    TextInputEditText title;

    TextView year;
    String formattedDate = "";

    RecyclerView leaveView;
    RecyclerView.LayoutManager layoutManager;
    LeaveBalanceAdapter leaveBalanceAdapter;

    TextView noLeaveBal;

    ArrayList<LeaveBalanceList> leaveBalanceLists;

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;

    String emp_id = "";
    String yearrr = "";

    Logger logger = Logger.getLogger(LeaveBalance.class.getName());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_balance);

        name = findViewById(R.id.name_leave);
        join = findViewById(R.id.joining_leave);
        title = findViewById(R.id.calling_title_leave);
        noLeaveBal = findViewById(R.id.no_leave_balance_msg);
        noLeaveBal.setVisibility(View.GONE);

        year = findViewById(R.id.from_to_year);

        leaveView = findViewById(R.id.leave_list_view);


        leaveBalanceLists = new ArrayList<>();

        if (!userInfoLists.isEmpty()) {
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

        if (!userDesignations.isEmpty()) {
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

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);
        SimpleDateFormat yf = new SimpleDateFormat("yyyy", Locale.ENGLISH);

        String yt = "YEAR: "+yf.format(c);
        year.setText(yt);

        formattedDate = df.format(c);

        leaveView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        leaveView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(leaveView.getContext(),DividerItemDecoration.VERTICAL);
        leaveView.addItemDecoration(dividerItemDecoration);

        getLeaveBalance();
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
//                LeaveDetails();
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
//
//                leaveBalanceAdapter = new LeaveBalanceAdapter(leaveBalanceLists, LeaveBalance.this);
//                leaveView.setAdapter(leaveBalanceAdapter);
//
//                year.setText("YEAR: "+yearrr);
//
//            }
//            else {
//                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
//                AlertDialog dialog = new AlertDialog.Builder(LeaveBalance.this)
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
//
//                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
//                negative.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        dialog.dismiss();
//                        finish();
//
//                    }
//                });
//            }
//        }
//    }

//    public void LeaveDetails() {
//        try {
//            this.connection = createConnection();
//            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();
//
//
//            leaveBalanceLists = new ArrayList<>();
//            Statement stmt = connection.createStatement();
//
//
//
//
//            ResultSet rs=stmt.executeQuery("SELECT EM.LBEM_EMP_ID,\n" +
//                    "       TO_CHAR(YD.LBYD_YEAR,'YYYY'),\n" +
//                    "       \n" +
//                    "       \n" +
//                    "       lc.lc_name,\n" +
//                    "       lc.lc_short_code,\n" +
//                    "       LD.LBD_BALANCE_QTY,\n" +
//                    "       \n" +
//                    "       LD.LBD_OPENING_QTY,\n" +
//                    "       LD.LBD_CURRENT_QTY,\n" +
//                    "       LD.LBD_TAKEN_QTY,\n" +
//                    "       LD.LBD_CASH_TAKEN_QTY,\n" +
//                    "       LD.LBD_TRANSFER_QTY\n" +
//                    "  FROM LEAVE_BALANCE_EMP_MST  EM,\n" +
//                    "       LEAVE_BALANCE_YEAR_DTL YD,\n" +
//                    "       LEAVE_BALANCE_DTL      LD,\n" +
//                    "       leave_category lc\n" +
//                    " WHERE     EM.LBEM_ID = YD.LBYD_LBEM_ID\n" +
//                    "       AND YD.LBYD_ID = LD.LBD_LBYD_ID\n" +
//                    "       and ld.lbd_lc_id = lc.lc_id\n" +
//                    "       AND EM.LBEM_EMP_ID = "+emp_id+"\n" +
//                    "       AND TO_CHAR (YD.LBYD_YEAR, 'YYYY') = TO_CHAR (TO_DATE('"+formattedDate+"'), 'YYYY')");
//
//
//
//            while(rs.next()) {
//
//                leaveBalanceLists.add(new LeaveBalanceList(rs.getString(3),rs.getString(4),rs.getString(6),
//                        rs.getString(7),rs.getString(8),rs.getString(10),rs.getString(9),rs.getString(5)));
//
//                yearrr = rs.getString(2);
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

    public void getLeaveBalance() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;

        leaveBalanceLists = new ArrayList<>();
        yearrr = "";

        String url = api_url_front + "leave/getLeaveBalance/"+emp_id+"/"+formattedDate;

        RequestQueue requestQueue = Volley.newRequestQueue(LeaveBalance.this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject leaveBalanceInfo = array.getJSONObject(i);

                        String now_year = leaveBalanceInfo.getString("now_year");
                        String lc_name = leaveBalanceInfo.getString("lc_name");
                        String lc_short_code = leaveBalanceInfo.getString("lc_short_code");
                        String lbd_balance_qty = leaveBalanceInfo.getString("lbd_balance_qty").equals("null") ? "0" : leaveBalanceInfo.getString("lbd_balance_qty");
                        String lbd_opening_qty = leaveBalanceInfo.getString("lbd_opening_qty").equals("null") ? "0" : leaveBalanceInfo.getString("lbd_opening_qty");
                        String lbd_current_qty = leaveBalanceInfo.getString("lbd_current_qty").equals("null") ? "0" : leaveBalanceInfo.getString("lbd_current_qty");
                        String lbd_taken_qty = leaveBalanceInfo.getString("lbd_taken_qty").equals("null") ? "0" : leaveBalanceInfo.getString("lbd_taken_qty");
                        String lbd_cash_taken_qty = leaveBalanceInfo.getString("lbd_cash_taken_qty").equals("null") ? "0" : leaveBalanceInfo.getString("lbd_cash_taken_qty");
                        String lbd_transfer_qty = leaveBalanceInfo.getString("lbd_transfer_qty").equals("null") ? "0" : leaveBalanceInfo.getString("lbd_transfer_qty");

                        leaveBalanceLists.add(new LeaveBalanceList(lc_name,lc_short_code,lbd_opening_qty,
                                lbd_current_qty,lbd_taken_qty,lbd_transfer_qty,lbd_cash_taken_qty,lbd_balance_qty));

                        yearrr = now_year;
                    }
                }
                connected = true;
                updateLayout();
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
                if (leaveBalanceLists.isEmpty()) {
                    noLeaveBal.setVisibility(View.VISIBLE);
                }
                else {
                    noLeaveBal.setVisibility(View.GONE);
                }
                leaveBalanceAdapter = new LeaveBalanceAdapter(leaveBalanceLists, LeaveBalance.this);
                leaveView.setAdapter(leaveBalanceAdapter);

                conn = false;
                connected = false;
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(LeaveBalance.this)
                        .setMessage("There is a network issue in the server. Please Try later.")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    getLeaveBalance();
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
            AlertDialog dialog = new AlertDialog.Builder(LeaveBalance.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel",null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                getLeaveBalance();
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