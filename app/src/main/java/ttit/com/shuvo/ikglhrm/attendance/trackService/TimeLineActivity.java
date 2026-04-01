package ttit.com.shuvo.ikglhrm.attendance.trackService;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import ttit.com.shuvo.ikglhrm.R;
import ttit.com.shuvo.ikglhrm.WaitProgress;

import static ttit.com.shuvo.ikglhrm.attendance.report.AttenReportAdapter.blobFromAdapter;

public class TimeLineActivity extends AppCompatActivity implements OnMapReadyCallback, LocationAdapter.ClickedItem{

    private GoogleMap mMap;

    RecyclerView locationView;
    LocationAdapter locationAdapter;
    RecyclerView.LayoutManager layoutManager;

    WaitProgress waitProgress = new WaitProgress();
    private Boolean connected = false;

    ArrayList<LocationNameArray> locationNameArrays;
    ArrayList<PolyLindata> polyLindata;
    ArrayList<MarkerData> markerData;
    String elr_id = "";

    String downloadFile = "Downloaded_GPX.gpx";

    boolean blobNotNull = false;
    ArrayList<WaypointList> wptList;

    public static ArrayList<ArrrayFile> multiGpxList;
    Logger logger = Logger.getLogger(TimeLineActivity.class.getName());

    private final ExecutorService geocodeExecutor = Executors.newFixedThreadPool(2);
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.timeline_map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        locationView = findViewById(R.id.location_details_review);

        wptList = new ArrayList<>();
        multiGpxList = new ArrayList<>();
        locationNameArrays = new ArrayList<>();
        polyLindata = new ArrayList<>();
        markerData = new ArrayList<>();

        locationView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        locationView.setLayoutManager(layoutManager);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);

        Intent intent = getIntent();
        elr_id = intent.getStringExtra("ELR");

        getMapData();

        mMap.setOnMapClickListener(latLng -> {
            for (int i = 0 ; i < polyLindata.size(); i++) {
                Polyline polyline = polyLindata.get(i).getPolyline();
                polyline.setColor(Color.parseColor("#74b9ff"));
                polyline.setWidth(17);
            }
            for (int i = 0; i < markerData.size(); i++) {
                Marker marker = markerData.get(i).getMarker();
                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.location_icon));
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    public void GpxInMap() {
        locationNameArrays = new ArrayList<>();
        if (locationAdapter == null) {
            locationAdapter = new LocationAdapter(locationNameArrays, TimeLineActivity.this, TimeLineActivity.this);
            locationView.setAdapter(locationAdapter);
        }
        String stringFIle = getExternalFilesDir(null).getPath() + File.separator +  downloadFile;

        File file = new File(stringFIle);

        if (!file.exists()) {
            Toast.makeText(getApplicationContext(), "File Not Found", Toast.LENGTH_SHORT).show();
        }
        else {
            wptList = GPXFileDecoder.decodeWPT(file);
            multiGpxList = GPXFileDecoder.multiLine(file);

            if (multiGpxList.isEmpty() && wptList.isEmpty()) {
                Toast.makeText(getApplicationContext(),"Track Not Found",Toast.LENGTH_SHORT).show();
            }
            else {
                if (!wptList.isEmpty()) {
                    float zoom = wptList.size() == 1 ? 18 : 14;
                    for (int i = 0; i < wptList.size(); i++) {
//                    String addss = getAddress(wptList.get(i).getLocation().getLatitude(), wptList.get(i).getLocation().getLongitude());
                        double lat = wptList.get(i).getLocation().getLatitude();
                        double lng = wptList.get(i).getLocation().getLongitude();

                        LatLng wpt = new LatLng(lat, lng);
                        Marker marker = mMap.addMarker(new MarkerOptions().position(wpt).title("Loading address...").snippet(wptList.get(i).getTime()).icon(BitmapDescriptorFactory.fromResource(R.drawable.location_icon)));
                        markerData.add(new MarkerData(marker, String.valueOf(i)));
                        locationNameArrays.add(new LocationNameArray("Loading address...", "", true, wptList.get(i).getTime(), "", "", "", null, String.valueOf(i)));

                        final int index = i;
                        getAddressAsync(lat, lng, address -> {
                            if (marker != null) {
                                marker.setTitle(address);
                            }

                            if (index < locationNameArrays.size()) {
                                locationNameArrays.get(index).setFirstLocation(address);   // only if setter exists
                                locationAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                    double lat = wptList.get(0).getLocation().getLatitude();
                    double lng = wptList.get(0).getLocation().getLongitude();

                    LatLng wpt = new LatLng(lat, lng);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(wpt, zoom));
                }

                if (!multiGpxList.isEmpty()) {
                    for (int a = 0; a < multiGpxList.size(); a++) {
                        ArrayList<Location> gpxList = multiGpxList.get(a).getMyLatlng();
                        ArrayList<String> timelist = multiGpxList.get(a).getMyTime();
                        String lengthh = multiGpxList.get(a).getDescc();
                        System.out.println(lengthh);
                        String firstTime = "";
                        String lastTime = "";
                        String distance;
                        String calculateTime = "";

                        if (!timelist.isEmpty()) {
                            firstTime = timelist.get(0);
                            lastTime = timelist.get(timelist.size() - 1);

                            SimpleDateFormat sdfTime = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);

                            Date first = null;
                            Date last = null;

                            try {
                                first = sdfTime.parse(firstTime);
                                last = sdfTime.parse(lastTime);
                            } catch (ParseException e) {
                                logger.log(Level.WARNING, e.getMessage(), e);
                            }

                            if (first != null && last != null) {
                                long millis = last.getTime() - first.getTime();
                                int hours = (int) (millis / (1000 * 60 * 60));
                                int mins = (int) ((millis / (1000 * 60)) % 60);

                                if (hours == 0) {
                                    calculateTime = mins + " Minutes";
                                } else {
                                    if (hours > 1) {
                                        calculateTime = hours + " hours " + mins + " Minutes";
                                    } else {
                                        calculateTime = hours + " hour " + mins + " Minutes";
                                    }

                                }
                                System.out.println("Calculate Time: " + calculateTime);
                            }
                        }

                        int index = lengthh.indexOf(" ");
                        int index2 = lengthh.indexOf(" KM");
                        String substr;
                        if (index < 0 && index2 < 0) {
                            substr = "0";
                        } else {
                            Log.i("Index of 1st:", String.valueOf(index));
                            Log.i("Index of 2nd:", String.valueOf(index2));
                            substr = lengthh.substring(index + 1, index2);
                            System.out.println(substr);
                        }

                        distance = substr + " KM";

                        PolylineOptions option = new PolylineOptions().width(17).color(Color.parseColor("#74b9ff")).geodesic(true).clickable(true).zIndex(a);
                        for (int z = 0; z < gpxList.size(); z++) {
                            LatLng point = new LatLng(gpxList.get(z).getLatitude(), gpxList.get(z).getLongitude());
                            option.add(point);
                        }

                        Polyline polyline = mMap.addPolyline(option);
                        polyLindata.add(new PolyLindata(polyline, String.valueOf(a)));


                        locationNameArrays.add(new LocationNameArray("Loading start location...", "Loading end location...", false, firstTime, lastTime, distance, calculateTime, String.valueOf(a), null));

                        final int indexes = locationNameArrays.size() - 1;

                        for (int i = 0; i < gpxList.size(); i++) {
                            MarkerOptions options = new MarkerOptions();
                            LatLng gpx = new LatLng(gpxList.get(i).getLatitude(), gpxList.get(i).getLongitude());

                            options.position(gpx);

                            if (i == 0) {
                                options.icon(BitmapDescriptorFactory.fromResource(R.drawable.star_loc_icon_new));
                                options.anchor((float) 0.5, (float) 0.5);
                                options.snippet("0 KM");
                                options.flat(true);

                                final Marker startMarker = mMap.addMarker(options.title("Loading start location..."));
                                getAddressAsync(gpxList.get(i).getLatitude(), gpxList.get(i).getLongitude(), address -> {
                                    if (startMarker != null) {
                                        startMarker.setTitle(address);
                                    }

                                    if (indexes < locationNameArrays.size()) {
                                        locationNameArrays.get(indexes).setFirstLocation(address);   // only if setter exists
                                        locationAdapter.notifyDataSetChanged();
                                    }
                                });

                            } else if (i == gpxList.size() - 1) {

                                options.icon(BitmapDescriptorFactory.fromResource(R.drawable.stop_loc_icon_new));
                                options.anchor((float) 0.5, (float) 0.5);
                                options.snippet(substr + " KM");
                                options.flat(true);

                                final Marker endMarker = mMap.addMarker(options.title("Loading end location..."));
                                getAddressAsync(gpxList.get(i).getLatitude(), gpxList.get(i).getLongitude(), address -> {
                                    if (endMarker != null) {
                                        endMarker.setTitle(address);
                                    }

                                    if (indexes < locationNameArrays.size()) {
                                        locationNameArrays.get(indexes).setLastLocation(address);   // only if setter exists
                                        locationAdapter.notifyDataSetChanged();
                                    }
                                });
                            }
                        }
                    }

                    ArrayList<Location> gpxList = multiGpxList.get(0).getMyLatlng();
                    if (!gpxList.isEmpty()) {
                        int i = (gpxList.size() - 1) / 2;
                        LatLng gpx = new LatLng(gpxList.get(i).getLatitude(), gpxList.get(i).getLongitude());
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(gpx, 14));
                    }
                }
            }
        }
    }

    public interface AddressCallback {
        void onResult(String address);
    }

    public void getAddressAsync(double lat, double lng, AddressCallback callback) {
        geocodeExecutor.execute(() -> {
            String result = "Address unavailable";

            try {
                if (Geocoder.isPresent()) {
                    Geocoder geocoder = new Geocoder(TimeLineActivity.this, Locale.ENGLISH);
                    List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);

                    if (addresses != null && !addresses.isEmpty()) {
                        String line = addresses.get(0).getAddressLine(0);
                        if (line != null && !line.isEmpty()) {
                            result = line;
                        } else {
                            result = "Unknown location";
                        }
                    } else {
                        result = "Unknown location";
                    }
                }
            } catch (Exception e) {
                logger.log(Level.WARNING, e.getMessage(), e);
            }

            String finalResult = result;
            mainHandler.post(() -> callback.onResult(finalResult));
        });
    }

    @Override
    public void onCategoryClicked(int CategoryPosition) {

        String  polyId = locationNameArrays.get(CategoryPosition).getPolyId();
        String distance = locationNameArrays.get(CategoryPosition).getDistance();
        String markerId = locationNameArrays.get(CategoryPosition).getMarId();
        System.out.println(polyId);
        if (polyId != null) {
            for (int i = 0; i < polyLindata.size(); i++) {
                Polyline polyline = polyLindata.get(i).getPolyline();
                String poid = polyLindata.get(i).getId();

                if (polyId.equals(poid)) {
                    polyline.setColor(Color.parseColor("#1e3799"));
                    polyline.setWidth(30);
                    int size = polyline.getPoints().size();
                    size = size / 2;
                    double latitude = polyline.getPoints().get(size).latitude;
                    double longitude = polyline.getPoints().get(size).longitude;
                    LatLng gpx = new LatLng(latitude, longitude);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(gpx, 17));
                    Toast.makeText(getApplicationContext(),distance,Toast.LENGTH_SHORT).show();
                } else {
                    polyline.setColor(Color.parseColor("#74b9ff"));
                    polyline.setWidth(17);
//                    int size = polyline.getPoints().size();
                }
            }
        } else {
            for (int i = 0 ; i < polyLindata.size(); i++) {
                Polyline polyline = polyLindata.get(i).getPolyline();
                polyline.setColor(Color.parseColor("#74b9ff"));
                polyline.setWidth(17);
            }
        }

        if (markerId != null) {
            for (int i = 0 ; i < markerData.size(); i++) {
                Marker marker = markerData.get(i).getMarker();
                LatLng latLng = marker.getPosition();
                if (markerId.equals(markerData.get(i).getId())) {
                    marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.location_icon_active));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                } else {
                    marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.location_icon));
                }
            }
        } else {
            for (int i = 0; i < markerData.size(); i++) {
                Marker marker = markerData.get(i).getMarker();
                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.location_icon));
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!geocodeExecutor.isShutdown()) {
            geocodeExecutor.shutdownNow();
        }
    }

    public void getMapData() {
        waitProgress.show(getSupportFragmentManager(), "WaitBar");
        waitProgress.setCancelable(false);
        connected = false;

        try {
            if (blobFromAdapter != null && blobFromAdapter.length() != 0) {
                System.out.println("BLOB paise");
                File myExternalFile = new File(getExternalFilesDir(null),downloadFile);

                InputStream r = blobFromAdapter.getBinaryStream();
                FileWriter fw=new FileWriter(myExternalFile);
                int i;
                while((i=r.read())!=-1)
                    fw.write((char)i);
                fw.close();
                blobNotNull = true;
            } else {
                System.out.println("BLOB pai nai");
                blobNotNull = false;
            }
            connected = true;
            updateMap();
        }
        catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            connected = false;
            updateMap();
        }
    }

    public void updateMap() {
        waitProgress.dismiss();
        if (connected) {
            connected = false;

            System.out.println("GPX File Created");

            if (blobNotNull) {
                GpxInMap();
            } else {
                locationAdapter = new LocationAdapter(locationNameArrays, TimeLineActivity.this,TimeLineActivity.this);
                locationView.setAdapter(locationAdapter);
                Toast.makeText(getApplicationContext(), "No Record Found", Toast.LENGTH_SHORT).show();
            }

            blobNotNull = false;

        }
        else {
            wptList = new ArrayList<>();
            multiGpxList = new ArrayList<>();
            locationNameArrays = new ArrayList<>();
            polyLindata = new ArrayList<>();
            markerData = new ArrayList<>();

            locationAdapter = new LocationAdapter(locationNameArrays, TimeLineActivity.this,TimeLineActivity.this);
            locationView.setAdapter(locationAdapter);

            AlertDialog dialog = new AlertDialog.Builder(TimeLineActivity.this)
                    .setMessage("Failed to Retrieve Data From File.")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel",null)
                    .show();


            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                getMapData();
                dialog.dismiss();
            });

            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> dialog.dismiss());
        }
    }
}