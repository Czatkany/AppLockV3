package com.example.tomi.applockv3;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.os.Looper;
import android.widget.CheckBox;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Tomi on 2014.12.01..
 */
public class BackGroundService extends AsyncTask {
    private ArrayList<String> apps;
    private Context appContext;
    private PackageManager pManager;
    ActivityManager am = null;

    public BackGroundService(ArrayList<String> apps, Context appContext, PackageManager pManager) {
        this.apps = apps;
        this.appContext = appContext;
        this.pManager = pManager;
        am = (ActivityManager) appContext.getSystemService(Context.ACTIVITY_SERVICE);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
    }

    @Override
    protected void onProgressUpdate(Object[] values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled(Object o) {
        super.onCancelled(o);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    public void run(){

        boolean appFinded = false;

        while(true){
            // Return a list of the tasks that are currently running,
            // with the most recent being first and older ones after in order.
            // Taken 1 inside getRunningTasks method means want to take only
            // top activity from stack and forgot the olders.
            List< ActivityManager.RunningTaskInfo > taskInfo = am.getRunningTasks(1);

            String currentRunningActivityName = taskInfo.get(0).topActivity.getClassName();

            for (String s:apps)
            {
                if (currentRunningActivityName.equals(s)) {
                    Toast.makeText(appContext, "kijelölve", Toast.LENGTH_SHORT).show();
                    appFinded = true;
                }
            }

            if(appFinded == true)
            {
                break;
            }

        }

    }

    protected Object doInBackground(Object[] params) {
        Toast.makeText(appContext, "működök faszán :)))))", Toast.LENGTH_SHORT).show();
        //run();

        return null;
    }

}
