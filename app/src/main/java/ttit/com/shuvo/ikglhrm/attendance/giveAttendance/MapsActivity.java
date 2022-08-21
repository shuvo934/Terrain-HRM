package ttit.com.shuvo.ikglhrm.attendance.giveAttendance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import ttit.com.shuvo.ikglhrm.R;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Button close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map1);
        mapFragment.getMapAsync(this);

        close = findViewById(R.id.att_finish_loc);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

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
        //mp.icon(BitmapDescriptorFactory.fromResource(R.drawable.circle));
        //mp.anchor((float) 0.5, (float) 0.5);
        mp.title(status);
        mp.snippet(snippet);
        mMap.addMarker(mp);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, 18));
    }
}