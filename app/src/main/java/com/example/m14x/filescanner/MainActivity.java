package com.example.m14x.filescanner;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.m14x.filescanner.Controller.CustomForegroundService;
import com.example.m14x.filescanner.Controller.ViewPagerAdapter;
import com.example.m14x.filescanner.Fragments.Average;
import com.example.m14x.filescanner.Fragments.Frecuent;
import com.example.m14x.filescanner.Fragments.Top10;
import com.example.m14x.filescanner.Model.Constants;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = "FileScanner";
    public static final String BROADCAST_KEY = "Files";
    private Button startButton;
    private HashMap<String,Integer> fileDetail = new HashMap<String,Integer>(); // file name of each file found in each folder , file size
    private HashMap<String,String> filePath = new HashMap<String,String>();//filepath of each folder , single name of each folder
    private ArrayList<String> folderName = new ArrayList<String>(); //name of each folder found in the external storage
    private List<Integer> sizeCollection;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Map<String,Integer> mExtentionFrec = new HashMap<String,Integer>();
    private Intent service;
    private CustomForegroundService foregroundService;




    final private int REQUEST_CODE_ASK_PERMISSIONS = 112;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startButton = (Button) findViewById(R.id.actionButton);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter(BROADCAST_KEY));
    }

    public void setUpViewPager(ViewPager viewPager){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new Top10(sizeCollection,fileDetail),"Top 10");
        adapter.addFrag(new Average(getAverage()),"Average");
        adapter.addFrag(new Frecuent(mExtentionFrec),"Trendy Ext");
        viewPager.setAdapter(adapter);
    }
    public void startReading(View view){

        if (android.os.Build.VERSION.SDK_INT >= 23) {
            //permission
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Log.d(LOG_TAG, "onCreate: " + "Show explanation");
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_CODE_ASK_PERMISSIONS);
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_CODE_ASK_PERMISSIONS);
                }
            } else {
                Log.d(LOG_TAG, "onCreate: " + "Permission already granted!");

                setUpService();
            }

        }else{
            setUpService();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(LOG_TAG, "onRequestPermissionsResult: Good to go!");
                    setUpService();

                } else {
                    Log.d(LOG_TAG, "onRequestPermissionsResult: Bad user");
                }
            }
        }
    }

    public void setUpService(){
        foregroundService = new CustomForegroundService();
        service = new Intent(MainActivity.this, CustomForegroundService.class);
        if (!foregroundService.IS_SERVICE_RUNNING) {
            service.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
            foregroundService.IS_SERVICE_RUNNING = true;
            startButton.setText("Stop");
        } else {
            service.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
            foregroundService.IS_SERVICE_RUNNING = false;
            startButton.setText("Start");

        }
        startService(service);
    }
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            folderName = intent.getStringArrayListExtra("folderName");
            filePath = (HashMap<String, String>) intent.getSerializableExtra("path-folder");
            fileDetail = (HashMap<String, Integer>) intent.getSerializableExtra("name-size");

            if (fileDetail.size() != 0) {

                Log.d(LOG_TAG, "fileDetail On Create: " + Integer.toString(fileDetail.size()));
                Log.d(LOG_TAG, "filePath On Create: " + Integer.toString(filePath.size()));
                Log.d(LOG_TAG, "folderName On Create: " + Integer.toString(folderName.size()));

                foregroundService.IS_SERVICE_RUNNING = false;
                stopService(service);
                startButton.setText("Start");


                orderBySize();
                getAverage();
                getExtention();

                setUpViewPager(viewPager);
                tabLayout = (TabLayout) findViewById(R.id.tabLayout);
                tabLayout.setupWithViewPager(viewPager);
            } else {
                Toast.makeText(getApplicationContext(), "No Files Found", Toast.LENGTH_SHORT).show();
            }
        }

    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        foregroundService.IS_SERVICE_RUNNING = false;
        stopService(service);
        startButton.setText("Start");
    }

    public void orderBySize(){
        sizeCollection = new ArrayList<Integer>(fileDetail.values());
        Collections.sort(sizeCollection,Collections.<Integer>reverseOrder());
        //Log.d(LOG_TAG,sizeCollection.toString());
        Log.d(LOG_TAG,sizeCollection.toString());
        Log.d(LOG_TAG,fileDetail.toString());

    }
    public int getAverage(){
       int size = sizeCollection.size();
        int sum = 0;
        for(int n: sizeCollection){
            sum+=n;
        }
        //Log.d(LOG_TAG,"Average "+ Integer.toString(sum/sizeCollection.size()));
        return (int)sum/size;

    }

    public void getExtention(){
        List<String> file = new ArrayList<String>(fileDetail.keySet());
        List<String> extention = new ArrayList<String>();
        String[] stringSplited = new String[2];
        for(String n: file){
            stringSplited= n.split(Pattern.quote("."));
            extention.add(stringSplited[1]);
        }
        HashSet<String> hashSet = new HashSet<String>(extention);
        for(String n: hashSet){
            mExtentionFrec.put(n,Collections.frequency(extention,n));

        }
        //Log.d(LOG_TAG,mExtentionFrec.toString());
    }
}
