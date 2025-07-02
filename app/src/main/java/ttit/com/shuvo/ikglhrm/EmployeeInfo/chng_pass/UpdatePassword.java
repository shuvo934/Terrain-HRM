package ttit.com.shuvo.ikglhrm.EmployeeInfo.chng_pass;

import static ttit.com.shuvo.ikglhrm.Login.userInfoLists;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.EMP_PASSWORD;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.LOGIN_ACTIVITY_FILE;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.MyPREFERENCES;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.api_url_front;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.checked;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.user_password;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.jakewharton.processphoenix.ProcessPhoenix;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import ttit.com.shuvo.ikglhrm.R;

public class UpdatePassword extends AppCompatActivity {

    LinearLayout fullLayout;
    CircularProgressIndicator circularProgressIndicator;
    TextInputLayout currentPassLay;
    TextInputEditText currentPass;
    TextInputLayout newPassLay;
    TextInputEditText newPass;
    TextInputLayout confirmLay;
    TextInputEditText confirmPass;

    ImageView close;

    Button update;
    SharedPreferences sharedpreferences;
    SharedPreferences sharedLoginRemember;
    String getPassword = "";
    String emp_password = "";
    boolean getChecked = false;

    private Boolean conn = false;
    private Boolean connected = false;
    String parsing_message = "";
    boolean loading = false;

    String confirm_pass = "";
    String usr_mail = "";

    Logger logger = Logger.getLogger(UpdatePassword.class.getName());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        currentPassLay = findViewById(R.id.current_password_text_layout);
        currentPass = findViewById(R.id.current_password_text);
        newPassLay = findViewById(R.id.new_password_text_layout);
        newPass = findViewById(R.id.new_password_text);
        confirmPass = findViewById(R.id.confirm_password_text);
        confirmLay = findViewById(R.id.confirm_password_text_layout);

        close = findViewById(R.id.close_logo_of_emp_password_update);
        update = findViewById(R.id.emp_change_pass_button);

        fullLayout = findViewById(R.id.emp_update_password_full_layout);
        circularProgressIndicator = findViewById(R.id.progress_indicator_emp_profile_update_password);
        circularProgressIndicator.setVisibility(View.GONE);

        sharedpreferences = getSharedPreferences(LOGIN_ACTIVITY_FILE, MODE_PRIVATE);
        sharedLoginRemember = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);

        getPassword = sharedLoginRemember.getString(user_password,null);
        getChecked = sharedLoginRemember.getBoolean(checked,false);

        emp_password = sharedpreferences.getString(EMP_PASSWORD,"");

        if (userInfoLists == null) {
            restart("Could Not Get Doctor Data. Please Restart the App.");
        }
        else {
            if (userInfoLists.isEmpty()) {
                restart("Could Not Get Doctor Data. Please Restart the App.");
            }
            else {
                usr_mail = userInfoLists.get(0).getEmail();
            }
        }

        close.setOnClickListener(v -> finish());

        currentPass.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                    event.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT) {
                if (event == null || !event.isShiftPressed()) {
                    // the user is done typing.
                    Log.i("Let see", "Come here");
                    currentPass.clearFocus();
                    closeKeyBoard();

                    return false; // consume.
                }
            }
            return false;
        });

        newPass.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                    event.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT) {
                if (event == null || !event.isShiftPressed()) {
                    // the user is done typing.
                    Log.i("Let see", "Come here");
                    newPass.clearFocus();
                    closeKeyBoard();

                    return false; // consume.
                }
            }
            return false;
        });

        confirmPass.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                    event.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT) {
                if (event == null || !event.isShiftPressed()) {
                    // the user is done typing.
                    Log.i("Let see", "Come here");
                    confirmPass.clearFocus();
                    closeKeyBoard();

                    return false; // consume.
                }
            }
            return false;
        });

        currentPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currentPassLay.setHelperText("");
            }
        });

        newPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                newPassLay.setHelperText("");
            }
        });

        confirmPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                confirmLay.setHelperText("");
            }
        });

        update.setOnClickListener(v -> {
            String current_pass = Objects.requireNonNull(currentPass.getText()).toString();
            String new_pass = Objects.requireNonNull(newPass.getText()).toString();
            confirm_pass = Objects.requireNonNull(confirmPass.getText()).toString();
            if (!current_pass.isEmpty()) {

                if (emp_password.equals(current_pass)) {

                    if (!new_pass.isEmpty()) {
                        if(new_pass.contains(" ") || new_pass.contains("\n") || new_pass.contains("'") || new_pass.contains("\"")) {
                            newPassLay.setHelperText("Invalid Character! Blank space, Quotation or New line is not allowed");
                            Toast.makeText(UpdatePassword.this, "Invalid Character! Blank space, Quotation or New line is not allowed", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            if (!new_pass.equals(current_pass)) {
                                if (!confirm_pass.isEmpty()) {
                                    if (confirm_pass.contains(" ") || confirm_pass.contains("\n") || confirm_pass.contains("'") || confirm_pass.contains("\"")) {
                                        confirmLay.setHelperText("Invalid Character! Blank space, Quotation or New line is not allowed");
                                        Toast.makeText(UpdatePassword.this, "Invalid Character! Blank space, Quotation or New line is not allowed", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        if (new_pass.equals(confirm_pass)) {
                                            updatePassword();
                                        }
                                        else {
                                            confirmLay.setHelperText("Confirm Password did not match");
                                            Toast.makeText(UpdatePassword.this, "Confirm Password did not match", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                                else {
                                    confirmLay.setHelperText("Please Provide Confirm Password");
                                    Toast.makeText(UpdatePassword.this, "Please Provide Confirm Password to Update", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                newPassLay.setHelperText("New Password is matched with Current Password.");
                                Toast.makeText(UpdatePassword.this, "New Password is matched with Current Password.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    else {
                        newPassLay.setHelperText("Please Provide New Password");
                        Toast.makeText(UpdatePassword.this, "Please Provide New Password to Update", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    currentPassLay.setHelperText("Current Password is Wrong");
                    Toast.makeText(UpdatePassword.this, "Current Password is Wrong", Toast.LENGTH_SHORT).show();

                }
            }
            else {
                currentPassLay.setHelperText("Please Provide Current Password");
                Toast.makeText(UpdatePassword.this, "Please Provide Current Password", Toast.LENGTH_SHORT).show();
            }
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (loading) {
                    Toast.makeText(getApplicationContext(),"Please wait while loading",Toast.LENGTH_SHORT).show();
                }
                else {
                    finish();
                }
            }
        });
    }

    private void closeKeyBoard () {
        View view = this.getCurrentFocus();
        if (view != null) {
            view.clearFocus();
            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void updatePassword() {
        fullLayout.setVisibility(View.GONE);
        circularProgressIndicator.setVisibility(View.VISIBLE);
        conn = false;
        connected = false;
        loading = true;

        String updatePassUrl = api_url_front+"emp_information/updatePassword";

        RequestQueue requestQueue = Volley.newRequestQueue(UpdatePassword.this);

        StringRequest attReq = new StringRequest(Request.Method.POST, updatePassUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String string_out = jsonObject.getString("string_out");
                if (string_out.equals("Successfully Created")) {
                    connected = true;
                }
                else {
                    parsing_message = string_out;
                    System.out.println(string_out);
                    connected = false;
                }
                updateLayout();
            }
            catch (JSONException e) {
                logger.log(Level.WARNING,e.getMessage(),e);
                parsing_message = e.getLocalizedMessage();
                connected = false;
                updateLayout();
            }
        }, error -> {
            logger.log(Level.WARNING,error.getMessage(),error);
            parsing_message = error.getLocalizedMessage();
            conn = false;
            connected = false;
            updateLayout();
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("P_MAIL",usr_mail);
                headers.put("P_PASS",confirm_pass);
                return  headers;
            }
        };

        requestQueue.add(attReq);
    }

    private void updateLayout() {
        loading = false;
        if(conn) {
            if (connected) {
                fullLayout.setVisibility(View.VISIBLE);
                circularProgressIndicator.setVisibility(View.GONE);
                conn = false;
                connected = false;

                if (getChecked) {
                    SharedPreferences.Editor editor = sharedLoginRemember.edit();
                    editor.remove(user_password);
                    editor.putString(user_password,confirm_pass);
                    editor.apply();
                    editor.commit();
                }
                SharedPreferences.Editor editor1 = sharedpreferences.edit();
                editor1.remove(EMP_PASSWORD);
                editor1.putString(EMP_PASSWORD, confirm_pass);
                editor1.apply();
                editor1.commit();

                MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(UpdatePassword.this);
                alertDialogBuilder.setTitle("Success!")
                        .setMessage("Password Updated Successfully")
                        .setPositiveButton("OK", (dialog, which) -> {
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
            else {
                alertMessage();
            }
        }
        else {
            alertMessage();
        }
    }

    public void alertMessage() {
        fullLayout.setVisibility(View.VISIBLE);
        circularProgressIndicator.setVisibility(View.GONE);
        if (parsing_message != null) {
            if (parsing_message.isEmpty() || parsing_message.equals("null")) {
                parsing_message = "Server problem or Internet not connected";
            }
        }
        else {
            parsing_message = "Server problem or Internet not connected";
        }
        MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(UpdatePassword.this);
        alertDialogBuilder.setTitle("Error!")
                .setMessage("Error Message: "+parsing_message+".\n"+"Please try again.")
                .setPositiveButton("Retry", (dialog, which) -> {
                    updatePassword();
                    dialog.dismiss();
                })
                .setNegativeButton("Exit",(dialog, which) -> {
                    dialog.dismiss();
                    finish();
                });

        AlertDialog alert = alertDialogBuilder.create();
        try {
            alert.show();
        }
        catch (Exception e) {
            restart("App is paused for a long time. Please Start the app again.");
        }
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