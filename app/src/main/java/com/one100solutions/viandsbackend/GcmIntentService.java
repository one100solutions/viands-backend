package com.one100solutions.viandsbackend;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * Created by sujith on 22/3/15.
 */
public class GcmIntentService extends IntentService {

    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;


    public GcmIntentService() {
        super("Gcm Intent Service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();

        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        String messageType = gcm.getMessageType(intent);

        String msg1 = extras.getString("key1");
        String msg2 = extras.getString("key2");

        Log.e("HomeActivity GCM", "Message Content:" + msg1 + ", " + msg2);

        sendNotification(msg1, msg2);

        GcmBroadCastReceiver.completeWakefulIntent(intent);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String msg1, String msg2) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        //PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
        //      new Intent(this, MainActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setLargeIcon(((BitmapDrawable) getApplicationContext().getResources().getDrawable(R.drawable.viands_icon)).getBitmap())
                        .setSmallIcon(R.drawable.viands_icon)
                        .setContentTitle(msg1)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg1))
                        .setContentText(msg2)
                        .setAutoCancel(true);


        // mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

}
