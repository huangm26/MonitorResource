package com.example.huangm26.monitorresource;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

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
