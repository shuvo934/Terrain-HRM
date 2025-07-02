package ttit.com.shuvo.ikglhrm.EmployeeInfo;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;
import ttit.com.shuvo.ikglhrm.EmployeeInfo.chng_pass.UpdatePassword;
import ttit.com.shuvo.ikglhrm.EmployeeInfo.dialogue.ImageTakerChoiceDialog;
import ttit.com.shuvo.ikglhrm.EmployeeInfo.image_capture.CameraPreview;
import ttit.com.shuvo.ikglhrm.EmployeeInfo.interfaces.BitmapCallBack;
import ttit.com.shuvo.ikglhrm.EmployeeInfo.interfaces.PictureChooseListener;
import ttit.com.shuvo.ikglhrm.EmployeeInfo.jobDesc.JobDescription;
import ttit.com.shuvo.ikglhrm.EmployeeInfo.performance.PerformanceApp;
import ttit.com.shuvo.ikglhrm.EmployeeInfo.personal.PersonalData;
import ttit.com.shuvo.ikglhrm.EmployeeInfo.transcript.EMPTranscript;
import ttit.com.shuvo.ikglhrm.R;
import ttit.com.shuvo.ikglhrm.WaitProgress;

import static ttit.com.shuvo.ikglhrm.Login.CompanyName;
import static ttit.com.shuvo.ikglhrm.Login.userDesignations;
import static ttit.com.shuvo.ikglhrm.Login.userInfoLists;
import static ttit.com.shuvo.ikglhrm.utilities.Constants.api_url_front;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.yalantis.ucrop.UCrop;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmplyeeInformation extends AppCompatActivity implements PictureChooseListener, BitmapCallBack {

    CircleImageView empImage;
    ImageView captureImage;
    MaterialCardView personal;
    MaterialCardView jobDesc;
    MaterialCardView performance;
    MaterialCardView empTrans;

    TextView empName;
    TextView empDesignation;
    TextView empDep;
    TextView empDiv;
    TextView compName;

    MaterialButton changePass;

    Button back;

    String emp_id = "";

    Bitmap selectedImage;
    boolean imageFound = false;
    boolean imageToLoad = true;

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;
    String parsing_message = "";
    Logger logger = Logger.getLogger(EmplyeeInformation.class.getName());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emplyee_information);

        empImage = findViewById(R.id.employee_profile_image);
        captureImage = findViewById(R.id.emp_profile_camera_view);

        empName = findViewById(R.id.emp_profile_name);
        empDesignation = findViewById(R.id.emp_designation_name);
        empDep = findViewById(R.id.emp_department_name);
        empDiv = findViewById(R.id.emp_division_name);

        personal = findViewById(R.id.personal_data);
        jobDesc = findViewById(R.id.job_description);
        performance = findViewById(R.id.performance_app);
        empTrans = findViewById(R.id.emp_trans);

        compName = findViewById(R.id.name_of_company_emp_info);

        changePass = findViewById(R.id.change_password_button);

        if (!userInfoLists.isEmpty()) {
            String firstname = userInfoLists.get(0).getUser_fname();
            String lastName = userInfoLists.get(0).getUser_lname();
            if (firstname == null) {
                firstname = "";
            }
            if (lastName == null) {
                lastName = "";
            }
            String empFullName = firstname + " " + lastName;
            empName.setText(empFullName);
            emp_id = userInfoLists.get(0).getEmp_id();
        }

        if (!userDesignations.isEmpty()) {
            String jsmName = userDesignations.get(0).getDesg_name();
            if (jsmName == null) {
                jsmName = "";
            }
            empDesignation.setText(jsmName);

            String deptName = userDesignations.get(0).getDept_name();
            if (deptName == null) {
                deptName = "";
            }
            empDep.setText(deptName);

            String divName = userDesignations.get(0).getDiv_name();
            if (divName == null) {
                divName = "";
            }
            empDiv.setText(divName);
        }

        compName.setText(CompanyName);

        back = findViewById(R.id.emp_info_back);

        personal.setOnClickListener(v -> {
            imageToLoad = false;
            Intent intent = new Intent(EmplyeeInformation.this, PersonalData.class);
            startActivity(intent);
        });

        jobDesc.setOnClickListener(v -> {
            imageToLoad = false;
            Intent intent = new Intent(EmplyeeInformation.this, JobDescription.class);
            startActivity(intent);
        });

        performance.setOnClickListener(v -> {
            imageToLoad = false;
            Intent intent = new Intent(EmplyeeInformation.this, PerformanceApp.class);
            startActivity(intent);
        });

        empTrans.setOnClickListener(v -> {
            imageToLoad = false;
            Intent intent = new Intent(EmplyeeInformation.this, EMPTranscript.class);
            startActivity(intent);
        });

        captureImage.setOnClickListener(v -> {
            imageToLoad = false;
            ImageTakerChoiceDialog imageTakerChoiceDialog = new ImageTakerChoiceDialog();
            imageTakerChoiceDialog.show(getSupportFragmentManager(),"CH_IMAGE_EMP");
        });

        changePass.setOnClickListener(v -> {
            imageToLoad = true;
            Intent intent = new Intent(EmplyeeInformation.this, UpdatePassword.class);
            startActivity(intent);
        });

        back.setOnClickListener(v -> finish());

        imageToLoad = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (imageToLoad) {
            getUserImage();
        }
    }

    public void getUserImage() {
        waitProgress.show(getSupportFragmentManager(), "WaitBar");
        waitProgress.setCancelable(false);
        connected = false;
        conn = false;
        selectedImage = null;

        String userImageUrl = api_url_front + "utility/getProfilePic?p_emp_id=" + emp_id;

        RequestQueue requestQueue = Volley.newRequestQueue(EmplyeeInformation.this);

        StringRequest imageReq = new StringRequest(Request.Method.GET, userImageUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject userImageInfo = array.getJSONObject(i);
                        String emp_image = userImageInfo.getString("emp_image");
                        if (emp_image.equals("null") || emp_image.isEmpty()) {
                            System.out.println("NULL IMAGE");
                            imageFound = false;
                        } else {
                            byte[] decodedString = Base64.decode(emp_image, Base64.DEFAULT);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                            if (bitmap != null) {
                                imageFound = true;
                                selectedImage = bitmap;
                            } else {
                                imageFound = false;
                            }
                        }
                    }
                }
                connected = true;
                updateInterface();
            } catch (JSONException e) {
                connected = false;
                logger.log(Level.WARNING, e.getMessage(), e);
                updateInterface();
            }
        }, error -> {
            conn = false;
            connected = false;
            logger.log(Level.WARNING, error.getMessage(), error);
            updateInterface();
        });

        requestQueue.add(imageReq);

    }

    private void updateInterface() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                if (imageFound) {
                    Glide.with(EmplyeeInformation.this)
                            .load(selectedImage)
                            .fitCenter()
                            .into(empImage);
                }
                else {
                    empImage.setImageResource(R.drawable.profile);
                }
                conn = false;
                connected = false;
                imageToLoad = false;
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(EmplyeeInformation.this)
                        .setMessage("There is a network issue in the server. Please Try later")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel", null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {
                    getUserImage();
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
            AlertDialog dialog = new AlertDialog.Builder(EmplyeeInformation.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel", null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {
                getUserImage();
                dialog.dismiss();
            });

            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> {
                dialog.dismiss();
                finish();
            });
        }
    }

    ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                if (uri != null) {
                    try {
                        startCrop(uri);
                    } catch (Exception e) {
                        logger.log(Level.WARNING,e.getMessage(),e);
                        Toast.makeText(getApplicationContext(),"Failed to upload image",Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(),"Failed to get image",Toast.LENGTH_SHORT).show();
                }
            });

    private Bitmap getCorrectlyOrientedBitmap(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            if (bitmap == null) {
                return null;
            }

            // Get real file path (copying file if necessary)
            String realPath = copyFileToInternalStorage(uri);

            // Read EXIF data
            if (realPath != null) {
                return modifyOrientation(bitmap, realPath);
            }
            else {
                return null;
            }
        }
        catch (IOException e) {
            logger.log(Level.WARNING,e.getMessage(),e);
            Toast.makeText(getApplicationContext(),"Failed to upload image",Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    private String copyFileToInternalStorage(Uri uri) {
        File directory = getFilesDir(); // Internal storage
        File file = new File(directory, "temp_image.jpg");

        try (InputStream inputStream = getContentResolver().openInputStream(uri);
             OutputStream outputStream = new FileOutputStream(file)) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            return file.getAbsolutePath(); // Now you have the file path

        } catch (IOException e) {
            logger.log(Level.WARNING,e.getMessage(),e);
            Toast.makeText(getApplicationContext(),"Failed to get image path",Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    public static Bitmap modifyOrientation(Bitmap bitmap, String image_absolute_path) throws IOException {
        ExifInterface ei = new ExifInterface(image_absolute_path);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotate(bitmap, 90);

            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotate(bitmap, 180);

            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotate(bitmap, 270);

            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                return flip(bitmap, true, false);

            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                return flip(bitmap, false, true);

            default:
                return bitmap;
        }
    }

    public static Bitmap rotate(Bitmap bitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Bitmap flip(Bitmap bitmap, boolean horizontal, boolean vertical) {
        Matrix matrix = new Matrix();
        matrix.preScale(horizontal ? -1 : 1, vertical ? -1 : 1);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1113) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                if (data != null) {
                    Uri resultUri = data.getData();
                    try {
                        selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                        System.out.println("UPLOADED PIC");

                        updateUserImage();

                    } catch (IOException e) {
                        logger.log(Level.WARNING,e.getMessage(),e);
                        Toast.makeText(getApplicationContext(),"Failed to upload image",Toast.LENGTH_SHORT).show();
                    }
                }
            }
            else {
                Toast.makeText(getApplicationContext(),"Failed to get image",Toast.LENGTH_SHORT).show();
            }
        }
        else if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            if (data != null) {
                Uri croppedUri = UCrop.getOutput(data);
                selectedImage = getCorrectlyOrientedBitmap(croppedUri);
                if (selectedImage != null) {
                    updateUserImage();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Invalid image",Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(getApplicationContext(),"Failed to crop image",Toast.LENGTH_SHORT).show();
            }
        }
        else if (resultCode == UCrop.RESULT_ERROR) {
            Toast.makeText(getApplicationContext(), "Invalid Image", Toast.LENGTH_SHORT).show();
        }
    }

    public Bitmap resizeBitmap(Bitmap bitmap, int maxWidth, int maxHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        float scaleWidth = ((float) maxWidth) / width;
        float scaleHeight = ((float) maxHeight) / height;
        float scale = Math.min(scaleWidth, scaleHeight); // Maintain aspect ratio

        int newWidth = Math.round(width * scale);
        int newHeight = Math.round(height * scale);

        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
    }

    public byte[] compressBitmap(Bitmap bitmap, int maxSizeKB) {
        int quality = 100; // Start at highest quality
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        do {
            outputStream.reset(); // Clear the stream
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            quality -= 5; // Reduce quality in steps of 5
        } while (outputStream.toByteArray().length / 1024 > maxSizeKB && quality > 5);

        return outputStream.toByteArray();
    }

    public void updateUserImage() {
        waitProgress.show(getSupportFragmentManager(), "WaitBar");
        waitProgress.setCancelable(false);
        connected = false;
        conn = false;
        String url = api_url_front +"emp_information/updateImage";

        selectedImage = resizeBitmap(selectedImage, 1080,1080);

        RequestQueue requestQueue = Volley.newRequestQueue(EmplyeeInformation.this);

        byte[] finalBArray = compressBitmap(selectedImage,1024);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response ->  {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String string_out = jsonObject.getString("string_out");
                if (string_out.equals("Successfully Created")) {
                    System.out.println(string_out);
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
        }, error ->  {
            logger.log(Level.WARNING,error.getMessage(),error);
            parsing_message = error.getLocalizedMessage();
            conn = false;
            connected = false;
            updateLayout();
        })
        {
            @Override
            public byte[] getBody() {
                return finalBArray;
            }
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("p_emp_id",emp_id);
                return params;
            }

            @Override
            public String getBodyContentType() {
                return "application/binary";
            }
        };

        requestQueue.add(stringRequest);
    }

    private void updateLayout() {
        waitProgress.dismiss();
        if(conn) {
            if (connected) {
                conn = false;
                connected = false;

                Toast.makeText(getApplicationContext(), "Picture Uploaded", Toast.LENGTH_SHORT).show();

                try {
                    Glide.with(getApplicationContext())
                            .load(selectedImage)
                            .fitCenter()
                            .into(empImage);
                }
                catch (Exception e) {
                    empImage.setImageResource(R.drawable.profile);
                }
            }
            else {
                updateAlertMessage();
            }
        }
        else {
            updateAlertMessage();
        }
    }

    public void updateAlertMessage() {
        if (parsing_message != null) {
            if (parsing_message.isEmpty() || parsing_message.equals("null")) {
                parsing_message = "Server problem or Internet not connected";
            }
        }
        else {
            parsing_message = "Server problem or Internet not connected";
        }
        MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(EmplyeeInformation.this);
        alertDialogBuilder.setTitle("Error!")
                .setMessage("Error Message: "+parsing_message+".\n"+"Please try again.")
                .setPositiveButton("Retry", (dialog, which) -> {
                    updateUserImage();
                    dialog.dismiss();
                })
                .setNegativeButton("Cancel",(dialog, which) -> dialog.dismiss());

        AlertDialog alert = alertDialogBuilder.create();
        alert.setCancelable(false);
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }

    @Override
    public void onBitmapReceived(Bitmap bitmap) {
        Uri uri = getImageUri(this,bitmap);
        startCrop(uri);
    }

    @Override
    public void onPictureChoose(int type) {
        if (type == 1) {
            Intent intent = new Intent(this, CameraPreview.class);
            CameraPreview.setBitmapCallback(this);
            startActivity(intent);
        }
        else if (type == 2) {
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        }
    }

    private Uri getImageUri(Context context, Bitmap bitmap) {
        File file = new File(context.getCacheDir(), "temp_image.jpg"); // Store in cache
        try (FileOutputStream fos = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (IOException e) {
            logger.log(Level.WARNING,e.getMessage(),e);
        }
        return FileProvider.getUriForFile(context, "ttit.com.shuvo.ikglhrm.fileProvider", file);
    }

    private void startCrop(Uri sourceUri) {
        Uri destinationUri = Uri.fromFile(new File(getCacheDir(), "cropped_image.jpg"));

        UCrop.of(sourceUri, destinationUri)
                //.withAspectRatio(1, 1)  // Optional: Set aspect ratio
                .withMaxResultSize(1080, 1080) // Optional: Set max resolution
                .start(this);
    }
}