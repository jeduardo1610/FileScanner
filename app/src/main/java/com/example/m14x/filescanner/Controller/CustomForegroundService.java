package com.example.m14x.filescanner.Controller;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;
import com.example.m14x.filescanner.MainActivity;
import com.example.m14x.filescanner.Model.Constants;
import com.example.m14x.filescanner.R;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by m14x on 04/20/2016.
 */
public class CustomForegroundService extends Service {

    private static final String LOG_TAG = "FileScanner";
    public static boolean IS_SERVICE_RUNNING = false;

    private HashMap<String,Integer> fileDetail = new HashMap<String,Integer>(); // file name of each file found in each folder , file size
    private HashMap<String,String> filePath = new HashMap<String,String>();//filepath of each folder , single name of each folder
    private ArrayList<String> folderName = new ArrayList<String>(); //name of each folder found in the external storage




    public CustomForegroundService(){

    }
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION)) {
            //Log.d(LOG_TAG, "Received Start Foreground Intent ");
            showNotification();
            Toast.makeText(this, "Scanning for your files!", Toast.LENGTH_SHORT).show();

            File dir= Environment.getExternalStorageDirectory();
            Log.d("dirTag",dir.toString());
            try {

                getFiles(dir);
                Intent dataIntent = new Intent(MainActivity.BROADCAST_KEY);
                sendData(dataIntent);
                Log.d(LOG_TAG,"fileDetail Service: "+Integer.toString(fileDetail.size()));
                Log.d(LOG_TAG,"filePath Service : "+Integer.toString(filePath.size()));
                Log.d(LOG_TAG,"folderName Service: "+Integer.toString(folderName.size()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            dir.deleteOnExit();


        }  else if (intent.getAction().equals(Constants.ACTION.PLAY_ACTION)) {
            Log.d(LOG_TAG, "Clicked Play");

            Toast.makeText(this, "Clicked Play!", Toast.LENGTH_SHORT).show();
        } else if (intent.getAction().equals(Constants.ACTION.STOP_ACTION)) {
            Log.d(LOG_TAG, "Clicked Stop");

            Toast.makeText(this, "Clicked Stop!", Toast.LENGTH_SHORT).show();
        } else if (intent.getAction().equals(
                Constants.ACTION.STOPFOREGROUND_ACTION)) {
            Log.d(LOG_TAG, "Received Stop Foreground Intent");
            stopForeground(true);
            stopSelf();
        }
        return START_STICKY;
    }

    public void getFiles(File dir) throws IOException {

      File[] files = dir.listFiles();
        filePath.clear();
        for (File file : files){
            filePath.put(String.valueOf(file.getAbsoluteFile()),String.valueOf(file.getName()));//Folder path, Folder single name
            folderName.add(String.valueOf(file.getName()));
        }

        for(String fName : folderName) {

            File folder = new File(dir, fName);

            for (File file : folder.listFiles()) {
                if (file.isFile()) {
                    int fileSize = (int) file.length();
                    fileDetail.put(file.getName(),fileSize);
                }
            }
        }
    }

    private void showNotification() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Intent playIntent = new Intent(this, CustomForegroundService.class);
        playIntent.setAction(Constants.ACTION.PLAY_ACTION);
        PendingIntent pplayIntent = PendingIntent.getService(this, 0,
                playIntent, 0);

        Intent stopIntet = new Intent(this, CustomForegroundService.class);
        stopIntet.setAction(Constants.ACTION.STOP_ACTION);
        PendingIntent pstopIntent = PendingIntent.getService(this, 0,
                stopIntet, 0);

        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_launcher);

        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("File Scanner")
                .setTicker("File Scaner")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(Bitmap.createScaledBitmap(icon, 128, 128, false))
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .addAction(android.R.drawable.ic_media_play, "Play",
                        pplayIntent)
                .addAction(android.R.drawable.ic_media_pause, "Stop",
                       pstopIntent).build();
        startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE,
                notification);

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "In onDestroy");
        Toast.makeText(getApplicationContext(),"Process completed",Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Used only in case if services are bound (Bound Services).
        return null;
    }

  public void sendData(Intent intent){
      intent.putExtra("name-size", fileDetail);
      intent.putExtra("path-folder", filePath);
      intent.putExtra("folderName", folderName);
      LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
  }

}
