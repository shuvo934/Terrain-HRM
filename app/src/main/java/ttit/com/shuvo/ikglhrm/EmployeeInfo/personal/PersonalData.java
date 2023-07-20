package ttit.com.shuvo.ikglhrm.EmployeeInfo.personal;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import java.util.ArrayList;

import ttit.com.shuvo.ikglhrm.R;
import ttit.com.shuvo.ikglhrm.WaitProgress;

import static ttit.com.shuvo.ikglhrm.Login.userInfoLists;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
//    private String message = null;
    private Boolean conn = false;
//    private Boolean infoConnected = false;
    private Boolean connected = false;

//    private Connection connection;

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

//        new Check().execute();
        getEmpInformation();


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
//                personalDataQuery();
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
//                if (empInformations.size() != 0) {
//                    for (int i = 0 ; i < empInformations.size(); i++) {
//
//                        String dateC = empInformations.get(i).getEffectedDAte();
////                        System.out.println(dateC);
////
////                        final String OLD_FORMAT = "yyyy-MM-dd";
////                        final String NEW_FORMAT = "dd-MMM-yyyy";
////
////                        String oldDateString = dateC;
////                        String newDateString;
////
////                        SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT,Locale.getDefault());
////                        Date d = null;
////                        try {
////                            d = sdf.parse(oldDateString);
////                        } catch (ParseException e) {
////                            e.printStackTrace();
////                        }
////                        sdf.applyPattern(NEW_FORMAT);
////                        //sdf.applyPattern(NEW_FORMAT);
////                        newDateString = sdf.format(d);
////                        System.out.println(newDateString);
//                        effectedDate.setText(dateC);
//
//                        //String datt = empInformations.get(i).getBirth_date().substring(0, 10);
//                        String datt = empInformations.get(i).getBirth_date();
////                        System.out.println(datt);
////                        oldDateString = datt;
////
////
////                        SimpleDateFormat df = new SimpleDateFormat(OLD_FORMAT,Locale.getDefault());
////
////                        try {
////                            d = df.parse(oldDateString);
////                        } catch (ParseException e) {
////                            e.printStackTrace();
////                        }
////                        df.applyPattern(NEW_FORMAT);
////                        newDateString = df.format(d);
////                        System.out.println(newDateString);
//                        dateOfBirth.setText(datt);
//
//
//                        revision.setText(empInformations.get(i).getRevision());
//
//                        if (empInformations.get(i).getBlood_grp() != null) {
////                            bloodGroup.setSelection(Integer.parseInt(empInformations.get(i).getBlood_grp()));
//                            bloodGroup.setText(bg.get(Integer.parseInt(empInformations.get(i).getBlood_grp())));
//                        }
//                        if (empInformations.get(i).getGrop_dis() != null) {
////                            groupDisplay.setSelection(Integer.parseInt(empInformations.get(i).getGrop_dis()));
//                            groupDisplay.setText(gd.get(Integer.parseInt(empInformations.get(i).getGrop_dis())));
//                        }
//                        if (empInformations.get(i).getReligion() != null) {
////                            religion.setSelection(Integer.parseInt(empInformations.get(i).getReligion()));
//                            religion.setText(rel.get(Integer.parseInt(empInformations.get(i).getReligion())));
//                        }
//
//                        nationality.setText(empInformations.get(i).getNational());
//
//                        if (empInformations.get(i).getSex() != null) {
////                            sex.setSelection(Integer.parseInt(empInformations.get(i).getSex()));
//                            sex.setText(se.get(Integer.parseInt(empInformations.get(i).getSex())));
//                        }
//
//                        familyName.setText(empInformations.get(i).getFamilyName());
//                        callingName.setText(empInformations.get(i).getCallingName());
//                        email.setText(empInformations.get(i).getEmail());
//                        personalAdd.setText(empInformations.get(i).getPrsentAdd());
//                        permanentAdd.setText(empInformations.get(i).getPerManentAdd());
//
//                        if (empInformations.get(i).getMarital_status() != null) {
////                            marital_status.setSelection(Integer.parseInt(empInformations.get(i).getMarital_status()));
//                            marital_status.setText(ms.get(Integer.parseInt(empInformations.get(i).getMarital_status())));
//                        }
//
//                        personalAddBangla.setText(empInformations.get(i).getPresent_bangla());
//                        permanentAddBangla.setText(empInformations.get(i).getPermanent_bangla());
//
//                        if (empInformations.get(i).getMailingAdd() != null) {
////                            mailing_address.setSelection(Integer.parseInt(empInformations.get(i).getMailingAdd()));
//                            mailing_address.setText(ma.get(Integer.parseInt(empInformations.get(i).getMailingAdd())));
//                        }
//
//                        if (banglaName == null) {
//                            banglaName = "";
//                        }
//                        nameBP.setText(banglaName);
//                    }
//                }
//
//
//
//
//
//            }
//            else {
//                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
//                AlertDialog dialog = new AlertDialog.Builder(PersonalData.this)
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
//                        finish();
//                    }
//                });
//            }
//        }
//    }
//
//    public void personalDataQuery() {
//
//
//        try {
//            this.connection = createConnection();
//            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();
//
//
//            Statement stmt = connection.createStatement();
//
//            banglaName = "";
//            empInformations = new ArrayList<>();
////            StringBuffer stringBuffer = new StringBuffer();
////            StringBuffer stringBuffer1 = new StringBuffer();
////            StringBuffer stringBuffer2 = new StringBuffer();
////            StringBuffer stringBuffer3 = new StringBuffer();
////            StringBuffer stringBuffer4 = new StringBuffer();
////            StringBuffer stringBuffer5 = new StringBuffer();
////            StringBuffer stringBuffer6 = new StringBuffer();
////            StringBuffer stringBuffer7 = new StringBuffer();
//
//
//
//            ResultSet rs=stmt.executeQuery("SELECT PI_RIVISION_CODE, TO_CHAR(TO_DATE(PI_EFF_DATE),'DD-MON-YYYY') PI_EFF_DATE, PI_BLOOD_GP, PI_BLOOD_GP_PUBLISH_FLAG, \n" +
//                    "TO_CHAR(TO_DATE(PI_DOB),'DD-MON-YYYY') PI_DOB, PI_RELIGION, PI_NATIONALITY, PI_SEX, PI_FAMILY_NAME, PI_CALLING_NAME, PI_EMAIL, PI_PRE_ADDRESS, \n" +
//                    "PI_PAR_ADDRESS, PI_MARITAL_STATUS, PI_PRE_ADDRESS_BAN, PI_PAR_ADDRESS_BAN, PI_ADDRESS_FLAG \n" +
//                    "FROM EMP_PERSONAL_INFO WHERE PI_EMP_ID = "+emp_id+"");
//
//
//            while(rs.next()) {
//
//                empInformations.add(new EMPInformation(rs.getString(1),rs.getString(2),rs.getString(3),
//                        rs.getString(4), rs.getString(5),rs.getString(6),rs.getString(7),
//                        rs.getString(8),rs.getString(9),rs.getString(10),rs.getString(11),
//                        rs.getString(12),rs.getString(13),rs.getString(14),rs.getString(15),
//                        rs.getString(16),rs.getString(17)));
//
//            }
//
//            ResultSet resultSet = stmt.executeQuery("SELECT EMP_MST.EMP_NAME_BN\n" +
//                    "  FROM EMP_MST\n" +
//                    " WHERE EMP_ID = "+emp_id+"");
//
//            while (resultSet.next()) {
//                banglaName = resultSet.getString(1);
//            }
////            System.out.println(stringBuffer);
////            System.out.println(stringBuffer1);
////            System.out.println(stringBuffer2);
////            System.out.println(stringBuffer3);
////            System.out.println(stringBuffer4);
////            System.out.println(stringBuffer5);
////            System.out.println(stringBuffer6);
////            System.out.println(stringBuffer7);
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
//
//    }

    public void getEmpInformation() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;

        banglaName = "";
        empInformations = new ArrayList<>();

        String jobDescUrl = "http://103.56.208.123:8001/apex/ttrams/emp_information/getPersonalInfo/"+emp_id+"";

        RequestQueue requestQueue = Volley.newRequestQueue(PersonalData.this);

        StringRequest emoInfoReq = new StringRequest(Request.Method.GET, jobDescUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject empInfo = array.getJSONObject(i);
                        String pi_rivision_code = empInfo.getString("pi_rivision_code");
                        if(pi_rivision_code.equals("null") || pi_rivision_code.equals("NULL")) {
                            pi_rivision_code = "";
                        }
                        pi_rivision_code = transformText(pi_rivision_code);

                        String pi_eff_date = empInfo.getString("pi_eff_date");
                        if(pi_eff_date.equals("null") || pi_eff_date.equals("NULL")) {
                            pi_eff_date = "";
                        }
                        pi_eff_date = transformText(pi_eff_date);

                        String pi_blood_gp = empInfo.getString("pi_blood_gp");
                        if(pi_blood_gp.equals("null") || pi_blood_gp.equals("NULL")) {
                            pi_blood_gp = "";
                        }
                        pi_blood_gp = transformText(pi_blood_gp);

                        String pi_blood_gp_publish_flag = empInfo.getString("pi_blood_gp_publish_flag");
                        if(pi_blood_gp_publish_flag.equals("null") || pi_blood_gp_publish_flag.equals("NULL")) {
                            pi_blood_gp_publish_flag = "";
                        }
                        pi_blood_gp_publish_flag = transformText(pi_blood_gp_publish_flag);

                        String pi_dob = empInfo.getString("pi_dob");
                        if(pi_dob.equals("null") || pi_dob.equals("NULL")) {
                            pi_dob = "";
                        }
                        pi_dob = transformText(pi_dob);

                        String pi_religion = empInfo.getString("pi_religion");
                        if(pi_religion.equals("null") || pi_religion.equals("NULL")) {
                            pi_religion = "";
                        }
                        pi_religion = transformText(pi_religion);

                        String pi_nationality = empInfo.getString("pi_nationality");
                        if(pi_nationality.equals("null") || pi_nationality.equals("NULL")) {
                            pi_nationality = "";
                        }
                        pi_nationality = transformText(pi_nationality);

                        String pi_sex = empInfo.getString("pi_sex");
                        if(pi_sex.equals("null") || pi_sex.equals("NULL")) {
                            pi_sex = "";
                        }
                        pi_sex = transformText(pi_sex);

                        String pi_family_name = empInfo.getString("pi_family_name");
                        if(pi_family_name.equals("null") || pi_family_name.equals("NULL")) {
                            pi_family_name = "";
                        }
                        pi_family_name = transformText(pi_family_name);

                        String pi_calling_name = empInfo.getString("pi_calling_name");
                        if(pi_calling_name.equals("null") || pi_calling_name.equals("NULL")) {
                            pi_calling_name = "";
                        }
                        pi_calling_name = transformText(pi_calling_name);

                        String pi_email = empInfo.getString("pi_email");
                        if(pi_email.equals("null") || pi_email.equals("NULL")) {
                            pi_email = "";
                        }
                        pi_email = transformText(pi_email);

                        String pi_pre_address = empInfo.getString("pi_pre_address");
                        if(pi_pre_address.equals("null") || pi_pre_address.equals("NULL")) {
                            pi_pre_address = "";
                        }
                        pi_pre_address = transformText(pi_pre_address);

                        String pi_par_address = empInfo.getString("pi_par_address");
                        if(pi_par_address.equals("null") || pi_par_address.equals("NULL")) {
                            pi_par_address = "";
                        }
                        pi_par_address = transformText(pi_par_address);

                        String pi_marital_status = empInfo.getString("pi_marital_status");
                        if(pi_marital_status.equals("null") || pi_marital_status.equals("NULL")) {
                            pi_marital_status = "";
                        }
                        pi_marital_status = transformText(pi_marital_status);

                        String pi_pre_address_ban = empInfo.getString("pi_pre_address_ban");
                        if(pi_pre_address_ban.equals("null") || pi_pre_address_ban.equals("NULL")) {
                            pi_pre_address_ban = "";
                        }
                        pi_pre_address_ban = transformText(pi_pre_address_ban);

                        String pi_par_address_ban = empInfo.getString("pi_par_address_ban");
                        if(pi_par_address_ban.equals("null") || pi_par_address_ban.equals("NULL")) {
                            pi_par_address_ban = "";
                        }
                        pi_par_address_ban = transformText(pi_par_address_ban);

                        String pi_address_flag = empInfo.getString("pi_address_flag");
                        if(pi_address_flag.equals("null") || pi_address_flag.equals("NULL")) {
                            pi_address_flag = "";
                        }
                        pi_address_flag = transformText(pi_address_flag);

                        String emp_name_bn = empInfo.getString("emp_name_bn");
                        if(emp_name_bn.equals("null") || emp_name_bn.equals("NULL")) {
                            emp_name_bn = "";
                        }
                        emp_name_bn = transformText(emp_name_bn);

                        empInformations.add(new EMPInformation(pi_rivision_code,pi_eff_date,pi_blood_gp,
                                pi_blood_gp_publish_flag, pi_dob,pi_religion,pi_nationality,
                                pi_sex,pi_family_name,pi_calling_name,pi_email,
                                pi_pre_address,pi_par_address,pi_marital_status,pi_pre_address_ban,
                                pi_par_address_ban,pi_address_flag));
                        banglaName = emp_name_bn;
                    }
                }
                connected = true;
                updateLayout();
            }
            catch (JSONException e) {
                connected = false;
                e.printStackTrace();
                updateLayout();
            }
        },error -> {
            conn = false;
            connected = false;
            error.printStackTrace();
            updateLayout();
        });

        requestQueue.add(emoInfoReq);
    }

    private void updateLayout() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                if (empInformations.size() != 0) {
                    for (int i = 0 ; i < empInformations.size(); i++) {

                        String dateC = empInformations.get(i).getEffectedDAte();
                        effectedDate.setText(dateC);

                        String datt = empInformations.get(i).getBirth_date();
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
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(PersonalData.this)
                        .setMessage("There is a network issue in the server. Please Try later")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    getEmpInformation();
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
            AlertDialog dialog = new AlertDialog.Builder(PersonalData.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel",null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                getEmpInformation();
                dialog.dismiss();
            });
            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> {

                dialog.dismiss();
                finish();
            });
        }
    }

    //    --------------------------Transforming Bangla Text-----------------------------
    private String transformText(String text) {
        byte[] bytes = text.getBytes(ISO_8859_1);
        return new String(bytes, UTF_8);
    }
}