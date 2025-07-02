package ttit.com.shuvo.ikglhrm.leaveAll.leaveApprove;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import ttit.com.shuvo.ikglhrm.R;
import ttit.com.shuvo.ikglhrm.WaitProgress;
import ttit.com.shuvo.ikglhrm.attendance.approve.dialogApproveReq.ForwardDialogue;
import ttit.com.shuvo.ikglhrm.attendance.approve.dialogApproveReq.SelectApproveReq;
import ttit.com.shuvo.ikglhrm.attendance.approve.dialogApproveReq.SelectApproveReqList;
import ttit.com.shuvo.ikglhrm.attendance.update.dialogue.DialogueText;
import ttit.com.shuvo.ikglhrm.leaveAll.leaveApprove.leaveAppDialogues.ForwardHistoryDial;
import ttit.com.shuvo.ikglhrm.leaveAll.leaveApprove.leaveAppDialogues.ForwardHistoryList;

import static ttit.com.shuvo.ikglhrm.Login.userInfoLists;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.api_url_front;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LeaveApprove extends AppCompatActivity {

    public static int number = 0;
    public static String hintLA = "";
    public static String textLA = "";

    public static int forwardFromLeave = 0;

    public static int fromLApp = 0;

    CardView afterSelecting;
    LinearLayout afterSelectingButton;
    LinearLayout forLay;

    public static TextInputLayout commentsLay;

    TextInputEditText name;
    TextInputEditText empCode;
    TextInputEditText appDate;
    TextInputEditText title;
    TextInputEditText leaveType;
    TextInputEditText leaveBalance;
    TextInputEditText fromDate;
    TextInputEditText toDate;
    TextInputEditText totalDays;
    TextInputEditText reason;
    public static TextInputEditText comments;

    Button approve;
    Button forward;
    Button reject;
    MaterialButton fh;

    String emp_name = "";
    String emp_id = "";
    String app_date = "";
    String call_title = "";
    String lc_id = "";
    String leave_type = "";
    String leave_bal = "";
    String from_date = "";
    String to_date = "";
    String total_day = "";
    String reason_desc = "";
//    String comm = "";

    String sl_check = "0";
    String approveSuccess = "";
    String rejectSuccess = "";

    WaitProgress waitProgress = new WaitProgress();

    private Boolean conn = false;
    private Boolean connected = false;

    private Boolean dataIn = false;
    private Boolean inDataaa = false;

    private Boolean isApprovedd = false;
    private Boolean isApprovedChecked = false;
    private Boolean appppppprrrrr = false;

    private Boolean isRejected = false;
    private Boolean isRejectedChecked = false;
    private Boolean rrreeejjjeecctt = false;
    
    String emp_code = "";
    String user_id = "";
    public static String req_code_leave = "";
    public static String la_id = "";
    public static String la_emp_id = "";

    public static TextInputEditText requestCodeLeave;

    public static ArrayList<SelectApproveReqList> leaveReqList;

    public static ArrayList<ForwardHistoryList> forwardHistoryLists;

    Logger logger = Logger.getLogger(LeaveApprove.class.getName());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_approve);

        afterSelecting = findViewById(R.id.after_request_selecting_leave_approve);
        afterSelectingButton = findViewById(R.id.button_visiblity_leave_lay);
        forLay = findViewById(R.id.forward_layout_leave);

        requestCodeLeave = findViewById(R.id.request_code_leave_approve);
        name = findViewById(R.id.name_leave_approve);
        empCode = findViewById(R.id.id_leave_approve);
        appDate = findViewById(R.id.now_date_leave_approve);
        title = findViewById(R.id.calling_title_leave_approve);
        leaveType = findViewById(R.id.leave_type_leave_approve);
        leaveBalance = findViewById(R.id.leave_balance_leave_approve);
        fromDate = findViewById(R.id.from_date_leave_approve);
        toDate = findViewById(R.id.to_date_leave_approve);
        totalDays = findViewById(R.id.total_days_leave_approve);
        reason = findViewById(R.id.reason_leave_approve);
        comments = findViewById(R.id.comments_given_leave_approve);
        commentsLay = findViewById(R.id.comments_given_layout_leave_approve);

        approve = findViewById(R.id.approve_button_leave);
        forward = findViewById(R.id.forward_button_leave);
        reject = findViewById(R.id.reject_button_leave);
        fh = findViewById(R.id.forward_history_button_leave);


        emp_code = userInfoLists.get(0).getUserName();
        user_id = userInfoLists.get(0).getEmp_id();

        leaveReqList = new ArrayList<>();
        forwardHistoryLists = new ArrayList<>();

//        new Check().execute();
        getReqLists();

        requestCodeLeave.setOnClickListener(v -> {
            fromLApp = 1;
            SelectApproveReq selectRequest = new SelectApproveReq();
            selectRequest.show(getSupportFragmentManager(),"Request");
        });

        requestCodeLeave.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                getReqData();
            }
        });

        fh.setOnClickListener(v -> {
            forwardFromLeave = 1;
            ForwardHistoryDial forwardHistoryDial = new ForwardHistoryDial();
            forwardHistoryDial.show(getSupportFragmentManager(),"Forward");
        });

        comments.setOnClickListener(v -> {
            number = 1;
            hintLA = Objects.requireNonNull(commentsLay.getHint()).toString();
            textLA = Objects.requireNonNull(comments.getText()).toString();
            DialogueText dialogueText = new DialogueText();
            dialogueText.show(getSupportFragmentManager(),"TEXTEDIT");
        });

        approve.setOnClickListener(v -> {
//                text = comments.getText().toString();
//                new ApproveCheck().execute();

            AlertDialog.Builder builder = new AlertDialog.Builder(LeaveApprove.this);
            builder.setTitle("Approve Leave!")
                    .setMessage("Do you want approve this leave?")
                    .setPositiveButton("YES", (dialog, which) -> {


                        // checking it is sick leave or not
                        if (lc_id.equals("2")) {
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(LeaveApprove.this);
                            builder1.setTitle("Prescription Check!")
                                    .setMessage("Did you checked prescription of the applicant?")
                                    .setPositiveButton("YES", (dialog1, which1) -> {


                                        sl_check = "1";
                                        textLA = Objects.requireNonNull(comments.getText()).toString();
//                                                    new ApproveCheck().execute();
                                        leaveApproveProcess();
                                    })
                                    .setNegativeButton("NO", (dialog12, which12) -> {
                                        sl_check = "0";
                                        textLA = Objects.requireNonNull(comments.getText()).toString();
//                                                    new ApproveCheck().execute();
                                        leaveApproveProcess();
                                    });
                            AlertDialog alert = builder1.create();
                            alert.show();

                        } else {

                            textLA = Objects.requireNonNull(comments.getText()).toString();
//                                    new ApproveCheck().execute();
                            leaveApproveProcess();
                        }

                    })
                    .setNegativeButton("NO", (dialog, which) -> {

                    });
            AlertDialog alert = builder.create();
            alert.show();
        });

        forward.setOnClickListener(v -> {
            forwardFromLeave = 1;
            ForwardDialogue forwardDialogue = new ForwardDialogue(LeaveApprove.this);
            forwardDialogue.show(getSupportFragmentManager(),"FORWARD");
        });

        reject.setOnClickListener(v -> {
//                text = comments.getText().toString();
//                new ApproveCheck().execute();

            textLA = Objects.requireNonNull(comments.getText()).toString();

            if (textLA.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please mention reason", Toast.LENGTH_SHORT).show();
            } else {

                AlertDialog.Builder builder = new AlertDialog.Builder(LeaveApprove.this);
                builder.setTitle("Reject Leave!")
                        .setMessage("Do you want reject this leave?")
                        .setPositiveButton("YES", (dialog, which) -> {

//                                    new RejectCheck().execute();
                            leaveRejectProcess();
                        })
                        .setNegativeButton("NO", (dialog, which) -> {

                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                req_code_leave = "";
                la_id = "";
                la_emp_id = "";
                leaveReqList = new ArrayList<>();
                finish();
            }
        });
    }

//    public boolean isConnected() {
//        boolean connected = false;
//        boolean isMobile = false;
//        try {
//            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
//            @SuppressLint("MissingPermission") NetworkInfo nInfo = cm.getActiveNetworkInfo();
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
//                RequestList();
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
//                conn = false;
//                connected = false;
//
//
//            }
//            else {
//                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
//                AlertDialog dialog = new AlertDialog.Builder(LeaveApprove.this)
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
//                        finish();
//                    }
//                });
//            }
//        }
//    }

//    public class GettingData extends AsyncTask<Void, Void, Void> {
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
//                DataApprove();
//                if (dataIn) {
//                    inDataaa = true;
//                    message= "Internet Connected";
//                }
//
//            } else {
//                inDataaa = false;
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
//            if (inDataaa) {
//
//                if (emp_name == null) {
//                    name.setText("");
//                } else {
//                    name.setText(emp_name);
//                }
//
//                if (emp_id == null) {
//                    empCode.setText("");
//                } else {
//                    empCode.setText(emp_id);
//                }
//
//                if (app_date == null) {
//                    appDate.setText("");
//                } else {
//                    appDate.setText(app_date);
//                }
//
//                if (call_title == null) {
//                    title.setText("");
//                } else {
//                    title.setText(call_title);
//                }
//
//                if (leave_type == null) {
//                    leaveType.setText("");
//                } else {
//                    leaveType.setText(leave_type);
//                }
//
//                if (leave_bal == null) {
//                    leaveBalance.setText("");
//                } else {
//                    leaveBalance.setText(leave_bal);
//                }
//
//                if (from_date == null) {
//                    fromDate.setText("");
//                } else {
//                    fromDate.setText(from_date);
//                }
//
//                if (to_date == null) {
//                    toDate.setText("");
//                } else {
//                    toDate.setText(to_date);
//                }
//
//                if (total_day == null) {
//                    totalDays.setText("");
//                } else {
//                    totalDays.setText(total_day);
//                }
//
//                if (reason_desc == null) {
//                    reason.setText("");
//                } else {
//                    reason.setText(reason_desc);
//                }
//
//                if (forwardHistoryLists.size() == 0) {
//                    forLay.setVisibility(View.GONE);
//                } else {
//                    forLay.setVisibility(View.VISIBLE);
//                }
//
//                dataIn = false;
//                inDataaa = false;
//
//
//            }
//            else {
//                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
//                AlertDialog dialog = new AlertDialog.Builder(LeaveApprove.this)
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
//                        new GettingData().execute();
//                        dialog.dismiss();
//                    }
//                });
//                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
//                negative.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                        finish();
//                    }
//                });
//            }
//        }
//    }

//    public class ApproveCheck extends AsyncTask<Void, Void, Void> {
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
//                ApproveClicked();
//                if (isApprovedChecked) {
//                    appppppprrrrr = true;
//
//                }
//
//            } else {
//                appppppprrrrr = false;
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
//            if (appppppprrrrr) {
//
//
//
//                if (approveSuccess.equals("OK")) {
//                    req_code_leave = "";
//                    la_id = "";
//                    la_emp_id = "";
//                    leaveReqList = new ArrayList<>();
//                    System.out.println("INSERTED");
//
//                    AlertDialog dialog = new AlertDialog.Builder(LeaveApprove.this)
//                            .setMessage("Leave Approved Successfully")
//                            .setPositiveButton("OK", null)
//                            .show();
//
//                    dialog.setCancelable(false);
//                    dialog.setCanceledOnTouchOutside(false);
//                    Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
//                    positive.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//                            dialog.dismiss();
//                            finish();
//                        }
//                    });
//                } else {
//                    Toast.makeText(getApplicationContext(), approveSuccess, Toast.LENGTH_SHORT).show();
//                }
//
//
//                appppppprrrrr = false;
//                isApprovedd = false;
//                isApprovedChecked = false;
//
//
//            }
//            else {
//                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
//                AlertDialog dialog = new AlertDialog.Builder(LeaveApprove.this)
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
//                        new ApproveCheck().execute();
//                        dialog.dismiss();
//                    }
//                });
//            }
//        }
//    }

//    public class RejectCheck extends AsyncTask<Void, Void, Void> {
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
//                RejectClicked();
//                if (isRejectedChecked) {
//                    rrreeejjjeecctt = true;
//
//                }
//
//            } else {
//                rrreeejjjeecctt = false;
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
//            if (rrreeejjjeecctt) {
//
//
//                if (rejectSuccess.equals("OK")) {
//
//                    req_code_leave = "";
//                    la_id = "";
//                    la_emp_id = "";
//                    leaveReqList = new ArrayList<>();
//                    System.out.println("INSERTED");
//
//                    AlertDialog dialog = new AlertDialog.Builder(LeaveApprove.this)
//                            .setMessage("Leave Rejected Successfully")
//                            .setPositiveButton("OK", null)
//                            .show();
//
//                    dialog.setCancelable(false);
//                    dialog.setCanceledOnTouchOutside(false);
//                    Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
//                    positive.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//                            dialog.dismiss();
//                            finish();
//                        }
//                    });
//
//                } else {
//
//                    Toast.makeText(getApplicationContext(), rejectSuccess, Toast.LENGTH_SHORT).show();
//
//                }
//
//                rrreeejjjeecctt = false;
//                isRejectedChecked = false;
//
//
//            }
//            else {
//                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
//                AlertDialog dialog = new AlertDialog.Builder(LeaveApprove.this)
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
//                        new RejectCheck().execute();
//                        dialog.dismiss();
//                    }
//                });
//            }
//        }
//    }

//    public void RequestList() {
//        try {
//            this.connection = createConnection();
//
//            Statement stmt = connection.createStatement();
//
//            leaveReqList = new ArrayList<>();
//
//
//            ResultSet rs=stmt.executeQuery("SELECT DISTINCT \n" +
//                    "                LA_APP_CODE,\n" +
//                    "                LA_EMP_ID,\n" +
//                    "                EMP_MST.EMP_CODE,\n" +
//                    "                EMP_MST.EMP_NAME,\n" +
//                    "                TO_CHAR (LA_DATE, 'DD-MON-YY') LA_DATE,\n" +
//                    "                LA_LEAVE_DAYS,\n" +
//                    "                LA_ID\n" +
//                    "  FROM (SELECT LA.LA_APP_CODE,\n" +
//                    "               LA.LA_EMP_ID,\n" +
//                    "               LA.LA_DATE,\n" +
//                    "               LA.LA_LEAVE_DAYS,\n" +
//                    "               LA.LA_ID\n" +
//                    "          FROM LEAVE_APPLICATION LA\n" +
//                    "         WHERE     LA.LA_EMP_ID IN\n" +
//                    "                      (SELECT C.JOB_EMP_ID\n" +
//                    "                         FROM JOB_SETUP_MST A,\n" +
//                    "                              JOB_SETUP_DTL B,\n" +
//                    "                              (SELECT JOB_EMP_ID, JOB_JSD_ID\n" +
//                    "                                 FROM EMP_JOB_HISTORY EJH\n" +
//                    "                                WHERE JOB_ID =\n" +
//                    "                                         (SELECT MAX (JOB_ID)\n" +
//                    "                                            FROM EMP_JOB_HISTORY EH\n" +
//                    "                                           WHERE EJH.JOB_EMP_ID =\n" +
//                    "                                                    EH.JOB_EMP_ID)) C,\n" +
//                    "                              DESIG_MST     D   --, leave_approval_hierarchy e\n" +
//                    "                        WHERE     A.JSM_ID = B.JSD_JSM_ID\n" +
//                    "                              AND C.JOB_JSD_ID = B.JSD_ID\n" +
//                    "                              AND D.DESIG_ID = A.JSM_DESIG_ID\n" +
//                    "                              AND D.DESIG_PRIORITY IN\n" +
//                    "                                     (SELECT L.LAH_BAND_NO\n" +
//                    "                                        FROM LEAVE_APPROVAL_HIERARCHY L\n" +
//                    "                                       WHERE INSTR (\n" +
//                    "                                                   ','\n" +
//                    "                                                || L.LAH_APPROVAL_BAND\n" +
//                    "                                                || ',',\n" +
//                    "                                                   ','\n" +
//                    "                                                || EMP_PACKAGE.GET_BAND_BY_EMP_CODE (\n" +
//                    "                                                      '"+emp_code+"')\n" +
//                    "                                                || ',') > 0))\n" +
//                    "               AND NVL (LA.LA_APPROVED, 0) = 0\n" +
//                    "        UNION ALL\n" +
//                    "        SELECT LA.LA_APP_CODE,\n" +
//                    "               LA.LA_EMP_ID,\n" +
//                    "               LA.LA_DATE,\n" +
//                    "               LA.LA_LEAVE_DAYS,\n" +
//                    "               LA.LA_ID\n" +
//                    "          FROM LEAVE_APPLICATION LA\n" +
//                    "         WHERE     LA.LA_EMP_ID IN\n" +
//                    "                      (SELECT C.JOB_EMP_ID\n" +
//                    "                         FROM JOB_SETUP_MST A,\n" +
//                    "                              JOB_SETUP_DTL B,\n" +
//                    "                              (SELECT JOB_EMP_ID, JOB_JSD_ID\n" +
//                    "                                 FROM EMP_JOB_HISTORY EJH\n" +
//                    "                                WHERE JOB_ID =\n" +
//                    "                                         (SELECT MAX (JOB_ID)\n" +
//                    "                                            FROM EMP_JOB_HISTORY EH\n" +
//                    "                                           WHERE EJH.JOB_EMP_ID =\n" +
//                    "                                                    EH.JOB_EMP_ID)) C,\n" +
//                    "                              DESIG_MST     D   --, leave_approval_hierarchy e\n" +
//                    "                        WHERE     A.JSM_ID = B.JSD_JSM_ID\n" +
//                    "                              AND C.JOB_JSD_ID = B.JSD_ID\n" +
//                    "                              AND D.DESIG_ID = A.JSM_DESIG_ID\n" +
//                    "                              AND D.DESIG_PRIORITY IN\n" +
//                    "                                     ( (SELECT LAH.LAH_BAND_NO\n" +
//                    "                                          FROM LEAVE_APPROVAL_HIERARCHY LAH\n" +
//                    "                                         WHERE INSTR (\n" +
//                    "                                                     ','\n" +
//                    "                                                  || LAH.LAH_SP_APPROVAL_CODE\n" +
//                    "                                                  || ',',\n" +
//                    "                                                  ',' || '"+emp_code+"' || ',') > 0)))\n" +
//                    "               AND NVL (LA.LA_APPROVED, 0) = 0) LEAVE,\n" +
//                    "       EMP_MST\n" +
//                    " WHERE     LEAVE.LA_EMP_ID = EMP_MST.EMP_ID\n" +
//                    "       AND LA_APP_CODE NOT IN\n" +
//                    "       (SELECT CASE\n" +
//                    "          WHEN "+user_id+" =\n" +
//                    "                  LAD_FORWARD_TO_ID\n" +
//                    "          THEN\n" +
//                    "             'No code'\n" +
//                    "          ELSE\n" +
//                    "             GET_LEAVE_APP_CODE (LAD_LA_ID)\n" +
//                    "       END\n" +
//                    "  FROM LEAVE_APPLICATION_DTL\n" +
//                    " WHERE LAD_ID IN (  SELECT MAX (LAD_ID)\n" +
//                    "                      FROM LEAVE_APPLICATION_DTL D, LEAVE_APPLICATION M\n" +
//                    "                     WHERE M.LA_ID = D.LAD_LA_ID\n" +
//                    "                  GROUP BY LAD_LA_ID))\n" +
//                    "                  order by 7 desc");
//
//
//
//            while(rs.next())  {
//                leaveReqList.add(new SelectApproveReqList(rs.getString(1),rs.getString(4),rs.getString(3),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(2)));
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

    public void getReqLists() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;

        leaveReqList = new ArrayList<>();

        String leaveReqUrl = api_url_front + "leaveRequest/leaveReqLists/"+user_id+"/"+emp_code;

        RequestQueue requestQueue = Volley.newRequestQueue(LeaveApprove.this);

        StringRequest req = new StringRequest(Request.Method.GET, leaveReqUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject reqListInfo = array.getJSONObject(i);

                        String la_app_code_new = reqListInfo.getString("la_app_code");
                        String la_emp_id_new = reqListInfo.getString("la_emp_id");
                        String emp_code_new = reqListInfo.getString("emp_code");
                        String emp_name_new = reqListInfo.getString("emp_name");
                        String la_date_new = reqListInfo.getString("la_date");
                        String la_leave_days_new = reqListInfo.getString("la_leave_days");
                        String la_id_new = reqListInfo.getString("la_id");

                        leaveReqList.add(new SelectApproveReqList(la_app_code_new,emp_name_new,emp_code_new,
                                la_date_new,la_leave_days_new,la_id_new,la_emp_id_new));

                    }
                }

                connected = true;
                updateReqLists();
            }
            catch (JSONException e) {
                logger.log(Level.WARNING, e.getMessage(), e);
                connected = false;
                updateReqLists();
            }
        }, error -> {
            logger.log(Level.WARNING, error.getMessage(), error);
            conn = false;
            connected = false;
            updateReqLists();
        });

        requestQueue.add(req);

    }

    private void updateReqLists() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                conn = false;
                connected = false;

                if (!leaveReqList.isEmpty()) {
                    fromLApp = 1;
                    SelectApproveReq selectRequest = new SelectApproveReq();
                    selectRequest.show(getSupportFragmentManager(), "Request");
                }
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(LeaveApprove.this)
                        .setMessage("There is a network issue in the server. Please Try later.")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    getReqLists();
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
            AlertDialog dialog = new AlertDialog.Builder(LeaveApprove.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel",null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                getReqLists();
                dialog.dismiss();
            });
            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> {
                dialog.dismiss();
                finish();
            });
        }
    }

//    public void DataApprove() {
//        try {
//            this.connection = createConnection();
//
//            Statement stmt = connection.createStatement();
//
//             emp_name = "";
//             emp_id = "";
//             app_date = "";
//             call_title = "";
//             leave_type = "";
//             leave_bal = "";
//             from_date = "";
//             to_date = "";
//             total_day = "";
//             reason_desc = "";
//             lc_id = "";
//
//             forwardHistoryLists = new ArrayList<>();
//
//
//            System.out.println("BEFORE QUERY: "+req_code_leave);
//            System.out.println(user_id);
//
//
//            ResultSet rs=stmt.executeQuery(" SELECT LA.LA_ID,\n" +
//                    "       LA.LA_APP_CODE,\n" +
//                    "       LA.LA_EMP_ID,\n" +
//                    "       EMP.EMP_NAME,\n" +
//                    "       EMP.EMP_CODE,\n" +
//                    "       LA.LA_CALLING_TITLE,\n" +
//                    "       TO_CHAR(LA.LA_DATE,'DD-MON-YY') LA_DATE,\n" +
//                    "       LA.LA_LC_ID,\n" +
//                    "       LC.LC_NAME LEAVE_TYPE,\n" +
//                    "       TO_CHAR(LA.LA_FROM_DATE,'DD-MON-YYYY') LA_FROM_DATE,\n" +
//                    "       TO_CHAR(LA.LA_TO_DATE,'DD-MON-YYYY') LA_TO_DATE,\n" +
//                    "       LA.LA_LEAVE_DAYS,\n" +
//                    "       GET_LEAVE_BALANCE (LA.LA_EMP_ID, SYSDATE, LC.LC_SHORT_CODE)\n" +
//                    "          LEAVE_BALANCE,LA_REASON\n" +
//                    "  FROM LEAVE_APPLICATION LA, LEAVE_CATEGORY LC, EMP_MST EMP\n" +
//                    " WHERE LA.LA_LC_ID = LC.LC_ID AND LA.LA_EMP_ID = EMP.EMP_ID\n" +
//                    " and LA.LA_ID = "+la_id+"");
//
//
//
//            while(rs.next())  {
//
//                emp_id = rs.getString(5);
//                emp_name = rs.getString(4);
//                call_title = rs.getString(6);
//                app_date = rs.getString(7);
//                lc_id = rs.getString(8);
//                leave_type = rs.getString(9);
//                from_date = rs.getString(10);
//                to_date = rs.getString(11);
//                total_day = rs.getString(12);
//                leave_bal= rs.getString(13);
//                reason_desc = rs.getString(14);
//
//            }
//
//            System.out.println(lc_id);
//
//            ResultSet resultSet = stmt.executeQuery("SELECT  EMP_PACKAGE.GET_EMP_NAME (LAD.LAD_FORWADER_ID) FORWARD_BY,\n" +
//                    "       LAD.LAD_RECOMMENDATION,\n" +
//                    "       EMP_PACKAGE.GET_EMP_NAME (LAD.LAD_FORWARD_TO_ID) FORWARD_TO\n" +
//                    "  FROM LEAVE_APPLICATION_DTL LAD\n" +
//                    "  where LAD.LAD_LA_ID = "+la_id+"");
//
//
//            while (resultSet.next()) {
//                forwardHistoryLists.add(new ForwardHistoryList(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3)));
//            }
//
//
//            dataIn = true;
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

    public void getReqData() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        dataIn = false;
        inDataaa = false;

        emp_name = "";
        emp_id = "";
        app_date = "";
        call_title = "";
        leave_type = "";
        leave_bal = "";
        from_date = "";
        to_date = "";
        total_day = "";
        reason_desc = "";
        lc_id = "";

        forwardHistoryLists = new ArrayList<>();

        System.out.println("BEFORE API CALL: "+req_code_leave);

        String reqDataUrl = api_url_front + "leaveRequest/getReqData/"+la_id;
        String forwardHisUrl = api_url_front + "leaveRequest/getForwardHistory/"+la_id;

        RequestQueue requestQueue = Volley.newRequestQueue(LeaveApprove.this);

        StringRequest forHisReq = new StringRequest(Request.Method.GET, forwardHisUrl, response -> {
            dataIn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject forHisInfo = array.getJSONObject(i);

                        String forward_by = forHisInfo.getString("forward_by")
                                .equals("null") ? "" : forHisInfo.getString("forward_by");
                        String lad_recommendation = forHisInfo.getString("lad_recommendation")
                                .equals("null") ? "" : forHisInfo.getString("lad_recommendation");
                        String forward_to = forHisInfo.getString("forward_to")
                                .equals("null") ? "" : forHisInfo.getString("forward_to");

                        forwardHistoryLists.add(new ForwardHistoryList(forward_by,lad_recommendation,forward_to));
                    }
                }
                inDataaa = true;
                updateLayout();
            }
            catch (JSONException e) {
                logger.log(Level.WARNING, e.getMessage(), e);
                inDataaa = false;
                updateLayout();
            }
        }, error -> {
            logger.log(Level.WARNING, error.getMessage(), error);
            dataIn = false;
            inDataaa = false;
            updateLayout();
        });

        StringRequest reqDataReq = new StringRequest(Request.Method.GET, reqDataUrl, response -> {
            dataIn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject reqDataInfo = array.getJSONObject(i);

                        emp_id = reqDataInfo.getString("emp_code")
                                .equals("null") ? "" : reqDataInfo.getString("emp_code");
                        emp_name = reqDataInfo.getString("emp_name")
                                .equals("null") ? "" : reqDataInfo.getString("emp_name");
                        call_title = reqDataInfo.getString("la_calling_title")
                                .equals("null") ? "" : reqDataInfo.getString("la_calling_title");
                        app_date = reqDataInfo.getString("la_date")
                                .equals("null") ? "" : reqDataInfo.getString("la_date");
                        lc_id = reqDataInfo.getString("la_lc_id")
                                .equals("null") ? "" : reqDataInfo.getString("la_lc_id");
                        leave_type = reqDataInfo.getString("leave_type")
                                .equals("null") ? "" : reqDataInfo.getString("leave_type");
                        from_date = reqDataInfo.getString("la_from_date")
                                .equals("null") ? "" : reqDataInfo.getString("la_from_date");
                        to_date = reqDataInfo.getString("la_to_date")
                                .equals("null") ? "" : reqDataInfo.getString("la_to_date");
                        total_day = reqDataInfo.getString("la_leave_days")
                                .equals("null") ? "" : reqDataInfo.getString("la_leave_days");
                        leave_bal= reqDataInfo.getString("leave_balance")
                                .equals("null") ? "" : reqDataInfo.getString("leave_balance");
                        reason_desc = reqDataInfo.getString("la_reason")
                                .equals("null") ? "" : reqDataInfo.getString("la_reason");
                    }
                }

                requestQueue.add(forHisReq);
            }
            catch (JSONException e) {
                logger.log(Level.WARNING, e.getMessage(), e);
                inDataaa = false;
                updateLayout();
            }
        }, error -> {
            logger.log(Level.WARNING, error.getMessage(), error);
            dataIn = false;
            inDataaa = false;
            updateLayout();
        });

        requestQueue.add(reqDataReq);
    }

    private void updateLayout() {
        waitProgress.dismiss();
        if (dataIn) {
            if (inDataaa) {
                afterSelecting.setVisibility(View.VISIBLE);
                afterSelectingButton.setVisibility(View.VISIBLE);

                if (emp_name == null) {
                    name.setText("");
                } else {
                    name.setText(emp_name);
                }

                if (emp_id == null) {
                    empCode.setText("");
                } else {
                    empCode.setText(emp_id);
                }

                if (app_date == null) {
                    appDate.setText("");
                } else {
                    appDate.setText(app_date);
                }

                if (call_title == null) {
                    title.setText("");
                } else {
                    title.setText(call_title);
                }

                if (leave_type == null) {
                    leaveType.setText("");
                } else {
                    leaveType.setText(leave_type);
                }

                if (leave_bal == null) {
                    leaveBalance.setText("");
                } else {
                    leaveBalance.setText(leave_bal);
                }

                if (from_date == null) {
                    fromDate.setText("");
                } else {
                    fromDate.setText(from_date);
                }

                if (to_date == null) {
                    toDate.setText("");
                } else {
                    toDate.setText(to_date);
                }

                if (total_day == null) {
                    totalDays.setText("");
                } else {
                    totalDays.setText(total_day);
                }

                if (reason_desc == null) {
                    reason.setText("");
                } else {
                    reason.setText(reason_desc);
                }

                if (forwardHistoryLists.isEmpty()) {
                    forLay.setVisibility(View.GONE);
                } else {
                    forLay.setVisibility(View.VISIBLE);
                }

                dataIn = false;
                inDataaa = false;
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(LeaveApprove.this)
                        .setMessage("There is a network issue in the server. Please Try later.")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    getReqData();
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
            AlertDialog dialog = new AlertDialog.Builder(LeaveApprove.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel",null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                getReqData();
                dialog.dismiss();
            });
            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> {
                dialog.dismiss();
                finish();
            });
        }
    }

//    public void ApproveClicked() {
//        try {
//            this.connection = createConnection();
//
//            Statement stmt = connection.createStatement();
//
//            approveSuccess = "";
//
//            CallableStatement callableStatement = connection.prepareCall("begin SET_LEAVE_APPROVAL(?,?,?,?,?); end;");
//            callableStatement.setInt(1, Integer.parseInt(la_id));
//            callableStatement.setString(2, emp_code);
//            callableStatement.setInt(3,Integer.parseInt(sl_check));
//            callableStatement.setString(4,textLA);
//            callableStatement.registerOutParameter(5, Types.VARCHAR);
//
//            callableStatement.execute();
//
//            approveSuccess = callableStatement.getString(5);
//            System.out.println("FROM PROCE:: " +approveSuccess);
//
//            callableStatement.close();
//
//
//            isApprovedChecked = true;
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

    public void leaveApproveProcess() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        appppppprrrrr = false;
        isApprovedd = false;
        isApprovedChecked = false;

        approveSuccess = "";

        String approveUrl = api_url_front + "leaveRequest/approveLeave";

        RequestQueue requestQueue = Volley.newRequestQueue(LeaveApprove.this);

        StringRequest approveReq = new StringRequest(Request.Method.POST, approveUrl, response -> {
            appppppprrrrr = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String string_out = jsonObject.getString("string_out");
                String updated_req = jsonObject.getString("updated_req");
                if (string_out.equals("Successfully Created")) {
                    isApprovedd = true;
                    isApprovedChecked = updated_req.equalsIgnoreCase("ok");
                    approveSuccess = updated_req;
                }
                else {
                    System.out.println(string_out);
                    isApprovedd = false;
                }
                updateAfterApprove();
            }
            catch (JSONException e) {
                logger.log(Level.WARNING, e.getMessage(), e);
                isApprovedd = false;
                updateAfterApprove();
            }
        }, error -> {
           logger.log(Level.WARNING, error.getMessage(), error);
           appppppprrrrr = false;
           isApprovedd = false;
           updateAfterApprove();
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("P_LA_ID",la_id);
                headers.put("P_EMP_CODE",emp_code);
                headers.put("P_SL_CHECK",sl_check);
                headers.put("P_TEXT_COMMENTS",textLA);
                return headers;
            }
        };

        requestQueue.add(approveReq);
    }

    private void updateAfterApprove() {
        waitProgress.dismiss();
        if (appppppprrrrr) {
            if (isApprovedd) {
                if (isApprovedChecked) {
                    req_code_leave = "";
                    la_id = "";
                    la_emp_id = "";
                    leaveReqList = new ArrayList<>();
                    System.out.println("INSERTED");

                    AlertDialog dialog = new AlertDialog.Builder(LeaveApprove.this)
                            .setMessage("Leave Approved Successfully")
                            .setPositiveButton("OK", null)
                            .show();

                    dialog.setCancelable(false);
                    dialog.setCanceledOnTouchOutside(false);
                    Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    positive.setOnClickListener(v -> {

                        dialog.dismiss();
                        finish();
                    });
                }
                else {
                    Toast.makeText(getApplicationContext(), approveSuccess, Toast.LENGTH_SHORT).show();
                }
                appppppprrrrr = false;
                isApprovedd = false;
                isApprovedChecked = false;
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(LeaveApprove.this)
                        .setMessage("There is a network issue in the server. Please Try later.")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();

                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    leaveApproveProcess();
                    dialog.dismiss();
                });
            }
        }
        else {
            AlertDialog dialog = new AlertDialog.Builder(LeaveApprove.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel",null)
                    .show();

            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                leaveApproveProcess();
                dialog.dismiss();
            });
        }
    }

//    public void RejectClicked() {
//        try {
//            this.connection = createConnection();
//
//            Statement stmt = connection.createStatement();
//
//            rejectSuccess = "";
//
//            CallableStatement callableStatement = connection.prepareCall("begin SET_LEAVE_REJECTION(?,?,?,?); end;");
//            callableStatement.setInt(1, Integer.parseInt(la_id));
//            callableStatement.setString(2, emp_code);
//            callableStatement.setString(3,textLA);
//            callableStatement.registerOutParameter(4, Types.VARCHAR);
//
//            callableStatement.execute();
//
//            rejectSuccess = callableStatement.getString(4);
//            System.out.println("FROM Reject PROCE:: " +rejectSuccess);
//
//            callableStatement.close();
//
//
//            isRejectedChecked = true;
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

    public void leaveRejectProcess() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);

        rejectSuccess = "";

        rrreeejjjeecctt = false;
        isRejected = false;
        isRejectedChecked = false;

        String rejectUrl = api_url_front + "leaveRequest/rejectLeave";

        RequestQueue requestQueue = Volley.newRequestQueue(LeaveApprove.this);

        StringRequest rejectReq = new StringRequest(Request.Method.POST, rejectUrl, response -> {
            rrreeejjjeecctt = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String string_out = jsonObject.getString("string_out");
                String updated_req = jsonObject.getString("updated_req");
                if (string_out.equals("Successfully Created")) {
                    isRejected = true;
                    isRejectedChecked = updated_req.equalsIgnoreCase("ok");
                    rejectSuccess = updated_req;
                }
                else {
                    System.out.println(string_out);
                    isRejected = false;
                }
                updateAfterReject();
            }
            catch (JSONException e) {
                logger.log(Level.WARNING, e.getMessage(), e);
                isRejected = false;
                updateAfterReject();
            }
        }, error -> {
            logger.log(Level.WARNING, error.getMessage(), error);
            rrreeejjjeecctt = false;
            isRejected = false;
            updateAfterReject();
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("P_LA_ID",la_id);
                headers.put("P_EMP_CODE",emp_code);
                headers.put("P_TEXT_COMMENTS",textLA);
                return headers;
            }
        };

        requestQueue.add(rejectReq);
    }

    private void updateAfterReject() {
        waitProgress.dismiss();
        if (rrreeejjjeecctt) {
            if (isRejected) {
                if (isRejectedChecked) {
                    req_code_leave = "";
                    la_id = "";
                    la_emp_id = "";
                    leaveReqList = new ArrayList<>();
                    System.out.println("INSERTED");

                    AlertDialog dialog = new AlertDialog.Builder(LeaveApprove.this)
                            .setMessage("Leave Rejected Successfully")
                            .setPositiveButton("OK", null)
                            .show();

                    dialog.setCancelable(false);
                    dialog.setCanceledOnTouchOutside(false);
                    Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    positive.setOnClickListener(v -> {

                        dialog.dismiss();
                        finish();
                    });

                }
                else {
                    Toast.makeText(getApplicationContext(), rejectSuccess, Toast.LENGTH_SHORT).show();
                }
                rrreeejjjeecctt = false;
                isRejected = false;
                isRejectedChecked = false;
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(LeaveApprove.this)
                        .setMessage("There is a network issue in the server. Please Try later.")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();

                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    leaveRejectProcess();
                    dialog.dismiss();
                });
            }
        }
        else {
            AlertDialog dialog = new AlertDialog.Builder(LeaveApprove.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel",null)
                    .show();

            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                leaveRejectProcess();
                dialog.dismiss();
            });
        }
    }

}