package rcas.stevenshighschool.apphysics2.projectcodename.simplesensorproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Starts our service on boot, which in turn starts our main activity.
 *
 * @author Alan Zhu
 * @version 1
 * @since April 2017
 */
public class BootCompletedReceiver extends BroadcastReceiver {

    /**
     * Recieves a the BootCompleted message and responds to it by starting {@link PersistenceService}
     *
     * @param context Context upon which to act upon (?)
     * @param intent idk
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, PersistenceService.class);
        context.startService(serviceIntent);
    }
}
