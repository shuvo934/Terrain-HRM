package ttit.com.shuvo.ikglhrm.EmployeeInfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import ttit.com.shuvo.ikglhrm.EmployeeInfo.jobDesc.JobDescription;
import ttit.com.shuvo.ikglhrm.EmployeeInfo.performance.PerformanceApp;
import ttit.com.shuvo.ikglhrm.EmployeeInfo.personal.PersonalData;
import ttit.com.shuvo.ikglhrm.EmployeeInfo.transcript.EMPTranscript;
import ttit.com.shuvo.ikglhrm.R;

import static ttit.com.shuvo.ikglhrm.Login.CompanyName;
import static ttit.com.shuvo.ikglhrm.Login.SoftwareName;

public class EmplyeeInformation extends AppCompatActivity {

    CardView personal;
    CardView jobDesc;
    CardView performance;
    CardView empTrans;

    TextView softName;
    TextView compName;

    Button back;

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

        setContentView(R.layout.activity_emplyee_information);

        personal = findViewById(R.id.personal_data);
        jobDesc = findViewById(R.id.job_description);
        performance = findViewById(R.id.performance_app);
        empTrans = findViewById(R.id.emp_trans);

        compName = findViewById(R.id.name_of_company_emp_info);
        softName = findViewById(R.id.name_of_soft_emp_info);

        String soft = CompanyName;
        softName.setText(soft);
        compName.setText(SoftwareName);

        back = findViewById(R.id.emp_info_back);

        personal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(EmplyeeInformation.this, PersonalData.class);
                startActivity(intent);
            }
        });

        jobDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(EmplyeeInformation.this, JobDescription.class);
                startActivity(intent);
            }
        });

        performance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmplyeeInformation.this, PerformanceApp.class);
                startActivity(intent);
            }
        });

        empTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmplyeeInformation.this, EMPTranscript.class);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}