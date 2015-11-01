package com.example.huangm26.monitorresource;

import android.app.ActivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Hide the title of App
        getSupportActionBar().setDisplayShowTitleEnabled(false);


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
        ListView listView = (ListView)findViewById(R.id.processes_list);
        /*
         * The Customer adapter can handle List data instead of array
         * It will also display the process name and Pid in the text view.
         * Can add extra information if needed!!!!!
         */
        CustomArrayAdapter myAdapter = new CustomArrayAdapter(this, processList);
        listView.setAdapter(myAdapter);
    }

    private void displayGeneralInfo(List<AndroidProcess> processList)
    {
        displayMemInfo();
        displayCPU();
    }

    private void displayCPU()
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
            Log.d("bbbb", "bbbb" + toks[2]);
            Log.d("cccc", "cccc" + toks[8]);
            long currTotal = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[4]);
            long currIdle = Long.parseLong(toks[5]);

            usage = currTotal * 100.0f / (currTotal + currIdle);
            total = currTotal;
            idle = currIdle;
            TextView cpuView = (TextView) findViewById(R.id.general_info);
            StringBuilder sb = new StringBuilder("Current CPU usage ");
            sb.append(usage);
            sb.append("%");
            cpuView.setText(sb.toString());
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
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
