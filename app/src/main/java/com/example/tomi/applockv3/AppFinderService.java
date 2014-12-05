package com.example.tomi.applockv3;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by Tomi on 2014.12.04..
 */
public class AppFinderService extends Service {

    //private ArrayList<String> packageNames;
    private ArrayList<String> stalkList;
    private Handler handler;
    //ArrayList<String> packageNames
    public AppFinderService() {
        // this.packageNames = packageNames;
        stalkList = new ArrayList<String>();
        handler = new Handler();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        handler.postDelayed(new Runnable() {
            public void run() {
                Toast.makeText(getBaseContext(), "AppLocker started!", Toast.LENGTH_SHORT).show();
            }
        }, 5000);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Timer timer  =  new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            public void run() {
                try
                {
                    Process mLogcatProc = null;
                    BufferedReader reader = null;
                    mLogcatProc = Runtime.getRuntime().exec(new String[]{"logcat", "-d"});

                    reader = new BufferedReader(new InputStreamReader(mLogcatProc.getInputStream()));

                    String line;
                    final StringBuilder log = new StringBuilder();
                    String separator = System.getProperty("line.separator");

                    while ((line = reader.readLine()) != null || line.contains("Starting activity:"));
                    {
                        log.append(line);
                        log.append(separator);
                    }
                    if(line != null)
                    {
                        Toast.makeText(getApplicationContext(),line, Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"nem talált", Toast.LENGTH_LONG).show();
                    }

                }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, 20000, 6000);  // every 6 seconds




        return START_STICKY;
    }

    private ActivityManager.RunningAppProcessInfo getForegroundApp() {
        ActivityManager.RunningAppProcessInfo result = null, info = null;

        final ActivityManager activityManager  =  (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);

        List <ActivityManager.RunningAppProcessInfo> l = activityManager.getRunningAppProcesses();
        Iterator <ActivityManager.RunningAppProcessInfo> i = l.iterator();
        while(i.hasNext()) {
            info = i.next();
            if(info.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                    && !isRunningService(info.processName)) {
                result = info;
                break;
            }
        }
        return result;
    }

    private boolean isRunningService(String processName) {
        if(processName == null)
            return false;

        ActivityManager.RunningServiceInfo service;

        final ActivityManager activityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);

        List <ActivityManager.RunningServiceInfo> l = activityManager.getRunningServices(9999);
        Iterator <ActivityManager.RunningServiceInfo> i = l.iterator();
        while(i.hasNext()){
            service = i.next();
            if(service.process.equals(processName))
                return true;
        }
        return false;
    }

    private boolean isRunningApp(String processName) {
        if(processName == null)
            return false;

        ActivityManager.RunningAppProcessInfo app;

        final ActivityManager activityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);

        List <ActivityManager.RunningAppProcessInfo> l = activityManager.getRunningAppProcesses();
        Iterator <ActivityManager.RunningAppProcessInfo> i = l.iterator();
        while(i.hasNext()){
            app = i.next();
            if(app.processName.equals(processName) && app.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_SERVICE)
                return true;
        }
        return false;
    }


    private boolean checkifThisIsActive(ActivityManager.RunningAppProcessInfo target){
        boolean result = false;
        ActivityManager.RunningTaskInfo info;

        if(target == null)
            return false;

        final ActivityManager activityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);

        List <ActivityManager.RunningTaskInfo> l = activityManager.getRunningTasks(9999);
        Iterator<ActivityManager.RunningTaskInfo> i = l.iterator();

        while(i.hasNext()){
            info=i.next();
            if(info.baseActivity.getPackageName().equals(target.processName)) {
                result = true;
                break;
            }
        }

        return result;
    }
    // what is in b that is not in a ?
    public static Collection subtractSets(Collection a, Collection b)
    {
        Collection result = new ArrayList(b);
        result.removeAll(a);
        return result;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
