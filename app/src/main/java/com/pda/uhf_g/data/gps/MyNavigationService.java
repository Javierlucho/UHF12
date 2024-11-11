package com.pda.uhf_g.data.gps;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

//import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.location.Priority;
import com.pda.uhf_g.R;

public class MyNavigationService  extends Service {
//    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "channel_id", // Unique channel ID
                    "Channel Name", // Channel name visible to the user
                    NotificationManager.IMPORTANCE_DEFAULT // Importance level
            );
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private Notification buildNotification() {
        // Replace with your icon
        // Make the notification ongoing (non-dismissible)
        return new NotificationCompat.Builder(this, "channel_id")
                .setContentTitle("My Navigation Service")
                .setContentText("Location tracking is active")
                .setSmallIcon(R.mipmap.logo) // Replace with your icon
                .setOngoing(true) // Make the notification ongoing (non-dismissible)
                .build();
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Create notification channel (if needed)
        createNotificationChannel();

        // Build the notification
        Notification notification = buildNotification();

        // Start foreground service and display notification
        startForeground(1, notification);

        // ... other service logic ...
        return START_NOT_STICKY; // Or other appropriate return value
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
