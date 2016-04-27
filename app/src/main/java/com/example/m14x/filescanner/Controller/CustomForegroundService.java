package com.example.m14x.filescanner.Controller;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
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
import java.util.TreeMap;


/**
 * Created by m14x on 04/20/2016.
 */
public class CustomForegroundService extends IntentService {

    private static final String LOG_TAG = "FileScanner";
    public static boolean IS_SERVICE_RUNNING = false;
    private HashMap<String,Integer> fileDetail = new HashMap<>(); // file name of each file found in each folder , file size
    private HashMap<String,String> filePath = new HashMap<String,String>();//filepath of each file , name of each file
    private ArrayList<String> folderName = new ArrayList<String>(); //name of each folder found in the external storage
    private ArrayList<File> files = new ArrayList<>(); //all files found
    private Context context;
    public static volatile boolean SERVICE_RUNNING = true;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public CustomForegroundService(String name) {
        super(name);

    }
    public CustomForegroundService(){
        super("ForegrounService");
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if(SERVICE_RUNNING) {
            try {
                getFiles(Environment.getExternalStorageDirectory().getPath() + "/", files);
                Intent dataIntent = new Intent(MainActivity.BROADCAST_KEY);
                sendData(dataIntent);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

/*    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        if (intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION)) {

            try {
                getFiles(Environment.getExternalStorageDirectory().getPath() + "/",files);
                Intent dataIntent = new Intent(MainActivity.BROADCAST_KEY);
                sendData(dataIntent);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


            if(intent.getAction().equals(Constants.ACTION.STOPFOREGROUND_ACTION)){
                stopForeground(true);
                stopSelf();
                Toast.makeText(this,"Service Stopped",Toast.LENGTH_SHORT).show();
                Log.d(LOG_TAG,"Service Stopped");
            }


/*

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
        }*/
     /*   return START_STICKY;
    }*/

    public void getFiles(String directoryName, ArrayList<File> files) throws IOException {
        File directory = new File(directoryName);
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // get all the files from a directory
        File[] fList = directory.listFiles();
        for (File file : fList) {
            if (file.isFile()) {
                files.add(file);
                filePath.put(file.getAbsolutePath(),file.getName());
                int length = (int) file.length();
                fileDetail.put(file.getName(),length);
               Log.d("FILES - FILE",file.getName());
                //Log.d("FILES",file.getPath());

            } else if (file.isDirectory()) {
                Log.d("FILES - FOLDER",file.getName());
                folderName.add(file.getName());
                getFiles(file.getAbsolutePath(), files);
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
    public void sendData(Intent intent){
      intent.putExtra("name-size", fileDetail);
      intent.putExtra("path-folder", filePath);
      intent.putExtra("folderName", folderName);
      LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
  }




}
