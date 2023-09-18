package ttit.com.shuvo.ikglhrm.attendance.att_widget;

import static android.content.Context.MODE_PRIVATE;

import static ttit.com.shuvo.ikglhrm.scheduler.Uploader.channelId;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import ttit.com.shuvo.ikglhrm.R;
import ttit.com.shuvo.ikglhrm.scheduler.MyWorker;


public class AttWidgetReceiver extends BroadcastReceiver {

    SharedPreferences attendanceWidgetPreferences;
    public static final String WIDGET_FILE = "WIDGET_FILE";
    public static final String WIDGET_EMP_ID = "WIDGET_EMP_ID";
    public static final String WIDGET_TRACKER_FLAG = "WIDGET_TRACKER_FLAG";
    String emp_id = "";
    String tracker_flag = "";
    @Override
    public void onReceive(Context context, Intent intent) {

//        Toast.makeText(context, "ATTENDANCE GIVEN", Toast.LENGTH_SHORT).show();

        attendanceWidgetPreferences = context.getSharedPreferences(WIDGET_FILE,MODE_PRIVATE);
        emp_id = attendanceWidgetPreferences.getString(WIDGET_EMP_ID,"");
        tracker_flag = attendanceWidgetPreferences.getString(WIDGET_TRACKER_FLAG,"");

        if (!emp_id.isEmpty()) {
            if (tracker_flag.equals("1")) {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, context.getString(R.string.att_channel_id))
                        .setSmallIcon(R.drawable.thrn_logo)
                        .setContentTitle("Attendance System")
                        .setContentText("Your tracking flag is active. You need to give attendance from the app.")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
                notificationManagerCompat.notify(222, builder.build());
            }
            else {
                Data data = new Data.Builder()
                        .putString(WidgetAttendanceWorker.WORKER_W_EMP_ID, emp_id)
                        .build();

                final OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(WidgetAttendanceWorker.class)
                        .setInputData(data)
                        .build();
                WorkManager.getInstance(context).enqueue(workRequest);
            }
        }
        else {
            Toast.makeText(context, "Please Login to Terrain HRM", Toast.LENGTH_SHORT).show();
        }

    }
}
