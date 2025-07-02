package ttit.com.shuvo.ikglhrm.payRoll.paySlip;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.dewinjm.monthyearpicker.MonthFormat;
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import ttit.com.shuvo.ikglhrm.R;
import ttit.com.shuvo.ikglhrm.WaitProgress;

import static ttit.com.shuvo.ikglhrm.Login.userDesignations;
import static ttit.com.shuvo.ikglhrm.Login.userInfoLists;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.api_url_front;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PaySlip extends AppCompatActivity {

    TextInputEditText selectMonth;
    TextInputLayout selectMonthLay;

    CardView reportCard;

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
    TextView medical;
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

    String data_available_count = "";

    String select_month_id = "";
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
    String medical_cost = "";
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
    private Boolean conn = false;
    private Boolean connected = false;

    Logger logger = Logger.getLogger(PaySlip.class.getName());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_slip);

        select_month_id = "";

        selectMonth = findViewById(R.id.select_month_pay_slip);
        selectMonthLay = findViewById(R.id.select_month_pay_slip_lay);

        reportCard = findViewById(R.id.pay_slip_report_card);


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
        medical = findViewById(R.id.medical_pay_slip);
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

        emp_id = userInfoLists.get(0).getEmp_id();

        if (!userInfoLists.isEmpty()) {
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

        if (!userDesignations.isEmpty()) {
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


//        selectMonth.setOnClickListener(v -> {
//            MonthSelectDialogue monthSelectDialogue = new MonthSelectDialogue(PaySlip.this);
//            monthSelectDialogue.show(getSupportFragmentManager(),"MONTH");
//        });
//
//        selectMonth.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                reportCard.setVisibility(View.GONE);
////                new Check().execute();
//                getPaySlipData();
//
//            }
//        });

        selectMonth.setOnClickListener(v -> {

            Date c = Calendar.getInstance().getTime();

            String formattedYear;
            String monthValue;
            String lastformattedYear;
            String lastdateView;

            SimpleDateFormat df = new SimpleDateFormat("yyyy", Locale.ENGLISH);
            SimpleDateFormat sdf = new SimpleDateFormat("MM",Locale.ENGLISH);

            formattedYear = df.format(c);
            monthValue = sdf.format(c);
            int nowMonNumb = Integer.parseInt(monthValue);
            nowMonNumb = nowMonNumb - 2;
            int lastMonNumb = nowMonNumb - 11;

            if (lastMonNumb < 0) {
                lastMonNumb = lastMonNumb + 12;
                int formatY = Integer.parseInt(formattedYear);
                formatY = formatY - 1;
                lastformattedYear = String.valueOf(formatY);
            } else {
                lastformattedYear = formattedYear;
            }

            Date today = new Date();

            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(today);

            calendar1.set(Calendar.DAY_OF_MONTH, 1);
            calendar1.add(Calendar.DATE, -1);

            Date lastDayOfMonth = calendar1.getTime();

            SimpleDateFormat sdff = new SimpleDateFormat("dd",Locale.ENGLISH);
            lastdateView = sdff.format(lastDayOfMonth);

            int yearSelected;
            int monthSelected;
            MonthFormat monthFormat = MonthFormat.LONG;
            String customTitle = "Select Month";
// Use the calendar for create ranges
            Calendar calendar = Calendar.getInstance();
            if (!select_month_id.isEmpty()) {
                SimpleDateFormat myf = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);
                Date md = null;
                try {
                    md = myf.parse(select_month_id);
                } catch (ParseException e) {
                    logger.log(Level.WARNING, e.getMessage(), e);
                }

                if (md != null) {
                    calendar.setTime(md);
                }
            }
            yearSelected = calendar.get(Calendar.YEAR);
            monthSelected = calendar.get(Calendar.MONTH);
            calendar.clear();
            calendar.set(Integer.parseInt(lastformattedYear), lastMonNumb, 1); // Set minimum date to show in dialog
            long minDate = calendar.getTimeInMillis(); // Get milliseconds of the modified date

            calendar.clear();
            calendar.set(Integer.parseInt(formattedYear), nowMonNumb, Integer.parseInt(lastdateView)); // Set maximum date to show in dialog
            long maxDate = calendar.getTimeInMillis(); // Get milliseconds of the modified date

// Create instance with date ranges values
            MonthYearPickerDialogFragment dialogFragment =  MonthYearPickerDialogFragment
                    .getInstance(monthSelected, yearSelected, minDate, maxDate, customTitle, monthFormat);



            dialogFragment.show(getSupportFragmentManager(), null);

            dialogFragment.setOnDateSetListener((year, monthOfYear) -> {
                System.out.println(year);
                System.out.println(monthOfYear);

                int month = monthOfYear + 1;
                String monthName = "";
                String mon = "";
                String yearName;

                if (month == 1) {
                    monthName = "JANUARY";
                    mon = "JAN";
                } else if (month == 2) {
                    monthName = "FEBRUARY";
                    mon = "FEB";
                } else if (month == 3) {
                    monthName = "MARCH";
                    mon = "MAR";
                } else if (month == 4) {
                    monthName = "APRIL";
                    mon = "APR";
                } else if (month == 5) {
                    monthName = "MAY";
                    mon = "MAY";
                } else if (month == 6) {
                    monthName = "JUNE";
                    mon = "JUN";
                } else if (month == 7) {
                    monthName = "JULY";
                    mon = "JUL";
                } else if (month == 8) {
                    monthName = "AUGUST";
                    mon = "AUG";
                } else if (month == 9) {
                    monthName = "SEPTEMBER";
                    mon = "SEP";
                } else if (month == 10) {
                    monthName = "OCTOBER";
                    mon = "OCT";
                } else if (month == 11) {
                    monthName = "NOVEMBER";
                    mon = "NOV";
                } else if (month == 12) {
                    monthName = "DECEMBER";
                    mon = "DEC";
                }

                yearName  = String.valueOf(year);
                yearName = yearName.substring(yearName.length()-2);

                select_month_id = "01-"+mon+"-"+yearName;
                //selected_date = "01-"+mon+"-"+yearName;
                String tt = monthName + "-" + year;
                selectMonth.setText(tt);
                selectMonthLay.setHint("Month");

                getPaySlipData();
            });

        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                select_month_id = "";
                finish();
            }
        });

    }

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
//                PaySlipDetails();
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
//                errorMsgMonth.setVisibility(View.GONE);
//
//                reportCard.setVisibility(View.VISIBLE);
//
//                monthName.setText(selectMonth.getText().toString());
//
//                empName.setText(emp_name);
//                id.setText(user_id);
//                band.setText(ban);
//                strDes.setText(str_DES);
//                jobPosition.setText(job_pos);
//                division.setText(div);
//                department.setText(dep);
//
//                if (email_name != null) {
//                    if (!email_name.isEmpty()) {
//                        email.setText(email_name);
//                    } else {
//                        email.setText("Not Applicable");
//                    }
//                } else {
//                    email.setText("Not Applicable");
//                }
//
//
//                if (off_loc != null) {
//                    if (!off_loc.isEmpty()) {
//                        officeLoc.setText(off_loc);
//                    } else {
//                        officeLoc.setText("Not Applicable");
//                    }
//                } else {
//                    officeLoc.setText("Not Applicable");
//                }
//
//                if (off_ext != null) {
//                    if (!off_ext.isEmpty()) {
//                        officeExt.setText(off_ext);
//                    } else {
//                        officeExt.setText("Not Applicable");
//                    }
//                } else {
//                    officeExt.setText("Not Applicable");
//                }
//
//                if (mobile_no != null) {
//                    if (!mobile_no.isEmpty()) {
//                        mobile.setText(mobile_no);
//                    } else {
//                        mobile.setText("Not Applicable");
//                    }
//                } else {
//                    mobile.setText("Not Applicable");
//                }
//
//                if (charge != null) {
//                    if (!charge.isEmpty()) {
//                        addCharge.setText(charge);
//                    } else {
//                        addCharge.setText("Not Applicable");
//                    }
//                } else {
//                    addCharge.setText("Not Applicable");
//                }
//
//                if (data_available_count.equals("1")) {
//                    salaryLay.setVisibility(View.VISIBLE);
//                    salaryData.setVisibility(View.GONE);
//                    download.setVisibility(View.VISIBLE);
//                } else {
//                    salaryLay.setVisibility(View.GONE);
//                    salaryData.setVisibility(View.VISIBLE);
//                    download.setVisibility(View.GONE);
//                }
//                basic.setText(basic_salary);
//                houseRent.setText(hou_rent);
//                food.setText(food_cost);
//                conveyance.setText(conveyance_cost);
//                grossSalary.setText(gross_salary);
//                overTime.setText(over_time);
//                attBonus.setText(att_bon);
//                otherAllow.setText(other_allow);
//                totalPerquisite.setText(total_perqqq);
//
//                providentFund.setText(prov_fun);
//                leavePayText.setText(leavePayText.getText().toString() + leave_pay_days);
//                leavePay.setText(leave_pay);
//                absentDeducText.setText(absentDeducText.getText().toString() + abs_ded_days);
//                absentDeduc.setText(abs_ded);
//                loanDeducOneTime.setText(loan_ded_one);
//                loanDeducSched.setText(loan_ded_sche);
//                loanDeducProFun.setText(loan_ded_pf);
//                taxDeduc.setText(tax_ded);
//                lunchDeduc.setText(lun_ded);
//                stamp.setText(stam);
//                otherDeduc.setText(oth_ded);
//                loanDeducBoard.setText(loan_ded_boa);
//                totalDeduc.setText(total_ded);
//
//                netPayment.setText(total);
//                inWord.setText(total_word);
//
//                accountName.setText(acc_name);
//                accountNo.setText(acc_no);
//                bankName.setText(bank);
//
//
//
//
//
//            }
//            else {
//                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
//                AlertDialog dialog = new AlertDialog.Builder(PaySlip.this)
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

//    public void PaySlipDetails() {
//        try {
//            this.connection = createConnection();
//
//            Statement stmt = connection.createStatement();
//
//             basic_salary = "";
//             hou_rent = "";
//             food_cost = "";
//             conveyance_cost = "";
//             gross_salary = "";
//             over_time = "";
//             att_bon = "";
//             other_allow = "";
//             total_perqqq = "";
//
//             prov_fun = "";
//             leave_pay = "";
//             leave_pay_days = "";
//             abs_ded_days = "";
//             abs_ded = "";
//             loan_ded_one = "";
//             loan_ded_sche = "";
//             loan_ded_pf = "";
//             tax_ded = "";
//             lun_ded = "";
//             stam = "";
//             oth_ded = "";
//             loan_ded_boa = "";
//             total_ded = "";
//
//             total = "";
//             total_word = "";
//
//             acc_name = "";
//             acc_no = "";
//             bank = "";
//
//
//
////            ResultSet rs=stmt.executeQuery(" Select JOB_PABX_CORPORATE, JOB_AD_CHARGE, \n" +
////                    "                    (Select COMPANY_OFFICE_ADDRESS.COA_NAME FROM COMPANY_OFFICE_ADDRESS WHERE COA_ID  = JOB_PRI_COA_ID) as office,\n" +
////                    "                    (Select listagg(ESH_MBL_SIM,','||chr(10)) within group(order by ESH_MBL_SIM) ESH_MBL_SIM  FROM EMP_SIM_HISTORY WHERE EMP_SIM_HISTORY.ESH_JOB_ID = JOB_ID group by ESH_JOB_ID) as mobile\n" +
////                    "                    FROM EMP_JOB_HISTORY where JOB_EMP_ID = "+emp_id+"");
////
////
////
////            while(rs.next())  {
////                 off_loc = rs.getString(3);
////                 off_ext = rs.getString(1);
////                 mobile_no = rs.getString(4);
////                 charge = rs.getString(2);
////            }
//
////            ResultSet rs1 = stmt.executeQuery("select count(1) from  salary_mst m, salary_dtl d\n" +
////                    "where m.sm_id = d.sd_sm_id\n" +
////                    "and d.sd_emp_id = "+emp_id+"\n" +
////                    "and to_char(m.sm_pms_month,'MMRRRR') = to_char(to_date('"+select_month_id+"'),'MMRRRR')");
////
////            while (rs1.next()) {
////                data_available_count = rs1.getString(1);
////            }
//
//            ResultSet resultSet = stmt.executeQuery("SELECT DISTINCT\n" +
//                    "       SALARY_DTL.SD_ATTD_BONUS_AMT,\n" +
//                    "       EMP_BANK_ACC_MST.EBAM_ACC_NUMBER,\n" +
//                    "       EMP_BANK_ACC_MST.EBAM_BANK_ACCOUNT_NAME,\n" +
//                    "       EMP_BANK_ACC_MST.EBAM_BANK_NAME,\n" +
//                    "       SALARY_DTL.SD_BASIC,\n" +
//                    "       SALARY_DTL.SD_HR,\n" +
//                    "       SALARY_DTL.SD_FOOD_SUBSIDY_AMT,\n" +
//                    "       '(Days '||SD_ABSENT||' )' absent_days,\n" +
//                    "       SALARY_DTL.SD_ABSENT_AMT,\n" +
//                    "       SALARY_DTL.SD_PF,\n" +
//                    "       SALARY_DTL.SD_TAX,\n" +
//                    "       SALARY_DTL.SD_OTH_DEDUCT,\n" +
//                    "       SALARY_DTL.SD_ADVANCE_DEDUCT,\n" +
//                    "       SALARY_DTL.SD_TA,\n" +
//                    "       SALARY_DTL.SD_GROSS_SAL,\n" +
//                    "       SALARY_DTL.SD_STAMP,\n" +
//                    "       '(Days '||SD_LWPAY||' )' lwPayDays,\n" +
//                    "       SALARY_DTL.SD_LWPAY_AMT,\n" +
//                    "       ROUND ( (NVL (SALARY_DTL.SD_OT_HR, 0) * SALARY_DTL.SD_OT_RATE))\n" +
//                    "          TOTAL_OT_AMT,\n" +
//                    "       SALARY_DTL.SD_MD_ADVANCE_DEDUCT,\n" +
//                    "       SALARY_DTL.SD_SCH_ADVANCE_DEDUCT,\n" +
//                    "       SALARY_DTL.SD_PF_LOAN_DEDUCT,\n" +
//                    "       SALARY_DTL.SD_LUNCH_DEDUCT,\n" +
//                    "       (  NVL (SD_JOB_HABITATION, 0)\n" +
//                    "        + NVL (SD_JOB_UTILITIES, 0)\n" +
//                    "        + NVL (SD_JOB_HOUSE_ALLOWANCE, 0)\n" +
//                    "        + NVL (SD_JOB_ENTERTAINMENT, 0)\n" +
//                    "        + NVL (SD_COMMITTED_SALARY, 0)\n" +
//                    "        + NVL (SD_FIXED_OT_AMT, 0)\n" +
//                    "        + NVL (SD_FOOD_SUBSIDY_AMT, 0)\n" +
//                    "        + NVL (SD_FACTORY_ALLOWANCE_AMT, 0)\n" +
//                    "        + NVL (SD_HOLIDAY_AMT, 0)\n" +
//                    "        + NVL (SD_REFUND_OTH_PAYMENT_AMT, 0)\n" +
//                    "        + NVL (SD_PROFIT_SHARE_AMT, 0)\n" +
//                    "        + NVL (SD_PERFORMANCE_BONUS_AMT, 0))\n" +
//                    "          Other_allowances,\n" +
//                    "          (nvl(SALARY_DTL.SD_ATTD_BONUS_AMT,0)+\n" +
//                    "       nvl(SALARY_DTL.SD_GROSS_SAL,0)+(ROUND ( (NVL (SALARY_DTL.SD_OT_HR, 0) * NVL(SALARY_DTL.SD_OT_RATE,0))))+\n" +
//                    "       (  NVL (SD_JOB_HABITATION, 0)\n" +
//                    "        + NVL (SD_JOB_UTILITIES, 0)\n" +
//                    "        + NVL (SD_JOB_HOUSE_ALLOWANCE, 0)\n" +
//                    "        + NVL (SD_JOB_ENTERTAINMENT, 0)\n" +
//                    "        + NVL (SD_COMMITTED_SALARY, 0)\n" +
//                    "        + NVL (SD_FIXED_OT_AMT, 0)\n" +
//                    "        + NVL (SD_FOOD_SUBSIDY_AMT, 0)\n" +
//                    "        + NVL (SD_FACTORY_ALLOWANCE_AMT, 0)\n" +
//                    "        + NVL (SD_HOLIDAY_AMT, 0)\n" +
//                    "        + NVL (SD_REFUND_OTH_PAYMENT_AMT, 0)\n" +
//                    "        + NVL (SD_PROFIT_SHARE_AMT, 0)\n" +
//                    "        + NVL (SD_PERFORMANCE_BONUS_AMT, 0))) total_salary  ,\n" +
//                    "        (nvl(SALARY_DTL.SD_PF,0)+\n" +
//                    "       nvl(SALARY_DTL.SD_LWPAY_AMT,0)+ \n" +
//                    "       nvl(SALARY_DTL.SD_ABSENT_AMT,0)+\n" +
//                    "       nvl(SALARY_DTL.SD_ADVANCE_DEDUCT,0)+\n" +
//                    "       nvl(SALARY_DTL.SD_SCH_ADVANCE_DEDUCT,0)+\n" +
//                    "       nvl(SALARY_DTL.SD_PF_LOAN_DEDUCT,0)+\n" +
//                    "       nvl(SALARY_DTL.SD_TAX,0)+\n" +
//                    "       nvl(SALARY_DTL.SD_LUNCH_DEDUCT,0)+\n" +
//                    "       nvl(SALARY_DTL.SD_STAMP,0)+\n" +
//                    "       nvl(SALARY_DTL.SD_OTH_DEDUCT,0)+\n" +
//                    "       nvl(SALARY_DTL.SD_MD_ADVANCE_DEDUCT,0)) Total_Deduction    ,\n" +
//                    "       ((nvl(SALARY_DTL.SD_ATTD_BONUS_AMT,0)+\n" +
//                    "       nvl(SALARY_DTL.SD_GROSS_SAL,0)+(ROUND ( (NVL (SALARY_DTL.SD_OT_HR, 0) * NVL(SALARY_DTL.SD_OT_RATE,0))))+\n" +
//                    "       (  NVL (SD_JOB_HABITATION, 0)\n" +
//                    "        + NVL (SD_JOB_UTILITIES, 0)\n" +
//                    "        + NVL (SD_JOB_HOUSE_ALLOWANCE, 0)\n" +
//                    "        + NVL (SD_JOB_ENTERTAINMENT, 0)\n" +
//                    "        + NVL (SD_COMMITTED_SALARY, 0)\n" +
//                    "        + NVL (SD_FIXED_OT_AMT, 0)\n" +
//                    "        + NVL (SD_FOOD_SUBSIDY_AMT, 0)\n" +
//                    "        + NVL (SD_FACTORY_ALLOWANCE_AMT, 0)\n" +
//                    "        + NVL (SD_HOLIDAY_AMT, 0)\n" +
//                    "        + NVL (SD_REFUND_OTH_PAYMENT_AMT, 0)\n" +
//                    "        + NVL (SD_PROFIT_SHARE_AMT, 0)\n" +
//                    "        + NVL (SD_PERFORMANCE_BONUS_AMT, 0)))-  (nvl(SALARY_DTL.SD_PF,0)+\n" +
//                    "       nvl(SALARY_DTL.SD_LWPAY_AMT,0)+ \n" +
//                    "       nvl(SALARY_DTL.SD_ABSENT_AMT,0)+\n" +
//                    "       nvl(SALARY_DTL.SD_ADVANCE_DEDUCT,0)+\n" +
//                    "       nvl(SALARY_DTL.SD_SCH_ADVANCE_DEDUCT,0)+\n" +
//                    "       nvl(SALARY_DTL.SD_PF_LOAN_DEDUCT,0)+\n" +
//                    "       nvl(SALARY_DTL.SD_TAX,0)+\n" +
//                    "       nvl(SALARY_DTL.SD_LUNCH_DEDUCT,0)+\n" +
//                    "       nvl(SALARY_DTL.SD_STAMP,0)+\n" +
//                    "       nvl(SALARY_DTL.SD_OTH_DEDUCT,0)+\n" +
//                    "       nvl(SALARY_DTL.SD_MD_ADVANCE_DEDUCT,0))) Net_SALARY,\n" +
//                    "       inword(((nvl(SALARY_DTL.SD_ATTD_BONUS_AMT,0)+\n" +
//                    "       nvl(SALARY_DTL.SD_GROSS_SAL,0)+(ROUND ( (NVL (SALARY_DTL.SD_OT_HR, 0) * NVL(SALARY_DTL.SD_OT_RATE,0))))+\n" +
//                    "       (  NVL (SD_JOB_HABITATION, 0)\n" +
//                    "        + NVL (SD_JOB_UTILITIES, 0)\n" +
//                    "        + NVL (SD_JOB_HOUSE_ALLOWANCE, 0)\n" +
//                    "        + NVL (SD_JOB_ENTERTAINMENT, 0)\n" +
//                    "        + NVL (SD_COMMITTED_SALARY, 0)\n" +
//                    "        + NVL (SD_FIXED_OT_AMT, 0)\n" +
//                    "        + NVL (SD_FOOD_SUBSIDY_AMT, 0)\n" +
//                    "        + NVL (SD_FACTORY_ALLOWANCE_AMT, 0)\n" +
//                    "        + NVL (SD_HOLIDAY_AMT, 0)\n" +
//                    "        + NVL (SD_REFUND_OTH_PAYMENT_AMT, 0)\n" +
//                    "        + NVL (SD_PROFIT_SHARE_AMT, 0)\n" +
//                    "        + NVL (SD_PERFORMANCE_BONUS_AMT, 0)))-  (nvl(SALARY_DTL.SD_PF,0)+\n" +
//                    "       nvl(SALARY_DTL.SD_LWPAY_AMT,0)+ \n" +
//                    "       nvl(SALARY_DTL.SD_ABSENT_AMT,0)+\n" +
//                    "       nvl(SALARY_DTL.SD_ADVANCE_DEDUCT,0)+\n" +
//                    "       nvl(SALARY_DTL.SD_SCH_ADVANCE_DEDUCT,0)+\n" +
//                    "       nvl(SALARY_DTL.SD_PF_LOAN_DEDUCT,0)+\n" +
//                    "       nvl(SALARY_DTL.SD_TAX,0)+\n" +
//                    "       nvl(SALARY_DTL.SD_LUNCH_DEDUCT,0)+\n" +
//                    "       nvl(SALARY_DTL.SD_STAMP,0)+\n" +
//                    "       nvl(SALARY_DTL.SD_OTH_DEDUCT,0)+\n" +
//                    "       nvl(SALARY_DTL.SD_MD_ADVANCE_DEDUCT,0)))) salary_in_word\n" +
//                    "\n" +
//                    "\n" +
//                    "  FROM EMP_MST,\n" +
//                    "       JOB_SETUP_DTL,\n" +
//                    "       JOB_SETUP_MST,\n" +
//                    "       DEPT_MST,\n" +
//                    "       DIVISION_MST,\n" +
//                    "       EMP_JOB_HISTORY,\n" +
//                    "       EMP_ADOPTED_HISTORY,\n" +
//                    "       DESIG_MST,\n" +
//                    "       COMPANY_OFFICE_ADDRESS,\n" +
//                    "       COMPANY_OFFICE_ADDRESS COMPANY_OFFICE_ADDRESS_A1,\n" +
//                    "       SALARY_DTL,\n" +
//                    "       EMP_BANK_ACC_MST,\n" +
//                    "       SALARY_MST\n" +
//                    " WHERE     (    (JOB_SETUP_DTL.JSD_JSM_ID = JOB_SETUP_MST.JSM_ID)\n" +
//                    "            AND (JOB_SETUP_MST.JSM_DEPT_ID = DEPT_MST.DEPT_ID)\n" +
//                    "            AND (JOB_SETUP_MST.JSM_DIVM_ID = DIVISION_MST.DIVM_ID)\n" +
//                    "            AND (EMP_ADOPTED_HISTORY.EAH_JOB_ID(+) = EMP_JOB_HISTORY.JOB_ID)\n" +
//                    "            AND (JOB_SETUP_MST.JSM_DESIG_ID = DESIG_MST.DESIG_ID)\n" +
//                    "            AND (EMP_JOB_HISTORY.JOB_PRI_COA_ID =\n" +
//                    "                    COMPANY_OFFICE_ADDRESS.COA_ID)\n" +
//                    "            AND (EMP_BANK_ACC_MST.EBAM_EMP_ID(+) = EMP_MST.EMP_ID)\n" +
//                    "            -----AND (JOB_SETUP_DTL.JSD_ID = EMP_MST.EMP_JSD_ID)\n" +
//                    "            AND (JOB_SETUP_MST.JSM_ID = SALARY_DTL.SD_JSM_ID)\n" +
//                    "            AND (EMP_JOB_HISTORY.JOB_ID = EMP_MST.EMP_JOB_ID)\n" +
//                    "            AND (COMPANY_OFFICE_ADDRESS_A1.COA_ID(+) =\n" +
//                    "                    EMP_JOB_HISTORY.JOB_SEC_COA_ID)\n" +
//                    "            AND (EMP_MST.EMP_ID = SALARY_DTL.SD_EMP_ID)\n" +
//                    "            AND (SALARY_DTL.SD_SM_ID = SALARY_MST.SM_ID))\n" +
//                    "       AND EMP_MST.EMP_ID = "+emp_id+"\n" +
//                    "       AND TO_DATE (TO_CHAR (TO_DATE (SALARY_MST.SM_PMS_MONTH), 'MM/RRRR'),\n" +
//                    "                    'MM/RRRR') =\n" +
//                    "              TO_DATE (TO_CHAR (TO_DATE ('"+select_month_id+"'), 'MM/RRRR'), 'MM/RRRR')");
//
//            while (resultSet.next()) {
//                att_bon = resultSet.getString(1);
//                acc_no = resultSet.getString(2);
//                acc_name = resultSet.getString(3);
//                bank = resultSet.getString(4);
//                basic_salary = resultSet.getString(5);
//                hou_rent = resultSet.getString(6);
//                food_cost = resultSet.getString(7);
//                abs_ded_days = resultSet.getString(8);
//                abs_ded = resultSet.getString(9);
//                prov_fun = resultSet.getString(10);
//                tax_ded = resultSet.getString(11);
//                oth_ded = resultSet.getString(12);
//                loan_ded_one = resultSet.getString(13);
//                conveyance_cost = resultSet.getString(14);
//                gross_salary = resultSet.getString(15);
//                stam = resultSet.getString(16);
//                leave_pay_days = resultSet.getString(17);
//                leave_pay = resultSet.getString(18);
//                over_time = resultSet.getString(19);
//                loan_ded_boa = resultSet.getString(20);
//                loan_ded_sche = resultSet.getString(21);
//                loan_ded_pf = resultSet.getString(22);
//                lun_ded = resultSet.getString(23);
//                other_allow = resultSet.getString(24);
//                total_perqqq = resultSet.getString(25);
//                total_ded = resultSet.getString(26);
//                total = resultSet.getString(27);
//                total_word = resultSet.getString(28);
//            }
//
//            if (DEFAULT_USERNAME.equals("IKGL")) {
//                URL = "http://103.56.208.123:7778/reports/rwservlet?hrsikgl+report=D:\\ibrahim_knit\\Reports\\PAY_SLIP.rep+EMPID="+emp_id+"+MMONTH='"+select_month_id+"'";
//            }
//            else if (DEFAULT_USERNAME.equals("TTRAMS")) {
//                URL = "http://103.56.208.123:7778/reports/rwservlet?hrsttrams+report=D:\\TTIT_RAMS\\Reports\\PAY_SLIP.rep+EMPID="+emp_id+"+MMONTH='"+select_month_id+"'";
//            }
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

    public void getPaySlipData() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;

        basic_salary = "";
        hou_rent = "";
        food_cost = "";
        medical_cost = "";
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

        String officeExtraUrl = api_url_front + "paySlip/getOfficeExtra/"+emp_id;
        String dataCCUrl = api_url_front + "paySlip/getCount/"+emp_id+"/"+select_month_id;
        String paySlipDataUrl = api_url_front + "paySlip/getPaySlipDetails/"+emp_id+"/"+select_month_id;

        RequestQueue requestQueue = Volley.newRequestQueue(PaySlip.this);

        StringRequest paySlipReq = new StringRequest(Request.Method.GET, paySlipDataUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject paySlipInfo = array.getJSONObject(i);

                        att_bon = paySlipInfo.getString("sd_attd_bonus_amt")
                                .equals("null") ? "0" : paySlipInfo.getString("sd_attd_bonus_amt");
                        acc_no = paySlipInfo.getString("ebam_acc_number")
                                .equals("null") ? "" : paySlipInfo.getString("ebam_acc_number");
                        acc_name = paySlipInfo.getString("ebam_bank_account_name")
                                .equals("null") ? "" : paySlipInfo.getString("ebam_bank_account_name");
                        bank = paySlipInfo.getString("ebam_bank_name")
                                .equals("null") ? "" : paySlipInfo.getString("ebam_bank_name");
                        basic_salary = paySlipInfo.getString("sd_basic")
                                .equals("null") ? "0" : paySlipInfo.getString("sd_basic");
                        hou_rent = paySlipInfo.getString("sd_hr")
                                .equals("null") ? "0" : paySlipInfo.getString("sd_hr");
                        food_cost = paySlipInfo.getString("sd_food_subsidy_amt")
                                .equals("null") ? "0" : paySlipInfo.getString("sd_food_subsidy_amt");
                        medical_cost = paySlipInfo.getString("sd_md")
                                .equals("null") ? "0" : paySlipInfo.getString("sd_md");
                        abs_ded_days = paySlipInfo.getString("absent_days")
                                .equals("null") ? "" : paySlipInfo.getString("absent_days");
                        abs_ded = paySlipInfo.getString("sd_absent_amt")
                                .equals("null") ? "0" : paySlipInfo.getString("sd_absent_amt");
                        prov_fun = paySlipInfo.getString("sd_pf")
                                .equals("null") ? "0" : paySlipInfo.getString("sd_pf");
                        tax_ded = paySlipInfo.getString("sd_tax")
                                .equals("null") ? "0" : paySlipInfo.getString("sd_tax");
                        oth_ded = paySlipInfo.getString("sd_oth_deduct")
                                .equals("null") ? "0" : paySlipInfo.getString("sd_oth_deduct");
                        loan_ded_one = paySlipInfo.getString("sd_advance_deduct")
                                .equals("null") ? "0" : paySlipInfo.getString("sd_advance_deduct");
                        conveyance_cost = paySlipInfo.getString("sd_ta")
                                .equals("null") ? "0" : paySlipInfo.getString("sd_ta");
                        gross_salary = paySlipInfo.getString("sd_gross_sal")
                                .equals("null") ? "0" : paySlipInfo.getString("sd_gross_sal");
                        stam = paySlipInfo.getString("sd_stamp")
                                .equals("null") ? "0" : paySlipInfo.getString("sd_stamp");
                        leave_pay_days = paySlipInfo.getString("lwpaydays")
                                .equals("null") ? "" : paySlipInfo.getString("lwpaydays");
                        leave_pay = paySlipInfo.getString("sd_lwpay_amt")
                                .equals("null") ? "0" : paySlipInfo.getString("sd_lwpay_amt");
                        over_time = paySlipInfo.getString("total_ot_amt")
                                .equals("null") ? "0" : paySlipInfo.getString("total_ot_amt");
                        loan_ded_boa = paySlipInfo.getString("sd_md_advance_deduct")
                                .equals("null") ? "0" : paySlipInfo.getString("sd_md_advance_deduct");
                        loan_ded_sche = paySlipInfo.getString("sd_sch_advance_deduct")
                                .equals("null") ? "0" : paySlipInfo.getString("sd_sch_advance_deduct");
                        loan_ded_pf = paySlipInfo.getString("sd_pf_loan_deduct")
                                .equals("null") ? "0" : paySlipInfo.getString("sd_pf_loan_deduct");
                        lun_ded = paySlipInfo.getString("sd_lunch_deduct")
                                .equals("null") ? "0" : paySlipInfo.getString("sd_lunch_deduct");
                        other_allow = paySlipInfo.getString("other_allowances")
                                .equals("null") ? "0" : paySlipInfo.getString("other_allowances");
                        total_perqqq = paySlipInfo.getString("total_salary")
                                .equals("null") ? "0" : paySlipInfo.getString("total_salary");
                        total_ded = paySlipInfo.getString("total_deduction")
                                .equals("null") ? "0" : paySlipInfo.getString("total_deduction");
                        total = paySlipInfo.getString("net_salary")
                                .equals("null") ? "" : paySlipInfo.getString("net_salary");
                        total_word = paySlipInfo.getString("salary_in_word")
                                .equals("null") ? "" : paySlipInfo.getString("salary_in_word");

                        if(!total_word.isEmpty()) {
                            int index = total_word.indexOf("Taka");
                            total_word = total_word.substring(index);
                        }
                    }
                }
                connected = true;
                updateInterface();
            }
            catch (JSONException e) {
                logger.log(Level.WARNING, e.getMessage(), e);
                connected = false;
                updateInterface();
            }
        }, error -> {
            logger.log(Level.WARNING, error.getMessage(), error);
            conn = false;
            connected = false;
            updateInterface();
        });

        StringRequest countReq = new StringRequest(Request.Method.GET, dataCCUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject countInfo = array.getJSONObject(i);

                        data_available_count = countInfo.getString("data_cc");
                    }
                }

                requestQueue.add(paySlipReq);
            }
            catch (JSONException e) {
                logger.log(Level.WARNING, e.getMessage(), e);
                connected = false;
                updateInterface();
            }
        }, error -> {
           logger.log(Level.WARNING, error.getMessage(), error);
           conn = false;
           connected = false;
           updateInterface();
        });

        StringRequest offExtReq = new StringRequest(Request.Method.GET, officeExtraUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject offExtInfo = array.getJSONObject(i);

                        off_loc = offExtInfo.getString("office")
                                .equals("null") ? "" : offExtInfo.getString("office");
                        off_ext = offExtInfo.getString("job_pabx_corporate")
                                .equals("null") ? "" : offExtInfo.getString("job_pabx_corporate");
                        mobile_no = offExtInfo.getString("mobile")
                                .equals("null") ? "" : offExtInfo.getString("mobile");
                        charge = offExtInfo.getString("job_ad_charge")
                                .equals("null") ? "" : offExtInfo.getString("job_ad_charge");
                    }
                }

                requestQueue.add(countReq);
            }
            catch (JSONException e) {
                logger.log(Level.WARNING, e.getMessage(), e);
                connected = false;
                updateInterface();
            }
        }, error -> {
           logger.log(Level.WARNING, error.getMessage(), error);
           conn = false;
           connected = false;
           updateInterface();
        });

        requestQueue.add(offExtReq);
    }

    private void updateInterface() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                reportCard.setVisibility(View.VISIBLE);
                empName.setText(emp_name);
                id.setText(user_id);
                band.setText(ban);
                strDes.setText(str_DES);
                jobPosition.setText(job_pos);
                division.setText(div);
                department.setText(dep);

                String nat = "Not Applicable";
                if (email_name != null) {
                    if (!email_name.isEmpty()) {
                        email.setText(email_name);
                    } else {
                        email.setText(nat);
                    }
                } else {
                    email.setText(nat);
                }


                if (off_loc != null) {
                    if (!off_loc.isEmpty()) {
                        officeLoc.setText(off_loc);
                    } else {
                        officeLoc.setText(nat);
                    }
                } else {
                    officeLoc.setText(nat);
                }

                if (off_ext != null) {
                    if (!off_ext.isEmpty()) {
                        officeExt.setText(off_ext);
                    } else {
                        officeExt.setText(nat);
                    }
                } else {
                    officeExt.setText(nat);
                }

                if (mobile_no != null) {
                    if (!mobile_no.isEmpty()) {
                        mobile.setText(mobile_no);
                    } else {
                        mobile.setText(nat);
                    }
                } else {
                    mobile.setText(nat);
                }

                if (charge != null) {
                    if (!charge.isEmpty()) {
                        addCharge.setText(charge);
                    } else {
                        addCharge.setText(nat);
                    }
                } else {
                    addCharge.setText(nat);
                }

                if (data_available_count.equals("1")) {
                    salaryLay.setVisibility(View.VISIBLE);
                    salaryData.setVisibility(View.GONE);
                } else {
                    salaryLay.setVisibility(View.GONE);
                    salaryData.setVisibility(View.VISIBLE);
                }
                basic.setText(basic_salary);
                houseRent.setText(hou_rent);
                food.setText(food_cost);
                medical.setText(medical_cost);
                conveyance.setText(conveyance_cost);
                grossSalary.setText(gross_salary);
                overTime.setText(over_time);
                attBonus.setText(att_bon);
                otherAllow.setText(other_allow);
                totalPerquisite.setText(total_perqqq);

                providentFund.setText(prov_fun);
                String lpt = "Leave Without Pay " + leave_pay_days;
                leavePayText.setText(lpt);
                leavePay.setText(leave_pay);
                String adt = "Absent Deduction " + abs_ded_days;
                absentDeducText.setText(adt);
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
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(PaySlip.this)
                        .setMessage("There is a network issue in the server. Please Try later.")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    getPaySlipData();
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
            AlertDialog dialog = new AlertDialog.Builder(PaySlip.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel",null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                getPaySlipData();
                dialog.dismiss();
            });
            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> {
                dialog.dismiss();
                finish();
            });
        }
    }
}