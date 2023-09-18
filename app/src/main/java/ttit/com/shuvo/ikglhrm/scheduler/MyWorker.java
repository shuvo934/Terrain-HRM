package ttit.com.shuvo.ikglhrm.scheduler;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import ttit.com.shuvo.ikglhrm.R;

import static android.content.Context.MODE_PRIVATE;
import static ttit.com.shuvo.ikglhrm.scheduler.Uploader.channelId;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyWorker extends Worker {

//    private String message = null;
    private Boolean conn = false;
    private Boolean connected = false;


//    private Connection connection;

    String emp_id = "";
    String emp_code = "";
    int tracking_flag = 0;
    int dateCount = 0;
    int retryNo = 0;
    boolean updated = false;

    ArrayList<String> lastTenDays;
    ArrayList<String> lastTenDaysFromSQL;
    ArrayList<String> updatedFiles;
    ArrayList<String> updatedXml;
    Context mContext;

    SharedPreferences sharedPreferencesDA;
    public static String FILE_OF_DAILY_ACTIVITY = "";
    public static  String DISTANCE = "DISTANCE";
    public static  String TOTAL_TIME = "TOTAL_TIME";
    public static  String STOPPED_TIME = "STOPPED_TIME";


    public static final String TASK_DESC_EMP_ID = "task_desc";
    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {

        super(context, workerParams);
        mContext = context;
    }

    @NonNull
    @Override
    public Result doWork() {

        emp_id = getInputData().getString(TASK_DESC_EMP_ID);
        UploadingFile();
        return Result.success();
    }


    private void UploadingFile() {

        lastTenDays = new ArrayList<>();
        lastTenDaysFromSQL = new ArrayList<>();
        updatedFiles = new ArrayList<>();
        updatedXml = new ArrayList<>();
        updated = false;

        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_YEAR, -11);

        for (int i = 0 ; i < 10 ;i ++) {
            cal.add(Calendar.DAY_OF_YEAR, +1);
            Date calTime = cal.getTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);
            String ddd = simpleDateFormat.format(calTime);

            ddd = ddd.toUpperCase();
            System.out.println(ddd);
            lastTenDays.add(ddd);
        }

//        if (isConnected() && isOnline()) {
//            new Check().execute();
        getEmpData();
//        } else {
//            NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, channelId)
//                    .setSmallIcon(R.drawable.thrn_logo)
//                    .setContentTitle("Tracking Service")
//                    .setContentText("Failed to Upload File. Your Internet is not Connected")
//                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//
//            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(mContext);
////        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
////        builder.setSound(alarmSound);
//            notificationManagerCompat.notify(200, builder.build());
//        }
    }

//    public boolean isConnected() {
//        boolean connected = false;
//        boolean isMobile = false;
//        try {
//            ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
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
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
////            if (isConnected() && isOnline()) {
//
//                AttendanceGraph();
//                if (connected) {
//                    conn = true;
//                    message= "Internet Connected";
//                }
//
////            } else {
////                conn = false;
////                message = "Not Connected";
////            }
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//
//            if (conn) {
//
//                if (dateCount > 0) {
//
//                    if (updatedFiles.size() != 0) {
//                        for (int i = 0; i < updatedFiles.size(); i++) {
//                            String stringFile = updatedFiles.get(i);
//                            File blobFile = new File(stringFile);
//                            if (blobFile.exists()) {
//                                boolean deleted = blobFile.delete();
//                                if (deleted) {
//                                    System.out.println("Deleted");
//                                }
//                            }
//                        }
//                    }
//                    if (updatedXml.size() != 0) {
//                        for (int i = 0 ; i < updatedXml.size(); i++) {
//
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                                File dir = new File(mContext.getApplicationInfo().dataDir, "shared_prefs/" + updatedXml.get(i)+ ".xml");
//                                if(dir.exists()) {
//                                    mContext.getSharedPreferences(updatedXml.get(i), MODE_PRIVATE).edit().clear().apply();
//                                    boolean ddd = dir.delete();
//                                    System.out.println(ddd);
//                                } else {
//                                    System.out.println(false);
//                                }
//                            }
//                        }
//                    }
//
//                    if (dateCount == 1) {
//                        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, channelId)
//                                .setSmallIcon(R.drawable.thrn_logo)
//                                .setContentTitle("Tracking Service")
//                                .setContentText(dateCount + " File Uploaded")
//                                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//
//                        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(mContext);
////        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
////        builder.setSound(alarmSound);
//                        notificationManagerCompat.notify(200, builder.build());
//                    } else {
//                        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, channelId)
//                                .setSmallIcon(R.drawable.thrn_logo)
//                                .setContentTitle("Tracking Service")
//                                .setContentText(dateCount + " Files Uploaded")
//                                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//
//                        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(mContext);
////        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
////        builder.setSound(alarmSound);
//                        notificationManagerCompat.notify(200, builder.build());
//                    }
//
//                }
////                else {
////                    NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, channelId)
////                            .setSmallIcon(R.drawable.thrn_logo)
////                            .setContentTitle("Tracking Service")
////                            .setContentText("No Files Found")
////                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);
////
////                    NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(mContext);
//////                    Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//////                    builder.setSound(alarmSound);
////                    notificationManagerCompat.notify(200, builder.build());
////                }
//
//                conn = false;
//                connected = false;
//
//            }else {
//
//                retryNo++;
//                if (retryNo <= 3) {
//                    new Check().execute();
//                }
////                else {
////                    NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, channelId)
////                            .setSmallIcon(R.drawable.thrn_logo)
////                            .setContentTitle("Tracking Service")
////                            .setContentText("Failed to Upload File. Could not Connect to the Server.")
////                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);
////
////                    NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(mContext);
//////        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//////        builder.setSound(alarmSound);
////                    notificationManagerCompat.notify(200, builder.build());
////                }
//
//
//
//            }
//        }
//    }

//    public void AttendanceGraph() {
//        try {
//            this.connection = createConnection();
//            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();
//            lastTenDaysFromSQL = new ArrayList<>();
//            //updatedFiles = new ArrayList<>();
//
//            Statement stmt = connection.createStatement();
//
//
//
//            int job_id = 0;
//            String coa_id = "";
//            String divm_id = "";
//            String dept_id = "";
//            ResultSet resultSet2 = stmt.executeQuery("SELECT\n" +
//                    "    emp_mst.emp_timeline_tracker_flag,\n" +
//                    "    emp_mst.emp_job_id,\n" +
//                    "    emp_job_history.job_pri_coa_id,\n" +
//                    "    job_setup_mst.jsm_divm_id,\n" +
//                    "    job_setup_mst.jsm_dept_id,\n" +
//                    "    emp_mst.emp_code\n" +
//                    "FROM\n" +
//                    "         emp_mst\n" +
//                    "    INNER JOIN emp_job_history ON emp_mst.emp_id = emp_job_history.job_emp_id\n" +
//                    "    INNER JOIN job_setup_dtl ON emp_mst.emp_jsd_id = job_setup_dtl.jsd_id\n" +
//                    "    INNER JOIN job_setup_mst ON job_setup_dtl.jsd_jsm_id = job_setup_mst.jsm_id\n" +
//                    "where\n" +
//                    "    emp_mst.emp_id = "+emp_id+"");
//            while (resultSet2.next()) {
//                tracking_flag = resultSet2.getInt(1);
//                job_id = resultSet2.getInt(2);
//                coa_id = resultSet2.getString(3);
//                divm_id = resultSet2.getString(4);
//                dept_id = resultSet2.getString(5);
//                emp_code = resultSet2.getString(6);
//
//            }
//
//
//
//            if (tracking_flag == 1) {
//
//                String elr_id = "";
//                Date c = Calendar.getInstance().getTime();
//
//
//                Timestamp timestamp = new Timestamp(c.getTime());
//                //SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.ENGLISH);
//
//                ResultSet rs2 = stmt.executeQuery("SELECT TO_CHAR(ELR_DATE,'DD-MON-RR') ELR_DATE FROM EMP_LOCATION_RECORD WHERE ELR_EMP_ID = "+emp_id+"\n" +
//                        "AND ELR_DATE > SYSDATE-15\n" +
//                        "ORDER BY ELR_DATE ASC\n");
//
//                while (rs2.next()) {
//                    lastTenDaysFromSQL.add(rs2.getString(1));
//                }
//
//                dateCount = 0;
//
//                for (int i = 0; i < lastTenDays.size(); i++) {
//
//                    boolean dateFound = false;
//                    String date = lastTenDays.get(i);
//
//                    for (int j = 0; j < lastTenDaysFromSQL.size(); j++) {
//                        if (date.equals(lastTenDaysFromSQL.get(j))) {
//                            dateFound = true;
//                            System.out.println(date);
//                        }
//                    }
//
//                    if (!dateFound) {
//                        ResultSet resultSet3 = stmt.executeQuery("select nvl(max(elr_id),0)+1 from emp_location_record");
//
//                        while (resultSet3.next()) {
//                            elr_id = resultSet3.getString(1);
//                            System.out.println(elr_id);
//                        }
//
//                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yy",Locale.ENGLISH);
//
//                        Date gpxDate = null;
//
//                        gpxDate = simpleDateFormat.parse(date);
//
////                    String fileName = simpleDateFormat.format(c);
//                        String fileName = emp_id+"_"+date+"_track";
//
//                        FILE_OF_DAILY_ACTIVITY = fileName;
//
//                        sharedPreferencesDA = mContext.getSharedPreferences(FILE_OF_DAILY_ACTIVITY, MODE_PRIVATE);
//
//                        String dist = sharedPreferencesDA.getString(DISTANCE,null);
//                        String totalTime = sharedPreferencesDA.getString(TOTAL_TIME,null);
//                        String stoppedTime = sharedPreferencesDA.getString(STOPPED_TIME,null);
//
//                        PreparedStatement pstmt = connection.prepareStatement("Insert into emp_location_record (ELR_ID, ELR_EMP_ID, ELR_JOB_ID, ELR_COA_ID, ELR_DIVM_ID, ELR_DEPT_ID, ELR_LOCATION_FILE,\n" +
//                                "\n" +
//                                "ELR_FILE_NAME, ELR_MIMETYPE, ELR_CHARACTERSET, ELR_FILETYPE, ELR_DATE, ELR_USER, ELR_FILE_UPDATED_ON, ELR_ACTIVE, TOTAL_DISTANCE_KM, TOTAL_TIME, TOTAL_STOPPED_TIME) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
//
//                        String stringFIle = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + File.separator +  fileName +".gpx";
//
//                        File blobFile = new File(stringFIle);
//
//                        updatedXml.add(fileName);
//
//                        if (blobFile.exists()) {
//                            dateCount++;
//                            InputStream in = new FileInputStream(blobFile);
//                            pstmt.setBinaryStream(7, in, (int)blobFile.length());
//                            pstmt.setInt(15,1);
//                            pstmt.setString(8,fileName);
//                            pstmt.setString(9,"application/gpx+xml");
//                            pstmt.setString(11,".gpx");
//                            updatedFiles.add(stringFIle);
//
//
//                        } else {
//                            pstmt.setBlob(7, (Blob) null);
//                            pstmt.setInt(15,0);
//                            pstmt.setString(8,null);
//                            pstmt.setString(9,null);
//                            pstmt.setString(11,null);
//                        }
//                        pstmt.setInt(1, Integer.parseInt(elr_id));
//                        System.out.println("File paise");
//                        pstmt.setInt(2,Integer.parseInt(emp_id));
//                        System.out.println("File paise");
//                        pstmt.setInt(3,job_id);
//                        System.out.println("File paise");
//                        pstmt.setInt(4,Integer.parseInt(coa_id));
//                        System.out.println("File paise");
//                        pstmt.setInt(5,Integer.parseInt(divm_id));
//                        System.out.println("File paise");
//                        pstmt.setInt(6,Integer.parseInt(dept_id));
//                        System.out.println("File paise");
//
//
//                        pstmt.setString(10,null);
//                        System.out.println("File paise");
//
//                        System.out.println("File paise");
//                        pstmt.setObject(12, new java.sql.Date(gpxDate.getTime()));
//                        System.out.println("File paise");
//                        pstmt.setString(13,emp_code);
//                        System.out.println("File paise");
//                        pstmt.setTimestamp(14,timestamp);
//                        System.out.println("File paise");
//
//                        pstmt.setString(16,dist);
//                        pstmt.setString(17,totalTime);
//                        pstmt.setString(18,stoppedTime);
//
//                        pstmt.executeUpdate();
//                        System.out.println("File paise");
//                        connection.commit();
//                        System.out.println("File paise");
//                        updated = true;
//
////                        if (updated) {
////                            if (blobFile.exists()) {
////                                boolean deleted = blobFile.delete();
////                                if (deleted) {
////                                    System.out.println("deleted");
////                                }
////                                updated = false;
////                            }
////                        }
//
//
//
//                    } else {
//                        System.out.println("Ei "+date +" database e ase");
//                    }
//                }
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

    public void getEmpData() {
        final int[] job_id = {0};
        final String[] coa_id = {""};
        final String[] divm_id = {""};
        final String[] dept_id = {""};
        conn = false;
        connected = false;

        String empDataUrl = "http://103.56.208.123:8001/apex/ttrams/attendance/getEmpData/"+emp_id+"";

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        StringRequest empDataReq = new StringRequest(Request.Method.GET, empDataUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject empDataInfo = array.getJSONObject(i);
                        tracking_flag = empDataInfo.getInt("emp_timeline_tracker_flag");
                        job_id[0] = empDataInfo.getInt("emp_job_id");
                        coa_id[0] = empDataInfo.getString("job_pri_coa_id");
                        divm_id[0] = empDataInfo.getString("jsm_divm_id");
                        dept_id[0] = empDataInfo.getString("jsm_dept_id");
                        emp_code = empDataInfo.getString("emp_code");
                    }
                    if (tracking_flag == 1) {
                        getTrackerUploadDate(job_id[0],coa_id[0],divm_id[0],dept_id[0]);
                    }
                    else {
                        connected = true;
                        notifyUser();
                    }
                }
                else {
                    connected = false;
                    notifyUser();
                }
            }
            catch (JSONException e) {
                connected = false;
                e.printStackTrace();
                notifyUser();
            }
        }, error -> {
            conn = false;
            connected = false;
            error.printStackTrace();
            notifyUser();
        });

        requestQueue.add(empDataReq);

    }

    public void getTrackerUploadDate(int job_id, String coa_id, String divm_id, String dept_id) {
        lastTenDaysFromSQL = new ArrayList<>();
        conn = false;
        connected = false;

        String trackerDataUrl = "http://103.56.208.123:8001/apex/ttrams/attendance/getTrackUploadedDate/"+emp_id+"";

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        StringRequest trackerDataReq = new StringRequest(Request.Method.GET, trackerDataUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject trackerDataInfo = array.getJSONObject(i);
                        String elr_date = trackerDataInfo.getString("elr_date");
                        lastTenDaysFromSQL.add(elr_date);
                    }
                }
                dateCount = 0;
                getTrackerDate(job_id,coa_id,divm_id,dept_id);

            }
            catch (JSONException e) {
                connected = false;
                e.printStackTrace();
                notifyUser();
            }
        },error -> {
            conn = false;
            connected = false;
            error.printStackTrace();
            notifyUser();
        });

        requestQueue.add(trackerDataReq);

    }

    public void getTrackerDate(int job_id, String coa_id, String divm_id, String dept_id) {
        boolean noDatetoUp = true;
        for (int i = 0; i < lastTenDays.size(); i++) {
            boolean dateFound = false;
            String date = lastTenDays.get(i);

            for (int j = 0; j < lastTenDaysFromSQL.size(); j++) {
                if (date.equals(lastTenDaysFromSQL.get(j))) {
                    dateFound = true;
                    System.out.println(date);
                }
            }
            if (!dateFound) {
                noDatetoUp = false;
                String fileName = emp_id+"_"+date+"_track";

                FILE_OF_DAILY_ACTIVITY = fileName;

                sharedPreferencesDA = mContext.getSharedPreferences(FILE_OF_DAILY_ACTIVITY, MODE_PRIVATE);

                String dist = sharedPreferencesDA.getString(DISTANCE,null);
                String totalTime = sharedPreferencesDA.getString(TOTAL_TIME,null);
                String stoppedTime = sharedPreferencesDA.getString(STOPPED_TIME,null);

                String stringFIle = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + File.separator +  fileName +".gpx";

                File blobFile = new File(stringFIle);

                updatedXml.add(fileName);
                byte[] bytes = null;
                if (blobFile.exists()) {
                    dateCount++;
                    try {
                        bytes = method(blobFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    updatedFiles.add(stringFIle);
                }
                uploadEmpTrackerFile(job_id,coa_id,divm_id,dept_id,date,bytes, blobFile,fileName,dist,totalTime,stoppedTime);
                break;
            }
            else {
                System.out.println("Ei "+date +" database e ase");
            }
        }
        if (noDatetoUp) {
            connected = true;
            notifyUser();
        }
    }

    public void uploadEmpTrackerFile(int job_id, String coa_id, String divm_id, String dept_id, String date, byte[] bytes, File blobFile, String fileName, String dist, String totalTime, String stoppedTime) {

        String uploadFileUrl = "http://103.56.208.123:8001/apex/ttrams/attendance/uploadTrackerFile";

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        StringRequest uploadReq = new StringRequest(Request.Method.POST, uploadFileUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String string_out = jsonObject.getString("string_out");
                System.out.println(string_out);
                if (string_out.equals("Successfully Created")) {
                    lastTenDaysFromSQL.add(date);
                    getTrackerDate(job_id,coa_id,divm_id,dept_id);
                }
                else {
                    System.out.println("EKHANE ASHE 3");
                    connected = false;
                    notifyUser();
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
                System.out.println("EKHANE ASHE 0");
                connected = false;
                notifyUser();
            }
        },error -> {
            error.printStackTrace();
            System.out.println("EKHANE ASHE -1");
            conn = false;
            connected = false;
            notifyUser();
        }){
            @Override
            public byte[] getBody() throws AuthFailureError {
                return bytes;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                if (blobFile.exists()) {
                    headers.put("P_ELR_ACTIVE","1");
                    headers.put("P_ELR_FILE_NAME",fileName);
                    headers.put("P_ELR_MIMETYPE","application/gpx+xml");
                    headers.put("P_ELR_FILETYPE",".gpx");
                }
                else {
                    headers.put("P_ELR_ACTIVE","0");
                    headers.put("P_ELR_FILE_NAME",null);
                    headers.put("P_ELR_MIMETYPE",null);
                    headers.put("P_ELR_FILETYPE",null);
                }
                headers.put("P_ELR_EMP_ID",emp_id);
                headers.put("P_ELR_JOB_ID",String.valueOf(job_id));
                headers.put("P_ELR_COA_ID",coa_id);
                headers.put("P_ELR_DIVM_ID",divm_id);
                headers.put("P_ELR_DEPT_ID",dept_id);
                headers.put("P_ELR_DATE",date);
                headers.put("P_ELR_USER",emp_code);
                headers.put("P_TOTAL_DISTANCE_KM",dist);
                headers.put("P_TOTAL_TIME",totalTime);
                headers.put("P_TOTAL_STOPPED_TIME",stoppedTime);
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/binary";
            }
        };

        requestQueue.add(uploadReq);
    }

    public static byte[] method(File file) throws IOException {
        FileInputStream fl = new FileInputStream(file);
        byte[] arr = new byte[(int) file.length()];
        fl.read(arr);
        fl.close();
        return arr;
    }

    public void notifyUser() {
        if (conn) {
            if(connected) {
                if (dateCount > 0) {
                    if (updatedFiles.size() != 0) {
                        for (int i = 0; i < updatedFiles.size(); i++) {
                            String stringFile = updatedFiles.get(i);
                            File blobFile = new File(stringFile);
                            if (blobFile.exists()) {
                                boolean deleted = blobFile.delete();
                                if (deleted) {
                                    System.out.println("Deleted");
                                }
                            }
                        }
                    }
                    if (updatedXml.size() != 0) {
                        for (int i = 0 ; i < updatedXml.size(); i++) {

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                File dir = new File(mContext.getApplicationInfo().dataDir, "shared_prefs/" + updatedXml.get(i)+ ".xml");
                                if(dir.exists()) {
                                    mContext.getSharedPreferences(updatedXml.get(i), MODE_PRIVATE).edit().clear().apply();
                                    boolean ddd = dir.delete();
                                    System.out.println(ddd);
                                } else {
                                    System.out.println(false);
                                }
                            }
                        }
                    }

                    if (dateCount == 1) {
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, channelId)
                                .setSmallIcon(R.drawable.thrn_logo)
                                .setContentTitle("Tracking Service")
                                .setContentText(dateCount + " File Uploaded")
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(mContext);
//        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        builder.setSound(alarmSound);
                        notificationManagerCompat.notify(200, builder.build());
                    } else {
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, channelId)
                                .setSmallIcon(R.drawable.thrn_logo)
                                .setContentTitle("Tracking Service")
                                .setContentText(dateCount + " Files Uploaded")
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(mContext);
//        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        builder.setSound(alarmSound);
                        notificationManagerCompat.notify(200, builder.build());
                    }

                }
                conn = false;
                connected = false;
            }
            else {
                retryNo++;
                if (retryNo <= 3) {
                    getEmpData();
                }
            }
        }
        else {
            retryNo++;
            if (retryNo <= 3) {
                getEmpData();
            }
        }
    }
}
