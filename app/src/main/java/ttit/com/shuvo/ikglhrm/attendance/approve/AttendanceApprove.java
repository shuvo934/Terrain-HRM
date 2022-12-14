package ttit.com.shuvo.ikglhrm.attendance.approve;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
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
import ttit.com.shuvo.ikglhrm.attendance.approve.dialogApproveReq.ForwardDialogue;
import ttit.com.shuvo.ikglhrm.attendance.approve.dialogApproveReq.SelectApproveReq;
import ttit.com.shuvo.ikglhrm.attendance.approve.dialogApproveReq.SelectApproveReqList;
import ttit.com.shuvo.ikglhrm.attendance.reqUpdate.AttendanceReqUpdate;
import ttit.com.shuvo.ikglhrm.attendance.reqUpdate.dialogueFromReq.SelectRequest;
import ttit.com.shuvo.ikglhrm.attendance.update.AttendanceUpdate;
import ttit.com.shuvo.ikglhrm.attendance.update.ReasonList;
import ttit.com.shuvo.ikglhrm.attendance.update.dialogue.DialogueText;

import static ttit.com.shuvo.ikglhrm.Login.userDesignations;
import static ttit.com.shuvo.ikglhrm.Login.userInfoLists;
import static ttit.com.shuvo.ikglhrm.OracleConnection.createConnection;

public class AttendanceApprove extends AppCompatActivity {

    public static int number = 0;
    public static String hint = "";
    public static String text = "";

    public static int forwardFromAtt = 0;

    public static int fromAttApp = 0;

    LinearLayout afterSelecting;
    LinearLayout afterSelectingButton;
    LinearLayout inLay;
    LinearLayout outLay;
    LinearLayout forLay;

    public static TextInputLayout commentsLay;

    public static TextInputEditText requestCode;
    TextInputEditText name;
    TextInputEditText empCode;
    TextInputEditText appDate;
    TextInputEditText title;
    TextInputEditText requestType;
    TextInputEditText shiftUpdated;
    TextInputEditText dateUpdated;
    TextInputEditText inUpdated;
    TextInputEditText outUpdated;
    TextInputEditText machineIn;
    TextInputEditText machineOut;
    TextInputEditText shift;
    TextInputEditText reason;
    TextInputEditText reasonDesc;
    public static TextInputEditText comments;
    TextInputEditText forwardedBy;
    TextInputEditText forwardComm;

    Button approve;
    Button forward;
    Button reject;
    Button close;

    public static ArrayList<SelectApproveReqList> selectApproveReqLists;

    WaitProgress waitProgress = new WaitProgress();

    private String message = null;
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

    private Connection connection;

    String emp_code = "";
    String user_id = "";
    public static String req_code = "";
    public static String darm_id = "";
    public static String darm_emp_id = "";

    String emp_name = "";
    String emp_id = "";
    String app_date = "";
    String call_title = "";
    String req_type = "";
    String shift_update = "";
    String date_update = "";
    String arr_time = "";
    String dep_time = "";
    String mac_in = "";
    String mac_out = "";
    String current_shift = "";
    String reason_type = "";
    String reason_desc = "";
    String forwarded_by = "";
    String forward_comm = "";


    String approvedEmpId = "";
    String jobCalling = "";
    String jsmID = "";
    String deptId = "";
    String divmId = "";
    String nowUpdateDate = "";
    String jobEmail = "";

    int req_status_count = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_approve);

        afterSelecting = findViewById(R.id.after_request_selecting_att_approve);
        afterSelectingButton = findViewById(R.id.button_visiblity_lay);
        inLay = findViewById(R.id.in_time_lay_att_approve);
        outLay = findViewById(R.id.out_time_lay_att_approve);
        forLay = findViewById(R.id.forward_layout);

        requestCode = findViewById(R.id.request_code_att_approve);
        name = findViewById(R.id.name_att_att_approve);
        empCode = findViewById(R.id.id_att_att_approve);
        appDate = findViewById(R.id.now_date_att_att_approve);
        title = findViewById(R.id.calling_title_att_approve);
        requestType = findViewById(R.id.req_type_att_approve);
        shiftUpdated = findViewById(R.id.shift_update_att_approve);
        dateUpdated = findViewById(R.id.date_to_be_updated_att_approve);
        inUpdated = findViewById(R.id.arrival_time_to_be_updated_att_approve);
        outUpdated = findViewById(R.id.departure_time_to_be_updated_att_approve);
        machineIn = findViewById(R.id.machine_in_time_att_approve);
        machineOut = findViewById(R.id.machine_out_time_att_approve);
        shift = findViewById(R.id.current_shift_att_approve);
        reason = findViewById(R.id.reason_type_att_approve);
        reasonDesc = findViewById(R.id.reason_description_att_approve);
        comments = findViewById(R.id.comments_given_att_approve);
        commentsLay = findViewById(R.id.comments_given_layout_att_approve);
        forwardedBy = findViewById(R.id.forwarded_by_att_approve);
        forwardComm = findViewById(R.id.forward_comment_att_approve);

        approve = findViewById(R.id.approve_button_att);
        forward = findViewById(R.id.forward_button_att);
        reject = findViewById(R.id.reject_button_att);
        close = findViewById(R.id.close_button_att_approve);


        selectApproveReqLists = new ArrayList<>();

        emp_code = userInfoLists.get(0).getUserName();
        user_id = userInfoLists.get(0).getEmp_id();
//        user_id = "493";
//        emp_code = "10001";
        new Check().execute();

        requestCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromAttApp = 1;
                SelectApproveReq selectRequest = new SelectApproveReq();
                selectRequest.show(getSupportFragmentManager(),"Request");
            }
        });


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                req_code = "";
                darm_id = "";
                darm_emp_id = "";
                selectApproveReqLists = new ArrayList<>();
                finish();
            }
        });

        requestCode.addTextChangedListener(new TextWatcher() {
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

        comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number = 1;
                hint = commentsLay.getHint().toString();
                text = comments.getText().toString();
                DialogueText dialogueText = new DialogueText();
                dialogueText.show(getSupportFragmentManager(),"TEXTEDIT");
            }
        });

        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                text = comments.getText().toString();
//                new ApproveCheck().execute();

                AlertDialog.Builder builder = new AlertDialog.Builder(AttendanceApprove.this);
                builder.setTitle("Approve Request!")
                        .setMessage("Do you want approve this request?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                text = comments.getText().toString();
                                new ApproveCheck().execute();
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

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                text = comments.getText().toString();
//                new RejectCheck().execute();

                AlertDialog.Builder builder = new AlertDialog.Builder(AttendanceApprove.this);
                builder.setTitle("Reject Request!")
                        .setMessage("Do you want reject this request?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                text = comments.getText().toString();
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
        });

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forwardFromAtt = 1;
                ForwardDialogue forwardDialogue = new ForwardDialogue(AttendanceApprove.this);
                forwardDialogue.show(getSupportFragmentManager(),"FORWARD");
            }
        });


    }

    @Override
    public void onBackPressed() {
        req_code = "";
        darm_id = "";
        darm_emp_id = "";
        selectApproveReqLists = new ArrayList<>();
        finish();
    }

    public boolean isConnected() {
        boolean connected = false;
        boolean isMobile = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
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

                if (req_type == null) {
                    requestType.setText("");
                } else {
                    requestType.setText(req_type);
                }

                if (shift_update == null) {
                    shiftUpdated.setText("");
                } else {
                    shiftUpdated.setText(shift_update);
                }

                if (date_update == null) {
                    dateUpdated.setText("");
                } else {
                    dateUpdated.setText(date_update);
                }

                if (arr_time == null) {
                    inUpdated.setText("");
                    inLay.setVisibility(View.GONE);
                } else {
                    inUpdated.setText(arr_time);
                    inLay.setVisibility(View.VISIBLE);
                }

                if (dep_time == null) {
                    outUpdated.setText("");
                    outLay.setVisibility(View.GONE);
                } else {
                    outUpdated.setText(dep_time);
                    outLay.setVisibility(View.VISIBLE);
                }

                if (mac_in == null) {
                    machineIn.setText("");
                } else {
                    machineIn.setText(mac_in);
                }

                if (mac_out == null) {
                    machineOut.setText("");
                } else {
                    machineOut.setText(mac_out);
                }

                if (current_shift == null) {
                    shift.setText("");
                } else {
                    shift.setText(current_shift);
                }

                if (reason_type == null) {
                    reason.setText("");
                } else {
                    reason.setText(reason_type);
                }

                if (reason_desc == null) {
                    reasonDesc.setText("");
                } else {
                    reasonDesc.setText(reason_desc);
                }

                if (forwarded_by == null) {
                    forLay.setVisibility(View.GONE);
                    forwardedBy.setText("");
                } else {
                    forwardedBy.setText(forwarded_by);
                    forLay.setVisibility(View.VISIBLE);
                }

                if (forward_comm == null) {
                    forwardComm.setText("");
                } else {
                    forwardComm.setText(forward_comm);
                }

                dataIn = false;
                inDataaa = false;


            }else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(AttendanceApprove.this)
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
                AlertDialog dialog = new AlertDialog.Builder(AttendanceApprove.this)
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


                if (isApprovedd) {

                    req_code = "";
                    darm_id = "";
                    darm_emp_id = "";
                    selectApproveReqLists = new ArrayList<>();
                    System.out.println("INSERTED");

                    AlertDialog dialog = new AlertDialog.Builder(AttendanceApprove.this)
                            .setMessage("Request Approved Successfully")
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

                    Toast.makeText(getApplicationContext(), "Already Updated by Another User", Toast.LENGTH_SHORT).show();

                }

                appppppprrrrr = false;
                isApprovedd = false;
                isApprovedChecked = false;


            }else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(AttendanceApprove.this)
                        .setMessage("Please Check Your Internet Connection")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();


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


                if (isRejected) {

                    req_code = "";
                    darm_id = "";
                    darm_emp_id = "";
                    selectApproveReqLists = new ArrayList<>();
                    System.out.println("INSERTED");

                    AlertDialog dialog = new AlertDialog.Builder(AttendanceApprove.this)
                            .setMessage("Request Rejected Successfully")
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

                    Toast.makeText(getApplicationContext(), "Already Updated by Another User", Toast.LENGTH_SHORT).show();

                }

                rrreeejjjeecctt = false;
                isRejected = false;
                isRejectedChecked = false;


            }else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(AttendanceApprove.this)
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

            selectApproveReqLists = new ArrayList<>();


            ResultSet rs=stmt.executeQuery(" SELECT DAILY_ATTEN_REQ_MST.DARM_APP_CODE,emp_mst.emp_name, emp_mst.emp_code,TO_CHAR(DAILY_ATTEN_REQ_MST.DARM_DATE,'DD-MON-YY') DARM_DATE, \n" +
                    "TO_CHAR(DAILY_ATTEN_REQ_MST.DARM_UPDATE_DATE,'DD-MON-YY') DARM_UPDATE_DATE, DAILY_ATTEN_REQ_MST.DARM_ID,DAILY_ATTEN_REQ_MST.DARM_EMP_ID\n" +
                    "                          FROM DAILY_ATTEN_REQ_MST, emp_mst, emp_job_history\n" +
                    "                         WHERE DAILY_ATTEN_REQ_MST.DARM_EMP_ID = emp_mst.emp_id\n" +
                    "                         and emp_mst.emp_id = emp_job_history.job_emp_id\n" +
                    "                         and DARM_APP_CODE IN\n" +
                    "                                  (SELECT DAAHL_APP_CODE\n" +
                    "                                     FROM DAILY_ATTEN_APP_HIERARCHY_LOG, DAILY_ATTEN_REQ_MST\n" +
                    "                                    WHERE     DAILY_ATTEN_APP_HIERARCHY_LOG.DAAHL_APP_CODE =\n" +
                    "                                                 DAILY_ATTEN_REQ_MST.DARM_APP_CODE\n" +
                    "                                          AND NVL (DAILY_ATTEN_REQ_MST.DARM_APPROVED, 0) = 0\n" +
                    "                                          AND (DAILY_ATTEN_APP_HIERARCHY_LOG.DAAHL_APPROVER_BAND_ID =\n" +
                    "                                                  (SELECT EMP_ID\n" +
                    "                                                     FROM EMP_MST\n" +
                    "                                                    WHERE EMP_CODE = '"+emp_code+"'))) \n" +
                    "order by 6 desc");



            while(rs.next())  {
                selectApproveReqLists.add(new SelectApproveReqList(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7)));
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
             req_type = "";
             shift_update = "";
             date_update = "";
             arr_time = "";
             dep_time = "";
             mac_in = "";
             mac_out = "";
             current_shift = "";
             reason_type = "";
             reason_desc = "";
             forwarded_by = "";
             forward_comm = "";

             System.out.println("BEFORE QUERY: "+req_code);
             System.out.println(user_id);


            ResultSet rs=stmt.executeQuery(" SELECT EMP_MST.EMP_CODE,\n" +
                    "       EMP_MST.EMP_NAME,\n" +
                    "       EMP_JOB_HISTORY.JOB_CALLING_TITLE,\n" +
                    "       TO_CHAR (DAILY_ATTEN_REQ_MST.DARM_DATE, 'DD-MON-YY') DARM_DATE,\n" +
                    "       DARM_REQ_TYPE,\n" +
                    "       (SELECT OFFICE_SHIFT_MST.OSM_NAME\n" +
                    "          FROM OFFICE_SHIFT_MST\n" +
                    "         WHERE OFFICE_SHIFT_MST.OSM_ID = DARM_REQ_OSM_ID)\n" +
                    "          AS SHIFT,\n" +
                    "       TO_CHAR (DAILY_ATTEN_REQ_MST.DARM_UPDATE_DATE, 'DD-MON-YY')\n" +
                    "          DARM_UPDATE_DATE,\n" +
                    "       TO_CHAR (DAILY_ATTEN_REQ_MST.DARM_REQ_ARRIVAL_TIME, 'HH:MIAM')\n" +
                    "          ARRIVAL_TIME,\n" +
                    "       TO_CHAR (DAILY_ATTEN_REQ_MST.DARM_REQ_DEPART_TIME, 'HH:MIAM')\n" +
                    "          DEPARTURE_TIME,\n" +
                    "       (SELECT ATTD_UP_REQ_REASON_MST.AURRM_NAME\n" +
                    "          FROM ATTD_UP_REQ_REASON_MST\n" +
                    "         WHERE ATTD_UP_REQ_REASON_MST.AURRM_ID = DARM_AURRM_ID)\n" +
                    "          AS REASON,\n" +
                    "       DARM_REASON,\n" +
                    "       (SELECT TO_CHAR (DA_CHECK.DAC_MAC_IN_TIME, 'HH:MI:SS AM')\n" +
                    "                  DAC_MAC_IN_TIME\n" +
                    "          FROM DA_CHECK\n" +
                    "         WHERE     DA_CHECK.DAC_EMP_ID = DAILY_ATTEN_REQ_MST.DARM_EMP_ID\n" +
                    "               AND DA_CHECK.DAC_DATE = DAILY_ATTEN_REQ_MST.DARM_UPDATE_DATE)\n" +
                    "          MAC_IN_TIME,\n" +
                    "       (SELECT TO_CHAR (DA_CHECK.DAC_MAC_OUT_TIME, 'HH:MI:SS AM')\n" +
                    "                  DAC_MAC_OUT_TIME\n" +
                    "          FROM DA_CHECK\n" +
                    "         WHERE     DA_CHECK.DAC_EMP_ID = DAILY_ATTEN_REQ_MST.DARM_EMP_ID\n" +
                    "               AND DA_CHECK.DAC_DATE = DAILY_ATTEN_REQ_MST.DARM_UPDATE_DATE)\n" +
                    "          MAC_OUT_TIME,\n" +
                    "       (SELECT OFFICE_SHIFT_MST.OSM_NAME\n" +
                    "          FROM DA_CHECK, OFFICE_SHIFT_MST\n" +
                    "         WHERE     DA_CHECK.DAC_OSM_ID = OFFICE_SHIFT_MST.OSM_ID\n" +
                    "               AND DA_CHECK.DAC_EMP_ID = DAILY_ATTEN_REQ_MST.DARM_EMP_ID\n" +
                    "               AND DA_CHECK.DAC_DATE = DAILY_ATTEN_REQ_MST.DARM_UPDATE_DATE)\n" +
                    "          CURRENT_SHIFT,\n" +
                    "       COMM.DARD_RECOMMENDATION,\n" +
                    "       emp_package.get_emp_name(COMM.DARD_FORWADER_ID) forwarder_name\n" +
                    "  FROM DAILY_ATTEN_REQ_MST,\n" +
                    "       EMP_MST,\n" +
                    "       EMP_JOB_HISTORY,\n" +
                    "       (SELECT DARD_RECOMMENDATION,\n" +
                    "               DARD_DARM_ID,\n" +
                    "               DARD_FORWADER_ID,\n" +
                    "               DARD_ID\n" +
                    "          FROM DAILY_ATTEN_REQ_DTL D, DAILY_ATTEN_REQ_MST M\n" +
                    "         WHERE DARD_ID = (SELECT MAX (DARD_ID)\n" +
                    "                            FROM DAILY_ATTEN_REQ_DTL\n" +
                    "                           WHERE DARD_DARM_ID = M.DARM_ID\n" +
                    "                           AND DARD_FORWARD_TO_ID = "+user_id+")) COMM\n" +
                    " WHERE     DAILY_ATTEN_REQ_MST.DARM_EMP_ID = EMP_MST.EMP_ID\n" +
                    "       AND EMP_MST.EMP_ID = EMP_JOB_HISTORY.JOB_EMP_ID\n" +
                    "       AND DAILY_ATTEN_REQ_MST.DARM_ID = COMM.DARD_DARM_ID(+)\n" +
                    "       AND DARM_APP_CODE = '"+req_code+"'");



            while(rs.next())  {

                emp_id = rs.getString(1);
                emp_name = rs.getString(2);
                call_title = rs.getString(3);
                app_date = rs.getString(4);
                req_type = rs.getString(5);
                shift_update = rs.getString(6);
                date_update = rs.getString(7);
                arr_time = rs.getString(8);
                dep_time = rs.getString(9);
                reason_type = rs.getString(10);
                reason_desc = rs.getString(11);
                mac_in = rs.getString(12);
                mac_out = rs.getString(13);
                current_shift = rs.getString(14);
                forward_comm = rs.getString(15);
                forwarded_by = rs.getString(16);
            }

            System.out.println(forwarded_by);


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

             approvedEmpId = "";
             jobCalling = "";
             jsmID = "";
             deptId = "";
             divmId = "";
             nowUpdateDate = "";
             jobEmail = "";

             req_status_count = 0;


            ResultSet rs=stmt.executeQuery(" SELECT EMP_MST.EMP_ID,\n" +
                    "       EMP_JOB_HISTORY.JOB_CALLING_TITLE,\n" +
                    "       JOB_SETUP_MST.JSM_ID,\n" +
                    "       DEPT_MST.DEPT_ID,\n" +
                    "       DIVISION_MST.DIVM_ID,\n" +
                    "       TO_CHAR(SYSDATE,'DD-MON-YY') update_date,\n" +
                    "       EMP_JOB_HISTORY.JOB_EMAIL\n" +
                    "  FROM EMP_MST,\n" +
                    "       COMPANY_OFFICE_ADDRESS,\n" +
                    "       EMP_JOB_HISTORY,\n" +
                    "       JOB_SETUP_DTL,\n" +
                    "       JOB_SETUP_MST,\n" +
                    "       DEPT_MST,\n" +
                    "       DIVISION_MST\n" +
                    " WHERE     EMP_MST.EMP_CODE = '"+emp_code+"'\n" +
                    "       AND (    (EMP_JOB_HISTORY.JOB_PRI_COA_ID =\n" +
                    "                    COMPANY_OFFICE_ADDRESS.COA_ID)\n" +
                    "            AND (JOB_SETUP_DTL.JSD_JSM_ID = JOB_SETUP_MST.JSM_ID)\n" +
                    "            AND (JOB_SETUP_MST.JSM_DEPT_ID = DEPT_MST.DEPT_ID)\n" +
                    "            AND (JOB_SETUP_MST.JSM_DIVM_ID = DIVISION_MST.DIVM_ID)\n" +
                    "            AND (EMP_JOB_HISTORY.JOB_ID = EMP_MST.EMP_JOB_ID)\n" +
                    "            AND (JOB_SETUP_DTL.JSD_ID = EMP_JOB_HISTORY.JOB_JSD_ID))");



            while(rs.next())  {
                approvedEmpId = rs.getString(1);
                jobCalling = rs.getString(2);
                jsmID = rs.getString(3);
                deptId = rs.getString(4);
                divmId = rs.getString(5);
                nowUpdateDate = rs.getString(6);
                jobEmail = rs.getString(7);
            }

            ResultSet resultSet = stmt.executeQuery("SELECT COUNT (1) req_status_count\n" +
                    "     FROM DAILY_ATTEN_REQ_MST\n" +
                    "     WHERE DARM_ID = "+darm_id+"\n" +
                    "          AND NVL (DARM_APPROVED, 0) = 0");

            while (resultSet.next()) {
                req_status_count = resultSet.getInt(1);
            }

            if (req_status_count >= 1) {

                stmt.executeUpdate("update DAILY_ATTEN_REQ_MST\n" +
                        "    set DARM_APP_REJECT_EMP_ID = "+approvedEmpId+",\n" +
                        "                    DARM_APP_REJECT_CALLING_TITLE = '"+jobCalling+"',\n" +
                        "                    DARM_APP_REJECT_JSM_ID = "+jsmID+",\n" +
                        "                    DARM_APP_REJECT_DEPT_ID = "+deptId+",\n" +
                        "                    DARM_APP_REJECT_DIVM_ID = "+divmId+",\n" +
                        "                    DARM_APP_REJ_DATE = '"+nowUpdateDate+"',\n" +
                        "                    DARM_COMMENTS = '"+text+"',\n" +
                        "                    DARM_APPROVED = 1\n"+
                        "    where DARM_ID = "+darm_id+"");


                CallableStatement callableStatement = connection.prepareCall("begin DAILY_ATTEN_REQ_APPROVE(?,?); end;");
                callableStatement.setInt(1, Integer.parseInt(darm_emp_id));
                callableStatement.setString(2, date_update);

                callableStatement.execute();

                callableStatement.close();

                isApprovedd = true;
            } else {
                message = "Already Updated by Another User";

                isApprovedd = false;
            }


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

            approvedEmpId = "";
            jobCalling = "";
            jsmID = "";
            deptId = "";
            divmId = "";
            nowUpdateDate = "";
            jobEmail = "";

            req_status_count = 0;


            ResultSet rs=stmt.executeQuery(" SELECT EMP_MST.EMP_ID,\n" +
                    "       EMP_JOB_HISTORY.JOB_CALLING_TITLE,\n" +
                    "       JOB_SETUP_MST.JSM_ID,\n" +
                    "       DEPT_MST.DEPT_ID,\n" +
                    "       DIVISION_MST.DIVM_ID,\n" +
                    "       TO_CHAR(SYSDATE,'DD-MON-YY') update_date,\n" +
                    "       EMP_JOB_HISTORY.JOB_EMAIL\n" +
                    "  FROM EMP_MST,\n" +
                    "       COMPANY_OFFICE_ADDRESS,\n" +
                    "       EMP_JOB_HISTORY,\n" +
                    "       JOB_SETUP_DTL,\n" +
                    "       JOB_SETUP_MST,\n" +
                    "       DEPT_MST,\n" +
                    "       DIVISION_MST\n" +
                    " WHERE     EMP_MST.EMP_CODE = '"+emp_code+"'\n" +
                    "       AND (    (EMP_JOB_HISTORY.JOB_PRI_COA_ID =\n" +
                    "                    COMPANY_OFFICE_ADDRESS.COA_ID)\n" +
                    "            AND (JOB_SETUP_DTL.JSD_JSM_ID = JOB_SETUP_MST.JSM_ID)\n" +
                    "            AND (JOB_SETUP_MST.JSM_DEPT_ID = DEPT_MST.DEPT_ID)\n" +
                    "            AND (JOB_SETUP_MST.JSM_DIVM_ID = DIVISION_MST.DIVM_ID)\n" +
                    "            AND (EMP_JOB_HISTORY.JOB_ID = EMP_MST.EMP_JOB_ID)\n" +
                    "            AND (JOB_SETUP_DTL.JSD_ID = EMP_JOB_HISTORY.JOB_JSD_ID))");



            while(rs.next())  {
                approvedEmpId = rs.getString(1);
                jobCalling = rs.getString(2);
                jsmID = rs.getString(3);
                deptId = rs.getString(4);
                divmId = rs.getString(5);
                nowUpdateDate = rs.getString(6);
                jobEmail = rs.getString(7);
            }

            ResultSet resultSet = stmt.executeQuery("SELECT COUNT (1) req_status_count\n" +
                    "     FROM DAILY_ATTEN_REQ_MST\n" +
                    "     WHERE DARM_ID = "+darm_id+"\n" +
                    "          AND NVL (DARM_APPROVED, 0) = 0");

            while (resultSet.next()) {
                req_status_count = resultSet.getInt(1);
            }

            if (req_status_count >= 1) {

                stmt.executeUpdate("update DAILY_ATTEN_REQ_MST\n" +
                        "    set DARM_APP_REJECT_EMP_ID = "+approvedEmpId+",\n" +
                        "                    DARM_APP_REJECT_CALLING_TITLE = '"+jobCalling+"',\n" +
                        "                    DARM_APP_REJECT_JSM_ID = "+jsmID+",\n" +
                        "                    DARM_APP_REJECT_DEPT_ID = "+deptId+",\n" +
                        "                    DARM_APP_REJECT_DIVM_ID = "+divmId+",\n" +
                        "                    DARM_APP_REJ_DATE = '"+nowUpdateDate+"',\n" +
                        "                    DARM_COMMENTS = '"+text+"',\n" +
                        "                    DARM_APPROVED = 2\n"+
                        "    where DARM_ID = "+darm_id+"");



                isRejected = true;
            } else {
                message = "Already Updated by Another User";

                isRejected = false;
            }


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