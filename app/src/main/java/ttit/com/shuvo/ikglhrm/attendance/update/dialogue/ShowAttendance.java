package ttit.com.shuvo.ikglhrm.attendance.update.dialogue;

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

import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import ttit.com.shuvo.ikglhrm.R;
import ttit.com.shuvo.ikglhrm.WaitProgress;
import ttit.com.shuvo.ikglhrm.attendance.reqUpdate.AttendanceReqUpdate;

import static ttit.com.shuvo.ikglhrm.Login.userInfoLists;
import static ttit.com.shuvo.ikglhrm.OracleConnection.createConnection;
import static ttit.com.shuvo.ikglhrm.attendance.reqUpdate.AttendanceReqUpdate.dateToShowUpdate;
import static ttit.com.shuvo.ikglhrm.attendance.update.AttendanceUpdate.dateToShow;
import static ttit.com.shuvo.ikglhrm.attendance.update.AttendanceUpdate.showAttendanceNumber;

public class ShowAttendance extends AppCompatDialogFragment {

    TextInputEditText intime;
    TextInputEditText latetime;
    TextInputEditText outtime;
    TextInputEditText machArrTime;
    TextInputEditText machDepTime;
    TextInputEditText exShNa;
    TextInputEditText exLocNa;

    WaitProgress waitProgress = new WaitProgress();
    private String message = null;
    private Boolean conn = false;
    private Boolean connected = false;

    private Connection connection;
    AppCompatActivity activity;

    String emp_id = "";
    String date = "";

    String shiftin = "";
    String shiftout = "";
    String lateArr = "";
    String machineArr = "";
    String machiDep = "";
    String shiftName = "";
    String locName = "";

    AlertDialog showAttdialog;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.show_attendance, null);
        activity = (AppCompatActivity) view.getContext();
        emp_id = userInfoLists.get(0).getEmp_id();

        intime = view.findViewById(R.id.shift_in_time);
        latetime = view.findViewById(R.id.late_arrival_time);
        outtime = view.findViewById(R.id.shift_out_time);
        machArrTime = view.findViewById(R.id.mach_arri_time);
        machDepTime = view.findViewById(R.id.mach_depa_time);
        exShNa = view.findViewById(R.id.existing_shift_name);
        exLocNa = view.findViewById(R.id.existing_loc_name);


        if (showAttendanceNumber == 1) {
            date = dateToShow;
        } else if (AttendanceReqUpdate.showAttendanceNumberUpdate == 2) {
            date = dateToShowUpdate;
        }

        new Check().execute();

        builder.setView(view);

        showAttdialog = builder.create();

        showAttdialog.setCancelable(false);
        showAttdialog.setCanceledOnTouchOutside(false);

        showAttdialog.setButton(Dialog.BUTTON_NEGATIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                showAttendanceNumber = 0;
                AttendanceReqUpdate.showAttendanceNumberUpdate = 0;
                dialog.dismiss();
            }
        });

        return showAttdialog;

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
                AttendanceShow();
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

                if (shiftin == null) {
                    intime.setText("No Time Found");
                }else {
                    if (shiftin.isEmpty()) {
                        intime.setText("No Time Found");
                    } else {
                        intime.setText(shiftin);
                    }

                }

                if (shiftout == null) {
                    outtime.setText("No Time Found");
                }else {
                    if (shiftout.isEmpty()) {
                        outtime.setText("No Time Found");
                    } else {
                        outtime.setText(shiftout);
                    }
                }

                if (lateArr == null) {
                    latetime.setText("No Time Found");
                }else {
                    if (lateArr.isEmpty()) {
                        latetime.setText("No Time Found");
                    } else {
                        latetime.setText(lateArr);
                    }
                }

                if (machineArr == null) {
                    machArrTime.setText("No Time Found");
                }else {
                    if (machineArr.isEmpty()) {
                        machArrTime.setText("No Time Found");
                    } else {
                        machArrTime.setText(machineArr);
                    }
                }

                if (machiDep == null) {
                    machDepTime.setText("No Time Found");
                }else {
                    if (machiDep.isEmpty()) {
                        machDepTime.setText("No Time Found");
                    } else {
                        machDepTime.setText(machiDep);
                    }
                }

                if (shiftName == null) {
                    exShNa.setText("No Shift Found");
                }else {
                    if (shiftName.isEmpty()) {
                        exShNa.setText("No Shift Found");
                    } else {
                        exShNa.setText(shiftName);
                    }

                }

                if (locName == null) {
                    exLocNa.setText("No Location Found");
                }else {
                    if (locName.isEmpty()) {
                        exLocNa.setText("No Location Found");
                    } else {
                        exLocNa.setText(locName);
                    }
                }


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
                        showAttdialog.dismiss();

                    }
                });
            }
        }
    }

    public void AttendanceShow() {

        try {
            this.connection = createConnection();

            Statement stmt = connection.createStatement();

             shiftin = "";
             shiftout = "";
             lateArr = "";
             machineArr = "";
             machiDep = "";
             shiftName = "";
             locName = "";

            ResultSet rs=stmt.executeQuery("SELECT TO_CHAR (DA_CHECK.DAC_START_TIME, 'HH:MI:SS AM')   DAC_START_TIME,\n" +
                    "       TO_CHAR (DA_CHECK.DAC_END_TIME, 'HH:MI:SS AM')     DAC_END_TIME,\n" +
                    "       TO_CHAR (DA_CHECK.DAC_LATE_AFTER, 'HH:MI:SS AM')   DAC_LATE_AFTER,\n" +
                    "       TO_CHAR (DA_CHECK.DAC_MAC_IN_TIME, 'HH:MI:SS AM')  DAC_MAC_IN_TIME,\n" +
                    "       TO_CHAR (DA_CHECK.DAC_MAC_OUT_TIME, 'HH:MI:SS AM') DAC_MAC_OUT_TIME,\n" +
                    "       OFFICE_SHIFT_MST.OSM_NAME,\n" +
                    "       AMS.COA_NAME\n" +
                    "  FROM DA_CHECK,\n" +
                    "       OFFICE_SHIFT_MST,\n" +
                    "       (SELECT COA_NAME, AMS_MECHINE_CODE\n" +
                    "          FROM COMPANY_OFFICE_ADDRESS, ATTENDANCE_MECHINE_SETUP\n" +
                    "         WHERE COMPANY_OFFICE_ADDRESS.COA_ID =\n" +
                    "                  ATTENDANCE_MECHINE_SETUP.AMS_COA_ID) AMS\n" +
                    " WHERE     DA_CHECK.DAC_OSM_ID = OFFICE_SHIFT_MST.OSM_ID\n" +
                    "       AND DA_CHECK.DAC_AMS_MECHINE_CODE = AMS.AMS_MECHINE_CODE\n" +
                    "       AND DA_CHECK.DAC_EMP_ID = "+emp_id+"\n" +
                    "       AND DA_CHECK.DAC_DATE = '"+date+"'");



            while(rs.next()) {

                shiftin = rs.getString(1);
                lateArr = rs.getString(3);
                shiftout = rs.getString(2);
                machineArr = rs.getString(4);
                machiDep = rs.getString(5);
                shiftName = rs.getString(6);
                locName = rs.getString(7);
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
