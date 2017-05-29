package rcas.stevenshighschool.apphysics2.projectcodename.simplesensorproject;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;

/**
 * This service ensures that if the app is backgrounded, it will start back up again within 5
 * seconds, given that it's not the system using one of its extreme measures, so that measurements
 * are taken as long as possible.
 *
 * This means that if you install the app on your phone - it's really hard to get rid of until you
 * adb uninstall it.
 *
 * @author Alan Zhu
 * @version 1
 * @since April 2017
 */
public class PersistenceService extends Service {

    /**
     * Default, necessary constructor
     */
    public PersistenceService() {
    }

    /**
     * This is the old onStart method that will be called on the pre-2.0
     * platform.  On 2.0 or later we override onStartCommand() so this
     * method will not be called. This came with the Android tutorial and I'm not quite sure
     * why we still have it.
     *
     * @param intent intent that started the service
     * @param startId honestly I'm not sure, but it's not important for us really
     */
    @Override
    public void onStart(Intent intent, int startId) {
    }

    /**
     * Starts a thread that checks if the app is in the foreground and puts it there if not.
     *
     * @param intent intent that started the service
     * @param flags any flags, although we really don't use them
     * @param startId not sure, not important
     * @return START_STICKY, so that our service is kept (hopefully)
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            public void run() {
                // always
                while (true) {
                    try {
                        // wait five seconds and then
                        Thread.sleep(1000 * 5);

                        // open the manager and check for the foreground task
                        ActivityManager am = (ActivityManager) PersistenceService.this.getSystemService(ACTIVITY_SERVICE);
                        // The first in the list of RunningTasks is always the foreground task.
                        ActivityManager.RunningTaskInfo foregroundTaskInfo = am.getRunningTasks(1).get(0);
                        String foregroundTaskPackageName = foregroundTaskInfo.topActivity.getPackageName();

                        // if it's not CODENAME...
                        if (!foregroundTaskPackageName.equals("rcas.stevenshighschool.apphysics2.projectcodename.simplesensorproject")) {
                            PackageManager manager = PersistenceService.this.getPackageManager();
                            try {
                                // let's start it up
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
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();
        return START_STICKY;
    }

    /**
     * Not useful.
     *
     * @param intent intent that started the service/binder (?)
     * @return null
     */
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
