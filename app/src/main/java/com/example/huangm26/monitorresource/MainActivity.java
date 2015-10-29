package com.example.huangm26.monitorresource;

import android.app.ActivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Iterator;
import java.util.List;
import library.src.main.java.com.jaredrummler.android.processes.ProcessManager;
import library.src.main.java.com.jaredrummler.android.processes.models.AndroidAppProcess;
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


//        ActivityManager actvityManager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
//        List<ActivityManager.RunningAppProcessInfo> processList =  actvityManager.getRunningAppProcesses();
        List<AndroidProcess> processList = ProcessManager.getRunningProcesses();
        StringBuilder sb = new StringBuilder();
        for (AndroidProcess process : processList) {
            sb.append(process.name).append('\n');
        }
        Log.d("Process", "List size " + processList.size());
//        Iterator iterator = processList.iterator();
        Log.d("Process",sb.toString());
//        while(iterator.hasNext())
//        {
//            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo)iterator.next();
//            Log.d("Process ","Process " + info.pid + " " + info.processName);
//        }
    }

    private void setProcessList( List<ActivityManager.RunningAppProcessInfo> processList)
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
