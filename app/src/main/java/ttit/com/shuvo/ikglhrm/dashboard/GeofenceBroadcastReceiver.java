package ttit.com.shuvo.ikglhrm.dashboard;

import static android.content.Context.MODE_PRIVATE;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

import ttit.com.shuvo.ikglhrm.R;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "GeofenceBroadcastRcv";

    SharedPreferences attendanceWidgetPreferences;
    public static final String WIDGET_FILE = "WIDGET_FILE";
    public static final String WIDGET_EMP_ID = "WIDGET_EMP_ID";
    public static final String WIDGET_TRACKER_FLAG = "WIDGET_TRACKER_FLAG";
    String emp_id = "";
    String tracker_flag = "";

    @Override
    public void onReceive(Context context, Intent intent) {

        attendanceWidgetPreferences = context.getSharedPreferences(WIDGET_FILE,MODE_PRIVATE);
        emp_id = attendanceWidgetPreferences.getString(WIDGET_EMP_ID,"");
        tracker_flag = attendanceWidgetPreferences.getString(WIDGET_TRACKER_FLAG,"");

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

        if (geofencingEvent != null) {
            if (geofencingEvent.hasError()) {
                String errorMessage = GeofenceStatusCodes
                        .getStatusCodeString(geofencingEvent.getErrorCode());
                Log.e(TAG, errorMessage);
                return;
            }
            // Get the transition type.
            int geofenceTransition = geofencingEvent.getGeofenceTransition();

            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();
            Location location = geofencingEvent.getTriggeringLocation();
            if (location != null) {
                Log.d(TAG,location.toString());
            }

            if (triggeringGeofences != null) {
                for( Geofence geofence : triggeringGeofences) {
                    Log.d(TAG,geofence.getRequestId());
                }
            }

            switch (geofenceTransition) {
                case Geofence.GEOFENCE_TRANSITION_ENTER:
                case Geofence.GEOFENCE_TRANSITION_EXIT:
                case Geofence.GEOFENCE_TRANSITION_DWELL:
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
                                    .putString(GeoFenceWorker.WORKER_W_EMP_ID, emp_id)
                                    .build();

                            final OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(GeoFenceWorker.class)
                                    .setInputData(data)
                                    .build();
                            WorkManager.getInstance(context).enqueue(workRequest);
                        }
                    }
                    else {
                        Toast.makeText(context, "Please Login to Terrain HRM", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }
}
