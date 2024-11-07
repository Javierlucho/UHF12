package com.pda.uhf_g.data.gps;

import android.location.Location;

import androidx.annotation.NonNull;

//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationCallback;
//import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.location.LocationResult;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.location.Priority;

public class LocationDataSource {
    //FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

    public void setupLocationUpdates() {


//        LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
//                .setWaitForAccurateLocation(false) // Optional: Wait for a more accurate location
//                .setMinUpdateIntervalMillis(5000) // Optional: Minimum update interval
//                .setMaxUpdateDelayMillis(15000) // Optional: Maximum update delay
//                .setDurationMillis(60000) // Optional: Duration for which location updates are requested
//                .build();
//
//        LocationCallback locationCallback = new LocationCallback() {
//            @Override
//            public void onLocationResult(@NonNull LocationResult locationResult) {
//                for (Location location : locationResult.getLocations()) {
//                    // Handle location updates here
//                    double latitude = location.getLatitude();
//                    double longitude = location.getLongitude();
//
//                }
//            }
//        };
    }
    public void requestLocationUpdates() {
        //fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }
    public void stopLocationUpdates() {
        //fusedLocationClient.removeLocationUpdates(locationCallback);
    }
}
