package com.example.appmomos.pdfdownloader.broadcastReceivers;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.example.appmomos.pdfdownloader.Activity1;
import com.example.appmomos.pdfdownloader.R;

public class UpdateDownloadProgressBroadCastReceiver extends BroadcastReceiver
{
    String fileName = "";

    NotificationCompat.Builder builder;
    NotificationManager notificationmanager;


    @Override
    public void onReceive(Context context, Intent intent)
    {

        if (intent != null)
        {
            String msg = intent.getStringExtra("msg");
            fileName = intent.getStringExtra("fileName");
            Notification(context, msg,fileName);
        }
    }


    @SuppressLint("SetTextI18n")
    public void Notification(Context context, String message, String fileName)
    {
        // Create Notification using NotificationCompat.Builder

        Intent intent = new Intent();
        intent.setAction("Cancel Download BroadCast");
        PendingIntent cancelDownloadIntent = PendingIntent.getBroadcast(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);



        if (Integer.parseInt(message) < 99)
        {
            try {
                Activity1.updateTextViewFun("Downloading....");
            } catch (Exception ignored) {

            }


            builder = new NotificationCompat.Builder(
                    context)
                    // Set Icon
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    // Set Ticker Message
                    .setTicker("tiacker "+message)
                    // Set Title
                    .setContentTitle(fileName)
                    // Set Text
                    .setContentText(message+"%")
                    .setProgress(100, Integer.parseInt(message), false)
                    // Add an Action Button below Notification
                    .addAction(R.drawable.ic_launcher_background, "Cancel", cancelDownloadIntent)
                    // Set PendingIntent into Notification
                    //  .setContentIntent(pStopSelf)
                    // Dismiss Notification
                    .setAutoCancel(true);
        }
        else
        {
            try {
                Activity1.updateTextViewFun("Download Completed.");
            } catch (Exception ignored) {

            }

            //textView.setText("Download Completed.");
            Toast.makeText(context, context.getResources().getString(R.string.pdfDownloadedMsg)+"\t /sdcard/"+fileName, Toast.LENGTH_SHORT).show();

            builder = new NotificationCompat.Builder(
                    context)
                    // Set Icon
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    // Set Ticker Message
                    .setTicker("tiacker "+message)
                    // Set Title
                    .setContentTitle("Download Completed.")
                    // Set Text
                    .setContentText("100%")
                    .setProgress(100, 100, false)
                    // Add an Action Button below Notification
                    // .addAction(R.drawable.ic_launcher_background, "Cancel", cancelDownloadIntent)
                    // Set PendingIntent into Notification
                    //  .setContentIntent(pStopSelf)
                    // Dismiss Notification
                    .setAutoCancel(true);

        }


        // Create Notification Manager
        notificationmanager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // Build Notification with Notification Manager
        assert notificationmanager != null;
        notificationmanager.notify(0, builder.build());

    }




}
