package com.digitalDreams.millionaire_game;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class NotificationService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //Log.i("tagg","lllllllllllllllll");
        issueNotification(context);


    }

    void issueNotification(Context context)
    {

        // make the channel. The method has been discussed before.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            makeNotificationChannel("CHANNEL_1", "Millionaire Game", NotificationManager.IMPORTANCE_DEFAULT,context);
        }
        // the check ensures that the channel will only be made
        // if the device is running Android 8+

        NotificationCompat.Builder notification =
                new NotificationCompat.Builder(context, "CHANNEL_1");
        // the second parameter is the channel id.
        // it should be the same as passed to the makeNotificationChannel() method
        Intent intentAction = new Intent(context,Dashboard.class);
        final int flag =  Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE : PendingIntent.FLAG_UPDATE_CURRENT;


        PendingIntent pIntent = PendingIntent.getActivity(context,1,intentAction,flag);

String message = "Beat your last score in Millionaire Game and stand tall as a champion. \n PLAY NOW";
        notification
                .setSmallIcon(R.drawable.game_logo) // can use any other icon
                .setBadgeIconType(NotificationCompat.BADGE_ICON_LARGE)
                //.setLargeIcon(R.drawable.game_logo)
                .setLargeIcon(BitmapFactory.decodeResource (context.getResources(), R.drawable.game_logo ))

                .setContentTitle("Millionaire Game")

                .setContentIntent(pIntent)


                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))


                .setContentText(message)
                .setNumber(5).build(); // this shows a number in the notification dots


        NotificationManager notificationManager =
                (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);

        assert notificationManager != null;
        notificationManager.notify(1, notification.build());
        // it is better to not use 0 as notification id, so used 1.
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void makeNotificationChannel(String id, String name, int importance,Context context)
    {
        NotificationChannel channel = new NotificationChannel(id, name, importance);
        channel.setShowBadge(true); // set false to disable badges, Oreo exclusive

        NotificationManager notificationManager =
                (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);

        assert notificationManager != null;
        notificationManager.createNotificationChannel(channel);
    }



}
