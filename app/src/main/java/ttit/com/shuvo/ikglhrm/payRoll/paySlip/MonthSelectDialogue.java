package ttit.com.shuvo.ikglhrm.payRoll.paySlip;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import ttit.com.shuvo.ikglhrm.R;
import ttit.com.shuvo.ikglhrm.WaitProgress;
import ttit.com.shuvo.ikglhrm.attendance.reqUpdate.AttendanceReqUpdate;
import ttit.com.shuvo.ikglhrm.attendance.update.dialogue.SelectAllAdapter;
import ttit.com.shuvo.ikglhrm.attendance.update.dialogue.SelectAllList;
import ttit.com.shuvo.ikglhrm.attendance.update.dialogue.ShowAttendance;
import ttit.com.shuvo.ikglhrm.attendance.update.dialogue.ShowShiftAdapter;
import ttit.com.shuvo.ikglhrm.leaveAll.LeaveApplication;

import static ttit.com.shuvo.ikglhrm.OracleConnection.createConnection;
import static ttit.com.shuvo.ikglhrm.attendance.update.AttendanceUpdate.dialogText;
import static ttit.com.shuvo.ikglhrm.payRoll.paySlip.PaySlip.errorMsgMonth;
import static ttit.com.shuvo.ikglhrm.payRoll.paySlip.PaySlip.selectMonth;
import static ttit.com.shuvo.ikglhrm.payRoll.paySlip.PaySlip.selectMonthLay;
import static ttit.com.shuvo.ikglhrm.payRoll.paySlip.PaySlip.select_month_id;

public class MonthSelectDialogue extends AppCompatDialogFragment implements MonthSelectAdapter.ClickedItem {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    MonthSelectAdapter monthSelectAdapter;


    TextInputEditText search;
    AlertDialog dialog;

    Boolean isfiltered = false;
    ArrayList<MonthSelectList> filteredList = new ArrayList<>();
    private ArrayList<MonthSelectList> lists = new ArrayList<>();


    WaitProgress waitProgress = new WaitProgress();
    private String message = null;
    private Boolean conn = false;
    private Boolean connected = false;

    private Connection connection;
    AppCompatActivity activity;

    Context mContext;

    public MonthSelectDialogue(Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.month_select_dialogue, null);

        activity = (AppCompatActivity) view.getContext();
        recyclerView = view.findViewById(R.id.month_list_of_item);

        search = view.findViewById(R.id.month_search);
        lists = new ArrayList<>();

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);



        new Check().execute();

        builder.setView(view);
        dialog = builder.create();

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);





        dialog.setButton(Dialog.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                filter(s.toString());
            }
        });

        return dialog;
    }

    private void filter(String text) {

        filteredList = new ArrayList<>();
        for (MonthSelectList item : lists) {
            if (item.getMonthName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add((item));
                isfiltered = true;
            }
        }
        monthSelectAdapter.filterList(filteredList);
    }


    @Override
    public void onCategoryClicked(int CategoryPosition) {

        String name = "";
        String id = "";
        if (isfiltered) {
            name = filteredList.get(CategoryPosition).getMonthName();
            id = filteredList.get(CategoryPosition).getMonthId();
        } else {
            name = lists.get(CategoryPosition).getMonthName();
            id = lists.get(CategoryPosition).getMonthId();
        }

        System.out.println(name);
        System.out.println(id);

        select_month_id = id;
        selectMonth.setText(name);
        errorMsgMonth.setVisibility(View.GONE);
        selectMonthLay.setHint("Month:");
        dialog.dismiss();
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
                MonthShow();
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

                monthSelectAdapter = new MonthSelectAdapter(lists,getContext(), MonthSelectDialogue.this);
                recyclerView.setAdapter(monthSelectAdapter);


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
                        ((Activity)mContext).finish();
                    }
                });

            }
        }
    }

    public void MonthShow() {

        try {
            this.connection = createConnection();

            Statement stmt = connection.createStatement();
            lists = new ArrayList<>();

            ResultSet rs=stmt.executeQuery("SELECT TO_CHAR(PAYROLL_MONTH_SETUP.PMS_MONTH,'fmMon-RRRR') MONTH_NAME, TO_CHAR(PAYROLL_MONTH_SETUP.PMS_MONTH,'DD-MON-YY') MONTH_ID,\n" +
                    "TO_CHAR(PAYROLL_MONTH_SETUP.PMS_STD_DATE,'DD-MON-YY') MONTH_START,TO_CHAR(PAYROLL_MONTH_SETUP.PMS_END_DATE,'DD-MON-YY') MONTH_END\n" +
                    "FROM PAYROLL_MONTH_SETUP\n" +
                    "ORDER BY PAYROLL_MONTH_SETUP.PMS_MONTH DESC");



            while(rs.next()) {

                lists.add(new MonthSelectList(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4)));

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
