package com.example.appmomos.pdfdownloader;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appmomos.pdfdownloader.services.DownloadService;
import static android.content.ContentValues.TAG;

public class Activity1 extends AppCompatActivity
{

    Button btn;
    @SuppressLint("StaticFieldLeak")
    static TextView textView;

    //Pdf File Download
     String urlLink = "http://appmomos.com/walna_latest/system/storage/download/MPL_2017_low-res.pdf";
   // String urlLink = "https://www.tutorialspoint.com/java/java_tutorial.pdf";
   // String urlLink = "https://resize.indiatvnews.com/en/resize/newbucket/715_-/2017/12/xx-1512970816.jpg";
    String fileName;


    //DownloadService downloadService;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_1);

        textView = findViewById(R.id.title);
        btn = findViewById(R.id.btn);

        isStoragePermissionGranted();

        btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(Activity1.this, DownloadService.class);
                i.putExtra("urlLink",urlLink);
               // bindService(i, mConnection, BIND_AUTO_CREATE);
                startService(i);

                textView.setVisibility(View.VISIBLE);

                fileName = urlLink.substring(urlLink.lastIndexOf('/') + 1);

                Toast.makeText(Activity1.this, "Download Starting.. ", Toast.LENGTH_SHORT).show();
            }
        });

    }

/*

    ServiceConnection mConnection = new ServiceConnection()
    {
        @Override
        public void onServiceDisconnected(ComponentName name)
        {
            downloadService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service)
        {
            DownloadService.LocalBinder mLocalBinder = (DownloadService.LocalBinder)service;
            downloadService = mLocalBinder.getServerInstance();
        }
    };
*/



/*    public  boolean isServiceRunning(Class<?> serviceClass)
    {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        assert manager != null;
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
        {
            if (serviceClass.getName().equals(service.service.getClassName()))
            {
                return true;
            }
        }
        return false;
    }*/

    public static void updateTextViewFun(String msg)
    {
        textView.setVisibility(View.VISIBLE);
        textView.setText(msg);
    }


    // Getting Read Write Permission to Sd card
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
        }
    }



    public void isStoragePermissionGranted()
    {
        if (Build.VERSION.SDK_INT >= 23)
        {
            if (Activity1.this.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(Activity1.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
        else
        { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
        }
    }

}
