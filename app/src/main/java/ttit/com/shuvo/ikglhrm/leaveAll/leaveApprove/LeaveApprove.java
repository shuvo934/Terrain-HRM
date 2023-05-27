package ttit.com.shuvo.ikglhrm.leaveAll.leaveApprove;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;

import ttit.com.shuvo.ikglhrm.R;
import ttit.com.shuvo.ikglhrm.WaitProgress;
import ttit.com.shuvo.ikglhrm.attendance.approve.AttendanceApprove;
import ttit.com.shuvo.ikglhrm.attendance.approve.dialogApproveReq.ForwardDialogue;
import ttit.com.shuvo.ikglhrm.attendance.approve.dialogApproveReq.SelectApproveReq;
import ttit.com.shuvo.ikglhrm.attendance.approve.dialogApproveReq.SelectApproveReqList;
import ttit.com.shuvo.ikglhrm.attendance.update.dialogue.DialogueText;
import ttit.com.shuvo.ikglhrm.leaveAll.leaveApprove.leaveAppDialogues.ForwardHistoryDial;
import ttit.com.shuvo.ikglhrm.leaveAll.leaveApprove.leaveAppDialogues.ForwardHistoryList;
import ttit.com.shuvo.ikglhrm.payRoll.PayRollInfo;

import static ttit.com.shuvo.ikglhrm.Login.userInfoLists;
import static ttit.com.shuvo.ikglhrm.OracleConnection.createConnection;

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
    Button close;
    Button fh;

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
    String comm = "";

    String sl_check = "0";
    String approveSuccess = "";
    String rejectSuccess = "";



    WaitProgress waitProgress = new WaitProgress();

    private String message = null;
    private Boolean conn = false;
    private Boolean connected = false;

    private Boolean dataIn = false;
    private Boolean inDataaa = false;

    private Boolean isApprovedd = false;
    private Boolean isApprovedChecked = false;
    private Boolean appppppprrrrr = false;

    private Boolean isRejectedChecked = false;
    private Boolean rrreeejjjeecctt = false;

    private Connection connection;

    String emp_code = "";
    String user_id = "";
    public static String req_code_leave = "";
    public static String la_id = "";
    public static String la_emp_id = "";

    public static TextInputEditText requestCodeLeave;

    public static ArrayList<SelectApproveReqList> leaveReqList;

    public static ArrayList<ForwardHistoryList> forwardHistoryLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(LeaveApprove.this,R.color.secondaryColor));
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
        close = findViewById(R.id.close_button_leave_approve);
        fh = findViewById(R.id.forward_history_button_leave);


        emp_code = userInfoLists.get(0).getUserName();
        user_id = userInfoLists.get(0).getEmp_id();

        leaveReqList = new ArrayList<>();
        forwardHistoryLists = new ArrayList<>();

        new Check().execute();

        requestCodeLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fromLApp = 1;
                SelectApproveReq selectRequest = new SelectApproveReq();
                selectRequest.show(getSupportFragmentManager(),"Request");
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                req_code_leave = "";
                la_id = "";
                la_emp_id = "";
                leaveReqList = new ArrayList<>();
                finish();
            }
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

                afterSelecting.setVisibility(View.VISIBLE);
                afterSelectingButton.setVisibility(View.VISIBLE);
                new GettingData().execute();

            }
        });

        fh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forwardFromLeave = 1;
                ForwardHistoryDial forwardHistoryDial = new ForwardHistoryDial();
                forwardHistoryDial.show(getSupportFragmentManager(),"Forward");
            }
        });

        comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number = 1;
                hintLA = commentsLay.getHint().toString();
                textLA = comments.getText().toString();
                DialogueText dialogueText = new DialogueText();
                dialogueText.show(getSupportFragmentManager(),"TEXTEDIT");
            }
        });

        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                text = comments.getText().toString();
//                new ApproveCheck().execute();

                AlertDialog.Builder builder = new AlertDialog.Builder(LeaveApprove.this);
                builder.setTitle("Approve Leave!")
                        .setMessage("Do you want approve this leave?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                // checking it is sick leave or not
                                if (lc_id.equals("2")) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(LeaveApprove.this);
                                    builder.setTitle("Prescription Check!")
                                            .setMessage("Did you checked prescription of the applicant?")
                                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {


                                                    sl_check = "1";
                                                    textLA = comments.getText().toString();
                                                    new ApproveCheck().execute();
                                                }
                                            })
                                            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    textLA = comments.getText().toString();
                                                    new ApproveCheck().execute();
                                                }
                                            });
                                    AlertDialog alert = builder.create();
                                    alert.show();

                                } else {

                                    textLA = comments.getText().toString();
                                    new ApproveCheck().execute();
                                }

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
        });

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                forwardFromLeave = 1;
                ForwardDialogue forwardDialogue = new ForwardDialogue(LeaveApprove.this);
                forwardDialogue.show(getSupportFragmentManager(),"FORWARD");
            }
        });

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                text = comments.getText().toString();
//                new ApproveCheck().execute();

                textLA = comments.getText().toString();

                if (textLA.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please mention reason", Toast.LENGTH_SHORT).show();
                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(LeaveApprove.this);
                    builder.setTitle("Reject Leave!")
                            .setMessage("Do you want reject this leave?")
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    new RejectCheck().execute();
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
        });
    }

    @Override
    public void onBackPressed() {
        req_code_leave = "";
        la_id = "";
        la_emp_id = "";
        leaveReqList = new ArrayList<>();
        finish();
    }

    public boolean isConnected() {
        boolean connected = false;
        boolean isMobile = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            @SuppressLint("MissingPermission") NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }

    public boolean isOnline() {

        Runtime runtime = Runtime.getRuntime();
        try {
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

            waitProgress.show(getSupportFragmentManager(),"WaitBar");
            waitProgress.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isConnected() && isOnline()) {

                RequestList();
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

                conn = false;
                connected = false;


            }else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(LeaveApprove.this)
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
                        finish();
                    }
                });
            }
        }
    }

    public class GettingData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            waitProgress.show(getSupportFragmentManager(),"WaitBar");
            waitProgress.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isConnected() && isOnline()) {

                DataApprove();
                if (dataIn) {
                    inDataaa = true;
                    message= "Internet Connected";
                }

            } else {
                inDataaa = false;
                message = "Not Connected";
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            waitProgress.dismiss();
            if (inDataaa) {

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

                if (forwardHistoryLists.size() == 0) {
                    forLay.setVisibility(View.GONE);
                } else {
                    forLay.setVisibility(View.VISIBLE);
                }

                dataIn = false;
                inDataaa = false;


            }else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(LeaveApprove.this)
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

                        new GettingData().execute();
                        dialog.dismiss();
                    }
                });
                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        finish();
                    }
                });
            }
        }
    }

    public class ApproveCheck extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            waitProgress.show(getSupportFragmentManager(),"WaitBar");
            waitProgress.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isConnected() && isOnline()) {

                ApproveClicked();
                if (isApprovedChecked) {
                    appppppprrrrr = true;

                }

            } else {
                appppppprrrrr = false;

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            waitProgress.dismiss();
            if (appppppprrrrr) {



                if (approveSuccess.equals("OK")) {
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
                    positive.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            dialog.dismiss();
                            finish();
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), approveSuccess, Toast.LENGTH_SHORT).show();
                }


                appppppprrrrr = false;
                isApprovedd = false;
                isApprovedChecked = false;


            }else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(LeaveApprove.this)
                        .setMessage("Please Check Your Internet Connection")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();

//                dialog.setCancelable(false);
//                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new ApproveCheck().execute();
                        dialog.dismiss();
                    }
                });
            }
        }
    }

    public class RejectCheck extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            waitProgress.show(getSupportFragmentManager(),"WaitBar");
            waitProgress.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isConnected() && isOnline()) {

                RejectClicked();
                if (isRejectedChecked) {
                    rrreeejjjeecctt = true;

                }

            } else {
                rrreeejjjeecctt = false;

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            waitProgress.dismiss();
            if (rrreeejjjeecctt) {


                if (rejectSuccess.equals("OK")) {

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
                    positive.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            dialog.dismiss();
                            finish();
                        }
                    });

                } else {

                    Toast.makeText(getApplicationContext(), rejectSuccess, Toast.LENGTH_SHORT).show();

                }

                rrreeejjjeecctt = false;
                isRejectedChecked = false;


            }else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(LeaveApprove.this)
                        .setMessage("Please Check Your Internet Connection")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();

//                dialog.setCancelable(false);
//                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new RejectCheck().execute();
                        dialog.dismiss();
                    }
                });
            }
        }
    }

    public void RequestList() {
        try {
            this.connection = createConnection();

            Statement stmt = connection.createStatement();

            leaveReqList = new ArrayList<>();


            ResultSet rs=stmt.executeQuery("SELECT DISTINCT \n" +
                    "                LA_APP_CODE,\n" +
                    "                LA_EMP_ID,\n" +
                    "                EMP_MST.EMP_CODE,\n" +
                    "                EMP_MST.EMP_NAME,\n" +
                    "                TO_CHAR (LA_DATE, 'DD-MON-YY') LA_DATE,\n" +
                    "                LA_LEAVE_DAYS,\n" +
                    "                LA_ID\n" +
                    "  FROM (SELECT LA.LA_APP_CODE,\n" +
                    "               LA.LA_EMP_ID,\n" +
                    "               LA.LA_DATE,\n" +
                    "               LA.LA_LEAVE_DAYS,\n" +
                    "               LA.LA_ID\n" +
                    "          FROM LEAVE_APPLICATION LA\n" +
                    "         WHERE     LA.LA_EMP_ID IN\n" +
                    "                      (SELECT C.JOB_EMP_ID\n" +
                    "                         FROM JOB_SETUP_MST A,\n" +
                    "                              JOB_SETUP_DTL B,\n" +
                    "                              (SELECT JOB_EMP_ID, JOB_JSD_ID\n" +
                    "                                 FROM EMP_JOB_HISTORY EJH\n" +
                    "                                WHERE JOB_ID =\n" +
                    "                                         (SELECT MAX (JOB_ID)\n" +
                    "                                            FROM EMP_JOB_HISTORY EH\n" +
                    "                                           WHERE EJH.JOB_EMP_ID =\n" +
                    "                                                    EH.JOB_EMP_ID)) C,\n" +
                    "                              DESIG_MST     D   --, leave_approval_hierarchy e\n" +
                    "                        WHERE     A.JSM_ID = B.JSD_JSM_ID\n" +
                    "                              AND C.JOB_JSD_ID = B.JSD_ID\n" +
                    "                              AND D.DESIG_ID = A.JSM_DESIG_ID\n" +
                    "                              AND D.DESIG_PRIORITY IN\n" +
                    "                                     (SELECT L.LAH_BAND_NO\n" +
                    "                                        FROM LEAVE_APPROVAL_HIERARCHY L\n" +
                    "                                       WHERE INSTR (\n" +
                    "                                                   ','\n" +
                    "                                                || L.LAH_APPROVAL_BAND\n" +
                    "                                                || ',',\n" +
                    "                                                   ','\n" +
                    "                                                || EMP_PACKAGE.GET_BAND_BY_EMP_CODE (\n" +
                    "                                                      '"+emp_code+"')\n" +
                    "                                                || ',') > 0))\n" +
                    "               AND NVL (LA.LA_APPROVED, 0) = 0\n" +
                    "        UNION ALL\n" +
                    "        SELECT LA.LA_APP_CODE,\n" +
                    "               LA.LA_EMP_ID,\n" +
                    "               LA.LA_DATE,\n" +
                    "               LA.LA_LEAVE_DAYS,\n" +
                    "               LA.LA_ID\n" +
                    "          FROM LEAVE_APPLICATION LA\n" +
                    "         WHERE     LA.LA_EMP_ID IN\n" +
                    "                      (SELECT C.JOB_EMP_ID\n" +
                    "                         FROM JOB_SETUP_MST A,\n" +
                    "                              JOB_SETUP_DTL B,\n" +
                    "                              (SELECT JOB_EMP_ID, JOB_JSD_ID\n" +
                    "                                 FROM EMP_JOB_HISTORY EJH\n" +
                    "                                WHERE JOB_ID =\n" +
                    "                                         (SELECT MAX (JOB_ID)\n" +
                    "                                            FROM EMP_JOB_HISTORY EH\n" +
                    "                                           WHERE EJH.JOB_EMP_ID =\n" +
                    "                                                    EH.JOB_EMP_ID)) C,\n" +
                    "                              DESIG_MST     D   --, leave_approval_hierarchy e\n" +
                    "                        WHERE     A.JSM_ID = B.JSD_JSM_ID\n" +
                    "                              AND C.JOB_JSD_ID = B.JSD_ID\n" +
                    "                              AND D.DESIG_ID = A.JSM_DESIG_ID\n" +
                    "                              AND D.DESIG_PRIORITY IN\n" +
                    "                                     ( (SELECT LAH.LAH_BAND_NO\n" +
                    "                                          FROM LEAVE_APPROVAL_HIERARCHY LAH\n" +
                    "                                         WHERE INSTR (\n" +
                    "                                                     ','\n" +
                    "                                                  || LAH.LAH_SP_APPROVAL_CODE\n" +
                    "                                                  || ',',\n" +
                    "                                                  ',' || '"+emp_code+"' || ',') > 0)))\n" +
                    "               AND NVL (LA.LA_APPROVED, 0) = 0) LEAVE,\n" +
                    "       EMP_MST\n" +
                    " WHERE     LEAVE.LA_EMP_ID = EMP_MST.EMP_ID\n" +
                    "       AND LA_APP_CODE NOT IN\n" +
                    "       (SELECT CASE\n" +
                    "          WHEN "+user_id+" =\n" +
                    "                  LAD_FORWARD_TO_ID\n" +
                    "          THEN\n" +
                    "             'No code'\n" +
                    "          ELSE\n" +
                    "             GET_LEAVE_APP_CODE (LAD_LA_ID)\n" +
                    "       END\n" +
                    "  FROM LEAVE_APPLICATION_DTL\n" +
                    " WHERE LAD_ID IN (  SELECT MAX (LAD_ID)\n" +
                    "                      FROM LEAVE_APPLICATION_DTL D, LEAVE_APPLICATION M\n" +
                    "                     WHERE M.LA_ID = D.LAD_LA_ID\n" +
                    "                  GROUP BY LAD_LA_ID))\n" +
                    "                  order by 7 desc");



            while(rs.next())  {
                leaveReqList.add(new SelectApproveReqList(rs.getString(1),rs.getString(4),rs.getString(3),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(2)));
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

    public void DataApprove() {
        try {
            this.connection = createConnection();

            Statement stmt = connection.createStatement();

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


            System.out.println("BEFORE QUERY: "+req_code_leave);
            System.out.println(user_id);


            ResultSet rs=stmt.executeQuery(" SELECT LA.LA_ID,\n" +
                    "       LA.LA_APP_CODE,\n" +
                    "       LA.LA_EMP_ID,\n" +
                    "       EMP.EMP_NAME,\n" +
                    "       EMP.EMP_CODE,\n" +
                    "       LA.LA_CALLING_TITLE,\n" +
                    "       TO_CHAR(LA.LA_DATE,'DD-MON-YY') LA_DATE,\n" +
                    "       LA.LA_LC_ID,\n" +
                    "       LC.LC_NAME LEAVE_TYPE,\n" +
                    "       TO_CHAR(LA.LA_FROM_DATE,'DD-MON-YYYY') LA_FROM_DATE,\n" +
                    "       TO_CHAR(LA.LA_TO_DATE,'DD-MON-YYYY') LA_TO_DATE,\n" +
                    "       LA.LA_LEAVE_DAYS,\n" +
                    "       GET_LEAVE_BALANCE (LA.LA_EMP_ID, SYSDATE, LC.LC_SHORT_CODE)\n" +
                    "          LEAVE_BALANCE,LA_REASON\n" +
                    "  FROM LEAVE_APPLICATION LA, LEAVE_CATEGORY LC, EMP_MST EMP\n" +
                    " WHERE LA.LA_LC_ID = LC.LC_ID AND LA.LA_EMP_ID = EMP.EMP_ID\n" +
                    " and LA.LA_ID = "+la_id+"");



            while(rs.next())  {

                emp_id = rs.getString(5);
                emp_name = rs.getString(4);
                call_title = rs.getString(6);
                app_date = rs.getString(7);
                lc_id = rs.getString(8);
                leave_type = rs.getString(9);
                from_date = rs.getString(10);
                to_date = rs.getString(11);
                total_day = rs.getString(12);
                leave_bal= rs.getString(13);
                reason_desc = rs.getString(14);

            }

            System.out.println(lc_id);

            ResultSet resultSet = stmt.executeQuery("SELECT  EMP_PACKAGE.GET_EMP_NAME (LAD.LAD_FORWADER_ID) FORWARD_BY,\n" +
                    "       LAD.LAD_RECOMMENDATION,\n" +
                    "       EMP_PACKAGE.GET_EMP_NAME (LAD.LAD_FORWARD_TO_ID) FORWARD_TO\n" +
                    "  FROM LEAVE_APPLICATION_DTL LAD\n" +
                    "  where LAD.LAD_LA_ID = "+la_id+"");


            while (resultSet.next()) {
                forwardHistoryLists.add(new ForwardHistoryList(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3)));
            }


            dataIn = true;

            connection.close();

        }
        catch (Exception e) {

            //   Toast.makeText(MainActivity.this, ""+e,Toast.LENGTH_LONG).show();
            Log.i("ERRRRR", e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    public void ApproveClicked() {
        try {
            this.connection = createConnection();

            Statement stmt = connection.createStatement();

            approveSuccess = "";

            CallableStatement callableStatement = connection.prepareCall("begin SET_LEAVE_APPROVAL(?,?,?,?,?); end;");
            callableStatement.setInt(1, Integer.parseInt(la_id));
            callableStatement.setString(2, emp_code);
            callableStatement.setInt(3,Integer.parseInt(sl_check));
            callableStatement.setString(4,textLA);
            callableStatement.registerOutParameter(5, Types.VARCHAR);
            
            callableStatement.execute();

            approveSuccess = callableStatement.getString(5);
            System.out.println("FROM PROCE:: " +approveSuccess);

            callableStatement.close();


            isApprovedChecked = true;
            connection.close();

        }
        catch (Exception e) {

            //   Toast.makeText(MainActivity.this, ""+e,Toast.LENGTH_LONG).show();
            Log.i("ERRRRR", e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    public void RejectClicked() {
        try {
            this.connection = createConnection();

            Statement stmt = connection.createStatement();

            rejectSuccess = "";

            CallableStatement callableStatement = connection.prepareCall("begin SET_LEAVE_REJECTION(?,?,?,?); end;");
            callableStatement.setInt(1, Integer.parseInt(la_id));
            callableStatement.setString(2, emp_code);
            callableStatement.setString(3,textLA);
            callableStatement.registerOutParameter(4, Types.VARCHAR);

            callableStatement.execute();

            rejectSuccess = callableStatement.getString(4);
            System.out.println("FROM Reject PROCE:: " +rejectSuccess);

            callableStatement.close();


            isRejectedChecked = true;
            connection.close();

        }
        catch (Exception e) {

            //   Toast.makeText(MainActivity.this, ""+e,Toast.LENGTH_LONG).show();
            Log.i("ERRRRR", e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

}