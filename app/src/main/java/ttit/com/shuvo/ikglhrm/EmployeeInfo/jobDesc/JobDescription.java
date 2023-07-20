package ttit.com.shuvo.ikglhrm.EmployeeInfo.jobDesc;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import ttit.com.shuvo.ikglhrm.R;
import ttit.com.shuvo.ikglhrm.WaitProgress;

import static ttit.com.shuvo.ikglhrm.Login.userInfoLists;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class JobDescription extends AppCompatActivity {

    TextView no_job;
    Button jobFinish;
    RecyclerView job_list;
    public static JobAdapter jobAdapter;
    RecyclerView.LayoutManager layoutManager;

    public static ArrayList<JobDescDetails> jobDescDetails;

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
//            //w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
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
        window.setStatusBarColor(ContextCompat.getColor(JobDescription.this,R.color.secondaryColor));

        setContentView(R.layout.activity_job_description);

        emp_id = userInfoLists.get(0).getEmp_id();
        no_job = findViewById(R.id.no_job);
        job_list = findViewById(R.id.job_desc_list);
        jobFinish = findViewById(R.id.job_finish);

        jobDescDetails = new ArrayList<>();

//        new Check().execute();
        getJobDescription();

        job_list.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        job_list.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(job_list.getContext(),DividerItemDecoration.VERTICAL);
        job_list.addItemDecoration(dividerItemDecoration);



        jobFinish.setOnClickListener(new View.OnClickListener() {
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
//                JobDetails();
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
//                jobAdapter = new JobAdapter(jobDescDetails, JobDescription.this);
//                job_list.setAdapter(jobAdapter);
//                if (jobDescDetails.size() == 0) {
//                    no_job.setVisibility(View.VISIBLE);
//                    job_list.setVisibility(View.GONE);
//                } else {
//                    no_job.setVisibility(View.GONE);
//                    job_list.setVisibility(View.VISIBLE);
//                }
//
//                conn = false;
//                connected = false;
//
//
//
//            }
//            else {
//                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
//                AlertDialog dialog = new AlertDialog.Builder(JobDescription.this)
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
//
//                        dialog.dismiss();
//                        finish();
//                    }
//                });
//            }
//        }
//    }

//    public void JobDetails() {
//        try {
//            this.connection = createConnection();
//            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();
//
//
//            jobDescDetails = new ArrayList<>();
//            Statement stmt = connection.createStatement();
//
//
//
//
//            ResultSet rs=stmt.executeQuery("SELECT EJD_JSDD_NAME FROM EMP_JOB_DESCRIPTION WHERE EJD_EMP_ID ="+emp_id+"");
//
//
//            int i = 0;
//            while(rs.next()) {
//
//                i++;
//                jobDescDetails.add(new JobDescDetails(String.valueOf(i),rs.getString(1)));
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

    public void getJobDescription() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        connected = false;
        conn = false;

        jobDescDetails = new ArrayList<>();

        String jobDescUrl = "http://103.56.208.123:8001/apex/ttrams/emp_information/getJobDescription/"+emp_id+"";

        RequestQueue requestQueue = Volley.newRequestQueue(JobDescription.this);

        StringRequest jobDescReq = new StringRequest(Request.Method.GET, jobDescUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    int j = 0;
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jobDescInfo = array.getJSONObject(i);
                        String ejd_jsdd_name = jobDescInfo.getString("ejd_jsdd_name");
                        ejd_jsdd_name = transformText(ejd_jsdd_name);
                        j++;
                        jobDescDetails.add(new JobDescDetails(String.valueOf(j),ejd_jsdd_name));
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

        requestQueue.add(jobDescReq);
    }

    private void updateLayout() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                jobAdapter = new JobAdapter(jobDescDetails, JobDescription.this);
                job_list.setAdapter(jobAdapter);
                if (jobDescDetails.size() == 0) {
                    no_job.setVisibility(View.VISIBLE);
                    job_list.setVisibility(View.GONE);
                } else {
                    no_job.setVisibility(View.GONE);
                    job_list.setVisibility(View.VISIBLE);
                }
                conn = false;
                connected = false;
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(JobDescription.this)
                        .setMessage("There is a network issue in the server. Please Try later")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    getJobDescription();
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
            AlertDialog dialog = new AlertDialog.Builder(JobDescription.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel",null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                getJobDescription();
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