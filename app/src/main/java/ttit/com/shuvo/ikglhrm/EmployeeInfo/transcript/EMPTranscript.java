package ttit.com.shuvo.ikglhrm.EmployeeInfo.transcript;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ScrollView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ttit.com.shuvo.ikglhrm.R;
import ttit.com.shuvo.ikglhrm.attendance.report.AttendanceReport;

public class EMPTranscript extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Button transFinish;
    ScrollView scrollView;

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
        //getWindow().setNavigationBarColor(Color.parseColor("#f0932b"));
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
        window.setStatusBarColor(ContextCompat.getColor(EMPTranscript.this,R.color.secondaryColor));

        setContentView(R.layout.activity_e_m_p_transcript);

        transFinish = findViewById(R.id.transcript_finish);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        scrollView = findViewById(R.id.emp_trans_scroll_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(onClickListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FirstFragment(EMPTranscript.this)).commit();

        transFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener onClickListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {


                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFrag = null;

                    switch (item.getItemId()) {
                        case R.id.first_fragment:
                            selectedFrag = new FirstFragment(EMPTranscript.this);
                            item.setIcon(R.drawable.star);

                            Menu menu = bottomNavigationView.getMenu();
                            menu.findItem(R.id.second_fragment).setIcon(R.drawable.star_border);

                            break;
                        case R.id.second_fragment:
                            selectedFrag = new SecondFragment(EMPTranscript.this);
                            item.setIcon(R.drawable.star);

                            Menu menu1 = bottomNavigationView.getMenu();
                            menu1.findItem(R.id.first_fragment).setIcon(R.drawable.star_border);

                            break;
                    }
                    scrollView.smoothScrollTo(0,0);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFrag).commit();
                    return true;
                }
            };
}