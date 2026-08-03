package ttit.com.shuvo.ikglhrm.leaveAll.leaveApplication;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import ttit.com.shuvo.ikglhrm.R;
import ttit.com.shuvo.ikglhrm.WaitProgress;

import static ttit.com.shuvo.ikglhrm.user_login.Login.userInfoLists;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.api_url_front;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.jakewharton.processphoenix.ProcessPhoenix;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ShowLeaveBalance extends AppCompatDialogFragment {

    RecyclerView apptRecyclerView;
    LeaveBalanceFroAPPAdapter leaveBalanceFroAPPAdapter;
    RecyclerView.LayoutManager apptLayout;

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;

    AppCompatActivity activity;

    ArrayList<LeaveBalanceForAPPList> leaveBalanceForAPPLists;

    String emp_id = "";
    String formattedDate = "";

    AlertDialog lBdialog;

    Context mContext;

    public ShowLeaveBalance(Context mContext) {
        this.mContext = mContext;
    }

    Logger logger = Logger.getLogger(ShowLeaveBalance.class.getName());
    String parsing_message = "";

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.show_leave_balance, null);

        activity = (AppCompatActivity) view.getContext();

        leaveBalanceForAPPLists = new ArrayList<>();

        if (userInfoLists == null) {
            restart("Could Not Get Employee Data. Please Restart the App.");
        }
        else {
            if (userInfoLists.isEmpty()) {
                restart("Could Not Get Employee Data. Please Restart the App.");
            }
            else {
                emp_id = userInfoLists.get(0).getEmp_id();
            }
        }

        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);

        formattedDate = df.format(c);

        apptRecyclerView = view.findViewById(R.id.all_leave_balance_list);
        apptLayout = new LinearLayoutManager(getContext());
        apptRecyclerView.setLayoutManager(apptLayout);


        getLeaveBalance();



        builder.setView(view);

        lBdialog = builder.create();

        lBdialog.setCancelable(false);
        lBdialog.setCanceledOnTouchOutside(false);
        setCancelable(false);

        lBdialog.setButton(Dialog.BUTTON_NEGATIVE, "OK", (dialog, which) -> dialog.dismiss());

        return lBdialog;
    }

//    public boolean isConnected() {
//        boolean connected = false;
//        boolean isMobile = false;
//        try {
//            ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
//            NetworkInfo nInfo = cm.getActiveNetworkInfo();
//            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
//            isMobile = nInfo.getType() == ConnectivityManager.TYPE_MOBILE;
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
//            //Process ipProcess = runtime.exec("/system/bin/ping -c 1 192.168.1.5");
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
//            waitProgress.show(activity.getSupportFragmentManager(),"WaitBar");
//            waitProgress.setCancelable(false);
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            if (isConnected() && isOnline()) {
//                LeaveBalanceShow();
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
//                leaveBalanceFroAPPAdapter = new LeaveBalanceFroAPPAdapter(leaveBalanceForAPPLists,getContext());
//                apptRecyclerView.setAdapter(leaveBalanceFroAPPAdapter);
//
//
//
//            }
//            else {
//                Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
//                AlertDialog dialog = new AlertDialog.Builder(getContext())
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
//                        lBdialog.dismiss();
//                    }
//                });
//            }
//        }
//    }

//    public void LeaveBalanceShow() {
//
//        try {
//            this.connection = createConnection();
//
//            Statement stmt = connection.createStatement();
//            leaveBalanceForAPPLists = new ArrayList<>();
//
//            ResultSet rs=stmt.executeQuery("SELECT EM.LBEM_EMP_ID,lc.lc_name, LD.LBD_BALANCE_QTY\n" +
//                    "FROM LEAVE_BALANCE_EMP_MST  EM,\n" +
//                    "LEAVE_BALANCE_YEAR_DTL YD,\n" +
//                    "LEAVE_BALANCE_DTL      LD,\n" +
//                    "leave_category lc\n" +
//                    "WHERE \n" +
//                    "EM.LBEM_ID = YD.LBYD_LBEM_ID\n" +
//                    "AND YD.LBYD_ID = LD.LBD_LBYD_ID\n" +
//                    "and ld.lbd_lc_id = lc.lc_id\n" +
//                    "AND EM.LBEM_EMP_ID = "+emp_id+"\n" +
//                    "AND TO_CHAR (YD.LBYD_YEAR, 'YYYY') = TO_CHAR (TO_DATE('"+formattedDate+"'), 'YYYY')");
//
//
//
//            while(rs.next()) {
//                leaveBalanceForAPPLists.add(new LeaveBalanceForAPPList(rs.getString(2),rs.getString(3)));
//            }
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
//
//    }

    public void getLeaveBalance() {
        waitProgress.show(activity.getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;

        leaveBalanceForAPPLists = new ArrayList<>();

        String url = api_url_front + "leave/show_leave_balance/"+emp_id+"/"+formattedDate;

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

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

                        String lc_name = leaveBalanceInfo.getString("lc_name")
                                .equals("null") ? "" : leaveBalanceInfo.getString("lc_name");
                        String lbd_balance_qty = leaveBalanceInfo.getString("lbd_balance_qty")
                                .equals("null") ? "" : leaveBalanceInfo.getString("lbd_balance_qty");

                        leaveBalanceForAPPLists.add(new LeaveBalanceForAPPList(lc_name,lbd_balance_qty));

                    }
                }
                connected = true;
                updateDial();
            }
            catch (JSONException e) {
                logger.log(Level.WARNING, e.getMessage(), e);
                parsing_message = e.getLocalizedMessage();
                connected = false;
                updateDial();
            }
        }, error -> {
            logger.log(Level.WARNING, error.getMessage(), error);
            parsing_message = error.getLocalizedMessage();
            conn = false;
            connected = false;
            updateDial();
        });

        requestQueue.add(stringRequest);
    }

    private void updateDial() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                leaveBalanceFroAPPAdapter = new LeaveBalanceFroAPPAdapter(leaveBalanceForAPPLists,getContext());
                apptRecyclerView.setAdapter(leaveBalanceFroAPPAdapter);
            }
            else {
                alertMessage();
            }
        }
        else {
            alertMessage();
        }
    }

    public void alertMessage() {
        if (parsing_message != null) {
            if (parsing_message.isEmpty() || parsing_message.equals("null")) {
                parsing_message = "Server problem or Internet not connected";
            }
        }
        else {
            parsing_message = "Server problem or Internet not connected";
        }
        MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(mContext);
        alertDialogBuilder.setTitle("System Warning!")
                .setIcon(R.drawable.hrm_new_round_icon_custom)
                .setMessage("Message: "+parsing_message+".\n"+"Please try again.")
                .setPositiveButton("Retry", (dialog, which) -> {
                    getLeaveBalance();
                    dialog.dismiss();
                })
                .setNegativeButton("Cancel",(dialog, which) -> {
                    dialog.dismiss();
                    lBdialog.dismiss();
                });

        AlertDialog alert = alertDialogBuilder.create();
        alert.setCancelable(false);
        alert.setCanceledOnTouchOutside(false);
        try {
            alert.show();
        }
        catch (Exception e) {
            restart("App is paused for a long time. Please Start the app again.");
        }
    }

    public void restart(String msg) {
        try {
            ProcessPhoenix.triggerRebirth(mContext);
        }
        catch (Exception e) {
            Toast.makeText(mContext,msg,Toast.LENGTH_SHORT).show();
            System.exit(0);
        }
    }
}
