package com.example.appmomos.pdfdownloader;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
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


import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import static android.app.ProgressDialog.STYLE_HORIZONTAL;
import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity
{

    Button btn;
    TextView textView;

    //Pdf File Download
    private ProgressDialog progressDialog;
    static InputStream input = null;
    String fileName;
    String urlLink = "http://appmomos.com/walna_latest/system/storage/download/MPL_2017_low-res.pdf";
    static OutputStream output = null;
    Boolean haveSdCardPerimission = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.title);
        btn = findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                fileName = urlLink.substring(urlLink.lastIndexOf('/') + 1);
                new DownloadFileAsync().execute(urlLink);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        isStoragePermissionGranted();
    }

    // Dialog to  pdf Download progress
    @SuppressLint("StaticFieldLeak")
    private class DownloadFileAsync extends AsyncTask<String, String, String>
    {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMax(100);
            progressDialog.setCancelable(false);
            progressDialog.setTitle("Downloading Pdf");
            progressDialog.setMessage("\nPlease wait..");
            progressDialog.setProgressStyle(STYLE_HORIZONTAL);
            progressDialog.show();
        }

        @SuppressLint({"DefaultLocale", "SdCardPath"})
        @Override
        protected String doInBackground(String... aurl) {
            int count;

            try {

                URL url = new URL(aurl[0]);
                URLConnection conexion = url.openConnection();
                conexion.connect();

                int lenghtOfFile = conexion.getContentLength();

                input = new BufferedInputStream(url.openStream());

                output = new FileOutputStream("/sdcard/"+fileName);

                byte[] data;
                data = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress(""+(int)((total*100)/lenghtOfFile));
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();
                haveSdCardPerimission = true;
            }
            catch (Exception ignored)
            {
                haveSdCardPerimission = false;
                Log.d("Error", String.valueOf(ignored));
            }
            return null;

        }

        protected void onProgressUpdate(String... progress)
        {
            progressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @SuppressLint("SdCardPath")
        @Override
        protected void onPostExecute(String unused)
        {
            progressDialog.dismiss();
            if (haveSdCardPerimission)
            {
                Toast.makeText(MainActivity.this, getResources().getString(R.string.pdfDownloadedMsg)+"\t /sdcard/"+fileName, Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(MainActivity.this, getResources().getString(R.string.sdcardPermissionError), Toast.LENGTH_LONG).show();
            }
        }
    }




    // Getting Read Write Permission to Sd card
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
        }
    }



    public void isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (MainActivity.this.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
        }
    }







}
