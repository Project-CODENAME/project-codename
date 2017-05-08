package rcas.stevenshighschool.apphysics2.projectcodename.simplesensorproject;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PersistenceService extends Service {
    public PersistenceService() {
    }

    // This is the old onStart method that will be called on the pre-2.0
// platform.  On 2.0 or later we override onStartCommand() so this
// method will not be called.
    @Override
    public void onStart(Intent intent, int startId) {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable(){
            public void run() {
                while(true)
                {
                    try {
                        Thread.sleep(1000 * 5);
                        ActivityManager am = (ActivityManager) PersistenceService.this.getSystemService(ACTIVITY_SERVICE);
// The first in the list of RunningTasks is always the foreground task.
                        ActivityManager.RunningTaskInfo foregroundTaskInfo = am.getRunningTasks(1).get(0);
                        String foregroundTaskPackageName = foregroundTaskInfo.topActivity.getPackageName();
                        if(!foregroundTaskPackageName.equals("rcas.stevenshighschool.apphysics2.projectcodename.simplesensorproject")){
                            PackageManager manager = PersistenceService.this.getPackageManager();
                            try {
                                Intent i = manager.getLaunchIntentForPackage("rcas.stevenshighschool.apphysics2.projectcodename.simplesensorproject");
                                if (i == null) {
                                    throw new PackageManager.NameNotFoundException();
                                }
                                i.addCategory(Intent.CATEGORY_LAUNCHER);
                                PersistenceService.this.startActivity(i);
                            } catch (PackageManager.NameNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

            }
        }).start();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
