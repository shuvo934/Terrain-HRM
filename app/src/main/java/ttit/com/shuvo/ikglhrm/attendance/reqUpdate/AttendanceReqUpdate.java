package ttit.com.shuvo.ikglhrm.attendance.reqUpdate;

//import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

//import android.app.DatePickerDialog;
//import android.app.TimePickerDialog;
//import android.content.Context;
//import android.content.res.ColorStateList;
//import android.graphics.Color;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.Gravity;
import android.view.View;
//import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.DatePicker;
//import android.widget.LinearLayout;
//import android.widget.Spinner;
//import android.widget.TextView;
//import android.widget.TimePicker;
//import android.widget.Toast;
//
//import com.google.android.material.textfield.TextInputEditText;
//import com.google.android.material.textfield.TextInputLayout;
//
//import java.io.IOException;
//import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.Statement;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.Locale;
//
//import javax.crypto.Mac;

import ttit.com.shuvo.ikglhrm.R;
//import ttit.com.shuvo.ikglhrm.WaitProgress;
//import ttit.com.shuvo.ikglhrm.attendance.reqUpdate.dialogueFromReq.SelectReqList;
//import ttit.com.shuvo.ikglhrm.attendance.reqUpdate.dialogueFromReq.SelectRequest;
//import ttit.com.shuvo.ikglhrm.attendance.update.AttendanceReqType;
//import ttit.com.shuvo.ikglhrm.attendance.update.AttendanceUpdate;
//import ttit.com.shuvo.ikglhrm.attendance.update.LocUpdateList;
//import ttit.com.shuvo.ikglhrm.attendance.update.ReasonList;
//import ttit.com.shuvo.ikglhrm.attendance.update.dialogue.DialogueText;
//import ttit.com.shuvo.ikglhrm.attendance.update.dialogue.SelectAll;
//import ttit.com.shuvo.ikglhrm.attendance.update.dialogue.SelectAllList;
//import ttit.com.shuvo.ikglhrm.attendance.update.dialogue.ShowAttendance;
//import ttit.com.shuvo.ikglhrm.attendance.update.dialogue.ShowShift;
//
//import static ttit.com.shuvo.ikglhrm.Login.userInfoLists;

public class AttendanceReqUpdate extends AppCompatActivity {


    /*public static int dialogText_update = 0;
    public static int showAttendanceNumberUpdate = 0;
    public static int showShiftNumberUpdate = 0;

    Boolean reasonExecuted = false;
    Boolean locationExecuted = false;

    TextView errorTimeUpdate;
    TextView errorLocationUpdate;
    public static TextView errorShiftUpdate;
    TextView errorReasonUpdate;
    public static TextView errorApproverUpdate;
    public static TextView errorReasonDescUpdate;

    LinearLayout afterReq;
    TextInputLayout dateUpdateLayUpdate;
    TextInputLayout reasonLayUpdate;
    TextInputLayout addStaLayUpdate;

    TextInputLayout inTimeLay;
    TextInputLayout outTimeLay;

    public static TextInputLayout shiftTestLayUpdate;
    public static TextInputEditText shiftTestEditUpdate;

    public static TextInputLayout approverTestLayUpdate;
    public static TextInputEditText approverTestEditUpdate;

    public static TextInputEditText requestCode;
    TextInputEditText machCode;
    TextInputEditText machType;
    TextInputEditText userName;
    TextInputEditText userID;
    TextInputEditText todayDate;
    TextInputEditText dateUpdated;
    TextInputEditText arrivalTimeUpdated;
    TextInputEditText departTimeUpdated;
    public static TextInputEditText reasonDescUpdate;
    public static TextInputEditText addressStationUpdate;

    Spinner reqType;
    Spinner attenType;
    Spinner locUpdate;
    Spinner reasonType;

    Button existingAttUpdate;
    public static Button showShoftTimeUpdate;

    Button close;
    Button update;

    Button arriClear;
    Button deptClear;

    public static ArrayList<SelectAllList> selectAllListsUpdate;
    public static ArrayList<SelectAllList> allShiftDetailsUpdate;

    public static ArrayList<SelectAllList> allApproverDivisionUpdate;
    public static ArrayList<SelectAllList> allApproverWithoutDivUpdate;
    public static ArrayList<SelectAllList> allApproverEmpUpdate;
    public static ArrayList<SelectAllList> allSelectedApproverUpdate;

    public ArrayList<LocUpdateList> locUpdateListsUpdate;
    public ArrayList<String> onlyLocationListsUpdate;

    public ArrayList<String> reqListUpdate;

    public ArrayList<AttendanceReqType> attendanceReqTypesUpdate;
    public ArrayList<String> attenTypeListUpdate;

    public ArrayList<ReasonList> reasonListsUpdate;
    public ArrayList<String> reasonNameUpdate;

    public ArrayAdapter<String> locUpdateAdapterUpdate;
    public ArrayAdapter<String> reqTypeAdapterUpdate;
    public ArrayAdapter<String> attenTypeAdapterUpdate;
    public ArrayAdapter<String> reasonAdapterUpdate;

    String emp_id = "";
    String emp_name = "";
    String user_id = "";

    public static String darm_id = "";

    WaitProgress waitProgress = new WaitProgress();
    WaitProgress waitProgress1 = new WaitProgress();
    WaitProgress waitProgress2 = new WaitProgress();
    WaitProgress waitProgress3 = new WaitProgress();
    private String message = null;
    private Boolean conn = false;
    private Boolean machineCon = false;
    private Boolean machineConnn = false;
    private Boolean connected = false;
    private Boolean reasonCon = false;
    private Boolean reasonConnnn = false;
    private Boolean insertCon = false;
    private Boolean insertConnn = false;

    private Boolean gathered = false;
    private Boolean gatherConnn = false;

    private Connection connection;
    private int mYear, mMonth, mDay, mHour, mMinute;

    public static ArrayList<SelectReqList> selectReqLists;

    String selected_request = "";
    String selected_attendance_type= "";
    String updatedLocationName = "";
    String selected_loc_id = "";

    String machineCode = "";
    String machineType = "";
    public static String dateToShowUpdate = "";

    public static String selected_shift_name_update = "";
    public static String selected_shift_id_update = "";
    public static String shift_osm_id_update = "";
    String selected_reason_name = "";
    public static String selected_approver_name_update = "";

    public static String hint_update = "";
    public static int number_update = 0;
    public static String text_update = "";

    String desig_priority = "";
    String divm_id = "";
    String approval_band = "";
    int flag = 1;
    int count_approv_emp = 0;


    String selected_update_date = "";
    String selected_reason_desc = "";
    String selected_address_station = "";
    String now_date = "";
    String selected_reason_id = "";
    String app_code = "";
    String app_code_id = "";
    public static String selected_approver_id_update = "";
    String arrival_time = "";
    String depart_time = "";

    String selected_jsm_id = "";
    String selected_dept_id = "";
    String selected_divm_id = "";
    String calling_title = "";


    String from__selected_request = "";
    String form_selected_attendance_type = "";
    String from_selected_aurrm_id = "";
    String from_update_date = "";
    String from_arrival_time = "";
    String from_depart_time = "";
    String from_selected_loc_id = "";
    String from_selected_shift_id = "";
    String from_reason_desc = "";
    String from_add_during_cause = "";
    String from_selected_approver_id = "";
    String from_app_date = "";

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
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
           // w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        View decorView = getWindow().getDecorView();
// Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        setContentView(R.layout.activity_attendance_req_update);


        /*darm_id = "";

        selected_shift_id_update = "";
        selected_approver_id_update = "";

        shiftTestLayUpdate = findViewById(R.id.shift_description_Layout_update);
        shiftTestEditUpdate = findViewById(R.id.shift_description_update);

        approverTestLayUpdate = findViewById(R.id.approver_description_Layout_update);
        approverTestEditUpdate = findViewById(R.id.approver_description_update);

        inTimeLay = findViewById(R.id.select_in_time_from_update);
        outTimeLay = findViewById(R.id.select_out_time_from_update);

        errorTimeUpdate = findViewById(R.id.error_input_time_update);
        errorLocationUpdate = findViewById(R.id.error_input_location_update);
        errorShiftUpdate = findViewById(R.id.error_input_shift_update);
        errorReasonUpdate = findViewById(R.id.error_input_reason_update);
        errorApproverUpdate = findViewById(R.id.error_input_approver_update);
        errorReasonDescUpdate = findViewById(R.id.error_input_reason_desc_update);

        existingAttUpdate = findViewById(R.id.existing_att_time_update);
        showShoftTimeUpdate = findViewById(R.id.show_shift_item_button_update);
        close = findViewById(R.id.update_finish_update);
        update = findViewById(R.id.update_req_button_update);
        arriClear = findViewById(R.id.arrival_clear_update);
        deptClear = findViewById(R.id.departure_clear_update);

        afterReq = findViewById(R.id.after_request_selecting);
        dateUpdateLayUpdate = findViewById(R.id.date_updated_layout_update);
        addStaLayUpdate = findViewById(R.id.address_station_layout_update);
        reasonLayUpdate = findViewById(R.id.reason_description_Layout_update);

        requestCode = findViewById(R.id.request_code);
        machCode = findViewById(R.id.updated_machine_code_update);
        machType = findViewById(R.id.updated_machine_type_update);
        userName = findViewById(R.id.name_att_update_update);
        userID = findViewById(R.id.id_att_update_update);
        todayDate = findViewById(R.id.now_date_att_update_update);
        dateUpdated = findViewById(R.id.date_to_be_updated_update);
        arrivalTimeUpdated = findViewById(R.id.arrival_time_to_be_updated_update);
        departTimeUpdated = findViewById(R.id.departure_time_to_be_updated_update);
        addressStationUpdate = findViewById(R.id.address_outside_sta_update);
        reasonDescUpdate = findViewById(R.id.reason_description_update);

        locUpdate = findViewById(R.id.spinner_loc_updated_update);
        attenType = findViewById(R.id.spinner_attendance_type_update);
        reqType = findViewById(R.id.spinner_req_type_update);
        reasonType = findViewById(R.id.spinner_reason_type_updated_update);

        selectAllListsUpdate = new ArrayList<>();

        allShiftDetailsUpdate = new ArrayList<>();
        allApproverDivisionUpdate = new ArrayList<>();
        allApproverWithoutDivUpdate = new ArrayList<>();
        allApproverEmpUpdate = new ArrayList<>();
        allSelectedApproverUpdate = new ArrayList<>();

        emp_id = userInfoLists.get(0).getEmp_id();

        locUpdateListsUpdate = new ArrayList<>();
        attendanceReqTypesUpdate = new ArrayList<>();
        reasonListsUpdate = new ArrayList<>();

        onlyLocationListsUpdate = new ArrayList<>();
        reqListUpdate = new ArrayList<>();
        attenTypeListUpdate = new ArrayList<>();
        reasonNameUpdate = new ArrayList<>();

        reasonNameUpdate.add("Select");

        onlyLocationListsUpdate.add("Select");

        reqListUpdate.add("Select");
        reqListUpdate.add("PRE");
        reqListUpdate.add("POST");

        attendanceReqTypesUpdate.add(new AttendanceReqType("Early","Early Departure Information"));
        attendanceReqTypesUpdate.add(new AttendanceReqType("Late","Late Arrival Information"));
        attendanceReqTypesUpdate.add(new AttendanceReqType("General","Work Time Update"));

        attenTypeListUpdate.add("Select");

        //onlyShiftName.add("Select");


        for (int i = 0 ; i < attendanceReqTypesUpdate.size(); i++) {
            attenTypeListUpdate.add(attendanceReqTypesUpdate.get(i).getAttendance_req_details());
        }

        selectReqLists = new ArrayList<>();

        emp_name = userInfoLists.get(0).getUser_fname() + " " + userInfoLists.get(0).getUser_lname();

        user_id = userInfoLists.get(0).getUserName();

        userName.setText(emp_name);
        userID.setText(user_id);

        Date c = Calendar.getInstance().getTime();

        String formattedDate = "";

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy", Locale.getDefault());

        formattedDate = df.format(c);

        todayDate.setText(formattedDate);

        new Check().execute();


        // Location Update Spinner
        locUpdateAdapterUpdate = new ArrayAdapter<String>(
                this,R.layout.item_country,onlyLocationListsUpdate){
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
        locUpdateAdapterUpdate.setDropDownViewResource(R.layout.item_country);
        locUpdate.setAdapter(locUpdateAdapterUpdate);


        // Selecting Location From Spinner
        locUpdate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position > 0){
                    updatedLocationName = (String) parent.getItemAtPosition(position);
                    errorLocationUpdate.setVisibility(View.GONE);

                    // Notify the selected item text
                    for (int i = 0; i < locUpdateListsUpdate.size(); i++) {
                        if (updatedLocationName.equals(locUpdateListsUpdate.get(i).getLocation())) {
                            selected_loc_id = locUpdateListsUpdate.get(i).getLocID();

                            System.out.println(selected_loc_id);
                        }
                    }

                    new MachineInfo().execute();

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Request Type Spinner
        reqTypeAdapterUpdate = new ArrayAdapter<String>(
                this,R.layout.item_country,reqListUpdate){
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
        reqTypeAdapterUpdate.setDropDownViewResource(R.layout.item_country);
        reqType.setAdapter(reqTypeAdapterUpdate);

        // Selecting Request Type
        reqType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position > 0){
                    selected_request = (String) parent.getItemAtPosition(position);
                    if (locationExecuted) {

                        dateUpdated.setText(from_update_date);
                        dateUpdateLayUpdate.setHint("Update Date");
                        dateUpdateLayUpdate.setHelperText("");
                        existingAttUpdate.setVisibility(View.VISIBLE);
                        locationExecuted = false;

                    } else {
                        dateUpdated.setText("");
                        dateUpdateLayUpdate.setHint("Select Update Date");
                        dateUpdateLayUpdate.setHelperText("");
                        existingAttUpdate.setVisibility(View.GONE);
                    }


                    // Notify the selected item text
                    System.out.println(selected_request);
                    if (!selected_request.isEmpty() && !selected_attendance_type.isEmpty()) {

                        System.out.println(1);
//                        after.setVisibility(View.VISIBLE);
//                        update.setVisibility(View.VISIBLE);
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Attendance Type Spinner
        attenTypeAdapterUpdate = new ArrayAdapter<String>(
                this,R.layout.item_country,attenTypeListUpdate){
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
        attenTypeAdapterUpdate.setDropDownViewResource(R.layout.item_country);
        attenType.setAdapter(attenTypeAdapterUpdate);


        // Selecting Attendance Type
        attenType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position > 0){
                    selected_attendance_type = (String) parent.getItemAtPosition(position);

                    for (int i = 0; i < attendanceReqTypesUpdate.size(); i++) {
                        if (selected_attendance_type.equals(attendanceReqTypesUpdate.get(i).getAttendance_req_details())) {
                            selected_attendance_type = attendanceReqTypesUpdate.get(i).getAttendance_req();

                            System.out.println(selected_attendance_type);
                        }
                    }


                    new ReasonInfo().execute();
                    // Notify the selected item text
                    if (!selected_request.isEmpty() && !selected_attendance_type.isEmpty()) {

                        System.out.println(2);
//                        after.setVisibility(View.VISIBLE);
//                        update.setVisibility(View.VISIBLE);
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Reason Type Spinner
        reasonAdapterUpdate = new ArrayAdapter<String>(
                this,R.layout.item_country,reasonNameUpdate){
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
        reasonAdapterUpdate.setDropDownViewResource(R.layout.item_country);
        reasonType.setAdapter(reasonAdapterUpdate);

        // Selecting Reason Type
        reasonType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position > 0){
                    selected_reason_name = (String) parent.getItemAtPosition(position);
                    errorReasonUpdate.setVisibility(View.GONE);

                    for (int i = 0; i < reasonListsUpdate.size(); i++) {
                        if (selected_reason_name.equals(reasonListsUpdate.get(i).getReason_name())) {
                            selected_reason_id = reasonListsUpdate.get(i).getReason_id();

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
                        DatePickerDialog datePickerDialog = new DatePickerDialog(AttendanceReqUpdate.this, new DatePickerDialog.OnDateSetListener() {
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

                                    System.out.println(today);
                                    System.out.println(updateDate);

                                    Date nowDate = null;
                                    Date givenDate = null;

                                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy", Locale.getDefault());
                                    try {
                                        nowDate = sdf.parse(today);
                                        givenDate = sdf.parse(updateDate);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    if (nowDate != null && givenDate != null) {

                                        if (!givenDate.after(nowDate) || givenDate.equals(nowDate)) {
                                            //Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_SHORT).show();
//                                            dateUpdateLayUpdate.setHelperText("Requested Date never Less than Application Date");
//                                            dateUpdateLayUpdate.setHelperTextColor(ColorStateList.valueOf(Color.RED));
//                                            dateUpdated.setText("");
//                                            System.out.println("AASS");
//                                            existingAttUpdate.setVisibility(View.GONE);

                                            dateUpdated.setText(dayOfMonthName + "-" + monthName + "-" + yearName);
                                            dateUpdateLayUpdate.setHint("Update Date");
                                            dateUpdateLayUpdate.setHelperText("");
                                            existingAttUpdate.setVisibility(View.VISIBLE);
                                            System.out.println("AHSE let");

                                        } else {
//                                            dateUpdated.setText(dayOfMonthName + "-" + monthName + "-" + yearName);
//                                            dateUpdateLayUpdate.setHelperText("");
//                                            existingAttUpdate.setVisibility(View.VISIBLE);
//                                            System.out.println("AHSE");

                                            dateUpdateLayUpdate.setHelperText("Requested Date never Less than Application Date");
                                            dateUpdateLayUpdate.setHelperTextColor(ColorStateList.valueOf(Color.RED));
                                            dateUpdated.setText("");
                                            dateUpdateLayUpdate.setHint("Select Update Date");
                                            System.out.println("AASS PRE");
                                            existingAttUpdate.setVisibility(View.GONE);
                                        }
                                    }
                                }
                                else if (selected_request.equals("POST")) {

                                    String today = todayDate.getText().toString();
                                    String updateDate = dateUpdated.getText().toString();

                                    Date nowDate = null;
                                    Date givenDate = null;

                                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy", Locale.getDefault());
                                    try {
                                        nowDate = sdf.parse(today);
                                        givenDate = sdf.parse(updateDate);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    if (nowDate != null && givenDate != null) {

                                        if (givenDate.before(nowDate)) {
                                            //Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_SHORT).show();
//                                            dateUpdateLayUpdate.setHelperText("Requested Date should be Less than Application Date");
//                                            dateUpdateLayUpdate.setHelperTextColor(ColorStateList.valueOf(Color.RED));
//                                            dateUpdated.setText("");
//                                            existingAttUpdate.setVisibility(View.GONE);

                                            dateUpdated.setText(dayOfMonthName + "-" + monthName + "-" + yearName);
                                            dateUpdateLayUpdate.setHint("Update Date");
                                            dateUpdateLayUpdate.setHelperText("");
                                            existingAttUpdate.setVisibility(View.VISIBLE);
                                            System.out.println("AHSEkkkk");


                                        } else {
//                                            dateUpdated.setText(dayOfMonthName + "-" + monthName + "-" + yearName);
//                                            dateUpdateLayUpdate.setHelperText("");
//                                            existingAttUpdate.setVisibility(View.VISIBLE);
//                                            System.out.println("AHSEkkkk");

                                            dateUpdateLayUpdate.setHelperText("Requested Date should be Less than Application Date");
                                            dateUpdateLayUpdate.setHint("Select Update Date");
                                            dateUpdateLayUpdate.setHelperTextColor(ColorStateList.valueOf(Color.RED));
                                            dateUpdated.setText("");
                                            existingAttUpdate.setVisibility(View.GONE);
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

                TimePickerDialog timePickerDialog = new TimePickerDialog(AttendanceReqUpdate.this, new TimePickerDialog.OnTimeSetListener() {
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
                            errorTimeUpdate.setVisibility(View.GONE);
                        } else {
                            arrivalTimeUpdated.setText(hourOfDay + ":" + minute + AM_PM);
                            inTimeLay.setHint("In Time");
                            arriClear.setVisibility(View.VISIBLE);
                            errorTimeUpdate.setVisibility(View.GONE);
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

                TimePickerDialog timePickerDialog = new TimePickerDialog(AttendanceReqUpdate.this, new TimePickerDialog.OnTimeSetListener() {
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
                            errorTimeUpdate.setVisibility(View.GONE);
                        } else {
                            departTimeUpdated.setText(hourOfDay + ":" + minute + AM_PM);
                            outTimeLay.setHint("Out Time");
                            deptClear.setVisibility(View.VISIBLE);
                            errorTimeUpdate.setVisibility(View.GONE);
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
        reasonDescUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                number_update = 1;
                hint_update = reasonLayUpdate.getHint().toString();
                text_update = reasonDescUpdate.getText().toString();
                DialogueText dialogueText = new DialogueText();
                dialogueText.show(getSupportFragmentManager(),"TEXTEDIT");
            }
        });

        // Address out of Station
        addressStationUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number_update = 2;
                hint_update = addStaLayUpdate.getHint().toString();
                text_update = addressStationUpdate.getText().toString();
                DialogueText dialogueText = new DialogueText();
                dialogueText.show(getSupportFragmentManager(),"TEXTEDIT");
            }
        });

        shiftTestEditUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogText_update = 1;
                selectAllListsUpdate = allShiftDetailsUpdate;
                SelectAll selectAll = new SelectAll();
                selectAll.show(getSupportFragmentManager(),"TEXTEDIT");
            }
        });

        approverTestEditUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogText_update = 2;
                selectAllListsUpdate = allSelectedApproverUpdate;
                SelectAll selectAll = new SelectAll();
                selectAll.show(getSupportFragmentManager(),"TEXTEDIT");
            }
        });


        existingAttUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAttendanceNumberUpdate = 2;
                dateToShowUpdate = dateUpdated.getText().toString();
                if (!dateToShowUpdate.isEmpty()) {
                    ShowAttendance showAttendance = new ShowAttendance(AttendanceReqUpdate.this);
                    showAttendance.show(getSupportFragmentManager(),"Attendance");
                }

            }
        });

        showShoftTimeUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShiftNumberUpdate = 2;
                shift_osm_id_update = selected_shift_id_update;
                ShowShift showShift = new ShowShift(AttendanceReqUpdate.this);
                showShift.show(getSupportFragmentManager(),"Shift");
            }
        });

        requestCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectRequest selectRequest = new SelectRequest();
                selectRequest.show(getSupportFragmentManager(),"Request");
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

                afterReq.setVisibility(View.VISIBLE);
                update.setVisibility(View.VISIBLE);
                new GatherData().execute();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selected_shift_id_update = "";
                selected_approver_id_update = "";
                darm_id = "";
                finish();


            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                now_date = todayDate.getText().toString();
                selected_update_date = dateUpdated.getText().toString();
                arrival_time = arrivalTimeUpdated.getText().toString();
                depart_time = departTimeUpdated.getText().toString();
                selected_reason_desc = reasonDescUpdate.getText().toString();
                selected_address_station = addressStationUpdate.getText().toString();

//                if (selected_address_station.isEmpty()) {
//                    selected_address_station = null;
//                }

                if (!selected_update_date.isEmpty()) {

                    if (arrival_time.isEmpty() && depart_time.isEmpty()) {
                        Toast.makeText(getApplicationContext(),"Please Check Time to be Updated",Toast.LENGTH_SHORT).show();
                        errorTimeUpdate.setVisibility(View.VISIBLE);
                    } else {
                        errorTimeUpdate.setVisibility(View.GONE);
                        if (!selected_loc_id.isEmpty()) {
                            errorLocationUpdate.setVisibility(View.GONE);

                            if (!selected_shift_id_update.isEmpty()) {
                                System.out.println(selected_shift_id_update);
                                errorShiftUpdate.setVisibility(View.GONE);

                                if (!selected_reason_id.isEmpty()) {
                                    errorReasonUpdate.setVisibility(View.GONE);

                                    if (!selected_reason_desc.isEmpty()) {
                                        errorReasonDescUpdate.setVisibility(View.GONE);

                                        if (!selected_approver_id_update.isEmpty()) {
                                            errorApproverUpdate.setVisibility(View.GONE);

                                            new UpdateCheck().execute();

                                        } else {
                                            Toast.makeText(getApplicationContext(),"Please Check Approver Name",Toast.LENGTH_SHORT).show();
                                            errorApproverUpdate.setVisibility(View.VISIBLE);
                                        }
                                    }else {
                                        Toast.makeText(getApplicationContext(),"Please Check Reason Description",Toast.LENGTH_SHORT).show();
                                        errorReasonDescUpdate.setVisibility(View.VISIBLE);
                                    }

                                } else {
                                    Toast.makeText(getApplicationContext(),"Please Check Reason Type",Toast.LENGTH_SHORT).show();
                                    errorReasonUpdate.setVisibility(View.VISIBLE);
                                }

                            } else {
                                Toast.makeText(getApplicationContext(),"Please Check Shift to be Updated",Toast.LENGTH_SHORT).show();
                                errorShiftUpdate.setVisibility(View.VISIBLE);
                            }

                        } else {
                            Toast.makeText(getApplicationContext(),"Please Check Location to be Updated",Toast.LENGTH_SHORT).show();
                            errorLocationUpdate.setVisibility(View.VISIBLE);
                        }

                    }

                } else {
                    Toast.makeText(getApplicationContext(),"Please Check Date to be Updated",Toast.LENGTH_SHORT).show();
                    dateUpdateLayUpdate.setHelperText("Please Provide 'Date to be Updated'");
                }

            }
        });
*/
    }


    // -----------------------------------------------------------------------------------------------------------------


//    @Override
//    public void onBackPressed() {
//        selected_shift_id_update = "";
//        selected_approver_id_update = "";
//        darm_id = "";
//        finish();
//    }
//
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
//
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
//                if (locUpdateListsUpdate.size() != 0) {
//                    for (int i = 0; i < locUpdateListsUpdate.size(); i++) {
//                        onlyLocationListsUpdate.add(locUpdateListsUpdate.get(i).getLocation());
//                    }
//                }
//                locUpdateAdapterUpdate.notifyDataSetChanged();
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
//            }else {
//                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
//                AlertDialog dialog = new AlertDialog.Builder(AttendanceReqUpdate.this)
//                        .setMessage("Please Check Your Internet Connection")
//                        .setPositiveButton("Retry", null)
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
//            }
//        }
//    }
//
//    public class GatherData extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//            waitProgress3.show(getSupportFragmentManager(),"WaitBar");
//            waitProgress3.setCancelable(false);
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            if (isConnected() && isOnline()) {
//
//                GettingData();
//                if (gathered) {
//                    gatherConnn = true;
//                    message= "Internet Connected";
//                }
//
//            } else {
//                gatherConnn = false;
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
//            waitProgress3.dismiss();
//            if (gatherConnn) {
//
//                int index = 0;
//                for (int i = 0; i < reqListUpdate.size(); i++) {
//                    if (from__selected_request.equals(reqListUpdate.get(i))) {
//                        selected_request = reqListUpdate.get(i);
//                        index = reqListUpdate.indexOf(selected_request);
//                    }
//                }
//                reqType.setSelection(index);
//
//                for (int i = 0; i < attendanceReqTypesUpdate.size(); i++) {
//                    if (form_selected_attendance_type.equals(attendanceReqTypesUpdate.get(i).getAttendance_req())) {
//                        selected_attendance_type = attendanceReqTypesUpdate.get(i).getAttendance_req_details();
//                        for (int j = 0; j < attenTypeListUpdate.size(); j++) {
//                            if (selected_attendance_type.equals(attenTypeListUpdate.get(j))) {
//                                index = attenTypeListUpdate.indexOf(selected_attendance_type);
//                            }
//
//                        }
//                       selected_attendance_type = form_selected_attendance_type;
//                    }
//                }
//
//                attenType.setSelection(index);
//
//                //new ReasonInfo().execute();
//                if (from_update_date != null) {
//                    dateUpdated.setText(from_update_date);
//                    dateUpdateLayUpdate.setHint("Update Date");
//                }
//                if (from_arrival_time != null) {
//                    arrivalTimeUpdated.setText(from_arrival_time);
//                    inTimeLay.setHint("In Time");
//                    arriClear.setVisibility(View.VISIBLE);
//                }
//                if (from_depart_time != null) {
//                    departTimeUpdated.setText(from_depart_time);
//                    outTimeLay.setHint("Out Time");
//                    deptClear.setVisibility(View.VISIBLE);
//                }
//
//                for (int i = 0; i < locUpdateListsUpdate.size(); i++) {
//                    if (from_selected_loc_id.equals(locUpdateListsUpdate.get(i).getLocID())) {
//                        String location = locUpdateListsUpdate.get(i).getLocation();
//                        for (int j = 0; j < onlyLocationListsUpdate.size(); j++) {
//                            if (location.equals(onlyLocationListsUpdate.get(j))) {
//                                index = onlyLocationListsUpdate.indexOf(location);
//                            }
//                        }
//                    }
//                }
//
//                locUpdate.setSelection(index);
//
//                selected_loc_id = from_selected_loc_id;
//                //new MachineInfo().execute();
//
//                String shiftName = "";
//                for (int i = 0; i < allShiftDetailsUpdate.size(); i++) {
//                    if (from_selected_shift_id.equals(allShiftDetailsUpdate.get(i).getId())) {
//                        shiftName = allShiftDetailsUpdate.get(i).getFirst();
//                    }
//                }
//                shiftTestEditUpdate.setText(shiftName);
//                shiftTestEditUpdate.setTextColor(Color.BLACK);
//                selected_shift_id_update = from_selected_shift_id;
//
//                if (from_reason_desc != null) {
//                    reasonDescUpdate.setText(from_reason_desc);
//                }
//
//                if (from_add_during_cause != null) {
//                    addressStationUpdate.setText(from_add_during_cause);
//                }
//
//                String approver = "";
//                for (int i = 0; i < allSelectedApproverUpdate.size(); i++) {
//                    if (from_selected_approver_id.equals(allSelectedApproverUpdate.get(i).getId())) {
//                        approver = allSelectedApproverUpdate.get(i).getFirst();
//                    }
//                }
//                approverTestEditUpdate.setText(approver);
//                approverTestEditUpdate.setTextColor(Color.BLACK);
//                selected_approver_id_update = from_selected_approver_id;
//                todayDate.setText(from_app_date);
//
//                //new GettingDataSecond().execute();
//
//
//
//            }else {
//                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
//                AlertDialog dialog = new AlertDialog.Builder(AttendanceReqUpdate.this)
//                        .setMessage("Please Check Your Internet Connection")
//                        .setPositiveButton("Retry", null)
//                        .show();
//
//                dialog.setCancelable(false);
//                dialog.setCanceledOnTouchOutside(false);
//                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
//                positive.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        new GatherData().execute();
//                        dialog.dismiss();
//                    }
//                });
//            }
//        }
//    }

//    public class GettingDataSecond extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            if (isConnected() && isOnline()) {
//
//                ReasonData(selected_attendance_type);
//                MachineData(selected_loc_id);
//                if (reasonCon && machineCon) {
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
//            if (reasonConnnn) {
//
//                reasonNameUpdate = new ArrayList<>();
//                reasonNameUpdate.add("Select");
//
//
//                if (reasonListsUpdate.size() != 0) {
//                    for (int i = 0; i < reasonListsUpdate.size(); i++) {
//                        reasonNameUpdate.add(reasonListsUpdate.get(i).getReason_name());
//                        reasonAdapterUpdate.setNotifyOnChange(true);
//                        System.out.println(reasonListsUpdate.get(i).getReason_name());
//                    }
//                }
//
//                reasonAdapterUpdate.clear();
//                reasonAdapterUpdate.addAll(reasonNameUpdate);
//                reasonAdapterUpdate.notifyDataSetChanged();
//                reasonType.setSelection(0);
//
//
//                    int index = 0;
//                    for (int i = 0; i < reasonListsUpdate.size(); i++) {
//                        if (from_selected_aurrm_id.equals(reasonListsUpdate.get(i).getReason_id())) {
//                            String reason = reasonListsUpdate.get(i).getReason_name();
//                            for (int j = 0; j < reasonNameUpdate.size(); j++) {
//                                if (reason.equals(reasonNameUpdate.get(j))) {
//                                    index = reasonNameUpdate.indexOf(reason);
//                                }
//                            }
//                        }
//                    }
//                    reasonType.setSelection(index);
//                    selected_reason_id = from_selected_aurrm_id;
//                    if (!machineCode.isEmpty() && !machineType.isEmpty()) {
//                        machCode.setText(machineCode);
//                        machType.setText(machineType);
//                    }
//
//
//            }else {
//                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
//                AlertDialog dialog = new AlertDialog.Builder(AttendanceReqUpdate.this)
//                        .setMessage("Please Check Your Internet Connection")
//                        .setPositiveButton("Retry", null)
//                        .show();
//
//                dialog.setCancelable(false);
//                dialog.setCanceledOnTouchOutside(false);
//                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
//                positive.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        new GettingDataSecond().execute();
//                        dialog.dismiss();
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
//            waitProgress1.show(getSupportFragmentManager(),"WaitBar");
//            waitProgress1.setCancelable(false);
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
//            waitProgress1.dismiss();
//            if (machineConnn) {
//
//                if (!machineCode.isEmpty() && !machineType.isEmpty()) {
//                    machCode.setText(machineCode);
//                    machType.setText(machineType);
//                }
//
//            }else {
//                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
//                AlertDialog dialog = new AlertDialog.Builder(AttendanceReqUpdate.this)
//                        .setMessage("Please Check Your Internet Connection")
//                        .setPositiveButton("Retry", null)
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
//            }
//        }
//    }
//
//    public class ReasonInfo extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//            waitProgress2.show(getSupportFragmentManager(),"WaitBar");
//            waitProgress2.setCancelable(false);
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
//            waitProgress2.dismiss();
//            if (reasonConnnn) {
//
//                reasonNameUpdate = new ArrayList<>();
//                reasonNameUpdate.add("Select");
//
//
//                if (reasonListsUpdate.size() != 0) {
//                    for (int i = 0; i < reasonListsUpdate.size(); i++) {
//                        reasonNameUpdate.add(reasonListsUpdate.get(i).getReason_name());
//                        reasonAdapterUpdate.setNotifyOnChange(true);
//                        System.out.println(reasonListsUpdate.get(i).getReason_name());
//                    }
//                }
//
//                reasonAdapterUpdate.clear();
//                reasonAdapterUpdate.addAll(reasonNameUpdate);
//                reasonAdapterUpdate.notifyDataSetChanged();
//                reasonType.setSelection(0);
//
//                if (reasonExecuted) {
//                    int index = 0;
//                    for (int i = 0; i < reasonListsUpdate.size(); i++) {
//                        if (from_selected_aurrm_id.equals(reasonListsUpdate.get(i).getReason_id())) {
//                            String reason = reasonListsUpdate.get(i).getReason_name();
//                            for (int j = 0; j < reasonNameUpdate.size(); j++) {
//                                if (reason.equals(reasonNameUpdate.get(j))) {
//                                    index = reasonNameUpdate.indexOf(reason);
//                                }
//                            }
//                        }
//                    }
//                    reasonType.setSelection(index);
//                    selected_reason_id = from_selected_aurrm_id;
//                }
//                reasonExecuted = false;
//
//            }else {
//                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
//                AlertDialog dialog = new AlertDialog.Builder(AttendanceReqUpdate.this)
//                        .setMessage("Please Check Your Internet Connection")
//                        .setPositiveButton("Retry", null)
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
//            }
//        }
//    }
//
//    public class UpdateCheck extends AsyncTask<Void, Void, Void> {
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
//                selected_shift_id_update = "";
//                selected_approver_id_update = "";
//                darm_id = "";
//                System.out.println("INSERTED");
//
//                AlertDialog dialog = new AlertDialog.Builder(AttendanceReqUpdate.this)
//                        .setMessage("Request Updated Successfully")
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
//            }else {
//                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
//                AlertDialog dialog = new AlertDialog.Builder(AttendanceReqUpdate.this)
//                        .setMessage("Please Check Your Internet Connection")
//                        .setPositiveButton("Retry", null)
//                        .show();
//
//                dialog.setCancelable(false);
//                dialog.setCanceledOnTouchOutside(false);
//                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
//                positive.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        new UpdateCheck().execute();
//                        dialog.dismiss();
//                    }
//                });
//            }
//        }
//    }
//
//    public void UpdateInfo() {
//        try {
//            this.connection = createConnection();
//
//            locUpdateListsUpdate = new ArrayList<>();
//            allShiftDetailsUpdate = new ArrayList<>();
//
//            allApproverDivisionUpdate = new ArrayList<>();
//            allApproverWithoutDivUpdate = new ArrayList<>();
//            allApproverEmpUpdate = new ArrayList<>();
//            allSelectedApproverUpdate = new ArrayList<>();
//
//            selectReqLists = new ArrayList<>();
//
//
//
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
//            ResultSet newResult = stmt.executeQuery("SELECT daily_atten_req_mst.darm_id,\n" +
//                    " DAILY_ATTEN_REQ_MST.DARM_APP_CODE, \n" +
//                    "  TO_CHAR(DAILY_ATTEN_REQ_MST.DARM_UPDATE_DATE,'DD-MON-YY') DARM_UPDATE_DATE,  \n" +
//                    "    TO_CHAR(DAILY_ATTEN_REQ_MST.DARM_REQ_ARRIVAL_TIME,'HH:MI:SS AM') ARRIVAL_TIME,\n" +
//                    "    TO_CHAR(DAILY_ATTEN_REQ_MST.DARM_REQ_DEPART_TIME,'HH:MI:SS AM') DEPARTURE_TIME,\n" +
//                    "    TO_CHAR(DAILY_ATTEN_REQ_MST.DARM_DATE,'DD-MON-YY') DARM_DATE\n" +
//                    "    FROM DAILY_ATTEN_REQ_MST, EMP_MST, DIVISION_MST\n" +
//                    "WHERE NVL(DAILY_ATTEN_REQ_MST.DARM_APPROVED, 0) = 0\n" +
//                    " AND (EMP_MST.EMP_ID = DAILY_ATTEN_REQ_MST.DARM_EMP_ID)\n" +
//                    " AND (DIVISION_MST.DIVM_ID = DAILY_ATTEN_REQ_MST.DARM_DIVM_ID)\n" +
//                    " AND (DAILY_ATTEN_REQ_MST.DARM_ENTRY_USER = '"+user_id+"')\n" +
//                    " Order by 1 desc");
//
//            while (newResult.next()) {
//                selectReqLists.add(new SelectReqList(newResult.getString(1),newResult.getString(2),newResult.getString(3),newResult.getString(4),newResult.getString(5),newResult.getString(6)));
//            }
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
//                locUpdateListsUpdate.add(new LocUpdateList(rs.getString(1),rs.getString(2)));
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
//                allShiftDetailsUpdate.add(new SelectAllList(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4),resultSet.getString(5)));
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
//                allApproverDivisionUpdate.add(new SelectAllList(rs1.getString(1),rs1.getString(2),rs1.getString(3),rs1.getString(4),rs1.getString(5)));
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
//                allApproverWithoutDivUpdate.add(new SelectAllList(rs2.getString(1),rs2.getString(2),rs2.getString(3),rs2.getString(4),rs2.getString(5)));
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
//                allApproverEmpUpdate.add(new SelectAllList(rs3.getString(1),rs3.getString(2),rs3.getString(3),rs3.getString(4),rs3.getString(5)));
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
//                            allSelectedApproverUpdate = allApproverWithoutDivUpdate;
//                        } else {
//                            // selectedApproverList = approverEmployee;
//                            allSelectedApproverUpdate = allApproverEmpUpdate;
//                        }
//                    } else {
//                        //selectedApproverList = approverDivision;
//                        allSelectedApproverUpdate = allApproverDivisionUpdate;
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
//
//    public void GettingData() {
//        try {
//            this.connection = createConnection();
//
//            Statement stmt = connection.createStatement();
//
//
//
//
//            ResultSet rs=stmt.executeQuery(" Select DARM_APPLICATION_TYPE,DARM_REQ_TYPE,DARM_AURRM_ID," +
//                    "TO_CHAR(DAILY_ATTEN_REQ_MST.DARM_UPDATE_DATE,'DD-MON-YY') DARM_UPDATE_DATE," +
//                    "TO_CHAR(DAILY_ATTEN_REQ_MST.DARM_REQ_ARRIVAL_TIME,'HH:MIAM') ARRIVAL_TIME,\n" +
//                    "TO_CHAR(DAILY_ATTEN_REQ_MST.DARM_REQ_DEPART_TIME,'HH:MIAM') DEPARTURE_TIME," +
//                    "DARM_REQ_LOCATION_ID,DARM_REQ_OSM_ID," +
//                    "DARM_REASON,DARM_ADD_DURING_CAUSE,DARM_APPLIED_TO_ID," +
//                    "TO_CHAR(DAILY_ATTEN_REQ_MST.DARM_DATE,'DD-MON-YYYY') DARM_DATE " +
//                    "From DAILY_ATTEN_REQ_MST " +
//                    "WHERE DARM_ID = "+darm_id+"");
//
//
//
//            while(rs.next())  {
//                from__selected_request = rs.getString(1);
//                form_selected_attendance_type = rs.getString(2);
//                from_selected_aurrm_id = rs.getString(3);
//                from_update_date = rs.getString(4);
//                from_arrival_time = rs.getString(5);
//                from_depart_time = rs.getString(6);
//                from_selected_loc_id = rs.getString(7);
//                from_selected_shift_id = rs.getString(8);
//                from_reason_desc = rs.getString(9);
//                from_add_during_cause = rs.getString(10);
//                from_selected_approver_id = rs.getString(11);
//                from_app_date = rs.getString(12);
//
//            }
//
//            System.out.println(from__selected_request);
//            System.out.println(form_selected_attendance_type);
//            System.out.println(from_selected_aurrm_id);
//            System.out.println(from_update_date);
//            System.out.println(from_arrival_time);
//            System.out.println(from_depart_time);
//            System.out.println(from_selected_loc_id);
//            System.out.println(from_selected_shift_id);
//            System.out.println(from_reason_desc);
//            System.out.println(from_add_during_cause);
//            System.out.println(from_selected_approver_id);
//
//
//
//
//            locationExecuted = true;
//
//            reasonExecuted = true;
//
//            gathered = true;
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
//
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
//
//    public void ReasonData(String reason) {
//        try {
//            this.connection = createConnection();
//
//            Statement stmt = connection.createStatement();
//
//            reasonListsUpdate = new ArrayList<>();
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
//                reasonListsUpdate.add(new ReasonList(rs.getString(1),rs.getString(2)));
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
//
//
//    public void InsertReq() {
//        try {
//            this.connection = createConnection();
//
//            Statement stmt = connection.createStatement();
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
//                            System.out.println("Approver ID: "+ selected_approver_id_update);
//                            System.out.println("Attendance Type: " + selected_attendance_type);
//                            System.out.println("Depart Time: "+ depart_time);
//                            System.out.println("Arrival Time: " + arrival_time);
//                            System.out.println("Loc ID: "+ selected_loc_id);
//                            System.out.println("Shift: "+ selected_shift_id_update);
//                            System.out.println("Reason ID: "+ selected_reason_id);
//
//                            if (arrival_time.isEmpty() && !depart_time.isEmpty()) {
//                                System.out.println("INSERTED TEST1");
//                                stmt.executeUpdate("UPDATE DAILY_ATTEN_REQ_MST\n" +
//                                        "   SET DARM_EMP_ID = "+emp_id+",\n" +
//                                        "       DARM_REASON = '"+selected_reason_desc+"',\n" +
//                                        "       DARM_ADD_DURING_CAUSE = '"+selected_address_station+"',\n" +
//                                        "       DARM_UPDATE_DATE = TO_DATE('"+selected_update_date+"', 'DD-MON-YY'),\n" +
//                                        "       DARM_DEPT_ID = "+selected_dept_id+",\n" +
//                                        "       DARM_DIVM_ID = "+selected_divm_id+",\n" +
//                                        "       DARM_APPROVED = 0,\n" +
//                                        "       DARM_JSM_ID = "+selected_jsm_id+",\n" +
//                                        "       DARM_CALLING_TITLE = '"+calling_title+"',\n" +
//                                        "       DARM_ENTRY_USER = '"+user_id+"',\n" +
//                                        "       DARM_APPLICATION_TYPE = '"+selected_request+"',\n" +
//                                        "       DARM_APPLIED_TO_ID = "+selected_approver_id_update+",\n" +
//                                        "       DARM_REQ_TYPE = '"+selected_attendance_type+"',\n" +
//                                        "       DARM_REQ_ARRIVAL_TIME = NULL,\n" +
//                                        "       DARM_REQ_DEPART_TIME = TO_DATE('"+selected_update_date+" "+depart_time+"', 'DD-MON-YY HH12:MIPM'),\n" +
//                                        "       DARM_REQ_LOCATION_ID = "+selected_loc_id+",\n" +
//                                        "       DARM_REQ_OSM_ID = "+selected_shift_id_update+",\n" +
//                                        "       DARM_AURRM_ID = "+selected_reason_id+",\n" +
//                                        "       DARM_UPDATE_APPLICATION_FLAG = 1,\n" +
//                                        "       DARM_MODIFIED_BY = '"+user_id+"'\n" +
//                                        " WHERE DARM_ID = "+darm_id+"");
//
//                            }
//                            else if (depart_time.isEmpty() && !arrival_time.isEmpty()) {
//                                System.out.println("INSERTED TEST2");
//
//                                stmt.executeUpdate("UPDATE DAILY_ATTEN_REQ_MST\n" +
//                                        "   SET DARM_EMP_ID = "+emp_id+",\n" +
//                                        "       DARM_REASON = '"+selected_reason_desc+"',\n" +
//                                        "       DARM_ADD_DURING_CAUSE = '"+selected_address_station+"',\n" +
//                                        "       DARM_UPDATE_DATE = TO_DATE('"+selected_update_date+"', 'DD-MON-YY'),\n" +
//                                        "       DARM_DEPT_ID = "+selected_dept_id+",\n" +
//                                        "       DARM_DIVM_ID = "+selected_divm_id+",\n" +
//                                        "       DARM_APPROVED = 0,\n" +
//                                        "       DARM_JSM_ID = "+selected_jsm_id+",\n" +
//                                        "       DARM_CALLING_TITLE = '"+calling_title+"',\n" +
//                                        "       DARM_ENTRY_USER = '"+user_id+"',\n" +
//                                        "       DARM_APPLICATION_TYPE = '"+selected_request+"',\n" +
//                                        "       DARM_APPLIED_TO_ID = "+selected_approver_id_update+",\n" +
//                                        "       DARM_REQ_TYPE = '"+selected_attendance_type+"',\n" +
//                                        "       DARM_REQ_ARRIVAL_TIME = TO_DATE('"+selected_update_date+" "+arrival_time+"', 'DD-MON-YY HH12:MIPM'),\n" +
//                                        "       DARM_REQ_DEPART_TIME = NULL,\n" +
//                                        "       DARM_REQ_LOCATION_ID = "+selected_loc_id+",\n" +
//                                        "       DARM_REQ_OSM_ID = "+selected_shift_id_update+",\n" +
//                                        "       DARM_AURRM_ID = "+selected_reason_id+",\n" +
//                                        "       DARM_UPDATE_APPLICATION_FLAG = 1,\n" +
//                                        "       DARM_MODIFIED_BY = '"+user_id+"'\n" +
//                                        " WHERE DARM_ID = "+darm_id+"");
//
//
//                            }
//                            else {
//                                System.out.println("INSERTED TEST3");
//
//                                stmt.executeUpdate("UPDATE DAILY_ATTEN_REQ_MST\n" +
//                                        "   SET DARM_EMP_ID = "+emp_id+",\n" +
//                                        "       DARM_REASON = '"+selected_reason_desc+"',\n" +
//                                        "       DARM_ADD_DURING_CAUSE = '"+selected_address_station+"',\n" +
//                                        "       DARM_UPDATE_DATE = TO_DATE('"+selected_update_date+"', 'DD-MON-YY'),\n" +
//                                        "       DARM_DEPT_ID = "+selected_dept_id+",\n" +
//                                        "       DARM_DIVM_ID = "+selected_divm_id+",\n" +
//                                        "       DARM_APPROVED = 0,\n" +
//                                        "       DARM_JSM_ID = "+selected_jsm_id+",\n" +
//                                        "       DARM_CALLING_TITLE = '"+calling_title+"',\n" +
//                                        "       DARM_ENTRY_USER = '"+user_id+"',\n" +
//                                        "       DARM_APPLICATION_TYPE = '"+selected_request+"',\n" +
//                                        "       DARM_APPLIED_TO_ID = "+selected_approver_id_update+",\n" +
//                                        "       DARM_REQ_TYPE = '"+selected_attendance_type+"',\n" +
//                                        "       DARM_REQ_ARRIVAL_TIME = TO_DATE('"+selected_update_date+" "+arrival_time+"', 'DD-MON-YY HH12:MIPM'),\n" +
//                                        "       DARM_REQ_DEPART_TIME = TO_DATE('"+selected_update_date+" "+depart_time+"', 'DD-MON-YY HH12:MIPM'),\n" +
//                                        "       DARM_REQ_LOCATION_ID = "+selected_loc_id+",\n" +
//                                        "       DARM_REQ_OSM_ID = "+selected_shift_id_update+",\n" +
//                                        "       DARM_AURRM_ID = "+selected_reason_id+",\n" +
//                                        "       DARM_UPDATE_APPLICATION_FLAG = 1,\n" +
//                                        "       DARM_MODIFIED_BY = '"+user_id+"'\n" +
//                                        " WHERE DARM_ID = "+darm_id+"");
//
//                            }
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


}