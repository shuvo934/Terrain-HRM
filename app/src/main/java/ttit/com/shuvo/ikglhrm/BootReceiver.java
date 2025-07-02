package ttit.com.shuvo.ikglhrm;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import ttit.com.shuvo.ikglhrm.dashboard.GeoLocationList;
import ttit.com.shuvo.ikglhrm.dashboard.GeofenceBroadcastReceiver;

public class BootReceiver extends BroadcastReceiver {

    SharedPreferences geoSharedData;
    final String GEO_FILE = "GEO_ACTIVE_FILE";
    final String GEO_ALL_DATA = "GEO_ALL_DATA";
    Gson gson = new Gson();
    String json = "";

    ArrayList<GeoLocationList> geoLocationLists;

    @Override
    public void onReceive(Context context, Intent intent) {

        geoSharedData = context.getSharedPreferences(GEO_FILE, MODE_PRIVATE);
        json = geoSharedData.getString(GEO_ALL_DATA, "");
        Type type = new TypeToken<ArrayList<GeoLocationList>>() {}.getType();
        ArrayList<GeoLocationList> savedLocationLists = gson.fromJson(json, type);
        GeofencingClient geofencingClient = LocationServices.getGeofencingClient(context);
        geoLocationLists = new ArrayList<>();

        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            ArrayList<Geofence> geofenceList = new ArrayList<>();

            if (savedLocationLists != null) {
                if (!savedLocationLists.isEmpty()) {

                    ArrayList<String> ids = new ArrayList<>();
                    for (int i = 0; i < savedLocationLists.size(); i++) {
                        String id = savedLocationLists.get(i).getGeo_id();
                        String lat = savedLocationLists.get(i).getGeo_lat();
                        String lng = savedLocationLists.get(i).getGeo_lng();
                        String radius = savedLocationLists.get(i).getGeo_radius();
                        String coa_id = savedLocationLists.get(i).getCoa_id();

                        geoLocationLists.add(new GeoLocationList(id,lat,lng,radius,coa_id));
                        ids.add(id);
                    }
                    geofencingClient.removeGeofences(ids)
                            .addOnSuccessListener(unused -> Toast.makeText(context, "GeoFence Removed Successfully", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> {
                                String errorMessage = getErrorString(e);
                                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                            });
                }
            }

            if (!geoLocationLists.isEmpty()) {

                ArrayList<GeoLocationList> newLocationLists = new ArrayList<>();

                for (int i = 0; i < geoLocationLists.size(); i++) {
                    String req_id = geoLocationLists.get(i).getGeo_id();
                    String lat = geoLocationLists.get(i).getGeo_lat();
                    String lng = geoLocationLists.get(i).getGeo_lng();
                    String org_radius = geoLocationLists.get(i).getGeo_radius();
                    String radius = geoLocationLists.get(i).getGeo_radius();
                    String c_id = geoLocationLists.get(i).getCoa_id();

                    LatLng latLng;

                    if (!lat.isEmpty() && !lng.isEmpty()) {
                        latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                        if (radius.isEmpty()) {
                            radius = "0";
                        }
                        else if (Float.parseFloat(radius) > 100) {
                            float radius_1 = (Float.parseFloat(radius) - 100);
                            radius = String.valueOf(radius_1);
                        }
                        float rad = Float.parseFloat(radius);

                        Geofence geofence = new Geofence.Builder()
                                .setCircularRegion(latLng.latitude, latLng.longitude, rad)
                                .setRequestId(req_id)
                                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_DWELL | Geofence.GEOFENCE_TRANSITION_EXIT)
                                .setLoiteringDelay(120000)
                                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                                .build();

                        geofenceList.add(geofence);
                        newLocationLists.add(new GeoLocationList(req_id,lat,lng,org_radius,c_id));
                    }
                }

                if (!geofenceList.isEmpty()) {
                    GeofencingRequest geofencingRequest = new GeofencingRequest.Builder()
                            .addGeofences(geofenceList)
                            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                            .build();

                    Intent brd_intent = new Intent(context, GeofenceBroadcastReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1120, brd_intent, PendingIntent.FLAG_UPDATE_CURRENT);

                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    geofencingClient.addGeofences(geofencingRequest, pendingIntent)
                            .addOnSuccessListener(unused -> {
                                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, context.getString(R.string.att_channel_id))
                                        .setSmallIcon(R.drawable.thrn_logo)
                                        .setContentTitle("Attendance System")
                                        .setContentText("GeoFence Added Successfully.")
                                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
                                notificationManagerCompat.notify(222, builder.build());
                            })
                            .addOnFailureListener(e -> {
                                String errorMsg = getErrorString(e);
                                Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
                            });
                }

                json = gson.toJson(newLocationLists);

                SharedPreferences.Editor editor1 = geoSharedData.edit();
                editor1.remove(GEO_ALL_DATA);
                editor1.putString(GEO_ALL_DATA, json);
                editor1.apply();
                editor1.commit();
            }
        }
    }

    public String getErrorString (Exception e) {
        if (e instanceof ApiException) {
            ApiException apiException = (ApiException) e;
            switch (apiException.getStatusCode()) {
                case GeofenceStatusCodes
                             .GEOFENCE_NOT_AVAILABLE:
                    return "GEOFENCE NOT AVAILABLE";
                case GeofenceStatusCodes
                             .GEOFENCE_TOO_MANY_GEOFENCES:
                    return "TOO MANY GEOFENCES";
                case GeofenceStatusCodes
                             .GEOFENCE_TOO_MANY_PENDING_INTENTS:
                    return "TOO MANY PENDING INTENTS";
                case GeofenceStatusCodes.GEOFENCE_INSUFFICIENT_LOCATION_PERMISSION:
                    return "INSUFFICIENT PERMISSIONS";
            }
        }
        return e.getLocalizedMessage();
    }
}
