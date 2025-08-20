package ttit.com.shuvo.ikglhrm.EmployeeInfo.jobDesc;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import ttit.com.shuvo.ikglhrm.R;
import ttit.com.shuvo.ikglhrm.WaitProgress;

import static ttit.com.shuvo.ikglhrm.user_login.Login.userInfoLists;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.api_url_front;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.jakewharton.processphoenix.ProcessPhoenix;

public class JobDescription extends AppCompatActivity {

    TextView no_job;
    RecyclerView job_list;
    JobAdapter jobAdapter;
    RecyclerView.LayoutManager layoutManager;

    ArrayList<JobDescDetails> jobDescDetails;

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;

    String emp_id = "";

    Logger logger = Logger.getLogger(JobDescription.class.getName());
    String parsing_message = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_description);

        if (userInfoLists == null) {
            restart("Could Not Get Employee Data. Please Restart the App.");
        }
        else {
            if (userInfoLists.isEmpty()) {
                restart("Could Not Get Employee Data. Please Restart the App.");
            }
            else {
                emp_id = userInfoLists.get(0).getEmp_id();
            }
        }
        no_job = findViewById(R.id.no_job);
        job_list = findViewById(R.id.job_desc_list);

        jobDescDetails = new ArrayList<>();

        getJobDescription();

        job_list.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        job_list.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(job_list.getContext(),DividerItemDecoration.VERTICAL);
        job_list.addItemDecoration(dividerItemDecoration);
    }

    public void getJobDescription() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        connected = false;
        conn = false;

        jobDescDetails = new ArrayList<>();

        String jobDescUrl = api_url_front + "emp_information/getJobDescription/"+emp_id;

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
                logger.log(Level.WARNING, e.getMessage(), e);
                parsing_message = e.getLocalizedMessage();
                updateLayout();
            }
        },error -> {
            conn = false;
            connected = false;
            logger.log(Level.WARNING, error.getMessage(), error);
            parsing_message = error.getLocalizedMessage();
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
                if (jobDescDetails.isEmpty()) {
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
                alertMessage();
            }
        }
        else {
            alertMessage();
        }
    }

    public void alertMessage() {
        if (parsing_message != null) {
            if (parsing_message.isEmpty() || parsing_message.equals("null")) {
                parsing_message = "Server problem or Internet not connected";
            }
        }
        else {
            parsing_message = "Server problem or Internet not connected";
        }
        MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(JobDescription.this);
        alertDialogBuilder.setTitle("System Warning!")
                .setIcon(R.drawable.hrm_new_round_icon_custom)
                .setMessage("Message: "+parsing_message+".\n"+"Please try again.")
                .setPositiveButton("Retry", (dialog, which) -> {
                    getJobDescription();
                    dialog.dismiss();
                })
                .setNegativeButton("Cancel",(dialog, which) -> {
                    dialog.dismiss();
                    finish();
                });

        AlertDialog alert = alertDialogBuilder.create();
        alert.setCancelable(false);
        alert.setCanceledOnTouchOutside(false);
        try {
            alert.show();
        }
        catch (Exception e) {
            restart("App is paused for a long time. Please Start the app again.");
        }
    }

    //    --------------------------Transforming Bangla Text-----------------------------
    private String transformText(String text) {
        byte[] bytes = text.getBytes(ISO_8859_1);
        return new String(bytes, UTF_8);
    }

    public void restart(String msg) {
        try {
            ProcessPhoenix.triggerRebirth(getApplicationContext());
        }
        catch (Exception e) {
            Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
            System.exit(0);
        }
    }
}