package com.example.huangm26.monitorresource;

import android.app.ActivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ProcessDetail extends AppCompatActivity implements View.OnClickListener{

    long total = 0;
    long idle = 0;
    float total_usage = 0;

    float process_usage = 0;
    long memory_usage = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button exit_button = (Button) findViewById(R.id.exit_button);
        exit_button.setOnClickListener(this);

        int pid = getIntent().getExtras().getInt("PID");
        String name = getIntent().getExtras().getString("Name");
//        Toast.makeText(this, name + " PID " + pid, Toast.LENGTH_SHORT).show();
        process_usage = parseCPUUsage(pid);
        memory_usage = getMemory(pid, name);

        TextView displayView = (TextView) findViewById(R.id.process_info);
        StringBuilder sb = new StringBuilder("The Process name:    ");
        sb.append(name).append("\n");
        sb.append("The PID    ").append(pid).append("\n");
        sb.append("The CPU usage    ").append(process_usage).append("\n");
        sb.append("The Memory usage    ").append(memory_usage).append("\n");
        displayView.setText(sb.toString());
    }

    private void calculateTotalCPU()
    {
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("/proc/stat")), 1000);
            String load = reader.readLine();
            reader.close();

            String[] toks = load.split(" ");
            long currTotal = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[4]);
            long currIdle = Long.parseLong(toks[5]);

            total_usage = currTotal * 100.0f / (currTotal + currIdle);
            total = currTotal;
            idle = currIdle;
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }

    }

    private float parseCPUUsage(int pid)
    {
        calculateTotalCPU();
        float usage = 0.0f;
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("/proc/" + pid + "/stat")), 1000);
            String load = reader.readLine();
            reader.close();

            String[] toks = load.split(" ");
//            Toast.makeText(this, toks[1], Toast.LENGTH_SHORT).show();
            long currTotal = Long.parseLong(toks[13]) + Long.parseLong(toks[14]);
            usage = currTotal * 100.0f / (total + idle);
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
        return usage;
    }

    private long getMemory(int pid, String name)
    {
        ActivityManager activityManager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        int [] pids = {pid};
        android.os.Debug.MemoryInfo[] memoryInfoArray = activityManager.getProcessMemoryInfo(pids);
        android.os.Debug.MemoryInfo pidMemoryInfo = memoryInfoArray[0];

        Log.d("Memory", String.format("** MEMINFO in pid %d [%s] **\n", pids[0], name));
        Log.d("Memory", " pidMemoryInfo.getTotalPrivateDirty(): " + pidMemoryInfo.getTotalPrivateDirty() + "\n");
        Log.d("Memory", " pidMemoryInfo.getTotalPss(): " + pidMemoryInfo.getTotalPss() + "\n");
        Log.d("Memory", " pidMemoryInfo.getTotalSharedDirty(): " + pidMemoryInfo.getTotalSharedDirty() + "\n");
        return (pidMemoryInfo.getTotalPrivateDirty() + pidMemoryInfo.getTotalPss() + pidMemoryInfo.getTotalSharedDirty());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.exit_button:
                finish();
                break;
            default:
                break;
        }
    }
}
