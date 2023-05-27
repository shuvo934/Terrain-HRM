package ttit.com.shuvo.ikglhrm.payRoll.paySlip;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import ttit.com.shuvo.ikglhrm.R;
import ttit.com.shuvo.ikglhrm.WaitProgress;
import ttit.com.shuvo.ikglhrm.attendance.report.AttenReportAdapter;
import ttit.com.shuvo.ikglhrm.attendance.report.AttendanceReport;
import ttit.com.shuvo.ikglhrm.attendance.update.ReasonList;
import ttit.com.shuvo.ikglhrm.leaveAll.leaveStatus.LeaveStatus;

import static ttit.com.shuvo.ikglhrm.Login.userDesignations;
import static ttit.com.shuvo.ikglhrm.Login.userInfoLists;
import static ttit.com.shuvo.ikglhrm.OracleConnection.DEFAULT_USERNAME;
import static ttit.com.shuvo.ikglhrm.OracleConnection.createConnection;

public class PaySlip extends AppCompatActivity {

    public static TextInputEditText selectMonth;
    public static TextInputLayout selectMonthLay;

    public static TextView errorMsgMonth;

    Button show;
    MaterialButton download;

    CardView reportCard;

    TextView monthName;

    TextInputEditText empName;
    TextInputEditText id;
    TextInputEditText band;
    TextInputEditText strDes;
    TextInputEditText jobPosition;
    TextInputEditText division;
    TextInputEditText department;
    TextInputEditText officeLoc;
    TextInputEditText officeExt;
    TextInputEditText email;
    TextInputEditText mobile;
    TextInputEditText addCharge;

    TextView salaryData;
    LinearLayout salaryLay;

    TextView basic;
    TextView houseRent;
    TextView food;
    TextView conveyance;
    TextView grossSalary;
    TextView overTime;
    TextView attBonus;
    TextView otherAllow;
    TextView totalPerquisite;

    TextView providentFund;
    TextView leavePayText;
    TextView leavePay;
    TextView absentDeducText;
    TextView absentDeduc;
    TextView loanDeducOneTime;
    TextView loanDeducSched;
    TextView loanDeducProFun;
    TextView taxDeduc;
    TextView lunchDeduc;
    TextView stamp;
    TextView otherDeduc;
    TextView loanDeducBoard;
    TextView totalDeduc;

    TextView netPayment;
    TextView inWord;

    TextView accountName;
    TextView accountNo;
    TextView bankName;

    Button close;

    String count = "";

    public static String select_month_id = "";
    String emp_id = "";

    String emp_name = "";
    String user_id = "";
    String ban = "";
    String str_DES = "";
    String job_pos = "";
    String div = "";
    String dep = "";

    String off_loc = "";
    String off_ext = "";
    String email_name = "";
    String mobile_no = "";
    String charge = "";

    String basic_salary = "";
    String hou_rent = "";
    String food_cost = "";
    String conveyance_cost = "";
    String gross_salary = "";
    String over_time = "";
    String att_bon = "";
    String other_allow = "";
    String total_perqqq = "";

    String prov_fun = "";
    String leave_pay = "";
    String leave_pay_days = "";
    String abs_ded_days = "";
    String abs_ded = "";
    String loan_ded_one = "";
    String loan_ded_sche = "";
    String loan_ded_pf = "";
    String tax_ded = "";
    String lun_ded = "";
    String stam = "";
    String oth_ded = "";
    String loan_ded_boa = "";
    String total_ded = "";

    String total = "";
    String total_word = "";

    String acc_name = "";
    String acc_no = "";
    String bank = "";

    WaitProgress waitProgress = new WaitProgress();
    private String message = null;
    private Boolean conn = false;
    private Boolean connected = false;
    private Boolean downConn = false;

    private Connection connection;

    private ProgressDialog pDialog;

    String URL = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window w = getWindow();
//            //w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        }
//        if (Build.VERSION.SDK_INT < 16) {
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        }
//        View decorView = getWindow().getDecorView();
//// Hide the status bar.
//        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
//        decorView.setSystemUiVisibility(uiOptions);
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(PaySlip.this,R.color.secondaryColor));
        setContentView(R.layout.activity_pay_slip);

        select_month_id = "";

        selectMonth = findViewById(R.id.select_month_pay_slip);
        selectMonthLay = findViewById(R.id.select_month_pay_slip_lay);

        errorMsgMonth = findViewById(R.id.error_msg_for_no_entry_pay_slip);

        show = findViewById(R.id.show_pay_slip);
        download = findViewById(R.id.download_pay_slip_report);

        reportCard = findViewById(R.id.pay_slip_report_card);

        monthName = findViewById(R.id.month_year_name);

        empName = findViewById(R.id.name_pay_slip);
        id = findViewById(R.id.id_pay_slip);
        band = findViewById(R.id.band_pay_slip);
        strDes = findViewById(R.id.str_des_pay_slip);
        jobPosition = findViewById(R.id.job_pos_pay_slip);
        division = findViewById(R.id.division_pay_slip);
        department = findViewById(R.id.departmnet_pay_slip);
        officeLoc = findViewById(R.id.office_loca_pay_slip);
        officeExt = findViewById(R.id.office_extenstion_pay_slip);
        email = findViewById(R.id.email_pay_slip);
        mobile = findViewById(R.id.mobile_pay_slip);
        addCharge = findViewById(R.id.additional_charge_pay_slip);

        salaryData = findViewById(R.id.salary_data_msg);
        salaryLay = findViewById(R.id.salary_info_pay_slip_lay);

        basic = findViewById(R.id.basic_salary_pay_slip);
        houseRent = findViewById(R.id.house_rent_pay_slip);
        food = findViewById(R.id.food_pay_slip);
        conveyance = findViewById(R.id.conveyance_pay_slip);
        grossSalary = findViewById(R.id.gross_salary_pay_slip);
        overTime = findViewById(R.id.over_time_pay_slip);
        attBonus = findViewById(R.id.attendance_bonus_pay_slip);
        otherAllow = findViewById(R.id.other_allow_payment_pay_slip);
        totalPerquisite = findViewById(R.id.total_perquisite_pay_slip);

        providentFund = findViewById(R.id.provident_fund_pay_slip);
        leavePayText = findViewById(R.id.leave_without_pay_text);
        leavePay = findViewById(R.id.leave_without_pay_slip);
        absentDeducText = findViewById(R.id.absent_deduc_text);
        absentDeduc = findViewById(R.id.absent_deduc_pay_slip);
        loanDeducOneTime = findViewById(R.id.loan_deduc_one_time_pay_slip);
        loanDeducSched = findViewById(R.id.loan_deduc_shedule_pay_slip);
        loanDeducProFun = findViewById(R.id.loan_deduc_pf_pay_slip);
        taxDeduc = findViewById(R.id.tax_deduction_pay_slip);
        lunchDeduc = findViewById(R.id.lunch_deduc_pay_slip);
        stamp = findViewById(R.id.stamp_pay_slip);
        otherDeduc = findViewById(R.id.other_deduct_pay_slip);
        loanDeducBoard = findViewById(R.id.loan_deduc_from_board_pay_slip);
        totalDeduc = findViewById(R.id.total_deduction_pay_slip);

        netPayment = findViewById(R.id.net_payment_pay_slip);
        inWord = findViewById(R.id.in_word_pay_slip);

        accountName = findViewById(R.id.account_name_pay_slip);
        accountNo = findViewById(R.id.account_number_pay_slip);
        bankName = findViewById(R.id.bank_name_pay_slip);

        close = findViewById(R.id.pay_slip_finish);

        emp_id = userInfoLists.get(0).getEmp_id();

        if (userInfoLists.size() != 0) {
            String firstname = userInfoLists.get(0).getUser_fname();
            String lastName = userInfoLists.get(0).getUser_lname();
            if (firstname == null) {
                firstname = "";
            }
            if (lastName == null) {
                lastName = "";
            }
            emp_name = firstname+" "+lastName;
            user_id = userInfoLists.get(0).getUserName();

        }

        if (userDesignations.size() != 0) {
            str_DES = userDesignations.get(0).getJsm_name();
            if (str_DES == null) {
                str_DES = "";
            }
            ban = userDesignations.get(0).getDesg_priority();
            if (ban == null) {
                ban = "";
            }
            job_pos = userDesignations.get(0).getDesg_name();
            if (job_pos == null) {
                job_pos = "";
            }
            div = userDesignations.get(0).getDiv_name();
            if (div == null) {
                div = "";
            }
            dep = userDesignations.get(0).getDept_name();
            if (dep == null) {
                dep = "";
            }
        }

        email_name = userInfoLists.get(0).getEmail();


        selectMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MonthSelectDialogue monthSelectDialogue = new MonthSelectDialogue(PaySlip.this);
                monthSelectDialogue.show(getSupportFragmentManager(),"MONTH");
            }
        });

        selectMonth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                errorMsgMonth.setVisibility(View.GONE);
                download.setVisibility(View.GONE);
                reportCard.setVisibility(View.GONE);
                new Check().execute();

            }
        });

//        show.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                errorMsgMonth.setVisibility(View.GONE);
//                download.setVisibility(View.GONE);
//                reportCard.setVisibility(View.GONE);
//                if (!select_month_id.isEmpty()) {
//                    new Check().execute();
//                } else {
//                    Toast.makeText(getApplicationContext(), "Please Select Month", Toast.LENGTH_SHORT).show();
//                    errorMsgMonth.setVisibility(View.VISIBLE);
//                }
//            }
//        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_month_id = "";
                finish();
            }
        });

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PaySlip.this);
                builder.setTitle("Download Pay Slip!")
                        .setMessage("Do you want to download this pay slip?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                new DownloadPDF().execute();

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


    }

    @Override
    public void onBackPressed() {
        select_month_id = "";
        finish();
    }

    public void Download(String url, String title) {

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        String tempTitle = title.replace(" ", "_");
        request.setTitle(tempTitle);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        }

        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, tempTitle+".pdf");
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        request.setMimeType("application/pdf");
        request.allowScanningByMediaScanner();
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        downloadManager.enqueue(request);
        downConn = true;

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

    public class DownloadPDF extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(getApplicationContext());
            pDialog.setMessage("Downloading...");
            pDialog.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isConnected() && isOnline()) {

                Download(URL, "Pay Slip"+" "+select_month_id);

            } else {
                downConn = false;
//                message = "Not Connected";
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            pDialog.dismiss();
            if (downConn) {
                Toast.makeText(getApplicationContext(), "Downloading...", Toast.LENGTH_SHORT).show();
                downConn = false;
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(PaySlip.this)
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

                        new DownloadPDF().execute();
                        dialog.dismiss();
                    }
                });

                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();

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

                PaySlipDetails();
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

                errorMsgMonth.setVisibility(View.GONE);

                reportCard.setVisibility(View.VISIBLE);

                monthName.setText(selectMonth.getText().toString());

                empName.setText(emp_name);
                id.setText(user_id);
                band.setText(ban);
                strDes.setText(str_DES);
                jobPosition.setText(job_pos);
                division.setText(div);
                department.setText(dep);

                if (email_name != null) {
                    if (!email_name.isEmpty()) {
                        email.setText(email_name);
                    } else {
                        email.setText("Not Applicable");
                    }
                } else {
                    email.setText("Not Applicable");
                }


                if (off_loc != null) {
                    if (!off_loc.isEmpty()) {
                        officeLoc.setText(off_loc);
                    } else {
                        officeLoc.setText("Not Applicable");
                    }
                } else {
                    officeLoc.setText("Not Applicable");
                }

                if (off_ext != null) {
                    if (!off_ext.isEmpty()) {
                        officeExt.setText(off_ext);
                    } else {
                        officeExt.setText("Not Applicable");
                    }
                } else {
                    officeExt.setText("Not Applicable");
                }

                if (mobile_no != null) {
                    if (!mobile_no.isEmpty()) {
                        mobile.setText(mobile_no);
                    } else {
                        mobile.setText("Not Applicable");
                    }
                } else {
                    mobile.setText("Not Applicable");
                }

                if (charge != null) {
                    if (!charge.isEmpty()) {
                        addCharge.setText(charge);
                    } else {
                        addCharge.setText("Not Applicable");
                    }
                } else {
                    addCharge.setText("Not Applicable");
                }

                if (count.equals("1")) {
                    salaryLay.setVisibility(View.VISIBLE);
                    salaryData.setVisibility(View.GONE);
                    download.setVisibility(View.VISIBLE);
                } else {
                    salaryLay.setVisibility(View.GONE);
                    salaryData.setVisibility(View.VISIBLE);
                    download.setVisibility(View.GONE);
                }
                basic.setText(basic_salary);
                houseRent.setText(hou_rent);
                food.setText(food_cost);
                conveyance.setText(conveyance_cost);
                grossSalary.setText(gross_salary);
                overTime.setText(over_time);
                attBonus.setText(att_bon);
                otherAllow.setText(other_allow);
                totalPerquisite.setText(total_perqqq);

                providentFund.setText(prov_fun);
                leavePayText.setText(leavePayText.getText().toString() + leave_pay_days);
                leavePay.setText(leave_pay);
                absentDeducText.setText(absentDeducText.getText().toString() + abs_ded_days);
                absentDeduc.setText(abs_ded);
                loanDeducOneTime.setText(loan_ded_one);
                loanDeducSched.setText(loan_ded_sche);
                loanDeducProFun.setText(loan_ded_pf);
                taxDeduc.setText(tax_ded);
                lunchDeduc.setText(lun_ded);
                stamp.setText(stam);
                otherDeduc.setText(oth_ded);
                loanDeducBoard.setText(loan_ded_boa);
                totalDeduc.setText(total_ded);

                netPayment.setText(total);
                inWord.setText(total_word);

                accountName.setText(acc_name);
                accountNo.setText(acc_no);
                bankName.setText(bank);





            }else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(PaySlip.this)
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

    public void PaySlipDetails() {
        try {
            this.connection = createConnection();

            Statement stmt = connection.createStatement();

             basic_salary = "";
             hou_rent = "";
             food_cost = "";
             conveyance_cost = "";
             gross_salary = "";
             over_time = "";
             att_bon = "";
             other_allow = "";
             total_perqqq = "";

             prov_fun = "";
             leave_pay = "";
             leave_pay_days = "";
             abs_ded_days = "";
             abs_ded = "";
             loan_ded_one = "";
             loan_ded_sche = "";
             loan_ded_pf = "";
             tax_ded = "";
             lun_ded = "";
             stam = "";
             oth_ded = "";
             loan_ded_boa = "";
             total_ded = "";

             total = "";
             total_word = "";

             acc_name = "";
             acc_no = "";
             bank = "";



            ResultSet rs=stmt.executeQuery(" Select JOB_PABX_CORPORATE, JOB_AD_CHARGE, \n" +
                    "                    (Select COMPANY_OFFICE_ADDRESS.COA_NAME FROM COMPANY_OFFICE_ADDRESS WHERE COA_ID  = JOB_PRI_COA_ID) as office,\n" +
                    "                    (Select listagg(ESH_MBL_SIM,','||chr(10)) within group(order by ESH_MBL_SIM) ESH_MBL_SIM  FROM EMP_SIM_HISTORY WHERE EMP_SIM_HISTORY.ESH_JOB_ID = JOB_ID group by ESH_JOB_ID) as mobile\n" +
                    "                    FROM EMP_JOB_HISTORY where JOB_EMP_ID = "+emp_id+"");



            while(rs.next())  {
                 off_loc = rs.getString(3);
                 off_ext = rs.getString(1);
                 mobile_no = rs.getString(4);
                 charge = rs.getString(2);
            }

            ResultSet rs1 = stmt.executeQuery("select count(1) from  salary_mst m, salary_dtl d\n" +
                    "where m.sm_id = d.sd_sm_id\n" +
                    "and d.sd_emp_id = "+emp_id+"\n" +
                    "and to_char(m.sm_pms_month,'MMRRRR') = to_char(to_date('"+select_month_id+"'),'MMRRRR')");

            while (rs1.next()) {
                count = rs1.getString(1);
            }

            ResultSet resultSet = stmt.executeQuery("SELECT DISTINCT\n" +
                    "       SALARY_DTL.SD_ATTD_BONUS_AMT,\n" +
                    "       EMP_BANK_ACC_MST.EBAM_ACC_NUMBER,\n" +
                    "       EMP_BANK_ACC_MST.EBAM_BANK_ACCOUNT_NAME,\n" +
                    "       EMP_BANK_ACC_MST.EBAM_BANK_NAME,\n" +
                    "       SALARY_DTL.SD_BASIC,\n" +
                    "       SALARY_DTL.SD_HR,\n" +
                    "       SALARY_DTL.SD_FOOD_SUBSIDY_AMT,\n" +
                    "       '(Days '||SD_ABSENT||' )' absent_days,\n" +
                    "       SALARY_DTL.SD_ABSENT_AMT,\n" +
                    "       SALARY_DTL.SD_PF,\n" +
                    "       SALARY_DTL.SD_TAX,\n" +
                    "       SALARY_DTL.SD_OTH_DEDUCT,\n" +
                    "       SALARY_DTL.SD_ADVANCE_DEDUCT,\n" +
                    "       SALARY_DTL.SD_TA,\n" +
                    "       SALARY_DTL.SD_GROSS_SAL,\n" +
                    "       SALARY_DTL.SD_STAMP,\n" +
                    "       '(Days '||SD_LWPAY||' )' lwPayDays,\n" +
                    "       SALARY_DTL.SD_LWPAY_AMT,\n" +
                    "       ROUND ( (NVL (SALARY_DTL.SD_OT_HR, 0) * SALARY_DTL.SD_OT_RATE))\n" +
                    "          TOTAL_OT_AMT,\n" +
                    "       SALARY_DTL.SD_MD_ADVANCE_DEDUCT,\n" +
                    "       SALARY_DTL.SD_SCH_ADVANCE_DEDUCT,\n" +
                    "       SALARY_DTL.SD_PF_LOAN_DEDUCT,\n" +
                    "       SALARY_DTL.SD_LUNCH_DEDUCT,\n" +
                    "       (  NVL (SD_JOB_HABITATION, 0)\n" +
                    "        + NVL (SD_JOB_UTILITIES, 0)\n" +
                    "        + NVL (SD_JOB_HOUSE_ALLOWANCE, 0)\n" +
                    "        + NVL (SD_JOB_ENTERTAINMENT, 0)\n" +
                    "        + NVL (SD_COMMITTED_SALARY, 0)\n" +
                    "        + NVL (SD_FIXED_OT_AMT, 0)\n" +
                    "        + NVL (SD_FOOD_SUBSIDY_AMT, 0)\n" +
                    "        + NVL (SD_FACTORY_ALLOWANCE_AMT, 0)\n" +
                    "        + NVL (SD_HOLIDAY_AMT, 0)\n" +
                    "        + NVL (SD_REFUND_OTH_PAYMENT_AMT, 0)\n" +
                    "        + NVL (SD_PROFIT_SHARE_AMT, 0)\n" +
                    "        + NVL (SD_PERFORMANCE_BONUS_AMT, 0))\n" +
                    "          Other_allowances,\n" +
                    "          (nvl(SALARY_DTL.SD_ATTD_BONUS_AMT,0)+\n" +
                    "       nvl(SALARY_DTL.SD_GROSS_SAL,0)+(ROUND ( (NVL (SALARY_DTL.SD_OT_HR, 0) * NVL(SALARY_DTL.SD_OT_RATE,0))))+\n" +
                    "       (  NVL (SD_JOB_HABITATION, 0)\n" +
                    "        + NVL (SD_JOB_UTILITIES, 0)\n" +
                    "        + NVL (SD_JOB_HOUSE_ALLOWANCE, 0)\n" +
                    "        + NVL (SD_JOB_ENTERTAINMENT, 0)\n" +
                    "        + NVL (SD_COMMITTED_SALARY, 0)\n" +
                    "        + NVL (SD_FIXED_OT_AMT, 0)\n" +
                    "        + NVL (SD_FOOD_SUBSIDY_AMT, 0)\n" +
                    "        + NVL (SD_FACTORY_ALLOWANCE_AMT, 0)\n" +
                    "        + NVL (SD_HOLIDAY_AMT, 0)\n" +
                    "        + NVL (SD_REFUND_OTH_PAYMENT_AMT, 0)\n" +
                    "        + NVL (SD_PROFIT_SHARE_AMT, 0)\n" +
                    "        + NVL (SD_PERFORMANCE_BONUS_AMT, 0))) total_salary  ,\n" +
                    "        (nvl(SALARY_DTL.SD_PF,0)+\n" +
                    "       nvl(SALARY_DTL.SD_LWPAY_AMT,0)+ \n" +
                    "       nvl(SALARY_DTL.SD_ABSENT_AMT,0)+\n" +
                    "       nvl(SALARY_DTL.SD_ADVANCE_DEDUCT,0)+\n" +
                    "       nvl(SALARY_DTL.SD_SCH_ADVANCE_DEDUCT,0)+\n" +
                    "       nvl(SALARY_DTL.SD_PF_LOAN_DEDUCT,0)+\n" +
                    "       nvl(SALARY_DTL.SD_TAX,0)+\n" +
                    "       nvl(SALARY_DTL.SD_LUNCH_DEDUCT,0)+\n" +
                    "       nvl(SALARY_DTL.SD_STAMP,0)+\n" +
                    "       nvl(SALARY_DTL.SD_OTH_DEDUCT,0)+\n" +
                    "       nvl(SALARY_DTL.SD_MD_ADVANCE_DEDUCT,0)) Total_Deduction    ,\n" +
                    "       ((nvl(SALARY_DTL.SD_ATTD_BONUS_AMT,0)+\n" +
                    "       nvl(SALARY_DTL.SD_GROSS_SAL,0)+(ROUND ( (NVL (SALARY_DTL.SD_OT_HR, 0) * NVL(SALARY_DTL.SD_OT_RATE,0))))+\n" +
                    "       (  NVL (SD_JOB_HABITATION, 0)\n" +
                    "        + NVL (SD_JOB_UTILITIES, 0)\n" +
                    "        + NVL (SD_JOB_HOUSE_ALLOWANCE, 0)\n" +
                    "        + NVL (SD_JOB_ENTERTAINMENT, 0)\n" +
                    "        + NVL (SD_COMMITTED_SALARY, 0)\n" +
                    "        + NVL (SD_FIXED_OT_AMT, 0)\n" +
                    "        + NVL (SD_FOOD_SUBSIDY_AMT, 0)\n" +
                    "        + NVL (SD_FACTORY_ALLOWANCE_AMT, 0)\n" +
                    "        + NVL (SD_HOLIDAY_AMT, 0)\n" +
                    "        + NVL (SD_REFUND_OTH_PAYMENT_AMT, 0)\n" +
                    "        + NVL (SD_PROFIT_SHARE_AMT, 0)\n" +
                    "        + NVL (SD_PERFORMANCE_BONUS_AMT, 0)))-  (nvl(SALARY_DTL.SD_PF,0)+\n" +
                    "       nvl(SALARY_DTL.SD_LWPAY_AMT,0)+ \n" +
                    "       nvl(SALARY_DTL.SD_ABSENT_AMT,0)+\n" +
                    "       nvl(SALARY_DTL.SD_ADVANCE_DEDUCT,0)+\n" +
                    "       nvl(SALARY_DTL.SD_SCH_ADVANCE_DEDUCT,0)+\n" +
                    "       nvl(SALARY_DTL.SD_PF_LOAN_DEDUCT,0)+\n" +
                    "       nvl(SALARY_DTL.SD_TAX,0)+\n" +
                    "       nvl(SALARY_DTL.SD_LUNCH_DEDUCT,0)+\n" +
                    "       nvl(SALARY_DTL.SD_STAMP,0)+\n" +
                    "       nvl(SALARY_DTL.SD_OTH_DEDUCT,0)+\n" +
                    "       nvl(SALARY_DTL.SD_MD_ADVANCE_DEDUCT,0))) Net_SALARY,\n" +
                    "       inword(((nvl(SALARY_DTL.SD_ATTD_BONUS_AMT,0)+\n" +
                    "       nvl(SALARY_DTL.SD_GROSS_SAL,0)+(ROUND ( (NVL (SALARY_DTL.SD_OT_HR, 0) * NVL(SALARY_DTL.SD_OT_RATE,0))))+\n" +
                    "       (  NVL (SD_JOB_HABITATION, 0)\n" +
                    "        + NVL (SD_JOB_UTILITIES, 0)\n" +
                    "        + NVL (SD_JOB_HOUSE_ALLOWANCE, 0)\n" +
                    "        + NVL (SD_JOB_ENTERTAINMENT, 0)\n" +
                    "        + NVL (SD_COMMITTED_SALARY, 0)\n" +
                    "        + NVL (SD_FIXED_OT_AMT, 0)\n" +
                    "        + NVL (SD_FOOD_SUBSIDY_AMT, 0)\n" +
                    "        + NVL (SD_FACTORY_ALLOWANCE_AMT, 0)\n" +
                    "        + NVL (SD_HOLIDAY_AMT, 0)\n" +
                    "        + NVL (SD_REFUND_OTH_PAYMENT_AMT, 0)\n" +
                    "        + NVL (SD_PROFIT_SHARE_AMT, 0)\n" +
                    "        + NVL (SD_PERFORMANCE_BONUS_AMT, 0)))-  (nvl(SALARY_DTL.SD_PF,0)+\n" +
                    "       nvl(SALARY_DTL.SD_LWPAY_AMT,0)+ \n" +
                    "       nvl(SALARY_DTL.SD_ABSENT_AMT,0)+\n" +
                    "       nvl(SALARY_DTL.SD_ADVANCE_DEDUCT,0)+\n" +
                    "       nvl(SALARY_DTL.SD_SCH_ADVANCE_DEDUCT,0)+\n" +
                    "       nvl(SALARY_DTL.SD_PF_LOAN_DEDUCT,0)+\n" +
                    "       nvl(SALARY_DTL.SD_TAX,0)+\n" +
                    "       nvl(SALARY_DTL.SD_LUNCH_DEDUCT,0)+\n" +
                    "       nvl(SALARY_DTL.SD_STAMP,0)+\n" +
                    "       nvl(SALARY_DTL.SD_OTH_DEDUCT,0)+\n" +
                    "       nvl(SALARY_DTL.SD_MD_ADVANCE_DEDUCT,0)))) salary_in_word\n" +
                    "\n" +
                    "\n" +
                    "  FROM EMP_MST,\n" +
                    "       JOB_SETUP_DTL,\n" +
                    "       JOB_SETUP_MST,\n" +
                    "       DEPT_MST,\n" +
                    "       DIVISION_MST,\n" +
                    "       EMP_JOB_HISTORY,\n" +
                    "       EMP_ADOPTED_HISTORY,\n" +
                    "       DESIG_MST,\n" +
                    "       COMPANY_OFFICE_ADDRESS,\n" +
                    "       COMPANY_OFFICE_ADDRESS COMPANY_OFFICE_ADDRESS_A1,\n" +
                    "       SALARY_DTL,\n" +
                    "       EMP_BANK_ACC_MST,\n" +
                    "       SALARY_MST\n" +
                    " WHERE     (    (JOB_SETUP_DTL.JSD_JSM_ID = JOB_SETUP_MST.JSM_ID)\n" +
                    "            AND (JOB_SETUP_MST.JSM_DEPT_ID = DEPT_MST.DEPT_ID)\n" +
                    "            AND (JOB_SETUP_MST.JSM_DIVM_ID = DIVISION_MST.DIVM_ID)\n" +
                    "            AND (EMP_ADOPTED_HISTORY.EAH_JOB_ID(+) = EMP_JOB_HISTORY.JOB_ID)\n" +
                    "            AND (JOB_SETUP_MST.JSM_DESIG_ID = DESIG_MST.DESIG_ID)\n" +
                    "            AND (EMP_JOB_HISTORY.JOB_PRI_COA_ID =\n" +
                    "                    COMPANY_OFFICE_ADDRESS.COA_ID)\n" +
                    "            AND (EMP_BANK_ACC_MST.EBAM_EMP_ID(+) = EMP_MST.EMP_ID)\n" +
                    "            -----AND (JOB_SETUP_DTL.JSD_ID = EMP_MST.EMP_JSD_ID)\n" +
                    "            AND (JOB_SETUP_MST.JSM_ID = SALARY_DTL.SD_JSM_ID)\n" +
                    "            AND (EMP_JOB_HISTORY.JOB_ID = EMP_MST.EMP_JOB_ID)\n" +
                    "            AND (COMPANY_OFFICE_ADDRESS_A1.COA_ID(+) =\n" +
                    "                    EMP_JOB_HISTORY.JOB_SEC_COA_ID)\n" +
                    "            AND (EMP_MST.EMP_ID = SALARY_DTL.SD_EMP_ID)\n" +
                    "            AND (SALARY_DTL.SD_SM_ID = SALARY_MST.SM_ID))\n" +
                    "       AND EMP_MST.EMP_ID = "+emp_id+"\n" +
                    "       AND TO_DATE (TO_CHAR (TO_DATE (SALARY_MST.SM_PMS_MONTH), 'MM/RRRR'),\n" +
                    "                    'MM/RRRR') =\n" +
                    "              TO_DATE (TO_CHAR (TO_DATE ('"+select_month_id+"'), 'MM/RRRR'), 'MM/RRRR')");

            while (resultSet.next()) {
                att_bon = resultSet.getString(1);
                acc_no = resultSet.getString(2);
                acc_name = resultSet.getString(3);
                bank = resultSet.getString(4);
                basic_salary = resultSet.getString(5);
                hou_rent = resultSet.getString(6);
                food_cost = resultSet.getString(7);
                abs_ded_days = resultSet.getString(8);
                abs_ded = resultSet.getString(9);
                prov_fun = resultSet.getString(10);
                tax_ded = resultSet.getString(11);
                oth_ded = resultSet.getString(12);
                loan_ded_one = resultSet.getString(13);
                conveyance_cost = resultSet.getString(14);
                gross_salary = resultSet.getString(15);
                stam = resultSet.getString(16);
                leave_pay_days = resultSet.getString(17);
                leave_pay = resultSet.getString(18);
                over_time = resultSet.getString(19);
                loan_ded_boa = resultSet.getString(20);
                loan_ded_sche = resultSet.getString(21);
                loan_ded_pf = resultSet.getString(22);
                lun_ded = resultSet.getString(23);
                other_allow = resultSet.getString(24);
                total_perqqq = resultSet.getString(25);
                total_ded = resultSet.getString(26);
                total = resultSet.getString(27);
                total_word = resultSet.getString(28);
            }

            if (DEFAULT_USERNAME.equals("IKGL")) {
                URL = "http://103.56.208.123:7778/reports/rwservlet?hrsikgl+report=D:\\ibrahim_knit\\Reports\\PAY_SLIP.rep+EMPID="+emp_id+"+MMONTH='"+select_month_id+"'";
            }
            else if (DEFAULT_USERNAME.equals("TTRAMS")) {
                URL = "http://103.56.208.123:7778/reports/rwservlet?hrsikgl+report=D:\\ttit_rams\\Reports\\PAY_SLIP.rep+EMPID="+emp_id+"+MMONTH='"+select_month_id+"'";
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