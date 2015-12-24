package com.digitcreativestudio.safian.adadosen.Utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.digitcreativestudio.safian.adadosen.MainActivity;
import com.digitcreativestudio.safian.adadosen.R;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by faqih_000 on 11/10/2015.
 */
public class MyNotificationManager extends BroadcastReceiver{

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;
    private static final int MAX_NOTIFICATION = 5;
    private static final int  NOTIFICATION_ID = 1756;
    private static final String KEY_CANCEL_NOTIFICATION = "notification_cancelled";

    // Sharedpref file name
    private static final String PREF_NAME = "Notification";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_NOTIFICATION = "notification";

    public MyNotificationManager(){
    }
    // Constructor
    public MyNotificationManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     * */
    public void createNotification(String newLine){
        // Storing login value as TRUE
        //Set<String> notifications = pref.getStringSet(KEY_NOTIFICATION, new HashSet<String>());
        Set<String> notifications = new LinkedHashSet<>();
        notifications.add(newLine);
        notifications.addAll(pref.getStringSet(KEY_NOTIFICATION, new LinkedHashSet<String>()));
        editor.putStringSet(KEY_NOTIFICATION, notifications);
        editor.commit();

        Intent intent = new Intent(_context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(_context, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Intent deleteIntent = new Intent(_context, MyNotificationManager.class);
        deleteIntent.setAction(KEY_CANCEL_NOTIFICATION);


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(_context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(_context.getString(R.string.app_name))
                .setContentText(notifications.size() == 1 ? newLine : notifications.size()+" updates")
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(pendingIntent)
                .setDeleteIntent(PendingIntent.getBroadcast(_context, 0, deleteIntent, PendingIntent.FLAG_CANCEL_CURRENT));

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle(_context.getString(R.string.app_name));

        String[] notificationsArray = notifications.toArray(new String[notifications.size()]);

        for(int i = 0; i < notificationsArray.length; i++){
            if(i == MAX_NOTIFICATION){
                break;
            }
            inboxStyle.addLine(notificationsArray[i]);
        }
        if(notificationsArray.length > MAX_NOTIFICATION){
            inboxStyle.setSummaryText("+"+(notifications.size()-MAX_NOTIFICATION)+" more");
        }

        notificationBuilder.setStyle(inboxStyle);

        NotificationManager notificationManager =
                (NotificationManager) _context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    public void removeNotifications(){
        editor.clear();
        editor.commit();

        NotificationManager nm = (NotificationManager) _context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(NOTIFICATION_ID);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if (action.equals(KEY_CANCEL_NOTIFICATION)) {
            Log.e("Cancel Reciever", "Broadcast received");
            SharedPreferences sp = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
            sp.edit().clear().commit();
            Log.e("Cancel Reciever", "Successfully deleted");
        }
    }
}
