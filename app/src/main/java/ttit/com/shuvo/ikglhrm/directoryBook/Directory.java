package ttit.com.shuvo.ikglhrm.directoryBook;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import ttit.com.shuvo.ikglhrm.R;
import ttit.com.shuvo.ikglhrm.WaitProgress;

import static ttit.com.shuvo.ikglhrm.Login.userDesignations;
import static ttit.com.shuvo.ikglhrm.Login.userInfoLists;
import static ttit.com.shuvo.ikglhrm.OracleConnection.createConnection;

public class Directory extends AppCompatActivity {

    RecyclerView directoryView;
    DirectoryAdapter directoryAdapter;
    RecyclerView.LayoutManager layoutManager;

    public static ArrayList<PhoneList> allPhoneLists;
    public static ArrayList<DirectoryList> allDirectoryLists;


    TextInputEditText search;
    TextInputLayout searchLay;

    TextInputEditText searchDep;
    TextInputLayout searchDepLay;

    TextInputEditText searchDes;
    TextInputLayout searchDesLay;

    ListView depList;
    ListView desigList;

//    TextInputEditText searchDiv;
//    TextInputLayout searchDivLay;
    Button close;

    WaitProgress waitProgress = new WaitProgress();
    private String message = null;
    private Boolean conn = false;
    private Boolean connected = false;
    private Connection connection;

    String emp_id = "";
    ArrayList<DirectoryList> filteredList;
    String searchingDep = "";
    String searchingName = "";
    String searchingDesg = "";
    String divm_id = "";
    ArrayList<String> departments;
    ArrayList<String> designations;

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
        setContentView(R.layout.activity_directory);

        directoryView = findViewById(R.id.directory_list_view);
        close = findViewById(R.id.directory_finish);

        search = findViewById(R.id.search_directory);
        searchLay = findViewById(R.id.search_directory_lay);

        searchDep = findViewById(R.id.search_directory_dep);
        searchDepLay = findViewById(R.id.search_directory_dep_lay);

        desigList = findViewById(R.id.desig_list_view);
        depList = findViewById(R.id.dept_list_view);

        searchDes = findViewById(R.id.search_designation);
        searchDesLay = findViewById(R.id.search_directory_desig_lay);

//        searchDiv = findViewById(R.id.search_directory_div);
//        searchDivLay = findViewById(R.id.search_directory_div_lay);

        emp_id = userInfoLists.get(0).getEmp_id();
        divm_id = userDesignations.get(0).getDiv_id();

        allPhoneLists = new ArrayList<>();
        allDirectoryLists = new ArrayList<>();
        filteredList = new ArrayList<>();
        departments = new ArrayList<>();
        designations = new ArrayList<>();

        directoryView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        directoryView.setLayoutManager(layoutManager);

        new Check().execute();

        searchDep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int vis = depList.getVisibility();
                System.out.println(depList.getVisibility());
                if (vis == 0) {
                    searchDepLay.setEndIconDrawable(R.drawable.arrow_drop_down_24);
                    depList.setVisibility(View.GONE);
                } else {
                    searchDepLay.setEndIconDrawable(R.drawable.arrow_drop_up_24);
                    depList.setVisibility(View.VISIBLE);
                }
            }
        });
        depList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String text = parent.getItemAtPosition(position).toString();
                searchDep.setText(text);
                searchDepLay.setHint("Department");
                searchDepLay.setEndIconDrawable(R.drawable.arrow_drop_down_24);
                depList.setVisibility(View.GONE);
            }
        });

        searchDes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int vis = desigList.getVisibility();
                System.out.println(desigList.getVisibility());
                if (vis == 0) {
                    searchDesLay.setEndIconDrawable(R.drawable.arrow_drop_down_24);
                    desigList.setVisibility(View.GONE);
                } else {
                    searchDesLay.setEndIconDrawable(R.drawable.arrow_drop_up_24);
                    desigList.setVisibility(View.VISIBLE);
                }
            }
        });

        desigList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String text = parent.getItemAtPosition(position).toString();
                searchDes.setText(text);
                searchDesLay.setHint("Designation");
                searchDesLay.setEndIconDrawable(R.drawable.arrow_drop_down_24);
                desigList.setVisibility(View.GONE);
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                searchingName = s.toString();
                filter(s.toString());
                if (s.toString().isEmpty()) {
                    searchLay.setHint("Search By Name");
                } else {
                    searchLay.setHint("Name");
                }
            }
        });

        searchDep.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                searchingDep = s.toString();
                filterDep(s.toString());
                if (s.toString().isEmpty()) {
                    searchDepLay.setHint("Search By Department");
                }
            }
        });

        searchDes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                searchingDesg = s.toString();
                filterDesg(s.toString());
                if (s.toString().isEmpty()) {
                    searchDesLay.setHint("Search By Designation");
                }
            }
        });

//        searchDiv.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                filterDiv(s.toString());
//                if (s.toString().isEmpty()) {
//                    searchDivLay.setHint("Search By Division");
//                }
//            }
//        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                        event.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT) {
                    if (event == null || !event.isShiftPressed()) {
                        // the user is done typing.
                        Log.i("Let see", "Come here");
                        closeKeyBoard();



                        return false; // consume.
                    }
                }
                return false;
            }
        });

//        searchDiv.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
//                        actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
//                        event.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT) {
//                    if (event == null || !event.isShiftPressed()) {
//                        // the user is done typing.
//                        Log.i("Let see", "Come here");
//                        closeKeyBoard();
//
//
//
//                        return false; // consume.
//                    }
//                }
//                return false;
//            }
//        });

        searchDep.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                        event.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT) {
                    if (event == null || !event.isShiftPressed()) {
                        // the user is done typing.
                        Log.i("Let see", "Come here");
                        closeKeyBoard();



                        return false; // consume.
                    }
                }
                return false;
            }
        });

        searchDes.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                        event.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT) {
                    if (event == null || !event.isShiftPressed()) {
                        // the user is done typing.
                        Log.i("Let see", "Come here");
                        closeKeyBoard();



                        return false; // consume.
                    }
                }
                return false;
            }
        });


    }

    private void closeKeyBoard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            view.clearFocus();
            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        closeKeyBoard();
        return super.onTouchEvent(event);
    }

    private void filter(String text) {

        filteredList = new ArrayList<>();
        for (DirectoryList item : allDirectoryLists) {
            if (searchingDep.isEmpty()) {
                if (searchingDesg.isEmpty()) {
                    if (item.getEmp_name().toLowerCase().contains(text.toLowerCase())) {
                        filteredList.add((item));
                    }
                } else {
                    if (item.getDes_name().toLowerCase().contains(searchingDesg.toLowerCase())) {
                        if (item.getEmp_name().toLowerCase().contains(text.toLowerCase())) {
                            filteredList.add((item));
                        }
                    }
                }
            } else {
                if (searchingDesg.isEmpty()) {
                    if (item.getDep_name().toLowerCase().contains(searchingDep.toLowerCase())) {
                        if (item.getEmp_name().toLowerCase().contains(text.toLowerCase())) {
                            filteredList.add((item));

                        }
                    }
                } else {
                    if (item.getDes_name().toLowerCase().contains(searchingDesg.toLowerCase())) {
                        if (item.getDep_name().toLowerCase().contains(searchingDep.toLowerCase())) {
                            if (item.getEmp_name().toLowerCase().contains(text.toLowerCase())) {
                                filteredList.add((item));

                            }
                        }
                    }
                }

            }

        }
        directoryAdapter.filterList(filteredList);
    }

    private void filterDep(String text) {

        filteredList = new ArrayList<>();
        for (DirectoryList item : allDirectoryLists) {
            if (searchingDesg.isEmpty()){
                if (searchingName.isEmpty()) {
                    if (item.getDep_name().toLowerCase().contains(text.toLowerCase())) {
                        filteredList.add((item));
                    }
                } else {
                    if (item.getEmp_name().toLowerCase().contains(searchingName.toLowerCase())) {
                        if (item.getDep_name().toLowerCase().contains(text.toLowerCase())) {
                            filteredList.add((item));
                        }
                    }
                }
            } else {
                if (searchingName.isEmpty()) {
                    if (item.getDes_name().toLowerCase().contains(searchingDesg.toLowerCase())) {
                        if (item.getDep_name().toLowerCase().contains(text.toLowerCase())) {
                            filteredList.add((item));
                        }
                    }
                } else {
                    if (item.getDes_name().toLowerCase().contains(searchingDesg.toLowerCase())) {
                        if (item.getEmp_name().toLowerCase().contains(searchingName.toLowerCase())) {
                            if (item.getDep_name().toLowerCase().contains(text.toLowerCase())) {
                                filteredList.add((item));
                            }
                        }
                    }
                }
            }
//            if (searchingName.isEmpty()) {
//                if (item.getDep_name().toLowerCase().contains(text.toLowerCase())) {
//                    filteredList.add((item));
//                }
//            } else {
//                if (item.getEmp_name().toLowerCase().contains(searchingName.toLowerCase())) {
//                    if (item.getDep_name().toLowerCase().contains(text.toLowerCase())) {
//                        filteredList.add((item));
//                    }
//                }
//            }

        }
        directoryAdapter.filterList(filteredList);
    }

    private void filterDesg(String text) {

        filteredList = new ArrayList<>();
        for (DirectoryList item : allDirectoryLists) {
            if (searchingDep.isEmpty()) {
                if (searchingName.isEmpty()) {
                    if (item.getDes_name().toLowerCase().contains(text.toLowerCase())){
                        filteredList.add((item));
                    }
                } else {
                    if (item.getEmp_name().toLowerCase().contains(searchingName.toLowerCase())){
                        if (item.getDes_name().toLowerCase().contains(text.toLowerCase())){
                            filteredList.add((item));
                        }
                    }
                }
            } else {
                if (searchingName.isEmpty()) {
                    if (item.getDep_name().toLowerCase().contains(searchingDep.toLowerCase())){
                        if (item.getDes_name().toLowerCase().contains(text.toLowerCase())){
                            filteredList.add((item));
                        }
                    }
                } else {
                    if (item.getDep_name().toLowerCase().contains(searchingDep.toLowerCase())){
                        if (item.getEmp_name().toLowerCase().contains(searchingName.toLowerCase())){
                            if (item.getDes_name().toLowerCase().contains(text.toLowerCase())){
                                filteredList.add((item));
                            }
                        }
                    }
                }
            }

        }
        directoryAdapter.filterList(filteredList);
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

                DirectoryData();
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

                directoryAdapter = new DirectoryAdapter(Directory.this, allDirectoryLists);
                directoryView.setAdapter(directoryAdapter);

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Directory.this, R.layout.simple_list,R.id.textView, departments);
                depList.setAdapter(arrayAdapter);

                ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(Directory.this, R.layout.simple_list,R.id.textView, designations);
                desigList.setAdapter(arrayAdapter1);



            }else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(Directory.this)
                        .setMessage("Please Check Your Internet Connection")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel", null)
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


    public void DirectoryData() {
        try {
            this.connection = createConnection();

            Statement stmt = connection.createStatement();

            allPhoneLists = new ArrayList<>();
            allDirectoryLists = new ArrayList<>();
            ArrayList<String> idofALl = new ArrayList<>();
            departments = new ArrayList<>();
            designations = new ArrayList<>();



            ResultSet rs=stmt.executeQuery(" SELECT EMP_ID,\n" +
                    "    EMP_NAME,\n" +
                    "       DEPT_NAME,\n" +
                    "       DIVM_NAME,\n" +
                    "       DESIG_NAME,\n" +
                    "       PHONE_NUMBER,\n" +
                    "       JOB_EMAIL EMAIL\n" +
                    "  FROM EMP_DETAILS_V\n" +
                    "  where divm_id = (select divm_id from EMP_DETAILS_V\n" +
                    " WHERE EMP_ID = "+emp_id+")" +
                    "and emp_id <> "+emp_id+"");



            while(rs.next())  {
                allDirectoryLists.add(new DirectoryList(rs.getString(1),rs.getString(2),rs.getString(4),rs.getString(3),rs.getString(5),rs.getString(7),"1"));

                String p_id = rs.getString(1);
                System.out.println("DIRECTORY: " +p_id);
                idofALl.add(p_id);



            }

            for (int i = 0; i < idofALl.size(); i++) {
                String id = idofALl.get(i);

                ResultSet resultSet = stmt.executeQuery("Select JOB_EMP_ID,ESH_MBL_SIM FROM EMP_SIM_HISTORY, EMP_JOB_HISTORY Where ESH_JOB_ID = EMP_JOB_HISTORY.JOB_ID and JOB_EMP_ID = "+id+"");

                while (resultSet.next()) {
                    System.out.println("PHONE: "+resultSet.getString(1));
                    allPhoneLists.add(new PhoneList(resultSet.getString(1),resultSet.getString(2)));
                }
            }

            ResultSet rs1=stmt.executeQuery(" select dept_name from dept_mst where dept_divm_id = "+divm_id+"");

            while(rs1.next())  {
                departments.add(rs1.getString(1));
            }


            ResultSet resultSet1 = stmt.executeQuery("select distinct desig_name from emp_details_v where desig_name is not NULL and divm_id = "+divm_id+"\n");

            while (resultSet1.next()) {
                designations.add(resultSet1.getString(1));
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

    public void getExtraData() {
        try {
            this.connection = createConnection();

            Statement stmt = connection.createStatement();










            connection.close();

        }
        catch (Exception e) {

            //   Toast.makeText(MainActivity.this, ""+e,Toast.LENGTH_LONG).show();
            Log.i("ERRRRR", e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

}