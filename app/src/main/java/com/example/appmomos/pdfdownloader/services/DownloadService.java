package com.example.appmomos.pdfdownloader.services;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;


public class DownloadService extends Service
{

    //Pdf File Download
    static InputStream input = null;
    String fileName;
    static OutputStream output = null;
    Boolean haveSdCardPerimission = false;
    String urlLink;

    String oldProgress = "0";
    String newProgress;

    static DownloadFileAsync downloadFileAsync;

    IBinder mBinder = new LocalBinder();


    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class LocalBinder extends Binder
    {
        public DownloadService getServerInstance()
        {
            return DownloadService.this;
        }
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        if(intent != null)
        {
            urlLink = intent.getStringExtra("urlLink");
            fileName = urlLink.substring(urlLink.lastIndexOf('/') + 1);
            downloadFileAsync = (DownloadFileAsync) new DownloadFileAsync().execute(urlLink);
        }

    }



    // Dialog to  pdf Download progress
    @SuppressLint("StaticFieldLeak")
    private class DownloadFileAsync extends AsyncTask<String, String, String>
    {

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

                while ((count = input.read(data)) != -1)
                {
                    //Checking Wifi or mobile connectivity
                    do
                    {
                        if(isInternetOn())
                        {
                            if (isCancelled())
                                break;
                            total += count;
                            publishProgress(""+(int)((total*100)/lenghtOfFile));
                            output.write(data, 0, count);
                        }
                        else
                        {
                            Thread.sleep(3000);
                        }

                    }while(isInternetOn());
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


        @Override
        protected void onProgressUpdate(String... values)
        {
            super.onProgressUpdate(values);
            newProgress = values[0];
            if (!oldProgress.equals(newProgress))
            {
                Intent intent = new Intent("update Notification");
                intent.putExtra("msg", values[0]);
                intent.putExtra("fileName", fileName);
                sendBroadcast(intent);

                oldProgress = newProgress;
            }


        }
    }


    public static void cancelDownloadFun()
    {
        downloadFileAsync.cancel(true);
    }


    public boolean isInternetOn() {

        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // Check for network connections
        assert connec != null;
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {


            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {


            return false;
        }
        return false;
    }

}
