package ttit.com.shuvo.ikglhrm.user_login;

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
import static ttit.com.shuvo.ikglhrm.utilities.Constants.api_url_front;
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

import ttit.com.shuvo.ikglhrm.AllUrlList;
import ttit.com.shuvo.ikglhrm.R;
import ttit.com.shuvo.ikglhrm.UserDesignation;
import ttit.com.shuvo.ikglhrm.UserInfoList;
import ttit.com.shuvo.ikglhrm.WaitProgress;
import ttit.com.shuvo.ikglhrm.dashboard.Dashboard;
import ttit.com.shuvo.ikglhrm.user_login.dialoges.SelectCenterDialogue;
import ttit.com.shuvo.ikglhrm.user_login.interfaces.CallBackListener;
import ttit.com.shuvo.ikglhrm.user_login.model.CenterList;
import ttit.com.shuvo.ikglhrm.utilities.EdgeToEdgeHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity implements CallBackListener {

    TextInputEditText user;
    TextInputEditText pass;

    TextView login_failed;
    TextView softName;

    Button login;

    CheckBox checkBox;

    String userName = "";
    String password = "";
    public static String CompanyName = "";
    public static String SoftwareName = "";
    public static int isApproved = 0;
    public static int isLeaveApproved = 0;

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean infoConnected = false;
    private Boolean connected = false;

    SharedPreferences sharedpreferences;

    SharedPreferences sharedLogin;

    SharedPreferences attendanceWidgetPreferences;

    String getUserName = "";
    String getPassword = "";
    boolean getChecked = false;

    public static ArrayList<UserInfoList> userInfoLists;
    public static ArrayList<UserDesignation> userDesignations;

    String emp_id = "";
    String emp_code = "";
    int live_loc_flag = 0;
    String tracker_flag = "";

    ArrayList<AllUrlList> urls;
    String text_url = "https://raw.githubusercontent.com/shuvo934/Story/refs/heads/master/hrmServers";
    ArrayList<CenterList> centerLists;

    Logger logger = Logger.getLogger(Login.class.getName());
    String parsing_message = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdgeHelper.enable(this);
        setContentView(R.layout.activity_login);
        EdgeToEdgeHelper.applyInsets(this,
                findViewById(R.id.login_root),
                findViewById(R.id.login_scroll_view),
                findViewById(R.id.login_scroll_content),
                false,
                false);

        userInfoLists = new ArrayList<>();
        userDesignations = new ArrayList<>();

        softName = findViewById(R.id.name_of_soft_login);
        user = findViewById(R.id.user_name_given);
        pass = findViewById(R.id.password_given);
        checkBox = findViewById(R.id.remember_checkbox);

        login_failed = findViewById(R.id.email_pass_miss);

        login = findViewById(R.id.login_button);

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

//        String android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

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
        emp_code = "";
        CompanyName = "";
        SoftwareName = "";
        centerLists = new ArrayList<>();
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
            updateLayout();
        }
    }

    public void getLoginData(String url, int index) {
        emp_code = "";
        CompanyName = "";
        emp_id = "";
        String useridUrl = url + "login/getUserLoginData?p_mail="+userName+"&p_password="+password;
        String companyUrl = url + "utility/getCompanyName";

        RequestQueue requestQueue = Volley.newRequestQueue(Login.this);

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
                        CompanyName = companyInfo.getString("cim_name").equals("null") ? "No Name Found" : companyInfo.getString("cim_name");
                    }
                }
                centerLists.add(new CenterList(url,CompanyName,emp_code,emp_id));
                urls.get(index).setChecked(true);
                checkToGetLoginData();
            }
            catch (JSONException e) {
                connected = false;
                parsing_message = e.getLocalizedMessage();
                logger.log(Level.WARNING,e.getMessage(),e);
                urls.get(index).setChecked(true);
                checkToGetLoginData();
            }
        }, error -> {
            conn = false;
            connected = false;
            parsing_message = error.getLocalizedMessage();
            logger.log(Level.WARNING,error.getMessage(),error);
            urls.get(index).setChecked(true);
            checkToGetLoginData();
        });

        StringRequest getUserMessage = new StringRequest(Request.Method.GET, useridUrl, response -> {
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
                       emp_code = userIdInfo.getString("emp_code")
                                .equals("null") ? "" : userIdInfo.getString("emp_code");
                       emp_id = userIdInfo.getString("emp_id")
                                .equals("null") ? "" : userIdInfo.getString("emp_id");
                    }
                }

                if (emp_code.isEmpty()) {
                    urls.get(index).setChecked(true);
                    checkToGetLoginData();
                }
                else {
                    requestQueue.add(getCompanyRequest);
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

        requestQueue.add(getUserMessage);
    }

    private void updateLayout() {
        for (int i = 0; i < urls.size(); i++) {
            urls.get(i).setChecked(false);
        }
        if (!centerLists.isEmpty()) {
            if (centerLists.size() == 1) {
                api_url_front = centerLists.get(0).getCenter_api();
                emp_code = centerLists.get(0).getUser_emp_code();
                emp_id = centerLists.get(0).getUser_emp_id();
                CompanyName = centerLists.get(0).getCenter_name();
                getUserDetails();
            }
            else {
                waitProgress.dismiss();
                SelectCenterDialogue selectCenterDialogue = new SelectCenterDialogue(centerLists,Login.this);
                selectCenterDialogue.show(getSupportFragmentManager(),"CENTER");
            }
        }
        else {
            waitProgress.dismiss();
            if (conn) {
                if (connected) {
                    login_failed.setVisibility(View.VISIBLE);
                    MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(Login.this);
                    alertDialogBuilder.setTitle("Warning!")
                            .setIcon(R.drawable.hrm_new_round_icon_custom)
                            .setMessage("No User Found")
                            .setPositiveButton("OK", (dialog, which) -> dialog.dismiss());

                    AlertDialog alert = alertDialogBuilder.create();
                    alert.show();
                }
                else {
                    if (parsing_message != null) {
                        if (parsing_message.isEmpty() || parsing_message.equals("null")) {
                            parsing_message = "Server problem or Internet not connected";
                        }
                    }
                    else {
                        parsing_message = "Server problem or Internet not connected";
                    }
                    MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(Login.this);
                    alertDialogBuilder.setTitle("System Warning!")
                            .setIcon(R.drawable.hrm_new_round_icon_custom)
                            .setMessage("Message: "+parsing_message+".\n"+"Please try again.")
                            .setPositiveButton("Retry", (dialog, which) -> {
                                dynamicLoginCheck();
                                dialog.dismiss();
                            });

                    AlertDialog alert = alertDialogBuilder.create();
                    alert.show();
                }
            }
            else {
                if (parsing_message != null) {
                    if (parsing_message.isEmpty() || parsing_message.equals("null")) {
                        parsing_message = "Server problem or Internet not connected";
                    }
                }
                else {
                    parsing_message = "Server problem or Internet not connected";
                }
                MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(Login.this);
                alertDialogBuilder.setTitle("System Warning!")
                        .setIcon(R.drawable.hrm_new_round_icon_custom)
                        .setMessage("Message: "+parsing_message+".\n"+"Please try again.")
                        .setPositiveButton("Retry", (dialog, which) -> {
                            dynamicLoginCheck();
                            dialog.dismiss();
                        });

                AlertDialog alert = alertDialogBuilder.create();
                alert.show();
            }
        }
    }

//    public void getLoginData(String url, int index) {
//        String useridUrl = url + "login/getUserIdNew?user_name="+userName+"&pass="+password;
//
//        RequestQueue requestQueue = Volley.newRequestQueue(Login.this);
//
//        StringRequest getUserId = new StringRequest(Request.Method.GET, useridUrl, response -> {
//            conn = true;
//            try {
//                connected = true;
//                JSONObject jsonObject = new JSONObject(response);
//                String items = jsonObject.getString("items");
//                String count = jsonObject.getString("count");
//                if (!count.equals("0")) {
//                    JSONArray array = new JSONArray(items);
//                    for (int i = 0; i < array.length(); i++) {
//                        JSONObject userIdInfo = array.getJSONObject(i);
//                        userId = userIdInfo.getString("val").equals("null") ? "" : userIdInfo.getString("val");
//                    }
//                }
//
//                if (userId.isEmpty() || userId.equals("-1")) {
//                    urls.get(index).setChecked(true);
//                    checkToGetLoginData();
//                }
//                else {
//                    center_api = url;
//                    getEmpCode(userId);
//                }
//            }
//            catch (JSONException e) {
//                connected = false;
//                logger.log(Level.WARNING,e.getMessage(),e);
//                urls.get(index).setChecked(true);
//                checkToGetLoginData();
//            }
//
//        }, error -> {
//            conn = false;
//            connected = false;
//            logger.log(Level.WARNING,error.getMessage(),error);
//            urls.get(index).setChecked(true);
//            checkToGetLoginData();
//        });
//
//        requestQueue.add(getUserId);
//    }

//    public void getEmpCode(String u_id) {
//        emp_id = "";
//        adminConnected = false;
//        noUser = false;
//        String empCodeUrl = center_api + "login/getEmpCodebyUser/"+u_id;
//        String userInfoUrl = center_api + "login/getUserInfo/"+u_id;
//
//        RequestQueue requestQueue = Volley.newRequestQueue(Login.this);
//
//
//
//        StringRequest empCodeRequest = new StringRequest(Request.Method.GET, empCodeUrl, response -> {
//            conn = true;
//            try {
//                connected = true;
//                JSONObject jsonObject = new JSONObject(response);
//                String items = jsonObject.getString("items");
//                String count = jsonObject.getString("count");
//                if (!count.equals("0")) {
//                    JSONArray array = new JSONArray(items);
//                    for (int i = 0; i < array.length(); i++) {
//                        JSONObject empCodeInfo = array.getJSONObject(i);
//                        emp_code = empCodeInfo.getString("valu");
//                    }
//                    if (emp_code.equals("0000")) {
//                        adminConnected = true;
//                        goToDashboard();
//                    }
//                    else if (!emp_code.equals("NO USER FOUND")) {
//                        adminConnected = false;
//                        noUser = false;
//                        requestQueue.add(userInfoRequest);
//                    }
//                    else {
//                        adminConnected = false;
//                        noUser = true;
//                        goToDashboard();
//                    }
//                }
//            }
//            catch (JSONException e) {
//                connected = false;
//                logger.log(Level.WARNING,e.getMessage(),e);
//                goToDashboard();
//            }
//
//        },error -> {
//            conn = false;
//            connected = false;
//            logger.log(Level.WARNING,error.getMessage(),error);
//            goToDashboard();
//        });
//
//        requestQueue.add(empCodeRequest);
//    }

    public void getUserDetails() {
        String userInfoUrl = api_url_front + "login/getUserInfoData?p_emp_code="+emp_code;
        String designationUrl = api_url_front + "login/getUserDesignations/"+emp_id;
        String attendanceAppUrl = api_url_front + "approval_flag/getAttendanceApproval/"+emp_code;
        String leaveAppUrl = api_url_front + "approval_flag/getLeaveApproval/"+emp_code;
//        String liveLocFlagUrl = api_url_front + "utility/getLiveLocationFlag/"+emp_code;
        String updateEmpFlagUrl = api_url_front + "login/updateEmpFlag";
//        String companyUrl = api_url_front + "utility/getCompanyName";
        String softwareUrl = api_url_front + "utility/getSoftwareName";

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
                 parsing_message = e.getLocalizedMessage();
                 logger.log(Level.WARNING,e.getMessage(),e);
                 goToDashboard();
             }
        }, error -> {
            conn = false;
            connected = false;
            parsing_message = error.getLocalizedMessage();
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
                        SoftwareName = softwareInfo.getString("lic_software_name")
                                .equals("null") ? "No Name Found" : softwareInfo.getString("lic_software_name");
                    }
                }
                requestQueue.add(updateFlag);
            }
            catch (JSONException e) {
                connected = false;
                parsing_message = e.getLocalizedMessage();
                logger.log(Level.WARNING,e.getMessage(),e);
                goToDashboard();
            }
        },error -> {
            conn = false;
            connected = false;
            parsing_message = error.getLocalizedMessage();
            logger.log(Level.WARNING,error.getMessage(),error);
            goToDashboard();
        });

//        StringRequest getCompanyRequest = new StringRequest(Request.Method.GET, companyUrl, response -> {
//            conn = true;
//            try {
//                connected = true;
//                JSONObject jsonObject = new JSONObject(response);
//                String items = jsonObject.getString("items");
//                String count = jsonObject.getString("count");
//                if (!count.equals("0")) {
//                    JSONArray array = new JSONArray(items);
//                    for (int i = 0; i < array.length(); i++) {
//                        JSONObject companyInfo = array.getJSONObject(i);
//                        CompanyName = companyInfo.getString("cim_name");
//                    }
//                }
//                requestQueue.add(getSoftwareRequest);
//            }
//            catch (JSONException e) {
//                connected = false;
//                logger.log(Level.WARNING,e.getMessage(),e);
//                goToDashboard();
//            }
//        }, error -> {
//            conn = false;
//            connected = false;
//            logger.log(Level.WARNING,error.getMessage(),error);
//            goToDashboard();
//        });

//        StringRequest livLocFlReq = new StringRequest(Request.Method.GET, liveLocFlagUrl, response -> {
//            conn = true;
//            try {
//                connected = true;
//                JSONObject jsonObject = new JSONObject(response);
//                String items = jsonObject.getString("items");
//                String count = jsonObject.getString("count");
//                if (!count.equals("0")) {
//                    JSONArray array = new JSONArray(items);
//                    for (int i = 0; i < array.length(); i++) {
//                        JSONObject livLocFlInfo = array.getJSONObject(i);
//                        live_loc_flag = Integer.parseInt(livLocFlInfo.getString("emp_live_loc_tracker_flag")
//                                .equals("null") ? "0" : livLocFlInfo.getString("emp_live_loc_tracker_flag"));
//                        tracker_flag = livLocFlInfo.getString("emp_timeline_tracker_flag")
//                                .equals("null") ? "" :livLocFlInfo.getString("emp_timeline_tracker_flag");
//                    }
//                }
//                requestQueue.add(getCompanyRequest);
//            }
//            catch (JSONException e) {
//                connected = false;
//                logger.log(Level.WARNING,e.getMessage(),e);
//                goToDashboard();
//            }
//        }, error -> {
//            conn = false;
//            connected = false;
//            logger.log(Level.WARNING,error.getMessage(),error);
//            goToDashboard();
//        });

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
                requestQueue.add(getSoftwareRequest);
            }
            catch (JSONException e) {
                connected = false;
                parsing_message = e.getLocalizedMessage();
                logger.log(Level.WARNING,e.getMessage(),e);
                goToDashboard();
            }
        },error -> {
            conn = false;
            connected = false;
            parsing_message = error.getLocalizedMessage();
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
                parsing_message = e.getLocalizedMessage();
                logger.log(Level.WARNING,e.getMessage(),e);
                goToDashboard();
            }
        },error -> {
            conn = false;
            connected = false;
            parsing_message = error.getLocalizedMessage();
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
                        String jsm_code = desigInfo.getString("jsm_code")
                                .equals("null") ? "" : desigInfo.getString("jsm_code");
                        String temp_title = desigInfo.getString("temp_title")
                                .equals("null") ? "" : desigInfo.getString("temp_title");
                        String jsd_id = desigInfo.getString("jsd_id")
                                .equals("null") ? "" : desigInfo.getString("jsd_id");
                        String jsd_objective = desigInfo.getString("jsd_objective")
                                .equals("null") ? "" : desigInfo.getString("jsd_objective");
                        String dept_name = desigInfo.getString("dept_name")
                                .equals("null") ? "" : desigInfo.getString("dept_name");
                        String divm_name = desigInfo.getString("divm_name")
                                .equals("null") ? "" : desigInfo.getString("divm_name");
                        String desig_name = desigInfo.getString("desig_name")
                                .equals("null") ? "" : desigInfo.getString("desig_name");
                        String desig_priority = desigInfo.getString("desig_priority")
                                .equals("null") ? "" : desigInfo.getString("desig_priority");
                        String joiningdate = desigInfo.getString("joiningdate")
                                .equals("null") ? "" : desigInfo.getString("joiningdate");
                        String divm_id = desigInfo.getString("divm_id")
                                .equals("null") ? "" : desigInfo.getString("divm_id");

                        userDesignations.add(new UserDesignation(jsm_code,temp_title,jsd_id,jsd_objective,dept_name,divm_name,desig_name,desig_priority,joiningdate,divm_id));
                    }
                }
                requestQueue.add(attendAppReq);
            }
            catch (JSONException e) {
                connected = false;
                parsing_message = e.getLocalizedMessage();
                logger.log(Level.WARNING,e.getMessage(),e);
                goToDashboard();
            }
        }, error -> {
            conn = false;
            connected = false;
            parsing_message = error.getLocalizedMessage();
            logger.log(Level.WARNING,error.getMessage(),error);
            goToDashboard();
        });

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
                        String usr_name = userInfo.getString("emp_name")
                                .equals("null") ? "" : userInfo.getString("emp_name");
                        String usr_n_name = userInfo.getString("emp_nick_name")
                                .equals("null") ? "" : userInfo.getString("emp_nick_name");
                        String usr_email = userInfo.getString("emp_email")
                                .equals("null") ? "" : userInfo.getString("emp_email");
                        String usr_contact = userInfo.getString("emp_contact")
                                .equals("null") ? "" : userInfo.getString("emp_contact");
                        String usr_emp_id = userInfo.getString("emp_id")
                                .equals("null") ? "" : userInfo.getString("emp_id");
                        live_loc_flag = Integer.parseInt(userInfo.getString("emp_live_loc_tracker_flag")
                                .equals("null") ? "0" : userInfo.getString("emp_live_loc_tracker_flag"));
                        tracker_flag = userInfo.getString("emp_timeline_tracker_flag")
                                .equals("null") ? "" :userInfo.getString("emp_timeline_tracker_flag");
                        userInfoLists.add(new UserInfoList(emp_code,usr_name,usr_n_name,usr_email,usr_contact,usr_emp_id));
                    }
                }
                requestQueue.add(designationRequest);
            }
            catch (JSONException e) {
                connected = false;
                parsing_message = e.getLocalizedMessage();
                logger.log(Level.WARNING,e.getMessage(),e);
                goToDashboard();
            }
        }, error -> {
            conn = false;
            connected = false;
            parsing_message = error.getLocalizedMessage();
            logger.log(Level.WARNING,error.getMessage(),error);
            goToDashboard();
        });

        requestQueue.add(userInfoRequest);
    }

    public void goToDashboard() {
        waitProgress.dismiss();
        for (int i = 0; i < urls.size(); i++) {
            urls.get(i).setChecked(false);
        }
        if (conn) {
            if (connected) {
                if (infoConnected) {
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

                    editor1.putString(USER_NAME, userInfoLists.get(0).getEmp_code());
                    editor1.putString(USER_F_NAME, userInfoLists.get(0).getUser_name());
                    editor1.putString(USER_L_NAME, userInfoLists.get(0).getUser_nick_name());
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
                    editor1.putString(CENTER_API_FRONT, api_url_front);
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
                conn = false;
                connected = false;
                infoConnected = false;
            }
            else {
                if (parsing_message != null) {
                    if (parsing_message.isEmpty() || parsing_message.equals("null")) {
                        parsing_message = "Server problem or Internet not connected";
                    }
                }
                else {
                    parsing_message = "Server problem or Internet not connected";
                }
                MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(Login.this);
                alertDialogBuilder.setTitle("System Warning!")
                        .setIcon(R.drawable.hrm_new_round_icon_custom)
                        .setMessage("Message: "+parsing_message+".\n"+"Please try again.")
                        .setPositiveButton("Retry", (dialog, which) -> {
                            dynamicLoginCheck();
                            dialog.dismiss();
                        });
                AlertDialog alert = alertDialogBuilder.create();
                alert.show();
            }
        }
        else {
            if (parsing_message != null) {
                if (parsing_message.isEmpty() || parsing_message.equals("null")) {
                    parsing_message = "Server problem or Internet not connected";
                }
            }
            else {
                parsing_message = "Server problem or Internet not connected";
            }
            MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(Login.this);
            alertDialogBuilder.setTitle("System Warning!")
                    .setIcon(R.drawable.hrm_new_round_icon_custom)
                    .setMessage("Message: "+parsing_message+".\n"+"Please try again.")
                    .setPositiveButton("Retry", (dialog, which) -> {
                        dynamicLoginCheck();
                        dialog.dismiss();
                    });
            AlertDialog alert = alertDialogBuilder.create();
            alert.show();
        }
    }

    @Override
    public void onDismiss(int position) {
        api_url_front = centerLists.get(position).getCenter_api();
        emp_code = centerLists.get(position).getUser_emp_code();
        emp_id = centerLists.get(position).getUser_emp_id();
        CompanyName = centerLists.get(position).getCenter_name();
        waitProgress.show(getSupportFragmentManager(), "WaitBar");
        waitProgress.setCancelable(false);
        getUserDetails();
    }

}