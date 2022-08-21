package ttit.com.shuvo.ikglhrm.scheduler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;



import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import ttit.com.shuvo.ikglhrm.R;
import ttit.com.shuvo.ikglhrm.attendance.Attendance;

import static ttit.com.shuvo.ikglhrm.OracleConnection.createConnection;

public class Uploader extends BroadcastReceiver {
    // Trigger at Dashboard

    public static String channelId = "NotifyMe";

    private String message = null;
    private Boolean conn = false;
    private Boolean connected = false;
    private Handler mHandler = new Handler();

    private Connection connection;

    String emp_id = "";
    String emp_code = "";
    int tracking_flag = 0;
    int dateCount = 0;
    int retryNo = 0;

    ArrayList<String> lastTenDays;
    ArrayList<String> lastTenDaysFromSQL;
    Context mContext;

    SharedPreferences sharedPreferences;
    public static final String SCHEDULING_FILE = "SCHEDULING FILE";
    public static final String SCHEDULING_EMP_ID = "SCHEDULING EMP ID";
    public static final String TRIGGERING = "TRIGGER TRUE FALSE";

    @Override
    public void onReceive(Context context, Intent intent) {

//        lastTenDays = new ArrayList<>();
//        lastTenDaysFromSQL = new ArrayList<>();
//
//        mContext = context;

        sharedPreferences = context.getSharedPreferences(SCHEDULING_FILE, Context.MODE_PRIVATE);
        emp_id = sharedPreferences.getString(SCHEDULING_EMP_ID,null);

        Data data = new Data.Builder()
                .putString(MyWorker.TASK_DESC_EMP_ID, emp_id)
                .build();

        //This is the subclass of our WorkRequest
        final OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(MyWorker.class)
                .setInputData(data)
                .build();

        WorkManager.getInstance().enqueue(workRequest);

//        Calendar cal = GregorianCalendar.getInstance();
//        cal.setTime(new Date());
//        cal.add(Calendar.DAY_OF_YEAR, -11);
//
//        for (int i = 0 ; i < 10 ;i ++) {
//            cal.add(Calendar.DAY_OF_YEAR, +1);
//            Date calTime = cal.getTime();
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yy", Locale.getDefault());
//            String ddd = simpleDateFormat.format(calTime);
//
//            ddd = ddd.toUpperCase();
//            System.out.println(ddd);
//            lastTenDays.add(ddd);
//        }
//
//        if (isConnected() && isOnline()) {
//            new Check().execute();
//        } else {
//            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
//                    .setSmallIcon(R.drawable.thrn_logo)
//                    .setContentTitle("Tracking Service")
//                    .setContentText("Failed to Upload File. Your Internet is not Connected")
//                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//
//            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
////        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
////        builder.setSound(alarmSound);
//            notificationManagerCompat.notify(200, builder.build());
//        }


    }

    public boolean isConnected() {
        boolean connected = false;
        boolean isMobile = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
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

        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isConnected() && isOnline()) {

                AttendanceGraph();
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

            if (conn) {


                if (dateCount > 0) {
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

                } else {
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, channelId)
                            .setSmallIcon(R.drawable.thrn_logo)
                            .setContentTitle("Tracking Service")
                            .setContentText("No Files Found")
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                    NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(mContext);
//                    Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//                    builder.setSound(alarmSound);
                    notificationManagerCompat.notify(200, builder.build());
                }

                conn = false;
                connected = false;

            }else {

                NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, channelId)
                        .setSmallIcon(R.drawable.thrn_logo)
                        .setContentTitle("Tracking Service")
                        .setContentText("Failed to Upload File. Server Connection Error and will be Retry in Next Day")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(mContext);
//        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        builder.setSound(alarmSound);
                notificationManagerCompat.notify(200, builder.build());


            }
        }
    }

    public void AttendanceGraph() {
        try {
            this.connection = createConnection();
            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();
            lastTenDaysFromSQL = new ArrayList<>();

            Statement stmt = connection.createStatement();



            int job_id = 0;
            String coa_id = "";
            String divm_id = "";
            String dept_id = "";
            ResultSet resultSet2 = stmt.executeQuery("SELECT\n" +
                    "    emp_mst.emp_timeline_tracker_flag,\n" +
                    "    emp_mst.emp_job_id,\n" +
                    "    emp_job_history.job_pri_coa_id,\n" +
                    "    job_setup_mst.jsm_divm_id,\n" +
                    "    job_setup_mst.jsm_dept_id\n" +
                    "    emp_mst.emp_code\n" +
                    "FROM\n" +
                    "         emp_mst\n" +
                    "    INNER JOIN emp_job_history ON emp_mst.emp_id = emp_job_history.job_emp_id\n" +
                    "    INNER JOIN job_setup_dtl ON emp_mst.emp_jsd_id = job_setup_dtl.jsd_id\n" +
                    "    INNER JOIN job_setup_mst ON job_setup_dtl.jsd_jsm_id = job_setup_mst.jsm_id\n" +
                    "where\n" +
                    "    emp_mst.emp_id = "+emp_id+"");
            while (resultSet2.next()) {
                tracking_flag = resultSet2.getInt(1);
                job_id = resultSet2.getInt(2);
                coa_id = resultSet2.getString(3);
                divm_id = resultSet2.getString(4);
                dept_id = resultSet2.getString(5);
                emp_code = resultSet2.getString(6);

            }



            if (tracking_flag == 1) {

                String elr_id = "";
                Date c = Calendar.getInstance().getTime();


                Timestamp timestamp = new Timestamp(c.getTime());
                //SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());

                ResultSet rs2 = stmt.executeQuery("SELECT TO_CHAR(ELR_DATE,'DD-MON-RR') ELR_DATE FROM EMP_LOCATION_RECORD WHERE ELR_EMP_ID = 2008\n" +
                        "AND ELR_DATE > SYSDATE-15\n" +
                        "ORDER BY ELR_DATE ASC\n");

                while (rs2.next()) {
                    lastTenDaysFromSQL.add(rs2.getString(1));
                }

                dateCount = 0;

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
                        ResultSet resultSet3 = stmt.executeQuery("select nvl(max(elr_id),0)+1 from emp_location_record");

                        while (resultSet3.next()) {
                            elr_id = resultSet3.getString(1);
                            System.out.println(elr_id);
                        }

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yy",Locale.getDefault());

                        Date gpxDate = null;

                        gpxDate = simpleDateFormat.parse(date);

//                    String fileName = simpleDateFormat.format(c);
                        String fileName = emp_id+"_"+date+"_track";

                        PreparedStatement pstmt = connection.prepareStatement("Insert into emp_location_record (ELR_ID, ELR_EMP_ID, ELR_JOB_ID, ELR_COA_ID, ELR_DIVM_ID, ELR_DEPT_ID, ELR_LOCATION_FILE,\n" +
                                "\n" +
                                "ELR_FILE_NAME, ELR_MIMETYPE, ELR_CHARACTERSET, ELR_FILETYPE, ELR_DATE, ELR_USER, ELR_FILE_UPDATED_ON, ELR_ACTIVE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

                        String stringFIle = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + File.separator +  fileName +".gpx";

                        File blobFile = new File(stringFIle);


                        if (blobFile.exists()) {
                            dateCount++;
                            InputStream in = new FileInputStream(blobFile);
                            pstmt.setBinaryStream(7, in, (int)blobFile.length());
                            pstmt.setInt(15,1);
                            pstmt.setString(8,fileName);
                            pstmt.setString(9,"application/gpx+xml");
                            pstmt.setString(11,".gpx");

                        } else {
                            pstmt.setBlob(7, (Blob) null);
                            pstmt.setInt(15,0);
                            pstmt.setString(8,null);
                            pstmt.setString(9,null);
                            pstmt.setString(11,null);
                        }
                        pstmt.setInt(1, Integer.parseInt(elr_id));
                        System.out.println("File paise");
                        pstmt.setInt(2,Integer.parseInt(emp_id));
                        System.out.println("File paise");
                        pstmt.setInt(3,job_id);
                        System.out.println("File paise");
                        pstmt.setInt(4,Integer.parseInt(coa_id));
                        System.out.println("File paise");
                        pstmt.setInt(5,Integer.parseInt(divm_id));
                        System.out.println("File paise");
                        pstmt.setInt(6,Integer.parseInt(dept_id));
                        System.out.println("File paise");


                        pstmt.setString(10,null);
                        System.out.println("File paise");

                        System.out.println("File paise");
                        pstmt.setObject(12, new java.sql.Date(gpxDate.getTime()));
                        System.out.println("File paise");
                        pstmt.setString(13,emp_code);
                        System.out.println("File paise");
                        pstmt.setTimestamp(14,timestamp);
                        System.out.println("File paise");


                        pstmt.executeUpdate();
                        System.out.println("File paise");
                        connection.commit();
                        System.out.println("File paise");

                    } else {
                        System.out.println("Ei "+date +" database e ase");
                    }
                }
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
}
