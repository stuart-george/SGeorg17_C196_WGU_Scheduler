package edu.wgu.sgeor17.wguscheduler.utility;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import edu.wgu.sgeor17.wguscheduler.R;

public class AlertBroadcastReceiver extends BroadcastReceiver {

    int notificationID = -1;
    String notificationTitle = "Title Not Set";
    String notificationMessage = "Message was not set";
    String notificationTag = "default";
    boolean isCourse, isAssessment;

    @Override
    public void onReceive(Context context, Intent intent) {


        Bundle extras = intent.getExtras();
        if (extras != null) {
            notificationID = extras.getInt(Constants.NOTIFICATION_ID_KEY, -1);
            notificationTitle = extras.getString(Constants.NOTIFICATION_TITLE_KEY , "Title Not Set");
            notificationMessage = extras.getString(Constants.NOTIFICATION_MESSAGE_KEY, "Message was not Set");
            notificationTag = extras.getString(Constants.NOTIFICATION_TAG_KEY, "default");
        }


        Notification notification = new NotificationCompat.Builder(context, context.getString(R.string.default_channel_id))
                .setSmallIcon(R.drawable.ic_adb)
                .setContentTitle(notificationTitle)
                .setContentText(notificationMessage)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .build();
        NotificationManagerCompat.from(context).notify(notificationTag, notificationID, notification);
    }
}
