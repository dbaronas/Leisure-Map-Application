package com.example.leisuremap;

import static android.content.Context.ACTIVITY_SERVICE;

import android.app.ActivityManager;
import android.content.Context;
import android.os.AsyncTask;

import java.util.Iterator;
import java.util.List;

public class IsAppRunning {

    Context context;
//    Context context;
//
//    @Override
//    protected Boolean doInBackground(Context... params) {
//        final Context context = params[0].getApplicationContext();
//        return isAppOnForeground();
//    }
//
//    private boolean isAppOnForeground() {
//        ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
//        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
//        if (appProcesses == null) {
//            return false;
//        }
//        final String packageName = context.getPackageName();
//        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
//            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    // Use like this:
//    //boolean foregroud = new IsAppRunning().execute(context).get();
//
//
//    private boolean isAppRunning(Context context) {
//        ActivityManager m = (ActivityManager) context.getSystemService( ACTIVITY_SERVICE );
//        List<ActivityManager.RunningTaskInfo> runningTaskInfoList =  m.getRunningTasks(10);
//        Iterator<ActivityManager.RunningTaskInfo> itr = runningTaskInfoList.iterator();
//        int n=0;
//        while(itr.hasNext()){
//            n++;
//            itr.next();
//        }
//        if(n==1){ // App is killed
//            return false;
//        }
//        else if (isAppOnForeground() == true) {
//            return true; //app is in foreground
//        }
//        else {
//            return true; // App is in background or foreground
//        }
//
//    }


    public boolean isRunning() {
        ActivityManager m = (ActivityManager) context.getSystemService( ACTIVITY_SERVICE );
        List<ActivityManager.RunningTaskInfo> runningTaskInfoList =  m.getRunningTasks(10);
        Iterator<ActivityManager.RunningTaskInfo> itr = runningTaskInfoList.iterator();
        int n=0;
        while(itr.hasNext()){
            n++;
            itr.next();
        }
        if(n==1){ // App is killed
            return false;
        }

        return true; // App is in background or foreground
    }
}
