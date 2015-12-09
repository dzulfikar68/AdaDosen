package com.digitcreativestudio.safian.adadosen.Utils;

import android.app.NotificationManager;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.digitcreativestudio.safian.adadosen.R;

/**
 * Created by faqih_000 on 11/17/2015.
 */
public class MyLocationListener implements LocationListener{

    private Context _context;

    public MyLocationListener(Context context){
        _context = context;
    }
        public void onLocationChanged(Location location) {
            // Called when a new location is found by the network location provider.
            //makeUseOfNewLocation(location);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(_context)
                    .setSmallIcon(R.drawable.ic_action_refresh)
                    .setContentTitle(_context.getString(R.string.app_name))
                    .setContentText(location.getLatitude()+", "+location.getLongitude())
                    .setAutoCancel(true);


            NotificationManager notificationManager =
                    (NotificationManager) _context.getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(10210, notificationBuilder.build());
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {}

        public void onProviderEnabled(String provider) {}

        public void onProviderDisabled(String provider) {}
}
