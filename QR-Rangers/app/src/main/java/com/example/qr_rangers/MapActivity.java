//adapted from https://stackoverflow.com/questions/52431299/how-can-i-use-osm-in-android-studio-to-show-a-map

package com.example.qr_rangers;


import static android.os.Build.VERSION_CODES.M;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.mylocation.DirectedLocationOverlay;

/**
 * This is an activity that shows the user a map of all nearby scanned QR codes
 * @author Alexander Salm
 * @version 1.0
 */
public class MapActivity extends AppCompatActivity implements LocationListener {

    private MapView map = null;
    private MapView mapView;
    private MapController mapController;
    private CompassOverlay compassOverlay;
    private DirectedLocationOverlay locationOverlay;
    private GpsTracker tracker;
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


        if (Build.VERSION.SDK_INT >= M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                String[] perms = {Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(perms, 1);
            }
        }

        tracker = new GpsTracker(MapActivity.this);
        if(!tracker.canGetLocation()) {
            tracker.showSettingsAlert();
            Log.i("NOTE", "Could not get location...");
        }
        else {
            double longitude = tracker.getLongitude();
            double latitude = tracker.getLatitude();
            location = new Location(longitude, latitude);
            Log.i("NOTE", "Got location: " + Double.toString(location.getLatitude()) + " " + Double.toString(location.getLongitude()));
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        mapView = (MapView) findViewById(R.id.map);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);

        mapController = (MapController) mapView.getController();
        mapController.setZoom(3);

        GeoPoint center = new GeoPoint(location.getLatitude(),location.getLongitude());
        Log.e("LOCATION", "lat: " + Double.toString(location.getLatitude()) + " | lon: " + Double.toString(location.getLongitude()));
        mapController.animateTo(center);
        addMarker(center);

        mapView.setMapListener(new MapListener() {
            @Override
            public boolean onScroll(ScrollEvent event) {
                Log.i("NOTE", "onScroll()");
                return false;
            }

            @Override
            public boolean onZoom(ZoomEvent event) {
                Log.i("NOTE", "onZoom()");
                return false;
            }
        });

    }

    public void addMarker (GeoPoint center){
        Marker marker = new Marker(mapView);
        marker.setPosition(center);
        marker.setAnchor(0, 0);//Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marker.setIcon(getResources().getDrawable(R.drawable.ic_menu_compass));
        mapView.getOverlays().clear();
        mapView.getOverlays().add(marker);
        mapView.invalidate();
        marker.setTitle("You are here");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    this.recreate();

                }

            }
        }
    }

    @Override
    public void onLocationChanged(@NonNull android.location.Location location) {
        double lat = location.getLatitude();
        double lon = location.getLongitude();

        GeoPoint center = new GeoPoint(lat, lon);

        mapController.animateTo(center);
        addMarker(center);

        Log.i("NOTE", "New location: {Lat: " + Double.toString(lat) + ", Long: " + Double.toString(lon) + "}");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
