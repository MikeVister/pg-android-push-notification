package com.pirack.mobile;

import android.app.IntentService;
import android.content.Intent;
import org.apache.cordova.api.LOG;

/**
 * In case of an app boot, the message needs to be delivered with a delay
 * to allow a proper PG bootstrap
 *
 * User: tmaus
 *
 */
public class NPNDelayedDeliveryIntent extends IntentService {

    private final String TAG = getClass().getName();

    public NPNDelayedDeliveryIntent() {
        super("DelayedDeliveryIntent");
    }

    protected void onHandleIntent(Intent intent) {
        LOG.d(TAG,"delay deliver notification");
        try {
            Thread.sleep(1000);
            NPNPlugin plugin =  NPNPlugin.getInstance();
            plugin.deliverNotification(intent.getExtras());
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
