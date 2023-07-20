package ttit.com.shuvo.ikglhrm.directoryBook;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rosemaryapp.amazingspinner.AmazingSpinner;

import java.util.ArrayList;
import ttit.com.shuvo.ikglhrm.R;
import ttit.com.shuvo.ikglhrm.WaitProgress;
import static ttit.com.shuvo.ikglhrm.Login.userDesignations;
import static ttit.com.shuvo.ikglhrm.Login.userInfoLists;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DirectoryWithDivision extends AppCompatActivity {

    RecyclerView directoryView;
    DirectoryAdapter directoryAdapter;
    RecyclerView.LayoutManager layoutManager;

    public static ArrayList<PhoneList> allPhoneLists;
    public static ArrayList<DirectoryList> allDirectoryLists;

    String emp_id = "";
    ArrayList<DirectoryList> filteredList;
    String searchingDep = "";
    String searchingName = "";
    String searchingDesg = "";
//    ArrayList<String> departments;
//    ArrayList<String> designations;

    WaitProgress waitProgress = new WaitProgress();
//    private String message = null;
    private Boolean conn = false;
    private Boolean connected = false;
//    private Connection connection;

    AmazingSpinner divSpinner;
    AmazingSpinner depSpinner;
    AmazingSpinner desSpinner;

    TextInputEditText search;
    TextInputLayout searchLay;

    Button close;

    String divName = "";
    String depName = "";
    String desigName = "";
    String div_id = "";
    String dep_id = "";
    String desig_id = "";

    ArrayList<TwoItemLists> divLists;
    ArrayList<TwoItemLists> depLists;
    ArrayList<TwoItemLists> desLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(DirectoryWithDivision.this,R.color.secondaryColor));
        setContentView(R.layout.activity_directory_with_division);

        depSpinner = findViewById(R.id.department_type_spinner);
        divSpinner = findViewById(R.id.division_type_spinner);
        desSpinner = findViewById(R.id.designation_type_spinner);

        directoryView = findViewById(R.id.directory_division_list_view);
        close = findViewById(R.id.directory_division_finish);

        search = findViewById(R.id.search_item_name_diretory_division);
        searchLay = findViewById(R.id.search_item_name_lay_directory_division);

        allPhoneLists = new ArrayList<>();
        allDirectoryLists = new ArrayList<>();
        filteredList = new ArrayList<>();
        divLists = new ArrayList<>();
        depLists = new ArrayList<>();
        desLists = new ArrayList<>();

        emp_id = userInfoLists.get(0).getEmp_id();
        div_id = userDesignations.get(0).getDiv_id();

        directoryView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        directoryView.setLayoutManager(layoutManager);

        // Selecting Division
        divSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = parent.getItemAtPosition(position).toString();
                System.out.println(position+": "+name);

                depSpinner.setText("");
                desSpinner.setText("");
                search.setText("");
                depName = "";
                desigName = "";
                searchingName = "";
                searchingDep = "";
                searchingDesg = "";
                for (int i = 0; i <divLists.size(); i++) {
                    if (name.equals(divLists.get(i).getName())) {
                        div_id = divLists.get(i).getId();
                        if (div_id.isEmpty()) {
                            divName = "";
                        } else {
                            divName = divLists.get(i).getName();
                        }

                    }
                }

                dep_id = "";
                desig_id = "";


//                new DepartmentCheck().execute();
                getDivisionWiseDepData();

            }
        });

        // Selecting Department
        depSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = parent.getItemAtPosition(position).toString();
                System.out.println(position+": "+name);
                desSpinner.setText("");
                search.setText("");
                desigName = "";
                searchingName = "";
                searchingDesg = "";
                for (int i = 0; i <depLists.size(); i++) {
                    if (name.equals(depLists.get(i).getName())) {
                        dep_id = depLists.get(i).getId();
                        if (dep_id.isEmpty()) {
                            depName = "";
                            searchingDep = "";
                        } else {
                            depName = depLists.get(i).getName();
                            searchingDep = depLists.get(i).getName();
                        }
                    }
                }

                desig_id = "";
//                new DesignationCheck().execute();
                getDesignations();

            }
        });

        // Selecting Employee
        desSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = parent.getItemAtPosition(position).toString();
                System.out.println(position+": "+name);

                search.setText("");
                searchingName = "";

                for (int i = 0; i <desLists.size(); i++) {
                    if (name.equals(desLists.get(i).getName())) {
                        desig_id = desLists.get(i).getId();
                        if (desig_id.isEmpty()) {
                            desigName = "";
                            searchingDesg = "";
                        } else {
                            searchingDesg = desLists.get(i).getName();
                            desigName = desLists.get(i).getName();
                        }
                    }
                }


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

        depSpinner.addTextChangedListener(new TextWatcher() {
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

            }
        });

        desSpinner.addTextChangedListener(new TextWatcher() {
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

            }
        });

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

//        new Check().execute();
        getFirstData();
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

//    public boolean isConnected() {
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
//                FirstCheckData();
//                if (connected) {
//                    conn = true;
//                }
//
//            } else {
//                conn = false;
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
//                conn = false;
//                connected = false;
//
//                //String[] type = new String[] {"Approved", "Pending","Both"};
//                ArrayList<String> type = new ArrayList<>();
//                for(int i = 0; i < divLists.size(); i++) {
//                    type.add(divLists.get(i).getName());
//                }
//
//                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);
//
//                divSpinner.setAdapter(arrayAdapter);
//
//
////                ArrayList<String> type1 = new ArrayList<>();
////                for(int i = 0; i < depLists.size(); i++) {
////                    type1.add(depLists.get(i).getName());
////                }
////
////                ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(getApplicationContext(),R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type1);
////
////                depSpinner.setAdapter(arrayAdapter1);
//
//                directoryAdapter = new DirectoryAdapter(DirectoryWithDivision.this, allDirectoryLists);
//                directoryView.setAdapter(directoryAdapter);
//
////                int index = 0;
////                for (int i = 0; i < divLists.size(); i++) {
////                    if (div_id.equals(divLists.get(i).getId())) {
////                        index = i;
////                    }
////                }
//
//                //divSpinner.setText(index);
//
//
//
//                //new ItemCheck().execute();
//
////                new ReOrderFragment.ReOrderLevelCheck().execute();
//
//            }
//            else {
//                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
//                AlertDialog dialog = new AlertDialog.Builder(DirectoryWithDivision.this)
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

//    public class DepartmentCheck extends AsyncTask<Void, Void, Void> {
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
//                DepartmentData();
//                if (connected) {
//                    conn = true;
//                }
//
//            } else {
//                conn = false;
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
//                conn = false;
//                connected = false;
//
//                //String[] type = new String[] {"Approved", "Pending","Both"};
//                ArrayList<String> type = new ArrayList<>();
//                for(int i = 0; i < depLists.size(); i++) {
//                    type.add(depLists.get(i).getName());
//                }
//
//                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);
//
//                depSpinner.setAdapter(arrayAdapter);
//
//
//                desLists = new ArrayList<>();
//                ArrayList<String> type1 = new ArrayList<>();
//                for(int i = 0; i < desLists.size(); i++) {
//                    type1.add(desLists.get(i).getName());
//                }
//
//                ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(getApplicationContext(),R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type1);
//
//                desSpinner.setAdapter(arrayAdapter1);
//
//                directoryAdapter = new DirectoryAdapter(DirectoryWithDivision.this, allDirectoryLists);
//                directoryView.setAdapter(directoryAdapter);
//
//                //new ReOrderLevelCheck().execute();
//
//            }
//            else {
//                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
//                AlertDialog dialog = new AlertDialog.Builder(DirectoryWithDivision.this)
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
//                        new DepartmentCheck().execute();
//                        dialog.dismiss();
//                    }
//                });
//
//                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
//                negative.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });
//            }
//        }
//    }

//    public class DesignationCheck extends AsyncTask<Void, Void, Void> {
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
//                DesignationData();
//                if (connected) {
//                    conn = true;
//                }
//
//            } else {
//                conn = false;
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
//                conn = false;
//                connected = false;
//
//                //String[] type = new String[] {"Approved", "Pending","Both"};
//                ArrayList<String> type = new ArrayList<>();
//                for(int i = 0; i < desLists.size(); i++) {
//                    type.add(desLists.get(i).getName());
//                }
//
//                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);
//
//                desSpinner.setAdapter(arrayAdapter);
//
//                //new ReOrderLevelCheck().execute();
//
//            }
//            else {
//                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
//                AlertDialog dialog = new AlertDialog.Builder(DirectoryWithDivision.this)
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
//                        new DesignationCheck().execute();
//                        dialog.dismiss();
//                    }
//                });
//
//                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
//                negative.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });
//            }
//        }
//    }

//    public void FirstCheckData() {
//        try {
//            this.connection = createConnection();
//            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();
//
//            Statement stmt = connection.createStatement();
//
//            divLists = new ArrayList<>();
//            depLists = new ArrayList<>();
//            allPhoneLists = new ArrayList<>();
//            allDirectoryLists = new ArrayList<>();
//            ArrayList<String> idofALl = new ArrayList<>();
//
//            if (div_id.isEmpty()) {
//                div_id = null;
//            }
//
//            ResultSet resultSet1 = stmt.executeQuery("SELECT\n" +
//                    "    division_mst.divm_id,\n" +
//                    "    division_mst.divm_name\n" +
//                    "FROM\n" +
//                    "    division_mst\n" +
//                    "order by divm_id");
//
//            while (resultSet1.next()) {
//                divLists.add(new TwoItemLists(resultSet1.getString(1),resultSet1.getString(2)));
//            }
//
//            ResultSet resultSet = stmt.executeQuery("SELECT\n" +
//                    "    dept_mst.dept_id,\n" +
//                    "    dept_mst.dept_name,\n" +
//                    "    dept_mst.dept_divm_id\n" +
//                    "FROM\n" +
//                    "    dept_mst\n" +
//                    "where dept_divm_id = "+div_id+"\n" +
//                    "order by dept_id");
//
//            while (resultSet.next()) {
//                depLists.add(new TwoItemLists(resultSet.getString(1),resultSet.getString(2)));
//            }
//
//
////            ResultSet rs=stmt.executeQuery(" SELECT EMP_ID,\n" +
////                    "    EMP_NAME,\n" +
////                    "       DEPT_NAME,\n" +
////                    "       DIVM_NAME,\n" +
////                    "       DESIG_NAME,\n" +
////                    "       PHONE_NUMBER,\n" +
////                    "       JOB_EMAIL EMAIL\n" +
////                    "  FROM EMP_DETAILS_V\n" +
////                    "  where divm_id = "+div_id+"" +
////                    "and emp_id <> "+emp_id+"");
//
//
//            ResultSet rs = stmt.executeQuery("SELECT\n" +
//                    "emp_mst.emp_id,\n" +
//                    "emp_mst.emp_name,\n" +
//                    "dept_mst.dept_name,\n" +
//                    "division_mst.divm_name,\n" +
//                    "desig_mst.desig_name,\n" +
//                    "isp_user.usr_contact,\n" +
//                    "isp_user.usr_email\n" +
//                    "FROM\n" +
//                    "emp_mst,job_setup_dtl,job_setup_mst,division_mst,dept_mst,desig_mst,isp_user\n" +
//                    "WHERE\n" +
//                    "emp_mst.emp_jsd_id = job_setup_dtl.jsd_id\n" +
//                    "AND job_setup_dtl.jsd_jsm_id = job_setup_mst.jsm_id\n" +
//                    "AND job_setup_mst.jsm_divm_id = division_mst.divm_id\n" +
//                    "AND job_setup_mst.jsm_dept_id = dept_mst.dept_id\n" +
//                    "AND job_setup_mst.jsm_desig_id = desig_mst.desig_id\n" +
//                    "AND emp_mst.emp_id = isp_user.usr_emp_id\n" +
//                    "AND division_mst.divm_id = "+div_id+"\n" +
//                    "AND emp_mst.emp_id <> "+emp_id+"");
//
//            while(rs.next())  {
//                allDirectoryLists.add(new DirectoryList(rs.getString(1),rs.getString(2),rs.getString(4),rs.getString(3),rs.getString(5),rs.getString(7),rs.getString(6),"2"));
//
//                String p_id = rs.getString(1);
//                System.out.println("DIRECTORY: " +p_id);
//                idofALl.add(p_id);
//            }
//
//            for (int i = 0; i < idofALl.size(); i++) {
//                String id = idofALl.get(i);
//
//                ResultSet resultSet2 = stmt.executeQuery("Select JOB_EMP_ID,ESH_MBL_SIM FROM EMP_SIM_HISTORY, EMP_JOB_HISTORY Where ESH_JOB_ID = EMP_JOB_HISTORY.JOB_ID and JOB_EMP_ID = "+id+"");
//
//                while (resultSet2.next()) {
//                    System.out.println("PHONE: "+resultSet2.getString(1));
//                    allPhoneLists.add(new PhoneList(resultSet2.getString(1),resultSet2.getString(2)));
//                }
//            }
//
//            if (div_id == null) {
//                div_id = "";
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
//            e.printStackTrace();
//        }
//    }

    public void getFirstData() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;

        divLists = new ArrayList<>();
        depLists = new ArrayList<>();
        allPhoneLists = new ArrayList<>();
        allDirectoryLists = new ArrayList<>();
        ArrayList<String> idofALl = new ArrayList<>();

        String divUrl = "http://103.56.208.123:8001/apex/ttrams/directory/getDivisions";
        String depUrl = "http://103.56.208.123:8001/apex/ttrams/directory/getDepartments/"+div_id+"";
        String divDirUrl = "http://103.56.208.123:8001/apex/ttrams/directory/getDivDirectories/"+div_id+"/"+emp_id+"";

        RequestQueue requestQueue = Volley.newRequestQueue(DirectoryWithDivision.this);

        StringRequest divDirReq = new StringRequest(Request.Method.GET, divDirUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject dirInfo = array.getJSONObject(i);

                        String emp_id_new = dirInfo.getString("emp_id")
                                .equals("null") ? "" : dirInfo.getString("emp_id");
                        String emp_name_new = dirInfo.getString("emp_name")
                                .equals("null") ? "" : dirInfo.getString("emp_name");
                        String dept_name_new = dirInfo.getString("dept_name")
                                .equals("null") ? "" : dirInfo.getString("dept_name");
                        String divm_name_new = dirInfo.getString("divm_name")
                                .equals("null") ? "" : dirInfo.getString("divm_name");
                        String desig_name_new = dirInfo.getString("desig_name")
                                .equals("null") ? "" : dirInfo.getString("desig_name");
                        String usr_contact_new = dirInfo.getString("usr_contact")
                                .equals("null") ? null : dirInfo.getString("usr_contact");
                        String usr_email_new = dirInfo.getString("usr_email")
                                .equals("null") ? null : dirInfo.getString("usr_email");

                        allDirectoryLists.add(new DirectoryList(emp_id_new,emp_name_new,divm_name_new,
                                dept_name_new,desig_name_new,usr_email_new,usr_contact_new,"2"));

                        idofALl.add(emp_id_new);
                    }
                }
                getPhoneLists(idofALl);
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

        StringRequest depReq = new StringRequest(Request.Method.GET, depUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject depInfo = array.getJSONObject(i);
                        String dept_id_new = depInfo.getString("dept_id");
                        String dept_name_new = depInfo.getString("dept_name");

                        depLists.add(new TwoItemLists(dept_id_new,dept_name_new));
                    }
                }
                requestQueue.add(divDirReq);
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

        StringRequest divReq = new StringRequest(Request.Method.GET, divUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject divInfo = array.getJSONObject(i);
                        String divm_id_new = divInfo.getString("divm_id");
                        String divm_name_new = divInfo.getString("divm_name");

                        divLists.add(new TwoItemLists(divm_id_new,divm_name_new));
                    }
                }

                requestQueue.add(depReq);
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

        requestQueue.add(divReq);
    }

    public void getPhoneLists(ArrayList<String> allId) {
        if (allId.size() != 0) {
            String id = allId.get(0);
            getPhones(id,allId);
        }
        else {
            connected = true;
            updateLay();
        }
    }

    public void getPhones(String id,ArrayList<String> allId) {
        String url = "http://103.56.208.123:8001/apex/ttrams/directory/getAllPhones/"+id+"";

        RequestQueue requestQueue = Volley.newRequestQueue(DirectoryWithDivision.this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject phnInfo = array.getJSONObject(i);

                        String job_emp_id = phnInfo.getString("job_emp_id");
                        String esh_mbl_sim = phnInfo.getString("esh_mbl_sim");

                        allPhoneLists.add(new PhoneList(job_emp_id,esh_mbl_sim));
                    }
                }
                allId.remove(0);
                getPhoneLists(allId);
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

        requestQueue.add(stringRequest);
    }

    private void updateLay() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                conn = false;
                connected = false;

                //String[] type = new String[] {"Approved", "Pending","Both"};
                for(int i = 0; i < divLists.size(); i++) {
                    if(div_id.equals(divLists.get(i).getId())) {
                        divSpinner.setText(divLists.get(i).getName());
                    }
                }
                ArrayList<String> type = new ArrayList<>();
                for(int i = 0; i < divLists.size(); i++) {
                    type.add(divLists.get(i).getName());
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);

                divSpinner.setAdapter(arrayAdapter);


                ArrayList<String> type1 = new ArrayList<>();
                for(int i = 0; i < depLists.size(); i++) {
                    type1.add(depLists.get(i).getName());
                }

                ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(getApplicationContext(),R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type1);

                depSpinner.setAdapter(arrayAdapter1);

                directoryAdapter = new DirectoryAdapter(DirectoryWithDivision.this, allDirectoryLists);
                directoryView.setAdapter(directoryAdapter);

            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(DirectoryWithDivision.this)
                        .setMessage("There is a network issue in the server. Please Try later")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel", null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    getFirstData();
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
            AlertDialog dialog = new AlertDialog.Builder(DirectoryWithDivision.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel", null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                getFirstData();
                dialog.dismiss();
            });

            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> {
                dialog.dismiss();
                finish();
            });
        }
    }

//    public void DepartmentData() {
//        try {
//            this.connection = createConnection();
//            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();
//
//            Statement stmt = connection.createStatement();
//
//            depLists = new ArrayList<>();
//            allPhoneLists = new ArrayList<>();
//            allDirectoryLists = new ArrayList<>();
//            ArrayList<String> idofALl = new ArrayList<>();
//
//            if (div_id.isEmpty()) {
//                div_id = null;
//            }
//
//            ResultSet resultSet1 = stmt.executeQuery("SELECT\n" +
//                    "    dept_mst.dept_id,\n" +
//                    "    dept_mst.dept_name,\n" +
//                    "    dept_mst.dept_divm_id\n" +
//                    "FROM\n" +
//                    "    dept_mst\n" +
//                    "where dept_divm_id = "+div_id+"\n" +
//                    "order by dept_id");
//
//            while (resultSet1.next()) {
//                depLists.add(new TwoItemLists(resultSet1.getString(1),resultSet1.getString(2)));
//            }
////            categoryLists.add(new ReceiveTypeList("","All Categories"));
//
////            ResultSet rs=stmt.executeQuery(" SELECT EMP_ID,\n" +
////                    "    EMP_NAME,\n" +
////                    "       DEPT_NAME,\n" +
////                    "       DIVM_NAME,\n" +
////                    "       DESIG_NAME,\n" +
////                    "       PHONE_NUMBER,\n" +
////                    "       JOB_EMAIL EMAIL\n" +
////                    "  FROM EMP_DETAILS_V\n" +
////                    "  where divm_id = "+div_id+"" +
////                    "and emp_id <> "+emp_id+"");
//
//            // no isp_user for hamidur rahman
//            ResultSet rs = stmt.executeQuery("SELECT\n" +
//                    "emp_mst.emp_id,\n" +
//                    "emp_mst.emp_name,\n" +
//                    "dept_mst.dept_name,\n" +
//                    "division_mst.divm_name,\n" +
//                    "desig_mst.desig_name,\n" +
//                    "isp_user.usr_contact,\n" +
//                    "isp_user.usr_email\n" +
//                    "FROM\n" +
//                    "emp_mst,job_setup_dtl,job_setup_mst,division_mst,dept_mst,desig_mst,isp_user\n" +
//                    "WHERE\n" +
//                    "emp_mst.emp_jsd_id = job_setup_dtl.jsd_id\n" +
//                    "AND job_setup_dtl.jsd_jsm_id = job_setup_mst.jsm_id\n" +
//                    "AND job_setup_mst.jsm_divm_id = division_mst.divm_id\n" +
//                    "AND job_setup_mst.jsm_dept_id = dept_mst.dept_id\n" +
//                    "AND job_setup_mst.jsm_desig_id = desig_mst.desig_id\n" +
//                    "AND emp_mst.emp_id = isp_user.usr_emp_id\n" +
//                    "AND division_mst.divm_id = "+div_id+"\n" +
//                    "AND emp_mst.emp_id <> "+emp_id+"");
//
//
//            while(rs.next())  {
//                allDirectoryLists.add(new DirectoryList(rs.getString(1),rs.getString(2),rs.getString(4),rs.getString(3),rs.getString(5),rs.getString(7),rs.getString(6),"2"));
//
//                String p_id = rs.getString(1);
//                System.out.println("DIRECTORY: " +p_id);
//                idofALl.add(p_id);
//            }
//
//            for (int i = 0; i < idofALl.size(); i++) {
//                String id = idofALl.get(i);
//
//                ResultSet resultSet2 = stmt.executeQuery("Select JOB_EMP_ID,ESH_MBL_SIM FROM EMP_SIM_HISTORY, EMP_JOB_HISTORY Where ESH_JOB_ID = EMP_JOB_HISTORY.JOB_ID and JOB_EMP_ID = "+id+"");
//
//                while (resultSet2.next()) {
//                    System.out.println("PHONE: "+resultSet2.getString(1));
//                    allPhoneLists.add(new PhoneList(resultSet2.getString(1),resultSet2.getString(2)));
//                }
//            }
//
//            if (div_id == null) {
//                div_id = "";
//            }
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

    public void getDivisionWiseDepData() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;

        depLists = new ArrayList<>();
        allPhoneLists = new ArrayList<>();
        allDirectoryLists = new ArrayList<>();
        ArrayList<String> idofALl = new ArrayList<>();

        String depUrl = "http://103.56.208.123:8001/apex/ttrams/directory/getDepartments/"+div_id+"";
        String divDirUrl = "http://103.56.208.123:8001/apex/ttrams/directory/getDivDirectories/"+div_id+"/"+emp_id+"";

        RequestQueue requestQueue = Volley.newRequestQueue(DirectoryWithDivision.this);

        StringRequest divDirReq = new StringRequest(Request.Method.GET, divDirUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject dirInfo = array.getJSONObject(i);

                        String emp_id_new = dirInfo.getString("emp_id")
                                .equals("null") ? "" : dirInfo.getString("emp_id");
                        String emp_name_new = dirInfo.getString("emp_name")
                                .equals("null") ? "" : dirInfo.getString("emp_name");
                        String dept_name_new = dirInfo.getString("dept_name")
                                .equals("null") ? "" : dirInfo.getString("dept_name");
                        String divm_name_new = dirInfo.getString("divm_name")
                                .equals("null") ? "" : dirInfo.getString("divm_name");
                        String desig_name_new = dirInfo.getString("desig_name")
                                .equals("null") ? "" : dirInfo.getString("desig_name");
                        String usr_contact_new = dirInfo.getString("usr_contact")
                                .equals("null") ? null : dirInfo.getString("usr_contact");
                        String usr_email_new = dirInfo.getString("usr_email")
                                .equals("null") ? null : dirInfo.getString("usr_email");

                        allDirectoryLists.add(new DirectoryList(emp_id_new,emp_name_new,divm_name_new,
                                dept_name_new,desig_name_new,usr_email_new,usr_contact_new,"2"));

                        idofALl.add(emp_id_new);
                    }
                }
                getDivPhoneLists(idofALl);
            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                updateAfterSelectingDiv();
            }
        }, error -> {
            error.printStackTrace();
            conn = false;
            connected = false;
            updateAfterSelectingDiv();
        });

        StringRequest depReq = new StringRequest(Request.Method.GET, depUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject depInfo = array.getJSONObject(i);
                        String dept_id_new = depInfo.getString("dept_id");
                        String dept_name_new = depInfo.getString("dept_name");

                        depLists.add(new TwoItemLists(dept_id_new,dept_name_new));
                    }
                }
                requestQueue.add(divDirReq);
            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                updateAfterSelectingDiv();
            }
        }, error -> {
            error.printStackTrace();
            conn = false;
            connected = false;
            updateAfterSelectingDiv();
        });

        requestQueue.add(depReq);
    }

    public void getDivPhoneLists(ArrayList<String> allId) {
        if (allId.size() != 0) {
            String id = allId.get(0);
            getDivPhones(id,allId);
        }
        else {
            connected = true;
            updateAfterSelectingDiv();
        }
    }

    public void getDivPhones(String id,ArrayList<String> allId) {
        String url = "http://103.56.208.123:8001/apex/ttrams/directory/getAllPhones/"+id+"";

        RequestQueue requestQueue = Volley.newRequestQueue(DirectoryWithDivision.this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject phnInfo = array.getJSONObject(i);

                        String job_emp_id = phnInfo.getString("job_emp_id");
                        String esh_mbl_sim = phnInfo.getString("esh_mbl_sim");

                        allPhoneLists.add(new PhoneList(job_emp_id,esh_mbl_sim));
                    }
                }
                allId.remove(0);
                getDivPhoneLists(allId);
            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                updateAfterSelectingDiv();
            }
        }, error -> {
            error.printStackTrace();
            conn = false;
            connected = false;
            updateAfterSelectingDiv();
        });

        requestQueue.add(stringRequest);
    }

    private void updateAfterSelectingDiv() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                conn = false;
                connected = false;

                ArrayList<String> type = new ArrayList<>();
                for(int i = 0; i < depLists.size(); i++) {
                    type.add(depLists.get(i).getName());
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);

                depSpinner.setAdapter(arrayAdapter);


                desLists = new ArrayList<>();
                ArrayList<String> type1 = new ArrayList<>();
                for(int i = 0; i < desLists.size(); i++) {
                    type1.add(desLists.get(i).getName());
                }

                ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(getApplicationContext(),R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type1);

                desSpinner.setAdapter(arrayAdapter1);

                directoryAdapter = new DirectoryAdapter(DirectoryWithDivision.this, allDirectoryLists);
                directoryView.setAdapter(directoryAdapter);

            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(DirectoryWithDivision.this)
                        .setMessage("There is a network issue in the server. Please Try later")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel", null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    getDivisionWiseDepData();
                    dialog.dismiss();
                });

                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negative.setOnClickListener(v -> dialog.dismiss());
            }
        }
        else {
            AlertDialog dialog = new AlertDialog.Builder(DirectoryWithDivision.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel", null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                getDivisionWiseDepData();
                dialog.dismiss();
            });

            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> dialog.dismiss());
        }
    }

//    public void DesignationData() {
//        try {
//            this.connection = createConnection();
//            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();
//
//            Statement stmt = connection.createStatement();
//
//            desLists = new ArrayList<>();
//
//
//
//            if (div_id.isEmpty()) {
//                div_id = null;
//            }
//            if (dep_id.isEmpty()) {
//                dep_id = null;
//            }
//
////            ResultSet resultSet1 = stmt.executeQuery("select distinct desig_id, desig_name from emp_details_v where desig_name is not NULL and dept_id = "+dep_id+" and divm_id = "+div_id+"\n");
//
//            ResultSet resultSet1 = stmt.executeQuery("select distinct desig_mst.desig_id, desig_mst.desig_name \n" +
//                    "from desig_mst,job_setup_mst,dept_mst,division_mst \n" +
//                    "where desig_mst.desig_name is not NULL\n" +
//                    "and desig_mst.desig_id = job_setup_mst.jsm_desig_id\n" +
//                    "and job_setup_mst.jsm_divm_id = division_mst.divm_id\n" +
//                    "AND job_setup_mst.jsm_dept_id = dept_mst.dept_id\n" +
//                    "AND division_mst.divm_id = "+div_id+"\n" +
//                    "AND dept_mst.dept_id = "+dep_id+"");
//
//            while (resultSet1.next()) {
//                desLists.add(new TwoItemLists(resultSet1.getString(1),resultSet1.getString(2)));
//            }
////            categoryLists.add(new ReceiveTypeList("","All Categories"));
//
//            if (div_id == null) {
//                div_id = "";
//            }
//            if (dep_id == null) {
//                dep_id = "";
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
//            e.printStackTrace();
//        }
//    }

    public void getDesignations() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;

        desLists = new ArrayList<>();

        String url = "http://103.56.208.123:8001/apex/ttrams/directory/getDesignations/"+div_id+"/"+dep_id+"";

        RequestQueue requestQueue = Volley.newRequestQueue(DirectoryWithDivision.this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject desInfo = array.getJSONObject(i);

                        String desig_id_new = desInfo.getString("desig_id");
                        String desig_name_new = desInfo.getString("desig_name");

                        desLists.add(new TwoItemLists(desig_id_new,desig_name_new));
                    }
                }
                connected = true;
                updateAfterSelectingDEP();
            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                updateAfterSelectingDEP();
            }
        }, error -> {
            error.printStackTrace();
            conn = false;
            connected = false;
            updateAfterSelectingDEP();
        });

        requestQueue.add(stringRequest);
    }

    private void updateAfterSelectingDEP() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                conn = false;
                connected = false;

                ArrayList<String> type = new ArrayList<>();
                for(int i = 0; i < desLists.size(); i++) {
                    type.add(desLists.get(i).getName());
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);

                desSpinner.setAdapter(arrayAdapter);
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(DirectoryWithDivision.this)
                        .setMessage("There is a network issue in the server. Please Try later")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel", null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    getDesignations();
                    dialog.dismiss();
                });

                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negative.setOnClickListener(v -> dialog.dismiss());
            }
        }
        else {
            AlertDialog dialog = new AlertDialog.Builder(DirectoryWithDivision.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel", null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                getDesignations();
                dialog.dismiss();
            });

            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> dialog.dismiss());
        }
    }

}