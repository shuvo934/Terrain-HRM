package ttit.com.shuvo.ikglhrm.attendance.update;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import ttit.com.shuvo.ikglhrm.R;
import ttit.com.shuvo.ikglhrm.WaitProgress;
import ttit.com.shuvo.ikglhrm.attendance.update.dialogue.DialogueText;
import ttit.com.shuvo.ikglhrm.attendance.update.dialogue.SelectAll;
import ttit.com.shuvo.ikglhrm.attendance.update.dialogue.SelectAllList;
import ttit.com.shuvo.ikglhrm.attendance.update.dialogue.ShowAttendance;
import ttit.com.shuvo.ikglhrm.attendance.update.dialogue.ShowShift;

import static ttit.com.shuvo.ikglhrm.Login.userInfoLists;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AttendanceUpdate extends AppCompatActivity {

    public static int dialogText = 0;
    public static int showAttendanceNumber = 0;
    public static int showShiftNumber = 0;

    TextView errorTime;
    TextView errorLocation;
    public static TextView errorShift;
    TextView errorReason;
    public static TextView errorApprover;
    public static TextView errorReasonDesc;

    LinearLayout after;
    TextInputLayout dateUpdateLay;
    public static TextInputLayout reasonLay;
    public static TextInputLayout addStaLay;

    TextInputLayout inTimeLay;
    TextInputLayout outTimeLay;

    public static TextInputLayout shiftTestLay;
    public static TextInputEditText shiftTestEdit;

    public static TextInputLayout approverTestLay;
    public static TextInputEditText approverTestEdit;

    TextInputEditText machCode;
    TextInputEditText machType;
    TextInputEditText userName;
    TextInputEditText userID;
    TextInputEditText todayDate;
    TextInputEditText dateUpdated;
    TextInputEditText arrivalTimeUpdated;
    TextInputEditText departTimeUpdated;
    public static TextInputEditText reasonDesc;
    public static TextInputEditText addressStation;

    Spinner locUpdate;
    Spinner reqType;
    Spinner attenType;
    //Spinner shiftUpdated;
    Spinner reasonType;
    //Spinner approverName;

    Button existingAtt;
    public static Button showShoftTime;

    Button close;
    Button update;
    LinearLayout updateButtonEnable;

    Button arriClear;
    Button deptClear;

    public static ArrayList<SelectAllList> selectAllLists;
    public static ArrayList<SelectAllList> allShiftDetails;

    public static ArrayList<SelectAllList> allApproverDivision;
    public static ArrayList<SelectAllList> allApproverWithoutDiv;
    public static ArrayList<SelectAllList> allApproverEmp;
    public static ArrayList<SelectAllList> allSelectedApprover;

//    public static ArrayList<ApproverList> approverDivision;
//    public static ArrayList<ApproverList> approverWithoutDivision;
//    public static ArrayList<ApproverList> approverEmployee;
//
//    public static ArrayList<ApproverList> selectedApproverList;
//    public ArrayList<String> approverNameList;

    public ArrayList<LocUpdateList> locUpdateLists;
    public ArrayList<String> onlyLocationLists;

    public ArrayList<String> reqList;

    public ArrayList<AttendanceReqType> attendanceReqTypes;
    public ArrayList<String> attenTypeList;

    //public ArrayList<ShiftUpdateList> shiftUpdateLists;
    //public ArrayList<String> onlyShiftName;

    public ArrayList<ReasonList> reasonLists;
    public ArrayList<String> reasonName;


    public ArrayAdapter<String> locUpdateAdapter;
    public ArrayAdapter<String> reqTypeAdapter;
    public ArrayAdapter<String> attenTypeAdapter;
    //public ArrayAdapter<String> shiftUpdateAdapter;
    public ArrayAdapter<String> reasonAdapter;
    //public ArrayAdapter<String> approverAdapter;

    WaitProgress waitProgress = new WaitProgress();
//    private String message = null;
    private Boolean conn = false;
    private Boolean machineCon = false;
    private Boolean machineConnn = false;
    private Boolean connected = false;
    private Boolean reasonCon = false;
    private Boolean reasonConnnn = false;
    private Boolean insertCon = false;
    private Boolean insertConnn = false;
    private Boolean isInserted = false;

//    private Connection connection;
    private int mYear, mMonth, mDay, mHour, mMinute;

    String emp_id = "";
    String emp_name = "";
    String user_id = "";

    String updatedLocationName = "";
    String selected_loc_id = "";

    String machineCode = "";
    String machineType = "";

    public static String dateToShow = "";

    String selected_request = "";
    String selected_attendance_type= "";
    public static String selected_shift_name = "";
    public static String selected_shift_id = "";
    public static String shift_osm_id = "";
    String selected_reason_name = "";
    public static String selected_approver_name = "";

    public static String hint = "";
    public static int number = 0;
    public static String text = "";

    String desig_priority = "";
    String divm_id = "";
    String approval_band = "";
    int flag = 1;
    int count_approv_emp = 0;


//    String darm_id = "";
    String selected_update_date = "";
    String selected_reason_desc = "";
    String selected_address_station = "";
    String now_date = "";
    String selected_reason_id = "";
//    String app_code = "";
//    String app_code_id = "";
    public static String selected_approver_id = "";
    String arrival_time = "";
    String depart_time = "";

    String selected_jsm_id = "";
    String selected_dept_id = "";
    String selected_divm_id = "";
    String calling_title = "";



//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        if (hasFocus) {
//            hideSystemUI();
//        }
//    }
//    private void hideSystemUI() {
//        View decorView = getWindow().getDecorView();
//        decorView.setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_IMMERSIVE
//                        // Set the content to appear under the system bars so that the
//                        // content doesn't resize when the system bars hide and show.
//                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                        // Hide the nav bar and status bar
//                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
//    }
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
        window.setStatusBarColor(ContextCompat.getColor(AttendanceUpdate.this,R.color.secondaryColor));
        setContentView(R.layout.activity_attendance_update);

        selected_shift_id = "";
        selected_approver_id = "";

        shiftTestLay = findViewById(R.id.shift_description_Layout);
        shiftTestEdit = findViewById(R.id.shift_description);

        approverTestLay = findViewById(R.id.approver_description_Layout);
        approverTestEdit = findViewById(R.id.approver_description);

        inTimeLay = findViewById(R.id.select_in_time_from);
        outTimeLay = findViewById(R.id.select_out_time_from);

        errorTime = findViewById(R.id.error_input_time);
        errorLocation = findViewById(R.id.error_input_location);
        errorShift = findViewById(R.id.error_input_shift);
        errorReason = findViewById(R.id.error_input_reason);
        errorApprover = findViewById(R.id.error_input_approver);
        errorReasonDesc = findViewById(R.id.error_input_reason_desc);

        existingAtt = findViewById(R.id.existing_att_time);
        showShoftTime = findViewById(R.id.show_shift_item_button);
        close = findViewById(R.id.update_finish);
        update = findViewById(R.id.update_req_button);
        updateButtonEnable = findViewById(R.id.linearLayout_new_request_att_send);
        arriClear = findViewById(R.id.arrival_clear);
        deptClear = findViewById(R.id.departure_clear);

        after = findViewById(R.id.afterselecting);
        dateUpdateLay = findViewById(R.id.date_updated_layout);
        addStaLay = findViewById(R.id.address_station_layout);
        reasonLay = findViewById(R.id.reason_description_Layout);

        machCode = findViewById(R.id.updated_machine_code);
        machType = findViewById(R.id.updated_machine_type);
        userName = findViewById(R.id.name_att_update);
        userID = findViewById(R.id.id_att_update);
        todayDate = findViewById(R.id.now_date_att_update);
        dateUpdated = findViewById(R.id.date_to_be_updated);
        arrivalTimeUpdated = findViewById(R.id.arrival_time_to_be_updated);
        departTimeUpdated = findViewById(R.id.departure_time_to_be_updated);
        addressStation = findViewById(R.id.address_outside_sta);
        reasonDesc = findViewById(R.id.reason_description);

        locUpdate = findViewById(R.id.spinner_loc_updated);
        reqType = findViewById(R.id.spinner_req_type);
        attenType = findViewById(R.id.spinner_attendance_type);
        //shiftUpdated = findViewById(R.id.spinner_shift_updated);
        reasonType = findViewById(R.id.spinner_reason_type_updated);
        //approverName = findViewById(R.id.spinner_approver_updated);

        selectAllLists = new ArrayList<>();

        allShiftDetails = new ArrayList<>();
        allApproverDivision = new ArrayList<>();
        allApproverWithoutDiv = new ArrayList<>();
        allApproverEmp = new ArrayList<>();
        allSelectedApprover = new ArrayList<>();

//        approverDivision = new ArrayList<>();
//        approverWithoutDivision = new ArrayList<>();
//        approverEmployee = new ArrayList<>();
//
//        selectedApproverList = new ArrayList<>();

        locUpdateLists = new ArrayList<>();

        attendanceReqTypes = new ArrayList<>();

        //shiftUpdateLists = new ArrayList<>();

        reasonLists = new ArrayList<>();

        onlyLocationLists = new ArrayList<>();
        reqList = new ArrayList<>();
        attenTypeList = new ArrayList<>();
        //onlyShiftName = new ArrayList<>();
        reasonName = new ArrayList<>();
        //approverNameList = new ArrayList<>();

        //approverNameList.add("Select");

        reasonName.add("Select");

        onlyLocationLists.add("Select");

        reqList.add("Select");
        reqList.add("PRE");
        reqList.add("POST");

        attendanceReqTypes.add(new AttendanceReqType("Early","Early Departure Information"));
        attendanceReqTypes.add(new AttendanceReqType("Late","Late Arrival Information"));
        attendanceReqTypes.add(new AttendanceReqType("General","Work Time Update"));

        attenTypeList.add("Select");

        //onlyShiftName.add("Select");


        for (int i = 0 ; i < attendanceReqTypes.size(); i++) {
            attenTypeList.add(attendanceReqTypes.get(i).getAttendance_req_details());
        }

        emp_id = userInfoLists.get(0).getEmp_id();

        emp_name = userInfoLists.get(0).getUser_fname() + " " + userInfoLists.get(0).getUser_lname();

        user_id = userInfoLists.get(0).getUserName();

        userName.setText(emp_name);
        userID.setText(user_id);

        Date c = Calendar.getInstance().getTime();

        String formattedDate = "";

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);

        formattedDate = df.format(c);

        todayDate.setText(formattedDate);

//        new Check().execute();
        getDataInfo();


        // Location Update Spinner
        locUpdateAdapter = new ArrayAdapter<String>(
                this,R.layout.item_country,onlyLocationLists){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view.findViewById(R.id.tvCountry);
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        locUpdate.setGravity(Gravity.END);
        locUpdateAdapter.setDropDownViewResource(R.layout.item_country);
        locUpdate.setAdapter(locUpdateAdapter);


        // Selecting Location From Spinner
        locUpdate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position > 0){
                    updatedLocationName = (String) parent.getItemAtPosition(position);
                    errorLocation.setVisibility(View.GONE);

                    // Notify the selected item text
                    for (int i = 0; i < locUpdateLists.size(); i++) {
                        if (updatedLocationName.equals(locUpdateLists.get(i).getLocation())) {
                            selected_loc_id = locUpdateLists.get(i).getLocID();

                            System.out.println(selected_loc_id);
                        }
                    }

//                    new MachineInfo().execute();
                    getMachineData();

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Request Type Spinner
        reqTypeAdapter = new ArrayAdapter<String>(
                this,R.layout.item_country,reqList){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view.findViewById(R.id.tvCountry);
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        reqType.setGravity(Gravity.END);
        reqTypeAdapter.setDropDownViewResource(R.layout.item_country);
        reqType.setAdapter(reqTypeAdapter);

        // Selecting Request Type
        reqType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position > 0){
                    selected_request = (String) parent.getItemAtPosition(position);
                    dateUpdated.setText("");
                    dateUpdateLay.setHint("Select Update Date");
                    dateUpdateLay.setHelperText("");
                    existingAtt.setVisibility(View.GONE);

                    // Notify the selected item text
                    System.out.println(selected_request);
                    if (!selected_request.isEmpty() && !selected_attendance_type.isEmpty()) {

                        System.out.println(1);
                        after.setVisibility(View.VISIBLE);
                        updateButtonEnable.setVisibility(View.VISIBLE);
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Attendance Type Spinner
        attenTypeAdapter = new ArrayAdapter<String>(
                this,R.layout.item_country,attenTypeList){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view.findViewById(R.id.tvCountry);
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        attenType.setGravity(Gravity.END);
        attenTypeAdapter.setDropDownViewResource(R.layout.item_country);
        attenType.setAdapter(attenTypeAdapter);


        // Selecting Attendance Type
        attenType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position > 0){
                    selected_attendance_type = (String) parent.getItemAtPosition(position);

                    for (int i = 0; i < attendanceReqTypes.size(); i++) {
                        if (selected_attendance_type.equals(attendanceReqTypes.get(i).getAttendance_req_details())) {
                            selected_attendance_type = attendanceReqTypes.get(i).getAttendance_req();

                            System.out.println(selected_attendance_type);
                        }
                    }

//                    new ReasonInfo().execute();
                    getReasonData();
                    // Notify the selected item text
                    if (!selected_request.isEmpty() && !selected_attendance_type.isEmpty()) {

                        System.out.println(2);
                        after.setVisibility(View.VISIBLE);
                        updateButtonEnable.setVisibility(View.VISIBLE);
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // Shift Updated Spinner
//        shiftUpdateAdapter = new ArrayAdapter<String>(
//                this,R.layout.item_country,onlyShiftName){
//            @Override
//            public boolean isEnabled(int position){
//                if(position == 0)
//                {
//                    // Disable the first item from Spinner
//                    // First item will be use for hint
//                    return false;
//                }
//                else
//                {
//                    return true;
//                }
//            }
//            @Override
//            public View getDropDownView(int position, View convertView,
//                                        ViewGroup parent) {
//                View view = super.getDropDownView(position, convertView, parent);
//                TextView tv = (TextView) view.findViewById(R.id.tvCountry);
//                if(position == 0){
//                    // Set the hint text color gray
//                    tv.setTextColor(Color.GRAY);
//                }
//                else {
//                    tv.setTextColor(Color.BLACK);
//                }
//                return view;
//            }
//        };
//        shiftUpdated.setGravity(Gravity.END);
//        shiftUpdateAdapter.setDropDownViewResource(R.layout.item_country);
//        shiftUpdated.setAdapter(shiftUpdateAdapter);


        // Selecting Shift Updated
//        shiftUpdated.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//                if(position > 0){
//                    selected_shift_name = (String) parent.getItemAtPosition(position);
//                    errorShift.setVisibility(View.GONE);
//
//                    for (int i = 0; i < shiftUpdateLists.size(); i++) {
//                        if (selected_shift_name.equals(shiftUpdateLists.get(i).getShift_name())) {
//                            selected_shift_id = shiftUpdateLists.get(i).getShift_id();
//
//                            System.out.println(selected_shift_id);
//                        }
//                    }
//
//                    // Notify the selected item text
//
//
//                }
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        // Reason Type Spinner
        reasonAdapter = new ArrayAdapter<String>(
                this,R.layout.item_country,reasonName){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view.findViewById(R.id.tvCountry);
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        reasonType.setGravity(Gravity.END);
        reasonAdapter.setDropDownViewResource(R.layout.item_country);
        reasonType.setAdapter(reasonAdapter);

        // Selecting Reason Type
        reasonType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position > 0){
                    selected_reason_name = (String) parent.getItemAtPosition(position);
                    errorReason.setVisibility(View.GONE);

                    for (int i = 0; i < reasonLists.size(); i++) {
                        if (selected_reason_name.equals(reasonLists.get(i).getReason_name())) {
                            selected_reason_id = reasonLists.get(i).getReason_id();

                            System.out.println(selected_reason_id);
                        }
                    }

                    // Notify the selected item text


                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // Approver Spinner

//        approverAdapter = new ArrayAdapter<String>(
//                this,R.layout.item_country,approverNameList){
//            @Override
//            public boolean isEnabled(int position){
//                if(position == 0)
//                {
//                    // Disable the first item from Spinner
//                    // First item will be use for hint
//                    return false;
//                }
//                else
//                {
//                    return true;
//                }
//            }
//            @Override
//            public View getDropDownView(int position, View convertView,
//                                        ViewGroup parent) {
//                View view = super.getDropDownView(position, convertView, parent);
//                TextView tv = (TextView) view.findViewById(R.id.tvCountry);
//                if(position == 0){
//                    // Set the hint text color gray
//                    tv.setTextColor(Color.GRAY);
//                }
//                else {
//                    tv.setTextColor(Color.BLACK);
//                }
//                return view;
//            }
//        };
//        approverName.setGravity(Gravity.END);
//        approverAdapter.setDropDownViewResource(R.layout.item_country);
//        approverName.setAdapter(approverAdapter);

        // Selecting Approver Adapter
//        approverName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//                if(position > 0){
//                    selected_approver_name = (String) parent.getItemAtPosition(position);
//                    errorApprover.setVisibility(View.GONE);
//
//                    for (int i = 0; i < selectedApproverList.size(); i++) {
//                        if (selected_approver_name.equals(selectedApproverList.get(i).getApprover_emp_name())) {
//                            selected_approver_id = selectedApproverList.get(i).getApprover_emp_id();
//
//                            System.out.println(selected_approver_id);
//                        }
//                    }
//
//                    // Notify the selected item text
//
//
//                }
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        // Date to be Updated
        dateUpdated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    final Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        DatePickerDialog datePickerDialog = new DatePickerDialog(AttendanceUpdate.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                                String monthName = "";
                                String dayOfMonthName = "";
                                String yearName = "";
                                month = month + 1;
                                if (month == 1) {
                                    monthName = "JAN";
                                } else if (month == 2) {
                                    monthName = "FEB";
                                } else if (month == 3) {
                                    monthName = "MAR";
                                } else if (month == 4) {
                                    monthName = "APR";
                                } else if (month == 5) {
                                    monthName = "MAY";
                                } else if (month == 6) {
                                    monthName = "JUN";
                                } else if (month == 7) {
                                    monthName = "JUL";
                                } else if (month == 8) {
                                    monthName = "AUG";
                                } else if (month == 9) {
                                    monthName = "SEP";
                                } else if (month == 10) {
                                    monthName = "OCT";
                                } else if (month == 11) {
                                    monthName = "NOV";
                                } else if (month == 12) {
                                    monthName = "DEC";
                                }

                                if (dayOfMonth <= 9) {
                                    dayOfMonthName = "0" + String.valueOf(dayOfMonth);
                                } else {
                                    dayOfMonthName = String.valueOf(dayOfMonth);
                                }
                                yearName  = String.valueOf(year);
                                yearName = yearName.substring(yearName.length()-2);
                                System.out.println(yearName);
                                System.out.println(dayOfMonthName);
                                dateUpdated.setText(dayOfMonthName + "-" + monthName + "-" + yearName);

                                if (selected_request.equals("PRE")) {

                                    String today = todayDate.getText().toString();
                                    String updateDate = dateUpdated.getText().toString();

                                    Date nowDate = null;
                                    Date givenDate = null;

                                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);
                                    try {
                                        nowDate = sdf.parse(today);
                                        givenDate = sdf.parse(updateDate);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    if (nowDate != null && givenDate != null) {

                                        if (givenDate.after(nowDate) || givenDate.equals(nowDate)) {
                                            //Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_SHORT).show();
//                                            dateUpdateLay.setHelperText("Requested Date never Less than Application Date");
//                                            dateUpdateLay.setHelperTextColor(ColorStateList.valueOf(Color.RED));
//                                            dateUpdated.setText("");
//                                            existingAtt.setVisibility(View.GONE);

                                            dateUpdated.setText(dayOfMonthName + "-" + monthName + "-" + yearName);
                                            dateUpdateLay.setHelperText("");
                                            dateUpdateLay.setHint("Update Date");
                                            existingAtt.setVisibility(View.VISIBLE);
                                            System.out.println("AHSE let");

                                        } else {
//                                            dateUpdated.setText(dayOfMonthName + "-" + monthName + "-" + yearName);
//                                            dateUpdateLay.setHelperText("");
//                                            existingAtt.setVisibility(View.VISIBLE);
//                                            System.out.println("AHSE");

                                            dateUpdateLay.setHelperText("Requested Date never Less than Application Date");
                                            dateUpdateLay.setHint("Select Update Date");
                                            dateUpdateLay.setHelperTextColor(ColorStateList.valueOf(Color.RED));
                                            dateUpdated.setText("");
                                            existingAtt.setVisibility(View.GONE);
                                        }
                                    }
                                }
                                else if (selected_request.equals("POST")) {

                                    String today = todayDate.getText().toString();
                                    String updateDate = dateUpdated.getText().toString();

                                    Date nowDate = null;
                                    Date givenDate = null;

                                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);
                                    try {
                                        nowDate = sdf.parse(today);
                                        givenDate = sdf.parse(updateDate);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    if (nowDate != null && givenDate != null) {

                                        if (givenDate.before(nowDate)) {
                                            //Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_SHORT).show();
//                                            dateUpdateLay.setHelperText("Requested Date should be Less than Application Date");
//                                            dateUpdateLay.setHelperTextColor(ColorStateList.valueOf(Color.RED));
//                                            dateUpdated.setText("");
//                                            existingAtt.setVisibility(View.GONE);

                                            dateUpdated.setText(dayOfMonthName + "-" + monthName + "-" + yearName);
                                            dateUpdateLay.setHelperText("");
                                            dateUpdateLay.setHint("Update Date");
                                            existingAtt.setVisibility(View.VISIBLE);
                                            System.out.println("AHSE POST");


                                        } else {
//                                            dateUpdated.setText(dayOfMonthName + "-" + monthName + "-" + yearName);
//                                            dateUpdateLay.setHelperText("");
//                                            existingAtt.setVisibility(View.VISIBLE);
//                                            System.out.println("AHSE");

                                            dateUpdateLay.setHelperText("Requested Date should be Less than Application Date");
                                            dateUpdateLay.setHint("Select Update Date");
                                            dateUpdateLay.setHelperTextColor(ColorStateList.valueOf(Color.RED));
                                            dateUpdated.setText("");
                                            existingAtt.setVisibility(View.GONE);
                                        }
                                    }
                                }

                            }
                        }, mYear, mMonth, mDay);
                        datePickerDialog.show();
                    }
                }
            }
        });

        // Time to be Updated - Arrival
        arrivalTimeUpdated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(AttendanceUpdate.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        String AM_PM ;
                        if(hourOfDay < 12) {
                            AM_PM = "AM";
                            if (hourOfDay == 0) {
                                hourOfDay = 12;
                            }
                        } else {
                            AM_PM = "PM";
                            if (hourOfDay > 12 ) {
                                hourOfDay = hourOfDay - 12;
                            }
                        }
                        String tt = String.valueOf(minute);
                        if (tt.length() == 1) {
                            tt = "0"+tt;
                            arrivalTimeUpdated.setText(hourOfDay + ":" + tt + AM_PM);
                            inTimeLay.setHint("In Time");
                            arriClear.setVisibility(View.VISIBLE);
                            errorTime.setVisibility(View.GONE);
                        } else {
                            arrivalTimeUpdated.setText(hourOfDay + ":" + minute + AM_PM);
                            inTimeLay.setHint("In Time");
                            arriClear.setVisibility(View.VISIBLE);
                            errorTime.setVisibility(View.GONE);
                        }
                    }
                },mHour, mMinute,false);
                timePickerDialog.show();
            }
        });

        // Time to be updated - Departure
        departTimeUpdated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(AttendanceUpdate.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        String AM_PM ;
                        if(hourOfDay < 12) {
                            AM_PM = "AM";
                            if (hourOfDay == 0) {
                                hourOfDay = 12;
                            }
                        } else {
                            AM_PM = "PM";
                            if (hourOfDay > 12 ) {
                                hourOfDay = hourOfDay - 12;
                            }
                        }
                        String tt = String.valueOf(minute);
                        if (tt.length() == 1) {
                            tt = "0"+tt;
                            departTimeUpdated.setText(hourOfDay + ":" + tt + AM_PM);
                            outTimeLay.setHint("Out Time");
                            deptClear.setVisibility(View.VISIBLE);
                            errorTime.setVisibility(View.GONE);
                        } else {
                            departTimeUpdated.setText(hourOfDay + ":" + minute + AM_PM);
                            outTimeLay.setHint("Out Time");
                            deptClear.setVisibility(View.VISIBLE);
                            errorTime.setVisibility(View.GONE);
                        }
                    }
                },mHour, mMinute,false);
                timePickerDialog.show();
            }
        });


        arriClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrivalTimeUpdated.setText("");
                inTimeLay.setHint("Select In Time");
                arriClear.setVisibility(View.GONE);
            }
        });

        deptClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                departTimeUpdated.setText("");
                outTimeLay.setHint("Select Out Time");
                deptClear.setVisibility(View.GONE);
            }
        });


        // Reason Description
        reasonDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                number = 1;
                hint = reasonLay.getHint().toString();
                text = reasonDesc.getText().toString();
                DialogueText dialogueText = new DialogueText();
                dialogueText.show(getSupportFragmentManager(),"TEXTEDIT");
            }
        });

        // Address out of Station
        addressStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number = 2;
                hint = addStaLay.getHint().toString();
                text = addressStation.getText().toString();
                DialogueText dialogueText = new DialogueText();
                dialogueText.show(getSupportFragmentManager(),"TEXTEDIT");
            }
        });

        shiftTestEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogText = 1;
                selectAllLists = allShiftDetails;
                SelectAll selectAll = new SelectAll();
                selectAll.show(getSupportFragmentManager(),"TEXTEDIT");
            }
        });

        approverTestEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogText = 2;
                selectAllLists = allSelectedApprover;
                SelectAll selectAll = new SelectAll();
                selectAll.show(getSupportFragmentManager(),"TEXTEDIT");
            }
        });

        existingAtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAttendanceNumber = 1;
                dateToShow = dateUpdated.getText().toString();
                if (!dateToShow.isEmpty()) {
                    ShowAttendance showAttendance = new ShowAttendance(AttendanceUpdate.this);
                    showAttendance.show(getSupportFragmentManager(),"Attendance");
                }

            }
        });

        showShoftTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showShiftNumber = 1;
                shift_osm_id = selected_shift_id;
                ShowShift showShift = new ShowShift(AttendanceUpdate.this);
                showShift.show(getSupportFragmentManager(),"Shift");
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                now_date = todayDate.getText().toString();
                selected_update_date = dateUpdated.getText().toString();
                arrival_time = arrivalTimeUpdated.getText().toString();
                depart_time = departTimeUpdated.getText().toString();
                selected_reason_desc = reasonDesc.getText().toString();
                selected_address_station = addressStation.getText().toString();

//                if (selected_address_station.isEmpty()) {
//                    selected_address_station = null;
//                }

                if (!selected_update_date.isEmpty()) {

                    if (arrival_time.isEmpty() && depart_time.isEmpty()) {
                        Toast.makeText(getApplicationContext(),"Please Check Time to be Updated",Toast.LENGTH_SHORT).show();
                        errorTime.setVisibility(View.VISIBLE);
                    } else {
                        errorTime.setVisibility(View.GONE);
                        if (!selected_loc_id.isEmpty()) {
                            errorLocation.setVisibility(View.GONE);

                            if (!selected_shift_id.isEmpty()) {
                                System.out.println(selected_shift_id);
                                errorShift.setVisibility(View.GONE);

                                if (!selected_reason_id.isEmpty()) {
                                    errorReason.setVisibility(View.GONE);

                                    if (!selected_reason_desc.isEmpty()) {
                                        errorReasonDesc.setVisibility(View.GONE);

                                        if (!selected_approver_id.isEmpty()) {
                                            errorApprover.setVisibility(View.GONE);

                                            AlertDialog.Builder builder = new AlertDialog.Builder(AttendanceUpdate.this);
                                            builder.setTitle("Attendance Update Request!")
                                                    .setMessage("Do you want send this request?")
                                                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {

//                                                            new InsertCheck().execute();
                                                            insertReq();

                                                        }
                                                    })
                                                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {

                                                        }
                                                    });
                                            AlertDialog alert = builder.create();
                                            alert.show();



                                        } else {
                                            Toast.makeText(getApplicationContext(),"Please Check Approver Name",Toast.LENGTH_SHORT).show();
                                            errorApprover.setVisibility(View.VISIBLE);
                                        }
                                    }else {
                                        Toast.makeText(getApplicationContext(),"Please Check Reason Description",Toast.LENGTH_SHORT).show();
                                        errorReasonDesc.setVisibility(View.VISIBLE);
                                    }

                                } else {
                                    Toast.makeText(getApplicationContext(),"Please Check Reason Type",Toast.LENGTH_SHORT).show();
                                    errorReason.setVisibility(View.VISIBLE);
                                }

                            } else {
                                Toast.makeText(getApplicationContext(),"Please Check Shift to be Updated",Toast.LENGTH_SHORT).show();
                                errorShift.setVisibility(View.VISIBLE);
                            }

                        } else {
                            Toast.makeText(getApplicationContext(),"Please Check Location to be Updated",Toast.LENGTH_SHORT).show();
                            errorLocation.setVisibility(View.VISIBLE);
                        }

                    }

                } else {
                    Toast.makeText(getApplicationContext(),"Please Check Date to be Updated",Toast.LENGTH_SHORT).show();
                    dateUpdateLay.setHelperText("Please Provide 'Date to be Updated'");
                }

            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected_shift_id = "";
                selected_approver_id = "";

                finish();

            }
        });

    }


    //----------------------------------------------------


    @Override
    public void onBackPressed() {

        selected_shift_id = "";
        selected_approver_id = "";

        finish();
    }

//    public boolean isConnected() {
//        boolean connected = false;
//        boolean isMobile = false;
//        try {
//            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
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
//                UpdateInfo();
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
//                if (locUpdateLists.size() != 0) {
//                    for (int i = 0; i < locUpdateLists.size(); i++) {
//                        onlyLocationLists.add(locUpdateLists.get(i).getLocation());
//                    }
//                }
//                locUpdateAdapter.notifyDataSetChanged();
//
////                if (shiftUpdateLists.size() != 0) {
////                    for (int i = 0; i < shiftUpdateLists.size(); i++) {
////                        onlyShiftName.add(shiftUpdateLists.get(i).getShift_name());
////                    }
////                }
////                shiftUpdateAdapter.notifyDataSetChanged();
//
////                if (selectedApproverList.size() != 0) {
////                    for (int i = 0 ; i < selectedApproverList.size(); i++) {
////                        approverNameList.add(selectedApproverList.get(i).getApprover_emp_name());
////                    }
////                }
////                approverAdapter.notifyDataSetChanged();
//
//                conn = false;
//                connected = false;
//
//            }
//            else {
//                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
//                AlertDialog dialog = new AlertDialog.Builder(AttendanceUpdate.this)
//                        .setMessage("Please Check Your Internet Connection")
//                        .setPositiveButton("Retry", null)
//                        .setNegativeButton("Cancel", null)
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
//
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

//    public class MachineInfo extends AsyncTask<Void, Void, Void> {
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
//                MachineData(selected_loc_id);
//                if (machineCon) {
//                    machineConnn = true;
//                    message= "Internet Connected";
//                }
//
//            } else {
//                machineConnn = false;
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
//            if (machineConnn) {
//
//                if (!machineCode.isEmpty() && !machineType.isEmpty()) {
//                    machCode.setText(machineCode);
//                    machType.setText(machineType);
//                }
//
//                machineConnn = false;
//                machineCon = false;
//
//            }
//            else {
//                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
//                AlertDialog dialog = new AlertDialog.Builder(AttendanceUpdate.this)
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
//                        new MachineInfo().execute();
//                        dialog.dismiss();
//                    }
//                });
//
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

//    public class ReasonInfo extends AsyncTask<Void, Void, Void> {
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
//                ReasonData(selected_attendance_type);
//                if (reasonCon) {
//                    reasonConnnn = true;
//                    message= "Internet Connected";
//                }
//
//            } else {
//                reasonConnnn = false;
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
//            if (reasonConnnn) {
//
//                reasonName = new ArrayList<>();
//                reasonName.add("Select");
//
//
//                if (reasonLists.size() != 0) {
//                    for (int i = 0; i < reasonLists.size(); i++) {
//                        reasonName.add(reasonLists.get(i).getReason_name());
//                        reasonAdapter.setNotifyOnChange(true);
//                        System.out.println(reasonLists.get(i).getReason_name());
//                    }
//                }
//
//                reasonAdapter.clear();
//                reasonAdapter.addAll(reasonName);
//                reasonAdapter.notifyDataSetChanged();
//                reasonType.setSelection(0);
//
//                reasonConnnn = false;
//                reasonCon = false;
//
//            }
//            else {
//                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
//                AlertDialog dialog = new AlertDialog.Builder(AttendanceUpdate.this)
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
//                        new ReasonInfo().execute();
//                        dialog.dismiss();
//                    }
//                });
//
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

//    public class InsertCheck extends AsyncTask<Void, Void, Void> {
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
//                InsertReq();
//                if (insertCon) {
//                    insertConnn = true;
//                    System.out.println("INSERTED");
//                    message= "Internet Connected";
//                }
//
//            } else {
//                insertConnn = false;
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
//            if (insertConnn) {
//                selected_shift_id = "";
//                selected_approver_id = "";
//                System.out.println("INSERTED");
//
//                AlertDialog dialog = new AlertDialog.Builder(AttendanceUpdate.this)
//                        .setMessage("Request Sent Successfully")
//                        .setPositiveButton("OK", null)
//                        .show();
//
//                dialog.setCancelable(false);
//                dialog.setCanceledOnTouchOutside(false);
//                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
//                positive.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        dialog.dismiss();
//                        finish();
//                    }
//                });
//
//            }
//            else {
//                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
//                AlertDialog dialog = new AlertDialog.Builder(AttendanceUpdate.this)
//                        .setMessage("Please Check Your Internet Connection")
//                        .setPositiveButton("Retry", null)
//                        .setNegativeButton("Cancel", null)
//                        .show();
//
//                dialog.setCancelable(false);
//                dialog.setCanceledOnTouchOutside(false);
//                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
//                positive.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        new InsertCheck().execute();
//                        dialog.dismiss();
//                    }
//                });
//
//                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
//                negative.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        dialog.dismiss();
//
//                    }
//                });
//            }
//        }
//    }


//    public void UpdateInfo() {
//        try {
//            this.connection = createConnection();
//
//            locUpdateLists = new ArrayList<>();
//            allShiftDetails = new ArrayList<>();
//
//            allApproverDivision = new ArrayList<>();
//            allApproverWithoutDiv = new ArrayList<>();
//            allApproverEmp = new ArrayList<>();
//            allSelectedApprover = new ArrayList<>();
//            //shiftUpdateLists = new ArrayList<>();
////            approverEmployee = new ArrayList<>();
////            approverDivision = new ArrayList<>();
////            approverWithoutDivision = new ArrayList<>();
//
//            //selectAllLists = new ArrayList<>();
//
//            //selectedApproverList = new ArrayList<>();
//
//            Statement stmt = connection.createStatement();
//
//
//            ResultSet rs=stmt.executeQuery("SELECT DISTINCT COMPANY_OFFICE_ADDRESS.COA_ID, \n" +
//                    "COMPANY_OFFICE_ADDRESS.COA_NAME \n" +
//                    "FROM COMPANY_OFFICE_ADDRESS,ATTENDANCE_MECHINE_SETUP \n" +
//                    "WHERE ATTENDANCE_MECHINE_SETUP.AMS_COA_ID=COMPANY_OFFICE_ADDRESS.COA_ID\n" +
//                    "AND COMPANY_OFFICE_ADDRESS.COA_USE_TYPE_FLAG=0\n" +
//                    "ORDER BY 1");
//
//            while(rs.next())  {
//                locUpdateLists.add(new LocUpdateList(rs.getString(1),rs.getString(2)));
//            }
//
//            ResultSet resultSet = stmt.executeQuery("SELECT  OFFICE_SHIFT_MST.OSM_ID, OFFICE_SHIFT_MST.OSM_NAME,\n" +
//                    "TO_CHAR(OFFICE_SHIFT_MST.OSM_START_TIME, 'HH:MI AM') START_TIME, \n" +
//                    "TO_CHAR(OFFICE_SHIFT_MST.OSM_LATE_AFTER, 'HH:MI AM') LATE_AFTER, \n" +
//                    "TO_CHAR(OFFICE_SHIFT_MST.OSM_END_TIME, 'HH:MI AM') END_TIME\n" +
//                    "FROM OFFICE_SHIFT_MST \n" +
//                    "ORDER BY OFFICE_SHIFT_MST.OSM_ID");
//
//            while (resultSet.next()) {
//                //shiftUpdateLists.add(new ShiftUpdateList(resultSet.getString(1),resultSet.getString(2)));
//                allShiftDetails.add(new SelectAllList(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4),resultSet.getString(5)));
//            }
//
//
//            // Employee Information
//            ResultSet emp = stmt.executeQuery("SELECT DISTINCT \n" +
//                    " JOB_SETUP_MST.JSM_ID, \n" +
//                    "EMP_JOB_HISTORY.JOB_CALLING_TITLE, \n" +
//                    "dept_mst.dept_id, division_mst.divm_id\n" +
//                    "FROM EMP_MST, JOB_SETUP_DTL, JOB_SETUP_MST, DEPT_MST, DIVISION_MST, EMP_JOB_HISTORY, DESIG_MST, COMPANY_OFFICE_ADDRESS, COMPANY_OFFICE_ADDRESS COMPANY_OFFICE_ADDRESS_A1\n" +
//                    "WHERE EMP_MST.EMP_ID = "+emp_id+"\n" +
//                    "  AND ((JOB_SETUP_DTL.JSD_JSM_ID = JOB_SETUP_MST.JSM_ID)\n" +
//                    " AND (JOB_SETUP_MST.JSM_DEPT_ID = DEPT_MST.DEPT_ID)\n" +
//                    " AND (JOB_SETUP_MST.JSM_DIVM_ID = DIVISION_MST.DIVM_ID)\n" +
//                    " AND (JOB_SETUP_MST.JSM_DESIG_ID = DESIG_MST.DESIG_ID)\n" +
//                    " AND (EMP_JOB_HISTORY.JOB_PRI_COA_ID = COMPANY_OFFICE_ADDRESS.COA_ID)\n" +
//                    " AND (JOB_SETUP_DTL.JSD_ID = EMP_MST.EMP_JSD_ID)\n" +
//                    " AND (EMP_JOB_HISTORY.JOB_ID = EMP_MST.EMP_JOB_ID)\n" +
//                    " AND (COMPANY_OFFICE_ADDRESS_A1.COA_ID(+) = EMP_JOB_HISTORY.JOB_SEC_COA_ID))");
//
//            while (emp.next()) {
//                selected_jsm_id = emp.getString(1);
//                calling_title = emp.getString(2);
//                selected_dept_id = emp.getString(3);
//                selected_divm_id = emp.getString(4);
//            }
//
//            // Approver Division
//            ResultSet rs1 = stmt.executeQuery("SELECT EMP_MST.EMP_ID,EMP_MST.EMP_NAME,EMP_JOB_HISTORY.JOB_CALLING_TITLE,JOB_SETUP_MST.JSM_NAME,DIVISION_MST.DIVM_NAME \n" +
//                    "    FROM EMP_MST, EMP_JOB_HISTORY,  JOB_SETUP_DTL, JOB_SETUP_MST, DESIG_MST,DIVISION_MST\n" +
//                    "    WHERE JOB_SETUP_MST.JSM_DIVM_ID=(SELECT JOB_SETUP_MST.JSM_DIVM_ID\n" +
//                    "                                    FROM EMP_MST, EMP_JOB_HISTORY,  JOB_SETUP_DTL, JOB_SETUP_MST, DESIG_MST\n" +
//                    "                                    WHERE EMP_MST.EMP_ID="+emp_id+"\n" +
//                    "                                    AND (JOB_SETUP_DTL.JSD_JSM_ID = JOB_SETUP_MST.JSM_ID)\n" +
//                    "                                    AND (JOB_SETUP_MST.JSM_DESIG_ID = DESIG_MST.DESIG_ID)\n" +
//                    "                                    AND (EMP_JOB_HISTORY.JOB_ID = EMP_MST.EMP_JOB_ID)\n" +
//                    "                                    AND (JOB_SETUP_DTL.JSD_ID = EMP_JOB_HISTORY.JOB_JSD_ID))\n" +
//                    "    AND EMP_JOB_HISTORY.JOB_STATUS NOT IN('Closed','Suspended')\n" +
//                    "    AND (JOB_SETUP_DTL.JSD_JSM_ID = JOB_SETUP_MST.JSM_ID)\n" +
//                    "    AND (JOB_SETUP_MST.JSM_DESIG_ID = DESIG_MST.DESIG_ID)\n" +
//                    "    AND (EMP_JOB_HISTORY.JOB_ID = EMP_MST.EMP_JOB_ID)\n" +
//                    "    AND (JOB_SETUP_DTL.JSD_ID = EMP_JOB_HISTORY.JOB_JSD_ID)\n" +
//                    "    AND EMP_JOB_HISTORY.JOB_EMAIL IS NOT NULL\n" +
//                    "    AND JOB_SETUP_MST.JSM_DIVM_ID=DIVISION_MST.DIVM_ID\n" +
//                    "    AND DESIG_MST.DESIG_PRIORITY IN (SELECT DISTINCT REGEXP_SUBSTR(LAH_APPROVAL_BAND,'[^,]+', 1, LEVEL) \n" +
//                    "                                      FROM (SELECT LAH_APPROVAL_BAND\n" +
//                    "                                      FROM LEAVE_APPROVAL_HIERARCHY\n" +
//                    "                                      WHERE LAH_BAND_NO=( SELECT DESIG_MST.DESIG_PRIORITY\n" +
//                    "                                                          FROM EMP_MST, EMP_JOB_HISTORY,  JOB_SETUP_DTL, JOB_SETUP_MST, DESIG_MST\n" +
//                    "                                                          WHERE EMP_MST.EMP_ID="+emp_id+"\n" +
//                    "                                                          AND (JOB_SETUP_DTL.JSD_JSM_ID = JOB_SETUP_MST.JSM_ID)\n" +
//                    "                                                          AND (JOB_SETUP_MST.JSM_DESIG_ID = DESIG_MST.DESIG_ID)\n" +
//                    "                                                          AND (EMP_JOB_HISTORY.JOB_ID = EMP_MST.EMP_JOB_ID)\n" +
//                    "                                                          AND (JOB_SETUP_DTL.JSD_ID = EMP_JOB_HISTORY.JOB_JSD_ID)))\n" +
//                    "                                      connect by regexp_substr(LAH_APPROVAL_BAND, '[^,]+', 1, level) is not null)\n" +
//                    "UNION\n" +
//                    "SELECT EMP_MST.EMP_ID,EMP_MST.EMP_NAME,EMP_JOB_HISTORY.JOB_CALLING_TITLE,JOB_SETUP_MST.JSM_NAME,DIVISION_MST.DIVM_NAME \n" +
//                    "    FROM EMP_MST, EMP_JOB_HISTORY,  JOB_SETUP_DTL, JOB_SETUP_MST, DESIG_MST,DIVISION_MST\n" +
//                    "    WHERE EMP_MST.EMP_CODE = null--:PARAMETERP_CHAIRMAN\n" +
//                    "    AND (JOB_SETUP_DTL.JSD_JSM_ID = JOB_SETUP_MST.JSM_ID)\n" +
//                    "    AND (JOB_SETUP_MST.JSM_DESIG_ID = DESIG_MST.DESIG_ID)\n" +
//                    "    AND (EMP_JOB_HISTORY.JOB_ID = EMP_MST.EMP_JOB_ID)\n" +
//                    "    AND (JOB_SETUP_DTL.JSD_ID = EMP_JOB_HISTORY.JOB_JSD_ID)\n" +
//                    "    AND EMP_JOB_HISTORY.JOB_EMAIL IS NOT NULL\n" +
//                    "    AND JOB_SETUP_MST.JSM_DIVM_ID=DIVISION_MST.DIVM_ID\n" +
//                    "UNION\n" +
//                    "SELECT EMP_MST.EMP_ID,EMP_MST.EMP_NAME,EMP_JOB_HISTORY.JOB_CALLING_TITLE,JOB_SETUP_MST.JSM_NAME,DIVISION_MST.DIVM_NAME \n" +
//                    "    FROM EMP_MST, EMP_JOB_HISTORY,  JOB_SETUP_DTL, JOB_SETUP_MST, DESIG_MST,DIVISION_MST\n" +
//                    "   WHERE EMP_JOB_HISTORY.JOB_STATUS NOT IN('Closed','Suspended')\n" +
//                    "    AND (JOB_SETUP_DTL.JSD_JSM_ID = JOB_SETUP_MST.JSM_ID)\n" +
//                    "    AND (JOB_SETUP_MST.JSM_DESIG_ID = DESIG_MST.DESIG_ID)\n" +
//                    "    AND (EMP_JOB_HISTORY.JOB_ID = EMP_MST.EMP_JOB_ID)\n" +
//                    "    AND (JOB_SETUP_DTL.JSD_ID = EMP_JOB_HISTORY.JOB_JSD_ID)\n" +
//                    "    AND EMP_JOB_HISTORY.JOB_EMAIL IS NOT NULL\n" +
//                    "    AND JOB_SETUP_MST.JSM_DIVM_ID=DIVISION_MST.DIVM_ID\n" +
//                    "    AND EMP_MST.EMP_CODE      IN\n" +
//                    "                                  (SELECT DISTINCT REGEXP_SUBSTR(LAH_SP_APPROVAL_CODE, '[^,]+', 1, Level)\n" +
//                    "                                  FROM\n" +
//                    "                                    (SELECT LEAVE_APPROVAL_HIERARCHY.LAH_APPROVAL_BAND,\n" +
//                    "                                      LEAVE_APPROVAL_HIERARCHY.LAH_SP_APPROVAL_CODE\n" +
//                    "                                    FROM LEAVE_APPROVAL_HIERARCHY\n" +
//                    "                                    WHERE LEAVE_APPROVAL_HIERARCHY.LAH_BAND_NO =\n" +
//                    "                                                                              (SELECT DESIG_MST.DESIG_PRIORITY\n" +
//                    "                                                                              FROM EMP_MST,\n" +
//                    "                                                                                EMP_JOB_HISTORY,\n" +
//                    "                                                                                JOB_SETUP_DTL,\n" +
//                    "                                                                                JOB_SETUP_MST,\n" +
//                    "                                                                                DESIG_MST\n" +
//                    "                                                                              WHERE (JOB_SETUP_DTL.JSD_JSM_ID = JOB_SETUP_MST.JSM_ID)\n" +
//                    "                                                                              AND (JOB_SETUP_MST.JSM_DESIG_ID = DESIG_MST.DESIG_ID)\n" +
//                    "                                                                              AND (EMP_JOB_HISTORY.JOB_ID     = EMP_MST.EMP_JOB_ID)\n" +
//                    "                                                                              AND (JOB_SETUP_DTL.JSD_ID       = EMP_JOB_HISTORY.JOB_JSD_ID)\n" +
//                    "                                                                              AND EMP_MST.EMP_ID              = "+emp_id+"\n" +
//                    "                                                                              )\n" +
//                    "                                    )\n" +
//                    "                                    CONNECT BY regexp_substr(LAH_SP_APPROVAL_CODE, '[^,]+', 1, Level) IS NOT NULL\n" +
//                    "                                  )");
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
//            ResultSet rs2 = stmt.executeQuery("SELECT EMP_MST.EMP_ID,EMP_MST.EMP_NAME,EMP_JOB_HISTORY.JOB_CALLING_TITLE,JOB_SETUP_MST.JSM_NAME,DIVISION_MST.DIVM_NAME \n" +
//                    "    FROM EMP_MST, EMP_JOB_HISTORY,  JOB_SETUP_DTL, JOB_SETUP_MST, DESIG_MST,DIVISION_MST\n" +
//                    "    WHERE EMP_JOB_HISTORY.JOB_STATUS NOT IN('Closed','Suspended')\n" +
//                    "    AND (JOB_SETUP_DTL.JSD_JSM_ID = JOB_SETUP_MST.JSM_ID)\n" +
//                    "    AND (JOB_SETUP_MST.JSM_DESIG_ID = DESIG_MST.DESIG_ID)\n" +
//                    "    AND (EMP_JOB_HISTORY.JOB_ID = EMP_MST.EMP_JOB_ID)\n" +
//                    "    AND (JOB_SETUP_DTL.JSD_ID = EMP_JOB_HISTORY.JOB_JSD_ID)\n" +
//                    "    AND EMP_JOB_HISTORY.JOB_EMAIL IS NOT NULL\n" +
//                    "    AND JOB_SETUP_MST.JSM_DIVM_ID=DIVISION_MST.DIVM_ID\n" +
//                    "    AND DESIG_MST.DESIG_PRIORITY IN (SELECT DISTINCT REGEXP_SUBSTR(LAH_APPROVAL_BAND,'[^,]+', 1, LEVEL) \n" +
//                    "                                      FROM (SELECT LAH_APPROVAL_BAND\n" +
//                    "                                      FROM LEAVE_APPROVAL_HIERARCHY\n" +
//                    "                                      WHERE LAH_BAND_NO=( SELECT DESIG_MST.DESIG_PRIORITY\n" +
//                    "                                                          FROM EMP_MST, EMP_JOB_HISTORY,  JOB_SETUP_DTL, JOB_SETUP_MST, DESIG_MST\n" +
//                    "                                                          WHERE EMP_MST.EMP_ID="+emp_id+"\n" +
//                    "                                                          AND (JOB_SETUP_DTL.JSD_JSM_ID = JOB_SETUP_MST.JSM_ID)\n" +
//                    "                                                          AND (JOB_SETUP_MST.JSM_DESIG_ID = DESIG_MST.DESIG_ID)\n" +
//                    "                                                          AND (EMP_JOB_HISTORY.JOB_ID = EMP_MST.EMP_JOB_ID)\n" +
//                    "                                                          AND (JOB_SETUP_DTL.JSD_ID = EMP_JOB_HISTORY.JOB_JSD_ID)))\n" +
//                    "                                      CONNECT BY REGEXP_SUBSTR(LAH_APPROVAL_BAND, '[^,]+', 1, LEVEL) IS NOT NULL)\n" +
//                    "UNION\n" +
//                    "SELECT EMP_MST.EMP_ID,EMP_MST.EMP_NAME,EMP_JOB_HISTORY.JOB_CALLING_TITLE,JOB_SETUP_MST.JSM_NAME,DIVISION_MST.DIVM_NAME \n" +
//                    "    FROM EMP_MST, EMP_JOB_HISTORY,  JOB_SETUP_DTL, JOB_SETUP_MST, DESIG_MST,DIVISION_MST\n" +
//                    "   WHERE EMP_JOB_HISTORY.JOB_STATUS NOT IN('Closed','Suspended')\n" +
//                    "    AND (JOB_SETUP_DTL.JSD_JSM_ID = JOB_SETUP_MST.JSM_ID)\n" +
//                    "    AND (JOB_SETUP_MST.JSM_DESIG_ID = DESIG_MST.DESIG_ID)\n" +
//                    "    AND (EMP_JOB_HISTORY.JOB_ID = EMP_MST.EMP_JOB_ID)\n" +
//                    "    AND (JOB_SETUP_DTL.JSD_ID = EMP_JOB_HISTORY.JOB_JSD_ID)\n" +
//                    "    AND EMP_JOB_HISTORY.JOB_EMAIL IS NOT NULL\n" +
//                    "    AND JOB_SETUP_MST.JSM_DIVM_ID=DIVISION_MST.DIVM_ID\n" +
//                    "    AND EMP_MST.EMP_CODE      IN\n" +
//                    "                                  (SELECT DISTINCT REGEXP_SUBSTR(LAH_SP_APPROVAL_CODE, '[^,]+', 1, Level)\n" +
//                    "                                  FROM\n" +
//                    "                                    (SELECT LEAVE_APPROVAL_HIERARCHY.LAH_APPROVAL_BAND,\n" +
//                    "                                      LEAVE_APPROVAL_HIERARCHY.LAH_SP_APPROVAL_CODE\n" +
//                    "                                    FROM LEAVE_APPROVAL_HIERARCHY\n" +
//                    "                                    WHERE LEAVE_APPROVAL_HIERARCHY.LAH_BAND_NO =\n" +
//                    "                                                                              (SELECT DESIG_MST.DESIG_PRIORITY\n" +
//                    "                                                                              FROM EMP_MST,\n" +
//                    "                                                                                EMP_JOB_HISTORY,\n" +
//                    "                                                                                JOB_SETUP_DTL,\n" +
//                    "                                                                                JOB_SETUP_MST,\n" +
//                    "                                                                                DESIG_MST\n" +
//                    "                                                                              WHERE (JOB_SETUP_DTL.JSD_JSM_ID = JOB_SETUP_MST.JSM_ID)\n" +
//                    "                                                                              AND (JOB_SETUP_MST.JSM_DESIG_ID = DESIG_MST.DESIG_ID)\n" +
//                    "                                                                              AND (EMP_JOB_HISTORY.JOB_ID     = EMP_MST.EMP_JOB_ID)\n" +
//                    "                                                                              AND (JOB_SETUP_DTL.JSD_ID       = EMP_JOB_HISTORY.JOB_JSD_ID)\n" +
//                    "                                                                              AND EMP_MST.EMP_ID              = "+emp_id+"\n" +
//                    "                                                                              )\n" +
//                    "                                    )\n" +
//                    "                                    CONNECT BY regexp_substr(LAH_SP_APPROVAL_CODE, '[^,]+', 1, Level) IS NOT NULL\n" +
//                    "                                  )");
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
//            ResultSet rs3 = stmt.executeQuery("SELECT EMP_MST.EMP_ID,EMP_MST.EMP_NAME,EMP_JOB_HISTORY.JOB_CALLING_TITLE,JOB_SETUP_MST.JSM_NAME,DIVISION_MST.DIVM_NAME \n" +
//                    "      FROM EMP_MST, EMP_JOB_HISTORY,JOB_SETUP_DTL, JOB_SETUP_MST, DESIG_MST,DIVISION_MST\n" +
//                    "      WHERE (JOB_SETUP_DTL.JSD_JSM_ID = JOB_SETUP_MST.JSM_ID)\n" +
//                    "      AND (JOB_SETUP_MST.JSM_DESIG_ID = DESIG_MST.DESIG_ID)\n" +
//                    "      AND (EMP_JOB_HISTORY.JOB_ID = EMP_MST.EMP_JOB_ID)\n" +
//                    "      AND (JOB_SETUP_DTL.JSD_ID = EMP_JOB_HISTORY.JOB_JSD_ID)\n" +
//                    "      AND EMP_JOB_HISTORY.JOB_EMAIL IS NOT NULL\n" +
//                    "      AND JOB_SETUP_MST.JSM_DIVM_ID=DIVISION_MST.DIVM_ID\n" +
//                    "      AND EMP_MST.EMP_CODE IN (SELECT DISTINCT REGEXP_SUBSTR(LAH_SP_APPROVAL_CODE,'[^,]+', 1, LEVEL) \n" +
//                    "                                        FROM (SELECT LAH_SP_APPROVAL_CODE\n" +
//                    "                                        FROM LEAVE_APPROVAL_HIERARCHY\n" +
//                    "                                        WHERE LAH_BAND_NO=( SELECT DESIG_MST.DESIG_PRIORITY\n" +
//                    "                                                          FROM EMP_MST, EMP_JOB_HISTORY,  JOB_SETUP_DTL, JOB_SETUP_MST, DESIG_MST\n" +
//                    "                                                          WHERE EMP_MST.EMP_ID="+emp_id+"\n" +
//                    "                                                          AND (JOB_SETUP_DTL.JSD_JSM_ID = JOB_SETUP_MST.JSM_ID)\n" +
//                    "                                                          AND (JOB_SETUP_MST.JSM_DESIG_ID = DESIG_MST.DESIG_ID)\n" +
//                    "                                                          AND (EMP_JOB_HISTORY.JOB_ID = EMP_MST.EMP_JOB_ID)\n" +
//                    "                                                          AND (JOB_SETUP_DTL.JSD_ID = EMP_JOB_HISTORY.JOB_JSD_ID)))\n" +
//                    "                                        CONNECT BY REGEXP_SUBSTR(LAH_SP_APPROVAL_CODE, '[^,]+', 1, LEVEL) IS NOT NULL)  ");
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
//                           // selectedApproverList = approverEmployee;
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
//
//
//
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

    //--------------------------------------------
    public void getDataInfo() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;

        locUpdateLists = new ArrayList<>();
        allShiftDetails = new ArrayList<>();

        allApproverDivision = new ArrayList<>();
        allApproverWithoutDiv = new ArrayList<>();
        allApproverEmp = new ArrayList<>();
        allSelectedApprover = new ArrayList<>();

        String companyOfficeUrl = "http://103.56.208.123:8001/apex/ttrams/attendanceUpNewReq/getCompanyOffice";
        String shiftsUrl = "http://103.56.208.123:8001/apex/ttrams/attendanceUpNewReq/getShiftLists";
        String empInfoUrl = "http://103.56.208.123:8001/apex/ttrams/attendanceUpNewReq/getEmpInfo/"+emp_id+"";
        String forwardListsWithDivUrl = "http://103.56.208.123:8001/apex/ttrams/attendanceUpNewReq/getForward_ApproverListWithDiv/"+emp_id+"";
        String forwardWithoutDivUrl = "http://103.56.208.123:8001/apex/ttrams/attendanceUpNewReq/getFor_AppListWIthoutDiv/"+emp_id+"";
        String forwardAllUrl = "http://103.56.208.123:8001/apex/ttrams/attendanceUpNewReq/getAllFor_AppList/"+emp_id+"";
        String desigPriorUrl = "http://103.56.208.123:8001/apex/ttrams/forwardReq/getDesigPriority/"+emp_id+"";

        RequestQueue requestQueue = Volley.newRequestQueue(AttendanceUpdate.this);

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
                    getForwarderApproverList();
                }
                else {
                    connected = true;
                    updateLay();
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                updateLay();
            }
        }, error -> {
            error.printStackTrace();
            conn = false;
            connected = false;
            updateLay();
        });

        StringRequest forwardAllReq = new StringRequest(Request.Method.GET, forwardAllUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject forwardInfo = array.getJSONObject(i);

                        String emp_id_new = forwardInfo.getString("emp_id")
                                .equals("null") ? "" : forwardInfo.getString("emp_id");
                        String emp_name_new  = forwardInfo.getString("emp_name")
                                .equals("null") ? "" : forwardInfo.getString("emp_name");
                        String job_calling_title_new = forwardInfo.getString("job_calling_title")
                                .equals("null") ? "" : forwardInfo.getString("job_calling_title");
                        String jsm_name_new = forwardInfo.getString("jsm_name")
                                .equals("null") ? "" : forwardInfo.getString("jsm_name");
                        String divm_name_new = forwardInfo.getString("divm_name")
                                .equals("null") ? "" : forwardInfo.getString("divm_name");

                        allApproverEmp.add(new SelectAllList(emp_id_new,emp_name_new,job_calling_title_new,
                                jsm_name_new,divm_name_new));
                    }
                }
                requestQueue.add(desigPriorReq);
            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                updateLay();
            }
        }, error -> {
            error.printStackTrace();
            conn = false;
            connected = false;
            updateLay();
        });

        StringRequest forwardWithoutDivReq = new StringRequest(Request.Method.GET, forwardWithoutDivUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {

                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject forwardInfo = array.getJSONObject(i);

                        String emp_id_new = forwardInfo.getString("emp_id")
                                .equals("null") ? "" : forwardInfo.getString("emp_id");
                        String emp_name_new  = forwardInfo.getString("emp_name")
                                .equals("null") ? "" : forwardInfo.getString("emp_name");
                        String job_calling_title_new = forwardInfo.getString("job_calling_title")
                                .equals("null") ? "" : forwardInfo.getString("job_calling_title");
                        String jsm_name_new = forwardInfo.getString("jsm_name")
                                .equals("null") ? "" : forwardInfo.getString("jsm_name");
                        String divm_name_new = forwardInfo.getString("divm_name")
                                .equals("null") ? "" : forwardInfo.getString("divm_name");

                        allApproverWithoutDiv.add(new SelectAllList(emp_id_new,emp_name_new,job_calling_title_new,
                                jsm_name_new,divm_name_new));
                    }
                }

                requestQueue.add(forwardAllReq);
            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                updateLay();
            }
        }, error -> {
            error.printStackTrace();
            conn = false;
            connected = false;
            updateLay();
        });

        StringRequest forwardWithDivReq = new StringRequest(Request.Method.GET, forwardListsWithDivUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject forwardInfo = array.getJSONObject(i);

                        String emp_id_new = forwardInfo.getString("emp_id")
                                .equals("null") ? "" : forwardInfo.getString("emp_id");
                        String emp_name_new  = forwardInfo.getString("emp_name")
                                .equals("null") ? "" : forwardInfo.getString("emp_name");
                        String job_calling_title_new = forwardInfo.getString("job_calling_title")
                                .equals("null") ? "" : forwardInfo.getString("job_calling_title");
                        String jsm_name_new = forwardInfo.getString("jsm_name")
                                .equals("null") ? "" : forwardInfo.getString("jsm_name");
                        String divm_name_new = forwardInfo.getString("divm_name")
                                .equals("null") ? "" : forwardInfo.getString("divm_name");


                        allApproverDivision.add(new SelectAllList(emp_id_new,emp_name_new,job_calling_title_new,
                                jsm_name_new,divm_name_new));
                    }
                }

                requestQueue.add(forwardWithoutDivReq);
            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                updateLay();
            }
        }, error -> {
            error.printStackTrace();
            conn = false;
            connected = false;
            updateLay();
        });

        StringRequest empInfoReq = new StringRequest(Request.Method.GET, empInfoUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject empSomeInfo = array.getJSONObject(i);

                        selected_jsm_id = empSomeInfo.getString("jsm_id");
                        calling_title = empSomeInfo.getString("job_calling_title");
                        selected_dept_id = empSomeInfo.getString("dept_id");
                        selected_divm_id = empSomeInfo.getString("divm_id");
                    }
                }

                requestQueue.add(forwardWithDivReq);
            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                updateLay();
            }
        }, error -> {
            error.printStackTrace();
            conn = false;
            connected = false;
            updateLay();
        });

        StringRequest shiftReq = new StringRequest(Request.Method.GET, shiftsUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject shiftInfo = array.getJSONObject(i);

                        String osm_id_new = shiftInfo.getString("osm_id");
                        String osm_name_name = shiftInfo.getString("osm_name");
                        String start_time_new = shiftInfo.getString("start_time");
                        String late_after_new = shiftInfo.getString("late_after");
                        String end_time_new = shiftInfo.getString("end_time");

                        allShiftDetails.add(new SelectAllList(osm_id_new,osm_name_name,start_time_new,
                                late_after_new,end_time_new));

                    }
                }

                requestQueue.add(empInfoReq);
            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                updateLay();
            }
        }, error -> {
            error.printStackTrace();
            conn = false;
            connected = false;
            updateLay();
        });

        StringRequest comOffReq = new StringRequest(Request.Method.GET, companyOfficeUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject compOffInfo = array.getJSONObject(i);
                        String coa = compOffInfo.getString("coa_id");
                        String coa_name = compOffInfo.getString("coa_name");

                        locUpdateLists.add(new LocUpdateList(coa,coa_name));
                    }
                }
                requestQueue.add(shiftReq);
            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                updateLay();
            }
        }, error -> {
            error.printStackTrace();
            conn = false;
            connected = false;
            updateLay();
        });

        requestQueue.add(comOffReq);
    }

    public void getForwarderApproverList() {

        String approvalBandUrl = "http://103.56.208.123:8001/apex/ttrams/forwardReq/getApprovalBand/"+desig_priority+"";
        String countApp1Url = "http://103.56.208.123:8001/apex/ttrams/forwardReq/getCountApprovEmp/"+divm_id+"/"+desig_priority+"";
        String countApp2Url = "http://103.56.208.123:8001/apex/ttrams/forwardReq/getCountApprovEmp_2/"+desig_priority+"";

        RequestQueue requestQueue = Volley.newRequestQueue(AttendanceUpdate.this);

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

                connected = true;
                updateLay();

            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                updateLay();
            }
        }, error -> {
            error.printStackTrace();
            conn = false;
            connected = false;
            updateLay();
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
                    connected = true;
                    updateLay();
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                updateLay();
            }
        }, error -> {
            error.printStackTrace();
            conn = false;
            connected = false;
            updateLay();
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
                    connected = true;
                    updateLay();
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                updateLay();
            }
        }, error -> {
            error.printStackTrace();
            conn = false;
            connected = false;
            updateLay();
        });

        requestQueue.add(appBandReq);
    }

    public void updateLay() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                if (locUpdateLists.size() != 0) {
                    for (int i = 0; i < locUpdateLists.size(); i++) {
                        onlyLocationLists.add(locUpdateLists.get(i).getLocation());
                    }
                }
                locUpdateAdapter.notifyDataSetChanged();

                conn = false;
                connected = false;
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(AttendanceUpdate.this)
                        .setMessage("There is a network issue in the server. Please Try later.")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel", null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    getDataInfo();
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
            AlertDialog dialog = new AlertDialog.Builder(AttendanceUpdate.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel", null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                getDataInfo();
                dialog.dismiss();
            });

            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> {
                dialog.dismiss();
                finish();
            });
        }
    }

//    public void MachineData(String id) {
//        try {
//            this.connection = createConnection();
//
//            Statement stmt = connection.createStatement();
//
//
//            ResultSet rs=stmt.executeQuery("SELECT AMS_MECHINE_CODE, AMS_ATTENDANCE_TYPE\n" +
//                    "\t\tFROM ATTENDANCE_MECHINE_SETUP\n" +
//                    "\t\tWHERE AMS_ID = (SELECT MAX(AMS_ID)\n" +
//                    "\t\t\t\t\t\t\t\t\t\tFROM ATTENDANCE_MECHINE_SETUP\n" +
//                    "\t\t\t\t\t\t\t\t\t\tWHERE AMS_COA_ID = "+id+")");
//
//
//
//            while(rs.next())  {
//                machineCode = rs.getString(1);
//                machineType = rs.getString(2);
//            }
//
//
//            machineCon = true;
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

    //--------------------------------------------
    public void getMachineData() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        machineConnn = false;
        machineCon = false;

        machineCode = "";
        machineType = "";

        String url = "http://103.56.208.123:8001/apex/ttrams/attendanceUpNewReq/getMachineData/"+selected_loc_id+"";
        RequestQueue requestQueue = Volley.newRequestQueue(AttendanceUpdate.this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            machineConnn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject macInfo = array.getJSONObject(i);
                        machineCode = macInfo.getString("ams_mechine_code")
                                .equals("null") ? "" : macInfo.getString("ams_mechine_code");
                        machineType = macInfo.getString("ams_attendance_type")
                                .equals("null") ? "" : macInfo.getString("ams_attendance_type");
                    }
                }
                machineCon = true;
                updateMachine();
            }
            catch (JSONException e) {
                e.printStackTrace();
                machineCon = false;
                updateMachine();
            }
        }, error -> {
            error.printStackTrace();
            machineConnn = false;
            machineCon = false;
            updateMachine();
        });

        requestQueue.add(stringRequest);
    }

    private void updateMachine() {
        waitProgress.dismiss();
        if (machineConnn) {
            if (machineCon) {
                if (!machineCode.isEmpty() && !machineType.isEmpty()) {
                    machCode.setText(machineCode);
                    machType.setText(machineType);
                }

                machineConnn = false;
                machineCon = false;
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(AttendanceUpdate.this)
                        .setMessage("There is a network issue in the server. Please Try later.")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    getMachineData();
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
            AlertDialog dialog = new AlertDialog.Builder(AttendanceUpdate.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel",null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                getMachineData();
                dialog.dismiss();
            });

            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> {
                dialog.dismiss();
                finish();
            });
        }
    }

//    public void ReasonData(String reason) {
//        try {
//            this.connection = createConnection();
//
//            Statement stmt = connection.createStatement();
//
//            reasonLists = new ArrayList<>();
//
//
//            ResultSet rs=stmt.executeQuery(" SELECT ATTD_UP_REQ_REASON_MST.AURRM_ID,\n" +
//                    "  ATTD_UP_REQ_REASON_MST.AURRM_NAME\n" +
//                    "  FROM ATTD_UP_REQ_REASON_MST\n" +
//                    "WHERE ATTD_UP_REQ_REASON_MST.AURRM_REQ_TYPE= '"+reason+"'\n" +
//                    "ORDER BY ATTD_UP_REQ_REASON_MST.AURRM_NAME");
//
//
//
//            while(rs.next())  {
//               reasonLists.add(new ReasonList(rs.getString(1),rs.getString(2)));
//            }
//
//
//            reasonCon = true;
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

    //--------------------------------------------
    public void getReasonData() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        reasonConnnn = false;
        reasonCon = false;

        reasonLists = new ArrayList<>();

        String url = "http://103.56.208.123:8001/apex/ttrams/attendanceUpNewReq/getReasonData/"+selected_attendance_type+"";
        RequestQueue requestQueue = Volley.newRequestQueue(AttendanceUpdate.this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            reasonConnnn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject reasonInfo = array.getJSONObject(i);

                        String aurrm_id = reasonInfo.getString("aurrm_id");
                        String aurrm_name = reasonInfo.getString("aurrm_name");

                        reasonLists.add(new ReasonList(aurrm_id,aurrm_name));
                    }
                }
                reasonCon = true;
                updateReason();
            }
            catch (JSONException e) {
                e.printStackTrace();
                reasonCon = false;
                updateReason();
            }
        }, error -> {
            error.printStackTrace();
            reasonConnnn = false;
            reasonCon = false;
            updateReason();
        });

        requestQueue.add(stringRequest);
    }

    private void updateReason() {
        waitProgress.dismiss();
        if (reasonConnnn) {
            if (reasonCon) {
                reasonName = new ArrayList<>();
                reasonName.add("Select");

                if (reasonLists.size() != 0) {
                    for (int i = 0; i < reasonLists.size(); i++) {
                        reasonName.add(reasonLists.get(i).getReason_name());
                        reasonAdapter.setNotifyOnChange(true);
                        System.out.println(reasonLists.get(i).getReason_name());
                    }
                }

                reasonAdapter.clear();
                reasonAdapter.addAll(reasonName);
                reasonAdapter.notifyDataSetChanged();
                reasonType.setSelection(0);

                reasonConnnn = false;
                reasonCon = false;
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(AttendanceUpdate.this)
                        .setMessage("There is a network issue in the server. Please Try later.")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    getReasonData();
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
            AlertDialog dialog = new AlertDialog.Builder(AttendanceUpdate.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel",null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                getReasonData();
                dialog.dismiss();
            });

            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> {
                dialog.dismiss();
                finish();
            });
        }
    }

//    public void InsertReq() {
//        try {
//            this.connection = createConnection();
//
//            Statement stmt = connection.createStatement();
//
//
//
//            ResultSet rs=stmt.executeQuery("  SELECT NVL(MAX(DARM_ID),0)+1 --INTO :DAILY_ATTEN_REQ_MST.DARM_ID \n" +
//                    "\tFROM DAILY_ATTEN_REQ_MST");
//
//            while(rs.next())  {
//                darm_id = rs.getString(1);
//            }
//
//            if (!darm_id.isEmpty()) {
//                ResultSet rs1 = stmt.executeQuery("SELECT EMP_CODE --INTO V_APP_CODE\n" +
//                        "  FROM EMP_MST\n" +
//                        "  WHERE EMP_ID="+emp_id+"");
//
//                while (rs1.next()) {
//                    app_code = rs1.getString(1);
//
//                }
//                if (!app_code.isEmpty()) {
//
//                    ResultSet rs2 = stmt.executeQuery("SELECT NVL(MAX(DARM_APP_CODE_ID),0)+1 --INTO V_ID \n" +
//                            "  FROM DAILY_ATTEN_REQ_MST\n" +
//                            "  WHERE TO_CHAR(DARM_DATE,'RRRR')=TO_CHAR(TO_DATE('"+now_date+"','DD-MON-YY'),'RRRR')\n" +
//                            "  AND DARM_EMP_ID="+emp_id+"");
//                    while (rs2.next()) {
//                        app_code_id = rs2.getString(1);
//                    }
//                    if (!app_code_id.isEmpty()) {
//
//                        ResultSet rs3 = stmt.executeQuery(" Select TO_CHAR(TO_DATE('"+now_date+"','DD-MON-YY'),'RRRR')||'/'||'"+app_code+"'||'/'||LTRIM(RTRIM('"+app_code_id+"')) from Dual");
//
//                        while (rs3.next()) {
//                            app_code = rs3.getString(1);
//                            System.out.println(app_code);
//                        }
//                        if (!app_code.isEmpty()) {
//
//                            System.out.println("DARM_ID: " + darm_id);
//                            System.out.println("EMP ID: "+ emp_id);
//                            System.out.println("REASON DESC: "+ selected_reason_desc);
//                            System.out.println("Address Station: "+ selected_address_station);
//                            System.out.println("Updated Date: "+ selected_update_date);
//                            System.out.println("Now Date: "+ now_date);
//                            System.out.println("Dept ID: "+ selected_dept_id);
//                            System.out.println("Divm ID: "+ selected_divm_id);
//                            System.out.println("JSM ID: "+ selected_jsm_id);
//                            System.out.println("Calling Title: " + calling_title);
//                            System.out.println("Request: "+ selected_request);
//                            System.out.println("App Code: "+ app_code);
//                            System.out.println("APP CODE ID: "+ app_code_id);
//                            System.out.println("Approver ID: "+ selected_approver_id);
//                            System.out.println("Attendance Type: " + selected_attendance_type);
//                            System.out.println("Depart Time: "+ depart_time);
//                            System.out.println("Arrival Time: " + arrival_time);
//                            System.out.println("Loc ID: "+ selected_loc_id);
//                            System.out.println("Shift: "+ selected_shift_id);
//                            System.out.println("Reason ID: "+ selected_reason_id);
//
//                            if (arrival_time.isEmpty() && !depart_time.isEmpty()) {
//                                System.out.println("INSERTED TEST1");
//                                stmt.executeUpdate("Insert into DAILY_ATTEN_REQ_MST\n" +
//                                        "   (DARM_ID, DARM_EMP_ID, DARM_REASON, DARM_ADD_DURING_CAUSE, DARM_UPDATE_DATE, \n" +
//                                        "    DARM_DATE, DARM_DEPT_ID, DARM_DIVM_ID, DARM_COMMENTS, DARM_APPROVED, \n" +
//                                        "    DARM_TEL_DURING_CAUSE, DARM_JSM_ID, DARM_CALLING_TITLE, DARM_ENTRY_USER, DARM_APPLICATION_TYPE, \n" +
//                                        "    DARM_APP_REJECT_EMP_ID, DARM_APP_REJECT_CALLING_TITLE, DARM_APP_REJECT_JSM_ID, DARM_APP_REJECT_DEPT_ID, DARM_APP_REJECT_DIVM_ID, \n" +
//                                        "    DARM_APP_CODE, DARM_APP_CODE_ID, DARM_APP_REJ_DATE, DARM_CANCAL_EMP_ID, DARM_CANCAL_CALLING_TITLE, \n" +
//                                        "    DARM_CANCAL_JSM_ID, DARM_CANCAL_DEPT_ID, DARM_CANCAL_DIVM_ID, DARM_CANCAL_DATE, DARM_CANCAL_COMMENTS, \n" +
//                                        "    DARM_WORK_BACK_EMP_ID, DARM_APPLIED_TO_ID, DARM_UPDATE_TIME, DARM_REQ_TYPE, DARM_REQ_ARRIVAL_TIME, \n" +
//                                        "    DARM_REQ_DEPART_TIME, DARM_REQ_LOCATION_ID, DARM_ARRIVAL_LOG, DARM_DEPARTURE_LOG, DARM_LOCATION_LOG, \n" +
//                                        "    DARM_REQ_OSM_ID, DARM_AURRM_ID,DARM_INSERT_APPLICATION_FLAG,DARM_CREATED_BY)\n" +
//                                        " Values\n" +
//                                        "   ("+darm_id+", "+emp_id+", '"+selected_reason_desc+"', '"+selected_address_station+"', TO_DATE('"+selected_update_date+"', 'DD-MON-YY'), \n" +
//                                        "    TO_DATE('"+now_date+"', 'DD-MON-YY'), "+selected_dept_id+", "+selected_divm_id+", NULL, 0, \n" +
//                                        "    NULL, "+selected_jsm_id+", '"+calling_title+"', '"+user_id+"', '"+selected_request+"', \n" +
//                                        "    NULL, NULL, NULL, NULL, NULL, \n" +
//                                        "    '"+app_code+"', "+app_code_id+", NULL, NULL, NULL, \n" +
//                                        "    NULL, NULL, NULL, NULL, NULL, \n" +
//                                        "    NULL, "+selected_approver_id+", NULL, '"+selected_attendance_type+"', NULL, \n" +
//                                        "    TO_DATE('"+selected_update_date+" "+depart_time+"', 'DD-MON-YY HH12:MIPM'), "+selected_loc_id+", NULL, NULL, NULL, \n" +
//                                        "    "+selected_shift_id+", "+selected_reason_id+",1,'"+user_id+"')");
//                            }
//                            else if (depart_time.isEmpty() && !arrival_time.isEmpty()) {
//                                System.out.println("INSERTED TEST2");
//                                stmt.executeUpdate("Insert into DAILY_ATTEN_REQ_MST\n" +
//                                        "   (DARM_ID, DARM_EMP_ID, DARM_REASON, DARM_ADD_DURING_CAUSE, DARM_UPDATE_DATE, \n" +
//                                        "    DARM_DATE, DARM_DEPT_ID, DARM_DIVM_ID, DARM_COMMENTS, DARM_APPROVED, \n" +
//                                        "    DARM_TEL_DURING_CAUSE, DARM_JSM_ID, DARM_CALLING_TITLE, DARM_ENTRY_USER, DARM_APPLICATION_TYPE, \n" +
//                                        "    DARM_APP_REJECT_EMP_ID, DARM_APP_REJECT_CALLING_TITLE, DARM_APP_REJECT_JSM_ID, DARM_APP_REJECT_DEPT_ID, DARM_APP_REJECT_DIVM_ID, \n" +
//                                        "    DARM_APP_CODE, DARM_APP_CODE_ID, DARM_APP_REJ_DATE, DARM_CANCAL_EMP_ID, DARM_CANCAL_CALLING_TITLE, \n" +
//                                        "    DARM_CANCAL_JSM_ID, DARM_CANCAL_DEPT_ID, DARM_CANCAL_DIVM_ID, DARM_CANCAL_DATE, DARM_CANCAL_COMMENTS, \n" +
//                                        "    DARM_WORK_BACK_EMP_ID, DARM_APPLIED_TO_ID, DARM_UPDATE_TIME, DARM_REQ_TYPE, DARM_REQ_ARRIVAL_TIME, \n" +
//                                        "    DARM_REQ_DEPART_TIME, DARM_REQ_LOCATION_ID, DARM_ARRIVAL_LOG, DARM_DEPARTURE_LOG, DARM_LOCATION_LOG, \n" +
//                                        "    DARM_REQ_OSM_ID, DARM_AURRM_ID,DARM_INSERT_APPLICATION_FLAG,DARM_CREATED_BY)\n" +
//                                        " Values\n" +
//                                        "   ("+darm_id+", "+emp_id+", '"+selected_reason_desc+"', '"+selected_address_station+"', TO_DATE('"+selected_update_date+"', 'DD-MON-YY'), \n" +
//                                        "    TO_DATE('"+now_date+"', 'DD-MON-YY'), "+selected_dept_id+", "+selected_divm_id+", NULL, 0, \n" +
//                                        "    NULL, "+selected_jsm_id+", '"+calling_title+"', '"+user_id+"', '"+selected_request+"', \n" +
//                                        "    NULL, NULL, NULL, NULL, NULL, \n" +
//                                        "    '"+app_code+"', "+app_code_id+", NULL, NULL, NULL, \n" +
//                                        "    NULL, NULL, NULL, NULL, NULL, \n" +
//                                        "    NULL, "+selected_approver_id+", NULL, '"+selected_attendance_type+"', TO_DATE('"+selected_update_date+" "+arrival_time+"', 'DD-MON-YY HH12:MIPM'), \n" +
//                                        "    NULL, "+selected_loc_id+", NULL, NULL, NULL, \n" +
//                                        "    "+selected_shift_id+", "+selected_reason_id+",1,'"+user_id+"')");
//                            }
//                            else {
//                                System.out.println("INSERTED TEST3");
////                                stmt.executeUpdate("Insert into DAILY_ATTEN_REQ_MST\n" +
////                                        "   (DARM_ID, DARM_EMP_ID, DARM_REASON, DARM_ADD_DURING_CAUSE, DARM_UPDATE_DATE, \n" +
////                                        "    DARM_DATE, DARM_DEPT_ID, DARM_DIVM_ID, DARM_COMMENTS, DARM_APPROVED, \n" +
////                                        "    DARM_TEL_DURING_CAUSE, DARM_JSM_ID, DARM_CALLING_TITLE, DARM_ENTRY_USER, DARM_APPLICATION_TYPE, \n" +
////                                        "    DARM_APP_REJECT_EMP_ID, DARM_APP_REJECT_CALLING_TITLE, DARM_APP_REJECT_JSM_ID, DARM_APP_REJECT_DEPT_ID, DARM_APP_REJECT_DIVM_ID, \n" +
////                                        "    DARM_APP_CODE, DARM_APP_CODE_ID, DARM_APP_REJ_DATE, DARM_CANCAL_EMP_ID, DARM_CANCAL_CALLING_TITLE, \n" +
////                                        "    DARM_CANCAL_JSM_ID, DARM_CANCAL_DEPT_ID, DARM_CANCAL_DIVM_ID, DARM_CANCAL_DATE, DARM_CANCAL_COMMENTS, \n" +
////                                        "    DARM_WORK_BACK_EMP_ID, DARM_APPLIED_TO_ID, DARM_UPDATE_TIME, DARM_REQ_TYPE, DARM_REQ_ARRIVAL_TIME, \n" +
////                                        "    DARM_REQ_DEPART_TIME, DARM_REQ_LOCATION_ID, DARM_ARRIVAL_LOG, DARM_DEPARTURE_LOG, DARM_LOCATION_LOG, \n" +
////                                        "    DARM_REQ_OSM_ID, DARM_AURRM_ID,DARM_INSERT_APPLICATION_FLAG,DARM_CREATED_BY)\n" +
////                                        " Values\n" +
////                                        "   ("+darm_id+", "+emp_id+", '"+selected_reason_desc+"', '"+selected_address_station+"', TO_DATE('"+selected_update_date+"', 'DD-MON-YY'), \n" +
////                                        "    TO_DATE('"+now_date+"', 'DD-MON-YY'), "+selected_dept_id+", "+selected_divm_id+", NULL, 0, \n" +
////                                        "    NULL, "+selected_jsm_id+", '"+calling_title+"', '"+emp_id+"', '"+selected_request+"', \n" +
////                                        "    NULL, NULL, NULL, NULL, NULL, \n" +
////                                        "    '"+app_code+"', "+app_code_id+", NULL, NULL, NULL, \n" +
////                                        "    NULL, NULL, NULL, NULL, NULL, \n" +
////                                        "    NULL, "+selected_approver_id+", NULL, '"+selected_attendance_type+"', TO_DATE('"+selected_update_date+" "+arrival_time+"', 'DD-MON-YY HH12:MIPM'), \n" +
////                                        "    TO_DATE('"+selected_update_date+" "+depart_time+"', 'DD-MON-YY HH12:MIPM'), "+selected_loc_id+", NULL, NULL, NULL, \n" +
////                                        "    "+selected_shift_id+", "+selected_reason_id+",1,'"+emp_id+"')");
//                                stmt.executeUpdate("Insert into DAILY_ATTEN_REQ_MST\n" +
//                                        "                                           (DARM_ID, DARM_EMP_ID, DARM_REASON, DARM_ADD_DURING_CAUSE, DARM_UPDATE_DATE,  \n" +
//                                        "                                            DARM_DATE, DARM_DEPT_ID, DARM_DIVM_ID, DARM_APPROVED, DARM_JSM_ID, \n" +
//                                        "                                            DARM_CALLING_TITLE, DARM_ENTRY_USER, DARM_APPLICATION_TYPE, DARM_APP_CODE, DARM_APP_CODE_ID, \n" +
//                                        "                                            DARM_APPLIED_TO_ID, DARM_REQ_TYPE, DARM_REQ_ARRIVAL_TIME, DARM_REQ_DEPART_TIME, DARM_REQ_LOCATION_ID, \n" +
//                                        "                                            DARM_REQ_OSM_ID, DARM_AURRM_ID,DARM_INSERT_APPLICATION_FLAG,DARM_CREATED_BY)\n" +
//                                        "                                         Values\n" +
//                                        "                                           ("+darm_id+", "+emp_id+", '"+selected_reason_desc+"', '"+selected_address_station+"', TO_DATE('"+selected_update_date+"', 'DD-MON-YY'), \n" +
//                                        "                                            TO_DATE('"+now_date+"', 'DD-MON-YY'), "+selected_dept_id+", "+selected_divm_id+", 0, \n" +
//                                        "                                            "+selected_jsm_id+", '"+calling_title+"', '"+user_id+"', '"+selected_request+"', '"+app_code+"', "+app_code_id+",\n" +
//                                        "                                            "+selected_approver_id+", '"+selected_attendance_type+"', TO_DATE('"+selected_update_date+" "+arrival_time+"', 'DD-MON-YY HH12:MIPM'), \n" +
//                                        "                                            TO_DATE('"+selected_update_date+" "+depart_time+"', 'DD-MON-YY HH12:MIPM'), "+selected_loc_id+", \n" +
//                                        "                                            "+selected_shift_id+", "+selected_reason_id+",1,'"+user_id+"')");
//                            }
//
//                        }
//
//                    }
//                }
//            }
//
//
//            insertCon = true;
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

    //--------------------------------------------
    public void insertReq() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);

        insertConnn = false;
        insertCon = false;
        isInserted = false;

        String insertUrl = "http://103.56.208.123:8001/apex/ttrams/attendanceUpNewReq/insertAttUpReq";

        RequestQueue requestQueue = Volley.newRequestQueue(AttendanceUpdate.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, insertUrl, response -> {
            insertConnn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String string_out = jsonObject.getString("string_out");
                String updated_req = jsonObject.getString("updated_req");
                if (string_out.equals("Successfully Created")) {
                    insertCon = true;
                    isInserted = updated_req.equals("true");
                }
                else {
                    System.out.println(string_out);
                    insertCon = false;
                }
                updateInsertLay();
            }
            catch (JSONException e) {
                insertCon = false;
                updateInsertLay();
            }
        }, error -> {
            error.printStackTrace();
            insertConnn = false;
            insertCon = false;
            updateInsertLay();
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("NOW_DATE", now_date);
                headers.put("P_ADDRESS",selected_address_station);
                headers.put("P_ARRIVAL_TIME",arrival_time);
                headers.put("P_CALLING_TITLE",calling_title);
                headers.put("P_DEPART_TIME", depart_time);
                headers.put("P_DEPT_ID",selected_dept_id);
                headers.put("P_DIVM_ID",selected_divm_id);
                headers.put("P_EMP_ID",emp_id);
                headers.put("P_JSM_ID",selected_jsm_id);
                headers.put("P_LOC_ID",selected_loc_id);
                headers.put("P_REASON_DETAILS",selected_reason_desc);
                headers.put("P_REASON_ID",selected_reason_id);
                headers.put("P_SELECTED_APPROVER_ID",selected_approver_id);
                headers.put("P_SELECTED_ATT_TYPE",selected_attendance_type);
                headers.put("P_SELECTED_REQUEST",selected_request);
                headers.put("P_SELECTED_UPDATE_DATE",selected_update_date);
                headers.put("P_SHIFT_ID",selected_shift_id);
                headers.put("P_USER_ID",user_id);
                return headers;
            }
        };

        requestQueue.add(stringRequest);
    }

    private void updateInsertLay() {
        waitProgress.dismiss();
        if (insertConnn) {
            if (insertCon) {
                if (isInserted) {
                    selected_shift_id = "";
                    selected_approver_id = "";
                    System.out.println("INSERTED");

                    AlertDialog dialog = new AlertDialog.Builder(AttendanceUpdate.this)
                            .setMessage("Request Sent Successfully")
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
                }
                else {
                    Toast.makeText(getApplicationContext(), "Failed to Send Request.", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(AttendanceUpdate.this)
                        .setMessage("There is a network issue in the server. Please Try later.")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel", null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    insertReq();
                    dialog.dismiss();
                });

                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negative.setOnClickListener(v -> dialog.dismiss());
            }
        }
        else {
            AlertDialog dialog = new AlertDialog.Builder(AttendanceUpdate.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel", null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                insertReq();
                dialog.dismiss();
            });

            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> dialog.dismiss());
        }
    }

}