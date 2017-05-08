package rcas.stevenshighschool.apphysics2.projectcodename.simplesensorproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Tfel on 5/7/2017.
 */

public class BootCompletedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, PersistenceService.class);
        context.startService(serviceIntent);
    }
}
