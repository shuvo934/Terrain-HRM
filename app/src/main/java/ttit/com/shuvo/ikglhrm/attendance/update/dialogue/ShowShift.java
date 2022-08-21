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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import ttit.com.shuvo.ikglhrm.R;
import ttit.com.shuvo.ikglhrm.WaitProgress;
import ttit.com.shuvo.ikglhrm.attendance.reqUpdate.AttendanceReqUpdate;

import static ttit.com.shuvo.ikglhrm.OracleConnection.createConnection;
import static ttit.com.shuvo.ikglhrm.attendance.update.AttendanceUpdate.shift_osm_id;
import static ttit.com.shuvo.ikglhrm.attendance.update.AttendanceUpdate.showShiftNumber;

public class ShowShift extends AppCompatDialogFragment {

    private RecyclerView apptRecyclerView;
    public static ShowShiftAdapter showShiftAdapter;
    private RecyclerView.LayoutManager apptLayout;


    WaitProgress waitProgress = new WaitProgress();
    private String message = null;
    private Boolean conn = false;
    private Boolean connected = false;

    private Connection connection;
    AppCompatActivity activity;

    public static ArrayList<ShowShiftList> showShiftLists;

    String osm_id = "";
    int reliever = 0;
    AlertDialog showShiftdialog;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.show_shift, null);

        activity = (AppCompatActivity) view.getContext();

        showShiftLists = new ArrayList<>();

        if (showShiftNumber == 1) {
            osm_id = shift_osm_id;
        } else if (AttendanceReqUpdate.showShiftNumberUpdate == 2) {
            osm_id = AttendanceReqUpdate.shift_osm_id_update;
        }

        new Check().execute();
        apptRecyclerView = view.findViewById(R.id.all_shift_time_view);
        apptRecyclerView.setHasFixedSize(true);
        apptLayout = new LinearLayoutManager(getContext());
        apptRecyclerView.setLayoutManager(apptLayout);




        builder.setView(view);

        showShiftdialog = builder.create();

        showShiftdialog.setCancelable(false);
        showShiftdialog.setCanceledOnTouchOutside(false);

        showShiftdialog.setButton(Dialog.BUTTON_NEGATIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                showShiftNumber = 0;
                AttendanceReqUpdate.showShiftNumberUpdate = 0;
                dialog.dismiss();
            }
        });

        return showShiftdialog;
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

                showShiftAdapter = new ShowShiftAdapter(showShiftLists,getContext());
                apptRecyclerView.setAdapter(showShiftAdapter);



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
                        showShiftdialog.dismiss();
                    }
                });
            }
        }
    }

    public void AttendanceShow() {

        try {
            this.connection = createConnection();

            Statement stmt = connection.createStatement();
            showShiftLists = new ArrayList<>();

            ResultSet rs=stmt.executeQuery("SELECT OSM_RELIEVER_FLAG\n" +
                    "\t\t--INTO V_RELIEVER_FLAG\n" +
                    "\t\tFROM OFFICE_SHIFT_MST\n" +
                    "\t\tWHERE OSM_ID = "+osm_id+"");



            while(rs.next()) {
                reliever = Integer.parseInt(rs.getString(1));
            }

            if (reliever == 1) {

                ResultSet resultSet = stmt.executeQuery("SELECT OSM_NAME, TO_CHAR(OSM_START_TIME,'HH:MI:SS AM') OSM_START_TIME,\n" +
                        "    TO_CHAR(OSM_LATE_AFTER,'HH:MI:SS AM') OSM_LATE_AFTER,\n" +
                        "    TO_CHAR(OSM_EARLY_BEFORE,'HH:MI:SS AM') OSM_EARLY_BEFORE,\n" +
                        "    TO_CHAR(OSM_END_TIME,'HH:MI:SS AM') OSM_END_TIME,\n" +
                        "    TO_CHAR(OSM_EXTD_OUT_TIME,'HH:MI:SS AM') OSM_EXTD_OUT_TIME\n" +
                        "\tFROM OFFICE_SHIFT_MST OSM, (SELECT OSD_OSM_ASSIGN_ID REL_OSM_ID\n" +
                        "\t                            FROM OFFICE_SHIFT_MST, OFFICE_SHIFT_DTL\n" +
                        "\t                            WHERE OSD_OSM_ID = OSM_ID\n" +
                        "\t                            AND OSM_ID = "+osm_id+"\n" +
                        "\t                            AND OSM_RELIEVER_FLAG = 1) REL\n" +
                        "\tWHERE OSM.OSM_ID = REL.REL_OSM_ID");

                while (resultSet.next()) {
                    showShiftLists.add(new ShowShiftList(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4),resultSet.getString(5),resultSet.getString(6)));
                }
            } else {
                ResultSet resultSet1 = stmt.executeQuery("SELECT OSM_NAME, \n" +
                        "    TO_CHAR(OSM_START_TIME,'HH:MI:SS AM') OSM_START_TIME,\n" +
                        "    TO_CHAR(OSM_LATE_AFTER,'HH:MI:SS AM') OSM_LATE_AFTER,\n" +
                        "    TO_CHAR(OSM_EARLY_BEFORE,'HH:MI:SS AM') OSM_EARLY_BEFORE,\n" +
                        "    TO_CHAR(OSM_END_TIME,'HH:MI:SS AM') OSM_END_TIME,\n" +
                        "    TO_CHAR(OSM_EXTD_OUT_TIME,'HH:MI:SS AM') OSM_EXTD_OUT_TIME\n" +
                        "\tFROM OFFICE_SHIFT_MST\n" +
                        "\tWHERE OSM_ID = "+osm_id+"");

                while (resultSet1.next()) {
                    showShiftLists.add(new ShowShiftList(resultSet1.getString(1),resultSet1.getString(2),resultSet1.getString(3),resultSet1.getString(4),resultSet1.getString(5),resultSet1.getString(6)));

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
