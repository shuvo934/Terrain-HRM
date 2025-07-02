package ttit.com.shuvo.ikglhrm.attendance.update.dialogue;

import android.app.Dialog;
import android.content.Context;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import ttit.com.shuvo.ikglhrm.R;
import ttit.com.shuvo.ikglhrm.WaitProgress;

import static ttit.com.shuvo.ikglhrm.attendance.update.AttendanceUpdate.shift_osm_id;
import static ttit.com.shuvo.ikglhrm.attendance.update.AttendanceUpdate.showShiftNumber;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.api_url_front;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class ShowShift extends AppCompatDialogFragment {

    private RecyclerView apptRecyclerView;
    ShowShiftAdapter showShiftAdapter;
    RecyclerView.LayoutManager apptLayout;


    WaitProgress waitProgress = new WaitProgress();
//    private String message = null;
    private Boolean conn = false;
    private Boolean connected = false;

//    private Connection connection;
    AppCompatActivity activity;

    public static ArrayList<ShowShiftList> showShiftLists;

    String osm_id = "";
//    int reliever = 0;
    AlertDialog showShiftdialog;

    Context mContext;

    Logger logger = Logger.getLogger(ShowShift.class.getName());

    public ShowShift(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.show_shift, null);

        activity = (AppCompatActivity) view.getContext();

        showShiftLists = new ArrayList<>();

        if (showShiftNumber == 1) {
            osm_id = shift_osm_id;
        }
//        else if (AttendanceReqUpdate.showShiftNumberUpdate == 2) {
//            osm_id = AttendanceReqUpdate.shift_osm_id_update;
//        }

//        new Check().execute();
        getShiftDetails();
        apptRecyclerView = view.findViewById(R.id.all_shift_time_view);
        apptRecyclerView.setHasFixedSize(true);
        apptLayout = new LinearLayoutManager(getContext());
        apptRecyclerView.setLayoutManager(apptLayout);




        builder.setView(view);

        showShiftdialog = builder.create();

        showShiftdialog.setCancelable(false);
        showShiftdialog.setCanceledOnTouchOutside(false);

        showShiftdialog.setButton(Dialog.BUTTON_NEGATIVE, "OK", (dialog, which) -> {

            showShiftNumber = 0;
//                AttendanceReqUpdate.showShiftNumberUpdate = 0;
            dialog.dismiss();
        });

        return showShiftdialog;
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
//        catch (IOException | InterruptedException e)          { e.printStackTrace(); }
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
//                AttendanceShow();
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
//                showShiftAdapter = new ShowShiftAdapter(showShiftLists,getContext());
//                apptRecyclerView.setAdapter(showShiftAdapter);
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
//                        showShiftdialog.dismiss();
//                    }
//                });
//            }
//        }
//    }

//    public void AttendanceShow() {
//
//        try {
//            this.connection = createConnection();
//
//            Statement stmt = connection.createStatement();
//            showShiftLists = new ArrayList<>();
//
//            ResultSet rs=stmt.executeQuery("SELECT OSM_RELIEVER_FLAG\n" +
//                    "\t\t--INTO V_RELIEVER_FLAG\n" +
//                    "\t\tFROM OFFICE_SHIFT_MST\n" +
//                    "\t\tWHERE OSM_ID = "+osm_id+"");
//
//
//
//            while(rs.next()) {
//                reliever = Integer.parseInt(rs.getString(1));
//            }
//
//            if (reliever == 1) {
//
//                ResultSet resultSet = stmt.executeQuery("SELECT OSM_NAME, TO_CHAR(OSM_START_TIME,'HH:MI:SS AM') OSM_START_TIME,\n" +
//                        "    TO_CHAR(OSM_LATE_AFTER,'HH:MI:SS AM') OSM_LATE_AFTER,\n" +
//                        "    TO_CHAR(OSM_EARLY_BEFORE,'HH:MI:SS AM') OSM_EARLY_BEFORE,\n" +
//                        "    TO_CHAR(OSM_END_TIME,'HH:MI:SS AM') OSM_END_TIME,\n" +
//                        "    TO_CHAR(OSM_EXTD_OUT_TIME,'HH:MI:SS AM') OSM_EXTD_OUT_TIME\n" +
//                        "\tFROM OFFICE_SHIFT_MST OSM, (SELECT OSD_OSM_ASSIGN_ID REL_OSM_ID\n" +
//                        "\t                            FROM OFFICE_SHIFT_MST, OFFICE_SHIFT_DTL\n" +
//                        "\t                            WHERE OSD_OSM_ID = OSM_ID\n" +
//                        "\t                            AND OSM_ID = "+osm_id+"\n" +
//                        "\t                            AND OSM_RELIEVER_FLAG = 1) REL\n" +
//                        "\tWHERE OSM.OSM_ID = REL.REL_OSM_ID");
//
//                while (resultSet.next()) {
//                    showShiftLists.add(new ShowShiftList(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4),resultSet.getString(5),resultSet.getString(6)));
//                }
//            } else {
//                ResultSet resultSet1 = stmt.executeQuery("SELECT OSM_NAME, \n" +
//                        "    TO_CHAR(OSM_START_TIME,'HH:MI:SS AM') OSM_START_TIME,\n" +
//                        "    TO_CHAR(OSM_LATE_AFTER,'HH:MI:SS AM') OSM_LATE_AFTER,\n" +
//                        "    TO_CHAR(OSM_EARLY_BEFORE,'HH:MI:SS AM') OSM_EARLY_BEFORE,\n" +
//                        "    TO_CHAR(OSM_END_TIME,'HH:MI:SS AM') OSM_END_TIME,\n" +
//                        "    TO_CHAR(OSM_EXTD_OUT_TIME,'HH:MI:SS AM') OSM_EXTD_OUT_TIME\n" +
//                        "\tFROM OFFICE_SHIFT_MST\n" +
//                        "\tWHERE OSM_ID = "+osm_id+"");
//
//                while (resultSet1.next()) {
//                    showShiftLists.add(new ShowShiftList(resultSet1.getString(1),resultSet1.getString(2),resultSet1.getString(3),resultSet1.getString(4),resultSet1.getString(5),resultSet1.getString(6)));
//
//                }
//            }
//            connected = true;
//
//            connection.close();
//
//        }
//        catch (Exception e) {
//
//            //   Toast.makeText(MainActivity.this, ""+e,Toast.LENGTH_LONG).show();
//            Log.i("ERRRRR", e.getLocalizedMessage());
//            e.printStackTrace();
//        }
//
//    }

    public void getShiftDetails() {
        waitProgress.show(activity.getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;

        showShiftLists = new ArrayList<>();

        String url = api_url_front + "attendanceUpdateReq/getShiftDetails/"+osm_id;

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url , response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String osm_name = jsonObject.getString("osm_name")
                        .equals("null") ? "" : jsonObject.getString("osm_name");
                String osm_start_time = jsonObject.getString("osm_start_time")
                        .equals("null") ? "" : jsonObject.getString("osm_start_time");
                String osm_late_after = jsonObject.getString("osm_late_after")
                        .equals("null") ? "" : jsonObject.getString("osm_late_after");
                String osm_early_before = jsonObject.getString("osm_early_before")
                        .equals("null") ? "" : jsonObject.getString("osm_early_before");
                String osm_end_time = jsonObject.getString("osm_end_time")
                        .equals("null") ? "" : jsonObject.getString("osm_end_time");
                String osm_extd_out_time = jsonObject.getString("osm_extd_out_time")
                        .equals("null") ? "" : jsonObject.getString("osm_extd_out_time");

                showShiftLists.add(new ShowShiftList(osm_name,osm_start_time,osm_late_after,
                        osm_early_before,osm_end_time,osm_extd_out_time));

                connected = true;
                updateLay();
            }
            catch (JSONException e) {
                logger.log(Level.WARNING, e.getMessage(), e);
                connected = false;
                updateLay();
            }
        }, error -> {
            logger.log(Level.WARNING, error.getMessage(), error);
            conn = false;
            connected = false;
            updateLay();
        });

        requestQueue.add(stringRequest);
    }

    private void updateLay() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                showShiftAdapter = new ShowShiftAdapter(showShiftLists,getContext());
                apptRecyclerView.setAdapter(showShiftAdapter);
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(mContext)
                        .setMessage("There is a network issue in the server. Please Try later.")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    getShiftDetails();
                    dialog.dismiss();
                });
                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negative.setOnClickListener(v -> {
                    dialog.dismiss();
                    showShiftdialog.dismiss();
                });
            }
        }
        else {
            AlertDialog dialog = new AlertDialog.Builder(mContext)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel",null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                getShiftDetails();
                dialog.dismiss();
            });
            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> {
                dialog.dismiss();
                showShiftdialog.dismiss();
            });
        }
    }

}
