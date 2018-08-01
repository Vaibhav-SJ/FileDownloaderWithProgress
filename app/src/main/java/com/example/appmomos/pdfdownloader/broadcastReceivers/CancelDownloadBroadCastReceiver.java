package com.example.appmomos.pdfdownloader.broadcastReceivers;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.example.appmomos.pdfdownloader.Activity1;
import com.example.appmomos.pdfdownloader.R;
import com.example.appmomos.pdfdownloader.services.DownloadService;

public class CancelDownloadBroadCastReceiver extends BroadcastReceiver
{

    NotificationCompat.Builder builder;
    NotificationManager notificationmanager;
    //DownloadService downloadService;
   // boolean mBounded;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (intent != null)
        {
            boolean serviceRunningStatus = isServiceRunning(context ,DownloadService.class);
            if (serviceRunningStatus)
            {
                DownloadService.cancelDownloadFun();
                Activity1.updateTextViewFun("Download Cancelled.");

               /* try {
                    context.unbindService(mConnection);
                }
                catch (Exception ignored)
                {

                }
                mBounded = false;*/
                Toast.makeText(context, "Download Cancelled.", Toast.LENGTH_SHORT).show();
                context.stopService(new Intent(context, DownloadService.class));
            }
            cancelNotificationFun(context);
        }

    }

    private void cancelNotificationFun(Context context)
    {
        // Create Notification Manager
        notificationmanager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // Build Notification with Notification Manager


        builder = new NotificationCompat.Builder(context)
                // Set Icon
                .setSmallIcon(R.drawable.ic_launcher_background)
                // Set Ticker Message
                .setTicker("")
                // Set Title
                .setContentTitle("Download Cancelled")
                // Set Text
                .setContentText("")
                // Add an Action Button below Notification
                //.addAction(R.drawable.ic_launcher_background, "Cancel", cancelDownloadIntent)
                // Set PendingIntent into Notification
                //  .setContentIntent(pStopSelf)
                // Dismiss Notification
                .setAutoCancel(false);

        assert notificationmanager != null;
        notificationmanager.cancel(0);
        notificationmanager.notify(1, builder.build());

    }


   /* ServiceConnection mConnection = new ServiceConnection()
    {
        @Override
        public void onServiceDisconnected(ComponentName name)
        {
            mBounded = false;
            downloadService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service)
        {
            mBounded = true;
            DownloadService.LocalBinder mLocalBinder = (DownloadService.LocalBinder)service;
            downloadService = mLocalBinder.getServerInstance();
        }
    };*/



    public  boolean isServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        assert manager != null;
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }



}
