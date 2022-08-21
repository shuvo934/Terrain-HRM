package ttit.com.shuvo.ikglhrm.leaveAll.leaveApplication;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import static ttit.com.shuvo.ikglhrm.Login.userInfoLists;
import static ttit.com.shuvo.ikglhrm.OracleConnection.createConnection;

public class ShowLeaveBalance extends AppCompatDialogFragment {

    private RecyclerView apptRecyclerView;
    public static LeaveBalanceFroAPPAdapter leaveBalanceFroAPPAdapter;
    private RecyclerView.LayoutManager apptLayout;

    WaitProgress waitProgress = new WaitProgress();
    private String message = null;
    private Boolean conn = false;
    private Boolean connected = false;

    private Connection connection;
    AppCompatActivity activity;

    public static ArrayList<LeaveBalanceForAPPList> leaveBalanceForAPPLists;

    String emp_id = "";
    String formattedDate = "";

    AlertDialog lBdialog;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.show_leave_balance, null);

        activity = (AppCompatActivity) view.getContext();

        leaveBalanceForAPPLists = new ArrayList<>();

        emp_id = userInfoLists.get(0).getEmp_id();

        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy", Locale.getDefault());

        formattedDate = df.format(c);

        apptRecyclerView = view.findViewById(R.id.all_leave_balance_list);
        apptRecyclerView.setHasFixedSize(true);
        apptLayout = new LinearLayoutManager(getContext());
        apptRecyclerView.setLayoutManager(apptLayout);


        new Check().execute();



        builder.setView(view);

        lBdialog = builder.create();

        lBdialog.setCancelable(false);
        lBdialog.setCanceledOnTouchOutside(false);

        lBdialog.setButton(Dialog.BUTTON_NEGATIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        return lBdialog;
    }

    public boolean isConnected() {
        boolean connected = false;
        boolean isMobile = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            isMobile = nInfo.getType() == ConnectivityManager.TYPE_MOBILE;
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }

    public boolean isOnline() {

        Runtime runtime = Runtime.getRuntime();
        try {
            //Process ipProcess = runtime.exec("/system/bin/ping -c 1 192.168.1.5");
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

            waitProgress.show(activity.getSupportFragmentManager(),"WaitBar");
            waitProgress.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isConnected() && isOnline()) {
                LeaveBalanceShow();
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

                leaveBalanceFroAPPAdapter = new LeaveBalanceFroAPPAdapter(leaveBalanceForAPPLists,getContext());
                apptRecyclerView.setAdapter(leaveBalanceFroAPPAdapter);



            }else {
                Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(getContext())
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
                        lBdialog.dismiss();
                    }
                });
            }
        }
    }

    public void LeaveBalanceShow() {

        try {
            this.connection = createConnection();

            Statement stmt = connection.createStatement();
            leaveBalanceForAPPLists = new ArrayList<>();

            ResultSet rs=stmt.executeQuery("SELECT EM.LBEM_EMP_ID,lc.lc_name, LD.LBD_BALANCE_QTY\n" +
                    "FROM LEAVE_BALANCE_EMP_MST  EM,\n" +
                    "LEAVE_BALANCE_YEAR_DTL YD,\n" +
                    "LEAVE_BALANCE_DTL      LD,\n" +
                    "leave_category lc\n" +
                    "WHERE \n" +
                    "EM.LBEM_ID = YD.LBYD_LBEM_ID\n" +
                    "AND YD.LBYD_ID = LD.LBD_LBYD_ID\n" +
                    "and ld.lbd_lc_id = lc.lc_id\n" +
                    "AND EM.LBEM_EMP_ID = "+emp_id+"\n" +
                    "AND TO_CHAR (YD.LBYD_YEAR, 'YYYY') = TO_CHAR (TO_DATE('"+formattedDate+"'), 'YYYY')");



            while(rs.next()) {
                leaveBalanceForAPPLists.add(new LeaveBalanceForAPPList(rs.getString(2),rs.getString(3)));
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
