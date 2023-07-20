package ttit.com.shuvo.ikglhrm.EmployeeInfo.performance;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import ttit.com.shuvo.ikglhrm.EmployeeInfo.jobDesc.JobAdapter;
import ttit.com.shuvo.ikglhrm.EmployeeInfo.jobDesc.JobDescDetails;
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

public class PerformanceApp extends AppCompatActivity {


    TextView no_gpi, no_kpi;
    Button gpikpiFinish;
    RecyclerView gpiList;
    public static JobAdapter gpiAdapter;
    RecyclerView.LayoutManager layoutManager;

    RecyclerView kpiList;
    public static JobAdapter kpiAdapter;
    RecyclerView.LayoutManager layoutManager1;

    public static ArrayList<JobDescDetails> gpiDetails;
    public static ArrayList<JobDescDetails> kpiDetails;

    WaitProgress waitProgress = new WaitProgress();
//    private String message = null;
    private Boolean conn = false;
//    private Boolean infoConnected = false;
    private Boolean connected = false;

//    private Connection connection;

    String emp_id = "";

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
//           // w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        }
//        if (Build.VERSION.SDK_INT < 16) {
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        }
//        View decorView = getWindow().getDecorView();
//        // Hide the status bar.
//        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
//        decorView.setSystemUiVisibility(uiOptions);
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(PerformanceApp.this,R.color.secondaryColor));

        setContentView(R.layout.activity_performance_app);

        emp_id = userInfoLists.get(0).getEmp_id();

        no_gpi = findViewById(R.id.no_gpi);
        no_kpi = findViewById(R.id.no_kpi);

        gpikpiFinish = findViewById(R.id.gpi_kpi_finish);

        gpiDetails = new ArrayList<>();
        kpiDetails = new ArrayList<>();

        gpiList = findViewById(R.id.gpi_list);
        kpiList = findViewById(R.id.kpi_list);

//        new Check().execute();
        getGpiKpiDetails();

        gpiList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        gpiList.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(gpiList.getContext(),DividerItemDecoration.VERTICAL);
        gpiList.addItemDecoration(dividerItemDecoration);



        kpiList.setHasFixedSize(true);
        layoutManager1 = new LinearLayoutManager(this);
        kpiList.setLayoutManager(layoutManager1);
        DividerItemDecoration dividerItemDecoration1 = new DividerItemDecoration(kpiList.getContext(), DividerItemDecoration.VERTICAL);
        kpiList.addItemDecoration(dividerItemDecoration1);



        gpikpiFinish.setOnClickListener(new View.OnClickListener() {
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
//                GpiDetails();
//                KpiDetails();
//                if (connected & infoConnected) {
//                    conn = true;
//                    message= "Internet Connected";
//                }
//
//            } else {
//                conn = false;
//                connected = false;
//                infoConnected = false;
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
//                gpiAdapter = new JobAdapter(gpiDetails, PerformanceApp.this);
//                kpiAdapter = new JobAdapter(kpiDetails,PerformanceApp.this);
//                gpiList.setAdapter(gpiAdapter);
//                kpiList.setAdapter(kpiAdapter);
//                if (gpiDetails.size() == 0) {
//                    no_gpi.setVisibility(View.VISIBLE);
//                    gpiList.setVisibility(View.GONE);
//                } else {
//                    no_gpi.setVisibility(View.GONE);
//                    gpiList.setVisibility(View.VISIBLE);
//                }
//
//                if (kpiDetails.size() == 0) {
//                    no_kpi.setVisibility(View.VISIBLE);
//                    kpiList.setVisibility(View.GONE);
//                } else {
//                    no_kpi.setVisibility(View.GONE);
//                    kpiList.setVisibility(View.VISIBLE);
//                }
//
//                connected = false;
//                infoConnected = false;
//                conn = false;
//
//
//
//            }
//            else {
//                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
//                AlertDialog dialog = new AlertDialog.Builder(PerformanceApp.this)
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
//
//                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
//                negative.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        dialog.dismiss();
//                        finish();
//                    }
//                });
//            }
//        }
//    }
//
//    public void GpiDetails() {
//        try {
//            this.connection = createConnection();
//            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();
//
//
//            gpiDetails = new ArrayList<>();
//            Statement stmt = connection.createStatement();
//
//
//
//
//            ResultSet rs=stmt.executeQuery("SELECT EGI_PGPI_FACTOR FROM EMP_GPI_INDICATOR WHERE EGI_EMP_ID ="+emp_id+"");
//
//
//            int i = 0;
//            while(rs.next()) {
//
//                i++;
//                gpiDetails.add(new JobDescDetails(String.valueOf(i),rs.getString(1)));
//
//            }
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
//    public void KpiDetails() {
//        try {
//            this.connection = createConnection();
//            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();
//
//
//            kpiDetails = new ArrayList<>();
//            Statement stmt = connection.createStatement();
//
//
//
//
//            ResultSet rs=stmt.executeQuery("SELECT EKI_KPI_FACTOR FROM EMP_KPI_INDICATOR WHERE EKI_EMP_ID ="+emp_id+"");
//
//
//            int i = 0;
//            while(rs.next()) {
//
//                i++;
//                kpiDetails.add(new JobDescDetails(String.valueOf(i),rs.getString(1)));
//
//            }
//
//
//            infoConnected = true;
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

    public void getGpiKpiDetails() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;

        gpiDetails = new ArrayList<>();
        kpiDetails = new ArrayList<>();

        String gpiUrl = "http://103.56.208.123:8001/apex/ttrams/emp_information/getGpiDetails/"+emp_id+"";
        String kpiUrl = "http://103.56.208.123:8001/apex/ttrams/emp_information/getKpiDetails/"+emp_id+"";

        RequestQueue requestQueue = Volley.newRequestQueue(PerformanceApp.this);

        StringRequest kpiReq = new StringRequest(Request.Method.GET, kpiUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    int j = 0;
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject kpiInfo = array.getJSONObject(i);
                        String eki_kpi_factor = kpiInfo.getString("eki_kpi_factor");
                        eki_kpi_factor = transformText(eki_kpi_factor);
                        j++;
                        kpiDetails.add(new JobDescDetails(String.valueOf(j),eki_kpi_factor));
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
        }, error -> {
            conn = false;
            connected = false;
            error.printStackTrace();
            updateLayout();
        });

        StringRequest gpiReq = new StringRequest(Request.Method.GET, gpiUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    int j = 0;
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject gpiInfo = array.getJSONObject(i);
                        String egi_pgpi_factor = gpiInfo.getString("egi_pgpi_factor");
                        egi_pgpi_factor = transformText(egi_pgpi_factor);
                        j++;
                        gpiDetails.add(new JobDescDetails(String.valueOf(j),egi_pgpi_factor));
                    }
                }
                requestQueue.add(kpiReq);
            }
            catch (JSONException e) {
                connected = false;
                e.printStackTrace();
                updateLayout();
            }
        }, error -> {
            conn = false;
            connected = false;
            error.printStackTrace();
            updateLayout();
        });

        requestQueue.add(gpiReq);
    }

    private void updateLayout() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                gpiAdapter = new JobAdapter(gpiDetails, PerformanceApp.this);
                kpiAdapter = new JobAdapter(kpiDetails,PerformanceApp.this);
                gpiList.setAdapter(gpiAdapter);
                kpiList.setAdapter(kpiAdapter);
                if (gpiDetails.size() == 0) {
                    no_gpi.setVisibility(View.VISIBLE);
                    gpiList.setVisibility(View.GONE);
                } else {
                    no_gpi.setVisibility(View.GONE);
                    gpiList.setVisibility(View.VISIBLE);
                }

                if (kpiDetails.size() == 0) {
                    no_kpi.setVisibility(View.VISIBLE);
                    kpiList.setVisibility(View.GONE);
                } else {
                    no_kpi.setVisibility(View.GONE);
                    kpiList.setVisibility(View.VISIBLE);
                }

                connected = false;
                conn = false;
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(PerformanceApp.this)
                        .setMessage("There is a network issue in the server. Please Try later")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    getGpiKpiDetails();
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
            AlertDialog dialog = new AlertDialog.Builder(PerformanceApp.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel",null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                getGpiKpiDetails();
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