package com.araara.mocho;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationServiceActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleMap.OnMarkerClickListener,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    public static final String TAG = LocationServiceActivity.class.getSimpleName();
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private Marker mCurrLocationMarker;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    LocationRequest mLocationRequest;
    private int idx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_service);
        idx = getIntent().getIntExtra("idxmonster", 0);
        // Create an instance of GoogleAPIClient.

        // call location settings intent
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = service
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!enabled) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Enable GPS Settings");
            builder.setMessage("Do you want to enable GPS for better tracking?");

            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    // Do nothing but close the dialog
                    startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
                    dialog.dismiss();
                }
            });

            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    // Do nothing
                    dialog.dismiss();
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }


        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.setOnMarkerClickListener(this);
        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
            else{
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        Log.i(TAG, "MASUK SAMPE SINI");

        LatLng labtekV = new LatLng(-6.890598, 107.609628);
        mMap.addMarker(new MarkerOptions().position(labtekV).title("Labtek V"));

        LatLng bengkok = new LatLng(-6.889300, 107.611734);
        mMap.addMarker(new MarkerOptions().position(bengkok).title("Kantin Bengkok"));

        LatLng tengahBawah = new LatLng(-6.893218, 107.610443);
        mMap.addMarker(new MarkerOptions().position(tengahBawah).title("Gerbang Depan"));

//        LatLng kananBawah = new LatLng(-6.893580, 107.611881);
//        mMap.addMarker(new MarkerOptions().position(kananBawah).title("Lapangan Seni Rupa"));
//
//        LatLng kiriBawah = new LatLng(-6.893804, 107.608472);
//        mMap.addMarker(new MarkerOptions().position(kiriBawah).title("Lapangan Sipil"));
//
//        LatLng boulevard = new LatLng(-6.891908, 107.610390);
//        mMap.addMarker(new MarkerOptions().position(boulevard).title("Boulevard"));
//
//        LatLng tengah = new LatLng(-6.889891, 107.610357);
//        mMap.addMarker(new MarkerOptions().position(tengah).title("Titik Pusat"));
//
//        LatLng perpus = new LatLng(-6.888203, 107.610693);
//        mMap.addMarker(new MarkerOptions().position(perpus).title("Perpustakaan Pusat"));
//
//        LatLng barat = new LatLng(-6.890152, 107.608226);
//        mMap.addMarker(new MarkerOptions().position(barat).title("Titik Barat"));
//
//        LatLng nasijepang = new LatLng(-6.892491, 107.612052);
//        mMap.addMarker(new MarkerOptions().position(nasijepang).title("Nasi Jepang"));

        Log.i(TAG, "onMapReady done!");
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(tengahBawah));

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        Log.i(TAG, "onLocationChanged " + mLastLocation.toString());

        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(latLng.toString());
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17));

        //stop location updates
        /*
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }*/
    }

    protected synchronized void buildGoogleApiClient() {
        //Log.d(TAG, "build Google Api Client " + mLastLocation.toString());
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],@NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // Permission was granted.
                    if (ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient != null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {
                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
            }

            // other 'case' lines to check for other permissions this app might request.
            //You can add here other case statements according to your requirement.
        }
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        if (marker != mCurrLocationMarker) {
            Log.d(TAG, "onMarkerClick: " + countDistance(mCurrLocationMarker, marker));
            if (countDistance(mCurrLocationMarker, marker) > 10000) { // 10km
                Toast.makeText(this, "This location is too far.", Toast.LENGTH_LONG).show();
                return false;
            } else {
                Intent intent = new Intent(this, SubtypeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("loc", marker.getTitle());
                bundle.putInt("idxmonster", idx);

                intent.putExtra("bundle", bundle);
                startActivity(intent);
            }
        }
        return true;
    }

    private float countDistance(Marker m1, Marker m2) {
        float[] results = new float[1];
        Location.distanceBetween(m1.getPosition().latitude, m1.getPosition().longitude,
                m2.getPosition().latitude, m2.getPosition().longitude, results);
        return results[0];
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
        Log.i(TAG, "Kena Pause");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.i(TAG, "Kena Resume");
        if(mLocationRequest != null && mGoogleApiClient != null) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            }
        }
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }
}
