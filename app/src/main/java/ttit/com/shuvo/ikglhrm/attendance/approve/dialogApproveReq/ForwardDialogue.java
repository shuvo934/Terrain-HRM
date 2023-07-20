package ttit.com.shuvo.ikglhrm.attendance.approve.dialogApproveReq;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ttit.com.shuvo.ikglhrm.R;
import ttit.com.shuvo.ikglhrm.WaitProgress;
import ttit.com.shuvo.ikglhrm.attendance.approve.AttendanceApprove;
import ttit.com.shuvo.ikglhrm.attendance.update.dialogue.SelectAllList;
import ttit.com.shuvo.ikglhrm.leaveAll.leaveApprove.LeaveApprove;

import static ttit.com.shuvo.ikglhrm.Login.userInfoLists;
import static ttit.com.shuvo.ikglhrm.attendance.approve.AttendanceApprove.darm_id;
import static ttit.com.shuvo.ikglhrm.attendance.approve.AttendanceApprove.forwardFromAtt;
import static ttit.com.shuvo.ikglhrm.attendance.approve.AttendanceApprove.req_code;
import static ttit.com.shuvo.ikglhrm.leaveAll.leaveApprove.LeaveApprove.forwardFromLeave;
import static ttit.com.shuvo.ikglhrm.leaveAll.leaveApprove.LeaveApprove.la_id;
import static ttit.com.shuvo.ikglhrm.leaveAll.leaveApprove.LeaveApprove.req_code_leave;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ForwardDialogue extends AppCompatDialogFragment implements ForwardAdapter.ClickedItem {

    TextInputEditText employeeName;
    TextInputLayout employeeNameLayout;

    LinearLayout lisLay;
    RecyclerView empListView;
    RecyclerView.LayoutManager layoutManager;
    ForwardAdapter forwardAdapter;

    TextInputLayout commLay;
    TextInputEditText comm;

    TextView noEmp;

    Button goBack;
    Button cont;

    AppCompatActivity activity;

    WaitProgress waitProgress = new WaitProgress();

    private String message = null;
    private Boolean conn = false;
    private Boolean connected = false;

    private Boolean isForwarded = false;
    private Boolean ffffoooorrrwww = false;
    private Boolean isForwardExe = false;

    private Boolean isForwardedLeave = false;
    private Boolean ffffoooorrrwwwllll = false;
    private Boolean isForwardExeLeave = false;

//    private Connection connection;

    public  ArrayList<ForwardEMPList> forwardEMPLists;
    public  ArrayList<SelectAllList> allSelectedApprover;
    public  ArrayList<SelectAllList> allApproverEmp;
    public  ArrayList<SelectAllList> allApproverWithoutDiv;
    public  ArrayList<SelectAllList> allApproverDivision;

    String emp_id = "";
    String desig_priority = "";
    String divm_id = "";
    String approval_band = "";
    int count_approv_emp = 0;

//    String dard_id = "";
//    String lad_id = "";
    String forward_to_id = "";
    String forward_comm = "";
//    int req_status_count = 0;
//    int req_status_count_leave = 0;
    Context mContext;

    View view;
    AlertDialog forwarDialog;

    public ForwardDialogue(Context context) {

        this.mContext = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        view = inflater.inflate(R.layout.forward_req_view, null);
        activity = (AppCompatActivity) view.getContext();

        employeeName = view.findViewById(R.id.emp_name_drop_down);
        employeeNameLayout = view.findViewById(R.id.emp_name_drop_down_lay);

        lisLay = view.findViewById(R.id.forward_list_view_approve_lay);
        empListView = view.findViewById(R.id.forward_list_view_approve);

        commLay = view.findViewById(R.id.comments_given_for_forward_lay);
        comm = view.findViewById(R.id.comments_given_for_forward);

        goBack = view.findViewById(R.id.forward_go_back);
        cont = view.findViewById(R.id.forward_continue);

        noEmp = view.findViewById(R.id.no_employee_msg);
        forwardEMPLists = new ArrayList<>();
        allApproverDivision = new ArrayList<>();
        allApproverEmp = new ArrayList<>();
        allApproverWithoutDiv = new ArrayList<>();
        allSelectedApprover = new ArrayList<>();

        emp_id = userInfoLists.get(0).getEmp_id();
        //emp_id="493";

//        new CheckFORLISt().execute();
        getForwardToEmp();

        empListView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        empListView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(empListView.getContext(),DividerItemDecoration.VERTICAL);
        empListView.addItemDecoration(dividerItemDecoration);




        builder.setView(view);

        forwarDialog = builder.create();
        forwarDialog.setCancelable(false);
        forwarDialog.setCanceledOnTouchOutside(false);

        comm.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                        event.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT) {
                    if (event == null || !event.isShiftPressed()) {
                        // the user is done typing.
                        Log.i("Let see", "Come here");
                        comm.clearFocus();
                        closeKeyBoard();

                        return false; // consume.
                    }
                }
                return false;
            }
        });

        employeeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int vis = lisLay.getVisibility();
                if (vis == 0) {
                    lisLay.setVisibility(View.GONE);
                } else {
                    lisLay.setVisibility(View.VISIBLE);
                }

            }
        });

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forwarDialog.dismiss();
            }
        });

        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                forward_comm = comm.getText().toString();
                System.out.println(forwardFromAtt);
                System.out.println(forwardFromLeave);

                if (forwardFromAtt == 1) {
                    if (forward_comm.isEmpty()) {
                        Toast.makeText(getContext(),"Please enter forward comment",Toast.LENGTH_SHORT).show();
                    } else {
                        if (forward_to_id.isEmpty()) {
                            Toast.makeText(getContext(),"Please enter forward to employee",Toast.LENGTH_SHORT).show();
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                            builder.setTitle("Forward Request!")
                                    .setMessage("Do you want forward this request?")
                                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

//                                            new ForwardCheck().execute();
                                            forwardAttReq();
                                        }
                                    })
                                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();


                        }
                    }
                } else if (forwardFromLeave == 1) {
                    if (forward_comm.isEmpty()) {
                        Toast.makeText(getContext(),"Please enter forward comment",Toast.LENGTH_SHORT).show();
                    } else {
                        if (forward_to_id.isEmpty()) {
                            Toast.makeText(getContext(),"Please enter forward to employee",Toast.LENGTH_SHORT).show();
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                            builder.setTitle("Forward Leave Application!")
                                    .setMessage("Do you want forward this leave application?")
                                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

//                                            new ForwardCheckLeave().execute();
                                            forwardLeaveReq();
                                        }
                                    })
                                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();


                        }
                    }
                }



            }
        });

        return forwarDialog;

    }

    private void closeKeyBoard() {

        if (view != null) {
            view.clearFocus();
            InputMethodManager mgr = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onCategoryClicked(int CategoryPosition) {
        lisLay.setVisibility(View.GONE);
        String name = "";

        name = forwardEMPLists.get(CategoryPosition).getEmpName();
        forward_to_id = forwardEMPLists.get(0).getEmpID();
        employeeName.setText(name);
        employeeName.setTextColor(Color.BLACK);
        employeeNameLayout.setHint("Employee Name");

    }

//    public boolean isConnected() {
//        boolean connected = false;
//        boolean isMobile = false;
//        try {
//            ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
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
//        catch (IOException | InterruptedException e)          { e.printStackTrace(); }
//
//        return false;
//    }

//    public class CheckFORLISt extends AsyncTask<Void, Void, Void> {
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
//            if (isConnected()) {
//
//                ForwardToEMP();
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
//                //Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
//                if (forwardEMPLists.size() == 0) {
//                    noEmp.setVisibility(View.VISIBLE);
//                } else {
//                    noEmp.setVisibility(View.GONE);
//                    forwardAdapter = new ForwardAdapter(forwardEMPLists, getContext(), ForwardDialogue.this);
//                    empListView.setAdapter(forwardAdapter);
//                    forwardAdapter.notifyDataSetChanged();
//                }
//
//
//                conn = false;
//                connected = false;
//            }
//            else {
//                Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
//                AlertDialog dialog;
//                dialog = new AlertDialog.Builder(getContext())
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
//                        new CheckFORLISt().execute();
//                        dialog.dismiss();
//                    }
//                });
//
//                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
//                negative.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                        forwarDialog.dismiss();
//                    }
//                });
//            }
//        }
//
//    }

//    public class ForwardCheck extends AsyncTask<Void, Void, Void> {
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
//
//                ForwardedQuery();
//                if (isForwarded) {
//                    ffffoooorrrwww = true;
//
//                }
//
//            } else {
//                ffffoooorrrwww = false;
//
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
//            if (ffffoooorrrwww) {
//
//
//                if (isForwardExe) {
//
//                    req_code = "";
//                    darm_id = "";
//                    AttendanceApprove.darm_emp_id = "";
//                    AttendanceApprove.selectApproveReqLists = new ArrayList<>();
//                    forwardFromAtt = 0;
//                    System.out.println("INSERTED");
//
//                    forwarDialog.dismiss();
//
//                    AlertDialog dialog1 = new AlertDialog.Builder(activity)
//                            .setMessage("Request Forwarded Successfully")
//                            .setPositiveButton("OK", null)
//                            .show();
//
//                    dialog1.setCancelable(false);
//                    dialog1.setCanceledOnTouchOutside(false);
//                    Button positive = dialog1.getButton(AlertDialog.BUTTON_POSITIVE);
//                    positive.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//                            dialog1.dismiss();
//                            ((Activity)mContext).finish();
//
//                        }
//                    });
//
//                } else {
//
//                    Toast.makeText(getContext(), "Already Updated by Another User", Toast.LENGTH_SHORT).show();
//
//                }
//
//                ffffoooorrrwww = false;
//                isForwardExe = false;
//                isForwarded = false;
//
//
//            }
//            else {
//                Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
//                AlertDialog dialog = new AlertDialog.Builder(activity)
//                        .setMessage("Please Check Your Internet Connection")
//                        .setPositiveButton("Retry", null)
//                        .setNegativeButton("Cancel",null)
//                        .show();
//
////                dialog.setCancelable(false);
////                dialog.setCanceledOnTouchOutside(false);
//                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
//                positive.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        new ForwardCheck().execute();
//                        dialog.dismiss();
//                    }
//                });
//            }
//        }
//    }

//    public class ForwardCheckLeave extends AsyncTask<Void, Void, Void> {
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
//
//                ForwardedQueryLeave();
//                if (isForwardedLeave) {
//                    ffffoooorrrwwwllll = true;
//
//                }
//
//            } else {
//                ffffoooorrrwwwllll = false;
//
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
//            if (ffffoooorrrwwwllll) {
//
//
//                if (isForwardExeLeave) {
//
//                    req_code_leave = "";
//                    la_id = "";
//                    LeaveApprove.la_emp_id = "";
//                    LeaveApprove.leaveReqList = new ArrayList<>();
//                    forwardFromLeave = 0;
//                    System.out.println("INSERTED");
//
//                    forwarDialog.dismiss();
//
//                    AlertDialog dialog1 = new AlertDialog.Builder(activity)
//                            .setMessage("Leave Application Forwarded Successfully")
//                            .setPositiveButton("OK", null)
//                            .show();
//
//                    dialog1.setCancelable(false);
//                    dialog1.setCanceledOnTouchOutside(false);
//                    Button positive = dialog1.getButton(AlertDialog.BUTTON_POSITIVE);
//                    positive.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//                            dialog1.dismiss();
//                            ((Activity)mContext).finish();
//
//                        }
//                    });
//
//                } else {
//
//                    Toast.makeText(getContext(), "Already Updated by Another User", Toast.LENGTH_SHORT).show();
//
//                }
//
//                ffffoooorrrwwwllll = false;
//                isForwardExeLeave = false;
//                isForwardedLeave = false;
//
//
//            }
//            else {
//                Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
//                AlertDialog dialog = new AlertDialog.Builder(activity)
//                        .setMessage("Please Check Your Internet Connection")
//                        .setPositiveButton("Retry", null)
//                        .setNegativeButton("Cancel",null)
//                        .show();
//
////                dialog.setCancelable(false);
////                dialog.setCanceledOnTouchOutside(false);
//                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
//                positive.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        new ForwardCheckLeave().execute();
//                        dialog.dismiss();
//                    }
//                });
//            }
//        }
//    }

//    public void ForwardToEMP() {
//        try {
//            this.connection = createConnection();
//
//            Statement stmt = connection.createStatement();
//
//            forwardEMPLists = new ArrayList<>();
//            allApproverDivision = new ArrayList<>();
//            allApproverEmp = new ArrayList<>();
//            allApproverWithoutDiv = new ArrayList<>();
//            allSelectedApprover = new ArrayList<>();
//
//             desig_priority = "";
//             divm_id = "";
//             approval_band = "";
//             count_approv_emp = 0;
//
//            // Approver Division
//            ResultSet rs1 = stmt.executeQuery("SELECT EMP_MST.EMP_ID,\n" +
//                    "       EMP_MST.EMP_NAME,\n" +
//                    "       EMP_JOB_HISTORY.JOB_CALLING_TITLE,\n" +
//                    "       JOB_SETUP_MST.JSM_NAME,\n" +
//                    "       DIVISION_MST.DIVM_NAME\n" +
//                    "  FROM EMP_MST,\n" +
//                    "       EMP_JOB_HISTORY,\n" +
//                    "       JOB_SETUP_DTL,\n" +
//                    "       JOB_SETUP_MST,\n" +
//                    "       DESIG_MST,\n" +
//                    "       DIVISION_MST\n" +
//                    " WHERE     JOB_SETUP_MST.JSM_DIVM_ID =\n" +
//                    "              (SELECT JOB_SETUP_MST.JSM_DIVM_ID\n" +
//                    "                 FROM EMP_MST,\n" +
//                    "                      EMP_JOB_HISTORY,\n" +
//                    "                      JOB_SETUP_DTL,\n" +
//                    "                      JOB_SETUP_MST,\n" +
//                    "                      DESIG_MST\n" +
//                    "                WHERE     EMP_MST.EMP_ID = "+emp_id+"\n" +
//                    "                      AND (JOB_SETUP_DTL.JSD_JSM_ID = JOB_SETUP_MST.JSM_ID)\n" +
//                    "                      AND (JOB_SETUP_MST.JSM_DESIG_ID = DESIG_MST.DESIG_ID)\n" +
//                    "                      AND (EMP_JOB_HISTORY.JOB_ID = EMP_MST.EMP_JOB_ID)\n" +
//                    "                      AND (JOB_SETUP_DTL.JSD_ID = EMP_JOB_HISTORY.JOB_JSD_ID))\n" +
//                    "       AND EMP_JOB_HISTORY.JOB_STATUS NOT IN ('Closed', 'Suspended')\n" +
//                    "       AND (JOB_SETUP_DTL.JSD_JSM_ID = JOB_SETUP_MST.JSM_ID)\n" +
//                    "       AND (JOB_SETUP_MST.JSM_DESIG_ID = DESIG_MST.DESIG_ID)\n" +
//                    "       AND (EMP_JOB_HISTORY.JOB_ID = EMP_MST.EMP_JOB_ID)\n" +
//                    "       AND (JOB_SETUP_DTL.JSD_ID = EMP_JOB_HISTORY.JOB_JSD_ID)\n" +
//                    "       AND EMP_JOB_HISTORY.JOB_EMAIL IS NOT NULL\n" +
//                    "       AND JOB_SETUP_MST.JSM_DIVM_ID = DIVISION_MST.DIVM_ID\n" +
//                    "       AND DESIG_MST.DESIG_PRIORITY IN\n" +
//                    "              (    SELECT DISTINCT REGEXP_SUBSTR (LAH_APPROVAL_BAND,\n" +
//                    "                                                  '[^,]+',\n" +
//                    "                                                  1,\n" +
//                    "                                                  LEVEL)\n" +
//                    "                     FROM (SELECT LAH_APPROVAL_BAND\n" +
//                    "                             FROM LEAVE_APPROVAL_HIERARCHY\n" +
//                    "                            WHERE LAH_BAND_NO =\n" +
//                    "                                     (SELECT DESIG_MST.DESIG_PRIORITY\n" +
//                    "                                        FROM EMP_MST,\n" +
//                    "                                             EMP_JOB_HISTORY,\n" +
//                    "                                             JOB_SETUP_DTL,\n" +
//                    "                                             JOB_SETUP_MST,\n" +
//                    "                                             DESIG_MST\n" +
//                    "                                       WHERE     EMP_MST.EMP_ID =\n" +
//                    "                                                    "+emp_id+"\n" +
//                    "                                             AND (JOB_SETUP_DTL.JSD_JSM_ID =\n" +
//                    "                                                     JOB_SETUP_MST.JSM_ID)\n" +
//                    "                                             AND (JOB_SETUP_MST.JSM_DESIG_ID =\n" +
//                    "                                                     DESIG_MST.DESIG_ID)\n" +
//                    "                                             AND (EMP_JOB_HISTORY.JOB_ID =\n" +
//                    "                                                     EMP_MST.EMP_JOB_ID)\n" +
//                    "                                             AND (JOB_SETUP_DTL.JSD_ID =\n" +
//                    "                                                     EMP_JOB_HISTORY.JOB_JSD_ID)))\n" +
//                    "               CONNECT BY REGEXP_SUBSTR (LAH_APPROVAL_BAND,\n" +
//                    "                                         '[^,]+',\n" +
//                    "                                         1,\n" +
//                    "                                         LEVEL)\n" +
//                    "                             IS NOT NULL)");
//
//            while (rs1.next()) {
//
//                //approverDivision.add(new ApproverList(rs1.getString(1),rs1.getString(2)+",\n("+rs1.getString(4)+", "+rs1.getString(5)+")"));
//                allApproverDivision.add(new SelectAllList(rs1.getString(1),rs1.getString(2),rs1.getString(3),rs1.getString(4),rs1.getString(5)));
//
//
//            }
//
//            // Approver without Division
//            ResultSet rs2 = stmt.executeQuery("SELECT EMP_MST.EMP_ID,\n" +
//                    "         EMP_MST.EMP_NAME,\n" +
//                    "         EMP_JOB_HISTORY.JOB_CALLING_TITLE,\n" +
//                    "         JOB_SETUP_MST.JSM_NAME,\n" +
//                    "         DIVISION_MST.DIVM_NAME\n" +
//                    "    FROM EMP_MST,\n" +
//                    "         EMP_JOB_HISTORY,\n" +
//                    "         JOB_SETUP_DTL,\n" +
//                    "         JOB_SETUP_MST,\n" +
//                    "         DESIG_MST,\n" +
//                    "         DIVISION_MST\n" +
//                    "   WHERE     EMP_JOB_HISTORY.JOB_STATUS NOT IN ('Closed', 'Suspended')\n" +
//                    "         AND (JOB_SETUP_DTL.JSD_JSM_ID = JOB_SETUP_MST.JSM_ID)\n" +
//                    "         AND (JOB_SETUP_MST.JSM_DESIG_ID = DESIG_MST.DESIG_ID)\n" +
//                    "         AND (EMP_JOB_HISTORY.JOB_ID = EMP_MST.EMP_JOB_ID)\n" +
//                    "         AND (JOB_SETUP_DTL.JSD_ID = EMP_JOB_HISTORY.JOB_JSD_ID)\n" +
//                    "         AND EMP_JOB_HISTORY.JOB_EMAIL IS NOT NULL\n" +
//                    "         AND JOB_SETUP_MST.JSM_DIVM_ID = DIVISION_MST.DIVM_ID\n" +
//                    "         AND DESIG_MST.DESIG_PRIORITY IN\n" +
//                    "                (    SELECT DISTINCT REGEXP_SUBSTR (LAH_APPROVAL_BAND,\n" +
//                    "                                                    '[^,]+',\n" +
//                    "                                                    1,\n" +
//                    "                                                    LEVEL)\n" +
//                    "                       FROM (SELECT LAH_APPROVAL_BAND\n" +
//                    "                               FROM LEAVE_APPROVAL_HIERARCHY\n" +
//                    "                              WHERE LAH_BAND_NO =\n" +
//                    "                                       (SELECT DESIG_MST.DESIG_PRIORITY\n" +
//                    "                                          FROM EMP_MST,\n" +
//                    "                                               EMP_JOB_HISTORY,\n" +
//                    "                                               JOB_SETUP_DTL,\n" +
//                    "                                               JOB_SETUP_MST,\n" +
//                    "                                               DESIG_MST\n" +
//                    "                                         WHERE     EMP_MST.EMP_ID =\n" +
//                    "                                                      "+emp_id+"\n" +
//                    "                                               AND (JOB_SETUP_DTL.JSD_JSM_ID =\n" +
//                    "                                                       JOB_SETUP_MST.JSM_ID)\n" +
//                    "                                               AND (JOB_SETUP_MST.JSM_DESIG_ID =\n" +
//                    "                                                       DESIG_MST.DESIG_ID)\n" +
//                    "                                               AND (EMP_JOB_HISTORY.JOB_ID =\n" +
//                    "                                                       EMP_MST.EMP_JOB_ID)\n" +
//                    "                                               AND (JOB_SETUP_DTL.JSD_ID =\n" +
//                    "                                                       EMP_JOB_HISTORY.JOB_JSD_ID)))\n" +
//                    "                 CONNECT BY REGEXP_SUBSTR (LAH_APPROVAL_BAND,\n" +
//                    "                                           '[^,]+',\n" +
//                    "                                           1,\n" +
//                    "                                           LEVEL)\n" +
//                    "                               IS NOT NULL)\n" +
//                    "ORDER BY DIVISION_MST.DIVM_NAME");
//
//
//            while (rs2.next()) {
//
//                //approverWithoutDivision.add(new ApproverList(rs2.getString(1),rs2.getString(2)+",\n("+rs2.getString(4)+", "+rs2.getString(5)+")"));
//                allApproverWithoutDiv.add(new SelectAllList(rs2.getString(1),rs2.getString(2),rs2.getString(3),rs2.getString(4),rs2.getString(5)));
//
//
//            }
//
//            // Approver Employee
//            ResultSet rs3 = stmt.executeQuery("SELECT EMP_MST.EMP_ID,\n" +
//                    "       EMP_MST.EMP_NAME,\n" +
//                    "       EMP_JOB_HISTORY.JOB_CALLING_TITLE,\n" +
//                    "       JOB_SETUP_MST.JSM_NAME,\n" +
//                    "       DIVISION_MST.DIVM_NAME\n" +
//                    "  FROM EMP_MST,\n" +
//                    "       EMP_JOB_HISTORY,\n" +
//                    "       JOB_SETUP_DTL,\n" +
//                    "       JOB_SETUP_MST,\n" +
//                    "       DESIG_MST,\n" +
//                    "       DIVISION_MST\n" +
//                    " WHERE     EMP_JOB_HISTORY.JOB_STATUS NOT IN ('Closed', 'Suspended')\n" +
//                    "       AND (JOB_SETUP_DTL.JSD_JSM_ID = JOB_SETUP_MST.JSM_ID)\n" +
//                    "       AND (JOB_SETUP_MST.JSM_DESIG_ID = DESIG_MST.DESIG_ID)\n" +
//                    "       AND (EMP_JOB_HISTORY.JOB_ID = EMP_MST.EMP_JOB_ID)\n" +
//                    "       AND (JOB_SETUP_DTL.JSD_ID = EMP_JOB_HISTORY.JOB_JSD_ID)\n" +
//                    "       AND EMP_JOB_HISTORY.JOB_EMAIL IS NOT NULL\n" +
//                    "       AND JOB_SETUP_MST.JSM_DIVM_ID = DIVISION_MST.DIVM_ID\n" +
//                    "       AND EMP_MST.EMP_ID <> "+emp_id+"\n" +
//                    "       AND EMP_MST.EMP_CODE IN\n" +
//                    "              (    SELECT DISTINCT REGEXP_SUBSTR (LAH_SP_APPROVAL_CODE,\n" +
//                    "                                                  '[^,]+',\n" +
//                    "                                                  1,\n" +
//                    "                                                  LEVEL)\n" +
//                    "                     FROM (SELECT LAH_SP_APPROVAL_CODE\n" +
//                    "                             FROM LEAVE_APPROVAL_HIERARCHY\n" +
//                    "                            WHERE LAH_BAND_NO =\n" +
//                    "                                     (SELECT DESIG_MST.DESIG_PRIORITY\n" +
//                    "                                        FROM EMP_MST,\n" +
//                    "                                             EMP_JOB_HISTORY,\n" +
//                    "                                             JOB_SETUP_DTL,\n" +
//                    "                                             JOB_SETUP_MST,\n" +
//                    "                                             DESIG_MST\n" +
//                    "                                       WHERE     EMP_MST.EMP_ID =\n" +
//                    "                                                    "+emp_id+"\n" +
//                    "                                             AND (JOB_SETUP_DTL.JSD_JSM_ID =\n" +
//                    "                                                     JOB_SETUP_MST.JSM_ID)\n" +
//                    "                                             AND (JOB_SETUP_MST.JSM_DESIG_ID =\n" +
//                    "                                                     DESIG_MST.DESIG_ID)\n" +
//                    "                                             AND (EMP_JOB_HISTORY.JOB_ID =\n" +
//                    "                                                     EMP_MST.EMP_JOB_ID)\n" +
//                    "                                             AND (JOB_SETUP_DTL.JSD_ID =\n" +
//                    "                                                     EMP_JOB_HISTORY.JOB_JSD_ID)))\n" +
//                    "               CONNECT BY REGEXP_SUBSTR (LAH_SP_APPROVAL_CODE,\n" +
//                    "                                         '[^,]+',\n" +
//                    "                                         1,\n" +
//                    "                                         LEVEL)\n" +
//                    "                             IS NOT NULL)");
//
//            while (rs3.next()) {
//
//                //approverEmployee.add(new ApproverList(rs3.getString(1),rs3.getString(2)+",\n("+rs3.getString(4)+",\n"+rs3.getString(5)+")"));
//                allApproverEmp.add(new SelectAllList(rs3.getString(1),rs3.getString(2),rs3.getString(3),rs3.getString(4),rs3.getString(5)));
//
//            }
//
//            ResultSet approver1 = stmt.executeQuery("  SELECT DESIG_MST.DESIG_PRIORITY,JOB_SETUP_MST.JSM_DIVM_ID\n" +
//                    "  --INTO V_DESIG_PRIORITY,V_JSM_DIVM_ID\n" +
//                    "  FROM EMP_MST, EMP_JOB_HISTORY,  JOB_SETUP_DTL, JOB_SETUP_MST, DESIG_MST\n" +
//                    "  WHERE EMP_MST.EMP_ID="+emp_id+"\n" +
//                    "  AND (JOB_SETUP_DTL.JSD_JSM_ID = JOB_SETUP_MST.JSM_ID)\n" +
//                    "  AND (JOB_SETUP_MST.JSM_DESIG_ID = DESIG_MST.DESIG_ID)\n" +
//                    "  AND (EMP_JOB_HISTORY.JOB_ID = EMP_MST.EMP_JOB_ID)\n" +
//                    "  AND (JOB_SETUP_DTL.JSD_ID = EMP_JOB_HISTORY.JOB_JSD_ID)");
//            while (approver1.next()) {
//                desig_priority = approver1.getString(1);
//                divm_id = approver1.getString(2);
//            }
//            System.out.println("Found App1");
//
//            if (!desig_priority.isEmpty()) {
//
//                ResultSet approver2 = stmt.executeQuery("SELECT LAH_APPROVAL_BAND --INTO V_APPROVAL_BAND\n" +
//                        "    FROM LEAVE_APPROVAL_HIERARCHY\n" +
//                        "    WHERE LAH_BAND_NO="+desig_priority+"");
//
//                while (approver2.next()) {
//                    approval_band = approver2.getString(1);
//                }
//                System.out.println("Found App2");
//
//                if (!approval_band.isEmpty()) {
//
//                    ResultSet approver3 = stmt.executeQuery(" SELECT COUNT(1) --INTO V_COUNT_APPROVAL_EMP   -- return minimum 1 if at least exist  otherwise return 0\n" +
//                            "    FROM EMP_MST, EMP_JOB_HISTORY,  JOB_SETUP_DTL, JOB_SETUP_MST, DESIG_MST\n" +
//                            "    WHERE JOB_SETUP_MST.JSM_DIVM_ID="+divm_id+"\n" +
//                            "    AND EMP_JOB_HISTORY.JOB_STATUS NOT IN('Closed','Suspended')\n" +
//                            "    AND (JOB_SETUP_DTL.JSD_JSM_ID = JOB_SETUP_MST.JSM_ID)\n" +
//                            "    AND (JOB_SETUP_MST.JSM_DESIG_ID = DESIG_MST.DESIG_ID)\n" +
//                            "    AND (EMP_JOB_HISTORY.JOB_ID = EMP_MST.EMP_JOB_ID)\n" +
//                            "    AND (JOB_SETUP_DTL.JSD_ID = EMP_JOB_HISTORY.JOB_JSD_ID)\n" +
//                            "    AND EMP_JOB_HISTORY.JOB_EMAIL IS NOT NULL\n" +
//                            "    AND DESIG_MST.DESIG_PRIORITY IN (SELECT DISTINCT REGEXP_SUBSTR(LAH_APPROVAL_BAND,'[^,]+', 1, LEVEL) \n" +
//                            "                                      FROM (SELECT LAH_APPROVAL_BAND\n" +
//                            "                                      FROM LEAVE_APPROVAL_HIERARCHY\n" +
//                            "                                      WHERE LAH_BAND_NO="+desig_priority+")\n" +
//                            "                                      connect by regexp_substr(LAH_APPROVAL_BAND, '[^,]+', 1, level) is not null)  ");
//
//                    while (approver3.next()) {
//                        count_approv_emp = Integer.parseInt(approver3.getString(1));
//                    }
//                    System.out.println("Found App3");
//
//                    if (count_approv_emp <= 0) {
//
//                        ResultSet approver4 = stmt.executeQuery("SELECT COUNT(1) --INTO V_COUNT_APPROVAL_EMP   -- return minimum 1 if at least exist  otherwise return 0\n" +
//                                "      FROM EMP_MST, EMP_JOB_HISTORY\n" +
//                                "      WHERE (EMP_JOB_HISTORY.JOB_ID = EMP_MST.EMP_JOB_ID)\n" +
//                                "      AND EMP_JOB_HISTORY.JOB_EMAIL IS NOT NULL\n" +
//                                "      AND EMP_MST.EMP_CODE IN (SELECT DISTINCT REGEXP_SUBSTR(LAH_SP_APPROVAL_CODE,'[^,]+', 1, LEVEL) \n" +
//                                "                                        FROM (SELECT LAH_SP_APPROVAL_CODE\n" +
//                                "                                        FROM LEAVE_APPROVAL_HIERARCHY\n" +
//                                "                                        WHERE LAH_BAND_NO="+desig_priority+")\n" +
//                                "                                        CONNECT BY REGEXP_SUBSTR(LAH_SP_APPROVAL_CODE, '[^,]+', 1, LEVEL) IS NOT NULL)");
//
//                        while (approver4.next()) {
//                            count_approv_emp = Integer.parseInt(approver4.getString(1));
//                        }
//                        System.out.println("Found App4");
//
//                        if (count_approv_emp <= 0) {
//                            //selectedApproverList = approverWithoutDivision;
//                            allSelectedApprover = allApproverWithoutDiv;
//                        } else {
//                            // selectedApproverList = approverEmployee;
//                            allSelectedApprover = allApproverEmp;
//                        }
//                    } else {
//                        //selectedApproverList = approverDivision;
//                        allSelectedApprover = allApproverDivision;
//                    }
//
//                }
//
//            }
//
//            if (allSelectedApprover.size() != 0) {
//                for (int i = 0; i<allSelectedApprover.size(); i++) {
//                    forwardEMPLists.add(new ForwardEMPList(allSelectedApprover.get(i).getId(),allSelectedApprover.get(i).getFirst(),allSelectedApprover.get(i).getSecond(),allSelectedApprover.get(i).getThird(),allSelectedApprover.get(i).getFourth()));
//                }
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
//            e.printStackTrace();
//        }
//    }

    // getting forward to employee list
    public void getForwardToEmp() {
        waitProgress.show(activity.getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;

        forwardEMPLists = new ArrayList<>();
        allApproverDivision = new ArrayList<>();
        allApproverEmp = new ArrayList<>();
        allApproverWithoutDiv = new ArrayList<>();
        allSelectedApprover = new ArrayList<>();

        desig_priority = "";
        divm_id = "";
        approval_band = "";
        count_approv_emp = 0;

        String approverDivUrl = "http://103.56.208.123:8001/apex/ttrams/forwardReq/attReqApproverWithDiv/"+emp_id+"";
        String appWithoutDivUrl  = "http://103.56.208.123:8001/apex/ttrams/forwardReq/attReqApproverWithoutDiv/"+emp_id+"";
        String allapproverUrl = "http://103.56.208.123:8001/apex/ttrams/forwardReq/attReqAllApprover/"+emp_id+"";
        String desigPriorUrl = "http://103.56.208.123:8001/apex/ttrams/forwardReq/getDesigPriority/"+emp_id+"";

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        StringRequest desigPriorReq = new StringRequest(Request.Method.GET, desigPriorUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject desigPrInfo = array.getJSONObject(i);
                        desig_priority  = desigPrInfo.getString("desig_priority")
                                .equals("null") ? "" : desigPrInfo.getString("desig_priority");
                        divm_id = desigPrInfo.getString("jsm_divm_id")
                                .equals("null") ? "" : desigPrInfo.getString("jsm_divm_id");

                        System.out.println("designation1: " + desig_priority);
                    }
                }

                if (!desig_priority.isEmpty()) {
                    System.out.println("designation2: " + desig_priority);
                    getForwarderList();
                }
                else {
                    if (allSelectedApprover.size() != 0) {
                        for (int i = 0; i<allSelectedApprover.size(); i++) {
                            forwardEMPLists.add(new ForwardEMPList(allSelectedApprover.get(i).getId(),allSelectedApprover.get(i).getFirst(),allSelectedApprover.get(i).getSecond(),allSelectedApprover.get(i).getThird(),allSelectedApprover.get(i).getFourth()));
                        }
                    }
                    connected = true;
                    updateInfo();
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                updateInfo();
            }
        }, error -> {
            error.printStackTrace();
            conn = false;
            connected = false;
            updateInfo();
        });

        StringRequest allAppReq = new StringRequest(Request.Method.GET, allapproverUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject allAppInfo = array.getJSONObject(i);

                        String emp_id_new = allAppInfo.getString("emp_id");

                        String emp_name = allAppInfo.getString("emp_name")
                                .equals("null") ? "" : allAppInfo.getString("emp_name");
                        String job_calling_title = allAppInfo.getString("job_calling_title")
                                .equals("null") ? "" : allAppInfo.getString("job_calling_title");
                        String jsm_name = allAppInfo.getString("jsm_name")
                                .equals("null") ? "" : allAppInfo.getString("jsm_name");
                        String divm_name = allAppInfo.getString("divm_name")
                                .equals("null") ? "" : allAppInfo.getString("divm_name");

                        allApproverEmp.add(new SelectAllList(emp_id_new,emp_name,job_calling_title,jsm_name,divm_name));
                    }
                }

                requestQueue.add(desigPriorReq);
            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                updateInfo();
            }
        }, error -> {
            error.printStackTrace();
            conn = false;
            connected = false;
            updateInfo();
        });

        StringRequest appWithoutDivReq = new StringRequest(Request.Method.GET, appWithoutDivUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject appWithoutDivInfo = array.getJSONObject(i);

                        String emp_id_new = appWithoutDivInfo.getString("emp_id");

                        String emp_name = appWithoutDivInfo.getString("emp_name")
                                .equals("null") ? "" : appWithoutDivInfo.getString("emp_name");
                        String job_calling_title = appWithoutDivInfo.getString("job_calling_title")
                                .equals("null") ? "" : appWithoutDivInfo.getString("job_calling_title");
                        String jsm_name = appWithoutDivInfo.getString("jsm_name")
                                .equals("null") ? "" : appWithoutDivInfo.getString("jsm_name");
                        String divm_name = appWithoutDivInfo.getString("divm_name")
                                .equals("null") ? "" : appWithoutDivInfo.getString("divm_name");

                        allApproverWithoutDiv.add(new SelectAllList(emp_id_new,emp_name,job_calling_title,jsm_name,divm_name));

                    }
                }

                requestQueue.add(allAppReq);
            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                updateInfo();
            }
        }, error -> {
            error.printStackTrace();
            conn = false;
            connected = false;
            updateInfo();
        });

        StringRequest appDivReq = new StringRequest(Request.Method.GET, approverDivUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject appDivInfo = array.getJSONObject(i);
                        String emp_id_new = appDivInfo.getString("emp_id");

                        String emp_name = appDivInfo.getString("emp_name")
                                .equals("null") ? "" : appDivInfo.getString("emp_name");
                        String job_calling_title = appDivInfo.getString("job_calling_title")
                                .equals("null") ? "" : appDivInfo.getString("job_calling_title");
                        String jsm_name = appDivInfo.getString("jsm_name")
                                .equals("null") ? "" : appDivInfo.getString("jsm_name");
                        String divm_name = appDivInfo.getString("divm_name")
                                .equals("null") ? "" : appDivInfo.getString("divm_name");

                        allApproverDivision.add(new SelectAllList(emp_id_new,emp_name,job_calling_title,jsm_name,divm_name));
                    }
                }
                requestQueue.add(appWithoutDivReq);
            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                updateInfo();
            }
        }, error -> {
            error.printStackTrace();
            conn = false;
            connected = false;
            updateInfo();
        });

        requestQueue.add(appDivReq);
    }

    public void getForwarderList() {

        String approvalBandUrl = "http://103.56.208.123:8001/apex/ttrams/forwardReq/getApprovalBand/"+desig_priority+"";
        String countApp1Url = "http://103.56.208.123:8001/apex/ttrams/forwardReq/getCountApprovEmp/"+divm_id+"/"+desig_priority+"";
        String countApp2Url = "http://103.56.208.123:8001/apex/ttrams/forwardReq/getCountApprovEmp_2/"+desig_priority+"";

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        StringRequest countApp2Req = new StringRequest(Request.Method.GET, countApp2Url, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject cApp2Info = array.getJSONObject(i);
                        count_approv_emp = cApp2Info.getInt("cc2");
                    }
                }

                if (count_approv_emp <= 0) {
                    //selectedApproverList = approverWithoutDivision;
                    allSelectedApprover = allApproverWithoutDiv;
                } else {
                    // selectedApproverList = approverEmployee;
                    allSelectedApprover = allApproverEmp;
                }

                if (allSelectedApprover.size() != 0) {
                    for (int i = 0; i<allSelectedApprover.size(); i++) {
                        forwardEMPLists.add(new ForwardEMPList(allSelectedApprover.get(i).getId(),allSelectedApprover.get(i).getFirst(),allSelectedApprover.get(i).getSecond(),allSelectedApprover.get(i).getThird(),allSelectedApprover.get(i).getFourth()));
                    }
                }
                connected = true;
                updateInfo();

            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                updateInfo();
            }
        }, error -> {
            error.printStackTrace();
            conn = false;
            connected = false;
            updateInfo();
        });

        StringRequest countApp1Req = new StringRequest(Request.Method.GET, countApp1Url, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject cApp1Info = array.getJSONObject(i);
                        count_approv_emp = cApp1Info.getInt("cc");
                    }
                }
                if (count_approv_emp <= 0) {
                    requestQueue.add(countApp2Req);
                }
                else {
                    allSelectedApprover = allApproverDivision;
                    if (allSelectedApprover.size() != 0) {
                        for (int i = 0; i<allSelectedApprover.size(); i++) {
                            forwardEMPLists.add(new ForwardEMPList(allSelectedApprover.get(i).getId(),allSelectedApprover.get(i).getFirst(),allSelectedApprover.get(i).getSecond(),allSelectedApprover.get(i).getThird(),allSelectedApprover.get(i).getFourth()));
                        }
                    }
                    connected = true;
                    updateInfo();
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                updateInfo();
            }
        }, error -> {
            error.printStackTrace();
            conn = false;
            connected = false;
            updateInfo();
        });

        StringRequest appBandReq = new StringRequest(Request.Method.GET, approvalBandUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject appBandInfo = array.getJSONObject(i);
                        approval_band = appBandInfo.getString("lah_approval_band")
                                .equals("null") ? "" : appBandInfo.getString("lah_approval_band");

                    }
                }
                if (!approval_band.isEmpty()) {
                    requestQueue.add(countApp1Req);
                }
                else {
                    if (allSelectedApprover.size() != 0) {
                        for (int i = 0; i<allSelectedApprover.size(); i++) {
                            forwardEMPLists.add(new ForwardEMPList(allSelectedApprover.get(i).getId(),allSelectedApprover.get(i).getFirst(),allSelectedApprover.get(i).getSecond(),allSelectedApprover.get(i).getThird(),allSelectedApprover.get(i).getFourth()));
                        }
                    }
                    connected = true;
                    updateInfo();
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                updateInfo();
            }
        }, error -> {
            error.printStackTrace();
            conn = false;
            connected = false;
            updateInfo();
        });

        requestQueue.add(appBandReq);
    }

    private void updateInfo() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                if (forwardEMPLists.size() == 0) {
                    noEmp.setVisibility(View.VISIBLE);
                } else {
                    noEmp.setVisibility(View.GONE);
                    forwardAdapter = new ForwardAdapter(forwardEMPLists, getContext(), ForwardDialogue.this);
                    empListView.setAdapter(forwardAdapter);
                    forwardAdapter.notifyDataSetChanged();
                }
                conn = false;
                connected = false;
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

                    getForwardToEmp();
                    dialog.dismiss();
                });

                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negative.setOnClickListener(v -> {
                    dialog.dismiss();
                    forwarDialog.dismiss();
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

                getForwardToEmp();
                dialog.dismiss();
            });

            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> {
                dialog.dismiss();
                forwarDialog.dismiss();
            });
        }
    }

//    public void ForwardedQuery() {
//        try {
//            this.connection = createConnection();
//
//            Statement stmt = connection.createStatement();
//
//
//            dard_id = "";
//            req_status_count = 0;
//
//
//            ResultSet rs=stmt.executeQuery("SELECT NVL (MAX (DARD_ID), 0) + 1 DARD_ID\n" +
//                    "FROM DAILY_ATTEN_REQ_DTL");
//
//
//
//            while(rs.next())  {
//                dard_id = rs.getString(1);
//            }
//
//            ResultSet resultSet = stmt.executeQuery("SELECT COUNT (1) req_status_count\n" +
//                    "     FROM DAILY_ATTEN_REQ_MST\n" +
//                    "     WHERE DARM_ID = "+darm_id+"\n" +
//                    "          AND NVL (DARM_APPROVED, 0) = 0");
//
//            while (resultSet.next()) {
//                req_status_count = resultSet.getInt(1);
//            }
//
//            if (req_status_count >= 1) {
//                if (dard_id != null) {
//                    stmt.executeUpdate("INSERT INTO DAILY_ATTEN_REQ_DTL ( DARD_ID, DARD_DARM_ID, DARD_FORWADER_ID, DARD_FORWARD_TO_ID, DARD_RECOMMENDATION) \n" +
//                            "    VALUES ("+dard_id+","+darm_id+", "+emp_id+", "+forward_to_id+", '"+forward_comm+"')");
//
//
//                    stmt.executeUpdate("UPDATE DAILY_ATTEN_APP_HIERARCHY_LOG\n" +
//                            "    SET DAAHL_APPROVER_BAND_ID = "+forward_to_id+"\n" +
//                            "    WHERE DAAHL_APP_CODE = '"+req_code+"'");
//
//                    isForwardExe = true;
//
//                }
//            }
//
//
//
//            isForwarded = true;
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
//    }

    // forward attendance update request
    public void forwardAttReq() {
        waitProgress.show(activity.getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        ffffoooorrrwww = false;
        isForwardExe = false;
        isForwarded = false;

        String forwardAttReqUrl = "http://103.56.208.123:8001/apex/ttrams/forwardReq/forwardAttReq";

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        StringRequest forwardAtt = new StringRequest(Request.Method.POST, forwardAttReqUrl, response -> {
            ffffoooorrrwww = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String string_out = jsonObject.getString("string_out");
                String updated_req = jsonObject.getString("updated_req");
                if (string_out.equals("Successfully Created")) {
                    isForwarded = true;
                    isForwardExe = updated_req.equals("true");
                }
                else {
                    System.out.println(string_out);
                    isForwarded = false;
                }
                updateLayout();
            }
            catch (JSONException e) {
                e.printStackTrace();
                isForwarded = false;
                updateLayout();
            }
        }, error -> {
            error.printStackTrace();
            ffffoooorrrwww = false;
            isForwarded = false;
            updateLayout();
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("P_DARM_ID",darm_id);
                headers.put("P_EMP_ID",emp_id);
                headers.put("P_FORWARD_COMM",forward_comm);
                headers.put("P_FORWARD_TO_ID",forward_to_id);
                headers.put("P_REQ_CODE",req_code);
                return headers;
            }
        };

        requestQueue.add(forwardAtt);

    }

    private void updateLayout() {
        waitProgress.dismiss();
        if (ffffoooorrrwww) {
            if (isForwarded) {
                if (isForwardExe) {
                    req_code = "";
                    darm_id = "";
                    AttendanceApprove.darm_emp_id = "";
                    AttendanceApprove.selectApproveReqLists = new ArrayList<>();
                    forwardFromAtt = 0;
                    System.out.println("INSERTED");
                    forwarDialog.dismiss();
                    AlertDialog dialog1 = new AlertDialog.Builder(activity)
                            .setMessage("Request Forwarded Successfully")
                            .setPositiveButton("OK", null)
                            .show();

                    dialog1.setCancelable(false);
                    dialog1.setCanceledOnTouchOutside(false);
                    Button positive = dialog1.getButton(AlertDialog.BUTTON_POSITIVE);
                    positive.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            dialog1.dismiss();
                            ((Activity)mContext).finish();

                        }
                    });

                }
                else {
                    Toast.makeText(getContext(), "Already Updated by Another User", Toast.LENGTH_SHORT).show();
                }
                ffffoooorrrwww = false;
                isForwardExe = false;
                isForwarded = false;
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(activity)
                        .setMessage("There is a network issue in the server. Please Try later.")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();

                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        forwardAttReq();
                        dialog.dismiss();
                    }
                });
            }
        }
        else {
            AlertDialog dialog = new AlertDialog.Builder(activity)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel",null)
                    .show();

            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    forwardAttReq();
                    dialog.dismiss();
                }
            });
        }
    }

//    public void ForwardedQueryLeave() {
//        try {
//            this.connection = createConnection();
//
//            Statement stmt = connection.createStatement();
//
//
//            lad_id = "";
//            req_status_count_leave = 0;
//
//
//            ResultSet rs=stmt.executeQuery("SELECT NVL (MAX (LAD_ID), 0) + 1 LAD_ID\n" +
//                    "    FROM LEAVE_APPLICATION_DTL");
//
//
//
//            while(rs.next())  {
//                lad_id = rs.getString(1);
//            }
//
//            ResultSet resultSet = stmt.executeQuery("SELECT COUNT (1) req_status_count\n" +
//                    "                         FROM LEAVE_APPLICATION\n" +
//                    "                         WHERE LA_ID = "+la_id+"\n" +
//                    "                              AND NVL (LA_APPROVED, 0) = 0");
//
//            while (resultSet.next()) {
//                req_status_count_leave = resultSet.getInt(1);
//            }
//
//            if (req_status_count_leave >= 1) {
//                if (lad_id != null) {
//
//                    stmt.executeUpdate("INSERT INTO LEAVE_APPLICATION_DTL ( LAD_ID, LAD_LA_ID, LAD_FORWADER_ID,\n" +
//                            "                            LAD_FORWARD_TO_ID, LAD_RECOMMENDATION) \n" +
//                            "    VALUES ("+lad_id+","+la_id+", "+emp_id+", "+forward_to_id+", '"+forward_comm+"')");
//
//
//                    isForwardExeLeave = true;
//
//                }
//            }
//
//
//
//            isForwardedLeave = true;
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
//    }

    // forward leave request
    public void forwardLeaveReq() {
        waitProgress.show(activity.getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        ffffoooorrrwwwllll = false;
        isForwardExeLeave = false;
        isForwardedLeave = false;

        String forwardAttReqUrl = "http://103.56.208.123:8001/apex/ttrams/forwardReq/forwardLeaveReq";

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        StringRequest forLeaveReq = new StringRequest(Request.Method.POST, forwardAttReqUrl, response -> {
            ffffoooorrrwwwllll = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String string_out = jsonObject.getString("string_out");
                String updated_req = jsonObject.getString("updated_req");
                if (string_out.equals("Successfully Created")) {
                    isForwardedLeave = true;
                    isForwardExeLeave = updated_req.equals("true");
                }
                else {
                    System.out.println(string_out);
                    isForwardedLeave = false;
                }
                updateLayoutLeave();
            }
            catch (JSONException e) {
                e.printStackTrace();
                isForwardedLeave = false;
                updateLayoutLeave();
            }
        }, error -> {
            error.printStackTrace();
            ffffoooorrrwwwllll = false;
            isForwardedLeave = false;
            updateLayoutLeave();
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("P_LA_ID",la_id);
                headers.put("P_EMP_ID",emp_id);
                headers.put("P_FORWARD_COMM",forward_comm);
                headers.put("P_FORWARD_TO_ID",forward_to_id);
                return headers;
            }
        };

        requestQueue.add(forLeaveReq);
    }

    private void updateLayoutLeave() {
        waitProgress.dismiss();
        if (ffffoooorrrwwwllll) {
            if (isForwardedLeave) {
                if (isForwardExeLeave) {
                    req_code_leave = "";
                    la_id = "";
                    LeaveApprove.la_emp_id = "";
                    LeaveApprove.leaveReqList = new ArrayList<>();
                    forwardFromLeave = 0;
                    System.out.println("INSERTED");

                    forwarDialog.dismiss();

                    AlertDialog dialog1 = new AlertDialog.Builder(activity)
                            .setMessage("Leave Application Forwarded Successfully")
                            .setPositiveButton("OK", null)
                            .show();

                    dialog1.setCancelable(false);
                    dialog1.setCanceledOnTouchOutside(false);
                    Button positive = dialog1.getButton(AlertDialog.BUTTON_POSITIVE);
                    positive.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            dialog1.dismiss();
                            ((Activity)mContext).finish();

                        }
                    });

                }
                else {
                    Toast.makeText(getContext(), "Already Updated by Another User", Toast.LENGTH_SHORT).show();
                }
                ffffoooorrrwwwllll = false;
                isForwardExeLeave = false;
                isForwardedLeave = false;
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(activity)
                        .setMessage("There is a network issue in the server. Please Try later.")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();

                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        forwardLeaveReq();
                        dialog.dismiss();
                    }
                });
            }
        }
        else {
            AlertDialog dialog = new AlertDialog.Builder(activity)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel",null)
                    .show();

            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    forwardLeaveReq();
                    dialog.dismiss();
                }
            });
        }
    }

}
