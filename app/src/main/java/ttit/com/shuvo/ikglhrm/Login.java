package ttit.com.shuvo.ikglhrm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.media.Image;
import android.media.ImageReader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.rosemaryapp.amazingspinner.AmazingSpinner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

import oracle.sql.BLOB;
import ttit.com.shuvo.ikglhrm.MainPage.MainMenu;
import ttit.com.shuvo.ikglhrm.dashboard.Dashboard;

import static ttit.com.shuvo.ikglhrm.OracleConnection.createConnection;
import static ttit.com.shuvo.ikglhrm.OracleConnection.DEFAULT_USERNAME;

public class Login extends AppCompatActivity {

    TextInputEditText user;
    TextInputEditText pass;

    TextView login_failed;
    TextView softName;

    Button login;

    CheckBox checkBox;

    BLOB pic;
    ImageView imageView;

    String userName = "";
    String password = "";
    public static String CompanyName = "";
    public static String SoftwareName = "";
    public static int isApproved = 0;
    public static int isLeaveApproved = 0;

    WaitProgress waitProgress = new WaitProgress();
    private String message = null;
    private Boolean conn = false;
    private Boolean infoConnected = false;
    private Boolean adminConnected = false;
    private Boolean connected = false;

    private Boolean getConn = false;
    private Boolean getConnected = false;

    private Connection connection;
    AmazingSpinner database;


    public static final String LOGIN_ACTIVITY_FILE = "LOGIN_ACTIVITY_FILE";

    public static final String USER_NAME = "USER_NAME";
    public static final String USER_F_NAME = "USER_F_NAME";
    public static final String USER_L_NAME = "USER_L_NAME";
    public static final String EMAIL = "EMAIL";
    public static final String CONTACT = "CONTACT";
    public static final String EMP_ID_LOGIN = "EMP_ID";

    public static final String JSM_CODE = "JSM_CODE";
    public static final String JSM_NAME = "JSM_NAME";
    public static final String JSD_ID_LOGIN = "JSD_ID";
    public static final String JSD_OBJECTIVE = "JSD_OBJECTIVE";
    public static final String DEPT_NAME = "DEPT_NAME";
    public static final String DIV_NAME = "DIV_NAME";
    public static final String DESG_NAME = "DESG_NAME";
    public static final String DESG_PRIORITY = "DESG_PRIORITY";
    public static final String JOINING_DATE = "JOINING_DATE";
    public static final String DIV_ID = "DIV_ID";
    public static final String LOGIN_TF = "TRUE_FALSE";

    public static final String IS_ATT_APPROVED = "IS_ATT_APPROVED";
    public static final String IS_LEAVE_APPROVED = "IS_LEAVE_APPROVED";
    public static final String COMPANY = "COMPANY";
    public static final String SOFTWARE = "SOFTWARE";
    public static final String LIVE_FLAG = "LIVE_FLAG";
    public static final String DATABASE_NAME = "DATABASE_NAME";

    public static final String MyPREFERENCES = "UserPass" ;
    public static final String user_emp_code = "nameKey";
    public static final String user_password = "passKey";
    public static final String checked = "trueFalse";

    SharedPreferences sharedpreferences;

    SharedPreferences sharedLogin;

    String getUserName = "";
    String getPassword = "";
    boolean getChecked = false;

    String userId = "";
    public static ArrayList<UserInfoList> userInfoLists;
    public static ArrayList<UserDesignation> userDesignations;

    String emp_id = "";
    int live_loc_flag = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window w = getWindow();
//           // w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        }
//        if (Build.VERSION.SDK_INT < 16) {
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        }
//        View decorView = getWindow().getDecorView();
//// Hide the status bar.
//        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
//        decorView.setSystemUiVisibility(uiOptions);
        setContentView(R.layout.activity_login);



        userInfoLists = new ArrayList<>();
        userDesignations = new ArrayList<>();

        softName = findViewById(R.id.name_of_soft_login);
        imageView = findViewById(R.id.image_from_db);
        user = findViewById(R.id.user_name_given);
        pass = findViewById(R.id.password_given);
        checkBox = findViewById(R.id.remember_checkbox);

        login_failed = findViewById(R.id.email_pass_miss);

        login = findViewById(R.id.login_button);
        database = findViewById(R.id.database_spinner);

        sharedLogin = getSharedPreferences(LOGIN_ACTIVITY_FILE,MODE_PRIVATE);

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

        database.setText(DEFAULT_USERNAME);
        ArrayList<String> type = new ArrayList<>();
        type.add("IKGL");
        type.add("TTRAMS");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);

        database.setAdapter(arrayAdapter);

        database.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DEFAULT_USERNAME = parent.getItemAtPosition(position).toString();
                new Check().execute();
            }
        });


        new Check().execute();

        pass.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
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
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                closeKeyBoard();

                login_failed.setVisibility(View.INVISIBLE);
                userName = user.getText().toString();
                password = pass.getText().toString();

                if (!userName.isEmpty() && !password.isEmpty()) {
                    if (!userName.equals("admin")) {
                        new CheckLogin().execute();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Admin can not login to this app", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Please Give User Name and Password", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }




//    private void enableFileAccess() {
//
//        if (Build.VERSION.SDK_INT >= 23) {
//            int REQUEST_CODE_PERMISSION_STORAGE = 100;
//            String[] permission = {
//                    Manifest.permission.READ_EXTERNAL_STORAGE,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE
//            };
//
//            for (String str : permission) {
//                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
//                    this.requestPermissions(permission, REQUEST_CODE_PERMISSION_STORAGE);
//                    return;
//                }
//
//            }
//
////            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
////                enableGPS();
////            }
//
//
//        }
//    }



//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == FINE_LOCATION_ACCESS_REQUEST_CODE) {
//            Log.i("Ekhane", "7");
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Log.i("Ekhane", "8");
//                // we have the permission
//                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
//                    // TODO: Consider calling
//                    //    ActivityCompat#requestPermissions
//                    // here to request the missing permissions, and then overriding
//                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                    //                                          int[] grantResults)
//                    // to handle the case where the user grants the permission. See the documentation
//                    // for ActivityCompat#requestPermissions for more details.
//                    Log.i("Ekhane", "9");
//                    return;
//                }
//
//                enableFileAccess();
//
//            }
//        }
//    }



    @Override
    public void onBackPressed () {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("EXIT!")
                .setMessage("Do you want to EXIT?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        System.exit(0);
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

    public boolean isConnected () {
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

    public boolean isOnline () {

        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }

    public class CheckLogin extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            waitProgress.show(getSupportFragmentManager(), "WaitBar");
            waitProgress.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isConnected() && isOnline()) {

                LoginQuery();
                if (connected) {
                    conn = true;
                    message = "Internet Connected";
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

                if (!userId.equals("-1")) {
                    if (adminConnected) {
                        Toast.makeText(getApplicationContext(),"Admin can not access this app.",Toast.LENGTH_SHORT).show();
                    }
                    else {
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
                            }

//                        user.setText("");
//                        pass.setText("");
//                        checkBox.setChecked(false);



                            SharedPreferences.Editor editor1 = sharedLogin.edit();
                            editor1.remove(USER_NAME);
                            editor1.remove(USER_F_NAME);
                            editor1.remove(USER_L_NAME);
                            editor1.remove(EMAIL);
                            editor1.remove(CONTACT);
                            editor1.remove(EMP_ID_LOGIN);

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
                            editor1.remove(DATABASE_NAME);

                            editor1.putString(USER_NAME, userInfoLists.get(0).getUserName());
                            editor1.putString(USER_F_NAME, userInfoLists.get(0).getUser_fname());
                            editor1.putString(USER_L_NAME, userInfoLists.get(0).getUser_lname());
                            editor1.putString(EMAIL, userInfoLists.get(0).getEmail());
                            editor1.putString(CONTACT, userInfoLists.get(0).getContact());
                            editor1.putString(EMP_ID_LOGIN, userInfoLists.get(0).getEmp_id());

                            if (userDesignations.size() != 0) {
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

//                        editor1.putString(JSM_CODE, userDesignations.get(0).getJsm_code());
//                        editor1.putString(JSM_NAME, userDesignations.get(0).getJsm_name());
//                        editor1.putString(JSD_ID_LOGIN, userDesignations.get(0).getJsd_id());
//                        editor1.putString(JSD_OBJECTIVE, userDesignations.get(0).getJsd_objective());
//                        editor1.putString(DEPT_NAME, userDesignations.get(0).getDept_name());
//                        editor1.putString(DIV_NAME, userDesignations.get(0).getDiv_name());
//                        editor1.putString(DESG_NAME, userDesignations.get(0).getDesg_name());
//                        editor1.putString(DESG_PRIORITY, userDesignations.get(0).getDesg_priority());
//                        editor1.putString(JOINING_DATE, userDesignations.get(0).getJoining_date());
//                        editor1.putString(DIV_ID, userDesignations.get(0).getDiv_id());
                            editor1.putBoolean(LOGIN_TF,true);

                            editor1.putInt(IS_ATT_APPROVED, isApproved);
                            editor1.putInt(IS_LEAVE_APPROVED, isLeaveApproved);
                            editor1.putString(COMPANY, CompanyName);
                            editor1.putString(SOFTWARE,SoftwareName);
                            editor1.putInt(LIVE_FLAG,live_loc_flag);
                            editor1.putString(DATABASE_NAME,DEFAULT_USERNAME);
                            editor1.apply();
                            editor1.commit();


                            Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                            startActivity(intent);
                            finish();

                        } else {
                            new CheckLogin().execute();
                        }
                    }

                } else {

                    login_failed.setVisibility(View.VISIBLE);
                }
                conn = false;
                connected = false;
                adminConnected = false;
                infoConnected = false;


            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(Login.this)
                        .setMessage("Please Check Your Internet Connection")
                        .setPositiveButton("Retry", null)
                        .show();

//                dialog.setCancelable(false);
//                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new CheckLogin().execute();
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

            waitProgress.show(getSupportFragmentManager(), "WaitBar");
            waitProgress.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isConnected() && isOnline()) {

                GettingData();
                if (getConnected) {
                    getConn = true;
                    message = "Internet Connected";
                }


            } else {
                getConn = false;
                message = "Not Connected";
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            waitProgress.dismiss();
            if (getConn) {

                if (CompanyName == null) {
                    CompanyName = "No Name Found";
                }

                if (SoftwareName == null) {
                    SoftwareName = "Name of App";
                }
                softName.setText(CompanyName);

                getConnected = false;
                getConn = false;


            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(Login.this)
                        .setMessage("Please Check Your Internet Connection")
                        .setPositiveButton("Retry", null)
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
            }
        }
    }

    public void LoginQuery () {


        try {
            this.connection = createConnection();
            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();

            userInfoLists = new ArrayList<>();
            userDesignations = new ArrayList<>();
            isApproved = 0;
            isLeaveApproved = 0;

            Statement stmt = connection.createStatement();
            StringBuffer stringBuffer = new StringBuffer();
//            StringBuffer stringBuffer1 = new StringBuffer();
//            StringBuffer stringBuffer2 = new StringBuffer();
//            StringBuffer stringBuffer3 = new StringBuffer();
//            StringBuffer stringBuffer4 = new StringBuffer();
//            StringBuffer stringBuffer5 = new StringBuffer();
//            StringBuffer stringBuffer6 = new StringBuffer();
//            StringBuffer stringBuffer7 = new StringBuffer();


            ResultSet rs = stmt.executeQuery("select VALIDATE_USER_DB('" + userName + "',HAMID_ENCRYPT_DESCRIPTION_PACK.HEDP_ENCRYPT('" + password + "')) val from dual\n");


            while (rs.next()) {
                stringBuffer.append("USER ID: " + rs.getString(1) + "\n");
                userId = rs.getString(1);

            }

            if (!userId.equals("-1")) {

                String empCode = "";
                ResultSet resEmpCode = stmt.executeQuery("select COM_PACK.GET_EMP_CODE_BY_EMP_ID(COM_PACK.GET_EMPLOYEE_ID_BY_USER('"+userName+"')) valu from dual");
                while (resEmpCode.next()) {
                    empCode = resEmpCode.getString(1);
                }
                resEmpCode.close();

                if (empCode.equals("0000")) {
                    adminConnected = true;
                }
                else {
                    adminConnected = false;
                    ResultSet resultSet = stmt.executeQuery("Select USR_NAME, USR_FNAME, USR_LNAME, USR_EMAIL, USR_CONTACT, USR_EMP_ID FROM ISP_USER\n" +
                            "where USR_ID = " + userId + "\n");

                    while (resultSet.next()) {
                        emp_id = resultSet.getString(6);
                        userInfoLists.add(new UserInfoList(empCode, resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5), resultSet.getString(6)));
                    }

                    ResultSet resultSet1 = stmt.executeQuery("SELECT DISTINCT JOB_SETUP_MST.JSM_CODE, JOB_SETUP_MST.JSM_NAME TEMP_TITLE, \n" +
                            "JOB_SETUP_DTL.JSD_ID, JOB_SETUP_DTL.JSD_OBJECTIVE, DEPT_MST.DEPT_NAME, \n" +
                            "DIVISION_MST.DIVM_NAME, DESIG_MST.DESIG_NAME, DESIG_MST.DESIG_PRIORITY, (Select TO_CHAR(MAX(EMP_JOB_HISTORY.JOB_ACTUAL_DATE),'DD-MON-YYYY') from EMP_JOB_HISTORY) as JOININGDATE, DIVISION_MST.DIVM_ID \n" +
                            "FROM JOB_SETUP_MST, JOB_SETUP_DTL, DEPT_MST, DIVISION_MST, DESIG_MST, EMP_JOB_HISTORY\n" +
                            "WHERE ((JOB_SETUP_DTL.JSD_JSM_ID = JOB_SETUP_MST.JSM_ID)\n" +
                            " AND (JOB_SETUP_MST.JSM_DIVM_ID = DIVISION_MST.DIVM_ID)\n" +
                            " AND (DEPT_MST.DEPT_ID = JOB_SETUP_MST.JSM_DEPT_ID)\n" +
                            " AND (JOB_SETUP_MST.JSM_DESIG_ID = DESIG_MST.DESIG_ID))\n" +
                            " AND JOB_SETUP_DTL.JSD_ID = (SELECT JOB_JSD_ID\n" +
                            "                            FROM EMP_JOB_HISTORY\n" +
                            "                            WHERE JOB_ID = (SELECT MAX(JOB_ID) FROM EMP_JOB_HISTORY WHERE JOB_EMP_ID = " + emp_id + "))" +
                            "AND EMP_JOB_HISTORY.JOB_JSD_ID = JOB_SETUP_DTL.JSD_ID");

                    while (resultSet1.next()) {

                        userDesignations.add(new UserDesignation(resultSet1.getString(1), resultSet1.getString(2), resultSet1.getString(3), resultSet1.getString(4), resultSet1.getString(5), resultSet1.getString(6), resultSet1.getString(7), resultSet1.getString(8), resultSet1.getString(9), resultSet1.getString(10)));
                    }

                    ResultSet resultSet2 = stmt.executeQuery("SELECT COUNT (1) VAL\n" +
                            "  FROM DAILY_ATTEN_REQ_MST\n" +
                            " WHERE DARM_APP_CODE IN\n" +
                            "          (SELECT DAAHL_APP_CODE\n" +
                            "             FROM DAILY_ATTEN_APP_HIERARCHY_LOG, DAILY_ATTEN_REQ_MST\n" +
                            "            WHERE     DAILY_ATTEN_APP_HIERARCHY_LOG.DAAHL_APP_CODE =\n" +
                            "                         DAILY_ATTEN_REQ_MST.DARM_APP_CODE\n" +
                            "                  AND NVL (DAILY_ATTEN_REQ_MST.DARM_APPROVED, 0) = 0\n" +
                            "                  AND (DAILY_ATTEN_APP_HIERARCHY_LOG.DAAHL_APPROVER_BAND_ID =\n" +
                            "                          (SELECT EMP_ID\n" +
                            "                             FROM EMP_MST\n" +
                            "                            WHERE EMP_CODE = '" + empCode + "')))");

                    while (resultSet2.next()) {
                        isApproved = resultSet2.getInt(1);
                        System.out.println(isApproved);
                    }
                    System.out.println("isApproved: "+isApproved);

                    ResultSet resultSet3 = stmt.executeQuery("SELECT count(DISTINCT LA_APP_CODE)\n" +
                            "  FROM (SELECT LA.LA_APP_CODE\n" +
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
                            "                                                      '" + empCode + "')\n" +
                            "                                                || ',') > 0))\n" +
                            "               AND NVL (LA.LA_APPROVED, 0) = 0\n" +
                            "        UNION ALL\n" +
                            "        SELECT LA.LA_APP_CODE\n" +
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
                            "                                                  ',' || '" + empCode + "' || ',') > 0)))\n" +
                            "               AND NVL (LA.LA_APPROVED, 0) = 0)\n" +
                            " WHERE LA_APP_CODE NOT IN\n" +
                            "       (SELECT CASE\n" +
                            "          WHEN COM_PACK.GET_EMPLOYEE_ID_BY_USER ('" + empCode + "') =\n" +
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
                            "                  GROUP BY LAD_LA_ID))");

                    while (resultSet3.next()) {
                        isLeaveApproved = resultSet3.getInt(1);
                        System.out.println("LEAVE PRRR: " + isLeaveApproved);
                    }

                    ResultSet resultSet4 = stmt.executeQuery("select EMP_LIVE_LOC_TRACKER_FLAG from EMP_MST WHERE EMP_CODE = '"+empCode+"'");

                    while (resultSet4.next()) {
                        live_loc_flag = resultSet4.getInt(1);
                        System.out.println("LIVE LOCATION FLAG: "+ live_loc_flag);
                    }
                    infoConnected = true;
                }
            }
            System.out.println(stringBuffer);


            connected = true;

            connection.close();

        } catch (Exception e) {

            //   Toast.makeText(MainActivity.this, ""+e,Toast.LENGTH_LONG).show();
            Log.i("ERRRRR", e.getLocalizedMessage());
            e.printStackTrace();
        }

    }

    public void GettingData () {
        try {
            this.connection = createConnection();
            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();


            Statement stmt = connection.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT CIM_NAME--,CIM_LOGO_APPS \n" +
                    "FROM COMPANY_INFO_MST\n");

            while (rs.next()) {
                CompanyName = rs.getString(1);
                System.out.println(CompanyName);

//                pic = (BLOB) rs.getBlob(2);
//                System.out.println(String.valueOf(pic));
//
//                byte[] barr = pic.getBytes(1,(int)pic.length());
//                System.out.println(Arrays.toString(barr));
//                String encodedImageString = Base64.encodeToString(barr, Base64.DEFAULT);
//                byte[] bytes = Base64.decode(encodedImageString,Base64.DEFAULT);
//                System.out.println(Arrays.toString(bytes));

//                String bytes = rs.getString(2);

//                System.out.println(bytes);
//                byte[] b = bytes.getBytes();
//                System.out.println(Arrays.toString(b));

                // Blob image = rs.getBlob(2);
//                Bitmap bmp= BitmapFactory.decodeByteArray(bytes, 0 , bytes.length);
//                System.out.println(bmp);
//                imageView.setImageBitmap(bmp);

//                File myExternalFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"Logo.jpeg");
//                FileOutputStream fos = new FileOutputStream(myExternalFile);//Get OutputStream for NewFile Location
//                fos.write(barr);
//                fos.close();
//
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inMutable = true;
//                options.inJustDecodeBounds = true;
//                Bitmap bitmap = BitmapFactory.decodeByteArray(barr,0,barr.length);
//                System.out.println(bitmap);
//                imageView.setImageBitmap(bitmap);
//
//                InputStream inputStream = pic.getBinaryStream();
//                System.out.println(inputStream);
//
//                bitmap = BitmapFactory.decodeStream(inputStream);
//                System.out.println(bitmap);


            }


            ResultSet resultSet = stmt.executeQuery(" select LIC_SOFTWARE_NAME from isp_runauto where rownum=1\n");

            while (resultSet.next()) {
                SoftwareName = resultSet.getString(1);
                System.out.println(SoftwareName);
            }


//            CallableStatement callableStatement = connection.prepareCall("begin ? := GET_LEAVE_BALANCE(?,?,?); end;");
//            callableStatement.setInt(2,2008);
//            callableStatement.setString(3, "16-AUG-21");
//            callableStatement.setString(4,"EL");
//            callableStatement.registerOutParameter(1,Types.INTEGER);
//            callableStatement.execute();
//            int ddd = callableStatement.getInt(1);
//            System.out.println(ddd);
//            callableStatement.close();
//
//            CallableStatement callableStatement1 = connection.prepareCall("begin ? := GET_CONSUMED_LEAVE_BY_MONTH(?,?,?); end;");
//            callableStatement1.setInt(2,2008);
//            callableStatement1.setString(3, "01-AUG-21");
//            callableStatement1.setString(4,"EL");
//            callableStatement1.registerOutParameter(1,Types.INTEGER);
//            callableStatement1.execute();
//            int dddd = callableStatement1.getInt(1);
//            System.out.println(dddd);
//            callableStatement1.close();


            getConnected = true;
            connection.close();

        } catch (Exception e) {

            //   Toast.makeText(MainActivity.this, ""+e,Toast.LENGTH_LONG).show();
            Log.i("ERRRRR", e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

}