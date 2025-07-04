package ttit.com.shuvo.ikglhrm;

import static ttit.com.shuvo.ikglhrm.utilities.Constants.CENTER_API_FRONT;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.COMPANY;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.CONTACT;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.DEPT_NAME;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.DESG_NAME;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.DESG_PRIORITY;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.DIV_ID;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.DIV_NAME;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.EMAIL;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.EMP_ID_LOGIN;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.EMP_PASSWORD;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.IS_ATT_APPROVED;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.IS_LEAVE_APPROVED;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.JOINING_DATE;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.JSD_ID_LOGIN;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.JSD_OBJECTIVE;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.JSM_CODE;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.JSM_NAME;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.LIVE_FLAG;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.LOGIN_ACTIVITY_FILE;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.LOGIN_TF;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.MyPREFERENCES;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.SOFTWARE;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.USER_F_NAME;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.USER_L_NAME;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.USER_NAME;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.WIDGET_EMP_ID;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.WIDGET_FILE;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.WIDGET_TRACKER_FLAG;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.checked;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.user_emp_code;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.user_password;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import ttit.com.shuvo.ikglhrm.dashboard.Dashboard;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {

    TextInputEditText user;
    TextInputEditText pass;

    TextView login_failed;
    TextView softName;

    Button login;

    CheckBox checkBox;

//    BLOB pic;

    String userName = "";
    String password = "";
    public static String CompanyName = "";
    public static String SoftwareName = "";
    public static int isApproved = 0;
    public static int isLeaveApproved = 0;

    WaitProgress waitProgress = new WaitProgress();
//    private String message = null;
    private Boolean conn = false;
    private Boolean infoConnected = false;
    private Boolean adminConnected = false;
    private Boolean noUser = false;
    private Boolean connected = false;

//    private Connection connection;
//    AmazingSpinner database;

//    public static final String DATABASE_NAME = "DATABASE_NAME";

    SharedPreferences sharedpreferences;

    SharedPreferences sharedLogin;

    SharedPreferences attendanceWidgetPreferences;

    String getUserName = "";
    String getPassword = "";
    boolean getChecked = false;

    String userId = "";
    public static ArrayList<UserInfoList> userInfoLists;
    public static ArrayList<UserDesignation> userDesignations;

    String emp_id = "";
    String emp_code = "";
    int live_loc_flag = 0;
    String tracker_flag = "";
    String center_api = "";

    ArrayList<AllUrlList> urls;
    String text_url = "https://raw.githubusercontent.com/shuvo934/Story/refs/heads/master/hrmServers";

    Logger logger = Logger.getLogger(Login.class.getName());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userInfoLists = new ArrayList<>();
        userDesignations = new ArrayList<>();

        softName = findViewById(R.id.name_of_soft_login);
        user = findViewById(R.id.user_name_given);
        pass = findViewById(R.id.password_given);
        checkBox = findViewById(R.id.remember_checkbox);

        login_failed = findViewById(R.id.email_pass_miss);

        login = findViewById(R.id.login_button);
//        database = findViewById(R.id.database_spinner);

        sharedLogin = getSharedPreferences(LOGIN_ACTIVITY_FILE,MODE_PRIVATE);
        attendanceWidgetPreferences = getSharedPreferences(WIDGET_FILE,MODE_PRIVATE);

        sharedpreferences = getSharedPreferences(MyPREFERENCES,MODE_PRIVATE);
        getUserName = sharedpreferences.getString(user_emp_code,null);
        getPassword = sharedpreferences.getString(user_password,null);
        getChecked = sharedpreferences.getBoolean(checked,false);

        if (getUserName != null) {
            user.setText(getUserName);
        }
        if (getPassword != null) {
            pass.setText(getPassword);
        }
        checkBox.setChecked(getChecked);

//        database.setText(DEFAULT_USERNAME);
//        ArrayList<String> type = new ArrayList<>();
//        type.add("IKGL");
//        type.add("TTRAMS");
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);

//        database.setAdapter(arrayAdapter);

//        database.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                DEFAULT_USERNAME = parent.getItemAtPosition(position).toString();
////                new Check().execute();
//                getData();
//            }
//        });

        pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                login_failed.setVisibility(View.GONE);
            }
        });

        user.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                login_failed.setVisibility(View.GONE);
            }
        });

        user.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                    event.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT) {
                if (event == null || !event.isShiftPressed()) {
                    // the user is done typing.
                    Log.i("Let see", "Come here");
                    user.clearFocus();
                    closeKeyBoard();

                    return false; // consume.
                }
            }
            return false;
        });

        pass.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                    event.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT) {
                if (event == null || !event.isShiftPressed()) {
                    // the user is done typing.
                    Log.i("Let see", "Come here");
                    pass.clearFocus();
                    closeKeyBoard();

                    return false; // consume.
                }
            }
            return false;
        });

        login.setOnClickListener(v -> {
            closeKeyBoard();

            login_failed.setVisibility(View.GONE);
            userName = Objects.requireNonNull(user.getText()).toString();
            password = Objects.requireNonNull(pass.getText()).toString();

            if (!userName.isEmpty() && !password.isEmpty()) {
                if (!userName.equals("admin")) {
                    dynamicLoginCheck();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Admin can not login to this app", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(getApplicationContext(), "Please Give User Name and Password", Toast.LENGTH_SHORT).show();
            }
        });

        readApiText();

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(Login.this);
                builder.setTitle("EXIT!")
                        .setIcon(R.drawable.hrm_new_round_icon_custom)
                        .setMessage("Do you want to EXIT?")
                        .setPositiveButton("YES", (dialog, which) -> System.exit(0))
                        .setNegativeButton("NO", (dialog, which) -> {
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    public void readApiText() {
        waitProgress.show(getSupportFragmentManager(), "WaitBar");
        waitProgress.setCancelable(false);
        new Thread(() -> {
            urls = new ArrayList<>();
            try {
                URL url = new URL(text_url);
                HttpURLConnection conn=(HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(60000); // timing out in a minute
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String str;
                while ((str = in.readLine()) != null) {
                    urls.add(new AllUrlList(str,false));
                }
                in.close();
            }
            catch (Exception e) {
                urls.add(new AllUrlList("http://103.56.208.123:8001/apex/ttrams/",false));
                urls.add(new AllUrlList("http://103.56.208.123:8001/apex/mnm/",false));
                Log.d("MyTag",e.toString());
            }

            runOnUiThread(() -> {
                if (urls.isEmpty()) {
                    urls.add(new AllUrlList("http://103.56.208.123:8001/apex/ttrams/",false));
                    urls.add(new AllUrlList("http://103.56.208.123:8001/apex/mnm/",false));
                }
                else {
                    for (int i = 0; i < urls.size(); i++) {
                        System.out.println(urls.get(i).getUrls());
                    }
                }
                waitProgress.dismiss();
            });

        }).start();
    }

    private void closeKeyBoard () {
        View view = this.getCurrentFocus();
        if (view != null) {
            view.clearFocus();
            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public boolean onTouchEvent (MotionEvent event){
        closeKeyBoard();
        return super.onTouchEvent(event);
    }

//    public boolean isConnected () {
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
//    public boolean isOnline () {
//
//        Runtime runtime = Runtime.getRuntime();
//        try {
//            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
//            int exitValue = ipProcess.waitFor();
//            return (exitValue == 0);
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        return false;
//    }

//    public class CheckLogin extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//            waitProgress.show(getSupportFragmentManager(), "WaitBar");
//            waitProgress.setCancelable(false);
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            if (isConnected() && isOnline()) {
//
//                LoginQuery();
//                if (connected) {
//                    conn = true;
//                    message = "Internet Connected";
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
//                if (!userId.equals("-1")) {
//                    if (adminConnected) {
//                        Toast.makeText(getApplicationContext(),"Admin can not access this app.",Toast.LENGTH_SHORT).show();
//                    }
//                    else {
//                        if (infoConnected) {
//
//                            if (checkBox.isChecked()) {
//                                System.out.println("Remembered");
//                                SharedPreferences.Editor editor = sharedpreferences.edit();
//                                editor.remove(user_emp_code);
//                                editor.remove(user_password);
//                                editor.remove(checked);
//                                editor.putString(user_emp_code,userName);
//                                editor.putString(user_password,password);
//                                editor.putBoolean(checked,true);
//                                editor.apply();
//                                editor.commit();
//                            } else {
//                                System.out.println("Not Remembered");
//                            }
//
////                        user.setText("");
////                        pass.setText("");
////                        checkBox.setChecked(false);
//
//
//
//                            SharedPreferences.Editor editor1 = sharedLogin.edit();
//                            editor1.remove(USER_NAME);
//                            editor1.remove(USER_F_NAME);
//                            editor1.remove(USER_L_NAME);
//                            editor1.remove(EMAIL);
//                            editor1.remove(CONTACT);
//                            editor1.remove(EMP_ID_LOGIN);
//
//                            editor1.remove(JSM_CODE);
//                            editor1.remove(JSM_NAME);
//                            editor1.remove(JSD_ID_LOGIN);
//                            editor1.remove(JSD_OBJECTIVE);
//                            editor1.remove(DEPT_NAME);
//                            editor1.remove(DIV_NAME);
//                            editor1.remove(DESG_NAME);
//                            editor1.remove(DESG_PRIORITY);
//                            editor1.remove(JOINING_DATE);
//                            editor1.remove(DIV_ID);
//                            editor1.remove(LOGIN_TF);
//
//                            editor1.remove(IS_ATT_APPROVED);
//                            editor1.remove(IS_LEAVE_APPROVED);
//                            editor1.remove(COMPANY);
//                            editor1.remove(SOFTWARE);
//                            editor1.remove(LIVE_FLAG);
//                            editor1.remove(DATABASE_NAME);
//
//                            editor1.putString(USER_NAME, userInfoLists.get(0).getUserName());
//                            editor1.putString(USER_F_NAME, userInfoLists.get(0).getUser_fname());
//                            editor1.putString(USER_L_NAME, userInfoLists.get(0).getUser_lname());
//                            editor1.putString(EMAIL, userInfoLists.get(0).getEmail());
//                            editor1.putString(CONTACT, userInfoLists.get(0).getContact());
//                            editor1.putString(EMP_ID_LOGIN, userInfoLists.get(0).getEmp_id());
//
//                            if (userDesignations.size() != 0) {
//                                editor1.putString(JSM_CODE, userDesignations.get(0).getJsm_code());
//                                editor1.putString(JSM_NAME, userDesignations.get(0).getJsm_name());
//                                editor1.putString(JSD_ID_LOGIN, userDesignations.get(0).getJsd_id());
//                                editor1.putString(JSD_OBJECTIVE, userDesignations.get(0).getJsd_objective());
//                                editor1.putString(DEPT_NAME, userDesignations.get(0).getDept_name());
//                                editor1.putString(DIV_NAME, userDesignations.get(0).getDiv_name());
//                                editor1.putString(DESG_NAME, userDesignations.get(0).getDesg_name());
//                                editor1.putString(DESG_PRIORITY, userDesignations.get(0).getDesg_priority());
//                                editor1.putString(JOINING_DATE, userDesignations.get(0).getJoining_date());
//                                editor1.putString(DIV_ID, userDesignations.get(0).getDiv_id());
//                            } else {
//                                editor1.putString(JSM_CODE, null);
//                                editor1.putString(JSM_NAME, null);
//                                editor1.putString(JSD_ID_LOGIN, null);
//                                editor1.putString(JSD_OBJECTIVE, null);
//                                editor1.putString(DEPT_NAME, null);
//                                editor1.putString(DIV_NAME, null);
//                                editor1.putString(DESG_NAME, null);
//                                editor1.putString(DESG_PRIORITY, null);
//                                editor1.putString(JOINING_DATE, null);
//                                editor1.putString(DIV_ID, null);
//                            }
//
////                        editor1.putString(JSM_CODE, userDesignations.get(0).getJsm_code());
////                        editor1.putString(JSM_NAME, userDesignations.get(0).getJsm_name());
////                        editor1.putString(JSD_ID_LOGIN, userDesignations.get(0).getJsd_id());
////                        editor1.putString(JSD_OBJECTIVE, userDesignations.get(0).getJsd_objective());
////                        editor1.putString(DEPT_NAME, userDesignations.get(0).getDept_name());
////                        editor1.putString(DIV_NAME, userDesignations.get(0).getDiv_name());
////                        editor1.putString(DESG_NAME, userDesignations.get(0).getDesg_name());
////                        editor1.putString(DESG_PRIORITY, userDesignations.get(0).getDesg_priority());
////                        editor1.putString(JOINING_DATE, userDesignations.get(0).getJoining_date());
////                        editor1.putString(DIV_ID, userDesignations.get(0).getDiv_id());
//                            editor1.putBoolean(LOGIN_TF,true);
//
//                            editor1.putInt(IS_ATT_APPROVED, isApproved);
//                            editor1.putInt(IS_LEAVE_APPROVED, isLeaveApproved);
//                            editor1.putString(COMPANY, CompanyName);
//                            editor1.putString(SOFTWARE,SoftwareName);
//                            editor1.putInt(LIVE_FLAG,live_loc_flag);
//                            editor1.putString(DATABASE_NAME,DEFAULT_USERNAME);
//                            editor1.apply();
//                            editor1.commit();
//
//
//                            Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(getApplicationContext(), Dashboard.class);
//                            startActivity(intent);
//                            finish();
//
//                        } else {
//                            new CheckLogin().execute();
//                        }
//                    }
//
//                } else {
//
//                    login_failed.setVisibility(View.VISIBLE);
//                }
//                conn = false;
//                connected = false;
//                adminConnected = false;
//                infoConnected = false;
//
//
//            }
//            else {
//                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
//                AlertDialog dialog = new AlertDialog.Builder(Login.this)
//                        .setMessage("Please Check Your Internet Connection")
//                        .setPositiveButton("Retry", null)
//                        .show();
//
////                dialog.setCancelable(false);
////                dialog.setCanceledOnTouchOutside(false);
//                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
//                positive.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        new CheckLogin().execute();
//                        dialog.dismiss();
//                    }
//                });
//            }
//        }
//    }

//    public class Check extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//            waitProgress.show(getSupportFragmentManager(), "WaitBar");
//            waitProgress.setCancelable(false);
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            if (isConnected() && isOnline()) {
//
//                GettingData();
//                if (getConnected) {
//                    getConn = true;
//                    message = "Internet Connected";
//                }
//
//
//            } else {
//                getConn = false;
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
//            if (getConn) {
//
//                if (CompanyName == null) {
//                    CompanyName = "No Name Found";
//                }
//
//                if (SoftwareName == null) {
//                    SoftwareName = "Name of App";
//                }
//                softName.setText(CompanyName);
//
//                getConnected = false;
//                getConn = false;
//
//
//            }
//            else {
//                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
//                AlertDialog dialog = new AlertDialog.Builder(Login.this)
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

//    public void LoginQuery () {
//
//
//        try {
//            this.connection = createConnection();
//            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();
//
//            userInfoLists = new ArrayList<>();
//            userDesignations = new ArrayList<>();
//            isApproved = 0;
//            isLeaveApproved = 0;
//
//            Statement stmt = connection.createStatement();
//            StringBuffer stringBuffer = new StringBuffer();
////            StringBuffer stringBuffer1 = new StringBuffer();
////            StringBuffer stringBuffer2 = new StringBuffer();
////            StringBuffer stringBuffer3 = new StringBuffer();
////            StringBuffer stringBuffer4 = new StringBuffer();
////            StringBuffer stringBuffer5 = new StringBuffer();
////            StringBuffer stringBuffer6 = new StringBuffer();
////            StringBuffer stringBuffer7 = new StringBuffer();
//
//
//            ResultSet rs = stmt.executeQuery("select VALIDATE_USER_DB('" + userName + "',HAMID_ENCRYPT_DESCRIPTION_PACK.HEDP_ENCRYPT('" + password + "')) val from dual\n");
//
//
//            while (rs.next()) {
//                stringBuffer.append("USER ID: " + rs.getString(1) + "\n");
//                userId = rs.getString(1);
//
//            }
//
//            if (!userId.equals("-1")) {
//
//                String empCode = "";
//                ResultSet resEmpCode = stmt.executeQuery("select COM_PACK.GET_EMP_CODE_BY_EMP_ID((SELECT USR_EMP_ID FROM ISP_USER WHERE USR_ID = "+userId+")) valu from dual");
//                while (resEmpCode.next()) {
//                    empCode = resEmpCode.getString(1);
//                }
//                resEmpCode.close();
//
//                if (empCode.equals("0000")) {
//                    adminConnected = true;
//                }
//                else if (!empCode.equals("NO USER FOUND")){
//                    adminConnected = false;
//                    ResultSet resultSet = stmt.executeQuery("Select USR_NAME, USR_FNAME, USR_LNAME, USR_EMAIL, USR_CONTACT, USR_EMP_ID FROM ISP_USER\n" +
//                            "where USR_ID = " + userId + "\n");
//
//                    while (resultSet.next()) {
//                        emp_id = resultSet.getString(6);
//                        userInfoLists.add(new UserInfoList(empCode, resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5), resultSet.getString(6)));
//                    }
//
//                    ResultSet resultSet1 = stmt.executeQuery("SELECT DISTINCT JOB_SETUP_MST.JSM_CODE, JOB_SETUP_MST.JSM_NAME TEMP_TITLE, \n" +
//                            "JOB_SETUP_DTL.JSD_ID, JOB_SETUP_DTL.JSD_OBJECTIVE, DEPT_MST.DEPT_NAME, \n" +
//                            "DIVISION_MST.DIVM_NAME, DESIG_MST.DESIG_NAME, DESIG_MST.DESIG_PRIORITY, (Select TO_CHAR(MAX(EMP_JOB_HISTORY.JOB_ACTUAL_DATE),'DD-MON-YYYY') from EMP_JOB_HISTORY) as JOININGDATE, DIVISION_MST.DIVM_ID \n" +
//                            "FROM JOB_SETUP_MST, JOB_SETUP_DTL, DEPT_MST, DIVISION_MST, DESIG_MST, EMP_JOB_HISTORY\n" +
//                            "WHERE ((JOB_SETUP_DTL.JSD_JSM_ID = JOB_SETUP_MST.JSM_ID)\n" +
//                            " AND (JOB_SETUP_MST.JSM_DIVM_ID = DIVISION_MST.DIVM_ID)\n" +
//                            " AND (DEPT_MST.DEPT_ID = JOB_SETUP_MST.JSM_DEPT_ID)\n" +
//                            " AND (JOB_SETUP_MST.JSM_DESIG_ID = DESIG_MST.DESIG_ID))\n" +
//                            " AND JOB_SETUP_DTL.JSD_ID = (SELECT JOB_JSD_ID\n" +
//                            "                            FROM EMP_JOB_HISTORY\n" +
//                            "                            WHERE JOB_ID = (SELECT MAX(JOB_ID) FROM EMP_JOB_HISTORY WHERE JOB_EMP_ID = " + emp_id + "))" +
//                            "AND EMP_JOB_HISTORY.JOB_JSD_ID = JOB_SETUP_DTL.JSD_ID");
//
//                    while (resultSet1.next()) {
//
//                        userDesignations.add(new UserDesignation(resultSet1.getString(1), resultSet1.getString(2), resultSet1.getString(3), resultSet1.getString(4), resultSet1.getString(5), resultSet1.getString(6), resultSet1.getString(7), resultSet1.getString(8), resultSet1.getString(9), resultSet1.getString(10)));
//                    }
//
//                    ResultSet resultSet2 = stmt.executeQuery("SELECT COUNT (1) VAL\n" +
//                            "  FROM DAILY_ATTEN_REQ_MST\n" +
//                            " WHERE DARM_APP_CODE IN\n" +
//                            "          (SELECT DAAHL_APP_CODE\n" +
//                            "             FROM DAILY_ATTEN_APP_HIERARCHY_LOG, DAILY_ATTEN_REQ_MST\n" +
//                            "            WHERE     DAILY_ATTEN_APP_HIERARCHY_LOG.DAAHL_APP_CODE =\n" +
//                            "                         DAILY_ATTEN_REQ_MST.DARM_APP_CODE\n" +
//                            "                  AND NVL (DAILY_ATTEN_REQ_MST.DARM_APPROVED, 0) = 0\n" +
//                            "                  AND (DAILY_ATTEN_APP_HIERARCHY_LOG.DAAHL_APPROVER_BAND_ID =\n" +
//                            "                          (SELECT EMP_ID\n" +
//                            "                             FROM EMP_MST\n" +
//                            "                            WHERE EMP_CODE = '" + empCode + "')))");
//
//                    while (resultSet2.next()) {
//                        isApproved = resultSet2.getInt(1);
//                        System.out.println(isApproved);
//                    }
//                    System.out.println("isApproved: "+isApproved);
//
//                    ResultSet resultSet3 = stmt.executeQuery("SELECT count(DISTINCT LA_APP_CODE)\n" +
//                            "  FROM (SELECT LA.LA_APP_CODE\n" +
//                            "          FROM LEAVE_APPLICATION LA\n" +
//                            "         WHERE     LA.LA_EMP_ID IN\n" +
//                            "                      (SELECT C.JOB_EMP_ID\n" +
//                            "                         FROM JOB_SETUP_MST A,\n" +
//                            "                              JOB_SETUP_DTL B,\n" +
//                            "                              (SELECT JOB_EMP_ID, JOB_JSD_ID\n" +
//                            "                                 FROM EMP_JOB_HISTORY EJH\n" +
//                            "                                WHERE JOB_ID =\n" +
//                            "                                         (SELECT MAX (JOB_ID)\n" +
//                            "                                            FROM EMP_JOB_HISTORY EH\n" +
//                            "                                           WHERE EJH.JOB_EMP_ID =\n" +
//                            "                                                    EH.JOB_EMP_ID)) C,\n" +
//                            "                              DESIG_MST     D   --, leave_approval_hierarchy e\n" +
//                            "                        WHERE     A.JSM_ID = B.JSD_JSM_ID\n" +
//                            "                              AND C.JOB_JSD_ID = B.JSD_ID\n" +
//                            "                              AND D.DESIG_ID = A.JSM_DESIG_ID\n" +
//                            "                              AND D.DESIG_PRIORITY IN\n" +
//                            "                                     (SELECT L.LAH_BAND_NO\n" +
//                            "                                        FROM LEAVE_APPROVAL_HIERARCHY L\n" +
//                            "                                       WHERE INSTR (\n" +
//                            "                                                   ','\n" +
//                            "                                                || L.LAH_APPROVAL_BAND\n" +
//                            "                                                || ',',\n" +
//                            "                                                   ','\n" +
//                            "                                                || EMP_PACKAGE.GET_BAND_BY_EMP_CODE (\n" +
//                            "                                                      '" + empCode + "')\n" +
//                            "                                                || ',') > 0))\n" +
//                            "               AND NVL (LA.LA_APPROVED, 0) = 0\n" +
//                            "        UNION ALL\n" +
//                            "        SELECT LA.LA_APP_CODE\n" +
//                            "          FROM LEAVE_APPLICATION LA\n" +
//                            "         WHERE     LA.LA_EMP_ID IN\n" +
//                            "                      (SELECT C.JOB_EMP_ID\n" +
//                            "                         FROM JOB_SETUP_MST A,\n" +
//                            "                              JOB_SETUP_DTL B,\n" +
//                            "                              (SELECT JOB_EMP_ID, JOB_JSD_ID\n" +
//                            "                                 FROM EMP_JOB_HISTORY EJH\n" +
//                            "                                WHERE JOB_ID =\n" +
//                            "                                         (SELECT MAX (JOB_ID)\n" +
//                            "                                            FROM EMP_JOB_HISTORY EH\n" +
//                            "                                           WHERE EJH.JOB_EMP_ID =\n" +
//                            "                                                    EH.JOB_EMP_ID)) C,\n" +
//                            "                              DESIG_MST     D   --, leave_approval_hierarchy e\n" +
//                            "                        WHERE     A.JSM_ID = B.JSD_JSM_ID\n" +
//                            "                              AND C.JOB_JSD_ID = B.JSD_ID\n" +
//                            "                              AND D.DESIG_ID = A.JSM_DESIG_ID\n" +
//                            "                              AND D.DESIG_PRIORITY IN\n" +
//                            "                                     ( (SELECT LAH.LAH_BAND_NO\n" +
//                            "                                          FROM LEAVE_APPROVAL_HIERARCHY LAH\n" +
//                            "                                         WHERE INSTR (\n" +
//                            "                                                     ','\n" +
//                            "                                                  || LAH.LAH_SP_APPROVAL_CODE\n" +
//                            "                                                  || ',',\n" +
//                            "                                                  ',' || '" + empCode + "' || ',') > 0)))\n" +
//                            "               AND NVL (LA.LA_APPROVED, 0) = 0)\n" +
//                            " WHERE LA_APP_CODE NOT IN\n" +
//                            "       (SELECT CASE\n" +
//                            "          WHEN COM_PACK.GET_EMPLOYEE_ID_BY_USER ('" + empCode + "') =\n" +
//                            "                  LAD_FORWARD_TO_ID\n" +
//                            "          THEN\n" +
//                            "             'No code'\n" +
//                            "          ELSE\n" +
//                            "             GET_LEAVE_APP_CODE (LAD_LA_ID)\n" +
//                            "       END\n" +
//                            "  FROM LEAVE_APPLICATION_DTL\n" +
//                            " WHERE LAD_ID IN (  SELECT MAX (LAD_ID)\n" +
//                            "                      FROM LEAVE_APPLICATION_DTL D, LEAVE_APPLICATION M\n" +
//                            "                     WHERE M.LA_ID = D.LAD_LA_ID\n" +
//                            "                  GROUP BY LAD_LA_ID))");
//
//                    while (resultSet3.next()) {
//                        isLeaveApproved = resultSet3.getInt(1);
//                        System.out.println("LEAVE PRRR: " + isLeaveApproved);
//                    }
//
//                    ResultSet resultSet4 = stmt.executeQuery("select EMP_LIVE_LOC_TRACKER_FLAG from EMP_MST WHERE EMP_CODE = '"+empCode+"'");
//
//                    while (resultSet4.next()) {
//                        live_loc_flag = resultSet4.getInt(1);
//                        System.out.println("LIVE LOCATION FLAG: "+ live_loc_flag);
//                    }
//                    infoConnected = true;
//                }
//            }
//            System.out.println(stringBuffer);
//
//
//            connected = true;
//
//            connection.close();
//
//        } catch (Exception e) {
//
//            //   Toast.makeText(MainActivity.this, ""+e,Toast.LENGTH_LONG).show();
//            Log.i("ERRRRR", e.getLocalizedMessage());
//            e.printStackTrace();
//        }
//
//    }

    public void dynamicLoginCheck() {
        waitProgress.show(getSupportFragmentManager(), "WaitBar");
        waitProgress.setCancelable(false);
        userInfoLists = new ArrayList<>();
        userDesignations = new ArrayList<>();
        isApproved = 0;
        isLeaveApproved = 0;
        live_loc_flag = 0;
        conn = false;
        connected = false;
        infoConnected = false;
        userId = "";
        CompanyName = "";
        SoftwareName = "";
        System.out.println("START");

        checkToGetLoginData();
    }

    public void checkToGetLoginData() {
        boolean allUpdated = false;
        for (int i = 0; i < urls.size(); i++) {
            allUpdated = urls.get(i).isChecked();
            if (!urls.get(i).isChecked()) {
                allUpdated = urls.get(i).isChecked();
                String url = urls.get(i).getUrls();
                System.out.println(i+" Started");
                getLoginData(url,i);
                break;
            }
        }
        if (allUpdated) {
            System.out.println("all clear");
            goToDashboard();
        }
    }

    public void getLoginData(String url, int index) {
        String useridUrl = url + "login/getUserIdNew?user_name="+userName+"&pass="+password;

        RequestQueue requestQueue = Volley.newRequestQueue(Login.this);

        StringRequest getUserId = new StringRequest(Request.Method.GET, useridUrl, response -> {
            conn = true;
            try {
                connected = true;
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject userIdInfo = array.getJSONObject(i);
                        userId = userIdInfo.getString("val").equals("null") ? "" : userIdInfo.getString("val");
                    }
                }

                if (userId.isEmpty() || userId.equals("-1")) {
                    urls.get(index).setChecked(true);
                    checkToGetLoginData();
                }
                else {
                    center_api = url;
                    getEmpCode(userId);
                }
            }
            catch (JSONException e) {
                connected = false;
                logger.log(Level.WARNING,e.getMessage(),e);
                urls.get(index).setChecked(true);
                checkToGetLoginData();
            }

        }, error -> {
            conn = false;
            connected = false;
            logger.log(Level.WARNING,error.getMessage(),error);
            urls.get(index).setChecked(true);
            checkToGetLoginData();
        });

        requestQueue.add(getUserId);
    }

    public void getEmpCode(String u_id) {
        emp_code = "";
        emp_id = "";
        adminConnected = false;
        noUser = false;
        String empCodeUrl = center_api + "login/getEmpCodebyUser/"+u_id;
        String userInfoUrl = center_api + "login/getUserInfo/"+u_id;

        RequestQueue requestQueue = Volley.newRequestQueue(Login.this);

        StringRequest userInfoRequest = new StringRequest(Request.Method.GET, userInfoUrl, response -> {
            conn = true;
            try {
                connected = true;
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject userInfo = array.getJSONObject(i);
//                        String usr_name = userInfo.getString("usr_name");
                        String usr_fname = userInfo.getString("usr_fname");
                        String usr_lname = userInfo.getString("usr_lname");
                        String usr_email = userInfo.getString("usr_email");
                        String usr_contact = userInfo.getString("usr_contact");
                        String usr_emp_id = userInfo.getString("usr_emp_id");
                        emp_id = usr_emp_id;
                        userInfoLists.add(new UserInfoList(emp_code,usr_fname,usr_lname,usr_email,usr_contact,usr_emp_id));
                    }
                }
                getUserDetails();
            }
            catch (JSONException e) {
                connected = false;
                logger.log(Level.WARNING,e.getMessage(),e);
                goToDashboard();
            }
        }, error -> {
            conn = false;
            connected = false;
            logger.log(Level.WARNING,error.getMessage(),error);
            goToDashboard();
        });

        StringRequest empCodeRequest = new StringRequest(Request.Method.GET, empCodeUrl, response -> {
            conn = true;
            try {
                connected = true;
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject empCodeInfo = array.getJSONObject(i);
                        emp_code = empCodeInfo.getString("valu");
                    }
                    if (emp_code.equals("0000")) {
                        adminConnected = true;
                        goToDashboard();
                    }
                    else if (!emp_code.equals("NO USER FOUND")) {
                        adminConnected = false;
                        noUser = false;
                        requestQueue.add(userInfoRequest);
                    }
                    else {
                        adminConnected = false;
                        noUser = true;
                        goToDashboard();
                    }
                }
            }
            catch (JSONException e) {
                connected = false;
                logger.log(Level.WARNING,e.getMessage(),e);
                goToDashboard();
            }

        },error -> {
            conn = false;
            connected = false;
            logger.log(Level.WARNING,error.getMessage(),error);
            goToDashboard();
        });

        requestQueue.add(empCodeRequest);
    }

    public void getUserDetails() {
        String designationUrl = center_api + "login/getUserDesignations/"+emp_id;
        String attendanceAppUrl = center_api + "approval_flag/getAttendanceApproval/"+emp_code;
        String leaveAppUrl = center_api + "approval_flag/getLeaveApproval/"+emp_code;
        String liveLocFlagUrl = center_api + "utility/getLiveLocationFlag/"+emp_code;
        String updateEmpFlagUrl = center_api + "login/updateEmpFlag";
        String companyUrl = center_api + "utility/getCompanyName";
        String softwareUrl = center_api + "utility/getSoftwareName";

        RequestQueue requestQueue = Volley.newRequestQueue(Login.this);

        StringRequest updateFlag = new StringRequest(Request.Method.POST, updateEmpFlagUrl, response -> {
            conn = true;
             try {
                 connected = true;
                 JSONObject jsonObject = new JSONObject(response);
                 String string_out = jsonObject.getString("string_out");
                 if (string_out.equals("Successfully Created")) {
                     infoConnected = true;
                 }
                 else {
                     System.out.println(string_out);
                     connected = false;
                 }
                 goToDashboard();
             }
             catch (JSONException e) {
                 connected = false;
                 logger.log(Level.WARNING,e.getMessage(),e);
                 goToDashboard();
             }
        }, error -> {
            conn = false;
            connected = false;
            logger.log(Level.WARNING,error.getMessage(),error);
            goToDashboard();
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("P_EMP_ID", emp_id);
                return headers;
            }
        };

        StringRequest getSoftwareRequest = new StringRequest(Request.Method.GET, softwareUrl, response -> {
            conn = true;
            try {
                connected = true;
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject softwareInfo = array.getJSONObject(i);
                        SoftwareName = softwareInfo.getString("lic_software_name");
                    }
                }
                requestQueue.add(updateFlag);
            }
            catch (JSONException e) {
                connected = false;
                logger.log(Level.WARNING,e.getMessage(),e);
                goToDashboard();
            }
        },error -> {
            conn = false;
            connected = false;
            logger.log(Level.WARNING,error.getMessage(),error);
            goToDashboard();
        });

        StringRequest getCompanyRequest = new StringRequest(Request.Method.GET, companyUrl, response -> {
            conn = true;
            try {
                connected = true;
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject companyInfo = array.getJSONObject(i);
                        CompanyName = companyInfo.getString("cim_name");
                    }
                }
                requestQueue.add(getSoftwareRequest);
            }
            catch (JSONException e) {
                connected = false;
                logger.log(Level.WARNING,e.getMessage(),e);
                goToDashboard();
            }
        }, error -> {
            conn = false;
            connected = false;
            logger.log(Level.WARNING,error.getMessage(),error);
            goToDashboard();
        });

        StringRequest livLocFlReq = new StringRequest(Request.Method.GET, liveLocFlagUrl, response -> {
            conn = true;
            try {
                connected = true;
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject livLocFlInfo = array.getJSONObject(i);
                        live_loc_flag = Integer.parseInt(livLocFlInfo.getString("emp_live_loc_tracker_flag")
                                .equals("null") ? "0" : livLocFlInfo.getString("emp_live_loc_tracker_flag"));
                        tracker_flag = livLocFlInfo.getString("emp_timeline_tracker_flag")
                                .equals("null") ? "" :livLocFlInfo.getString("emp_timeline_tracker_flag");
                    }
                }
                requestQueue.add(getCompanyRequest);
            }
            catch (JSONException e) {
                connected = false;
                logger.log(Level.WARNING,e.getMessage(),e);
                goToDashboard();
            }
        }, error -> {
            conn = false;
            connected = false;
            logger.log(Level.WARNING,error.getMessage(),error);
            goToDashboard();
        });

        StringRequest leaveAppReq = new StringRequest(Request.Method.GET, leaveAppUrl, response -> {
            conn = true;
            try {
                connected = true;
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject leaveAppInfo = array.getJSONObject(i);
                        isLeaveApproved = leaveAppInfo.getInt("l_val");
                    }
                }
                requestQueue.add(livLocFlReq);
            }
            catch (JSONException e) {
                connected = false;
                logger.log(Level.WARNING,e.getMessage(),e);
                goToDashboard();
            }
        },error -> {
            conn = false;
            connected = false;
            logger.log(Level.WARNING,error.getMessage(),error);
            goToDashboard();
        });

        StringRequest attendAppReq = new StringRequest(Request.Method.GET,attendanceAppUrl, response -> {
            conn = true;
            try {
                connected = true;
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject attAppInfo = array.getJSONObject(i);
                        isApproved = attAppInfo.getInt("val");
                    }
                }
                requestQueue.add(leaveAppReq);
            }
            catch (JSONException e) {
                connected = false;
                logger.log(Level.WARNING,e.getMessage(),e);
                goToDashboard();
            }
        },error -> {
            conn = false;
            connected = false;
            logger.log(Level.WARNING,error.getMessage(),error);
            goToDashboard();
        });

        StringRequest designationRequest = new StringRequest(Request.Method.GET, designationUrl, response -> {
            conn = true;
            try {
                connected = true;
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject desigInfo = array.getJSONObject(i);
                        String jsm_code = desigInfo.getString("jsm_code");
                        String temp_title = desigInfo.getString("temp_title");
                        String jsd_id = desigInfo.getString("jsd_id");
                        String jsd_objective = desigInfo.getString("jsd_objective");
                        String dept_name = desigInfo.getString("dept_name");
                        String divm_name = desigInfo.getString("divm_name");
                        String desig_name = desigInfo.getString("desig_name");
                        String desig_priority = desigInfo.getString("desig_priority");
                        String joiningdate = desigInfo.getString("joiningdate");
                        String divm_id = desigInfo.getString("divm_id");
                        userDesignations.add(new UserDesignation(jsm_code,temp_title,jsd_id,jsd_objective,dept_name,divm_name,desig_name,desig_priority,joiningdate,divm_id));
                    }
                }
                requestQueue.add(attendAppReq);
            }
            catch (JSONException e) {
                connected = false;
                logger.log(Level.WARNING,e.getMessage(),e);
                goToDashboard();
            }
        }, error -> {
            conn = false;
            connected = false;
            logger.log(Level.WARNING,error.getMessage(),error);
            goToDashboard();
        });

        requestQueue.add(designationRequest);
    }

    public void goToDashboard() {
        waitProgress.dismiss();
        for (int i = 0; i < urls.size(); i++) {
            urls.get(i).setChecked(false);
        }
        if (conn) {
            if (connected) {
                if (!userId.equals("-1") && !userId.isEmpty()) {
                    if (adminConnected) {
                        Toast.makeText(getApplicationContext(),"Admin can not access this app.",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if (noUser) {
                            Toast.makeText(getApplicationContext(),"No User Found.",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            if (infoConnected) {
                                if (CompanyName == null) {
                                    CompanyName = "No Name Found";
                                }

                                if (SoftwareName == null) {
                                    SoftwareName = "Name of App";
                                }

                                if (checkBox.isChecked()) {
                                    System.out.println("Remembered");
                                    SharedPreferences.Editor editor = sharedpreferences.edit();
                                    editor.remove(user_emp_code);
                                    editor.remove(user_password);
                                    editor.remove(checked);
                                    editor.putString(user_emp_code,userName);
                                    editor.putString(user_password,password);
                                    editor.putBoolean(checked,true);
                                    editor.apply();
                                    editor.commit();
                                } else {
                                    System.out.println("Not Remembered");
                                    SharedPreferences.Editor editor = sharedpreferences.edit();
                                    editor.remove(user_emp_code);
                                    editor.remove(user_password);
                                    editor.remove(checked);
                                    editor.apply();
                                    editor.commit();
                                }

                                SharedPreferences.Editor widgetEditor = attendanceWidgetPreferences.edit();
                                widgetEditor.remove(WIDGET_EMP_ID);
                                widgetEditor.remove(WIDGET_TRACKER_FLAG);

                                widgetEditor.putString(WIDGET_EMP_ID, userInfoLists.get(0).getEmp_id());
                                widgetEditor.putString(WIDGET_TRACKER_FLAG, tracker_flag);
                                widgetEditor.apply();
                                widgetEditor.commit();

                                SharedPreferences.Editor editor1 = sharedLogin.edit();
                                editor1.remove(USER_NAME);
                                editor1.remove(USER_F_NAME);
                                editor1.remove(USER_L_NAME);
                                editor1.remove(EMAIL);
                                editor1.remove(CONTACT);
                                editor1.remove(EMP_ID_LOGIN);
                                editor1.remove(EMP_PASSWORD);

                                editor1.remove(JSM_CODE);
                                editor1.remove(JSM_NAME);
                                editor1.remove(JSD_ID_LOGIN);
                                editor1.remove(JSD_OBJECTIVE);
                                editor1.remove(DEPT_NAME);
                                editor1.remove(DIV_NAME);
                                editor1.remove(DESG_NAME);
                                editor1.remove(DESG_PRIORITY);
                                editor1.remove(JOINING_DATE);
                                editor1.remove(DIV_ID);
                                editor1.remove(LOGIN_TF);

                                editor1.remove(IS_ATT_APPROVED);
                                editor1.remove(IS_LEAVE_APPROVED);
                                editor1.remove(COMPANY);
                                editor1.remove(SOFTWARE);
                                editor1.remove(LIVE_FLAG);
//                                editor1.remove(DATABASE_NAME);

                                editor1.putString(USER_NAME, userInfoLists.get(0).getUserName());
                                editor1.putString(USER_F_NAME, userInfoLists.get(0).getUser_fname());
                                editor1.putString(USER_L_NAME, userInfoLists.get(0).getUser_lname());
                                editor1.putString(EMAIL, userInfoLists.get(0).getEmail());
                                editor1.putString(CONTACT, userInfoLists.get(0).getContact());
                                editor1.putString(EMP_ID_LOGIN, userInfoLists.get(0).getEmp_id());
                                editor1.putString(EMP_PASSWORD,password);

                                if (!userDesignations.isEmpty()) {
                                    editor1.putString(JSM_CODE, userDesignations.get(0).getJsm_code());
                                    editor1.putString(JSM_NAME, userDesignations.get(0).getJsm_name());
                                    editor1.putString(JSD_ID_LOGIN, userDesignations.get(0).getJsd_id());
                                    editor1.putString(JSD_OBJECTIVE, userDesignations.get(0).getJsd_objective());
                                    editor1.putString(DEPT_NAME, userDesignations.get(0).getDept_name());
                                    editor1.putString(DIV_NAME, userDesignations.get(0).getDiv_name());
                                    editor1.putString(DESG_NAME, userDesignations.get(0).getDesg_name());
                                    editor1.putString(DESG_PRIORITY, userDesignations.get(0).getDesg_priority());
                                    editor1.putString(JOINING_DATE, userDesignations.get(0).getJoining_date());
                                    editor1.putString(DIV_ID, userDesignations.get(0).getDiv_id());
                                } else {
                                    editor1.putString(JSM_CODE, null);
                                    editor1.putString(JSM_NAME, null);
                                    editor1.putString(JSD_ID_LOGIN, null);
                                    editor1.putString(JSD_OBJECTIVE, null);
                                    editor1.putString(DEPT_NAME, null);
                                    editor1.putString(DIV_NAME, null);
                                    editor1.putString(DESG_NAME, null);
                                    editor1.putString(DESG_PRIORITY, null);
                                    editor1.putString(JOINING_DATE, null);
                                    editor1.putString(DIV_ID, null);
                                }

                                editor1.putBoolean(LOGIN_TF,true);

                                editor1.putInt(IS_ATT_APPROVED, isApproved);
                                editor1.putInt(IS_LEAVE_APPROVED, isLeaveApproved);
                                editor1.putString(COMPANY, CompanyName);
                                editor1.putString(SOFTWARE,SoftwareName);
                                editor1.putInt(LIVE_FLAG,live_loc_flag);
                                editor1.putString(CENTER_API_FRONT, center_api);
//                                editor1.putString(DATABASE_NAME,DEFAULT_USERNAME);
                                editor1.apply();
                                editor1.commit();


                                Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                                startActivity(intent);
                                finish();
                            }
                            else {
                                dynamicLoginCheck();
                            }
                        }
                    }
                }
                else {
                    login_failed.setVisibility(View.VISIBLE);
                }
                conn = false;
                connected = false;
                adminConnected = false;
                infoConnected = false;
                noUser = false;
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(Login.this)
                        .setMessage("There is a network issue in the server. Please Try later.")
                        .setPositiveButton("Retry", null)
                        .show();

                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    dynamicLoginCheck();
                    dialog.dismiss();
                });
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            AlertDialog dialog = new AlertDialog.Builder(Login.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .show();

            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                dynamicLoginCheck();
                dialog.dismiss();
            });
        }
    }

//    public void GettingData () {
//        try {
//            this.connection = createConnection();
//            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();
//
//
//            Statement stmt = connection.createStatement();
//
//            ResultSet rs = stmt.executeQuery("SELECT CIM_NAME--,CIM_LOGO_APPS \n" +
//                    "FROM COMPANY_INFO_MST\n");
//
//            while (rs.next()) {
//                CompanyName = rs.getString(1);
//                System.out.println(CompanyName);
//
////                pic = (BLOB) rs.getBlob(2);
////                System.out.println(String.valueOf(pic));
////
////                byte[] barr = pic.getBytes(1,(int)pic.length());
////                System.out.println(Arrays.toString(barr));
////                String encodedImageString = Base64.encodeToString(barr, Base64.DEFAULT);
////                byte[] bytes = Base64.decode(encodedImageString,Base64.DEFAULT);
////                System.out.println(Arrays.toString(bytes));
//
////                String bytes = rs.getString(2);
//
////                System.out.println(bytes);
////                byte[] b = bytes.getBytes();
////                System.out.println(Arrays.toString(b));
//
//                // Blob image = rs.getBlob(2);
////                Bitmap bmp= BitmapFactory.decodeByteArray(bytes, 0 , bytes.length);
////                System.out.println(bmp);
////                imageView.setImageBitmap(bmp);
//
////                File myExternalFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"Logo.jpeg");
////                FileOutputStream fos = new FileOutputStream(myExternalFile);//Get OutputStream for NewFile Location
////                fos.write(barr);
////                fos.close();
////
////                BitmapFactory.Options options = new BitmapFactory.Options();
////                options.inMutable = true;
////                options.inJustDecodeBounds = true;
////                Bitmap bitmap = BitmapFactory.decodeByteArray(barr,0,barr.length);
////                System.out.println(bitmap);
////                imageView.setImageBitmap(bitmap);
////
////                InputStream inputStream = pic.getBinaryStream();
////                System.out.println(inputStream);
////
////                bitmap = BitmapFactory.decodeStream(inputStream);
////                System.out.println(bitmap);
//
//
//            }
//
//
//            ResultSet resultSet = stmt.executeQuery(" select LIC_SOFTWARE_NAME from isp_runauto where rownum=1\n");
//
//            while (resultSet.next()) {
//                SoftwareName = resultSet.getString(1);
//                System.out.println(SoftwareName);
//            }
//
//
////            CallableStatement callableStatement = connection.prepareCall("begin ? := GET_LEAVE_BALANCE(?,?,?); end;");
////            callableStatement.setInt(2,2008);
////            callableStatement.setString(3, "16-AUG-21");
////            callableStatement.setString(4,"EL");
////            callableStatement.registerOutParameter(1,Types.INTEGER);
////            callableStatement.execute();
////            int ddd = callableStatement.getInt(1);
////            System.out.println(ddd);
////            callableStatement.close();
////
////            CallableStatement callableStatement1 = connection.prepareCall("begin ? := GET_CONSUMED_LEAVE_BY_MONTH(?,?,?); end;");
////            callableStatement1.setInt(2,2008);
////            callableStatement1.setString(3, "01-AUG-21");
////            callableStatement1.setString(4,"EL");
////            callableStatement1.registerOutParameter(1,Types.INTEGER);
////            callableStatement1.execute();
////            int dddd = callableStatement1.getInt(1);
////            System.out.println(dddd);
////            callableStatement1.close();
//
//
//            getConnected = true;
//            connection.close();
//
//        } catch (Exception e) {
//
//            //   Toast.makeText(MainActivity.this, ""+e,Toast.LENGTH_LONG).show();
//            Log.i("ERRRRR", e.getLocalizedMessage());
//            e.printStackTrace();
//        }
//    }

}