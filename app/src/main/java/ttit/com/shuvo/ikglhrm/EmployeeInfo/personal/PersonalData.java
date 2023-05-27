package ttit.com.shuvo.ikglhrm.EmployeeInfo.personal;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ttit.com.shuvo.ikglhrm.Login;
import ttit.com.shuvo.ikglhrm.MainPage.MainMenu;
import ttit.com.shuvo.ikglhrm.R;
import ttit.com.shuvo.ikglhrm.UserDesignation;
import ttit.com.shuvo.ikglhrm.UserInfoList;
import ttit.com.shuvo.ikglhrm.WaitProgress;
import ttit.com.shuvo.ikglhrm.attendance.report.AttendanceReport;

import static ttit.com.shuvo.ikglhrm.Login.userInfoLists;
import static ttit.com.shuvo.ikglhrm.OracleConnection.createConnection;

public class PersonalData extends AppCompatActivity {

//    Spinner bloodGroup;
//    Spinner groupDisplay;
//    Spinner religion;
//    Spinner sex;
//    Spinner marital_status;
//    Spinner mailing_address;
    EditText bloodGroup;
    EditText groupDisplay;
    EditText religion;
    EditText sex;
    EditText marital_status;
    EditText mailing_address;

    public ArrayList<String> bg;
    public ArrayList<String> gd;
    public ArrayList<String> rel;
    public ArrayList<String> se;
    public ArrayList<String> ms;
    public ArrayList<String> ma;

    ArrayAdapter<String> bgArrayAdapter;
    ArrayAdapter<String> gdArrayAdapter;
    ArrayAdapter<String> relArrayAdapter;
    ArrayAdapter<String> seArrayAdapter;
    ArrayAdapter<String> msArrayAdapter;
    ArrayAdapter<String> maArrayAdapter;

    EditText nameP;
    EditText nameBP;
    EditText revision;
    EditText effectedDate;
    EditText dateOfBirth;
    EditText nationality;
    EditText familyName;
    EditText callingName;
    EditText email;
    EditText personalAdd;
    EditText permanentAdd;
    EditText personalAddBangla;
    EditText permanentAddBangla;

    public static ArrayList<EMPInformation> empInformations;

    WaitProgress waitProgress = new WaitProgress();
    private String message = null;
    private Boolean conn = false;
    private Boolean infoConnected = false;
    private Boolean connected = false;

    private Connection connection;

    String emp_id = "";

    String empName = "";
    String banglaName = "";

    Button ok;

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
        window.setStatusBarColor(ContextCompat.getColor(PersonalData.this,R.color.secondaryColor));

        setContentView(R.layout.activity_personal_data);

        emp_id = userInfoLists.get(0).getEmp_id();

        bloodGroup = findViewById(R.id.spinner_blood);
        groupDisplay = findViewById(R.id.spinner_gr_display);
        religion = findViewById(R.id.spineer_religion);
        sex = findViewById(R.id.spinner_sex);
        marital_status = findViewById(R.id.spinner_marital_status);
        mailing_address = findViewById(R.id.spinner_mailing_address);

        nameP = findViewById(R.id.emp_name_p);
        nameBP = findViewById(R.id.bangla_name_p);
        revision = findViewById(R.id.revision_no);
        effectedDate = findViewById(R.id.effected_da);
        dateOfBirth = findViewById(R.id.birth);
        nationality = findViewById(R.id.nationality);
        familyName = findViewById(R.id.family_name);
        callingName = findViewById(R.id.calling_name);
        email = findViewById(R.id.personal_email);
        personalAdd = findViewById(R.id.present_address);
        permanentAdd = findViewById(R.id.permanent_address);
        personalAddBangla = findViewById(R.id.present_address_bangla);
        permanentAddBangla = findViewById(R.id.permanent_address_bangla);

        ok = findViewById(R.id.finish_button);

//        bloodGroup.setEnabled(false);
//        groupDisplay.setEnabled(false);
//        religion.setEnabled(false);
//        sex.setEnabled(false);
//        marital_status.setEnabled(false);
//        mailing_address.setEnabled(false);

        bg = new ArrayList<>();
        gd = new ArrayList<>();
        se = new ArrayList<>();
        rel = new ArrayList<>();
        ms = new ArrayList<>();
        ma = new ArrayList<>();

        empInformations = new ArrayList<>();

        bg.add("Select Blood Group");
        bg.add("A+");
        bg.add("A-");
        bg.add("B+");
        bg.add("B-");
        bg.add("AB+");
        bg.add("AB-");
        bg.add("O+");
        bg.add("O-");

        gd.add("Publish");
        gd.add("Not Publish");

        se.add("Select Sex");
        se.add("Male");
        se.add("Female");

        rel.add("Select Religion");
        rel.add("Islam");
        rel.add("Christian");
        rel.add("Buddhist");
        rel.add("Hindu");
        rel.add("Others");

        ms.add("Select Marital Status");
        ms.add("Married");
        ms.add("Single");
        ms.add("Divorced");
        ms.add("Widowed");
        ms.add("Widower");

        ma.add("Present Address");
        ma.add("Permanent Address");

        String firstname = userInfoLists.get(0).getUser_fname();
        String lastName = userInfoLists.get(0).getUser_lname();
        if (firstname == null) {
            firstname = "";
        }
        if (lastName == null) {
            lastName = "";
        }
        empName = firstname+" "+lastName;

        nameP.setText(empName);


// BLOOD GROUP
//        bgArrayAdapter = new ArrayAdapter<String>(
//                this,R.layout.item_country,bg){
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
//        bloodGroup.setGravity(Gravity.END);
//        bgArrayAdapter.setDropDownViewResource(R.layout.item_country);
//        bloodGroup.setAdapter(bgArrayAdapter);
//
//        // SEX
//        seArrayAdapter = new ArrayAdapter<String>(
//                this,R.layout.item_country,se){
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
//        sex.setGravity(Gravity.END);
//        seArrayAdapter.setDropDownViewResource(R.layout.item_country);
//        sex.setAdapter(seArrayAdapter);
//
//
//        // RELIGION
//        relArrayAdapter = new ArrayAdapter<String>(
//                this,R.layout.item_country,rel){
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
//        religion.setGravity(Gravity.END);
//        relArrayAdapter.setDropDownViewResource(R.layout.item_country);
//        religion.setAdapter(relArrayAdapter);
//
//        // MARITAL STATUS
//        msArrayAdapter = new ArrayAdapter<String>(
//                this,R.layout.item_country,ms){
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
//        marital_status.setGravity(Gravity.END);
//        msArrayAdapter.setDropDownViewResource(R.layout.item_country);
//        marital_status.setAdapter(msArrayAdapter);
//
//
//        // GROUP DISPLAY
//
//        gdArrayAdapter = new ArrayAdapter<String>(
//                this,R.layout.item_country,gd){
//            @Override
//            public boolean isEnabled(int position){
////                if(position == 0)
////                {
////                    // Disable the first item from Spinner
////                    // First item will be use for hint
////                    return false;
////                }
////                else
////                {
////                    return true;
////                }
//                return true;
//            }
//            @Override
//            public View getDropDownView(int position, View convertView,
//                                        ViewGroup parent) {
//                View view = super.getDropDownView(position, convertView, parent);
//                TextView tv = (TextView) view.findViewById(R.id.tvCountry);
////                if(position == 0){
////                    // Set the hint text color gray
////                    tv.setTextColor(Color.GRAY);
////                }
////                else {
////                    tv.setTextColor(Color.BLACK);
////                }
//                tv.setTextColor(Color.BLACK);
//                return view;
//            }
//        };
//        groupDisplay.setGravity(Gravity.END);
//        gdArrayAdapter.setDropDownViewResource(R.layout.item_country);
//        groupDisplay.setAdapter(gdArrayAdapter);
//
//
//        // MAILING ADDRESS
//        maArrayAdapter = new ArrayAdapter<String>(
//                this,R.layout.item_country,ma){
//            @Override
//            public boolean isEnabled(int position){
////                if(position == 0)
////                {
////                    // Disable the first item from Spinner
////                    // First item will be use for hint
////                    return false;
////                }
////                else
////                {
////                    return true;
////                }
//                return true;
//            }
//            @Override
//            public View getDropDownView(int position, View convertView,
//                                        ViewGroup parent) {
//                View view = super.getDropDownView(position, convertView, parent);
//                TextView tv = (TextView) view.findViewById(R.id.tvCountry);
////                if(position == 0){
////                    // Set the hint text color gray
////                    tv.setTextColor(Color.GRAY);
////                }
////                else {
////                    tv.setTextColor(Color.BLACK);
////                }
//                tv.setTextColor(Color.BLACK);
//                return view;
//            }
//        };
//        mailing_address.setGravity(Gravity.END);
//        maArrayAdapter.setDropDownViewResource(R.layout.item_country);
//        mailing_address.setAdapter(maArrayAdapter);

        new Check().execute();


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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

                personalDataQuery();
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
                if (empInformations.size() != 0) {
                    for (int i = 0 ; i < empInformations.size(); i++) {

                        String dateC = empInformations.get(i).getEffectedDAte();
//                        System.out.println(dateC);
//
//                        final String OLD_FORMAT = "yyyy-MM-dd";
//                        final String NEW_FORMAT = "dd-MMM-yyyy";
//
//                        String oldDateString = dateC;
//                        String newDateString;
//
//                        SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT,Locale.getDefault());
//                        Date d = null;
//                        try {
//                            d = sdf.parse(oldDateString);
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//                        sdf.applyPattern(NEW_FORMAT);
//                        //sdf.applyPattern(NEW_FORMAT);
//                        newDateString = sdf.format(d);
//                        System.out.println(newDateString);
                        effectedDate.setText(dateC);

                        //String datt = empInformations.get(i).getBirth_date().substring(0, 10);
                        String datt = empInformations.get(i).getBirth_date();
//                        System.out.println(datt);
//                        oldDateString = datt;
//
//
//                        SimpleDateFormat df = new SimpleDateFormat(OLD_FORMAT,Locale.getDefault());
//
//                        try {
//                            d = df.parse(oldDateString);
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//                        df.applyPattern(NEW_FORMAT);
//                        newDateString = df.format(d);
//                        System.out.println(newDateString);
                        dateOfBirth.setText(datt);


                        revision.setText(empInformations.get(i).getRevision());

                        if (empInformations.get(i).getBlood_grp() != null) {
//                            bloodGroup.setSelection(Integer.parseInt(empInformations.get(i).getBlood_grp()));
                            bloodGroup.setText(bg.get(Integer.parseInt(empInformations.get(i).getBlood_grp())));
                        }
                        if (empInformations.get(i).getGrop_dis() != null) {
//                            groupDisplay.setSelection(Integer.parseInt(empInformations.get(i).getGrop_dis()));
                            groupDisplay.setText(gd.get(Integer.parseInt(empInformations.get(i).getGrop_dis())));
                        }
                        if (empInformations.get(i).getReligion() != null) {
//                            religion.setSelection(Integer.parseInt(empInformations.get(i).getReligion()));
                            religion.setText(rel.get(Integer.parseInt(empInformations.get(i).getReligion())));
                        }

                        nationality.setText(empInformations.get(i).getNational());

                        if (empInformations.get(i).getSex() != null) {
//                            sex.setSelection(Integer.parseInt(empInformations.get(i).getSex()));
                            sex.setText(se.get(Integer.parseInt(empInformations.get(i).getSex())));
                        }

                        familyName.setText(empInformations.get(i).getFamilyName());
                        callingName.setText(empInformations.get(i).getCallingName());
                        email.setText(empInformations.get(i).getEmail());
                        personalAdd.setText(empInformations.get(i).getPrsentAdd());
                        permanentAdd.setText(empInformations.get(i).getPerManentAdd());

                        if (empInformations.get(i).getMarital_status() != null) {
//                            marital_status.setSelection(Integer.parseInt(empInformations.get(i).getMarital_status()));
                            marital_status.setText(ms.get(Integer.parseInt(empInformations.get(i).getMarital_status())));
                        }

                        personalAddBangla.setText(empInformations.get(i).getPresent_bangla());
                        permanentAddBangla.setText(empInformations.get(i).getPermanent_bangla());

                        if (empInformations.get(i).getMailingAdd() != null) {
//                            mailing_address.setSelection(Integer.parseInt(empInformations.get(i).getMailingAdd()));
                            mailing_address.setText(ma.get(Integer.parseInt(empInformations.get(i).getMailingAdd())));
                        }

                        if (banglaName == null) {
                            banglaName = "";
                        }
                        nameBP.setText(banglaName);
                    }
                }





            }else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(PersonalData.this)
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
                        finish();
                    }
                });
            }
        }
    }

    public void personalDataQuery() {


        try {
            this.connection = createConnection();
            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();


            Statement stmt = connection.createStatement();

            banglaName = "";
            empInformations = new ArrayList<>();
//            StringBuffer stringBuffer = new StringBuffer();
//            StringBuffer stringBuffer1 = new StringBuffer();
//            StringBuffer stringBuffer2 = new StringBuffer();
//            StringBuffer stringBuffer3 = new StringBuffer();
//            StringBuffer stringBuffer4 = new StringBuffer();
//            StringBuffer stringBuffer5 = new StringBuffer();
//            StringBuffer stringBuffer6 = new StringBuffer();
//            StringBuffer stringBuffer7 = new StringBuffer();



            ResultSet rs=stmt.executeQuery("SELECT PI_RIVISION_CODE, TO_CHAR(TO_DATE(PI_EFF_DATE),'DD-MON-YYYY') PI_EFF_DATE, PI_BLOOD_GP, PI_BLOOD_GP_PUBLISH_FLAG, \n" +
                    "TO_CHAR(TO_DATE(PI_DOB),'DD-MON-YYYY') PI_DOB, PI_RELIGION, PI_NATIONALITY, PI_SEX, PI_FAMILY_NAME, PI_CALLING_NAME, PI_EMAIL, PI_PRE_ADDRESS, \n" +
                    "PI_PAR_ADDRESS, PI_MARITAL_STATUS, PI_PRE_ADDRESS_BAN, PI_PAR_ADDRESS_BAN, PI_ADDRESS_FLAG \n" +
                    "FROM EMP_PERSONAL_INFO WHERE PI_EMP_ID = "+emp_id+"");


            while(rs.next()) {

                empInformations.add(new EMPInformation(rs.getString(1),rs.getString(2),rs.getString(3),
                        rs.getString(4), rs.getString(5),rs.getString(6),rs.getString(7),
                        rs.getString(8),rs.getString(9),rs.getString(10),rs.getString(11),
                        rs.getString(12),rs.getString(13),rs.getString(14),rs.getString(15),
                        rs.getString(16),rs.getString(17)));

            }

            ResultSet resultSet = stmt.executeQuery("SELECT EMP_MST.EMP_NAME_BN\n" +
                    "  FROM EMP_MST\n" +
                    " WHERE EMP_ID = "+emp_id+"");

            while (resultSet.next()) {
                banglaName = resultSet.getString(1);
            }
//            System.out.println(stringBuffer);
//            System.out.println(stringBuffer1);
//            System.out.println(stringBuffer2);
//            System.out.println(stringBuffer3);
//            System.out.println(stringBuffer4);
//            System.out.println(stringBuffer5);
//            System.out.println(stringBuffer6);
//            System.out.println(stringBuffer7);



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