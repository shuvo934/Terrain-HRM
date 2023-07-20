package ttit.com.shuvo.ikglhrm.payRoll.paySlip;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import ttit.com.shuvo.ikglhrm.R;
import ttit.com.shuvo.ikglhrm.WaitProgress;
import static ttit.com.shuvo.ikglhrm.payRoll.paySlip.PaySlip.errorMsgMonth;
import static ttit.com.shuvo.ikglhrm.payRoll.paySlip.PaySlip.selectMonth;
import static ttit.com.shuvo.ikglhrm.payRoll.paySlip.PaySlip.selectMonthLay;
import static ttit.com.shuvo.ikglhrm.payRoll.paySlip.PaySlip.select_month_id;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MonthSelectDialogue extends AppCompatDialogFragment implements MonthSelectAdapter.ClickedItem {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    MonthSelectAdapter monthSelectAdapter;


    TextInputEditText search;
    AlertDialog dialog;
    TextView noMonthMsg;

    Boolean isfiltered = false;
    ArrayList<MonthSelectList> filteredList = new ArrayList<>();
    private ArrayList<MonthSelectList> lists = new ArrayList<>();


    WaitProgress waitProgress = new WaitProgress();
//    private String message = null;
    private Boolean conn = false;
    private Boolean connected = false;

//    private Connection connection;
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
        noMonthMsg = view.findViewById(R.id.no_month_found_msg);
        noMonthMsg.setVisibility(View.GONE);

        search = view.findViewById(R.id.month_search);
        lists = new ArrayList<>();

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);



//        new Check().execute();
        getMonths();

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
        errorMsgMonth.setVisibility(View.GONE);
        selectMonthLay.setHint("Month:");
        selectMonth.setText(name);
        dialog.dismiss();
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
//
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
//                MonthShow();
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
//                if (lists.size() == 0) {
//                    noMonthMsg.setVisibility(View.VISIBLE);
//                }
//                else {
//                    noMonthMsg.setVisibility(View.GONE);
//                }
//                monthSelectAdapter = new MonthSelectAdapter(lists,getContext(), MonthSelectDialogue.this);
//                recyclerView.setAdapter(monthSelectAdapter);
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
//                        ((Activity)mContext).finish();
//                    }
//                });
//
//            }
//        }
//    }
//
//    public void MonthShow() {
//
//        try {
//            this.connection = createConnection();
//
//            Statement stmt = connection.createStatement();
//            lists = new ArrayList<>();
//
//            ResultSet rs=stmt.executeQuery("SELECT TO_CHAR(PAYROLL_MONTH_SETUP.PMS_MONTH,'fmMon-RRRR') MONTH_NAME, TO_CHAR(PAYROLL_MONTH_SETUP.PMS_MONTH,'DD-MON-YY') MONTH_ID,\n" +
//                    "TO_CHAR(PAYROLL_MONTH_SETUP.PMS_STD_DATE,'DD-MON-YY') MONTH_START,TO_CHAR(PAYROLL_MONTH_SETUP.PMS_END_DATE,'DD-MON-YY') MONTH_END\n" +
//                    "FROM PAYROLL_MONTH_SETUP\n" +
//                    "ORDER BY PAYROLL_MONTH_SETUP.PMS_MONTH DESC");
//
//
//
//            while(rs.next()) {
//
//                lists.add(new MonthSelectList(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4)));
//
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

    public void getMonths() {
        waitProgress.show(activity.getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;
        lists = new ArrayList<>();

        String url = "http://103.56.208.123:8001/apex/ttrams/paySlip/getPayMonths";

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
                        JSONObject monthInfo = array.getJSONObject(i);

                        String month_name = monthInfo.getString("month_name")
                                .equals("null") ? "" : monthInfo.getString("month_name");
                        String month_id = monthInfo.getString("month_id")
                                .equals("null") ? "" : monthInfo.getString("month_id");
                        String month_start = monthInfo.getString("month_start")
                                .equals("null") ? "" : monthInfo.getString("month_start");
                        String month_end = monthInfo.getString("month_end")
                                .equals("null") ? "" : monthInfo.getString("month_end");

                        lists.add(new MonthSelectList(month_name,month_id,month_start,month_end));
                    }
                }
                connected = true;
                updateDial();
            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                updateDial();
            }
        }, error -> {
           error.printStackTrace();
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
                if (lists.size() == 0) {
                    noMonthMsg.setVisibility(View.VISIBLE);
                }
                else {
                    noMonthMsg.setVisibility(View.GONE);
                }
                monthSelectAdapter = new MonthSelectAdapter(lists,getContext(), MonthSelectDialogue.this);
                recyclerView.setAdapter(monthSelectAdapter);
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setMessage("There is a network issue in the server. Please Try later.")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    getMonths();
                    dialog.dismiss();
                });
                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negative.setOnClickListener(v -> {
                    dialog.dismiss();
                    ((Activity)mContext).finish();
                });
            }
        }
        else {
            AlertDialog dialog = new AlertDialog.Builder(getContext())
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel",null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                getMonths();
                dialog.dismiss();
            });
            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> {
                dialog.dismiss();
                ((Activity)mContext).finish();
            });

        }
    }

}
