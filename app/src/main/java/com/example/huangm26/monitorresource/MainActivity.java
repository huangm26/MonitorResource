package com.example.huangm26.monitorresource;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import library.src.main.java.com.jaredrummler.android.processes.ProcessManager;
import library.src.main.java.com.jaredrummler.android.processes.models.AndroidProcess;

/**
 * For the running process statistics, library from    https://github.com/jaredrummler/AndroidProcesses
 */


public class MainActivity extends AppCompatActivity{

    ListView listView;
    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        @Override
        //When Event is published, onReceive method is called
        public void onReceive(Context c, Intent i) {
            //Get Battery %
            int level = i.getIntExtra("level", 0);
            //Find textview control created in main.xml
            TextView tv = (TextView) findViewById(R.id.textfield);
            //Set TextView with text
            tv.setText("Battery Level: " + Integer.toString(level) + "%");
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Hide the title of App
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        registerReceiver(mBatInfoReceiver, new IntentFilter(
                Intent.ACTION_BATTERY_CHANGED));


        /*
         ** get the running processes by using the customized library
         */
//        ActivityManager actvityManager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
//        List<ActivityManager.RunningAppProcessInfo> processList =  actvityManager.getRunningAppProcesses();
        List<AndroidProcess> processList = ProcessManager.getRunningProcesses();

        //Display the current process number and CPU usage, and memory usage
        displayGeneralInfo(processList);
        //Display the processes in the listView
        displayProcessList(processList);


    }

    private void displayProcessList(List<AndroidProcess> processList)
    {
        //find the listView
        listView = (ListView)findViewById(R.id.processes_list);

        /*
         * The Customer adapter can handle List data instead of array
         * It will also display the process name and Pid in the text view.
         * Can add extra information if needed!!!!!
         */
        final CustomArrayAdapter myAdapter = new CustomArrayAdapter(this, processList);
        listView.setAdapter(myAdapter);


//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> arg0, View view, int position,
//                                    long id) {
//                AndroidProcess item = (AndroidProcess) myAdapter.getItem(position);
//                Log.d("Process", "Process Choosen    " + item.name);
//            }
//        });
        setAdapterListener(myAdapter);
    }

    private void setAdapterListener(final CustomArrayAdapter myAdapter)
    {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position,
                                    long id) {
                AndroidProcess item = (AndroidProcess) myAdapter.getItem(position);
                Log.d("Process", "Process Choosen    " + item.name);
                startNewIntent(item);
            }
        });
    }

    private void startNewIntent(AndroidProcess processChosen)
    {
        Intent displayDetail = new Intent(this, ProcessDetail.class);
        displayDetail.putExtra("PID",processChosen.pid);
        displayDetail.putExtra("Name", processChosen.name);
        startActivity(displayDetail);
    }

    private void displayGeneralInfo(List<AndroidProcess> processList)
    {
        displayMemInfo();
        calculateCPU();
    }

    private void calculateCPU()
    {
        long total = 0;
        long idle = 0;
        float usage = 0;
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("/proc/stat")), 1000);
            String load = reader.readLine();
            reader.close();

            String[] toks = load.split(" ");
            long currTotal = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[4]);
            long currIdle = Long.parseLong(toks[5]);

            usage = currTotal * 100.0f / (currTotal + currIdle);
            total = currTotal;
            idle = currIdle;
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }

        displayCPU(usage);
    }

    private void displayCPU(float usage){
        TextView cpuView = (TextView) findViewById(R.id.general_info);
        StringBuilder sb = new StringBuilder("Current CPU usage ");
        sb.append(usage);
        sb.append("%");
        cpuView.setText(sb.toString());
    }

    /*
     * Display the available memory and total memory in MB
     */
    private void displayMemInfo()
    {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        long availableMegs = mi.availMem / 1048576L;
        long totalMem = mi.totalMem / 1048576L;
        Log.d("Memory info", "Available memory " + availableMegs + " Total Available " + totalMem + "\n");
        TextView memoryView = (TextView) findViewById(R.id.memory_info);
        StringBuilder sb = new StringBuilder("Available memory ");
        sb.append(availableMegs).append("MB").append(" Total Available ").append(totalMem).append("MB");
        memoryView.setText(sb.toString());
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        switch (id)
        {
            case R.id.action_information:
                break;
            case R.id.action_setting:
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
