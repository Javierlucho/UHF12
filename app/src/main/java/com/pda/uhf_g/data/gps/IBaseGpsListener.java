package com.pda.uhf_g.data.gps;

import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;


public interface IBaseGpsListener extends LocationListener, GpsStatus.Listener {
    void onGpsStatusChanged(int i);

    void onLocationChanged(Location location);

    void onStatusChanged(String s, int i, Bundle bundle);

    void onProviderEnabled(String provider);

    void onProviderDisabled(String provider);
}
