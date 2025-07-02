package ttit.com.shuvo.ikglhrm.attendance.giveAttendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import ttit.com.shuvo.ikglhrm.R;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap mMap;
    Button close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map1);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        close = findViewById(R.id.att_finish_loc);
        close.setOnClickListener(v -> finish());
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);

        Intent intent = getIntent();
        String lat = intent.getStringExtra("lat");
        String lon = intent.getStringExtra("lon");
        String time = intent.getStringExtra("time");
        String date = intent.getStringExtra("date");
        String status = intent.getStringExtra("status");


        MarkerOptions mp = new MarkerOptions();
        LatLng ll = new LatLng(Double.parseDouble(lat),Double.parseDouble(lon));
        String snippet = date+","+time;

        mp.position(ll);
        mp.title(status);
        mp.snippet(snippet);
        mMap.addMarker(mp);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, 18));
    }
}